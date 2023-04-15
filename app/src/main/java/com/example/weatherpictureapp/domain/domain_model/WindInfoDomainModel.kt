package com.example.weatherpictureapp.domain.domain_model

import com.google.gson.annotations.SerializedName


data class WindInfoDomainModel(
	@SerializedName("speed")
	var speed: Double? = null,
	@SerializedName("deg")
	var degree: Int? = null
)
