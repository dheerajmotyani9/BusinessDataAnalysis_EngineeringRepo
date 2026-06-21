package mapitgis.jalnigam.rfi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostFEInspection {

    @SerializedName("rfi_id")
    private String rfiId;
    @SerializedName("assigne_by")
    private String assignedBy;
    @SerializedName("assigne_to")
    private String assignedTo;
    @SerializedName("responce_comments")
    private String comments;
    @SerializedName("review_id")
    private String reviewId;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    private String address;
    @SerializedName("image_base64")
    private List<String> images;
    @SerializedName("etoken")
    private String token;

    public void setRfiId(String rfiId) {
        this.rfiId = rfiId;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "PostFEInspection{" +
                "rfiId='" + rfiId + '\'' +
                ", assignedBy='" + assignedBy + '\'' +
                ", assignedTo='" + assignedTo + '\'' +
                ", comments='" + comments + '\'' +
                ", reviewId='" + reviewId + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
