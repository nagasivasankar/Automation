package models;

import java.io.Serializable;

/**
 * Created by Administrator on 3/7/2017.
 */
public enum NotificationType implements Serializable {

    /*TYPE 1*/
    SCHEDULE_PUBLISH("schedule_publish"), SCHEDULE_UPDATE("schedule_update"), SCHEDULE_UPDATED("schedule_updated"), SCHEDULE_FINALIZED("schedule_finalized"),

    /*TYPE 2*/
    SHIFT_OFFER("shift_offer"),

    /*TYPE 3*/
    SHIFT_UPDATED("shift_updated");

    private String type;

    private NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}