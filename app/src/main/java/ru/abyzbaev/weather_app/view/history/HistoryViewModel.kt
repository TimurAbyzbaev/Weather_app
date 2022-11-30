package ru.abyzbaev.weather_app.view.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.abyzbaev.weather_app.App.Companion.getHistoryDao
import ru.abyzbaev.weather_app.AppState
import ru.abyzbaev.weather_app.model.repository.LocalRepository
import ru.abyzbaev.weather_app.model.repository.LocalRepositoryImpl

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
) : ViewModel() {
    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.Success(historyRepository.getAllHistory())
    }
}