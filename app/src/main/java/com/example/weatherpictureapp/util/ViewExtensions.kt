package com.example.weatherpictureapp.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.View

/**
 * Returns the bitmap of the view provided
 */
fun View.captureView(): Bitmap {
	//creates an empty bitmap that has the same size of the view provided
	val imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
	val canvas = Canvas(imageBitmap)
	this.draw(canvas) // draws the container onto the canvas which uses in the emptyBitmap
	return imageBitmap

}
