package ru.abyzbaev.weather_app.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.domain.getDefaultCity
import ru.abyzbaev.weather_app.model.dto.Fact
import ru.abyzbaev.weather_app.model.dto.WeatherDTO
import java.io.BufferedReader
import java.util.stream.Collectors

class Utils {
}

fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: Fact = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(), fact.temp, fact.feelsLike))
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE;
}

fun View.showSnackBar(
    text: String,
    actionText: String,
    length: Int = Snackbar.LENGTH_INDEFINITE,
    action: (View) -> Unit
) {
    Snackbar.make(this, text, length).setAction(actionText, action).show()
}