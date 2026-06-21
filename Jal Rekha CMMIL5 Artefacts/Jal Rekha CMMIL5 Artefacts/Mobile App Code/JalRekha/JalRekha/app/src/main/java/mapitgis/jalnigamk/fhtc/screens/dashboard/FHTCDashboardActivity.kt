package mapitgis.jalnigamk.fhtc.screens.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import mapitgis.jalnigam.databinding.ActivityFhtcdashboardBinding
import mapitgis.jalnigamk.base.BaseKActivity
import mapitgis.jalnigamk.fhtc.screens.download.FHTCSelectVillageActivity
import mapitgis.jalnigamk.fhtc.screens.surveylist.FHTCSurveyListActivity
import mapitgis.jalnigamk.fhtc.screens.historylist.FHTCHistoryListActivity
import mapitgis.jalnigamk.network.SurveySyncWorker

class FHTCDashboardActivity : BaseKActivity<FHTCDashboardVM>() {


    override val viewModel: FHTCDashboardVM by viewModels()

    private val binding: ActivityFhtcdashboardBinding by lazy {
        ActivityFhtcdashboardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar(binding.appbar.toolbar)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        binding.btnDownload.setOnClickListener{view ->
            startActivity(Intent(this, FHTCSelectVillageActivity::class.java))
        }

        binding.btnSurvey.setOnClickListener{view ->
            startActivity(Intent(this, FHTCSurveyListActivity::class.java))
        }

        binding.btnHistory.setOnClickListener{view ->
            startActivity(Intent(this, FHTCHistoryListActivity::class.java).apply {
                putExtra("DRAFT_OR_HISTORY",1)
            })
        }

        binding.btnDraft.setOnClickListener{view ->
            startActivity(Intent(this, FHTCHistoryListActivity::class.java).apply {
                putExtra("DRAFT_OR_HISTORY",0)
            })
        }
    }


    override fun onResume() {
        super.onResume()
        SurveySyncWorker.enqueueSurveySync(baseContext)
    }

}