package ru.abyzbaev.weather_app.view.weatherList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.abyzbaev.weather_app.model.*
import ru.abyzbaev.weather_app.viewmodel.AppState


class WeatherListViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()): ViewModel() {
    private lateinit var repositoryListWeather: RepositoryListWeather
    private lateinit var repositorySingleWeather: RepositorySingleWeather
    fun getLiveData(): MutableLiveData<AppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository(){
        repositorySingleWeather = if(isConnection()){
            RepositoryRemoteImpl()
        } else{
            RepositoryLocalImpl()
        }
        repositoryListWeather = RepositoryLocalImpl()
    }

    fun getWeatherListForRussia(){
        sentRequest(Location.Russian)
    }
    fun getWeatherListForWorld(){
        sentRequest(Location.World)
    }

    private fun sentRequest(location: Location){
        liveData.value = AppState.Loading// пошла загрузка
        if (false) {
            liveData.postValue(AppState.Error(throw IllegalStateException("Что то пошло не так...")))
        }
        else{
            liveData.postValue(AppState.SuccessMulti(repositoryListWeather.getListWeather(location)))
        }

    }

    private fun isConnection(): Boolean {
        return false
    }

    override fun onCleared() { //поиграться
        super.onCleared()

    }
}