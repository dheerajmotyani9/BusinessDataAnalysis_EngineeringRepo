package mapitgis.jalnigam.di;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.ImgActivity;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.AdapterImageBinding;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final List<Bitmap> bitmaps;
    private final Context context;

    public ImageAdapter(@NonNull Context context,List<Bitmap> bitmaps) {
        this.context = context;
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void clear() {
        int len = this.bitmaps.size();
        this.bitmaps.clear();
        this.notifyItemRangeRemoved(0, len);
    }

    public void add(Bitmap bitmap) {
        int pos = getItemCount();
        this.bitmaps.add(bitmap);
        this.notifyItemInserted(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = bitmaps.get(position);
        holder.binding.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<Bitmap> bitmaps) {
        this.bitmaps.addAll(bitmaps);
        this.notifyDataSetChanged();
    }

    @NonNull
    public Bitmap getItem(int i) {
        return bitmaps.get(i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterImageBinding binding;

        @SuppressLint("NotifyDataSetChanged")
        public ViewHolder(@NonNull AdapterImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.imageView.setOnClickListener(view -> {
                Bitmap bitmap = bitmaps.get(getAdapterPosition());
                ImgActivity.openImage(context, Utility.base64(bitmap), context.getString(R.string.photo), 1);
            });
        }
    }
}
