package com.example.weatherpictureapp.data.mappers

import com.example.weatherpictureapp.data.data_model.WeatherDataModel
import com.example.weatherpictureapp.data.data_model.WeatherInfoDataModel
import com.example.weatherpictureapp.data.data_model.WeatherStatusDataModel
import com.example.weatherpictureapp.data.data_model.WindInfoDataModel

fun WeatherDataModel.toData(): WeatherDataModel {
	return WeatherDataModel(
		weatherStatus = weatherStatus?.map { it.toData() },
		weatherData = weatherData?.toData(),
		wind = wind?.toData(),
		visibility = visibility
	)
}

fun WeatherInfoDataModel.toData(): WeatherInfoDataModel {
	return WeatherInfoDataModel(
		temprature = temprature,
		feelsLike = feelsLike,
		minTemperature = minTemperature,
		maxTemperature = maxTemperature,
		pressure = pressure,
		humidity = humidity
	)

}

fun WeatherStatusDataModel.toData(): WeatherStatusDataModel {
	return WeatherStatusDataModel(
		id = id,
		status = status,
		description = description,
		icon = icon
	)

}

fun WindInfoDataModel.toData(): WindInfoDataModel {
	return WindInfoDataModel(
		speed = speed,
		degree = degree
	)
}
