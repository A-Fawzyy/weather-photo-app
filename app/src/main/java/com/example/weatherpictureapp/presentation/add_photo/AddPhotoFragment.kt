package com.example.weatherpictureapp.presentation.add_photo

import android.content.ContentValues
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weatherpictureapp.databinding.FragmentAddPhotoBinding
import com.example.weatherpictureapp.presentation.weather_photo_app.WeatherPhotoApplication.Companion.WEATHER_PHOTO_FOLDER
import java.text.SimpleDateFormat
import java.util.Locale

class AddPhotoFragment : Fragment() {

	private var _binding: FragmentAddPhotoBinding? = null
	private val binding get() = _binding!!

	private val safeArgs: AddPhotoFragmentArgs by navArgs()


	private var imageCapture: ImageCapture? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentAddPhotoBinding.inflate(inflater, container, false)

		startCamera()
		binding.imageCaptureButton.setOnClickListener {
			takePhoto()
		}

		return binding.root
	}

	private fun takePhoto() {
		val imageCapture = imageCapture ?: return

		// Create time stamped name and MediaStore entry.
		val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
			.format(System.currentTimeMillis())
		val contentValues = ContentValues().apply {
			put(MediaStore.MediaColumns.DISPLAY_NAME, name)
			put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
			if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
				put(MediaStore.Images.Media.RELATIVE_PATH, WEATHER_PHOTO_FOLDER)
			}

		}

		val outputOptions = ImageCapture.OutputFileOptions
			.Builder(requireContext().contentResolver,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				contentValues)
			.build()

		imageCapture.takePicture(
			outputOptions,
			ContextCompat.getMainExecutor(requireContext()),
			object : ImageCapture.OnImageSavedCallback {
				override fun onError(exc: ImageCaptureException) {
					Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
				}

				override fun
						onImageSaved(output: ImageCapture.OutputFileResults){
					val msg = "Photo capture succeeded: ${output.savedUri}"
					Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
					Log.d(TAG, msg)
					navigateToPhotoWeatherFragment(safeArgs.lastKnownLocation, output.savedUri)
				}
			}
		)
	}

	private fun navigateToPhotoWeatherFragment(location: Location, savedUri: Uri?) {
		val direction = AddPhotoFragmentDirections.actionAddPhotoFragmentToPhotoWeatherFragment(
			location,
			savedUri
		)
		binding.root.findNavController().navigate(direction)
	}

	private fun startCamera() {
		val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

		cameraProviderFuture.addListener({
			val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

			// Preview
			val preview = Preview.Builder()
				.build()
				.apply {
					setSurfaceProvider(binding.viewFinder.surfaceProvider)
				}

			imageCapture = ImageCapture.Builder()
				.setJpegQuality(30)
				.build()

			val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

			try {
				cameraProvider.unbindAll()

				cameraProvider.bindToLifecycle(
					this, cameraSelector, preview, imageCapture)

			} catch(exc: Exception) {
				Log.e(TAG, "Use case binding failed", exc)
			}

		}, ContextCompat.getMainExecutor(requireContext()))
	}

	companion object {
		private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
		private const val TAG = "AddPhotoFragment"
	}
}
