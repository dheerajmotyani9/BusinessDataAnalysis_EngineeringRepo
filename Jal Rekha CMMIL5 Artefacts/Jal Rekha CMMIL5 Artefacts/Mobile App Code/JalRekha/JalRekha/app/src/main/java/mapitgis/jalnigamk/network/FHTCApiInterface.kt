package mapitgis.jalnigamk.network

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import mapitgis.jalnigamk.fhtc.database.table.FHTCDistBlockVillage
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface FHTCApiInterface {

    companion object {
        const val BASE_URL = "https://jalsamagramp.nic.in/api/JalNigam/get_SurveyListByVillageID"
        const val CLIENT_CODE = "JR01"
        const val API_KEY = "DD70157CCD11402287E0C95AF"

        const val ENCRYPTION_KEY = "f1StaSFGpqRxVbw47ghA9DCehXUTmzev"
    }

    @POST("SyncFHTC")
    suspend fun fetchDistBlockVillage(@Body body: MutableMap<String, String>): Response<BaseResponse<List<FHTCDistBlockVillage>>>




    @FormUrlEncoded
    @POST(BASE_URL)
    suspend fun fetchNICSurveyList(@Header("ClientCode") clientCode: String,
                                   @Header("APIKey") apiKey: String,
                                   @Field("VillageID") villageId: String,): Response<ResponseBody>


    @POST("InsertFHTCSurvey")
    suspend fun insertFHTCSurvey(@Body body: MutableMap<String, Any>): BaseResponse<JsonArray>


}