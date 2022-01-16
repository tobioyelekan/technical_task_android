package com.example.userapp.util

import com.example.userapp.model.User
import com.example.userapp.model.UserResponse

object TestObjectUtil {

    val users = listOf(
        User("1", "tobi", "email@email.com", "male", "active"),
        User("2", "tosin", "tosin@email.com", "female", "active"),
        User("3", "ayo", "ayo@email.com", "female", "inactive")
    )
    val userResponse = UserResponse(data = users)
}