package mapitgis.jalnigam.rfi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostInspectionRequest {

    @SerializedName("etoken")
    private String eToken;
    @SerializedName("piu_id")
    private String puiId;
    @SerializedName("component_id")
    private String componentId;
    @SerializedName("component_line_stage_id")
    private String stageId;
    @SerializedName("point_id")
    private String pointId;
    @SerializedName("point_name")
    private String pointName;
    @SerializedName("vill_cds")
    private String villageId;
    @SerializedName("application_id")
    private String applicationId;
    @SerializedName("contractor_id")
    private String contractorId;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("geo_address")
    private String geoAddress;
    @SerializedName("address")
    private String address;
    @SerializedName("description")
    private String description;
    @SerializedName("inspection_date")
    private String inspectionDate;
    @SerializedName("scheme_id")
    private String schemeId;
    @SerializedName("image_base64")
    private List<String>imageList;

    @SerializedName("pipe_no")
    private String pipeNo;

    @SerializedName("survey_uid")
    private String surveyUID;

    @SerializedName("length_slot")
    private String lengthSlot;

    @SerializedName("type_of_sopan_oht")
    private int typeOfSopanOHT=0;



    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String geteToken() {
        return eToken;
    }

    public void seteToken(String eToken) {
        this.eToken = eToken;
    }

    public String getPuiId() {
        return puiId;
    }

    public void setPuiId(String puiId) {
        this.puiId = puiId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getContractorId() {
        return contractorId;
    }

    public void setContractorId(String contractorId) {
        this.contractorId = contractorId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGeoAddress() {
        return geoAddress;
    }

    public void setGeoAddress(String geoAddress) {
        this.geoAddress = geoAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }


    public void setPipeNo(String pipeNo) {
        this.pipeNo = pipeNo;
    }

    public void setSurveyUID(String surveyUID) {
        this.surveyUID = surveyUID;
    }

    public void setLengthSlot(String lengthSlot) {
        this.lengthSlot = lengthSlot;
    }

    public void setTypeOfSopanOHT(int typeOfSopanOHT) {
        this.typeOfSopanOHT = typeOfSopanOHT;
    }

    @Override
    public String toString() {
        return "PostInspectionRequest{" +
                "schemeId='" + schemeId + '\'' +
                ", inspectionDate='" + inspectionDate + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", geoAddress='" + geoAddress + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", contractorId='" + contractorId + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", villageId='" + villageId + '\'' +
                ", pointName='" + pointName + '\'' +
                ", pointId='" + pointId + '\'' +
                ", componentId='" + componentId + '\'' +
                ", puiId='" + puiId + '\'' +
                ", eToken='" + eToken + '\'' +
                '}';
    }
}
