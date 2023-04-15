package com.example.weatherpictureapp.data.datasource

import com.example.weatherpictureapp.core.api_services.WeatherApi
import com.example.weatherpictureapp.core.util.error_handling.ErrorMapper
import com.example.weatherpictureapp.data.base_datasource.BaseWeatherRemoteDataSource
import com.example.weatherpictureapp.data.data_model.WeatherDataModel
import com.example.weatherpictureapp.domain.in_arguments.LatLng
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
	private val api: WeatherApi
) : BaseWeatherRemoteDataSource(api) {

	override fun getWeatherDataByCoordinates(
		location: LatLng
	): Flow<NetworkResult<WeatherDataModel>> {
		return flow {
			emit(NetworkResult.Loading)
			val response = api.getWeatherByLocationCoordinates(location.latitude, location.longitude)
			if (response.isSuccessful && response.body() != null) {
				emit(NetworkResult.Success(response.body()!!))
			} else {
				throw Exception(response.message())
			}
		}
	}
}
