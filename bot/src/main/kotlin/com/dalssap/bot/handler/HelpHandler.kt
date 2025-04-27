package com.dalssap.bot.handler

import com.dalssap.bot.common.CommandHandler
import com.dalssap.bot.common.CommandHandlers

@CommandHandlers
class HelpHandler {

    @CommandHandler("help", aliases = ["h"])
    fun help() = "무엇을 도와드릴까용?"
}