package ru.abyzbaev.weather_app.domain

data class Weather (
    val city:City = getDefaultCity(),
    val temperature: Int = 15,
    val feelsLike: Int = 13
)

data class City(
    val name: String,
    val lat: Double,
    val lon: Double
)

fun getDefaultCity() = City("Москва", 27.3243523, 47.823434)
