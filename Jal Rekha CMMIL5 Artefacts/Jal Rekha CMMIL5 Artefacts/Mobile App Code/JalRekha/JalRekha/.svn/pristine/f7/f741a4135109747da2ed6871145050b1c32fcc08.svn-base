package mapitgis.jalnigam;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.core.SqLite;

public abstract class AssetAdapter extends BaseAdapter {
    private final List<Asset> assets;
    private final Context context;
    private final boolean all;

    public AssetAdapter(@NonNull Context context, boolean all) {
        this.assets = SqLite.instance(context).getAsset(all,SqLite.instance(context).getLogin().getMobile());
        this.context = context;
        this.all = all;
    }

    @Override
    public int getCount() {
        return assets.size();
    }

    @Override
    public Asset getItem(int i) {
        return assets.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.adapter_asset, new FrameLayout(context), false);
            holder = new ViewHolder();
            holder.imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
            holder.textViewScheme = view.findViewById(R.id.textViewScheme);
            holder.textViewReview = view.findViewById(R.id.textViewReview);
            holder.textViewESR = view.findViewById(R.id.textViewESR);
            holder.textViewComponent = view.findViewById(R.id.textViewComponent);
            holder.buttonUpload = view.findViewById(R.id.buttonUpload);
            holder.buttonDiscard = view.findViewById(R.id.buttonDiscard);
            holder.checkBox = view.findViewById(R.id.checkBox);
            holder.linearLayoutButton = view.findViewById(R.id.linearLayoutButton);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Asset asset = getItem(i);
        holder.textViewScheme.setText(asset.getSchemeName());
        holder.textViewReview.setText(asset.getRemark());
        holder.textViewESR.setText(asset.getEsrName());
        holder.textViewComponent.setText(asset.getComponentNameWithPoint());

        if (all || asset.isUploaded()) {
            holder.checkBox.setVisibility(View.GONE);
            holder.linearLayoutButton.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.linearLayoutButton.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(asset.isCheck());
            holder.checkBox.setOnClickListener(v -> {
                asset.changeCheck();
                onChange(getCheckedCount());
                notifyDataSetChanged();
            });
            holder.buttonDiscard.setOnClickListener(v -> onDiscard(asset));
            holder.buttonUpload.setOnClickListener(v -> onUpload(asset));
        }
        if(asset.isNoPhoto()){
            Glide.with(context).load(R.drawable.picture_not_available).into(holder.imageViewPhoto);
        }else if (asset.isPng()) {
            RequestOptions options = new RequestOptions().placeholder(R.drawable.picture_not_available).error(R.drawable.picture_not_available);
            Glide.with(context).load(asset.getPhotoURL()).apply(options).into(holder.imageViewPhoto);
        } else {
            try {
                byte[] decodedString = Base64.decode(asset.getPhoto(), Base64.DEFAULT);
                holder.imageViewPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            } catch (Exception ignored) {
                Glide.with(context).load(R.drawable.picture_not_available).into(holder.imageViewPhoto);
            }
        }
        return view;
    }

    protected abstract void onUpload(@NonNull Asset asset);

    protected abstract void onDiscard(@NonNull Asset asset);

    protected abstract void onChange(int count);

    private int getCheckedCount() {
        int count = 0;
        for (Asset asset : assets) {
            if (asset.isCheck()) count++;
        }
        return count;
    }

    @Nullable
    public Asset getCheckedItem() {
        for (Asset asset : this.assets) {
            if (asset.isCheck()) return asset;
        }
        return null;
    }
//    public void addAll(@NonNull List<Asset> assets) {
//        this.assets.addAll(assets);
//        notifyDataSetChanged();
//    }
//
//    public void clear() {
//        this.assets.clear();
//        notifyDataSetChanged();
//    }
//
//    public void add(Asset asset) {
//        this.assets.add(asset);
//    }


    public void remove(Asset asset) {
        this.assets.remove(asset);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView textViewScheme, textViewReview, textViewESR, textViewComponent;
        private ImageView imageViewPhoto;
        private Button buttonDiscard, buttonUpload;
        private CheckBox checkBox;
        private LinearLayout linearLayoutButton;
    }
}
