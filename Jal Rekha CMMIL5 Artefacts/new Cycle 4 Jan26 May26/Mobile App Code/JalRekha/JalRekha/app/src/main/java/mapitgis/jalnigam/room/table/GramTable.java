package mapitgis.jalnigam.room.table;

import androidx.room.ColumnInfo;

public class GramTable {

    @ColumnInfo(name = "gram_id")
    private String gramId;
    @ColumnInfo(name = "gram_name")
    private String gramName;

    public String getGramId() {
        return gramId;
    }

    public void setGramId(String gramId) {
        this.gramId = gramId;
    }

    public String getGramName() {
        return gramName;
    }

    public void setGramName(String gramName) {
        this.gramName = gramName;
    }
}
