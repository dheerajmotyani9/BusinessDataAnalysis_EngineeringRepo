package mapitgis.jalnigam;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.rfi.helper.MyApplication;
import mapitgis.jalnigam.rfi.helper.PermissionHelper;
import mapitgis.jalnigam.rfi.helper.PrefManager;
import mapitgis.jalnigam.rfi.helper.ProgressHelper;
import mapitgis.jalnigam.rfi.model.AppVersion;
import mapitgis.jalnigam.network.ApiClient;
import mapitgis.jalnigam.network.ApiInterface;
import mapitgis.jalnigam.rfi.receiver.TimeChangeReceiver;
import mapitgis.jalnigamk.jalsamadhaan.screens.selectuser.SelectUserTypeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity implements PermissionHelper.StoragePermissionListener {

    private PermissionHelper permissionHelper;
    private ProgressHelper progressHelper;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        } catch (Exception ignored) {
        }
        setContentView(R.layout.activity_welcome);
        TextView textViewVersion = findViewById(R.id.textViewVersion);
        textViewVersion.setText(String.format("Version %s", BuildConfig.VERSION_NAME));

        apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
        permissionHelper = new PermissionHelper(this);
        permissionHelper.requestStoragePermission(this);
        progressHelper = new ProgressHelper(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(new TimeChangeReceiver(), filter);

    }

    private void gotoHome() {
        //Utility.goFirst(this);
        startActivity(new Intent(this, SelectUserTypeActivity.class));
        finish();
    }

    @Override
    public void onStoragePermissionAllow(boolean isAllow) {
//        if (BuildConfig.DEBUG) {
//            gotoHome();
//        } else {
        checkAppVersion();
//        }
        if (isAllow) {
            new MyApplication().onCreate();
        }
    }

    // TODO: 30-08-2024 : check app update or not.
    private void checkAppVersion() {
        int versionCode = BuildConfig.VERSION_CODE;

        Map<String, Integer> body = new HashMap<>();
        body.put("version", versionCode);
        Call<AppVersion> call = apiInterface.appVersion(body);

        call.enqueue(new Callback<AppVersion>() {
            @Override
            public void onResponse(Call<AppVersion> call, Response<AppVersion> response) {
                if (response.body() != null && response.body().isSuccess()) {

                    if (response.body().getData().isMandate()) {
                        showMandatoryDialog(response.body().getData().getMessage());
                    } else {
                        showQuestionDialog(response.body().getData().getMessage());
                    }
                } else {
                    gotoHome();
                }
            }

            @Override
            public void onFailure(Call<AppVersion> call, Throwable t) {
                gotoHome();
            }
        });
    }

    private void showQuestionDialog(String message) {
        progressHelper.showQuestionDialog("App Update", message, "Update", "Skip", new ProgressHelper.QuestionDialogListener() {
            @Override
            public void onQuestionDialog() {
                update();
            }

            @Override
            public void onCancelQuestionDialog() {
                gotoHome();
            }
        });
    }

    private void showMandatoryDialog(String message) {
        progressHelper.showSuccessDialog(message, "App Update", "back", "Update App", type -> update());
    }

    private void update() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
