package com.apollo.architecture.model.api

class SimpleException : Exception {
    val errorCode: Int
    val errorMessage: String

    constructor(errorCode: Int, errorMessage: String) {
        this.errorCode = errorCode
        this.errorMessage = errorMessage;
    }

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        errorCode = error.getErrorCode()
        errorMessage = error.getErrorMessage()
    }

    companion object {
        const val ERROR: Int = 444444
    }

}

enum class ERROR(private val errorCode: Int, private val errorMessage: String) {
    UNKNOWN(999999, "未知错误"),
    PARSE_ERROR(888888, "解析错误"),
    NETWORK_ERROR(777777, "网络错误"),
    SSL_ERROR(666666, "证书出错"),
    TIMEOUT_ERROR(555555, "连接超时");

    fun getErrorMessage(): String {
        return errorMessage
    }

    fun getErrorCode(): Int {
        return errorCode
    }
}