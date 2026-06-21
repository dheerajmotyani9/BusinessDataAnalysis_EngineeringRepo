package mapitgis.jalnigam.dhara.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.FragmentDharaESRBinding;
import mapitgis.jalnigam.dhara.DharaHistoryDetailActivity;
import mapitgis.jalnigam.rfi.adapter.dhara.DharaESRHistoryAdapter;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.dhara.DharaESRHistory;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DharaESRFragment extends Fragment implements DharaESRHistoryAdapter.DharaESRHistoryListener {

    private FragmentDharaESRBinding binding;

    private ApiInterface apiInterface;
    private List<DharaESRHistory.DharaESRHistoryData> dataList;
    private DharaESRHistoryAdapter adapter;

    public DharaESRFragment() {
        // Required empty public constructor
    }

    public static DharaESRFragment newInstance() {
        return new DharaESRFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDharaESRBinding.inflate(getLayoutInflater());
        apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);

        dataList = new ArrayList<>();
        adapter = new DharaESRHistoryAdapter(getActivity(), dataList, this);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.dharaEsrRecyclerView.setLayoutManager(manager);
        binding.dharaEsrRecyclerView.setAdapter(adapter);

        getHistory();

        return binding.getRoot();
    }

    private void getHistory() {
        binding.dharaEsrProgressBar.setVisibility(View.VISIBLE);
        Login login = SqLite.instance(requireActivity()).getLogin();
        Map<String, String> body = new HashMap<>();
        body.put("esr_name", "");
        body.put(Utility.E_TOKEN, "");
        body.put("survey_date", "");
        body.put("user_id", String.valueOf(login.getId()));
//        body.put("user_id", prefManager.getSqcId());
        body.put("village_name", "");

        Call<DharaESRHistory> call = apiInterface.getDharaESRHistory(body);

        call.enqueue(new Callback<DharaESRHistory>() {
            @Override
            public void onResponse(Call<DharaESRHistory> call, Response<DharaESRHistory> response) {
                binding.dharaEsrProgressBar.setVisibility(View.GONE);

                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {
                        dataList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.noDharaEsrHisTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.noDharaEsrHisTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DharaESRHistory> call, Throwable t) {
                binding.noDharaEsrHisTv.setVisibility(View.VISIBLE);
                binding.noDharaEsrHisTv.setText(ProgressHelper.ERROR_MESSAGE);
                binding.dharaEsrProgressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onDetailClicked(DharaESRHistory.DharaESRHistoryData data) {
        startActivity(new Intent(getActivity(), DharaHistoryDetailActivity.class)
                .putExtra("data", data));
    }
}