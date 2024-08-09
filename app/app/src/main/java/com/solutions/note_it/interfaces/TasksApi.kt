package com.solutions.note_it.interfaces

import com.solutions.note_it.data.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TasksApi {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>

    @GET("tasks/{id}")
    suspend fun getTaskById(@Path("id") id: String): Response<Task>

    @POST("tasks")
    suspend fun createTask(@Body task: Task): Response<Task>

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body task: Task): Response<Task>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<Unit>
}