package com.but.parkour.clientkotlin.infrastructure

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class OffsetDateTimeAdapter {
    private val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    private val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @ToJson
    fun toJson(value: OffsetDateTime): String {
        return value.format(formatter1)
    }

    @FromJson
    fun fromJson(value: String): OffsetDateTime {
        return try {
            OffsetDateTime.parse(value, formatter1)
        } catch (e: Exception) {
            val localDateTime = LocalDateTime.parse(value, formatter2)
            localDateTime.atOffset(ZoneOffset.UTC)
        }
    }

}
