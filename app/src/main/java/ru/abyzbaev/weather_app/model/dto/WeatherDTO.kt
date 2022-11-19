package ru.abyzbaev.weather_app.model.dto


import com.google.gson.annotations.SerializedName
import ru.abyzbaev.weather_app.domain.City
import ru.abyzbaev.weather_app.domain.Weather

data class WeatherDTO(
    val fact: Fact?
)

fun WeatherDTO.mapTo(city: City):Weather{
    return Weather(city, this.fact?.temp?:0, this.fact?.feelsLike?:0)
}