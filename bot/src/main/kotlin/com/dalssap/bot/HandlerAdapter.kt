package com.dalssap.bot

import com.dalssap.bot.common.CommandRequest
import com.dalssap.bot.common.CommandHandler
import com.dalssap.bot.common.CommandHandlers
import com.dalssap.bot.common.CommandResponse
import com.dalssap.bot.common.DefaultMessageResponse
import com.dalssap.bot.common.RandomMessageResponse
import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Component
class HandlerAdapter(
    private val applicationContext: ApplicationContext
) {
    private val handlers: MutableMap<String, CommandWrapper> = mutableMapOf()
    private val defaultResponse = RandomMessageResponse(listOf("애옹", "..."))
    private val errorResponse = RandomMessageResponse(listOf("망가졌다옹...", "@!)@*(#***@"))


    fun invoke(request: CommandRequest): CommandResponse {
        if (!handlers.containsKey(request.command())) {
            return defaultResponse
        }

        return try {
            handlers[request.command()]!!.execute(request)
        } catch (e: Exception) {
            e.printStackTrace()
            errorResponse
        }
    }


    @PostConstruct
    fun init() {
        val handlerClasses = applicationContext.getBeansWithAnnotation(CommandHandlers::class.java)

        handlerClasses.values.forEach { handlerClass ->
            handlerClass.javaClass.declaredMethods.forEach { handler ->
                handler.getAnnotation(CommandHandler::class.java)?.let { annotation ->
                    putHandler(annotation.command, CommandWrapper(annotation, handler, handlerClass))

                    annotation.aliases.forEach { alias ->
                        putHandler(alias, CommandWrapper(annotation, handler, handlerClass))
                    }
                }
            }
        }
    }

    fun putHandler(key: String, commandWrapper: CommandWrapper) {
        if (handlers.containsKey(key)) {
            throw RuntimeException("Command $key already exists")
        }

        handlers[key] = commandWrapper
    }

    class CommandWrapper(
        val annotation: CommandHandler,
        val method: Method,
        val bean: Any
    ) {
        companion object {
            private val completeResponse = RandomMessageResponse(listOf("완료다옹..."))
        }

        fun execute(request: CommandRequest): CommandResponse {
            val response = invoke(request)

            return when (response) {
                is CommandResponse -> response
                is String -> DefaultMessageResponse(response)
                else -> completeResponse
            }
        }

        fun invoke(request: CommandRequest): Any {

            return method.invoke(bean, *extractParams(request))
        }

        fun extractParams(request: CommandRequest): Array<Any?> {

            return method.parameters.map { parameter ->
                when (parameter.type) {
                    CommandRequest::class.java -> request
                    String::class.java -> request.option(parameter.name)
                    else -> null
                }
            }.toTypedArray()
        }
    }
}