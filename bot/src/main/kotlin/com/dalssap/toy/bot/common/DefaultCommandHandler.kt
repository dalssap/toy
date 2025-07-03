package com.dalssap.toy.bot.common

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@CommandHandler(default = true, matcher = MatcherType.SKIP)
annotation class DefaultCommandHandler
