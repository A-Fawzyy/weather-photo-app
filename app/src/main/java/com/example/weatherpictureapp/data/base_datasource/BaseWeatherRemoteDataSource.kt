package com.example.weatherpictureapp.data.base_datasource

import com.example.weatherpictureapp.core.api_services.WeatherApi
import com.example.weatherpictureapp.core.util.error_handling.ErrorMapper
import com.example.weatherpictureapp.data.data_model.WeatherDataModel
import com.example.weatherpictureapp.domain.in_arguments.LatLng
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.Flow

/**
 * Base interface for weather remote data source
 *
 * Defines remote methods for getting weather data
 */
abstract class BaseWeatherRemoteDataSource(
	private val api: WeatherApi,
) {

	/**
	 * Get weather data by location coordinates
	 *
	 * [location] is used to get weather data for specific location
	 * it contains the latitude and longitude of the location
	 *
	 * Returns [WeatherDataModel] if the request was successful, null otherwise
	 */
	abstract fun getWeatherDataByCoordinates(
		location: LatLng
	): Flow<NetworkResult<WeatherDataModel>>
}
