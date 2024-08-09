package com.solutions.note_it.interfaces

import com.solutions.note_it.data.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsersApi {
    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<Unit>
}