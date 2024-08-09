package com.solutions.note_it.data

import androidx.compose.ui.graphics.Color

enum class TaskStatus(val desc: String, val color: Color) {
    Todo("To do", Color.Gray),
    InProgress("In progress", Color.Yellow),
    Completed("Completed", Color.Green),
    Archived("Archived", Color.LightGray)
}