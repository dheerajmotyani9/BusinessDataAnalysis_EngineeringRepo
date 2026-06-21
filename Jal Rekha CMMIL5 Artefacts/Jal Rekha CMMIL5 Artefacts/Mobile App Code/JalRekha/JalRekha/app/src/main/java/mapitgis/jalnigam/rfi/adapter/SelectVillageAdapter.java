package mapitgis.jalnigam.rfi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.room.table.VillageTable;

public class SelectVillageAdapter extends RecyclerView.Adapter<SelectVillageAdapter.SelectVillageHolder>implements Filterable {

    private Context context;
    private List<VillageTable> villageList;
    private List<VillageTable> filteredList;
    private SelectVillageListener listener;
    private String selectedVillage;

    public SelectVillageAdapter(Context context, List<VillageTable> villageList, SelectVillageListener listener,String selectedVillage) {
        this.context = context;
        this.villageList = villageList;
        this.filteredList = villageList;
        this.listener = listener;
        this.selectedVillage = selectedVillage;
    }

    @NonNull
    @Override
    public SelectVillageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_select_data_bottom_sheet, parent, false);
        return new SelectVillageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectVillageHolder holder, int position) {
        VillageTable village = filteredList.get(position);

        holder.nameTextView.setText(village.getVillageName());
        holder.nameTextView.setOnClickListener(v -> listener.onVillageSelected(village, position));

        if(village.getVillageName().equalsIgnoreCase(selectedVillage)){
            holder.nameTextView.setTextColor(Color.RED);
            holder.nameTextView.setTypeface(holder.nameTextView.getTypeface(), Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();

                Log.e("TAG","CHA "+charString);

                if (charString.isEmpty()) {
                    filteredList = villageList;
                } else {

                    List<VillageTable> filterList = new ArrayList<>();

                    for (VillageTable name : villageList) {
                        if (name.getVillageName().toLowerCase().contains(charString.toLowerCase())) {
                            filterList.add(name);
                        }
                        filteredList = filterList;
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<VillageTable>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class SelectVillageHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        public SelectVillageHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.layout_select_data_bottom_name_text_view);
        }
    }


    public interface SelectVillageListener {
        void onVillageSelected(VillageTable village, int position);
    }
}
