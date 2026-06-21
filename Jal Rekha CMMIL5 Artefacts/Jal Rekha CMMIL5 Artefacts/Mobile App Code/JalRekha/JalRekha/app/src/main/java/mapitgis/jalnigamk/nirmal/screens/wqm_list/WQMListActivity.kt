package mapitgis.jalnigamk.nirmal.screens.wqm_list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import mapitgis.jalnigam.R
import mapitgis.jalnigam.core.ProgressDialog
import mapitgis.jalnigam.databinding.ActivityNirmlaWqmlistBinding
import mapitgis.jalnigam.databinding.DialogNirmalFilterBinding
import mapitgis.jalnigam.nirmal.camera.ViewImageActivity
import mapitgis.jalnigamk.nirmal.base.NirmalBaseActivity
import mapitgis.jalnigamk.nirmal.collection.WaterPointType
import mapitgis.jalnigamk.nirmal.database.table.NirmalWQMEntity
import mapitgis.jalnigamk.nirmal.screens.viewdetail.WQMDetailsActivity
import mapitgis.jalnigamk.util.DateTimePicker

class WQMListActivity : NirmalBaseActivity() {

    private val binding: ActivityNirmlaWqmlistBinding by lazy {
        ActivityNirmlaWqmlistBinding.inflate(layoutInflater)
    }

    private val viewModel: WQMListVM by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    private lateinit var adapter: WQMAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        baseVM = viewModel
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupToolbar()
        initView()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        adapter = WQMAdapter(
            viewModel,
            onUploadClick = { item ->
                showUploadConfirmationDialog(item)
            },
            onDiscardClick = { item ->
                showDiscardConfirmationDialog(item)
            },
            onImageClick = { path ->
                ViewImageActivity.viewImage(baseContext, path)
            },
            onViewClick = { item ->
                WQMDetailsActivity.pushActivity(baseContext, item)
            }
        )

        binding.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.tvDate.setOnClickListener {
            DateTimePicker.showDatePicker(
                context = this,
                format = "yyyy-MM-dd"
            ) { selectedDate ->
                viewModel.setSelectedDate(selectedDate)
            }
        }

        viewModel.surveyTransList.observe(this) { list ->
            list?.let { adapter.setData(it) }
        }
    }

    private fun initView(){

    }

    private fun setupToolbar(){
        setSupportActionBar(binding.appbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Water Quality Monitoring"
        //supportActionBar?.subtitle = "Date: ${Utility.getCurrentDate().component1()}"
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
                ProgressDialog.startDialog(this@WQMListActivity, R.string.please_wait)
                viewModel.uploadWQM(item) { isSuccess ->
                    ProgressDialog.stopDialog()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_nirmal_filter, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showFilterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun showFilterDialog() {
        val binding = DialogNirmalFilterBinding.inflate(LayoutInflater.from(this))
        binding.checkboxInlet.text = WaterPointType.WTP_INLET.value
        binding.checkboxOutlet.text = WaterPointType.WTP_OUTLET.value
        binding.checkboxBulkMeter.text = WaterPointType.BULK_METER.value
        binding.checkboxOht.text = WaterPointType.OHT_ESR.value

        binding.checkboxInlet.isChecked = viewModel.isFilterSelected(WaterPointType.WTP_INLET.id)
        binding.checkboxOutlet.isChecked = viewModel.isFilterSelected(WaterPointType.WTP_OUTLET.id)
        binding.checkboxBulkMeter.isChecked = viewModel.isFilterSelected(WaterPointType.BULK_METER.id)
        binding.checkboxOht.isChecked = viewModel.isFilterSelected(WaterPointType.OHT_ESR.id)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Select Filters")
            .setView(binding.root)
            .setPositiveButton("Apply") { _, _ ->
                val selectedOptions = mutableListOf<Int>()
                if (binding.checkboxInlet.isChecked) selectedOptions.add(WaterPointType.WTP_INLET.id)
                if (binding.checkboxOutlet.isChecked) selectedOptions.add(WaterPointType.WTP_OUTLET.id)
                if (binding.checkboxBulkMeter.isChecked) selectedOptions.add(WaterPointType.BULK_METER.id)
                if (binding.checkboxOht.isChecked) selectedOptions.add(WaterPointType.OHT_ESR.id)

                if(selectedOptions.isEmpty()){
                    showToast("Please select at least one filter")
                }else{
                    viewModel.filterList(selectedOptions)
                }
            }
            .setNegativeButton("Cancel",null)
            .create()

        dialog.show()
    }
}