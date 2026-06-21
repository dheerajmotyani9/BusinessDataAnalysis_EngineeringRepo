package mapitgis.jalnigamk.fhtc.repository

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mapitgis.jalnigam.BuildConfig
import mapitgis.jalnigam.core.Login
import mapitgis.jalnigam.core.SqLite
import mapitgis.jalnigam.core.Utility
import mapitgis.jalnigam.network.ApiClient
import mapitgis.jalnigam.rfi.helper.PrefManager
import mapitgis.jalnigamk.fhtc.database.FHTCDb
import mapitgis.jalnigamk.fhtc.database.dao.FHTCDao
import mapitgis.jalnigamk.fhtc.database.dao.FHTCDistBlockVillageDao
import mapitgis.jalnigamk.fhtc.database.dao.FHTCNICSurveyDao
import mapitgis.jalnigamk.fhtc.database.dao.FHTCUpdateSurveyDao
import mapitgis.jalnigamk.fhtc.database.table.FHTCDistBlockVillage
import mapitgis.jalnigamk.fhtc.database.table.FHTCNICSurveyInfo
import mapitgis.jalnigamk.fhtc.database.table.FHTCUpdateSurveyInfo
import mapitgis.jalnigamk.network.BaseResponseNIC
import mapitgis.jalnigamk.network.FHTCApiInterface
import mapitgis.jalnigamk.network.FHTCApiInterface.Companion.API_KEY
import mapitgis.jalnigamk.network.FHTCApiInterface.Companion.CLIENT_CODE
import mapitgis.jalnigamk.network.FHTCApiInterface.Companion.ENCRYPTION_KEY
import mapitgis.jalnigamk.util.AESHelper
import mapitgis.jalnigamk.util.BitmapUtil
import mapitgis.jalnigamk.util.getSafeDouble
import org.json.JSONObject

class FHTCRepository(val context: Context) {

    private var distBlockVillageDao: FHTCDistBlockVillageDao

    private var nicSurveyDao: FHTCNICSurveyDao
    private var fhtcDao: FHTCDao

    private var updateSurveyDao: FHTCUpdateSurveyDao

    private var apiInterface: FHTCApiInterface
    private var sqLite = SqLite.instance(context)
    var login: Login = sqLite.login
    var isContractor = login.roleId == 10

    var prefManager: PrefManager

    init {
        val db = FHTCDb.getInstance(context)
        distBlockVillageDao = db.fhtcDistBlockDao()
        nicSurveyDao = db.fhtcVillageSurveyDao()
        fhtcDao = db.fhtcDao()
        updateSurveyDao = db.fhtcUpdateSurveyDao()
        prefManager = PrefManager(context)
        apiInterface = ApiClient.getClient(context).create(FHTCApiInterface::class.java)
    }


    suspend fun syncFHTCModuleAsync() : Pair<Boolean, String> {
        val map = HashMap<String,String>().apply {
            put(Utility.E_TOKEN,login.token)
            put("scheme_id",prefManager.schemeId)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = apiInterface.fetchDistBlockVillage(map)
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()?.Success == true) {
                        val data = response.body()?.Data
                        data?.let {
                            saveDistBlockVillage(data)
                        }
                    }
                }
                return@withContext Pair(response.body()?.Success?: false,response.body()?.Message ?: "Something went wrong")
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Pair(false,"Something went wrong")
            }
        }
    }


    suspend fun insertFHTCSurveys() : Pair<Boolean, String>{
        return withContext(Dispatchers.IO) {
            val gson = Gson()
            val draftSurveyList = fhtcDao.getPendingUpdateSurveys()
            if(draftSurveyList.isEmpty()){
                return@withContext Pair(true,"No draft survey found")
            }

            var error = false
            for (survey in draftSurveyList) {
                try {
                    val jsonString = gson.toJson(survey.NICSurvey)
                    val type = object : TypeToken<HashMap<String, Any>>() {}.type
                    val map = gson.fromJson<HashMap<String, Any>>(jsonString, type)
                    map.apply {
                        put("SurveyID", survey.update.surveyId)
                        put("etoken", login.token)
                        put("contractor_id", survey.update.surveyorId)
                        put("remark", survey.update.remark?:"")
                        put("update_latitude", survey.update.latitude)
                        put("update_longitude", survey.update.longitude)
                        put("update_accuracy", survey.update.accuracy)
                        put("update_datetime", survey.update.surveyDateTime)
                    }

                    val bitmap = BitmapUtil.uriToBitmap(context = context, imageUri = Uri.parse(survey.update.imagePath ?: ""))
                    bitmap?.let {
                        val bitmapString = BitmapUtil.encodeImageToBase64(it)
                        map.put("image", bitmapString)
                    }

                    val isInvalidPhoto = map["image"]?.toString().isNullOrBlank()
                    if (isInvalidPhoto) {
                        return@withContext Pair(false,"Invalid sample photo")
                    }

                    val json = Gson().toJson(map)
                    println(json)
                    val response = apiInterface.insertFHTCSurvey(map)
                    if(response.Success && response.Data != null){
                        val updatedInfo = response.Data.get(0)?.asJsonObject
                        updatedInfo?.let { it ->
                            //val statusMsg = it.get("status").asString
                            updateSurveyDao.updateSurveyDetails(survey.update.surveyId,
                                imagePath = BuildConfig.JAL_NIGAM_IMAGE+it.get("image_base").asString,
                                latitude = it.get("lat")?.asDouble ?: 0.0,
                                longitude = it.get("logn")?.asDouble ?: 0.0,
                                accuracy =  it.getSafeDouble("accuracy"),
                                remark = it.get("remark")?.asString ?: "",
                                surveyDateTime = it.get("updated_survey_date").asString)
                            //delete local image
                            BitmapUtil.deleteImage(context,survey.update.imagePath)
                        }
                    }else{
                        error = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    error = true
                }
            }

            if(error){
                return@withContext Pair(false,"Something went wrong")
            }else{
                return@withContext Pair(true,"Synced Successfully")
            }
        }
    }



    suspend fun fetchVillageSurveyData(villageCode: String) : Pair<Boolean, String> {
        return withContext(Dispatchers.IO) {
            try {
                val encryVill = AESHelper.encrypt(villageCode,ENCRYPTION_KEY)
                val response = apiInterface.fetchNICSurveyList(CLIENT_CODE,API_KEY,encryVill)
                if (response.body() != null) {
                    val decryptedJson = AESHelper.decrypt(response.body()!!.string(),ENCRYPTION_KEY)

                    val status = JSONObject(decryptedJson).getString("Status")
                    if(status.equals("00")){
                        val gson = GsonBuilder().setLenient().create()
                        val type = object : TypeToken<BaseResponseNIC<List<FHTCNICSurveyInfo>>>() {}.type
                        val baseRes: BaseResponseNIC<List<FHTCNICSurveyInfo>> = gson.fromJson(decryptedJson, type)
                        baseRes.Data?.let {
                            saveExistingVillageSurveyData(villageCode,it)
                        }
                        return@withContext Pair(true,baseRes.Message)
                    }else{
                        return@withContext Pair(false,JSONObject(decryptedJson).getString("Message"))
                    }
                }else{
                    return@withContext Pair(false,"Something went wrong")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext Pair(false,"Something went wrong")
            }
        }
    }


    suspend fun saveDistBlockVillage(list: List<FHTCDistBlockVillage>) {
        withContext(Dispatchers.IO){
            distBlockVillageDao.clearTable()
            distBlockVillageDao.insertAll(list)
        }
    }

    suspend fun saveExistingVillageSurveyData(vCode:String,list: List<FHTCNICSurveyInfo>) {
        withContext(Dispatchers.IO){
            val updatedList = list.map { survey -> survey.copy(villageCode = vCode,schemeId = prefManager.schemeId) }
            nicSurveyDao.deleteByVillageCode(vCode)
            nicSurveyDao.insertSurveys(updatedList)
        }
    }



    private fun getStringSafely(jsonObject: JsonObject, key: String): String? {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull && jsonObject.get(key).asString.isNotBlank()) {
            jsonObject.get(key).asString
        } else {
            null
        }
    }

    suspend fun getDistricts() = distBlockVillageDao.getDistricts()

    suspend fun getBlocks(districtCode: String) = distBlockVillageDao.getBlocks(districtCode)

    suspend fun getGramPanchayats(blockCode: String) = distBlockVillageDao.getGramPanchayats(blockCode)

    suspend fun getVillages(gpCode: String) = distBlockVillageDao.getVillages(gpCode)

    fun getVillageSurveyListNIC(villageCode: String, consent: String) = fhtcDao.getSurveyByVillageCode(villageCode,consent)

    fun getUpdatedVillageSurveyList(villageCode: String,status: Int) = fhtcDao.getSurveysHistoryByVillage(villageCode,status)

    fun getAssignedVillages() = fhtcDao.getAssignedVillageInfo(prefManager.schemeId)

    fun getTotalExistingSurveyCount() = nicSurveyDao.getTotalCountLive(prefManager.schemeId)
    fun getTotalDraftSurveyCount() = updateSurveyDao.getTotalDraftCountLive(prefManager.schemeId)
    fun getTotalCompletedSurveyCount() = updateSurveyDao.getTotalCompletedCountLive(prefManager.schemeId)

    suspend fun saveSurveyInfo(surveyInfo: FHTCUpdateSurveyInfo) = updateSurveyDao.insertSurvey(surveyInfo)

    suspend fun deleteDraftSurvey(surveyInfo: FHTCUpdateSurveyInfo){
        withContext(Dispatchers.IO){
            updateSurveyDao.deleteSurvey(surveyInfo.surveyId)
            BitmapUtil.deleteImage(context,surveyInfo.imagePath)
        }
    }

    suspend fun checkIfAllSurveyUpdated(villageCode: String): Boolean {
        return withContext(Dispatchers.IO) {
            nicSurveyDao.getSurveyCountForVillage(villageCode) == updateSurveyDao.getSurveyCountForVillage(villageCode)
        }
    }
}



