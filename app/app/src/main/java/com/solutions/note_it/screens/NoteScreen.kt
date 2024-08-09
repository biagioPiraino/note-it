package com.solutions.note_it.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.solutions.note_it.data.CategoryType
import com.solutions.note_it.data.Note
import com.solutions.note_it.shared.CategoryDropdownMenu
import com.solutions.note_it.viewmodels.NoteViewModel

@Composable
fun UpdateNoteScreen(
    noteId: String,
    onCommit: () -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {
    LaunchedEffect(noteId) {
        viewModel.loadNoteById(noteId)
    }

    NoteDetailView(
        note = viewModel.note,
        onNoteChange = { updatedNote ->
            viewModel.updateNoteDetail(updatedNote)
        },
        onSubmit = {
            viewModel.saveNote()
            onCommit()
        },
        onDelete = {
            viewModel.deleteNote()
            onCommit()
        }
    )
}

@Composable
fun CreateNoteScreen(
    onCommit: () -> Unit,
    viewModel: NoteViewModel = hiltViewModel()
) {
    NoteDetailView(
        note = viewModel.note,
        onNoteChange = { updatedNote ->
            viewModel.updateNoteDetail(updatedNote)
        },
        onSubmit = {
            viewModel.saveNote()
            onCommit()
        }
    )
}

@Composable
fun NoteDetailView(
    note: Note,
    onNoteChange: (Note) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit = {}
) {
    var noteState = note
    Surface(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize()
    ) {
        val spacerPad = 8.dp
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                label = { Text(text = "Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape,
                singleLine = true,
                value = noteState.title,
                onValueChange = { newTitle ->
                    noteState = noteState.copy(title = newTitle)
                    onNoteChange(noteState)
                }
            )
            Spacer(modifier = Modifier.height(spacerPad))

            CategoryDropdownMenu(
                type = CategoryType.Note,
                selected = noteState.category,
                updateCategory = { selectedCategory ->
                    noteState = noteState.copy(category = selectedCategory)
                    onNoteChange(noteState)
                })
            Spacer(modifier = Modifier.height(spacerPad))

            OutlinedTextField(
                label = { Text(text = "Summary") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                minLines = 2,
                value = noteState.summary,
                onValueChange = { newSummary ->
                    noteState = noteState.copy(summary = newSummary)
                    onNoteChange(noteState)
                }
            )
            Spacer(modifier = Modifier.height(spacerPad))

            OutlinedTextField(
                label = { Text(text = "Content") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                minLines = 4,
                value = noteState.content,
                onValueChange = { newContent ->
                    noteState = noteState.copy(content = newContent)
                    onNoteChange(noteState)
                }
            )
            Spacer(modifier = Modifier.height(spacerPad))

            Button(
                onClick = { onSubmit() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }

            if (note.id.isNotEmpty()) {
                Spacer(modifier = Modifier.height(spacerPad))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = { onDelete() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete")
                }
            }
        }
    }
}