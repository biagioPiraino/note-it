package com.solutions.note_it.data

import com.google.gson.annotations.SerializedName

data class Task(
    @field:SerializedName("id") val id: String = "",
    @field:SerializedName("title") val title: String = "",
    @field:SerializedName("content") val content: String = "",
    @field:SerializedName("status") val status: Int = TaskStatus.Todo.ordinal,
    @field:SerializedName("scheduledAt") val scheduledAt: String = "",
    @field:SerializedName("category") val category: Category = Category(type = CategoryType.Task.ordinal)
)