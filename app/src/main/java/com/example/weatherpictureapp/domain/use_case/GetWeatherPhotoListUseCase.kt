package com.example.weatherpictureapp.domain.use_case

import com.example.weatherpictureapp.core.BaseUseCase
import com.example.weatherpictureapp.domain.base_repo.BaseWeatherPhotoRepo
import com.example.weatherpictureapp.domain.base_repo.BaseWeatherRepo
import com.example.weatherpictureapp.domain.domain_model.WeatherDomainModel
import com.example.weatherpictureapp.domain.in_arguments.LatLng
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BaseGetWeatherPhotoListUseCase : BaseUseCase<Flow<NetworkResult<List<String>>>, Nothing?>


class GetWeatherPhotoListUseCase @Inject constructor(
	private val repo: BaseWeatherPhotoRepo
) : BaseGetWeatherPhotoListUseCase {
	override fun invoke(params: Nothing?): Flow<NetworkResult<List<String>>> {
		return repo.getWeatherPhotoUrisList()
	}

}

