package mapitgis.jalnigam.rfi.activity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityAddContractorBinding;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.AddContractor;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContractorActivity extends AppCompatActivity implements ProgressHelper.ShowDialogListener {

    private ActivityAddContractorBinding binding;

    private ImageView backImageView;
    private TextView titleTextView;

    private PrefManager prefManager;
    private boolean isPasswordVisible = false;
    private ProgressHelper progressHelper;
    private ApiInterface apiInterface;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        // setContentView(R.layout.activity_add_contractor);
        binding = ActivityAddContractorBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        login = SqLite.instance(this).getLogin();


        prefManager = new PrefManager(this);
        progressHelper = new ProgressHelper(this);
        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Add Contractor");
        backImageView.setOnClickListener(v -> finish());

        binding.inputAddContractorScheme.setText(prefManager.getSchemeName());

        binding.btnAddContractor.setOnClickListener(v -> addContractor());

        binding.addContractorViewLoginPasswordImageView.setOnClickListener(v -> viewPassword());

    }

    private void viewPassword() {
        if (!isPasswordVisible) {
            binding.addContractorInputLoginPassword.setTransformationMethod(null);
            binding.addContractorViewLoginPasswordImageView.setImageResource(R.drawable.outline_visibility_black_48dp);
            isPasswordVisible = true;
        } else {
            binding.addContractorInputLoginPassword.setTransformationMethod(new PasswordTransformationMethod());
            binding.addContractorViewLoginPasswordImageView.setImageResource(R.drawable.outline_visibility_off_black_48dp);
            isPasswordVisible = false;
        }

        binding.addContractorInputLoginPassword.setSelection(binding.addContractorInputLoginPassword.length());
    }

    private void addContractor() {
        String name = binding.inputAddContractorUserName.getText().toString().trim();
        String mobile = binding.inputAddContractorMobile.getText().toString().trim();
        String email = binding.inputAddContractorEmail.getText().toString().trim();
        String password = binding.addContractorInputLoginPassword.getText().toString().trim();

        if (name.isEmpty()) {
            progressHelper.message("Enter contractor name");
            return;
        }

        if (mobile.length() != 10) {
            progressHelper.message("Enter contractor valid mobile no.");
            return;
        }

        if (!progressHelper.isValidEmail(email)) {
            progressHelper.message("Enter contractor valid email.");
            return;
        }

        if (password.length() < 5) {
            progressHelper.message("Enter 5 or more digit password");
            return;
        }

        /*
        {
    "etoken": "3jiF7A37G04X00e5eBxMGIuTldJY05jSk7Kh5RSQV5pXt39CFSDx8b1lC0EdW8f5Zghjuw7Yf0G73KfJ4g4bK70y1lGtvkEa63lP",
    "scheme_id": "74",
    "email_id": "test@test.com",
    "mobile_number": "9191889191",
    "name": "Test",
    "pass": "test@123"
    }
         */

        Map<String, String> body = new HashMap<>();
        body.put(Utility.E_TOKEN, login.getToken());//prefManager.getToken1());
        body.put("scheme_id", prefManager.getSchemeId());
        body.put("email_id", email);
        body.put("mobile_number", mobile);
        body.put("name", name);
        body.put("pass", password);

        progressHelper.showProgress("Please wait...");

        Call<AddContractor> call = apiInterface.addContractor(body);

        call.enqueue(new Callback<AddContractor>() {
            @Override
            public void onResponse(Call<AddContractor> call, Response<AddContractor> response) {
                progressHelper.dismissProgress();
                if (response.body().isStatus()) {
                    String message = "Email address: " + response.body().getData() + "\nPassword: " + password;
                    progressHelper.showSuccessDialog(message, "Registration Success", "cancel", "Done", AddContractorActivity.this);
                } else {
                    progressHelper.message(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<AddContractor> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void onShowDialogButtonClicked(String type) {
        binding.inputAddContractorEmail.setText("");
        binding.inputAddContractorUserName.setText("");
        binding.addContractorInputLoginPassword.setText("");
        binding.inputAddContractorMobile.setText("");
    }
}