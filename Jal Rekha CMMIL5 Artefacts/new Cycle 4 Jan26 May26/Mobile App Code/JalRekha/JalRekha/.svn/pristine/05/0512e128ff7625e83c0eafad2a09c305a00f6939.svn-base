package mapitgis.jalnigam.rfi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.Objects;

import mapitgis.jalnigam.BaseActivity;
import mapitgis.jalnigam.Menu;
import mapitgis.jalnigam.MenuAdapter;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityWorkMonitoringBinding;
import mapitgis.jalnigam.rfi.helper.PrefManager;

public class WorkMonitoringActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWorkMonitoringBinding binding = ActivityWorkMonitoringBinding.inflate(getLayoutInflater());
        //EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appbar.toolbar);//findViewById(R.id.toolbar)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Work Monitoring/अनुश्रवण");
//        setContentView(R.layout.activity_work_monitoring);

        Login login = SqLite.instance(this).getLogin();

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

//        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
//        titleTextView = findViewById(R.id.toolbar_title_text_view);
//        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
//        titleTextView.setText("Work Monitoring/अनुश्रवण");
//        backImageView.setOnClickListener(v -> finish());

//        PrefManager prefManager = new PrefManager(this);

//        recyclerView = findViewById(R.id.work_monitoring_recycler_view);
//        rfiDashboardList = new ArrayList<>();
//        dashboardAdapter = new RFIDashboardAdapter(this, rfiDashboardList, this);
//
//        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(dashboardAdapter);

        MenuAdapter menuAdapter = new MenuAdapter(this) {
            @Override
            protected void onClick(@NonNull Menu menu) {
                onMenu(menu);
            }
        };

        if (login.getRoleId() == 9) {//RFI Contractor
//        if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
            menuAdapter.add(new Menu(1, R.color.green, R.drawable.work_monitoring, R.string.inspection_request, R.string.inspection_request_detail));
            menuAdapter.add(new Menu(2, R.color.blue, R.drawable.monitoring, R.string.inspection_history, R.string.inspection_history_detail));
            menuAdapter.add(new Menu(3, R.color.red, R.drawable.baseline_history_24, R.string.re_inspection_request, R.string.re_inspection_request_detail));
        }else{
            menuAdapter.add(new Menu(4, R.color.blue, R.drawable.baseline_history_24, R.string.inspection_history, R.string.inspection_history_detail));
            menuAdapter.add(new Menu(5, R.color.colorPrimary, R.drawable.monitoring, R.string.inspection_assigned_list, R.string.inspection_assigned_list_detail));
        }
        binding.gridView.setAdapter(menuAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void onMenu(@NonNull Menu menu) {
        switch (menu.getId()) {
            case 1:
                Utility.open(this, RequestInspectionActivity.class);
                break;
            case 2:
                startActivity(new Intent(this, InspectionHistoryActivity.class)
                        .putExtra("title", menu.getName())
                        .putExtra("status", ""));
                break;
            case 3:
                startActivity(new Intent(this, InspectionHistoryActivity.class)
                        .putExtra("title", menu.getName())
                        .putExtra("status", "6"));
                break;
            case 4:
                Utility.open(this, RFIHistoryActivity.class);
                break;
            case 5:
                Utility.open(this, InspectionAssignActivity.class);
                break;
        }
    }

//    void addMonitoring() {
//        RFIDashboard dashboard;
//
//        if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
//            dashboard = new RFIDashboard();
//            dashboard.setTitle("Inspection Request");
//            dashboard.setDescription("");
//            dashboard.setColor(R.color.gray_et);
//            dashboard.setIcon(R.drawable.work_monitoring);
//            rfiDashboardList.add(dashboard);
//        }
//
//        dashboard = new RFIDashboard();
//        dashboard.setTitle("Inspection History");
//        dashboard.setDescription("");
//        dashboard.setColor(R.color.redLightColor);
//        dashboard.setIcon(R.drawable.monitoring);
//        rfiDashboardList.add(dashboard);
//
//        if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
//            dashboard = new RFIDashboard();
//            dashboard.setTitle("Re-Inspection Requests");
//            dashboard.setDescription("");
//            dashboard.setColor(R.color.redColor);
//            dashboard.setIcon(R.drawable.baseline_history_24);
//            rfiDashboardList.add(dashboard);
//        }
//
//        if (prefManager.getUserType().equalsIgnoreCase("field engineer")) {
//            dashboard = new RFIDashboard();
//            dashboard.setTitle("Inspection Assign List");
//            dashboard.setDescription("");
//            dashboard.setColor(R.color.redLightColor);
//            dashboard.setIcon(R.drawable.monitoring);
//            rfiDashboardList.add(dashboard);
//        }
//
//        dashboardAdapter.notifyDataSetChanged();
//    }

//    @Override
//    public void onDashboardClicked(RFIDashboard rfiDashboard, int position) {
//
//        if (prefManager.getUserType().equalsIgnoreCase("contractor")) {
//            if (position == 0) {
//                startActivity(new Intent(WorkMonitoringActivity.this, RequestInspectionActivity.class));
//            } else if (position == 1) {
//                startActivity(new Intent(WorkMonitoringActivity.this, InspectionHistoryActivity.class)
//                        .putExtra("title", rfiDashboard.getTitle())
//                        .putExtra("status", ""));
//            } else if (position == 2) {
//                startActivity(new Intent(WorkMonitoringActivity.this, InspectionHistoryActivity.class)
//                        .putExtra("title", rfiDashboard.getTitle())
//                        .putExtra("status", "6"));
//            }
//        } else if (prefManager.getUserType().equalsIgnoreCase("field engineer")) {
//            if (position == 0) {
//                startActivity(new Intent(WorkMonitoringActivity.this, RFIHistoryActivity.class));
//            } else if (position == 1) {
//                startActivity(new Intent(WorkMonitoringActivity.this, InspectionAssignActivity.class));
//            }
//        }
//
//
//      /*
//      else if (position == 1) {
//            startActivity(new Intent(WorkMonitoringActivity.this, InspectionHistoryActivity.class));
//        } else if (position == 2) {
//            startActivity(new Intent(WorkMonitoringActivity.this, InspectionAssignActivity.class));
//        }*/
//    }
}