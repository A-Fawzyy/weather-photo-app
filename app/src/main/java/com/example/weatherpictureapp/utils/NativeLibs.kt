package com.example.weatherpictureapp.utils

class NativeLibs {
	init {
		System.loadLibrary("weatherpictureapp")
	}



	external fun apiKey(): String
}
