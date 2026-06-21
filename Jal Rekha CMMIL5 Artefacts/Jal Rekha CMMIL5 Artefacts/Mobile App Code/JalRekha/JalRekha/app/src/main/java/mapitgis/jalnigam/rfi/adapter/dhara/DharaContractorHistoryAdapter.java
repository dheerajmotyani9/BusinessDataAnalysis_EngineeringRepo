/**
 * Created on : 21-10-2024 03:46 pm
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
import mapitgis.jalnigam.rfi.model.dhara.DharaCommentByContractor;

public class DharaContractorHistoryAdapter extends RecyclerView.Adapter<DharaContractorHistoryAdapter.DharaContractorHistoryHolder> {

    private Context context;
    private List<DharaCommentByContractor.DharaCommentByContractorData> dataList;
    private DharaContractorHistoryListener listener;

    public DharaContractorHistoryAdapter(Context context, List<DharaCommentByContractor.DharaCommentByContractorData> dataList, DharaContractorHistoryListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DharaContractorHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dhara_contractor_history_list,parent,false);
        return new DharaContractorHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DharaContractorHistoryHolder holder, int position) {
        DharaCommentByContractor.DharaCommentByContractorData data = dataList.get(position);

        holder.intakeTv.setText(data.getIntakeName());
        holder.wtpTv.setText(data.getWtpName());
        holder.esrTv.setText(data.getEsrName());
        holder.villageTv.setText(data.getVillageName());
        holder.statusTv.setText(data.getSurveyDate());
        holder.btnDetail.setOnClickListener(v -> listener.onDetailClicked(position,data));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DharaContractorHistoryHolder extends RecyclerView.ViewHolder {

        private TextView intakeTv,wtpTv,esrTv,villageTv,statusTv;
        private Button btnDetail;

        public DharaContractorHistoryHolder(@NonNull View itemView) {
            super(itemView);
            intakeTv = itemView.findViewById(R.id.layout_dhara_contractor_history_intake_text_view);
            wtpTv = itemView.findViewById(R.id.layout_dhara_contractor_history_wtp_text_view);
            esrTv = itemView.findViewById(R.id.layout_dhara_contractor_history_esr_text_view);
            villageTv = itemView.findViewById(R.id.layout_dhara_contractor_history_cillage_text_view);
            statusTv = itemView.findViewById(R.id.layout_dhara_contractor_history_status_text_view);
            btnDetail = itemView.findViewById(R.id.layout_dhara_contractor_history_btn_detail);
        }
    }

    public interface DharaContractorHistoryListener {
        void onDetailClicked(int position, DharaCommentByContractor.DharaCommentByContractorData data);
    }
}
