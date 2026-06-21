package mapitgis.jalnigam.rfi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.model.ContractorLogs;

public class ContractorLogsAdapter extends RecyclerView.Adapter<ContractorLogsAdapter.ContractorViewHolder> {

    private Context context;
    private List<ContractorLogs.ContractorLogsData> contractorLogsDataList;

    public ContractorLogsAdapter(Context context, List<ContractorLogs.ContractorLogsData> contractorLogsDataList) {
        this.context = context;
        this.contractorLogsDataList = contractorLogsDataList;
    }

    @NonNull
    @Override
    public ContractorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contractort_logs_list,parent,false);
        return new ContractorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractorViewHolder holder, int position) {
        ContractorLogs.ContractorLogsData data = contractorLogsDataList.get(position);

        holder.allotTypeTv.setText(data.getAllotmentType());
        holder.assingTextView.setText(data.getAssignToName());
        holder.statusTv.setText(data.getStatusName());


        if(data.getInspectionDate() !=null){
            holder.inspectionDateTv.setText(data.getInspectionDate());
        }else{
            holder.inspectionDateTv.setText("NA");
        }

        if(data.getQaQcReview() !=null){
            holder.commentTv.setText(data.getResponseComments());
            holder.qaQcTv.setText(data.getQaQcReview());
            holder.commentLayout.setVisibility(View.VISIBLE);
        }else{
            holder.commentLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return contractorLogsDataList.size();
    }

    public static class ContractorViewHolder extends RecyclerView.ViewHolder {

        private TextView assingTextView,statusTv,inspectionDateTv;
        private TextView commentTv,qaQcTv,allotTypeTv;
        private RelativeLayout commentLayout;

        public ContractorViewHolder(@NonNull View itemView) {
            super(itemView);
            assingTextView = itemView.findViewById(R.id.layout_contractor_assign_name_tv);
            statusTv = itemView.findViewById(R.id.layout_contractor_log_status_tv);
            inspectionDateTv = itemView.findViewById(R.id.layout_contractor_log_inspection_date_tv);
            commentTv = itemView.findViewById(R.id.layout_contractor_comments_tv);
            qaQcTv = itemView.findViewById(R.id.layout_contractor_qa_qc_review_tv);
            commentLayout = itemView.findViewById(R.id.layout_contractor_comment_layout);
            allotTypeTv = itemView.findViewById(R.id.layout_contractor_log_alot_type_tv);
        }
    }
}
