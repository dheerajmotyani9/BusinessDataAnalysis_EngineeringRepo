package mapitgis.jalnigamk.fhtc.screens.surveylist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import mapitgis.jalnigamk.base.BaseViewModel
import mapitgis.jalnigamk.fhtc.database.dao.VillageProjection
import mapitgis.jalnigamk.fhtc.database.table.FHTCNICSurveyInfo
import mapitgis.jalnigamk.fhtc.repository.FHTCRepository



class FHTCSurveyListVM(application: Application) : BaseViewModel(application)  {

    private var repo: FHTCRepository = FHTCRepository(application.baseContext)

    val villages: LiveData<List<VillageProjection>> get() = repo.getAssignedVillages()

    private val filter = MutableLiveData<Pair<String, String>>() // villageCode, consent

    val surveyList: LiveData<List<FHTCNICSurveyInfo>> = filter.switchMap {
        repo.getVillageSurveyListNIC(it.first, it.second)
    }

    fun getVillageSurveyList(villageCode: String, isConsent: Boolean) {
        val consent = if (isConsent) "Yes" else "No"
        filter.value = villageCode to consent

        if(villageCode.isNotBlank())checkIfAllSurveyUpdated(villageCode)
    }

    private fun checkIfAllSurveyUpdated(villageCode: String){
        launch {
            val flag = repo.checkIfAllSurveyUpdated(villageCode)
            if(flag){
                toast("All surveys are updated for selected village")
            }
        }
    }
}