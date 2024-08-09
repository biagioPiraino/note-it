package com.solutions.note_it.services

import android.util.Log
import com.solutions.note_it.data.Category
import com.solutions.note_it.data.CategoryType
import com.solutions.note_it.interfaces.CategoriesApi
import javax.inject.Inject

class CategoriesServices @Inject constructor(private val api: CategoriesApi) {

    private val loggingTag: String = "Api request: Categories"

    suspend fun getCategories(): List<Category> {
        Log.i(loggingTag, "Retrieving categories...")
        val response = api.getCategories()

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning categories...")
            return response.body() ?: emptyList()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return emptyList()
    }

    suspend fun getCategoriesByType(type: CategoryType): List<Category> {
        Log.i(loggingTag, "Retrieving categories of type $type...")
        val response = api.getCategoriesByType(type.ordinal)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning categories...")
            return response.body() ?: emptyList()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return emptyList()
    }

    suspend fun createCategory(category: Category): List<Category>  {
        Log.i(loggingTag, "Creating a new category...")
        val response = api.createCategory(category)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning categories...")
            return response.body() ?: emptyList()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return emptyList()
    }

    suspend fun updateCategory(category: Category): List<Category> {
        Log.i(loggingTag, "Updating category ${category.id}...")
        val response = api.updateCategory(category.id, category)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning categories...")
            return response.body() ?: emptyList()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return emptyList()
    }

    suspend fun deleteCategory(id: String): Boolean {
        Log.i(loggingTag, "Deleting category ${id}...")
        val response = api.deleteCategory(id)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Category $id deleted.")
            return true
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return false
    }
}