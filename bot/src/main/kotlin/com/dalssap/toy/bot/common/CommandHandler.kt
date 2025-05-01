package com.dalssap.toy.bot.common

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandHandler(
    val command: String,
    val aliases: Array<String> = [],
    val description: String = "",
    val masterOnly: Boolean = false,
)
