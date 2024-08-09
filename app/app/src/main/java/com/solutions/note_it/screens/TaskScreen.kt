package com.solutions.note_it.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.solutions.note_it.R.string.btn_submit
import com.solutions.note_it.data.CategoryType
import com.solutions.note_it.data.Task
import com.solutions.note_it.data.TaskStatus
import com.solutions.note_it.shared.CategoryDropdownMenu
import com.solutions.note_it.shared.DatePickerWithDialog
import com.solutions.note_it.shared.TaskStatusDropdownMenu
import com.solutions.note_it.utils.Calendar
import com.solutions.note_it.viewmodels.TaskViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateTaskScreen(
    taskId: String,
    onCommit: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    LaunchedEffect(taskId) {
        viewModel.loadTaskById(taskId)
    }

    TaskDetailView(
        task = viewModel.task,
        onTaskChange = { updatedTask ->
            viewModel.updateTaskDetail(updatedTask)
        },
        onSubmit = {
            viewModel.saveTask()
            onCommit()
        },
        onDelete = {
            viewModel.deleteTask()
            onCommit()
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateTaskScreen(
    onCommit: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    TaskDetailView(
        task = viewModel.task,
        onTaskChange = { updatedTask ->
            viewModel.updateTaskDetail(updatedTask)
        },
        onSubmit = {
            viewModel.saveTask()
            onCommit()
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskDetailView(
    task: Task,
    onTaskChange: (Task) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit = {}
) {
    var taskState = task
    Surface(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize()
    ) {
        val spacerPad = 8.dp
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(label = { Text(text = "Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                singleLine = true,
                value = taskState.title,
                onValueChange = { title ->
                    taskState = taskState.copy(title = title)
                    onTaskChange(taskState)
                })
            Spacer(modifier = Modifier.height(spacerPad))

            TaskStatusDropdownMenu(
                enumValues<TaskStatus>().first { x -> x.ordinal == taskState.status },
                updateStatus = { selectedStatus ->
                    taskState = taskState.copy(status = selectedStatus.ordinal)
                    onTaskChange(taskState)
                })
            Spacer(modifier = Modifier.height(spacerPad))

            CategoryDropdownMenu(type = CategoryType.Task,
                selected = taskState.category,
                updateCategory = { selectedCategory ->
                    taskState = taskState.copy(category = selectedCategory)
                    onTaskChange(taskState)
                })
            Spacer(modifier = Modifier.height(spacerPad))

            DatePickerWithDialog(taskState.scheduledAt, updateSchedule = { selectedDate ->
                taskState = taskState.copy(scheduledAt = Calendar.formatDate(selectedDate))
                onTaskChange(taskState)
            })
            Spacer(modifier = Modifier.height(spacerPad))

            OutlinedTextField(label = { Text(text = "Content") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                minLines = 4,
                value = taskState.content,
                onValueChange = { content ->
                    taskState = taskState.copy(content = content)
                    onTaskChange(taskState)
                })
            Spacer(modifier = Modifier.height(spacerPad))

            Button(
                onClick = { onSubmit() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(btn_submit))
            }

            if (task.id.isNotEmpty()) {
                Spacer(modifier = Modifier.height(spacerPad))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = { onDelete() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete")
                }
            }
        }
    }
}