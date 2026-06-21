package mapitgis.jalnigamk.nirmal.database.dao

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import kotlinx.parcelize.Parcelize
import mapitgis.jalnigamk.nirmal.collection.WaterPointType
import mapitgis.jalnigamk.nirmal.database.table.NirmalWQMEntity
import java.text.SimpleDateFormat
import java.util.Locale

@Dao
interface NirmalSurveyTransDao {


    @Query(
        """
           SELECT 
    nas.assignId AS surveyId, 
    nas.schemeId AS schId, 
    nas.schemeName AS schName, 
    nas.allotmentType AS allotType, 
    nas.allotmentTypeName AS allotTypeName, 
    nas.activeDate AS activeDate, 
    nas.beneficiary AS institute, 
    nas.esrOhtCode AS esrCode, 
    nas.esrName AS esrName,
    nas.assetLatitude AS assetLatitude, 
    nas.assetLongitude AS assetLongitude, 
    nqm.*, 
    (SELECT COUNT(*) 
     FROM nirmal_quality_monitoring 
     WHERE assignedSurveyId = nas.assignId 
       AND collectionId = :beneficiaryType   
       AND uploaded = 0) AS ohtBeneficiaryOffline,
    (SELECT COUNT(*) 
     FROM nirmal_quality_monitoring 
     WHERE assignedSurveyId = nas.assignId 
       AND collectionId = :beneficiaryType 
       AND uploaded = 1) AS ohtBeneficiarySubmitted
    FROM nirmal_assigned_survey nas 
    LEFT JOIN nirmal_quality_monitoring nqm 
        ON nas.assignId = nqm.assignedSurveyId 
        AND nas.allotmentType = nqm.collectionId
        AND nas.userId = nqm.userId 
        AND strftime('%Y-%m-%d', nqm.created_at / 1000, 'unixepoch') = date(:strDate) 
    WHERE nas.isActive = '1' 
        AND nas.userId = :userId 
        ORDER BY nas.allotmentType;
        """
    )
    fun getCombinedSurveyTransactions(strDate: String, beneficiaryType: Int, userId: String): LiveData<List<CombineSurveyTransaction>>




    /*for filter with collect type*/
    @Query(
        """
           SELECT 
    nas.assignId AS surveyId, 
    nas.schemeId AS schId, 
    nas.schemeName AS schName, 
    nas.allotmentType AS allotType, 
    nas.allotmentTypeName AS allotTypeName, 
    nas.activeDate AS activeDate, 
    nas.beneficiary AS institute, 
    nas.esrOhtCode AS esrCode, 
    nas.esrName AS esrName, 
    nas.assetLatitude AS assetLatitude, 
    nas.assetLongitude AS assetLongitude, 
    nqm.*, 
    (SELECT COUNT(*) 
     FROM nirmal_quality_monitoring 
     WHERE assignedSurveyId = nas.assignId 
       AND collectionId = :beneficiaryType   
       AND uploaded = 0) AS ohtBeneficiaryOffline,
    (SELECT COUNT(*) 
     FROM nirmal_quality_monitoring 
     WHERE assignedSurveyId = nas.assignId 
       AND collectionId = :beneficiaryType 
       AND uploaded = 1) AS ohtBeneficiarySubmitted
    FROM nirmal_assigned_survey nas 
    LEFT JOIN nirmal_quality_monitoring nqm 
        ON nas.assignId = nqm.assignedSurveyId 
        AND nas.allotmentType = nqm.collectionId 
        AND nas.userId = nqm.userId
        AND strftime('%Y-%m-%d', nqm.created_at / 1000, 'unixepoch') = date(:strDate) 
    WHERE nas.isActive = '1' 
        AND nas.userId = :userId 
        AND nas.allotmentType IN (:filter) 
        ORDER BY nas.allotmentType;
        """
    )
    fun getCombinedSurveyTransactionsFilter(strDate: String, beneficiaryType: Int, userId: String, filter:List<Int>): LiveData<List<CombineSurveyTransaction>>
}


@Parcelize
data class CombineSurveyTransaction(
    val surveyId: String = "",
    val schName: String = "",
    val schId: String = "",
    val allotTypeName: String = "",
    val allotType: String = "",
    val activeDate: String = "",
    val institute: String = "",
    val esrName: String = "",
    val esrCode: String = "",
    val ohtBeneficiaryOffline: Int = 0,
    val ohtBeneficiarySubmitted: Int = 0,
    val assetLatitude: Double? = 0.0,
    val assetLongitude: Double? = 0.0,


    @Embedded val transaction: NirmalWQMEntity? = null
) : Parcelable {
    fun isBeneficiaryType(): Boolean {
        return allotType.toIntOrNull() == WaterPointType.BENEFICIARY.id
    }

    fun isBulkMeterType(): Boolean{
        return allotType.toIntOrNull() == WaterPointType.BULK_METER.id
    }

    fun isOHTType(): Boolean{
        return allotType.toIntOrNull() == WaterPointType.OHT_ESR.id
    }

    fun getBeneficiaryCount():Int{
        return ohtBeneficiaryOffline+ohtBeneficiarySubmitted
    }

    fun isAssetLocationValid(): Boolean {
        return assetLatitude != null && assetLatitude != 0.0 &&
                assetLongitude != null && assetLongitude != 0.0
    }

    fun getFormattedActiveDate():String{
        return try {
            val parser = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val formatter = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            val date = parser.parse(activeDate)
            date?.let { formatter.format(it) } ?: activeDate
        } catch (e: Exception) {
            activeDate
        }

    }
}