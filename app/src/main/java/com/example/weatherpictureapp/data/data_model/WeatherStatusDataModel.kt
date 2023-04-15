package com.example.weatherpictureapp.data.data_model

import com.google.gson.annotations.SerializedName


data class WeatherStatusDataModel(

	@SerializedName("id")
	var id: Int? = null,
	@SerializedName("main")
	var status: String? = null,
	@SerializedName("description")
	var description: String? = null,
	@SerializedName("icon")
	var icon: String? = null

)
