package com.example.weatherpictureapp.domain.use_case

import com.example.weatherpictureapp.core.BaseUseCase
import com.example.weatherpictureapp.domain.base_repo.BaseWeatherPhotoRepo
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BaseSaveWeatherPhotoUseCase : BaseUseCase<Flow<NetworkResult<Nothing?>>, String>


class SaveWeatherPhotoUseCase @Inject constructor(
	private val repo: BaseWeatherPhotoRepo
) : BaseSaveWeatherPhotoUseCase {

	override fun invoke(params: String): Flow<NetworkResult<Nothing?>> {
		return repo.saveWeatherPhotoUri(params)
	}


}

