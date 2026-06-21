package mapitgis.jalnigamk.fhtc.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "fhtc_dist_village")
data class FHTCDistBlockVillage(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("vill_cd") val villageCode: String,
    @SerializedName("vil_nm_e") val villageName: String,

    @SerializedName("dist_cd") val districtCode: String,
    @SerializedName("dist_nm_e") val districtName: String,

    @SerializedName("b_cd") val blockCode: String,
    @SerializedName("b_nm_e") val blockName: String,

    @SerializedName("lgd_gp_cd") val lgdGpCode: String,
    @SerializedName("lgd_gp_nm_e") val lgdGpName: String,



    @SerializedName("scheme_id") val schemeId: String,
    @SerializedName("scheme_name") val schemeName: String,
)
