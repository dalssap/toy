package com.dalssap.toy.bot.common


interface CommandResponse {
    fun message(): String
    fun buttons(): List<Buttons> = emptyList()
    fun reusable(): Boolean = true
}

data class Buttons(
    val buttons: List<Button>
)

data class Button(
    val message: String,
    val callback: String,
)

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


class ButtonAttachedMessageResponse(
    private val message: String,
    private val buttons: List<Buttons>,
): CommandResponse {

    override fun message() = message
    override fun buttons() = buttons
}