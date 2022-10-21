package ru.abyzbaev.weather_app.view.weatherList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.abyzbaev.weather_app.MainActivity
import ru.abyzbaev.weather_app.R
import ru.abyzbaev.weather_app.databinding.FragmentWeatherListBinding
import ru.abyzbaev.weather_app.domain.Weather
import ru.abyzbaev.weather_app.view.details.DetailsFragment
import ru.abyzbaev.weather_app.view.details.OnItemClick
import ru.abyzbaev.weather_app.viewmodel.AppState

class WeatherListFragment : Fragment(), OnItemClick {
    companion object {
        fun newInstance() = WeatherListFragment()
    }

    var isRussian = true

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
        //viewModel.sentRequest()

        binding.weatherListFragmentFab.setOnClickListener {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherListForRussia()
                binding.weatherListFragmentFab.setImageResource(R.drawable.rus_icon)
            } else {
                viewModel.getWeatherListForWorld()
                binding.weatherListFragmentFab.setImageResource(R.drawable.world_icon)
            }
        }
        viewModel.getWeatherListForRussia()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                binding.showResult()
            }
            AppState.Loading -> {
                binding.loading()
            }
            is AppState.SuccessMulti -> {
                binding.showResult()
                binding.weatherListRecycleView.adapter =
                    WeatherListAdapter(appState.weatherList, this)
            }
            is AppState.SuccessOne -> {
                binding.showResult()

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