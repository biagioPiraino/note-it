package com.solutions.note_it.services

import android.util.Log
import com.solutions.note_it.data.Note
import com.solutions.note_it.interfaces.NotesApi
import javax.inject.Inject

class NotesServices @Inject constructor(private val api: NotesApi) {

    private val loggingTag: String = "Api request: Notes"

    suspend fun getNotes(): List<Note> {
        Log.i(loggingTag, "Retrieving notes...")
        val response = api.getNotes()

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning notes...")
            return response.body() ?: emptyList()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return emptyList()
    }

    suspend fun getNote(id: String): Note {
        Log.i(loggingTag, "Retrieving note $id...")
        val response = api.getNoteById(id)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning note...")
            return response.body() ?: Note()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return Note()
    }

    suspend fun createNote(note: Note): Note {
        Log.i(loggingTag, "Creating a new note...")
        val response = api.createNote(note)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning note...")
            return response.body() ?: Note()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return Note()
    }

    suspend fun updateNote(note: Note): Note {
        Log.i(loggingTag, "Updating note ${note.id}...")
        val response = api.updateNote(note.id, note)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Returning note...")
            return response.body() ?: Note()
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return note
    }

    suspend fun deleteNote(id: String): Boolean {
        Log.i(loggingTag, "Deleting note ${id}...")
        val response = api.deleteNote(id)

        if (response.isSuccessful){
            Log.i(loggingTag, "Request completed successfully. Note $id deleted.")
            return true
        }

        Log.e(loggingTag, "An error occurred. Status: ${response.code()}, Message: ${response.errorBody()}")
        return false
    }
}