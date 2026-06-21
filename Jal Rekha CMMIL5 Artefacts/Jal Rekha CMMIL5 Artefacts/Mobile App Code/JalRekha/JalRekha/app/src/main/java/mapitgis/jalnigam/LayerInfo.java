package mapitgis.jalnigam;

import mapitgis.jalnigam.core.Component;

public class LayerInfo {
    String id,name;
    Component component;
    boolean check;

    public LayerInfo(String id, String name, Component component,boolean check) {
        this.id = id;
        this.name = name;
        this.component = component;
        this.check = check;
    }

    public void toggleCheck() {
        this.check = !this.check;
    }
}
