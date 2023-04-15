package com.example.weatherpictureapp.domain.domain_model

import com.google.gson.annotations.SerializedName

data class WeatherDomainModel(
	@SerializedName("weather")
	var weatherStatus: List<WeatherStatusDomainModel>? = listOf(),
	@SerializedName("main")
	var weatherData: WeatherInfoDomainModel? = WeatherInfoDomainModel(),
	@SerializedName("wind")
	var wind: WindInfoDomainModel? = WindInfoDomainModel(),
	@SerializedName("visibility")
	var visibility: Int? = null
)
