package mapitgis.jalnigam.rfi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.rfi.adapter.ContractorDISurveyAdapter;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.ContractorDISurvey;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import mapitgis.jalnigam.rfi.viewmodel.ContractorViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractorDISurveyActivity extends AppCompatActivity implements ContractorDISurveyAdapter.ContractorDISurveyListener {

    private ImageView backImageView;
    private TextView titleTextView;

    private ProgressHelper progressHelper;
    private PrefManager prefManager;
    private ContractorViewModel contractorViewModel;
    private ApiInterface apiInterface;

    private RecyclerView recyclerView;
    private List<ContractorDISurvey.ContractorDISurveyData> contractorDISurveyList;
    private ContractorDISurveyAdapter adapter;
    private ProgressBar progressBar;
    private TextView noDataTextView;

    private ActivityResultLauncher<Intent> activityResultLaunch;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contractor_disurvey);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        login = SqLite.instance(this).getLogin();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        contractorViewModel = new ViewModelProvider(this).get(ContractorViewModel.class);
        progressHelper = new ProgressHelper(this);
        prefManager = new PrefManager(this);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Complaint List");
        backImageView.setOnClickListener(v -> finish());

        progressBar = findViewById(R.id.contractor_di_survey_progress_bar);
        noDataTextView = findViewById(R.id.no_contractor_di_data_tv);

        recyclerView = findViewById(R.id.contractor_di_survey_recycler_view);
        contractorDISurveyList = new ArrayList<>();
        adapter = new ContractorDISurveyAdapter(this, contractorDISurveyList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        if (login.getRoleId() == 9) {//RFI Contractor
//        if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
            getSurveyForContractor();
        } else {
            getSurveyForFE();
        }


        onIntentCallback();
    }

    // TODO: 06-08-2024 : getting survey history for contractor
    private void getSurveyForContractor() {

        progressBar.setVisibility(View.VISIBLE);
        noDataTextView.setVisibility(View.GONE);
        contractorDISurveyList.clear();
        adapter.notifyDataSetChanged();

        Map<String, String> body = new HashMap<>();
        body.put("etokens", login.getToken());//prefManager.getToken1());
        Call<ContractorDISurvey> call = apiInterface.getDIRequestContractor(body);

        call.enqueue(new Callback<ContractorDISurvey>() {
            @Override
            public void onResponse(Call<ContractorDISurvey> call, Response<ContractorDISurvey> response) {
                progressBar.setVisibility(View.GONE);

                if (response.body().isSuccess()) {
                    contractorDISurveyList.addAll(response.body().getSurveyDataList());
                    adapter.notifyDataSetChanged();
                } else {
                    noDataTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ContractorDISurvey> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                noDataTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    // TODO: 06-08-2024 : getting survey history for FE

    private void getSurveyForFE() {
        progressBar.setVisibility(View.VISIBLE);
        noDataTextView.setVisibility(View.GONE);
        contractorDISurveyList.clear();
        adapter.notifyDataSetChanged();

        Map<String, String> body = new HashMap<>();
        body.put("etokens", login.getToken());//prefManager.getToken1());
        Call<ContractorDISurvey> call = apiInterface.getDIRequestFE(body);

        call.enqueue(new Callback<ContractorDISurvey>() {
            @Override
            public void onResponse(Call<ContractorDISurvey> call, Response<ContractorDISurvey> response) {
                progressBar.setVisibility(View.GONE);

                if (response.body().isSuccess()) {

                    for (ContractorDISurvey.ContractorDISurveyData data : response.body().getSurveyDataList()) {
                        if (data.getInspectionStatus() != null) {
                            if (data.getInspectionStatus().equals("3")) {
                                contractorDISurveyList.add(data);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    if (contractorDISurveyList.isEmpty()) {
                        noDataTextView.setVisibility(View.VISIBLE);

                    }

                } else {
                    noDataTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ContractorDISurvey> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                noDataTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onUpdateStatusClicked(ContractorDISurvey.ContractorDISurveyData diSurvey) {
        activityResultLaunch.launch(new Intent(ContractorDISurveyActivity.this, UpdateContractorSurveyActivity.class)
                .putExtra("data", diSurvey));
    }

    @Override
    public void onLogsClicked(ContractorDISurvey.ContractorDISurveyData diSurvey) {
        startActivity(new Intent(ContractorDISurveyActivity.this, DIComplaintLogsActivity.class)
                .putExtra("data", diSurvey));
    }

    private void onIntentCallback() {
        activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == 123) {
                        Intent data = result.getData();
                        String myStr = data.getStringExtra("ok");
                        assert myStr != null;
                        if (myStr.equalsIgnoreCase("ok")) {
                            if (login.getRoleId() == 9) {//RFI Contractor
//                            if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
                                getSurveyForContractor();
                            } else {
                                getSurveyForFE();
                            }

                        }
                    }
                });
    }

}