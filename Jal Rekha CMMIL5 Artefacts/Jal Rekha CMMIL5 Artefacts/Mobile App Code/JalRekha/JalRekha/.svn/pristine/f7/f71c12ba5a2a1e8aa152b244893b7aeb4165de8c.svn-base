package mapitgis.jalnigam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Objects;

import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityMultiImgBinding;
import mapitgis.jalnigam.di.ImageAdapter;

public class MultiImgActivity extends BaseActivity {
    private ActivityMultiImgBinding binding;
    private static JSONArray urls;

    public static void showImages(@NonNull Context context, @NonNull JSONArray urls, String name, int type) {
        Intent intent = new Intent(context, MultiImgActivity.class);
        MultiImgActivity.urls = urls;
//        intent.putExtra(Utility.G_KEY,url);
        intent.putExtra(Utility.G_KEY1,name);
        intent.putExtra(Utility.SF,type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultiImgBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar(binding.appbar.toolbar,true);
        String name = Objects.requireNonNull(getIntent().getStringExtra(Utility.G_KEY1));
        int type = getIntent().getIntExtra(Utility.SF,0);
        setTitle(name);


        ImageAdapter imageAdapter = new ImageAdapter(this,new ArrayList<>());
        binding.recyclerViewImages.setAdapter(imageAdapter);

        for (int i = 0; i < urls.length(); i++) {
            String url = urls.optString(i,"");
            if (type == 1) {
                imageAdapter.add(Utility.bitmap(url));
            } else {

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
