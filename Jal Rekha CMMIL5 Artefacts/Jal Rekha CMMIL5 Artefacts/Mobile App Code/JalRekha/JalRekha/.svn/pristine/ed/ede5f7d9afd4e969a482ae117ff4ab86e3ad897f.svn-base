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
import mapitgis.jalnigam.room.table.ComponentTypeTable;

public class ComponentTypeAdapter extends RecyclerView.Adapter<ComponentTypeAdapter.ComponentTypeHolder> implements Filterable {

    private Context context;
    private List<ComponentTypeTable> componentTypeList;
    private List<ComponentTypeTable> filteredList;
    private ComponentTypeListener listener;
    private String selectedComponent;

    public ComponentTypeAdapter(Context context, List<ComponentTypeTable> componentTypeList, ComponentTypeListener listener, String selectedComponent) {
        this.context = context;
        this.componentTypeList = componentTypeList;
        this.listener = listener;
        this.selectedComponent = selectedComponent;
        this.filteredList = componentTypeList;
    }

    @NonNull
    @Override
    public ComponentTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_select_data_bottom_sheet, parent, false);
        return new ComponentTypeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComponentTypeHolder holder, int position) {
        ComponentTypeTable type = filteredList.get(position);


        holder.nameTextView.setText(type.getComponentName());
        holder.nameTextView.setOnClickListener(v -> listener.onSelectedComponentType(type, position));

        if (selectedComponent.equalsIgnoreCase(type.getComponentName())) {
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
                    filteredList = componentTypeList;
                } else {

                    List<ComponentTypeTable> filterList = new ArrayList<>();

                    for (ComponentTypeTable name : componentTypeList) {
                        if (name.getComponentName().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredList = (ArrayList<ComponentTypeTable>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ComponentTypeHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        public ComponentTypeHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.layout_select_data_bottom_name_text_view);
        }
    }

    public interface ComponentTypeListener {
        void onSelectedComponentType(ComponentTypeTable componentType, int position);
    }
}
