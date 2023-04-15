package com.example.weatherpictureapp.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.weatherpictureapp.core.shared_preferences.BaseSharedPrefs
import com.example.weatherpictureapp.core.shared_preferences.SharedPreferencesClient
import com.example.weatherpictureapp.core.shared_preferences.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

	@Singleton
	@Provides
	fun provideSharedPrefs(
		defaultSharedPreferences: SharedPreferences
	): BaseSharedPrefs {
		return SharedPrefs(defaultSharedPreferences)
	}

	@Singleton
	@Provides
	fun provideDefaultSharedPreferences(
		@ApplicationContext context: Context
	): SharedPreferences {
		return PreferenceManager.getDefaultSharedPreferences(context)
	}

	@Singleton
	@Provides
	fun provideSharedPreferencesClient(sharedPrefs: BaseSharedPrefs): SharedPreferencesClient {
		return SharedPreferencesClient(sharedPrefs)
	}
}
