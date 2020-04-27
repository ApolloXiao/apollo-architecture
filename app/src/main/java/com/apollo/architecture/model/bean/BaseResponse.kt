package com.apollo.architecture.model.bean

data class BaseResponse<out T>(val errorCode: Int, val errorMsg: String, val data: T)