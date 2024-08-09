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
class NotesViewModel @Inject internal constructor(
    private val services: NotesServices
) : ViewModel() {
    var notes by mutableStateOf(emptyList<Note>())
        private set

    fun loadNotes() {
        viewModelScope.launch {
            notes = services.getNotes()
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            services.deleteNote(id)
            loadNotes()
        }
    }
}