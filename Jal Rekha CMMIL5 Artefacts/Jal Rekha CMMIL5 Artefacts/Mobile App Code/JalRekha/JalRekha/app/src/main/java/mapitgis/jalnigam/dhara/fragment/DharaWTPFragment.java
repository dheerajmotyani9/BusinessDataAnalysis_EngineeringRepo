package mapitgis.jalnigam.dhara.fragment;

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
import mapitgis.jalnigam.databinding.FragmentDharaWTPBinding;
import mapitgis.jalnigam.rfi.adapter.dhara.DharaWTPHistoryAdapter;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.dhara.DharaWTPHistory;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DharaWTPFragment extends Fragment implements DharaWTPHistoryAdapter.DharaWTPHistoryListener {

    private FragmentDharaWTPBinding binding;

    private ApiInterface apiInterface;
    private ProgressHelper progressHelper;

    private List<DharaWTPHistory.DharaWTPHistoryData> dataList;
    private DharaWTPHistoryAdapter adapter;
    private PrefManager prefManager;


    public DharaWTPFragment() {
        // Required empty public constructor
    }
    public static DharaWTPFragment newInstance() {
        return new DharaWTPFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDharaWTPBinding.inflate(getLayoutInflater());
        apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        progressHelper = new ProgressHelper(getActivity());
        prefManager = new PrefManager(requireActivity());

        dataList = new ArrayList<>();
        adapter = new DharaWTPHistoryAdapter(getActivity(), dataList, this);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.dharaWtpRecyclerView.setLayoutManager(manager);
        binding.dharaWtpRecyclerView.setAdapter(adapter);


        getHistory();
        return binding.getRoot();
    }


    private void getHistory() {
        Login login = SqLite.instance(requireActivity()).getLogin();
        binding.dharaWtpProgressBar.setVisibility(View.VISIBLE);
        Map<String, Object> body = new HashMap<>();
        body.put("wtp_id", "0");
        body.put(Utility.E_TOKEN, "");
        body.put("survey_date", "");
        body.put("user_id", String.valueOf(login.getId()));
//        body.put("user_id", prefManager.getSqcId());


        Call<DharaWTPHistory> call = apiInterface.getDharaWTPHistory(body);

        call.enqueue(new Callback<DharaWTPHistory>() {
            @Override
            public void onResponse(Call<DharaWTPHistory> call, Response<DharaWTPHistory> response) {
                binding.dharaWtpProgressBar.setVisibility(View.GONE);

                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {
                        dataList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.noDharaWtpHisTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.noDharaWtpHisTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DharaWTPHistory> call, Throwable t) {
                binding.noDharaWtpHisTv.setVisibility(View.VISIBLE);
                binding.noDharaWtpHisTv.setText(ProgressHelper.ERROR_MESSAGE);
                binding.dharaWtpProgressBar.setVisibility(View.GONE);

            }
        });
    }


    @Override
    public void onDetailClicked(DharaWTPHistory.DharaWTPHistoryData data) {

    }
}