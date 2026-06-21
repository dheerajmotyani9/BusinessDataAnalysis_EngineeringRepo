package mapitgis.jalnigamk.fhtc.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "update_survey")
data class FHTCUpdateSurveyInfo(
    @PrimaryKey
    @SerializedName("SurveyID")
    val surveyId: Int,
    val surveyorId: Int,
    val villageCode: String = "",
    val schemeId: String = "",

    val imagePath: String = "",
    val latitude: Double=0.0,
    val longitude: Double=0.0,
    val accuracy: Double=0.0,
    val remark: String? = null,
    val surveyDateTime: String = getCurrentDateTime(),
    val status: Int=0, //0 - offline, 1 - online
)


fun getCurrentDateTime(): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
    return sdf.format(java.util.Date())
}