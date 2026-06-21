package mapitgis.jalnigamk.network

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    val Success: Boolean,
    val Message: String,
    val Data: T?
)


data class BaseResponseNIC<T>(
    val Status: String,
    val Message: String,
    @SerializedName("Rows") val Data: T?
)




