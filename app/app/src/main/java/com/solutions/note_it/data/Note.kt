package com.solutions.note_it.data

import com.google.gson.annotations.SerializedName

data class Note(
    @field:SerializedName("id") val id: String = "",
    @field:SerializedName("title") val title: String = "",
    @field:SerializedName("summary") val summary: String = "",
    @field:SerializedName("content") val content: String = "",
    @field:SerializedName("category") val category: Category = Category(type = CategoryType.Note.ordinal)
) {
}