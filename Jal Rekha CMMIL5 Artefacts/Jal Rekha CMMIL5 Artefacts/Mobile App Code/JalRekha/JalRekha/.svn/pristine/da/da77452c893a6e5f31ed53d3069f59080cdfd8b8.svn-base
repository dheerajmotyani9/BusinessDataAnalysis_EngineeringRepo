package mapitgis.jalnigam.wms;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;


public class TileProviderFactory {
    public final static int SIZE_OF_TILE = 500;
//    public final static String[] ALL_LAYERS = {"adminbdy:intake_well", "adminbdy:oht", "adminbdy:bpt", "adminbdy:mbr", "adminbdy:junction", "adminbdy:wtp", "adminbdy:valve"
//            , "adminbdy:cwgm", "adminbdy:distribution_pipe", "adminbdy:cwpm", "adminbdy:rwpm", "adminbdy:pmbr"};

//    public final static String LAYER_1 = "adminbdy:cwgm";
//    public final static String LAYER_2 = "adminbdy:distribution_pipe";
//    public final static String LAYER_3 = "adminbdy:cwpm";
    public final static String LAYER_DIST = "adminbdy:mpdistrict";
//    public final static String MP_BB = "73.74718150856566,20.93212582492597,82.85484707696983,27.197411439107817";
    public final static String MP_BB = "73.74718150856566,19.5109358478,82.85484707696983,28.6186014162";

    private final static String GEOSERVER_FORMAT =
            "https://jalrekha.mp.gov.in/geoproxy/geourl/wms" +
                    "?service=WMS" +
                    "&version=1.1.1" +
                    "&request=GetMap" +
                    "&layers=%s" +
                    "&bbox=%f,%f,%f,%f" +
                    "&width="+SIZE_OF_TILE +
                    "&height="+SIZE_OF_TILE +
                    "&srs=EPSG:4326" +
                    "&format=image%%2Fpng" +
                    "&transparent=true";//900913

    private final static String GEOSERVER_IMAGE_FORMAT =
            "https://jalrekha.mp.gov.in/geoproxy/geourl/wms" +
                    "?service=WMS" +
                    "&version=1.1.1" +
                    "&request=GetLegendGraphic" +
                    "&layer=%s" +
                    "&width=20" +
                    "&height=20" +
                    "&format=image%%2Fpng";

    @NonNull
    public static TileProvider getTileProvider(@NonNull String layer) {
        return getTileProvider(layer, null);
    }

    @NonNull
    public static TileProvider getTileProvider(@NonNull String layer, @Nullable String extra) {
//        return new WMSTileProvider(256, 256) {
//            @Override
//            public synchronized URL getTileUrl(int x, int y, int zoom) {
//                try {
//                    double[] box = getBoundingBox(x, y, zoom);
//                    String s;
//                    if (extra != null) {
//                        s = String.format(Locale.US, GEOSERVER_FORMAT, URLEncoder.encode(layer, "UTF-8") + "&" + extra, box[MINX], box[MINY], box[MAXX], box[MAXY]);
//                    } else {
//                        s = String.format(Locale.US, GEOSERVER_FORMAT, URLEncoder.encode(layer, "UTF-8"), box[MINX], box[MINY], box[MAXX], box[MAXY]);
//                    }
////                    Log.e("URL", s);
//                    URL url = new URL(s);
//                    url.openConnection().addRequestProperty("Referer", "https://geoportal.mp.gov.in/MPPHE_New/");
//                    return url;
//                } catch (Exception e) {
//                    throw new AssertionError(e);
//                }
//            }
//        };
        return new WMSTileProvider1() {
            @Nullable
            @Override
            public Tile getTile(int x, int y, int zoom) {
//                if (!checkTileExists(x, y, zoom)) {
//                    return null;
//                }

                try {
                    if(layer.equals(LAYER_DIST)) {
                        if (zoom < 5) {
                            return null;
                        }
                    }else{
                        if (zoom < 10) {
                            return null;
                        }
                    }
                    Log.e("ABCD", "ZOOOOM Level " + zoom);
                    double[] box = getBoundingBox(x, y, zoom);
                    String s = String.format(Locale.US, GEOSERVER_FORMAT, URLEncoder.encode(layer, "UTF-8"), box[MINX], box[MINY], box[MAXX], box[MAXY]);

                    Log.e("ABCD", "ZOOOOM Level " + s);
//                    URL tileUrl = new URL(s);
                    //Download the PNG as byte[], I suggest using OkHTTP library or see next code!4
                    final byte[] data = downloadData(s);
//                    final int height = SIZE_OF_TILE;
//                    final int width = SIZE_OF_TILE;
                    return new Tile(SIZE_OF_TILE, SIZE_OF_TILE, data);
                } catch (IOException e) {
                    Log.e("ABCD", "N/A 4 " + e.getMessage());
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @NonNull
    private static byte[] downloadData(@NonNull String url) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
//            urlConnection.setConnectTimeout(5000);
//            urlConnection.setReadTimeout(20000);
//            urlConnection.setRequestProperty("Accept-Encoding", "identity");
            urlConnection.setRequestProperty("Referer", "https://geoportal.mp.gov.in/MPPHE_New/");
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            is.close();
//            Log.e("ABCD","YES "+result.toString("UTF-8"));
        } catch (Exception e) {
            Log.e("ABCD", "N/A 1 " + e.getMessage());
        }
        return result.toByteArray();
    }

    @NonNull
    public static GlideUrl getGlideURL(@NonNull String layer) {
        return new GlideUrl(getLegends(layer), new LazyHeaders.Builder()
                .addHeader("Referer", "https://geoportal.mp.gov.in/MPPHE_New/")
                .build());
    }

    @NonNull
    public static String getLegends(@NonNull String layer) {
        return String.format(Locale.US, GEOSERVER_IMAGE_FORMAT, layer);
    }
}