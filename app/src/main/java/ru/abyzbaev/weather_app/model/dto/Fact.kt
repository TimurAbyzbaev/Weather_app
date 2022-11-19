package ru.abyzbaev.weather_app.model.dto


import com.google.gson.annotations.SerializedName

data class Fact(
    @SerializedName("feels_like")
    val feelsLike: Int,
    @SerializedName("temp")
    val temp: Int,

)