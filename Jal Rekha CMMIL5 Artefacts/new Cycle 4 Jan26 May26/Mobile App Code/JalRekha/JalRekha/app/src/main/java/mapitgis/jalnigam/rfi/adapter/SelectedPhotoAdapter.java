package mapitgis.jalnigam.rfi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import mapitgis.jalnigam.R;

public class SelectedPhotoAdapter extends RecyclerView.Adapter<SelectedPhotoAdapter.SelectedPhotoHolder> {

    private Context context;
    private List<String>selectedPhotosList;
    private SelectedPhotoListener listener;

    public SelectedPhotoAdapter(Context context, List<String> selectedPhotosList, SelectedPhotoListener listener) {
        this.context = context;
        this.selectedPhotosList = selectedPhotosList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectedPhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_selected_photos_list,parent,false);
        return new SelectedPhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedPhotoHolder holder, int position) {
        String photos = selectedPhotosList.get(position);
        Glide.with(context).load(photos).into(holder.photoImageView);
        holder.removeImageView.setOnClickListener(v -> listener.onPhotoClicked(position,photos));
    }

    @Override
    public int getItemCount() {
        return selectedPhotosList.size();
    }

    public static class SelectedPhotoHolder extends RecyclerView.ViewHolder {

        private final ImageView photoImageView,removeImageView;

        public SelectedPhotoHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.layout_selected_photo_image_view);
            removeImageView = itemView.findViewById(R.id.layout_selected_remove_image_view);
        }
    }

    public interface SelectedPhotoListener{
        void onPhotoClicked(int position, String photos);
    }

}
