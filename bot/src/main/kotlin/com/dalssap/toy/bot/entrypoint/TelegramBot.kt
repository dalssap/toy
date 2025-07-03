package com.dalssap.toy.bot.entrypoint

import com.dalssap.toy.bot.common.request.TelegramCommandRequest
import com.dalssap.toy.bot.handler.HandlerAdapter
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Component
class TelegramBot(
    @Value("\${telegram.bot.token}")
    private val botToken: String,
    private val handlerAdapter: HandlerAdapter
): SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    companion object: KLogging()

    private val telegramClient = OkHttpTelegramClient(botToken)

    override fun getBotToken() = botToken

    override fun getUpdatesConsumer() = this

    override fun consume(update: Update) {
        logger.info { update.message?.text ?: "" }

        if (!(update.isCommand() || update.hasCallbackQuery())) {
            return
        }

//        try {
//            if (update.hasCallbackQuery()) {
//                val call_data = update.callbackQuery.data
//                val message_id = update.callbackQuery.message.messageId
//                val chat_id = update.callbackQuery.message.chatId
//                if (call_data.startsWith("popup")) {
//                    telegramClient.execute(
//                        AnswerCallbackQuery.builder()
//                            .callbackQueryId(update.callbackQuery.id)
//                            .text(call_data.removePrefix("popup "))  // 팝업에 표시될 메시지
//                            .showAlert(true)  // true로 설정하면 팝업이 표시됩니다
//                            .build()
//                    )
//                } else {
//
//                    telegramClient.execute(
////                        EditMessageText.builder()
//                        SendMessage.builder()
//                            .chatId(update.callbackQuery.message.chatId)
//                            .text("callback")
//                            .replyMarkup(
//                                InlineKeyboardMarkup
//                                    .builder()
//                                    .keyboardRow(
//                                        InlineKeyboardRow(
//                                            InlineKeyboardButton
//                                                .builder()
//                                                .text("Update message text1")
//                                                .callbackData("update_msg_text")
//                                                .build(),
//                                            InlineKeyboardButton
//                                                .builder()
//                                                .text("Update message text2")
//                                                .callbackData("popup message to show")
//                                                .build()
//                                        ),
//                                    )
//                                    .build()
//                            )
//                            .build()
//                    )
//                }
//            } else {
//                telegramClient.execute(
//                SendPhoto
//                    .builder()
//                    .chatId(update.message.chatId)
//                    .photo(InputFile("https://storage.googleapis.com/nokchax-dev.firebasestorage.app/kanji/0101_thumbnail.jpg"))
//                    .caption("This is a little cat :)")
//                    .replyMarkup(
//                            InlineKeyboardMarkup
//                                .builder()
//                                .keyboardRow(
//                                    InlineKeyboardRow(
//                                        InlineKeyboardButton
//                                            .builder()
//                                            .text("Update message text")
//                                            .callbackData("update_msg_text")
//                                            .build()
//                                    )
//                                )
//                                .build()
//                    )
//                    .build()
//                )
////                telegramClient.execute(
////                    SendMessage.builder()
////                        .chatId(update.message.chatId)
////                        .text("test")
////                        .replyMarkup(
////                            InlineKeyboardMarkup
////                                .builder()
////                                .keyboardRow(
////                                    InlineKeyboardRow(
////                                        InlineKeyboardButton
////                                            .builder()
////                                            .text("Update message text")
////                                            .callbackData("update_msg_text")
////                                            .build()
////                                    )
////                                )
////                                .build()
////                        )
////                        .build()
////                )
//            }
//        } catch (e: TelegramApiException) {
//            e.printStackTrace()
//        }

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
