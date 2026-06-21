package mapitgis.jalnigam.rfi.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.rfi.adapter.QAQCReviewAdapter;
import mapitgis.jalnigam.rfi.adapter.SelectedPhotoAdapter;
import mapitgis.jalnigam.rfi.helper.ImageHelper;
import mapitgis.jalnigam.rfi.helper.PermissionHelper;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.ContractorDISurvey;
import mapitgis.jalnigam.rfi.model.SimpleResponse;
import mapitgis.jalnigam.rfi.model.UpdateStatusDIContractor;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import mapitgis.jalnigam.rfi.viewmodel.ContractorViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateContractorSurveyActivity extends AppCompatActivity implements QAQCReviewAdapter.QAQCReviewListener, PermissionHelper.LocationPermissionListener, SelectedPhotoAdapter.SelectedPhotoListener, PermissionHelper.CameraPermissionListener, ImageHelper.ImageCompressListener {

    private ImageView backImageView;
    private TextView titleTextView;

    private ProgressHelper progressHelper;
    private PrefManager prefManager;
    private ContractorViewModel contractorViewModel;
    private ApiInterface apiInterface;
    private PermissionHelper permissionHelper;
    private ImageHelper imageHelper;

    private Button btnUpdate, btnViewImage;
    private EditText inputComment;

    private ContractorDISurvey.ContractorDISurveyData data;

    private RecyclerView qaQcRecyclerView;
    private List<SpinnerItem> qaList;
    private QAQCReviewAdapter reviewAdapter;

    private SpinnerItem selectedQaQcReview;

    private FusedLocationProviderClient fusedLocationClient;
    private double latitude, longitude;
    private String geoAddress;

    private TextView selectPhotoTv;
    private RecyclerView photosRecyclerView;
    private SelectedPhotoAdapter selectedPhotoAdapter;
    private List<String> selectedPhotosList = new ArrayList<>();

    private ActivityResultLauncher<Intent> activityResultMIC;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_contractor_survey);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        login = SqLite.instance(this).getLogin();

        data = (ContractorDISurvey.ContractorDISurveyData) getIntent().getSerializableExtra("data");

        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        contractorViewModel = new ViewModelProvider(this).get(ContractorViewModel.class);
        progressHelper = new ProgressHelper(this);
        prefManager = new PrefManager(this);
        permissionHelper = new PermissionHelper(this);
        imageHelper = new ImageHelper(this);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Update Status");
        backImageView.setOnClickListener(v -> finish());

        LinearLayout micContainer = findViewById(R.id.contractor_di_tap_mic_container);
        micContainer.setOnClickListener(v -> openMic());

        selectPhotoTv = findViewById(R.id.select_contractor_di_survey_photo_text_view);
        photosRecyclerView = findViewById(R.id.contractor_di_photo_recycler_view);
        selectedPhotoAdapter = new SelectedPhotoAdapter(this, selectedPhotosList, this);

        LinearLayoutManager photoManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photosRecyclerView.setLayoutManager(photoManager);
        photosRecyclerView.setAdapter(selectedPhotoAdapter);


        selectPhotoTv.setOnClickListener(v -> permissionHelper.requestCameraPermission(this));

        inputComment = findViewById(R.id.input_contractor_di_comment);
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            String blockCharacterSet = "~#^|$%&*!:'-;";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        };
        inputComment.setFilters(new InputFilter[]{filter});

        qaQcRecyclerView = findViewById(R.id.contractor_di_qa_qc_review_recycler_view);
        // TODO: 15-07-2024 : getting QA and QC Review
        qaList = SqLite.instance(this).GET_QA_QC_REVIEW_DI_NEW();
        reviewAdapter = new QAQCReviewAdapter(this, qaList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        qaQcRecyclerView.setLayoutManager(manager);
        qaQcRecyclerView.setAdapter(reviewAdapter);

        btnUpdate = findViewById(R.id.btn_update_contractor_di_status);
        btnUpdate.setOnClickListener(v -> updateContractor());

        btnViewImage = findViewById(R.id.btn_view_image_contractor_di_status);
        btnViewImage.setOnClickListener(v -> photos(data.getImgUrl()));

        permissionHelper.requestLocationPermission(this);

        micIntentCallBack();

        if (login.getRoleId() == 9) {//RFI Contractor
//        if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
            qaQcRecyclerView.setVisibility(View.GONE);
        } else {
            qaQcRecyclerView.setVisibility(View.VISIBLE);
        }
    }

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
                inputComment.setText(String.format("%s %s", inputComment.getText().toString().trim(), r != null ? r.get(0) : ""));
            }
        });

    }

    private void photos(String photos) {

        String[] itemsArray = photos.split(",");

        List<String> itemsList = Arrays.asList(itemsArray);
        ArrayList<String> updatedItemsList = addDefaultValueToList(itemsList);

        startActivity(new Intent(this, FullImageActivity.class)
                .putStringArrayListExtra("list", updatedItemsList));
    }

    private ArrayList<String> addDefaultValueToList(List<String> list) {
        ArrayList<String> updatedList = new ArrayList<>();
        for (String item : list) {
            updatedList.add(item.trim());
        }
        return updatedList;
    }

    @Override
    public void onQAQCReviewClicked(SpinnerItem qaqcReview) {
        selectedQaQcReview = qaqcReview;
    }

    private void updateContractor() {

        List<String> newList = new ArrayList<>();

        String comment = inputComment.getText().toString();

        if (login.getRoleId() != 9) {//Not RFI Contractor
//        if (prefManager.getUserType().equalsIgnoreCase("field engineer")) {
            if (selectedQaQcReview == null) {
                progressHelper.message("Please select status");
                return;
            }
        }

        if (comment.isEmpty()) {
            progressHelper.message("Please enter comment");
            return;
        }

        if (selectedPhotosList.size() > 10) {
            progressHelper.message("Photo limit exceed. Maximum 10 photos allowed");
            return;
        }

        if (latitude == 0) {
            progressHelper.message("Location searching...");
            permissionHelper.requestLocationPermission(this);
            return;
        }

        progressHelper.showProgress("Please wait...");


        for (String photos : selectedPhotosList) {
            String image = imageHelper.imageToBase64(photos);
            newList.add(image);
        }

        UpdateStatusDIContractor updateStatus = new UpdateStatusDIContractor();
        updateStatus.setInspectionId(data.getInspectionId());
        updateStatus.setInspectionComments(comment);
        updateStatus.setReviewId(login.getRoleId() == 9 ? "0" : selectedQaQcReview.getKeyString());
        updateStatus.setToken(login.getToken());///prefManager.getToken1());
        updateStatus.setLatitude(String.valueOf(latitude));
        updateStatus.setLongitude(String.valueOf(longitude));
        updateStatus.setImageList(newList);
        updateStatus.setInspectionStatus(login.getRoleId() == 9 ? "3" : "0");
        updateStatus.setAllotmentType(login.getRoleId() == 9 ? "FE" : "Contractor");
        updateStatus.setAssignTo(login.getRoleId() == 9 ? data.getCreatedById() : data.getContractorId());

        Call<SimpleResponse> call;

        if (login.getRoleId() == 9) {//RFI Contractor
//        if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
            call = apiInterface.updateStatusDIReqContractor(updateStatus);
        } else {
            call = apiInterface.updateStatusDIReqFE(updateStatus);
        }


        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressHelper.dismissProgress();
                if (response.body().isSuccess()) {
                    progressHelper.message(response.body().getMessage());
                    Intent intent = new Intent();
                    intent.putExtra("ok", "ok");
                    setResult(123, intent);
                    finish();
                } else {
                    if (response.body().getMessage().toLowerCase().contains("session expired")) {
                        progressHelper.showSuccessDialog("Your session is expired. Please login again", "Session Expired", "back", "OK", new ProgressHelper.ShowDialogListener() {
                            @Override
                            public void onShowDialogButtonClicked(String type) {
                                prefManager.logout();
                                Utility.goFirst(UpdateContractorSurveyActivity.this,true);
//                                startActivity(new Intent(UpdateContractorSurveyActivity.this, TypeActivity.class)
//                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                                finish();
                            }
                        });
                    } else {
                        progressHelper.message(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message("Something went wrong");
            }
        });
    }

    @Override
    public void onLocationPermissionAllow(PermissionHelper.LocationPermissionEnum permission) {
        if (permission == PermissionHelper.LocationPermissionEnum.ALLOW) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getCurrentLocation();
        } else if (permission == PermissionHelper.LocationPermissionEnum.DENY) {
        } else if (permission == PermissionHelper.LocationPermissionEnum.PERMANENT_DENY) {

        } else if (permission == PermissionHelper.LocationPermissionEnum.GPS_DISABLED) {

        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        geoAddress = permissionHelper.getAddressFromLatLng(latitude, longitude);
                    } else {
                        progressHelper.dismissProgress();
                    }
                });
    }

    @Override
    public void onCameraPermissionAllow(boolean allow) {
        if (allow) {
            imageHelper.dispatchTakePictureIntent();
        } else {
            progressHelper.message("Camera Permission deny");
        }
    }

    @Override
    public void onPhotoClicked(int position, String photos) {
        selectedPhotosList.remove(photos);
        selectedPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageHelper.CAPTURE_IMAGE_CODE
                && resultCode == RESULT_OK) {

            Uri imageUri = imageHelper.imageUri;

            File file = new File(imageHelper.moveImageToDestination(imageHelper.photoFilePath,latitude,longitude));
            imageHelper.compressImage(file.getPath(), file, this);

            /*if (!selectedPhotosList.contains(imageHelper.photoFilePath)) {
                selectedPhotosList.add(imageHelper.photoFilePath);
                selectedPhotoAdapter.notifyDataSetChanged();
            }*/

        }
    }

    @Override
    public void onImageCompressCompleted(String path, boolean isError) {
        if (!selectedPhotosList.contains(path)) {
            selectedPhotosList.add(path);
            selectedPhotoAdapter.notifyDataSetChanged();
        }
    }
}