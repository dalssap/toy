package com.dalssap.toy.common.domain.concept

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.time.ZonedDateTime

@Entity
@DiscriminatorValue("COMMON")
class Common(name: String, meaning: String, learnedAt: ZonedDateTime? = null): Concept(name, meaning, learnedAt = learnedAt) {

    companion object {
        fun of(name: String, meaning: String, learnedAt: ZonedDateTime? = null): Common {

            return Common(name, meaning, learnedAt)
        }
    }
}