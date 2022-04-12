package com.borshevskiy.simpleweatherapp.presenters

import com.borshevskiy.simpleweatherapp.business.ApiProvider
import com.borshevskiy.simpleweatherapp.business.model.GeoCodeModel
import com.borshevskiy.simpleweatherapp.business.repos.MenuRepo
import com.borshevskiy.simpleweatherapp.business.repos.MenuRepo.Companion.SAVED
import com.borshevskiy.simpleweatherapp.view.MenuView

class MenuPresenter : BasePresenter<MenuView>() {

    private val repo = MenuRepo(ApiProvider())

    override fun enable() {
        repo.dataEmitter.subscribe {
            viewState.setLoading(false)
            if (it.purpose == SAVED) {
                viewState.fillFavorites(it.data)
            } else {
                viewState.fillPredictionList(it.data)
            }
        }
    }

    fun searchFor(str: String) {
        repo.getCities(str)
    }

    fun removeLocation(data: GeoCodeModel) {
        repo.remove(data)
    }

    fun saveLocation(data: GeoCodeModel) {
        repo.add(data)
    }

    fun getFavoriteList() {
        repo.updateFavorite()
    }
}