package com.solutions.note_it.data

import com.google.gson.annotations.SerializedName

data class User(
    val id: String = "",
    @field:SerializedName("name") val name: String = "",
    @field:SerializedName("email") val email: String = "",
    @field:SerializedName("nickname") val nickname: String = "",
    @field:SerializedName("connection") val connection: String = "Username-Password-Authentication"
) {
}