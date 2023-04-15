package com.example.weatherpictureapp.data.base_datasource

import com.example.weatherpictureapp.core.shared_preferences.SharedPreferencesClient
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.Flow
import java.net.URI

/**
 * Base interface for weather photos local dataSource
 *
 * provides abstraction for saving and retrieving weather photos
 */
abstract class BaseWeatherPhotoLocalDataSource(
	private val prefsClient: SharedPreferencesClient,
) {

	/**
	 * Gets weather photos [URI] list
	 *
	 * Returns [List] of [String] representing the [URI] of files
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
