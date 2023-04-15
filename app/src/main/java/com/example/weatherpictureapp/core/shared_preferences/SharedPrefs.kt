package com.example.weatherpictureapp.core.shared_preferences

import android.content.SharedPreferences
import javax.inject.Inject

class SharedPrefs @Inject constructor(
	private val prefs: SharedPreferences
) : BaseSharedPrefs {
	override fun saveString(key: String, value: String) {
		prefs.edit().putString(key, value).apply()
	}

	override fun getString(key: String): String {
		return prefs.getString(key, "") ?: ""
	}

	override fun saveInt(key: String, value: Int) {
		prefs.edit().putInt(key, value).apply()
	}

	override fun getInt(key: String): Int {
		return prefs.getInt(key, 0)
	}

	override fun saveBoolean(key: String, value: Boolean) {
		prefs.edit().putBoolean(key, value).apply()
	}

	override fun getBoolean(key: String): Boolean {
		return prefs.getBoolean(key, false)
	}

	override fun saveFloat(key: String, value: Float) {
		prefs.edit().putFloat(key, value).apply()
	}

	override fun getFloat(key: String): Float {
		return prefs.getFloat(key, 0f)
	}

	override fun saveLong(key: String, value: Long) {
		prefs.edit().putLong(key, value).apply()
	}

	override fun getLong(key: String): Long {
		return prefs.getLong(key, 0L)
	}

	override fun remove(key: String) {
		prefs.edit().remove(key).apply()
	}

	override fun clear() {
		prefs.edit().clear().apply()
	}

}
