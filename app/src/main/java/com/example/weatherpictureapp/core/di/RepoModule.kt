package com.example.weatherpictureapp.core.di

import com.example.weatherpictureapp.core.api_services.WeatherApi
import com.example.weatherpictureapp.data.base_datasource.BaseWeatherRemoteDataSource
import com.example.weatherpictureapp.data.datasource.WeatherRemoteDataSource
import com.example.weatherpictureapp.data.repo.WeatherRepo
import com.example.weatherpictureapp.domain.base_repo.BaseWeatherRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

	@Provides
	@Singleton
	fun providesWeatherRepository(
		weatherRemoteDataSource: BaseWeatherRemoteDataSource,
	): BaseWeatherRepo = WeatherRepo(weatherRemoteDataSource = weatherRemoteDataSource)
}
