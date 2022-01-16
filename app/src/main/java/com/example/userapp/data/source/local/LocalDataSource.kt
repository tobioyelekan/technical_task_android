package com.example.userapp.data.source.local

import com.example.userapp.model.User
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun saveUsers(users: List<User>)
    suspend fun addUser(user: User)
    suspend fun removeUser(id: String)
    fun getUsers(): Flow<List<User>>
}