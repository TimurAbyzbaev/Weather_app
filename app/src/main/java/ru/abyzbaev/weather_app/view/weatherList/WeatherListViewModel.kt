package ru.abyzbaev.weather_app.view.weatherList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.abyzbaev.weather_app.model.*
import ru.abyzbaev.weather_app.viewmodel.AppState
import java.lang.Math.random
import kotlin.random.Random


class WeatherListViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()) :
    ViewModel() {
    private lateinit var repositoryListWeather: RepositoryListWeather
    private lateinit var repositorySingleWeather: RepositorySingleWeather
    fun getLiveData(): MutableLiveData<AppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        repositorySingleWeather = if (isConnection()) {
            RepositoryRemoteImpl()
        } else {
            RepositoryLocalImpl()
        }
        repositoryListWeather = RepositoryLocalImpl()
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
            //liveData.postValue(AppState.SuccessMulti(repositoryListWeather.getListWeather(location)))
            if ((0..3).random() == 2) {
                liveData.postValue(AppState.Error(IllegalStateException("Что то пошло не так...")))
            } else {
                liveData.postValue(
                    AppState.SuccessMulti(
                        repositoryListWeather.getListWeather(
                            location
                        )
                    )
                )
            }
        }.start()
    }

    private fun isConnection(): Boolean {
        return false
    }

    override fun onCleared() { //поиграться
        super.onCleared()

    }
}