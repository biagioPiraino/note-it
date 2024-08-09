package com.solutions.note_it.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.solutions.note_it.data.Category
import com.solutions.note_it.data.CategoryType
import com.solutions.note_it.utils.Colors.Utils.hexToColor
import com.solutions.note_it.viewmodels.CategoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownMenu(
    type: CategoryType,
    selected: Category,
    viewModel: CategoriesViewModel = hiltViewModel(),
    updateCategory: (Category) -> Unit
) {
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }

    var updatingCategory: Category? by remember { mutableStateOf(null)}
    var showUpdateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(type) {
        viewModel.loadCategories(type)
    }

    val categories = viewModel.categories

    ExposedDropdownMenuBox(
        expanded = categoryMenuExpanded,
        modifier = Modifier.fillMaxWidth(),
        onExpandedChange = {
            if (categories.isEmpty()) showCreateDialog = true
            else categoryMenuExpanded = !categoryMenuExpanded
        }
    ) {
        OutlinedTextField(
            value = selected.name,
            shape = CircleShape,
            label = { Text(text = "Category") },
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showCreateDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "create category",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .absoluteOffset(x = 6.dp)
                        .clip(CircleShape)
                        .background(hexToColor(selected.colour))
                )
            },

            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = categoryMenuExpanded,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { categoryMenuExpanded = false }
        ) {
            categories.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        categoryMenuExpanded = false
                        updateCategory(item)
                    },
                    trailingIcon = {
                        Row {
                            IconButton(onClick = {
                                updatingCategory = item
                                showUpdateDialog = true }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit category",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            IconButton(onClick = {
                                viewModel.deleteCategory(item.id, type)
                                categoryMenuExpanded = false
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete category",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    if (showCreateDialog) {
        CategoryDialog(
            type = type,
            onConfirm = { category ->
                viewModel.saveCategory(category, type)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }

    if (showUpdateDialog) {
        CategoryDialog(
            category = updatingCategory,
            type = type,
            onConfirm = { category ->
                viewModel.saveCategory(category, type)
                showUpdateDialog = false
            },
            onDismiss = { showUpdateDialog = false }
        )
    }
}
