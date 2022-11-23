package ru.abyzbaev.weather_app.view.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.abyzbaev.weather_app.AppState
import ru.abyzbaev.weather_app.model.DetailsRepositoryImpl
import ru.abyzbaev.weather_app.model.RemoteDataSource
import ru.abyzbaev.weather_app.model.dto.WeatherDTO
import ru.abyzbaev.weather_app.utils.convertDtoToModel


private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepositoryImpl = DetailsRepositoryImpl(
        RemoteDataSource()
    )
) : ViewModel() {
    fun getLiveData() = detailsLiveData

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callback)
    }

    private val callback = object : Callback<WeatherDTO> {
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    private fun checkResponse(serverResponse: WeatherDTO): AppState {
        val fact = serverResponse.fact
        return if (fact == null || fact.temp == null || fact.feelsLike == null) {
            AppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            AppState.Success(convertDtoToModel(serverResponse))
        }
    }
}