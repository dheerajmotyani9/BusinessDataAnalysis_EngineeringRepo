package mapitgis.jalnigam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Utility class for easy access to the device location on Android
 */
public class MyLocation {

    private final Context context;
    private final LocListener mListener;
//    private final boolean mPassive = false;
//    private final long mInterval = 0;
    private final boolean mRequireLastLocation;
    private final FusedLocationProviderClient fusedLocationClient;
    private int mBlurRadius = 0;
    private LocationCallback locationCallback;
    private Location mPosition;
    private static final double SQUARE_ROOT_TWO = Math.sqrt(2.0);
    private static final int LOCATION_SETTING_REQUEST_CODE = 5;
//    private static final String PROVIDER_COARSE = LocationManager.NETWORK_PROVIDER;
//    private static final String PROVIDER_FINE = LocationManager.GPS_PROVIDER;
//    private static final String PROVIDER_FINE_PASSIVE = LocationManager.PASSIVE_PROVIDER;
//    private static final long INTERVAL_DEFAULT = 10 * 60 * 1000;
    private static final double KILOMETER_TO_METER = 1000.0;
    private static final float LATITUDE_TO_KILOMETER = 111.133f;
    private static final float LONGITUDE_TO_KILOMETER_AT_ZERO_LATITUDE = 111.320f;
//    private static final int REQUEST_CHECK_SETTINGS = 11;
    private static final Random mRandom = new Random();
//    private static Location mCachedPosition;
    private final LocationRequest locationRequest;

    public MyLocation(Context context, LocationRequest locationRequest, boolean requireLastLocation, LocListener listener) {
        this.context = context;
        this.mRequireLastLocation = requireLastLocation;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.locationRequest = (locationRequest != null) ? locationRequest : new LocationRequest();
        this.locationRequest.setInterval(10000);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        this.mListener = listener;

        if (mRequireLastLocation) {
            cachedPosition();
        }
    }

    public MyLocation(Context context, boolean requireLastLocation, LocListener listener) {
        this(context, null, requireLastLocation, listener);
    }

//    public void setListener(LocListener listener) {
//        mListener = listener;
//    }

    public boolean hasLocationEnabled() {
        try {
            int locationMode;
            String locationProviders;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
                return locationMode != Settings.Secure.LOCATION_MODE_OFF;
            } else {
                locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                return !TextUtils.isEmpty(locationProviders);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startLocation() {
        checkLocationSetting();
    }

    @SuppressLint("MissingPermission")
    private void beginUpdates() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    mListener.locationCancelled();
                } else {
                    for (Location location : locationResult.getLocations()) {
                        mListener.currentLocation(location);
                    }
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    @SuppressLint("MissingPermission")
    public void endUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private Location blurWithRadius(Location originalLocation) {
        if (mBlurRadius <= 0) {
            return originalLocation;
        } else {
            Location newLocation = new Location(originalLocation);
            double blurMeterLong = calculateRandomOffset(mBlurRadius) / SQUARE_ROOT_TWO;
            double blurMeterLat = calculateRandomOffset(mBlurRadius) / SQUARE_ROOT_TWO;
            newLocation.setLongitude(newLocation.getLongitude() + meterToLongitude(blurMeterLong, newLocation.getLatitude()));
            newLocation.setLatitude(newLocation.getLatitude() + meterToLatitude(blurMeterLat));
            return newLocation;
        }
    }

    public Point getPosition() {
        return (mPosition == null) ? null : new Point(blurWithRadius(mPosition).getLatitude(), blurWithRadius(mPosition).getLongitude());
    }

    public double getLatitude() {
        return (mPosition == null) ? 0.0 : blurWithRadius(mPosition).getLatitude();
    }

    public double getLongitude() {
        return (mPosition == null) ? 0.0 : blurWithRadius(mPosition).getLongitude();
    }

    public float getSpeed() {
        return (mPosition != null) ? mPosition.getSpeed() : 0.0f;
    }

    public double getAltitude() {
        return (mPosition != null) ? mPosition.getAltitude() : 0.0;
    }

    public void setBlurRadius(int blurRadius) {
        mBlurRadius = blurRadius;
    }

    @SuppressLint("MissingPermission")
    private void cachedPosition() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                mListener.currentLocation(location);
            } else {
                checkLocationSetting();
                beginUpdates();
                endUpdates();
            }
        });
    }

//    @Deprecated
//    private void cachePosition() {
//        if (mPosition != null) {
//            mCachedPosition = mPosition;
//        }
//    }

    public static class Point implements Parcelable {
        private final double latitude;
        private final double longitude;

        public Point(double lat, double lon) {
            latitude = lat;
            longitude = lon;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        private Point(Parcel in) {
            latitude = in.readDouble();
            longitude = in.readDouble();
        }

        @Override
        public String toString() {
            return "(" + latitude + ", " + longitude + ")";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeDouble(latitude);
            out.writeDouble(longitude);
        }

        public static final Parcelable.Creator<Point> CREATOR = new Parcelable.Creator<Point>() {
            @Override
            public Point createFromParcel(Parcel in) {
                return new Point(in);
            }

            @Override
            public Point[] newArray(int size) {
                return new Point[size];
            }
        };
    }

    public void onActivityResult(int result) {
        if (result == Activity.RESULT_OK) {
            mListener.locationOn();
            beginUpdates();
        } else if (result == Activity.RESULT_CANCELED) {
            mListener.locationCancelled();
        }
    }

    private void checkLocationSetting() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.setAlwaysShow(true);
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();
        LocationServices.getSettingsClient(context).checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(locationSettingsResponse -> {
                    if (mRequireLastLocation) {
                        beginUpdates();
                        endUpdates();
                    } else {
                        beginUpdates();
                    }
                })
                .addOnFailureListener(e -> {
                    if (e instanceof ResolvableApiException) {
                        try {
                            ((ResolvableApiException) e).startResolutionForResult((Activity) context, LOCATION_SETTING_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException sendEx) {
                            sendEx.printStackTrace();
                        }
                    }
                });
    }

    private static int calculateRandomOffset(int radius) {
        return mRandom.nextInt((radius + 1) * 2) - radius;
    }

    public static void openSettings(Context context) {
        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public static double latitudeToKilometer(double latitude) {
        return latitude * LATITUDE_TO_KILOMETER;
    }

    public static double kilometerToLatitude(double kilometer) {
        return kilometer / latitudeToKilometer(1.0);
    }

    public static double latitudeToMeter(double latitude) {
        return latitudeToKilometer(latitude) * KILOMETER_TO_METER;
    }

    public static double meterToLatitude(double meter) {
        return meter / latitudeToMeter(1.0);
    }

    public static double longitudeToKilometer(double longitude, double latitude) {
        return longitude * LONGITUDE_TO_KILOMETER_AT_ZERO_LATITUDE * Math.cos(Math.toRadians(latitude));
    }

    public static double kilometerToLongitude(double kilometer, double latitude) {
        return kilometer / longitudeToKilometer(1.0, latitude);
    }

    public static double longitudeToMeter(double longitude, double latitude) {
        return longitudeToKilometer(longitude, latitude) * KILOMETER_TO_METER;
    }

    public static double meterToLongitude(double meter, double latitude) {
        return meter / longitudeToMeter(1.0, latitude);
    }

    public static double calculateDistance(Point start, Point end) {
        return calculateDistance(start.getLatitude(), start.getLongitude(), end.getLatitude(), end.getLongitude());
    }

    public static double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        float[] results = new float[3];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
        return results[0];
    }

    @NonNull
    public static String getAddress(Context context, Double latitude, Double longitude, boolean country, boolean fullAddress) {
        String add = "";
        Geocoder geoCoder = new Geocoder(((Activity) context).getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                if (country) {
                    add = addresses.get(0).getCountryName();
                } else if (fullAddress) {
                    add = addresses.get(0).getFeatureName() + "," +
                            addresses.get(0).getSubLocality() + "," +
                            addresses.get(0).getSubAdminArea() + "," +
                            addresses.get(0).getPostalCode() + "," +
                            addresses.get(0).getCountryName();
                } else {
                    add = addresses.get(0).getLocality();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return add.replace(",null", "");
    }
}