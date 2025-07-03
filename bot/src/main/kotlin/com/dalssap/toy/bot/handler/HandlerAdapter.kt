package com.dalssap.toy.bot.handler

import com.dalssap.toy.bot.common.*
import com.dalssap.toy.bot.common.request.CommandRequest
import com.dalssap.toy.bot.common.request.TelegramCommandRequest
import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.objects.Update
import java.lang.reflect.Method
import kotlin.reflect.full.findAnnotations

@Component
class HandlerAdapter(
    private val applicationContext: ApplicationContext
) {
    private val handlers: MutableMap<String, HandlerClassWrapper> = mutableMapOf()
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
        val hash = mutableSetOf<String>()

        handlerClasses.values.forEach { handlerClass ->
            val config = handlerClass.javaClass.getAnnotation(CommandHandlers::class.java)!!
            val handlerWrappers = mutableListOf<HandlerWrapper>()

            hash.putOrThrow(config.command)
            config.aliases.forEach { alias -> hash.putOrThrow(alias) }

            handlerClass.javaClass.declaredMethods.forEach { handler ->
                handler.getAnnotation(CommandHandler::class.java)?.let { annotation ->
                    handlerWrappers.add(HandlerWrapper(annotation, handler, handlerClass))
                }
            }

            val handlerClassWrapper = HandlerClassWrapper(
                handlerClass.defaultHandler(),
                handlerWrappers
            )

            handlers.put(config.command, handlerClassWrapper)
            config.aliases.forEach { alias -> handlers.put(alias, handlerClassWrapper) }
        }
    }

    class HandlerClassWrapper(
        val defaultHandler: HandlerWrapper,
        val handlers: List<HandlerWrapper>,
    ) {
        fun execute(request: CommandRequest): CommandResponse {
            return findHandler(request).execute(request)
        }

        fun findHandler(request: CommandRequest): HandlerWrapper {
            return handlers.firstOrNull { handler ->
                val config = handler.annotation
                config.matcher.matches(request, config.requireOptions)
            } ?: defaultHandler
        }
    }

    class HandlerWrapper(
        val annotation: CommandHandler,
        val method: Method,
        val bean: Any
    ) {
        companion object {
            private val completeResponse = RandomMessageResponse(listOf("완료다옹..."))
        }

        fun execute(request: CommandRequest): CommandResponse {
            val response = invoke(refineRequest(request))

            return when (response) {
                is CommandResponse -> response
                is String -> DefaultMessageResponse(response)
                else -> completeResponse
            }
        }

        fun refineRequest(request: CommandRequest): CommandRequest {
            val constructor = method.parameterTypes.filter { CommandRequest::class.java.isAssignableFrom(it) }
                .filter { it != CommandRequest::class.java }
                .map { it.getDeclaredConstructor(Update::class.java) }
                .firstOrNull() ?: return request

            return constructor.newInstance(request.update()) as CommandRequest
        }

        fun invoke(request: CommandRequest): Any {

            return method.invoke(bean, *extractParams(request))
        }

        fun extractParams(request: CommandRequest): Array<Any?> {

            return method.parameters.map { parameter ->
                when {
                    CommandRequest::class.java.isAssignableFrom(parameter.type) -> request
                    parameter.type == String::class.java -> request.option(parameter.name)
                    else -> null
                }
            }.toTypedArray()
        }
    }
}

fun <T: Any> MutableSet<T>.putOrThrow(e: T) {
    if (this.contains(e)) {
        throw IllegalArgumentException("${e.javaClass.name} is already set")
    }

    this.add(e)
}

fun Any.defaultHandler(): HandlerAdapter.HandlerWrapper {
    this.javaClass.declaredMethods.forEach { method ->
        if (method.isAnnotationPresent(DefaultCommandHandler::class.java)) {
            val annotation = method.getAnnotation(DefaultCommandHandler::class.java)
                .annotationClass.findAnnotations(CommandHandler::class)[0]

            return HandlerAdapter.HandlerWrapper(
                annotation,
                method,
                this
            )
        }
    }

    throw IllegalArgumentException("${this.javaClass.name} has no default handler")
}