package com.example.weatherpictureapp.data.data_model

import com.google.gson.annotations.SerializedName

data class WeatherDataModel(
	@SerializedName("weather")
	var weatherStatus: List<WeatherStatusDataModel>? = listOf(),
	@SerializedName("main")
	var weatherData: WeatherInfoDataModel? = WeatherInfoDataModel(),
	@SerializedName("wind")
	var wind: WindInfoDataModel? = WindInfoDataModel(),
	@SerializedName("visibility")
	var visibility: Int? = null
)
