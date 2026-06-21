package mapitgis.jalnigam;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapitgis.jalnigam.core.API;
import mapitgis.jalnigam.core.ApiCaller;
import mapitgis.jalnigam.core.Component;
import mapitgis.jalnigam.core.Data;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.Module;
import mapitgis.jalnigam.core.Point;
import mapitgis.jalnigam.core.ProgressDialog;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityDashboardBinding;
import mapitgis.jalnigam.databinding.DialogSyncBinding;
import mapitgis.jalnigam.dhara1.DharaActivity;
import mapitgis.jalnigam.di.DIActivity;
import mapitgis.jalnigam.isa.ISAActivity;
import mapitgis.jalnigam.isa.IsaInfo;
//import mapitgis.jalnigam.nirmal.AddNirmalActivity;
//import mapitgis.jalnigam.nirmal.repository.NirmalRepository;
//import mapitgis.jalnigam.nirmal.screens.wqm_list.WQMListActivity;
import mapitgis.jalnigamk.nirmal.repository.NirmalRepository;
import mapitgis.jalnigamk.nirmal.screens.wqm_list.WQMListActivity;
import mapitgis.jalnigam.rfi.activity.RFIDBActivity;


public class DashboardActivity extends AppCompatActivity {
    private ActivityDashboardBinding binding;
    private Login login;
    private SyncAdapter syncAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        login = SqLite.instance(this).getLogin();
        syncAdapter = new SyncAdapter(this, login) {
            @Override
            protected void onClick(@NonNull Module module) {
                syncNow(module, -1);
            }
        };


        binding.textViewVersion.setOnLongClickListener(view1 -> {
            Utility.bkp(this);
            return true;
        });

        binding.buttonProfile.setOnClickListener(view -> onMenu(1));
        if(login.getRoleId()==10){
            //dhara contractor
            binding.buttonProfile.setVisibility(View.GONE);
        }

        binding.textViewVersion.setText(String.format("Version %s", BuildConfig.VERSION_NAME));

        MenuAdapter menuAdapter = new MenuAdapter(this) {
            @Override
            protected void onClick(@NonNull Menu menu) {
                onMenu(menu.getId());
            }
        };

        menuAdapter.add(new Menu(2, R.color.green, R.drawable.ic_sync, R.string.sync_module, R.string.sync_data_detail));

        if(login.isDailyInspection()){
            menuAdapter.add(new Menu(4, R.color.colorPrimaryDark, R.drawable.ic_di, R.string.daily_inspection, R.string.daily_inspection_dash));
        }
        if (login.isAssets()) {
            menuAdapter.add(new Menu(3, R.color.blue, R.drawable.ic_dollar, R.string.assets_mapping, R.string.mapping_asset_detail));
        }
        if (login.isAnusravana()) {
            menuAdapter.add(new Menu(7, R.color.green, R.drawable.ic_rfi, R.string.rfi, R.string.rfi_detail));
        }
        if (login.isDhara()) {
            menuAdapter.add(new Menu(5, R.color.blue1, R.drawable.ic_quntity, R.string.water_quantity, R.string.water_quantity_detail));
        }
        if (login.isIsa()) {
            menuAdapter.add(new Menu(6, R.color.red, R.drawable.ic_isa, R.string.isa, R.string.isa_detail));
        }

        if(login.isNirmal()){
            menuAdapter.add(new Menu(8, R.color.colorYellow, R.drawable.ic_quality, R.string.water_quality, R.string.water_quality_detail));
        }
//        menuAdapter.add(new Menu(8, R.color.colorYellow, R.drawable.ic_quality, R.string.water_quality, R.string.water_quality_detail));

        binding.gridView.setAdapter(menuAdapter);
        binding.textViewLogout.setText(Utility.html(String.format("<u>%s</u>", getString(R.string.logout))));
        binding.textViewLogout.setOnClickListener(view -> {
            Data data = new Data();
            data.put("imei", login.getMobile());//Utility.getDeviceID(this));
            Utility.show(this, getString(R.string.are_you_sure), getString(R.string.want_to_logout_from_app), getString(R.string.logout), view1 ->{
                SqLite.instance(this).logout();
                Utility.goFirst(this, true);
                Utility.show(this, "Logged out successfully.");
            }, true);
        });
    }

    private void onMenu(int id) {
        switch (id) {
            case 1:
                Utility.open(this, RegisterActivity.class);
                break;
            case 2:
                syncDialog();
                break;
            case 3:
                if (check(Module.ASSETS_MAPPING, id)) {
                    Utility.open(this, AssetActivity.class);
                }
                break;
            case 4:
                if (check(Module.DI, id)) {
                    Utility.open(this, DIActivity.class);
                }
                break;
            case 5:
                if (check(Module.DHARA, id, true)) {
                    Utility.open(this, DharaActivity.class);
                }
                break;
            case 6:
                if (check(Module.ISA, id)) {
                    Utility.open(this, ISAActivity.class);
                }
                break;
            case 7:
                if (check(Module.DI, id)) {
                    Utility.open(this, RFIDBActivity.class);
                }
                break;
            case 8:
                if (check(Module.NIRMAL, id)) {
                    Utility.open(this, WQMListActivity.class);
                }
                //Utility.open(this, WQMListActivity.class);
                break;
        }
    }

    private boolean check(@NonNull Module module, int menuId) {
        return check(module, menuId, false);
    }

    private boolean check(@NonNull Module module, int menuId, boolean isToday) {
        String data = SqLite.instance(this).getLastTime(module);
        boolean check = data != null;
        if (!check) {
            Utility.show(this, "Please sync module (" + module.name + ")");
        }
        if (isToday && check) {
            if (!data.startsWith(Utility.getCurrentDate().first)) {
                Utility.show(this, "Please sync module (" + module.name + ")\nआपको यह Module हर दिन Sync करना होगा।.");
                check = false;
            }
        }
        if (!check) {
            syncNow(module, menuId);
        }
        return check;
    }

    private void syncNow(Module module, int menuId) {
        if (module == Module.DI) {
            syncDI(module, menuId);
        } else if (module == Module.ASSETS_MAPPING) {
            syncAM(module, menuId);
        } else if (module == Module.ISA) {
            syncISA(module, menuId);
        } else if (module == Module.DHARA) {
            syncDhara(module, menuId);
        } else if (module == Module.NIRMAL) {
            syncNirmal(module, menuId);
        } else {
            Utility.show(DashboardActivity.this, "Module not for sync");
        }
    }

    AlertDialog alertDialog,alertDialog2;
    private void closeSyncD(){
        if(alertDialog != null && alertDialog.isShowing()){
            alertDialog.dismiss();
        }
    }
    private void syncDialog() {
        closeSyncD();
        if(alertDialog2 != null && alertDialog2.isShowing()){
            alertDialog2.dismiss();
        }
        DialogSyncBinding dialogSyncBinding = DialogSyncBinding.inflate(getLayoutInflater());
        alertDialog2 = new AlertDialog.Builder(this)
                .setView(dialogSyncBinding.getRoot())
                .setCancelable(false)
                .setPositiveButton(R.string.close, (d, v) -> d.dismiss())
                .create();
        dialogSyncBinding.listView.setAdapter(syncAdapter);

        alertDialog2.show();
    }

    private void syncDI(@NonNull Module module, int menuId) {
        closeSyncD();
        alertDialog = new AlertDialog.Builder(this).setTitle(module.name).setMessage(R.string.do_you_really_want_to_sync_data).setNegativeButton(R.string.cancel, (d, v) -> d.dismiss()).setPositiveButton(R.string.ok, null).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            Data data = new Data();
            data.put(Utility.E_TOKEN, login.getToken());
            new ApiCaller(this, (response, key) -> {
                try {
                    JSONObject jsonObject1 = new JSONObject(response);
                    if (jsonObject1.getBoolean(Utility.SUCCESS)) {
                        JSONObject jsonObject = jsonObject1.getJSONObject(Utility.DATA);
                        SqLite.instance(this).resetLastTime(module);


                        SqLite.instance(this).CLEAR_APPLICATION_TYPE();
//                    SqLite.instance(this).CLEAR_SCHEME();
                        SqLite.instance(this).CLEAR_SCHEME_DI_NEW();
                        SqLite.instance(this).CLEAR_DISTRICT_DI_NEW();
                        SqLite.instance(this).CLEAR_COMPONENT_DI_NEW();
                        SqLite.instance(this).CLEAR_QA_QC_REVIEW_DI_NEW();

                        JSONArray jsonArray = jsonObject.getJSONArray("deptInspectionAppType");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            SqLite.instance(this).ADD_APPLICATION_TYPE(new SpinnerItem(jsonObject2.getString("application_type_id"), jsonObject2.getString("application_type_nm")));
                        }

                        jsonArray = jsonObject.getJSONArray("deptInspectionScheme");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                        SqLite.instance(this).ADD_SCHEME(new SpinnerItem(jsonObject2.getString("scheme_id"), jsonObject2.getString("scheme_name")), jsonObject2.getString("piu_dist_cd"));
                            SqLite.instance(this).ADD_SCHEME_DI_NEW(new SpinnerItem(jsonObject2.getString("scheme_id"), jsonObject2.getString("scheme_name")), jsonObject2.getString("piu_dist_cd"));
                        }

                        jsonArray = jsonObject.getJSONArray("deptInspectionPIU");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            SqLite.instance(this).ADD_DISTRICT_DI_NEW(new SpinnerItem(jsonObject2.getString("piu_dist_cd"), jsonObject2.getString("piu_name")));
                        }

                        jsonArray = jsonObject.getJSONArray("deptInspectionComponent");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            SqLite.instance(this).ADD_COMPONENT_DI_NEW(new SpinnerItem(jsonObject2.getString("component_id"), jsonObject2.getString("component_name"), new Component("", "", jsonObject2.getInt("component_type"))), "");
                        }

                        jsonArray = jsonObject.getJSONArray("deptInspection_qa_qc_review");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            SqLite.instance(this).ADD_QA_QC_REVIEW_DI_NEW(new SpinnerItem(jsonObject2.getString("qa_qc_review_type_id"), jsonObject2.getString("qa_qc_review")));
                        }

//                        prefManager.setIsDiLogged(true);
//                        prefManager.setToken(jsonObject.getString("etoken"));


                        SqLite.instance(this).updateLastTime(module);

                        syncAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();
//                        String ls = SqLite.instance(this).getLastTime(module);
//                        dialogSyncBinding.textViewLastSyncDI.setText(ls == null ? "N/A" : ls);


                        onMenu(menuId);
                        Utility.show(this, jsonObject1.getString(Utility.MESSAGE));
                    } else {
                        Utility.show(this, jsonObject1.getString(Utility.MESSAGE));
                    }
                } catch (Exception e) {
                    Utility.show(this, e);
                    finish();
                }
            }, 1, data.toString(), getString(R.string.please_wait)).start(API.DI.SYNC);
        });
    }

    private void syncAM(Module module, int menuId) {
        if (login.getMobile().equals("1234567890")) {
            SqLite.instance(this).updateLastTime(module);
            Utility.show(this, "Sync Done.");
            return;
        }
        closeSyncD();
        alertDialog = new AlertDialog.Builder(this).setTitle(module.name).setMessage(R.string.do_you_really_want_to_sync_data).setNegativeButton(R.string.cancel, (d, v) -> d.dismiss()).setPositiveButton(R.string.ok, null).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            Data data = new Data();
//            Log.e("EMI", login.getMobile());//Utility.getDeviceID(this));
            data.put("imei", login.getMobile());//Utility.getDeviceID(this));
            new ApiCaller(this, (response, key) -> {
                ProgressDialog.startDialog(this);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        SqLite sqLite = SqLite.instance(this);
                        sqLite.CLEAR_SCHEME();
                        sqLite.CLEAR_ESR();
                        sqLite.CLEAR_COMPONENT_AM();
                        sqLite.CLEAR_DISTRICT_AM();
                        sqLite.CLEAR_POINTS();
                        sqLite.CLEAR_UPLOADED_ASSETS();

                        sqLite.CLEAR_DEPTH_COVER();
                        sqLite.CLEAR_MATERIAL_TYPE();
                        sqLite.CLEAR_PIPE_DIAMETER();
                        sqLite.CLEAR_MATERIAL_INFO_VALUE();


                        sqLite.resetLastTime(module);
                        if (jsonObject.getBoolean(Utility.SUCCESS)) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject(Utility.DATA);
                            JSONObject jsonObject2;

                            JSONArray jsonArray = jsonObject1.getJSONArray("schemes");
                            StringBuilder Q = new StringBuilder();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                Q.append(sqLite.ADD_SCHEME_QUERY(new SpinnerItem(jsonObject2.getString("SchemeID"), jsonObject2.getString("Name")), i));
                            }
//                            Log.e("ABCD",Q.toString());
                            sqLite.EXECUTE(Q.toString());
                            Q.setLength(0);

                            jsonArray = jsonObject1.getJSONArray("ESRs");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                Q.append(sqLite.ADD_ESR_QUERY(new SpinnerItem(jsonObject2.getString("ESRID"), jsonObject2.getString("Name")), jsonObject2.getString("SchemeID"), i));
                            }

                            sqLite.EXECUTE(Q.toString());
                            Q.setLength(0);

                            jsonArray = jsonObject1.getJSONArray("Components");
                            //Log.e("ABCD","IMEI - "+Utility.getDeviceID(this)+" ABC - "+jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                Q.append(sqLite.ADD_COMPONENT_QUERY(new SpinnerItem(jsonObject2.getString("Id"), jsonObject2.getString("Name"), new Component(jsonObject2.getString("Head"), jsonObject2.getString("Layer"), jsonObject2.getInt("component_type"))), jsonObject2.getString("SchemeID"), i));
                            }

                            sqLite.EXECUTE(Q.toString());
                            Q.setLength(0);

                            jsonArray = jsonObject1.getJSONArray("district");
                            //Log.e("ABCD","IMEI - "+Utility.getDeviceID(this)+" ABC - "+jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                Q.append(sqLite.ADD_DISTRICT_QUERY_AM(new SpinnerItem(jsonObject2.getString("DistrictID"), jsonObject2.getString("Name")), i));
                            }

                            sqLite.EXECUTE(Q.toString());
                            Q.setLength(0);

                            jsonArray = jsonObject1.getJSONArray("points");
                            //Log.e("ABCD","IMEI - "+Utility.getDeviceID(this)+" ABC - "+jsonArray);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                if (i % 10000 == 0) {
                                    Log.e("ABCD", i + " A");
                                    sqLite.EXECUTE(Q.toString());
                                    Q.setLength(0);
                                }
                                Q.append(sqLite.ADD_POINTS_QUERY(new SpinnerItem(jsonObject2.getString("gid"), jsonObject2.getString("name"), new Point(jsonObject2.getDouble("lat"), jsonObject2.getDouble("lng"))), jsonObject2.getString("scheme_id"), jsonObject2.getString("cid"), jsonObject2.getString("vill_lgdcd"), jsonObject2.getString("vill_nm"), i));
                            }

                            sqLite.EXECUTE(Q.toString());
                            Q.setLength(0);

//                            jsonArray = jsonObject1.getJSONArray("Save_AssetLists");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                jsonObject2 = jsonArray.getJSONObject(i);
//                                if (i % 100 == 0) {
//                                    Log.e("ABCD",i+" A");
//                                    sqLite.EXECUTE(Q.toString());
//                                    Q.setLength(0);
//                                }
//                                Q.append(sqLite.addAssetQuery(new Asset(0, jsonObject2.getString("get_past_point_id"), jsonObject2.getString("scheme_id"), jsonObject2.getString("scheme_name"), jsonObject2.getString("esr_id"), jsonObject2.getString("esr_name"), jsonObject2.getString("get_point_id"), jsonObject2.getString("point_name"), jsonObject2.getString("component_id"), jsonObject2.getString("component_name"), jsonObject2.getString("latitude"), jsonObject2.getString("longitude"), jsonObject2.getString("remark"), jsonObject2.getString("photo_path"), Utility.getMyTimeUsingDate(jsonObject2.getString("captured_datetime")), jsonObject2.getString("depth_cover"), jsonObject2.getString("diameter"), jsonObject2.getString("material"), jsonObject2.optString("alignment", ""), jsonObject2.getString("material_info_name"), jsonObject2.getString("material_info_value"), jsonObject2.getString("line_json"), true), jsonObject2.getString("imei"),i));
//                            }

//                            Log.e("ABCD",Q.toString());
//                            sqLite.EXECUTE(Q.toString());
//                            Q.setLength(0);

                            jsonArray = jsonObject1.getJSONArray("Material_Details");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                Q.append(sqLite.ADD_MATERIAL_TYPE_QUERY(new SpinnerItem(jsonObject2.getString("material_id"), jsonObject2.getString("material_name")), jsonObject2.getString("material_info_name"), i));
                            }

                            sqLite.EXECUTE(Q.toString());
                            Q.setLength(0);

                            jsonArray = jsonObject1.getJSONArray("Depth_Details");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                Q.append(sqLite.ADD_DEPTH_COVER_QUERY(new SpinnerItem(jsonObject2.getString("depth_cover_id"), jsonObject2.getString("depth_cover")), i));
                            }


                            sqLite.EXECUTE(Q.toString());
                            Q = new StringBuilder();

                            jsonArray = jsonObject1.getJSONArray("Diameter_Details");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                Q.append(sqLite.ADD_PIPE_DIAMETER_QUERY(new SpinnerItem(jsonObject2.getString("diameter_id"), jsonObject2.getString("diameter")), jsonObject2.getString("material_id"), i));
                            }


                            sqLite.EXECUTE(Q.toString());
                            Q.setLength(0);

                            jsonArray = jsonObject1.getJSONArray("GetMaterial_InfoDetails");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                Q.append(sqLite.ADD_MATERIAL_INFO_VALUE_QUERY(new SpinnerItem(jsonObject2.getString("material_info_id"), jsonObject2.getString("material_info_value")), jsonObject2.getString("materail_id"), i));
                            }


                            sqLite.EXECUTE(Q.toString());
//                            Q = new StringBuilder();

                            sqLite.updateLastTime(module);

                            handler.post(() -> {
                                try {
                                    alertDialog.dismiss();
                                    onMenu(menuId);
                                    Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                                } catch (Exception e) {
                                    Utility.show(this, e);
                                }
                            });
                        } else {
                            handler.post(() -> {
                                try {
                                    Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                                } catch (Exception e) {
                                    Utility.show(this, e);
                                }
                            });
                        }
                    } catch (Exception e) {
                        handler.post(() -> Utility.show(this, e));
                    }
                    handler.post(() -> {
                        syncAdapter.notifyDataSetChanged();
//                        String ls = SqLite.instance(this).getLastTime(module);
//                        dialogSyncBinding.textViewLastSyncAM.setText(ls == null ? "N/A" : ls);
                        ProgressDialog.stopDialog();
                    });
                });
            }, 1, data.toString(), getString(R.string.please_wait)).start(API.SyncAssignedWork);
        });
    }

    private void syncISA(Module module, int menuId) {
        if (login.getMobile().equals("1234567890")) {
            SqLite.instance(this).updateLastTime(module);
            Utility.show(this, "Sync Done.");
            return;
        }
        closeSyncD();
        alertDialog = new AlertDialog.Builder(this).setTitle(module.name).setMessage(R.string.do_you_really_want_to_sync_data).setNegativeButton(R.string.cancel, (d, v) -> d.dismiss()).setPositiveButton(R.string.ok, null).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            Data data = new Data();
            data.put(Utility.E_TOKEN, login.getToken());//Utility.getDeviceID(this));
            new ApiCaller(this, (response, key) -> {
                ProgressDialog.startDialog(this);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());
                executor.execute(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        SqLite sqLite = SqLite.instance(this);
                        if (jsonObject.getBoolean(Utility.SUCCESS)) {
                            sqLite.CLEAR_ISA_VILLAGE();
                            sqLite.CLEAR_ISA_ACTIVITY();
                            sqLite.resetLastTime(module);

                            JSONObject jsonObject1 = jsonObject.getJSONObject(Utility.DATA);
                            JSONObject jsonObject2;
                            JSONArray jsonArray = jsonObject1.getJSONArray("activity");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                sqLite.ADD_ISA_ACTIVITY(new SpinnerItem(jsonObject2.getString("act_id"), jsonObject2.getString("activity_name")));
                            }
                            jsonArray = jsonObject1.getJSONArray("user_mapping");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject2 = jsonArray.getJSONObject(i);
                                sqLite.ADD_ISA_VILLAGE(new IsaInfo(
                                                jsonObject2.getString("piu_id"),
                                                jsonObject2.getString("piu_name"),
                                                jsonObject2.getString("scheme_id"),
                                                jsonObject2.getString("scheme_name"),
                                                jsonObject2.getString("b_cd"),
                                                jsonObject2.getString("b_nm_e"),
                                                jsonObject2.getString("lgd_gp_cd"),
                                                jsonObject2.getString("lgd_gp_nm_e"),
                                                jsonObject2.getString("vill_cd"),
                                                jsonObject2.getString("vil_nm_e")
                                        )
                                );
                            }
                            sqLite.updateLastTime(module);

                            handler.post(() -> {
                                try {
                                    alertDialog.dismiss();
                                    onMenu(menuId);
                                    Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                                } catch (Exception e) {
                                    Utility.show(this, e);
                                }
                            });
                        } else {
                            handler.post(() -> {
                                try {
                                    Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                                } catch (Exception e) {
                                    Utility.show(this, e);
                                }
                            });
                        }
                    } catch (Exception e) {
                        handler.post(() -> Utility.show(this, e));
                    }
                    handler.post(() -> {
                        syncAdapter.notifyDataSetChanged();
//                        String ls = SqLite.instance(this).getLastTime(module);
//                        dialogSyncBinding.textViewLastSyncISA.setText(ls == null ? "N/A" : ls);
                        ProgressDialog.stopDialog();
                    });
                });
            }, 1, data.toString(), getString(R.string.please_wait)).start(API.ISA.SYNC);
        });
    }

    private void syncDhara(Module module, int menuId) {
        if (login.getMobile().equals("1234567890")) {
            SqLite.instance(this).updateLastTime(module);
            Utility.show(this, "Sync Done.");
            return;
        }
        closeSyncD();
        alertDialog = new AlertDialog.Builder(this).setTitle(module.name).setMessage(R.string.do_you_really_want_to_sync_data).setNegativeButton(R.string.cancel, (d, v) -> d.dismiss()).setPositiveButton(R.string.ok, null).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> Utility.syncDhara(alertDialog, module, this, login, syncAdapter, v1 -> onMenu(menuId)));
    }

    private void syncNirmal(Module module, int menuId) {
        closeSyncD();
        alertDialog = new AlertDialog.Builder(this).setTitle(module.name).setMessage(R.string.do_you_really_want_to_sync_data).setNegativeButton(R.string.cancel, (d, v) -> d.dismiss()).setPositiveButton(R.string.ok, null).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            alertDialog.dismiss();
            ProgressDialog.startDialog(DashboardActivity.this);
            NirmalRepository repository = new NirmalRepository(getBaseContext());
            repository.syncNirmalModuleAsync(module, (isSuccess,message) -> {
                runOnUiThread(() -> {
                    if (isSuccess) {
                        syncAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Module "+module.name+" sync successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                    onMenu(menuId);
                    ProgressDialog.stopDialog();
                });
                return null; // Required for Kotlin-Java interoperability
            });
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermissions();
    }

    private void requestPermissions() {
        if (!checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, 100);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED));
        } else {
            return ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED));
        }
    }
}
