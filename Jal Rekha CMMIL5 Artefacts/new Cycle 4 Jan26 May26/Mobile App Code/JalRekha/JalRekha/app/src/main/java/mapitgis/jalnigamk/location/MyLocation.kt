package mapitgis.jalnigamk.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import java.io.IOException
import java.util.Locale

/**
 * Utility class for easy access to the device location
 */
class MyLocation(
    private val context: Context,
    private val locationRequest: LocationRequest? = null,
    private val requireLastLocation: Boolean,
    private var mListener: MLocListener
) {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private var locationCallback: LocationCallback? = null
    private var mBlurRadius = 0

    init {
        if (requireLastLocation) fetchLastKnownLocation()
    }


    fun hasLocationEnabled(): Boolean {
        return try {
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } else {
                @Suppress("DEPRECATION")
                Settings.Secure.getString(context.contentResolver, Settings.Secure.LOCATION_PROVIDERS_ALLOWED)?.isNotEmpty() == true
            }
            mode != Settings.Secure.LOCATION_MODE_OFF
        } catch (e: Exception) {
            false
        }
    }

    fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun startLocation() {
        checkLocationSetting()
    }

    @SuppressLint("MissingPermission")
    private fun beginUpdates() {
        if (!isLocationPermissionGranted()) return

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                /*locationResult.locations.firstOrNull()?.let {
                    mListener.currentLocation(it)
                }*/
                locationResult.locations.firstOrNull()?.let {
                    mListener.currentLocation(it)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest ?: defaultLocationRequest(),
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun endUpdates() {
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }

    private fun defaultLocationRequest() = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .setMinUpdateIntervalMillis(2000)
        .setWaitForAccurateLocation(true)
        .setMaxUpdateDelayMillis(5000)
        .setMinUpdateDistanceMeters(5f)
        .build()

    @SuppressLint("MissingPermission")
    private fun fetchLastKnownLocation() {
        if (!isLocationPermissionGranted()) return

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let { mListener.currentLocation(it) } ?: run {
                checkLocationSetting()
                beginUpdates()
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        if (!isLocationPermissionGranted()) {
            mListener.locationCancelled()
            return
        }

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mListener.currentLocation(location)
                } else {
                    mListener.locationCancelled()
                }
            }
            .addOnFailureListener {
                mListener.locationCancelled()
            }
    }


    private fun checkLocationSetting() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest ?: defaultLocationRequest()).setAlwaysShow(true)

        LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())
            .addOnSuccessListener { beginUpdates() }
            .addOnFailureListener { e ->
                if (e is ResolvableApiException) {
                    try {
                        e.startResolutionForResult(context as Activity, LOCATION_SETTING_REQUEST_CODE)
                    } catch (sendEx: Exception) {
                        sendEx.printStackTrace()
                    }
                }
            }
    }

    fun getAddress(location: Location): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()?.getAddressLine(0)
        } catch (e: IOException) {
            null
        }
    }

    fun onActivityResult(result: Int) {
        if (result == Activity.RESULT_OK) {
            mListener.locationOn()
            beginUpdates()
        } else if (result == Activity.RESULT_CANCELED) {
            mListener.locationCancelled()
        }
    }

    fun setBlurRadius(blurRadius: Int) {
        mBlurRadius = blurRadius
    }


    fun getDistanceDisplay(
        currentLat: Double, currentLng: Double,
        destLat: Double, destLng: Double
    ): String {
        val startLocation = Location("start").apply {
            latitude = currentLat
            longitude = currentLng
        }

        val endLocation = Location("end").apply {
            latitude = destLat
            longitude = destLng
        }

        val distanceMeters = startLocation.distanceTo(endLocation) // in meters

        val distanceText = if (distanceMeters < 1000) {
            "${distanceMeters.toInt()} m"
        } else {
            String.format("%.1f km", distanceMeters / 1000)
        }
        return "You are $distanceText away from the location"
    }


    companion object {
        const val LOCATION_SETTING_REQUEST_CODE = 5

        fun openSettings(context: Context) {
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

        fun getAddress(context: Context, latitude: Double, longitude: Double, country: Boolean, fullAddress: Boolean): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            return try {
                geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()?.let { address ->
                    when {
                        country -> address.countryName ?: ""
                        fullAddress -> listOfNotNull(
                            address.featureName,
                            address.subLocality,
                            address.subAdminArea,
                            address.postalCode,
                            address.countryName
                        ).joinToString(", ")
                        else -> address.locality ?: ""
                    }
                } ?: ""
            } catch (e: IOException) {
                ""
            }
        }
    }
}
