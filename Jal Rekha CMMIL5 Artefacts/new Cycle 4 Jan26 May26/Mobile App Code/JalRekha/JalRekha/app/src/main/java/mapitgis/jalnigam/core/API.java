package mapitgis.jalnigam.core;

public class API {
    public static final String CheckDeviceMobile = "CheckDeviceMobile";
//    public static final String GetDeviceByLogin = "GetDeviceByLogin";
    public static final String GetDeviceUserForLogin = "GetDeviceUserForLogin";
    public static final String GetDevice = "GetDevice";
    public static final String RegisterDevice = "RegisterDevice";
//    public static final String RegisterWithOTP = "RegisterWithOTP";

    public static final String UpdateProfile = "UpdateProfile";
    public static final String SyncAssignedWork = "SyncAssignedWork";
    public static final String SaveAsset = "SaveAsset";


//    public static final String SaveQuality = "Save_Water_Quality";
//    public static final String SaveQuantity = "Save_Water_Quality_Pressure";
//    public static final String SaveComplaint = "Save_Complaints";

    public static final String GetDesignation = "GetDesignation";
    public static final String GetDistricts = "GetDistricts";

    public static final String LOGOUT = "logoutDevice";
    public static final String SendRegisterMail = "SendRegisterMail";
    public static final String FORGOT_GET_OTP = "SendOtpForForgotPass";
    public static final String FORGOT = "Update_Device_User_Pass";
    public static final String IMAGE_URL = "https://geoportal.mp.gov.in/mpjalnigam/images/";

    public static class ISA {
        public static final String SYNC = "NGOSync";
        public static final String SAVE = "Insert_DailyActivityNGO";
    }
    public static class DI {
//        public static final String LOGIN = "Auth_Login";
        public static final String SYNC = "di_sync";
        public static final String SAVE = "Ins_dept_inspection_data";
        public static final String LIST = "GetDeptInspection_Data";
    }
    public static class DHARA {
        public static final String SYNC = "SyncDataForDhara";
        public static final String SAVE = "PushReadingByAPP";
    }
}
