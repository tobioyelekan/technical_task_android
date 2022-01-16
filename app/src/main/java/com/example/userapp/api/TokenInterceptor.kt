package com.example.userapp.api

import com.example.userapp.BuildConfig
import com.example.userapp.utils.Constants.HTTP_GET_METHOD
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer ${BuildConfig.ACCESS_TOKEN}")
        return chain.proceed(request.build())
    }
}