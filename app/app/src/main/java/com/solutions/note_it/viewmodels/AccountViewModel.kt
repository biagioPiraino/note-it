package com.solutions.note_it.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solutions.note_it.data.User
import com.solutions.note_it.services.UserServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val services: UserServices
) : ViewModel() {
    var user by mutableStateOf(User())
        private set

    fun loadUser() {
        viewModelScope.launch {
            user = services.getUser()
        }
    }

    fun updateUserDetail(updatedUser: User) {
        user = updatedUser
    }

    fun updateUser() {
        viewModelScope.launch {
            val updatedUser = services.updateUser(user)
            updateUserDetail(updatedUser)
        }
    }
}