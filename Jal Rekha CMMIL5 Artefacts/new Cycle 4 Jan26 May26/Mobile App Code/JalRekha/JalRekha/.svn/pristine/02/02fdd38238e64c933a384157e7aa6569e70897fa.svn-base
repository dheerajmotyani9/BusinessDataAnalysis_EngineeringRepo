/**
 * Created on : 18-10-2024 04:17 pm
 */
package mapitgis.jalnigam.rfi.model.dhara;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DharaESR {

    @SerializedName("Data")
    private List<DharaESRData> data;
    @SerializedName("Message")
    private String message;
    @SerializedName("Success")
    private boolean success;
    @SerializedName("DataLength")
    private int dataLength;
    @SerializedName("StatusCode")
    private int statusCode;

    public static class DharaESRData{
        @SerializedName("esr_ost_name")
        private String esrName;

        public String getEsrName() {
            return esrName;
        }
    }

    public List<DharaESRData> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getDataLength() {
        return dataLength;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
