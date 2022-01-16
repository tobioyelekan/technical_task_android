package com.example.userapp.data.source.remote

import com.example.userapp.api.UserService
import com.example.userapp.data.helper.Resource
import com.example.userapp.model.User
import com.example.userapp.model.UserResponse
import com.example.userapp.utils.parseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val service: UserService
) : RemoteDataSource {
    override suspend fun fetchUsers(): Resource<UserResponse> {
        return makeCall { service.getUsers() }
    }

    override suspend fun addUser(user: User): Resource<Unit> {
        return makeCall { service.createUser(user) }
    }

    override suspend fun removeUser(id: String): Resource<Unit> {
        return makeCall { service.deleteUser(id) }
    }

    companion object {
        suspend fun <T : Any> makeCall(call: suspend () -> Response<T>): Resource<T> {
            return try {
                val response = withContext(Dispatchers.IO) { call.invoke() }
                when {
                    response.isSuccessful -> {
                        Resource.Success(response.body())
                    }
                    else -> {
                        val msg = response.errorBody()?.string()
                        Resource.Error(message = msg.parseError())
                    }
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: e.localizedMessage)
            }
        }
    }
}