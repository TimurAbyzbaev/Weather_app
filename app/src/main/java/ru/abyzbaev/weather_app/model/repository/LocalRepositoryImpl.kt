package ru.abyzbaev.weather_app.model.repository

import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.model.room.HistoryDao
import ru.abyzbaev.weather_app.utils.convertHistoryEntityToWeather
import ru.abyzbaev.weather_app.utils.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }


    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }


}