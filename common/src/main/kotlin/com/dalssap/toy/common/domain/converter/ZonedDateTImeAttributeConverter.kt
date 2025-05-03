package com.dalssap.toy.common.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

@Converter(autoApply = true)
class ZonedDateTImeAttributeConverter: AttributeConverter<ZonedDateTime, Date> {

    override fun convertToDatabaseColumn(zonedDateTime: ZonedDateTime?): Date? {
        return zonedDateTime?.let { Date.from(zonedDateTime.toInstant()) }
    }

    override fun convertToEntityAttribute(date: Date?): ZonedDateTime? {
        return date?.let { ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) }
    }
}
