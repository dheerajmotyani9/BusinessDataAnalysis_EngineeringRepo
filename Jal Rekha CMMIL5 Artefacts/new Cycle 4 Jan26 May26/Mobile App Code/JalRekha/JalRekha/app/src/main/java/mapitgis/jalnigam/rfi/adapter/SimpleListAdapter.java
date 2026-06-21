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

public class SimpleListAdapter<T> extends RecyclerView.Adapter<SimpleListAdapter.MyHolder> implements Filterable {

    private final Context context;
    private List<T> itemList;
    private List<T> filteredList;
    private final OnItemSelectListener<T> listener;
    private T selectedItem;

    public SimpleListAdapter(Context context, List<T> itemList, OnItemSelectListener<T> listener, T selectedItem) {
        this.context = context;
        this.itemList = itemList;
        this.filteredList = new ArrayList<>(itemList);
        this.listener = listener;
        this.selectedItem = selectedItem;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_select_data_bottom_sheet, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        T item = filteredList.get(position);
        String label = item.toString(); // You can customize this via interface if needed

        holder.nameTextView.setText(label);
        holder.nameTextView.setOnClickListener(v -> listener.onItemSelected(item, position));

        if (item.equals(selectedItem)) {
            holder.nameTextView.setTextColor(Color.RED);
            holder.nameTextView.setTypeface(holder.nameTextView.getTypeface(), Typeface.BOLD);
        } else {
            holder.nameTextView.setTextColor(Color.BLACK);
            holder.nameTextView.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchText = constraint != null ? constraint.toString().trim().toLowerCase() : "";

                List<T> resultList = new ArrayList<>();
                if (searchText.isEmpty()) {
                    resultList = new ArrayList<>(itemList);
                } else {
                    for (T item : itemList) {
                        if (item.toString().toLowerCase().contains(searchText)) {
                            resultList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = resultList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<T>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.layout_select_data_bottom_name_text_view);
        }
    }

    public interface OnItemSelectListener<T> {
        void onItemSelected(T item, int position);
    }

    public void setSelectedItem(T selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public void updateList(List<T> newList) {
        this.itemList = new ArrayList<>(newList);
        this.filteredList = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}
