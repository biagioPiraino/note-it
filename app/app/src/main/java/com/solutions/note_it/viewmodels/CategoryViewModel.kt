package com.solutions.note_it.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.solutions.note_it.data.Category
import com.solutions.note_it.data.CategoryType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor() : ViewModel() {
    var category by mutableStateOf(Category(type = 0))
        private set
    var title by mutableStateOf("")
        private set
    var confirm by mutableStateOf("")
        private set

    fun loadCategory(type: CategoryType, existingCategory: Category?) {
        if (existingCategory == null) {
            loadEmptyCategory(type)
            title = "Create a category"
            confirm = "Create"
        } else {
            loadExistingCategory(existingCategory)
            title = "Update a category"
            confirm = "Update"
        }
    }

    fun updateCategory(updatedCategory: Category) {
        category = updatedCategory
    }

    private fun loadEmptyCategory(type: CategoryType) {
        category = Category(type = type.ordinal)
    }

    private fun loadExistingCategory(existingCategory: Category) {
        category = existingCategory
    }
}
