package mapitgis.jalnigam.room.table

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "pipe_line_master")
data class PipeLineTable(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @SerializedName("component_id") val componentId: String,
    @SerializedName("dia") val dia: String,
    @SerializedName("length") val length: String,
    @SerializedName("material") val material: String,
    @SerializedName("mclass") val mclass: String? = null,
    @SerializedName("pip_no") val pipNo: String,
    @SerializedName("scheme_id") val schemeId: String,
    @SerializedName("start_node") val startNode: String,
    @SerializedName("stop_node") val stopNode: String,
    @SerializedName("survey_uid") val surveyUid: String,
    @SerializedName("thickness") val thickness: String,
    @SerializedName("zone_no") val zoneNo: String?,

) : Parcelable {
    override fun toString(): String {
        return pipNo
    }
}
