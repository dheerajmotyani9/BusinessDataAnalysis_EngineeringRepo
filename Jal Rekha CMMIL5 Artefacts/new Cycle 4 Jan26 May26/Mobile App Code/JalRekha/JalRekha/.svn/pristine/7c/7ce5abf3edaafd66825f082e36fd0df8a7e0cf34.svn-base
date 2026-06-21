package mapitgis.jalnigam.dhara;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.databinding.ActivityDharaHistoryBinding;
import mapitgis.jalnigam.rfi.adapter.dhara.DharaHistoryAdapter;
import mapitgis.jalnigam.rfi.adapter.dhara.HistoryTabAdapter;
import mapitgis.jalnigam.dhara.fragment.DharaESRFragment;
import mapitgis.jalnigam.dhara.fragment.DharaIntakeFragment;
import mapitgis.jalnigam.dhara.fragment.DharaWTPFragment;
import mapitgis.jalnigam.rfi.model.dhara.DharaHistory;

public class DharaHistoryActivity extends AppCompatActivity implements DharaHistoryAdapter.DharaHistoryListener {

    private ActivityDharaHistoryBinding binding;

    private ImageView backImageView;
    private TextView titleTextView;

    private List<DharaHistory> dharaHistoryList;
    private DharaHistoryAdapter historyAdapter;

    private int indicatorWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_dhara_history);
        binding = ActivityDharaHistoryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
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
        titleTextView.setText("Daily water supply history");
        backImageView.setOnClickListener(v -> finish());

       /*
        dharaHistoryList = new ArrayList<>();
        historyAdapter = new DharaHistoryAdapter(this,dharaHistoryList,this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        binding.dharaHistoryRecyclerView.setLayoutManager(manager);
        binding.dharaHistoryRecyclerView.setAdapter(historyAdapter);

        getHistory();
        */

        initTabs();
    }

    private void initTabs(){
        HistoryTabAdapter adapter = new HistoryTabAdapter(getSupportFragmentManager());
        adapter.addFragment(DharaIntakeFragment.newInstance(), "Intake");
        adapter.addFragment(DharaWTPFragment.newInstance(), "WTP");
        adapter.addFragment(DharaESRFragment.newInstance(), "ESR");
        binding.viewPager.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.viewPager);

        //Determine indicator width at runtime
        binding.tab.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = binding.tab.getWidth() / binding.tab.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) binding.indicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                binding.indicator.setLayoutParams(indicatorParams);
            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)binding.indicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset+i) * indicatorWidth ;
                params.leftMargin = (int) translationOffset;
                binding.indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    void getHistory(){
        for (int i = 0; i < 8; i++) {
            dharaHistoryList.add(new DharaHistory());
        }
    }

    @Override
    public void onViewDetailClicked(int position, DharaHistory history) {
        startActivity(new Intent(DharaHistoryActivity.this,DharaHistoryDetailActivity.class));
    }
}