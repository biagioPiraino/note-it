package com.solutions.note_it.utils

class Strings {
    companion object Utils {
        fun formatSummary(summary: String): String{
            val charLimit = 100
            if (summary.length >= charLimit){
                return "${summary.substring(0, charLimit)} ..."
            }
            return summary
        }
    }
}