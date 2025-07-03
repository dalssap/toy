package com.dalssap.toy.bot.handler

import com.dalssap.toy.bot.common.CommandHandler
import com.dalssap.toy.bot.common.CommandHandlers
import com.dalssap.toy.bot.common.DefaultCommandHandler
import com.dalssap.toy.bot.common.request.CommandRequest
import com.dalssap.toy.bot.common.request.HelpRequest

@CommandHandlers("help", aliases = ["h"])
class HelpHandler {

    @DefaultCommandHandler
    fun help(helpRequest: HelpRequest) = "무엇을 도와드릴까용?"

    @CommandHandler(requireOptions = ["d"])
    fun debug(commandRequest: CommandRequest) = "디버깅입니다용"
}