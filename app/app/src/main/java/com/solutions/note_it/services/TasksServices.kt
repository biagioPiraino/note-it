package com.solutions.note_it.services

import android.util.Log
import com.solutions.note_it.data.Task
import com.solutions.note_it.interfaces.TasksApi
import javax.inject.Inject

class TasksServices @Inject constructor(private val api: TasksApi) {

    private val loggingTag: String = "Api request: Tasks"

    suspend fun getTasks(): List<Task> {
        Log.i(loggingTag, "Retrieving tasks...")
        val response = api.getTasks()

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning tasks...")
            return response.body() ?: emptyList()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return emptyList()
    }

    suspend fun getTask(id: String): Task {
        Log.i(loggingTag, "Retrieving task $id...")
        val response = api.getTaskById(id)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning task...")
            return response.body() ?: Task()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return Task()
    }

    suspend fun createTask(task: Task): Task {
        Log.i(loggingTag, "Creating a new task...")
        val response = api.createTask(task)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning task...")
            return response.body() ?: Task()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return Task()
    }

    suspend fun updateTask(task: Task): Task {
        Log.i(loggingTag, "Updating task ${task.id}...")
        val response = api.updateTask(task.id, task)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning task...")
            return response.body() ?: Task()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return task
    }

    suspend fun deleteTask(id: String): Boolean {
        Log.i(loggingTag, "Deleting task ${id}...")
        val response = api.deleteTask(id)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Task $id deleted.")
            return true
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return false
    }
}