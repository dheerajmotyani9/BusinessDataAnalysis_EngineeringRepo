package mapitgis.jalnigam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mapitgis.jalnigam.core.Component;
import mapitgis.jalnigam.core.Line;
import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.Point;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SpinnerManager;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityAddAssetBinding;
import mapitgis.jalnigam.wms.TileProviderFactory;

public class AddAssetActivity extends BaseActivity {

    private ActivityAddAssetBinding binding;
    private GoogleMap googleMap;
    private LatLng latLng;
    private Geocoder geocoder;
    private boolean show_message;
    //    private JSONArray jsonArrayTraverse;
    private final MutableLiveData<PolylineOptions> polylineOptionsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Mkr> mkrMutableLiveData = new MutableLiveData<>();

    private static class Mkr {
        private final MarkerOptions mo;
        private final String title;

        public Mkr(MarkerOptions mo, String title) {
            this.mo = mo;
            this.title = title;
        }
    }

    private File photoFile;
    private Login login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAssetBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appbar.toolbar);//findViewById(R.id.toolbar)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        login = SqLite.instance(this).getLogin();

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


//            for (String layer : TileProviderFactory.ALL_LAYERS) {
//                TileProvider tileProvider3 = TileProviderFactory.getTileProvider(layer);
//                this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider3));
//            }

            showPastLine();

            this.googleMap.setOnCameraMoveListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(true));
            this.googleMap.setOnCameraIdleListener(() -> binding.scrollView.requestDisallowInterceptTouchEvent(false));
        });

        binding.imageViewCurrentLocation.setVisibility(View.VISIBLE);
        binding.imageViewCurrentLocation.setOnClickListener(v -> {
            if (mFusedLocationClient == null) {
                show_message = true;
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

        binding.buttonSave.setOnClickListener(view -> {
            SpinnerItem scheme, esr, component, point;
            String lastPointID;
            String review;
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

            if (binding.buttonSave.getTag() != null) {
                int nop = Integer.parseInt(binding.radioGroupNOR.findViewById(binding.radioGroupNOR.getCheckedRadioButtonId()).getTag().toString());
                Intent intent = new Intent(this, UpdateLocationActivity.class);
                // Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra(Utility.DATA, scheme);
                intent.putExtra(Utility.G_KEY, component);
                intent.putExtra("nop", nop);
                startActivity(intent);
                finish();
                return;
            }

            if (binding.linearLayoutESR.getTag() == null) {
//                    Utility.show(this, Utility.select(this, R.string.esr));
//                    return;
                esr = new SpinnerItem("", "");
            } else {
                esr = ((SpinnerItem) binding.linearLayoutESR.getTag());
            }

            if (binding.linearLayoutPoints.getVisibility() == View.VISIBLE) {
                if (binding.linearLayoutPoints.getTag() == null) {
                    Utility.show(this, Utility.select(this, R.string.points));
                    return;
                }
                point = ((SpinnerItem) binding.linearLayoutPoints.getTag());

                if (point.getValue().equalsIgnoreCase("-- ADD NEW --")) {
                    String enterPoint = binding.editTextPointName.getText().toString();
                    if (enterPoint.isEmpty()) {
                        Utility.show(this, "Enter point name");
                        return;
                    }
                    point = new SpinnerItem("0", enterPoint);
                }

            } else {
                point = new SpinnerItem("", "");
            }

            String diameter = "", material = "", depth_cover = "", alignment = "", materialInfoName = "", materialInfoValue = "", line_json = "";
//            if (binding.linearLayoutPastPoints.getTag() == null) {
            lastPointID = "0";
//            } else {
//                lastPointID = ((SpinnerItem) binding.linearLayoutPastPoints.getTag()).getKeyString();
//            }

            review = binding.editTextReview.getText().toString();
            if (review.isEmpty()) {
                Utility.show(this, Utility.check(this, R.string.review));
                return;
            }

            if (latLng == null) {
                Utility.show(this, Utility.check(this, R.string.location));
                buildGoogleApiClient();
                return;
            }

            String photo = "";
            if (binding.linearLayoutImage.getVisibility() == View.VISIBLE) {
                if (binding.imageViewPhoto.getTag() == null) {
                    Utility.show(this, Utility.select(this, R.string.photo));
                    return;
                }
                photo = binding.imageViewPhoto.getTag().toString();
            }

//            if (binding.buttonTraverse.getTag() != null) {
//                if (jsonArrayTraverse == null) {
//                    Utility.show(this, R.string.traverse_msg);
//                    return;
//                }
//                int totalLine = jsonArrayTraverse.length();
//                for (int j = 0; j < totalLine; j++) {
//                    JSONObject jsonObjectTraverse = jsonArrayTraverse.optJSONObject(j);
//
//                    diameter = jsonObjectTraverse.optString("diameter");//binding.editTextPipeDiameter.getText().toString();
//                    material = jsonObjectTraverse.optString("material");//binding.editTextMaterialType.getText().toString();
//                    depth_cover = jsonObjectTraverse.optString("depth");
//                    alignment = jsonObjectTraverse.optString("alignment", "");
//                    materialInfoName = jsonObjectTraverse.optString("materialInfoName");
//                    materialInfoValue = jsonObjectTraverse.optString("materialInfoValue");
//                    JSONArray jsonArray = jsonObjectTraverse.optJSONArray("traverse");
//                    if (jsonArray == null) {
//                        Utility.show(this, R.string.traverse_msg);
//                        return;
//                    }
//                    line_json = jsonArray.toString();
//                    SqLite.instance(this).addAsset(new Asset(0, lastPointID, scheme.getKeyString(), scheme.getValue(), esr.getKeyString(), esr.getValue(), point.getKeyString(), point.getValue(), component.getKeyString(), component.getValue(), String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), totalLine == 1 ? review : String.format(Locale.UK, "%s - (LINE NO %d)", review, j + 1), photo, Utility.getCurrentTimeUsingDate(), depth_cover, diameter, material, alignment, materialInfoName, materialInfoValue, line_json, false), login.getMobile());
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
//                dlgAlert.setMessage(R.string.saved_successfully);
//                dlgAlert.setTitle(R.string.success);
//                dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> finish());
//                dlgAlert.setCancelable(false);
//                dlgAlert.create().show();
//                return;
//            }
            SqLite.instance(this).addAsset(new Asset(0, lastPointID, scheme.getKeyString(), scheme.getValue(), esr.getKeyString(), esr.getValue(), point.getKeyString(), point.getValue(), component.getKeyString(), component.getValue(), String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), review, photo, Utility.getCurrentTimeUsingDate(), depth_cover, diameter, material, alignment, materialInfoName, materialInfoValue, line_json, false), login.getMobile());
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage(R.string.saved_successfully);
            dlgAlert.setTitle(R.string.success);
            dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> finish());
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        });

        ActivityResultLauncher<Intent> activityResultPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {// && null != result.getData()
                try {
//                    Bundle extras = result.getData().getExtras();
//                    Bitmap bitmap = (Bitmap) extras.get("data");
                    Bitmap bitmap = Utility.getResizedBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
                    //noinspection ResultOfMethodCallIgnored
                    photoFile.delete();
                    binding.imageViewPhoto.setVisibility(View.VISIBLE);
                    binding.imageViewPhoto.setImageBitmap(bitmap);
                    binding.imageViewPhoto.setTag(Utility.base64(bitmap));
                } catch (Exception e) {
                    Utility.show(this, e);
                    binding.imageViewPhoto.setVisibility(View.GONE);
                    binding.imageViewPhoto.setTag(null);
                }
            } else {
                Utility.show(this, Utility.select(this, R.string.photo));
                binding.imageViewPhoto.setVisibility(View.GONE);
                binding.imageViewPhoto.setTag(null);
            }
        });

        binding.linearLayoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoFile = Utility.createImageFile(this);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, String.format("%s.fileProvider", BuildConfig.APPLICATION_ID), photoFile));
            activityResultPhoto.launch(intent);
        });

//        ActivityResultLauncher<Intent> activityResultMain = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            try {
//                if (result.getResultCode() == RESULT_OK) {
//                    assert result.getData() != null;
//                    jsonArrayTraverse = new JSONArray(Objects.requireNonNull(result.getData().getStringExtra(Utility.G_KEY1)));
//                    showCurrentLine();
//                } else {
//                    jsonArrayTraverse = null;
//                    assert result.getData() != null;
//                    Utility.show(this, Objects.requireNonNull(result.getData().getStringExtra(Utility.G_KEY)));
//                }
//            } catch (Exception e) {
//                Utility.show(this, e);
//                jsonArrayTraverse = null;
//            }
//        });

//        binding.buttonTraverse.setOnClickListener(v -> {
//            SpinnerItem scheme;
//
//            if (binding.linearLayoutScheme.getTag() == null) {
//                Utility.show(this, Utility.select(this, R.string.scheme));
//                return;
//            }
//
//            scheme = ((SpinnerItem) binding.linearLayoutScheme.getTag());
//            DialogRoadBinding dialogRoadBinding = DialogRoadBinding.inflate(LayoutInflater.from(this));
//            AlertDialog alertDialog = new AlertDialog.Builder(this).setView(dialogRoadBinding.getRoot())
//                    .setPositiveButton(R.string.ok, (d, v1) -> {
//                        int nop = Integer.parseInt(dialogRoadBinding.radioGroup.findViewById(dialogRoadBinding.radioGroup.getCheckedRadioButtonId()).getTag().toString());
//                        d.dismiss();
//                        Intent intent = new Intent(this, UpdateLocationActivity.class);
//                       // Intent intent = new Intent(this, MapActivity.class);
//                        intent.putExtra(Utility.DATA, scheme);
//                        intent.putExtra("nop", nop);
//
////                        if (binding.linearLayoutPoints.getVisibility() == View.VISIBLE) {
////                            if (binding.linearLayoutPoints.getTag() == null) {
////                                Utility.show(this, Utility.select(this, R.string.points));
////                                return;
////                            }
////                            SpinnerItem pointC = ((SpinnerItem) binding.linearLayoutPoints.getTag());
////                            if (binding.linearLayoutPastPoints.getTag() == null) {
////                                Utility.show(this, Utility.select(this, R.string.linked_with_last_point));
////                                return;
////                            }
////                            SpinnerItem pointP = ((SpinnerItem) binding.linearLayoutPastPoints.getTag());
////                            intent.putExtra(Utility.G_KEY, (Point) pointP.getExtra());
////                            intent.putExtra(Utility.G_KEY1, (Point) pointC.getExtra());
////                        }
//                        activityResultMain.launch(intent);
//                    })
//                    .setNegativeButton(R.string.close, (d, v1) -> d.dismiss())
//                    .setCancelable(false)
//                    .create();
//            alertDialog.show();
//            ((RadioButton) dialogRoadBinding.radioGroup.getChildAt(0)).setChecked(true);
//        });

        SpinnerManager spinnerManager = new SpinnerManager(binding.linearLayoutScheme, 1, this, SqLite.instance(this).GET_SCHEME());
        if (spinnerManager.getSpinnerItems().size() == 1) {
            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
        }

        buildGoogleApiClient();

        this.polylineOptionsMutableLiveData.observe(this, polylineOptions -> {
            if (googleMap != null) {
                googleMap.addPolyline(polylineOptions);
            }
        });

        this.mkrMutableLiveData.observe(this, mkr -> {
            if (googleMap != null) {
                googleMap.addMarker(mkr.mo.title(mkr.title));
//                if (!x) {
//                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mkr.mo.getPosition(), 17), 2000, null);//.zoomTo(15), 2000, null);
//                    x = true;
//                }
            }
        });

    }

//    boolean x = false;


//    private void showAdd() {
//        clearForm();
//        binding.scrollView.setVisibility(View.VISIBLE);
//        binding.scrollView.scrollTo(0, 0);
//        binding.viewList.setVisibility(View.GONE);
//
//        binding.textViewTitle.setText(R.string.new_asset);
//
//        binding.cardViewButton.setVisibility(View.VISIBLE);
//        binding.cardViewButton.setTag(1);
//        binding.textViewButton.setText(R.string.save);
//        binding.imageViewButton.setImageResource(R.drawable.check);
//
//        SpinnerManager spinnerManager = new SpinnerManager(binding.linearLayoutScheme, 1, this, SqLite.instance(this).GET_SCHEME());
//        if (spinnerManager.getSpinnerItems().size() == 1) {
//            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
//        }
////        new SpinnerManager(binding.linearLayoutPipeDepthCover, 6, this, SqLite.instance(this).GET_DEPTH_COVER());
////        new SpinnerManager(binding.linearLayoutMaterialType, 7, this, SqLite.instance(this).GET_MATERIAL_TYPE());
//        buildGoogleApiClient();
//    }

//    private void clearForm() {
//        binding.imageViewPhoto.setVisibility(View.GONE);
//        binding.imageViewPhoto.setTag(null);
//
//        binding.editTextReview.setText("");
//
//        setEmpty(binding.linearLayoutScheme);
//        setEmpty(binding.linearLayoutComponent);
//        setEmpty(binding.linearLayoutESR);
//        setEmpty(binding.linearLayoutPoints);
//        setEmpty(binding.linearLayoutPastPoints);
//        binding.buttonTraverse.setVisibility(View.GONE);
//        binding.buttonTraverse.setTag(null);
////        setEmpty(binding.linearLayoutPipeDepthCover);
////        setEmpty(binding.linearLayoutMaterialType);
////        setEmpty(binding.linearLayoutPipeDiameter);
////        ((TextView) binding.linearLayoutComponent.getChildAt(0)).setText(R.string.select);
////        binding.linearLayoutComponent.setTag(null);
//
//        for (String layer : TileProviderFactory.ALL_LAYERS) {
//            TileProvider tileProvider3 = TileProviderFactory.getTileProvider(layer);
//            this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider3));
//        }
//    }

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
                ((TextView) binding.linearLayoutScheme.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutScheme.setTag(spinnerItem);

                spinnerManager = new SpinnerManager(binding.linearLayoutComponent, 2, this, SqLite.instance(this).GET_COMPONENT_AM(spinnerItem.getKeyString(), SqLite.INT_ALL));
                if (spinnerManager.getSpinnerItems().size() == 1) {
                    set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
                } else {
                    setEmpty(binding.linearLayoutComponent);
                    setEmpty(binding.linearLayoutESR);
                    setEmpty(binding.linearLayoutPoints);
//                    setEmpty(binding.linearLayoutPastPoints);
                    latLngPoint = null;
                    namePoint = "";
//                    comp_id = 0;
                }
                break;
            case 2:
                ((TextView) binding.linearLayoutComponent.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutComponent.setTag(spinnerItem);
//                int comp_id = Integer.parseInt(spinnerItem.getKeyString());

//                if (comp_id == 5) {
//                    binding.textViewPastPoints.setVisibility(View.VISIBLE);
//                    binding.linearLayoutPastPoints.setVisibility(View.VISIBLE);
//                } else {
//                    binding.textViewPastPoints.setVisibility(View.GONE);
//                    binding.linearLayoutPastPoints.setVisibility(View.GONE);
//                }

                Component component = (Component) spinnerItem.getExtra();
                if (component.getType() == 2) {//comp_id == 9 || comp_id == 10 || comp_id == 11|| comp_id == 12) {

                    binding.textViewPhoto.setVisibility(View.GONE);
                    binding.linearLayoutImage.setVisibility(View.GONE);
                    binding.imageViewPhoto.setVisibility(View.GONE);
                    binding.linearLayoutImage.setTag(null);

                    binding.textViewPoints.setVisibility(View.GONE);
                    binding.linearLayoutPoints.setVisibility(View.GONE);
                    binding.editTextPointName.setVisibility(View.GONE);
                    setEmpty(binding.linearLayoutPoints);

                    binding.textViewESR.setVisibility(View.GONE);
                    binding.linearLayoutESR.setVisibility(View.GONE);
                    setEmpty(binding.linearLayoutESR);


                    binding.textViewReview.setVisibility(View.GONE);
                    binding.editTextReview.setVisibility(View.GONE);


                    binding.textViewNOR.setVisibility(View.VISIBLE);
                    binding.radioGroupNOR.setVisibility(View.VISIBLE);

                    binding.mapBox.setVisibility(View.GONE);

//                    binding.buttonTraverse.setVisibility(View.VISIBLE);
//                    binding.buttonTraverse.setTag(1);

                    binding.buttonSave.setTag(1);
                    binding.buttonSave.setText(R.string.start_traverse);

                    ((RadioButton) binding.radioGroupNOR.getChildAt(0)).setChecked(true);

//                    setEmpty(binding.linearLayoutPastPoints);
                } else {
                    binding.textViewPhoto.setVisibility(View.VISIBLE);
                    binding.linearLayoutImage.setVisibility(View.VISIBLE);
                    binding.linearLayoutImage.setTag(null);

                    binding.textViewPoints.setVisibility(View.VISIBLE);
                    binding.linearLayoutPoints.setVisibility(View.VISIBLE);
                    setEmpty(binding.linearLayoutPoints);

                    binding.textViewESR.setVisibility(View.VISIBLE);
                    binding.linearLayoutESR.setVisibility(View.VISIBLE);
                    setEmpty(binding.linearLayoutESR);

//                    binding.buttonTraverse.setVisibility(View.GONE);
//                    binding.buttonTraverse.setTag(null);

                    binding.textViewNOR.setVisibility(View.GONE);
                    binding.radioGroupNOR.setVisibility(View.GONE);

                    binding.textViewReview.setVisibility(View.VISIBLE);
                    binding.editTextReview.setVisibility(View.VISIBLE);

                    binding.mapBox.setVisibility(View.VISIBLE);

                    binding.buttonSave.setTag(null);
                    binding.buttonSave.setText(R.string.save);


//                    new SpinnerManager(binding.linearLayoutPastPoints, 5, this, SqLite.instance(this).GET_PAST_POINT(spinnerItem.getKeyString()));
//                    setEmpty(binding.linearLayoutPastPoints);

                    SpinnerItem spinnerItem1 = (SpinnerItem) binding.linearLayoutScheme.getTag();
                    clearAndShowLayer(spinnerItem1.getKeyString(), spinnerItem.getKeyString());
                    spinnerManager = new SpinnerManager(binding.linearLayoutESR, 3, this, SqLite.instance(this).GET_ESR_NEW(spinnerItem1.getKeyString(), spinnerItem.getKeyString()));
                    if (spinnerManager.getSpinnerItems().size() == 1) {
                        set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
                    } else {
                        setEmpty(binding.linearLayoutESR);
//                    setEmpty(linearLayoutPoints);

                        List<SpinnerItem> list = new ArrayList<>();
                        list.add(new SpinnerItem("0", "-- ADD NEW --"));
                        list.addAll(SqLite.instance(this).GET_POINTS(spinnerItem1.getKeyString(), spinnerItem.getKeyString(), null));

                        Log.e("TAG", "Spin " + list.get(0).getValue());
                        spinnerManager = new SpinnerManager(binding.linearLayoutPoints, 4, this, list);
                        if (spinnerManager.getSpinnerItems().size() == 1) {
                            set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
                        } else {
                            setEmpty(binding.linearLayoutPoints);
                            latLngPoint = null;
                            namePoint = "";
                        }
//                    latLngPoint = null;
//                    namePoint = "";
                    }
                }

//                jsonArrayTraverse = null;
                showPoint(true);
                break;
            case 3:
                ((TextView) binding.linearLayoutESR.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutESR.setTag(spinnerItem);
                SpinnerItem spinnerItem2 = (SpinnerItem) binding.linearLayoutScheme.getTag();
                SpinnerItem spinnerItem3 = (SpinnerItem) binding.linearLayoutComponent.getTag();

                List<SpinnerItem> list = new ArrayList<>();
                list.add(new SpinnerItem("0", "-- ADD NEW --"));
                list.addAll(SqLite.instance(this).GET_POINTS(spinnerItem2.getKeyString(), spinnerItem3.getKeyString(), spinnerItem.getKeyString()));

                spinnerManager = new SpinnerManager(binding.linearLayoutPoints, 4, this, list);
                if (spinnerManager.getSpinnerItems().size() == 1) {
                    set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
                } else {
                    setEmpty(binding.linearLayoutPoints);
                    latLngPoint = null;
                    namePoint = "";
                }
                break;
            case 4:
                ((TextView) binding.linearLayoutPoints.getChildAt(0)).setText(spinnerItem.getValue());
                binding.linearLayoutPoints.setTag(spinnerItem);


                // TODO: 07-08-2024 : show input point..

                if (spinnerItem.getValue().equalsIgnoreCase("-- ADD NEW --")) {
                    binding.editTextPointName.setVisibility(View.VISIBLE);
                } else {
                    binding.editTextPointName.setVisibility(View.GONE);
                }

                if (spinnerItem.getExtra() != null) {
                    Point point = (Point) spinnerItem.getExtra();
                    latLngPoint = new LatLng(point.getLat(), point.getLng());
                    namePoint = spinnerItem.getValue();
                } else {
                    latLngPoint = null;
                    namePoint = "";
                }

                showPoint(true);
                break;

//            case 5:
//                ((TextView) binding.linearLayoutPastPoints.getChildAt(0)).setText(spinnerItem.getValue());
//                binding.linearLayoutPastPoints.setTag(spinnerItem);
//                if (spinnerItem.getKeyString().isEmpty()) {
//                    binding.buttonTraverse.setVisibility(View.GONE);
//                    binding.buttonTraverse.setTag(null);
//                    jsonArrayTraverse = null;

//                    setEmpty(binding.linearLayoutPipeDepthCover);
//                    setEmpty(binding.linearLayoutPipeDiameter);
//                    setEmpty(binding.linearLayoutMaterialType);

//                    binding.editTextMaterialType.setText("");
//                    binding.editTextPipeDiameter.setText("");
//                } else {
//                    binding.buttonTraverse.setVisibility(View.VISIBLE);
//                    binding.buttonTraverse.setTag(1);
//                    jsonArrayTraverse = null;
//                }
//                showPoint(false);
//                break;
        }
    }

    private TileOverlay last;
    private void clearAndShowLayer(String id, String cid) {
        if(last!=null){
            try{
                Log.e("QQCC","CLEAR "+last);
                last.remove();
            }catch (Exception ignore){

            }
        }
        LayerInfo layerInfos = SqLite.instance(this).GET_COMPONENT_ONLY1_AM(id, cid);
        if (layerInfos != null) {
            Log.e("QQCC","ADD "+layerInfos.name);
            TileProvider tileProvider3 = TileProviderFactory.getTileProvider(layerInfos.component.getSurveyedLayer());
            last = this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider3));
        }else{
            Log.e("QQCC","NOT FOUND");
        }
    }

    private LatLng latLngPoint;
    private String namePoint;
    private Marker marker1, marker2;
//    private int comp_id;

    private void showPoint(boolean navigate) {
        if (googleMap != null) {
//            googleMap.clear();
            if (marker1 != null) {
                marker1.remove();
            }
            if (marker2 != null) {
                marker2.remove();
            }

//            for (String layer : TileProviderFactory.ALL_LAYERS) {
//                TileProvider tileProvider3 = TileProviderFactory.getTileProvider(layer);
//                this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider3));
//            }

//            if (comp_id > 0) {
//                TileProvider tileProvider = TileProviderFactory.getTileProvider(TileProviderFactory.LAYERS[comp_id - 1]);
//                this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
//            }

//            TileProvider tileProvider1 = TileProviderFactory.getTileProvider(TileProviderFactory.LAYER_1);
//            this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider1));
//
//            TileProvider tileProvider2 = TileProviderFactory.getTileProvider(TileProviderFactory.LAYER_2);
//            this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider2));


//            Marker marker2;
//
            if (latLngPoint != null) {
                marker2 = googleMap.addMarker(new MarkerOptions().position(latLngPoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(namePoint));
//                mkrMutableLiveData.postValue(new Mkr(new MarkerOptions().position(latLngPoint).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)), namePoint));
            } else {
                marker2 = null;
            }


//            googleMap.getUiSettings().setZoomControlsEnabled(true);


            if (navigate) {
                if (latLngPoint != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngPoint, 17), 2000, null);//.zoomTo(15), 2000, null);
                } else if (latLng != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 2000, null);//.zoomTo(15), 2000, null);
                }
            }

            if (latLng != null) {
//                mkrMutableLiveData.postValue(new Mkr(new MarkerOptions().position(latLng), "Your Location"));
                marker1 = googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
            }

            //showCurrentLine();


            new Handler().postDelayed(() -> {
                try {
                    if (marker2 != null) {
                        marker2.showInfoWindow();
                    }
                } catch (Exception ignore) {
                }
            }, 2000);
        }
    }

//    private final List<Polyline> polylines = new ArrayList<>();

//    private void showCurrentLine() {
//        try {
//            for (int i = 0; i < polylines.size(); i++) {
//                polylines.get(i).remove();
//            }
//            polylines.clear();
//            if (jsonArrayTraverse != null) {
//                binding.buttonTraverse.setVisibility(View.GONE);
//                PolylineOptions polylineOptions;
//                for (int j = 0; j < jsonArrayTraverse.length(); j++) {
//                    polylineOptions = new PolylineOptions();
//                    polylineOptions.width(15);
//                    polylineOptions.color(j == 0 ? Color.BLUE : (j == 1 ? Color.YELLOW : Color.CYAN));
//                    JSONObject jsonObjectTraverse = jsonArrayTraverse.getJSONObject(j);
//                    JSONArray jsonArray = jsonObjectTraverse.getJSONArray("traverse");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        polylineOptions.add(new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng")));
//                    }
//                    polylines.add(googleMap.addPolyline(polylineOptions));
//                }
//            }
//        } catch (Exception ignore) {
//        }
//    }

    private void showPastLine() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
//        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            List<Line> lines = SqLite.instance(this).getAssetLine();
            for (Line line : lines) {
                if (!line.getName().isEmpty()) {
                    mkrMutableLiveData.postValue(new Mkr(new MarkerOptions().position(line.getPoint1()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)), line.getName()));
//                    binding.appbar.toolbar.post(() -> googleMap.addMarker(new MarkerOptions().position(line.getPoint1()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title(line.getName())));
                }
                if (line.getJsonArray() != null) {
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.width(15);
//                if (line.getPoint2() != null) {
//                    polylineOptions.add(line.getPoint2());
//                }

                    try {
                        JSONArray jsonArray = line.getJsonArray();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            polylineOptions.add(new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng")));
                        }
                    } catch (Exception ignore) {
                    }
//                    SystemClock.sleep(500);
                    polylineOptionsMutableLiveData.postValue(polylineOptions);
                    SystemClock.sleep(5);
//                    binding.appbar.toolbar.post(() ->googleMap.addPolyline(polylineOptions));
//                polylineOptions.add(line.getPoint1());
                }
            }
        });
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
                            rae.startResolutionForResult(AddAssetActivity.this, REQUEST_CHECK_SETTINGS);
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
                assert location != null;
                int accuracy = 100;
                if (Utility.isCorrectLocation(location,accuracy)) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    showLocation();
                    binding.imageViewCurrentLocation.setAnimation(null);
                    if (show_message) {
                        show_message = false;
                        Utility.show(AddAssetActivity.this, "Location Refreshed");
                    }

                    if (mFusedLocationClient != null) {
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                        mFusedLocationClient = null;
                    }
                }else{
                    latLng = null;
                    binding.textViewLocation.setText(String.format(String.format(Locale.UK,"Accuracy = %%s , must be less than %d meter", accuracy), location.getAccuracy()));
                }
            }
        };
    }

//    private boolean isCorrect(@NonNull Location location) {
//        return !(location.getAccuracy() > 15);
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
        if (latLngPoint != null) {
            if (Utility.distance(latLng, latLngPoint) <= 20) {
                latLng = latLngPoint;
            }
        }

        showPoint(false);
//        if (googleMap != null && latLng != null) {
//            googleMap.clear();
//            googleMap.addMarker(new MarkerOptions().position(latLng).title("Your Location"));
////            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            googleMap.getUiSettings().setZoomControlsEnabled(true);
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 2000, null);//.zoomTo(15), 2000, null);
//        }
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
}
