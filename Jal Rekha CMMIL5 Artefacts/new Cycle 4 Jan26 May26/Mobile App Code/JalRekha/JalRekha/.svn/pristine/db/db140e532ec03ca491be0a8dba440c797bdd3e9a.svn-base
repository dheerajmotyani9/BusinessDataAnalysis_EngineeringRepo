package mapitgis.jalnigam.di;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import mapitgis.jalnigam.BaseActivity;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.API;
import mapitgis.jalnigam.core.ApiCaller;
import mapitgis.jalnigam.core.Data;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityDiBinding;

public class DIActivity extends BaseActivity {
    private ActivityDiBinding binding;
    private DIAdapter diAdapter;
    private Login login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        diAdapter = new DIAdapter() {
            @Override
            protected void onDiscard(int pos, @NonNull DI di) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DIActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you really want to discard");
                builder.setNegativeButton(R.string.cancel, (d, v) -> d.dismiss());
                builder.setPositiveButton(R.string.discard, null);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> discard(alertDialog, pos, di));
            }

            @Override
            protected void onUpload(int pos, @NonNull DI di) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DIActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you really want to upload");
                builder.setNegativeButton(R.string.cancel, (d, v) -> d.dismiss());
                builder.setPositiveButton(R.string.upload, null);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> upload(alertDialog, pos, di));
            }
        };
        binding.recyclerView.setAdapter(diAdapter);

        login = SqLite.instance(this).getLogin();

        binding.cardViewButtonSync.setOnClickListener(view -> syncDIInfo());
        binding.buttonAdd.setOnClickListener(view -> Utility.open(this, AddDIActivity.class));
    }


    private void syncDIInfo() {
        Data data = new Data();
        data.put("etokens", login.getToken());
        new ApiCaller(this, (response, key) -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean(Utility.SUCCESS)) {
                    SqLite.instance(this).CLEAR_UPLOADED_DI_NEW();
                    JSONArray jsonArray = jsonObject.getJSONArray(Utility.DATA);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String[] a = jsonObject1.getString("image_base64").split(",");
                        List<String> images = new ArrayList<>(Arrays.asList(a));

                        SqLite.instance(this).addDailyInspection(
                                true,
                                jsonObject1.getString("village_name"),
                                jsonObject1.getString("gp_name"),
                                jsonObject1.getString("block_name"),
                                jsonObject1.getString("description"),
                                jsonObject1.getString("qa_qc_remark"),
                                new LatLng(jsonObject1.getDouble("latitude"), jsonObject1.getDouble("longitude")),
                                jsonObject1.getString("address"),
                                new SpinnerItem(jsonObject1.getString("piu_id"), jsonObject1.getString("piu_name")),
                                new SpinnerItem(jsonObject1.getString("scheme_id"), jsonObject1.getString("scheme_name")),
                                new SpinnerItem(jsonObject1.getString("component_id"), jsonObject1.getString("component_name")),
                                new SpinnerItem(jsonObject1.getString("application_type_id"), ""),
                                new SpinnerItem(jsonObject1.getString("qa_qc_review"), SqLite.instance(this).GET_REVIEW_DI_NEW(jsonObject1.getString("qa_qc_review"))),
                                images,
                                this
                        );
                    }
                    Utility.show(this, "Successfully sync");
                } else {
                    Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                }
            } catch (Exception e) {
                Utility.show(this, e);
            }
            getList();
        }, 1, data.toString(), getString(R.string.please_wait)).start(API.DI.LIST);
    }

    private void discard(@NonNull AlertDialog alertDialog, int pos, @NonNull DI di) {
        diAdapter.remove(pos);
        SqLite.instance(this).discardDI(di.getId(), this);
        alertDialog.dismiss();
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Successfully discard data");
        dlgAlert.setTitle(R.string.success);
        dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        checkEmpty();
    }

    private void upload(@NonNull AlertDialog alertDialog, int pos, @NonNull DI di) {
        try {
            SqLite.instance(this).getDIObject(login.getToken(), di.getId(), this, (success, data) -> {
                if (!success || data == null) {
                    Utility.show(this, "Something is wrong.");
                    return;
                }
                new ApiCaller(this, (response, key) -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean(Utility.SUCCESS)) {
                            diAdapter.updateUploaded(pos);
                            SqLite.instance(this).setDIUploaded(di.getId());
                            alertDialog.dismiss();
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                            dlgAlert.setMessage(jsonObject.getString(Utility.MESSAGE));
                            dlgAlert.setTitle(R.string.success);
                            dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                        } else {
                            Utility.show(this, jsonObject.getString(Utility.MESSAGE));
                        }
                    } catch (Exception e) {
                        Utility.show(this, e);
                    }
                }, 1, data.toString(), getString(R.string.please_wait)).start(API.DI.SAVE);
            });
        } catch (Exception e) {
            Utility.show(this, e);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    private void getList() {
        diAdapter.clear();
        diAdapter.addAll(SqLite.instance(this).getDailyInspection());
        checkEmpty();
    }

    private void checkEmpty() {
        if (diAdapter.isEmpty()) {
            binding.linearLayoutEmpty.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.linearLayoutEmpty.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * @noinspection deprecation
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
