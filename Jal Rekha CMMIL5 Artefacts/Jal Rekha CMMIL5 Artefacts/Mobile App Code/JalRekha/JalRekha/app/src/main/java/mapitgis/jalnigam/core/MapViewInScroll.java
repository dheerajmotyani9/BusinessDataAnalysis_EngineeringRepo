package mapitgis.jalnigam.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

public class MapViewInScroll extends MapView {
    public MapViewInScroll(@NonNull Context context) {
        super(context);
    }

    public MapViewInScroll(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MapViewInScroll(@NonNull Context context, @Nullable GoogleMapOptions options) {
        super(context, options);
    }

    public MapViewInScroll(@NonNull Context context, @NonNull AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
