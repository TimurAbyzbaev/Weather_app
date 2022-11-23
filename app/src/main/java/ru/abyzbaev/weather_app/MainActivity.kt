package ru.abyzbaev.weather_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.abyzbaev.weather_app.databinding.ActivityMainBinding
import ru.abyzbaev.weather_app.view.weatherList.WeatherListFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.myRoot)



        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }
    }
}