package mapitgis.jalnigamk.fhtc.screens.download

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mapitgis.jalnigamk.base.BaseViewModel
import mapitgis.jalnigamk.fhtc.database.dao.BlockProjection
import mapitgis.jalnigamk.fhtc.database.dao.DistrictProjection
import mapitgis.jalnigamk.fhtc.database.dao.GpProjection
import mapitgis.jalnigamk.fhtc.database.dao.VillageProjection
import mapitgis.jalnigamk.fhtc.repository.FHTCRepository
import mapitgis.jalnigamk.network.FHTCApiInterface.Companion.ENCRYPTION_KEY
import mapitgis.jalnigamk.util.AESHelper

class FHTCSelectVillageVM(application: Application) : BaseViewModel(application) {

    private var repo: FHTCRepository = FHTCRepository(application.baseContext)

    private val _districts = MutableLiveData<List<DistrictProjection>>()
    val districts: LiveData<List<DistrictProjection>> get() = _districts

    private val _blocks = MutableLiveData<List<BlockProjection>>()
    val blocks: LiveData<List<BlockProjection>> get() = _blocks

    private val _gps = MutableLiveData<List<GpProjection>>()
    val gps: LiveData<List<GpProjection>> get() = _gps

    private val _villages = MutableLiveData<List<VillageProjection>>()
    val villages: LiveData<List<VillageProjection>> get() = _villages

    fun loadDistricts() = launch {
        _districts.postValue(repo.getDistricts())
    }

    fun loadBlocks(districtCode: String) = launch {
        _blocks.postValue(repo.getBlocks(districtCode))
    }

    fun loadGramPanchayats(blockCode: String) = launch {
        _gps.postValue(repo.getGramPanchayats(blockCode))
    }

    fun loadVillages(gpCode: String) = launch {
        _villages.postValue(repo.getVillages(gpCode))
    }


    fun  fetchVillageMaster(){
        launchLoading {
            val result = repo.syncFHTCModuleAsync()
            if(result.first){
                loadDistricts()
            }else{
                toast(result.second)
            }
        }
    }


    fun fetchVillageSurveyData(villageCode:String){
        launchLoading {
            try {
                val result = repo.fetchVillageSurveyData(villageCode)
                if(result.first){
                    toast("Fetched successfully")
                    finishCurrentActivity()
                }else{
                    toast(result.second)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
