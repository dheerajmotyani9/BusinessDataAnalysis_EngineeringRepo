package mapitgis.jalnigam.dhara;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import mapitgis.jalnigam.databinding.ActivityDharaSupplyDetailsBinding;
import mapitgis.jalnigam.rfi.model.dhara.DailyWaterSupply;

public class DharaSupplyDetailsActivity extends AppCompatActivity {

    private ImageView backImageView;
    private TextView titleTextView;

    private ActivityDharaSupplyDetailsBinding binding;
    private DailyWaterSupply.DailyWaterSupplyData waterSupplyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_dhara_supply_details);
        binding = ActivityDharaSupplyDetailsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        waterSupplyData = (DailyWaterSupply.DailyWaterSupplyData) getIntent().getSerializableExtra("data");
        assert waterSupplyData != null;

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Water supply detail");
        backImageView.setOnClickListener(v -> finish());

        binding.dailyWaterDetailAllotTypeTv.setText(waterSupplyData.getAllotmentTypeName());
        binding.dailyWaterDetailPiuTv.setText(waterSupplyData.getPiuName());
        binding.dailyWaterDetailOmPartialTv.setText(waterSupplyData.getoMPartialoM());
        binding.dailyWaterDetailIntakeNameTv.setText(waterSupplyData.getIntakeName());
        binding.dailyWaterDetailWtpNameTv.setText(waterSupplyData.getWtpName());
        binding.dailyWaterDetailEsrNameTv.setText(waterSupplyData.getEsrName());
        binding.dailyWaterDetailSchemeNameTv.setText(waterSupplyData.getIntakeName());

        if (waterSupplyData.getAllotmentTypeName().equalsIgnoreCase("esr")){
            binding.dailyWaterDetailEsrNameTv.setVisibility(View.VISIBLE);
            binding.dailyWaterDetailEsrTitleTv.setVisibility(View.VISIBLE);
        }else{
            binding.dailyWaterDetailEsrNameTv.setVisibility(View.GONE);
            binding.dailyWaterDetailEsrTitleTv.setVisibility(View.GONE);
        }

        binding.btnDharaSupplyStartEntry.setOnClickListener(v -> {
            startActivity(new Intent(this, DharaWaterFormActivity.class)
                    .putExtra("data",waterSupplyData));
        });

    }
}