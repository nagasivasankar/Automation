package models;

import java.io.Serializable;

/**
 * Created by Administrator on 11/29/2016.
 */
public class Schedule implements Serializable{

    String year;
    String weekStartDayOfTheYear;
    String rating;
    String scheduleStatus;
    String acknowledgeDate;
    String startOfWeekDate;
    String endOfWeekDate;
    boolean isSeen;
    String weeklyScheduleId;
    boolean isCurrentWeek;
    boolean isFutureWeek;
    boolean isFinal;
    boolean isPastWeek;
    WorkerKey workerKey;
    BusinessKey businessKey;

    public boolean isFutureWeek() {
        return isFutureWeek;
    }

    public void setIsFutureWeek(boolean isFutureWeek) {
        this.isFutureWeek = isFutureWeek;
    }

    public boolean isCurrentWeek() {
        return isCurrentWeek;
    }

    public void setIsCurrentWeek(boolean isCurrentWeek) {
        this.isCurrentWeek = isCurrentWeek;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean isPastWeek() {
        return isPastWeek;
    }

    public void setIsPastWeek(boolean isPastWeek) {
        this.isPastWeek = isPastWeek;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getWeekStartDayOfTheYear() {
        return weekStartDayOfTheYear;
    }

    public void setWeekStartDayOfTheYear(String weekStartDayOfTheYear) {
        this.weekStartDayOfTheYear = weekStartDayOfTheYear;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getAcknowledgeDate() {
        return acknowledgeDate;
    }

    public void setAcknowledgeDate(String acknowledgeDate) {
        this.acknowledgeDate = acknowledgeDate;
    }

    public String getStartOfWeekDate() {
        return startOfWeekDate;
    }

    public void setStartOfWeekDate(String startOfWeekDate) {
        this.startOfWeekDate = startOfWeekDate;
    }

    public String getEndOfWeekDate() {
        return endOfWeekDate;
    }

    public void setEndOfWeekDate(String endOfWeekDate) {
        this.endOfWeekDate = endOfWeekDate;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public String getWeeklyScheduleId() {
        return weeklyScheduleId;
    }

    public void setWeeklyScheduleId(String weeklyScheduleId) {
        this.weeklyScheduleId = weeklyScheduleId;
    }

    public WorkerKey getWorkerKey() {
        return workerKey;
    }

    public void setWorkerKey(WorkerKey workerKey) {
        this.workerKey = workerKey;
    }

    public BusinessKey getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(BusinessKey businessKey) {
        this.businessKey = businessKey;
    }
}
