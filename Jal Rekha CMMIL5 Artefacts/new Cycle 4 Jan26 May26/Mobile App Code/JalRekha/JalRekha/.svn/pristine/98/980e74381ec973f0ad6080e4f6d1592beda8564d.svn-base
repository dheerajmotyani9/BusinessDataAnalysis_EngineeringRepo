package mapitgis.jalnigam;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.Module;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.databinding.AdapterSyncBinding;

public abstract class SyncAdapter extends BaseAdapter {
    private final List<Module> modules;
    private final Context context;

    public SyncAdapter(@NonNull Context context, @NonNull Login login) {
        this.modules = new ArrayList<>();

        if(login.isDailyInspection()){
            this.modules.add(Module.DI);
        }

        if (login.isAssets()) {
            this.modules.add(Module.ASSETS_MAPPING);
        }

        if (login.isDhara()) {
            this.modules.add(Module.DHARA);
        }

        if (login.isIsa()) {
            this.modules.add(Module.ISA);
        }

        if(login.isNirmal()){
            this.modules.add(Module.NIRMAL);
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return modules.size();
    }

    @Override
    public Module getItem(int i) {
        return modules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        AdapterSyncBinding holder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            holder = AdapterSyncBinding.inflate(inflater, new FrameLayout(context), false);
            view = holder.getRoot();
            view.setTag(holder);
        } else {
            holder = (AdapterSyncBinding) view.getTag();
        }
        Module module = getItem(i);
        holder.textViewName.setText(module.name);
        String ls = SqLite.instance(context).getLastTime(module);
        holder.textViewLastSync.setText(ls == null ? "N/A" : ls);
        holder.buttonSync.setOnClickListener(view1 -> onClick(module));
        return view;
    }

    protected abstract void onClick(@NonNull Module module);
}
