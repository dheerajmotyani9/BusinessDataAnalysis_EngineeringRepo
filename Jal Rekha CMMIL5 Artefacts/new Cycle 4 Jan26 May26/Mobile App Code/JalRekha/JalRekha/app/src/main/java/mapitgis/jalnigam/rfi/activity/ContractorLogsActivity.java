package mapitgis.jalnigam.rfi.activity;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.rfi.adapter.ContractorLogsAdapter;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.ContractorLogs;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import mapitgis.jalnigam.room.table.InspectionRequestTable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractorLogsActivity extends AppCompatActivity {

    private ImageView backImageView;
    private TextView titleTextView;

    private ProgressBar progressBar;
    private TextView noDataTv;
    private RecyclerView recyclerView;
    private List<ContractorLogs.ContractorLogsData> contractorLogsDataList;
    private ContractorLogsAdapter adapter;

    private ProgressHelper progressHelper;
    private PrefManager prefManager;
    private ApiInterface apiInterface;

    private InspectionRequestTable history;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contractor_logs);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        login = SqLite.instance(this).getLogin();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        history = (InspectionRequestTable) getIntent().getSerializableExtra("detail");

        prefManager = new PrefManager(this);
        progressHelper = new ProgressHelper(this);
        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Logs");
        backImageView.setOnClickListener(v -> finish());

        noDataTv = findViewById(R.id.no_contractor_log_found_tv);
        progressBar = findViewById(R.id.contractor_log_progress_bar);

        recyclerView = findViewById(R.id.contractor_log_recycler_view);
        contractorLogsDataList = new ArrayList<>();
        adapter = new ContractorLogsAdapter(this,contractorLogsDataList);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        getLogs();
    }

    private void getLogs(){
        progressBar.setVisibility(View.VISIBLE);

        Map<String,String>body = new HashMap<>();
        body.put(Utility.E_TOKEN,login.getToken());//prefManager.getToken1());
        body.put("rfi_id",history.getRfiId());

        Call<ContractorLogs> call;

        if (login.getRoleId() == 9) {//RFI Contractor
//        if (prefManager.getUserType().equalsIgnoreCase("contractor")){
            call = apiInterface.getContractorLogs(body);
        }else{
            call = apiInterface.getFELogs(body);
        }


        call.enqueue(new Callback<ContractorLogs>() {
            @Override
            public void onResponse(Call<ContractorLogs> call, Response<ContractorLogs> response) {
                progressBar.setVisibility(View.GONE);
                if (response.body().isSuccess()) {
                   contractorLogsDataList.addAll(response.body().getData());
                   adapter.notifyDataSetChanged();
                } else {

                    if (response.body().getMessage().toLowerCase().contains("session expired")) {
                        progressHelper.showSuccessDialog("Your session is expired. Please login again", "Session Expired", "back", "OK", new ProgressHelper.ShowDialogListener() {
                            @Override
                            public void onShowDialogButtonClicked(String type) {
                                prefManager.logout();
                                Utility.goFirst(ContractorLogsActivity.this,true);
//                                startActivity(new Intent(ContractorLogsActivity.this, TypeActivity.class)
//                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                                finish();
                            }
                        });
                    } else {
                        noDataTv.setVisibility(View.VISIBLE);
                      //  progressHelper.message(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ContractorLogs> call, Throwable t) {
                progressHelper.message("Something went wrong");
                noDataTv.setVisibility(View.VISIBLE);
                progressHelper.dismissProgress();
            }
        });
    }
}