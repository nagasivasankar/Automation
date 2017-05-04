package models;

import android.widget.LinearLayout;

import java.io.Serializable;

/**
 * Created by Administrator on 05-Dec-16.
 */
public class Slot implements Serializable {
    int slotSerialNumber;
    LinearLayout view;
    String endTime;
    String startTime;
    boolean isAvailabilityMode;
    int weight = 1;
    SlotType slotType;
    boolean isPTO;
    int dayOfTheWeek;
    int startTimeMins;
    int endTimeMins;
    int actualStartTimeOfSlot;
    int actualEndTimeOfSlot;

    public int getStartTimeMins() {
        return startTimeMins;
    }

    public void setStartTimeMins(int startTimeMins) {
        this.startTimeMins = startTimeMins;
    }

    public int getEndTimeMins() {
        return endTimeMins;
    }

    public void setEndTimeMins(int endTimeMins) {
        this.endTimeMins = endTimeMins;
    }

    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public boolean isPTO() {
        return isPTO;
    }

    public void setIsPTO(boolean isPTO) {
        this.isPTO = isPTO;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public void setSlotType(SlotType slotType) {
        this.slotType = slotType;
    }

    public int getSlotSerialNumber() {
        return slotSerialNumber;
    }

    public void setSlotSerialNumber(int slotSerialNumber) {
        this.slotSerialNumber = slotSerialNumber;
    }

    public LinearLayout getView() {
        return view;
    }

    public void setView(LinearLayout view) {
        this.view = view;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailabilityMode() {
        return isAvailabilityMode;
    }

    public void setAvailabilityMode(boolean availabilityMode) {
        isAvailabilityMode = availabilityMode;
    }

    public int getActualStartTimeOfSlot() {
        return actualStartTimeOfSlot;
    }

    public void setActualStartTimeOfSlot(int actualStartTimeOfSlot) {
        this.actualStartTimeOfSlot = actualStartTimeOfSlot;
    }

    public int getActualEndTimeOfSlot() {
        return actualEndTimeOfSlot;
    }

    public void setActualEndTimeOfSlot(int actualEndTimeOfSlot) {
        this.actualEndTimeOfSlot = actualEndTimeOfSlot;
    }
}
