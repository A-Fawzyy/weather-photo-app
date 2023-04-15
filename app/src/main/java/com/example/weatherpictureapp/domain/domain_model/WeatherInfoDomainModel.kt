package com.example.weatherpictureapp.domain.domain_model

import com.google.gson.annotations.SerializedName

data class WeatherInfoDomainModel(

	@SerializedName("temp")
	var temprature: Double? = null,
	@SerializedName("feels_like")
	var feelsLike: Double? = null,
	@SerializedName("temp_min")
	var minTemperature: Double? = null,
	@SerializedName("temp_max")
	var maxTemperature: Double? = null,
	@SerializedName("pressure")
	var pressure: Int? = null,
	@SerializedName("humidity")
	var humidity: Int? = null

)
