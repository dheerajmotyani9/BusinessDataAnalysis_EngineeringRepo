package mapitgis.jalnigam;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import mapitgis.jalnigam.core.Login;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SpinnerManager;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityUpdateLocationBinding;
import mapitgis.jalnigam.databinding.DialogFinalBinding;
import mapitgis.jalnigam.databinding.DialogPipeBinding;
import mapitgis.jalnigam.databinding.DialogPointBinding;
import mapitgis.jalnigam.wms.TileProviderFactory;

@SuppressWarnings("deprecation")
public class UpdateLocationActivity extends AppCompatActivity {
    private ActivityUpdateLocationBinding binding;
    private List<LatLng> locations1, locationsF, locations2;
    private double distance;
    private int time;
    private final MutableLiveData<String> times = new MutableLiveData<>();
    private Thread counterThread;
    private boolean isCounting = false;

    private GoogleMap googleMap;
    private boolean stop;
    //    @Nullable
//    private LatLng point1, point2;
    private Login login;
    private SpinnerItem scheme, component;
    private File photoFile;
    private boolean leftLine, rightLine;
    private LayerInfoAdapter layerInfoAdapterSurveyed, layerInfoAdapterDD;
//    private ActionBarDrawerToggle toggle;


//    private double getBearing(@NonNull LatLng start, @NonNull LatLng end) {
//        double lat1 = Math.toRadians(start.latitude);
//        double lon1 = Math.toRadians(start.longitude);
//        double lat2 = Math.toRadians(end.latitude);
//        double lon2 = Math.toRadians(end.longitude);
//
//        double deltaLon = lon2 - lon1;
//
//        double y = Math.sin(deltaLon) * Math.cos(lat2);
//        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon);
//
//        return Math.toDegrees(Math.atan2(y, x));
//    }

    @NonNull
    private LatLng calculatePointAtDistance(@NonNull LatLng start, double bearing) {
        double distance = 0.002;
        double radiusEarth = 6371.0; // Earth's radius in km
        double lat1 = Math.toRadians(start.latitude);
        double lon1 = Math.toRadians(start.longitude);
        double angularDistance = distance / radiusEarth;
        double bearingRad = Math.toRadians(bearing);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(angularDistance) +
                Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(bearingRad));
        double lon2 = lon1 + Math.atan2(
                Math.sin(bearingRad) * Math.sin(angularDistance) * Math.cos(lat1),
                Math.cos(angularDistance) - Math.sin(lat1) * Math.sin(lat2)
        );

        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        login = SqLite.instance(this).getLogin();

        int nop = getIntent().getIntExtra("nop", 0);
        leftLine = nop > 1;
        rightLine = nop > 2;

        binding.cardViewButtonFilter.setOnClickListener(v -> {
            if (this.binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                this.binding.drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                this.binding.drawerLayout.openDrawer(GravityCompat.END);
            }
        });
//        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open_drawer, R.string.close_drawer);
//        binding.drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

//        toggle.getDrawerArrowDrawable().setColor(Color.BLACK);
//        toggle.setHomeAsUpIndicator(R.drawable.ic_filter);

//        if (getIntent().hasExtra(Utility.G_KEY1)) {
//            Point point1 = (Point) getIntent().getSerializableExtra(Utility.G_KEY);
//            Point point2 = (Point) getIntent().getSerializableExtra(Utility.G_KEY1);
//            assert point1 != null;
//            assert point2 != null;
//            this.point1 = new LatLng(point1.getLat(), point1.getLng());
//            this.point2 = new LatLng(point2.getLat(), point2.getLng());
//        }

        scheme = (SpinnerItem) getIntent().getSerializableExtra(Utility.DATA);
        component = (SpinnerItem) getIntent().getSerializableExtra(Utility.G_KEY);

        layerInfoAdapterSurveyed = new LayerInfoAdapter(this, 2, SqLite.instance(this).GET_COMPONENT_ONLY_AM(true, scheme.getKeyString(), SqLite.INT_ALL, component.getKeyString()));
        binding.navigation.listViewS.setAdapter(layerInfoAdapterSurveyed);


        layerInfoAdapterDD = new LayerInfoAdapter(this, 1, SqLite.instance(this).GET_COMPONENT_ONLY_AM(false, scheme.getKeyString(), SqLite.INT_ALL, component.getKeyString()));
        binding.navigation.listViewDD.setAdapter(layerInfoAdapterDD);

        binding.navigation.buttonApplyFilter.setOnClickListener(view -> {
            if (this.binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                this.binding.drawerLayout.closeDrawer(GravityCompat.END);
            }
            showAndClearLocation(false);
        });

//        work = (Work) getIntent().getSerializableExtra("work");
//        setTitle(String.format("Work: %s", work.getName()));

        locations1 = new ArrayList<>();
        locationsF = new ArrayList<>();
        locations2 = new ArrayList<>();

        binding.textViewAccuracy.setText("");

        if (savedInstanceState != null) {
            stop = savedInstanceState.getBoolean("stop", true);
            distance = savedInstanceState.getDouble("distance", 0);
            if (savedInstanceState.containsKey("photoFile")) {
                photoFile = new File(savedInstanceState.getString("photoFile", ""));
            }
            if (savedInstanceState.containsKey("location1")) {
                try {
                    JSONArray jsonArray = new JSONArray(savedInstanceState.getString("location1", ""));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LatLng latLng = new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng"));
                        locations1.add(latLng);
                    }
                } catch (Exception ignore) {

                }
            }
            if (savedInstanceState.containsKey("locationF")) {
                try {
                    JSONArray jsonArray = new JSONArray(savedInstanceState.getString("locationF", ""));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LatLng latLng = new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng"));
                        locationsF.add(latLng);
                    }
                } catch (Exception ignore) {

                }
            }
            if (savedInstanceState.containsKey("location2")) {
                try {
                    JSONArray jsonArray = new JSONArray(savedInstanceState.getString("location2", ""));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        LatLng latLng = new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng"));
                        locations2.add(latLng);
                    }
                } catch (Exception ignore) {

                }
            }
        }

        binding.fab.setOnClickListener(v -> {
            if (v.getTag().toString().equals("1")) {
//                tmp.setText("");
                binding.textViewGuid.setVisibility(View.GONE);
                locations1.clear();
                locationsF.clear();
                locations2.clear();
                distance = 0;
                time = 0;
                startCounter();
//                if (point1 != null) {
//                    if (leftLine) {
//                        locations1.add(this.point1);
//                    }
//                    locationsF.add(this.point1);
//                    if (rightLine) {
//                        locations2.add(this.point1);
//                    }
//                }
                stop = false;
                buildGoogleApiClient();
                showAndClearLocation(true);
                v.setTag("2");
                binding.textView.setText(R.string.stop);
                binding.cardViewButton.setVisibility(View.VISIBLE);

                //: 08-08-2024 :start timer
//                traverseTimeList.clear();
//                traverseDistance = 0;
//                startTimer();
            } else {
                stopUpdate();
                stopCounter();
                v.setTag("1");
                binding.textView.setText(R.string.start);
//                if (!locations.isEmpty()) {
                stop = true;
                showSingleLocation();
//                }
                binding.cardViewButton.setVisibility(View.GONE);

                // TODO: 08-08-2024 : pause timer
                //    pauseTimer();

//               StringBuilder stringBuilder=new StringBuilder();
//                for (Location location : locations) {
//                    stringBuilder.append(location.getLatitude()).append(" : ").append(location.getLongitude()).append("\n");
//                }
//                tmp.setText(stringBuilder.toString());
            }
        });

        binding.buttonRestart.setOnClickListener(v -> {
//            resetTimer();
            showStart();
        });

        binding.buttonResume.setOnClickListener(v -> {
            binding.textViewGuid.setVisibility(View.GONE);
            stop = false;
            buildGoogleApiClient();
            binding.fab.setTag("2");
            binding.textView.setText(R.string.stop);
            binding.cardViewButton.setVisibility(View.VISIBLE);

            binding.frameLayoutFAB.setVisibility(View.VISIBLE);
            binding.linearLayoutButton.setVisibility(View.GONE);
//            startTimer();
            startCounter();
        });

        binding.buttonSave.setOnClickListener(v -> {
            if (locationsF.size() > 2) {
                showPipeDialog();
            } else {
                Toast.makeText(this, "Minimum 2 point needed for save", Toast.LENGTH_SHORT).show();
            }
//            Data data = new Data();
//            data.put("token", work.getToken());
//            StringBuilder stringBuilder = new StringBuilder();
//            for (LatLng location : locations) {
//                if (!stringBuilder.toString().equals("")) {
//                    stringBuilder.append(",");
//                }
//                stringBuilder.append(location.latitude).append(":").append(location.longitude);
//            }
//            data.put("lat_lng", stringBuilder.toString());
//            new WebRequest((response, apiKey) -> {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getBoolean("status")) {
//                        finish();
//                    }
//                    Util.show(this, jsonObject.optString("message"));
//                } catch (Exception ignore) {
//                    Util.show(this, "Unknown error");
//                }
//            }, this, "save-location", data.toString(), getString(R.string.please_wait), 1);
        });

        showStart();

        ActivityResultLauncher<Intent> activityResultPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {// && null != result.getData()
                try {
//                    Bundle extras = result.getData().getExtras();
//                    Bitmap bitmap = (Bitmap) extras.get("data");
                    Bitmap bitmap = Utility.getResizedBitmap(BitmapFactory.decodeFile(photoFile.getAbsolutePath()));
                    //noinspection ResultOfMethodCallIgnored
                    photoFile.delete();
//                    dialogPointBinding.imageViewPhoto.setVisibility(View.VISIBLE);
                    dialogPointBinding.imageViewPhoto.setImageBitmap(bitmap);
                    dialogPointBinding.imageViewPhoto.setTag(Utility.base64(bitmap));
                } catch (Exception e) {
                    Utility.show(this, e);
                    dialogPointBinding.imageViewPhoto.setImageResource(R.drawable.add_image);
//                    dialogPointBinding.imageViewPhoto.setVisibility(View.GONE);
                    dialogPointBinding.imageViewPhoto.setTag(null);
                }
            } else {
                Utility.show(this, Utility.select(this, R.string.photo));
                dialogPointBinding.imageViewPhoto.setImageResource(R.drawable.add_image);
//                dialogPointBinding.imageViewPhoto.setVisibility(View.GONE);
                dialogPointBinding.imageViewPhoto.setTag(null);
            }
        });

        // TODO: 01-08-2024 : here we add points
        binding.cardViewButton.setOnClickListener(view -> {
            if (locationsF.isEmpty()) {
                return;
            }
            if (binding.fab.getTag().toString().equals("2")) {
                binding.fab.performClick();
            }
            LatLng lastLatLng = locationsF.get(locationsF.size() - 1);
            dialogPointBinding = DialogPointBinding.inflate(LayoutInflater.from(this));
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setView(dialogPointBinding.getRoot())
                    .setCancelable(false)
                    .setNegativeButton(R.string.cancel, (d, v) -> d.dismiss())
                    .setPositiveButton(R.string.ok, null)
                    .create();
            alertDialog.show();

            dialogPointBinding.imageViewPhoto.setOnClickListener(v -> {
                photoFile = Utility.createImageFile(this);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, String.format("%s.fileProvider", BuildConfig.APPLICATION_ID), photoFile));
                activityResultPhoto.launch(intent);
            });

            SpinnerManager spinnerManager = new SpinnerManager(dialogPointBinding.linearLayoutComponent, 99, this, SqLite.instance(this).GET_COMPONENT_AM(scheme.getKeyString(), 1));//1 POINT
            if (spinnerManager.getSpinnerItems().size() == 1) {
                set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
            } else {
                setEmpty(dialogPointBinding.linearLayoutPoint);

                dialogPointBinding.editTextPointName.setText("");
                dialogPointBinding.editTextPointName.setEnabled(true);
            }

            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view1 -> {
                if (dialogPointBinding.linearLayoutComponent.getTag() == null) {
                    Utility.show(this, Utility.select(this, R.string.component));
                    return;
                }
                SpinnerItem component = ((SpinnerItem) dialogPointBinding.linearLayoutComponent.getTag());

                if (dialogPointBinding.linearLayoutPoint.getTag() == null) {
                    Utility.show(this, Utility.select(this, R.string.points));
                    return;
                }
                SpinnerItem point = ((SpinnerItem) dialogPointBinding.linearLayoutPoint.getTag());

                String name = dialogPointBinding.editTextPointName.getText().toString();
                if (name.isEmpty()) {
                    Utility.show(this, Utility.check(this, R.string.point_name));
                    return;
                }
                String review = dialogPointBinding.editTextReview.getText().toString();
                if (review.isEmpty()) {
                    Utility.show(this, Utility.check(this, R.string.review));
                    return;
                }
                if (dialogPointBinding.imageViewPhoto.getTag() == null) {
                    Utility.show(this, Utility.select(this, R.string.photo));
                    return;
                }
                String photo = dialogPointBinding.imageViewPhoto.getTag().toString();
                savePoint(alertDialog, lastLatLng, name, review, photo, component, point);
            });
        });

        times.observe(this, s -> binding.textViewTime.setText(s));

        initMap();

//        setTraverseDistanceAndTime();
    }

    public void startCounter() {
        if (isCounting) return;
        isCounting = true;

        counterThread = new Thread(() -> {
            try {
                while (isCounting) {
                    Thread.sleep(1000); // 1 second delay
                    if (times != null) {
                        time++;
                        int m = time / 60;
                        int s = time % 60;
                        int h = m / 60;
                        m = h % 60;
                        times.postValue(String.format(Locale.UK, "%02d:%02d:%02d", h, m, s));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        counterThread.start();
    }

    public void stopCounter() {
        isCounting = false;
        if (counterThread != null) {
            counterThread.interrupt();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("stop", stop);
        outState.putDouble("distance", distance);
        if (photoFile != null) {
            outState.putString("photoFile", photoFile.getAbsolutePath());
        }
        try {
            JSONArray jsonArray = new JSONArray();
            for (LatLng location : locations1) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lat", location.latitude);
                jsonObject.put("lng", location.longitude);
                jsonArray.put(jsonObject);
            }
            outState.putString("location1", jsonArray.toString());
        } catch (Exception ignore) {

        }
        try {
            JSONArray jsonArray = new JSONArray();
            for (LatLng location : locationsF) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lat", location.latitude);
                jsonObject.put("lng", location.longitude);
                jsonArray.put(jsonObject);
            }
            outState.putString("locationF", jsonArray.toString());
        } catch (Exception ignore) {

        }
        try {
            JSONArray jsonArray = new JSONArray();
            for (LatLng location : locations2) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lat", location.latitude);
                jsonObject.put("lng", location.longitude);
                jsonArray.put(jsonObject);
            }
            outState.putString("location2", jsonArray.toString());
        } catch (Exception ignore) {

        }
    }

    private DialogPointBinding dialogPointBinding;

    private void savePoint(@NonNull AlertDialog alertDialog, @NonNull LatLng lastLatLng, @NonNull String name, @NonNull String remark, @NonNull String photo, @NonNull SpinnerItem component, @NonNull SpinnerItem point) {
        SqLite.instance(this).addAsset(new Asset(scheme.getKeyString(), scheme.getValue(), point.getKeyString(), name, component.getKeyString(), component.getValue(), String.valueOf(lastLatLng.latitude), String.valueOf(lastLatLng.longitude), remark, photo, Utility.getCurrentTimeUsingDate()), login.getMobile());
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(R.string.saved_successfully);
        dlgAlert.setTitle(R.string.success);
        dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> {
            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions().position(lastLatLng))).setTitle(name);
            alertDialog.dismiss();
            dialog.dismiss();
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void initMap() {
//        linearLayout.setVisibility(View.GONE);
//        relativeLayout.setVisibility(View.VISIBLE);
        binding.mapView.onCreate(null);
        binding.mapView.onResume();// needed to get the map to display immediately
        binding.mapView.getMapAsync(this::syncMap);
    }

    private void syncMap(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.473324, 77.947998), 6));

    /*
        LatLng latLng = new LatLng(20.5937, 78.9629);
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(latLng, 5);
        googleMap.moveCamera(cameraPosition);
        googleMap.animateCamera(cameraPosition);
*/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//        showAndClearLocation();
    }

//    @Nullable
//    private Bitmap getBitmapFromAssets(String fileName) {
//        AssetManager assetManager = getAssets();
//        InputStream inputStream = null;
//        try {
//            // Open the image file in the assets folder
//            inputStream = assetManager.open(fileName);
//            // Decode the input stream into a Bitmap
//            return BitmapFactory.decodeStream(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (inputStream != null) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private void showAndClearLocation(boolean zoomNeed) {
        if (googleMap == null) {
            return;
        }
        binding.textViewGuid.setVisibility(View.GONE);
        if (stop) {
            binding.frameLayoutFAB.setVisibility(View.GONE);
            binding.linearLayoutButton.setVisibility(View.VISIBLE);
        } else {
            binding.frameLayoutFAB.setVisibility(View.VISIBLE);
            binding.linearLayoutButton.setVisibility(View.GONE);
        }
        googleMap.clear();

        // Define the bounds (as before)
//        LatLngBounds bounds = new LatLngBounds(
//                new LatLng(19.5109358478, 73.74718150856566), // Bottom-left corner (southwest)
//                new LatLng(28.6186014162, 82.85484707696983)  // Top-right corner (northeast)
//        );
        // Load the image from assets
//        Bitmap bitmap = getBitmapFromAssets("mpdistrict_mpgeo.png");


//        TileProvider tileProvider = TileProviderFactory.getTileProvider(TileProviderFactory.LAYER_DIST);
//        this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));

        for (LayerInfo layerInfo : layerInfoAdapterSurveyed.getItems()) {
            if (layerInfo.check) {
                TileProvider tileProvider3 = TileProviderFactory.getTileProvider(layerInfo.component.getSurveyedLayer());
                this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider3));
            }
        }

        for (LayerInfo layerInfo : layerInfoAdapterDD.getItems()) {
            if (layerInfo.check) {
                TileProvider tileProvider3 = TileProviderFactory.getTileProvider(layerInfo.component.getLayer());
                this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider3));
            }
        }

//        if (point1 != null) {
//            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions().position(point1))).setTitle("START");
//        }
//        if (point2 != null) {
//            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions().position(point2))).setTitle("END");
//        }

        LatLng latLng = null;
        LatLng location;

        for (int i = 0; i < locations1.size(); i++) {
            location = locations1.get(i);
            if (latLng != null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.YELLOW);
                polylineOptions.width(15);
                polylineOptions.add(location);
                polylineOptions.add(latLng);
                googleMap.addPolyline(polylineOptions);
            }
            latLng = location;///new LatLng(location.latitude, location.longitude); ////your lat lng
        }

        latLng = null;
        for (int i = 0; i < locations2.size(); i++) {
            location = locations2.get(i);
            if (latLng != null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.CYAN);
                polylineOptions.width(15);
                polylineOptions.add(location);
                polylineOptions.add(latLng);
                googleMap.addPolyline(polylineOptions);
            }
            latLng = location;///new LatLng(location.latitude, location.longitude); ////your lat lng
        }
        latLng = null;

        for (int i = 0; i < locationsF.size(); i++) {
            location = locationsF.get(i);
            if (latLng != null) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(15);
                polylineOptions.add(location);
                polylineOptions.add(latLng);
                googleMap.addPolyline(polylineOptions);
            }
            latLng = location;///new LatLng(location.latitude, location.longitude); ////your lat lng
        }
        if (zoomNeed) {
            if (latLng != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17), 2000, null);
            } else {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.473324, 77.947998), 6), 2000, null);
            }
        }
//        new Handler().postDelayed(() -> {
//            if (bitmap != null) {
//                // Create a GroundOverlayOptions object with the bitmap
//                GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions()
//                        .image(BitmapDescriptorFactory.fromBitmap(bitmap))
//                        .positionFromBounds(bounds)
//                        .transparency(0f); // Optional: Transparency of the overlay
//
//                // Add the overlay to the map
//                GroundOverlay groundOverlay = googleMap.addGroundOverlay(groundOverlayOptions);
//            }
//        }, 2000);
    }

    private void showSingleLocation() {
        if (googleMap == null) {
            return;
        }
        binding.textViewGuid.setVisibility(View.GONE);
        if (stop) {
            binding.frameLayoutFAB.setVisibility(View.GONE);
            binding.linearLayoutButton.setVisibility(View.VISIBLE);
        } else {
            binding.frameLayoutFAB.setVisibility(View.VISIBLE);
            binding.linearLayoutButton.setVisibility(View.GONE);
        }
//        googleMap.clear();

//        for (String layer : TileProviderFactory.ALL_LAYERS) {
//            TileProvider tileProvider3 = TileProviderFactory.getTileProvider(layer);
//            this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider3));
//        }
//
//        if (point1 != null) {
//            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions().position(point1))).setTitle("START");
//        }
//        if (point2 != null) {
//            Objects.requireNonNull(googleMap.addMarker(new MarkerOptions().position(point2))).setTitle("END");
//        }

        int size = locations1.size();
        if (size >= 2) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.YELLOW);
            polylineOptions.width(15);
//            polylineOptions.add(locations1.get(size - 3));
            polylineOptions.add(locations1.get(size - 2));
            polylineOptions.add(locations1.get(size - 1));
            googleMap.addPolyline(polylineOptions);
        }
        size = locations2.size();
        if (size >= 2) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.CYAN);
            polylineOptions.width(15);
//            polylineOptions.add(locations2.get(size - 3));
            polylineOptions.add(locations2.get(size - 2));
            polylineOptions.add(locations2.get(size - 1));
            googleMap.addPolyline(polylineOptions);
        }
        size = locationsF.size();
        if (size >= 2) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.BLUE);
            polylineOptions.width(15);
            polylineOptions.add(locationsF.get(size - 2));
            polylineOptions.add(locationsF.get(size - 1));
            googleMap.addPolyline(polylineOptions);
        }
        binding.textViewDistance.setText(Utility.showDist(distance));
    }

//    private void showRout(@NonNull String points, @NonNull String start, @NonNull String end) {
//        String url = getMapsApiDirectionsUrl(points, start, end);
//        new WebRequest((response, apiKey) -> {
//            try {
//                ParserTask parserTask = new ParserTask(googleMap, ContextCompat.getColor(this, R.color.colorPrimary));
//                parserTask.execute(response);
//            } catch (Exception ignore) {
//            }
//        }, this, url, null, null, 1);
//    }

//    private String getMapsApiDirectionsUrl(@NonNull String points, @NonNull String start, @NonNull String end) {
//        return String.format("https://maps.googleapis.com/maps/api/directions/json?key=%s&sensor=false&origin=%s&destination=%s&waypoints=%s", getString(R.string.api_key), start, end, points);
//    }

    private void showStart() {

//        traverseTimeList.clear();
//        traverseDistance = 0;
//        setTraverseDistanceAndTime();
        binding.frameLayoutFAB.setVisibility(View.VISIBLE);
        binding.linearLayoutButton.setVisibility(View.GONE);
        binding.textViewGuid.setVisibility(View.VISIBLE);
    }

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    private void buildGoogleApiClient() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SettingsClient mSettingsClient = LocationServices.getSettingsClient(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
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
                            rae.startResolutionForResult(UpdateLocationActivity.this, REQUEST_CHECK_SETTINGS);
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
                if(location != null){
                    binding.textViewAccuracy.setText(String.valueOf(location.getAccuracy()));
                }
                if (Utility.isCorrectLocation(location,15)) {

                    if (stop) return;
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    if (leftLine) {
                        if (!locationsF.isEmpty()) {
//                                locations1.add(latLng);
//                                if (rightLine) {
//                                    locations2.add(latLng);
//                                }
//                            } else {
                            LatLng last = locationsF.get(locationsF.size() - 1);
                            double bearing = 0;//getBearing(last, latLng);
                            locations1.add(calculatePointAtDistance(last, bearing + 90));//, 0.002
                            locations1.add(calculatePointAtDistance(latLng, bearing + 90));

                            if (rightLine) {
                                locations2.add(calculatePointAtDistance(last, bearing - 90));
                                locations2.add(calculatePointAtDistance(latLng, bearing - 90));
                            }
                        }
                    }


                    if (!locationsF.isEmpty()) {
                        distance = distance + Utility.distance(latLng, locationsF.get(locationsF.size() - 1));
                    }
                    locationsF.add(latLng);

                    // 08-08-2024 : here we set travers distance and time
//                    traverseTimeList.add(new Date());
//                    setTraverseDistanceAndTime();
//                        stop = false;

//                        if (point2 != null) {
//                            if (Utility.distance(latLng, point2) <= 15) {
//                                locationsF.add(point2);
//                                binding.fab.performClick();
//                                return;
//                            }
//                        }
                    showSingleLocation();
//                        showLocation();
//                        tmp.setText(String.format(Locale.UK, "%s\n%d. --> %f:%f", tmp.getText(), locations.size(), latLng.latitude, latLng.longitude));
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 214) {
            buildGoogleApiClient();
        } else if (requestCode == 100) {
            if (data != null && resultCode == RESULT_OK) {
                set(data.getIntExtra("requestCode", 0), (SpinnerItem) data.getSerializableExtra("spinnerItem"));
            }
        }
    }

//    private boolean isCorrect(@Nullable Location location) {
//        if (location == null) return false;
//        binding.textViewAccuracy.setText(String.valueOf(location.getAccuracy()));
////        Log.e("GPSA", location.getAccuracy() + "");
////        return location.getAccuracy() <= 0.3;
//        return location.getAccuracy() <= 15;
////        for (LatLng latLng1 : locations) {
////            if (Utility.distance(latLng, latLng1) < 100f) {
////                return false;
////            }
////        }
//    }

    private void stopUpdate() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mFusedLocationClient = null;
        }
    }

    private void connectGoogleClient() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            mGoogleApiClient.connect();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {
        if (mFusedLocationClient != null)
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUpdate();
    }

    @Override
    public void onBackPressed() {
        if (this.binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            this.binding.drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            Intent data = new Intent();
            data.putExtra(Utility.G_KEY, "Back Pressed");
            setResult(RESULT_CANCELED, data);
            super.onBackPressed();
            finish();
        }
    }

    private DialogPipeBinding dialogPipeBinding;
    private JSONArray jsonArray;
    private void showPipeDialog() {
        int total = leftLine && rightLine ? 3 : (leftLine ? 2 : 1);
        jsonArray = new JSONArray();
        dialogPipeBinding = DialogPipeBinding.inflate(LayoutInflater.from(this));
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(dialogPipeBinding.getRoot())
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.close, (d, v) -> d.dismiss())
                .setCancelable(false)
                .create();
        alertDialog.show();
        if (total == 1) {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.ok);
        } else {
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.next);
        }

        dialogPipeBinding.textViewTitle.setText(String.format("%s Of Line 1", getString(R.string.give_pipe_parameter)));

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
            SpinnerItem depth, material, diameter, materialInfo;


            if (dialogPipeBinding.linearLayoutPipeDepthCover.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.depth_cover));
                return;
            }
            depth = ((SpinnerItem) dialogPipeBinding.linearLayoutPipeDepthCover.getTag());

            if (depth.getValue().equalsIgnoreCase("-- ADD NEW --")) {
                String inputDepth = dialogPipeBinding.editTextDepthCover.getText().toString();
                if (inputDepth.isEmpty()) {
                    Utility.show(this, "Enter Depth/Cover");
                    return;
                }
                depth = new SpinnerItem("0", inputDepth);
            }


            if (dialogPipeBinding.linearLayoutMaterialType.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.material_type));
                return;
            }
            material = ((SpinnerItem) dialogPipeBinding.linearLayoutMaterialType.getTag());
            if (dialogPipeBinding.linearLayoutPipeDiameter.getTag() == null) {
                Utility.show(this, Utility.select(this, R.string.pipe_diameter));
                return;
            }
            diameter = ((SpinnerItem) dialogPipeBinding.linearLayoutPipeDiameter.getTag());

            if (diameter.getValue().equalsIgnoreCase("-- ADD NEW --")) {
                String inputDia = dialogPipeBinding.editTextPipeDiamter.getText().toString();
                if (inputDia.isEmpty()) {
                    Utility.show(this, "Enter Pipe Diameter");
                    return;
                }
                diameter = new SpinnerItem("0", inputDia);
            }

            String alignment = dialogPipeBinding.editTextAlignmentName.getText().toString();
            if (alignment.isEmpty()) {
                Utility.show(this, Utility.check(this, R.string.alignment_name));
                return;
            }
            if (dialogPipeBinding.linearLayoutExtra.getTag() == null) {
                Utility.show(this, Utility.select(this, material.getExtra().toString()));
                return;
            }
            materialInfo = ((SpinnerItem) dialogPipeBinding.linearLayoutExtra.getTag());

            try {
                jsonArray.put(getIntentJO(depth, material, diameter, materialInfo.getValue(), alignment));

                if (jsonArray.length() == total) {
                    alertDialog.dismiss();
                    saveDialog();
                } else if (jsonArray.length() == 1) {
                    String msg = String.format("%s Of Line 2", getString(R.string.give_pipe_parameter));
                    dialogPipeBinding.textViewTitle.setText(msg);
                    Utility.show(this, msg);

                    if (total == 2) {
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.ok);
                    } else {
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.next);
                    }
                } else if (jsonArray.length() == 2) {
                    String msg = String.format("%s Of Line 3", getString(R.string.give_pipe_parameter));
                    dialogPipeBinding.textViewTitle.setText(msg);
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.ok);
                }
            } catch (Exception e) {
                Intent data = new Intent();
                data.putExtra(Utility.G_KEY, e.getMessage());
                setResult(RESULT_CANCELED, data);
                finish();
            }
        });
        List<SpinnerItem> depthCoverList = new ArrayList<>();
        depthCoverList.add(new SpinnerItem("0", "-- ADD NEW --"));
        depthCoverList.addAll(SqLite.instance(this).GET_DEPTH_COVER());
//        new SpinnerManager(dialogPipeBinding.linearLayoutPipeDepthCover, 6, this, SqLite.instance(this).GET_DEPTH_COVER());
        new SpinnerManager(dialogPipeBinding.linearLayoutPipeDepthCover, 6, this, depthCoverList);
        new SpinnerManager(dialogPipeBinding.linearLayoutMaterialType, 7, this, SqLite.instance(this).GET_MATERIAL_TYPE());
        new SpinnerManager(dialogPipeBinding.linearLayoutAlignment, 9, this, SqLite.instance(this).GET_ALIGNMENT());
    }

    private void saveDialog() {
        DialogFinalBinding dialogFinalBinding = DialogFinalBinding.inflate(LayoutInflater.from(this));
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(dialogFinalBinding.getRoot())
                .setPositiveButton(R.string.ok, null)
//                .setNegativeButton(R.string.close, (d, v1) -> d.dismiss())
                .setCancelable(false)
                .create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
            String review = dialogFinalBinding.editTextReview.getText().toString();
            if (review.isEmpty()) {
                Utility.show(this, Utility.check(this, R.string.review));
                return;
            }
            String diameter, material, depth_cover, alignment, materialInfoName, materialInfoValue, line_json;
            int totalLine = jsonArray.length();

            LatLng lastLatLng = locationsF.get(locationsF.size() - 1);
            for (int j = 0; j < totalLine; j++) {
                JSONObject jsonObjectTraverse = jsonArray.optJSONObject(j);

                diameter = jsonObjectTraverse.optString("diameter");//binding.editTextPipeDiameter.getText().toString();
                material = jsonObjectTraverse.optString("material");//binding.editTextMaterialType.getText().toString();
                depth_cover = jsonObjectTraverse.optString("depth");
                alignment = jsonObjectTraverse.optString("alignment", "");
                materialInfoName = jsonObjectTraverse.optString("materialInfoName");
                materialInfoValue = jsonObjectTraverse.optString("materialInfoValue");
                JSONArray jsonArray = jsonObjectTraverse.optJSONArray("traverse");
                if (jsonArray == null) {
                    Utility.show(this, R.string.traverse_msg);
                    return;
                }
                line_json = jsonArray.toString();
                SqLite.instance(this).addAsset(new Asset(0, "0", scheme.getKeyString(), scheme.getValue(), "", "", "", "", component.getKeyString(), component.getValue(), String.valueOf(lastLatLng.latitude), String.valueOf(lastLatLng.longitude), totalLine == 1 ? review : String.format(Locale.UK, "%s - (LINE NO %d)", review, j + 1), "", Utility.getCurrentTimeUsingDate(), depth_cover, diameter, material, alignment, materialInfoName, materialInfoValue, line_json, false), login.getMobile());
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage(R.string.saved_successfully);
            dlgAlert.setTitle(R.string.success);
            dlgAlert.setPositiveButton(R.string.ok, (dialog, which) -> finish());
            dlgAlert.setCancelable(false);
            alertDialog.dismiss();
            dlgAlert.create().show();

//            setResult(RESULT_OK, null);
//            finish();
        });
//        Intent data = new Intent();
//        data.putExtra(Utility.G_KEY, "Saved Line");
//        data.putExtra(Utility.G_KEY1, jsonArray.toString());
//        setResult(RESULT_OK, data);
//        finish();
    }

    @NonNull
    private JSONObject getIntentJO(@NonNull SpinnerItem depth, @NonNull SpinnerItem material, @NonNull SpinnerItem diameter, @NonNull String materialInfoValue, @NonNull String alignment) throws JSONException {
        JSONObject jsonObjectMain = new JSONObject();
        jsonObjectMain.put("depth", depth.getValue());
        jsonObjectMain.put("material", material.getValue());
        jsonObjectMain.put("diameter", diameter.getValue());
        jsonObjectMain.put("materialInfoName", material.getExtra().toString());
        jsonObjectMain.put("materialInfoValue", materialInfoValue);
        jsonObjectMain.put("alignment", alignment);
        int type = this.jsonArray.length();

        JSONArray jsonArray = new JSONArray();
        for (LatLng location : (type == 0 ? locationsF : (type == 1 ? locations1 : locations2))) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("lat", location.latitude);
            jsonObject.put("lng", location.longitude);
            jsonArray.put(jsonObject);
        }
        jsonObjectMain.put("traverse", jsonArray);
        return jsonObjectMain;
    }

    private void set(int code, SpinnerItem spinnerItem) {
//        SpinnerManager spinnerManager;
        switch (code) {
            case 6:
                Log.e("tag", "NEW DEPTH COVER === " + spinnerItem.getValue());

                if (spinnerItem.getValue().equalsIgnoreCase("-- ADD NEW --")) {
                    dialogPipeBinding.editTextDepthCover.setVisibility(View.VISIBLE);
                } else {
                    dialogPipeBinding.editTextDepthCover.setVisibility(View.GONE);
                }

                ((TextView) dialogPipeBinding.linearLayoutPipeDepthCover.getChildAt(0)).setText(spinnerItem.getValue());
                dialogPipeBinding.linearLayoutPipeDepthCover.setTag(spinnerItem);
                break;
            case 7:
                ((TextView) dialogPipeBinding.linearLayoutMaterialType.getChildAt(0)).setText(spinnerItem.getValue());
                dialogPipeBinding.linearLayoutMaterialType.setTag(spinnerItem);
                setEmpty(dialogPipeBinding.linearLayoutPipeDiameter);
                setEmpty(dialogPipeBinding.linearLayoutExtra);
                dialogPipeBinding.textViewExtra.setText(spinnerItem.getExtra().toString());
                List<SpinnerItem> pipeDiameterList = new ArrayList<>();
                pipeDiameterList.add(new SpinnerItem("0", "-- ADD NEW --"));
                pipeDiameterList.addAll(SqLite.instance(this).GET_PIPE_DIAMETER(spinnerItem.getKeyString()));
                new SpinnerManager(dialogPipeBinding.linearLayoutPipeDiameter, 8, this, pipeDiameterList);
                new SpinnerManager(dialogPipeBinding.linearLayoutExtra, 10, this, SqLite.instance(this).GET_MATERIAL_INFO_VALUE(spinnerItem.getKeyString()));
                break;
            case 8:
                if (spinnerItem.getValue().equalsIgnoreCase("-- ADD NEW --")) {
                    dialogPipeBinding.editTextPipeDiamter.setVisibility(View.VISIBLE);
                } else {
                    dialogPipeBinding.editTextPipeDiamter.setVisibility(View.GONE);
                }
                ((TextView) dialogPipeBinding.linearLayoutPipeDiameter.getChildAt(0)).setText(spinnerItem.getValue());
                dialogPipeBinding.linearLayoutPipeDiameter.setTag(spinnerItem);
                break;
            case 9:
                ((TextView) dialogPipeBinding.linearLayoutAlignment.getChildAt(0)).setText(spinnerItem.getValue());
                dialogPipeBinding.linearLayoutAlignment.setTag(spinnerItem);
                dialogPipeBinding.editTextAlignmentName.setText(spinnerItem.getKeyString());
                if (spinnerItem.getKeyString().isEmpty()) {
                    dialogPipeBinding.textViewAlignmentName.setVisibility(View.VISIBLE);
                    dialogPipeBinding.editTextAlignmentName.setVisibility(View.VISIBLE);
                } else {
                    dialogPipeBinding.textViewAlignmentName.setVisibility(View.GONE);
                    dialogPipeBinding.editTextAlignmentName.setVisibility(View.GONE);
                }
                break;
            case 10:
                ((TextView) dialogPipeBinding.linearLayoutExtra.getChildAt(0)).setText(spinnerItem.getValue());
                dialogPipeBinding.linearLayoutExtra.setTag(spinnerItem);
                break;
            case 99:
                ((TextView) dialogPointBinding.linearLayoutComponent.getChildAt(0)).setText(spinnerItem.getValue());
                dialogPointBinding.linearLayoutComponent.setTag(spinnerItem);
                List<SpinnerItem> list = new ArrayList<>();
                list.add(new SpinnerItem("0", "-- ADD NEW --"));
                list.addAll(SqLite.instance(this).GET_POINTS(scheme.getKeyString(), spinnerItem.getKeyString(), null));
                SpinnerManager spinnerManager = new SpinnerManager(dialogPointBinding.linearLayoutPoint, 999, this, list);
                set(spinnerManager.getRequestCode(), spinnerManager.getSpinnerItems().get(0));
                break;
            case 999:
                ((TextView) dialogPointBinding.linearLayoutPoint.getChildAt(0)).setText(spinnerItem.getValue());
                dialogPointBinding.linearLayoutPoint.setTag(spinnerItem);
                if (spinnerItem.getKeyString().equals("0")) {
                    dialogPointBinding.editTextPointName.setText("");
                    dialogPointBinding.editTextPointName.setEnabled(true);
                } else {
                    dialogPointBinding.editTextPointName.setText(spinnerItem.getValue());
                    dialogPointBinding.editTextPointName.setEnabled(false);
                }
                break;
        }
    }

    private void setEmpty(@NonNull LinearLayout linearLayout) {
        ((TextView) linearLayout.getChildAt(0)).setText(R.string.select);
        linearLayout.setTag(null);
    }


}