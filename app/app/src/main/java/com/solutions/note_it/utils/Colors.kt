package com.solutions.note_it.utils

import androidx.compose.ui.graphics.Color

class Colors {
    companion object Utils {
        fun hexToColor(hex: String): Color {
            return Color(android.graphics.Color.parseColor(hex))
        }
    }
}