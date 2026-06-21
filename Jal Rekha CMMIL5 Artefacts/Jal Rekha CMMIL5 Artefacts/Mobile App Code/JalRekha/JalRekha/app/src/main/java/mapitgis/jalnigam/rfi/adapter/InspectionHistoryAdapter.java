package mapitgis.jalnigam.rfi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.room.table.InspectionRequestTable;

public class InspectionHistoryAdapter extends RecyclerView.Adapter<InspectionHistoryAdapter.InspectionHistoryHolder> implements Filterable {
    private Context context;
    private List<InspectionRequestTable> inspectionHistoryList;
    private List<InspectionRequestTable> filteredInspectionHistoryList;
    private InspectionHistoryListener listener;
    private Login login;

    public InspectionHistoryAdapter(Context context, List<InspectionRequestTable> inspectionHistoryList, InspectionHistoryListener listener) {
        this.context = context;
        this.inspectionHistoryList = inspectionHistoryList;
        this.listener = listener;
        login = SqLite.instance(context).getLogin();
      filteredInspectionHistoryList = inspectionHistoryList;

    }

    @NonNull
    @Override
    public InspectionHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_monitoring_inspection_history_list, parent, false);
        return new InspectionHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectionHistoryHolder holder, int position) {
        InspectionRequestTable history = filteredInspectionHistoryList.get(position);

        holder.schemeNameTv.setText(history.getSchemeName());
        holder.componentTypeTv.setText(history.getComponentName());
        holder.pointNameTv.setText(history.getPointName());
        holder.stageNameTv.setText(history.getStageName());
        holder.locationTv.setText(history.getLocation());
        holder.inspectionDateTv.setText(history.getInspectionDate());
        holder.descriptionTextView.setText(history.getDescription());
        holder.requestIdTv.setText(history.getRfiId().equals("-1") ? "":history.getRfiId());
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
        if (history.getUploadStatus() == 0) {
            holder.firstContainer.setVisibility(View.GONE);
            holder.thirdLineView.setVisibility(View.GONE);
            holder.firstLineView.setVisibility(View.GONE);
            holder.feContainer.setVisibility(View.GONE);
            holder.descriptionContainer.setVisibility(View.VISIBLE);

            holder.btnDetail.setText("Upload");
            holder.btnRevisit.setText("Remove");

            holder.btnDetail.setBackground(ContextCompat.getDrawable(context, R.drawable.success_btn_shape));
            holder.btnRevisit.setBackground(ContextCompat.getDrawable(context, R.drawable.failed_button_shape));

            holder.btnViewLogs.setVisibility(View.GONE);

            holder.highlightMessageTv.setVisibility(View.VISIBLE);
            holder.highlightMessageTv.setBackgroundResource(R.drawable.failed_button_shape);
            holder.highlightMessageTv.setText("Your RFI in Offline mode. Please Upload it.");
        } else {
            holder.firstContainer.setVisibility(View.VISIBLE);
            holder.firstLineView.setVisibility(View.VISIBLE);
            holder.thirdLineView.setVisibility(View.VISIBLE);
            holder.feContainer.setVisibility(View.VISIBLE);
            holder.descriptionContainer.setVisibility(View.GONE);

            if (history.getRfiId().equalsIgnoreCase("-1")) {
                holder.btnViewLogs.setVisibility(View.GONE);

                holder.highlightMessageTv.setBackgroundResource(R.drawable.success_btn_shape);
                holder.highlightMessageTv.setVisibility(View.VISIBLE);
                holder.highlightMessageTv.setText("Your RFI is generated. Please Sync it.");
            } else {
                holder.btnViewLogs.setVisibility(View.VISIBLE);
                holder.highlightMessageTv.setVisibility(View.GONE);
            }

            holder.btnDetail.setText("View Detail");
            holder.btnRevisit.setText("Revisit Request");

            holder.btnDetail.setBackground(ContextCompat.getDrawable(context, R.drawable.button_shape));
            holder.btnRevisit.setBackground(ContextCompat.getDrawable(context, R.drawable.failed_button_shape));

            if (login.getRoleId() != 9) {//Not RFI Contractor
//            if (prefManager.getUserType().equalsIgnoreCase("field engineer")) {
                holder.btnRevisit.setVisibility(View.GONE);
            } else {
                holder.btnRevisit.setVisibility(View.VISIBLE);
            }

            if (history.getStatus().equalsIgnoreCase("6")) {
                holder.mainContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.redLightColor));
                holder.btnRevisit.setVisibility(View.VISIBLE);
                holder.btnReschedule.setVisibility(View.GONE);
            } else {
                holder.btnRevisit.setVisibility(View.GONE);
                holder.mainContainer.setBackgroundColor(Color.WHITE);
                holder.btnReschedule.setVisibility(View.VISIBLE);
            }
        }

        holder.btnDetail.setOnClickListener(v -> listener.onViewDetailClicked(history, position));
        holder.btnRevisit.setOnClickListener(v -> listener.onRevisitRequestClicked(history, position));
        holder.btnViewLogs.setOnClickListener(v -> listener.onViewLogsClicked(history, position));
        holder.btnReschedule.setOnClickListener(v -> listener.onRescheduleClicked(history, position));
    }

    @Override
    public int getItemCount() {
        return filteredInspectionHistoryList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredInspectionHistoryList = inspectionHistoryList;
                } else {

                    List<InspectionRequestTable> filterList = new ArrayList<>();

                    for (InspectionRequestTable name : inspectionHistoryList) {
                        String componentName = name.getComponentName() == null ? "":name.getComponentName();
                        String rfiId = name.getRfiId() == null ? "":name.getRfiId();
                        String insertDate = name.getInsertDate() == null ? "":name.getInsertDate();
                        String status = name.getStatusName() == null ? "":name.getStatusName();

                        if (rfiId.toLowerCase().contains(charString.toLowerCase()) ||
                               componentName.toLowerCase().contains(charString.toLowerCase()) ||
                                insertDate.equalsIgnoreCase(charString) ||
                                status.toLowerCase().contains(charString.toLowerCase()) ) {
                            filterList.add(name);
                        }
                        filteredInspectionHistoryList = filterList;
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredInspectionHistoryList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredInspectionHistoryList = (ArrayList<InspectionRequestTable>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class InspectionHistoryHolder extends RecyclerView.ViewHolder {

        private Button btnDetail, btnRevisit, btnViewLogs,btnReschedule;
        private TextView requestIdTv, requestDateTv, statusTv, descriptionTextView;
        private TextView schemeNameTv, componentTypeTv, pointNameTv,stageNameTv;
        private TextView feTextView, locationTv, inspectionDateTv,highlightMessageTv;
        private LinearLayout firstContainer, pointContainer,stageContainer, feContainer;
        private LinearLayout descriptionContainer;
        private View firstLineView, thirdLineView;
        private LinearLayout mainContainer;

        public InspectionHistoryHolder(@NonNull View itemView) {
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
            btnReschedule = itemView.findViewById(R.id.layout_inspection_history_reschedule_btn);
            highlightMessageTv = itemView.findViewById(R.id.layout_inspection_history_message_tv);
        }
    }

    public interface InspectionHistoryListener {
        void onRevisitRequestClicked(InspectionRequestTable history, int position);

        void onViewDetailClicked(InspectionRequestTable history, int position);

        void onViewLogsClicked(InspectionRequestTable history, int position);

        void onRescheduleClicked(InspectionRequestTable history, int position);
    }

}
