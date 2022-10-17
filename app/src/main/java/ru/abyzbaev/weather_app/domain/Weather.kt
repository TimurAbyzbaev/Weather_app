package ru.abyzbaev.weather_app.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather (
    val city:City = getDefaultCity(),
    val temperature: Int = 15,
    val feelsLike: Int = 13
):Parcelable

@Parcelize
data class City(
    val name: String,
    val lat: Double,
    val lon: Double
):Parcelable

fun getDefaultCity() = City("Москва", 27.3243523, 47.823434)

fun getWorldCities():List<Weather>{
    return listOf(
        Weather(City("Токио", -27.3243523, 447.823434), 1, 2),
        Weather(City("Лондон", 294.845638, 484.23183), 13, 15),
        Weather(City("Рим", 287.5644813, 235.163686), 11, 9),
        Weather(City("Париж", -365.68486, -437.37263),12, 13),
        Weather(City("Берлин", 232.5584, -948.478),9, 8),
        Weather(City("Минск", 130.938736, 242.304846),-1, -3),
        Weather(City("Стамбул", 240.86357, 186.384675),18, 19),
        Weather(City("Вашингтон", -745.43798, -239.286453),-1, 0),
        Weather(City("Киев", -84764.34978, 134.9387),12, 12),
        Weather(City("Пекин", -874.2465, -87847.3469),14, 14),
    )
}

fun getRussianCities():List<Weather>{
    return listOf(
        Weather(City("Москва", 27.3243523, 47.823434), 15, 13),
        Weather(City("Санкт-Петербург", 94.845638, 84.23183), 13, 13),
        Weather(City("Екатеринбург", 87.5644813, 35.163686), 10, 9),
        Weather(City("Новосибирск", 365.68486, 437.37263),10, 9),
        Weather(City("Казань", 32.5584, 948.478),14, 13),
        Weather(City("Учалы", 30.938736, 42.304846),10, 9),
        Weather(City("Тольятти", 40.86357, 86.384675),12, 12),
        Weather(City("Нижний Новгород", 745.43798, 239.286453),10, 9),
        Weather(City("Сочи", 84764.34978, 34.9387),15, 14),
        Weather(City("Адлер", 874.2465, 87847.3469),17, 16),
    )
}
