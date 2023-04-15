package com.example.weatherpictureapp.core.util.error_handling

import okio.IOException
import retrofit2.HttpException

interface ErrorMapper {
    fun map(throwable: Throwable): AppError
}

class DefaultErrorMapper : ErrorMapper {
    override fun map(throwable: Throwable): AppError {
        return when (throwable) {
            is HttpException -> {
                val response = throwable.response()
                AppError(
                    code = response?.code(),
                    message = response?.message(),
                    type = ErrorType.SERVER
                )
            }
            is IOException -> {
                AppError(
                    message = throwable.message,
                    type = ErrorType.NETWORK,
                    throwable = throwable
                )
            }
            else -> {
                AppError(
                    message = throwable.message,
                    type = ErrorType.CLIENT,
                    throwable = throwable
                )
            }
        }
    }
}
