package mapitgis.jalnigamk.fhtc.screens.surveylist

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mapitgis.jalnigam.R
import mapitgis.jalnigam.databinding.ItemFhtcNicSurveyBinding
import mapitgis.jalnigamk.fhtc.database.table.FHTCNICSurveyInfo

class FHTCSurveyListAdapter(
    private val onViewClick: (FHTCNICSurveyInfo) -> Unit,
) : RecyclerView.Adapter<FHTCSurveyListAdapter.MViewHolder>() {

    private var list: List<FHTCNICSurveyInfo> = emptyList()
    private var fullList: List<FHTCNICSurveyInfo> = emptyList()

    fun setData(newList: List<FHTCNICSurveyInfo>) {
        list = newList
        fullList = newList // keep backup for search
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        list = if (query.isBlank()) {
            fullList
        } else {
            fullList.filter {
                (it.nameEn?.contains(query, ignoreCase = true) ?: false) ||
                        (it.mobileNumber?.contains(query, ignoreCase = true) ?: false)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val binding: ItemFhtcNicSurveyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_fhtc_nic_survey,
            parent,
            false
        )
        return MViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class MViewHolder(private val binding: ItemFhtcNicSurveyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FHTCNICSurveyInfo) {
            binding.surveyInfo = item
            binding.root.setOnClickListener { onViewClick(item) }
        }
    }
}
