/**
 * Created on : 15-10-2024 04:03 pm
 */
package mapitgis.jalnigam.rfi.adapter.dhara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.model.dhara.DharaWTPHistory;

public class DharaWTPHistoryAdapter extends RecyclerView.Adapter<DharaWTPHistoryAdapter.DharaWTPHolder> {

    private Context context;
    private List<DharaWTPHistory.DharaWTPHistoryData> dataList;
    private DharaWTPHistoryListener listener;

    public DharaWTPHistoryAdapter(Context context, List<DharaWTPHistory.DharaWTPHistoryData> dataList, DharaWTPHistoryListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DharaWTPHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dhara_wtp_history_list, parent, false);
        return new DharaWTPHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DharaWTPHolder holder, int position) {
        DharaWTPHistory.DharaWTPHistoryData data = dataList.get(position);
        holder.remarksTv.setText(data.getRemarks());
        holder.surveyDateTv.setText(data.getSurveyDate());
        holder.surveyTimeTv.setText(data.getSurveyTime());
        holder.totalTv.setText(data.getTotalWaterSupplied());
        holder.endTv.setText(data.getEndReading());
        holder.startTv.setText(data.getStartReading());
        holder.intakeNameTv.setText(data.getIntakeName());
        holder.wtpNameTv.setText(data.getWtpName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DharaWTPHolder extends RecyclerView.ViewHolder {

        private TextView intakeNameTv, startTv, endTv, totalTv, wtpNameTv;
        private TextView surveyTimeTv, surveyDateTv, remarksTv;

        public DharaWTPHolder(@NonNull View itemView) {
            super(itemView);
            intakeNameTv = itemView.findViewById(R.id.layout_dhara_wtp_history_intake_tv);
            wtpNameTv = itemView.findViewById(R.id.layout_dhara_wtp_history_wtp_name_tv);
            startTv = itemView.findViewById(R.id.layout_dhara_wtp_history_start_tv);
            endTv = itemView.findViewById(R.id.layout_dhara_wtp_history_end_tv);
            totalTv = itemView.findViewById(R.id.layout_dhara_wtp_history_wtp_total_tv);
            surveyTimeTv = itemView.findViewById(R.id.layout_dhara_wtp_history_survey_time_tv);
            surveyDateTv = itemView.findViewById(R.id.layout_dhara_wtp_history_survey_date_tv);
            remarksTv = itemView.findViewById(R.id.layout_dhara_wtp_history_remark_tv);
        }
    }

    public interface DharaWTPHistoryListener {
        void onDetailClicked(DharaWTPHistory.DharaWTPHistoryData data);
    }
}
