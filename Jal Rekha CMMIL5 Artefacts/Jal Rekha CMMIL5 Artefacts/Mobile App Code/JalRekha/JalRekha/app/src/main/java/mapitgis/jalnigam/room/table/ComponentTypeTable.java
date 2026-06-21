package mapitgis.jalnigam.room.table;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "component_type_table")
public class ComponentTypeTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "comp_id")
    private String componentId;

    @ColumnInfo(name = "component_name")
    private String componentName;

    @ColumnInfo(name = "component_type")
    private String componentType;

    @ColumnInfo(name = "component_type_name")
    private String componentTypeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentTypeName() {
        return componentTypeName;
    }

    public void setComponentTypeName(String componentTypeName) {
        this.componentTypeName = componentTypeName;
    }
}
