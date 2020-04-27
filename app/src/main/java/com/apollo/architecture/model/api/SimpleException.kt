package com.apollo.architecture.model.api

import java.io.IOException

class SimpleException(val errorCode: Int, val errorMessage: String?) : IOException(errorMessage) {
    companion object {
        const val UNKNOWN_CODE = 999999
    }
}