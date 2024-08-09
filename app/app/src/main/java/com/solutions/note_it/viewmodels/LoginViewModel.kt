package com.solutions.note_it.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.solutions.note_it.services.AuthorizationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val services: AuthorizationServices
) : ViewModel() {

    var isLoginSuccess by mutableStateOf(false)
        private set

    suspend fun login(context: Context) {
        isLoginSuccess = services.login(context)
    }
}