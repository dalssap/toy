package com.dalssap.toy.bot.common.request

import org.telegram.telegrambots.meta.api.objects.Update

class HelpRequest(update: Update): TelegramCommandRequest(update) {

    override fun postInit() {
        when (messageTokens.size) {
            1 -> {
                options.put("d", messageTokens[0])
            }
            2 -> {
                options.put("d", messageTokens[1])
            }
            else -> {
                println("case default")
            }
        }
    }
}