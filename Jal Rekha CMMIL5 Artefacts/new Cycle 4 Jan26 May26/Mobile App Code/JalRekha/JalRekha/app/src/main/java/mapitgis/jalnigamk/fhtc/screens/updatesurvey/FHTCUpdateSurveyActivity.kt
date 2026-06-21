package mapitgis.jalnigamk.fhtc.screens.updatesurvey

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mapitgis.jalnigam.R
import mapitgis.jalnigam.databinding.ActivityFhtcsurveyListBinding
import mapitgis.jalnigam.databinding.ActivityFhtcupdateSurveyBinding
import mapitgis.jalnigam.nirmal.camera.ViewImageActivity
import mapitgis.jalnigamk.base.BaseKActivity
import mapitgis.jalnigamk.camera.CameraActivity
import mapitgis.jalnigamk.fhtc.database.table.FHTCNICSurveyInfo
import mapitgis.jalnigamk.fhtc.screens.surveylist.FHTCSurveyListAdapter
import mapitgis.jalnigamk.fhtc.screens.surveylist.FHTCSurveyListVM
import mapitgis.jalnigamk.location.GeofenceUtil
import mapitgis.jalnigamk.util.BitmapUtil
import mapitgis.jalnigamk.util.MySupportMapFragment
import kotlin.getValue

class FHTCUpdateSurveyActivity : BaseKActivity<FHTCUpdateSurveyVM>(){


    override val viewModel: FHTCUpdateSurveyVM by viewModels()

    private val binding: ActivityFhtcupdateSurveyBinding by lazy {
        ActivityFhtcupdateSurveyBinding.inflate(layoutInflater)
    }

    private lateinit var googleMap: GoogleMap

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.initLocationRequest(baseContext)
        setupToolbar(binding.appbar.toolbar)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        setupMap()

        binding.btnDirection.setOnClickListener {
            viewModel.previousSurveyLatLng?.let {
                openDefaultGoogleMap(it.latitude, it.longitude)
            }
        }

        binding.btnCapture.setOnClickListener {
            viewModel.geoLocation.value?.let {
                cameraLauncher.launch(Intent(this, CameraActivity::class.java))
            } ?: showToast("Searching for location")
        }

        binding.tapConnectionImage.setOnClickListener {view ->
            view.tag?.let { ViewImageActivity.viewImage(baseContext,it.toString()) }
        }

        binding.btnSubmit.setOnClickListener { performSubmitEvent() }

        val surveyInfo = intent.getParcelableExtra("surveyInfo", FHTCNICSurveyInfo::class.java)
        surveyInfo?.let { viewModel.setSurveyInfo(it) }

        viewModel.geoLocation.observe(this){
            it?.let { showLocationOnMap(it) }
        }
    }


    private fun setupMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? MySupportMapFragment
        mapFragment?.getMapAsync{ map ->
            googleMap = map
            googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            enableMapCurrentLocation()
            //show existing survey location market
            addLastSurveyLocationMarker()
        }
    }





    private fun showLocationOnMap(location: Location){
        val latLng = LatLng(location.latitude, location.longitude)
        //showing user location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18f))
    }

    override fun onResume() {
        super.onResume()
        checkLocationPermission()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLocationUpdates()
    }


    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            startLocationUpdate()
        } else {
            // Request permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startLocationUpdate() // Permission granted, access location
                enableMapCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }


    private fun startLocationUpdate(){
        viewModel.startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun enableMapCurrentLocation(){
        if(viewModel.isLocationPermissionGranted) {
            googleMap.isMyLocationEnabled = true
        }
    }


    private fun addLastSurveyLocationMarker(){
        val surveyInfo = viewModel.surveyInfo.value
        surveyInfo?.let {
            if(viewModel.previousSurveyLatLng!=null){
                googleMap.addMarker(
                    MarkerOptions()
                        .position(viewModel.previousSurveyLatLng!!)
                        .title(surveyInfo.nameEn ?: "Unknown")
                        .snippet("${surveyInfo.villageName}-${surveyInfo.blockName}-${surveyInfo.districtName}")
                )
            }
        }
    }

    private fun openDefaultGoogleMap(destinationLat: Double, destinationLng: Double){
        val gmmIntentUri = Uri.parse("google.navigation:q=$destinationLat,$destinationLng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            // Fallback: open in browser if Google Maps app not installed
            val browserUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destinationLat,$destinationLng")
            startActivity(Intent(Intent.ACTION_VIEW, browserUri))
        }
    }


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val uriString = result.data?.getStringExtra(CameraActivity.KEY_IMAGE)
            uriString?.let {
                val uri = Uri.parse(it)
                var bitmap = BitmapUtil.uriToBitmap(baseContext,uri,true)
                bitmap = bitmap?.let { it1 ->
                    BitmapUtil.drawTextOnBitmap(baseContext,it1,viewModel.getBitmapText())
                }

                bitmap?.let { _bitmap ->
                    BitmapUtil.deleteImage(baseContext,uriString)
                    val newImageUri = BitmapUtil.saveBitmapToStorage(baseContext,_bitmap)
                    if(newImageUri!=null){
                        Glide.with(this).load(newImageUri).into(binding.tapConnectionImage)
                        binding.tapConnectionImage.tag = newImageUri.toString()
                    }
                }
            }
        }
    }



    private fun performSubmitEvent(){
        if(viewModel.geoLocation.value==null){
            showToast("Searching for location...")
            return
        }

        if(binding.tapConnectionImage.tag==null){
            showToast("Please capture image")
            return
        }

        viewModel.performSubmitEvent(binding.etRemark.getFieldValue(),
            binding.tapConnectionImage.tag.toString())
    }


}