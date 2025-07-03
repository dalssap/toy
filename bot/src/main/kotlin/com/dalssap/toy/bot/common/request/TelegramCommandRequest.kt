package com.dalssap.toy.bot.common.request

import org.telegram.telegrambots.meta.api.objects.Update
import kotlin.text.iterator

open class TelegramCommandRequest private constructor(): CommandRequest {
    protected var update: Update? = null
    protected var chatId: Long = 0
    protected var messageId: Int? = null
    protected var command: String = ""
    protected var message: String = ""
    protected val messageTokens: MutableList<String> = mutableListOf()
    protected val options = mutableMapOf<String, String>()

    constructor(update: Update) : this() {
        this.update = update
        this.chatId = update.message.chatId
        this.messageId = update.message.messageId

        parseText(update.message.text)
        postInit()
    }

    open fun postInit() {
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
        var i = 0

        while (i < tokens.size) {
            val token = tokens[i]
            if (token.startsWith("-")) {
                val optionName = token.substring(1)
                if (i + 1 < tokens.size && !tokens[i + 1].startsWith("-")) {
                    putOption(optionName, tokens[i + 1])
                    i += 2
                } else {
                    putOption(optionName, "")
                    i += 1
                }
            } else {
                messageTokens.add(token)
                i += 1
            }
        }

        message = messageTokens.joinToString(" ")
    }

    fun putOption(key: String, value: String) {
        if (options.containsKey(key)) {
            throw Exception("Option with key $key already exists.")
        }

        options[key] = value
    }

    override fun update() = update!!

    override fun command() = command

    override fun message() = message

    override fun option(key: String) = options[key]

    override fun options() = options.keys

    override fun hasOption(key: String) = options.containsKey(key)
}