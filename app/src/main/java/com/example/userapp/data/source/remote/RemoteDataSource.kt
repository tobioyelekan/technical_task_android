package com.example.userapp.data.source.remote

import com.example.userapp.data.helper.Resource
import com.example.userapp.model.User
import com.example.userapp.model.UserResponse

interface RemoteDataSource {
    suspend fun fetchUsers(): Resource<UserResponse>
    suspend fun addUser(user: User): Resource<Unit>
    suspend fun removeUser(id: String): Resource<Unit>
}