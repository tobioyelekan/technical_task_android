package com.example.userapp

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.userapp.util.TestObjectUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest : DbSetup() {

    @Test
    fun insertAndRetrieve() = runBlocking {
        userDao.saveUsers(TestObjectUtil.users)
        val response = userDao.getUsers().first()
        assertThat(response, `is`(TestObjectUtil.users))
    }

    @Test
    fun insertSingleUserAndRetrieve() = runBlocking {
        userDao.addUser(TestObjectUtil.users[0])
        val response = userDao.getUsers().first()
        assertThat(response, `is`(TestObjectUtil.users.take(1)))
    }

    @Test
    fun verifyDeleteUser() = runBlocking {
        userDao.saveUsers(TestObjectUtil.users)
        val userToBeRemoved = TestObjectUtil.users[0]
        userDao.removeUser(userToBeRemoved.id)

        val response = userDao.getUsers().first()
        val expectedUsers = TestObjectUtil.users.filterNot { it == userToBeRemoved }

        assertThat(response, `is`(expectedUsers))
        assertThat(response.size, `is`(expectedUsers.size))
    }
}