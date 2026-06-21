package mapitgis.jalnigam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import mapitgis.jalnigam.wms.TileProviderFactory;

public class LayerInfoAdapter extends BaseAdapter {
    private final List<LayerInfo> layerInfos;
    private final Context context;
    private final int type;

    public LayerInfoAdapter(@NonNull Context context,int type,List<LayerInfo> layerInfos) {
        this.layerInfos = layerInfos;
        this.type = type;
        this.context = context;
    }

    @Override
    public int getCount() {
        return layerInfos.size();
    }

    @Override
    public LayerInfo getItem(int i) {
        return layerInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.adapter_layer_info,new FrameLayout(context),false);
            holder = new ViewHolder();
            holder.textView = view.findViewById(R.id.textView);
            holder.checkBox = view.findViewById(R.id.checkBox);
            holder.imageView  = view.findViewById(R.id.imageView);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        LayerInfo layerInfo = getItem(i);
        holder.textView.setText(layerInfo.name);
        holder.checkBox.setChecked(layerInfo.check);
        holder.checkBox.setOnClickListener(v-> {
            layerInfo.toggleCheck();
            notifyDataSetChanged();
        });
        RequestOptions options = new RequestOptions().placeholder(R.drawable.picture_not_available).error(R.drawable.picture_not_available);
        Glide.with(context).load(TileProviderFactory.getGlideURL(type==1?layerInfo.component.getLayer():layerInfo.component.getSurveyedLayer())).apply(options).into(holder.imageView);
        return view;
    }

    public void addAll(@NonNull List<LayerInfo> layerInfos) {
        this.layerInfos.addAll(layerInfos);
        notifyDataSetChanged();
    }

    public void clear() {
        this.layerInfos.clear();
        notifyDataSetChanged();
    }

    public void add(LayerInfo layerInfo) {
        this.layerInfos.add(layerInfo);
    }

    public List<LayerInfo> getItems() {
        return layerInfos;
    }

    private static class ViewHolder{
        private TextView textView;
        private ImageView imageView;
        private CheckBox checkBox;
    }
}
