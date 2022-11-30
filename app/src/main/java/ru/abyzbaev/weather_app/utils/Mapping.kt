package ru.abyzbaev.weather_app.utils

import ru.abyzbaev.weather_app.domain.City
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.domain.getDefaultCity
import ru.abyzbaev.weather_app.model.dto.Fact
import ru.abyzbaev.weather_app.model.dto.WeatherDTO
import ru.abyzbaev.weather_app.model.room.HistoryEntity

class Mapping {

}

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: Fact = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(), fact.temp, fact.feelsLike))
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, it.feelsLike)
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.temperature, weather.feelsLike)
}