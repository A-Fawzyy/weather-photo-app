package com.example.weatherpictureapp.domain.base_repo

import com.example.weatherpictureapp.data.base_datasource.BaseWeatherRemoteDataSource
import com.example.weatherpictureapp.domain.domain_model.WeatherDomainModel
import com.example.weatherpictureapp.domain.in_arguments.LatLng
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.Flow

/**
 * Base interface for weather repository
 *
 * Defines methods for getting weather data from DataSources
 */
abstract class BaseWeatherRepo(
	private val weatherRemoteDataSource: BaseWeatherRemoteDataSource
) {

	/**
	 * Get weather data from remote data source
	 *
	 * [location] is used to get weather data for specific location
	 * it contains the latitude and longitude of the location
	 *
	 * Returns [WeatherDomainModel] containing weather data for location
	 */
	abstract fun getWeatherData(location: LatLng): Flow<NetworkResult<WeatherDomainModel>>
}
