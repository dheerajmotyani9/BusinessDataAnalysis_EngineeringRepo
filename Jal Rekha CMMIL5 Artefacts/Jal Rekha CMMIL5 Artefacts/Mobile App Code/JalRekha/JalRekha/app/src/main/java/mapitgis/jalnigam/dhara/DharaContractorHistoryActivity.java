package mapitgis.jalnigam.dhara;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
import mapitgis.jalnigam.databinding.ActivityDharaContractorHistoryBinding;
import mapitgis.jalnigam.rfi.adapter.dhara.DharaContractorHistoryAdapter;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.dhara.DharaCommentByContractor;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DharaContractorHistoryActivity extends AppCompatActivity implements DharaContractorHistoryAdapter.DharaContractorHistoryListener {

    private ImageView backImageView;
    private TextView titleTextView;

    private ActivityDharaContractorHistoryBinding binding;

    private ApiInterface apiInterface;
    private ProgressHelper progressHelper;
    private PrefManager prefManager;

    private List<DharaCommentByContractor.DharaCommentByContractorData> dataList;
    private DharaContractorHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        binding = ActivityDharaContractorHistoryBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

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
        prefManager = new PrefManager(this);

        backImageView = findViewById(R.id.tool_bar_navigation_icon_image_view);
        titleTextView = findViewById(R.id.toolbar_title_text_view);
        backImageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        titleTextView.setText("History");
        backImageView.setOnClickListener(v -> finish());

        dataList = new ArrayList<>();
        adapter = new DharaContractorHistoryAdapter(this, dataList, this);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.dharaContractorHistoryRecyclerView.setLayoutManager(manager);
        binding.dharaContractorHistoryRecyclerView.setAdapter(adapter);

        getHistory();
    }

    private void getHistory() {
        binding.dharaContractorHistoryProgressBar.setVisibility(View.VISIBLE);

        Map<String, String> body = new HashMap<>();
        body.put("intake_id", prefManager.getSchemeId());

        Call<DharaCommentByContractor> call = apiInterface.getDharaContractorHistory(body);

        call.enqueue(new Callback<DharaCommentByContractor>() {
            @Override
            public void onResponse(Call<DharaCommentByContractor> call, Response<DharaCommentByContractor> response) {
                binding.dharaContractorHistoryProgressBar.setVisibility(View.GONE);

                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {
                        dataList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.dharaContractorHistoryNoDataTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.dharaContractorHistoryNoDataTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DharaCommentByContractor> call, Throwable t) {
                binding.dharaContractorHistoryProgressBar.setVisibility(View.GONE);
                binding.dharaContractorHistoryNoDataTv.setText(ProgressHelper.ERROR_MESSAGE);
                binding.dharaContractorHistoryNoDataTv.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDetailClicked(int position, DharaCommentByContractor.DharaCommentByContractorData data) {
        startActivity(new Intent(DharaContractorHistoryActivity.this, DharaContractorHistoryDetailActivity.class)
                .putExtra("data",data));
    }
}