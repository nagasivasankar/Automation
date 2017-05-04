package models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 1/9/2017.
 */
public class ShiftOfferedRequested implements Serializable {
    String role;
    String name;
    String notes;
    String year;
    String weekStartDayOfTheYear;
    String dayOfTheWeek;
    String startMin;
    String endMin;
    String shiftStartDate;
    String shiftEndDate;
    String shiftId;
    String status;
    String clockInTime;
    String clockOutTime;
    String estimatedPay;
    String availability;
    String hasMeal;
    String unpaidBreakMinutes;
    String doubleOvertimeMinutes;
    String weekOvertimeMinutes;
    String weekDoubleOvertimeMinutes;
    String cost;
    String regularMinutes;
    String type;
    String offerStatus;
    ArrayList<AssociatedWorker> associatedWorkerArrayList;
    Worker workerKey;
    BusinessKey businessKey;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getStartMin() {
        return startMin;
    }

    public void setStartMin(String startMin) {
        this.startMin = startMin;
    }

    public String getEndMin() {
        return endMin;
    }

    public void setEndMin(String endMin) {
        this.endMin = endMin;
    }

    public String getShiftStartDate() {
        return shiftStartDate;
    }

    public void setShiftStartDate(String shiftStartDate) {
        this.shiftStartDate = shiftStartDate;
    }

    public String getShiftEndDate() {
        return shiftEndDate;
    }

    public void setShiftEndDate(String shiftEndDate) {
        this.shiftEndDate = shiftEndDate;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(String clockInTime) {
        this.clockInTime = clockInTime;
    }

    public String getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(String clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public String getEstimatedPay() {
        return estimatedPay;
    }

    public void setEstimatedPay(String estimatedPay) {
        this.estimatedPay = estimatedPay;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getHasMeal() {
        return hasMeal;
    }

    public void setHasMeal(String hasMeal) {
        this.hasMeal = hasMeal;
    }

    public String getUnpaidBreakMinutes() {
        return unpaidBreakMinutes;
    }

    public void setUnpaidBreakMinutes(String unpaidBreakMinutes) {
        this.unpaidBreakMinutes = unpaidBreakMinutes;
    }

    public String getDoubleOvertimeMinutes() {
        return doubleOvertimeMinutes;
    }

    public void setDoubleOvertimeMinutes(String doubleOvertimeMinutes) {
        this.doubleOvertimeMinutes = doubleOvertimeMinutes;
    }

    public String getWeekOvertimeMinutes() {
        return weekOvertimeMinutes;
    }

    public void setWeekOvertimeMinutes(String weekOvertimeMinutes) {
        this.weekOvertimeMinutes = weekOvertimeMinutes;
    }

    public String getWeekDoubleOvertimeMinutes() {
        return weekDoubleOvertimeMinutes;
    }

    public void setWeekDoubleOvertimeMinutes(String weekDoubleOvertimeMinutes) {
        this.weekDoubleOvertimeMinutes = weekDoubleOvertimeMinutes;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRegularMinutes() {
        return regularMinutes;
    }

    public void setRegularMinutes(String regularMinutes) {
        this.regularMinutes = regularMinutes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public ArrayList<AssociatedWorker> getAssociatedWorkerArrayList() {
        return associatedWorkerArrayList;
    }

    public void setAssociatedWorkerArrayList(ArrayList<AssociatedWorker> associatedWorkerArrayList) {
        this.associatedWorkerArrayList = associatedWorkerArrayList;
    }

    public Worker getWorkerKey() {
        return workerKey;
    }

    public void setWorkerKey(Worker workerKey) {
        this.workerKey = workerKey;
    }

    public BusinessKey getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(BusinessKey businessKey) {
        this.businessKey = businessKey;
    }
}
