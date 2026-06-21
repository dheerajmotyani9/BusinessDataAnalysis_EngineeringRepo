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
import mapitgis.jalnigam.room.table.BlockTable;

public class SelectBlockAdapter extends RecyclerView.Adapter<SelectBlockAdapter.SelectBlockHolder> implements Filterable {

    private Context context;
    private List<BlockTable> blockList;
    private List<BlockTable> filteredList;
    private SelectBlockListener listener;
    private String selectedBlock;

    public SelectBlockAdapter(Context context, List<BlockTable> blockList, SelectBlockListener listener,String selectedBlock) {
        this.context = context;
        this.blockList = blockList;
        this.filteredList = blockList;
        this.listener = listener;
        this.selectedBlock = selectedBlock;
    }

    @NonNull
    @Override
    public SelectBlockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_select_data_bottom_sheet, parent, false);
        return new SelectBlockHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectBlockHolder holder, int position) {
        BlockTable block = filteredList.get(position);

        holder.nameTextView.setText(block.getBlockName());
        holder.nameTextView.setOnClickListener(v -> listener.onBlockSelected(block, position));

        if(selectedBlock.equalsIgnoreCase(block.getBlockName())){
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
                    filteredList = blockList;
                } else {

                    List<BlockTable> filterList = new ArrayList<>();

                    for (BlockTable name : blockList) {
                        if (name.getBlockName().toLowerCase().contains(charString.toLowerCase())) {
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
                filteredList = (ArrayList<BlockTable>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class SelectBlockHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public SelectBlockHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.layout_select_data_bottom_name_text_view);
        }
    }

    public interface SelectBlockListener {
        void onBlockSelected(BlockTable block, int position);
    }
}
