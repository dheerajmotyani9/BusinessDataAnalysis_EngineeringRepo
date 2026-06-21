package mapitgis.jalnigam.rfi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.rfi.adapter.InspectionAssignAdapter;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.room.table.InspectionRequestTable;
import mapitgis.jalnigam.rfi.viewmodel.ContractorViewModel;

public class InspectionAssignActivity extends AppCompatActivity implements InspectionAssignAdapter.InspectionAssignListener {

    private RecyclerView recyclerView;
    private List<InspectionRequestTable> assignList;
    private InspectionAssignAdapter adapter;

    private ImageView backImageView;
    private TextView titleTextView;
    private TextView noDataTextView;

    private ContractorViewModel contractorViewModel;

    private ProgressHelper progressHelper;
    private PrefManager prefManager;

    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inspection_assign);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        login = SqLite.instance(this).getLogin();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        contractorViewModel = new ViewModelProvider(this).get(ContractorViewModel.class);
        progressHelper = new ProgressHelper(this);
        prefManager = new PrefManager(this);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("RFI Request");
        backImageView.setOnClickListener(v -> finish());

        noDataTextView = findViewById(R.id.inspection_assing_no_data_text_view);
        recyclerView = findViewById(R.id.inspection_assign_recycler_view);
        assignList = new ArrayList<>();
        adapter = new InspectionAssignAdapter(this, assignList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        getAssign();
    }

    private void getAssign() {
//        contractorViewModel.getInspectionRequest(prefManager.getUserType(), prefManager.getUserId(),"").observe(this, inspectionRequestTables -> {
        contractorViewModel.getInspectionRequest(login.getRoleLC(), login.getIdS(),"").observe(this, inspectionRequestTables -> {
            assignList.clear();
            assignList.addAll(inspectionRequestTables);
            adapter.notifyDataSetChanged();

            if (assignList.isEmpty()) {
                noDataTextView.setVisibility(View.VISIBLE);
            } else {
                noDataTextView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onViewDetailClicked(int position, InspectionRequestTable assign) {
        startActivity(new Intent(InspectionAssignActivity.this, InspectionHistoryDetailActivity.class)
                .putExtra("detail", assign));
    }

    @Override
    public void onViewLogClicked(int position, InspectionRequestTable assign) {
        startActivity(new Intent(InspectionAssignActivity.this, ContractorLogsActivity.class)
                .putExtra("detail", assign));
    }

    @Override
    public void onStartInspectionClicked(int position, InspectionRequestTable assign) {
        startActivity(new Intent(InspectionAssignActivity.this, StartInspectionActivity.class)
                .putExtra("detail", assign));
    }
}