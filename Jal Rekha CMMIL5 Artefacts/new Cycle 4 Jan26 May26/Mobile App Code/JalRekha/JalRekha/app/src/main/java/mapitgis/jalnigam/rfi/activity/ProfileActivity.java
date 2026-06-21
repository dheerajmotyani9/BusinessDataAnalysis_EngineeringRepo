package mapitgis.jalnigam.rfi.activity;

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
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.databinding.ActivityProfileBinding;
import mapitgis.jalnigam.rfi.helper.PrefManager;

public class ProfileActivity extends AppCompatActivity {

    private ImageView backImageView;
    private TextView titleTextView;

    private PrefManager prefManager;

    private ActivityProfileBinding binding;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        //
        binding = ActivityProfileBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        login = SqLite.instance(this).getLogin();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));


        prefManager = new PrefManager(this);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Profile");
        backImageView.setOnClickListener(v -> finish());

        binding.inputProfileUserName.setText(login.getName());//prefManager.getUserName());
        binding.inputProfileEmail.setText(login.getEmail());//prefManager.getUserEmail());
        binding.inputProfileMobile.setText(login.getMobile());//prefManager.getUserMobile());
        binding.inputProfileDesignation.setText(login.getDesignation());//prefManager.getDesignation());
    }
}