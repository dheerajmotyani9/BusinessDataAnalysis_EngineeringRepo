package mapitgis.jalnigam.dhara;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;


import mapitgis.jalnigam.BuildConfig;
import mapitgis.jalnigam.Menu;
import mapitgis.jalnigam.MenuAdapter;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.API;
import mapitgis.jalnigam.core.ApiCaller;
import mapitgis.jalnigam.core.Data;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityDashboardBinding;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;


public class DharaDBActivity extends AppCompatActivity {
    private Login login;
    private PrefManager prefManager;
    private ProgressHelper progressHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDashboardBinding binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        login = SqLite.instance(this).getLogin();
        prefManager = new PrefManager(this);
        progressHelper = new ProgressHelper(this);

        View.OnClickListener logoutLi = view -> {
            Data data = new Data();
            data.put("imei", login.getMobile());//Utility.getDeviceID(this));
            Utility.show(this, getString(R.string.are_you_sure), getString(R.string.want_to_logout_from_app), getString(R.string.logout), view1 -> new ApiCaller(this, (response, key) -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    SqLite.instance(this).logout();
                    prefManager.logout();
                    Utility.goFirst(this, true);
                    Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                } catch (Exception e) {
                    Utility.show(this, e);
                }
            }, 1, data.toString(), getString(R.string.please_wait)).start(API.LOGOUT), true);
        };
        binding.buttonProfile.setText(R.string.logout);
        binding.buttonProfile.setOnClickListener(logoutLi);

        binding.textViewLogout.setText(Utility.html(String.format("<u>%s</u>", getString(R.string.logout))));
        binding.textViewLogout.setOnClickListener(logoutLi);

        binding.textViewVersion.setText(String.format("Version %s", BuildConfig.VERSION_NAME));

        MenuAdapter menuAdapter = new MenuAdapter(this) {
            @Override
            protected void onClick(@NonNull Menu menu) {
                onMenu(menu.getId());
            }
        };


        binding.textViewName.setText(login.getName());
        binding.textViewMobile.setText(login.getMobile());
        binding.textViewDesignation.setText(login.getDesignation());

        if (login.getRoleId() == 10) {
            menuAdapter.add(new Menu(1, R.color.green, R.drawable.monitoring, R.string.approved_details, R.string.approved_details_detail));
            menuAdapter.add(new Menu(2, R.color.blue, R.drawable.baseline_history_24, R.string.view_history, R.string.view_history_detail));
        }else{
            menuAdapter.add(new Menu(3, R.color.green, R.drawable.monitoring, R.string.water_supply, R.string.water_supply_detail));
            menuAdapter.add(new Menu(4, R.color.blue, R.drawable.ic_quntity, R.string.daily_water_supply, R.string.daily_water_supply_detail));
        }

        binding.linearLayoutRFIContractor.setVisibility(View.VISIBLE);
        binding.linearLayoutDesignation.setVisibility(View.VISIBLE);

        binding.gridView.setAdapter(menuAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!progressHelper.isAutoTimeEnabled(this)) {
            progressHelper.showAutoTimeDialog(this);
        }
    }

    private void onMenu(int id) {
        switch (id) {
            case 1:
                Utility.open(this, DharaContractorApprovedActivity.class);
                break;
            case 2:
                Utility.open(this, DharaContractorHistoryActivity.class);
                break;
            case 3:
                Utility.open(this, DharaHistoryActivity.class);
                break;
            case 4:
                Utility.open(this, DharaDailyWaterSupplyActivity.class);
                break;
        }
    }
}
