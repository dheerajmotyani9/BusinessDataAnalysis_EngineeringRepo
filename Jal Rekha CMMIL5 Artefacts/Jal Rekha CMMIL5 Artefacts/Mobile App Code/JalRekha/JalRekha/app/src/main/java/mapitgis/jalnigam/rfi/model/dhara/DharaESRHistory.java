/**
 * Created on : 15-10-2024 04:23 pm
 */
package mapitgis.jalnigam.rfi.model.dhara;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DharaESRHistory {
    @SerializedName("Data")
    private List<DharaESRHistoryData> data;
    @SerializedName("Message")
    private String message;
    @SerializedName("Success")
    private boolean success;
    @SerializedName("DataLength")
    private int dataLength;
    @SerializedName("StatusCode")
    private int statusCode;

    public List<DharaESRHistoryData> getData() {
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

    public static class DharaESRHistoryData implements Serializable {

        @SerializedName("created_by")
        private String createdBy;
        @SerializedName("created_by_role")
        private String createdByRole;
        @SerializedName("end_reading")
        private String endReading;
        @SerializedName("esr_end_reading")
        private String esrEndReading;
        @SerializedName("esr_latitude")
        private String esrLatitude;
        @SerializedName("esr_longitude")
        private String esrLongitude;
        @SerializedName("esr_name")
        private String esrName;
        @SerializedName("esr_photo_path_end_reading")
        private String esrPhotoPathEndReading;
        @SerializedName("esr_photo_path_start_reading")
        private String esrPhotoPathStartReading;
        @SerializedName("esr_remarks")
        private String esrRemarks;
        @SerializedName("esr_start_reading")
        private String esrStartReading;
        @SerializedName("esr_total_water_supplied")
        private String esrTotalWaterSupplied;
        @SerializedName("id")
        private String id;
        @SerializedName("insert_date")
        private String insertDate;
        @SerializedName("intake_id")
        private String intakeId;
        @SerializedName("intake_name")
        private String intakeName;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("photo_path_end_reading")
        private String photoPathEndReading;
        @SerializedName("photo_path_start_reading")
        private String photoPathStartReading;
        @SerializedName("relation_id")
        private String relationId;
        @SerializedName("remarks")
        private String remarks;
        @SerializedName("start_reading")
        private String startReading;
        @SerializedName("survey_date")
        private String surveyDate;
        @SerializedName("survey_time")
        private String surveyTime;
        @SerializedName("total_water_supplied")
        private String totalWaterSupplied;
        @SerializedName("update_date")
        private String updateDate;
        @SerializedName("updated_by")
        private String updatedBy;
        @SerializedName("updated_by_role")
        private String updatedByRole;
        @SerializedName("village_lgd_cd")
        private String villageLgdCd;
        @SerializedName("village_name")
        private String villageName;
        @SerializedName("wtp_id")
        private String wtpId;
        @SerializedName("wtp_name")
        private String wtpName;

        public String getCreatedBy() {
            return createdBy;
        }

        public String getCreatedByRole() {
            return createdByRole;
        }

        public String getEndReading() {
            return endReading;
        }

        public String getEsrEndReading() {
            return esrEndReading;
        }

        public String getEsrLatitude() {
            return esrLatitude;
        }

        public String getEsrLongitude() {
            return esrLongitude;
        }

        public String getEsrName() {
            return esrName;
        }

        public String getEsrPhotoPathEndReading() {
            return esrPhotoPathEndReading;
        }

        public String getEsrPhotoPathStartReading() {
            return esrPhotoPathStartReading;
        }

        public String getEsrRemarks() {
            return esrRemarks;
        }

        public String getEsrStartReading() {
            return esrStartReading;
        }

        public String getEsrTotalWaterSupplied() {
            return esrTotalWaterSupplied;
        }

        public String getId() {
            return id;
        }

        public String getInsertDate() {
            return insertDate;
        }

        public String getIntakeId() {
            return intakeId;
        }

        public String getIntakeName() {
            return intakeName;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getPhotoPathEndReading() {
            return photoPathEndReading;
        }

        public String getPhotoPathStartReading() {
            return photoPathStartReading;
        }

        public String getRelationId() {
            return relationId;
        }

        public String getRemarks() {
            return remarks;
        }

        public String getStartReading() {
            return startReading;
        }

        public String getSurveyDate() {
            return surveyDate;
        }

        public String getSurveyTime() {
            return surveyTime;
        }

        public String getTotalWaterSupplied() {
            return totalWaterSupplied;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public String getUpdatedByRole() {
            return updatedByRole;
        }

        public String getVillageLgdCd() {
            return villageLgdCd;
        }

        public String getVillageName() {
            return villageName;
        }

        public String getWtpId() {
            return wtpId;
        }

        public String getWtpName() {
            return wtpName;
        }
    }
}
