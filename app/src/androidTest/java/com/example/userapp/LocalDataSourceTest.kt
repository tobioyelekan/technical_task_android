package com.example.userapp

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.userapp.data.source.local.LocalDataSourceImpl
import com.example.userapp.model.User
import com.example.userapp.util.TestObjectUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class LocalDataSourceTest : DbSetup() {

    private lateinit var localDataSource: LocalDataSourceImpl

    @Before
    fun setup() = runBlocking {
        localDataSource = LocalDataSourceImpl(userDao)
        localDataSource.saveUsers(TestObjectUtil.users)
    }

    @Test
    fun retrieveUsers() = runBlocking {
        val response = localDataSource.getUsers().first()
        assertThat(response, `is`(TestObjectUtil.users))
    }

    @Test
    fun addUsers() = runBlocking {
        val given = User("4", "person", "email@email.com", "female", "active")
        localDataSource.addUser(given)

        val response = localDataSource.getUsers().first()

        val expected = TestObjectUtil.users + given
        assertThat(response, `is`(expected))
        assertThat(response.size, `is`(expected.size))
    }

    @Test
    fun removeUser() = runBlocking {
        val userToRemove = TestObjectUtil.users[0]
        localDataSource.removeUser(userToRemove.id)

        val response = localDataSource.getUsers().first()

        val expected = TestObjectUtil.users.filterNot { userToRemove == it }
        assertThat(response, `is`(expected))
        assertThat(response.size, `is`(expected.size))
    }
}