package com.borshevskiy.simpleweatherapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.borshevskiy.simpleweatherapp.business.model.GeoCodeModel
import com.borshevskiy.simpleweatherapp.databinding.ActivityMenuBinding
import com.borshevskiy.simpleweatherapp.presenters.MenuPresenter
import com.borshevskiy.simpleweatherapp.view.MenuView
import com.borshevskiy.simpleweatherapp.view.adapters.CityListAdapter
import com.borshevskiy.simpleweatherapp.view.createObservable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.util.concurrent.TimeUnit

class MenuActivity : MvpAppCompatActivity(), MenuView {

    private lateinit var binding: ActivityMenuBinding

    private val menuPresenter by moxyPresenter{ MenuPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuPresenter.enable()
        menuPresenter.getFavoriteList()

        with(binding) {
            initCityList(predictions)
            initCityList(favorites)
            searchField.createObservable()
                .doOnNext { setLoading(true) }
                .debounce(700, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { if(!it.isNullOrEmpty()) menuPresenter.searchFor(it) }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_left)
    }

    override fun setLoading(flag: Boolean) {
        with(binding) {
            loading.isActivated = true
            loading.visibility = if(flag) View.VISIBLE else View.GONE
        }
    }

    override fun fillPredictionList(data: List<GeoCodeModel>) {
        (binding.predictions.adapter as CityListAdapter).cityList = data
    }

    override fun fillFavorites(data: List<GeoCodeModel>) {
        (binding.favorites.adapter as CityListAdapter).cityList = data
    }

    private fun initCityList(rv: RecyclerView) {
        val cityListAdapter = CityListAdapter()
        cityListAdapter.clickListener = searchItemClickListener
        rv.apply {
            adapter = cityListAdapter
            layoutManager = object : LinearLayoutManager(this@MenuActivity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            setHasFixedSize(true)
        }
    }

    private val searchItemClickListener = object : CityListAdapter.SearchItemClickListener {

        override fun addToFavorite(item: GeoCodeModel) {
            menuPresenter.saveLocation(item)
        }

        override fun removeFromFavorite(item: GeoCodeModel) {
            menuPresenter.removeLocation(item)
        }

        override fun showWeatherIn(item: GeoCodeModel) {
            val intent = Intent(this@MenuActivity, MainActivity::class.java)
            val bundle = Bundle()
            bundle.putString("lat", item.lat.toString())
            bundle.putString("lon", item.lon.toString())
            intent.putExtra(COORDINATES, bundle)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, R.anim.slide_in_left)
        }

    }

    companion object {
        const val COORDINATES = "coordinates"
    }

}