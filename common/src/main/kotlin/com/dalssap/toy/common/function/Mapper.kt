package com.dalssap.toy.common.function

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class Mapper {
    companion object {
        private val mapper = ObjectMapper()

        fun toMap(json: String): Map<String, Any> {
            return mapper.readValue(json)
        }
    }
}

fun String.toMap() = Mapper.toMap(this)

fun Map<String, Any?>.uppercaseKeys(): Map<String, Any?> {
    return this.mapKeys { it.key.uppercase() }
        .mapValues { (_, value) ->
            when (value) {
                is Map<*, *> -> {
                    @Suppress("UNCHECKED_CAST")
                    (value as Map<String, Any?>).uppercaseKeys()
                }
                else -> value
            }
        }
}