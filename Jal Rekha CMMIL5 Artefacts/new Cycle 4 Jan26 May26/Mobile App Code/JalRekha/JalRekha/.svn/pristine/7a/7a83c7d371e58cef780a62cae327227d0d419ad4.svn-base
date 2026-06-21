package mapitgis.jalnigam.rfi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.rfi.model.ContractorDISurvey;

public class ContractorDISurveyAdapter extends RecyclerView.Adapter<ContractorDISurveyAdapter.ContractorDISurveyHolder> {

    private Context context;
    private List<ContractorDISurvey.ContractorDISurveyData> contractorDISurveyList;
    private ContractorDISurveyListener listener;
    private Login login;

    public ContractorDISurveyAdapter(Context context, List<ContractorDISurvey.ContractorDISurveyData> contractorDISurveyList, ContractorDISurveyListener listener) {
        this.context = context;
        this.contractorDISurveyList = contractorDISurveyList;
        this.listener = listener;
        this.login = SqLite.instance(context).getLogin();
    }

    @NonNull
    @Override
    public ContractorDISurveyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contractor_di_survey_list, parent, false);
        return new ContractorDISurveyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractorDISurveyHolder holder, int position) {

        ContractorDISurvey.ContractorDISurveyData diSurvey = contractorDISurveyList.get(position);

        holder.componentTextView.setText("Component: " + diSurvey.getComponentName());
        holder.schemeTextView.setText("Scheme: " + diSurvey.getSchemeName());
        holder.villageTextView.setText(diSurvey.getVillageName());
        holder.gramTextView.setText(diSurvey.getGpName());
        holder.blockTextView.setText(diSurvey.getBlockName());
        holder.statusTextView.setText("Status: " + diSurvey.getQaQcReviewName());
        holder.remarkTextView.setText("Remark: " + diSurvey.getQaQcRemark());

        if (diSurvey.getInspectionStatus() != null) {

            if (login.getRoleId() == 9) {//RFI Contractor
//            if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
                if (diSurvey.getInspectionStatus().equalsIgnoreCase("2")) {
                    holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.redColor));
                    holder.statusTextView.setVisibility(View.VISIBLE);
                    holder.btnUpdateStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                    holder.statusTextView.setVisibility(View.GONE);
                    holder.btnUpdateStatus.setVisibility(View.GONE);
                }
            } else {
                if (diSurvey.getInspectionStatus().equalsIgnoreCase("3")) {
                    holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.redColor));
                    holder.statusTextView.setVisibility(View.VISIBLE);
                    holder.statusTextView.setText("");
                    holder.btnUpdateStatus.setVisibility(View.VISIBLE);
                } else {
                    holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                    holder.statusTextView.setVisibility(View.GONE);
                    holder.btnUpdateStatus.setVisibility(View.GONE);
                }
            }


        } else {
            holder.statusTextView.setVisibility(View.GONE);
            holder.btnUpdateStatus.setVisibility(View.GONE);
        }


        holder.btnUpdateStatus.setOnClickListener(v -> listener.onUpdateStatusClicked(diSurvey));
        holder.btnViewLogs.setOnClickListener(v -> listener.onLogsClicked(diSurvey));
    }

    @Override
    public int getItemCount() {
        return contractorDISurveyList.size();
    }

    public static class ContractorDISurveyHolder extends RecyclerView.ViewHolder {
        private TextView btnUpdateStatus, btnViewLogs;
        private TextView componentTextView, schemeTextView, villageTextView;
        private TextView gramTextView, blockTextView;
        private TextView statusTextView, remarkTextView;

        public ContractorDISurveyHolder(@NonNull View itemView) {
            super(itemView);
            btnUpdateStatus = itemView.findViewById(R.id.layout_contractor_di_btn_status);
            componentTextView = itemView.findViewById(R.id.layout_contractor_di_component_tv);
            schemeTextView = itemView.findViewById(R.id.layout_contractor_di_scheme_tv);
            gramTextView = itemView.findViewById(R.id.layout_contractor_di_gram_tv);
            villageTextView = itemView.findViewById(R.id.layout_contractor_di_village_tv);
            blockTextView = itemView.findViewById(R.id.layout_contractor_di_block_tv);
            statusTextView = itemView.findViewById(R.id.layout_contractor_di_status_tv);
            remarkTextView = itemView.findViewById(R.id.layout_contractor_di_remark_tv);
            btnViewLogs = itemView.findViewById(R.id.layout_contractor_di_btn_logs);
        }
    }

    public interface ContractorDISurveyListener {
        void onUpdateStatusClicked(ContractorDISurvey.ContractorDISurveyData diSurvey);

        void onLogsClicked(ContractorDISurvey.ContractorDISurveyData diSurvey);
    }
}
