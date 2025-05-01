package com.dalssap.toy.bot

import com.dalssap.toy.common.config.YamlPropertySourceFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication(scanBasePackages = ["com.dalssap.toy"])
@PropertySource("classpath:/common.yaml", factory = YamlPropertySourceFactory::class)
class BotApplication

fun main(args: Array<String>) {
    runApplication<BotApplication>(*args)
}
