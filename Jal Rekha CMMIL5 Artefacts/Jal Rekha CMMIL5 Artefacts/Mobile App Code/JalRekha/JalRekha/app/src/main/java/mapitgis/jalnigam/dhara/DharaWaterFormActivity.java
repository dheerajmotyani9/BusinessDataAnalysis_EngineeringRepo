package mapitgis.jalnigam.dhara;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapitgis.jalnigam.BuildConfig;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityDharaWaterFormBinding;
import mapitgis.jalnigam.rfi.adapter.dhara.EMFReadingAdapter;
import mapitgis.jalnigam.rfi.helper.ImageHelper;
import mapitgis.jalnigam.rfi.helper.PermissionHelper;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.helper.SingleDatePicker;
import mapitgis.jalnigam.rfi.model.SimpleResponse;
import mapitgis.jalnigam.rfi.model.dhara.DailyWaterSupply;
import mapitgis.jalnigam.rfi.model.dhara.DharaESRHistory;
import mapitgis.jalnigam.rfi.model.dhara.DharaVillage;
import mapitgis.jalnigam.rfi.model.dhara.EMFReading;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import mapitgis.jalnigam.room.table.VillageTable;
import mapitgis.jalnigam.rfi.utils.CustomDialogHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DharaWaterFormActivity extends AppCompatActivity implements
        PermissionHelper.LocationPermissionListener, OnMapReadyCallback,
        EMFReadingAdapter.EMFReadingListener, PermissionHelper.CameraPermissionListener,
        ImageHelper.ImageCompressListener {

    private ImageView backImageView;
    private TextView titleTextView;
    private ActivityDharaWaterFormBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0, longitude = 0;
    private ProgressBar locationProgressBar;
    private TextView locationErrorTextView, locationAddressTextView;
    private Button btnAllowLocation;
    private LinearLayout googleMapContainer;
    private String permissionErrorType;
    private GoogleMap mMap;

    private PermissionHelper permissionHelper;
    private List<EMFReading> emfReadingList;
    private EMFReadingAdapter emfReadingAdapter;

    private ProgressHelper progressHelper;
    private ImageHelper imageHelper;
    private CustomDialogHelper customDialogHelper;

    private String photoType = "";
    private String startPhotoBase64 = "", endPhotoBase64 = "";
    private String startVillagePhotoBase64 = "", endVillagePhotoBase64 = "";

    //todo: formType => ESR, WTP, INTAKE
    private String formType = "";
    private DailyWaterSupply.DailyWaterSupplyData waterSupplyData;

    private ActivityResultLauncher<Intent> activityResultMIC;

    private ApiInterface apiInterface;

    //todo: village list (ESR case only)
    private List<VillageTable> villageList = new ArrayList<>();
    private String selectedVillage = "";
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        // setContentView(R.layout.activity_dhara_water_form);
        binding = ActivityDharaWaterFormBinding.inflate(LayoutInflater.from(this));
        login = SqLite.instance(this).getLogin();
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        waterSupplyData = (DailyWaterSupply.DailyWaterSupplyData) getIntent().getSerializableExtra("data");
        assert waterSupplyData != null;
        formType = waterSupplyData.getAllotmentTypeName();

        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        progressHelper = new ProgressHelper(this);
        imageHelper = new ImageHelper(this);
        customDialogHelper = new CustomDialogHelper(this);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Daily water supply form");
        backImageView.setOnClickListener(v -> finish());

        permissionHelper = new PermissionHelper(this);

        // TODO: 12-09-2024 : location views
        locationProgressBar = findViewById(R.id.location_progress_bar);
        locationAddressTextView = findViewById(R.id.location_address_text_view);
        locationErrorTextView = findViewById(R.id.location_permission_message_text_view);
        btnAllowLocation = findViewById(R.id.location_permission_btn_allow);
        googleMapContainer = findViewById(R.id.google_map_container);

        btnAllowLocation.setOnClickListener(v -> {
            if (permissionErrorType.equalsIgnoreCase("deny")) {
                permissionHelper.requestLocationPermission(this);
            } else if (permissionErrorType.equalsIgnoreCase("permanent")) {
                permissionHelper.openSettings();
            } else if (permissionErrorType.equalsIgnoreCase("gps")) {
                permissionHelper.enableGPS();
            }
        });

        binding.dharaWaterFormSchemeNameTv.setText(waterSupplyData.getWtpName());
        binding.dharaWaterFormEsrNameTv.setText(waterSupplyData.getEsrName());
        binding.dharaWaterFormIntakeNameTv.setText(waterSupplyData.getIntakeName());
        binding.dharaWaterFormWtpNameTv.setText(waterSupplyData.getWtpName());
        binding.dharaWaterFormPiuNameTv.setText(waterSupplyData.getPiuName());

        binding.btnAddMoreEmfReading.setOnClickListener(v -> addEmfReading());

        // TODO: 13-09-2024 :EMF READING Recycler_View.
        emfReadingList = new ArrayList<>();
        emfReadingAdapter = new EMFReadingAdapter(this, emfReadingList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.emfReadingRecyclerView.setLayoutManager(manager);
        binding.emfReadingRecyclerView.setAdapter(emfReadingAdapter);

        binding.selectEsrVillageWaterTextView.setOnClickListener(v -> showVillageDialog());
        binding.dharaWaterFormTapMicContainer.setOnClickListener(v -> openMic());

        binding.btnSubmitWaterForm.setOnClickListener(v -> {
            if (formType.equalsIgnoreCase("wtp")) {
                submitWTP();
            } else if (formType.equalsIgnoreCase("intake well")) {
                submitIntakeWell();
            } else if (formType.equalsIgnoreCase("esr")) {
                submitEsrForm();
            }
        });

        if (formType.equalsIgnoreCase("wtp")) {
            binding.wtpWaterFormContainer.setVisibility(View.VISIBLE);
            binding.intakeWellWaterFormContainer.setVisibility(View.GONE);
            binding.esrWaterFormContainer.setVisibility(View.GONE);

            initWTPForms();
        } else if (formType.equalsIgnoreCase("esr")) {
            binding.wtpWaterFormContainer.setVisibility(View.GONE);
            binding.intakeWellWaterFormContainer.setVisibility(View.GONE);
            binding.esrWaterFormContainer.setVisibility(View.VISIBLE);

            initESRForm();
            getVillage();
           /* if (!waterSupplyData.isUpdate()) {
                getVillage();
            }*/

        } else if (formType.equalsIgnoreCase("intake well")) {
            binding.wtpWaterFormContainer.setVisibility(View.GONE);
            binding.intakeWellWaterFormContainer.setVisibility(View.VISIBLE);
            binding.esrWaterFormContainer.setVisibility(View.GONE);

            initIntakeWellForm();
        }

        permissionHelper.requestLocationPermission(this);
        micIntentCallBack();
    }

    // TODO: 14-10-2024 : intake form views and events
    private void initIntakeWellForm() {
        binding.inputIntakeWaterStartReading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputIntakeWaterEndReading.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String startReading = binding.inputIntakeWaterStartReading.getText().toString();
                String endReading = binding.inputIntakeWaterEndReading.getText().toString();

                String totalValue = calculateTotal(startReading, endReading);
                binding.inputIntakeWaterTotalReading.setText(totalValue);
            }
        });
        binding.inputIntakeWaterEndReading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String startReading = binding.inputIntakeWaterStartReading.getText().toString();
                String endReading = binding.inputIntakeWaterEndReading.getText().toString();
                String totalValue = calculateTotal(startReading, endReading);
                binding.inputIntakeWaterTotalReading.setText(totalValue);
            }
        });

        // TODO: 04-10-2024 : upload photo btn
        binding.intakeWaterStartReadingPhotoBtn.setOnClickListener(v -> {
            photoType = "intake_start";
            permissionHelper.requestCameraPermission(this);
        });
        binding.intakeWaterEndReadingPhotoBtn.setOnClickListener(v -> {
            String endReading = binding.inputIntakeWaterEndReading.getText().toString();

            if (endReading.isEmpty()){
                progressHelper.message("Please enter intake end reading before upload photo");
                return;
            }
            photoType = "intake_end";
            permissionHelper.requestCameraPermission(this);
        });

        if (waterSupplyData.isUpdate()) {

            binding.inputIntakeWaterStartReading.setText(waterSupplyData.getStartReading());
            binding.inputIntakeWaterStartReading.setEnabled(false);
            binding.inputIntakeWaterEndReading.setText(waterSupplyData.getEndReading());
            binding.inputIntakeWaterTotalReading.setText(waterSupplyData.getTotalReading());
            binding.inputRemarkDharaWaterForm.setText(waterSupplyData.getRemark());


            binding.intakeWaterStartReadingPhotoBtn.setVisibility(View.GONE);
            Glide.with(this).load(BuildConfig.JAL_NIGAM_IMAGE + waterSupplyData.getStartPhotoPath()).into(binding.intakeWaterStartImageView);
            binding.intakeWaterStartImageView.setVisibility(View.VISIBLE);


            if (!waterSupplyData.getEndReading().equals("0.0")) {
                binding.inputIntakeWaterEndReading.setEnabled(false);
                binding.intakeWaterEndReadingPhotoBtn.setVisibility(View.GONE);
                Glide.with(this).load(BuildConfig.JAL_NIGAM_IMAGE + waterSupplyData.getStartPhotoPath()).into(binding.intakeWaterEndImageView);
                binding.intakeWaterEndImageView.setVisibility(View.VISIBLE);

                binding.inputRemarkDharaWaterForm.setEnabled(false);
                binding.dharaWaterFormTapMicContainer.setVisibility(View.GONE);

                //hide submit button
                binding.btnSubmitWaterForm.setVisibility(View.GONE);
            }
        }
    }

    // TODO: 14-10-2024 : WTP form views and events
    private void initWTPForms() {
        binding.inputStartReadingWtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputEndReadingWtp.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String startReading = binding.inputStartReadingWtp.getText().toString();
                String endReading = binding.inputEndReadingWtp.getText().toString();
                String totalValue = calculateTotal(startReading, endReading);
                binding.inputTotalReadingWtp.setText(totalValue);
            }
        });
        binding.inputEndReadingWtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String startReading = binding.inputStartReadingWtp.getText().toString();
                String endReading = binding.inputEndReadingWtp.getText().toString();
                String totalValue = calculateTotal(startReading, endReading);
                binding.inputTotalReadingWtp.setText(totalValue);
            }
        });

        binding.wtpWaterStartReadingPhotoBtn.setOnClickListener(v -> {
            photoType = "wtp_start";
            permissionHelper.requestCameraPermission(this);
        });
        binding.wtpWaterEndReadingPhotoBtn.setOnClickListener(v -> {
            String endReading = binding.inputEndReadingWtp.getText().toString();

            if (endReading.isEmpty()){
                progressHelper.message("Please enter WTP end reading before upload photo");
                return;
            }
            photoType = "wtp_end";
            permissionHelper.requestCameraPermission(this);
        });

        if (waterSupplyData.isUpdate()) {
            binding.inputStartReadingWtp.setText(waterSupplyData.getStartReading());
            binding.inputStartReadingWtp.setEnabled(false);

            binding.inputEndReadingWtp.setText(waterSupplyData.getEndReading());
            binding.inputTotalReadingWtp.setText(waterSupplyData.getTotalReading());

            binding.inputRemarkDharaWaterForm.setText(waterSupplyData.getRemark());

            binding.wtpWaterStartReadingPhotoBtn.setVisibility(View.GONE);
            Glide.with(this).load(BuildConfig.JAL_NIGAM_IMAGE + waterSupplyData.getStartPhotoPath()).into(binding.wtpWaterStartImageView);
            binding.wtpWaterStartImageView.setVisibility(View.VISIBLE);

            if (!waterSupplyData.getEndReading().equals("0.0")) {
                binding.inputEndReadingWtp.setEnabled(false);
                binding.wtpWaterEndReadingPhotoBtn.setVisibility(View.GONE);
                Glide.with(this).load(BuildConfig.JAL_NIGAM_IMAGE + waterSupplyData.getStartPhotoPath()).into(binding.wtpWaterEndImageView);
                binding.wtpWaterEndImageView.setVisibility(View.VISIBLE);

                binding.inputRemarkDharaWaterForm.setEnabled(false);
                binding.dharaWaterFormTapMicContainer.setVisibility(View.GONE);

                //hide submit button
                binding.btnSubmitWaterForm.setVisibility(View.GONE);

            }
        }
    }

    // TODO: 14-10-2024 : ESR form views and events
    private void initESRForm() {
        binding.inputStartReadingEsrWater.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputEndReadingEsrWater.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String startReading = binding.inputStartReadingEsrWater.getText().toString();
                String endReading = binding.inputEndReadingEsrWater.getText().toString();
                String totalValue = calculateTotal(startReading, endReading);
                binding.inputTotalReadingEsrWater.setText(totalValue);
            }
        });
        
        binding.inputEndReadingEsrWater.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String startReading = binding.inputStartReadingEsrWater.getText().toString();
                String endReading = binding.inputEndReadingEsrWater.getText().toString();
                String totalValue = calculateTotal(startReading, endReading);
                binding.inputTotalReadingEsrWater.setText(totalValue);
            }
        });

        binding.inputStartReadingVillageWater.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.inputEndReadingVillageWater.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                String startReading = binding.inputStartReadingVillageWater.getText().toString();
                String endReading = binding.inputEndReadingVillageWater.getText().toString();
                String totalValue = calculateTotal(startReading, endReading);
                binding.inputTotalReadingVillageWater.setText(totalValue);
            }
        });
        binding.inputEndReadingVillageWater.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String startReading = binding.inputStartReadingVillageWater.getText().toString();
                String endReading = binding.inputEndReadingVillageWater.getText().toString();
                String totalValue = calculateTotal(startReading, endReading);
                binding.inputTotalReadingVillageWater.setText(totalValue);
            }
        });

        binding.esrWaterStartReadingPhotoBtn.setOnClickListener(v -> {
            photoType = "esr_start";
            permissionHelper.requestCameraPermission(this);
        });
        binding.esrWaterEndReadingPhotoBtn.setOnClickListener(v -> {
            String endReading = binding.inputEndReadingEsrWater.getText().toString();

            if (endReading.isEmpty()){
                progressHelper.message("Please enter ESR end reading before upload photo");
                return;
            }
            photoType = "esr_end";
            permissionHelper.requestCameraPermission(this);
        });

        binding.esrWaterVillageStartReadingPhotoBtn.setOnClickListener(v -> {
            photoType = "village_start";
            permissionHelper.requestCameraPermission(this);
        });

        binding.esrWaterVillageEndReadingPhotoBtn.setOnClickListener(v -> {
            String endReading = binding.inputEndReadingVillageWater.getText().toString();

            if (endReading.isEmpty()){
                progressHelper.message("Please enter Village end reading before upload photo");
                return;
            }
            photoType = "village_end";
            permissionHelper.requestCameraPermission(this);
        });

        if (waterSupplyData.isUpdate()) {

            binding.inputStartReadingEsrWater.setText(waterSupplyData.getStartReading());
            binding.inputStartReadingEsrWater.setEnabled(false);

            binding.inputEndReadingEsrWater.setText(waterSupplyData.getEndReading());
            binding.inputTotalReadingEsrWater.setText(waterSupplyData.getTotalReading());

            binding.inputStartReadingVillageWater.setText(waterSupplyData.getStartReadingVillage());
            binding.inputStartReadingVillageWater.setEnabled(false);

            binding.inputEndReadingVillageWater.setText(waterSupplyData.getEndReadingVillage());

            binding.inputTotalReadingVillageWater.setText(waterSupplyData.getTotalReadingVillage());

            binding.inputRemarkDharaWaterForm.setText(waterSupplyData.getRemark());

            binding.esrWaterStartReadingPhotoBtn.setVisibility(View.GONE);
            Glide.with(this).load(BuildConfig.JAL_NIGAM_IMAGE + waterSupplyData.getStartPhotoPath()).into(binding.esrWaterStartImageView);
            binding.esrWaterStartImageView.setVisibility(View.VISIBLE);

            binding.esrWaterVillageStartReadingPhotoBtn.setVisibility(View.GONE);
            Glide.with(this).load(BuildConfig.JAL_NIGAM_IMAGE + waterSupplyData.getStartPhotoPathVillage()).into(binding.esrWaterStartVillageImageView);
            binding.esrWaterStartVillageImageView.setVisibility(View.VISIBLE);

            selectedVillage = waterSupplyData.getVillageName();
            binding.selectEsrVillageWaterTextView.setText(selectedVillage);

            // this is for ESR
            if (!waterSupplyData.getEndReading().equals("0.0")) {
                binding.inputEndReadingEsrWater.setEnabled(false);
                binding.esrWaterEndReadingPhotoBtn.setVisibility(View.GONE);
                Glide.with(this).load(BuildConfig.JAL_NIGAM_IMAGE + waterSupplyData.getEndPhotoPath()).into(binding.esrWaterEndImageView);
                binding.esrWaterEndImageView.setVisibility(View.VISIBLE);
            }

            // this is for village
            if (!waterSupplyData.getEndReadingVillage().equals("0.0")) {
                binding.inputEndReadingVillageWater.setEnabled(false);
                binding.esrWaterVillageEndReadingPhotoBtn.setVisibility(View.GONE);
                Glide.with(this).load(BuildConfig.JAL_NIGAM_IMAGE + waterSupplyData.getEndPhotoPathVillage()).into(binding.esrWaterVillageEndImageView);
                binding.esrWaterVillageEndImageView.setVisibility(View.VISIBLE);

                //hide submit button also
                binding.btnSubmitWaterForm.setVisibility(View.GONE);
                binding.inputRemarkDharaWaterForm.setEnabled(false);
                binding.dharaWaterFormTapMicContainer.setVisibility(View.GONE);

            } else {
                binding.inputEndReadingVillageWater.setEnabled(true);
                binding.esrWaterVillageEndReadingPhotoBtn.setVisibility(View.VISIBLE);
                binding.esrWaterVillageEndImageView.setVisibility(View.GONE);

                //hide submit button also
                binding.btnSubmitWaterForm.setVisibility(View.VISIBLE);
                binding.inputRemarkDharaWaterForm.setEnabled(true);
                binding.dharaWaterFormTapMicContainer.setVisibility(View.VISIBLE);
            }
        } else {

            binding.inputStartReadingVillageWater.setText("");
            binding.inputStartReadingVillageWater.setEnabled(true);

            binding.inputEndReadingVillageWater.setText("");
            binding.inputEndReadingVillageWater.setEnabled(true);

            binding.inputTotalReadingVillageWater.setText("");

            binding.inputRemarkDharaWaterForm.setText("");
            binding.inputRemarkDharaWaterForm.setEnabled(true);

            binding.esrWaterVillageStartReadingPhotoBtn.setVisibility(View.VISIBLE);
            binding.esrWaterVillageEndReadingPhotoBtn.setVisibility(View.VISIBLE);
            binding.btnSubmitWaterForm.setVisibility(View.VISIBLE);
            binding.btnSubmitWaterForm.setClickable(true);

            binding.esrWaterVillageEndImageView.setVisibility(View.GONE);
            binding.esrWaterStartVillageImageView.setVisibility(View.GONE);
        }
    }

    //todo: Function to calculate the total value ------------------------------
    private String calculateTotal(String startStr, String endStr) {
        // Check if both fields are filled
        if (!startStr.isEmpty() && !endStr.isEmpty()) {
            try {
                double startReading = Double.parseDouble(startStr);
                double endReading = Double.parseDouble(endStr);

                DecimalFormat df = new DecimalFormat("#.##");

                if (startReading <= endReading) {
                    double total = endReading - startReading;
                    return df.format(total);
                } else {
                    return "";
                    /* binding.inputIntakeWaterTotalReading.setError("Start reading should be less than End reading");*/
                }
            } catch (NumberFormatException e) {
                return "";
            }
        } else {
            return "";
        }
    }

    private boolean isReadingCorrect(String startStr, String endStr) {
        double startReading = Double.parseDouble(startStr);
        double endReading = Double.parseDouble(endStr);
        return startReading <= endReading;
    }

    //todo: submit intake well form.
    private void submitIntakeWell() {
        String startReading = binding.inputIntakeWaterStartReading.getText().toString();
        String endReading = binding.inputIntakeWaterEndReading.getText().toString();
        String totalReading = binding.inputIntakeWaterTotalReading.getText().toString();
        String remark = binding.inputRemarkDharaWaterForm.getText().toString();


        if (startReading.isEmpty()) {
            progressHelper.message("Please enter start reading");
            return;
        }

        if (waterSupplyData.isUpdate()) {
            if (endReading.isEmpty()) {
                progressHelper.message("Please enter end reading");
                return;
            }
        }
        if (!endReading.isEmpty()) {
            if (!isReadingCorrect(startReading, endReading)) {
                progressHelper.message("Intake well's end reading must be greater than start reading");
                return;
            }
        }

        if (remark.isEmpty()) {
            progressHelper.message("Please enter remark");
            return;
        }

        if (!progressHelper.isAutoTimeEnabled(this)) {
            progressHelper.message("Please set time automatically from settings");
            return;
        }

       /* if (latitude == 0 && longitude == 0) {
            progressHelper.message("Location not found.");
            return;
        }*/

        Map<String, Object> body = new HashMap<>();
        body.put(Utility.E_TOKEN, "");
        body.put("intake_id", waterSupplyData.getIntakeId());
        body.put("survey_date", SingleDatePicker.getCurrentDate());
        body.put("survey_time", SingleDatePicker.getCurrentTime());
        body.put("start_reading", Double.parseDouble(startReading));
        body.put("end_reading", endReading.isEmpty() ? 0 : Double.parseDouble(endReading));
        body.put("total_raw_water_drawn", totalReading.isEmpty() ? 0 : Double.parseDouble(totalReading));
        body.put("latitude", latitude);
        body.put("longitude", longitude);
        body.put("remarks", remark);

        if (waterSupplyData.isUpdate()) {
            body.put("photo_path_start_reading", waterSupplyData.getStartPhotoPath());
            body.put("photo_path_end_reading", endPhotoBase64);
            body.put("updated_by", String.valueOf(login.getId()));
            body.put("updated_by_role", String.valueOf(login.getRoleId()));
//            body.put("updated_by", prefManager.getSqcId());
//            body.put("updated_by_role", prefManager.getRoleId());
        } else {
            body.put("photo_path_start_reading", startPhotoBase64);
            body.put("photo_path_end_reading", endPhotoBase64);
            body.put("created_by", String.valueOf(login.getId()));
            body.put("created_by_role", String.valueOf(login.getRoleId()));
//            body.put("created_by", prefManager.getSqcId());
//            body.put("created_by_role", prefManager.getRoleId());
        }

        progressHelper.showProgress("Please wait...");

        Call<SimpleResponse> call;

        if (waterSupplyData.isUpdate()) {
            call = apiInterface.updateIntakeForm(body);
        } else {
            call = apiInterface.intakeSubmit(body);
        }

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressHelper.dismissProgress();

                String successMessage = endReading.isEmpty() ? "Intake's start reading submitted successfully\nEnd meter reading is pending" : "Intake's both reading submit successfully";

                if (response.body().isSuccess()) {
                    progressHelper.showSuccessDialog(successMessage, "Intake Well", "Done", "Done", type -> {
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    });
                } else {
                    progressHelper.message("Failed to submit form. Try again!");
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);
            }
        });
    }

    //todo: submit intake well form.
    private void submitWTP() {
        String startReading = binding.inputStartReadingWtp.getText().toString();
        String endReading = binding.inputEndReadingWtp.getText().toString();
        String totalReading = binding.inputTotalReadingWtp.getText().toString();
        String remark = binding.inputRemarkDharaWaterForm.getText().toString();

        if (startReading.isEmpty()) {
            progressHelper.message("Please enter start reading");
            return;
        }

        if (waterSupplyData.isUpdate()) {
            if (endReading.isEmpty()) {
                progressHelper.message("Please enter end reading");
                return;
            }
        }

        if (!endReading.isEmpty()) {
            if (!isReadingCorrect(startReading, endReading)) {
                progressHelper.message("WTP's end reading must be greater than start reading");
                return;
            }
        }


        if (remark.isEmpty()) {
            progressHelper.message("Please enter remark");
            return;
        }

        if (!progressHelper.isAutoTimeEnabled(this)) {
            progressHelper.message("Please set time automatically from settings");
            return;
        }

       /* if (latitude == 0 && longitude == 0) {
            progressHelper.message("Location not found.");
            return;
        }*/

        Map<String, Object> body = new HashMap<>();
        body.put(Utility.E_TOKEN, "");
        body.put("intake_id", waterSupplyData.getIntakeId());
        body.put("wtp_id", waterSupplyData.getWtpId());
        body.put("survey_date", SingleDatePicker.getCurrentDate());
        body.put("survey_time", SingleDatePicker.getCurrentTime());
        body.put("start_reading", Double.parseDouble(startReading));
        body.put("end_reading", endReading.isEmpty() ? 0 : Double.parseDouble(endReading));
        body.put("total_water_supplied", totalReading.isEmpty() ? 0 : Double.parseDouble(totalReading));
        body.put("latitude", latitude);
        body.put("longitude", longitude);
        body.put("remarks", remark);

        /*body.put("created_by", prefManager.getSqcId());
        body.put("created_by_role", prefManager.getRoleId());
        body.put("photo_path_start_reading", startPhotoBase64);
        body.put("photo_path_end_reading", endPhotoBase64);*/

        if (waterSupplyData.isUpdate()) {
            body.put("photo_path_start_reading", waterSupplyData.getStartPhotoPath());
            body.put("photo_path_end_reading", endPhotoBase64);
//            body.put("updated_by", prefManager.getSqcId());
//            body.put("updated_by_role", prefManager.getRoleId());

            body.put("updated_by", String.valueOf(login.getId()));
            body.put("updated_by_role", String.valueOf(login.getRoleId()));
        } else {
            body.put("photo_path_start_reading", startPhotoBase64);
            body.put("photo_path_end_reading", endPhotoBase64);

            body.put("created_by", String.valueOf(login.getId()));
            body.put("created_by_role", String.valueOf(login.getRoleId()));

//            body.put("created_by", prefManager.getSqcId());
//            body.put("created_by_role", prefManager.getRoleId());
        }


        progressHelper.showProgress("Please wait...");
        Call<SimpleResponse> call;

        if (waterSupplyData.isUpdate()) {
            call = apiInterface.updateWTPForm(body);
        } else {
            call = apiInterface.wtpSubmit(body);
        }

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressHelper.dismissProgress();


                String successMessage = endReading.isEmpty() ? "WTP's start reading submitted successfully.\nEnd meter reading is pending" : "WTP's both reading submit successfully";

                if (response.body().isSuccess()) {
                    progressHelper.showSuccessDialog(successMessage, "WTP", "Done", "Done", type -> {
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    });
                } else {
                    progressHelper.message("Failed to submit form. Try again!");
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);
            }
        });
    }

    // TODO: 04-10-2024 : add emf reading for ESR -------------------------
    private void addEmfReading() {
        String startEsr = binding.inputStartReadingEsrWater.getText().toString();
        String endEsr = binding.inputEndReadingEsrWater.getText().toString();
        String totalEsr = binding.inputTotalReadingEsrWater.getText().toString();

        String startVillage = binding.inputStartReadingVillageWater.getText().toString();
        String endVillage = binding.inputEndReadingVillageWater.getText().toString();
        String totalVillage = binding.inputTotalReadingVillageWater.getText().toString();

       /* if (startEsr.isEmpty()){
            progressHelper.message("Please enter ESR's start reading");
            return;
        }

        if(endEsr.isEmpty()){
            progressHelper.message("Please enter ESR's end reading");
            return;
        }

        if (!isReadingCorrect(startEsr,endEsr)){
            progressHelper.message("ESR's end reading must be greater than start reading");
            return;
        }*/

        if (startVillage.isEmpty()) {
            progressHelper.message("Please enter village's start reading");
            return;
        }

        if (endVillage.isEmpty()) {
            progressHelper.message("Please enter village's end reading");
            return;
        }

        if (!isReadingCorrect(startVillage, endVillage)) {
            progressHelper.message("village's end reading must be greater than start reading");
            return;
        }

        EMFReading emfReading = new EMFReading();
      /*emfReading.setStartEsr(startEsr);
        emfReading.setEndEsr(endEsr);
        emfReading.setTotalEsr(totalEsr);
        emfReading.setEsrId("0");
        emfReading.setEsrName("TEST");*/

        emfReading.setStartVillage(startVillage);
        emfReading.setEndVillage(endVillage);
        emfReading.setTotalVillage(totalVillage);
        emfReading.setVillageId("0");
        emfReading.setVillageName("Villa");
        emfReadingList.add(emfReading);
        emfReadingAdapter.notifyDataSetChanged();
    }

    private void submitEsrForm() {

        String remark = binding.inputRemarkDharaWaterForm.getText().toString();

        String startEsr = binding.inputStartReadingEsrWater.getText().toString();
        String endEsr = binding.inputEndReadingEsrWater.getText().toString();
        String totalEsr = binding.inputTotalReadingEsrWater.getText().toString();

        String startVillage = binding.inputStartReadingVillageWater.getText().toString();
        String endVillage = binding.inputEndReadingVillageWater.getText().toString();
        String totalVillage = binding.inputTotalReadingVillageWater.getText().toString();

        if (startEsr.isEmpty()) {
            progressHelper.message("Please enter ESR's start reading");
            return;
        }

        /*if (waterSupplyData.isUpdate()) {
            if (endEsr.isEmpty()) {
                progressHelper.message("Please enter ESR's end reading");
                return;
            }
        }*/

        if (!endEsr.isEmpty()) {
            if (!isReadingCorrect(startEsr, endEsr)) {
                progressHelper.message("ESR's end reading must be greater than start reading");
                return;
            }
        }

        if (selectedVillage.isEmpty()) {
            progressHelper.message("Please select village");
            return;
        }

        if (startVillage.isEmpty()) {
            progressHelper.message("Please enter village's start reading");
            return;
        }

        if (waterSupplyData.isUpdate()) {
            if (endVillage.isEmpty()) {
                progressHelper.message("Please enter village end reading");
                return;
            }
        }

        if (!endVillage.isEmpty()) {
            if (!isReadingCorrect(startVillage, endVillage)) {
                progressHelper.message("village's end reading must be greater than start reading");
                return;
            }
        }

        if (remark.isEmpty()) {
            progressHelper.message("Please enter remark");
            return;
        }

        if (!progressHelper.isAutoTimeEnabled(this)) {
            progressHelper.message("Please set time automatically from settings");
            return;
        }

       /* if (latitude == 0 && longitude == 0) {
            progressHelper.message("Location not found.");
            return;
        }*/

        Map<String, Object> body = new HashMap<>();
        body.put("intake_id", waterSupplyData.getIntakeId());
        body.put("wtp_id", waterSupplyData.getWtpId());
        body.put("relation_id", "0");
        body.put("esr_start_reading", Double.parseDouble(startEsr));
        body.put("esr_end_reading", endEsr.isEmpty() ? 0 : Double.parseDouble(endEsr));
        body.put("esr_total_water_supplied", endEsr.isEmpty() ? 0 : Double.parseDouble(totalEsr));
        body.put("esr_latitude", latitude);
        body.put("esr_longitude", longitude);
        body.put("esr_remarks", remark);
        body.put("village_name", selectedVillage);
        body.put("village_lgd_cd", "0");
        body.put("survey_date", SingleDatePicker.getCurrentDate());
        body.put("survey_time", SingleDatePicker.getCurrentTime());
        body.put("start_reading", Double.parseDouble(startVillage));
        body.put("end_reading", endVillage.isEmpty() ? 0 : Double.parseDouble(endVillage));
        body.put("total_water_supplied", totalVillage.isEmpty() ? 0 : Double.parseDouble(totalVillage));

        body.put("latitude", latitude);
        body.put("longitude", longitude);
        body.put("remarks", remark);
        body.put("esr_name", waterSupplyData.getEsrName());

        if (waterSupplyData.isUpdate()) {
            body.put("esr_photo_path_start_reading", waterSupplyData.getStartPhotoPath());
            body.put("esr_photo_path_end_reading", endPhotoBase64);
            body.put("photo_path_start_reading", waterSupplyData.getStartPhotoPathVillage());
            body.put("photo_path_end_reading", endVillagePhotoBase64);

            body.put("updated_by", String.valueOf(login.getId()));
            body.put("updated_by_role", String.valueOf(login.getRoleId()));
//            body.put("updated_by", prefManager.getSqcId());
//            body.put("updated_by_role", prefManager.getRoleId());
        } else {
            String esrPhoto = "";
            if (waterSupplyData.getStartPhotoPath() != null) {
                esrPhoto = waterSupplyData.getStartPhotoPath();
            } else {
                esrPhoto = startPhotoBase64;
            }
            body.put("esr_photo_path_start_reading", esrPhoto);
            body.put("esr_photo_path_end_reading", endPhotoBase64);
            body.put("photo_path_start_reading", startVillagePhotoBase64);
            body.put("photo_path_end_reading", endVillagePhotoBase64);
            body.put("created_by", String.valueOf(login.getId()));
            body.put("created_by_role", String.valueOf(login.getRoleId()));
//            body.put("created_by", prefManager.getSqcId());
//            body.put("created_by_role", prefManager.getRoleId());
        }

        progressHelper.showProgress("Please wait...");
        Call<SimpleResponse> call;

        if (waterSupplyData.isUpdate()) {
            call = apiInterface.updateESRForm(body);
        } else {
            call = apiInterface.esrSubmit(body);
        }

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressHelper.dismissProgress();

                //  String successMessage = endVillage.isEmpty() ? "Village start reading" : "Village end reading";
                String successMessage = endVillage.isEmpty() ? "Village start reading submitted successfully.\nEnd meter reading is pending" : "Village both reading submit successfully";


                if (response.body().isSuccess()) {
                    progressHelper.showSuccessDialog(successMessage, "ESR", "Done", "Done", type -> {
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    });
                } else {
                    progressHelper.message("Failed to submit form. Try again!");
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void onRemoveClicked(int position, EMFReading emfReading) {
        emfReadingList.remove(position);
        emfReadingAdapter.notifyDataSetChanged();
    }

    // TODO: 04-10-2024 : getting current location code
    @Override
    public void onLocationPermissionAllow(PermissionHelper.LocationPermissionEnum permission) {

        if (permission == PermissionHelper.LocationPermissionEnum.ALLOW) {
            locationErrorTextView.setVisibility(View.GONE);
            btnAllowLocation.setVisibility(View.GONE);
            googleMapContainer.setVisibility(View.VISIBLE);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getCurrentLocation();
        } else {
            latitude = 0;
            longitude = 0;
        }

        /*else if (permission == PermissionHelper.LocationPermissionEnum.DENY) {
            locationErrorTextView.setText("Location Permission Deny.Please allow permission");
            btnAllowLocation.setText("Allow Permission");
            permissionErrorType = "deny";
            locationErrorTextView.setVisibility(View.VISIBLE);
            btnAllowLocation.setVisibility(View.VISIBLE);
            googleMapContainer.setVisibility(View.GONE);
        } else if (permission == PermissionHelper.LocationPermissionEnum.PERMANENT_DENY) {
            locationErrorTextView.setText("Location Permission Deny.Please allow permission");
            btnAllowLocation.setText("Go to settings");
            permissionErrorType = "permanent";
            locationErrorTextView.setVisibility(View.VISIBLE);
            btnAllowLocation.setVisibility(View.VISIBLE);
            googleMapContainer.setVisibility(View.GONE);
        } else if (permission == PermissionHelper.LocationPermissionEnum.GPS_DISABLED) {
            locationErrorTextView.setText("Your GPS is disable.");
            btnAllowLocation.setText("Enable");
            permissionErrorType = "gps";
            locationErrorTextView.setVisibility(View.VISIBLE);
            btnAllowLocation.setVisibility(View.VISIBLE);
            googleMapContainer.setVisibility(View.GONE);
        }*/
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProgressBar.setVisibility(View.VISIBLE);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    locationProgressBar.setVisibility(View.GONE);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                        mapFragment.getMapAsync(DharaWaterFormActivity.this);

                        locationAddressTextView.setText(permissionHelper.getAddressFromLatLng(latitude, longitude));
                    } else {
                        locationProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.473324, 77.947998), 6));
        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        // mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (permissionErrorType != null) {
            permissionHelper.requestLocationPermission(this);
        }
    }

    // TODO: 04-10-2024 :-----------------------------------------------------------------
    @Override
    public void onCameraPermissionAllow(boolean allow) {
        if (allow) {
            imageHelper.showCaptureOrSelectImageDialog();//dispatchTakePictureIntent();
        } else {
            progressHelper.message("Camera Permission deny");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageHelper.CAPTURE_IMAGE_CODE
                && resultCode == RESULT_OK) {
            File file = new File(imageHelper.moveImageToDestination(imageHelper.photoFilePath, latitude, longitude));
            imageHelper.compressImage(file.getPath(), file, this);
        }else if (requestCode == ImageHelper.SELECT_IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            File file = new File(imageHelper.moveImageToDestination(imageHelper.getRealPath(data.getData()), latitude, longitude));
            imageHelper.compressImage(file.getPath(), file, this);
        }
    }

    @Override
    public void onImageCompressCompleted(String path, boolean isError) {
        if (photoType.equalsIgnoreCase("intake_start")) {
            Glide.with(DharaWaterFormActivity.this).load(path).into(binding.intakeWaterStartImageView);
            binding.intakeWaterStartImageView.setVisibility(View.VISIBLE);
            startPhotoBase64 = imageHelper.imageToBase64(path);
        } else if (photoType.equalsIgnoreCase("intake_end")) {

            Glide.with(DharaWaterFormActivity.this).load(path).into(binding.intakeWaterEndImageView);
            binding.intakeWaterEndImageView.setVisibility(View.VISIBLE);
            endPhotoBase64 = imageHelper.imageToBase64(path);

        } else if (photoType.equalsIgnoreCase("wtp_start")) {
            Glide.with(DharaWaterFormActivity.this).load(path).into(binding.wtpWaterStartImageView);
            binding.wtpWaterStartImageView.setVisibility(View.VISIBLE);
            startPhotoBase64 = imageHelper.imageToBase64(path);
        } else if (photoType.equalsIgnoreCase("wtp_end")) {
            Glide.with(DharaWaterFormActivity.this).load(path).into(binding.wtpWaterEndImageView);
            binding.wtpWaterEndImageView.setVisibility(View.VISIBLE);

            endPhotoBase64 = imageHelper.imageToBase64(path);

        } else if (photoType.equalsIgnoreCase("esr_start")) {
            Glide.with(DharaWaterFormActivity.this).load(path).into(binding.esrWaterStartImageView);
            startPhotoBase64 = imageHelper.imageToBase64(path);
            binding.esrWaterStartImageView.setVisibility(View.VISIBLE);
        } else if (photoType.equalsIgnoreCase("esr_end")) {
            Glide.with(DharaWaterFormActivity.this).load(path).into(binding.esrWaterEndImageView);
            binding.esrWaterEndImageView.setVisibility(View.VISIBLE);
            endPhotoBase64 = imageHelper.imageToBase64(path);
        } else if (photoType.equalsIgnoreCase("village_start")) {
            Glide.with(DharaWaterFormActivity.this).load(path).into(binding.esrWaterStartVillageImageView);
            binding.esrWaterStartVillageImageView.setVisibility(View.VISIBLE);
            startVillagePhotoBase64 = imageHelper.imageToBase64(path);
        } else if (photoType.equalsIgnoreCase("village_end")) {
            Glide.with(DharaWaterFormActivity.this).load(path).into(binding.esrWaterVillageEndImageView);
            binding.esrWaterVillageEndImageView.setVisibility(View.VISIBLE);
            endVillagePhotoBase64 = imageHelper.imageToBase64(path);
        }
    }

    // TODO: 14-10-2024 : speech to text
    private void openMic() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.need_to_speak));
        try {
            activityResultMIC.launch(intent);
        } catch (ActivityNotFoundException a) {
            Utility.show(this, getString(R.string.sorry_your_device_not_supported));
        }
    }

    private void micIntentCallBack() {
        activityResultMIC = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && null != result.getData()) {
                ArrayList<String> r = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                binding.inputRemarkDharaWaterForm.setText(String.format("%s %s", binding.inputRemarkDharaWaterForm.getText().toString().trim(), r != null ? r.get(0) : ""));
            }
        });

    }

    // TODO: 15-10-2024 : getting village list only in case of ESR
    private void getVillage() {
        Map<String, String> body = new HashMap<>();
        body.put("esr", waterSupplyData.getEsrName());
        Call<DharaVillage> call = apiInterface.getDharaVillage(body);

        call.enqueue(new Callback<DharaVillage>() {
            @Override
            public void onResponse(Call<DharaVillage> call, Response<DharaVillage> response) {
                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {
                        for (DharaVillage.DharaVillageData data : response.body().getData()) {
                            VillageTable village = new VillageTable();
                            village.setVillageId("0");
                            village.setVillageName(data.getVillageName());
                            villageList.add(village);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DharaVillage> call, Throwable t) {

            }
        });
    }

    private void showVillageDialog() {

        if (villageList.isEmpty()) {
            progressHelper.message("No village found");
            return;
        }

        customDialogHelper.showVillageDialog(village -> {
            selectedVillage = village.getVillageName();
            binding.selectEsrVillageWaterTextView.setText(selectedVillage);

            checkESRForm(waterSupplyData, selectedVillage);

        }, villageList, selectedVillage);
    }

    /**
     * here we check ESR already filled form
     *
     * @param supply      : Existing detail of supply data
     * @param villageName : Village name
     */
    private void checkESRForm(DailyWaterSupply.DailyWaterSupplyData supply, String villageName) {

        Map<String, String> body = new HashMap<>();
        body.put("esr_name", supply.getEsrName());
        body.put(Utility.E_TOKEN, "");
        body.put("village_name", villageName);
        body.put("survey_date", SingleDatePicker.getCurrentDate());
        body.put("user_id", String.valueOf(login.getId()));
//        body.put("user_id", prefManager.getSqcId());

        progressHelper.showProgress("Please wait...");
        Call<DharaESRHistory> call = apiInterface.getDharaESRHistory(body);

        call.enqueue(new Callback<DharaESRHistory>() {
            @Override
            public void onResponse(Call<DharaESRHistory> call, Response<DharaESRHistory> response) {
                progressHelper.dismissProgress();
                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {
                        List<DharaESRHistory.DharaESRHistoryData> list = response.body().getData();
                        DharaESRHistory.DharaESRHistoryData data = list.get(list.size() - 1);

                        supply.setStartReading(data.getEsrStartReading());
                        supply.setEndReading(data.getEsrEndReading());
                        supply.setTotalReading(data.getEsrTotalWaterSupplied());
                        supply.setStartPhotoPath(data.getEsrPhotoPathStartReading());
                        supply.setEndPhotoPath(data.getEsrPhotoPathEndReading());
                        supply.setRemark(data.getRemarks());

                        supply.setStartReadingVillage(data.getStartReading());
                        supply.setEndReadingVillage(data.getEndReading());
                        supply.setTotalReadingVillage(data.getTotalWaterSupplied());
                        supply.setStartPhotoPathVillage(data.getPhotoPathStartReading());
                        supply.setEndPhotoPathVillage(data.getPhotoPathEndReading());
                        supply.setVillageName(data.getVillageName());
                        supply.setUpdate(true);


                        waterSupplyData = supply;
                        initESRForm();
                    } else {
                        waterSupplyData.setUpdate(false);
                        initESRForm();
                    }
                } else {
                    waterSupplyData.setUpdate(false);
                    initESRForm();
                }
            }

            @Override
            public void onFailure(Call<DharaESRHistory> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);

            }
        });
    }


}

