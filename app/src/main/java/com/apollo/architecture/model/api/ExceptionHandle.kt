package com.apollo.architecture.model.api

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

object ExceptionHandle {
    fun handleException(e: Throwable): SimpleException {
        val ex: SimpleException
        if (e is HttpException) {
            ex = SimpleException(e.code(), "${e.code()} ${e.message()}")
        } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException || e is MalformedJsonException) {
            ex = SimpleException(ERROR.PARSE_ERROR, e)
        } else if (e is ConnectException) {
            ex = SimpleException(ERROR.NETWORK_ERROR, e)
        } else if (e is javax.net.ssl.SSLException) {
            ex = SimpleException(ERROR.SSL_ERROR, e)
        } else if (e is java.net.SocketTimeoutException) {
            ex = SimpleException(ERROR.TIMEOUT_ERROR, e)
        } else if (e is java.net.UnknownHostException) {
            ex = SimpleException(ERROR.TIMEOUT_ERROR, e)
        } else if (e is SimpleException) {
            ex = e
        } else {
            ex = if (!e.message.isNullOrEmpty()) SimpleException(SimpleException.ERROR, e.message!!)
            else SimpleException(ERROR.UNKNOWN, e)
        }
        return ex
    }
}