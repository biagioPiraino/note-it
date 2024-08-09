package com.solutions.note_it.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.solutions.note_it.R.string.label_notes
import com.solutions.note_it.R.string.label_tasks
import com.solutions.note_it.R.string.route_notes
import com.solutions.note_it.R.string.route_tasks
import com.solutions.note_it.screens.Screen

data class BottomNavigationItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun CustomBottomBar(
    currentScreen: Screen,
    navController: NavController
) {
    val navItemColors = NavigationBarItemColors(
        selectedIconColor = Color.White,
        selectedTextColor = MaterialTheme.colorScheme.primary,
        selectedIndicatorColor = MaterialTheme.colorScheme.secondary,
        unselectedIconColor = Color.DarkGray,
        unselectedTextColor = Color.DarkGray,
        disabledIconColor = Color.LightGray,
        disabledTextColor = Color.LightGray
    )

    val navItems = listOf(
        BottomNavigationItem(
            screen = Screen.Notes,
            label = stringResource(label_notes),
            icon = Icons.Filled.Edit,
            route = stringResource(route_notes)
        ),
        BottomNavigationItem(
            screen = Screen.Tasks,
            label = stringResource(label_tasks),
            icon = Icons.Filled.DateRange,
            route = stringResource(route_tasks)
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier = Modifier.height(96.dp),
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            navItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    colors = navItemColors,
                    selected = index == selectedItemIndex && currentScreen == item.screen,
                    onClick = {
                        selectedItemIndex = index
                        navController.navigate(item.route)
                    },
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = item.label
                        )
                    },
                    label = {
                        Text(text = item.label)
                    }
                )
            }
        }
    }
}