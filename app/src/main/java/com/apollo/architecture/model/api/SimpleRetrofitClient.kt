package com.apollo.architecture.model.api

import okhttp3.OkHttpClient

object SimpleRetrofitClient : BaseRetrofitClient() {
    override fun handleBuilder(builder: OkHttpClient.Builder) {

    }
}