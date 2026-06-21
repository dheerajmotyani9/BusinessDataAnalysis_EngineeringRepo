package mapitgis.jalnigam.room.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "application_type_table")
public class ApplicationTypeTable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "app_type_id")
    private String appTypeId;

    @ColumnInfo(name = "app_type_name")
    private String appTypeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(String appTypeId) {
        this.appTypeId = appTypeId;
    }

    public String getAppTypeName() {
        return appTypeName;
    }

    public void setAppTypeName(String appTypeName) {
        this.appTypeName = appTypeName;
    }
}
