package com.dalssap.toy.bot.common

interface CommandRequest {
    fun command(): String
    fun message(): String?
    fun option(key: String): String?
}