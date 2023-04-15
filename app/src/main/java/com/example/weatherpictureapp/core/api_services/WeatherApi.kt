package com.example.weatherpictureapp.core.api_services

import com.example.weatherpictureapp.core.util.OpenWeatherConstants
import com.example.weatherpictureapp.data.data_model.WeatherDataModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

	@GET(OpenWeatherConstants.CURRENT_WEATHER_DATA)
	suspend fun getWeatherByLocationCoordinates(
		@Query(value = "lat") latitude: Double,
		@Query(value = "lon") longitude: Double,
		@Query(value = "units") units: String = "metric",
	): Response<WeatherDataModel>
}
