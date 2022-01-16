package com.example.userapp.ui.users

import androidx.lifecycle.*
import com.example.userapp.data.helper.Resource
import com.example.userapp.data.repo.Repository
import com.example.userapp.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _loadingFetchUser = MutableLiveData<Resource<Unit>>()
    val loadingFetchUser: LiveData<Resource<Unit>> = _loadingFetchUser

    private val _loading = MutableLiveData<Resource<Unit>>()
    val loading: LiveData<Resource<Unit>> = _loading

    init {
        fetchUsers()
    }

    val users = repository.observeUsers().asLiveData()

    private fun fetchUsers() {
        _loadingFetchUser.value = Resource.Loading

        viewModelScope.launch {
            _loadingFetchUser.value = repository.fetchUsers()
        }
    }

    fun addUser(user: User) {
        _loading.value = Resource.Loading

        viewModelScope.launch {
            _loading.value = repository.addUser(user)
        }
    }

    fun removeUser(id: String) {
        _loading.value = Resource.Loading

        viewModelScope.launch {
            _loading.value = repository.removeUser(id)
        }
    }
}