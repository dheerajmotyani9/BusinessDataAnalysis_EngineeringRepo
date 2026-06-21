package mapitgis.jalnigam.di;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.databinding.AdapterDiOptionBinding;


public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    private final List<SpinnerItem> spinnerItems;
    private final OnOptionClickListener onSpinnerItemClickListener;
    public SpinnerItem selectedSpinnerItem;

//    public void setPosition(int position) {
//        this.position = position;
//    }

    public SpinnerItem getSelectedSpinnerItem() {
        return selectedSpinnerItem;
    }

    public OptionAdapter(AppCompatActivity activity, List<SpinnerItem> spinnerItems, OnOptionClickListener onSpinnerItemClickListener) {
        this.spinnerItems = spinnerItems;
        this.onSpinnerItemClickListener = onSpinnerItemClickListener;
        selectedSpinnerItem = null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterDiOptionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void clear() {
        int len = this.spinnerItems.size();
        this.spinnerItems.clear();
        this.notifyItemRangeRemoved(0, len);
    }

    public void add(SpinnerItem spinnerItem) {
        this.spinnerItems.add(spinnerItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpinnerItem spinnerItem = spinnerItems.get(position);
        holder.binding.textViewTitle.setText(spinnerItem.getValue());
        holder.binding.checkBox.setChecked(selectedSpinnerItem != null && spinnerItem.getKeyString().equals(selectedSpinnerItem.getKeyString()));
    }

    @Override
    public int getItemCount() {
        return spinnerItems.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<SpinnerItem> spinnerItems) {
        this.spinnerItems.addAll(spinnerItems);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterDiOptionBinding binding;

        @SuppressLint("NotifyDataSetChanged")
        public ViewHolder(@NonNull AdapterDiOptionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(view -> {
                selectedSpinnerItem = spinnerItems.get(getAdapterPosition());
                notifyDataSetChanged();
                onSpinnerItemClickListener.onSpinnerItemClick(selectedSpinnerItem);
            });
            this.binding.checkBox.setOnClickListener(view -> {
                selectedSpinnerItem = spinnerItems.get(getAdapterPosition());
                notifyDataSetChanged();
                onSpinnerItemClickListener.onSpinnerItemClick(selectedSpinnerItem);
            });
        }
    }

    interface OnOptionClickListener{
        void onSpinnerItemClick(@NonNull SpinnerItem spinnerItem);
    }
}
