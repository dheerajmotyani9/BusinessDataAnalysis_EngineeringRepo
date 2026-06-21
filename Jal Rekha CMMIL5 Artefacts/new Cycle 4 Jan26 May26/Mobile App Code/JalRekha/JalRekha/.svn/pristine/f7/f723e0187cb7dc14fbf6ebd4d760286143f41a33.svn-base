package mapitgis.jalnigam.di;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import mapitgis.jalnigam.BaseActivity;
import mapitgis.jalnigam.BuildConfig;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SpinnerManager;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityAddDiBinding;
import mapitgis.jalnigam.rfi.helper.PermissionHelper;

public class AddDIActivity extends BaseActivity {
    private ActivityAddDiBinding binding;
    private LatLng latLng;
    private Geocoder geocoder;
    private OptionAdapter optionAdapter;
    private ImageAdapter imageAdapter;
    private File photoFile;
//    private Login login;

    private PermissionHelper permissionHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDiBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appbar.toolbar);//findViewById(R.id.toolbar)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        permissionHelper = new PermissionHelper(this);

//        login = SqLite.instance(this).getLogin();

        geocoder = new Geocoder(this, Locale.getDefault());

        imageAdapter = new ImageAdapter(this,new ArrayList<>());
        binding.recyclerViewImages.setAdapter(imageAdapter);

        binding.buttonSave.setOnClickListener(view -> {
            SpinnerItem piu, scheme, component, application, review;
            String village, gp, block;
            String detail, comment;

            if (binding.linearLayoutPIU.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.piu_name));
                return;
            }
            piu = ((SpinnerItem) binding.linearLayoutPIU.getTag());
            if (binding.linearLayoutScheme.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.scheme));
                return;
            }
            scheme = ((SpinnerItem) binding.linearLayoutScheme.getTag());
            if (binding.linearLayoutComponent.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.component));
                return;
            }
            component = ((SpinnerItem) binding.linearLayoutComponent.getTag());
            if (binding.linearLayoutApplicationType.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.application_type));
                return;
            }
            application = ((SpinnerItem) binding.linearLayoutApplicationType.getTag());

            village = binding.editTextVillage.getText().toString();
            if (village.isEmpty()) {
                Utility.show(this, Utility.check(this, R.string.village));
                return;
            }
            gp = binding.editTextGP.getText().toString();
            if (gp.isEmpty()) {
                Utility.show(this, Utility.check(this, R.string.gram_panchayat));
                return;
            }
            block = binding.editTextBlock.getText().toString();
            if (block.isEmpty()) {
                Utility.show(this, Utility.check(this, R.string.block));
                return;
            }
            detail = binding.editTextDescription.getText().toString();
            if (detail.isEmpty()) {
                Utility.show(this, Utility.check(this, R.string.description_of_inspection));
                return;
            }

            if (binding.ll2.getVisibility() == View.GONE) {
                showForm(2);
            } else {
                if (optionAdapter.selectedSpinnerItem == null) {
                    Utility.show(this, Utility.select(this, R.string.qa_and_qc_review));
                    return;
                }
                review = optionAdapter.selectedSpinnerItem;
                comment = binding.editTextQaQcReview.getText().toString();
                if (comment.isEmpty()) {
                    Utility.show(this, Utility.check(this, R.string.qa_and_qc_comments));
                    return;
                }

                if (latLng == null) {
                    Utility.show(this, Utility.check(this, R.string.location));
                    buildGoogleApiClient();
                    return;
                }

                if(imageAdapter.getItemCount() == 0){
                    Utility.show(this, "Minimum 1 image required.");
                    return;
                }

                List<String> images = new ArrayList<>();
                for (int i = 0; i < imageAdapter.getItemCount(); i++) {
                    Bitmap bitmap = imageAdapter.getItem(i);
                    images.add(Utility.base64(bitmap));
                }
                try {
                    SqLite.instance(this).addDailyInspection(false,
                            village, gp, block, detail, comment, latLng, binding.textViewLocation.getText().toString(),
                            piu, scheme, component, application, review, images,this
                    );
                    androidx.appcompat.app.AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                    dlgAlert.setMessage(R.string.saved_successfully);
                    dlgAlert.setTitle(R.string.success);
                    dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> finish());
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                } catch (Exception e) {
                    Utility.show(this, e);
                }

//                JSONArray jsonArray = new JSONArray();
//                for (int i = 0; i < imageAdapter.getItemCount(); i++) {
//                    Bitmap bitmap = imageAdapter.getItem(i);
//                    jsonArray.put(Utility.base64(bitmap));
//                }

//                Data data = new Data();
//                data.put("etokens", login.getToken());
//                data.put("piu_id", piu.getKeyString());
//                data.put("scheme_id", scheme.getKeyString());
//                data.put("component_id", component.getKeyString());
//                data.put("village_name", village);
//                data.put("block_name", block);
//                data.put("gp_name", gp);
//                data.put("application_type_id", application.getKeyString());
//                data.put("description", detail);
//                data.put("qa_qc_review", review.getKeyString());
//                data.put("qa_qc_remark", comment);
//                data.put("image_base64", jsonArray);
//                data.put("latitude", latLng.latitude);
//                data.put("longitude", latLng.longitude);
//                data.put("address", binding.textViewLocation.getText().toString());
//                new ApiCaller(this, (response, key) -> {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        if (jsonObject.getBoolean(Utility.SUCCESS)) {
//                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//                            dlgAlert.setMessage(jsonObject.getString(Utility.MESSAGE));
//                            dlgAlert.setTitle(R.string.success);
//                            dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> finish());
//                            dlgAlert.setCancelable(true);
//                            dlgAlert.create().show();
//                        } else {
//                            Utility.show(this, jsonObject.getString(Utility.MESSAGE));
//                        }
//                    } catch (Exception e) {
//                        Utility.show(this, e);
//                    }
//                }, 1, data.toString(), getString(R.string.please_wait)).start(API.DI.SAVE);

//
//            String photo = "";
//            if (binding.linearLayoutImage.getVisibility() == View.VISIBLE) {
//                if (binding.imageViewPhoto.getTag() == null) {
//                    Utility.show(this, Utility.select(this, R.string.photo));
//                    return;
//                }
//                photo = binding.imageViewPhoto.getTag().toString();
//            }
            }
        });

        ActivityResultLauncher<Intent> activityResultPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {// && null != result.getData()
                try {
//                    Bundle extras = result.getData().getExtras();
//                    Bitmap bitmap = (Bitmap) extras.get("data");
                    Bitmap bitmap = Utility.getResizedBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
                    //noinspection ResultOfMethodCallIgnored
                    photoFile.delete();
                    imageAdapter.add(bitmap);
                } catch (Exception e) {
                    Utility.show(this, e);
                }
            } else {
                Utility.show(this, Utility.select(this, R.string.photo));
            }
        });

        binding.buttonAddImage.setOnClickListener(v -> {
            permissionHelper.requestCameraPermission(allow -> {
                if(allow){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoFile = Utility.createImageFile(AddDIActivity.this);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(AddDIActivity.this, String.format("%s.fileProvider", BuildConfig.APPLICATION_ID), photoFile));
                    activityResultPhoto.launch(intent);
                }else{
                    Utility.show(AddDIActivity.this,"Camera permission required");
                }
            });
        });

        SpinnerManager spinnerManager = new SpinnerManager(binding.linearLayoutPIU, 1, this, SqLite.instance(this).GET_DISTRICT_DI_NEW());
        if (spinnerManager.getSpinnerItems().size() == 1) {
            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
        }
        new SpinnerManager(binding.linearLayoutComponent, 3, this, SqLite.instance(this).GET_COMPONENT_DI_NEW());
        new SpinnerManager(binding.linearLayoutApplicationType, 4, this, SqLite.instance(this).GET_APPLICATION_TYPE());

        optionAdapter = new OptionAdapter(this, SqLite.instance(this).GET_QA_QC_REVIEW_DI_NEW(), (spinnerItem) -> {
        });
        binding.recyclerView.setAdapter(optionAdapter);

        activityResultMIC = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && null != result.getData()) {
                ArrayList<String> r = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                EditText editText = ((mic == 1) ? binding.editTextDescription : binding.editTextQaQcReview);
                editText.setText(String.format("%s %s", editText.getText().toString().trim(), r != null ? r.get(0) : ""));
            }
        });

        binding.textViewMic1.setOnClickListener(view -> openMic(1));
        binding.textViewMic2.setOnClickListener(view -> openMic(2));

        showForm(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildGoogleApiClient();
    }

    private ActivityResultLauncher<Intent> activityResultMIC;
    private int mic;

    private void openMic(int mic) {
        this.mic = mic;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, view.findViewById(radioGroupLanguage.getCheckedRadioButtonId()).getTag().toString().equals("1")?"hi":"en");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.need_to_speak));
        try {
            activityResultMIC.launch(intent);
        } catch (ActivityNotFoundException a) {
            Utility.show(this, getString(R.string.sorry_your_device_not_supported));
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
        if (binding.ll2.getVisibility() == View.VISIBLE) {
            showForm(1);
        } else {
            super.onBackPressed();
            finish();
        }
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
            case 1:
                ((TextView) binding.linearLayoutPIU.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutPIU.setTag(spinnerItem);

                // TODO: 18-07-2024 : OLD Scheme get data
//                spinnerManager = new SpinnerManager(binding.linearLayoutScheme, 2, this, SqLite.instance(this).GET_SCHEME(spinnerItem.getKeyString()));
                // TODO: 18-07-2024 : NEW Scheme get data
                spinnerManager = new SpinnerManager(binding.linearLayoutScheme, 2, this, SqLite.instance(this).GET_SCHEME_DI_NEW(spinnerItem.getKeyString()));
                if (spinnerManager.getSpinnerItems().size() == 1) {
                    set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
                } else {
                    setEmpty(binding.linearLayoutScheme);
                }
                break;
            case 2:
                ((TextView) binding.linearLayoutScheme.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutScheme.setTag(spinnerItem);
                break;
            case 3:
                ((TextView) binding.linearLayoutComponent.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutComponent.setTag(spinnerItem);
                break;
            case 4:
                ((TextView) binding.linearLayoutApplicationType.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutApplicationType.setTag(spinnerItem);
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
                            rae.startResolutionForResult(AddDIActivity.this, REQUEST_CHECK_SETTINGS);
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
                        latLng = null;
                        binding.textViewLocation.setText(String.format(String.format(Locale.UK,"Accuracy = %%s , must be less than %d meter", accuracy), location.getAccuracy()));
//                        binding.textViewLocation.setText(String.format("Accuracy = %s , must be less than 15 meter", location.getAccuracy()));
                    }
                }
            }
        };
    }

//    private boolean isCorrect(@NonNull Location location) {
//        return true;
////        return !(location.getAccuracy() > 15);
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
        String address = "";
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
    }

    void showForm(int page) {
        if (page == 1) {
            binding.ll1.setVisibility(View.VISIBLE);
            binding.ll2.setVisibility(View.GONE);
            binding.buttonSave.setText(R.string.next);
        } else {
            binding.ll1.setVisibility(View.GONE);
            binding.ll2.setVisibility(View.VISIBLE);
            binding.buttonSave.setText(R.string.save);
        }
        binding.scrollView.smoothScrollTo(0, 0);
    }

}
