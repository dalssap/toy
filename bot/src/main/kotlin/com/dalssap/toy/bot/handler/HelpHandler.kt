package com.dalssap.toy.bot.handler

import com.dalssap.toy.bot.common.CommandHandler
import com.dalssap.toy.bot.common.CommandHandlers

@CommandHandlers
class HelpHandler {

    @CommandHandler("help", aliases = ["h"])
    fun help() = "무엇을 도와드릴까용?"
}