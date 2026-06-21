package mapitgis.jalnigam.rfi.adapter;

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
import mapitgis.jalnigam.rfi.model.DIComplaintLogs;

public class DIComplaintLogsAdapter extends RecyclerView.Adapter<DIComplaintLogsAdapter.DIComplaintLogsHolder> {

    private Context context;
    private List<DIComplaintLogs.DIComplaintLogsData> dataList;
    private DIComplaintLogsListener listener;

    public DIComplaintLogsAdapter(Context context, List<DIComplaintLogs.DIComplaintLogsData> dataList, DIComplaintLogsListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DIComplaintLogsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_di_complaint_logs_list, parent, false);

        return new DIComplaintLogsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DIComplaintLogsHolder holder, int position) {
        DIComplaintLogs.DIComplaintLogsData logsData = dataList.get(position);

        holder.allotTypeTv.setText(logsData.getAlotmentType());
        holder.assingTextView.setText(logsData.getAssigneToName());
        holder.commentTv.setText(logsData.getInspectionComments());
        holder.qaQcTv.setText(logsData.getInsertDate());
        holder.statusTv.setText(logsData.getStatusName());
        holder.btnViewImage.setOnClickListener(v -> listener.onViewImageClicked(logsData));
//        holder.inspectionDateTv.setText(logsData.getInsertDate());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DIComplaintLogsHolder extends RecyclerView.ViewHolder {
        private TextView assingTextView, statusTv, inspectionDateTv;
        private TextView commentTv, qaQcTv, allotTypeTv;
        private Button btnViewImage;

        public DIComplaintLogsHolder(@NonNull View itemView) {
            super(itemView);

            assingTextView = itemView.findViewById(R.id.layout_di_complaint_log_assign_name_tv);
            statusTv = itemView.findViewById(R.id.layout_di_complaint_log_status_tv);
            commentTv = itemView.findViewById(R.id.layout_di_complaint_comments_tv);
            qaQcTv = itemView.findViewById(R.id.layout_di_complaint_qa_qc_review_tv);
            allotTypeTv = itemView.findViewById(R.id.layout_di_complaint_log_alot_type_tv);
            btnViewImage = itemView.findViewById(R.id.layout_di_complaint_view_photo_btn);
        }
    }

    public interface DIComplaintLogsListener {
        void onViewImageClicked(DIComplaintLogs.DIComplaintLogsData data);
    }
}
