package mapitgis.jalnigam.core;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.R;

class SpinnerAdapter extends BaseAdapter {
    private final List<SpinnerItem> allSpinnerItems;
    private final List<SpinnerItem> spinnerItems;
    private final Context context;
    private final OnSpinnerListener onSpinnerListener;
    private SpinnerItem spinnerItem;

    void filter(String string) {
        this.spinnerItems.clear();
        if (string.equals("")) {
            this.spinnerItems.addAll(this.allSpinnerItems);
        } else {
            for (SpinnerItem allSpinnerItem : this.allSpinnerItems) {
                if (allSpinnerItem.getValue().toLowerCase().contains(string.toLowerCase())) {
                    this.spinnerItems.add(allSpinnerItem);
                }
            }
        }
        notifyDataSetChanged();
    }

    SpinnerAdapter(List<SpinnerItem> spinnerItems, Context context, OnSpinnerListener onSpinnerListener) {
        this.allSpinnerItems = new ArrayList<>();
        this.allSpinnerItems.addAll(spinnerItems);
        this.spinnerItems = spinnerItems;
        this.context = context;
        this.onSpinnerListener = onSpinnerListener;
    }

    void setSpinnerItem(SpinnerItem spinnerItem) {
        this.spinnerItem = spinnerItem;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return spinnerItems.size();
    }

    @Override
    public SpinnerItem getItem(int position) {
        return spinnerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewGroup.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        textView.setTextColor(Color.BLACK);
        int dp16=(int)context.getResources().getDimension(R.dimen.dp16);
        textView.setPadding(dp16, dp16, dp16, dp16);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        convertView = textView;
        SpinnerItem spinnerItem = getItem(position);
        textView.setText(spinnerItem.getValue());
        textView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        if (this.spinnerItem != null) {
            if (this.spinnerItem.getKeyString().equals(spinnerItem.getKeyString())) {
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_color));
            }
        }
        convertView.setOnClickListener(v -> onSpinnerListener.onSpinnerClick(spinnerItem));
        return convertView;
    }

    interface OnSpinnerListener {
        void onSpinnerClick(SpinnerItem spinnerItem);
    }
}
