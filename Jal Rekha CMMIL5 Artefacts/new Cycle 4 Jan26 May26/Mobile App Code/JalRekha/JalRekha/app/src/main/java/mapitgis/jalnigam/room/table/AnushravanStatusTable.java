package mapitgis.jalnigam.room.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "status_table")
public class AnushravanStatusTable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "status_group")
    private int statusGroup;
    @ColumnInfo(name = "status_group_name")
    private String statusGrpName;
    @ColumnInfo(name = "status_id")
    private int statusId;
    @ColumnInfo(name = "status_name")
    private String statusName;

    public void setId(int id) {
        this.id = id;
    }


    public void setStatusGroup(int statusGroup) {
        this.statusGroup = statusGroup;
    }

    public void setStatusGrpName(String statusGrpName) {
        this.statusGrpName = statusGrpName;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getId() {
        return id;
    }

    public int getStatusGroup() {
        return statusGroup;
    }

    public String getStatusGrpName() {
        return statusGrpName;
    }

    public int getStatusId() {
        return statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    @Override
    public String toString() {
        return statusName;
    }
}
