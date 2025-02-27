package com.example.sisteminformasimenejemensatpam.ViewModel

import android.view.View
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sisteminformasimenejemensatpam.data.User
import com.example.sisteminformasimenejemensatpam.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel (private val repository: UserRepository): ViewModel(){
    private var _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    init {
        getAllUsers()
    }

    fun getAllUsers(){
        viewModelScope.launch {
            val userlist = repository.getUsers()
            _users.postValue(userlist)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch {
            repository.updateUser(user)
            getAllUsers()
        }
    }
}