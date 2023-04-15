package com.example.weatherpictureapp.domain.base_repo

import com.example.weatherpictureapp.data.base_datasource.BaseWeatherPhotoLocalDataSource
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.Flow

/**
 * Base interface for weather repository
 *
 * Defines methods for getting weather data from DataSources
 */
abstract class BaseWeatherPhotoRepo(
	private val weatherPhotoLocalDataSource: BaseWeatherPhotoLocalDataSource,
) {

	/**
	 * Gets weather photos URI list
	 *
	 * Returns [List] of [String] representing the URI of files
	 */
	abstract fun getWeatherPhotoUrisList(): Flow<NetworkResult<List<String>>>

	/**
	 * Saves weather photo [URI]
	 *
	 * appends it to the existing list of [URI]s
	 */
	abstract fun saveWeatherPhotoUri(
		photoUri: String
	): Flow<NetworkResult<Nothing?>>
}
