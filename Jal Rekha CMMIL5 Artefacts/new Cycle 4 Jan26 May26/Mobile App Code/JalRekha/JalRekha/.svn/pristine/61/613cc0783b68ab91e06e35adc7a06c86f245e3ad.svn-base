package mapitgis.jalnigam.dhara;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityDharaDailyWaterSupplyBinding;
import mapitgis.jalnigam.rfi.adapter.dhara.DharaDailyWaterSupplyAdapter;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.helper.SingleDatePicker;
import mapitgis.jalnigam.rfi.model.dhara.DailyWaterSupply;
import mapitgis.jalnigam.rfi.model.dhara.DharaESRHistory;
import mapitgis.jalnigam.rfi.model.dhara.DharaIntakeHistory;
import mapitgis.jalnigam.rfi.model.dhara.DharaWTPHistory;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DharaDailyWaterSupplyActivity extends AppCompatActivity implements DharaDailyWaterSupplyAdapter.DailyWaterSupplyListener {

    private ImageView backImageView;
    private TextView titleTextView;
    private ActivityDharaDailyWaterSupplyBinding binding;
    private List<DailyWaterSupply.DailyWaterSupplyData> dailyWaterSupplyList;
    private DharaDailyWaterSupplyAdapter adapter;
    private ApiInterface apiInterface;
    private ProgressHelper progressHelper;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String gotoScreen = "";
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivityDharaDailyWaterSupplyBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        login = SqLite.instance(this).getLogin();

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        progressHelper = new ProgressHelper(this);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Daily water supply list");
        backImageView.setOnClickListener(v -> finish());

        dailyWaterSupplyList = new ArrayList<>();
        adapter = new DharaDailyWaterSupplyAdapter(this, dailyWaterSupplyList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.dailyWaterSupplyRecyclerView.setLayoutManager(manager);
        binding.dailyWaterSupplyRecyclerView.setAdapter(adapter);
        getDailySupply();

        onActivityBack();

        binding.inputSearchDharaSupplyHistory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getDailySupply() {

        binding.dharaWaterSupplyProgressBar.setVisibility(View.VISIBLE);

        Map<String, String> body = new HashMap<>();
//        body.put("user_id", prefManager.getSqcId());
        body.put("user_id", String.valueOf(login.getId()));
        Call<DailyWaterSupply> call = apiInterface.getDailyWaterSupply(body);

        call.enqueue(new Callback<DailyWaterSupply>() {
            @Override
            public void onResponse(Call<DailyWaterSupply> call, Response<DailyWaterSupply> response) {
                binding.dharaWaterSupplyProgressBar.setVisibility(View.GONE);
                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {
                        dailyWaterSupplyList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.noDharaWaterSupplyTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.noDharaWaterSupplyTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DailyWaterSupply> call, Throwable t) {
                binding.dharaWaterSupplyProgressBar.setVisibility(View.GONE);
                binding.noDharaWaterSupplyTv.setVisibility(View.VISIBLE);
                binding.noDharaWaterSupplyTv.setText(ProgressHelper.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void onDetailClicked(DailyWaterSupply.DailyWaterSupplyData supply) {
       /* startActivity(new Intent(DharaDailyWaterSupplyActivity.this, DharaSupplyDetailsActivity.class)
                .putExtra("data", supply));*/

        gotoScreen = "detail";

        if (supply.getAllotmentTypeName().equalsIgnoreCase("intake well")) {
            checkIntakeForm(supply);
        } else if (supply.getAllotmentTypeName().equalsIgnoreCase("esr")) {
            checkESRForm(supply);
        } else if (supply.getAllotmentTypeName().equalsIgnoreCase("wtp")) {
            checkWTPForm(supply);
        }
    }

    @Override
    public void oStartClicked(DailyWaterSupply.DailyWaterSupplyData supply) {
      /*  activityResultLauncher.launch(new Intent(DharaDailyWaterSupplyActivity.this, DharaWaterFormActivity.class)
                .putExtra("data", supply));*/

        gotoScreen = "form";

        if (supply.getAllotmentTypeName().equalsIgnoreCase("intake well")) {
            checkIntakeForm(supply);
        } else if (supply.getAllotmentTypeName().equalsIgnoreCase("esr")) {
            checkESRForm(supply);
        } else if (supply.getAllotmentTypeName().equalsIgnoreCase("wtp")) {
            checkWTPForm(supply);
        }

    }

    private void onActivityBack() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        dailyWaterSupplyList.clear();
                        adapter.notifyDataSetChanged();
                        getDailySupply();
                    }
                }
        );
    }

    /**
     * here we check ESR/WTP/Intake already filled form
      * @param supply
     */
    private void checkIntakeForm(DailyWaterSupply.DailyWaterSupplyData supply) {
        Map<String, Object> body = new HashMap<>();
        body.put("intake_id", supply.getIntakeId());
        body.put(Utility.E_TOKEN, "");
        body.put("survey_date", SingleDatePicker.getCurrentDate());
        body.put("user_id", String.valueOf(login.getId()));
//        body.put("user_id", prefManager.getSqcId());

        progressHelper.showProgress("Please wait...");
        Call<DharaIntakeHistory> call = apiInterface.getDharaIntakeHistory(body);

        call.enqueue(new Callback<DharaIntakeHistory>() {
            @Override
            public void onResponse(Call<DharaIntakeHistory> call, Response<DharaIntakeHistory> response) {

                progressHelper.dismissProgress();

                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {
                        List<DharaIntakeHistory.DharaIntakeHistoryData> list = response.body().getData();
                        DharaIntakeHistory.DharaIntakeHistoryData data = list.get(list.size() - 1);

                        supply.setStartReading(data.getStartReading());
                        supply.setEndReading(data.getEndReading());
                        supply.setTotalReading(data.getTotalRawWaterDrawn());
                        supply.setStartPhotoPath(data.getPhotoPathStartReading());
                        supply.setEndPhotoPath(data.getPhotoPathEndReading());
                        supply.setRemark(data.getRemarks());
                        supply.setUpdate(true);

                        gotoDetail(supply);
                    } else {
                        gotoDetail(supply);
                    }
                } else {
                    gotoDetail(supply);
                }
            }

            @Override
            public void onFailure(Call<DharaIntakeHistory> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);
            }
        });
    }

    private void checkWTPForm(DailyWaterSupply.DailyWaterSupplyData supply) {
        Map<String, Object> body = new HashMap<>();
        body.put("wtp_id", supply.getWtpId());
        body.put(Utility.E_TOKEN, "");
        body.put("survey_date", SingleDatePicker.getCurrentDate());
        body.put("user_id", String.valueOf(login.getId()));
//        body.put("user_id", prefManager.getSqcId());

        Call<DharaWTPHistory> call = apiInterface.getDharaWTPHistory(body);

        call.enqueue(new Callback<DharaWTPHistory>() {
            @Override
            public void onResponse(Call<DharaWTPHistory> call, Response<DharaWTPHistory> response) {

                progressHelper.dismissProgress();
                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {
                        List<DharaWTPHistory.DharaWTPHistoryData> list = response.body().getData();
                        DharaWTPHistory.DharaWTPHistoryData data = list.get(list.size() - 1);

                        supply.setStartReading(data.getStartReading());
                        supply.setEndReading(data.getEndReading());
                        supply.setTotalReading(data.getTotalWaterSupplied());
                        supply.setStartPhotoPath(data.getPhotoPathStartReading());
                        supply.setEndPhotoPath(data.getPhotoPathEndReading());
                        supply.setRemark(data.getRemarks());
                        supply.setUpdate(true);

                        gotoDetail(supply);
                    } else {
                        gotoDetail(supply);
                    }
                } else {
                    gotoDetail(supply);
                }
            }

            @Override
            public void onFailure(Call<DharaWTPHistory> call, Throwable t) {
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);
                progressHelper.dismissProgress();

            }
        });
    }

    private void checkESRForm(DailyWaterSupply.DailyWaterSupplyData supply) {

        Map<String, String> body = new HashMap<>();
        body.put("esr_name", supply.getEsrName());
        body.put(Utility.E_TOKEN, "");
        body.put("village_name", "");
        body.put("survey_date", SingleDatePicker.getCurrentDate());

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

                        gotoDetail(supply);

                    } else {
                        gotoDetail(supply);
                    }
                } else {
                    gotoDetail(supply);
                }
            }

            @Override
            public void onFailure(Call<DharaESRHistory> call, Throwable t) {
                progressHelper.dismissProgress();
                progressHelper.message(ProgressHelper.ERROR_MESSAGE);

            }
        });
    }

    private void gotoDetail(DailyWaterSupply.DailyWaterSupplyData supply) {

        if (gotoScreen.equalsIgnoreCase("form")) {
            activityResultLauncher.launch(new Intent(DharaDailyWaterSupplyActivity.this, DharaWaterFormActivity.class)
                    .putExtra("data", supply));
        } else {
            startActivity(new Intent(DharaDailyWaterSupplyActivity.this, DharaSupplyDetailsActivity.class)
                    .putExtra("data", supply));
        }

    }
}