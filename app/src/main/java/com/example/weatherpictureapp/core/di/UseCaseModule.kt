package com.example.weatherpictureapp.core.di

import com.example.weatherpictureapp.domain.base_repo.BaseWeatherRepo
import com.example.weatherpictureapp.domain.use_case.BaseGetWeatherByLocationUseCase
import com.example.weatherpictureapp.domain.use_case.GetWeatherByLocationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

	@Provides
	@Singleton
	@Named(HiltNamedKeys.GET_WEATHER_BY_LOCATION_USE_CASE_KEY)
	fun providesWeatherUseCase(
		weatherRepo: BaseWeatherRepo,
	): BaseGetWeatherByLocationUseCase = GetWeatherByLocationUseCase(repo = weatherRepo)
}
