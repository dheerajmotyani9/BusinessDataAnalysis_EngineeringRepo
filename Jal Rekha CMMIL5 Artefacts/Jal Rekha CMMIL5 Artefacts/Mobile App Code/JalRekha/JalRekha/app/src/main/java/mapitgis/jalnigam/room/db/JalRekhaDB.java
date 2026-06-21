package mapitgis.jalnigam.room.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import mapitgis.jalnigam.room.dao.JalRekhaDao;
import mapitgis.jalnigam.room.table.AnushravanStatusTable;
import mapitgis.jalnigam.room.table.ApplicationTypeTable;
import mapitgis.jalnigam.room.table.ComponentTypeTable;
import mapitgis.jalnigam.room.table.InspectionRequestTable;
import mapitgis.jalnigam.room.table.LocationTable;
import mapitgis.jalnigam.room.table.MediaTable;
import mapitgis.jalnigam.room.table.PipeLineTable;
import mapitgis.jalnigam.room.table.PointTable;
import mapitgis.jalnigam.room.table.SopanOHT;

@Database(entities = {ApplicationTypeTable.class, LocationTable.class, ComponentTypeTable.class,
        PointTable.class, InspectionRequestTable.class,
        MediaTable.class, AnushravanStatusTable.class,
        PipeLineTable.class, SopanOHT.class
}, version = 5,exportSchema = false)
public abstract class JalRekhaDB extends RoomDatabase {

    public abstract JalRekhaDao jalRekhaDao();

    private static volatile JalRekhaDB jalRekhaDB;

    public static JalRekhaDB getDatabase(Context context) {

        if (jalRekhaDB == null) {

            synchronized (JalRekhaDB.class) {

                if (jalRekhaDB == null) {
                    jalRekhaDB = Room.databaseBuilder(context.getApplicationContext(),
                                    JalRekhaDB.class, "jal_rekha_rfi_only")
                            // .fallbackToDestructiveMigration()  // WARNING: Deletes old data
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .build();
                }
            }
        }

        return jalRekhaDB;
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE inspection_request_table ADD COLUMN stage TEXT");
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE inspection_request_table ADD COLUMN stageId TEXT");
        }
    };


    public static final Migration MIGRATION_3_4 = new Migration(3,4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `pipe_line_master` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`componentId` TEXT NOT NULL, " +
                    "`dia` TEXT NOT NULL, " +
                    "`length` TEXT NOT NULL, " +
                    "`material` TEXT NOT NULL, " +
                    "`mclass` TEXT, " +
                    "`pipNo` TEXT NOT NULL, " +
                    "`schemeId` TEXT NOT NULL, " +
                    "`startNode` TEXT NOT NULL, " +
                    "`stopNode` TEXT NOT NULL, " +
                    "`surveyUid` TEXT NOT NULL, " +
                    "`thickness` TEXT NOT NULL)");

            database.execSQL("ALTER TABLE inspection_request_table ADD COLUMN mbr_oht_surveyid TEXT NOT NULL DEFAULT '0'");
            database.execSQL("ALTER TABLE inspection_request_table ADD COLUMN pipe_no TEXT NOT NULL DEFAULT '0'");
            database.execSQL("ALTER TABLE inspection_request_table ADD COLUMN length_slot TEXT NOT NULL DEFAULT ''");
        }
    };



    public static final Migration MIGRATION_4_5 = new Migration(4,5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE sopan_oht_master (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "componentId TEXT NOT NULL, " +
                            "schemeId TEXT NOT NULL, " +
                            "ohtType TEXT NOT NULL, " +
                            "surveyUid TEXT NOT NULL, " +
                            "ohtCode TEXT NOT NULL, " +
                            "ohtName TEXT NOT NULL" +
                            ")"
            );

            database.execSQL("ALTER TABLE inspection_request_table ADD COLUMN type_of_sopan_oht TEXT NOT NULL DEFAULT ''");
            database.execSQL("ALTER TABLE pipe_line_master ADD COLUMN zoneNo TEXT");
        }
    };
}
