package com.example.weatherpictureapp.domain.use_case

import com.example.weatherpictureapp.core.BaseUseCase
import com.example.weatherpictureapp.data.repo.WeatherRepo
import com.example.weatherpictureapp.domain.base_repo.BaseWeatherRepo
import com.example.weatherpictureapp.domain.domain_model.WeatherDomainModel
import com.example.weatherpictureapp.domain.in_arguments.LatLng
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BaseGetWeatherByLocationUseCase : BaseUseCase<Flow<NetworkResult<WeatherDomainModel>>, LatLng>


class GetWeatherByLocationUseCase @Inject constructor(
	private val repo: BaseWeatherRepo
) : BaseGetWeatherByLocationUseCase {

	override fun invoke(params: LatLng): Flow<NetworkResult<WeatherDomainModel>> {
		return repo.getWeatherData(params)
	}
}

