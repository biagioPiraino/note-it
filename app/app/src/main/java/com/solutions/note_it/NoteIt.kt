package com.solutions.note_it

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.solutions.note_it.screens.AccountScreen
import com.solutions.note_it.screens.CreateNoteScreen
import com.solutions.note_it.screens.CreateTaskScreen
import com.solutions.note_it.screens.LoginScreen
import com.solutions.note_it.screens.NotesScreen
import com.solutions.note_it.screens.Screen
import com.solutions.note_it.screens.TasksScreen
import com.solutions.note_it.screens.UpdateNoteScreen
import com.solutions.note_it.screens.UpdateTaskScreen
import com.solutions.note_it.shared.CustomBottomBar
import com.solutions.note_it.shared.CustomFloatingActionButton
import com.solutions.note_it.shared.CustomTopBar
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteItApplication() : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any other libraries here if needed
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteIt() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.getScreen(backStackEntry?.destination?.route)

    Scaffold(topBar = {
        if (currentScreen.showCustomLayout) {
            CustomTopBar(
                navController = navController,
                title = currentScreen.title,
            )
        }
    }, bottomBar = {
        if (currentScreen.showCustomLayout) {
            CustomBottomBar(
                currentScreen = currentScreen, navController = navController
            )
        }
    }, floatingActionButton = {
        if (currentScreen.canCreate && currentScreen.showCustomLayout) {
            val screenTarget = if (currentScreen == Screen.Notes) Screen.CreateNote
            else Screen.CreateTask

            CustomFloatingActionButton(
                icon = Icons.Filled.Add,
                screenTarget = screenTarget,
                navController = navController
            )
        }
    }, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {
            NavHost(
                navController = navController, startDestination = Screen.Login.route
            ) {
                composable(route = Screen.Login.route) {
                    LoginScreen(
                        onLoginSuccess = {
                            navController.navigate(Screen.Notes.route)
                        })
                }

                composable(route = Screen.Notes.route) {
                    NotesScreen(onNoteClick = {
                        navController.navigate(
                            Screen.NoteDetails.createRoute(
                                noteId = it.id
                            )
                        )
                    })
                }

                composable(route = Screen.Tasks.route) {
                    TasksScreen(onTaskClick = {
                        navController.navigate(
                            Screen.TaskDetails.createRoute(
                                taskId = it.id
                            )
                        )
                    })
                }

                composable(
                    route = Screen.NoteDetails.route, arguments = Screen.NoteDetails.navArguments
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getString("noteId")
                    if (noteId != null) {
                        UpdateNoteScreen(
                            noteId = noteId,
                            onCommit = {
                                navController.navigate(Screen.Notes.route)
                            }
                        )
                    }
                }

                composable(
                    route = Screen.TaskDetails.route, arguments = Screen.TaskDetails.navArguments
                ) { backStackEntry ->
                    val taskId = backStackEntry.arguments?.getString("taskId")
                    if (taskId != null) {
                        UpdateTaskScreen(
                            taskId = taskId,
                            onCommit = {
                                navController.navigate(Screen.Tasks.route)
                            }
                        )
                    }
                }

                composable(route = Screen.CreateNote.route) {
                    CreateNoteScreen(
                        onCommit = {
                            navController.navigate(Screen.Notes.route)
                        }
                    )
                }
                composable(route = Screen.CreateTask.route) {
                    CreateTaskScreen(
                        onCommit = {
                            navController.navigate(Screen.Tasks.route)
                        }
                    )
                }
                composable(route = Screen.Account.route) {
                    AccountScreen()
                }
            }
        }
    }
}