package mapitgis.jalnigamk.nirmal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mapitgis.jalnigamk.nirmal.database.DbMigration.MIGRATION_1_2
import mapitgis.jalnigamk.nirmal.database.dao.NimalWQMDao
import mapitgis.jalnigamk.nirmal.database.dao.NirmalAssignedSurveyDao
import mapitgis.jalnigamk.nirmal.database.dao.NirmalDistBlockVillageDao
import mapitgis.jalnigamk.nirmal.database.dao.NirmalSurveyTransDao
import mapitgis.jalnigamk.nirmal.database.table.NirmalAssignedSurvey
import mapitgis.jalnigamk.nirmal.database.table.NirmalDistBlockVillage
import mapitgis.jalnigamk.nirmal.database.table.NirmalWQMEntity

@Database(entities = [NirmalAssignedSurvey::class, NirmalWQMEntity::class, NirmalDistBlockVillage::class], version = 2, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class NirmalDb : RoomDatabase() {

    abstract fun wqmDao(): NimalWQMDao
    abstract fun nirmalDistBlockDao(): NirmalDistBlockVillageDao
    abstract fun nirmalAssignedSurveyDao(): NirmalAssignedSurveyDao
    abstract fun nirmalSurveyTransDao(): NirmalSurveyTransDao

    companion object {
        @Volatile
        private var INSTANCE: NirmalDb? = null

        fun getInstance(context: Context): NirmalDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NirmalDb::class.java,
                    "jal_rekha_nirmal"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
