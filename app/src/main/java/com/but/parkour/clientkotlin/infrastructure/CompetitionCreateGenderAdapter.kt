package com.but.parkour.clientkotlin.infrastructure

import com.but.parkour.clientkotlin.models.CompetitionCreate.Gender
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class CompetitionCreateGenderAdapter {
    @FromJson
    fun fromJson(gender: String): Gender {
        return when (gender) {
            "H" -> Gender.H
            "F" -> Gender.F
            else -> throw IllegalArgumentException("Genre inconnu: $gender")
        }
    }

    @ToJson
    fun toJson(gender: Gender): String {
        return gender.value
    }
}