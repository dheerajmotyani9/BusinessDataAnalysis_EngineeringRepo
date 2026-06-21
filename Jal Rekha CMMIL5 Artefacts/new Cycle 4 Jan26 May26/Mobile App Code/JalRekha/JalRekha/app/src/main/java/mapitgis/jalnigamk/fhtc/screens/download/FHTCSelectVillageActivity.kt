package mapitgis.jalnigamk.fhtc.screens.download

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import mapitgis.jalnigam.BaseActivity
import mapitgis.jalnigam.databinding.ActivityFhtcSelectVillageBinding
import mapitgis.jalnigamk.base.BaseKActivity
import mapitgis.jalnigamk.fhtc.database.dao.BlockProjection
import mapitgis.jalnigamk.fhtc.database.dao.DistrictProjection
import mapitgis.jalnigamk.fhtc.database.dao.GpProjection
import mapitgis.jalnigamk.fhtc.database.dao.VillageProjection
import mapitgis.jalnigamk.fhtc.screens.dashboard.FHTCDashboardVM
import mapitgis.jalnigamk.nirmal.database.dao.VillageDetails
import kotlin.getValue

class FHTCSelectVillageActivity : BaseKActivity<FHTCSelectVillageVM>(){

    override val viewModel: FHTCSelectVillageVM by viewModels()

    private val binding: ActivityFhtcSelectVillageBinding by lazy {
        ActivityFhtcSelectVillageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar(binding.appbar.toolbar)
        viewModel.fetchVillageMaster()

        binding.btnFetch.setOnClickListener { onFetchButtonEvent() }

        binding.spinDist.onItemSelected<DistrictProjection> { item, _, position,isLast ->
            viewModel.loadBlocks(item.districtCode)
        }

        binding.spinBlock.onItemSelected<BlockProjection> { item, _, position, isLast ->
            viewModel.loadGramPanchayats(item.blockCode)
        }

        binding.spinGp.onItemSelected<GpProjection> { item, _, position, isLast ->
            viewModel.loadVillages(item.lgdGpCode)
        }



        viewModel.districts.observe(this) { list ->
            list.isNotEmpty().let {
                val arrayList = list.toMutableList() as ArrayList<DistrictProjection>
                arrayList.add(0, DistrictProjection("","-- Select --"))
                binding.spinDist.setAdapterData(arrayList, showPrompt = true)
            }
        }

        viewModel.blocks.observe(this) { list ->
            list.isNotEmpty().let {
                val arrayList = list.toMutableList() as ArrayList<BlockProjection>
                arrayList.add(0, BlockProjection("","-- Select --"))
                binding.spinBlock.setAdapterData(arrayList, showPrompt = true)
            }
        }

        viewModel.gps.observe(this) { list ->
            list.isNotEmpty().let {
                val arrayList = list.toMutableList() as ArrayList<GpProjection>
                arrayList.add(0, GpProjection("","-- Select --"))
                binding.spinGp.setAdapterData(arrayList, showPrompt = true)
            }
        }


        viewModel.villages.observe(this) { list ->
            list.isNotEmpty().let {
                val arrayList = list.toMutableList() as ArrayList<VillageProjection>
                arrayList.add(0, VillageProjection("","-- Select --"))
                binding.spinVillage.setAdapterData(arrayList, showPrompt = true)
            }
        }
    }

    fun onFetchButtonEvent(){
        if(binding.spinDist.getSelectedPosition()==0){
            showToast("Please select district")
        }else if(binding.spinBlock.getSelectedPosition()==0){
            showToast("Please select block")
        }else if(binding.spinGp.getSelectedPosition()==0){
            showToast("Please select gram panchayat")
        }else if(binding.spinVillage.getSelectedPosition()==0){
            showToast("Please select village")
        }else{
            val selectedVillage = binding.spinVillage.getSelectedItem() as VillageProjection
            viewModel.fetchVillageSurveyData(selectedVillage.villageCode)
        }
    }
}