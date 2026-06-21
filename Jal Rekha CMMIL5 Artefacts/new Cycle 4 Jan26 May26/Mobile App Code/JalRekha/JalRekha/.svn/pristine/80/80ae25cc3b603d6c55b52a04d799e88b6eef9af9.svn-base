package mapitgis.jalnigam.dhara1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Objects;

import mapitgis.jalnigam.BaseActivity;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SpinnerManager;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityDharaBinding;

public class DharaActivity extends BaseActivity {
    private ActivityDharaBinding binding;
    private DharaComponentAdapter dharaComponentAdapter;
    private Login login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDharaBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        dharaComponentAdapter = new DharaComponentAdapter(this) {
//            @Override
//            protected boolean isCurrentDay() {
//                return binding.radio1.isChecked();
//            }

            //            @Override
//            protected void onDiscard(int pos, @NonNull ISA isa) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(DharaActivity.this);
//                builder.setTitle("Are you sure?");
//                builder.setMessage("Do you really want to discard");
//                builder.setNegativeButton(R.string.cancel, (d, v) -> d.dismiss());
//                builder.setPositiveButton(R.string.discard, null);
//                builder.setCancelable(false);
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> discard(alertDialog, pos, isa));
//            }
//
//            @Override
//            protected void onUpload(int pos, @NonNull ISA isa) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(DharaActivity.this);
//                builder.setTitle("Are you sure?");
//                builder.setMessage("Do you really want to upload");
//                builder.setNegativeButton(R.string.cancel, (d, v) -> d.dismiss());
//                builder.setPositiveButton(R.string.upload, null);
//                builder.setCancelable(false);
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> upload(alertDialog, pos, isa));
//            }

            @Override
            protected void onView(int pos, @NonNull DharaComponent dharaComponent) {
                if (dharaComponent.isFilled()) {
                    needRefresh = true;
                    Intent intent = new Intent(DharaActivity.this, AddReadingActivity.class);
                    intent.putExtra(Utility.G_KEY, dharaComponent.getId());
                    intent.putExtra("METER_ID", dharaComponent.getMeterId());
                    intent.putExtra(Utility.G_KEY1, binding.radio1.isChecked());
                    startActivity(intent);
                } else {
                    if(binding.radio1.isChecked() && !dharaComponent.isNextPreviousFilled()){
                        new AlertDialog.Builder(DharaActivity.this)
                                .setTitle("Are you sure?")
                                .setMessage("पिछली रीडिंग दर्ज नहीं की गई है। वर्तमान रीडिंग दर्ज करने के बाद पिछली रीडिंग नहीं भरेगी। क्या आप फिर भी जारी रखना चाहते हैं?")
                                .setNegativeButton(R.string.close,(dialog, which) -> dialog.dismiss())
                                .setPositiveButton(R.string.ok,(dialog1, which) -> {
                                    dialog1.dismiss();
                                    needRefresh = true;
                                    Intent intent = new Intent(DharaActivity.this, AddReadingActivity.class);
                                    intent.putExtra(Utility.G_KEY, dharaComponent.getId());
                                    intent.putExtra("METER_ID", dharaComponent.getMeterId());
                                    intent.putExtra(Utility.G_KEY1, binding.radio1.isChecked());
                                    startActivity(intent);
                                })
                                .show();
//                        alertForPreviousNotFilled();
                    }else if(binding.radio2.isChecked() && dharaComponent.isNextPreviousFilled()){
                        new AlertDialog.Builder(DharaActivity.this)
                                .setTitle("Are you sure?")
                                .setMessage("आप पिछली रीडिंग दर्ज नहीं कर सकते क्योंकि वर्तमान रीडिंग पहले ही भरी जा चुकी है।")
                                .setPositiveButton(R.string.ok,(dialog, which) -> dialog.dismiss())
                                .show();
//                        alertForTodayFilledYouCannotFilledOld();
                    }else{
                        needRefresh = true;
                        Intent intent = new Intent(DharaActivity.this, AddReadingActivity.class);
                        intent.putExtra(Utility.G_KEY, dharaComponent.getId());
                        intent.putExtra("METER_ID", dharaComponent.getMeterId());
                        intent.putExtra(Utility.G_KEY1, binding.radio1.isChecked());
                        startActivity(intent);
                    }
                }
            }
        };

        @SuppressLint("NotifyDataSetChanged")
        View.OnClickListener ocl = view -> refresh();
        binding.radio1.setOnClickListener(ocl);
        binding.radio2.setOnClickListener(ocl);

        binding.recyclerView.setAdapter(dharaComponentAdapter);

        login = SqLite.instance(this).getLogin();

        SpinnerManager spinnerManager = new SpinnerManager(binding.linearLayoutScheme, 1, this, SqLite.instance(this).GET_DHARA_SCHEME());
        if (!spinnerManager.getSpinnerItems().isEmpty()) {
            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
        } else {
            setEmpty(binding.linearLayoutScheme);
        }

//        binding.buttonAdd.setOnClickListener(view -> Utility.open(this, AddISAActivity.class));
//        binding.cardViewButtonAddNew.setOnClickListener(view -> Utility.open(this,AddISAActivity.class));
    }

//    private void discard(@NonNull AlertDialog alertDialog, int pos, @NonNull ISA isa) {
//        isaAdapter.remove(pos);
//        SqLite.instance(this).discardISA(isa.getId(), this);
//        alertDialog.dismiss();
//        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//        dlgAlert.setMessage("Successfully discard data");
//        dlgAlert.setTitle(R.string.success);
//        dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
//        dlgAlert.setCancelable(true);
//        dlgAlert.create().show();
//        checkEmpty();
//    }
//
//    private void upload(@NonNull AlertDialog alertDialog, int pos, @NonNull ISA isa) {
//        try {
//            SqLite.instance(this).GET_ISA(login.getToken(), isa.getId(), this, (success, data) -> {
//                if (!success || data == null) {
//                    Utility.show(this, "Something is wrong.");
//                    return;
//                }
//
//                new ApiCaller(this, (response, key) -> {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        if (jsonObject.getBoolean(Utility.SUCCESS)) {
//                            SqLite.instance(this).discardISA(isa.getId(),this);
//                            isaAdapter.remove(pos);
//                            checkEmpty();
////                    isaAdapter.updateUploaded(pos);
//                            SqLite.instance(this).setISAUploaded(isa.getId());
//                            alertDialog.dismiss();
//                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//                            dlgAlert.setMessage(R.string.uploaded_successfully);
//                            dlgAlert.setTitle(R.string.success);
//                            dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
//                            dlgAlert.setCancelable(true);
//                            dlgAlert.create().show();
//                        } else {
//                            Utility.show(this, jsonObject.getString(Utility.MESSAGE));
//                        }
//                    } catch (Exception e) {
//                        Utility.show(this, e);
//                    }
//                }, 1, data.toString(), getString(R.string.please_wait)).start(API.ISA.SAVE);
//            });
//        } catch (Exception e) {
//            Utility.show(this, e);
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        if (needRefresh) {
            refresh();
            needRefresh = false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    boolean needRefresh;

    /**
     * @noinspection deprecation
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null && resultCode == RESULT_OK) {
                set(data.getIntExtra("requestCode", 0), (SpinnerItem) data.getSerializableExtra("spinnerItem"));
            }
        }
    }

    private void set(int code, SpinnerItem spinnerItem) {
//        SpinnerManager spinnerManager;
        if (code == 1) {
            ((TextView) binding.linearLayoutScheme.getChildAt(0)).setText(spinnerItem.getValue());
            binding.linearLayoutScheme.setTag(spinnerItem);
            refresh();
        }
    }

    private void refresh() {
        Log.e("ABCDCC", "R-NEW");
        if (binding.linearLayoutScheme.getTag() == null) {
            return;
        }
        SpinnerItem spinnerItem = (SpinnerItem) binding.linearLayoutScheme.getTag();
        dharaComponentAdapter.clear();
        dharaComponentAdapter.addAll(SqLite.instance(this).GET_DHARA_COMPONENT(spinnerItem.getKeyString(), binding.radio1.isChecked()));
    }

    private void setEmpty(@NonNull LinearLayout linearLayout) {
        ((TextView) linearLayout.getChildAt(0)).setText(R.string.select);
        linearLayout.setTag(null);
    }
}