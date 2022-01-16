package com.example.userapp.data.repo

import com.example.userapp.data.helper.Resource
import com.example.userapp.model.User
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun fetchUsers(): Resource<Unit>
    suspend fun addUser(user: User): Resource<Unit>
    suspend fun removeUser(id: String): Resource<Unit>
    fun observeUsers(): Flow<List<User>>
}