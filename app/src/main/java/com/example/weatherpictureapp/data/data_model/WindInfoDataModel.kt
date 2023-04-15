package com.example.weatherpictureapp.data.data_model

import com.google.gson.annotations.SerializedName


data class WindInfoDataModel(
	@SerializedName("speed")
	var speed: Double? = null,
	@SerializedName("deg")
	var degree: Int? = null
)
