package mapitgis.jalnigamk.fhtc.screens.historylist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import mapitgis.jalnigamk.base.BaseViewModel
import mapitgis.jalnigamk.fhtc.database.dao.UpdateSurveyWithNICSurvey
import mapitgis.jalnigamk.fhtc.database.dao.VillageProjection
import mapitgis.jalnigamk.fhtc.database.table.FHTCUpdateSurveyInfo
import mapitgis.jalnigamk.fhtc.repository.FHTCRepository



class FHTCHistoryListVM(application: Application) : BaseViewModel(application)  {

    private var repo: FHTCRepository = FHTCRepository(application.baseContext)

    val villages: LiveData<List<VillageProjection>> get() = repo.getAssignedVillages()

    private val filterVillageCode = MutableLiveData<String>()
    var draftOrHistoryStatus : Int = 0

    val surveyList: LiveData<List<UpdateSurveyWithNICSurvey>> = filterVillageCode.switchMap {
        repo.getUpdatedVillageSurveyList(it,draftOrHistoryStatus)
    }

    fun getVillageSurveyList(villageCode: String) {
        filterVillageCode.value = villageCode
    }

    fun deleteDraftSurvey(surveyInfo: FHTCUpdateSurveyInfo) {
        launch {
            repo.deleteDraftSurvey(surveyInfo)
        }
    }
}