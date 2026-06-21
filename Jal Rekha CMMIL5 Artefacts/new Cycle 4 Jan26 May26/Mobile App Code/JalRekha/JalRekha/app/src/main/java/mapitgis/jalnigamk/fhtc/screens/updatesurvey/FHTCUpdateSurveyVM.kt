package mapitgis.jalnigamk.fhtc.screens.updatesurvey

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mapitgis.jalnigam.core.Utility
import mapitgis.jalnigamk.base.BaseViewModel
import mapitgis.jalnigamk.fhtc.database.table.FHTCNICSurveyInfo
import mapitgis.jalnigamk.fhtc.database.table.FHTCUpdateSurveyInfo
import mapitgis.jalnigamk.fhtc.repository.FHTCRepository
import mapitgis.jalnigamk.location.MLocListener
import mapitgis.jalnigamk.location.MyLocation

class FHTCUpdateSurveyVM(application: Application) : BaseViewModel(application)  {

    private var repo: FHTCRepository = FHTCRepository(application.baseContext)

    private val _surveyInfo = MutableLiveData<FHTCNICSurveyInfo>()
    val surveyInfo: LiveData<FHTCNICSurveyInfo> get() = _surveyInfo
    var previousSurveyLatLng : LatLng? = null;


    /*Current Location */
    protected lateinit var myLocation: MyLocation
    private val _locationAddress = MutableLiveData<String?>("Searching for location...")
    val geoAddress: LiveData<String?> get() = _locationAddress


    private val _lastLocation = MutableLiveData<Location?>()
    val geoLocation: LiveData<Location?> get() = _lastLocation


    private val _distanceMsg = MutableLiveData<String?>(null)
    val distanceMeterMsg: LiveData<String?> get() = _distanceMsg


    val isLocationPermissionGranted: Boolean get() = myLocation.isLocationPermissionGranted()

    fun initLocationRequest(context: Context){
        myLocation = MyLocation(context,null,false,object : MLocListener {
            override fun locationOn() {}

            override fun currentLocation(location: Location) {
                _lastLocation.value = location
                previousSurveyLatLng?.let {
                    _distanceMsg.value = myLocation.getDistanceDisplay(
                        location.latitude,
                        location.longitude,
                        previousSurveyLatLng?.latitude?:0.0,
                        previousSurveyLatLng?.longitude?:0.0)
                }

                viewModelScope.launch(Dispatchers.IO) {
                    myLocation.getAddress(location)?.let {
                        _locationAddress.postValue(it)
                    }
                }
            }

            override fun locationCancelled() {}
        })
    }

    fun startLocationUpdates(){
        if (this::myLocation.isInitialized) myLocation.startLocation()
    }

    fun stopLocationUpdates(){
        if (this::myLocation.isInitialized) myLocation.endUpdates()
    }

    fun isLocationValid():Boolean{
        if(geoLocation.value==null){
            toast("Searching for location...")
            return false
        }
        return true
    }


    fun getBitmapText():String{
        return "Latitude: ${geoLocation.value?.latitude}\n" +
                "Longitude: ${geoLocation.value?.longitude}\n"+
                "Date: ${Utility.getCurrentTimeSync()}\n"+
                "Name: ${repo.login.name}\n"+
                "Number: ${repo.login.mobile}"
    }

    fun setSurveyInfo(info: FHTCNICSurveyInfo) {
        _surveyInfo.value = info
        if(info.latitude!=null && info.longitude!=null && info.latitude!="0.0" && info.longitude!="0.0"){
            val latLng = LatLng(info.latitude.toDouble(), info.longitude.toDouble())
            previousSurveyLatLng = latLng
        }
    }


    fun performSubmitEvent(remark:String,imageUri:String){
        val updateSurveyInfo = FHTCUpdateSurveyInfo(
            surveyId = surveyInfo.value?.surveyId ?: 0,
            villageCode = surveyInfo.value?.villageCode ?: "",
            imagePath = imageUri,
            latitude = geoLocation.value?.latitude ?: 0.0,
            longitude = geoLocation.value?.longitude ?: 0.0,
            accuracy = geoLocation.value?.accuracy?.toDouble() ?: 0.0,
            remark = remark,
            surveyorId = repo.login.id.toInt(),
            schemeId = repo.prefManager.schemeId
        )

        launch {
            repo.saveSurveyInfo(updateSurveyInfo)
            toast("Saved successfully")
            finishCurrentActivity()
        }
    }
}