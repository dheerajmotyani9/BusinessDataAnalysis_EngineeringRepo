package mapitgis.jalnigam.dhara1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.databinding.AdapterMeterStatusBinding;

public abstract class MeterStatusAdapter extends RecyclerView.Adapter<MeterStatusAdapter.ViewHolder> {
    private final Activity activity;
    private final List<SpinnerItem> items;
    private int selectedItem = 0;
    int extra;
    boolean enable = true;

    public void setSelectedItemPosition(int selectedItem) {
        int previousSelectedItemPosition = this.selectedItem;
        this.selectedItem = selectedItem;
        notifyItemChanged(previousSelectedItemPosition);
        notifyItemChanged(selectedItem);
    }

    public void setSelectedItem(int key) {
        setSelectedItem(String.valueOf(key));
    }
    public void setSelectedItem(String key) {
        int selectedItem = 0;
        for (int i=0;i<items.size();i++){
            if(items.get(i).getKeyString().equals(key)){
                selectedItem = i;
                break;
            }
        }
        setSelectedItemPosition(selectedItem);
    }

    abstract void onChange(SpinnerItem spinnerItem);

    public MeterStatusAdapter(Activity activity, List<SpinnerItem> items) {
        this.activity = activity;
        this.items = items;
    }
    public MeterStatusAdapter(Activity activity,int extra, List<SpinnerItem> items) {
        this.activity = activity;
        this.extra = extra;
        this.items = items;
    }

    @NonNull
    @Override
    public MeterStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterMeterStatusBinding.inflate(LayoutInflater.from(activity), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MeterStatusAdapter.ViewHolder holder, int position) {
        SpinnerItem item = items.get(position);
        holder.binding.textView.setText(item.getValue().replaceFirst(" ", "\n"));

        if (position == selectedItem) {
            holder.binding.textView.setEnabled(false);
            holder.binding.textView.setTextColor(ContextCompat.getColor(activity, R.color.white));
//            holder.binding.textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            holder.binding.textView.setEnabled(true);
            holder.binding.textView.setTextColor(ContextCompat.getColor(activity, R.color.gray));
//            holder.binding.textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(SpinnerItem item) {
        this.items.add(item);
    }

    public void clear() {
        items.clear();
    }

    public SpinnerItem getSelectedItem() {
        return items.get(selectedItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterMeterStatusBinding binding;

        public ViewHolder(@NonNull AdapterMeterStatusBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(v -> {
                if(enable) {
                    setSelectedItemPosition(getAdapterPosition());
                    onChange(getSelectedItem());
                }
            });
        }
    }
}
