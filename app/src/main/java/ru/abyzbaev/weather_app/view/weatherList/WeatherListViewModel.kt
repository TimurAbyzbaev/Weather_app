package ru.abyzbaev.weather_app.view.weatherList

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.abyzbaev.weather_app.model.Repository
import ru.abyzbaev.weather_app.model.RepositoryLocalImpl
import ru.abyzbaev.weather_app.model.RepositoryRemoteImpl
import ru.abyzbaev.weather_app.viewmodel.AppState
import java.lang.Math.random
import java.util.*


class WeatherListViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()): ViewModel() {
    private lateinit var repository: Repository
    fun getLiveData(): MutableLiveData<AppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository(){
        repository = if(isConnection()){
            RepositoryRemoteImpl()
        } else{
            RepositoryLocalImpl()
        }
    }

    fun sentRequest(){
        liveData.value = AppState.Loading// пошла загрузка
        val rnd:Int = (random()*10).toInt()
        /**
         * Якобы никогда не будет 1 при рандоме, но вроде как попадает туда иногда
         */
        if (rnd == 1) {
            liveData.postValue(AppState.Error(throw IllegalStateException("Что то пошло не так...")))
        }
        else{
            Thread{
                sleep(3000)
                liveData.postValue(AppState.Success(repository.getWeather(27.3243523, 47.823434)))
            }.start()

        }

    }

    private fun isConnection(): Boolean {
        return false
    }

    override fun onCleared() { //поиграться
        super.onCleared()

    }
}