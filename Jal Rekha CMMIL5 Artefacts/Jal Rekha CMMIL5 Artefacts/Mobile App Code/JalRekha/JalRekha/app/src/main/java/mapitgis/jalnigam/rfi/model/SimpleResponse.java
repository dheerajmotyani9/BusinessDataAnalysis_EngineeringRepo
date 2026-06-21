package mapitgis.jalnigam.rfi.model;

import com.google.gson.annotations.SerializedName;

public class SimpleResponse {

    @SerializedName("Message")
    private String message;
    @SerializedName("Success")
    private boolean success;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
