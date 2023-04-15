package com.example.weatherpictureapp.core.util.error_handling

data class AppError(
	val code: Int? = null,
	val message: String?,
	val type: ErrorType,
	val throwable: Throwable? = null
)
