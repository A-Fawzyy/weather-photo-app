package com.example.weatherpictureapp.data.datasource

import com.example.weatherpictureapp.core.shared_preferences.SharedPreferencesClient
import com.example.weatherpictureapp.data.base_datasource.BaseWeatherPhotoLocalDataSource
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.*
import java.net.URI
import javax.inject.Inject

class WeatherPhotoLocalDataSource @Inject constructor(
	private val prefsClient: SharedPreferencesClient
) : BaseWeatherPhotoLocalDataSource(prefsClient) {

	override fun getWeatherPhotoUrisList(): Flow<NetworkResult<List<String>>> = flow {
		emit(NetworkResult.Loading)
		val response = prefsClient.getWeatherPhotoUriList()
		if(response.isNotEmpty()) {
			emit(NetworkResult.Success(response))
		} else {
			throw Exception("No weather photos found")
		}
	}

	override fun saveWeatherPhotoUri(photoUri: String): Flow<NetworkResult<Nothing?>> = flow {
		emit(NetworkResult.Loading)
		prefsClient.saveWeatherPhotoUri(photoUri)
		emit(NetworkResult.Success(null))
	}
}
