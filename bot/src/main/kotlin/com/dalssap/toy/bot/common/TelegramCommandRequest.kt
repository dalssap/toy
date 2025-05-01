package com.dalssap.toy.bot.common

import org.telegram.telegrambots.meta.api.objects.Update
import kotlin.text.iterator

class TelegramCommandRequest(): CommandRequest {
    private var chatId: Long = 0
    private var messageId: Int? = null
    private var command: String = ""
    private var message: String = ""
    private val options = mutableMapOf<String, String>()

    constructor(update: Update) : this() {
        chatId = update.message.chatId
        messageId = update.message.messageId

        parseText(update.message.text)
    }

    private fun parseText(text: String) {
        val tokens = parseTokens(text)
        if (tokens.isEmpty()) return

        command = tokens[0].removePrefix("/")
        parseOptionsAndMessage(tokens.subList(1, tokens.size))
    }

    private fun parseTokens(text: String): List<String> {
        val tokens = mutableListOf<String>()
        var currentToken = StringBuilder()
        var inQuotes = false
        
        for (char in text) {
            when {
                char == '"' -> inQuotes = !inQuotes
                char == ' ' && !inQuotes -> {
                    if (currentToken.isNotEmpty()) {
                        tokens.add(currentToken.toString())
                        currentToken = StringBuilder()
                    }
                }
                else -> currentToken.append(char)
            }
        }
        
        if (currentToken.isNotEmpty()) {
            tokens.add(currentToken.toString())
        }
        
        return tokens
    }

    private fun parseOptionsAndMessage(tokens: List<String>) {
        val messageTokens = mutableListOf<String>()
        var i = 0
        
        while (i < tokens.size) {
            val token = tokens[i]
            if (token.startsWith("-")) {
                val optionName = token.substring(1)
                if (i + 1 < tokens.size && !tokens[i + 1].startsWith("-")) {
                    options[optionName] = tokens[i + 1]
                    i += 2
                } else {
                    options[optionName] = ""
                    i += 1
                }
            } else {
                messageTokens.add(token)
                i += 1
            }
        }
        
        message = messageTokens.joinToString(" ")
    }

    override fun command() = command

    override fun message() = message

    override fun option(key: String) = options[key]
}