package mapitgis.jalnigamk.nirmal.screens.contractor

import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.util.component1
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import mapitgis.jalnigam.R
import mapitgis.jalnigam.core.Utility
import mapitgis.jalnigam.databinding.ActivityNirmalContractorBinding
import mapitgis.jalnigamk.nirmal.base.NirmalBaseActivity
import mapitgis.jalnigamk.nirmal.collection.WaterPointType

class NirmalContractorActivity : NirmalBaseActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private val binding: ActivityNirmalContractorBinding by lazy {
        ActivityNirmalContractorBinding.inflate(layoutInflater)
    }

    private val viewModel: NirmalContractorVM by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        baseVM = viewModel
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setupMap()

        viewModel.geoLocation.observe(this){
            it?.let { showLocationOnMap(it) }
        }

        binding.spinComponent.onItemSelected<String> { component, view, i, b ->
            viewModel.setSelectedComponent(WaterPointType.fromString(component))
        }

        val list = listOf("-- Select Component --",WaterPointType.WTP_INLET.value, WaterPointType.WTP_OUTLET.value, WaterPointType.OHT_ESR.value, WaterPointType.BULK_METER.value)
        val arrayList = list.toMutableList() as ArrayList<String>
        binding.spinComponent.setAdapterData(arrayList, showPrompt = true)
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.appbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Water Quality Monitoring"
        supportActionBar?.subtitle = "Date: ${Utility.getCurrentDate().component1()}"
    }

    private fun setupMap(){
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
    }

    private fun showLocationOnMap(location: Location){
        val latLng = LatLng(location.latitude, location.longitude)
        //showing user location
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18f))
    }
}