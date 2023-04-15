package com.example.weatherpictureapp.core.di

import com.example.weatherpictureapp.core.api_services.WeatherApi
import com.example.weatherpictureapp.core.shared_preferences.SharedPreferencesClient
import com.example.weatherpictureapp.core.util.error_handling.ErrorMapper
import com.example.weatherpictureapp.data.base_datasource.BaseWeatherPhotoLocalDataSource
import com.example.weatherpictureapp.data.base_datasource.BaseWeatherRemoteDataSource
import com.example.weatherpictureapp.data.datasource.WeatherPhotoLocalDataSource
import com.example.weatherpictureapp.data.datasource.WeatherRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

	@Provides
	@Singleton
	fun providesWeatherRemoteDataSource(
		api: WeatherApi,
	): BaseWeatherRemoteDataSource = WeatherRemoteDataSource(api = api)

	@Provides
	@Singleton
	fun providesWeatherPhotoLocalDataSource(
		prefsClient: SharedPreferencesClient,
	): BaseWeatherPhotoLocalDataSource = WeatherPhotoLocalDataSource(prefsClient = prefsClient)
}
