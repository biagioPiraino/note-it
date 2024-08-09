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
class TasksViewModel @Inject internal constructor(
    private val services: TasksServices
) : ViewModel() {
    var tasks by mutableStateOf(emptyList<Task>())
        private set

    fun loadTasks(){
        viewModelScope.launch {
            tasks = services.getTasks()
        }
    }

    fun deleteTask(id: String){
        viewModelScope.launch {
            services.deleteTask(id)
            loadTasks()
        }
    }
}