package mapitgis.jalnigamk.nirmal.screens.beneficiary

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import mapitgis.jalnigam.R
import mapitgis.jalnigam.core.ProgressDialog
import mapitgis.jalnigam.databinding.ActivityNirmalBeneficiaryListBinding
import mapitgis.jalnigam.nirmal.camera.ViewImageActivity
import mapitgis.jalnigamk.nirmal.base.NirmalBaseActivity
import mapitgis.jalnigamk.nirmal.database.dao.CombineSurveyTransaction
import mapitgis.jalnigamk.nirmal.database.dao.VillageDetailsWithCount
import mapitgis.jalnigamk.nirmal.database.table.NirmalWQMEntity
import mapitgis.jalnigamk.nirmal.screens.viewdetail.WQMDetailsActivity
import mapitgis.jalnigamk.nirmal.screens.wqm_add.WQMFormActivity

class BeneficiaryListActivity : NirmalBaseActivity() {

    companion object{
        fun pushActivity(context: Context, surveyTran: CombineSurveyTransaction){
            context.startActivity(Intent(context, BeneficiaryListActivity::class.java).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("survey_transaction", surveyTran)
            })
        }
    }

    private val binding: ActivityNirmalBeneficiaryListBinding by lazy {
        ActivityNirmalBeneficiaryListBinding.inflate(layoutInflater)
    }

    private val viewModel: BeneficiaryListVM by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    private lateinit var adapter: BenificiaryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        baseVM = viewModel
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        initView()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.fetchValueFromIntent(intent)

        adapter = BenificiaryListAdapter(
            viewModel,
            onUploadClick = { item ->
                showUploadConfirmationDialog(item)
            },
            onDiscardClick = { item ->
                showDiscardConfirmationDialog(item)
            },
            onImageClick = { uri ->
                ViewImageActivity.viewImage(baseContext, uri)
            },
            onViewClick = { item ->
                WQMDetailsActivity.pushActivity(baseContext, item)
            }
        )

        binding.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.beneficiaryList.observe(this) { list ->
            list?.let { adapter.setData(it) }
        }
    }


    private fun initView(){
        binding.fab.setOnClickListener {
            WQMFormActivity.pushActivity(baseContext, viewModel.surveyTransaction)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewModel.selectedTabIndex = tab?.position ?:0
                viewModel.fetchEsrVillageDetails()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.spinVillage.onItemSelected<VillageDetailsWithCount> { village, view, i, b ->
            viewModel.getAllWQM(village.villageId)
        }

        viewModel.villageList.observe(this) { list ->
            list.isNotEmpty().let {
                val arrayList = list.toMutableList() as ArrayList<VillageDetailsWithCount>
                arrayList.add(0, VillageDetailsWithCount("","All Villages",arrayList.sumOf { it.count }))
                binding.spinVillage.setAdapterData(arrayList, showPrompt = true)
            }
        }
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.appbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun showDiscardConfirmationDialog(item: NirmalWQMEntity) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Discard")
            .setMessage("Are you sure you want to discard? This action cannot be undone.")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteWQMById(item.id)
                Toast.makeText(this, "discarded successfully", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showUploadConfirmationDialog(item: NirmalWQMEntity) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Upload")
            .setMessage("Are you sure you want to upload?")
            .setPositiveButton("Yes") { _, _ ->
                ProgressDialog.startDialog(this@BeneficiaryListActivity, R.string.please_wait)
                viewModel.uploadWQM(item){
                    ProgressDialog.stopDialog()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}