package com.example.userapp.data.repo

import com.example.userapp.data.helper.Resource
import com.example.userapp.data.helper.Resource.*
import com.example.userapp.data.source.local.LocalDataSource
import com.example.userapp.data.source.remote.RemoteDataSource
import com.example.userapp.model.User
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    override suspend fun fetchUsers(): Resource<Unit> {
        return when (val response = remoteDataSource.fetchUsers()) {
            is Success -> {
                response.data?.let { userResponse ->
                    localDataSource.saveUsers(userResponse.data)
                }
                Success(Unit)
            }
            is Error -> Error(response.message)
            is Loading -> Loading
        }
    }

    override suspend fun addUser(user: User): Resource<Unit> {
        return when (val response = remoteDataSource.addUser(user)) {
            is Success -> {
                localDataSource.addUser(user)
                Success(Unit)
            }
            is Error -> Error(response.message)
            is Loading -> Loading
        }
    }

    override suspend fun removeUser(id: String): Resource<Unit> {
        return when (val response = remoteDataSource.removeUser(id)) {
            is Success -> {
                localDataSource.removeUser(id)
                Success(Unit)
            }
            is Error -> Error(response.message)
            is Loading -> Loading
        }
    }

    override fun observeUsers() = localDataSource.getUsers()
}