/**
 * Created on : 30-09-2024 04:26 pm
 */
package mapitgis.jalnigam.rfi.adapter.dhara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.model.dhara.DailyWaterSupply;

public class DharaDailyWaterSupplyAdapter extends RecyclerView.Adapter<DharaDailyWaterSupplyAdapter.DailyWaterSupplyHolder> implements Filterable {

    private Context context;
    private List<DailyWaterSupply.DailyWaterSupplyData> dailyWaterSupplyList;
    private List<DailyWaterSupply.DailyWaterSupplyData> filteredWaterSupplyList;
    private DailyWaterSupplyListener listener;

    public DharaDailyWaterSupplyAdapter(Context context, List<DailyWaterSupply.DailyWaterSupplyData> dailyWaterSupplyList, DailyWaterSupplyListener listener) {
        this.context = context;
        this.dailyWaterSupplyList = dailyWaterSupplyList;
        this.listener = listener;
        filteredWaterSupplyList = dailyWaterSupplyList;
    }

    @NonNull
    @Override
    public DailyWaterSupplyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_daily_water_supply_list,parent,false);
        return new DailyWaterSupplyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWaterSupplyHolder holder, int position) {

        DailyWaterSupply.DailyWaterSupplyData dailyWaterSupply = filteredWaterSupplyList.get(position);

        holder.esrNameTv.setText(dailyWaterSupply.getEsrName());
        holder.intakeNameTv.setText(dailyWaterSupply.getIntakeName());
        holder.wtpNameTv.setText(dailyWaterSupply.getWtpName());
        holder.piuNameText.setText(dailyWaterSupply.getPiuName());
        holder.schemeNameTv.setText(dailyWaterSupply.getIntakeName());
        holder.allotTypeTv.setText(dailyWaterSupply.getAllotmentTypeName());

        if (dailyWaterSupply.getAllotmentTypeName().equalsIgnoreCase("esr")){
            holder.esrTitleTv.setVisibility(View.VISIBLE);
            holder.esrNameTv.setVisibility(View.VISIBLE);
        }else{
            holder.esrTitleTv.setVisibility(View.GONE);
            holder.esrNameTv.setVisibility(View.GONE);
        }

        holder.btnStart.setOnClickListener(v -> listener.oStartClicked(dailyWaterSupply));
        holder.btnDetail.setOnClickListener(v -> listener.onDetailClicked(dailyWaterSupply));
    }

    @Override
    public int getItemCount() {
        return filteredWaterSupplyList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredWaterSupplyList = dailyWaterSupplyList;
                } else {

                    List<DailyWaterSupply.DailyWaterSupplyData> filterList = new ArrayList<>();

                    for (DailyWaterSupply.DailyWaterSupplyData name : dailyWaterSupplyList) {
                        String componentName = name.getAllotmentTypeName() == null ? "":name.getAllotmentTypeName();
                        if (componentName.toLowerCase().contains(charString.toLowerCase()) ) {
                            filterList.add(name);
                        }
                        filteredWaterSupplyList = filterList;
                    }

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredWaterSupplyList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredWaterSupplyList = (ArrayList<DailyWaterSupply.DailyWaterSupplyData>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class DailyWaterSupplyHolder extends RecyclerView.ViewHolder {
        private Button btnStart,btnDetail;
        private TextView schemeNameTv,piuNameText,intakeNameTv,wtpNameTv,esrNameTv;
        private TextView allotTypeTv,esrTitleTv;
        public DailyWaterSupplyHolder(@NonNull View itemView) {
            super(itemView);
            btnStart = itemView.findViewById(R.id.layout_daily_water_supply_btn_start);
            btnDetail = itemView.findViewById(R.id.layout_daily_water_supply_btn_detail);
            schemeNameTv = itemView.findViewById(R.id.layout_daily_water_supply_scheme_name_tv);
            piuNameText = itemView.findViewById(R.id.layout_daily_water_supply_piu_name_tv);
            intakeNameTv = itemView.findViewById(R.id.layout_daily_water_supply_intake_name_tv);
            wtpNameTv = itemView.findViewById(R.id.layout_daily_water_supply_wtp_name_tv);
            esrNameTv = itemView.findViewById(R.id.layout_daily_water_supply_esr_name_tv);
            allotTypeTv = itemView.findViewById(R.id.layout_daily_water_allot_type_tv);
            esrTitleTv = itemView.findViewById(R.id.layout_daily_water_supply_esr_title_tv);
        }
    }

    public interface DailyWaterSupplyListener {
        void onDetailClicked(DailyWaterSupply.DailyWaterSupplyData supply);
        void oStartClicked(DailyWaterSupply.DailyWaterSupplyData supply);
    }
}
