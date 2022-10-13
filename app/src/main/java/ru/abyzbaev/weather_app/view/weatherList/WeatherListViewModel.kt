package ru.abyzbaev.weather_app.view.weatherList

import android.os.SystemClock.sleep
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.abyzbaev.weather_app.viewmodel.AppState

class WeatherListViewModel(val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()): ViewModel() {


    fun sentRequest(){
        liveData.value = AppState.Loading// пошла загрузка
        Thread{
            sleep(2000)
            //запрос репозиторий
            liveData.postValue(AppState.Success(Any())) //пришел ответ
        }.start()
        ///PostValue может залагать если 3 сразу использовать. Что то точно не отобразится.

    }
}