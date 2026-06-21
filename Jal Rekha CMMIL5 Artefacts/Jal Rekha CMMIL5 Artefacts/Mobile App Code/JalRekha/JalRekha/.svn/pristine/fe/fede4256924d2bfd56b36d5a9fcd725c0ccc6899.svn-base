package mapitgis.jalnigam;

import mapitgis.jalnigam.core.API;

public class Asset {
    private final long serial;
    private final String schemeId, schemeName, esrId, esrName, pastPointId, pointId, pointName, componentId, componentName, lat, lng, remark, photo, date;
    private final String depthCover,diameter, material,alignment,materialInfoName,materialInfoValue;
    private boolean check;
    private final boolean uploaded;
    private final String line_json;

    public Asset(long serial, String pastPointId, String schemeId, String schemeName, String esrId, String esrName, String pointId, String pointName, String componentId, String componentName, String lat, String lng, String remark, String photo, String date, String depthCover, String diameter, String material,String alignment, String materialInfoName, String materialInfoValue,String line_json, boolean uploaded) {
        this.serial = serial;
        this.pastPointId = pastPointId;
        this.schemeId = schemeId;
        this.schemeName = schemeName;
        this.esrId = esrId;
        this.esrName = esrName;
        this.pointId = pointId;
        this.pointName = pointName;
        this.componentId = componentId;
        this.componentName = componentName;
        this.lat = lat;
        this.lng = lng;
        this.remark = remark;
        this.photo = photo;
        this.date = date;
        this.check = false;
        this.depthCover = depthCover;
        this.diameter = diameter;
        this.material = material;
        this.alignment = alignment;
        this.materialInfoName = materialInfoName;
        this.materialInfoValue = materialInfoValue;
        this.line_json = line_json;
        this.uploaded = uploaded;
    }

    public Asset(String schemeId, String schemeName,String pointId, String pointName, String componentId, String componentName, String lat, String lng, String remark, String photo, String date) {
        this.serial = 0;
        this.pastPointId = "0";
        this.schemeId = schemeId;
        this.schemeName = schemeName;
        this.esrId = "";
        this.esrName = "";
        this.pointId = pointId;
        this.pointName = pointName;
        this.componentId = componentId;
        this.componentName = componentName;
        this.lat = lat;
        this.lng = lng;
        this.remark = remark;
        this.photo = photo;
        this.date = date;
        this.check = false;
        this.depthCover = "";
        this.diameter = "";
        this.material = "";
        this.alignment = "";
        this.materialInfoName = "";
        this.materialInfoValue = "";
        this.line_json = "";
        this.uploaded = false;
    }

    public String getAlignment() {
        return alignment;
    }

    public String getDepthCover() {
        return depthCover;
    }

    public String getDiameter() {
        return diameter;
    }

    public String getMaterial() {
        return material;
    }

    public String getPastPointId() {
        return pastPointId;
    }

    public String getPointId() {
        return pointId;
    }

    public String getPointName() {
        return pointName;
    }

    public boolean isCheck() {
        return check;
    }

    public void changeCheck() {
        check = !check;
    }

    public long getSerial() {
        return serial;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public String getEsrId() {
        return esrId;
    }

    public String getEsrName() {
        return esrName;
    }

    public String getComponentId() {
        return componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getRemark() {
        return remark;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDate() {
        return date;
    }

    public String getComponentNameWithPoint() {
        return String.format("%s %s", componentName, pointName);
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public boolean isPng() {
        return photo.endsWith(".png") || photo.endsWith(".jpg");
    }

    public String getPhotoURL() {
        return String.format("%s%s", API.IMAGE_URL, photo);
    }

    public String getLineJson() {
        return line_json;
    }

    public boolean isNoPhoto() {
        return photo.isEmpty();
    }

    public String getMaterialInfoName() {
        return materialInfoName;
    }

    public String getMaterialInfoValue() {
        return materialInfoValue;
    }
}
