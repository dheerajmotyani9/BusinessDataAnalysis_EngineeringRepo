package mapitgis.jalnigamk.fhtc.screens.dashboard

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import mapitgis.jalnigamk.base.BaseViewModel
import mapitgis.jalnigamk.fhtc.repository.FHTCRepository
class FHTCDashboardVM(application: Application) : BaseViewModel(application)  {

    private var fhtcRepository: FHTCRepository = FHTCRepository(application.baseContext)

    val totalSurveyCount: LiveData<Int> = fhtcRepository.getTotalExistingSurveyCount()
    val totalDraftCount: LiveData<Int> = fhtcRepository.getTotalDraftSurveyCount()
    val totalCompletedCount: LiveData<Int> = fhtcRepository.getTotalCompletedSurveyCount()


    fun syncOfflineData(view: View){
        launchLoading {
            val response = fhtcRepository.insertFHTCSurveys()
            toast(response.second)
        }
    }
}