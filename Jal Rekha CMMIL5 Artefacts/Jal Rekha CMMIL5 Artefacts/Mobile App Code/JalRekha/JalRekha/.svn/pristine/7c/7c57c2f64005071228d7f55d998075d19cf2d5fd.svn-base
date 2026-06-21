package mapitgis.jalnigam.rfi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DILogin {

    @SerializedName("Message")
    private String message;
    @SerializedName("Success")
    private boolean success;
    @SerializedName("Data")
    private DILoginData data;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public DILoginData getData() {
        return data;
    }

    public static class DILoginData {
        private String designation;
        @SerializedName("dist_cd")
        private String distCd;
        @SerializedName("email_id")
        private String email;
        @SerializedName("etoken")
        private String eToken;
        @SerializedName("mobile_number")
        private String mobileNo;
        private String name;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("role_id")
        private int roleId;
        @SerializedName("role_name")
        private String roleName;
        @SerializedName("deptInspectionAppType")
        private List<InspectionAppType> appTypeArray;
        @SerializedName("deptInspectionComponent")
        private List<InspectionComponent> componentArray;
        @SerializedName("deptInspectionPIU")
        private List<InspectionPIU> piuArray;
        @SerializedName("deptInspectionScheme")
        private List<InspectionScheme> schemeArray;
        @SerializedName("deptInspection_qa_qc_review")
        private List<InspectionQRQCReview> qaQcReviewArray;


        public static class  InspectionAppType{
            @SerializedName("application_type_id")
            private String appId;
            @SerializedName("application_type_nm")
            private String appName;

            public String getAppId() {
                return appId;
            }

            public String getAppName() {
                return appName;
            }
        }

        public static class InspectionComponent{
            @SerializedName("component_id")
            private String componentId;
            @SerializedName("component_name")
            private String componentName;
            @SerializedName("component_type")
            private String componentType;
            @SerializedName("component_type_nm")
            private String componentTypeName;

            public String getComponentId() {
                return componentId;
            }

            public String getComponentName() {
                return componentName;
            }

            public String getComponentType() {
                return componentType;
            }

            public String getComponentTypeName() {
                return componentTypeName;
            }
        }

        public static class InspectionPIU{
            @SerializedName("piu_dist_cd")
            private String piuId;
            @SerializedName("piu_name")
            private String piuName;

            public String getPiuId() {
                return piuId;
            }

            public String getPiuName() {
                return piuName;
            }
        }

        public static class InspectionScheme{
            @SerializedName("piu_dist_cd")
            private String piuDistCd;
            @SerializedName("scheme_id")
            private String schemeId;
            @SerializedName("scheme_name")
            private String schemeName;

            public String getPiuDistCd() {
                return piuDistCd;
            }

            public String getSchemeId() {
                return schemeId;
            }

            public String getSchemeName() {
                return schemeName;
            }
        }

        public static class InspectionQRQCReview{
            @SerializedName("qa_qc_review")
            private String qaQcReview;
            @SerializedName("qa_qc_review_type_id")
            private String getQaQcReviewId;

            public String getQaQcReview() {
                return qaQcReview;
            }

            public String getGetQaQcReviewId() {
                return getQaQcReviewId;
            }
        }


        public String getDesignation() {
            return designation;
        }

        public String getDistCd() {
            return distCd;
        }

        public String getEmail() {
            return email;
        }

        public String geteToken() {
            return eToken;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public String getName() {
            return name;
        }

        public String getUserId() {
            return userId;
        }

        public int getRoleId() {
            return roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public List<InspectionAppType> getAppTypeArray() {
            return appTypeArray;
        }

        public List<InspectionComponent> getComponentArray() {
            return componentArray;
        }

        public List<InspectionPIU> getPiuArray() {
            return piuArray;
        }

        public List<InspectionScheme> getSchemeArray() {
            return schemeArray;
        }

        public List<InspectionQRQCReview> getQaQcReviewArray() {
            return qaQcReviewArray;
        }
    }
}
