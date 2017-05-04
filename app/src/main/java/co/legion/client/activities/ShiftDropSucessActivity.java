package co.legion.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import base.Legion_BaseActivity;
import co.legion.client.R;
import models.AssociatedWorker;
import models.BusinessKey;
import models.ScheduleWorkerShift;
import models.WorkerKey;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 06-Jan-17.
 */
public class ShiftDropSucessActivity extends Legion_BaseActivity implements Legion_NetworkCallback {
    private ScheduleWorkerShift scheduleDetails;
    private String weekStartDayOfTheYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drop_request_sucess_activity);
        scheduleDetails = (ScheduleWorkerShift) getIntent().getExtras().get(Extras_Keys.WORKSHIFTS_LIST);
        findViewById(R.id.returnBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        // doPtoRequest();
        doLoadWeeklySchedules();
        prefsManager.save(Prefs_Keys.REFRESH,"REFRESH");
        //  doLoadScheduleSummary();
        //   doScheduleRatingRequest();
    }

    private void doLoadWeeklySchedules() {
        try {
            if (!LegionUtils.isOnline(this)) {
                LegionUtils.showOfflineDialog(this);
                return;
            }
            LegionUtils.hideKeyboard(this);
            Log.v("SESSION_ID", "" + prefsManager.get(Prefs_Keys.SESSION_ID));
            Calendar cal = Calendar.getInstance();
            RequestParams params = new RequestParams();
            String year = LegionUtils.getYearFromDate(prefsManager.get(Prefs_Keys.START_DATE));
            params.put("year", year);
            params.put("weekStartDayOfTheYear", LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", prefsManager.get(Prefs_Keys.START_DATE)));
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_WEEKLY_SCHEDULES_REQUEST_CODE, ServiceUrls.GET_WEEK_SCHEDULES_URL, params, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLoadScheduleSummary() {
        try {
            if (!LegionUtils.isOnline(this)) {
                LegionUtils.showOfflineDialog(this);
                return;
            }
            LegionUtils.hideKeyboard(this);
            Log.v("SESSION_ID", "" + prefsManager.get(Prefs_Keys.SESSION_ID));

            Calendar cal = Calendar.getInstance();
            RequestParams params = new RequestParams();
            String year = LegionUtils.getYearFromDate(Extras_Keys.START_DATE);
            params.put("year", year);
            params.put("weekStartDayOfTheYear", LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", prefsManager.get(Prefs_Keys.START_DATE)));
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SCHEDULE_SUMMARY_REQUEST_CODE, ServiceUrls.GET_SCHEDULE_SUMMERY_URL, params, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doPtoRequest() {
        try {
            if (this != null) {
                if (!LegionUtils.isOnline(this)) {
                    LegionUtils.showOfflineDialog(this);
                    return;
                }
                LegionUtils.hideKeyboard(this);
                Log.v("SESSION_ID", "" + prefsManager.get(Prefs_Keys.SESSION_ID));
                Calendar cal = Calendar.getInstance();
                RequestParams params = new RequestParams();
                String year = LegionUtils.getYearFromDate(prefsManager.get(Prefs_Keys.START_DATE));
                params.put("year", year);
                params.put("weekStartDayOfTheYear", LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", prefsManager.get(Prefs_Keys.START_DATE)));
                Legion_RestClient restClient = new Legion_RestClient(this, this);
                restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_PTO_REQUEST_CODE, ServiceUrls.GET_PTO_URL, params, prefsManager.get(Prefs_Keys.SESSION_ID), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doScheduleRatingRequest() {
        try {
            if (!LegionUtils.isOnline(this)) {
                LegionUtils.showOfflineDialog(this);
                return;
            }
            LegionUtils.hideKeyboard(this);
            Log.v("SESSION_ID", "" + prefsManager.get(Prefs_Keys.SESSION_ID));
            Calendar cal = Calendar.getInstance();
            RequestParams params = new RequestParams();
            String year = LegionUtils.getYearFromDate(prefsManager.get(Prefs_Keys.START_DATE));
            params.put("year", year);
            params.put("weekStartDayOfTheYear", weekStartDayOfTheYear = LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", prefsManager.get(Prefs_Keys.START_DATE)));
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SCHEDULED_RATING, ServiceUrls.GET_SCHEDULES_URL, params, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ShiftDropSucessActivity.this, ScheduleShiftDetailsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduleDetails);
        startActivity(i);
    }


    @Override
    public void onStartRequest(int requestCode) {
        LegionUtils.showProgressDialog(this);
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        LegionUtils.hideProgressDialog();
        if (requestCode == WebServiceRequestCodes.GET_WEEKLY_SCHEDULES_REQUEST_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    prefsManager.save(Prefs_Keys.WEEKLY_SCHEDULES, object.toString());
                    JSONArray workerShiftsArray = object.optJSONArray("workerShifts");
                    if (workerShiftsArray != null && workerShiftsArray.length() >= 1) {
                        int size = workerShiftsArray.length();
                        for (int i = 0; i < size; ++i) {
                            JSONObject workerShiftJsonObject = workerShiftsArray.optJSONObject(i);
                            String shiftId = workerShiftJsonObject.optString("shiftId");
                            if (shiftId.equalsIgnoreCase(scheduleDetails.getShiftId())) {
                                // scheduleDetails = LegionUtils.getUpdatedSchedule(workerShiftJsonObject, scheduleDetails);
                                ScheduleWorkerShift scheduleWorkerShift = scheduleDetails;
                                String year = (workerShiftJsonObject.optString("year"));
                                String weekStartDayOfTheYear = workerShiftJsonObject.optString("weekStartDayOfTheYear");
                                String role1 = workerShiftJsonObject.optString("role");
                                String name1 = workerShiftJsonObject.optString("name");
                                String notes = workerShiftJsonObject.optString("notes");
                                String dayOfTheWeek = workerShiftJsonObject.optString("dayOfTheWeek");
                                String startMin = workerShiftJsonObject.optString("startMin");
                                String endMin = workerShiftJsonObject.optString("endMin");
                                String shiftStartDate = workerShiftJsonObject.optString("shiftStartDate");
                                String shiftEndDate = workerShiftJsonObject.optString("shiftEndDate");
                                String shiftIdd = workerShiftJsonObject.optString("shiftId");
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
                                String offerStatus = workerShiftJsonObject.optString("offerStatus");
                                String type = workerShiftJsonObject.optString("type");
                                String regularMinutes = workerShiftJsonObject.optString("regularMinutes");
                                scheduleWorkerShift.setAvailability(availability);
                                scheduleWorkerShift.setClockInTime(clockInTime);
                                scheduleWorkerShift.setClockOutTime(clockOutTime);
                                scheduleWorkerShift.setCost(cost);
                                scheduleWorkerShift.setDayOfTheWeek(dayOfTheWeek);
                                scheduleWorkerShift.setDoubleOvertimeMinutes(doubleOvertimeMinutes);
                                scheduleWorkerShift.setEndMin(endMin);
                                scheduleWorkerShift.setEstimatedPay(estimatedPay);
                                scheduleWorkerShift.setHasMeal(hasMeal);
                                scheduleWorkerShift.setStatus(status1);
                                scheduleWorkerShift.setRegularMinutes(regularMinutes);
                                scheduleWorkerShift.setWeekStartDayOfTheYear(weekStartDayOfTheYear);
                                scheduleWorkerShift.setWeekDoubleOvertimeMinutes(weekDoubleOvertimeMinutes);
                                scheduleWorkerShift.setWeekOvertimeMinutes(weekOvertimeMinutes);
                                scheduleWorkerShift.setUnpaidBreakMinutes(unpaidBreakMinutes);
                                scheduleWorkerShift.setShiftEndDate(shiftEndDate);
                                scheduleWorkerShift.setShiftStartDate(shiftStartDate);
                                scheduleWorkerShift.setShiftId(shiftIdd);
                                scheduleWorkerShift.setYear(year);
                                scheduleWorkerShift.setRole(role1);
                                scheduleWorkerShift.setName(name1);
                                scheduleWorkerShift.setNotes(notes);
                                scheduleWorkerShift.setStartMin(startMin);
                                scheduleWorkerShift.setType(type);
                                scheduleWorkerShift.setOfferStatus(offerStatus);
                                JSONArray associatedWorkersArray = workerShiftJsonObject.optJSONArray("associatedWorkers");
                                Log.v("associateArray: ", "" + associatedWorkersArray.length());
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
                                        if(!nickName.equalsIgnoreCase("null") || nickName!=null || nickName.length()>0){
                                            associatedWorker.setNickName(associatedWorkerJsonObj.optString("nickName"));
                                        }else {
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
                                    businessKey.setAddress(address);
                                    businessKey.setBusinessId(businessId);
                                    businessKey.setEnterpriseId(enterpriseId);
                                    businessKey.setExternalId(externalId);
                                    businessKey.setEnterpriseName(enterpriseName);
                                    businessKey.setGooglePlacesId(googlePlacesId);
                                    businessKey.setName(name);
                                    scheduleWorkerShift.setBusinessKey(businessKey);
                                }
                                scheduleDetails =  scheduleWorkerShift;
                            }


                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == WebServiceRequestCodes.GET_PTO_REQUEST_CODE) {

            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    prefsManager.save(Prefs_Keys.WEEKLY_SCHEDULES, object.toString());

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == WebServiceRequestCodes.GET_SCHEDULED_RATING) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    prefsManager.save(Prefs_Keys.WEEKLY_SCHEDULES_RATING, object.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == WebServiceRequestCodes.GET_SCHEDULE_SUMMARY_REQUEST_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    prefsManager.save(Prefs_Keys.WEEKLY_SCHEDULES_SUMMARY, object.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {

    }
}
