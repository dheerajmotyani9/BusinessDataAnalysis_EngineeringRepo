package mapitgis.jalnigamk.fhtc.screens.historylist

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import mapitgis.jalnigam.R
import mapitgis.jalnigam.databinding.ItemFhtcHistorySurveyBinding
import mapitgis.jalnigam.databinding.ItemFhtcNicSurveyBinding
import mapitgis.jalnigamk.fhtc.database.dao.UpdateSurveyWithNICSurvey

class FHTCHistoryListAdapter(
    private val onViewClick: (UpdateSurveyWithNICSurvey) -> Unit,
    private val onDeleteClick: (UpdateSurveyWithNICSurvey) -> Unit,
) : RecyclerView.Adapter<FHTCHistoryListAdapter.MViewHolder>() {

    private var list: List<UpdateSurveyWithNICSurvey> = emptyList()
    private var fullList: List<UpdateSurveyWithNICSurvey> = emptyList()

    fun setData(newList: List<UpdateSurveyWithNICSurvey>) {
        list = newList
        fullList = newList // keep backup for search
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        list = if (query.isBlank()) {
            fullList
        } else {
            fullList.filter {
                (it.NICSurvey.nameEn?.contains(query, ignoreCase = true) ?: false) ||
                        (it.NICSurvey.mobileNumber?.contains(query, ignoreCase = true) ?: false)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        val binding: ItemFhtcHistorySurveyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_fhtc_history_survey,
            parent,
            false
        )
        return MViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class MViewHolder(private val binding: ItemFhtcHistorySurveyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UpdateSurveyWithNICSurvey) {
            binding.surveyInfo = item
            binding.tapConnectionImage.setOnClickListener { onViewClick(item) }
            binding.btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }
}
