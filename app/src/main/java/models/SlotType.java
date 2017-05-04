package models;

import java.io.Serializable;

/**
 * Created by Administrator on 12/6/2016.
 */
public enum SlotType implements Serializable{

    EMPTY("Empty"), GREEN("Green"), RED("Red");

    private String type;

    private SlotType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}