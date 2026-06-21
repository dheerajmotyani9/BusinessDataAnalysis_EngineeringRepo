package mapitgis.jalnigamk.fhtc.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import mapitgis.jalnigamk.fhtc.database.table.FHTCNICSurveyInfo
import mapitgis.jalnigamk.fhtc.database.table.FHTCUpdateSurveyInfo

@Dao
interface FHTCDao {

    @Query("""
        SELECT DISTINCT v.villageCode AS villageCode, v.villageName AS villageName
        FROM nic_survey s
        INNER JOIN fhtc_dist_village v
        ON s.villageCode = v.villageCode WHERE v.schemeId = :schemeId
    """)
    fun getAssignedVillageInfo(schemeId: String): LiveData<List<VillageProjection>>



    @Query("SELECT * FROM nic_survey WHERE villageCode = :villageCode AND waterConnectionConsent = :consent AND surveyId NOT IN (SELECT surveyId FROM update_survey)")
    fun getSurveyByVillageCode(
        villageCode: String,
        consent: String
    ): LiveData<List<FHTCNICSurveyInfo>>

    @Transaction
    @Query("SELECT * FROM update_survey WHERE status = 0")
    suspend  fun getPendingUpdateSurveys(): List<UpdateSurveyWithNICSurvey>


    @Transaction
    @Query("""
    SELECT * FROM update_survey 
    WHERE status = :status  AND surveyId IN (SELECT surveyId FROM nic_survey WHERE villageCode = :villageCode)
    """)
    fun getSurveysHistoryByVillage(villageCode: String,status:Int): LiveData<List<UpdateSurveyWithNICSurvey>>
}


data class UpdateSurveyWithNICSurvey(

    @Embedded
    val update: FHTCUpdateSurveyInfo,

    @Relation(
        parentColumn = "surveyId",
        entityColumn = "surveyId"
    )
    val NICSurvey: FHTCNICSurveyInfo
)
