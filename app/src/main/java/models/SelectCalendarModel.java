package models;

import java.io.Serializable;

/**
 * Created by Administrator on 08-Mar-17.
 */
public class SelectCalendarModel implements Serializable {
    public String calendarName;
    public boolean selectedCalendar;

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public boolean isSelectedCalendar() {
        return selectedCalendar;
    }

    public void setSelectedCalendar(boolean selectedCalendar) {
        this.selectedCalendar = selectedCalendar;
    }
}
