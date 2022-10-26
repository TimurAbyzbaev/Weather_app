package ru.abyzbaev.weather_app.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    var temperature: Int = 15,
    var feelsLike: Int = 13
) : Parcelable

@Parcelize
data class City(
    val name: String,
    val lat: Double,
    val lon: Double
) : Parcelable

fun getDefaultCity() = City("Москва", 27.3243523, 47.823434)

fun getWorldCities(): List<Weather> {
    return listOf(
        Weather(City("Токио", 35.7090259, 139.73199249999993), 1, 2),
        Weather(City("Лондон", 51.5073509, -0.1277583), 13, 15),
        Weather(City("Рим", 41.9027835, 12.4963655), 11, 9),
        Weather(City("Париж", 48.856614, 2.3522219), 12, 13),
        Weather(City("Берлин", 52.5200066, 13.404954), 9, 8),
        Weather(City("Минск", 53.90453979999999, 27.561524400000053), -1, -3),
        Weather(City("Стамбул", 41.00527, 28.97696), 18, 19),
        Weather(City("Вашингтон", 38.9071923, -77.0368707), -1, 0),
        Weather(City("Киев", 50.4501, 30.523400000000038), 12, 12),
        Weather(City("Пекин", 39.9075, 116.397), 14, 14),
    )
}

fun getRussianCities(): List<Weather> {
    return listOf(
        Weather(City("Москва", 55.751244, 37.618423), 15, 13),
        Weather(City("Санкт-Петербург", 59.937500, 30.308611), 13, 13),
        Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001), 10, 9),
        Weather(City("Новосибирск", 55.0415, 82.9346), 10, 9),
        Weather(City("Казань", 55.790278, 49.134722), 14, 13),
        Weather(City("Учалы", 54.308958, 59.408949), 10, 9),
        Weather(City("Тольятти", 53.5303, 49.3461), 12, 12),
        Weather(City("Нижний Новгород", 56.3287, 44.002), 10, 9),
        Weather(City("Сочи", 43.602808, 39.734154), 15, 14),
        Weather(City("Адлер", 43.434323, 39.933946), 17, 16),
    )
}
