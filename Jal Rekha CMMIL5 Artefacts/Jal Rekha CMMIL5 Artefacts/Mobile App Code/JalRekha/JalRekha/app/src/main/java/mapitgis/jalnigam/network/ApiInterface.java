package mapitgis.jalnigam.network;


import java.util.Map;

import mapitgis.jalnigam.rfi.model.AddContractor;
import mapitgis.jalnigam.rfi.model.AppVersion;
import mapitgis.jalnigam.rfi.model.ContractorDISurvey;
import mapitgis.jalnigam.rfi.model.ContractorLogs;
import mapitgis.jalnigam.rfi.model.DIComplaintLogs;
import mapitgis.jalnigam.rfi.model.InspectionRequest;
import mapitgis.jalnigam.rfi.model.Login;
import mapitgis.jalnigam.rfi.model.PostFEInspection;
import mapitgis.jalnigam.rfi.model.PostInspectionRequest;
import mapitgis.jalnigam.rfi.model.PostRevisitRequest;
import mapitgis.jalnigam.rfi.model.SimpleResponse;
import mapitgis.jalnigam.rfi.model.SyncLogin;
import mapitgis.jalnigam.rfi.model.UpdateStatusDIContractor;
import mapitgis.jalnigam.rfi.model.dhara.DailyWaterSupply;
import mapitgis.jalnigam.rfi.model.dhara.DharaCommentByContractor;
import mapitgis.jalnigam.rfi.model.dhara.DharaContractorApproved;
import mapitgis.jalnigam.rfi.model.dhara.DharaESR;
import mapitgis.jalnigam.rfi.model.dhara.DharaESRHistory;
import mapitgis.jalnigam.rfi.model.dhara.DharaIntakeHistory;
import mapitgis.jalnigam.rfi.model.dhara.DharaVillage;
import mapitgis.jalnigam.rfi.model.dhara.DharaWTPHistory;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    //todo: contractor apis
    @POST("Auth_loginFor_contractor")
    Call<Login> login(@Body Map<String, String> body);

    @POST("SyncDataForAnushravan")
    Call<SyncLogin> syncData(@Body Map<String, String> body);

    @POST("Ins_RfiDetailsForAnushravan")
    Call<SimpleResponse> postInspectionRequest(@Body PostInspectionRequest body);

    @POST("GetContractorReqforAnushravanForContractor")
    Call<InspectionRequest> getInspectionRequest(@Body Map<String, String> body);

    @POST("GetDeptInspection_DataForContractor")
    Call<ContractorDISurvey> getDIRequestContractor(@Body Map<String, String> body);

    @POST("Insert_Dept_Inspection_Data_ResponceForContractor")
    Call<SimpleResponse> updateStatusDIReqContractor(@Body UpdateStatusDIContractor body);

    @POST("GetDeptInspectionHistryForContrctor")
    Call<DIComplaintLogs> getDIContractorLogs(@Body Map<String, String> body);

    @POST("Insert_Contractor_User")
    Call<AddContractor> addContractor(@Body Map<String, String> body);

    @POST("Reschedule_RFI")
    Call<SimpleResponse> rescheduleRFI(@Body Map<String, String> body);

    // TODO: 12-07-2024 : DI and FE APIs
//    @POST("Auth_Login")
//    Call<DILogin> diLogin(@Body Map<String, String> body);
//
//    @POST("LoginWith_Sarthak")
//    Call<DILogin> sarthakLogin(@Body Map<String, String> body);

    @POST("GetContractorReqforAnushravanForFE")
    Call<InspectionRequest> getFEInspectionRequest(@Body Map<String, String> body);

    @POST("GetContractorReqforAnushravanForFE")
    Call<InspectionRequest> getFEInspectionHistory(@Body Map<String, String> body);

    @POST("Survey_By_FE")
    Call<SimpleResponse> submitInspectionByFE(@Body PostFEInspection body);

    @POST("Contractor_Reinpection_Req")
    Call<SimpleResponse> revisitRequestContractor(@Body PostRevisitRequest body);

    @POST("GetPiuResponceforAnushravanForContractor")
    Call<ContractorLogs> getContractorLogs(@Body Map<String, String> body);

    @POST("GetPiuResponceforAnushravanForMobile")
    Call<ContractorLogs> getFELogs(@Body Map<String, String> body);

    @POST("GetDeptInspection_DataForFE")
    Call<ContractorDISurvey> getDIRequestFE(@Body Map<String, String> body);

    @POST("Insert_Dept_Inspection_Data_ResponceForFE")
    Call<SimpleResponse> updateStatusDIReqFE(@Body UpdateStatusDIContractor body);

    @POST("GetDeptInspectionHistryForFE")
    Call<DIComplaintLogs> getDIFELogs(@Body Map<String, String> body);

    // TODO: 30-08-2024 : app version api
    @POST("AppVersion")
    Call<AppVersion> appVersion(@Body Map<String, Integer> body);

    // TODO: 14-10-2024 : DHARA APIs
    @POST("GetDhara_Assigne_User_to_Survey")
    Call<DailyWaterSupply> getDailyWaterSupply(@Body Map<String, String> body);

    @POST("Insert_intake_well_meter_reading")
    Call<SimpleResponse> intakeSubmit(@Body Map<String, Object> body);

    @POST("Update_intake_well_meter_reading")
    Call<SimpleResponse> updateIntakeForm(@Body Map<String, Object> body);

    @POST("Insert_wtp_well_meter_reading")
    Call<SimpleResponse> wtpSubmit(@Body Map<String, Object> body);

    @POST("Update_wtp_well_meter_reading")
    Call<SimpleResponse> updateWTPForm(@Body Map<String, Object> body);

    @POST("Insert_village_well_meter_reading")
    Call<SimpleResponse> esrSubmit(@Body Map<String, Object> body);

    @POST("Update_village_well_meter_reading")
    Call<SimpleResponse> updateESRForm(@Body Map<String, Object> body);

    @POST("GetVillageForDhara")
    Call<DharaVillage> getDharaVillage(@Body Map<String, String> body);

    @POST("Get_dhara_intake_well_meter_reading")
    Call<DharaIntakeHistory> getDharaIntakeHistory(@Body Map<String, Object> body);

    @POST("Get_dhara_wtp_emf_meter_reading")
    Call<DharaWTPHistory> getDharaWTPHistory(@Body Map<String, Object> body);

    @POST("Get_dhara_village_emf_meter_reading")
    Call<DharaESRHistory> getDharaESRHistory(@Body Map<String, String> body);

    @POST("GetEsrForDhara")
    Call<DharaESR> getESRByScheme(@Body Map<String, String> body);

    @POST("Get_Intake_wtp_esr_VillDataForContrator")
    Call<DharaContractorApproved> getDharaContractorApproved(@Body Map<String, String> body);

    @POST("CommentByContractor")
    Call<SimpleResponse> submitDharaContractorRemark(@Body Map<String, String> body);

    @POST("GetCommentByContractorForMobile")
    Call<DharaCommentByContractor> getDharaContractorHistory(@Body Map<String, String> body);
}
