package com.solutions.note_it.services

import android.util.Log
import com.solutions.note_it.data.User
import com.solutions.note_it.interfaces.UsersApi
import javax.inject.Inject

class UserServices @Inject constructor(
    private val api: UsersApi,
    private val services: AuthorizationServices
) {
    private val loggingTag: String = "Api request: Users"

    suspend fun getUser(): User {
        val profile = services.getUserProfile()
        return if (profile == null) {
            User()
        } else{
            User(
                id = profile.getId() ?: "",
                name = profile.name ?: "",
                email = profile.email ?: "",
                nickname = profile.nickname ?: ""
            )
        }
    }

    suspend fun updateUser(user: User): User {
        Log.i(loggingTag, "Updating user ${user.id}...")
        val response = api.updateUser(user.id, user)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning user...")
            return getUser()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return user
    }
}