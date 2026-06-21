package mapitgis.jalnigam.core;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class SpinnerItem implements Serializable {
    private final String keyString;
    private final int key;
    private final String value;
    private final Object extra;

    public SpinnerItem(String keyString, String value) {
        this.keyString = keyString;
        int key;
        try{
            key = Integer.parseInt(keyString);
        }catch (Exception ignore){
            key = 0;
        }
        this.key = key;
        this.value = value;
        this.extra = null;
    }

    public SpinnerItem(String keyString, String value, Object extra) {
        this.keyString = keyString;
        int key;
        try{
            key = Integer.parseInt(keyString);
        }catch (Exception ignore){
            key = 0;
        }
        this.key = key;
        this.value = value;
        this.extra = extra;
    }

    public SpinnerItem(int key, String value) {
        this.keyString = String.valueOf(key);
        this.key = key;
        this.value = value;
        this.extra = null;
    }

    public SpinnerItem(int key, String value, Object extra) {
        this.keyString = String.valueOf(key);
        this.key = key;
        this.value = value;
        this.extra = extra;
    }

    public Object getExtra() {
        return extra;
    }

    public String getKeyString() {
        return keyString;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }
}