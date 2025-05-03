package com.dalssap.toy.common.domain.concept

import com.dalssap.toy.common.domain.BaseEntity
import jakarta.persistence.Entity
import java.time.ZonedDateTime
import java.time.ZonedDateTime.now

@Entity
class Concept(
    category: String,
    name: String,
    meaning: String? = null
): BaseEntity() {
    val category: String = category
    val name: String = name
    val meaning: String = meaning ?: ""
    val learnedAt: ZonedDateTime = now()

    override fun toString(): String {
        return "Concept(category='$category', name='$name', meaning='$meaning', learnedAt=$learnedAt)"
    }

}