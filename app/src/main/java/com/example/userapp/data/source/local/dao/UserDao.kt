package com.example.userapp.data.source.local.dao

import androidx.room.*
import com.example.userapp.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(users: User)

    @Query("SELECT * FROM user")
    fun getUsers(): Flow<List<User>>

    @Query("DELETE FROM user WHERE id=:id")
    suspend fun removeUser(id: String)
}