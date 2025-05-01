package com.dalssap.toy.bot.common


interface CommandResponse {
    fun message(): String
}

class RandomMessageResponse(
    private val messages: List<String>
): CommandResponse {

    override fun message() = messages.shuffled().first()
}

class DefaultMessageResponse(
    private val message: String
): CommandResponse {

    override fun message() = message
}