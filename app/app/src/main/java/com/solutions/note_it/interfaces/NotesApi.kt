package com.solutions.note_it.interfaces

import com.solutions.note_it.data.Note
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NotesApi {
    @GET("notes")
    suspend fun getNotes(): Response<List<Note>>

    @GET("notes/{id}")
    suspend fun getNoteById(@Path("id") id: String): Response<Note>

    @POST("notes")
    suspend fun createNote(@Body note: Note): Response<Note>

    @PUT("notes/{id}")
    suspend fun updateNote(@Path("id") id: String, @Body note: Note): Response<Note>

    @DELETE("notes/{id}")
    suspend fun deleteNote(@Path("id") id: String): Response<Unit>
}