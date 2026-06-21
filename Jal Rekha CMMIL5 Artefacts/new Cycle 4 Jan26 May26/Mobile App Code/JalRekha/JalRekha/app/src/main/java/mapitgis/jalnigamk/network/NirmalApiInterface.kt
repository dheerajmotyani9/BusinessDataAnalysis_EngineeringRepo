package mapitgis.jalnigamk.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NirmalApiInterface {

    @POST("SyncDataForNirmal")
    fun syncNirmalModule(@Body body: MutableMap<String, String>): Call<BaseResponse<JsonObject>>

    @POST("InsNirmalSurveyByAPP")
    fun insertNirmalModule(@Body body: MutableMap<String, Any>): Call<BaseResponse<JsonObject>>
}