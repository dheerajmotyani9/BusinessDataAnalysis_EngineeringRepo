package mapitgis.jalnigamk.nirmal.screens.beneficiary

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mapitgis.jalnigamk.nirmal.base.NirmalBaseViewModel
import mapitgis.jalnigamk.nirmal.collection.WaterPointType
import mapitgis.jalnigamk.nirmal.database.table.NirmalWQMEntity
import mapitgis.jalnigamk.nirmal.database.dao.CombineSurveyTransaction
import mapitgis.jalnigamk.nirmal.database.dao.VillageDetailsWithCount

class BeneficiaryListVM(application: Application) : NirmalBaseViewModel(application) {


    private val _beneficiaryList = MutableLiveData<List<NirmalWQMEntity>>()
    val beneficiaryList: LiveData<List<NirmalWQMEntity>> = _beneficiaryList

    private val _villageList = MutableLiveData<List<VillageDetailsWithCount>>()
    val villageList: LiveData<List<VillageDetailsWithCount>> get() = _villageList

    lateinit var surveyTransaction: CombineSurveyTransaction
    var selectedTabIndex : Int = 0

    fun fetchValueFromIntent(intent: Intent){
        surveyTransaction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("survey_transaction", CombineSurveyTransaction::class.java)!!
        } else {
            intent.extras?.getParcelable("survey_transaction")!!
        }

        surveyTransaction = surveyTransaction.copy(
            allotTypeName = WaterPointType.BENEFICIARY.value,
            allotType = WaterPointType.BENEFICIARY.id.toString()
        )

        fetchEsrVillageDetails()
    }

    fun getAllWQM(villageCode:String) {
        viewModelScope.launch {
            nirmalRepo.getAllBeneficiary(surveyTransaction.surveyId,villageCode,selectedTabIndex).observeForever {
                _beneficiaryList.postValue(it)
            }
        }
    }


    fun fetchEsrVillageDetails() {
        nirmalRepo.getEsrVillageWithBeneficiaryCount(surveyTransaction.esrCode,surveyTransaction.surveyId,selectedTabIndex).observeForever { data ->
            _villageList.value = data
        }
    }

    fun deleteWQMById(id: Int) {
        viewModelScope.launch {
            nirmalRepo.deleteWQMById(id)
        }
    }

    fun uploadWQM(nirmalWQMEntity: NirmalWQMEntity, onResult: (Boolean) -> Unit) {
        if(isLocationValid()){
            viewModelScope.launch(Dispatchers.IO) {
                val pair = nirmalRepo.uploadWQMForm(nirmalWQMEntity,geoLocation.value!!)
                withContext(Dispatchers.Main) {
                    _toastMessage.value = pair.component2()
                    onResult(pair.component1())
                }
            }
        }
    }
}


