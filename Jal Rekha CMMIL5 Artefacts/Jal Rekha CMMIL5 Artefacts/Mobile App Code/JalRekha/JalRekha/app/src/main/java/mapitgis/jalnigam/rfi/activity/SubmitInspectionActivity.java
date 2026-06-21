package mapitgis.jalnigam.rfi.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
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
import mapitgis.jalnigam.rfi.model.PostFEInspection;
import mapitgis.jalnigam.rfi.model.SimpleResponse;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import mapitgis.jalnigam.room.table.InspectionRequestTable;
import mapitgis.jalnigam.rfi.viewmodel.ContractorViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitInspectionActivity extends AppCompatActivity implements QAQCReviewAdapter.QAQCReviewListener, PermissionHelper.LocationPermissionListener, SelectedPhotoAdapter.SelectedPhotoListener, PermissionHelper.CameraPermissionListener, ImageHelper.ImageCompressListener {

    private ImageView backImageView;
    private TextView titleTextView;
    private TextView selectEnclosurePhotoTv;

    private RecyclerView qaQcRecyclerView;
    private List<SpinnerItem> qaList;
    private QAQCReviewAdapter reviewAdapter;

    private EditText inputComment;

    private InspectionRequestTable history;
    private SpinnerItem selectedQaQcReview;

    private PrefManager prefManager;
    private ProgressHelper progressHelper;
    private ApiInterface apiInterface;
    private ImageHelper imageHelper;
    private PermissionHelper permissionHelper;

    private FusedLocationProviderClient fusedLocationClient;
    private double latitude, longitude;
    private String geoAddress;

    // TODO: 04-07-2024 : selected photos
    private RecyclerView photosRecyclerView;
    private SelectedPhotoAdapter selectedPhotoAdapter;
    private List<String> selectedPhotosList = new ArrayList<>();

    private Button btnView, btnSubmit;

    private ContractorViewModel contractorViewModel;

    private ActivityResultLauncher<Intent> activityResultMIC;

    private Login login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_submit_inspection);

        login = SqLite.instance(this).getLogin();

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        prefManager = new PrefManager(this);
        progressHelper = new ProgressHelper(this);
        imageHelper = new ImageHelper(this);
        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        permissionHelper = new PermissionHelper(this);
        contractorViewModel = new ViewModelProvider(this).get(ContractorViewModel.class);


        history = (InspectionRequestTable) getIntent().getSerializableExtra("detail");

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Inspection");
        backImageView.setOnClickListener(v -> finish());

        inputComment = findViewById(R.id.input_inspection_qa_qc_comment);
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            String blockCharacterSet = "~#^|$%&*!:'-;";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        };
        inputComment.setFilters(new InputFilter[]{filter});


        LinearLayout micContainer = findViewById(R.id.inspection_qa_qc_tap_mic_container);
        micContainer.setOnClickListener(v -> openMic());

        qaQcRecyclerView = findViewById(R.id.qa_qc_review_recycler_view);
        // TODO: 15-07-2024 : getting QA and QC Review
        qaList = SqLite.instance(this).GET_QA_QC_REVIEW_DI_NEW();
        reviewAdapter = new QAQCReviewAdapter(this, qaList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        qaQcRecyclerView.setLayoutManager(manager);
        qaQcRecyclerView.setAdapter(reviewAdapter);

        selectEnclosurePhotoTv = findViewById(R.id.select_inspection_photo_text_view);
        photosRecyclerView = findViewById(R.id.enclosure_photo_recycler_view);
        selectedPhotoAdapter = new SelectedPhotoAdapter(this, selectedPhotosList, this);

        LinearLayoutManager photoManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photosRecyclerView.setLayoutManager(photoManager);
        photosRecyclerView.setAdapter(selectedPhotoAdapter);

        permissionHelper.requestLocationPermission(this);
        selectEnclosurePhotoTv.setOnClickListener(v -> permissionHelper.requestCameraPermission(this));

        btnSubmit = findViewById(R.id.btn_submit_inspection);
        btnView = findViewById(R.id.btn_view_encolsure_req);

        btnView.setOnClickListener(v -> photos());
        btnSubmit.setOnClickListener(v -> submit());

        micIntentCallBack();
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

    private void photos() {
        String[] itemsArray = history.getImages().split(",");

        List<String> itemsList = Arrays.asList(itemsArray);
        ArrayList<String> updatedItemsList = addDefaultValueToList(itemsList);

        startActivity(new Intent(this, FullImageActivity.class)
                .putStringArrayListExtra("list", updatedItemsList));
    }

    private ArrayList<String> addDefaultValueToList(List<String> list) {
        ArrayList<String> updatedList = new ArrayList<>();
        for (String item : list) {
//            updatedList.add(defaultValue + item);
            updatedList.add(item.trim());
        }
        return updatedList;
    }

    @Override
    public void onQAQCReviewClicked(SpinnerItem qaqcReview) {
        selectedQaQcReview = qaqcReview;
    }

    private void submit() {

        List<String> newList = new ArrayList<>();
        String comments = inputComment.getText().toString().trim();


        if (comments.isEmpty()) {
            progressHelper.message("Please enter comments");
            return;
        }

        if (selectedQaQcReview == null) {
            progressHelper.message("Please select review");
            return;
        }

        if (selectedPhotosList.isEmpty()) {
            progressHelper.message("Please select at least 1 photo");
            return;
        }

        if (selectedPhotosList.size() > 10) {
            progressHelper.message("Photo limit exceed. Maximum 10 photos allowed");
            return;
        }

        for (String photos : selectedPhotosList) {
            String image = imageHelper.imageToBase64(photos);
            newList.add(image);
        }

        PostFEInspection postFEInspection = new PostFEInspection();
        postFEInspection.setRfiId(history.getRfiId());
//        postFEInspection.setToken(prefManager.getToken1());
        postFEInspection.setToken(login.getToken());
        //  postFEInspection.setAssignedBy("47");
        postFEInspection.setAssignedTo(history.getPiuId());
        postFEInspection.setReviewId(selectedQaQcReview.getKeyString());
        postFEInspection.setComments(comments.replace("'", "").replace("\"", "\\\""));
        postFEInspection.setLatitude(String.valueOf(latitude));
        postFEInspection.setLongitude(String.valueOf(longitude));
        postFEInspection.setAddress(geoAddress);
        postFEInspection.setImages(newList);

        Call<SimpleResponse> call = apiInterface.submitInspectionByFE(postFEInspection);

        progressHelper.showProgress("Please wait...");

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressHelper.dismissProgress();
                if (response.body().isSuccess()) {
                    progressHelper.message("Submitted");
                    contractorViewModel.deleteOrUpdateInspectionById(history.getId(), true);
                    startActivity(new Intent(SubmitInspectionActivity.this, RFIDBActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    finish();
                } else {

                    if (response.body().getMessage().toLowerCase().contains("session expired")) {
                        progressHelper.showSuccessDialog("Your session is expired. Please login again", "Session Expired", "back", "OK", new ProgressHelper.ShowDialogListener() {
                            @Override
                            public void onShowDialogButtonClicked(String type) {
                                prefManager.logout();
                                Utility.goFirst(SubmitInspectionActivity.this,true);
//                                startActivity(new Intent(SubmitInspectionActivity.this, TypeActivity.class)
//                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));

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
                progressHelper.message("Something went wrong");
                progressHelper.dismissProgress();
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
    public void onCameraPermissionAllow(boolean allow) {
        if (allow) {
            imageHelper.dispatchTakePictureIntent();
        } else {
            progressHelper.message("Camera Permission deny");
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