package mapitgis.jalnigam;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Objects;

import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.ActivityTraverseBinding;

public class TraversePathActivity extends BaseActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityTraverseBinding binding;
    private Location location;
    private MyLocation myLocation;
    private final ArrayList<LatLng> listLatLng = new ArrayList<>();
    private boolean isTrackingOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTraverseBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appbar.toolbar);//findViewById(R.id.toolbar)
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.start_traverse));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_traverse);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        initUI();
    }

    protected void initUI() {
        binding.layoutStartStop.setOnClickListener(view -> startTracking());
        binding.layoutRestartSave.setOnClickListener(view -> {
            isTrackingOn = false;
            drawPolygon();
            binding.layoutStartStop.setVisibility(View.GONE);
            binding.layoutRestartSave.setVisibility(View.VISIBLE);
            binding.countDown.stop();
        });
        binding.btnStart.setOnClickListener(view -> {
            binding.layoutStartStop.setVisibility(View.VISIBLE);
            binding.layoutRestartSave.setVisibility(View.GONE);
            startTracking();
        });
        binding.btnSave.setOnClickListener(view -> {
            if (listLatLng.size() >= 3) {
//                String json = new Gson().toJson(listLatLng);
                Intent intent = new Intent();
//                intent.putExtra("POLYGON", json);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Utility.show(this,"Error MESSAGE");
            }
        });
    }

    private void startTracking() {
        listLatLng.clear();
        if (mMap != null) mMap.clear();
        isTrackingOn = true;
        binding.layoutStartStop.setEnabled(false);
        binding.layoutRestartSave.setEnabled(true);
        binding.countDown.setBase(SystemClock.elapsedRealtime());
        binding.countDown.start();
    }

    private final LocListener locationListener = new LocListener() {
        @Override
        public void currentLocation(@NonNull Location _location) {
            if (location == null) {
                LatLng latLng = new LatLng(_location.getLatitude(), _location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18f));
            }
            runOnUiThread(() -> {
                String result = MyLocation.getAddress(TraversePathActivity.this, _location.getLatitude(),_location.getLongitude(),false,true);
                String latLng = "\n" + _location.getLatitude() + " , " + _location.getLongitude();
                binding.tvAddress.setText(String.format("%s%s", result, latLng));
            });
            if (isTrackingOn) {
                listLatLng.add(new LatLng(_location.getLatitude(), _location.getLongitude()));
                mMap.addPolyline(new PolylineOptions()
                        .addAll(listLatLng)
                        .width(4f)
                        .color(getResources().getColor(R.color.red)));
            }
            location = _location;
        }

        @Override
        public void locationCancelled() {

        }

        @Override
        public void locationOn() {

        }
    };

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        LocationRequest request = new LocationRequest();
        request.setInterval(4000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        myLocation = new MyLocation(this, request, false, locationListener);
        myLocation.startLocation();
    }

    @Override
    protected void onDestroy() {
        myLocation.endUpdates();
        super.onDestroy();
    }

    private void drawPolygon() {
        if (listLatLng.isEmpty()) {
            Utility.show(this,"Error MESSAGE");
            return;
        }
        if (listLatLng.size() >= 3) {
            mMap.clear();
            mMap.addPolygon(new PolygonOptions()
                    .addAll(listLatLng)
                    .strokeWidth(4f)
                    .strokeColor(getResources().getColor(R.color.red))
                    .fillColor(getResources().getColor(R.color.green)));
//            LatLng polyCenter = MUtil.getPolygonCenterPoint(listLatLng);
//            if (polyCenter != null) {
//                double polyArea = SphericalUtil.computeArea(listLatLng);
//                addText(polyCenter, "Area: " + String.format("%.4f", polyArea), 8, 14);
//            }
        }
    }

//    private void addText(LatLng location, String text, int padding, int fontSize) {
//        try {
//            TextView textView = new TextView(getApplicationContext());
//            textView.setText(text);
//            textView.setTextSize(fontSize);
//            Paint paintText = textView.getPaint();
//            Rect boundsText = new Rect();
//            paintText.getTextBounds(text, 0, textView.length(), boundsText);
//            paintText.setTextAlign(Paint.Align.CENTER);
//            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//            Bitmap bmpText = Bitmap.createBitmap(boundsText.width() + 2 * padding, boundsText.height() + 2 * padding, conf);
//            Canvas canvasText = new Canvas(bmpText);
//            paintText.setColor(Color.BLACK);
//            canvasText.drawText(text, canvasText.getWidth() / 2, canvasText.getHeight() - padding - boundsText.bottom, paintText);
//            MarkerOptions markerOptions = new MarkerOptions()
//                    .position(location)
//                    .icon(BitmapDescriptorFactory.fromBitmap(bmpText))
//                    .anchor(0.5f, 1f);
//            mMap.addMarker(markerOptions);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}


