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
import mapitgis.jalnigam.databinding.ActivityDharaContractorHistoryDetailBinding;
import mapitgis.jalnigam.rfi.model.dhara.DharaCommentByContractor;

public class DharaContractorHistoryDetailActivity extends AppCompatActivity {

    private ImageView backImageView;
    private TextView titleTextView;

    private ActivityDharaContractorHistoryDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivityDharaContractorHistoryDetailBinding.inflate(LayoutInflater.from(this));
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
        titleTextView.setText("History Detail");
        backImageView.setOnClickListener(v -> finish());

        DharaCommentByContractor.DharaCommentByContractorData data =
                (DharaCommentByContractor.DharaCommentByContractorData) getIntent().getSerializableExtra("data");

        binding.dharaContractorHistoryDetailApprovalDateTextView.setText(data.getApproveDate());
        binding.dharaContractorHistoryDetailApprovalRemarkTextView.setText(data.getApprovelRemarks());

        binding.dharaContractorHistoryDetailIntakeTextView.setText(data.getIntakeName());
        binding.dharaContractorHistoryDetailWtpTextView.setText(data.getWtpName());
        binding.dharaContractorHistoryDetailEsrTextView.setText(data.getEsrName());
        binding.dharaContractorHistoryDetailVillageTextView.setText(data.getVillageName());
        binding.dharaContractorHistoryDetailDateTextView.setText(data.getSurveyDate());

        binding.dharaContractorHistoryDetailContractorRemarkTextView.setText(data.getContractorRemarks());
       /* if (data.getContractorRemarkAcept().toString().isEmpty()){
            binding.dharaContractorHistoryDetailContractorStatusTextView.setText(data.getContractorRemarkDenay());
        }else{
            binding.dharaContractorHistoryDetailContractorStatusTextView.setText(data.getContractorRemarkAcept());
        }*/

    }
}