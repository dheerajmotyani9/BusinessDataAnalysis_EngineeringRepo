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
import mapitgis.jalnigam.room.table.ApplicationTypeTable;


public class ApplicationTypeAdapter extends RecyclerView.Adapter<ApplicationTypeAdapter.ApplicationTypeHolder> implements Filterable {

    private Context context;
    private List<ApplicationTypeTable> applicationTypeList;
    private List<ApplicationTypeTable> filteredList;
    private ApplicationTypeListener listener;
    private String selectedApp;

    public ApplicationTypeAdapter(Context context, List<ApplicationTypeTable> applicationTypeList, ApplicationTypeListener listener, String selectedApp) {
        this.context = context;
        this.applicationTypeList = applicationTypeList;
        this.filteredList = applicationTypeList;
        this.listener = listener;
        this.selectedApp = selectedApp;
    }

    @NonNull
    @Override
    public ApplicationTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_select_data_bottom_sheet, parent, false);
        return new ApplicationTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationTypeHolder holder, int position) {
        ApplicationTypeTable type = filteredList.get(position);

        holder.nameTextView.setText(type.getAppTypeName());
        holder.nameTextView.setOnClickListener(v -> listener.onApplicationTypeSelected(type, position));

        if (type.getAppTypeName().equalsIgnoreCase(selectedApp)) {
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
                    filteredList = applicationTypeList;
                } else {

                    List<ApplicationTypeTable> filterList = new ArrayList<>();

                    for (ApplicationTypeTable name : applicationTypeList) {
                        if (name.getAppTypeName().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredList = (ArrayList<ApplicationTypeTable>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ApplicationTypeHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        public ApplicationTypeHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.layout_select_data_bottom_name_text_view);
        }
    }

    public interface ApplicationTypeListener {
        void onApplicationTypeSelected(ApplicationTypeTable type, int position);
    }
}
