package com.solutions.note_it.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solutions.note_it.data.Task
import com.solutions.note_it.services.TasksServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val services: TasksServices
) : ViewModel() {
    var task by mutableStateOf(Task())
        private set

    fun loadTaskById(taskId: String) {
        viewModelScope.launch {
            task = services.getTask(taskId)
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            if (task.id.isEmpty()) {
                services.createTask(task)
            } else {
                services.updateTask(task)
            }
        }
    }

    fun deleteTask(){
        viewModelScope.launch {
            services.deleteTask(task.id)
        }
    }

    fun updateTaskDetail(updatedTask: Task) {
        task = updatedTask
    }
}