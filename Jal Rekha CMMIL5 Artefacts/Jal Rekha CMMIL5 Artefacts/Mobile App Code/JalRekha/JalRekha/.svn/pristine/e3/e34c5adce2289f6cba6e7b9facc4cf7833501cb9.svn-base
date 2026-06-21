package mapitgis.jalnigam.rfi.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PermissionHelper {

    public static final String FOLDER_NAME = "JalRekha";
    private Activity context;
    private boolean isPermissionAllow = false;

    private boolean isContactPermission = false;

    private Context mContext;

    public PermissionHelper(Activity context) {
        this.context = context;
    }

    public PermissionHelper(Context mContext){
        this.mContext =mContext;
    }

    public boolean checkContactPermission() {
        requestContactPermission();

        return isContactPermission;
    }

    public boolean checkStoragePermission() {

//        requestStoragePermission();

        return isPermissionAllow;

    }

    public void requestCameraPermission(CameraPermissionListener listener) {
        Dexter.withContext(context)
                .withPermissions(
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        listener.onCameraPermissionAllow(report.areAllPermissionsGranted());

                        if(report.areAllPermissionsGranted()){
                            createFolder();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog("This app needs camera permission to use this feature. You can grant them in app settings.");
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                        //  listener.onCameraPermissionAllow(false);
                    }
                }).
                withErrorListener(error -> {
                })
                .onSameThread()
                .check();
    }

    public void requestStoragePermission(StoragePermissionListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            Dexter.withContext(context)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_IMAGES)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {

                                isPermissionAllow = true;

                                createFolder();
                                listener.onStoragePermissionAllow(true);

                            } else {

                                isPermissionAllow = false;
                                listener.onStoragePermissionAllow(false);
                                //  showSettingsDialog();
                            }


                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                isPermissionAllow = false;
                                listener.onStoragePermissionAllow(false);
                                showSettingsDialog("This app needs  permission to use this feature. You can grant them in app settings.");
                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).
                    withErrorListener(error -> {
                    })
                    .onSameThread()
                    .check();
        } else {
            Dexter.withContext(context)
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                listener.onStoragePermissionAllow(true);
                                isPermissionAllow = true;
                                createFolder();
                            } else {

                                isPermissionAllow = false;
                                listener.onStoragePermissionAllow(false);
                                //  showSettingsDialog();
                            }


                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                isPermissionAllow = false;
                                listener.onStoragePermissionAllow(false);
                                //  showSettingsDialog();
                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).
                    withErrorListener(error -> {
                        // Toast.makeText(context, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    })
                    .onSameThread()
                    .check();
        }

    }

    private void requestContactPermission() {
        Dexter.withContext(context)
                .withPermissions(Manifest.permission.READ_CONTACTS)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        //showSettingsDialog();
                        isContactPermission = report.areAllPermissionsGranted();


                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            isContactPermission = false;
                            // showSettingsDialog();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> {
                    // Toast.makeText(context, "Error occurred! ", Toast.LENGTH_SHORT).show();
                })
                .onSameThread()
                .check();
    }

    public void requestLocationPermission(LocationPermissionListener listener) {
        Dexter.withContext(context)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        //   listener.onLocationPermissionAllow(report.areAllPermissionsGranted());

                        if (report.areAllPermissionsGranted()) {

                            if (!isGPSEnabled()) {
                                listener.onLocationPermissionAllow(LocationPermissionEnum.GPS_DISABLED);
                            } else {
                                listener.onLocationPermissionAllow(LocationPermissionEnum.ALLOW);

                            }
                        } else {
                            listener.onLocationPermissionAllow(LocationPermissionEnum.DENY);

                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            listener.onLocationPermissionAllow(LocationPermissionEnum.PERMANENT_DENY);
                            //showSettingsDialog("This app needs location permission to use this feature. You can grant them in app settings.");
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> {
                })
                .onSameThread()
                .check();
    }

    public void createFolder() {

        File pictureFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File folder = new File(pictureFolder, FOLDER_NAME);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    private void showSettingsDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();

    }

    // navigating user to app settings
    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivityForResult(intent, 101);
    }

    // TODO: 02-01-2023 : check internet connection
    public void checkNetworkConnection(CheckInternetListener listener) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        listener.onInternetAvailable(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected());
    }

    public boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } else {
            return false;
        }
    }

    public void enableGPS() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    public String getAddressFromLatLng(double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getAddressLine(0);
                return addressText;
            } else {
                return "No address found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error in address";
        }
    }

    public interface CheckInternetListener {
        void onInternetAvailable(boolean isAvailable);
    }

    public interface CameraPermissionListener {
        void onCameraPermissionAllow(boolean allow);
    }

    public interface LocationPermissionListener {
        void onLocationPermissionAllow(LocationPermissionEnum permission);
    }

    public interface StoragePermissionListener{
        void onStoragePermissionAllow(boolean isAllow);
    }

    public enum LocationPermissionEnum {ALLOW, DENY, PERMANENT_DENY, GPS_DISABLED}
}
