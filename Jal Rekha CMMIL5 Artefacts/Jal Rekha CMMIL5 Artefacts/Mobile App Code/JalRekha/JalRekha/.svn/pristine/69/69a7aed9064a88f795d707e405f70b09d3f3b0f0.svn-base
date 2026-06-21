/**
 * Created on : 15-10-2024 04:31 pm
 */
package mapitgis.jalnigam.rfi.adapter.dhara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.model.dhara.DharaESRHistory;

public class DharaESRHistoryAdapter extends RecyclerView.Adapter<DharaESRHistoryAdapter.DharaESRHolder> {

    private Context context;
    public List<DharaESRHistory.DharaESRHistoryData>dataList;
    private DharaESRHistoryListener listener;

    public DharaESRHistoryAdapter(Context context, List<DharaESRHistory.DharaESRHistoryData> dataList, DharaESRHistoryListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DharaESRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dhara_esr_history_list,parent,false);
        return new DharaESRHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DharaESRHolder holder, int position) {
        DharaESRHistory.DharaESRHistoryData data = dataList.get(position);

        holder.remarksTv.setText(data.getRemarks());
        holder.surveyDateTv.setText(data.getSurveyDate());
        holder.surveyTimeTv.setText(data.getSurveyTime());
        holder.totalTv.setText(data.getTotalWaterSupplied());
        holder.endTv.setText(data.getEndReading());
        holder.startTv.setText(data.getStartReading());
        holder.intakeNameTv.setText(data.getIntakeName());
        holder.wtpNameTv.setText(data.getWtpName());
        holder.villageNameTv.setText(data.getVillageName());
        holder.esrNameTv.setText(data.getEsrName());

        holder.btnDetail.setOnClickListener(v -> listener.onDetailClicked(data));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DharaESRHolder extends RecyclerView.ViewHolder {

        private TextView intakeNameTv, startTv, endTv, totalTv, wtpNameTv;
        private TextView surveyTimeTv, surveyDateTv, remarksTv,esrNameTv,villageNameTv;
        private Button btnDetail;

        public DharaESRHolder(@NonNull View itemView) {
            super(itemView);
            intakeNameTv = itemView.findViewById(R.id.layout_dhara_esr_history_intake_tv);
            wtpNameTv = itemView.findViewById(R.id.layout_dhara_esr_history_wtp_name_tv);
            startTv = itemView.findViewById(R.id.layout_dhara_esr_history_start_tv);
            endTv = itemView.findViewById(R.id.layout_dhara_esr_history_end_tv);
            totalTv = itemView.findViewById(R.id.layout_dhara_esr_history_esr_total_tv);
            surveyTimeTv = itemView.findViewById(R.id.layout_dhara_esr_history_survey_time_tv);
            surveyDateTv = itemView.findViewById(R.id.layout_dhara_esr_history_survey_date_tv);
            remarksTv = itemView.findViewById(R.id.layout_dhara_esr_history_remark_tv);
            btnDetail = itemView.findViewById(R.id.layout_dhara_esr_history_btn_detail);
            esrNameTv = itemView.findViewById(R.id.layout_dhara_esr_history_esr_name_tv);
            villageNameTv = itemView.findViewById(R.id.layout_dhara_esr_history_village_name_tv);

        }
    }

    public interface DharaESRHistoryListener{
        void onDetailClicked(DharaESRHistory.DharaESRHistoryData data);
    }
}
