package mapitgis.jalnigam.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import mapitgis.jalnigam.R;

public class InlineSpinnerAdapter extends ArrayAdapter<SpinnerItem> {
    private final List<SpinnerItem> spinnerItems;
    private final Context context;
    private final int color;

    public InlineSpinnerAdapter(@NonNull Context context, List<SpinnerItem> spinnerItems, int color) {
        super(context, mapitgis.jalnigam.R.layout.spinner_list_item, new ArrayList<>());
        this.color = color;
        this.spinnerItems = spinnerItems;
        this.context = context;
    }

    @Override
    public void add(@Nullable SpinnerItem spinnerItem) {
        super.add(spinnerItem);
        this.spinnerItems.add(spinnerItem);
    }

    @Override
    public void addAll(@NonNull Collection<? extends SpinnerItem> collection) {
        super.addAll(collection);
        spinnerItems.addAll(collection);
    }

    @Override
    public int getCount() {
        return spinnerItems.size();
    }

    @Nullable
    @Override
    public SpinnerItem getItem(int position) {
        return spinnerItems.get(position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = Objects.requireNonNull(inflater).inflate(R.layout.spinner_list_item,  new FrameLayout(context));
            textView = convertView.findViewById(R.id.textView);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        SpinnerItem spinnerItem = getItem(position);
        textView.setText(Objects.requireNonNull(spinnerItem).getValue());

        return convertView;
    }

    @Override
    public void clear() {
        super.clear();
        spinnerItems.clear();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setSingleLine();
        textView.setMaxLines(1);
        textView.setPadding(15,0,0,0);
        textView.setTextColor(color);
        SpinnerItem spinnerItem = getItem(position);
        textView.setText(Objects.requireNonNull(spinnerItem).getValue());
        return textView;
    }
}