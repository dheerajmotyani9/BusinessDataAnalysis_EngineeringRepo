/**
 * Created on : 15-10-2024 03:32 pm
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
import mapitgis.jalnigam.rfi.model.dhara.DharaIntakeHistory;

public class DharaIntakeHistoryAdapter extends RecyclerView.Adapter<DharaIntakeHistoryAdapter.DharaIntakeViewHolder> {

    private Context context;
    private List<DharaIntakeHistory.DharaIntakeHistoryData> dataList;
    private DharaIntakeHistoryListener listener;

    public DharaIntakeHistoryAdapter(Context context, List<DharaIntakeHistory.DharaIntakeHistoryData> dataList, DharaIntakeHistoryListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DharaIntakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dhara_intake_history_list,parent,false);
        return new DharaIntakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DharaIntakeViewHolder holder, int position) {
        DharaIntakeHistory.DharaIntakeHistoryData data = dataList.get(position);

        holder.remarksTv.setText(data.getRemarks());
        holder.surveyDateTv.setText(data.getSurveyDate());
        holder.surveyTimeTv.setText(data.getSurveyTime());
        holder.totalTv.setText(data.getTotalRawWaterDrawn());
        holder.endTv.setText(data.getEndReading());
        holder.startTv.setText(data.getStartReading());
        holder.intakeNameTv.setText(data.getIntakeName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DharaIntakeViewHolder extends RecyclerView.ViewHolder {

        private TextView intakeNameTv,startTv,endTv,totalTv;
        private TextView surveyTimeTv,surveyDateTv,remarksTv;

        public DharaIntakeViewHolder(@NonNull View itemView) {
            super(itemView);
            intakeNameTv = itemView.findViewById(R.id.layout_dhara_intake_history_intake_name_tv);
            startTv = itemView.findViewById(R.id.layout_dhara_intake_history_intake_start_tv);
            endTv = itemView.findViewById(R.id.layout_dhara_intake_history_intake_end_tv);
            totalTv = itemView.findViewById(R.id.layout_dhara_intake_history_intake_total_tv);
            surveyTimeTv =itemView.findViewById(R.id.layout_dhara_intake_history_survey_time_tv);
            surveyDateTv = itemView.findViewById(R.id.layout_dhara_intake_history_survey_date_tv);
            remarksTv = itemView.findViewById(R.id.layout_dhara_intake_history_remark_tv);
        }
    }

    public interface DharaIntakeHistoryListener {
        void onDetailClicked(DharaIntakeHistory.DharaIntakeHistoryData data);
    }
}
