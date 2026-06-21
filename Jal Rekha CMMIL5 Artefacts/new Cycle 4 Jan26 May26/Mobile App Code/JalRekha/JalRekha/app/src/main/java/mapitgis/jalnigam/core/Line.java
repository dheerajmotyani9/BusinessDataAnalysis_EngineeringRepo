package mapitgis.jalnigam.core;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

public class Line {
    private final String name;
    private final LatLng point1;
//    @Nullable
//    private final LatLng point2;
    private final JSONArray jsonArray;

    public Line(String name, LatLng point1,@Nullable JSONArray jsonArray) {//,@Nullable LatLng point2
        this.name = name;
        this.point1 = point1;
//        this.point2 = point2;
        this.jsonArray = jsonArray;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public String getName() {
        return name;
    }

    public LatLng getPoint1() {
        return point1;
    }

//    @Nullable
//    public LatLng getPoint2() {
//        return point2;
//    }
}
