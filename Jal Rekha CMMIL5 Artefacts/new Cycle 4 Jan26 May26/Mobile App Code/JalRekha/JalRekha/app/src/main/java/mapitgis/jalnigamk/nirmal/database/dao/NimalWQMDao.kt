package mapitgis.jalnigamk.nirmal.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update 
import mapitgis.jalnigamk.nirmal.database.table.NirmalWQMEntity

@Dao
interface NimalWQMDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWQM(location: NirmalWQMEntity)

    @Update
    suspend fun updateWQM(location: NirmalWQMEntity)

    @Delete
    suspend fun deleteWQM(location: NirmalWQMEntity)

    @Query("SELECT * FROM nirmal_quality_monitoring WHERE id = :id")
    suspend fun getWQMById(id: Int): NirmalWQMEntity?



    @Query("""
        SELECT * FROM nirmal_quality_monitoring
        WHERE assignedSurveyId = :surveyId AND collectionId = :collectionId AND uploaded = :status 
        AND (:villageId IS NULL OR :villageId = '' OR villageId = :villageId) ORDER BY created_at DESC
    """)
    fun getAllWQM(surveyId: String, collectionId: Int, villageId:String,status:Int): LiveData<List<NirmalWQMEntity>>

    @Query("DELETE FROM nirmal_quality_monitoring")
    suspend fun deleteAllWQM()

    @Query("DELETE FROM nirmal_quality_monitoring WHERE id = :id")
    suspend fun deleteWQMById(id: Int)

    @Query("SELECT * FROM nirmal_quality_monitoring WHERE date(created_at) = date(:formattedDate) AND assignedSurveyId = :surveyId AND collectionId = :collectionId")
    suspend fun getTransactionRecord(surveyId:String,collectionId:String, formattedDate: String): NirmalWQMEntity


    @Query("UPDATE nirmal_quality_monitoring SET uploaded = :uploadedStatus WHERE id = :id")
    suspend fun updateUploadedStatus(id: Int, uploadedStatus: Int=1)

    @Query("UPDATE nirmal_quality_monitoring SET sampleImgFilePath = :filePath WHERE id = :id")
    suspend fun updateSampleFilePath(id: Int, filePath: String)

    @Query("UPDATE nirmal_quality_monitoring SET frcImgFilePath = :filePath WHERE id = :id")
    suspend fun updateFRCFilePath(id: Int, filePath: String)

}