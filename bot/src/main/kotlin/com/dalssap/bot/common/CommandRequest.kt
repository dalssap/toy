package com.dalssap.bot.common

interface CommandRequest {
    fun command(): String
    fun message(): String?
    fun option(key: String): String?
}