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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.rfi.adapter.RFIHistoryAdapter;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.InspectionRequest;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import mapitgis.jalnigam.room.table.InspectionRequestTable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RFIHistoryActivity extends AppCompatActivity implements RFIHistoryAdapter.RFIHistoryListener {

    private ImageView backImageView;
    private TextView titleTextView;

    private ProgressBar progressBar;
    private TextView noDataTextView;
    private RecyclerView recyclerView;
    private List<InspectionRequest.InspectionRequestData> requestDataList;
    private RFIHistoryAdapter historyAdapter;

    private ProgressHelper progressHelper;
    private PrefManager prefManager;
    private ApiInterface apiInterface;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rfihistory);
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
        progressHelper = new ProgressHelper(this);
        prefManager = new PrefManager(this);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("Inspection History");
        backImageView.setOnClickListener(v -> finish());

        progressBar = findViewById(R.id.rfi_progress_bar);
        noDataTextView = findViewById(R.id.rfi_history_no_data_text_view);

        recyclerView = findViewById(R.id.rfi_history_recycler_view);
        requestDataList = new ArrayList<>();
        historyAdapter = new RFIHistoryAdapter(this,requestDataList,this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(historyAdapter);

        getHistory();
    }

    private void getHistory(){

        Map<String, String> body = new HashMap<>();
        body.put("piu_id", "0");
        body.put("sqc_user_id", "0");
        body.put("tpia_user_id", "0");
        body.put("is_assigned", "0");
        body.put("scheme_id", "0");
        body.put("insert_date", "0");
        body.put(Utility.E_TOKEN, login.getToken());//prefManager.getToken1());
        body.put("assigne_to", "0");
        body.put("contractor_id", "0");

        progressBar.setVisibility(View.VISIBLE);

        Call<InspectionRequest> call = apiInterface.getFEInspectionHistory(body);

        call.enqueue(new Callback<InspectionRequest>() {
            @Override
            public void onResponse(Call<InspectionRequest> call, Response<InspectionRequest> response) {

                progressBar.setVisibility(View.GONE);
                if (response.body().isSuccess()) {
                    if (!response.body().getDataList().isEmpty()) {
                        try {
                            Collections.sort(response.body().getDataList(), (o1, o2) -> {
                                int id1 = safeParseInt(o1.getRfiId());
                                int id2 = safeParseInt(o2.getRfiId());
                                return Integer.compare(id2, id1);
                            });
                        }catch (Exception ignore){

                        }
                       requestDataList.addAll(response.body().getDataList());
                       historyAdapter.notifyDataSetChanged();
                    } else {
                        noDataTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    progressHelper.dismissProgress();
                    if (response.body().getMessage().toLowerCase().contains("session expired")) {
                        progressHelper.showSuccessDialog("Your session is expired. Please login again", "Session Expired", "back", "OK", new ProgressHelper.ShowDialogListener() {
                            @Override
                            public void onShowDialogButtonClicked(String type) {
                                prefManager.logout();
                                Utility.goFirst(RFIHistoryActivity.this,true);
//                                startActivity(new Intent(RFIHistoryActivity.this, TypeActivity.class)
//                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
//
//                                finish();
                            }
                        });
                    } else {
                       noDataTextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<InspectionRequest> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                progressHelper.message("Something went wrong");
                progressHelper.dismissProgress();
            }
        });

    }

    private int safeParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0; // or any fallback you want
        }
    }

    @Override
    public void onRevisitRequestClicked(InspectionRequest.InspectionRequestData history, int position) {

    }

    @Override
    public void onViewDetailClicked(InspectionRequest.InspectionRequestData history, int position) {

    }

    @Override
    public void onViewLogsClicked(InspectionRequest.InspectionRequestData history, int position) {

        InspectionRequestTable requestTable = new InspectionRequestTable();
        requestTable.setRfiId(history.getRfiId());
        startActivity(new Intent(RFIHistoryActivity.this, ContractorLogsActivity.class)
                .putExtra("detail", requestTable));
    }

}