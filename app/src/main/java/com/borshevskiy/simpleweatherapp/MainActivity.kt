package com.borshevskiy.simpleweatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.borshevskiy.simpleweatherapp.MenuActivity.Companion.COORDINATES
import com.borshevskiy.simpleweatherapp.business.model.DailyWeatherModel
import com.borshevskiy.simpleweatherapp.business.model.HourlyWeatherModel
import com.borshevskiy.simpleweatherapp.business.model.WeatherDataModel
import com.borshevskiy.simpleweatherapp.databinding.ActivityMainBinding
import com.borshevskiy.simpleweatherapp.presenters.MainPresenter
import com.borshevskiy.simpleweatherapp.view.*
import com.borshevskiy.simpleweatherapp.view.adapters.MainDailyListAdapter
import com.borshevskiy.simpleweatherapp.view.adapters.MainHourlyListAdapter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    private val mainPresenter by moxyPresenter{ MainPresenter() }

    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy { initLocationRequest() }
    private lateinit var mLocation: Location

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!intent.hasExtra(COORDINATES)) {
            geoService.requestLocationUpdates(locationRequest,geoCallback, Looper.getMainLooper())
        } else {
            val coord = intent.extras!!.getBundle(COORDINATES)!!
            val loc = Location("")
            loc.latitude = coord.getString("lat")!!.toDouble()
            loc.longitude = coord.getString("lon")!!.toDouble()
            mLocation = loc
            mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
        }

        binding.mainSettingsBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
        }

        binding.mainMenuBtn.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }

        binding.mainHourlyList.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
        binding.mainDailyList.apply {
            adapter = MainDailyListAdapter()
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        mainPresenter.enable()
    }

    //----------------MOXY CODE-----------------------

    override fun displayLocation(data: String) {
        binding.mainCityNameTv.text = data
    }

    override fun displayCurrentData(data: WeatherDataModel) {
        with(binding){
            data.apply {
                mainDateTv.text = current.dt.toDateFormatOf(DAY_FULL_MONTH_NAME)
                mainWeatherConditionIcon.setImageResource(current.weather[0].icon.provideIcon())
                mainTemp.text = StringBuilder().append(current.temp.toDegree()).append("°").toString()
                daily[0].temp.apply {
                    maxTemp.text = StringBuilder().append(max.toDegree()).append("°").toString()
                    minTemp.text = StringBuilder().append(min.toDegree()).append("°").toString()
                    avgTemp.text = StringBuilder().append(((min+max)/2).toDegree()).append("°").toString()
                }
                mainWeatherImage.setImageResource(R.mipmap.cloud3x)

                val pressureSet = SettingsHolder.pressure
                pressure.text = getString(pressureSet.measureUnitStringRes, pressureSet.getValue(current.pressure.toDouble()))

                humidity.text = StringBuilder().append(current.humidity).append("%").toString()

                val windSpeedSet = SettingsHolder.windSpeed
                windSpeed.text = getString(windSpeedSet.measureUnitStringRes, pressureSet.getValue(current.pressure.toDouble()))
            }
        }
    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (binding.mainHourlyList.adapter as MainHourlyListAdapter).hourlyWeather = data
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (binding.mainDailyList.adapter as MainDailyListAdapter).dailyWeather = data
    }

    override fun displayError(error: Throwable) {

    }

    override fun setLoading(flag: Boolean) {

    }

    //----------------LOCATION CODE-----------------------

    private fun initLocationRequest() : LocationRequest {
        val request = LocationRequest.create()
        return request.apply {
            interval = 100000
            fastestInterval = 50000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY}
    }

    private val geoCallback = object : LocationCallback() {

        override fun onLocationResult(geo: LocationResult) {
            Log.d("TEST", "onLocationResult: ${geo.locations.size}")
            for (location in geo.locations) {
                mLocation = location
                Log.d("TEST", "onLocationResult: lat:${location.latitude} lon: ${location.longitude}")
                mainPresenter.refresh(mLocation.latitude.toString(),mLocation.longitude.toString())
            }
        }
    }
}