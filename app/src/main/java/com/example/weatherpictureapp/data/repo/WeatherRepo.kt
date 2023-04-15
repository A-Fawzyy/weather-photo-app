package com.example.weatherpictureapp.data.repo

import com.example.weatherpictureapp.data.base_datasource.BaseWeatherRemoteDataSource
import com.example.weatherpictureapp.data.datasource.WeatherRemoteDataSource
import com.example.weatherpictureapp.data.mappers.toDomain
import com.example.weatherpictureapp.domain.base_repo.BaseWeatherRepo
import com.example.weatherpictureapp.domain.domain_model.WeatherDomainModel
import com.example.weatherpictureapp.domain.in_arguments.LatLng
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepo @Inject constructor(
	private val weatherRemoteDataSource: BaseWeatherRemoteDataSource
) : BaseWeatherRepo(weatherRemoteDataSource) {

	override fun getWeatherData(
		location: LatLng,
	): Flow<NetworkResult<WeatherDomainModel>> {
		return weatherRemoteDataSource.getWeatherDataByCoordinates(location)
			.map { networkResult ->
				when (networkResult) {
					is NetworkResult.Success -> {
						NetworkResult.Success(networkResult.value.toDomain())
					}
					is NetworkResult.Error -> {
						NetworkResult.Error(networkResult.error)
					}
					is NetworkResult.Loading -> {
						NetworkResult.Loading
					}
				}
			}
	}
}
