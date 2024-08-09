package com.solutions.note_it.interfaces

import com.solutions.note_it.data.Category
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CategoriesApi {
    @GET("categories")
    suspend fun getCategories(): Response<List<Category>>

    @GET("categories/type/{type}")
    suspend fun getCategoriesByType(@Path("type") type: Int): Response<List<Category>>

    @POST("categories")
    suspend fun createCategory(@Body category: Category): Response<List<Category>>

    @PUT("categories/{id}")
    suspend fun updateCategory(@Path("id") id: String, @Body category: Category): Response<List<Category>>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(@Path("id") id: String): Response<List<Category>>
}