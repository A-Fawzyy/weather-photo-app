package com.example.weatherpictureapp.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(
	url: String,
	error: Drawable? = null
) {
	Glide.with(this)
		.load(url)
		.error(error)
		.into(this)
}
