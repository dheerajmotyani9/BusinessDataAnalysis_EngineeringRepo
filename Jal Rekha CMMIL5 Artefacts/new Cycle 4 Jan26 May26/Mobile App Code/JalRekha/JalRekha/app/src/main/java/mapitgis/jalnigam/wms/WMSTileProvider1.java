package mapitgis.jalnigam.wms;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.TileProvider;

public abstract class WMSTileProvider1 implements TileProvider {
    protected static final int MINX = 0;
    protected static final int MAXX = 1;
    protected static final int MINY = 2;
    protected static final int MAXY = 3;

    @NonNull
    public static double[] getBoundingBox(int x, int y, int zoom) {
        Log.i("TRACERT", "X : " + x + " | Y : " + y + " | Z : " + zoom);
        Log.i("TRACERT", "------------");
        double minLongitude = tileXToLongitude(x, zoom);
        double maxLongitude = tileXToLongitude(x + 1, zoom);
        double minLatitude = tileYToLatitude(y + 1, zoom);
        double maxLatitude = tileYToLatitude(y, zoom);
//        if(){
//
//        }

        return new double[]{minLongitude, maxLongitude, minLatitude, maxLatitude};
    }

    public static double tileXToLongitude(int x, int zoom) {
        return x / Math.pow(2.0, zoom) * 360.0 - 180.0;
    }

    // Convert Y coordinate to Latitude
    public static double tileYToLatitude(int y, int zoom) {
        double n = Math.PI - 2.0 * Math.PI * y / Math.pow(2.0, zoom);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }
}