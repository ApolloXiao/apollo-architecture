package com.apollo.architecture.model.api;

/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.util.Log;

import com.apollo.architecture.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;

import static okhttp3.internal.platform.Platform.INFO;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class HttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY
    }

    public interface Logger {
        void log(String message);

        /**
         * A {@link HttpLoggingInterceptor.Logger} defaults output appropriate for the current platform.
         */
        HttpLoggingInterceptor.Logger DEFAULT = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Platform.get().log(INFO, message, null);
            }
        };
    }

    public HttpLoggingInterceptor() {
        this(HttpLoggingInterceptor.Logger.DEFAULT);
    }

    public HttpLoggingInterceptor(HttpLoggingInterceptor.Logger logger) {
        this.logger = logger;
    }

    private final HttpLoggingInterceptor.Logger logger;

    private volatile HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public HttpLoggingInterceptor setLevel(HttpLoggingInterceptor.Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public HttpLoggingInterceptor.Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpLoggingInterceptor.Level level = this.level;
        String logstr = "";

        Request request = chain.request();

        if (level == HttpLoggingInterceptor.Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
        boolean logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        logstr += "http start:";
        logstr += "\n\n\n####################################################################";
        logstr += "\nREQUEST_START";
        logstr += "\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  ";

        logstr += "\n" + request.method() + "\n" + request.url() + "\n";
        if (!logHeaders && hasRequestBody) {
            logstr += " (" + requestBody.contentLength() + "-byte body)";
        }

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logstr += ("Content-Type: " + requestBody.contentType()) + "\n";
                }
                if (requestBody.contentLength() != -1) {
                    logstr += ("Content-Length: " + requestBody.contentLength()) + "\n";
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logstr += (name + ": " + headers.value(i)) + "\n";
                }
            }

            if (!logBody || !hasRequestBody) {
                logstr += ("--> END " + request.method());

            } else if (bodyEncoded(request.headers())) {
                logstr += ("--> END " + request.method() + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                if (isPlaintext(buffer)) {
                    logstr += buffer.readString(charset);
                    logstr += ("--> END " + request.method()
                            + " (" + requestBody.contentLength() + "-byte body)");
                } else {
                    logstr += ("--> END " + request.method() + " (binary "
                            + requestBody.contentLength() + "-byte body omitted)");
                }
            }
            logstr += "\nREQUEST_END";
            logstr += "\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  ";
        }

        long startNs = System.nanoTime();
        Response response;
        logstr += "\n\nRESPONSE_START";
        logstr += "\n~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ";

        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            logstr += ("\n<-- HTTP FAILED: " + e);
            logstr += "\nRESPONSE_END";
            logstr += "\n~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ";
            logstr += "\n####################################################################" + "\n";
            if(BuildConfig.DEBUG) {
                show(logstr);
            }
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        Protocol protocol = response.protocol() != null ? response.protocol() : Protocol.HTTP_1_1;

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logstr += ("\nresponse code" + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", "
                + bodySize + " body" : "") + ')') + "\n";

        if (logHeaders) {
            Headers headers = response.headers();
            logstr += "\nprotocol:"+protocol.name()+"\n";
            for (int i = 0, count = headers.size(); i < count; i++) {
                logstr += headers.name(i) + ": " + headers.value(i) + "\n";
            }

            if (!logBody || !HttpHeaders.hasBody(response)) {
                logstr += "\nRESPONSE_END";
                logstr += "\n~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ";
                logstr += "\n####################################################################" + "\n";
                if(BuildConfig.DEBUG) {
                    show(logstr);
                }
            } else if (bodyEncoded(response.headers())) {
                logstr += "RESPONSE_END (encoded body omitted)\n";
                logstr += "\n~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ";
                logstr += "\n####################################################################" + "\n";
                if(BuildConfig.DEBUG) {
                    show(logstr);
                }
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        logstr += "\n" + "Couldn't decode the response body; charset is likely malformed."
                                + "\n";
                        logstr += "RESPONSE_END";
                        logstr += "\n~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ";
                        logstr += "\n####################################################################" + "\n";
                        if(BuildConfig.DEBUG) {
                            show(logstr);
                        }
                        return response;
                    }
                }

                if (!isPlaintext(buffer)) {
                    logstr += ("\n" + "<-- END HTTP (binary " + buffer.size() + "-byte body omitted)") + "\n";
                    logstr += "RESPONSE_END";
                    logstr += "\n~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ";
                    logstr += "\n####################################################################" + "\n";
                    if(BuildConfig.DEBUG) {
                        show(logstr);
                    }
                    return response;
                }

                if (contentLength != 0) {
//                    logstr += "\n" + buffer.clone().readString(charset) + "\n";
                    String response_str = buffer.clone().readString(charset);

                    String message;
                    try {
                        if (response_str.startsWith("{")) {
                            JSONObject jsonObject = new JSONObject(response_str);
                            message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                        } else if (response_str.startsWith("[")) {
                            JSONArray jsonArray = new JSONArray(response_str);
                            message = jsonArray.toString(4);
                        } else {
                            message = response_str;
                        }
                    } catch (JSONException e) {
                        message = response_str;
                    }

                    logstr += LINE_SEPARATOR + message;
                }

                logstr += ("<-- END HTTP (" + buffer.size() + "-byte body)") + "\n";
                logstr += "RESPONSE_END";
                logstr += "\n~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ~ ~ ~ ~ ~  ";
                logstr += "\n####################################################################" + "\n";
                if(BuildConfig.DEBUG) {
                    show(logstr);
                }
            }
        }

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    public  final String LINE_SEPARATOR = System.getProperty("line.separator");

    public void show(String str) {
        str = str.trim();
        int index = 0;
        int maxLength = 4000;
        String sub;
        while (index < str.length()) {
            // java的字符不允许指定超过总的长度end
            if (str.length() <= index + maxLength) {
                sub = str.substring(index);
            } else {
                sub = str.substring(index, index+maxLength);
            }

            index += maxLength;
            if(BuildConfig.DEBUG) {
                Log.d("okhttp",sub.trim());
            }
        }
    }
}
