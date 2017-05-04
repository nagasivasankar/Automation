package models;

/**
 * Created by Administrator on 08-Mar-17.
 */
public class AlertModel {
    public String time;
    public boolean selectedTime;

    public String getTimeInText() {
        return timeInText;
    }

    public void setTimeInText(String timeInText) {
        this.timeInText = timeInText;
    }

    public String timeInText;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(boolean selectedTime) {
        this.selectedTime = selectedTime;
    }
}
