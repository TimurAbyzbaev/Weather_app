package ru.abyzbaev.weather_app.view.weatherList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.abyzbaev.weather_app.AppState
import ru.abyzbaev.weather_app.MainActivity
import ru.abyzbaev.weather_app.R
import ru.abyzbaev.weather_app.databinding.FragmentWeatherListBinding
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.utils.showSnackBar
import ru.abyzbaev.weather_app.view.details.DetailsFragment
import ru.abyzbaev.weather_app.view.details.OnItemClick

private const val IS_RUSSIAN_KEY = "LIST_OF_RUSSIAN_KEY"

class WeatherListFragment : Fragment(), OnItemClick {
    companion object {
        fun newInstance() = WeatherListFragment()
    }

    private var isRussian = true

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }
    lateinit var viewModel: WeatherListViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater)
        activity?.let {
            isRussian = it.getPreferences(Context.MODE_PRIVATE).getBoolean(IS_RUSSIAN_KEY, true)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<AppState> {
            override fun onChanged(t: AppState) {
                renderData(t)
            }
        })

        binding.weatherListFragmentFab.setOnClickListener {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherListForRussia()
                binding.weatherListFragmentFab.setImageResource(R.drawable.rus_icon)
            } else {
                viewModel.getWeatherListForWorld()
                binding.weatherListFragmentFab.setImageResource(R.drawable.world_icon)
            }
            saveListOfTowns()
        }
        viewModel.loadWeatherList(isRussian)
        //viewModel.getWeatherListForRussia()
    }

    private fun saveListOfTowns() {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()){
                putBoolean(IS_RUSSIAN_KEY, isRussian)
                apply()
            }
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.showResult()
                binding.weatherListRecycleView.showSnackBar(
                    "Ошибка",
                    "Попробовать снова",
                    Snackbar.LENGTH_INDEFINITE
                ) {
                    if (isRussian) {
                        viewModel.getWeatherListForRussia()
                        binding.weatherListFragmentFab.setImageResource(R.drawable.rus_icon)
                    } else {
                        viewModel.getWeatherListForWorld()
                        binding.weatherListFragmentFab.setImageResource(R.drawable.world_icon)
                    }
                }
            }
            AppState.Loading -> {
                binding.loading()
            }

            is AppState.Success -> {
                binding.showResult()
                binding.weatherListRecycleView.adapter =
                    WeatherListAdapter(appState.weatherData, this)

            }
        }
    }


    fun FragmentWeatherListBinding.loading() {
        this.loadingLayout.visibility = VISIBLE
    }

    fun FragmentWeatherListBinding.showResult() {
        this.loadingLayout.visibility = GONE
    }


    override fun onItemClick(weather: Weather) {
        (binding.root.context as MainActivity)
            .supportFragmentManager
            .beginTransaction()
            .hide(this)
            .add(R.id.container, DetailsFragment.newInstance(weather))
            .addToBackStack("")
            .commit()
    }


}