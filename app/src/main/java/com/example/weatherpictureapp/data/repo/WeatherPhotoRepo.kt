package com.example.weatherpictureapp.data.repo

import com.example.weatherpictureapp.data.base_datasource.BaseWeatherPhotoLocalDataSource
import com.example.weatherpictureapp.domain.base_repo.BaseWeatherPhotoRepo
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.net.URI
import javax.inject.Inject

class WeatherPhotoRepo @Inject constructor(
	private val weatherPhotoLocalDataSource: BaseWeatherPhotoLocalDataSource
) : BaseWeatherPhotoRepo(weatherPhotoLocalDataSource) {

	override fun getWeatherPhotoUrisList(): Flow<NetworkResult<List<String>>> =
		weatherPhotoLocalDataSource.getWeatherPhotoUrisList().map {
			when (it) {
				is NetworkResult.Success -> {
					NetworkResult.Success(it.value)
				}

				is NetworkResult.Error -> {
					NetworkResult.Error(it.error)
				}

				is NetworkResult.Loading -> {
					NetworkResult.Loading
				}
			}
		}

	override fun saveWeatherPhotoUri(photoUri: String): Flow<NetworkResult<Nothing?>> =
		weatherPhotoLocalDataSource.saveWeatherPhotoUri(photoUri).map {
			when (it) {
				is NetworkResult.Success -> {
					NetworkResult.Success(null)
				}

				is NetworkResult.Error -> {
					NetworkResult.Error(it.error)
				}

				is NetworkResult.Loading -> {
					NetworkResult.Loading
				}
			}
		}


}
