package com.solutions.note_it.shared

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerWithDialog(
    date: String,
    updateSchedule: (Long?) -> Unit
) {
    val dateState = rememberDatePickerState()

    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = date,
            shape = CircleShape,
            label = { Text(text = "Schedule") },
            onValueChange = {},
            readOnly = true,
            leadingIcon = {
                IconButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .size(20.dp)
                        .absoluteOffset(x = 6.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Open date picker",
                        tint = Color.Gray
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (showDialog) {
            DatePickerDialog(onDismissRequest = { showDialog = false }, confirmButton = {
                Button(onClick = {
                    showDialog = false
                    updateSchedule(dateState.selectedDateMillis)
                }) {
                    Text(text = "OK")
                }
            }, dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
            }) {
                DatePicker(
                    state = dateState, showModeToggle = true
                )
            }
        }
    }
}