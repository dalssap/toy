package com.dalssap.toy.bot.common

import com.dalssap.toy.bot.common.request.CommandRequest

enum class MatcherType(private val func: (CommandRequest, Array<String>) -> Boolean) {
    SKIP({ request, options -> false }),
    CONTAINS_ALL_OPTIONS({ request, options -> options.all { option -> request.hasOption(option) } }),
    ;

    fun matches(request: CommandRequest, options: Array<String>): Boolean {

        return func.invoke(request, options)
    }
}