package mapitgis.jalnigam.core;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Data {
    private final JSONObject jsonObject;

    public Data() {
        jsonObject=new JSONObject();
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void put(@NonNull String name, boolean value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(@NonNull String name, double value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(@NonNull String name, int value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(@NonNull String name, long value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void put(@NonNull String name, JSONArray value) {
        try {
            jsonObject.put(name, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void put(@NonNull String name, @NonNull Object value) {
        try {
            if(value instanceof Data){
                jsonObject.put(name,((Data) value).jsonObject);
            }else {
                jsonObject.put(name, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return jsonObject.toString();
    }
}
