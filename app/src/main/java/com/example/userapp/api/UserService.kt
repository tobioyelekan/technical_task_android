package com.example.userapp.api

import com.example.userapp.model.User
import com.example.userapp.model.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("users")
    suspend fun getUsers(@Query("page") page: Int = 70): Response<UserResponse>

    @POST("users")
    suspend fun createUser(@Body user: User): Response<Unit>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Unit>
}