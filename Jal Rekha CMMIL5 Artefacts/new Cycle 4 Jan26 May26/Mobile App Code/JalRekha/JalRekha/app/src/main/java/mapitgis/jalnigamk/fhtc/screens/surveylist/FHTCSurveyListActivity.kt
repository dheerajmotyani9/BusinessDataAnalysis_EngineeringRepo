package mapitgis.jalnigamk.fhtc.screens.surveylist

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import mapitgis.jalnigam.R
import mapitgis.jalnigam.databinding.ActivityFhtcdashboardBinding
import mapitgis.jalnigam.databinding.ActivityFhtcsurveyListBinding
import mapitgis.jalnigam.nirmal.camera.ViewImageActivity
import mapitgis.jalnigamk.base.BaseKActivity
import mapitgis.jalnigamk.fhtc.database.dao.GpProjection
import mapitgis.jalnigamk.fhtc.database.dao.VillageProjection
import mapitgis.jalnigamk.fhtc.screens.updatesurvey.FHTCUpdateSurveyActivity
import mapitgis.jalnigamk.nirmal.database.dao.VillageDetailsWithCount
import mapitgis.jalnigamk.nirmal.screens.viewdetail.WQMDetailsActivity
import mapitgis.jalnigamk.nirmal.screens.wqm_list.WQMAdapter

class FHTCSurveyListActivity : BaseKActivity<FHTCSurveyListVM>(){

    override val viewModel: FHTCSurveyListVM by viewModels()

    private lateinit var adapter: FHTCSurveyListAdapter


    private val binding: ActivityFhtcsurveyListBinding by lazy {
        ActivityFhtcsurveyListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar(binding.appbar.toolbar)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
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
            viewModel.getVillageSurveyList(item.villageCode,binding.rbYes.isChecked)
        }

        binding.rbYes.setOnClickListener {
            binding.spinVillage.tag?.let {
                viewModel.getVillageSurveyList(it.toString(),true)
            }
        }

        binding.rbNo.setOnClickListener {
            binding.spinVillage.tag?.let {
                binding.searchView.setText("")
                viewModel.getVillageSurveyList(it.toString(),false)
            }
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
        adapter = FHTCSurveyListAdapter(
            onViewClick = { item ->
                val intent = Intent(this, FHTCUpdateSurveyActivity::class.java).apply {
                    putExtra("surveyInfo", item)
                }
                startActivity(intent)
            }
        )
        binding.surveyList.adapter = adapter
        binding.surveyList.layoutManager = LinearLayoutManager(this)
    }
}