package mapitgis.jalnigam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

public abstract class MenuAdapter extends BaseAdapter {
    private final List<Menu> menus;
    private final Context context;

    public MenuAdapter(@NonNull Context context) {
        this.menus = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Menu getItem(int i) {
        return menus.get(i);
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
            view = inflater.inflate(R.layout.adapter_menu,new FrameLayout(context),false);
            holder = new ViewHolder();
            holder.textViewName = view.findViewById(R.id.textViewName);
            holder.textViewDetail = view.findViewById(R.id.textViewDetail);
            holder.imageViewIcon1 = view.findViewById(R.id.imageViewIcon1);
            holder.imageViewIcon2 = view.findViewById(R.id.imageViewIcon2);
            holder.cardView  = view.findViewById(R.id.cardView);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Menu menu = getItem(i);
        holder.textViewName.setText(menu.getName());
        holder.textViewDetail.setText(menu.getDetail());
        holder.imageViewIcon1.setImageResource(menu.getIcon());
        holder.imageViewIcon2.setImageResource(menu.getIcon());
        holder.cardView.setCardBackgroundColor(menu.getColor(context));
        view.setOnClickListener(v->onClick(menu));
        return view;
    }

    protected abstract void onClick(@NonNull Menu menu);

    public void addAll(@NonNull List<Menu> menus) {
        this.menus.addAll(menus);
        notifyDataSetChanged();
    }

    public void clear() {
        this.menus.clear();
        notifyDataSetChanged();
    }

    public void add(Menu menu) {
        this.menus.add(menu);
    }

    private static class ViewHolder{
        private TextView textViewName,textViewDetail;
        private ImageView imageViewIcon1,imageViewIcon2;
        private CardView cardView;
    }
}
