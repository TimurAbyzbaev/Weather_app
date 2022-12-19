package ru.abyzbaev.weather_app.view.weatherList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.abyzbaev.weather_app.AppState
import ru.abyzbaev.weather_app.model.repository.*


class WeatherListViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()) :
    ViewModel() {
    private lateinit var repositoryListWeather: RepositoryListWeather

    fun getLiveData(): MutableLiveData<AppState> {
        repositoryListWeather = RepositoryLocalImpl()
        return liveData
    }

    fun loadWeatherList(isRussian: Boolean) {
        if (isRussian) {
            getWeatherListForRussia()
        } else {
            getWeatherListForWorld()
        }
    }

    fun getWeatherListForRussia() {
        sentRequest(Location.Russian)
    }

    fun getWeatherListForWorld() {
        sentRequest(Location.World)
    }

    private fun sentRequest(location: Location) {
        liveData.value = AppState.Loading// пошла загрузка
        Thread {
            Thread.sleep(30L)
            if ((0..100).random() == 2) {
                liveData.postValue(AppState.Error(IllegalStateException("Что то пошло не так...")))
            } else {
                liveData.postValue(
                    AppState.Success(
                        repositoryListWeather.getListWeather(
                            location
                        )
                    )
                )
            }
        }.start()
    }
}