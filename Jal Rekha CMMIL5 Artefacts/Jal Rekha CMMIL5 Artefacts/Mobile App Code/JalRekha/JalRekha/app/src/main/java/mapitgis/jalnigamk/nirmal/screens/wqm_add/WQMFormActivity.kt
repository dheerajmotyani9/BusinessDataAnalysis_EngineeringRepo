package mapitgis.jalnigamk.nirmal.screens.wqm_add

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.Geofence
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mapitgis.jalnigam.R
import mapitgis.jalnigam.databinding.ActivityNirmalWqmformBinding
import mapitgis.jalnigam.nirmal.camera.ViewImageActivity
import mapitgis.jalnigamk.nirmal.base.NirmalBaseActivity
import mapitgis.jalnigamk.camera.CameraActivity
import mapitgis.jalnigamk.nirmal.collection.WaterPointType
import mapitgis.jalnigamk.nirmal.database.dao.CombineSurveyTransaction
import mapitgis.jalnigamk.nirmal.database.dao.VillageDetails
import mapitgis.jalnigamk.nirmal.database.table.NirmalWQMEntity
import mapitgis.jalnigamk.location.GeofenceUtil
import mapitgis.jalnigamk.util.BitmapUtil
import mapitgis.jalnigamk.util.DecimalInputFilter
import mapitgis.jalnigamk.util.MySupportMapFragment


class WQMFormActivity : NirmalBaseActivity(), OnMapReadyCallback {

    companion object{
        fun pushActivity(context: Context, surveyTran: CombineSurveyTransaction){
            context.startActivity(Intent(context, WQMFormActivity::class.java).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("survey_transaction", surveyTran)
            })
        }
    }

    private var selectedImageView: View? = null
    private lateinit var googleMap: GoogleMap
    private var geofenceCircle: Circle? = null
    private var isUserInsideGeofence: Boolean? = null


    private val binding: ActivityNirmalWqmformBinding by lazy {
        ActivityNirmalWqmformBinding.inflate(layoutInflater)
    }

    private val viewModel: WQMFormVM by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        baseVM = viewModel
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        binding.lifecycleOwner = this
        binding.nirmalVM = viewModel
        viewModel.fetchValueFromIntent(intent)
        initView()

        viewModel.geoLocation.observe(this){
            it?.let { showLocationOnMap(it) }
        }

        viewModel.villageList.observe(this) { list ->
            list.isNotEmpty().let {
                val arrayList = list.toMutableList() as ArrayList<VillageDetails>
                arrayList.add(0, VillageDetails("-- Select --",""))
                binding.spinVillage.setAdapterData(arrayList, showPrompt = true)
            }
        }

        setupMap()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView(){
        binding.btnSampleImage.setOnClickListener {
            if(viewModel.geoLocation.value==null){
                showToast("Searching for location...")
                return@setOnClickListener
            }
            selectedImageView = binding.samplePhotoPreview
            cameraLauncher.launch(Intent(this, CameraActivity::class.java))
        }

        binding.btnFrcImage.setOnClickListener {
            if(viewModel.geoLocation.value==null){
                showToast("Searching for location...")
                return@setOnClickListener
            }
            selectedImageView = binding.frcPhotoPreview
            cameraLauncher.launch(Intent(this, CameraActivity::class.java))
        }

        binding.samplePhotoPreview.setOnClickListener { view ->
            view.tag?.let { ViewImageActivity.viewImage(baseContext,it.toString()) }
        }

        binding.frcPhotoPreview.setOnClickListener { view ->
            view.tag?.let { ViewImageActivity.viewImage(baseContext,it.toString()) }
        }

        binding.btnSubmit.setOnClickListener {
            if(validate()){
                binding.btnSubmit.isEnabled = false
                viewModel.insertWQM(createNirmalWQMEntity())
                finish()
            }
        }

        binding.etFrcResult.getEditText().filters = arrayOf(DecimalInputFilter())
    }


    private fun setupToolbar(){
        setSupportActionBar(binding.appbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                        if(selectedImageView==binding.samplePhotoPreview){
                            Glide.with(this).load(newImageUri).into(binding.samplePhotoPreview)
                            binding.samplePhotoPreview.tag = newImageUri.toString()
                            binding.sampleImgCard.visibility = View.VISIBLE
                        }else if(selectedImageView==binding.frcPhotoPreview){
                            Glide.with(this).load(newImageUri).into(binding.frcPhotoPreview)
                            binding.frcPhotoPreview.tag = newImageUri.toString()
                            binding.frcImgCard.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun createNirmalWQMEntity(): NirmalWQMEntity {
        val baseEntity = NirmalWQMEntity(
            sampleId = viewModel.waterSampleId,
            assignedSurveyId = viewModel.surveyTransaction.surveyId,
            assignDate = viewModel.surveyTransaction.activeDate,
            schemeName = viewModel.surveyTransaction.schName,
            schemeId = viewModel.surveyTransaction.schId.toInt(),
            collectionId = viewModel.collectionType.id,
            collectionType = viewModel.collectionType.value,
            latitude = viewModel.geoLocation.value?.latitude ?: 0.0,
            longitude = viewModel.geoLocation.value?.longitude ?: 0.0,
            accuracy = viewModel.geoLocation.value?.accuracy?:0.0f,
            remark = binding.etRemark.getFieldValue(),
            sampleImgFilePath = binding.samplePhotoPreview.tag.toString()
        )

        val entity = when (viewModel.collectionType) {
            WaterPointType.WTP_INLET -> baseEntity

            WaterPointType.WTP_OUTLET -> baseEntity.copy(
                frcImgFilePath = binding.frcPhotoPreview.tag.toString(),
                frcTestResult = binding.etFrcResult.getFieldValue()
            )

            WaterPointType.OHT_ESR -> baseEntity.copy(
                frcImgFilePath = binding.frcPhotoPreview.tag.toString(),
                frcTestResult = binding.etFrcResult.getFieldValue(),
                ohtId = viewModel.surveyTransaction.esrCode,
                ohtName = viewModel.surveyTransaction.esrName
            )

            WaterPointType.BULK_METER -> baseEntity.copy(
                frcImgFilePath = binding.frcPhotoPreview.tag.toString(),
                frcTestResult = binding.etFrcResult.getFieldValue(),
                instituteOtherName = viewModel.surveyTransaction.institute,
                contactPersonName = binding.etContactName.getFieldValue(),
                contactPersonMobile = binding.etContactMobile.getFieldValue(),
                contactPersonDesignation = binding.etContactDesignation.getFieldValue()
            )

            WaterPointType.BENEFICIARY -> baseEntity.copy(
                ohtId = viewModel.surveyTransaction.esrCode,
                ohtName = viewModel.surveyTransaction.esrName,
                villageName = (binding.spinVillage.getSelectedItem()as VillageDetails).villageName,
                villageId = (binding.spinVillage.getSelectedItem()as VillageDetails).villageCode.toInt(),
                beneficiaryName = binding.etBeniName.getFieldValue(),
                beneficiaryMobile = binding.etBeniMobile.getFieldValue(),
                beneficiarySamagraId = binding.etBeniSamagra.getFieldValue(),
                frcImgFilePath = binding.frcPhotoPreview.tag.toString(),
                frcTestResult = binding.etFrcResult.getFieldValue(),
            )
        }
        return entity
    }



    private fun validate(): Boolean {
        if (!viewModel.isLocationValid()) return false

        if(viewModel.isGeofenceValidationRequired()){
            if(!viewModel.isAssetLocationValid()){
                showToast("Asset location is not valid")
                return false
            }
            if(!viewModel.isUserInsideGeofence()){
                return false
            }
        }

        return when (viewModel.collectionType) {
            WaterPointType.WTP_INLET -> {
                isWaterSampleValid()
            }

            WaterPointType.WTP_OUTLET,
            WaterPointType.OHT_ESR -> {
                isWaterSampleValid() && isFRCSampleValid()
            }

            WaterPointType.BULK_METER -> {
                isWaterSampleValid() &&
                        isInstituePersonDetailsValid() &&
                        isFRCSampleValid()
            }

            WaterPointType.BENEFICIARY -> {
                isESRVillageValid() &&
                        isWaterSampleValid() &&
                        isBeneficiaryDetailsValid() &&
                        isFRCSampleValid()
            }
        }
    }




    //---------------------Location ---------------//
    private fun setupMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? MySupportMapFragment
        mapFragment?.getMapAsync(this)
        mapFragment?.setListener(object : MySupportMapFragment.OnTouchListener {
            override fun onTouch() {
                binding.nestedScrollview.requestDisallowInterceptTouchEvent(true)
            }
        })
    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        if(viewModel.isLocationPermissionGranted) {
            googleMap.isMyLocationEnabled = true
            if(viewModel.isGeofenceValidationRequired() && viewModel.isAssetLocationValid()){
                addGeofenceOnMap(LatLng(viewModel.surveyTransaction.assetLatitude!!, viewModel.surveyTransaction.assetLongitude!!),GeofenceUtil.GEOFENCE_RADIUS)
                //addGeofenceOnMap(LatLng(23.2436052,77.4298743),GeofenceUtil.GEOFENCE_RADIUS)
            }
        }
    }

    private fun showLocationOnMap(location: Location){
        val latLng = LatLng(location.latitude, location.longitude)
        if(viewModel.isGeofenceValidationRequired() && viewModel.isAssetLocationValid()){
           //showing asset location geofence
            updateGeofenceColor(latLng)
        }else{
            //showing user location
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18f))
        }
    }





    //--------------------validation message----------------//
    private fun isWaterSampleValid(): Boolean{
        if(binding.samplePhotoPreview.tag==null){
            showToast("Please capture sample photo")
            return false
        }
        return true
    }

    private fun isFRCSampleValid(): Boolean{
        if(binding.frcPhotoPreview.tag==null){
            showToast("Please capture FRC test photo")
            return false
        }

        if(binding.etFrcResult.isEmpty()){
            showToast("Please enter FRC test result")
            return false
        }
        return true
    }

    private fun isInstituePersonDetailsValid(): Boolean{
        if(binding.etContactName.isEmpty()){
            showToast("Enter contact person name")
            return false
        }
        if(binding.etContactMobile.isEmpty()){
            showToast("Enter contact person mobile")
            return false
        }

        if(!binding.etContactMobile.isValidMobileNumber()){
            showToast("Enter valid mobile number")
            return false
        }

        if(binding.etContactDesignation.isEmpty()){
            showToast("Enter contact person designation")
            return false
        }
        return true
    }

    private fun isESRVillageValid(): Boolean{
        if(binding.spinVillage.getSelectedPosition()==0){
            showToast("Please select village")
            return false
        }
        return true
    }

    private fun isBeneficiaryDetailsValid(): Boolean{
        if(binding.etBeniName.isEmpty()){
            showToast("Enter beneficiary name")
            return false
        }
        if(binding.etBeniMobile.isEmpty()){
            showToast("Enter beneficiary mobile")
            return false
        }

        if(!binding.etBeniMobile.isValidMobileNumber()){
            showToast("Enter valid mobile number")
            return false
        }

        if(!binding.etBeniSamagra.isEmpty() && binding.etBeniSamagra.getFieldValue().length!=9){
            showToast("Enter valid samagra id")
            return false
        }
        return true
    }




    private val geofenceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val transitionType = intent.getIntExtra(GeofenceUtil.EXTRA_TRANSITION_TYPE, -1)
            //val geofenceId = intent.getStringExtra(GeofenceUtil.GEOFENCE_ID) ?: "Unknown"
            when (transitionType) {
                Geofence.GEOFENCE_TRANSITION_ENTER -> {
                    viewModel.geoLocation.value?.let { updateGeofenceColor(LatLng(it.latitude, it.longitude)) }
                    showToast("User is inside the geofence!")
                }
                Geofence.GEOFENCE_TRANSITION_EXIT -> {
                    viewModel.geoLocation.value?.let { updateGeofenceColor(LatLng(it.latitude, it.longitude)) }
                    showToast("User is outside the geofence!")
                }
                else -> {
                    //Toast.makeText(context, "Unknown geofence event", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            geofenceReceiver, IntentFilter(GeofenceUtil.GEOFENCE_ACTION)
        )
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(geofenceReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        geofenceCircle?.remove()
        geofenceCircle = null
        GeofenceUtil.removeGeofences(this,
            onSuccess = { Log.d("Geofence", "Geofence removed successfully") },
            onFailure = { Log.e("Geofence", "Error removing geofence: ${it.message}") }
        )
    }


    private fun addGeofenceOnMap(latLng: LatLng,radius: Double) {
        // Draw a circle on the map to represent the geofence
        geofenceCircle = googleMap.addCircle(
            CircleOptions()
                .center(latLng)
                .radius(radius)  // 100 meters radius
                .strokeColor(Color.RED)
                .fillColor(0x22FF0000) // Transparent red fill
                .strokeWidth(2f)
        )

        //asset point marker
        googleMap.addMarker(MarkerOptions()
            .position(latLng)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_circle_dot))
            .anchor(0.5f, 0.5f))

        // Calculate geofence bounds
        val bounds = GeofenceUtil.getGeofenceBounds(latLng, radius)
        // Animate the camera to fit the geofence
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

        // Add Geofence using GeofenceUtil
        GeofenceUtil.addGeofence(
            context = this,
            latLng = latLng,
            radius = radius.toFloat(),
            onSuccess = { Toast.makeText(this, "Geofence Added", Toast.LENGTH_SHORT).show() },
            onFailure = { e -> Log.e("Geofence", "Error geofence: ${e.message}") }
        )
    }


    private fun updateGeofenceColor(userLatLng: LatLng) {
        geofenceCircle?.let { circle ->
            val isInside = GeofenceUtil.isInsideGeofence(
                LatLng(userLatLng.latitude, userLatLng.longitude),
                circle.center,
                circle.radius
            )

            // Only update if the state has changed
            if (isUserInsideGeofence == null || isInside != isUserInsideGeofence) {
                isUserInsideGeofence = isInside

                circle.fillColor = if (isInside) {
                    0x2200FF00 // Green transparent if inside
                } else {
                    0x22FF0000 // Red transparent if outside
                }
                circle.strokeColor = if (isInside) {
                    Color.GREEN
                } else {
                    Color.RED
                }
            }
        }
    }

}


