package com.dalssap.toy.bot.common

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandHandler(
    val default: Boolean = false,
    val description: String = "",
    val masterOnly: Boolean = false,
    val requireOptions: Array<String> = [],
    val matcher: MatcherType = MatcherType.CONTAINS_ALL_OPTIONS,
)
