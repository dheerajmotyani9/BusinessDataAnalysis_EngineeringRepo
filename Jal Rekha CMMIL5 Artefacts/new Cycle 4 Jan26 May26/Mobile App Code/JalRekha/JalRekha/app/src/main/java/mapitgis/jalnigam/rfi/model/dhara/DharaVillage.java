/**
 * Created on : 15-10-2024 01:07 pm
 */
package mapitgis.jalnigam.rfi.model.dhara;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DharaVillage {

    @SerializedName("Message")
    private String message;
    @SerializedName("Success")
    private boolean success;
    @SerializedName("Data")
    private List<DharaVillageData> data;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<DharaVillageData> getData() {
        return data;
    }

    public static class DharaVillageData {
        @SerializedName("name_of_villages")
        private String villageName;

        public String getVillageName() {
            return villageName;
        }
    }
}
