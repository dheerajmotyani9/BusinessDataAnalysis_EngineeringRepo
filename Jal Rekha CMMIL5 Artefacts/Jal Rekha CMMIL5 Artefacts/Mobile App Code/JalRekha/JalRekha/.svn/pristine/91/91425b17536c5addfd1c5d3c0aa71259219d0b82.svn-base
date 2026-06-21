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
import mapitgis.jalnigam.databinding.FragmentDharaIntakeBinding;
import mapitgis.jalnigam.rfi.adapter.dhara.DharaIntakeHistoryAdapter;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.dhara.DharaIntakeHistory;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DharaIntakeFragment extends Fragment implements DharaIntakeHistoryAdapter.DharaIntakeHistoryListener {

    private FragmentDharaIntakeBinding binding;

    private ApiInterface apiInterface;
    private ProgressHelper progressHelper;

    private List<DharaIntakeHistory.DharaIntakeHistoryData> dataList;
    private DharaIntakeHistoryAdapter adapter;

    public DharaIntakeFragment() {
        // Required empty public constructor
    }

    public static DharaIntakeFragment newInstance() {
        return new DharaIntakeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentDharaIntakeBinding.inflate(getLayoutInflater());

        apiInterface = ApiClient.getClient(getContext()).create(ApiInterface.class);
        progressHelper = new ProgressHelper(getActivity());

        dataList = new ArrayList<>();
        adapter = new DharaIntakeHistoryAdapter(getActivity(), dataList, this);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.dharaIntakeRecyclerView.setLayoutManager(manager);
        binding.dharaIntakeRecyclerView.setAdapter(adapter);

        getHistory();
        return binding.getRoot();
    }

    private void getHistory() {
        binding.dharaIntakeProgressBar.setVisibility(View.VISIBLE);
        Login login = SqLite.instance(requireActivity()).getLogin();
        /*
        {
    "etoken": "",
    "intake_id": 0,
    "survey_date": ""
}
         */
        Map<String, Object> body = new HashMap<>();
        body.put("intake_id", "0");
        body.put(Utility.E_TOKEN, "");
        body.put("survey_date", "");
        body.put("user_id", String.valueOf(login.getId()));

        Call<DharaIntakeHistory> call = apiInterface.getDharaIntakeHistory(body);

        call.enqueue(new Callback<DharaIntakeHistory>() {
            @Override
            public void onResponse(Call<DharaIntakeHistory> call, Response<DharaIntakeHistory> response) {
                binding.dharaIntakeProgressBar.setVisibility(View.GONE);

                if (response.body().isSuccess()) {
                    if (!response.body().getData().isEmpty()) {
                        dataList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.noDharaIntakeHisTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.noDharaIntakeHisTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DharaIntakeHistory> call, Throwable t) {
                binding.noDharaIntakeHisTv.setVisibility(View.VISIBLE);
                binding.noDharaIntakeHisTv.setText(ProgressHelper.ERROR_MESSAGE);
                binding.dharaIntakeProgressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onDetailClicked(DharaIntakeHistory.DharaIntakeHistoryData data) {

    }
}