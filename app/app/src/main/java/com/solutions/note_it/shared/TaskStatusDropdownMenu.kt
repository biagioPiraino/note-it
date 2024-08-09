package com.solutions.note_it.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.solutions.note_it.data.TaskStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskStatusDropdownMenu(
    status: TaskStatus,
    updateStatus: (TaskStatus) -> Unit) {
    var categoryMenuExpanded by remember {
        mutableStateOf(false)
    }

    val statuses = TaskStatus.entries.toTypedArray()

    ExposedDropdownMenuBox(expanded = categoryMenuExpanded,
        modifier = Modifier.fillMaxWidth(),
        onExpandedChange = {
            categoryMenuExpanded = !categoryMenuExpanded
        }) {
        OutlinedTextField(value = status.desc,
            shape = CircleShape,
            label = { Text(text = "Status") },
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded)
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
                        .background(status.color)
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = categoryMenuExpanded,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { categoryMenuExpanded = false }) {
            statuses.forEach { item ->
                DropdownMenuItem(text = { Text(item.desc) }, onClick = {
                    categoryMenuExpanded = false
                    updateStatus(item)
                })
            }
        }
    }
}