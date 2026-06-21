/**
 * Created on  : 30-08-2024
 * Developer   : Jameel Khan
 * Designation : Mobile Developer
 */

package mapitgis.jalnigam.rfi.model;

import com.google.gson.annotations.SerializedName;

public class AppVersion {
    @SerializedName("Success")
    private boolean success;
    @SerializedName("Message")
    private String message;
    @SerializedName("Data")
    private AppVersionData data;

    public static class AppVersionData{
        @SerializedName("AppVersion")
        private int appVersion;
        @SerializedName("Message")
        private String message;
        @SerializedName("IsMandate")
        private boolean isMandate;

        public int getAppVersion() {
            return appVersion;
        }

        public String getMessage() {
            return message;
        }

        public boolean isMandate() {
            return isMandate;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public AppVersionData getData() {
        return data;
    }
}
