package mapitgis.jalnigam.rfi.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mapitgis.jalnigamk.nirmal.view.FormKeyValueText;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.rfi.adapter.SelectedPhotoAdapter;
import mapitgis.jalnigam.rfi.helper.ImageHelper;
import mapitgis.jalnigam.rfi.helper.PermissionHelper;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.helper.SingleDatePicker;
import mapitgis.jalnigam.room.table.ApplicationTypeTable;
import mapitgis.jalnigam.room.table.BlockTable;
import mapitgis.jalnigam.room.table.ComponentTypeTable;
import mapitgis.jalnigam.room.table.GramTable;
import mapitgis.jalnigam.room.table.InspectionRequestTable;
import mapitgis.jalnigam.room.table.PipeLineTable;
import mapitgis.jalnigam.room.table.PointTable;
import mapitgis.jalnigam.room.table.SopanOHT;
import mapitgis.jalnigam.room.table.VillageTable;
import mapitgis.jalnigam.rfi.utils.CustomDialogHelper;
import mapitgis.jalnigam.rfi.viewmodel.ContractorViewModel;

public class RequestInspectionActivity extends AppCompatActivity implements
        CustomDialogHelper.DialogComponentListener,
        CustomDialogHelper.DialogStagesListener,
        CustomDialogHelper.DialogBlockListener,
        CustomDialogHelper.DialogGramListener,
        CustomDialogHelper.DialogVillageListener,
        CustomDialogHelper.DialogApplicationTypeListener,
        CustomDialogHelper.DialogPointListener,
        CustomDialogHelper.DialogPipeNoListener,
        SingleDatePicker.SingleDateListener,
        PermissionHelper.CameraPermissionListener,
        SelectedPhotoAdapter.SelectedPhotoListener,
        PermissionHelper.LocationPermissionListener,
        OnMapReadyCallback, ImageHelper.ImageCompressListener {

    private ImageView backImageView;
    private TextView titleTextView;

    private TextView textViewStagesHead, textViewStages;
    private TextView piuNameTextView, schemeNameTextView;
    private TextView tvSelectedOHTMBR, tvLblOHTMBR, tvSelectedPipeNo, tvSelectedInbetweenLength;
    private TextView selectComponentTypeTextView, selectBlockTextView, selectGramTextView;
    private TextView selectVillageTextView, selectApplicationTypeTextView, selectPointTextView;
    private TextView selectInspectionDateTv, selectEnclosurePhotoTv;
    private EditText inputLocation, inputDescription;
    private LinearLayout pointContainerLayout,pipelineLayout, pipeInfoLayout;
    private LinearLayout sopanOHTLayout;

    private CustomDialogHelper customDialogHelper;
    private SingleDatePicker datePicker;
    private PermissionHelper permissionHelper;
    private ProgressHelper progressHelper;
    private ImageHelper imageHelper;

    private Button btnCancel, btnSubmit;

    // TODO: 04-07-2024 : selected photos
    private RecyclerView photosRecyclerView;
    private SelectedPhotoAdapter selectedPhotoAdapter;
    private List<String> selectedPhotosList = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;
    private double latitude, longitude;
    private ProgressBar locationProgressBar;
    private TextView locationErrorTextView, locationAddressTextView;
    private Button btnAllowLocation;
    private LinearLayout googleMapContainer;
    private String permissionErrorType;
    private GoogleMap mMap;

    private String componentType = "", point;
    private String block = "", gram = "", village = "", appType, currentLocation, description, inspectionDate;
    private ComponentTypeTable selectedComponent;
    private BlockTable selectedBlock;
    private GramTable selectedGramP;
    private VillageTable selectedVillage;
    private ApplicationTypeTable selectedApplicationType;
    private PointTable selectedPoint;
    private SpinnerItem selectedStage;

    private PrefManager prefManager;
    private ContractorViewModel contractorViewModel;

    private LinearLayout micContainer;
    private ActivityResultLauncher<Intent> activityResultMIC;

    private RadioGroup radioGroupOHTType;
    private TextView tvSelectedOHTTYPE;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);

        setContentView(R.layout.activity_request_inspection);
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
        contractorViewModel = new ViewModelProvider(this).get(ContractorViewModel.class);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Request Inspection");
        backImageView.setOnClickListener(v -> finish());

        customDialogHelper = new CustomDialogHelper(this);
        datePicker = new SingleDatePicker(this,true,true);
        permissionHelper = new PermissionHelper(this);
        progressHelper = new ProgressHelper(this);
        imageHelper = new ImageHelper(this);

        textViewStagesHead = findViewById(R.id.textViewStagesHead);
        textViewStages = findViewById(R.id.textViewStages);

        piuNameTextView = findViewById(R.id.select_inspection_piu_name_tv);
        schemeNameTextView = findViewById(R.id.select_inspection_scheme_name_tv);
        selectComponentTypeTextView = findViewById(R.id.select_component_type_text_view);
        selectBlockTextView = findViewById(R.id.select_block_name_text_view);
        selectGramTextView = findViewById(R.id.select_gram_text_view);
        selectVillageTextView = findViewById(R.id.select_village_name_text_view);
        selectApplicationTypeTextView = findViewById(R.id.select_application_type_text_view);
        selectPointTextView = findViewById(R.id.select_point_text_view);
        selectInspectionDateTv = findViewById(R.id.select_inspection_date_text_view);
        selectEnclosurePhotoTv = findViewById(R.id.select_encolsure_photo_text_view);

        locationProgressBar = findViewById(R.id.location_progress_bar);
        locationAddressTextView = findViewById(R.id.location_address_text_view);
        locationErrorTextView = findViewById(R.id.location_permission_message_text_view);
        btnAllowLocation = findViewById(R.id.location_permission_btn_allow);
        googleMapContainer = findViewById(R.id.google_map_container);
        pointContainerLayout = findViewById(R.id.select_point_contrainer_layout);
        pipelineLayout = findViewById(R.id.pipeline_layout);
        pipeInfoLayout = findViewById(R.id.layout_pipe_info);
        tvSelectedInbetweenLength = findViewById(R.id.select_inbetween_length);

        sopanOHTLayout = findViewById(R.id.layout_sopan_oht);

        tvLblOHTMBR = findViewById(R.id.tv_lbl_oht_mbr);
        tvSelectedOHTMBR = findViewById(R.id.tv_select_oht_mbr);
        tvSelectedPipeNo = findViewById(R.id.tv_select_pipe);

        //----------------------sopan oht------//
        tvSelectedOHTTYPE = findViewById(R.id.select_type_of_oht);
        tvSelectedOHTTYPE.setOnClickListener(view -> getSpoanOHT());
        radioGroupOHTType = findViewById(R.id.radio_group_oht_type);
        radioGroupOHTType.setOnCheckedChangeListener((radioGroup, i) -> {
            tvSelectedOHTTYPE.setTag(null);
            tvSelectedOHTTYPE.setText(radioGroup.getCheckedRadioButtonId()==R.id.radio_oht_conventional? "Select Conventional OHT" : "Select Pre-Cast OHT");
        });
        //--------------------------------------//

        micContainer = findViewById(R.id.request_inspection_tap_mic_container);
        micContainer.setOnClickListener(v -> openMic());

        inputDescription = findViewById(R.id.input_request_description);
        inputLocation = findViewById(R.id.input_request_current_location);
        btnCancel = findViewById(R.id.btn_inspection_req_cancel);
        btnSubmit = findViewById(R.id.btn_inspection_req_submit);

        // Define a custom InputFilter using regex to restrict special characters
        InputFilter filter = (source, start, end, dest, dstart, dend) -> {
            String blockCharacterSet = "~#^|$%&*!:'-;";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        };
        // Apply the filter to the EditText
        inputDescription.setFilters(new InputFilter[]{filter});

        pointContainerLayout.setVisibility(View.GONE);

        piuNameTextView.setText(prefManager.getPiuName());
        schemeNameTextView.setText(prefManager.getSchemeName());

        photosRecyclerView = findViewById(R.id.enclosure_photo_recycler_view);
        selectedPhotoAdapter = new SelectedPhotoAdapter(this, selectedPhotosList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photosRecyclerView.setLayoutManager(manager);
        photosRecyclerView.setAdapter(selectedPhotoAdapter);

        textViewStages.setOnClickListener(v -> getStages());

        selectComponentTypeTextView.setOnClickListener(v -> getComponent());

        selectBlockTextView.setOnClickListener(v -> getBlock());

        selectGramTextView.setOnClickListener(v -> getGramP());

        selectVillageTextView.setOnClickListener(v -> getVillage());

        selectApplicationTypeTextView.setOnClickListener(v -> getApplicationType());

        selectPointTextView.setOnClickListener(v -> getPoint());

        tvSelectedOHTMBR.setOnClickListener(v ->getMBROHT());

        tvSelectedPipeNo.setOnClickListener(v ->getPipeList());

        tvSelectedInbetweenLength.setOnClickListener(v ->{
            if(tvSelectedPipeNo.getTag()!=null){
                PipeLineTable pipeInfo = (PipeLineTable)tvSelectedPipeNo.getTag();
                List<String> slots = generateLengthSlots(Double.parseDouble(pipeInfo.getLength()));
                customDialogHelper.showFilterListDialog(
                        this,
                        slots,
                        tvSelectedInbetweenLength.getText().toString(),
                        "Select stretch", (selectedItem, position) -> {
                            tvSelectedInbetweenLength.setText(selectedItem);
                            tvSelectedInbetweenLength.setTag(selectedItem);
                        }
                );
            }
        });

        selectInspectionDateTv.setOnClickListener(v -> {
            if(selectedComponent == null){
                progressHelper.message("Please select component");
                return;
            }
            if(selectedComponent.getComponentType().equals("2")){
                datePicker = new SingleDatePicker(this,false,true);
            }else{
                datePicker = new SingleDatePicker(this,true,true);
            }

            datePicker.show(getSupportFragmentManager(), "Select Date");
        });

        selectEnclosurePhotoTv.setOnClickListener(v -> permissionHelper.requestCameraPermission(this));

        btnAllowLocation.setOnClickListener(v -> {
            if (permissionErrorType.equalsIgnoreCase("deny")) {
                permissionHelper.requestLocationPermission(this);
            } else if (permissionErrorType.equalsIgnoreCase("permanent")) {
                permissionHelper.openSettings();
            } else if (permissionErrorType.equalsIgnoreCase("gps")) {
                permissionHelper.enableGPS();
            }
        });

        btnSubmit.setOnClickListener(v -> submit());

        btnCancel.setOnClickListener(v -> {
            progressHelper.showQuestionDialog("Inspection Request", "Do you want to cancel this request", "Yes", "No", new ProgressHelper.QuestionDialogListener() {
                @Override
                public void onQuestionDialog() {
                    finish();
                }

                @Override
                public void onCancelQuestionDialog() {

                }
            });
        });

        if (datePicker.extendedDate() == 3){
            btnSubmit.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }else{
            btnCancel.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.VISIBLE);
        }

        // TODO: 04-07-2024 : location permission
        permissionHelper.requestLocationPermission(this);

        micIntentCallBack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!progressHelper.isAutoTimeEnabled(this)){
            progressHelper.showAutoTimeDialog(this);
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
                inputDescription.setText(String.format("%s %s", inputDescription.getText().toString().trim(), r != null ? r.get(0) : ""));
            }
        });

    }

    private void getComponent() {
        contractorViewModel.getComponentList().observe(this, componentTypeTables -> {
            customDialogHelper.showComponentTypeDialog(this, componentTypeTables, componentType);
        });
    }

    private void getStages() {
        customDialogHelper.showStageDialog(this, SqLite.instance(this).GET_STAGES_RFI(), selectedStage == null ?"":selectedStage.getKeyString());
    }

    private void getPoint() {
        contractorViewModel.getPoint(selectedComponent.getComponentId()).observe(this, pointTables -> {
            if (pointTables.isEmpty()) {
                progressHelper.message("No point found");
                return;
            }
            customDialogHelper.showPointDialog(this, pointTables, point);
        });
    }

    private void getMBROHT() {
        contractorViewModel.getComponentMBROHT(Integer.parseInt(selectedComponent.getComponentId())).observe(this, list -> {
            customDialogHelper.showFilterListDialog(
                    this,
                    list,
                    tvSelectedOHTMBR.getTag() != null ? tvSelectedOHTMBR.getTag().toString() : "",
                    selectedComponent.getComponentId().equals("12")?"OHT":"MBR", (selectedItem, position) -> {
                        tvSelectedOHTMBR.setText(selectedItem);
                        tvSelectedOHTMBR.setTag(selectedItem);

                        tvSelectedPipeNo.setText("Select Pipe No");
                        tvSelectedPipeNo.setTag(null);

                        pipeInfoLayout.setVisibility(View.GONE);
                    }
            );
        });
    }


    private void getSpoanOHT() {
        String ohtType = radioGroupOHTType.getCheckedRadioButtonId()==R.id.radio_oht_conventional? "1" : "2";
        String dialogTitle = tvSelectedOHTTYPE.getText().toString();
        contractorViewModel.getSopanOhtList(ohtType).observe(this, list -> {
            customDialogHelper.showFilterListDialog(
                    this,
                    list,
                    tvSelectedOHTTYPE.getTag() != null ? (SopanOHT) tvSelectedOHTTYPE.getTag() : null,
                    dialogTitle, (selectedItem, position) -> {
                        tvSelectedOHTTYPE.setText(selectedItem.toString());
                        tvSelectedOHTTYPE.setTag(selectedItem);
                    }
            );
        });
    }


    private void getPipeList() {
        if(tvSelectedOHTMBR.isShown() && tvSelectedOHTMBR.getTag()==null){
            progressHelper.message(selectedComponent.getComponentId().equals("12") ? "Please select OHT" : "Please select MBR");
            return;
        }

        String selectedOHTMBR = tvSelectedOHTMBR.getTag()==null?"":tvSelectedOHTMBR.getTag().toString();
        contractorViewModel.getMBROHTPineLines(Integer.parseInt(selectedComponent.getComponentId()),selectedOHTMBR).observe(this, list -> {
            customDialogHelper.showFilterListDialog(
                    this,
                    list,
                    (PipeLineTable) tvSelectedPipeNo.getTag(),
                    "Select Pipe No", (selectedItem, position) -> {
                        tvSelectedPipeNo.setText(selectedItem.toString());
                        tvSelectedPipeNo.setTag(selectedItem);
                        showPipInfo(selectedItem);
                    }
            );
        });
    }





    private void showPipInfo(PipeLineTable pipeInfo){
        pipeInfoLayout.setVisibility(View.VISIBLE);
        tvSelectedInbetweenLength.setText("Select");
        tvSelectedInbetweenLength.setTag(null);

        ((FormKeyValueText)findViewById(R.id.tv_pipeno)).setValue(pipeInfo.getPipNo());
        ((FormKeyValueText)findViewById(R.id.tv_zone)).setValue(pipeInfo.getZoneNo()==null ? "" : pipeInfo.getZoneNo());
        ((FormKeyValueText)findViewById(R.id.tv_startno)).setValue(pipeInfo.getStartNode());
        ((FormKeyValueText)findViewById(R.id.tv_stop_no)).setValue(pipeInfo.getStopNode());
        ((FormKeyValueText)findViewById(R.id.tv_diameter)).setValue(pipeInfo.getDia());
        ((FormKeyValueText)findViewById(R.id.tv_length)).setValue(pipeInfo.getLength());
        ((FormKeyValueText)findViewById(R.id.tv_material)).setValue(pipeInfo.getMaterial());
        ((FormKeyValueText)findViewById(R.id.tv_thickness)).setValue(pipeInfo.getThickness());

        if(Double.parseDouble(pipeInfo.getLength())>0){
            findViewById(R.id.select_inbetween_length).setVisibility(View.VISIBLE);
            findViewById(R.id.lbl_inbetween_length).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.select_inbetween_length).setVisibility(View.GONE);
            findViewById(R.id.lbl_inbetween_length).setVisibility(View.GONE);
        }
    }




    private void getBlock() {
        contractorViewModel.getBlock().observe(this, locationTables -> {
            customDialogHelper.showBlockDialog(this, locationTables, block);
        });
    }

    private void getGramP() {

        if (selectedBlock == null) {
            progressHelper.message("Please select block");
            return;
        }

        contractorViewModel.getGramPList(selectedBlock.getBlockId()).observe(this, gramTables -> {
            customDialogHelper.showGramDialog(this, gramTables, gram);
        });
    }

    private void getVillage() {
        if (selectedGramP == null) {
            progressHelper.message("Please select gram panchayat");
            return;
        }
        contractorViewModel.getVillage(selectedGramP.getGramId()).observe(this, villageTables -> {
            customDialogHelper.showVillageDialog(this, villageTables, village);
        });
    }

    private void getApplicationType() {
        contractorViewModel.getApplicationType().observe(this, applicationTypeTables -> {
            customDialogHelper.showApplicationTypeDialog(this, applicationTypeTables, appType);
        });
    }

    @Override
    public void onStagesSelected(SpinnerItem stage) {
        this.selectedStage = stage;
        textViewStages.setText(stage.getValue());
    }

    @Override
    public void onComponentSelected(ComponentTypeTable table) {
        selectedComponent = table;
        componentType = table.getComponentName();
        selectComponentTypeTextView.setText(table.getComponentName());
        selectedPoint = null;
        inspectionDate = null;
        selectInspectionDateTv.setText("Select Date");

        selectPointTextView.setText("Select Point");
        if (table.getComponentType().equalsIgnoreCase("1")) {
            pointContainerLayout.setVisibility(View.VISIBLE);

            textViewStagesHead.setVisibility(View.GONE);
            textViewStages.setVisibility(View.GONE);
        } else {
            pointContainerLayout.setVisibility(View.GONE);
            textViewStagesHead.setVisibility(View.VISIBLE);
            textViewStages.setVisibility(View.VISIBLE);
        }

        showPipelineLayoutForComponent();
        showSopanOHTForComponent();
    }

    /*show pipeline layout based on selected component */
    private void showPipelineLayoutForComponent(){
        tvSelectedOHTMBR.setTag(null);
        if(contractorViewModel.pipeLineComponentTypeId.contains(selectedComponent.getComponentId())){
            pipelineLayout.setVisibility(View.VISIBLE);
            pipeInfoLayout.setVisibility(View.GONE);
            tvSelectedPipeNo.setText("Select Pipe No");
            tvSelectedPipeNo.setTag(null);

            switch (Integer.parseInt(selectedComponent.getComponentId())){
                case 9: //CWGM -MBR
                    tvLblOHTMBR.setVisibility(View.VISIBLE);
                    tvSelectedOHTMBR.setVisibility(View.VISIBLE);
                    tvLblOHTMBR.setText(getString(R.string.mbr));
                    tvSelectedOHTMBR.setText("Select MBR");
                    break;
                case 10: //CWPM
                    tvLblOHTMBR.setVisibility(View.GONE);
                    tvSelectedOHTMBR.setVisibility(View.GONE);
                    break;
                case 11: //RWPM
                    tvLblOHTMBR.setVisibility(View.GONE);
                    tvSelectedOHTMBR.setVisibility(View.GONE);
                    break;
                case 12: //Distribution Pipe - OHT
                    tvLblOHTMBR.setVisibility(View.VISIBLE);
                    tvSelectedOHTMBR.setVisibility(View.VISIBLE);

                    tvLblOHTMBR.setText(getString(R.string.oht_name));
                    tvSelectedOHTMBR.setText("Select OHT");
            }
        }else{
            pipelineLayout.setVisibility(View.GONE);
        }
    }


    private void showSopanOHTForComponent(){
        String hint = radioGroupOHTType.getCheckedRadioButtonId()==R.id.radio_oht_conventional? "Select Conventional OHT" : "Select Pre-Cast OHT";
        tvSelectedOHTTYPE.setText(hint);
        tvSelectedOHTTYPE.setTag(null);
        if(Integer.parseInt(selectedComponent.getComponentId())==2){ //oht
            sopanOHTLayout.setVisibility(View.VISIBLE);

        }else{
            sopanOHTLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBlockSelected(BlockTable table) {
        selectedBlock = table;
        block = selectedBlock.getBlockName();
        selectBlockTextView.setText(block);

        selectedGramP = null;
        selectGramTextView.setText("Select Gram Panchayat");
        selectedVillage = null;
        selectVillageTextView.setText("Select Village");
    }

    @Override
    public void onGramSelected(GramTable table) {
        selectedGramP = table;
        gram = selectedGramP.getGramName();
        selectGramTextView.setText(table.getGramName());
        selectedVillage = null;
        selectVillageTextView.setText("Select Village");
    }

    @Override
    public void onVillageSelected(VillageTable table) {
        selectedVillage = table;
        village = selectedVillage.getVillageName();
        selectVillageTextView.setText(village);
    }

    @Override
    public void onApplicationTypeSelected(ApplicationTypeTable app) {
        selectedApplicationType = app;
        appType = app.getAppTypeName();
        selectApplicationTypeTextView.setText(appType);
    }

    @Override
    public void onPointSelected(PointTable pointTable) {
        selectedPoint = pointTable;
        point = selectedPoint.getName();
        selectPointTextView.setText(point);
    }

    @Override
    public void onPipeNoSelected(String pipeNo) {
        pipeInfoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSelected(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
        selectInspectionDateTv.setText(selectedDate);
        inspectionDate = selectedDate;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageHelper.CAPTURE_IMAGE_CODE
                && resultCode == RESULT_OK) {

            File file = new File(imageHelper.moveImageToDestination(imageHelper.photoFilePath, latitude, longitude));
            imageHelper.compressImage(file.getPath(), file, this);

           /* Bitmap rotatedBitmap = BitmapFactory.decodeFile(imageHelper.photoFilePath);
            Bitmap bitmap = imageHelper.rotateBitmapIfNeeded(rotatedBitmap,imageHelper.photoFilePath);
            String locationText = "Latitude: " + latitude + ", Longitude: " + longitude + " | Date & Time " + new Date();
            Bitmap imageWithLocation = imageHelper.addTextToImage(bitmap, locationText);
            File updatedFile = imageHelper.bitmapToFile(imageWithLocation);

            File file = new File(imageHelper.moveImageToDestination(updatedFile.getPath()));
            imageHelper.compressImage(file.getPath(), file, this);*/

        }
    }

    @Override
    public void onImageCompressCompleted(String path, boolean isError) {
        File file = new File(path);
        if (!selectedPhotosList.contains(path)) {
            selectedPhotosList.add(path);
            selectedPhotoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPhotoClicked(int position, String photos) {
        selectedPhotosList.remove(photos);
        selectedPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLocationPermissionAllow(PermissionHelper.LocationPermissionEnum permission) {
        if (permission == PermissionHelper.LocationPermissionEnum.ALLOW) {
            locationErrorTextView.setVisibility(View.GONE);
            btnAllowLocation.setVisibility(View.GONE);
            googleMapContainer.setVisibility(View.VISIBLE);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            getCurrentLocation();
        } else if (permission == PermissionHelper.LocationPermissionEnum.DENY) {
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
        }
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
                        mapFragment.getMapAsync(RequestInspectionActivity.this);

                        locationAddressTextView.setText(permissionHelper.getAddressFromLatLng(latitude, longitude));
                    } else {
                        locationProgressBar.setVisibility(View.GONE);
                        progressHelper.dismissProgress();
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

    private void submit() {

        String location = inputLocation.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();

        if (selectedComponent == null) {
            progressHelper.message("Please select component type");
            return;
        }

        if(tvSelectedOHTMBR.isShown() && tvSelectedOHTMBR.getTag()==null){
            progressHelper.message(selectedComponent.getComponentId().equals("12") ? "Please select OHT" : "Please select MBR");
            return;
        }

        if(tvSelectedOHTTYPE.isShown() && tvSelectedOHTTYPE.getTag()==null){
            progressHelper.message(radioGroupOHTType.getCheckedRadioButtonId()==R.id.radio_oht_conventional? "Please select Conventional OHT" : "Please select Pre-Cast OHT");
            return;
        }

        if(tvSelectedPipeNo.isShown() && tvSelectedPipeNo.getTag()==null){
            progressHelper.message("Please select pipe no");
            return;
        }

        if(tvSelectedInbetweenLength.isShown() && tvSelectedInbetweenLength.getTag()==null){
            progressHelper.message("Please select stretch no");
            return;
        }

        String stageId = "0",stageName = "0";
        if (selectedComponent.getComponentType().equals("2")) {
            if (selectedStage == null) {
                progressHelper.message("Please select stage");
                return;
            }
            stageId = selectedStage.getKeyString();
            stageName = selectedStage.getValue();
        }

        if (selectedBlock == null) {
            progressHelper.message("Please select block");
            return;
        }

        if (selectedGramP == null) {
            progressHelper.message("Please select gram");
            return;
        }

        if (selectedVillage == null) {
            progressHelper.message("Please select village");
            return;
        }

        if (selectedApplicationType == null) {
            progressHelper.message("Please select application type");
            return;
        }

        if (location.isEmpty()) {
            progressHelper.message("Please enter location");
            return;
        }

        if (description.isEmpty()) {
            progressHelper.message("Please enter description");
            return;
        }

        if (inspectionDate == null) {
            progressHelper.message("Please select inspection date");
            return;
        }

        if (selectedPhotosList.isEmpty()) {
            progressHelper.message("Please capture at least 1 photo");
            return;
        }

        if (selectedPhotosList.size() > 10) {
            progressHelper.message("Photo limit exceed. Maximum 10 photos allowed");
            return;
        }

        if (latitude == 0) {
            progressHelper.message("Location not found. Please check permission");
            permissionHelper.requestLocationPermission(this);
            return;
        }

        String insertDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        InspectionRequestTable requestTable = new InspectionRequestTable();

        requestTable.setPiuId(prefManager.getPiuId());
        requestTable.setPiuName(prefManager.getPiuName());
        requestTable.setSchemeId(prefManager.getSchemeId());
        requestTable.setSchemeName(prefManager.getSchemeName());
        requestTable.setComponentId(selectedComponent == null ? "0" : selectedComponent.getComponentId());
        requestTable.setComponentName(selectedComponent == null ? "0" : selectedComponent.getComponentName());
        requestTable.setPointId(selectedPoint == null ? "0" : "" + selectedPoint.getGid());
        requestTable.setPointName(selectedPoint == null ? "0" : selectedPoint.getName());
        requestTable.setBlockId(selectedBlock == null ? "0" : selectedBlock.getBlockId());
        requestTable.setBlockName(selectedBlock == null ? "-" : selectedBlock.getBlockName());
        requestTable.setGramName(selectedGramP == null ? "-" : selectedGramP.getGramName());
        requestTable.setGramId(selectedGramP == null ? "0" : selectedGramP.getGramId());
        requestTable.setVillageId(selectedVillage == null ? "0" : selectedVillage.getVillageId());
        requestTable.setVillageName(selectedVillage == null ? "-" : selectedVillage.getVillageName());
        requestTable.setApplicationId(selectedApplicationType == null ? "0" : selectedApplicationType.getAppTypeId());
        requestTable.setApplicationName(selectedApplicationType == null ? "0" : selectedApplicationType.getAppTypeName());
        requestTable.setLocation(location);
        requestTable.setDescription(description);
        requestTable.setInspectionDate(inspectionDate);

        requestTable.setImages("");
        requestTable.setLatitude(String.valueOf(latitude));
        requestTable.setLongitude(String.valueOf(longitude));
        requestTable.setGeoAddress(permissionHelper.getAddressFromLatLng(latitude, longitude));

        requestTable.setRfiId("-1");
        requestTable.setInsertDate(insertDate);
        requestTable.setStatusName("Not sync");

        requestTable.setAllotmentType("");

        requestTable.setContractorId(login.getIdS());//prefManager.getUserId());
        requestTable.setUploadStatus(0);
        requestTable.setStatus("Yet to upload");

        requestTable.setSavedByLoginId(login.getIdS());//prefManager.getUserId());
        requestTable.setSavedBy(login.getRoleLC());

        requestTable.setStageId(stageId);
        requestTable.setStageName(stageName);

        //for pipeline detail
        requestTable.setPipeNo(tvSelectedPipeNo.isShown()?tvSelectedPipeNo.getText().toString():"0");
        requestTable.setLengthSlot(tvSelectedInbetweenLength.isShown()?tvSelectedInbetweenLength.getText().toString():"0");
        requestTable.setMbrOhtSurveyId(tvSelectedOHTMBR.isShown()?tvSelectedOHTMBR.getText().toString():"0");

        //for sopans oht
        if(tvSelectedOHTTYPE.isShown()){
            SopanOHT sopanOHT = (SopanOHT) tvSelectedOHTTYPE.getTag();
            requestTable.setMbrOhtSurveyId(sopanOHT.getSurveyUid());
            requestTable.setTypeOfSopanOHT(sopanOHT.getOhtType());
        }

        contractorViewModel.insertInspectionRequest(requestTable, selectedPhotosList);
        progressHelper.showSuccessDialog("Added successfully", "Inspection Request",
                "back", "Done", new ProgressHelper.ShowDialogListener() {
            @Override
            public void onShowDialogButtonClicked(String type) {
                finish();
            }
        });
    }



    public List<String> generateLengthSlots(Double upto) {
        List<String> slots = new ArrayList<>();
        double start = 1.0;
        while (start <= upto) {
            double end = Math.min(start + 99, upto);  // 100 numbers per slot
            slots.add(formatDouble(start) + "-" + formatDouble(end));
            start = end + 1.0;
        }
        return slots;
    }

    private String formatDouble(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((int) value);  // remove .0 if whole number
        } else {
            return String.valueOf(value);
        }
    }
}