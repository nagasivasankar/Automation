package models;

import java.io.Serializable;

/**
 * Created by Rabbit on 3/6/2017.
 */

public class WorkerShiftSummary implements Serializable {
    String weeklyScheduleRating;
    String scheduleStatus;
    String totalShift;
    String totalPreferredMinutesScheduled;
    String totalPreferredMinutes;
    String totalConflicts;
    String totalProjectedPay;
    String totalOffers;
    String totalOffersClaimed;
    String totalOffersApproved;
    String totalShiftMinutes;
    String numberOfHours;

    public String getWeeklyScheduleRating() {
        return weeklyScheduleRating;
    }

    public void setWeeklyScheduleRating(String weeklyScheduleRating) {
        this.weeklyScheduleRating = weeklyScheduleRating;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public String getTotalShift() {
        return totalShift;
    }

    public void setTotalShift(String totalShift) {
        this.totalShift = totalShift;
    }

    public String getTotalPreferredMinutesScheduled() {
        return totalPreferredMinutesScheduled;
    }

    public void setTotalPreferredMinutesScheduled(String totalPreferredMinutesScheduled) {
        this.totalPreferredMinutesScheduled = totalPreferredMinutesScheduled;
    }

    public String getTotalPreferredMinutes() {
        return totalPreferredMinutes;
    }

    public void setTotalPreferredMinutes(String totalPreferredMinutes) {
        this.totalPreferredMinutes = totalPreferredMinutes;
    }

    public String getTotalConflicts() {
        return totalConflicts;
    }

    public void setTotalConflicts(String totalConflicts) {
        this.totalConflicts = totalConflicts;
    }

    public String getTotalProjectedPay() {
        return totalProjectedPay;
    }

    public void setTotalProjectedPay(String totalProjectedPay) {
        this.totalProjectedPay = totalProjectedPay;
    }

    public String getTotalOffers() {
        return totalOffers;
    }

    public void setTotalOffers(String totalOffers) {
        this.totalOffers = totalOffers;
    }

    public String getTotalOffersClaimed() {
        return totalOffersClaimed;
    }

    public void setTotalOffersClaimed(String totalOffersClaimed) {
        this.totalOffersClaimed = totalOffersClaimed;
    }

    public String getTotalOffersApproved() {
        return totalOffersApproved;
    }

    public void setTotalOffersApproved(String totalOffersApproved) {
        this.totalOffersApproved = totalOffersApproved;
    }

    public String getTotalShiftMinutes() {
        return totalShiftMinutes;
    }

    public void setTotalShiftMinutes(String totalShiftMinutes) {
        this.totalShiftMinutes = totalShiftMinutes;
    }

    public String getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(String numberOfHours) {
        this.numberOfHours = numberOfHours;
    }
}
