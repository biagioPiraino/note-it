package com.solutions.note_it.data

import com.google.gson.annotations.SerializedName

data class Category(
    @field:SerializedName("id") val id: String = "",
    @field:SerializedName("name") val name: String = "Select a category",
    @field:SerializedName("colour") val colour: String = "#999999",
    @field:SerializedName("type") val type: Int = 0
)