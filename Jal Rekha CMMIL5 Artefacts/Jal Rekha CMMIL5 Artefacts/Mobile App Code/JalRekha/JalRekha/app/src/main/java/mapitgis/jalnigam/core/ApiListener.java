package mapitgis.jalnigam.core;
import androidx.annotation.NonNull;

import java.io.InputStream;

public interface ApiListener {
    void onResponse(@NonNull String response, int key);

    default void webProgress(int progress, int key){

    }

    default void onInputStream(@NonNull InputStream inputStream){

    }

    default boolean getInputStream(){
        return false;
    }
}
