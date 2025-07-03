package com.dalssap.toy.bot.common.request

import org.telegram.telegrambots.meta.api.objects.Update

interface CommandRequest {
    fun update(): Update
    fun command(): String
    fun message(): String?
    fun option(key: String): String?
    fun hasOption(key: String): Boolean
    fun options(): Set<String>
}