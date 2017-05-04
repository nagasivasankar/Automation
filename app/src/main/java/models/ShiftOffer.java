package models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 1/4/2017.
 */
public class ShiftOffer implements Serializable {
    String offerId;
    String offerStatus;
    long statusChangeTimestamp;
    long expiryTimestamp;
    String offerType;
    String reason;
    String reasonText;
    String approvals;
    String estimatedPay;
    boolean seen;
    boolean pinned;
    Worker worker;
    Worker statusChangedByWorker;
    StaffingShift staffingShift;
    boolean isShiftSwapOffer;
    ShiftOfferedRequested shiftOffered;
    ShiftOfferedRequested shiftRequested;
    ArrayList<AssociatedWorker> associatedWorkerArrayList;

    public boolean isShiftSwapOffer() {
        return isShiftSwapOffer;
    }

    public void setIsShiftSwapOffer(boolean isShiftSwapOffer) {
        this.isShiftSwapOffer = isShiftSwapOffer;
    }

    public ShiftOfferedRequested getShiftOffered() {
        return shiftOffered;
    }

    public void setShiftOffered(ShiftOfferedRequested shiftOffered) {
        this.shiftOffered = shiftOffered;
    }

    public ShiftOfferedRequested getShiftRequested() {
        return shiftRequested;
    }

    public void setShiftRequested(ShiftOfferedRequested shiftRequested) {
        this.shiftRequested = shiftRequested;
    }

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

    public String getApprovals() {
        return approvals;
    }

    public void setApprovals(String approvals) {
        this.approvals = approvals;
    }

    public String getEstimatedPay() {
        return estimatedPay;
    }

    public void setEstimatedPay(String estimatedPay) {
        this.estimatedPay = estimatedPay;
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

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Worker getStatusChangedByWorker() {
        return statusChangedByWorker;
    }

    public void setStatusChangedByWorker(Worker statusChangedByWorker) {
        this.statusChangedByWorker = statusChangedByWorker;
    }

    public StaffingShift getStaffingShift() {
        return staffingShift;
    }

    public void setStaffingShift(StaffingShift staffingShift) {
        this.staffingShift = staffingShift;
    }

    public ArrayList<AssociatedWorker> getAssociatedWorkerArrayList() {
        return associatedWorkerArrayList;
    }

    public void setAssociatedWorkerArrayList(ArrayList<AssociatedWorker> associatedWorkerArrayList) {
        this.associatedWorkerArrayList = associatedWorkerArrayList;
    }
}
