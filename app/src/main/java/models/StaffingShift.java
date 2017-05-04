package models;

import java.io.Serializable;

/**
 * Created by Administrator on 1/4/2017.
 */
public class StaffingShift implements Serializable {
    BusinessKey businessKey;

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
    String slotOffset;
    String shiftLength;
    String shiftOrder;
    String unpaidBreakMinutes;
    String workerKey;
    String availability;
    String type;
    String staffingWorkerType;
    String assignmentWorkerType;
    String overtimeMinutes;
    String doubleOvertimeMinutes;
    String regularMinutes;
    boolean hasMeal;

    public BusinessKey getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(BusinessKey businessKey) {
        this.businessKey = businessKey;
    }

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

    public String getSlotOffset() {
        return slotOffset;
    }

    public void setSlotOffset(String slotOffset) {
        this.slotOffset = slotOffset;
    }

    public String getShiftLength() {
        return shiftLength;
    }

    public void setShiftLength(String shiftLength) {
        this.shiftLength = shiftLength;
    }

    public String getShiftOrder() {
        return shiftOrder;
    }

    public void setShiftOrder(String shiftOrder) {
        this.shiftOrder = shiftOrder;
    }

    public String getUnpaidBreakMinutes() {
        return unpaidBreakMinutes;
    }

    public void setUnpaidBreakMinutes(String unpaidBreakMinutes) {
        this.unpaidBreakMinutes = unpaidBreakMinutes;
    }

    public String getWorkerKey() {
        return workerKey;
    }

    public void setWorkerKey(String workerKey) {
        this.workerKey = workerKey;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStaffingWorkerType() {
        return staffingWorkerType;
    }

    public void setStaffingWorkerType(String staffingWorkerType) {
        this.staffingWorkerType = staffingWorkerType;
    }

    public String getAssignmentWorkerType() {
        return assignmentWorkerType;
    }

    public void setAssignmentWorkerType(String assignmentWorkerType) {
        this.assignmentWorkerType = assignmentWorkerType;
    }

    public String getOvertimeMinutes() {
        return overtimeMinutes;
    }

    public void setOvertimeMinutes(String overtimeMinutes) {
        this.overtimeMinutes = overtimeMinutes;
    }

    public String getDoubleOvertimeMinutes() {
        return doubleOvertimeMinutes;
    }

    public void setDoubleOvertimeMinutes(String doubleOvertimeMinutes) {
        this.doubleOvertimeMinutes = doubleOvertimeMinutes;
    }

    public String getRegularMinutes() {
        return regularMinutes;
    }

    public void setRegularMinutes(String regularMinutes) {
        this.regularMinutes = regularMinutes;
    }

    public boolean isHasMeal() {
        return hasMeal;
    }

    public void setHasMeal(boolean hasMeal) {
        this.hasMeal = hasMeal;
    }
}
