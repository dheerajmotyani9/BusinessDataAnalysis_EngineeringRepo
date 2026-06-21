package mapitgis.jalnigamk.fhtc.database.dao


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mapitgis.jalnigamk.fhtc.database.table.FHTCNICSurveyInfo
import mapitgis.jalnigamk.fhtc.database.table.FHTCUpdateSurveyInfo

@Dao
interface FHTCUpdateSurveyDao {

    // Insert or update (replace on conflict)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurvey(survey: FHTCUpdateSurveyInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurveys(surveys: List<FHTCUpdateSurveyInfo>)

    @Query("DELETE FROM update_survey WHERE surveyId = :surveyId")
    suspend fun deleteSurvey(surveyId: Int)

    // Get all surveys
    @Query("SELECT * FROM update_survey")
    fun getAllSurveys(): LiveData<List<FHTCUpdateSurveyInfo>>

    @Query("DELETE FROM update_survey")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM update_survey")
    fun getTotalCountLive(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM update_survey WHERE status = 0 AND schemeId =:schemeId")
    fun getTotalDraftCountLive(schemeId:String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM update_survey WHERE status = 1 AND schemeId =:schemeId")
    fun getTotalCompletedCountLive(schemeId:String): LiveData<Int>

    @Query("SELECT COUNT(*) FROM update_survey WHERE villageCode = :villageCode")
    suspend fun getSurveyCountForVillage(villageCode: String): Int

    @Query("""
        UPDATE update_survey 
        SET 
            imagePath = :imagePath,
            latitude = :latitude,
            longitude = :longitude,
            accuracy = :accuracy,
            remark = :remark,
            surveyDateTime = :surveyDateTime,
            status = 1 
        WHERE surveyId = :surveyId
    """)
    suspend fun updateSurveyDetails(
        surveyId: Int,
        imagePath: String,
        latitude: Double,
        longitude: Double,
        accuracy: Double,
        remark: String?,
        surveyDateTime: String
    )


    @Query("""UPDATE update_survey SET status = 1 WHERE surveyId = :surveyId""")
    suspend fun updateStatusCompleted(surveyId: Int)
}
