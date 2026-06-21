package mapitgis.jalnigam.rfi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.rfi.adapter.DIComplaintLogsAdapter;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.ContractorDISurvey;
import mapitgis.jalnigam.rfi.model.DIComplaintLogs;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DIComplaintLogsActivity extends AppCompatActivity implements DIComplaintLogsAdapter.DIComplaintLogsListener {

    private ImageView backImageView;
    private TextView titleTextView;

    private ApiInterface apiInterface;
    private ProgressHelper progressHelper;

    private TextView noDataTextView;
    private ProgressBar progressBar;
    private PrefManager prefManager;

    private RecyclerView recyclerView;
    private List<DIComplaintLogs.DIComplaintLogsData> dataList;
    private DIComplaintLogsAdapter adapter;

    private ContractorDISurvey.ContractorDISurveyData diSurvey;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dicomplaint_logs);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        login = SqLite.instance(this).getLogin();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        diSurvey = (ContractorDISurvey.ContractorDISurveyData) getIntent().getSerializableExtra("data");

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Complaint Logs");
        backImageView.setOnClickListener(v -> finish());

        prefManager = new PrefManager(this);
        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        progressHelper = new ProgressHelper(this);

        noDataTextView = findViewById(R.id.di_complaint_logs_no_data_text_view);
        progressBar = findViewById(R.id.di_complaint_logs_progress_bar);

        recyclerView = findViewById(R.id.di_complaint_logs_recycler_view);
        dataList = new ArrayList<>();
        adapter = new DIComplaintLogsAdapter(this, dataList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        getLogs();
    }

    private void getLogs() {
        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> body = new HashMap<>();
        body.put("etokens", login.getToken());//prefManager.getToken1());
        body.put("inspection_id", diSurvey.getInspectionId());
        Call<DIComplaintLogs> call;


        if (login.getRoleId() == 9) {//RFI Contractor
//        if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
            call = apiInterface.getDIContractorLogs(body);
        } else {
            call = apiInterface.getDIFELogs(body);
        }


        call.enqueue(new Callback<DIComplaintLogs>() {
            @Override
            public void onResponse(Call<DIComplaintLogs> call, Response<DIComplaintLogs> response) {
                progressBar.setVisibility(View.GONE);

                if (response.body().isSuccess()) {
                    if (response.body().getData().isEmpty()) {
                        noDataTextView.setVisibility(View.VISIBLE);
                    } else {
                        dataList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    noDataTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DIComplaintLogs> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                noDataTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onViewImageClicked(DIComplaintLogs.DIComplaintLogsData data) {
        photos(data);
    }

    private void photos(DIComplaintLogs.DIComplaintLogsData data) {
        String[] itemsArray = data.getPhotoPath().split(",");

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
}