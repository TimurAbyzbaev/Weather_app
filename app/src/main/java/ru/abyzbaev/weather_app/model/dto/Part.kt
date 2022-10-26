package ru.abyzbaev.weather_app.model.dto


import com.google.gson.annotations.SerializedName

data class Part(
    @SerializedName("condition")
    val condition: String,
    @SerializedName("daytime")
    val daytime: String,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("humidity")
    val humidity: Double,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("part_name")
    val partName: String,
    @SerializedName("polar")
    val polar: Boolean,
    @SerializedName("prec_mm")
    val precMm: Double,
    @SerializedName("prec_period")
    val precPeriod: Double,
    @SerializedName("prec_prob")
    val precProb: Double,
    @SerializedName("pressure_mm")
    val pressureMm: Double,
    @SerializedName("pressure_pa")
    val pressurePa: Double,
    @SerializedName("temp_avg")
    val tempAvg: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_gust")
    val windGust: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double
)