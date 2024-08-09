package com.solutions.note_it.screens

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val title: String,
    val canCreate: Boolean = false,
    val showCustomLayout: Boolean = true,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    data object Login : Screen(route = "login", title = "Login", showCustomLayout = false)
    data object Notes : Screen(route = "notes", title = "Notes", canCreate = true)
    data object Tasks : Screen(route = "tasks", title = "Tasks", canCreate = true)
    data object Account : Screen(route = "account", title = "Account")
    data object CreateNote : Screen(route = "note", title = "Create note")
    data object CreateTask : Screen(route = "task", title = "Create task")

    data object NoteDetails : Screen(
        route = "note/{noteId}",
        title = "Update note",
        navArguments = listOf(navArgument("noteId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(noteId: String) = "note/${noteId}"
    }

    data object TaskDetails : Screen(
        route = "task/{taskId}",
        title = "Update task",
        navArguments = listOf(navArgument("taskId") {
            type = NavType.StringType
        })
    ) {
        fun createRoute(taskId: String) = "task/${taskId}"
    }

    companion object Utils {
        fun getScreen(route: String?): Screen {
            return when (route) {
                "notes" -> Notes
                "tasks" -> Tasks
                "account" -> Account
                "note" -> CreateNote
                "task" -> CreateTask
                "note/{noteId}" -> NoteDetails
                "task/{taskId}" -> TaskDetails
                else -> {
                    Login
                }
            }
        }
    }
}