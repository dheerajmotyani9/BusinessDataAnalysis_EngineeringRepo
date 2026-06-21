package mapitgis.jalnigam.rfi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateStatusDIContractor {

     /*
    {
  "inspection_id": "12345",
  "alotment_type": "typeA",
  "assigne_by": "John Doe",
  "assigne_to": "Jane Smith",
  "inspection_comments": "All checks completed.",
  "inspection_status_id": "1",
  "review_id": "review123",
  "image_base64": [
    "iVBORw0KGgoAAAANSUhEUgAAAAUA",
    "iVBORw0KGgoAAAANSUhEUgAAAAUB",
    "iVBORw0KGgoAAAANSUhEUgAAAUC"
  ],
  "latitude": "40.712776",
  "longitude": "-74.005974",
  "insert_date": "2024-07-24T15:35:00Z"
}
     */

    @SerializedName("inspection_id")
    private String inspectionId;
    @SerializedName("alotment_type")
    private String allotmentType;
    @SerializedName("assigne_by")
    private String assignBy;
    @SerializedName("assigne_to")
    private String assignTo;
    @SerializedName("inspection_comments")
    private String inspectionComments;
    @SerializedName("inspection_status_id")
    private String inspectionStatus;
    @SerializedName("review_id")
    private String reviewId;
    @SerializedName("image_base64")
    private List<String> imageList;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;
    @SerializedName("etokens")
    private String token;

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public void setAllotmentType(String allotmentType) {
        this.allotmentType = allotmentType;
    }

    public void setAssignBy(String assignBy) {
        this.assignBy = assignBy;
    }

    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    public void setInspectionComments(String inspectionComments) {
        this.inspectionComments = inspectionComments;
    }

    public void setInspectionStatus(String inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
