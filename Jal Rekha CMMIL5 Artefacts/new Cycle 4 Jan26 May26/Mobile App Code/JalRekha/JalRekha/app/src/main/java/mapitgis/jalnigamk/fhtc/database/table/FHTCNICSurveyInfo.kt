package mapitgis.jalnigamk.fhtc.database.table

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Parcelize
@Entity(tableName = "nic_survey")
data class FHTCNICSurveyInfo(

    @PrimaryKey
    @SerializedName("SurveyID")
    val surveyId: Int,

    @SerializedName("DistrictName")
    val districtName: String,

    @SerializedName("BlockName")
    val blockName: String,

    @SerializedName("GramPanchayat")
    val gramPanchayat: String,

    @SerializedName("VillageName")
    val villageName: String,

    @SerializedName("SchemeName")
    val schemeName: String,

    // Nullable fields
    @SerializedName("Basahat") val basahat: String? = null,
    @SerializedName("FH_SamagraID") val samagraId: String? = null,
    @SerializedName("FamilyID") val familyId: String? = null,
    @SerializedName("FH_NameEn") val nameEn: String? = null,
    @SerializedName("FH_NameHI") val nameHi: String? = null,
    @SerializedName("Father_Husband_Name") val fatherHusbandName: String? = null,
    @SerializedName("Lat") val latitude: String? = null,
    @SerializedName("Long") val longitude: String? = null,
    @SerializedName("MobileNumber") val mobileNumber: String? = null,
    @SerializedName("CCDeposite_Consent") val ccDepositeConsent: String? = null,
    @SerializedName("SecurityDeposite_Consent") val securityDepositeConsent: String? = null,

    @SerializedName("NewTapWaterConnection_Consent")
    val waterConnectionConsent : String,

    @SerializedName("SurveyDate")
    val surveyDate : String,

    val villageCode: String = "",
    val schemeId: String = ""
): Parcelable {

    fun parseDate(): String {
        try {
            val parser = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy") // or "dd-MM-yyyy HH:mm"
            val dateTime = LocalDateTime.parse(surveyDate, parser)
            return dateTime.format(formatter)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    val fullAddress: String
        get() = "${villageName.orEmpty()}, ${blockName.orEmpty()}, ${districtName.orEmpty()}"

}