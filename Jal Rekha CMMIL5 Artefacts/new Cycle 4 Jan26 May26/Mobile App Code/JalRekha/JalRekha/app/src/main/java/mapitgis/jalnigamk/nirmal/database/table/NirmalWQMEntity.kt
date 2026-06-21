package mapitgis.jalnigamk.nirmal.database.table

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Parcelize
@Entity(tableName = "nirmal_quality_monitoring")
data class NirmalWQMEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val assignedSurveyId : String, //Mapping id - assignID
    val sampleId : String,
    val assignDate : String,

    val latitude: Double,
    val longitude: Double,

    val accuracy: Float,
    val schemeId: Int?=null,
    val schemeName: String,
    val collectionType: String,//allotmentType
    val collectionId: Int,
    val remark: String="",
    val sampleImgFilePath: String="",
    //----------------------------//

    @Expose val frcImgFilePath: String?=null,
    @Expose val frcTestResult: String?=null,

    @Expose val ohtId : String?=null,
    @Expose val ohtName : String?=null,

    @Expose val villageId : Int?=null,
    @Expose val villageName : String?=null,

    @Expose val instituteOtherName : String?=null,

    @Expose val contactPersonName : String?=null,
    @Expose val contactPersonMobile : String?=null,
    @Expose val contactPersonDesignation : String?=null,

    @Expose val beneficiaryName : String?=null,
    @Expose val beneficiaryMobile : String?=null,
    @Expose val beneficiarySamagraId : String?=null,

    @Expose(serialize = false)
    val uploaded: Int = 0, //0-offline, 1-submitted

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    val userId: Int = 0
) : Parcelable{
    fun getFormattedDate(): String {
        val formatter = SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault())
        return formatter.format(createdAt)
    }
}




/*val gson: Gson = GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation() // Important to enforce exclusion
    .create()
val jsonString = gson.toJson(surveyData)*/
