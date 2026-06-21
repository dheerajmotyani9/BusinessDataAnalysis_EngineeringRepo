package mapitgis.jalnigam.core;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Component implements Serializable {
    private String head,layer,surveyLayer;
    private int type;

    public Component(String head, @NonNull String layer, int type) {
        this.head = head;
        this.layer = layer;
        this.type = type;
        this.surveyLayer = layer.replace(":",":surveyed_");
    }

    public String getHead() {
        return head;
    }

    public String getLayer() {
        return layer;
    }
    public String getSurveyedLayer() {
        return surveyLayer;
    }

    public int getType() {
        return type;
    }
}
