package com.example.weatherpictureapp.presentation.history_list

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.example.weatherpictureapp.R
import com.example.weatherpictureapp.databinding.FragmentHistoryListBinding
import com.example.weatherpictureapp.domain.network_result.NetworkResult
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryListFragment : Fragment() {

	private var _binding: FragmentHistoryListBinding? = null
	private val binding get() = _binding!!

	lateinit var viewModel : HistoryListViewModel

	private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
		LocationServices.getFusedLocationProviderClient(requireContext())
	}


	private val locationManager: LocationManager by lazy {
		requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {

		viewModel = ViewModelProvider(this)[HistoryListViewModel::class.java]

		_binding = FragmentHistoryListBinding.inflate(inflater, container, false)

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.fabAddWeatherPhoto.setOnClickListener {
			requestNeededPermissions()
		}

		viewModel.getWeatherPhotoList()

		collectState()
	}

	private fun collectState() {
		binding.apply {
			viewLifecycleOwner.lifecycleScope.launch {
				viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.weatherPhotosUriList.collect { data ->
						when (data) {
							is NetworkResult.Loading -> {
								progressIndicator.isVisible = true
							}

							is NetworkResult.Success -> {
								progressIndicator.isVisible = false
								recyclerViewHistory.adapter =
									WeatherPhotoAdapter(data.value)
							}

							is NetworkResult.Error -> {
								progressIndicator.isVisible = false
								Snackbar.make(
									binding.root,
									data.error.message
										?: getString(R.string.unexpected_error_message),
									Snackbar.LENGTH_INDEFINITE
								).apply {
									show()
								}
							}
						}
					}
				}
			}
		}
	}

	private fun requestNeededPermissions() {
		if (allPermissionsGranted()) {
			getLastKnownLocation(
				callback = { location ->
					Log.i(Tag, "lat: ${location.latitude},  long: ${location.longitude}")
					navigateToAddPhotoFragment(location)
				},
			)
		} else {
			requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
		}
	}

	private val requestPermissionLauncher =
		registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
			if (permissions.all { it.value }) {
				getLastKnownLocation(
					callback = { location ->
						navigateToAddPhotoFragment(location)
					},
				)
			} else {
				Toast.makeText(
					requireContext(), "Permissions not granted by the user.", Toast.LENGTH_SHORT
				).show()
			}
		}

	private fun navigateToAddPhotoFragment(location: Location) {
		requireView().findNavController().navigate(
			HistoryListFragmentDirections.actionNavHomeToAddPhotoFragment(location)
		)
	}

	@SuppressLint("MissingPermission")
	private fun getLastKnownLocation(
		callback: (Location) -> Unit,
	) {
		val isLocationEnabled = isLocationEnabled()
		if (!isLocationEnabled) {
			val locationRequest = LocationRequest.Builder(10000).build()

			val locationSettingsRequest: LocationSettingsRequest =
				LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build()

			val settingsClient = LocationServices.getSettingsClient(requireContext())

			settingsClient.checkLocationSettings(locationSettingsRequest).addOnSuccessListener {
				fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
					callback(location)
				}
			}.addOnFailureListener { exception ->
				if (exception is com.google.android.gms.common.api.ResolvableApiException) {
					navigateToSettingsToEnableLocation()
				}
			}


		} else {
			fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
				if (task.isSuccessful && task.result != null) {
					callback(task.result!!)
				} else {
					callback(Location("").apply{
						latitude = 25.0
						longitude = 25.0
					})
					Toast.makeText(
						requireContext(), "Unable to get last location", Toast.LENGTH_SHORT
					).show()
				}
			}
		}
	}

	private fun navigateToSettingsToEnableLocation() {
		Toast.makeText(
			requireContext(), getString(R.string.please_turn_on_location), Toast.LENGTH_SHORT
		).show()
		val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
		startActivity(intent)
	}

	private fun isLocationEnabled(): Boolean {
		return isLocationEnabled(locationManager)
	}

	private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
		ContextCompat.checkSelfPermission(
			requireContext(), it
		) == PackageManager.PERMISSION_GRANTED
	}


	companion object {
		private val REQUIRED_PERMISSIONS = mutableListOf(
			Manifest.permission.CAMERA,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
		).apply {
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
				add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
			}
		}.toTypedArray()
		private const val Tag = "HistoryListFragment"
	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
