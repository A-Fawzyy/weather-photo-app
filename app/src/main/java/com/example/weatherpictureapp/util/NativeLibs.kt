package com.example.weatherpictureapp.util

class NativeLibs {
	init {
		System.loadLibrary("weatherpictureapp")
	}



	external fun apiKey(): String
}
