package com.solutions.note_it.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.solutions.note_it.data.Category
import com.solutions.note_it.data.CategoryType
import com.solutions.note_it.viewmodels.CategoryViewModel

@Composable
fun CategoryDialog(
    category: Category? = null,
    type: CategoryType,
    onDismiss: () -> Unit,
    onConfirm: (Category) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    viewModel.loadCategory(type, category)

    var categoryState by remember {
        mutableStateOf(viewModel.category)
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = viewModel.title,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    label = { Text(text = "Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape,
                    singleLine = true,
                    value = categoryState.name,
                    onValueChange = { name ->
                        categoryState = categoryState.copy(name = name)
                        viewModel.updateCategory(categoryState)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    label = { Text(text = "Colour") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = CircleShape,
                    singleLine = true,
                    value = categoryState.colour,
                    onValueChange = { colour ->
                        categoryState = categoryState.copy(colour = colour)
                        viewModel.updateCategory(categoryState)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            onConfirm(viewModel.category)
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(viewModel.confirm)
                    }
                }
            }
        }
    }
}