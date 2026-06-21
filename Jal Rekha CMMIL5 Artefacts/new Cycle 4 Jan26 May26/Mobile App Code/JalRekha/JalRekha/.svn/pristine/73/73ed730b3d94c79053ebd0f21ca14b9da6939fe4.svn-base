package mapitgis.jalnigam.rfi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import mapitgis.jalnigam.room.table.PointTable;

public class SelectPointAdapter extends RecyclerView.Adapter<SelectPointAdapter.SelectPointHolder> implements Filterable {

    private Context context;
    private List<PointTable> pointList;
    private List<PointTable> filteredList;
    private SelectPointListener listener;
    private String selectedPoint;

    public SelectPointAdapter(Context context, List<PointTable> pointList, SelectPointListener listener,String selectedPoint) {
        this.context = context;
        this.pointList = pointList;
        this.filteredList = pointList;
        this.listener = listener;
        this.selectedPoint = selectedPoint;
    }

    @NonNull
    @Override
    public SelectPointHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_select_data_bottom_sheet, parent, false);
        return new SelectPointHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPointHolder holder, int position) {
        PointTable point = filteredList.get(position);

        holder.nameTextView.setText(point.getName());
        holder.nameTextView.setOnClickListener(v -> listener.onPointSelected(point,position));

        if(point.getName().equalsIgnoreCase(selectedPoint)){
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

                if (charString.isEmpty()) {
                    filteredList = pointList;
                } else {

                    List<PointTable> filterList = new ArrayList<>();

                    for (PointTable name : pointList) {
                        if (name.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredList = (ArrayList<PointTable>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class SelectPointHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        public SelectPointHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.layout_select_data_bottom_name_text_view);
        }
    }

    public interface SelectPointListener {
        void onPointSelected(PointTable pointTable,int position);
    }
}
