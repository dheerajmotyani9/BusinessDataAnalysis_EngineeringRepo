package mapitgis.jalnigam.isa;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import mapitgis.jalnigam.BaseActivity;
import mapitgis.jalnigam.ImgActivity;
import mapitgis.jalnigam.MultiImgActivity;
import mapitgis.jalnigam.PdfActivity;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.API;
import mapitgis.jalnigam.core.ApiCaller;
import mapitgis.jalnigam.core.Data;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityIsaBinding;

public class ISAActivity extends BaseActivity {
    private ActivityIsaBinding binding;
    private ISAAdapter isaAdapter;
    private Login login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIsaBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        isaAdapter = new ISAAdapter() {
            @Override
            protected void onDiscard(int pos, @NonNull ISA isa) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ISAActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you really want to discard");
                builder.setNegativeButton(R.string.cancel, (d, v) -> d.dismiss());
                builder.setPositiveButton(R.string.discard, null);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> discard(alertDialog, pos, isa));
            }

            @Override
            protected void onUpload(int pos, @NonNull ISA isa) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ISAActivity.this);
                builder.setTitle("Are you sure?");
                builder.setMessage("Do you really want to upload");
                builder.setNegativeButton(R.string.cancel, (d, v) -> d.dismiss());
                builder.setPositiveButton(R.string.upload, null);
                builder.setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> upload(alertDialog, pos, isa));
            }

            @Override
            protected void onView(int pos, @NonNull ISA isa, int type) {
                show(type, isa);
            }
        };
        binding.recyclerView.setAdapter(isaAdapter);

        login = SqLite.instance(this).getLogin();

        binding.buttonAdd.setOnClickListener(view -> Utility.open(this, AddISAActivity.class));
//        binding.cardViewButtonAddNew.setOnClickListener(view -> Utility.open(this,AddISAActivity.class));
    }

    private void show(int type, @NonNull ISA isa) {
        if (type == 1) {
            try {
                SqLite.instance(this).getISABase64(isa.getId(), SqLite.PHOTO_FLAG.IMG, this, (success, jsonArray, base64) -> {
                    if (success && jsonArray != null) {
                        if (jsonArray.length() == 1) {
                            ImgActivity.openImage(this, jsonArray.optString(0, ""), getString(R.string.photo), 1);
                        } else {
                            MultiImgActivity.showImages(this, jsonArray, getString(R.string.photo), 1);
                        }
                    } else {
                        Utility.show(this, "Image not found");
                    }
                });
//                if (jsonArray != null && jsonArray.length() > 0) {
//                    if (jsonArray.length() == 1) {
//                        ImgActivity.openImage(this, jsonArray.optString(0, ""), getString(R.string.photo), 1);
//                    } else {
//                        MultiImgActivity.showImages(this, jsonArray, getString(R.string.photo), 1);
//                    }
//                } else {
//                    Utility.show(this, "Image not found");
//                }
            } catch (Exception e) {
                Utility.show(this, e);
            }
        } else {
            try {
                SqLite.instance(this).getISABase64(isa.getId(), SqLite.PHOTO_FLAG.DOC, type - 2, this, (success, jsonArray, base64) -> {
                    if (success && base64 != null) {
                        PdfActivity.openPDF(this, base64, getString(R.string.document), 1);
//                PdfActivity.openPDF(this, base64, getString(type == 2 ? R.string.document_1 : R.string.document_2), 1);
                    } else {
                        Utility.show(this, "PDF not found");
                    }
                });
            } catch (Exception e) {
                Utility.show(this, e);
            }
        }
    }

    private void discard(@NonNull AlertDialog alertDialog, int pos, @NonNull ISA isa) {
        isaAdapter.remove(pos);
        SqLite.instance(this).discardISA(isa.getId(), this);
        alertDialog.dismiss();
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage("Successfully discard data");
        dlgAlert.setTitle(R.string.success);
        dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
        checkEmpty();
    }

    private void upload(@NonNull AlertDialog alertDialog, int pos, @NonNull ISA isa) {
        try {
            SqLite.instance(this).GET_ISA(login.getToken(), isa.getId(), this, (success, data) -> {
                if (!success || data == null) {
                    Utility.show(this, "Something is wrong.");
                    return;
                }

                new ApiCaller(this, (response, key) -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean(Utility.SUCCESS)) {
                            SqLite.instance(this).discardISA(isa.getId(),this);
                            isaAdapter.remove(pos);
                            checkEmpty();
//                    isaAdapter.updateUploaded(pos);
                            SqLite.instance(this).setISAUploaded(isa.getId());
                            alertDialog.dismiss();
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                            dlgAlert.setMessage(R.string.uploaded_successfully);
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
                }, 1, data.toString(), getString(R.string.please_wait)).start(API.ISA.SAVE);
            });
        } catch (Exception e) {
            Utility.show(this, e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isaAdapter.clear();
        isaAdapter.addAll(SqLite.instance(this).GET_ISA());
        checkEmpty();
    }

    private void checkEmpty() {
        if (isaAdapter.isEmpty()) {
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