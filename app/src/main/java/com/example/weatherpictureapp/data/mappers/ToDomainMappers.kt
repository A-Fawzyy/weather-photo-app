package com.example.weatherpictureapp.data.mappers

import com.example.weatherpictureapp.data.data_model.WeatherDataModel
import com.example.weatherpictureapp.data.data_model.WeatherInfoDataModel
import com.example.weatherpictureapp.data.data_model.WeatherStatusDataModel
import com.example.weatherpictureapp.data.data_model.WindInfoDataModel
import com.example.weatherpictureapp.domain.domain_model.WeatherDomainModel
import com.example.weatherpictureapp.domain.domain_model.WeatherInfoDomainModel
import com.example.weatherpictureapp.domain.domain_model.WeatherStatusDomainModel
import com.example.weatherpictureapp.domain.domain_model.WindInfoDomainModel

fun WeatherDataModel.toDomain(): WeatherDomainModel {
	return WeatherDomainModel(
		weatherStatus = weatherStatus?.map { it.toDomain() },
		weatherData = weatherData?.toDomain(),
		wind = wind?.toDomain(),
		visibility = visibility
	)
}

fun WeatherInfoDataModel.toDomain(): WeatherInfoDomainModel {
	return WeatherInfoDomainModel(
		temprature = temprature,
		feelsLike = feelsLike,
		minTemperature = minTemperature,
		maxTemperature = maxTemperature,
		pressure = pressure,
		humidity = humidity
	)

}

fun WeatherStatusDataModel.toDomain(): WeatherStatusDomainModel {
	return WeatherStatusDomainModel(
		id = id,
		status = status,
		description = description,
		icon = icon
	)

}

fun WindInfoDataModel.toDomain(): WindInfoDomainModel {
	return WindInfoDomainModel(
		speed = speed,
		degree = degree
	)
}
