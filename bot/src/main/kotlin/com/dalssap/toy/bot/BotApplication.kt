package com.dalssap.toy.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.dalssap.toy"])
class BotApplication

fun main(args: Array<String>) {
    runApplication<BotApplication>(*args)
}
