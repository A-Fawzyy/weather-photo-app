package com.example.weatherpictureapp.presentation.add_weather_photo

import android.content.ContentValues
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
import androidx.lifecycle.ViewModelProvider
import com.example.weatherpictureapp.databinding.FragmentAddWeatherPhotoBinding
import com.example.weatherpictureapp.presentation.history_list.HistoryListViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AddWeatherPhotoFragment : Fragment() {

	private var _binding: FragmentAddWeatherPhotoBinding? = null
	private val binding get() = _binding!!


	private var imageCapture: ImageCapture? = null

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = FragmentAddWeatherPhotoBinding.inflate(inflater, container, false)

		val viewModel =
			ViewModelProvider(this)[HistoryListViewModel::class.java]



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
				put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
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
				}
			}
		)
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
		private const val TAG = "AddPhotoWeatherFragment"
	}
}
