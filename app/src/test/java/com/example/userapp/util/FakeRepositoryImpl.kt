package com.example.userapp.util

import com.example.userapp.data.helper.Resource
import com.example.userapp.data.repo.Repository
import com.example.userapp.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepositoryImpl : Repository {

    private var isErrorEnabled = false
    private val users = mutableListOf<User>()

    fun setError(status: Boolean) {
        isErrorEnabled = status
    }

    override suspend fun fetchUsers(): Resource<Unit> {
        delay(1000)
        return when (isErrorEnabled) {
            false -> {
                users.addAll(TestObjectUtil.users)
                Resource.Success(Unit)
            }
            else -> {
                Resource.Error("error message")
            }
        }
    }

    override suspend fun addUser(user: User): Resource<Unit> {
        delay(1000)
        return when (isErrorEnabled) {
            false -> {
                users.add(user)
                Resource.Success(Unit)
            }
            else -> {
                Resource.Error("error message")
            }
        }
    }

    override suspend fun removeUser(id: String): Resource<Unit> {
        delay(1000)
        return when (isErrorEnabled) {
            false -> {
                users.find { it.id == id }.let {
                    users.remove(it)
                }
                Resource.Success(Unit)
            }
            else -> {
                Resource.Error("error message")
            }
        }
    }

    override fun observeUsers(): Flow<List<User>> {
        return flowOf(users)
    }
}