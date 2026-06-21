package mapitgis.jalnigam.dhara;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityDharaContractorApprovedBinding;
import mapitgis.jalnigam.rfi.helper.ImageHelper;
import mapitgis.jalnigam.rfi.helper.PermissionHelper;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.helper.SingleDatePicker;
import mapitgis.jalnigam.rfi.model.SimpleResponse;
import mapitgis.jalnigam.rfi.model.dhara.DharaContractorApproved;
import mapitgis.jalnigam.rfi.model.dhara.DharaVillage;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import mapitgis.jalnigam.room.table.VillageTable;
import mapitgis.jalnigam.rfi.utils.CustomDialogHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DharaContractorApprovedActivity extends AppCompatActivity implements SingleDatePicker.SingleDateListener, PermissionHelper.CameraPermissionListener, ImageHelper.ImageCompressListener {

    private ImageView backImageView;
    private TextView titleTextView;
    private Button btnLogout;

    private ActivityDharaContractorApprovedBinding binding;

    private ProgressHelper progressHelper;
    private PrefManager prefManager;
    private ApiInterface apiInterface;
    private CustomDialogHelper dialogHelper;
    private SingleDatePicker singleDatePicker;
    private PermissionHelper permissionHelper;
    private ImageHelper imageHelper;

    private String selectedESR = "";
    private List<VillageTable> villageList = new ArrayList<>();
    private String selectedVillage = "";
    private String uploadAttachementBase64 = "";
    private String fileExtension;
    private String selectedDate = "";

    private ActivityResultLauncher<Intent> activityResultMIC;

    private DharaContractorApproved.DharaContractorApprovedData searchedApprovedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        //  setContentView(R.layout.activity_dhara_contractor_approved);
        binding = ActivityDharaContractorApprovedBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        progressHelper = new ProgressHelper(this);
        dialogHelper = new CustomDialogHelper(this);
        prefManager = new PrefManager(this);
        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        singleDatePicker = new SingleDatePicker(this, false,false);
        permissionHelper = new PermissionHelper(this);
        imageHelper = new ImageHelper(this);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Approved Details");
        backImageView.setOnClickListener(v -> finish());

        binding.dharaContractorEsrSelectTv.setOnClickListener(v -> showEsrDialog());

        binding.dharaContractorVillageSelectTv.setOnClickListener(v -> {
            if (selectedESR.isEmpty()) {
                progressHelper.message("Please select ESR");
                return;
            }
            showVillageDialog();
        });

        binding.dharaContractorSelectFromDateTv.setOnClickListener(v ->
                singleDatePicker.show(getSupportFragmentManager(), "Select Date"));

        binding.dharaContractorSearchBtnTv.setOnClickListener(v -> getContractorApprovedData());

        binding.dharaContractorApprovedAttachmentPhotoBtn.setOnClickListener(v -> permissionHelper.requestCameraPermission(this));

        binding.dharaContractorApprovedTapMicContainer.setOnClickListener(v -> openMic());

        binding.btnSubmitWaterContractorApproved.setOnClickListener(v -> submit());

        micIntentCallBack();
    }

    private void showEsrDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select ESR");

        String[] itemsArray = prefManager.getEsrNameList().toArray(new String[0]);
        builder.setItems(itemsArray, (dialog, which) -> {
            selectedESR = itemsArray[which];
            binding.dharaContractorEsrSelectTv.setText(selectedESR);

            selectedVillage = "";
            villageList.clear();
            binding.dharaContractorVillageSelectTv.setText("Select Village");
            getVillage();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // TODO: 15-10-2024 : getting village list only in case of ESR
    private void getVillage() {
        Map<String, String> body = new HashMap<>();
        body.put("intake_id", prefManager.getSchemeId());
        body.put("wtp_id", prefManager.getSchemeId());
        body.put("ohsr_code", selectedESR);
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

        dialogHelper.showVillageDialog(village -> {
            selectedVillage = village.getVillageName();
            binding.dharaContractorVillageSelectTv.setText(selectedVillage);

        }, villageList, selectedVillage);
    }

    @Override
    public void onDateSelected(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        selectedDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(c.getTime());
        binding.dharaContractorSelectFromDateTv.setText(selectedDate);
    }

    // TODO: 18-10-2024 : here we search water supply details
    private void getContractorApprovedData() {

        if (selectedESR.isEmpty()) {
            progressHelper.message("Please select ESR");
            return;
        }

        if (selectedVillage.isEmpty()) {
            progressHelper.message("Please select village");
            return;
        }

        if (selectedDate.isEmpty()) {
            progressHelper.message("Please select survey date");
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("intake_id", prefManager.getSchemeId());
        body.put("esr_ost_name", selectedESR);
        body.put("village_name", selectedVillage);
        body.put("survey_date", selectedDate);

        progressHelper.showProgress("Please wait...");

        Call<DharaContractorApproved> call = apiInterface.getDharaContractorApproved(body);

        call.enqueue(new Callback<DharaContractorApproved>() {
            @Override
            public void onResponse(Call<DharaContractorApproved> call, Response<DharaContractorApproved> response) {
                progressHelper.dismissProgress();

                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {

                        List<DharaContractorApproved.DharaContractorApprovedData> list = response.body().getData();
                        searchedApprovedData = list.get(list.size() - 1);
                        binding.dharaContractorApprovedNestScrollView.setVisibility(View.VISIBLE);

                        binding.dharaContractorApprovedIntakeStartReadingTv.setText(searchedApprovedData.getIntakStartReading());
                        binding.dharaContractorApprovedIntakeEndReadingTv.setText(searchedApprovedData.getIntakEndReading());
                        binding.dharaContractorApprovedIntakeTotalReadingTv.setText(searchedApprovedData.getIntakTotalRawWaterDrawn());

                        binding.dharaContractorApprovedWtpStartReadingTv.setText(searchedApprovedData.getWtpStartReading());
                        binding.dharaContractorApprovedWtpEndReadingTv.setText(searchedApprovedData.getWtpEndReading());
                        binding.dharaContractorApprovedWtpTotalReadingTv.setText(searchedApprovedData.getWtpTotalRawWaterDrawn());

                        binding.dharaContractorApprovedEsrStartReadingTv.setText(searchedApprovedData.getEsrStartReading());
                        binding.dharaContractorApprovedEsrEndReadingTv.setText(searchedApprovedData.getEsrEndReading());
                        binding.dharaContractorApprovedEsrTotalReadingTv.setText(searchedApprovedData.getEsrTotalWaterSupplied());

                        binding.dharaContractorApprovedVillageStartReadingTv.setText(searchedApprovedData.getStartReading());
                        binding.dharaContractorApprovedVillageEndReadingTv.setText(searchedApprovedData.getEndReading());
                        binding.dharaContractorApprovedVillageTotalReadingTv.setText(searchedApprovedData.getTotalWaterSupplied());

                    } else {
                        progressHelper.message("No data found");
                        binding.dharaContractorApprovedNestScrollView.setVisibility(View.GONE);
                    }
                } else {
                    progressHelper.message("No data found");
                    binding.dharaContractorApprovedNestScrollView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<DharaContractorApproved> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);
                binding.dharaContractorApprovedNestScrollView.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onCameraPermissionAllow(boolean allow) {
        if (allow) {
            imageHelper.dispatchTakePictureIntent();
        } else {
            progressHelper.message("Camera permission denied");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageHelper.CAPTURE_IMAGE_CODE
                && resultCode == RESULT_OK) {
            File file = new File(imageHelper.moveImageToDestination(imageHelper.photoFilePath, 0, 0));

            imageHelper.compressImage(file.getPath(), file, this);
        }
    }

    @Override
    public void onImageCompressCompleted(String path, boolean isError) {

        fileExtension = imageHelper.getFileExtension(path);

        Glide.with(DharaContractorApprovedActivity.this).load(path).into(binding.dharaContractorApprovedAttachementImageView);
        binding.dharaContractorApprovedAttachementImageView.setVisibility(View.VISIBLE);
        uploadAttachementBase64 = imageHelper.imageToBase64(path);
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
                binding.inputRemarkDharaContractorApproved.setText(String.format("%s %s", binding.inputRemarkDharaContractorApproved.getText().toString().trim(), r != null ? r.get(0) : ""));
            }
        });
    }

    private void submit() {

        String remark = binding.inputRemarkDharaContractorApproved.getText().toString();

        if (uploadAttachementBase64.isEmpty()) {
            progressHelper.message("Please Capture photo");
            return;
        }

        if (remark.isEmpty()) {
            progressHelper.message("Please enter remark");
        }

        Map<String, String> body = new HashMap<>();
        body.put("intake_id", searchedApprovedData.getIntakeId());
        body.put("wtp_id", searchedApprovedData.getWtpId());
        body.put("esr_name", selectedESR);
        body.put("village_name", selectedVillage);
        body.put("village_lgd_cd", "0");
        body.put("survey_date", selectedDate);
        body.put("contractor_remarks", remark);
        body.put("contractor_attachment", "");
        body.put("attachment_ext", fileExtension.replace(".", ""));

        progressHelper.showProgress("Please wait...");

        Call<SimpleResponse> call = apiInterface.submitDharaContractorRemark(body);

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressHelper.dismissProgress();

                if (response.body().isSuccess()) {
                    clearForm();
                    progressHelper.message("Data submitted successfully");
                } else {
                    progressHelper.message(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);
            }
        });

    }

    private void clearForm(){
        binding.dharaContractorApprovedNestScrollView.setVisibility(View.GONE);
        binding.dharaContractorVillageSelectTv.setText("Select Village");
        binding.dharaContractorEsrSelectTv.setText("Select ESR");
        binding.dharaContractorSelectFromDateTv.setText("Select Date");

        selectedDate = "";
        selectedESR = "";
        selectedVillage = "";

        binding.dharaContractorApprovedIntakeStartReadingTv.setText("");
        binding.dharaContractorApprovedIntakeEndReadingTv.setText("");
        binding.dharaContractorApprovedIntakeTotalReadingTv.setText("");

        binding.dharaContractorApprovedWtpStartReadingTv.setText("");
        binding.dharaContractorApprovedWtpEndReadingTv.setText("");
        binding.dharaContractorApprovedWtpTotalReadingTv.setText("");

        binding.dharaContractorApprovedEsrStartReadingTv.setText("");
        binding.dharaContractorApprovedEsrEndReadingTv.setText("");
        binding.dharaContractorApprovedEsrTotalReadingTv.setText("");

        binding.dharaContractorApprovedVillageStartReadingTv.setText("");
        binding.dharaContractorApprovedVillageEndReadingTv.setText("");
        binding.dharaContractorApprovedVillageTotalReadingTv.setText("");

        binding.inputRemarkDharaContractorApproved.setText("");
        binding.dharaContractorApprovedAttachementImageView.setVisibility(View.GONE);
        uploadAttachementBase64 = "";
    }
}