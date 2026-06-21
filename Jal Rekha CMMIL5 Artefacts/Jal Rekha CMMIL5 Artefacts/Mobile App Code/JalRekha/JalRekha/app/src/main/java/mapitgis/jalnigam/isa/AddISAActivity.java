package mapitgis.jalnigam.isa;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.softozin.time.DateUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import mapitgis.jalnigam.BaseActivity;
import mapitgis.jalnigam.BuildConfig;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SpinnerManager;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityAddIsaBinding;
import mapitgis.jalnigam.di.ImageAdapter;
import mapitgis.jalnigam.wms.TileProviderFactory;

public class AddISAActivity extends BaseActivity {
    private ActivityAddIsaBinding binding;
    private LatLng latLng;
    private Geocoder geocoder;
    private File photoFile;
    private ImageAdapter imageAdapter;
    private String address;
    private Login login;
    private GoogleMap googleMap;

//    private int doc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddIsaBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appbar.toolbar);//findViewById(R.id.toolbar)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        login = SqLite.instance(this).getLogin();
        imageAdapter = new ImageAdapter(this,new ArrayList<>());
        binding.recyclerViewImages.setAdapter(imageAdapter);

        geocoder = new Geocoder(this, Locale.getDefault());
        binding.mapView.setVisibility(View.VISIBLE);
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.onResume();// needed to get the map to display immediately
        binding.mapView.getMapAsync(googleMap -> {

            this.googleMap = googleMap;

            LatLng latLng = new LatLng(20.5937, 78.9629);
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(latLng, 5);
            googleMap.moveCamera(cameraPosition);
            googleMap.animateCamera(cameraPosition);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.473324, 77.947998), 6));

            TileProvider tileProvider = TileProviderFactory.getTileProvider(TileProviderFactory.LAYER_DIST);
            this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));


            this.googleMap.setOnCameraMoveListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));
            this.googleMap.setOnCameraIdleListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(false));
        });

        binding.imageViewCurrentLocation.setVisibility(View.VISIBLE);
        binding.imageViewCurrentLocation.setOnClickListener(v -> {
            if (mFusedLocationClient == null) {
//                show_message = true;
                binding.textViewLocation.setText("");
                latLng = null;
                RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1000);
                rotate.setInterpolator(new LinearInterpolator());
                rotate.setRepeatCount(Animation.INFINITE);
                rotate.setRepeatMode(Animation.ABSOLUTE);
                binding.imageViewCurrentLocation.startAnimation(rotate);
                buildGoogleApiClient();
            }
        });

        IsaInfo info = SqLite.instance(this).getISAInfo();
        if (info != null) {
            binding.textViewPIU.setText(info.getPiu());
            binding.textViewScheme.setText(info.getScheme());
            binding.textViewBlock.setText(info.getBlock());
            binding.textViewTodayDate.setText(Utility.getCurrentTimeSync());
        } else {
            Utility.show(this, "No Village assign to you.");
            finish();
        }
//        SpinnerManager spinnerManager = new SpinnerManager(binding.linearLayoutPIU, 1, this, SqLite.instance(this).getISAPiu());
//        if (spinnerManager.getSpinnerItems().size() == 1) {
//            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//        } else {
//            setEmpty(binding.linearLayoutPIU);
//        }


        SpinnerManager spinnerManager = new SpinnerManager(binding.linearLayoutGP, 1, this, SqLite.instance(this).getISAGp());
        if (spinnerManager.getSpinnerItems().size() == 1) {
            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
        } else {
            setEmpty(binding.linearLayoutGP);
        }

        spinnerManager = new SpinnerManager(binding.linearLayoutActivity, 3, this, SqLite.instance(this).GET_ISA_ACTIVITY());
        if (spinnerManager.getSpinnerItems().size() == 1) {
            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
        } else {
            setEmpty(binding.linearLayoutActivity);
        }


        binding.buttonSave.setOnClickListener(view -> {
            SpinnerItem gp, village, activity;
            String remark;

//            if (binding.linearLayoutPIU.getTag() == null) {
//                Utility.show(this, Utility.select(this, R.string.piu_name));
//                return;
//            }
//            piu = ((SpinnerItem) binding.linearLayoutPIU.getTag());
//            if (binding.linearLayoutScheme.getTag() == null) {
//                Utility.show(this, Utility.select(this, R.string.scheme));
//                return;
//            }
//            scheme = ((SpinnerItem) binding.linearLayoutScheme.getTag());
//
//            if (binding.linearLayoutBlock.getTag() == null) {
//                Utility.show(this, Utility.select(this, R.string.block));
//                return;
//            }
//            block = ((SpinnerItem) binding.linearLayoutBlock.getTag());

            if (binding.linearLayoutGP.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.gram_panchayat));
                return;
            }
            gp = ((SpinnerItem) binding.linearLayoutGP.getTag());

            if (binding.linearLayoutVillage.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.village));
                return;
            }
            village = ((SpinnerItem) binding.linearLayoutVillage.getTag());

            if (binding.linearLayoutActivity.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.activity));
                return;
            }
            activity = ((SpinnerItem) binding.linearLayoutActivity.getTag());


            if(!binding.radio1.isChecked() && !binding.radio2.isChecked()){
                Utility.show(this, Utility.select(this, R.string.type_of_activity));
                return;
            }


            List<String> docList = new ArrayList<>();
            if (binding.linearLayoutDocument.getTag() != null) {
                docList.add(binding.linearLayoutDocument.getTag().toString());
            }

            if (binding.editTextDate.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.date_of_activity));
                return;
            }
            String doa = binding.editTextDate.getTag().toString();

            if(binding.radio2.isChecked()){
                if(docList.isEmpty()){
                    Utility.show(this, Utility.select(this, R.string.document));
                    return;
                }
            }

//            if (binding.linearLayoutDocument2.getTag() != null) {
//                docList.add(binding.linearLayoutDocument2.getTag().toString());
//            }

//            List<String> imgList = new ArrayList<>();
//            if (binding.imageViewPhoto.getTag() == null) {
//                Utility.show(this, Utility.select(this, R.string.photo));
//                return;
//            }

            if(binding.radio1.isChecked()) {
                if (imageAdapter.getItemCount() == 0) {
                    Utility.show(this, "Minimum 1 photo required.");
                    return;
                }
            }
            List<String> images = new ArrayList<>();
            for (int i = 0; i < imageAdapter.getItemCount(); i++) {
                Bitmap bitmap = imageAdapter.getItem(i);
                images.add(Utility.base64(bitmap));
            }
//            imgList.add(binding.imageViewPhoto.getTag().toString());

            remark = binding.editTextRemark.getText().toString();
            if (remark.isEmpty()) {
                Utility.show(this, Utility.check(this, R.string.remark));
                return;
            }

            if (latLng == null) {
                Utility.show(this, Utility.check(this, R.string.location));
                buildGoogleApiClient();
                return;
            }

            try {
                SqLite.instance(this).ADD_ISA(new ISA(
                        false,
                        gp.getKeyString(),
                        gp.getValue(),
                        village.getKeyString(),
                        village.getValue(),
                        activity.getKeyString(),
                        activity.getValue(),
                        remark,
                        doa,
                        Utility.getCurrentTimeUsingDate(),
                        latLng.latitude,
                        latLng.longitude,
                        address,
                        false,
                        false
                ),docList,images,binding.radio2.isChecked(), this);
                androidx.appcompat.app.AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage(R.string.saved_successfully);
                dlgAlert.setTitle(R.string.success);
                dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> finish());
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            } catch (Exception e) {
                Utility.show(this, e);
            }

//            Data data = new Data();
//            data.put("gp_code", gp.getKeyString());
//            data.put("vill_cd", village.getKeyString());
//            data.put("activity_id", activity.getKeyString());
//            JSONArray jsonArray = new JSONArray();
//            jsonArray.put(document);
//            data.put("doc_path", jsonArray);
//            data.put("doc_ext", "jpg");
//            jsonArray = new JSONArray();
//            jsonArray.put(photo);
//            data.put("photo_path", jsonArray);
//            data.put("remark", review);
//            data.put(Utility.E_TOKEN, login.getToken());
////            data.put("insert_userid", login.getId());
//            data.put("capture_dt", Utility.getCurrentTimeUsingDate());
//
//            new ApiCaller(this,(response, key) -> {
//                try{
//                    JSONObject jsonObject = new JSONObject(response);
//                    Utility.show(this,jsonObject.getString(Utility.MESSAGE));
//                    if(jsonObject.getBoolean(Utility.SUCCESS)){
//                        finish();
//                    }
//                }catch (Exception e){
//                    Utility.show(this,e);
//                }
//            },1,data.toString(),getString(R.string.please_wait)).start(API.ISA.SAVE_ACTIVITY);
        });


        ActivityResultLauncher<Intent> activityResultPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {// && null != result.getData()
                try {
                    // Decode bitmap from the photo file
//                    Bitmap originalBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//
//                    // Rotate bitmap if needed
//                    ExifInterface exifInterface = new ExifInterface(photoFile.getAbsolutePath());
//                    int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//                    Bitmap rotatedBitmap = rotateBitmapIfNeeded(originalBitmap, orientation);
//
//                    // Resize the rotated bitmap
//                    bitmap = Utility.getResizedBitmap(rotatedBitmap);

                    bitmap = Utility.getImageRotation(photoFile);


//                    Bundle extras = result.getData().getExtras();
//                    Bitmap bitmap = (Bitmap) extras.get("data");
//                    bitmap = Utility.getResizedBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
                    //noinspection ResultOfMethodCallIgnored
                    photoFile.delete();
                    fetchPhoto = true;
                    startDialog();
                    buildGoogleApiClient();
                } catch (Exception e) {
                    Utility.show(this, e);
                }
            } else {
                Utility.show(this, Utility.select(this, R.string.photo));
            }
        });

        binding.buttonAddImage.setOnClickListener(v -> {
            if(imageAdapter.getItemCount() >= 5){
                Utility.show(this, "You can upload maximum 5 Photo");
                return;
            }
            Intent intent = Utility.getIntentForCameraFacing(false);
            photoFile = Utility.createImageFile(this);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, String.format("%s.fileProvider", BuildConfig.APPLICATION_ID), photoFile));
            activityResultPhoto.launch(intent);
        });


        ActivityResultLauncher<Intent> activityResultDocument = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && null != result.getData()) {
                try {
                    Uri pdfUri = result.getData().getData();
                    if (pdfUri != null) {
                        InputStream inputStream = getContentResolver().openInputStream(pdfUri);
                        if (inputStream != null) {
                            byte[] pdfBytes = readBytes(inputStream);
                            inputStream.close();
                            String base64Pdf = Base64.encodeToString(pdfBytes, Base64.DEFAULT);
                            boolean isLessThan5MB = Utility.isBase64PdfLessThan5MB(base64Pdf);
                            if(isLessThan5MB) {
//                                if (doc == 1) {
                                    ((TextView) binding.linearLayoutDocument.getChildAt(0)).setText(Utility.getFileNameFromUri(this,pdfUri));
                                    binding.linearLayoutDocument.setTag(base64Pdf);
//                                } else {
//                                    ((TextView) binding.linearLayoutDocument2.getChildAt(0)).setText(Utility.getFileNameFromUri(this,pdfUri));
//                                    binding.linearLayoutDocument2.setTag(base64Pdf);
//                                }
                            }else{
                                new AlertDialog.Builder(this)
                                        .setTitle("PDF is greater than 5 MB")
                                        .setMessage(Utility.html("PDF must be less than or equal to 5MB.<br/>Please compress pdf from below link<br/><br/><font color='blue'>https://www.ilovepdf.com/compress_pdf</font>"))
                                        .setNegativeButton(R.string.close,(dialog1, which) -> dialog1.dismiss())
                                        .setPositiveButton("Open URL",(dialog1, which) -> Utility.address("https://www.ilovepdf.com/compress_pdf",this))
                                        .show();
//                                Utility.show(this, "PDF must be less than or equal to 5MB");
//                                if(doc == 1) {
                                    ((TextView) binding.linearLayoutDocument.getChildAt(0)).setText(R.string.please_capture_pdf);
                                    binding.linearLayoutDocument.setTag(null);
//                                }else{
//                                    ((TextView) binding.linearLayoutDocument2.getChildAt(0)).setText(R.string.please_capture_pdf);
//                                    binding.linearLayoutDocument2.setTag(null);
//                                }
                            }
                        } else {
                            Utility.show(this, Utility.select(this, R.string.document));
//                            if(doc == 1) {
                                ((TextView) binding.linearLayoutDocument.getChildAt(0)).setText(R.string.please_capture_pdf);
                                binding.linearLayoutDocument.setTag(null);
//                            }else{
//                                ((TextView) binding.linearLayoutDocument2.getChildAt(0)).setText(R.string.please_capture_pdf);
//                                binding.linearLayoutDocument2.setTag(null);
//                            }
                        }
                    } else {
                        Utility.show(this, Utility.select(this, R.string.document));
//                        if(doc == 1) {
                            ((TextView) binding.linearLayoutDocument.getChildAt(0)).setText(R.string.please_capture_pdf);
                            binding.linearLayoutDocument.setTag(null);
//                        }else{
//                            ((TextView) binding.linearLayoutDocument2.getChildAt(0)).setText(R.string.please_capture_pdf);
//                            binding.linearLayoutDocument2.setTag(null);
//                        }
                    }
                } catch (Exception e) {
                    Utility.show(this, e);
//                    if(doc == 1) {
                        ((TextView) binding.linearLayoutDocument.getChildAt(0)).setText(R.string.please_capture_pdf);
                        binding.linearLayoutDocument.setTag(null);
//                    }else{
//                        ((TextView) binding.linearLayoutDocument2.getChildAt(0)).setText(R.string.please_capture_pdf);
//                        binding.linearLayoutDocument2.setTag(null);
//                    }
                }
            } else {
                Utility.show(this, Utility.select(this, R.string.document));
//                if(doc == 1) {
                    ((TextView) binding.linearLayoutDocument.getChildAt(0)).setText(R.string.please_capture_pdf);
                    binding.linearLayoutDocument.setTag(null);
//                }else{
//                    ((TextView) binding.linearLayoutDocument2.getChildAt(0)).setText(R.string.please_capture_pdf);
//                    binding.linearLayoutDocument2.setTag(null);
//                }
            }
        });

        binding.linearLayoutDocument.setOnClickListener(v -> {
//            doc=1;
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            activityResultDocument.launch(intent);
        });

//        binding.linearLayoutDocument2.setOnClickListener(v -> {
//            doc=2;
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//            intent.setType("application/pdf");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            activityResultDocument.launch(intent);
//        });


        binding.editTextDate.setText(Utility.select(this,R.string.date_of_activity));
        binding.radio1.setOnClickListener(v->{
            DateUtility.setCurrentDateTime(binding.editTextDate);
            binding.textViewStarPDF.setVisibility(View.INVISIBLE);
            binding.textViewStarPhoto.setVisibility(View.VISIBLE);
        });
        binding.radio2.setOnClickListener(v->{
            binding.editTextDate.setText(Utility.select(this,R.string.date_of_activity));
            binding.editTextDate.setTag(null);
            binding.textViewStarPDF.setVisibility(View.VISIBLE);
            binding.textViewStarPhoto.setVisibility(View.INVISIBLE);
        });
        binding.editTextDate.setOnClickListener(view -> {
            if(binding.radio2.isChecked()){
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -1); // Move to yesterday
                calendar.set(Calendar.HOUR_OF_DAY, 0);   // 12 AM (midnight)
                calendar.set(Calendar.MINUTE, 59);       // 59 minutes
                calendar.set(Calendar.SECOND, 0);        // 0 seconds
                calendar.set(Calendar.MILLISECOND, 0);   // 0 milliseconds

                long timestamp = calendar.getTimeInMillis();
                System.out.println("Timestamp of last night 12:59 AM: " + timestamp);
                DateUtility.showSelectDateAndTimeDialog(binding.editTextDate,null,timestamp);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!fetchPhoto) {
            buildGoogleApiClient();
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
//        if (binding.ll2.getVisibility() == View.VISIBLE) {
//            showForm(1);
//        } else {
        super.onBackPressed();
        finish();
//        }
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
        SpinnerManager spinnerManager;
        switch (code) {
//            case 1:
//                ((TextView) binding.linearLayoutPIU.getChildAt(0)).setText(spinnerItem.getValue());
//                binding.linearLayoutPIU.setTag(spinnerItem);
//                spinnerManager = new SpinnerManager(binding.linearLayoutScheme, 2, this, SqLite.instance(this).getISAScheme(spinnerItem.getKeyString()));
//                if (spinnerManager.getSpinnerItems().size() == 1) {
//                    set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//                } else {
//                    setEmpty(binding.linearLayoutScheme);
//                }
//                break;
//            case 2:
//                ((TextView) binding.linearLayoutScheme.getChildAt(0)).setText(spinnerItem.getValue());
//                binding.linearLayoutScheme.setTag(spinnerItem);
//                spinnerManager = new SpinnerManager(binding.linearLayoutBlock, 3, this, SqLite.instance(this).getISABlock(spinnerItem.getKeyString()));
//                if (spinnerManager.getSpinnerItems().size() == 1) {
//                    set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//                } else {
//                    setEmpty(binding.linearLayoutBlock);
//                }
//                break;
//            case 3:
//                ((TextView) binding.linearLayoutBlock.getChildAt(0)).setText(spinnerItem.getValue());
//                binding.linearLayoutBlock.setTag(spinnerItem);
//                spinnerManager = new SpinnerManager(binding.linearLayoutGP, 4, this, SqLite.instance(this).getISAGp(spinnerItem.getKeyString()));
//                if (spinnerManager.getSpinnerItems().size() == 1) {
//                    set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//                } else {
//                    setEmpty(binding.linearLayoutGP);
//                }
//                break;
            case 1:
                ((TextView) binding.linearLayoutGP.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutGP.setTag(spinnerItem);
                spinnerManager = new SpinnerManager(binding.linearLayoutVillage, 2, this, SqLite.instance(this).getISAVillage(spinnerItem.getKeyString()));
                if (spinnerManager.getSpinnerItems().size() == 1) {
                    set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
                } else {
                    setEmpty(binding.linearLayoutVillage);
                }
                break;
            case 2:
                ((TextView) binding.linearLayoutVillage.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutVillage.setTag(spinnerItem);
                break;
            case 3:
                ((TextView) binding.linearLayoutActivity.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutActivity.setTag(spinnerItem);
                break;
        }
    }

    private void setEmpty(@NonNull LinearLayout linearLayout) {
        ((TextView) linearLayout.getChildAt(0)).setText(R.string.select);
        linearLayout.setTag(null);
    }

    private void connectGoogleClient() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient.connect();
        }
    }

    @SuppressWarnings("deprecation")
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    @SuppressWarnings("deprecation")
    private void buildGoogleApiClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SettingsClient mSettingsClient = LocationServices.getSettingsClient(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(10 * 1000);
                mLocationRequest.setFastestInterval(5 * 1000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                builder.addLocationRequest(mLocationRequest);
                builder.setAlwaysShow(true);
                LocationSettingsRequest mLocationSettingsRequest = builder.build();

                mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(locationSettingsResponse -> requestLocationUpdate()).addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            int REQUEST_CHECK_SETTINGS = 214;
                            ResolvableApiException rae = (ResolvableApiException) e;
                            rae.startResolutionForResult(AddISAActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (Exception ignore) {
                        }
                        //                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    }
                }).addOnCanceledListener(() -> {

                });
            }

            @Override
            public void onConnectionSuspended(int i) {
                connectGoogleClient();
            }
        }).addOnConnectionFailedListener(connectionResult -> buildGoogleApiClient()).addApi(LocationServices.API).build();

        connectGoogleClient();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    int accuracy = 100;
                    if (Utility.isCorrectLocation(location,accuracy)) {
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        showLocation();
                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                            mFusedLocationClient = null;
                        }
                    } else {
                        if(fetchPhoto) {
                            buildGoogleApiClient();
                        }
                        latLng = null;
                        binding.textViewLocation.setText(String.format(String.format(Locale.UK,"Accuracy = %%s , must be less than %d meter", accuracy), location.getAccuracy()));
                    }
                }else{
                    if(fetchPhoto) {
                        buildGoogleApiClient();
                    }
                }
            }
        };
    }

//    private boolean isCorrect(@NonNull Location location) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            if(location.isMock()) return false;
//        }else{
//            if(location.isFromMockProvider()) return false;
//        }
////        return true;
//        return location.getAccuracy() < 15;
//    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {
        if (mFusedLocationClient != null)
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mFusedLocationClient = null;
        }
    }

    private void showLocation() {
        address = "";
        try {
            if (latLng != null) {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                assert addresses != null;
                String area = addresses.get(0).getSubLocality();
                String city = addresses.get(0).getLocality();
                String street = addresses.get(0).getAddressLine(0);
                address = area;
                address += " " + city;
                address += " " + street;
                address = address.replace("null", "").trim();
            }
        } catch (Exception ignored) {
//            e1.printStackTrace();
        }
        binding.textViewLocation.setText(address.isEmpty() ? (latLng == null ? "Location is null." : ("Lat:" + latLng.latitude + " Long:" + latLng.longitude)) : address);
        if(fetchPhoto) {
            fetchPhoto = false;
            bitmap = Utility.addLatLongToBitmap(bitmap,latLng.latitude,latLng.longitude,login.getName(),login.getMobile());
            imageAdapter.add(bitmap);
//            binding.imageViewPhoto.setVisibility(View.VISIBLE);
//            binding.imageViewPhoto.setImageBitmap(bitmap);
//            binding.imageViewPhoto.setTag(Utility.base64(bitmap));
            stopDialog();
        }
    }
    private boolean fetchPhoto;
    private Bitmap bitmap;

//    void showForm(int page) {
//        if (page == 1) {
//            binding.ll1.setVisibility(View.VISIBLE);
//            binding.ll2.setVisibility(View.GONE);
//            binding.buttonSave.setText(R.string.next);
//        } else {
//            binding.ll1.setVisibility(View.GONE);
//            binding.ll2.setVisibility(View.VISIBLE);
//            binding.buttonSave.setText(R.string.save);
//        }
//        binding.scrollView.smoothScrollTo(0, 0);
//    }


    @NonNull
    private byte[] readBytes(@NonNull InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // Buffer size
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private Dialog dialog;

    private void startDialog() {
        stopDialog();
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_api_call);
        AppCompatTextView textView = dialog.findViewById(R.id.textView);
        textView.setText(R.string.please_wait);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    private void stopDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}
