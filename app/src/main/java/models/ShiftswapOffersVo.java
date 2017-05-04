package models;

/**
 * Created by Rabbit on 2/19/2017.
 */

public class ShiftswapOffersVo {

    String offerId;
    String offerStatus;
    long statusChangeTimestamp;
    long expiryTimestamp;
    String offerType;
    String reason;
    String reasonText;
    boolean seen;
    boolean pinned;

    private ScheduleWorkerShift shiftOffered;
    private ScheduleWorkerShift shiftRequested;
    private StatusChangeByWorkerVo statechangedByworker;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public long getStatusChangeTimestamp() {
        return statusChangeTimestamp;
    }

    public void setStatusChangeTimestamp(long statusChangeTimestamp) {
        this.statusChangeTimestamp = statusChangeTimestamp;
    }

    public long getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public ScheduleWorkerShift getShiftOffered() {
        return shiftOffered;
    }

    public void setShiftOffered(ScheduleWorkerShift shiftOffered) {
        this.shiftOffered = shiftOffered;
    }

    public ScheduleWorkerShift getShiftRequested() {
        return shiftRequested;
    }

    public void setShiftRequested(ScheduleWorkerShift shiftRequested) {
        this.shiftRequested = shiftRequested;
    }

    public StatusChangeByWorkerVo getStatechangedByworker() {
        return statechangedByworker;
    }

    public void setStatechangedByworker(StatusChangeByWorkerVo statechangedByworker) {
        this.statechangedByworker = statechangedByworker;
    }
}
