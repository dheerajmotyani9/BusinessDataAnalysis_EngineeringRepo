/**
 * Created on : 13-09-2024
 */
package mapitgis.jalnigam.rfi.adapter.dhara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.model.dhara.EMFReading;

public class EMFReadingAdapter extends RecyclerView.Adapter<EMFReadingAdapter.EMFReadingHolder> {

    private Context context;
    private List<EMFReading> emfReadingList;
    private EMFReadingListener listener;

    public EMFReadingAdapter(Context context, List<EMFReading> emfReadingList, EMFReadingListener listener) {
        this.context = context;
        this.emfReadingList = emfReadingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EMFReadingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_emf_reading_list,parent,false);
        return new EMFReadingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EMFReadingHolder holder, int position) {

        EMFReading emfReading = emfReadingList.get(position);

        /*holder.esrNameTv.setText("ESR's Name: "+emfReading.getEsrName());
        holder.startEsrTv.setText("Start: "+emfReading.getStartEsr());
        holder.endEsrTv.setText("End: "+emfReading.getEndEsr());
        holder.totalEsrTv.setText("Total: "+emfReading.getTotalEsr());*/

        holder.villageNameTv.setText("Village name: "+emfReading.getVillageName());
        holder.startVillageTv.setText("Start: "+emfReading.getStartVillage());
        holder.endVillageTv.setText("End: "+emfReading.getEndVillage());
        holder.totalVillageTv.setText("Total: "+emfReading.getTotalVillage());

        holder.btnRemove.setOnClickListener(v -> listener.onRemoveClicked(position,emfReading));

    }

    @Override
    public int getItemCount() {
        return emfReadingList.size();
    }

    public static class EMFReadingHolder extends RecyclerView.ViewHolder {

        private final ImageView btnRemove;
        private TextView esrNameTv,startEsrTv,endEsrTv,totalEsrTv;
        private TextView villageNameTv,startVillageTv,endVillageTv,totalVillageTv;

        public EMFReadingHolder(@NonNull View itemView) {
            super(itemView);
            btnRemove = itemView.findViewById(R.id.layout_emf_reading_remove_iv);

           /* esrNameTv = itemView.findViewById(R.id.layout_emf_reading_esr_name_tv);
            startEsrTv = itemView.findViewById(R.id.layout_emf_reading_esr_start_tv);
            endEsrTv = itemView.findViewById(R.id.layout_emf_reading_esr_end_tv);
            totalEsrTv = itemView.findViewById(R.id.layout_emf_reading_esr_total_tv);*/

            villageNameTv = itemView.findViewById(R.id.layout_emf_reading_village_name_tv);
            startVillageTv = itemView.findViewById(R.id.layout_emf_reading_village_start_tv);
            endVillageTv = itemView.findViewById(R.id.layout_emf_reading_village_end_tv);
            totalVillageTv = itemView.findViewById(R.id.layout_emf_reading_village_total_tv);
        }
    }

    public interface EMFReadingListener{
        void onRemoveClicked(int position, EMFReading emfReading);
    }
}
