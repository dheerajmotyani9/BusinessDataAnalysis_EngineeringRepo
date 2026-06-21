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
import mapitgis.jalnigam.room.table.GramTable;

public class SelectGramAdapter extends RecyclerView.Adapter<SelectGramAdapter.SelectGramHolder>implements Filterable {

    private Context context;
    private List<GramTable> gramTableList;
    private List<GramTable> filteredList;
    private SelectGramListener listener;
    private String selectedGram;

    public SelectGramAdapter(Context context, List<GramTable> gramTableList, SelectGramListener listener,String selectedGram) {
        this.context = context;
        this.gramTableList = gramTableList;
        this.filteredList = gramTableList;
        this.listener = listener;
        this.selectedGram = selectedGram;
    }

    @NonNull
    @Override
    public SelectGramHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_select_data_bottom_sheet, parent, false);
        return new SelectGramHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectGramHolder holder, int position) {
        GramTable gram = filteredList.get(position);

        holder.nameTextView.setText(gram.getGramName());
        holder.nameTextView.setOnClickListener(v -> listener.onGramSelected(gram, position));

        if(selectedGram.equalsIgnoreCase(gram.getGramName())){
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
                    filteredList = gramTableList;
                } else {

                    List<GramTable> filterList = new ArrayList<>();

                    for (GramTable name : gramTableList) {
                        if (name.getGramName().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredList = (ArrayList<GramTable>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class SelectGramHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public SelectGramHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.layout_select_data_bottom_name_text_view);
        }
    }


    public interface SelectGramListener {
        void onGramSelected(GramTable gram, int position);
    }
}
