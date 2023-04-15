package com.example.weatherpictureapp.core.shared_preferences

import javax.inject.Inject

class SharedPreferencesClient @Inject constructor(
	private val prefs: BaseSharedPrefs
) {
	fun saveWeatherPhotoUri(uri: String) {
		val weatherPhotoUriStrings = getWeatherPhotoUriStrings()
		val newWeatherPhotoUriStrings = if (weatherPhotoUriStrings.isEmpty()) {
			uri
		} else {
			"$weatherPhotoUriStrings,$uri"
		}
		prefs.saveString(WEATHER_PHOTO_URI_KEY, newWeatherPhotoUriStrings)
	}

	fun getWeatherPhotoUriList(): List<String> {
		return getWeatherPhotoUriStrings().split(",")
	}

	private fun getWeatherPhotoUriStrings(): String {
		return prefs.getString(WEATHER_PHOTO_URI_KEY)
	}


	companion object {
		const val WEATHER_PHOTO_URI_KEY = "weather-photo-uri-key"
	}
}
