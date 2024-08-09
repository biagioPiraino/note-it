package com.solutions.note_it.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solutions.note_it.data.Category
import com.solutions.note_it.data.CategoryType
import com.solutions.note_it.services.CategoriesServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val services: CategoriesServices
) : ViewModel() {
    var categories by mutableStateOf(emptyList<Category>())
        private set

    fun loadCategories(type: CategoryType) {
        viewModelScope.launch {
            categories = services.getCategoriesByType(type)
        }
    }

    fun deleteCategory(id: String, type: CategoryType) {
        viewModelScope.launch {
            services.deleteCategory(id)
            reloadCategories(type)
        }
    }

    fun saveCategory(category: Category, type: CategoryType) {
        viewModelScope.launch {
            if (category.id.isEmpty()){
                services.createCategory(category)
            }
            else{
                services.updateCategory(category)
            }
            reloadCategories(type)
        }
    }

    private fun reloadCategories(type: CategoryType) {
        loadCategories(type)
    }
}
