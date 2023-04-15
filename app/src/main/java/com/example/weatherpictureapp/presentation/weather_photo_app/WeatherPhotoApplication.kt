package com.example.weatherpictureapp.presentation.weather_photo_app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherPhotoApplication: Application() {


	override fun onCreate() {
		super.onCreate()
	}

	companion object {
		@JvmStatic
		fun getAppContext(): Context {
			return WeatherPhotoApplication().applicationContext
		}

		const val WEATHER_PHOTO_FOLDER = "Pictures/Weather-Photo-App"
	}
}
