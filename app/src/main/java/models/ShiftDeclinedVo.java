package models;

/**
 * Created by Rabbit on 2/22/2017.
 */

public class ShiftDeclinedVo {

    boolean isTimeconflict;
    boolean isLocationIssue;
    String reason;

    public boolean isTimeconflict() {
        return isTimeconflict;
    }

    public void setTimeconflict(boolean timeconflict) {
        isTimeconflict = timeconflict;
    }

    public boolean isLocationIssue() {
        return isLocationIssue;
    }

    public void setLocationIssue(boolean locationIssue) {
        isLocationIssue = locationIssue;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
