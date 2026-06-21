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
import mapitgis.jalnigam.core.SpinnerItem;

public class StagesAdapter extends RecyclerView.Adapter<StagesAdapter.StagesHolder> implements Filterable {
    private Context context;
    private List<SpinnerItem> stagesList;
    private List<SpinnerItem> filteredList;
    private StagesListener listener;
    private String selectedStage;

    public StagesAdapter(Context context, List<SpinnerItem> stagesList, StagesListener listener, String selectedStage) {
        this.context = context;
        this.stagesList = stagesList;
        this.listener = listener;
        this.selectedStage = selectedStage;
        this.filteredList = stagesList;
    }

    @NonNull
    @Override
    public StagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_select_data_bottom_sheet, parent, false);
        return new StagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StagesHolder holder, int position) {
        SpinnerItem spinnerItem = filteredList.get(position);


        holder.nameTextView.setText(spinnerItem.getValue());
        holder.nameTextView.setOnClickListener(v -> listener.onSelectedStages(spinnerItem, position));

        if (selectedStage.equalsIgnoreCase(spinnerItem.getKeyString())) {
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
                    filteredList = stagesList;
                } else {

                    List<SpinnerItem> filterList = new ArrayList<>();

                    for (SpinnerItem name : stagesList) {
                        if (name.getValue().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredList = (ArrayList<SpinnerItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class StagesHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        public StagesHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.layout_select_data_bottom_name_text_view);
        }
    }

    public interface StagesListener {
        void onSelectedStages(SpinnerItem stages, int position);
    }
}
