package com.example.weatherpictureapp.presentation.create_weather_photo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherpictureapp.core.di.HiltNamedKeys
import com.example.weatherpictureapp.core.util.error_handling.AppError
import com.example.weatherpictureapp.core.util.error_handling.DefaultErrorMapper
import com.example.weatherpictureapp.core.util.error_handling.ErrorMapper
import com.example.weatherpictureapp.domain.domain_model.WeatherDomainModel
import com.example.weatherpictureapp.domain.in_arguments.LatLng
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import com.example.weatherpictureapp.domain.use_case.BaseGetWeatherByLocationUseCase
import com.example.weatherpictureapp.domain.use_case.BaseSaveWeatherPhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PhotoWeatherViewModel @Inject constructor(
	@Named(HiltNamedKeys.GET_WEATHER_BY_LOCATION_USE_CASE_KEY)
	private val getWeatherByLocationUseCase: BaseGetWeatherByLocationUseCase,
	@Named(HiltNamedKeys.SAVE_WEATHER_PHOTO_USE_CASE_KEY)
	private val saveWeatherPhotoUseCase: BaseSaveWeatherPhotoUseCase,
	@Named(HiltNamedKeys.DEFAULT_ERROR_MAPPER_KEY)
	private val errorMapper: ErrorMapper
) : ViewModel() {

	private val _weatherData = MutableStateFlow<NetworkResult<WeatherDomainModel>>(NetworkResult.Loading)
	val weatherData: StateFlow<NetworkResult<WeatherDomainModel>> = _weatherData

	private val _savePhotoState = MutableStateFlow<NetworkResult<Nothing?>>(NetworkResult.Loading)
	val savePhotoState: StateFlow<NetworkResult<Nothing?>> = _savePhotoState

	private val _errorData = MutableStateFlow<AppError?>(null)
	val errorData: StateFlow<AppError?> = _errorData

	fun getWeatherData(latitude: Double, longitude: Double) {
		viewModelScope.launch {
			getWeatherByLocationUseCase(LatLng(latitude, longitude))
				.catch { throwable ->
					_errorData.value = errorMapper.map(throwable)
					_weatherData.value = NetworkResult.Error(_errorData.value!!)
				}
				.collect { data ->
					_weatherData.value = data
				}
		}
	}

	fun saveWeatherPhoto(file: File) {
		viewModelScope.launch {
			saveWeatherPhotoUseCase(file.absolutePath)
				.catch { throwable ->
					_errorData.value = errorMapper.map(throwable)
					_savePhotoState.value = NetworkResult.Error(_errorData.value!!)
				}
				.collect { data ->
					_savePhotoState.value = data
				}
		}
	}
}

