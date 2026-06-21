/**
 * Created on : 30-09-2024 04:24 pm
 */
package mapitgis.jalnigam.rfi.model.dhara;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DailyWaterSupply {

    @SerializedName("Data")
    private List<DailyWaterSupplyData> data;
    @SerializedName("Message")
    private String message;
    @SerializedName("Success")
    private boolean success;
    @SerializedName("DataLength")
    private int dataLength;
    @SerializedName("StatusCode")
    private int statusCode;

    public List<DailyWaterSupplyData> getData() {
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

    public static class DailyWaterSupplyData implements Serializable {
        @SerializedName("alotement_type")
        private String alotementType;
        @SerializedName("alotement_type_name")
        private String allotmentTypeName;
        @SerializedName("assigne_to")
        private String assigneTo;
        @SerializedName("district_name")
        private String districtName;
        @SerializedName("esr_name")
        private String esrName;
        @SerializedName("intake_id")
        private int intakeId;
        @SerializedName("intake_name")
        private String intakeName;
        @SerializedName("o_m_partialo_m")
        private String oMPartialoM;
        @SerializedName("piu_name")
        private String piuName;
        @SerializedName("user_id")
        private int userId;
        @SerializedName("user_type")
        private String userType;
        @SerializedName("wtp_id")
        private int wtpId;
        @SerializedName("wtp_name")
        private String wtpName;

        //todo: this parameter added for update intake/wtp/esr
        private String startReading;
        private String endReading;
        private String totalReading;
        private String startPhotoPath;
        private String endPhotoPath;
        private String remark;

        private String startReadingVillage;
        private String endReadingVillage;
        private String totalReadingVillage;
        private String startPhotoPathVillage;
        private String endPhotoPathVillage;
        private String villageName;

        private boolean isUpdate = false;

        public String getAlotementType() {
            return alotementType;
        }

        public String getAssigneTo() {
            return assigneTo;
        }

        public String getDistrictName() {
            return districtName;
        }

        public String getEsrName() {
            return esrName;
        }

        public int getIntakeId() {
            return intakeId;
        }

        public String getIntakeName() {
            return intakeName;
        }

        public String getoMPartialoM() {
            return oMPartialoM;
        }

        public String getPiuName() {
            return piuName;
        }

        public int getUserId() {
            return userId;
        }

        public String getUserType() {
            return userType;
        }

        public int getWtpId() {
            return wtpId;
        }

        public String getWtpName() {
            return wtpName;
        }

        public void setAlotementType(String alotementType) {
            this.alotementType = alotementType;
        }

        public void setAssigneTo(String assigneTo) {
            this.assigneTo = assigneTo;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public void setEsrName(String esrName) {
            this.esrName = esrName;
        }

        public void setIntakeId(int intakeId) {
            this.intakeId = intakeId;
        }

        public void setIntakeName(String intakeName) {
            this.intakeName = intakeName;
        }

        public void setoMPartialoM(String oMPartialoM) {
            this.oMPartialoM = oMPartialoM;
        }

        public void setPiuName(String piuName) {
            this.piuName = piuName;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public void setWtpId(int wtpId) {
            this.wtpId = wtpId;
        }

        public void setWtpName(String wtpName) {
            this.wtpName = wtpName;
        }

        public String getStartReading() {
            return startReading;
        }

        public void setStartReading(String startReading) {
            this.startReading = startReading;
        }

        public String getEndReading() {
            return endReading;
        }

        public void setEndReading(String endReading) {
            this.endReading = endReading;
        }

        public String getTotalReading() {
            return totalReading;
        }

        public void setTotalReading(String totalReading) {
            this.totalReading = totalReading;
        }

        public String getStartPhotoPath() {
            return startPhotoPath;
        }

        public void setStartPhotoPath(String startPhotoPath) {
            this.startPhotoPath = startPhotoPath;
        }

        public String getEndPhotoPath() {
            return endPhotoPath;
        }

        public void setEndPhotoPath(String endPhotoPath) {
            this.endPhotoPath = endPhotoPath;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStartReadingVillage() {
            return startReadingVillage;
        }

        public void setStartReadingVillage(String startReadingVillage) {
            this.startReadingVillage = startReadingVillage;
        }

        public String getEndReadingVillage() {
            return endReadingVillage;
        }

        public void setEndReadingVillage(String endReadingVillage) {
            this.endReadingVillage = endReadingVillage;
        }

        public String getTotalReadingVillage() {
            return totalReadingVillage;
        }

        public void setTotalReadingVillage(String totalReadingVillage) {
            this.totalReadingVillage = totalReadingVillage;
        }

        public String getStartPhotoPathVillage() {
            return startPhotoPathVillage;
        }

        public void setStartPhotoPathVillage(String startPhotoPathVillage) {
            this.startPhotoPathVillage = startPhotoPathVillage;
        }

        public String getEndPhotoPathVillage() {
            return endPhotoPathVillage;
        }

        public void setEndPhotoPathVillage(String endPhotoPathVillage) {
            this.endPhotoPathVillage = endPhotoPathVillage;
        }

        public boolean isUpdate() {
            return isUpdate;
        }

        public void setUpdate(boolean update) {
            isUpdate = update;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public String getAllotmentTypeName() {
            return allotmentTypeName;
        }
    }
}
