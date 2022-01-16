package com.example.userapp.ui.users

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.userapp.data.helper.Resource
import com.example.userapp.model.User
import com.example.userapp.util.MainCoroutineRule
import com.example.userapp.util.TestObjectUtil
import com.example.userapp.util.getOrAwaitValue
import com.example.userapp.util.FakeRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UsersViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineDispatcher = MainCoroutineRule()

    private lateinit var viewModel: UsersViewModel
    private val repository = FakeRepositoryImpl()

    @Before
    fun setup() {
        viewModel = UsersViewModel(repository)
    }

    @Test
    fun `when fetchUser returns success from init{} verify loading and success status and observe data`() =
        mainCoroutineDispatcher.runBlockingTest {
            repository.setError(false)

            mainCoroutineDispatcher.pauseDispatcher()

            assertThat(viewModel.loadingFetchUser.getOrAwaitValue(), `is`(Resource.Loading))

            mainCoroutineDispatcher.resumeDispatcher()

            assertThat(viewModel.loadingFetchUser.getOrAwaitValue(), `is`(Resource.Success(Unit)))
            assertThat(viewModel.users.getOrAwaitValue(), `is`(TestObjectUtil.users))
        }

    @Test
    fun `when network returns error from fetchUser verify loading and error status`() =
        mainCoroutineDispatcher.runBlockingTest {

            repository.setError(true)

            mainCoroutineDispatcher.pauseDispatcher()

            assertThat(viewModel.loadingFetchUser.getOrAwaitValue(), `is`(Resource.Loading))

            mainCoroutineDispatcher.resumeDispatcher()

            assertThat(
                viewModel.loadingFetchUser.getOrAwaitValue(),
                `is`(Resource.Error("error message"))
            )
        }

    @Test
    fun `when network returns success from addUser verify loading, success status and data is persisted`() =
        mainCoroutineDispatcher.runBlockingTest {

            val given = User("4", "Adraino", "Addy@gmail.com", "male", "inactive")
            repository.setError(false)

            mainCoroutineDispatcher.pauseDispatcher()
            viewModel.addUser(given)

            assertThat(viewModel.loading.getOrAwaitValue(), `is`(Resource.Loading))

            mainCoroutineDispatcher.resumeDispatcher()

            val expected = TestObjectUtil.users + given

            assertThat(viewModel.loading.getOrAwaitValue(), `is`(Resource.Success(Unit)))
            assertThat(viewModel.users.getOrAwaitValue(), `is`(expected))
        }

    @Test
    fun `when network returns success from removeUer verify loading, success status and data is persisted`() =
        mainCoroutineDispatcher.runBlockingTest {

            val given = TestObjectUtil.users[0]
            repository.setError(false)

            mainCoroutineDispatcher.pauseDispatcher()
            viewModel.removeUser(given.id)

            assertThat(viewModel.loading.getOrAwaitValue(), `is`(Resource.Loading))

            mainCoroutineDispatcher.resumeDispatcher()

            val expected = TestObjectUtil.users - given

            assertThat(viewModel.loading.getOrAwaitValue(), `is`(Resource.Success(Unit)))
            assertThat(viewModel.users.getOrAwaitValue(), `is`(expected))
        }
}