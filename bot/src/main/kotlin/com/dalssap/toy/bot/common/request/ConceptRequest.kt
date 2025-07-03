package com.dalssap.toy.bot.common.request

import com.dalssap.toy.common.function.isDateFormat
import org.telegram.telegrambots.meta.api.objects.Update

class ConceptRequest(update: Update): TelegramCommandRequest(update) {

    override fun postInit() {
        val tokens = messageTokens.toMutableList()
        when (messageTokens.size) {
            2 -> {
                parse("k", tokens)
                parse("v", tokens)
            }
            3 -> {
                parseDate(tokens)
                parse("k", tokens)
                parse("v", tokens)
            }
            else -> {

            }
        }
    }

    fun parseDate(tokens: MutableList<String>) {
        val dateIdx = tokens.indexOfFirst { it.isDateFormat() }
        putOption("d", tokens.removeAt(dateIdx))
    }

    fun parse(key: String, tokens: MutableList<String>) {
        putOption(key, tokens.removeFirst())
    }
}