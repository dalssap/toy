package com.dalssap.toy.bot.common

import org.springframework.stereotype.Component

@Component
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandHandlers(
    val command: String,
    val aliases: Array<String> = [],
    val masterOnly: Boolean = false,
)
