package mapitgis.jalnigamk.fhtc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mapitgis.jalnigamk.fhtc.database.dao.FHTCDao
import mapitgis.jalnigamk.fhtc.database.dao.FHTCDistBlockVillageDao
import mapitgis.jalnigamk.fhtc.database.dao.FHTCNICSurveyDao
import mapitgis.jalnigamk.fhtc.database.dao.FHTCUpdateSurveyDao
import mapitgis.jalnigamk.fhtc.database.table.FHTCDistBlockVillage
import mapitgis.jalnigamk.fhtc.database.table.FHTCNICSurveyInfo
import mapitgis.jalnigamk.fhtc.database.table.FHTCUpdateSurveyInfo
import mapitgis.jalnigamk.nirmal.database.DateConverters

@Database(entities = [FHTCDistBlockVillage::class,
                        FHTCNICSurveyInfo::class,
                        FHTCUpdateSurveyInfo::class
                     ], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class FHTCDb : RoomDatabase() {

    abstract fun fhtcDistBlockDao(): FHTCDistBlockVillageDao

    abstract fun fhtcVillageSurveyDao(): FHTCNICSurveyDao
    abstract fun fhtcDao(): FHTCDao

    abstract fun fhtcUpdateSurveyDao(): FHTCUpdateSurveyDao

    companion object {
        @Volatile
        private var INSTANCE: FHTCDb? = null

        fun getInstance(context: Context): FHTCDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FHTCDb::class.java,
                    "jal_rekha_fhtc"
                )
                    //.addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
