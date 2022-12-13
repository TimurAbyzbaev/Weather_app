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
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import ru.abyzbaev.weather_app.domain.City
import java.io.IOException

private const val REFRESH_PERIOD = 30000L
private const val MINIMAL_DISTANCE = 100f

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
        binding.weatherListFragmentFabLocation.setOnClickListener {
            checkPermission()
        }
        binding.searchButton.setOnClickListener{
            val cityName = binding.cityNameInput.text.toString()
            getWeatherByName(cityName)
        }
    }

    private fun getWeatherByName(cityName: String) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val location = geoCoder.getFromLocationName(
                    cityName,
                    1
                )
                openDetailFragment(
                    Weather(
                        City(
                            location[0].featureName,
                            location[0].latitude,
                            location[0].longitude
                        )
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_message))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                    requestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            } else {
                showDialog(
                    getString(R.string.dialog_title_no_gps),
                    getString(R.string.dialog_message_no_gps)
                )
            }
        }

    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Получить менеджер геолокаций
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    private val onLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it, location)
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun getAddressAsync(
        context: Context,
        location: Location
    ) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                binding.weatherListFragmentFab.post {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    openDetailFragment(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }.show()
        }
    }


    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }


    private fun saveListOfTowns() {
        activity?.let {
            with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
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
        /*(binding.root.context as MainActivity)
            .supportFragmentManager
            .beginTransaction()
            .hide(this)
            .add(R.id.container, DetailsFragment.newInstance(weather))
            .addToBackStack("")
            .commit()*/
        openDetailFragment(weather)
    }

    private fun openDetailFragment(weather: Weather) {
        activity?.supportFragmentManager?.apply {
            beginTransaction()
                //.hide(this)
                .replace(R.id.container, DetailsFragment.newInstance(weather))
                .addToBackStack("")
                .commit()
        }
    }


}