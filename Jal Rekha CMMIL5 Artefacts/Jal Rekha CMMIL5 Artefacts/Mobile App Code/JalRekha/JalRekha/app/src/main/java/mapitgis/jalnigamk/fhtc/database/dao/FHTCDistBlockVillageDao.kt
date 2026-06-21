package mapitgis.jalnigamk.fhtc.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mapitgis.jalnigamk.fhtc.database.table.FHTCDistBlockVillage

@Dao
interface FHTCDistBlockVillageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVillage(village: FHTCDistBlockVillage)

    @Query("SELECT * FROM fhtc_dist_village WHERE villageCode = :villageCode")
    suspend fun getVillageByCode(villageCode: String): FHTCDistBlockVillage?

    @Query("SELECT * FROM fhtc_dist_village")
    suspend fun getAllVillages(): List<FHTCDistBlockVillage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(villages: List<FHTCDistBlockVillage>)

    @Query("DELETE FROM fhtc_dist_village")
    suspend fun clearTable()


    @Query("SELECT DISTINCT districtCode, districtName FROM fhtc_dist_village ORDER BY districtName")
    suspend fun getDistricts(): List<DistrictProjection>

    @Query("SELECT DISTINCT blockCode, blockName FROM fhtc_dist_village WHERE districtCode = :districtCode ORDER BY blockName")
    suspend fun getBlocks(districtCode: String): List<BlockProjection>

    @Query("SELECT DISTINCT lgdGpCode, lgdGpName FROM fhtc_dist_village WHERE blockCode = :blockCode ORDER BY lgdGpName")
    suspend fun getGramPanchayats(blockCode: String): List<GpProjection>

    @Query("SELECT DISTINCT villageCode, villageName FROM fhtc_dist_village WHERE lgdGpCode = :gpCode ORDER BY villageName")
    suspend fun getVillages(gpCode: String): List<VillageProjection>
}



data class DistrictProjection(
    val districtCode: String,
    val districtName: String
){
    override fun toString(): String {
        return districtName
    }
}

data class BlockProjection(
    val blockCode: String,
    val blockName: String
){
    override fun toString(): String {
        return blockName
    }
}

data class GpProjection(
    val lgdGpCode: String,
    val lgdGpName: String
){
    override fun toString(): String {
        return lgdGpName
    }
}

data class VillageProjection(
    val villageCode: String,
    val villageName: String
){
    override fun toString(): String {
        return villageName
    }
}