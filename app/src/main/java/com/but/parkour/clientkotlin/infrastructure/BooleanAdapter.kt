package com.but.parkour.clientkotlin.infrastructure

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class BooleanAdapter {
    @FromJson
    fun fromJson(value: Any): Boolean? {
        return when (value) {
            is Number -> value.toInt() != 0 // Convertit 0 en false, tout autre nombre en true
            is Boolean -> value
            else -> null
        }
    }

    @ToJson
    fun toJson(value: Boolean): Int {
        return if (value) 1 else 0 // Convertit true en 1, false en 0
    }
}