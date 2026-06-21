package mapitgis.jalnigam.rfi.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.adapter.FullImageViewPagerAdapter;

public class FullImageActivity extends AppCompatActivity {

    private ImageView backImageView;
    private TextView titleTextView;

    private ViewPager viewPager;
    private FullImageViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private List<String> photosList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_full_image);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Photos");
        backImageView.setOnClickListener(v -> finish());

        viewPager = findViewById(R.id.full_image_slider_view_pager);
        dotsLayout = findViewById(R.id.full_image_slider_layout_dots);
        photosList = getIntent().getStringArrayListExtra("list");

        Log.e("TAG",""+photosList);

        myViewPagerAdapter = new FullImageViewPagerAdapter(this, photosList);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        addBottomsDots(0);
    }

    private void addBottomsDots(int currentPage) {
        dots = new TextView[photosList.size()];

        int[] activeColor = new int[photosList.size()];
        int[] inactiveColor = new int[photosList.size()];

        for (int z = 0; z < photosList.size(); z++) {
            activeColor[z] = Color.BLACK;
            inactiveColor[z] = Color.GRAY;
        }

        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(inactiveColor[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(activeColor[currentPage]);
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomsDots(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}