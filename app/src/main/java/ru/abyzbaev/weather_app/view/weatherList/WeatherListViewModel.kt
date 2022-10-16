package ru.abyzbaev.weather_app.view.weatherList

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.abyzbaev.weather_app.model.*
import ru.abyzbaev.weather_app.viewmodel.AppState
import java.lang.Math.random
import java.util.*


class WeatherListViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()): ViewModel() {
    private lateinit var repositoryMulti: RepositoryMany
    private lateinit var repositoryOne: RepositoryOne
    fun getLiveData(): MutableLiveData<AppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository(){
        repositoryOne = if(isConnection()){
            RepositoryRemoteImpl()
        } else{
            RepositoryLocalImpl()
        }
        repositoryMulti = RepositoryLocalImpl()
    }

    fun getWeatherListForRussia(){
        sentRequest(Location.Russian)
    }
    fun getWeatherListForWorld(){
        sentRequest(Location.World)
    }

    private fun sentRequest(location: Location){
        liveData.value = AppState.Loading// пошла загрузка
        val rnd:Int = (random()*10).toInt()
        /**
         * Якобы никогда не будет 1 при рандоме, но вроде как попадает туда иногда
         */
        if (rnd == 1) {
            liveData.postValue(AppState.Error(throw IllegalStateException("Что то пошло не так...")))
        }
        else{
            liveData.postValue(AppState.SuccessMulti(repositoryMulti.getListWeather(location)))
        }

    }

    private fun isConnection(): Boolean {
        return false
    }

    override fun onCleared() { //поиграться
        super.onCleared()

    }
}