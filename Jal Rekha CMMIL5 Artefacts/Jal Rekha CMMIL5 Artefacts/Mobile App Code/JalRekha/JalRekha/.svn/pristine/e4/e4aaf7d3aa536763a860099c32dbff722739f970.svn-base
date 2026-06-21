package mapitgis.jalnigam.dhara;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.databinding.ActivityDharaHistoryDetailBinding;
import mapitgis.jalnigam.rfi.model.dhara.DharaESRHistory;

public class DharaHistoryDetailActivity extends AppCompatActivity {

    private ImageView backImageView;
    private TextView titleTextView;

    private ActivityDharaHistoryDetailBinding binding;

    private DharaESRHistory.DharaESRHistoryData data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_dhara_history_detail);
        binding = ActivityDharaHistoryDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        data = (DharaESRHistory.DharaESRHistoryData) getIntent().getSerializableExtra("data");

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Detail");
        backImageView.setOnClickListener(v -> finish());

        binding.dharaHistoryDetailSchemeNameTv.setText(data.getIntakeName());
        binding.dharaHistoryDetailEsrNameTv.setText(data.getEsrName());
        binding.dharaHistoryDetailVillageNameTv.setText(data.getVillageName());
        binding.dharaHistoryDetailWtpTv.setText(data.getWtpName());
        binding.dharaHistoryDetailIntakeTv.setText(data.getIntakeName());
        binding.dharaHistoryDetailEsrSurveyDateTv.setText(data.getSurveyDate());
        binding.dharaHistoryDetailSurveyTimeTv.setText(data.getSurveyTime());
        binding.dharaHistoryDetailRemarkTv.setText(data.getRemarks());

        binding.dharaHistoryDetailEsrStartTv.setText(data.getEsrStartReading());
        binding.dharaHistoryDetailEsrEndTv.setText(data.getEsrEndReading());
        binding.dharaHistoryDetailEsrTotalTv.setText(data.getEsrTotalWaterSupplied());

        binding.dharaHistoryDetailVillageStartTv.setText(data.getStartReading());
        binding.dharaHistoryDetailVillageEndTv.setText(data.getEndReading());
        binding.dharaHistoryDetailVillageTotalTv.setText(data.getTotalWaterSupplied());
    }
}