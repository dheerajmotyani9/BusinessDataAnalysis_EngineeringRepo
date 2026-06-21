package mapitgis.jalnigam.di;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.Asset;
import mapitgis.jalnigam.databinding.AdapterDiBinding;


public abstract class DIAdapter extends RecyclerView.Adapter<DIAdapter.ViewHolder> {
    private final List<DI> dis;

    public DIAdapter() {
        this.dis = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterDiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void clear() {
        int len = this.dis.size();
        this.dis.clear();
        this.notifyItemRangeRemoved(0, len);
    }

    public void add(DI di) {
        int pos = getItemCount();
        this.dis.add(di);
        this.notifyItemInserted(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DI di = dis.get(position);
        holder.binding.textViewPIU.setText(di.getPiu());
        holder.binding.textViewScheme.setText(di.getScheme());
        holder.binding.textViewComponent.setText(di.getComponent());
        holder.binding.textViewVillageGPBlock.setText(String.format("Village: %s ,GP: %s ,Block: %s", di.getVillage(), di.getGp(), di.getBlock()));
        holder.binding.textViewQCRemark.setText(di.getReview());

        if (di.isUploaded()) {
            holder.binding.linearLayoutButton.setVisibility(View.GONE);
        } else {
            holder.binding.linearLayoutButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dis.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<DI> dis) {
        this.dis.addAll(dis);
        this.notifyDataSetChanged();
    }

    @NonNull
    public DI getItem(int i) {
        return dis.get(i);
    }

    public void remove(int pos) {
        this.dis.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public void updateUploaded(int pos) {
        this.dis.get(pos).setUploaded(true);
        this.notifyItemChanged(pos);
    }

    public boolean isEmpty() {
        return dis.isEmpty();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterDiBinding binding;

        public ViewHolder(@NonNull AdapterDiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.buttonDiscard.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                onDiscard(pos,dis.get(pos));
            });
            binding.buttonUpload.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                onUpload(pos,dis.get(pos));
            });
        }
    }

    protected abstract void onUpload(int pos,@NonNull DI di);

    protected abstract void onDiscard(int pos,@NonNull DI di);
}
