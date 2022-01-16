package com.example.userapp.ui.adduser

import androidx.core.util.PatternsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.userapp.model.User
import com.example.userapp.utils.Event

class AddUserViewModel : ViewModel() {

    private val _validateInput = MutableLiveData<String>()
    val validateInput: LiveData<String> = _validateInput

    private val _proceed = MutableLiveData<Event<User>>()
    val proceed: LiveData<Event<User>> = _proceed

    fun validate(name: String, email: String, gender: String) {
        if (name.isEmpty()) {
            _validateInput.value = "name cannot be empty"
            return
        }

        if (email.isEmpty()) {
            _validateInput.value = "email cannot be empty"
            return
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            _validateInput.value = "enter a valid email"
            return
        }

        if (gender.isEmpty()) {
            _validateInput.value = "gender cannot be empty"
            return
        }

        _proceed.value = Event(User("", name, email, gender, "active"))
    }
}