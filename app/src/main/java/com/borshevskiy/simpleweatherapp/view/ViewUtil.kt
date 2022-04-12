package com.borshevskiy.simpleweatherapp.view

import android.text.Editable
import android.text.TextWatcher
import com.borshevskiy.simpleweatherapp.R
import com.borshevskiy.simpleweatherapp.business.model.GeoCodeModel
import com.borshevskiy.simpleweatherapp.business.room.GeoCodeEntity
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val DAY_FULL_MONTH_NAME = "dd MMMM"
const val DAY_WEEK_NAME_LONG = "dd EEEE"
const val HOUR_DOUBLE_DOT_MINUTE = "HH:mm"

fun Long.toDateFormatOf(format: String): String {
    val cal = Calendar.getInstance()
    val timeZone = cal.timeZone
    val sdf = SimpleDateFormat(format,Locale.getDefault())
    sdf.timeZone = timeZone
    return sdf.format(Date(this * 1000))
}

fun Double.toDegree() = (this - 273.15).roundToInt().toString()

fun Double.toPercentString(extraPart: String = "") = (this * 100).roundToInt().toString() + extraPart

fun String.provideIcon() = when(this){
    "01d", "01n" -> R.drawable.ic_01d
    "02d", "02n" -> R.drawable.ic_02d
    "03d", "03n" -> R.drawable.ic_03d
    "04d", "04n" -> R.drawable.ic_04d
    "09d", "09n" -> R.drawable.ic_09d
    "10d", "10n" -> R.drawable.ic_10d
    "11d", "11n" -> R.drawable.ic_11d
    "13d", "13n" -> R.drawable.ic_13d
    "50d", "50n" -> R.drawable.ic_50d
    else -> R.drawable.ic_error
}

fun TextInputEditText.createObservable(): Flowable<String> {
    return Flowable.create({
        addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                it.onNext(p0.toString())
            }
        })
    }, BackpressureStrategy.BUFFER)
}

fun GeoCodeModel.mapToEntity(): GeoCodeEntity {
    return GeoCodeEntity(name,local_names,lat, lon, country, state, isFavorite)
}

fun GeoCodeEntity.mapToModel(): GeoCodeModel {
    return GeoCodeModel(
        country, lat, localNames, lon, name, state, isFavorite)
}

private abstract class SimpleTextWatcher: TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
    }
}

