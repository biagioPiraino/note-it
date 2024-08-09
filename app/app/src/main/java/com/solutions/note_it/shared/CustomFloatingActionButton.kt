package com.solutions.note_it.shared

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.solutions.note_it.R.string.desc_float_create_button
import com.solutions.note_it.screens.Screen

@Composable
fun CustomFloatingActionButton(
    icon: ImageVector,
    screenTarget: Screen,
    navController: NavController
) {
    FloatingActionButton(
        onClick = { navController.navigate(screenTarget.route) },
        shape = CircleShape,
        contentColor = Color.White,
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        Icon(
            icon,
            contentDescription = stringResource(desc_float_create_button)
        )
    }
}