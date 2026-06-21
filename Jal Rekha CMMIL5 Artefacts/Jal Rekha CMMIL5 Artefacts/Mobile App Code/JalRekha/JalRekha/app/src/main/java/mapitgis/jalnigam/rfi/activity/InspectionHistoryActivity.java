package mapitgis.jalnigam.rfi.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.rfi.adapter.InspectionHistoryAdapter;
import mapitgis.jalnigam.rfi.helper.PermissionHelper;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.helper.SingleDatePicker;
import mapitgis.jalnigam.rfi.model.PostInspectionRequest;
import mapitgis.jalnigam.rfi.model.SimpleResponse;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import mapitgis.jalnigam.room.table.AnushravanStatusTable;
import mapitgis.jalnigam.room.table.InspectionRequestTable;
import mapitgis.jalnigam.room.table.MediaTable;
import mapitgis.jalnigam.rfi.viewmodel.ContractorViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InspectionHistoryActivity extends AppCompatActivity implements
        InspectionHistoryAdapter.InspectionHistoryListener, SingleDatePicker.SingleDateListener {

    private ImageView backImageView;
    private TextView titleTextView;
    private TextView noDataTextView;
    private RecyclerView recyclerView;
    private List<InspectionRequestTable> inspectionHistoryList;
    private InspectionHistoryAdapter historyAdapter;
    private ContractorViewModel contractorViewModel;
    private ProgressBar progressBar;
    private EditText inputSearch;
    private ImageView dateFilterImageView, statusFilterImageView;
    private ProgressHelper progressHelper;
    private PrefManager prefManager;
    private ApiInterface apiInterface;
    private PermissionHelper permissionHelper;
    private SingleDatePicker singleDatePicker;
    private Login login;

    /***
     * revisit status => 6, this pass from WorkMonitoringActivity.
     */
    private String revisitStatus;
    private InspectionRequestTable selectedRFIRequest;

    private List<AnushravanStatusTable> anushravanStatusTableList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inspection_history);

        login = SqLite.instance(this).getLogin();

        String title = getIntent().getStringExtra("title");
        revisitStatus = getIntent().getStringExtra("status");

        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        contractorViewModel = new ViewModelProvider(this).get(ContractorViewModel.class);
        progressHelper = new ProgressHelper(this);
        prefManager = new PrefManager(this);
        permissionHelper = new PermissionHelper(this);
        singleDatePicker = new SingleDatePicker(this, true, true);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText(title);
        backImageView.setOnClickListener(v -> finish());

        progressBar = findViewById(R.id.inspection_history_progress_bar);
        inputSearch = findViewById(R.id.input_search_inspection_history);
        dateFilterImageView = findViewById(R.id.inspection_history_date_filter_iv);
        statusFilterImageView = findViewById(R.id.inspection_history_status_filter_iv);

        noDataTextView = findViewById(R.id.inspection_history_no_data_text_view);

        recyclerView = findViewById(R.id.inspection_history_recycler_view);
        inspectionHistoryList = new ArrayList<>();
        historyAdapter = new InspectionHistoryAdapter(this, inspectionHistoryList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(historyAdapter);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                historyAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dateFilterImageView.setOnClickListener(v -> datePicker());

        statusFilterImageView.setOnClickListener(v -> showStatusDialog());

        // status filter
        getAnushravanStatus();

        getInspectionHistory();
    }

    private void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                InspectionHistoryActivity.this,
                (view1, selectedYear, selectedMonth, selectedDay) -> {
                    // Display the selected date in a TextView
                    String selected = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    inputSearch.setText(SingleDatePicker.formatDateDDMMMYYYY(selected));

                },
                year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void getAnushravanStatus() {
        anushravanStatusTableList.clear();
        contractorViewModel.getAnushravanStatus().observe(this, anushravanStatusTables -> {
            anushravanStatusTableList.addAll(anushravanStatusTables);
        });
    }

    private void showStatusDialog() {
        ArrayAdapter<AnushravanStatusTable> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, anushravanStatusTableList);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Status");

        builder.setAdapter(adapter, (dialog, which) -> {
            AnushravanStatusTable selectedStatus = anushravanStatusTableList.get(which);
            inputSearch.setText(selectedStatus.getStatusName());

        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getInspectionHistory() {

        progressBar.setVisibility(View.VISIBLE);
//        contractorViewModel.getInspectionRequest(prefManager.getUserType(), prefManager.getUserId(), revisitStatus).observe(this, inspectionRequestTables -> {
        contractorViewModel.getInspectionRequest(login.getRoleLC(), login.getIdS(), revisitStatus).observe(this, inspectionRequestTables -> {
            inspectionHistoryList.clear();

//            Collections.sort(inspectionRequestTables, (r1, r2) -> r1.getRfiId().compareTo(r2.getRfiId()));

            inspectionHistoryList.addAll(inspectionRequestTables);
            historyAdapter.notifyDataSetChanged();

            if (inspectionHistoryList.isEmpty()) {
                noDataTextView.setVisibility(View.VISIBLE);
            } else {
                noDataTextView.setVisibility(View.GONE);
            }

            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onRevisitRequestClicked(InspectionRequestTable history, int position) {
        if (history.getUploadStatus() == 0) {
            delete(history);
        } else {
            startActivity(new Intent(InspectionHistoryActivity.this, RevisitRequestActivity.class)
                    .putExtra("detail", history));
        }

    }

    @Override
    public void onViewDetailClicked(InspectionRequestTable history, int position) {
        if (history.getUploadStatus() == 0) {
            permissionHelper.checkNetworkConnection(isAvailable -> {
                if (isAvailable) {
                    upload(history);
                } else {
                    progressHelper.message("No internet available. Check your internet connection");
                }
            });
        } else {
            startActivity(new Intent(InspectionHistoryActivity.this, InspectionHistoryDetailActivity.class)
                    .putExtra("detail", history));
        }
    }

    @Override
    public void onViewLogsClicked(InspectionRequestTable history, int position) {
        startActivity(new Intent(InspectionHistoryActivity.this, ContractorLogsActivity.class)
                .putExtra("detail", history));
    }

    @Override
    public void onRescheduleClicked(InspectionRequestTable history, int position) {
        selectedRFIRequest = history;
        // singleDatePicker.show(getSupportFragmentManager(),"Select Reschedule date");
        rescheduleDatePicker(history.getInspectionDate());
    }

    private void rescheduleDatePicker(String selectedDate) {
        // Parse the selected date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        try {
            Date date = dateFormat.parse(selectedDate);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, 1); // Add one day

                // Open the DatePickerDialog with the min date set
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        this,
                        (view, year, month, dayOfMonth) -> {
                            String pickedDate = dayOfMonth + "-" + (month + 1) + "-" + year;

                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, month);
                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String rescheduleDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
                            String readableDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(c.getTime());

                            String message = "Do you want to reschedule RFI request no. " + selectedRFIRequest.getRfiId() + " from " + selectedRFIRequest.getInspectionDate() + " to " + readableDate;

                            progressHelper.showQuestionDialog("Reschedule Request", message, "Yes", "No",
                                    new ProgressHelper.QuestionDialogListener() {
                                        @Override
                                        public void onQuestionDialog() {
                                            rescheduleRFIRequest(selectedRFIRequest, rescheduleDate);
                                        }

                                        @Override
                                        public void onCancelQuestionDialog() {

                                        }
                                    });
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void delete(InspectionRequestTable data) {
        progressHelper.showQuestionDialog("Delete Request", "Do you want to delete this record", "Yes", "No", new ProgressHelper.QuestionDialogListener() {
            @Override
            public void onQuestionDialog() {
                contractorViewModel.deleteOrUpdateInspectionById(data.getId(), true);
                progressHelper.message("Deleted");
            }

            @Override
            public void onCancelQuestionDialog() {

            }
        });
    }

    /**
     * In this method we create data for upload RFI to server
     */
    private void upload(InspectionRequestTable history) {
        List<String> imageList = new ArrayList<>();

        progressHelper.showQuestionDialog("Upload Request", "Do you want to upload this record", "Yes", "No", new ProgressHelper.QuestionDialogListener() {
            @Override
            public void onQuestionDialog() {
                try {
                    PostInspectionRequest inspectionRequest = new PostInspectionRequest();

                    contractorViewModel.getMediaImage(history.getId()).observe(InspectionHistoryActivity.this, mediaTables -> {

                        for (MediaTable media : mediaTables) {
                            String image = Base64.encodeToString(media.getImage(), Base64.DEFAULT);
                            imageList.add(image);
                        }

                        inspectionRequest.seteToken(login.getToken());//prefManager.getToken1());
                        inspectionRequest.setPuiId(history.getPiuId());
                        inspectionRequest.setSchemeId(history.getSchemeId());
                        inspectionRequest.setComponentId(history.getComponentId());
                        inspectionRequest.setPointName(history.getPointName());
                        inspectionRequest.setPointId(history.getPointId());
                        inspectionRequest.setVillageId(history.getVillageId());
                        inspectionRequest.setApplicationId(history.getApplicationId());
                        //    inspectionRequest.setContractorId(history.getContractorId());
                        inspectionRequest.setLatitude(history.getLatitude());
                        inspectionRequest.setLongitude(history.getLongitude());
                        inspectionRequest.setGeoAddress(history.getGeoAddress());
                        inspectionRequest.setAddress(history.getLocation());
                        inspectionRequest.setDescription(history.getDescription());
                        inspectionRequest.setInspectionDate(history.getInspectionDate());
                        inspectionRequest.setImageList(imageList);
                        inspectionRequest.setStageId(history.getStageId());

                        inspectionRequest.setPipeNo(history.getPipeNo());
                        inspectionRequest.setLengthSlot(history.getLengthSlot());
                        inspectionRequest.setSurveyUID(history.getMbrOhtSurveyId());
                        if(!history.getTypeOfSopanOHT().equals("")){
                            inspectionRequest.setTypeOfSopanOHT(Integer.parseInt(history.getTypeOfSopanOHT()));
                        }
                    });

                    new Handler().postDelayed(() -> {
                        submitRequest(inspectionRequest, history.getId());
                    }, 300);
                } catch (Exception e) {
                    progressHelper.message("Something not right.");
                }


            }

            @Override
            public void onCancelQuestionDialog() {

            }
        });
    }

    private void submitRequest(PostInspectionRequest request, int reqId) {

        Call<SimpleResponse> call = apiInterface.postInspectionRequest(request);

        progressHelper.showProgress("Please wait...");

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressHelper.dismissProgress();
                if (response.body().isSuccess()) {
                    progressHelper.message("Uploaded");
                    String message = "Your RFI generated successfully";
                    progressHelper.showSuccessDialog(message, "RFI", "back", "Ok", type -> {
                    });
                    contractorViewModel.deleteOrUpdateInspectionById(reqId, false);
                } else {

                    if (response.body().getMessage().toLowerCase().contains("session expired")) {
                        progressHelper.showSuccessDialog("Your session is expired. Please login again", "Session Expired", "back", "OK", new ProgressHelper.ShowDialogListener() {
                            @Override
                            public void onShowDialogButtonClicked(String type) {
                                prefManager.logout();

                                Utility.goFirst(InspectionHistoryActivity.this,true);
//                                startActivity(new Intent(InspectionHistoryActivity.this, TypeActivity.class)
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
                progressHelper.message("Something went wrong");
                progressHelper.dismissProgress();
            }
        });

    }

    /**
     * Re-schedule RFI Request (only new RFI generated not for Revisit Condition)
     *
     * @param data
     * @param date
     */
    private void rescheduleRFIRequest(InspectionRequestTable data, String date) {
        Map<String, String> body = new HashMap<>();
        body.put("rfi_id", data.getRfiId());
        body.put("inspection_date", date);
        body.put(Utility.E_TOKEN, login.getToken());//prefManager.getToken1());

        progressHelper.showProgress("Please wait...");
        Call<SimpleResponse> call = apiInterface.rescheduleRFI(body);

        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                progressHelper.dismissProgress();

                if (response.body().isSuccess()) {
                    progressHelper.message("Reschedule successful");
                    contractorViewModel.deleteOrUpdateInspectionById(selectedRFIRequest.getId(), true);
                } else {
                    progressHelper.message("Failed! try again!");
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
    public void onDateSelected(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c.getTime());
        String readableDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(c.getTime());

        String message = "Do you want to reschedule RFI request no. " + selectedRFIRequest.getRfiId() + " from " + selectedRFIRequest.getInspectionDate() + " to " + readableDate;

        progressHelper.showQuestionDialog("Reschedule Request", message, "Yes", "No",
                new ProgressHelper.QuestionDialogListener() {
                    @Override
                    public void onQuestionDialog() {
                        rescheduleRFIRequest(selectedRFIRequest, selectedDate);
                    }

                    @Override
                    public void onCancelQuestionDialog() {

                    }
                });
    }
}