package com.example.userapp.ui.adduser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.userapp.model.User
import com.example.userapp.util.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class AddUserViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val viewModel = AddUserViewModel()

    @Test
    fun `verify livedata for invalid name`() {
        viewModel.validate("", "", "")

        val result = viewModel.validateInput.getOrAwaitValue()
        assertThat(result, `is`("name cannot be empty"))
    }

    @Test
    fun `verify livedata for invalid gender`() {
        viewModel.validate("tobi", "", "")

        val result = viewModel.validateInput.getOrAwaitValue()
        assertThat(result, `is`("email cannot be empty"))
    }

    @Test
    fun `verify livedata for empty email`() {
        viewModel.validate("tobi", "ads@a.com", "")

        val result = viewModel.validateInput.getOrAwaitValue()
        assertThat(result, `is`("gender cannot be empty"))
    }

    @Test
    fun `verify livedata for invalid email`() {
        viewModel.validate("tobi", "email", "male")

        val result = viewModel.validateInput.getOrAwaitValue()
        assertThat(result, `is`("enter a valid email"))
    }

    @Test
    fun `verify valid input sets proceed livedata`() {
        val givenUser = User("", "tobi", "email@e.com", "female", "active")
        viewModel.validate(givenUser.name, givenUser.email, givenUser.gender)

        val result = viewModel.proceed.getOrAwaitValue()
        assertThat(result.getContentIfNotHandled(), `is`(givenUser))
    }
}