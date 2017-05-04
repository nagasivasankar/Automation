package co.legion.client.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.SwapRequestStatusAdapter;
import base.Legion_BaseActivity;
import co.legion.client.R;
import models.AssociatedWorker;
import models.BusinessKey;
import models.ScheduleWorkerShift;
import models.ShiftswapOffersVo;
import models.WorkerKey;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 05-Jan-17.
 */
public class SwapRequestedStatusActivity extends Legion_BaseActivity implements Legion_Constants, View.OnClickListener {
    private ScheduleWorkerShift scheduleDetails;
    private ListView listView;
    private ArrayList<ScheduleWorkerShift> selectedShiftsList;
    private SwapRequestStatusAdapter adapter;
    private int sizeOfSelectedListsCount;
    private String responsedata;
    private ArrayList<ShiftswapOffersVo> offersListvo = new ArrayList<ShiftswapOffersVo>();
    boolean earlyAmBn, lateAmBn, afternoonBn, eveningBn, nightBn;
    boolean monLlBn, tueLlBn, wedLlBn, thuLlBn, friLlBn, satLlBn, sunLlBn;
    private ArrayList oneWeekDatesArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_request_status);
        listView = (ListView) findViewById(R.id.listView);
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setOnClickListener(this);
        tvToolbarTile.setText("Swap Request Status");
        tvToolbarTile.setFocusable(true);
        tvToolbarTile.setFocusableInTouchMode(true);
        tvToolbarTile.requestFocus();
        responsedata = getIntent().getStringExtra(Extras_Keys.STATUS_RESPONSE_KEY);
        doParseWorkerShifts(responsedata);
        adapter = new SwapRequestStatusAdapter(this, scheduleDetails, offersListvo);
        listView.setAdapter(adapter);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parent_ll));
    }

    private void doParseWorkerShifts(String response) {
        offersListvo.clear();
        try {
            JSONObject responseObject = new JSONObject(response);
            String responseStatus = responseObject.optString("responseStatus");
            String errorString = responseObject.optString("errorString");
            if (responseStatus.equalsIgnoreCase("SUCCESS")) {
                JSONArray shiftswapOfferslist = responseObject.optJSONArray("shiftSwapOffers");
                int shiftswapoffersize = shiftswapOfferslist.length();
                for (int swapoffersize = 0; swapoffersize < shiftswapoffersize; swapoffersize++) {
                    ShiftswapOffersVo offeritem = new ShiftswapOffersVo();
                    JSONObject shiftswapOffers = shiftswapOfferslist.optJSONObject(swapoffersize);
                    JSONObject shiftoffered = shiftswapOffers.getJSONObject("shiftOffered");         // shift offered
                    JSONObject shiftrequestedobj = shiftswapOffers.getJSONObject("shiftRequested");
                    offeritem.setShiftOffered(shiftofferedandRequested(shiftoffered));
                    offeritem.setShiftRequested(shiftofferedandRequested(shiftrequestedobj));
                    offeritem.setOfferStatus(shiftswapOffers.optString("offerStatus"));
                    offersListvo.add(offeritem);
                    LegionUtils.hideProgressDialog();
                }
                adapter.notifyDataSetChanged();
            } else {
                showToast(errorString);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LegionUtils.hideProgressDialog();
        }
    }


    private ScheduleWorkerShift shiftofferedandRequested(JSONObject workerShiftJsonObject) {
        ScheduleWorkerShift scheduleWorkerShift;
        scheduleWorkerShift = new ScheduleWorkerShift();
        String year = (workerShiftJsonObject.optString("year"));
        String weekStartDayOfTheYear = workerShiftJsonObject.optString("weekStartDayOfTheYear");
        String role1 = workerShiftJsonObject.optString("role");
        String name1 = workerShiftJsonObject.optString("name");
        String notes = workerShiftJsonObject.optString("notes");
        String dayOfTheWeek = workerShiftJsonObject.optString("dayOfTheWeek");
        String startMin = workerShiftJsonObject.optString("startMin");
        // checkHours(startMin);
        String endMin = workerShiftJsonObject.optString("endMin");
        String shiftStartDate = workerShiftJsonObject.optString("shiftStartDate");
        //   checkIntervals(shiftStartDate);
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
        String regularMinutes = workerShiftJsonObject.optString("regularMinutes");
        scheduleWorkerShift.setAvailability(availability);
        scheduleWorkerShift.setClockInTime(clockInTime);
        scheduleWorkerShift.setClockOutTime(clockOutTime);
        scheduleWorkerShift.setCost(cost);
        scheduleWorkerShift.setSelectedOrNot(false);
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
        scheduleWorkerShift.setShiftId(shiftId);
        scheduleWorkerShift.setYear(year);
        scheduleWorkerShift.setRole(role1);
        scheduleWorkerShift.setName(name1);
        scheduleWorkerShift.setNotes(notes);
        scheduleWorkerShift.setStartMin(startMin);
        scheduleWorkerShift.setType(type);

        JSONArray associatedWorkersArray = workerShiftJsonObject.optJSONArray("associatedWorkers");
        Log.v("associateArray: ", "" + associatedWorkersArray.length());
        if (associatedWorkersArray != null && associatedWorkersArray.length() > 0) {
            ArrayList<AssociatedWorker> associatedWorkerArrayList = new ArrayList<>();
            int associatedWorkersArraySize = associatedWorkersArray.length();
            for (int j = 0; j < associatedWorkersArraySize; ++j)
                try {
                    {
                        JSONObject associatedWorkerJsonObj = associatedWorkersArray.getJSONObject(j);
                        AssociatedWorker associatedWorker = new AssociatedWorker();
                        associatedWorker.setExternalId(associatedWorkerJsonObj.optString("externalId"));
                        associatedWorker.setAddress1(associatedWorkerJsonObj.optString("address1"));
                        associatedWorker.setAddress2(associatedWorkerJsonObj.optString("address2"));
                        associatedWorker.setCity(associatedWorkerJsonObj.optString("city"));
                        associatedWorker.setEmail(associatedWorkerJsonObj.optString("email"));
                        associatedWorker.setFirstName(associatedWorkerJsonObj.optString("firstName"));
                        String nickName = associatedWorkerJsonObj.optString("nickName");
                        if (!nickName.equalsIgnoreCase("null") || nickName != null || nickName.length() > 0) {
                            associatedWorker.setNickName(associatedWorkerJsonObj.optString("nickName"));
                        } else {
                            associatedWorker.setNickName("");
                        }
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
                } catch (JSONException e) {
                    e.printStackTrace();
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
        return scheduleWorkerShift;
    }


    public void checkIntervals(String shiftStartDate) {
        if (oneWeekDatesArray.get(0).equals(LegionUtils.getDatefromServerDate(shiftStartDate))) {
            monLlBn = true;
        } else if (oneWeekDatesArray.get(1).equals(LegionUtils.getDatefromServerDate(shiftStartDate))) {
            tueLlBn = true;
        } else if (oneWeekDatesArray.get(2).equals(LegionUtils.getDatefromServerDate(shiftStartDate))) {
            wedLlBn = true;
        } else if (oneWeekDatesArray.get(3).equals(LegionUtils.getDatefromServerDate(shiftStartDate))) {
            thuLlBn = true;
        } else if (oneWeekDatesArray.get(4).equals(LegionUtils.getDatefromServerDate(shiftStartDate))) {
            friLlBn = true;
        } else if (oneWeekDatesArray.get(5).equals(LegionUtils.getDatefromServerDate(shiftStartDate))) {
            satLlBn = true;
        } else if (oneWeekDatesArray.get(6).equals(LegionUtils.getDatefromServerDate(shiftStartDate))) {
            sunLlBn = true;
        }
    }

    public void checkHours(String startMins) {
        int startHour = LegionUtils.convertFromMinutesToHoursInt(startMins);
        if (startHour != 0) {
            if (startHour >= 5 && startHour < 9) {
                earlyAmBn = true;
            } else if (startHour >= 9 && startHour < 13) { //9Am to 1pm
                lateAmBn = true;
            } else if (startHour >= 13 && startHour < 17) { //1pm to 5pm
                afternoonBn = true;
            } else if (startHour >= 17 && startHour < 21) {  //5pm to 9pm
                eveningBn = true;
            } else {
                nightBn = true;
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.toolbarBack) {
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        onBack();
    }

    public void onBack() {
        finish();
    }


}
