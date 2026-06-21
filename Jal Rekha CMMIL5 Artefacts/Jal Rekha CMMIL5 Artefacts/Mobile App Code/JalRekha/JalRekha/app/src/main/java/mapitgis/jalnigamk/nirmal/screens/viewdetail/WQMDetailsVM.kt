package mapitgis.jalnigamk.nirmal.screens.viewdetail

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mapitgis.jalnigamk.nirmal.database.table.NirmalWQMEntity
import mapitgis.jalnigamk.location.GeoAddress

class WQMDetailsVM(application: Application) : AndroidViewModel(application){


    private val _locationAddress = MutableLiveData<String?>("Searching for location...")
    val geoAddress: LiveData<String?> get() = _locationAddress

    lateinit var surveyDetail:NirmalWQMEntity

    fun fetchValueFromIntent(intent: Intent){
        surveyDetail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("survey_detail", NirmalWQMEntity::class.java)!!
        } else {
            intent.extras?.getParcelable("survey_detail")!!
        }
    }

    fun getLatLngAddress(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            GeoAddress.getAddress(context,surveyDetail.latitude,surveyDetail.longitude)?.let {
                _locationAddress.postValue(it)
            }
        }
    }
}