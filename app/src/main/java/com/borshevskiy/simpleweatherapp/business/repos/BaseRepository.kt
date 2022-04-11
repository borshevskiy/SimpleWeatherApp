package com.borshevskiy.simpleweatherapp.business.repos

import com.borshevskiy.simpleweatherapp.App
import com.borshevskiy.simpleweatherapp.business.ApiProvider
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseRepository<T>(val api: ApiProvider) {

    val dataEmitter: BehaviorSubject<T> = BehaviorSubject.create()
    protected val db by lazy { App.db }

}