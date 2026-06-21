package mapitgis.jalnigam.core;

import android.view.View;
import android.view.ViewPropertyAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.GoogleMap;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.databinding.MapTypeBinding;

public class MapType {
    private final MapTypeBinding binding;
    @Nullable
    private GoogleMap googleMap;

    public void setGoogleMap(@Nullable GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public MapType(@NonNull MapTypeBinding binding, boolean visible) {
        this.binding = binding;
        animate(visible);
        binding.linearLayoutMT1.setOnClickListener(view -> {
            if (googleMap != null) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
            updateUI();
        });
        binding.linearLayoutMT2.setOnClickListener(view -> {
            if (googleMap != null) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
            updateUI();
        });
        binding.linearLayoutMT3.setOnClickListener(view -> {
            if (googleMap != null) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
            updateUI();
        });
        binding.linearLayoutMT4.setOnClickListener(view -> {
            if (googleMap != null) {
                googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            }
            updateUI();
        });
        this.updateUI();
    }

    private void updateUI() {
        binding.linearLayoutMT1.setBackgroundResource(R.drawable.map_bg);
        binding.linearLayoutMT2.setBackgroundResource(R.drawable.map_bg);
        binding.linearLayoutMT3.setBackgroundResource(R.drawable.map_bg);
        binding.linearLayoutMT4.setBackgroundResource(R.drawable.map_bg);
        if (googleMap != null) {
            switch (googleMap.getMapType()) {
                case GoogleMap.MAP_TYPE_SATELLITE:
                    binding.linearLayoutMT1.setBackgroundResource(R.drawable.map_bg_selected);
                    break;
                case GoogleMap.MAP_TYPE_TERRAIN:
                    binding.linearLayoutMT2.setBackgroundResource(R.drawable.map_bg_selected);
                    break;
                case GoogleMap.MAP_TYPE_NORMAL:
                    binding.linearLayoutMT3.setBackgroundResource(R.drawable.map_bg_selected);
                    break;
                case GoogleMap.MAP_TYPE_NONE:
                    binding.linearLayoutMT4.setBackgroundResource(R.drawable.map_bg_selected);
                    break;
            }
        }
    }



    public void animate(boolean visible) {
        if (visible) {
            if(binding.getRoot().getVisibility() != View.VISIBLE) {
                // If the view is not visible, show it from the right side
                binding.getRoot().setTranslationX(binding.getRoot().getWidth());
                binding.getRoot().setVisibility(View.VISIBLE);
                ViewPropertyAnimator animator = binding.getRoot().animate();
                animator.translationX(0);
            }
        } else {
            if(binding.getRoot().getVisibility() == View.VISIBLE) {
                binding.getRoot().animate().translationXBy(binding.getRoot().getWidth()).withEndAction(() -> binding.getRoot().setVisibility(View.GONE));
            }
        }
    }

    public void toggle() {
        animate(binding.getRoot().getVisibility() != View.VISIBLE);
        updateUI();
    }

    public boolean isHidden() {
        return binding.getRoot().getVisibility() != View.VISIBLE;
    }

    public void hideIFOpen() {
        if(!isHidden()){
            toggle();
        }
    }
}
