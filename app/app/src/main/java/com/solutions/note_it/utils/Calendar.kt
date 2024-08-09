package com.solutions.note_it.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class Calendar {
    companion object Utils {
        fun formatDate(selectedDateMillis: Long?): String {
            if (selectedDateMillis == null) {
                return "Select a date"
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDateMillis
            return sdf.format(calendar.time)
        }
    }
}