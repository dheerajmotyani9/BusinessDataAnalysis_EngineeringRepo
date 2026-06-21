package mapitgis.jalnigamk.fhtc.screens.historylist

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import mapitgis.jalnigam.databinding.ActivityFhtcupdatedsurveyListBinding
import mapitgis.jalnigam.nirmal.camera.ViewImageActivity
import mapitgis.jalnigamk.base.BaseKActivity
import mapitgis.jalnigamk.fhtc.database.dao.VillageProjection
import mapitgis.jalnigamk.fhtc.database.table.FHTCUpdateSurveyInfo
import mapitgis.jalnigamk.fhtc.screens.surveylist.FHTCSurveyListAdapter
import mapitgis.jalnigamk.nirmal.database.table.NirmalWQMEntity

class FHTCHistoryListActivity : BaseKActivity<FHTCHistoryListVM>(){

    override val viewModel: FHTCHistoryListVM by viewModels()

    private lateinit var adapter: FHTCHistoryListAdapter


    private val binding: ActivityFhtcupdatedsurveyListBinding by lazy {
        ActivityFhtcupdatedsurveyListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar(binding.appbar.toolbar)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        viewModel.draftOrHistoryStatus = intent.getIntExtra("DRAFT_OR_HISTORY",0)
        supportActionBar?.title = if(viewModel.draftOrHistoryStatus==0) "Draft" else "History"

        setupList()
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Called before the text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                adapter.filter(query)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        binding.spinVillage.onItemSelected<VillageProjection> { item, _, position, isLast ->
            binding.spinVillage.tag = item.villageCode
            viewModel.getVillageSurveyList(item.villageCode)
        }

        viewModel.villages.observe(this) { list ->
            list.isNotEmpty().let {
                val arrayList = list.toMutableList() as ArrayList<VillageProjection>
                if(list.size>1)arrayList.add(0, VillageProjection("","-- Select --"))
                binding.spinVillage.setAdapterData(arrayList, showPrompt = list.size>1)
            }
        }

        viewModel.surveyList.observe(this) { list ->
            list?.let {
               adapter.setData(list)
            }
        }
    }


    private fun setupList(){
        adapter = FHTCHistoryListAdapter(
            onViewClick = { item ->
                ViewImageActivity.viewImage(baseContext,item.update.imagePath)
            },
            onDeleteClick = { item ->
                showDiscardConfirmationDialog(item.update)
            }
        )
        binding.surveyList.adapter = adapter
        binding.surveyList.layoutManager = LinearLayoutManager(this)
    }


    private fun showDiscardConfirmationDialog(surveyInfo: FHTCUpdateSurveyInfo) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Discard")
            .setMessage("Are you sure you want to discard? This action cannot be undone.")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteDraftSurvey(surveyInfo)
                Toast.makeText(this, "discarded successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}