package mapitgis.jalnigam.rfi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DIComplaintLogs {

    @SerializedName("Data")
    private List<DIComplaintLogsData> data;
    @SerializedName("Message")
    private String message;
    @SerializedName("Success")
    private boolean success;
    @SerializedName("DataLength")
    private int dataLength;
    @SerializedName("StatusCode")
    private int statusCode;

    public List<DIComplaintLogsData> getData() {
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

    public static class DIComplaintLogsData {

        @SerializedName("Longitude")
        private double longitude;
        @SerializedName("alotment_type")
        private String alotmentType;
        @SerializedName("assigne_by")
        private int assigneBy;
        @SerializedName("assigne_by_name")
        private String assigneByName;
        @SerializedName("assigne_to")
        private int assigneTo;
        @SerializedName("assigne_to_name")
        private String assigneToName;
        @SerializedName("insert_date")
        private String insertDate;
        @SerializedName("inspection_comments")
        private String inspectionComments;
        @SerializedName("inspection_id")
        private int inspectionId;
        @SerializedName("inspection_status_id")
        private int inspectionStatusId;
        @SerializedName("latitude")
        private double latitude;
        @SerializedName("photo_path")
        private String photoPath;
        @SerializedName("qa_qc_review")
        private String qaQcReview;
        @SerializedName("review_id")
        private Integer reviewId;
        @SerializedName("status_name")
        private String statusName;

        public double getLongitude() {
            return longitude;
        }

        public String getAlotmentType() {
            return alotmentType;
        }

        public int getAssigneBy() {
            return assigneBy;
        }

        public String getAssigneByName() {
            return assigneByName;
        }

        public int getAssigneTo() {
            return assigneTo;
        }

        public String getAssigneToName() {
            return assigneToName;
        }

        public String getInsertDate() {
            return insertDate;
        }

        public String getInspectionComments() {
            return inspectionComments;
        }

        public int getInspectionId() {
            return inspectionId;
        }

        public int getInspectionStatusId() {
            return inspectionStatusId;
        }

        public double getLatitude() {
            return latitude;
        }

        public String getPhotoPath() {
            return photoPath;
        }

        public String getQaQcReview() {
            return qaQcReview;
        }

        public Integer getReviewId() {
            return reviewId;
        }

        public String getStatusName() {
            return statusName;
        }
    }
}
