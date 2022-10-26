package ru.abyzbaev.weather_app.model.dto


import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("date")
    val date: String,
    @SerializedName("date_ts")
    val dateTs: Double,
    @SerializedName("moon_code")
    val moonCode: Double,
    @SerializedName("moon_text")
    val moonText: String,
    @SerializedName("parts")
    val parts: List<Part>,
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("week")
    val week: Int
)