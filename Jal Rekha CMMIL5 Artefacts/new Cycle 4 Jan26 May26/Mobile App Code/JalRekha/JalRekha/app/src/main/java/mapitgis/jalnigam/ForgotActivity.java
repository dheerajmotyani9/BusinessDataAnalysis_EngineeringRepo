package mapitgis.jalnigam;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.Objects;

import mapitgis.jalnigam.core.API;
import mapitgis.jalnigam.core.ApiCaller;
import mapitgis.jalnigam.core.Data;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityForgotBinding;

public class ForgotActivity extends BaseActivity {
    private ActivityForgotBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appbar.toolbar);
        setTitle(R.string.forgot_password_q);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

//        String mobile = getIntent().getStringExtra(Utility.G_KEY);
//        if (mobile == null) {
//            mobile = "";
//            Utility.show(this, Utility.check(this, R.string.mobile_number));
//            finish();
//            return;
//        }
//        binding.editTextMobile.setText(mobile);
        binding.buttonSubmit.setOnClickListener(v -> {
            if (Utility.getIntTag(v, 1) == 1) {
                getOTP();
            } else {
                String mobile = binding.editTextMobile.getText().toString();
                if (mobile.isEmpty()) {
                    Utility.show(this, Utility.check(this, R.string.mobile_number));
                    return;
                }
                String otp = binding.editTextOTP.getText().toString();
                if (otp.isEmpty()) {
                    Utility.show(this, Utility.check(this, R.string.otp));
                    return;
                }
                String pass = binding.editTextPassword.getText().toString();
                if (pass.isEmpty()) {
                    Utility.show(this, Utility.check(this, R.string.password));
                    return;
                }
                String pass2 = binding.editTextPassword2.getText().toString();
                if (!pass.equals(pass2)) {
                    Utility.show(this, "Please enter same password in both box.");
                    return;
                }
                Data data = new Data();
                data.put("MobileNumber", mobile);
                data.put("Pass", pass);
                data.put("otp", otp);
                new ApiCaller(this, (response, key) -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                        if (jsonObject.getBoolean(Utility.SUCCESS)) {
                            finish();
                        }
                    } catch (Exception e) {
                        Utility.show(this, e);
                        finish();
                    }
                }, 1, data.toString(), getString(R.string.please_wait)).start(API.FORGOT);
            }
        });
        binding.buttonReSendOTP.setOnClickListener(v->getOTP());
    }

    private void getOTP() {
        String mobile = binding.editTextMobile.getText().toString();
        if (mobile.isEmpty()) {
            Utility.show(this, Utility.check(this, R.string.mobile_number));
            return;
        }
        Data data = new Data();
        data.put("_mobile_no", mobile);
        new ApiCaller(this, (response, key) -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean(Utility.SUCCESS)) {
                    binding.linearLayoutNext.setVisibility(View.VISIBLE);
                    binding.editTextMobile.setEnabled(false);
                    binding.buttonReSendOTP.setVisibility(View.VISIBLE);
                    binding.buttonSubmit.setTag(2);
                    binding.buttonSubmit.setText(R.string.update_password);
                }
                Utility.show(this, jsonObject.getString(Utility.MESSAGE));
            } catch (Exception e) {
                Utility.show(this, e);
                finish();
            }
        }, 1, data.toString(), getString(R.string.please_wait)).start(API.FORGOT_GET_OTP);
    }
}
