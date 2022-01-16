package com.example.userapp.data.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.userapp.data.helper.Resource
import com.example.userapp.data.source.local.LocalDataSource
import com.example.userapp.data.source.remote.RemoteDataSource
import com.example.userapp.util.MainCoroutineRule
import com.example.userapp.util.TestObjectUtil
import com.example.userapp.util.mock
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class RepositoryTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remoteDataSource = mock<RemoteDataSource>()
    private val localDataSource = mock<LocalDataSource>()

    private lateinit var repository: Repository

    @Before
    fun setup() {
        repository = RepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `fetchUser should return success when network is successful`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.fetchUsers()).thenReturn(Resource.Success(TestObjectUtil.userResponse))

            val response = repository.fetchUsers()

            verify(remoteDataSource).fetchUsers()
            verifyNoMoreInteractions(remoteDataSource)
            assertTrue(response is Resource.Success)
        }

    @Test
    fun `fetchUser should return error given network fails`() = mainCoroutineRule.runBlockingTest {
        `when`(remoteDataSource.fetchUsers()).thenReturn(Resource.Error("fail"))

        val response = repository.fetchUsers()

        verify(remoteDataSource).fetchUsers()
        verifyNoMoreInteractions(remoteDataSource)
        assertTrue(response is Resource.Error)
    }

    @Test
    fun `when network fails, verify that fetchUser doesn't save any data`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.fetchUsers()).thenReturn(Resource.Error("fail"))

            repository.fetchUsers()

            verify(remoteDataSource).fetchUsers()
            verifyNoMoreInteractions(remoteDataSource)
            verifyNoInteractions(localDataSource)
        }

    @Test
    fun `when network is successful verify that fetchUser saves result in local database`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.fetchUsers()).thenReturn(Resource.Success(TestObjectUtil.userResponse))

            repository.fetchUsers()

            verify(remoteDataSource).fetchUsers()
            verify(localDataSource).saveUsers(TestObjectUtil.userResponse.data)
            verifyNoMoreInteractions(remoteDataSource)
            verifyNoMoreInteractions(localDataSource)
        }
}