package com.solutions.note_it.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solutions.note_it.data.Note
import com.solutions.note_it.services.NotesServices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val services: NotesServices
) : ViewModel() {

    var note by mutableStateOf(Note())
        private set

    fun loadNoteById(noteId: String) {
        viewModelScope.launch {
            note = services.getNote(noteId)
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            if (note.id.isEmpty()) {
                services.createNote(note)
            } else {
                services.updateNote(note)
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            services.deleteNote(note.id)
        }
    }

    fun updateNoteDetail(updatedNote: Note) {
        note = updatedNote
    }
}