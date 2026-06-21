package mapitgis.jalnigam.rfi.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mapitgis.jalnigam.core.SqLite;

public class PrefManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    //shared pref mode
    private int PRIVATE_MODE = 0;

    //shared preferences file name
    private static final String PREF_NAME = "jal-rekha-app";

    private static final String LOGIN_WITH = "login_with";

//    private static final String USER_NAME = "user_name";
//    private static final String USER_EMAIL = "user_email";
//    private static final String USER_ID = "user_id";
//    private static final String USER_MOBILE = "user_mobile";
    private static final String USER_PLAN_ID = "user_plan_id";
//    private static final String USER_PASSWORD = "user_password";
//    private static final String IS_LOGGED_IN = "is_logged_in";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String CITY = "city";
//    private static final String USER_TYPE = "user_type";
//    private static final String TOKEN = "token";
    private static final String SCHEME_ID = "scheme_id";
    private static final String SCHEME_NAME = "scheme_name";
    private static final String SQC_NAME = "sqc_name";
//    private static final String SQC_ID = "sqc_id";
    private static final String TPIA_NAME = "tpia_name";
    private static final String TPIA_ID = "tpis_id";
    private static final String PIU_NAME = "piu_name";
    private static final String PIU_ID = "piu_id";
//    private static final String DESIGNATION = "designation";
    private static final String SOURCE_TYPE = "source_type";
//    private static final String ROLE_ID = "role_id";
//    private static final String IS_DHARA_ACCESS = "is_dhara_access";
    private static final String ESR_NAME_LIST = "esr_name_list";

//    private static final String IS_DI_LOGGED = "is_di_logged";
//    private static final String DI_MOBILE = "di_mobile";
//    private static final String DI_PASSWORD = "di_password";

    public PrefManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void setLoginWith(String loginWith) {
        editor.putString(LOGIN_WITH, loginWith);
        editor.commit();
    }

    public String getLoginWith() {
        return preferences.getString(LOGIN_WITH, null);
    }

//    public void setUserName(String userName) {
//        editor.putString(USER_NAME, userName);
//        editor.commit();
//    }
//
//    public String getUserName() {
//        return preferences.getString(USER_NAME, null);
//    }
//
//    public void setUserEmail(String userEmail) {
//        editor.putString(USER_EMAIL, userEmail);
//        editor.commit();
//    }
//
//    public String getUserEmail() {
//        return preferences.getString(USER_EMAIL, null);
//    }
//
//    public void setUserId(String userId) {
//        editor.putString(USER_ID, userId);
//        editor.commit();
//    }
//
//    public String getUserId() {
//        return preferences.getString(USER_ID, null);
//    }
//
//    public void setIsLoggedIn(boolean isLoggedIn) {
//        editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
//        editor.commit();
//    }
//
//    public boolean isLoggedIn() {
//        return preferences.getBoolean(IS_LOGGED_IN, false);
//    }
//
//    public void setUserMobile(String userMobile) {
//        editor.putString(USER_MOBILE, userMobile);
//        editor.commit();
//    }
//
//    public String getUserMobile() {
//        return preferences.getString(USER_MOBILE, null);
//    }

    public void setUserPlanId(String planId) {
        editor.putString(USER_PLAN_ID, planId);
        editor.commit();
    }

    public String getUserPlanId() {
        return preferences.getString(USER_PLAN_ID, null);
    }

//    public void setUserPassword(String password) {
//        editor.putString(USER_PASSWORD, password);
//        editor.commit();
//    }
//
//    public String getUserPassword() {
//        return preferences.getString(USER_PASSWORD, null);
//    }

    public void setLatitude(String latitude) {
        editor.putString(LATITUDE, latitude);
        editor.commit();
    }

    public String getLatitude() {
        return preferences.getString(LATITUDE, "0");
    }

    public void setLongitude(String longitude) {
        editor.putString(LONGITUDE, longitude);
        editor.commit();
    }

    public String getLongitude() {
        return preferences.getString(LONGITUDE, "0");
    }

    public void setCity(String city) {
        editor.putString(CITY, city);
        editor.commit();
    }

    public String getCity() {
        return preferences.getString(CITY, null);
    }

//    public void setUserType(String userType) {
//        editor.putString(USER_TYPE, userType);
//        editor.commit();
//    }
//
//    public String getUserType() {
//        return preferences.getString(USER_TYPE, "");
//    }

//    public void setToken1(String token) {
//        editor.putString(TOKEN, token);
//        editor.commit();
//    }
//
//    public String getToken1() {
//        return preferences.getString(TOKEN, "");
//    }

    public void setSchemeId(String id) {
        editor.putString(SCHEME_ID, id);
        editor.commit();
    }

    public String getSchemeId() {
        return preferences.getString(SCHEME_ID, "");
    }

    public void setSchemeName(String schemeName) {
        editor.putString(SCHEME_NAME, schemeName);
        editor.commit();
    }

    public String getSchemeName() {
        return preferences.getString(SCHEME_NAME, "");
    }

    public void setSqcName(String sqcName) {
        editor.putString(SQC_NAME, sqcName);
        editor.commit();
    }

    public String getSqcName() {
        return preferences.getString(SQC_NAME, "");
    }


//    public void setSqcId(String id) {
//        editor.putString(SQC_ID, id);
//        editor.commit();
//    }
//
//    public String getSqcId() {
//        return preferences.getString(SQC_ID, "");
//    }

    public void setTpiaName(String tpiaName) {
        editor.putString(TPIA_NAME, tpiaName);
        editor.commit();
    }

    public String getTpiaName() {
        return preferences.getString(TPIA_NAME, "");
    }

    public void setTpiaId(String tpiaId) {
        editor.putString(TPIA_ID, tpiaId);
        editor.commit();
    }

    public String getTpiaId() {
        return preferences.getString(TPIA_ID, "");
    }

    public void setPiuName(String name) {
        editor.putString(PIU_NAME, name);
        editor.commit();
    }

    public String getPiuName() {
        return preferences.getString(PIU_NAME, "");
    }

    public void setPiuId(String id) {
        editor.putString(PIU_ID, id);
        editor.commit();
    }

    public String getPiuId() {
        return preferences.getString(PIU_ID, "");
    }

//    public void setIsDiLogged(boolean isDiLogged) {
//        editor.putBoolean(IS_DI_LOGGED, isDiLogged);
//        editor.commit();
//    }
//
//    public boolean isDILogged() {
//        return preferences.getBoolean(IS_DI_LOGGED, false);
//    }

//    public void setDiPassword(String password) {
//        editor.putString(DI_PASSWORD, password);
//        editor.commit();
//    }

//    public String getDiPassword() {
//        return preferences.getString(DI_PASSWORD, "");
//    }

//    public void setDiMobile(String mobile) {
//        editor.putString(DI_MOBILE, mobile);
//        editor.commit();
//    }
//
//    public String getDiMobile() {
//        return preferences.getString(DI_MOBILE, "");
//    }

//    public void setDesignation(String designation) {
//        editor.putString(DESIGNATION, designation);
//        editor.commit();
//    }
//
//    public String getDesignation() {
//        return preferences.getString(DESIGNATION, "");
//    }


    public void setSourceType(String sourceType) {
        editor.putString(SOURCE_TYPE, sourceType);
        editor.commit();
    }

    public String getSourceType() {
        return preferences.getString(SOURCE_TYPE, "");
    }

//    public void setRoleId(String roleId) {
//        editor.putString(ROLE_ID, roleId);
//        editor.commit();
//    }
//
//    public String getRoleId() {
//        return preferences.getString(ROLE_ID, "");
//    }

//    public void setIsDharaAccess(int access) {
//        editor.putInt(IS_DHARA_ACCESS, access);
//        editor.commit();
//    }

//    public int isDharaAccess() {
//        return preferences.getInt(IS_DHARA_ACCESS, 0);
//    }

    public void setEsrNameList(List<String> esrNameList) {
        Set<String> stringSet = new HashSet<>(esrNameList);
        editor.putStringSet(ESR_NAME_LIST, stringSet);
        editor.commit();
    }

    public List<String> getEsrNameList() {
        Set<String> stringSet = preferences.getStringSet(ESR_NAME_LIST, new HashSet<>());
        List<String> stringList = new ArrayList<>(stringSet);
        return stringList;
    }

    public void logout() {
        editor.clear();
        editor.apply();
        SqLite.instance(context).logout();
    }

}
