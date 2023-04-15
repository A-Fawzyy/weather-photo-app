package com.example.weatherpictureapp.presentation.create_weather_photo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.weatherpictureapp.R
import com.example.weatherpictureapp.core.util.OpenWeatherConstants
import com.example.weatherpictureapp.databinding.FragmentPhotoWeatherBinding
import com.example.weatherpictureapp.domain.domain_model.WeatherDomainModel
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import com.example.weatherpictureapp.util.captureView
import com.example.weatherpictureapp.util.loadImage
import com.example.weatherpictureapp.util.saveImageToStorage
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class PhotoWeatherFragment : Fragment() {

	private lateinit var viewModel: PhotoWeatherViewModel

	private var _binding: FragmentPhotoWeatherBinding? = null
	private val binding get() = _binding!!

	private val safeArgs: PhotoWeatherFragmentArgs by navArgs()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentPhotoWeatherBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel = ViewModelProvider(this)[PhotoWeatherViewModel::class.java]
		viewModel.getWeatherData(
			safeArgs.lastKnownLocation.longitude, safeArgs.lastKnownLocation.latitude
		)

		binding.fabShareImage.setOnClickListener {
			captureAndShareWeatherPhoto()
		}

		binding.fabSaveImage.setOnClickListener {
			captureAndShareWeatherPhoto(true)
		}

		collectStates()
	}

	private fun captureAndShareWeatherPhoto(shouldSave: Boolean = false) {
		binding.fabShareImage.isVisible = false
		binding.constraintLayoutWeatherPhoto.captureView().let { bitmap ->
			saveBitmap(bitmap) {
				if(shouldSave) {
					viewModel.saveWeatherPhoto(it)
				} else {
					shareImage(it, requireContext())
				}
			}
			binding.fabShareImage.isVisible = true
		}
	}

	private fun saveBitmap(bitmap: Bitmap, callBack: (File) -> Unit) {
		lifecycleScope.launch(Dispatchers.IO) {
			safeArgs.photoUri?.let {
				saveImageToStorage(bitmap, requireContext(), it) { file ->
					callBack(file)
				}
			}
		}
	}

	private fun shareImage(file: File, context: Context) {
		val fileUri = FileProvider.getUriForFile(
			context,
			context.applicationContext.packageName + ".provider", //(use your app signature + ".provider" )
			file
		)

		val shareIntent = Intent(Intent.ACTION_SEND)
			.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			.setType("image/jpeg")
			.putExtra(Intent.EXTRA_STREAM, fileUri)
		val chooser = Intent.createChooser(shareIntent, getString(R.string.share_image))
		val resInfoList: List<ResolveInfo> =
			context.packageManager.queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

		for (resolveInfo in resInfoList) {
			val packageName = resolveInfo.activityInfo.packageName
			context.grantUriPermission(
				packageName,
				fileUri,
				Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
			)
		}
		context.startActivity(chooser)

	}


	private fun collectStates() {
		binding.apply {
			viewLifecycleOwner.lifecycleScope.launch {
				viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.weatherData.collect { data ->
						when (data) {
							is NetworkResult.Success -> {
								progressIndicator.isVisible = false
								groupWeatherInfo.isVisible = true
								bindWeatherData(data)

							}

							is NetworkResult.Error -> {
								binding.progressIndicator.isVisible = false
								groupWeatherInfo.isVisible = false
								Snackbar.make(
									binding.root,
									data.error.message
										?: getString(R.string.unexpected_error_message),
									Snackbar.LENGTH_INDEFINITE
								).apply {
									show()
								}
							}

							is NetworkResult.Loading -> {
								groupWeatherInfo.isVisible = false
								binding.progressIndicator.isVisible = true
							}
						}
					}
				}
			}
		}
	}

	private fun FragmentPhotoWeatherBinding.bindWeatherData(data: NetworkResult.Success<WeatherDomainModel>) {
		imageViewWeatherPhotoBackground.setImageURI(safeArgs.photoUri)
		imageViewStatusIcon.loadImage(
			OpenWeatherConstants.getImageUrl(data.value.weatherStatus?.get(0)?.icon)
		)
		textViewStatus.text = data.value.weatherStatus?.get(0)?.status
		textViewStatusDescription.text = data.value.weatherStatus?.get(0)?.description
		textViewFeelsLikeTemperature.text = "${data.value.weatherData?.feelsLike ?: 27.0}"
		textViewTemperature.text = "${data.value.weatherData?.temprature ?: 27.0}"
	}
}
