package com.example.userapp.data.source.local

import com.example.userapp.data.source.local.dao.UserDao
import com.example.userapp.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dao: UserDao
) : LocalDataSource {

    override suspend fun saveUsers(users: List<User>) {
        dao.saveUsers(users)
    }

    override suspend fun addUser(user: User) {
        dao.addUser(user)
    }

    override suspend fun removeUser(id: String) {
        dao.removeUser(id)
    }

    override fun getUsers(): Flow<List<User>> {
        return dao.getUsers()
            .distinctUntilChanged()
    }

}