package com.example.weatherpictureapp.core.di

import com.example.weatherpictureapp.BuildConfig
import com.example.weatherpictureapp.core.api_services.WeatherApi
import com.example.weatherpictureapp.core.util.OpenWeatherConstants
import com.example.weatherpictureapp.core.util.error_handling.DefaultErrorMapper
import com.example.weatherpictureapp.core.util.error_handling.ErrorMapper
import com.example.weatherpictureapp.util.NativeLibs
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiServiceModule {
	@Provides
	@Singleton
	@Named(HiltNamedKeys.DEFAULT_ERROR_MAPPER_KEY)
	fun provideDefaultErrorMapper(): ErrorMapper = DefaultErrorMapper()

	@Provides
	@Singleton
	@Named(HiltNamedKeys.BASE_URL_KEY)
	fun provideBaseUrl() = OpenWeatherConstants.BASE_URL

	@Provides
	@Singleton
	@Named(HiltNamedKeys.TIME_OUT_KEY)
	fun provideConnectionTimeout(): Long = 60L

	@Provides
	@Singleton
	fun provideGson(): Gson = GsonBuilder().setLenient().create()

	@Provides
	@Singleton
	@Named(HiltNamedKeys.API_KEY)
	fun provideApiKey() = NativeLibs().apiKey()

	@Singleton
	@Provides
	fun provideOkHttpClient(
		loggingInterceptor: HttpLoggingInterceptor,
		apiKeyRequestInterceptor: Interceptor,
		@Named(HiltNamedKeys.TIME_OUT_KEY) timeout: Long
	): OkHttpClient {
		val okHttpClientBuilder = OkHttpClient.Builder()
		if (BuildConfig.DEBUG) {
			okHttpClientBuilder.addInterceptor(loggingInterceptor)
		}

		okHttpClientBuilder.addInterceptor(apiKeyRequestInterceptor)
			.callTimeout(timeout, TimeUnit.SECONDS).connectTimeout(timeout, TimeUnit.SECONDS)
			.readTimeout(timeout, TimeUnit.SECONDS).writeTimeout(timeout, TimeUnit.SECONDS)
		return okHttpClientBuilder.build()
	}

	@Provides
	@Singleton
	fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
		setLevel(HttpLoggingInterceptor.Level.HEADERS)
		setLevel(HttpLoggingInterceptor.Level.BODY)
	}

	@Provides
	@Singleton
	fun provideApiKeyRequestInterceptor(
		@Named(HiltNamedKeys.API_KEY) apiKey: String,
	) = Interceptor { chain ->
		val url = chain.request().url.newBuilder()
			.addQueryParameter(OpenWeatherConstants.APP_ID_QUERY_PARAMETER_KEY, apiKey).build()

		val request = chain.request().newBuilder().url(url).build()
		return@Interceptor chain.proceed(request)
	}

	@Provides
	@Singleton
	fun provideWeatherApi(
		@Named(HiltNamedKeys.BASE_URL_KEY) baseUrl: String, gson: Gson, client: OkHttpClient
	): WeatherApi = Retrofit.Builder().baseUrl(baseUrl).client(client)
		.addConverterFactory(GsonConverterFactory.create(gson)).build()
		.create(WeatherApi::class.java)
}
