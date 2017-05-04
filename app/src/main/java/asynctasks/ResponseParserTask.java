package asynctasks;

import android.os.AsyncTask;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;

import helpers.Legion_PrefsManager;
import interfaces.ResponseParserListener;
import models.AssociatedWorker;
import models.BusinessKey;
import models.Schedule;
import models.ScheduleWorkerShift;
import models.ShiftOffer;
import models.ShiftOfferedRequested;
import models.Slot;
import models.StaffingShift;
import models.Worker;
import models.WorkerKey;
import models.WorkerShiftSummary;
import utils.LegionUtils;
import utils.Legion_Constants;

public class ResponseParserTask extends AsyncTask<String, Object, Object> implements Legion_Constants {

    private final ResponseParserListener parserListener;
    private final int parserId;
    private final Legion_PrefsManager prefsManager;

    public ResponseParserTask(int parserConstant, Legion_PrefsManager prefsManager, ResponseParserListener parserListener) {
        this.parserId = parserConstant;
        this.parserListener = parserListener;
        this.prefsManager = prefsManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (parserListener != null) {
            parserListener.onResponseParsingStart(parserId);
        }
    }

    @Override
    protected Object doInBackground(String... args) {
        String responseString = args[0];

        if (parserId == ResponseParserConstants.PARSE_WEEKLY_SHIFTS_OF_WORKER_RESPONSE) {
            return doParseWeeklyShiftsOfWorker(responseString);
        } else if (parserId == ResponseParserConstants.NOTIFICATION_SHIFTS_OF_WORKER_RESPONSE) {
            return doParseWeeklyShiftsOfWorker(responseString);
        } else if (parserId == ResponseParserConstants.PARSE_SHIFT_OFFERS) {
            return doParseShiftOffers(responseString);
        } else if (parserId == ResponseParserConstants.PARSE_SHIFT_SWAP_OFFERS) {
            return doParseShiftSwapOffers(responseString);
        } else if (parserId == ResponseParserConstants.PARSE_SCHEDULES) {
            return doParseSchedules(responseString);
        } else if (parserId == ResponseParserConstants.PARSE_SCHEDULE_SUMMARY_DETAILS) {
            return doParseSummaryDetails(responseString);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object resultInfo) {
        super.onPostExecute(resultInfo);
        if (parserListener != null) {
            parserListener.onResponseParsingComplete(parserId, resultInfo);
        }
    }

    private Object doParseShiftSwapOffers(String responseString) {
        try {
            JSONObject mainJsonObject = new JSONObject(responseString);
            String responseStatus = mainJsonObject.optString("responseStatus");
            String errorString = mainJsonObject.optString("errorString");
            String operationStatus = mainJsonObject.optString("operationStatus");

            if (responseStatus.equals("SUCCESS")) {
                JSONArray shiftOfferJsonArray = mainJsonObject.optJSONArray("shiftSwapOffers");
                ArrayList<ShiftOffer> shiftOffersList = new ArrayList<>();
                if (shiftOfferJsonArray != null){
                    int size = shiftOfferJsonArray.length();
                    for (int i = size - 1; i >= 0; --i) {
                        JSONObject shiftOfferJsonObject = shiftOfferJsonArray.getJSONObject(i);
                        ShiftOffer offer = new ShiftOffer();

                        offer.setIsShiftSwapOffer(true);
                        offer.setOfferId(shiftOfferJsonObject.optString("offerId"));
                        offer.setOfferStatus(shiftOfferJsonObject.optString("offerStatus"));
                        offer.setStatusChangeTimestamp(shiftOfferJsonObject.optLong("statusChangeTimestamp"));
                        offer.setExpiryTimestamp(shiftOfferJsonObject.optLong("expiryTimestamp"));
                        offer.setOfferType(shiftOfferJsonObject.optString("offerType"));
                        offer.setReason(shiftOfferJsonObject.optString("reason"));
                        offer.setReasonText(shiftOfferJsonObject.optString("reasonText"));
                        offer.setApprovals(shiftOfferJsonObject.optString("approvals"));
                        offer.setEstimatedPay(shiftOfferJsonObject.optString("estimatedPay"));
                        offer.setSeen(shiftOfferJsonObject.optBoolean("seen"));
                        offer.setPinned(shiftOfferJsonObject.optBoolean("pinned"));

                        JSONObject statusChangedByWorkerJsonObject = shiftOfferJsonObject.optJSONObject("statusChangedByWorker");
                        if (statusChangedByWorkerJsonObject != null) {
                            Worker worker = new Worker();
                            worker.setExternalId(statusChangedByWorkerJsonObject.optString("externalId"));
                            worker.setFirstName(statusChangedByWorkerJsonObject.optString("firstName"));
                            worker.setLastName(statusChangedByWorkerJsonObject.optString("lastName"));
                            worker.setPhoneNumber(statusChangedByWorkerJsonObject.optString("phoneNumber"));
                            worker.setEmail(statusChangedByWorkerJsonObject.optString("email"));
                            worker.setAddress1(statusChangedByWorkerJsonObject.optString("address1"));
                            worker.setAddress2(statusChangedByWorkerJsonObject.optString("address2"));
                            worker.setCity(statusChangedByWorkerJsonObject.optString("city"));
                            worker.setState(statusChangedByWorkerJsonObject.optString("state"));
                            worker.setZip(statusChangedByWorkerJsonObject.optString("zip"));
                            worker.setMemberId(statusChangedByWorkerJsonObject.optString("memberId"));
                            worker.setPictureUrl(statusChangedByWorkerJsonObject.optString("pictureUrl"));
                            worker.setStatus(statusChangedByWorkerJsonObject.optString("status"));
                            worker.setRole(statusChangedByWorkerJsonObject.optString("role"));
                            worker.setStartEngagementDate(statusChangedByWorkerJsonObject.optString("startEngagementDate"));
                            worker.setIsShiftLead(statusChangedByWorkerJsonObject.optBoolean("isShiftLead"));

                            offer.setStatusChangedByWorker(worker);
                        }

                        JSONObject shiftOfferedJsonObject = shiftOfferJsonObject.optJSONObject("shiftOffered");
                        if (shiftOfferedJsonObject != null) {
                            ShiftOfferedRequested shiftOffered = new ShiftOfferedRequested();

                            JSONObject businessJsonObject = shiftOfferedJsonObject.optJSONObject("businessKey");
                            if (businessJsonObject != null) {
                                BusinessKey businessKey = new BusinessKey();

                                businessKey.setAddress(businessJsonObject.optString("address"));
                                businessKey.setBusinessId(businessJsonObject.optString("businessId"));
                                businessKey.setEnterpriseId(businessJsonObject.optString("enterpriseId"));
                                businessKey.setExternalId(businessJsonObject.optString("externalId"));
                                businessKey.setEnterpriseName(businessJsonObject.optString("enterpriseName"));
                                businessKey.setGooglePlacesId(businessJsonObject.optString("googlePlacesId"));
                                businessKey.setName(businessJsonObject.optString("name"));
                                JSONArray photoUrlsJsonArray = businessJsonObject.optJSONArray("photoUrl");
                                if(photoUrlsJsonArray != null) {
                                    int photoUrlsSize = photoUrlsJsonArray.length();
                                    ArrayList<String> photoUrlsList = new ArrayList<>();
                                    for (int photoUrlIndex = 0; photoUrlIndex < photoUrlsSize; ++photoUrlIndex) {
                                        photoUrlsList.add(photoUrlsJsonArray.getString(photoUrlIndex));
                                    }
                                    businessKey.setPhotoUrls(photoUrlsList);
                                }
                                shiftOffered.setBusinessKey(businessKey);
                            }

                            JSONObject workerKeyJsonObject = shiftOfferedJsonObject.optJSONObject("workerKey");
                            if (workerKeyJsonObject != null) {

                                Worker worker = new Worker();
                                worker.setExternalId(workerKeyJsonObject.optString("externalId"));
                                worker.setFirstName(workerKeyJsonObject.optString("firstName"));
                                worker.setLastName(workerKeyJsonObject.optString("lastName"));
                                worker.setPhoneNumber(workerKeyJsonObject.optString("phoneNumber"));
                                worker.setEmail(workerKeyJsonObject.optString("email"));
                                worker.setAddress1(workerKeyJsonObject.optString("address1"));
                                worker.setAddress2(workerKeyJsonObject.optString("address2"));
                                worker.setCity(workerKeyJsonObject.optString("city"));
                                worker.setState(workerKeyJsonObject.optString("state"));
                                worker.setZip(workerKeyJsonObject.optString("zip"));
                                worker.setMemberId(workerKeyJsonObject.optString("memberId"));
                                worker.setPictureUrl(workerKeyJsonObject.optString("pictureUrl"));
                                worker.setStatus(workerKeyJsonObject.optString("status"));
                                worker.setRole(workerKeyJsonObject.optString("role"));
                                worker.setStartEngagementDate(workerKeyJsonObject.optString("startEngagementDate"));
                                worker.setIsShiftLead(workerKeyJsonObject.optBoolean("isShiftLead"));

                                shiftOffered.setWorkerKey(worker);
                            }

                            JSONArray associatedWorkersArray = shiftOfferedJsonObject.optJSONArray("associatedWorkers");
                            if (associatedWorkersArray != null && associatedWorkersArray.length() >= 1) {
                                ArrayList<AssociatedWorker> associatedWorkerArrayList = new ArrayList<>();
                                int associatedWorkersArraySize = associatedWorkersArray.length();
                                for (int j = 0; j < associatedWorkersArraySize; ++j) {
                                    JSONObject associatedWorkerJsonObj = associatedWorkersArray.getJSONObject(j);
                                    AssociatedWorker associatedWorker = new AssociatedWorker();
                                    associatedWorker.setExternalId(associatedWorkerJsonObj.optString("externalId"));
                                    associatedWorker.setAddress1(associatedWorkerJsonObj.optString("address1"));
                                    associatedWorker.setAddress2(associatedWorkerJsonObj.optString("address2"));
                                    associatedWorker.setCity(associatedWorkerJsonObj.optString("city"));
                                    associatedWorker.setEmail(associatedWorkerJsonObj.optString("email"));
                                    associatedWorker.setFirstName(associatedWorkerJsonObj.optString("firstName"));
                                    associatedWorker.setLastName(associatedWorkerJsonObj.optString("lastName"));
                                    associatedWorker.setMemberId(associatedWorkerJsonObj.optString("memberId"));
                                    associatedWorker.setPhoneNumber(associatedWorkerJsonObj.optString("phoneNumber"));
                                    associatedWorker.setPictureUrl(associatedWorkerJsonObj.optString("pictureUrl"));
                                    associatedWorker.setRole(associatedWorkerJsonObj.optString("role"));
                                    associatedWorker.setZip(associatedWorkerJsonObj.optString("zip"));
                                    associatedWorker.setState(associatedWorkerJsonObj.optString("state"));
                                    associatedWorker.setStartEngagementDate(associatedWorkerJsonObj.optString("startEngagementDate"));
                                    associatedWorker.setStatus(associatedWorkerJsonObj.optString("status"));
                                    associatedWorker.setShiftLead(Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead")));
                                    String nickName = associatedWorkerJsonObj.optString("nickName").trim();
                                    if (!nickName.equalsIgnoreCase("null") || nickName != null || nickName.length() > 0) {
                                        associatedWorker.setNickName(associatedWorkerJsonObj.optString("nickName"));
                                    } else {
                                        associatedWorker.setNickName("");
                                    }
                                    if (Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead"))) {
                                        associatedWorkerArrayList.add(0, associatedWorker);
                                    } else {
                                        associatedWorkerArrayList.add(associatedWorker);
                                    }
                                }
                                shiftOffered.setAssociatedWorkerArrayList(associatedWorkerArrayList);
                            }

                            shiftOffered.setDayOfTheWeek(shiftOfferedJsonObject.optString("dayOfTheWeek"));
                            shiftOffered.setWeekStartDayOfTheYear(shiftOfferedJsonObject.optString("weekStartDayOfTheYear"));
                            shiftOffered.setYear(shiftOfferedJsonObject.optString("year"));
                            shiftOffered.setNotes(shiftOfferedJsonObject.optString("notes"));
                            shiftOffered.setName(shiftOfferedJsonObject.optString("name"));
                            shiftOffered.setRole(shiftOfferedJsonObject.optString("role"));
                            shiftOffered.setOfferStatus(shiftOfferedJsonObject.optString("offerStatus"));
                            shiftOffered.setType(shiftOfferedJsonObject.optString("type"));
                            shiftOffered.setRegularMinutes(shiftOfferedJsonObject.optString("regularMinutes"));
                            shiftOffered.setCost(shiftOfferedJsonObject.optString("cost"));
                            shiftOffered.setWeekDoubleOvertimeMinutes(shiftOfferedJsonObject.optString("weekDoubleOvertimeMinutes"));
                            shiftOffered.setWeekOvertimeMinutes(shiftOfferedJsonObject.optString("weekOvertimeMinutes"));
                            shiftOffered.setDoubleOvertimeMinutes(shiftOfferedJsonObject.optString("doubleOvertimeMinutes"));
                            shiftOffered.setUnpaidBreakMinutes(shiftOfferedJsonObject.optString("unpaidBreakMinutes"));
                            shiftOffered.setHasMeal(shiftOfferedJsonObject.optString("hasMeal"));
                            shiftOffered.setAvailability(shiftOfferedJsonObject.optString("availability"));
                            shiftOffered.setEstimatedPay(shiftOfferedJsonObject.optString("estimatedPay"));
                            shiftOffered.setClockOutTime(shiftOfferedJsonObject.optString("clockOutTime"));
                            shiftOffered.setClockInTime(shiftOfferedJsonObject.optString("clockInTime"));
                            shiftOffered.setStatus(shiftOfferedJsonObject.optString("status"));
                            shiftOffered.setShiftId(shiftOfferedJsonObject.optString("shiftId"));
                            shiftOffered.setShiftEndDate(shiftOfferedJsonObject.optString("shiftEndDate"));
                            shiftOffered.setShiftStartDate(shiftOfferedJsonObject.optString("shiftStartDate"));
                            shiftOffered.setEndMin(shiftOfferedJsonObject.optString("endMin"));
                            shiftOffered.setStartMin(shiftOfferedJsonObject.optString("startMin"));

                            offer.setShiftOffered(shiftOffered);
                        }

                        JSONObject shiftRequestedJsonObject = shiftOfferJsonObject.optJSONObject("shiftRequested");
                        if (shiftRequestedJsonObject != null) {
                            ShiftOfferedRequested shiftRequested = new ShiftOfferedRequested();

                            JSONObject businessJsonObject = shiftRequestedJsonObject.optJSONObject("businessKey");
                            if (businessJsonObject != null) {
                                BusinessKey businessKey = new BusinessKey();

                                businessKey.setAddress(businessJsonObject.optString("address"));
                                businessKey.setBusinessId(businessJsonObject.optString("businessId"));
                                businessKey.setEnterpriseId(businessJsonObject.optString("enterpriseId"));
                                businessKey.setExternalId(businessJsonObject.optString("externalId"));
                                businessKey.setEnterpriseName(businessJsonObject.optString("enterpriseName"));
                                businessKey.setGooglePlacesId(businessJsonObject.optString("googlePlacesId"));
                                businessKey.setName(businessJsonObject.optString("name"));
                                JSONArray photoUrlsJsonArray = businessJsonObject.optJSONArray("photoUrl");
                                if(photoUrlsJsonArray != null) {
                                    int photoUrlsSize = photoUrlsJsonArray.length();
                                    ArrayList<String> photoUrlsList = new ArrayList<>();
                                    for (int photoUrlIndex = 0; photoUrlIndex < photoUrlsSize; ++photoUrlIndex) {
                                        photoUrlsList.add(photoUrlsJsonArray.getString(photoUrlIndex));
                                    }
                                    businessKey.setPhotoUrls(photoUrlsList);
                                }
                                shiftRequested.setBusinessKey(businessKey);
                            }

                            JSONObject workerKeyJsonObject = shiftRequestedJsonObject.optJSONObject("workerKey");
                            if (workerKeyJsonObject != null) {

                                Worker worker = new Worker();
                                worker.setExternalId(workerKeyJsonObject.optString("externalId"));
                                worker.setFirstName(workerKeyJsonObject.optString("firstName"));
                                worker.setLastName(workerKeyJsonObject.optString("lastName"));
                                worker.setPhoneNumber(workerKeyJsonObject.optString("phoneNumber"));
                                worker.setEmail(workerKeyJsonObject.optString("email"));
                                worker.setAddress1(workerKeyJsonObject.optString("address1"));
                                worker.setAddress2(workerKeyJsonObject.optString("address2"));
                                worker.setCity(workerKeyJsonObject.optString("city"));
                                worker.setState(workerKeyJsonObject.optString("state"));
                                worker.setZip(workerKeyJsonObject.optString("zip"));
                                worker.setMemberId(workerKeyJsonObject.optString("memberId"));
                                worker.setPictureUrl(workerKeyJsonObject.optString("pictureUrl"));
                                worker.setStatus(workerKeyJsonObject.optString("status"));
                                worker.setRole(workerKeyJsonObject.optString("role"));
                                worker.setStartEngagementDate(workerKeyJsonObject.optString("startEngagementDate"));
                                worker.setIsShiftLead(workerKeyJsonObject.optBoolean("isShiftLead"));

                                shiftRequested.setWorkerKey(worker);
                            }

                            JSONArray associatedWorkersArray = shiftRequestedJsonObject.optJSONArray("associatedWorkers");
                            if (associatedWorkersArray != null && associatedWorkersArray.length() >= 1) {
                                ArrayList<AssociatedWorker> associatedWorkerArrayList = new ArrayList<>();
                                int associatedWorkersArraySize = associatedWorkersArray.length();
                                for (int j = 0; j < associatedWorkersArraySize; ++j) {
                                    JSONObject associatedWorkerJsonObj = associatedWorkersArray.getJSONObject(j);
                                    AssociatedWorker associatedWorker = new AssociatedWorker();
                                    associatedWorker.setExternalId(associatedWorkerJsonObj.optString("externalId"));
                                    associatedWorker.setAddress1(associatedWorkerJsonObj.optString("address1"));
                                    associatedWorker.setAddress2(associatedWorkerJsonObj.optString("address2"));
                                    associatedWorker.setCity(associatedWorkerJsonObj.optString("city"));
                                    associatedWorker.setEmail(associatedWorkerJsonObj.optString("email"));
                                    associatedWorker.setFirstName(associatedWorkerJsonObj.optString("firstName"));
                                    associatedWorker.setLastName(associatedWorkerJsonObj.optString("lastName"));
                                    associatedWorker.setMemberId(associatedWorkerJsonObj.optString("memberId"));
                                    associatedWorker.setPhoneNumber(associatedWorkerJsonObj.optString("phoneNumber"));
                                    associatedWorker.setPictureUrl(associatedWorkerJsonObj.optString("pictureUrl"));
                                    associatedWorker.setRole(associatedWorkerJsonObj.optString("role"));
                                    associatedWorker.setZip(associatedWorkerJsonObj.optString("zip"));
                                    associatedWorker.setState(associatedWorkerJsonObj.optString("state"));
                                    associatedWorker.setStartEngagementDate(associatedWorkerJsonObj.optString("startEngagementDate"));
                                    associatedWorker.setStatus(associatedWorkerJsonObj.optString("status"));
                                    associatedWorker.setShiftLead(Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead")));
                                    if (Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead"))) {
                                        associatedWorkerArrayList.add(0, associatedWorker);
                                    } else {
                                        associatedWorkerArrayList.add(associatedWorker);
                                    }
                                }
                                shiftRequested.setAssociatedWorkerArrayList(associatedWorkerArrayList);
                            }

                            shiftRequested.setDayOfTheWeek(shiftRequestedJsonObject.optString("dayOfTheWeek"));
                            shiftRequested.setWeekStartDayOfTheYear(shiftRequestedJsonObject.optString("weekStartDayOfTheYear"));
                            shiftRequested.setYear(shiftRequestedJsonObject.optString("year"));
                            shiftRequested.setNotes(shiftRequestedJsonObject.optString("notes"));
                            shiftRequested.setName(shiftRequestedJsonObject.optString("name"));
                            shiftRequested.setRole(shiftRequestedJsonObject.optString("role"));
                            shiftRequested.setOfferStatus(shiftRequestedJsonObject.optString("offerStatus"));
                            shiftRequested.setType(shiftRequestedJsonObject.optString("type"));
                            shiftRequested.setRegularMinutes(shiftRequestedJsonObject.optString("regularMinutes"));
                            shiftRequested.setCost(shiftRequestedJsonObject.optString("cost"));
                            shiftRequested.setWeekDoubleOvertimeMinutes(shiftRequestedJsonObject.optString("weekDoubleOvertimeMinutes"));
                            shiftRequested.setWeekOvertimeMinutes(shiftRequestedJsonObject.optString("weekOvertimeMinutes"));
                            shiftRequested.setDoubleOvertimeMinutes(shiftRequestedJsonObject.optString("doubleOvertimeMinutes"));
                            shiftRequested.setUnpaidBreakMinutes(shiftRequestedJsonObject.optString("unpaidBreakMinutes"));
                            shiftRequested.setHasMeal(shiftRequestedJsonObject.optString("hasMeal"));
                            shiftRequested.setAvailability(shiftRequestedJsonObject.optString("availability"));
                            shiftRequested.setEstimatedPay(shiftRequestedJsonObject.optString("estimatedPay"));
                            shiftRequested.setClockOutTime(shiftRequestedJsonObject.optString("clockOutTime"));
                            shiftRequested.setClockInTime(shiftRequestedJsonObject.optString("clockInTime"));
                            shiftRequested.setStatus(shiftRequestedJsonObject.optString("status"));
                            shiftRequested.setShiftId(shiftRequestedJsonObject.optString("shiftId"));
                            shiftRequested.setShiftEndDate(shiftRequestedJsonObject.optString("shiftEndDate"));
                            shiftRequested.setShiftStartDate(shiftRequestedJsonObject.optString("shiftStartDate"));
                            shiftRequested.setEndMin(shiftRequestedJsonObject.optString("endMin"));
                            shiftRequested.setStartMin(shiftRequestedJsonObject.optString("startMin"));

                            offer.setShiftRequested(shiftRequested);
                        }
                        shiftOffersList.add(0, offer);
                    }
                }
                return shiftOffersList;
            }else{
                return errorString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    private Object doParseShiftOffers(String responseString) {
        try {
            JSONObject mainJsonObject = new JSONObject(responseString);
            String responseStatus = mainJsonObject.optString("responseStatus");
            String errorString = mainJsonObject.optString("errorString");
            String operationStatus = mainJsonObject.optString("operationStatus");

            if (responseStatus.equals("SUCCESS")) {
                ArrayList<ShiftOffer> shiftOffersList = new ArrayList<>();
                JSONArray shiftOfferJsonArray = mainJsonObject.optJSONArray("shiftOffer");
                if (shiftOfferJsonArray != null) {
                    int size = shiftOfferJsonArray.length();
                    for (int i = 0; i < size; ++i) {
                        JSONObject shiftOfferJsonObject = shiftOfferJsonArray.getJSONObject(i);
                        ShiftOffer shiftOffer = new ShiftOffer();

                        shiftOffer.setIsShiftSwapOffer(false);
                        shiftOffer.setOfferId(shiftOfferJsonObject.optString("offerId"));
                        shiftOffer.setOfferStatus(shiftOfferJsonObject.optString("offerStatus"));
                        shiftOffer.setStatusChangeTimestamp(shiftOfferJsonObject.optLong("statusChangeTimestamp"));
                        shiftOffer.setExpiryTimestamp(shiftOfferJsonObject.optLong("expiryTimestamp"));
                        shiftOffer.setOfferType(shiftOfferJsonObject.optString("offerType"));
                        shiftOffer.setReason(shiftOfferJsonObject.optString("reason"));
                        shiftOffer.setReasonText(shiftOfferJsonObject.optString("reasonText"));
                        shiftOffer.setApprovals(shiftOfferJsonObject.optString("approvals"));
                        shiftOffer.setEstimatedPay(shiftOfferJsonObject.optString("estimatedPay"));
                        shiftOffer.setSeen(shiftOfferJsonObject.optBoolean("seen"));
                        shiftOffer.setPinned(shiftOfferJsonObject.optBoolean("pinned"));

                        JSONObject workerJsonObject = shiftOfferJsonObject.optJSONObject("worker");
                        if (workerJsonObject != null) {
                            Worker worker = new Worker();
                            worker.setExternalId(workerJsonObject.optString("externalId"));
                            worker.setFirstName(workerJsonObject.optString("firstName"));
                            worker.setLastName(workerJsonObject.optString("lastName"));
                            worker.setPhoneNumber(workerJsonObject.optString("phoneNumber"));
                            worker.setEmail(workerJsonObject.optString("email"));
                            worker.setAddress1(workerJsonObject.optString("address1"));
                            worker.setAddress2(workerJsonObject.optString("address2"));
                            worker.setCity(workerJsonObject.optString("city"));
                            worker.setState(workerJsonObject.optString("state"));
                            worker.setZip(workerJsonObject.optString("zip"));
                            worker.setMemberId(workerJsonObject.optString("memberId"));
                            worker.setPictureUrl(workerJsonObject.optString("pictureUrl"));
                            worker.setStatus(workerJsonObject.optString("status"));
                            worker.setRole(workerJsonObject.optString("role"));
                            worker.setStartEngagementDate(workerJsonObject.optString("startEngagementDate"));
                            worker.setIsShiftLead(workerJsonObject.optBoolean("isShiftLead"));

                            shiftOffer.setWorker(worker);
                        }

                        JSONObject statusChangedByWorkerJsonObject = shiftOfferJsonObject.optJSONObject("statusChangedByWorker");
                        if (statusChangedByWorkerJsonObject != null) {
                            Worker worker = new Worker();
                            worker.setExternalId(statusChangedByWorkerJsonObject.optString("externalId"));
                            worker.setFirstName(statusChangedByWorkerJsonObject.optString("firstName"));
                            worker.setLastName(statusChangedByWorkerJsonObject.optString("lastName"));
                            worker.setPhoneNumber(statusChangedByWorkerJsonObject.optString("phoneNumber"));
                            worker.setEmail(statusChangedByWorkerJsonObject.optString("email"));
                            worker.setAddress1(statusChangedByWorkerJsonObject.optString("address1"));
                            worker.setAddress2(statusChangedByWorkerJsonObject.optString("address2"));
                            worker.setCity(statusChangedByWorkerJsonObject.optString("city"));
                            worker.setState(statusChangedByWorkerJsonObject.optString("state"));
                            worker.setZip(statusChangedByWorkerJsonObject.optString("zip"));
                            worker.setMemberId(statusChangedByWorkerJsonObject.optString("memberId"));
                            worker.setPictureUrl(statusChangedByWorkerJsonObject.optString("pictureUrl"));
                            worker.setStatus(statusChangedByWorkerJsonObject.optString("status"));
                            worker.setRole(statusChangedByWorkerJsonObject.optString("role"));
                            worker.setStartEngagementDate(statusChangedByWorkerJsonObject.optString("startEngagementDate"));
                            worker.setIsShiftLead(statusChangedByWorkerJsonObject.optBoolean("isShiftLead"));

                            shiftOffer.setStatusChangedByWorker(worker);
                        }

                        JSONArray associatedWorkersJsonArray = shiftOfferJsonObject.optJSONArray("associatedWorkers");
                        if (associatedWorkersJsonArray != null && associatedWorkersJsonArray.length() >= 1) {
                            ArrayList<AssociatedWorker> associatedWorkerArrayList = new ArrayList<>();
                            int associatedWorkersArraySize = associatedWorkersJsonArray.length();
                            for (int j = 0; j < associatedWorkersArraySize; ++j) {
                                JSONObject associatedWorkerJsonObj = associatedWorkersJsonArray.getJSONObject(j);
                                AssociatedWorker associatedWorker = new AssociatedWorker();
                                associatedWorker.setExternalId(associatedWorkerJsonObj.optString("externalId"));
                                associatedWorker.setAddress1(associatedWorkerJsonObj.optString("address1"));
                                associatedWorker.setAddress2(associatedWorkerJsonObj.optString("address2"));
                                associatedWorker.setCity(associatedWorkerJsonObj.optString("city"));
                                associatedWorker.setEmail(associatedWorkerJsonObj.optString("email"));
                                associatedWorker.setFirstName(associatedWorkerJsonObj.optString("firstName"));
                                associatedWorker.setLastName(associatedWorkerJsonObj.optString("lastName"));
                                associatedWorker.setMemberId(associatedWorkerJsonObj.optString("memberId"));
                                associatedWorker.setPhoneNumber(associatedWorkerJsonObj.optString("phoneNumber"));
                                String nickName = associatedWorkerJsonObj.optString("nickName").trim();
                                if (!nickName.equalsIgnoreCase("null") || nickName != null || nickName.length() > 0) {
                                    associatedWorker.setNickName(associatedWorkerJsonObj.optString("nickName"));
                                } else {
                                    associatedWorker.setNickName("");
                                }
                                associatedWorker.setPictureUrl(associatedWorkerJsonObj.optString("pictureUrl"));
                                associatedWorker.setRole(associatedWorkerJsonObj.optString("role"));
                                associatedWorker.setZip(associatedWorkerJsonObj.optString("zip"));
                                associatedWorker.setState(associatedWorkerJsonObj.optString("state"));
                                associatedWorker.setStartEngagementDate(associatedWorkerJsonObj.optString("startEngagementDate"));
                                associatedWorker.setStatus(associatedWorkerJsonObj.optString("status"));
                                associatedWorker.setShiftLead(Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead")));

                                if (Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead"))) {
                                    associatedWorkerArrayList.add(0, associatedWorker);
                                } else {
                                    associatedWorkerArrayList.add(associatedWorker);
                                }
                            }
                            shiftOffer.setAssociatedWorkerArrayList(associatedWorkerArrayList);
                        }

                        JSONObject staffingJsonObject = shiftOfferJsonObject.optJSONObject("staffingShift");
                        if (staffingJsonObject != null) {
                            StaffingShift staffingShift = new StaffingShift();

                            staffingShift.setRole(staffingJsonObject.optString("role"));
                            staffingShift.setName(staffingJsonObject.optString("name"));
                            staffingShift.setRegularMinutes(staffingJsonObject.optString("regularMinutes"));
                            staffingShift.setDoubleOvertimeMinutes(staffingJsonObject.optString("doubleOvertimeMinutes"));
                            staffingShift.setOvertimeMinutes(staffingJsonObject.optString("overtimeMinutes"));
                            staffingShift.setAssignmentWorkerType(staffingJsonObject.optString("assignmentWorkerType"));
                            staffingShift.setStaffingWorkerType(staffingJsonObject.optString("staffingWorkerType"));
                            staffingShift.setAvailability(staffingJsonObject.optString("availability"));
                            staffingShift.setWorkerKey(staffingJsonObject.optString("workerKey"));
                            staffingShift.setUnpaidBreakMinutes(staffingJsonObject.optString("unpaidBreakMinutes"));
                            staffingShift.setShiftOrder(staffingJsonObject.optString("shiftOrder"));
                            staffingShift.setShiftLength(staffingJsonObject.optString("shiftLength"));
                            staffingShift.setSlotOffset(staffingJsonObject.optString("slotOffset"));
                            staffingShift.setStatus(staffingJsonObject.optString("status"));
                            staffingShift.setShiftId(staffingJsonObject.optString("shiftId"));
                            staffingShift.setShiftEndDate(staffingJsonObject.optString("shiftEndDate"));
                            staffingShift.setShiftStartDate(staffingJsonObject.optString("shiftStartDate"));
                            staffingShift.setEndMin(staffingJsonObject.optString("endMin"));
                            staffingShift.setStartMin(staffingJsonObject.optString("startMin"));
                            staffingShift.setDayOfTheWeek(staffingJsonObject.optString("dayOfTheWeek"));
                            staffingShift.setWeekStartDayOfTheYear(staffingJsonObject.optString("weekStartDayOfTheYear"));
                            staffingShift.setYear(staffingJsonObject.optString("year"));
                            staffingShift.setNotes(staffingJsonObject.optString("notes"));
                            staffingShift.setHasMeal(staffingJsonObject.optBoolean("hasMeal"));
                            JSONObject businessJsonObject = staffingJsonObject.optJSONObject("businessKey");
                            if (businessJsonObject != null) {
                                BusinessKey businessKey = new BusinessKey();

                                String externalId = businessJsonObject.optString("externalId");
                                String enterpriseName = businessJsonObject.optString("enterpriseName");
                                String enterpriseId = businessJsonObject.optString("enterpriseId");
                                String name = businessJsonObject.optString("name");
                                String address = businessJsonObject.optString("address");
                                String businessId = businessJsonObject.optString("businessId");
                                String googlePlacesId = businessJsonObject.optString("googlePlacesId");
                                JSONArray photoUrlsJsonArray = businessJsonObject.optJSONArray("photoUrl");
                                if(photoUrlsJsonArray != null) {
                                    int photoUrlsSize = photoUrlsJsonArray.length();
                                    ArrayList<String> photoUrlsList = new ArrayList<>();
                                    for (int photoUrlIndex = 0; photoUrlIndex < photoUrlsSize; ++photoUrlIndex) {
                                        photoUrlsList.add(photoUrlsJsonArray.getString(photoUrlIndex));
                                    }
                                    businessKey.setPhotoUrls(photoUrlsList);
                                }
                                businessKey.setAddress(address);
                                businessKey.setBusinessId(businessId);
                                businessKey.setEnterpriseId(enterpriseId);
                                businessKey.setExternalId(externalId);
                                businessKey.setEnterpriseName(enterpriseName);
                                businessKey.setGooglePlacesId(googlePlacesId);
                                businessKey.setName(name);

                                staffingShift.setBusinessKey(businessKey);
                            }
                            shiftOffer.setStaffingShift(staffingShift);
                        }
                        shiftOffersList.add(shiftOffer);
                    }
                }
                return shiftOffersList;
            }else{
                return errorString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    private Object doParseWeeklyShiftsOfWorker(String response) {
        try {
            JSONObject responseObject = new JSONObject(response.toString());
            String responseStatus = responseObject.optString("responseStatus");
            String errorString = responseObject.optString("errorString");
            if (responseStatus.equalsIgnoreCase("SUCCESS")) {
                ArrayList<ScheduleWorkerShift> shiftsList = new ArrayList<>();
                JSONArray workerShiftsArray = responseObject.optJSONArray("workerShifts");
                int size = workerShiftsArray.length();
                for (int i = 0; i < size; ++i) {
                    JSONObject workerShiftJsonObject = workerShiftsArray.optJSONObject(i);

                    ScheduleWorkerShift scheduleWorkerShift = new ScheduleWorkerShift();
                    String year = (workerShiftJsonObject.optString("year"));
                    String weekStartDayOfTheYear = workerShiftJsonObject.optString("weekStartDayOfTheYear");
                    String role1 = workerShiftJsonObject.optString("role");
                    String name1 = workerShiftJsonObject.optString("name");
                    String notes = workerShiftJsonObject.optString("notes");
                    String dayOfTheWeek = workerShiftJsonObject.optString("dayOfTheWeek");
                    String startMinString = workerShiftJsonObject.optString("startMin");
                    String endMinString = workerShiftJsonObject.optString("endMin");
                    String offerStatus = workerShiftJsonObject.optString("offerStatus");
                    int shiftDuration = Integer.parseInt(endMinString) - Integer.parseInt(startMinString);
                    String shiftStartDate2 = workerShiftJsonObject.optString("shiftStartDate");
                    String shiftEndDate = workerShiftJsonObject.optString("shiftEndDate");
                    String shiftId = workerShiftJsonObject.optString("shiftId");

                    String status1 = workerShiftJsonObject.optString("status");
                    String clockInTime = workerShiftJsonObject.optString("clockInTime");
                    String clockOutTime = workerShiftJsonObject.optString("clockOutTime");
                    String estimatedPay = workerShiftJsonObject.optString("estimatedPay");
                    String availability = workerShiftJsonObject.optString("availability");
                    String hasMeal = workerShiftJsonObject.optString("hasMeal");
                    String unpaidBreakMinutes = workerShiftJsonObject.optString("unpaidBreakMinutes");
                    String doubleOvertimeMinutes = workerShiftJsonObject.optString("doubleOvertimeMinutes");
                    String weekOvertimeMinutes = workerShiftJsonObject.optString("weekOvertimeMinutes");
                    String weekDoubleOvertimeMinutes = workerShiftJsonObject.optString("weekDoubleOvertimeMinutes");
                    String cost = workerShiftJsonObject.optString("cost");
                    String type = workerShiftJsonObject.optString("type");
                    boolean allowShiftSwap = workerShiftJsonObject.optBoolean("allowShiftSwap");
                    String allowShiftSwapUntil = workerShiftJsonObject.optString("allowShiftSwapUntil");
                    String regularMinutes = workerShiftJsonObject.optString("regularMinutes");
                    scheduleWorkerShift.setAvailability(availability);
                    scheduleWorkerShift.setClockInTime(clockInTime);
                    scheduleWorkerShift.setClockOutTime(clockOutTime);
                    scheduleWorkerShift.setCost(cost);
                    scheduleWorkerShift.setDayOfTheWeek(dayOfTheWeek);
                    scheduleWorkerShift.setDoubleOvertimeMinutes(doubleOvertimeMinutes);
                    scheduleWorkerShift.setEndMin(endMinString);
                    scheduleWorkerShift.setEstimatedPay(estimatedPay);
                    scheduleWorkerShift.setHasMeal(hasMeal);
                    scheduleWorkerShift.setStatus(status1);
                    scheduleWorkerShift.setOfferStatus(offerStatus);
                    scheduleWorkerShift.setRegularMinutes(regularMinutes);
                    scheduleWorkerShift.setWeekStartDayOfTheYear(weekStartDayOfTheYear);
                    scheduleWorkerShift.setWeekDoubleOvertimeMinutes(weekDoubleOvertimeMinutes);
                    scheduleWorkerShift.setWeekOvertimeMinutes(weekOvertimeMinutes);
                    scheduleWorkerShift.setUnpaidBreakMinutes(unpaidBreakMinutes);
                    scheduleWorkerShift.setShiftEndDate(shiftEndDate);
                    scheduleWorkerShift.setShiftStartDate(shiftStartDate2);
                    scheduleWorkerShift.setShiftId(shiftId);
                    scheduleWorkerShift.setYear(year);
                    scheduleWorkerShift.setRole(role1);
                    scheduleWorkerShift.setName(name1);
                    scheduleWorkerShift.setNotes(notes);
                    scheduleWorkerShift.setStartMin(startMinString);
                    scheduleWorkerShift.setType(type);
                    scheduleWorkerShift.setAllowShiftSwap(allowShiftSwap);
                    scheduleWorkerShift.setAllowShiftSwapUntil(allowShiftSwapUntil);

                    JSONArray associatedWorkersArray = workerShiftJsonObject.optJSONArray("associatedWorkers");
                    if (associatedWorkersArray != null && associatedWorkersArray.length() > 0) {
                        ArrayList<AssociatedWorker> associatedWorkerArrayList = new ArrayList<>();
                        int associatedWorkersArraySize = associatedWorkersArray.length();
                        for (int j = 0; j < associatedWorkersArraySize; ++j) {
                            JSONObject associatedWorkerJsonObj = associatedWorkersArray.getJSONObject(j);
                            AssociatedWorker associatedWorker = new AssociatedWorker();
                            associatedWorker.setExternalId(associatedWorkerJsonObj.optString("externalId"));
                            associatedWorker.setAddress1(associatedWorkerJsonObj.optString("address1"));
                            associatedWorker.setAddress2(associatedWorkerJsonObj.optString("address2"));
                            associatedWorker.setCity(associatedWorkerJsonObj.optString("city"));
                            associatedWorker.setEmail(associatedWorkerJsonObj.optString("email"));
                            associatedWorker.setFirstName(associatedWorkerJsonObj.optString("firstName"));
                            associatedWorker.setLastName(associatedWorkerJsonObj.optString("lastName"));
                            String nickName = associatedWorkerJsonObj.optString("nickName").trim();
                            if (!nickName.equalsIgnoreCase("null") || nickName != null || nickName.length() > 0) {
                                associatedWorker.setNickName(associatedWorkerJsonObj.optString("nickName"));
                            } else {
                                associatedWorker.setNickName("");
                            }
                            associatedWorker.setMemberId(associatedWorkerJsonObj.optString("memberId"));
                            associatedWorker.setPhoneNumber(associatedWorkerJsonObj.optString("phoneNumber"));
                            associatedWorker.setPictureUrl(associatedWorkerJsonObj.optString("pictureUrl"));
                            associatedWorker.setRole(associatedWorkerJsonObj.optString("role"));
                            associatedWorker.setZip(associatedWorkerJsonObj.optString("zip"));
                            associatedWorker.setState(associatedWorkerJsonObj.optString("state"));
                            associatedWorker.setStartEngagementDate(associatedWorkerJsonObj.optString("startEngagementDate"));
                            associatedWorker.setStatus(associatedWorkerJsonObj.optString("status"));
                            associatedWorker.setShiftLead(Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead")));
                            if (Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead"))) {
                                associatedWorkerArrayList.add(0, associatedWorker);
                            } else {
                                associatedWorkerArrayList.add(associatedWorker);
                            }
                        }
                        scheduleWorkerShift.setAssociatedWorkerArrayList(associatedWorkerArrayList);
                    }

                    JSONObject workerJsonObject = workerShiftJsonObject.optJSONObject("workerKey");
                    if (workerJsonObject != null) {
                        WorkerKey workerKey = new WorkerKey();
                        String externalId = workerJsonObject.optString("externalId");
                        String firstName = workerJsonObject.optString("firstName");
                        String lastName = workerJsonObject.optString("lastName");
                        String email = workerJsonObject.optString("email");
                        String phoneNumber = workerJsonObject.optString("phoneNumber");
                        String address1 = workerJsonObject.optString("address1");
                        String address2 = workerJsonObject.optString("address2");
                        String city = workerJsonObject.optString("city");
                        String state = workerJsonObject.optString("state");
                        String zip = workerJsonObject.optString("zip");
                        String memberId = workerJsonObject.optString("memberId");
                        String pictureUrl = workerJsonObject.optString("pictureUrl");
                        String status = workerJsonObject.optString("status");
                        String role = workerJsonObject.optString("role");
                        String startEngagementDate = workerJsonObject.optString("startEngagementDate");
                        workerKey.setExternalId(externalId);
                        workerKey.setAddress1(address1);
                        workerKey.setAddress2(address2);
                        workerKey.setCity(city);
                        workerKey.setEmail(email);
                        workerKey.setFirstName(firstName);
                        workerKey.setLastName(lastName);
                        workerKey.setMemberId(memberId);
                        workerKey.setPhoneNumber(phoneNumber);
                        workerKey.setPictureUrl(pictureUrl);
                        workerKey.setRole(role);
                        workerKey.setZip(zip);
                        workerKey.setState(state);
                        workerKey.setStartEngagementDate(startEngagementDate);
                        workerKey.setStatus(status);
                        scheduleWorkerShift.setWorkerKey(workerKey);
                    }

                    JSONObject businessJsonObject = workerShiftJsonObject.optJSONObject("businessKey");
                    if (businessJsonObject != null) {
                        BusinessKey businessKey = new BusinessKey();

                        String externalId = businessJsonObject.optString("externalId");
                        String enterpriseName = businessJsonObject.optString("enterpriseName");
                        String enterpriseId = businessJsonObject.optString("enterpriseId");
                        String name = businessJsonObject.optString("name");
                        String address = businessJsonObject.optString("address");
                        String businessId = businessJsonObject.optString("businessId");
                        String googlePlacesId = businessJsonObject.optString("googlePlacesId");
                        JSONArray photoUrlsJsonArray = businessJsonObject.optJSONArray("photoUrl");
                        if(photoUrlsJsonArray != null) {
                            int photoUrlsSize = photoUrlsJsonArray.length();
                            ArrayList<String> photoUrlsList = new ArrayList<>();
                            for (int photoUrlIndex = 0; photoUrlIndex < photoUrlsSize; ++photoUrlIndex) {
                                photoUrlsList.add(photoUrlsJsonArray.getString(photoUrlIndex));
                            }
                            businessKey.setPhotoUrls(photoUrlsList);
                        }
                        businessKey.setAddress(address);
                        businessKey.setBusinessId(businessId);
                        businessKey.setEnterpriseId(enterpriseId);
                        businessKey.setExternalId(externalId);
                        businessKey.setEnterpriseName(enterpriseName);
                        businessKey.setGooglePlacesId(googlePlacesId);
                        businessKey.setName(name);

                        scheduleWorkerShift.setBusinessKey(businessKey);
                    }
                    shiftsList.add(scheduleWorkerShift);
                }
                return shiftsList;
            } else {
                return errorString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    public static int getOpenShiftOffersCount(String response) {
        try {
            int shiftsCount = 0;
            JSONObject mainJsonObject = new JSONObject(response);
            String responseStatus = mainJsonObject.optString("responseStatus");
            String errorString = mainJsonObject.optString("errorString");
            String operationStatus = mainJsonObject.optString("operationStatus");
            if (responseStatus.equals("SUCCESS")) {
                JSONArray shiftJsonArray = mainJsonObject.optJSONArray("shiftOffer");
                if (shiftJsonArray != null && shiftJsonArray.length() > 0) {
                    for (int i = 0; i < shiftJsonArray.length(); ++i) {
                        JSONObject weekScheduleRating = shiftJsonArray.getJSONObject(i);
                        boolean isSeen = weekScheduleRating.optBoolean("seen");
                        if (!isSeen) {
                            ++shiftsCount;
                        }
                    }
                }
            }
            return shiftsCount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getSwapShiftOffersCount(String response) {
        try {
            int shiftsCount = 0;
            JSONObject mainJsonObject = new JSONObject(response);
            String responseStatus = mainJsonObject.optString("responseStatus");
            String errorString = mainJsonObject.optString("errorString");
            String operationStatus = mainJsonObject.optString("operationStatus");
            if (responseStatus.equals("SUCCESS")) {
                JSONArray shiftJsonArray = mainJsonObject.optJSONArray("shiftSwapOffers");
                if (shiftJsonArray != null && shiftJsonArray.length() > 0) {
                    for (int i = 0; i < shiftJsonArray.length(); ++i) {
                        JSONObject weekScheduleRating = shiftJsonArray.getJSONObject(i);
                        boolean isSeen = weekScheduleRating.optBoolean("seen");
                        if (!isSeen) {
                            ++shiftsCount;
                        }
                    }
                }
            }
            return shiftsCount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getWeeklySchedulesRatingsCount(String response) {
        try {
            JSONObject responseObject = new JSONObject(response);
            String responseStatus = responseObject.optString("responseStatus");
            String errorString = responseObject.optString("errorString");
            int newSchedulesCount = 0;

            //setting calendar to next week.
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            cal.add(Calendar.DATE, 7);

            int weekStartDay = cal.get(Calendar.DAY_OF_YEAR);
            if (responseStatus.equalsIgnoreCase("SUCCESS")) {
                JSONArray weeklyScheduleRatingsArray = responseObject.optJSONArray("weeklyScheduleRatings");
                if (weeklyScheduleRatingsArray != null && weeklyScheduleRatingsArray.length() >= 1) {
                    int size = weeklyScheduleRatingsArray.length();
                    for (int i = 0; i < size; ++i) {
                        JSONObject weekScheduleRating = weeklyScheduleRatingsArray.getJSONObject(i);
                        String weekStartDayOfTheYear = weekScheduleRating.optString("weekStartDayOfTheYear");
                        boolean isSeen = weekScheduleRating.optBoolean("isSeen");
                        if (!isSeen && weekStartDay <= Integer.parseInt(weekStartDayOfTheYear)) {
                            ++newSchedulesCount;
                        }
                    }
                }
            }
            return newSchedulesCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Object doParseSchedules(String response) {
        try {
            JSONObject responseObject = new JSONObject(response);
            String responseStatus = responseObject.optString("responseStatus");
            String errorString = responseObject.optString("errorString");
            if (responseStatus.equalsIgnoreCase("SUCCESS")) {
                ArrayList<Schedule> schedulesList = new ArrayList<>();
                JSONArray weeklyScheduleRatingsArray = responseObject.optJSONArray("weeklyScheduleRatings");
                if (weeklyScheduleRatingsArray != null && weeklyScheduleRatingsArray.length() >= 1) {
                    int size = weeklyScheduleRatingsArray.length();
                    int moveToPosition = 0;
                    for (int i = 0; i < size; ++i) {
                        JSONObject weekScheduleRating = weeklyScheduleRatingsArray.getJSONObject(i);
                        Schedule schedule = new Schedule();

                        String year = weekScheduleRating.optString("year");
                        String weekStartDayOfTheYear = weekScheduleRating.optString("weekStartDayOfTheYear");
                        Log.d("week", weekStartDayOfTheYear);
                        String rating = weekScheduleRating.optString("rating", "0");
                        String scheduleStatus = weekScheduleRating.optString("scheduleStatus");
                        String acknowledgeDate = weekScheduleRating.optString("acknowledgeDate");
                        String startOfWeekDate = weekScheduleRating.optString("startOfWeekDate");
                        String endOfWeekDate = weekScheduleRating.optString("endOfWeekDate");
                        boolean isSeen = weekScheduleRating.optBoolean("isSeen");
                        String weeklyScheduleId = weekScheduleRating.optString("weeklyScheduleId");

                        boolean isCurrentWeek = LegionUtils.getDiffernceBnTwoDates(startOfWeekDate.replace("T", " ").replace("Z", " "));
                        schedule.setIsCurrentWeek(isCurrentWeek);
                        schedule.setIsPastWeek(LegionUtils.isPastWeek(startOfWeekDate.replace("T", " ").replace("Z", " "), endOfWeekDate.replace("T", " ").replace("Z", " ")));
                        schedule.setIsFinal(scheduleStatus != null && scheduleStatus.equals("Final"));
                        schedule.setYear(year);
                        schedule.setWeekStartDayOfTheYear(weekStartDayOfTheYear);
                        schedule.setRating((rating == null || rating.equals("null") || rating.equalsIgnoreCase("NoStar")) ? "0" : rating);
                        schedule.setAcknowledgeDate(acknowledgeDate);
                        schedule.setSeen(isSeen);
                        schedule.setStartOfWeekDate(startOfWeekDate);
                        schedule.setEndOfWeekDate(endOfWeekDate);
                        schedule.setWeeklyScheduleId(weeklyScheduleId);
                        schedule.setScheduleStatus(scheduleStatus);

                        JSONObject workerJsonObject = weekScheduleRating.optJSONObject("workerKey");
                        if (workerJsonObject != null) {
                            WorkerKey workerKey = new WorkerKey();
                            String externalId = workerJsonObject.optString("externalId");
                            String firstName = workerJsonObject.optString("firstName");
                            String lastName = workerJsonObject.optString("lastName");
                            String email = workerJsonObject.optString("email");
                            String phoneNumber = workerJsonObject.optString("phoneNumber");
                            String address1 = workerJsonObject.optString("address1");
                            String address2 = workerJsonObject.optString("address2");
                            String city = workerJsonObject.optString("city");
                            String state = workerJsonObject.optString("state");
                            String zip = workerJsonObject.optString("zip");
                            String memberId = workerJsonObject.optString("memberId");
                            String pictureUrl = workerJsonObject.optString("pictureUrl");
                            String status = workerJsonObject.optString("status");
                            String role = workerJsonObject.optString("role");
                            String startEngagementDate = workerJsonObject.optString("startEngagementDate");

                            workerKey.setExternalId(externalId);
                            workerKey.setAddress1(address1);
                            workerKey.setAddress2(address2);
                            workerKey.setCity(city);
                            workerKey.setEmail(email);
                            workerKey.setFirstName(firstName);
                            workerKey.setLastName(lastName);
                            workerKey.setMemberId(memberId);
                            workerKey.setPhoneNumber(phoneNumber);
                            workerKey.setPictureUrl(pictureUrl);
                            workerKey.setRole(role);
                            workerKey.setZip(zip);
                            workerKey.setState(state);
                            workerKey.setStartEngagementDate(startEngagementDate);
                            workerKey.setStatus(status);

                            schedule.setWorkerKey(workerKey);
                        }

                        JSONObject businessJsonObject = weekScheduleRating.optJSONObject("businessKey");
                        if (businessJsonObject != null) {
                            BusinessKey businessKey = new BusinessKey();

                            String externalId = businessJsonObject.optString("externalId");
                            String enterpriseName = businessJsonObject.optString("enterpriseName");
                            String enterpriseId = businessJsonObject.optString("enterpriseId");
                            String name = businessJsonObject.optString("name");
                            String address = businessJsonObject.optString("address");
                            String businessId = businessJsonObject.optString("businessId");
                            String googlePlacesId = businessJsonObject.optString("googlePlacesId");
                            JSONArray photoUrlsJsonArray = businessJsonObject.optJSONArray("photoUrl");
                            if(photoUrlsJsonArray != null) {
                                int photoUrlsSize = photoUrlsJsonArray.length();
                                ArrayList<String> photoUrlsList = new ArrayList<>();
                                for (int photoUrlIndex = 0; photoUrlIndex < photoUrlsSize; ++photoUrlIndex) {
                                    photoUrlsList.add(photoUrlsJsonArray.getString(photoUrlIndex));
                                }
                                businessKey.setPhotoUrls(photoUrlsList);
                            }

                            businessKey.setAddress(address);
                            businessKey.setBusinessId(businessId);
                            businessKey.setEnterpriseId(enterpriseId);
                            businessKey.setExternalId(externalId);
                            businessKey.setEnterpriseName(enterpriseName);
                            businessKey.setGooglePlacesId(googlePlacesId);
                            businessKey.setName(name);
                            schedule.setBusinessKey(businessKey);
                        }
                        schedulesList.add(schedule);
                    }
                    return schedulesList;
                } else {
                    //weeklyScheduleRatings zero.
                    return schedulesList;
                }
            } else {
                return errorString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }

    private static String convertFromMinutesToHours(String hrs) {
        String hours = "0";
        if (hrs != null) {
            hours = String.valueOf(Double.parseDouble(hrs) / 60).replace(".0", "");
        }
        return hours;
    }

    public static Object doParsePtoDetails(String response) {
        try {

            JSONObject responseObject = new JSONObject(response);
            String responseStatus = responseObject.optString("responseStatus");
            String errorString = responseObject.optString("error");
            ArrayList<Slot> slotArrayList = new ArrayList<Slot>();
            slotArrayList.clear();
            for (int ij = 0; ij < 8; ij++) {
                Slot slotModel = new Slot();
                slotModel.setDayOfTheWeek(0);
                slotModel.setIsPTO(false);
                slotArrayList.add(ij, slotModel);
            }

            if (responseStatus.equalsIgnoreCase("SUCCESS")) {
                JSONArray ptoInformationArray = responseObject.optJSONArray("ptoInformation");
                JSONObject ptoJsonObject = ptoInformationArray.getJSONObject(0);
                JSONArray slotArray = ptoJsonObject.getJSONArray("slot");
                for (int j = 0; j < slotArray.length(); j++) {
                    Slot slotModel = new Slot();
                    slotModel.setStartTimeMins(slotArray.getJSONObject(j).getInt("startMinutes"));
                    slotModel.setEndTimeMins(slotArray.getJSONObject(j).getInt("endMinutes"));
                    slotModel.setIsPTO(slotArray.getJSONObject(j).getBoolean("isPTO"));
                    slotModel.setDayOfTheWeek(slotArray.getJSONObject(j).getInt("dayOfTheWeek"));
                    slotArrayList.set(slotArray.getJSONObject(j).getInt("dayOfTheWeek"), slotModel);
                }
                Log.d("led", "" + slotArrayList.size());
                return slotArrayList;
            } else {
                return slotArrayList;
            }
        } catch (Exception e) {
            return e;
        }
    }

    private Object doParseSummaryDetails(String response) {
        try {
            JSONObject responseObject = new JSONObject(response);
            String responseStatus = responseObject.optString("responseStatus");
            String errorString = responseObject.optString("errorString");
            WorkerShiftSummary workerShiftSummary = new WorkerShiftSummary();
            workerShiftSummary.setNumberOfHours("0");

            if (responseStatus.equalsIgnoreCase("SUCCESS")) {
                JSONArray workerShiftsSummaryArray = responseObject.optJSONArray("workerShiftSummary");

                if ((workerShiftsSummaryArray != null) && (workerShiftsSummaryArray.length() > 0)) {
                    JSONObject workerShiftsSummaryObject = workerShiftsSummaryArray.getJSONObject(0);
                    String weeklyScheduleRating = workerShiftsSummaryObject.optString("weeklyScheduleRating");
                    String scheduleStatus = workerShiftsSummaryObject.optString("scheduleStatus");
                    String totalShift = workerShiftsSummaryObject.optString("totalShift");
                    String totalPreferredMinutesScheduled = workerShiftsSummaryObject.optString("totalPreferredMinutesScheduled");
                    String totalPreferredMinutes = workerShiftsSummaryObject.optString("totalPreferredMinutes");
                    String totalConflicts = workerShiftsSummaryObject.optString("totalConflicts");
                    String totalProjectedPay = workerShiftsSummaryObject.optString("totalProjectedPay");
                    String totalOffers = workerShiftsSummaryObject.optString("totalOffers");
                    String totalOffersClaimed = workerShiftsSummaryObject.optString("totalOffersClaimed");
                    String totalOffersApproved = workerShiftsSummaryObject.optString("totalOffersApproved");
                    String totalShiftMinutes = workerShiftsSummaryObject.optString("totalShiftMinutes");
                    String numberofhrs = convertFromMinutesToHours(totalShiftMinutes);
                    workerShiftSummary.setWeeklyScheduleRating(weeklyScheduleRating);
                    workerShiftSummary.setScheduleStatus(scheduleStatus);
                    workerShiftSummary.setTotalShift(totalShift);
                    workerShiftSummary.setTotalPreferredMinutesScheduled(totalPreferredMinutesScheduled);
                    workerShiftSummary.setTotalPreferredMinutes(totalPreferredMinutes);
                    workerShiftSummary.setTotalConflicts(totalConflicts);
                    workerShiftSummary.setTotalProjectedPay(totalProjectedPay);
                    workerShiftSummary.setTotalOffers(totalOffers);
                    workerShiftSummary.setTotalOffersClaimed(totalOffersClaimed);
                    workerShiftSummary.setTotalOffersApproved(totalOffersApproved);
                    workerShiftSummary.setTotalShiftMinutes(totalShiftMinutes);
                    workerShiftSummary.setNumberOfHours(numberofhrs);
                }
                return workerShiftSummary;
            } else {
                return errorString;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e;
        }
    }
}

