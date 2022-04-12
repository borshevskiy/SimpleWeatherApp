package com.borshevskiy.simpleweatherapp.business.repos

import com.borshevskiy.simpleweatherapp.business.ApiProvider
import com.borshevskiy.simpleweatherapp.business.model.GeoCodeModel
import com.borshevskiy.simpleweatherapp.view.mapToEntity
import com.borshevskiy.simpleweatherapp.view.mapToModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class MenuRepo(api: ApiProvider) : BaseRepository<MenuRepo.Content>(api) {

    private val dbAccess = db.getGeoCodeDAO()

    data class Content(val data: List<GeoCodeModel>, val purpose: Int)

    fun getCities(name: String) {
        api.provideGeoCodingApi().getCityByName(name).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { Content(it, CURRENT) }
            .subscribe { dataEmitter.onNext(it) }
    }

    fun add(data: GeoCodeModel) {
        getFavoriteListWith { dbAccess.add(data.mapToEntity()) }
    }

    fun remove(data: GeoCodeModel) {
        getFavoriteListWith { dbAccess.remove(data.mapToEntity()) }
    }

    fun updateFavorite() {
        getFavoriteListWith()
    }

    private fun getFavoriteListWith(daoQuery: (() -> Unit)? = null) {
        roomTransaction {
            daoQuery?.let { it() }
            Content(dbAccess.getAll().map { it.mapToModel() }, SAVED)
        }
    }

    companion object {
        const val SAVED = 1
        const val CURRENT = 0
    }
}