package com.dalssap.toy.bot

import com.dalssap.toy.bot.common.TelegramCommandRequest
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
    private val botToken: String,
    private val handlerAdapter: HandlerAdapter
): SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private val telegramClient = OkHttpTelegramClient(botToken)

    override fun getBotToken() = botToken

    override fun getUpdatesConsumer() = this

    override fun consume(update: Update) {
        if (!update.isCommand()) {
            return
        }

        try {
            val response = handlerAdapter.invoke(TelegramCommandRequest(update))

            telegramClient.execute(
                SendMessage.builder()
                    .chatId(update.message.chatId)
                    .text(response.message())
                    .build()
            )
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }

}

fun Update.isCommand(): Boolean {
    return this.hasMessage()
            && this.message.hasText()
            && this.message.text.startsWith("/")
}
