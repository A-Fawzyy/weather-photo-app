package com.example.weatherpictureapp.core.util

object OpenWeatherConstants {
	const val BASE_URL = "https://api.openweathermap.org/"
	private const val IMAGE_BASE_URL = "https://openweathermap.org/"
	const val CURRENT_WEATHER_DATA = "data/2.5/weather"
	private const val IMAGE_PATH = "img/wn/%s@2x.png"
	const val APP_ID_QUERY_PARAMETER_KEY = "appid"

	fun getImageUrl(icon: String?): String {
		return IMAGE_BASE_URL + IMAGE_PATH.replace("%s", icon ?: "https://openweathermap.org/img/wn/10d@2x.png")
	}
}
