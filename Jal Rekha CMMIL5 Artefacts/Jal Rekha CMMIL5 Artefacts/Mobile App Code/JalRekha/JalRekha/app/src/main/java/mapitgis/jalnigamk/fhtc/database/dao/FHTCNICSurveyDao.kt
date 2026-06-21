package mapitgis.jalnigamk.fhtc.database.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mapitgis.jalnigamk.fhtc.database.table.FHTCNICSurveyInfo

@Dao
interface FHTCNICSurveyDao {

    // Insert or update (replace on conflict)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurvey(survey: FHTCNICSurveyInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurveys(surveys: List<FHTCNICSurveyInfo>)

    // Get all surveys
    @Query("SELECT * FROM nic_survey")
    fun getAllSurveys(): LiveData<List<FHTCNICSurveyInfo>>

    @Query("DELETE FROM nic_survey WHERE villageCode = :villageCode")
    suspend fun deleteByVillageCode(villageCode: String)

    @Query("DELETE FROM nic_survey")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM nic_survey WHERE schemeId = :schemeId")
    fun getTotalCountLive(schemeId:String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM nic_survey WHERE villageCode = :villageCode")
    suspend fun getSurveyCountForVillage(villageCode: String): Int
}
