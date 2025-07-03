package com.dalssap.toy.common.function

import com.dalssap.toy.common.function.StringFunctions.Companion.DATE_TIME_FORMATTERS
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class StringFunctions {
    companion object {
        val DATE_TIME_FORMATTERS = listOf(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyyMMdd"),
            DateTimeFormatter.ofPattern("yyMMdd")
        )
    }
}

fun Any?.join(separator: String = ", "): String? {
    if (this == null) return null
    if (this is String) return this
    if (this is Array<*>) return this.joinToString(separator)
    if (this is List<*>) return this.joinToString(separator)

    throw IllegalArgumentException("${this.javaClass.name} can't be cast to joined string")
}

fun String?.isDateFormat(): Boolean {
    for (formatter in DATE_TIME_FORMATTERS) {
        try {
            LocalDate.parse(it, formatter)
            return true
        } catch (e: DateTimeParseException) {
            // continue trying other formats
        }
    }

    return false
}