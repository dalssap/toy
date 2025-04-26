package com.dalssap.bot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
class TelegramBot(
    @Value("\${telegram.bot.token}")
    private val botToken: String
): SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private val telegramClient = OkHttpTelegramClient(botToken)

    override fun getBotToken() = botToken

    override fun getUpdatesConsumer() = this

    override fun consume(update: Update) {
        if (!update.hasMessage()) {
            return
        }

        try {
            telegramClient.execute(
                SendMessage.builder()
                    .chatId(update.message.chatId)
                    .text("애옹")
                    .build()
            )
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

}
