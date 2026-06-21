package mapitgis.jalnigam.rfi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;

import java.util.List;

import mapitgis.jalnigam.BuildConfig;
import mapitgis.jalnigam.R;


public class FullImageViewPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<String> sliderImage;

    public FullImageViewPagerAdapter(Context context, List<String> sliderImage) {
        this.context = context;
        this.sliderImage = sliderImage;
        layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = null;
        view = layoutInflater.inflate(R.layout.layout_full_image_view_container, container, false);

        ImageView imageView = view.findViewById(R.id.layout_full_image_view);

        String image = sliderImage.get(position);
        Glide.with(context).load(BuildConfig.JAL_NIGAM_IMAGE + image).into(imageView);

        container.addView(view, 0);
        return view;
    }

    @Override
    public int getCount() {
        return sliderImage.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}