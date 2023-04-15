package com.example.weatherpictureapp.presentation.history_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherpictureapp.core.di.HiltNamedKeys
import com.example.weatherpictureapp.core.util.error_handling.AppError
import com.example.weatherpictureapp.core.util.error_handling.ErrorMapper
import com.example.weatherpictureapp.domain.domain_model.WeatherDomainModel
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import com.example.weatherpictureapp.domain.use_case.BaseGetWeatherByLocationUseCase
import com.example.weatherpictureapp.domain.use_case.BaseGetWeatherPhotoListUseCase
import com.example.weatherpictureapp.domain.use_case.BaseSaveWeatherPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.net.URI
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HistoryListViewModel @Inject constructor(
	@Named(HiltNamedKeys.GET_WEATHER_PHOTO_LIST_USE_CASE_KEY)
	private val getWeatherPhotoListUseCase: BaseGetWeatherPhotoListUseCase,
	@Named(HiltNamedKeys.DEFAULT_ERROR_MAPPER_KEY)
	private val errorMapper: ErrorMapper
) : ViewModel() {



	private val _weatherPhotosUriList = MutableStateFlow<NetworkResult<List<String>>>(NetworkResult.Loading)
	val weatherPhotosUriList: StateFlow<NetworkResult<List<String>>> = _weatherPhotosUriList

	private val _errorData = MutableStateFlow<AppError?>(null)
	val errorData: StateFlow<AppError?> = _errorData

	fun getWeatherPhotoList() {
		viewModelScope.launch {
			getWeatherPhotoListUseCase(null)
				.catch { throwable ->
					_errorData.value = errorMapper.map(throwable)
					_weatherPhotosUriList.value = NetworkResult.Error(_errorData.value!!)
				}
				.collect { data ->
					_weatherPhotosUriList.value = data
				}
		}
	}



}
