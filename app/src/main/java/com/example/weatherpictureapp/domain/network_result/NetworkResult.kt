package com.example.weatherpictureapp.domain.network_result

import com.example.weatherpictureapp.core.util.error_handling.AppError

sealed class NetworkResult<out T> {
	data class Success<out R>(val value: R) : NetworkResult<R>()
	data class Error(val error: AppError) : NetworkResult<Nothing>()
	object Loading : NetworkResult<Nothing>()
}
