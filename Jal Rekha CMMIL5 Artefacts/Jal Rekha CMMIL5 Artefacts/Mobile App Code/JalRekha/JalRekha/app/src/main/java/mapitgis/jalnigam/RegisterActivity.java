package mapitgis.jalnigam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import mapitgis.jalnigam.core.API;
import mapitgis.jalnigam.core.ApiCaller;
import mapitgis.jalnigam.core.Data;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SpinnerManager;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityRegisterBinding;

public class RegisterActivity extends BaseActivity {
    private ActivityRegisterBinding binding;

    private String name, mobile, email;//gender="Male"
    private SpinnerItem designation, district;
    private List<SpinnerItem> spinnerItemsDesignation, spinnerItemsDistrict;
    private boolean finished;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        finished = getIntent().getBooleanExtra(Utility.G_KEY, false);

        binding.buttonLogout.setVisibility(View.GONE);

        binding.buttonReSendOTP.setOnClickListener(v -> sendOTP());

//        spinnerItems = new ArrayList<>();
//        spinnerItems.add(new SpinnerItem("1", "Manager"));
//        spinnerItems.add(new SpinnerItem("2", "Deputy Manager"));
//        spinnerItems.add(new SpinnerItem("3", "Team Leader"));
//        spinnerItems.add(new SpinnerItem("4", "Resident/ Assistant Engineer"));
//        spinnerItems.add(new SpinnerItem("5", "Field Engineer"));
        Utility.fillDesignation(this, (spinnerItems, type) -> {
            this.spinnerItemsDesignation = spinnerItems;
            new SpinnerManager(binding.linearLayoutDesignation, 1, this, this.spinnerItemsDesignation);
            updateData(false);
        });

        Utility.fillDistrictAM(this, (spinnerItems, type) -> {
            this.spinnerItemsDistrict = spinnerItems;
            new SpinnerManager(binding.linearLayoutDistrict, 2, this, this.spinnerItemsDistrict);
            updateData(false);
        });

        binding.buttonSubmit.setOnClickListener(v -> {
            int type = (int) v.getTag();
            mobile = binding.editTextMobile.getText().toString();
            if (mobile.isEmpty()) {
                Utility.show(this, Utility.check(this, R.string.mobile_number));
                return;
            }
            if(type == 1) {
                sendOTP();
            }else{
                if (binding.linearLayoutDesignation.getTag() == null) {
                    Utility.show(this, Utility.select(this, R.string.designation));
                    return;
                }
                designation = ((SpinnerItem) binding.linearLayoutDesignation.getTag());
                if (binding.linearLayoutDistrict.getTag() == null) {
                    Utility.show(this, Utility.select(this, R.string.district));
                    return;
                }
                district = ((SpinnerItem) binding.linearLayoutDistrict.getTag());
                name = binding.editTextName.getText().toString();
                if (name.isEmpty()) {
                    Utility.show(this, Utility.check(this, R.string.full_name));
                    return;
                }
                email = binding.editTextEmail.getText().toString();
                if (email.isEmpty()) {
                    Utility.show(this, Utility.check(this, R.string.email_id));
                    return;
                }

                String pass = null, otp = null;
                if (type == 2) {
                    otp = binding.editTextOTP.getText().toString();
                    if (otp.isEmpty()) {
                        Utility.show(this, Utility.check(this, R.string.otp));
                        return;
                    }
                    pass = binding.editTextPassword.getText().toString();
                    if (pass.isEmpty()) {
                        Utility.show(this, Utility.check(this, R.string.password));
                        return;
                    }
                    String pass2 = binding.editTextPassword2.getText().toString();
                    if (!pass.equals(pass2)) {
                        Utility.show(this, "Please enter same password in both box.");
                        return;
                    }
                }

                Data data1 = new Data();
                data1.put("DesignationID", designation.getKeyString());
                data1.put("dist_cd", district.getKeyString());
                data1.put("Name", name);
                data1.put("MobileNumber", mobile);
                data1.put("EmailID", email);
                data1.put("Gender", "");
                if (pass != null) data1.put("Pass", pass);
                if (otp != null) data1.put("otp", otp);
                data1.put("IMEI", mobile);///Utility.getDeviceID(this));
                new ApiCaller(this, (response, key) -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean(Utility.SUCCESS)) {
                            if (type == 2) {
                                Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                                Utility.goFirst(this, true);
                            } else {
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                                dlgAlert.setMessage(R.string.update_profile_message);
                                dlgAlert.setTitle(R.string.information);
                                dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> onBackPressed());
                                dlgAlert.setOnCancelListener(dialog -> onBackPressed());
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        } else {
                            Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                        }
                    } catch (Exception e) {
                        Utility.show(this, e);
                        finish();
                    }
                }, 1, data1.toString(), getString(R.string.please_wait)).start(type == 2 ? API.RegisterDevice : API.UpdateProfile);
            }
        });


        updateData(true);
    }

    private void sendOTP() {
        mobile = binding.editTextMobile.getText().toString();
        if (mobile.isEmpty()) {
            Utility.show(this, Utility.check(this, R.string.mobile_number));
            return;
        }
        Data data1 = new Data();
        data1.put("email", mobile);
        new ApiCaller(this, (response, key) -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean(Utility.SUCCESS)) {
                    binding.editTextOTP.setVisibility(View.VISIBLE);
                    binding.textViewOTP.setVisibility(View.VISIBLE);

                    binding.editTextPassword.setVisibility(View.VISIBLE);
                    binding.editTextPassword2.setVisibility(View.VISIBLE);
                    binding.editTextEmail.setVisibility(View.VISIBLE);
                    binding.editTextName.setVisibility(View.VISIBLE);
                    binding.linearLayoutDesignation.setVisibility(View.VISIBLE);
                    binding.linearLayoutDistrict.setVisibility(View.VISIBLE);

                    binding.textViewPassword1.setVisibility(View.VISIBLE);
                    binding.textViewPassword2.setVisibility(View.VISIBLE);
                    binding.textViewEmail.setVisibility(View.VISIBLE);
                    binding.textViewName.setVisibility(View.VISIBLE);
                    binding.textViewDesignation.setVisibility(View.VISIBLE);
                    binding.textViewPIU.setVisibility(View.VISIBLE);

//                    binding.buttonSubmit.setVisibility(View.VISIBLE);


                    binding.buttonReSendOTP.setVisibility(View.VISIBLE);

                    binding.buttonSubmit.setTag(2);
                    binding.buttonSubmit.setText(R.string.register);
                }
                Utility.show(this, jsonObject.getString(Utility.MESSAGE));
            } catch (Exception e) {
                Utility.show(this, e);
                finish();
            }
        }, 1, data1.toString(), getString(R.string.please_wait)).start(API.SendRegisterMail);
    }

    private void updateData(boolean single) {
        Login login = SqLite.instance(this).getLogin();
        if (login != null) {
            setTitle(R.string.profile);
            binding.buttonLogout.setVisibility(View.VISIBLE);
            binding.buttonLogout.setOnClickListener(view -> {
                Data data = new Data();
                data.put("imei", login.getMobile());//Utility.getDeviceID(this));
                Utility.show(this, getString(R.string.are_you_sure), getString(R.string.want_to_logout_from_app), getString(R.string.logout), view1 -> new ApiCaller(this, (response, key) -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        SqLite.instance(this).logout();
                        Utility.goFirst(this, true);
                        Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                    } catch (Exception e) {
                        Utility.show(this, e);
                    }
                }, 1, data.toString(), getString(R.string.please_wait)).start(API.LOGOUT), true);
            });
            binding.editTextPassword.setVisibility(View.GONE);
            binding.editTextPassword2.setVisibility(View.GONE);
            binding.editTextOTP.setVisibility(View.GONE);
            binding.textViewPassword1.setVisibility(View.GONE);
            binding.textViewPassword2.setVisibility(View.GONE);
            binding.textViewOTP.setVisibility(View.GONE);
            binding.buttonReSendOTP.setVisibility(View.GONE);

//            binding.buttonSubmit.setVisibility(View.VISIBLE);

            binding.editTextName.setText(login.getName());
            binding.editTextEmail.setText(login.getEmail());
            binding.editTextMobile.setText(login.getMobile());
            updateDesignation(login.getDesignation());
            updateDistrict(login.getDistrict());
//            gender = "";
//            ((RadioButton) radioGroup.getChildAt(login.getGender().equalsIgnoreCase("male") ? 1 : 2)).setChecked(true);
            binding.buttonSubmit.setTag(3);
            binding.buttonSubmit.setText(R.string.request_update);

            binding.space.setVisibility(View.VISIBLE);

            if (single) {
                GetDetail(login);
            }
            if (finished) {
                Utility.goFirst(this,true);
                finish();
            }
        } else {
            setTitle(R.string.device_registration);
            binding.space.setVisibility(View.GONE);

            binding.editTextPassword.setVisibility(View.GONE);
            binding.editTextPassword2.setVisibility(View.GONE);
            binding.editTextEmail.setVisibility(View.GONE);
            binding.editTextName.setVisibility(View.GONE);
            binding.linearLayoutDesignation.setVisibility(View.GONE);
            binding.linearLayoutDistrict.setVisibility(View.GONE);

            binding.textViewPassword1.setVisibility(View.GONE);
            binding.textViewPassword2.setVisibility(View.GONE);
            binding.textViewEmail.setVisibility(View.GONE);
            binding.textViewName.setVisibility(View.GONE);
            binding.textViewDesignation.setVisibility(View.GONE);
            binding.textViewPIU.setVisibility(View.GONE);


            binding.buttonReSendOTP.setVisibility(View.GONE);

            binding.editTextOTP.setVisibility(View.GONE);
            binding.textViewOTP.setVisibility(View.GONE);
//            binding.buttonSubmit.setVisibility(View.GONE);
            binding.buttonLogout.setVisibility(View.GONE);

            binding.buttonSubmit.setTag(1);
            binding.buttonSubmit.setText(R.string.send_otp);
//            GetDetail(false);
        }
    }

    private void GetDetail(@NonNull Login login) {
        if (login.getMobile().equals("1234567890")) return;
        Data data = new Data();
        data.put("imei", login.getMobile());//Utility.getDeviceID(this));
        new ApiCaller(this, (response, key) -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean(Utility.SUCCESS)) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject(Utility.DATA);
                    if (jsonObject1.getString("Active").equals("0")) {
                        Utility.show(this, "Please wait for activate your account");
                    }
                    //jsonObject1.getString("Gender")
                    SqLite.instance(this).updateProfile(new Login(1, jsonObject1.getString("Name"), jsonObject1.getString("EmailId"), jsonObject1.getString("MobileNumber"), jsonObject1.getString("District"), jsonObject1.getString("Designation"), 0, "", "",""));//, Utility.getDeviceID(this)
                    updateData(false);
                } else {
//                  Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                    finish();
                }
            } catch (Exception e) {
                Utility.show(this, e);
                finish();
            }
        }, 1, data.toString(), getString(R.string.please_wait)).start(API.GetDevice);
    }

    private void updateDesignation(String designation) {
        if (spinnerItemsDesignation == null) {
            return;
        }
        for (SpinnerItem spinnerItem : spinnerItemsDesignation) {
            if (spinnerItem.getValue().equals(designation)) {
                updateDesignation(spinnerItem);
            }
        }
    }

    private void updateDistrict(String district) {
        if (spinnerItemsDistrict == null) {
            return;
        }
        for (SpinnerItem spinnerItem : spinnerItemsDistrict) {
            if (spinnerItem.getKeyString().equals(district)) {
                updateDistrict(spinnerItem);
            }
        }
    }

    private void updateDesignation(@NonNull SpinnerItem spinnerItem) {
        ((TextView) binding.linearLayoutDesignation.getChildAt(0)).setText(spinnerItem.getValue());
        binding.linearLayoutDesignation.setTag(spinnerItem);
    }

    private void updateDistrict(@NonNull SpinnerItem spinnerItem) {
        ((TextView) binding.linearLayoutDistrict.getChildAt(0)).setText(spinnerItem.getValue());
        binding.linearLayoutDistrict.setTag(spinnerItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null && resultCode == RESULT_OK) {
                set(data.getIntExtra("requestCode", 0), (SpinnerItem)
                        data.getSerializableExtra("spinnerItem"));
            }
        }
    }

    private void set(int code, SpinnerItem spinnerItem) {
        if (code == 1) {
            updateDesignation(spinnerItem);
        } else if (code == 2) {
            updateDistrict(spinnerItem);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
