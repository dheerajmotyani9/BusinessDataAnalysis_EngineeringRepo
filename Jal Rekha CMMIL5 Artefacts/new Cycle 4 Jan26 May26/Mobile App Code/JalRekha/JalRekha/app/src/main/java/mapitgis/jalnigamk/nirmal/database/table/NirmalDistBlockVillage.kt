package mapitgis.jalnigamk.nirmal.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "nirmal_dist_village")
data class NirmalDistBlockVillage(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("vill_cd") val villageCode: String,
    @SerializedName("b_cd") val blockCode: String,
    @SerializedName("b_nm_e") val blockName: String,
    @SerializedName("dist") val districtCode: String,
    @SerializedName("dist_nm_e") val districtName: String,
    @SerializedName("lgd_gp_cd") val lgdGpCode: String,
    @SerializedName("lgd_gp_nm_e") val lgdGpName: String,
    @SerializedName("vil_nm_e") val villageName: String,
    @SerializedName("ohsr_code") val esrCode: String,
    @SerializedName("ohsr_name") val esrName: String,
)
