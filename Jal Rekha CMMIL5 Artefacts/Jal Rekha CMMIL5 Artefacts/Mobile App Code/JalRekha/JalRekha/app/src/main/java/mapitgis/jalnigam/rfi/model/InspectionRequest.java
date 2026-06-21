package mapitgis.jalnigam.rfi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InspectionRequest {

    @SerializedName("Message")
    private String message;
    @SerializedName("Success")
    private boolean success;
    @SerializedName("Data")
    List<InspectionRequestData> dataList;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<InspectionRequestData> getDataList() {
        return dataList;
    }

    public static class InspectionRequestData {

        @SerializedName("address")
        private String address;
        @SerializedName("application_id")
        private String applicationId;
        @SerializedName("application_type_nm")
        private String applicationName;
        @SerializedName("component_id")
        private String componentId;
        @SerializedName("component_name")
        private String componentName;
        @SerializedName("component_points")
        private String componentPoints;
        @SerializedName("contractor_id")
        private String contractorId;
        @SerializedName("contractor_name")
        private String contractorName;
        @SerializedName("description")
        private String description;
        @SerializedName("fe_mobile")
        private String feMobile;
        @SerializedName("fe_name")
        private String feName;
        @SerializedName("fe_remark")
        private String feRemark;
        @SerializedName("final_qa_qc_review")
        private String finalQaQcReview;
        @SerializedName("geo_address")
        private String geoAddress;
        @SerializedName("image_path")
        private String imagePath;
        @SerializedName("insert_date")
        private String insertDate;
        @SerializedName("inspection_date")
        private String inspectionDate;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("piu_name")
        private String piuName;
        @SerializedName("piu_id")
        private String puiId;
        @SerializedName("point_id")
        private String pointId;
        @SerializedName("point_name")
        private String pointName;
        @SerializedName("qa_qc_review")
        private String qaQcReview;
        @SerializedName("rfi_id")
        private String rfiId;
        @SerializedName("scheme_id")
        private String schemeId;
        @SerializedName("scheme_name")
        private String schemeName;
        @SerializedName("slno")
        private String slno;
        @SerializedName("sqc_name")
        private String sqcName;
        @SerializedName("sqc_user_id")
        private String sqcUserId;
        @SerializedName("status")
        private String status;
        @SerializedName("status_name")
        private String statusName;
        @SerializedName("survey_address")
        private String surveyAddress;
        @SerializedName("survey_comment")
        private String surveyComment;
        @SerializedName("survey_latitude")
        private String surveyLatitude;
        @SerializedName("survey_longitude")
        private String surveyLongitude;
        @SerializedName("survey_photo_path")
        private String surveyPhotoPath;
        @SerializedName("survey_review")
        private String surveyReview;
        @SerializedName("tpia_name")
        private String tpiaName;
        @SerializedName("tpia_user_id")
        private String tpiaUserId;
        @SerializedName("vill_cds")
        private String villageId;
        @SerializedName("vill_name")
        private String villageName;
        @SerializedName("alotment_type")
        private String allotmentType;


        @SerializedName("component_line_stage_id")
        private String stageId;
        @SerializedName("component_line_stage_name")
        private String stageName;

        @SerializedName("pipe_no")
        private String pipeNo;

        @SerializedName("survey_uid")
        private String surveyUID;

        @SerializedName("length_slot")
        private String lengthSlot;



        public void setStageId(String stageId) {
            this.stageId = stageId;
        }

        public void setStageName(String stageName) {
            this.stageName = stageName;
        }

        public String getStageId() {
            return stageId;
        }

        public String getStageName() {
            return stageName;
        }

        public String getAllotmentType() {
            return allotmentType;
        }

        public String getAddress() {
            return address;
        }

        public String getApplicationId() {
            return applicationId;
        }

        public String getComponentId() {
            return componentId;
        }

        public String getComponentName() {
            return componentName;
        }

        public String getComponentPoints() {
            return componentPoints;
        }

        public String getContractorId() {
            return contractorId;
        }

        public String getContractorName() {
            return contractorName;
        }

        public String getDescription() {
            return description;
        }

        public String getFinalQaQcReview() {
            return finalQaQcReview;
        }

        public String getGeoAddress() {
            return geoAddress;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getInsertDate() {
            return insertDate;
        }

        public String getInspectionDate() {
            return inspectionDate;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getPiuName() {
            return piuName;
        }

        public String getRfiId() {
            return rfiId;
        }

        public String getSchemeId() {
            return schemeId;
        }

        public String getSchemeName() {
            return schemeName;
        }

        public String getSlno() {
            return slno;
        }

        public String getSqcName() {
            return sqcName;
        }

        public String getSqcUserId() {
            return sqcUserId;
        }

        public String getStatus() {
            return status;
        }

        public String getTpiaName() {
            return tpiaName;
        }

        public String getTpiaUserId() {
            return tpiaUserId;
        }

        public String getVillageId() {
            return villageId;
        }

        public String getVillageName() {
            return villageName;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public String getFeMobile() {
            return feMobile;
        }

        public String getFeName() {
            return feName;
        }

        public String getPuiId() {
            return puiId;
        }

        public String getPointId() {
            return pointId;
        }

        public String getPointName() {
            return pointName;
        }

        public String getQaQcReview() {
            return qaQcReview;
        }

        public String getStatusName() {
            return statusName;
        }

        public String getSurveyAddress() {
            return surveyAddress;
        }

        public String getSurveyComment() {
            return surveyComment;
        }

        public String getSurveyLatitude() {
            return surveyLatitude;
        }

        public String getSurveyLongitude() {
            return surveyLongitude;
        }

        public String getSurveyPhotoPath() {
            return surveyPhotoPath;
        }

        public String getSurveyReview() {
            return surveyReview;
        }

        public String getFeRemark() {
            return feRemark;
        }

        public boolean isStageIdNotFound() {
            return stageId == null || stageId.equals("0") || stageId.equals("null") || stageId.isEmpty();
        }


        public String getPipeNo() {
            return pipeNo;
        }

        public String getSurveyUID() {
            return surveyUID;
        }

        public String getLengthSlot() {
            return lengthSlot;
        }
    }
}
