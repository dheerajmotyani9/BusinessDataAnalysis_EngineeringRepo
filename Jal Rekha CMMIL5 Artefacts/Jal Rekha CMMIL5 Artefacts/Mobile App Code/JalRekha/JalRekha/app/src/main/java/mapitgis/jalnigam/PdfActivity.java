package mapitgis.jalnigam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.InputStream;
import java.util.Objects;

import mapitgis.jalnigam.core.ApiCaller;
import mapitgis.jalnigam.core.ApiListener;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityPdfBinding;

public class PdfActivity extends BaseActivity {
    private ActivityPdfBinding binding;
    private static String url;

    public static void openPDF(@NonNull Activity activity, @NonNull String url, String name,int type) {
        Intent intent = new Intent(activity,PdfActivity.class);
        PdfActivity.url = url;
//        intent.putExtra(Utility.G_KEY,url);
        intent.putExtra(Utility.G_KEY1,name);
        intent.putExtra(Utility.SF,type);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar(binding.appbar.toolbar,true);

//        String url = Objects.requireNonNull(getIntent().getStringExtra(Utility.G_KEY));
        String name = Objects.requireNonNull(getIntent().getStringExtra(Utility.G_KEY1));
        int type = getIntent().getIntExtra(Utility.SF,0);

        setTitle(name);

        binding.dialog.textView.setTextColor(Color.BLACK);
        binding.dialog.getRoot().setVisibility(View.VISIBLE);
        if(type == 1){
            //Base64;
            binding.pdfView.fromBytes(Base64.decode(url, Base64.DEFAULT))
                    .defaultPage(0)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .pageFling(true)
                    .pageSnap(true)
                    .autoSpacing(true)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .onLoad(nbPages -> binding.dialog.getRoot().setVisibility(View.GONE))
                    .load();
        }else {
            //URL;
            new ApiCaller(this, new ApiListener() {
                @Override
                public void onResponse(@NonNull String response, int key) {

                }

                @Override
                public void onInputStream(@NonNull InputStream inputStream) {
                    binding.dialog.getRoot().setVisibility(View.VISIBLE);
                    binding.pdfView.fromStream(inputStream)
                            .defaultPage(0)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .pageFling(true)
                            .pageSnap(true)
                            .autoSpacing(true)
                            .pageFitPolicy(FitPolicy.WIDTH)
                            .onLoad(nbPages -> binding.dialog.getRoot().setVisibility(View.GONE))
                            .load();
                }

                @Override
                public boolean getInputStream() {
                    return true;
                }
            }, 1, null, null).start(url);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_pdf, menu);
//        MenuItem itemDownload = menu.findItem(R.id.itemDownload);
//        itemDownload.setVisible(true);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.itemDownload) {
//            downloadStart();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    private void downloadStart() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            startDownload();
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                startDownload();
//            } else {
//                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    showPermissionExplanationDialog();
//                } else {
//                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
//                }
//            }
//        } else {
//            startDownload();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void showPermissionExplanationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Permission Needed");
//        builder.setMessage("This permission is required to save the downloaded file to your device.");
//        builder.setPositiveButton("OK", (dialog, which) -> requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE));
//        builder.setNegativeButton("Cancel", null);
//        builder.show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, start the download
//                startDownload();
//            } else {
//                // Permission denied, show a message or take appropriate action
//                Utility.show(this, "Permission denied. Cannot initiate download.");
//            }
//        }
//    }
//
//    private void startDownload() {
//        Utility.download(this, url, name, null);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
