package com.example.weatherpictureapp.presentation.history_list

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpictureapp.databinding.WeatherPhotoListItemBinding
import java.net.URI

class WeatherPhotoAdapter(private val imageUris: List<String>) :
	RecyclerView.Adapter<WeatherPhotoAdapter.WeatherPhotoViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherPhotoViewHolder {
		val binding =
			WeatherPhotoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return WeatherPhotoViewHolder(binding)
	}

	override fun onBindViewHolder(holder: WeatherPhotoViewHolder, position: Int) {
		val currentItem = imageUris[position]
		holder.bind(currentItem)
	}

	override fun getItemCount(): Int {
		return imageUris.size
	}

	inner class WeatherPhotoViewHolder(private val binding: WeatherPhotoListItemBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bind(imageUri: String) {
			binding.imageViewWeatherPhoto.setImageURI(Uri.parse(imageUri))
		}
	}
}


