package mapitgis.jalnigam.isa;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.databinding.AdapterIsaBinding;


public abstract class ISAAdapter extends RecyclerView.Adapter<ISAAdapter.ViewHolder> {
    private final List<ISA> isaList;

    public ISAAdapter() {
        this.isaList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterIsaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void clear() {
        int len = this.isaList.size();
        this.isaList.clear();
        this.notifyItemRangeRemoved(0, len);
    }

    public void add(ISA isa) {
        int pos = getItemCount();
        this.isaList.add(isa);
        this.notifyItemInserted(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ISA isa = isaList.get(position);
        holder.binding.textViewActivity.setText(isa.getActivity());

        holder.binding.textViewGP.setText(isa.getGp());
        holder.binding.textViewRemark.setText(isa.getRemark());
        holder.binding.textViewVillage.setText(isa.getVillage());

        holder.binding.imageViewDoc.setVisibility(isa.isDoc()?View.VISIBLE:View.GONE);
        holder.binding.imageViewPhoto.setVisibility(isa.isImg()?View.VISIBLE:View.GONE);

        if (isa.isUploaded()) {
            holder.binding.linearLayoutButton.setVisibility(View.GONE);
        } else {
            holder.binding.linearLayoutButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return isaList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<ISA> isaList) {
        this.isaList.addAll(isaList);
        this.notifyDataSetChanged();
    }

    @NonNull
    public ISA getItem(int i) {
        return isaList.get(i);
    }

    public void remove(int pos) {
        this.isaList.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public void updateUploaded(int pos) {
        this.isaList.get(pos).setUploaded(true);
        this.notifyItemChanged(pos);
    }

    public boolean isEmpty() {
        return this.isaList.isEmpty();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterIsaBinding binding;

        public ViewHolder(@NonNull AdapterIsaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.buttonDiscard.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                onDiscard(pos,isaList.get(pos));
            });
            binding.buttonUpload.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                onUpload(pos,isaList.get(pos));
            });
            binding.imageViewPhoto.setOnClickListener(v->{
                int pos = getAdapterPosition();
                onView(pos,isaList.get(pos),1);
            });
            binding.imageViewDoc.setOnClickListener(v->{
                int pos = getAdapterPosition();
                onView(pos,isaList.get(pos),2);
            });
//            binding.imageViewDoc2.setOnClickListener(v->{
//                int pos = getAdapterPosition();
//                onView(pos,isaList.get(pos),3);
//            });
        }
    }

    protected abstract void onUpload(int pos,@NonNull ISA isa);

    protected abstract void onDiscard(int pos,@NonNull ISA isa);

    protected abstract void onView(int pos,@NonNull ISA isa,int type);
}
