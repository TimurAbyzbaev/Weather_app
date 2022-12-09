package ru.abyzbaev.weather_app.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ID = "id"
const val CITY = "city"
const val TEMPERATURE = "temperature"
const val FEELS_LIKE = "feelsLike"

@Entity
class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temperature: Int,
    val feelsLike: Int
)