package mapitgis.jalnigam.room.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_table")
public class LocationTable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "block_id")
    private String blockId;
    @ColumnInfo(name = "block_name")
    private String blockName;
    @ColumnInfo(name = "district_id")
    private String districtId;
    @ColumnInfo(name = "district_name")
    private String districtName;
    @ColumnInfo(name = "gram_id")
    private String gramId;
    @ColumnInfo(name = "gram_name")
    private String gramName;
    @ColumnInfo(name = "scheme_id")
    private String schemeId;
    @ColumnInfo(name = "scheme_name")
    private String schemeName;
    @ColumnInfo(name = "village_id")
    private String villageId;
    @ColumnInfo(name = "village_name")
    private String villageName;

    public int getId() {
        return id;
    }

    public String getBlockId() {
        return blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getGramId() {
        return gramId;
    }

    public String getGramName() {
        return gramName;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public String getVillageId() {
        return villageId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public void setGramId(String gramId) {
        this.gramId = gramId;
    }

    public void setGramName(String gramName) {
        this.gramName = gramName;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
