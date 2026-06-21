package mapitgis.jalnigam.rfi.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.model.InspectionRequest;

public class RFIHistoryAdapter extends RecyclerView.Adapter<RFIHistoryAdapter.RFIHistoryHolder> {

    private Context context;
    private List<InspectionRequest.InspectionRequestData> requestDataList;
    private RFIHistoryListener listener;

    public RFIHistoryAdapter(Context context, List<InspectionRequest.InspectionRequestData> requestDataList, RFIHistoryListener listener) {
        this.context = context;
        this.requestDataList = requestDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RFIHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_monitoring_inspection_history_list, parent, false);
        return new RFIHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RFIHistoryHolder holder, int position) {

        InspectionRequest.InspectionRequestData history = requestDataList.get(position);

        holder.schemeNameTv.setText(history.getSchemeName());
        holder.componentTypeTv.setText(history.getComponentName());
        holder.pointNameTv.setText(history.getPointName());
        holder.stageNameTv.setText(history.getStageName());
        holder.locationTv.setText(history.getAddress());
        holder.inspectionDateTv.setText(history.getInspectionDate());
        holder.descriptionTextView.setText(history.getDescription());
        holder.requestIdTv.setText(history.getRfiId());
        holder.statusTv.setText(history.getStatusName());
        holder.feTextView.setText(history.getFeName());
        holder.requestDateTv.setText(history.getInsertDate());

        if (history.getPointName().equalsIgnoreCase("0")) {
            holder.pointContainer.setVisibility(View.GONE);
        } else {
            holder.pointContainer.setVisibility(View.VISIBLE);
        }

        if (history.isStageIdNotFound()) {
            holder.stageContainer.setVisibility(View.GONE);
            holder.pointContainer.setGravity(Gravity.END);
        } else {
            holder.stageContainer.setVisibility(View.VISIBLE);
            holder.pointContainer.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        // TODO: 22-07-2024 : here we check request uploaded or not, if status =0 we show upload btn
        holder.btnRevisit.setVisibility(View.GONE);
        holder.btnDetail.setVisibility(View.GONE);

        holder.btnDetail.setOnClickListener(v -> listener.onViewDetailClicked(history, position));
        holder.btnRevisit.setOnClickListener(v -> listener.onRevisitRequestClicked(history, position));
        holder.btnViewLogs.setOnClickListener(v -> listener.onViewLogsClicked(history, position));

    }

    @Override
    public int getItemCount() {
        return requestDataList.size();
    }

    public static class RFIHistoryHolder extends RecyclerView.ViewHolder {

        private Button btnDetail, btnRevisit, btnViewLogs;
        private TextView requestIdTv, requestDateTv, statusTv, descriptionTextView;
        private TextView schemeNameTv, componentTypeTv, pointNameTv,stageNameTv;
        private TextView feTextView, locationTv, inspectionDateTv;
        private LinearLayout firstContainer, pointContainer,stageContainer, feContainer;
        private LinearLayout descriptionContainer;
        private View firstLineView, thirdLineView;
        private LinearLayout mainContainer;

        public RFIHistoryHolder(@NonNull View itemView) {
            super(itemView);
            btnDetail = itemView.findViewById(R.id.layout_inspection_history_view_detail_btn);
            btnRevisit = itemView.findViewById(R.id.layout_inspection_history_revisit_req_btn);
            btnViewLogs = itemView.findViewById(R.id.layout_inspection_history_view_logs_btn);

            requestIdTv = itemView.findViewById(R.id.layout_inspection_history_req_id_tv);
            requestDateTv = itemView.findViewById(R.id.layout_inspection_history_req_date_tv);
            statusTv = itemView.findViewById(R.id.layout_inspection_history_status_tv);
            schemeNameTv = itemView.findViewById(R.id.layout_inspection_history_scheme_name_tv);
            componentTypeTv = itemView.findViewById(R.id.layout_inspection_history_comp_type_tv);
            pointNameTv = itemView.findViewById(R.id.layout_inspection_history_point_name_tv);
            stageNameTv = itemView.findViewById(R.id.layout_inspection_history_stage_name_tv);
            feTextView = itemView.findViewById(R.id.layout_inspection_history_f_e_name_tv);
            locationTv = itemView.findViewById(R.id.layout_inspection_history_location_tv);
            inspectionDateTv = itemView.findViewById(R.id.layout_inspection_history_ins_date_tv);
            firstContainer = itemView.findViewById(R.id.layout_inspection_history_first_container);
            firstLineView = itemView.findViewById(R.id.layout_inspection_first_line_view);
            pointContainer = itemView.findViewById(R.id.layout_inspection_point_container);
            stageContainer = itemView.findViewById(R.id.layout_inspection_stage_container);
            feContainer = itemView.findViewById(R.id.layout_inspection_history_fe_container);
            thirdLineView = itemView.findViewById(R.id.layout_inspection_third_line_view);
            descriptionContainer = itemView.findViewById(R.id.layout_inspection_desc_container);
            descriptionTextView = itemView.findViewById(R.id.layout_inspection_history_description_tv);
            mainContainer = itemView.findViewById(R.id.layout_inspection_history_main_container);
        }
    }

    public interface RFIHistoryListener {
        void onRevisitRequestClicked(InspectionRequest.InspectionRequestData history, int position);

        void onViewDetailClicked(InspectionRequest.InspectionRequestData history, int position);

        void onViewLogsClicked(InspectionRequest.InspectionRequestData history, int position);
    }
}
