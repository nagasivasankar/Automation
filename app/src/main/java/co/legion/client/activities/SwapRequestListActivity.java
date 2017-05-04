package co.legion.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.SwapRequestWorkerShiftsAdapter;
import base.Legion_BaseActivity;
import co.legion.client.R;
import helpers.Legion_PrefsManager;
import models.AssociatedWorker;
import models.BusinessKey;
import models.ScheduleWorkerShift;
import models.WorkerKey;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 05-Jan-17.
 */
public class SwapRequestListActivity extends Legion_BaseActivity implements Legion_NetworkCallback, AdapterView.OnItemClickListener, View.OnClickListener {
    boolean earlyAmBn, lateAmBn, afternoonBn, eveningBn, nightBn;
    boolean monLlBn, tueLlBn, wedLlBn, thuLlBn, friLlBn, satLlBn, sunLlBn;
    private ArrayList<ScheduleWorkerShift> workerShiftsList = new ArrayList<ScheduleWorkerShift>();
    private ArrayList<ScheduleWorkerShift> filterdworkerShiftsList = new ArrayList<ScheduleWorkerShift>();
    private Legion_PrefsManager legionPreferences;
    private ScheduleWorkerShift scheduleDetails;
    private ListView swapReqListView;
    private SwapRequestWorkerShiftsAdapter adapter;
    private ArrayList<String> timingsSelectedArrayList = new ArrayList<>();
    private ArrayList<String> daysSelectedArrayList;
    private ArrayList oneWeekDatesArray;
    private TextView dateMonTv, dateTueTv, dateWedTv, dateThuTv, dateFriTv, dateSatTv, dateSunTv;
    private TextView dayMonTv, dayTueTv, dayWedTv, dayThuTv, dayFriTv, daySatTv, daySunTv;
    private TextView earlyAmTv, lateAmTv, afernoonTv, eveningTv, nightTv;
    private TextView countTv;
    private TextView btNextTv;
    private LinearLayout monLL;
    private LinearLayout tueLL;
    private LinearLayout wedLL;
    private LinearLayout thuLL;
    private LinearLayout friLL;
    private LinearLayout satLL;
    private LinearLayout sunLL;
    private LinearLayout staticView;
    private int firstVisible;
    private int selectedDay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_sd_swift);
        legionPreferences = new Legion_PrefsManager(this);
        swapReqListView = (ListView) findViewById(R.id.swapReqListView);
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        countTv = (TextView) findViewById(R.id.countTv);
        TextView btBackTv = (TextView) findViewById(R.id.btBackTv);
         btNextTv = (TextView) findViewById(R.id.btNextTv);
        tvToolbarTile.setText("Find Shifts to Swap");
        staticView = (LinearLayout) findViewById(R.id.staticView);
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setImageResource(R.drawable.dismiss);
        backImage.setOnClickListener(this);
        dateMonTv = (TextView) findViewById(R.id.dateMonTv);
        dateTueTv = (TextView) findViewById(R.id.dateTueTv);
        dateWedTv = (TextView) findViewById(R.id.dateWedTv);
        dateThuTv = (TextView) findViewById(R.id.dateThuTv);
        dateFriTv = (TextView) findViewById(R.id.dateFriTv);
        dateSatTv = (TextView) findViewById(R.id.dateSatTv);
        dateSunTv = (TextView) findViewById(R.id.dateSunTv);
        dayMonTv = (TextView) findViewById(R.id.monTv);
        dayTueTv = (TextView) findViewById(R.id.tueTv);
        dayWedTv = (TextView) findViewById(R.id.wedTv);
        dayThuTv = (TextView) findViewById(R.id.thuTv);
        dayFriTv = (TextView) findViewById(R.id.friTv);
        daySatTv = (TextView) findViewById(R.id.satTv);
        daySunTv = (TextView) findViewById(R.id.sunTv);
        monLL = (LinearLayout) findViewById(R.id.monLL);
        tueLL = (LinearLayout) findViewById(R.id.tueLL);
        wedLL = (LinearLayout) findViewById(R.id.wedLL);
        thuLL = (LinearLayout) findViewById(R.id.thuLL);
        friLL = (LinearLayout) findViewById(R.id.friLL);
        satLL = (LinearLayout) findViewById(R.id.satLL);
        sunLL = (LinearLayout) findViewById(R.id.sunLL);
        monLL.setOnClickListener(this);
        tueLL.setOnClickListener(this);
        wedLL.setOnClickListener(this);
        thuLL.setOnClickListener(this);
        friLL.setOnClickListener(this);
        satLL.setOnClickListener(this);
        sunLL.setOnClickListener(this);
        btBackTv.setOnClickListener(this);
        btNextTv.setOnClickListener(this);
        earlyAmTv = (TextView) findViewById(R.id.earlyAmTv);
        lateAmTv = (TextView) findViewById(R.id.lateAmTv);
        afernoonTv = (TextView) findViewById(R.id.afernoonTv);
        eveningTv = (TextView) findViewById(R.id.eveningTv);
        nightTv = (TextView) findViewById(R.id.nightTv);
        earlyAmTv.setOnClickListener(this);
        lateAmTv.setOnClickListener(this);
        afernoonTv.setOnClickListener(this);
        eveningTv.setOnClickListener(this);
        nightTv.setOnClickListener(this);
        earlyAmTv.setSelected(false);
        lateAmTv.setSelected(false);
        afernoonTv.setSelected(false);
        eveningTv.setSelected(false);
        nightTv.setSelected(false);
        dateMonTv.setSelected(false);
        dateTueTv.setSelected(false);
        dateWedTv.setSelected(false);
        dateThuTv.setSelected(false);
        dateFriTv.setSelected(false);
        dateSatTv.setSelected(false);
        dateSunTv.setSelected(false);
        daysSelectedArrayList = new ArrayList<String>();
        btNextTv.setTextColor(getResources().getColor(R.color.gray_text_color));
        oneWeekDatesArray = LegionUtils.getOneWeekDates(legionPreferences.get(Prefs_Keys.START_DATE));
        scheduleDetails = (ScheduleWorkerShift) getIntent().getExtras().get(Extras_Keys.WORKSHIFTS_LIST);
        if (oneWeekDatesArray.size() == 7) {
            dateMonTv.setText(oneWeekDatesArray.get(0).toString());
            dateTueTv.setText(oneWeekDatesArray.get(1).toString());
            dateWedTv.setText(oneWeekDatesArray.get(2).toString());
            dateThuTv.setText(oneWeekDatesArray.get(3).toString());
            dateFriTv.setText(oneWeekDatesArray.get(4).toString());
            dateSatTv.setText(oneWeekDatesArray.get(5).toString());
            dateSunTv.setText(oneWeekDatesArray.get(6).toString());
        }
        doLoadWorkerShifts();
        adapter = new SwapRequestWorkerShiftsAdapter(this, filterdworkerShiftsList, countTv,btNextTv);
        swapReqListView.setAdapter(adapter);
        //The initial height of that view
        final int initialViewHeight = staticView.getLayoutParams().height;
       swapReqListView.setOnScrollListener(onScrollListener());

        LegionUtils.doApplyFont(getAssets(), (RelativeLayout) findViewById(R.id.parentLayout));
    }

    public ArrayList<ScheduleWorkerShift> getSelectedShifts() {
        String data = "";
        ArrayList<ScheduleWorkerShift> arrayList = new ArrayList();
        List<ScheduleWorkerShift> shiftsList = workerShiftsList;
        for (int i = 0; i < shiftsList.size(); i++) {
            ScheduleWorkerShift shifts = shiftsList.get(i);
            if (shifts.isSelectedOrNot() == true) {
                arrayList.add(shifts);
                data = data + "\n" + shifts.getName().toString();
            }
        }
        Log.d("selected Shifts: ", data);
        Log.d("selected Shifts Count: ", "" + arrayList.size());
        return arrayList;
    }


    public AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem == 0) {
                    // check if we reached the top or bottom of the list
                    View v = swapReqListView.getChildAt(0);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the top: visible header and footer
                        Log.i("TAG", "top reached");
                        staticView.setVisibility(View.VISIBLE);
                    }
                } else if (totalItemCount - visibleItemCount == firstVisibleItem) {
                    View v = swapReqListView.getChildAt(totalItemCount - 1);
                    int offset = (v == null) ? 0 : v.getTop();
                    if (offset == 0) {
                        // reached the bottom: visible header and footer
                        Log.i("TAG", "bottom reached!");
                        staticView.setVisibility(View.GONE);
                        //  setViewStatus(footer, header, View.VISIBLE);
                    }
                } else if (totalItemCount - visibleItemCount > firstVisibleItem) {
                    staticView.setVisibility(View.GONE);
                }
            }
        };
    }

    private void doLoadWorkerShifts() {
        try {
            if (!LegionUtils.isOnline(this)) {
                LegionUtils.showOfflineDialog(this);
                return;
            }
            LegionUtils.hideKeyboard(this);
            Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));
            String shiftId = scheduleDetails.getShiftId();
            RequestParams params = new RequestParams();
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.SWAP_REQUEST_LIST_CODE, ServiceUrls.GET_SWAP_REQUEST_LIST_URL + shiftId, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doParseWorkerShifts(String response) {
        workerShiftsList.clear();
        try {
            JSONObject responseObject = new JSONObject(response);
            String responseStatus = responseObject.optString("responseStatus");
            String errorString = responseObject.optString("errorString");
            if (responseStatus.equalsIgnoreCase("SUCCESS")) {
                JSONArray workerShiftsArray = responseObject.optJSONArray("workerShifts");
                if (workerShiftsArray != null && workerShiftsArray.length() >= 1) {
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
                        String startMin = workerShiftJsonObject.optString("startMin");
                        checkHours(startMin);
                        String endMin = workerShiftJsonObject.optString("endMin");
                        String shiftStartDate = workerShiftJsonObject.optString("shiftStartDate");
                        checkIntervals(shiftStartDate);
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
                        scheduleWorkerShift.setPosition(i);
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
                            for (int j = 0; j < associatedWorkersArraySize; ++j) {
                                JSONObject associatedWorkerJsonObj = associatedWorkersArray.getJSONObject(j);
                                AssociatedWorker associatedWorker = new AssociatedWorker();
                                associatedWorker.setExternalId(associatedWorkerJsonObj.optString("externalId"));
                                associatedWorker.setAddress1(associatedWorkerJsonObj.optString("address1"));
                                associatedWorker.setAddress2(associatedWorkerJsonObj.optString("address2"));
                                associatedWorker.setCity(associatedWorkerJsonObj.optString("city"));
                                associatedWorker.setEmail(associatedWorkerJsonObj.optString("email"));
                                associatedWorker.setFirstName(associatedWorkerJsonObj.optString("firstName"));
                                String nickName = associatedWorkerJsonObj.optString("nickName");
                                if(!nickName.equalsIgnoreCase("null") || nickName!=null || nickName.length()>0){
                                    associatedWorker.setNickName(associatedWorkerJsonObj.optString("nickName"));
                                }else {
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
                        workerShiftsList.add(scheduleWorkerShift);
                        filterdworkerShiftsList.add(scheduleWorkerShift);
                    }
                }
                checkingShiftsIntervalTimingsAvlbleOrNot();
                checkingShiftsDaysAvlbleOrNot();
                adapter.notifyDataSetChanged();
                LegionUtils.hideProgressDialog();
            } else {
                showToast(errorString);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LegionUtils.hideProgressDialog();
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

    private void checkingShiftsDaysAvlbleOrNot() {
        if (!monLlBn) {
            dateMonTv.setTextColor(ActivityCompat.getColor(this, R.color.gray_text_color));
            monLL.setEnabled(false);
        }
        if (!tueLlBn) {
            dateTueTv.setTextColor(ActivityCompat.getColor(this, R.color.gray_text_color));
            tueLL.setEnabled(false);
        }
        if (!wedLlBn) {
            dateWedTv.setTextColor(ActivityCompat.getColor(this, R.color.gray_text_color));
            wedLL.setEnabled(false);
        }
        if (!thuLlBn) {
            dateThuTv.setTextColor(ActivityCompat.getColor(this, R.color.gray_text_color));
            thuLL.setEnabled(false);
        }
        if (!friLlBn) {
            dateFriTv.setTextColor(ActivityCompat.getColor(this, R.color.gray_text_color));
            friLL.setEnabled(false);
        }
        if (!satLlBn) {
            dateSatTv.setTextColor(ActivityCompat.getColor(this, R.color.gray_text_color));
            satLL.setEnabled(false);
        }
        if (!sunLlBn) {
            dateSunTv.setTextColor(ActivityCompat.getColor(this, R.color.gray_text_color));
            sunLL.setEnabled(false);
        }
    }

    public void checkingShiftsIntervalTimingsAvlbleOrNot() {
        if (!earlyAmBn) {
            earlyAmTv.setBackgroundResource(R.color.very_light_gray);       //R.color.very_light_gray
            earlyAmTv.setEnabled(false);
        } else {
            earlyAmTv.setBackgroundResource(0);
            earlyAmTv.setEnabled(true);
        }
        if (!lateAmBn) {
            lateAmTv.setBackgroundResource(R.color.very_light_gray);
            lateAmTv.setEnabled(false);
        } else {
            lateAmTv.setBackgroundResource(0);
            lateAmTv.setEnabled(true);
        }
        if (!afternoonBn) {
            afernoonTv.setBackgroundResource(R.color.very_light_gray);
            afernoonTv.setEnabled(false);
        } else {
            afernoonTv.setBackgroundResource(0);
            afernoonTv.setEnabled(true);
        }
        if (!eveningBn) {
            eveningTv.setBackgroundResource(R.color.very_light_gray);
            eveningTv.setEnabled(false);
        } else {
            eveningTv.setBackgroundResource(0);
            eveningTv.setEnabled(true);
        }
        if (!nightBn) {
            nightTv.setBackgroundResource(R.color.very_light_gray);
            nightTv.setEnabled(false);
        } else {
            nightTv.setBackgroundResource(0);
            nightTv.setEnabled(true);
        }

    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.SWAP_REQUEST_LIST_CODE) {
            LegionUtils.showProgressDialog(this);
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.SWAP_REQUEST_LIST_CODE) {
            LegionUtils.hideProgressDialog();
            doParseWorkerShifts(response.toString());
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        if (reasonPhrase != null) {
            if (requestCode == WebServiceRequestCodes.SWAP_REQUEST_LIST_CODE) {
                LegionUtils.hideProgressDialog();
                if (reasonPhrase.contains("Something went wrong")) {
                    LegionUtils.doLogout(this);
                    return;
                }
                showToast(reasonPhrase);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onClick(View v) {

        if (String.valueOf(v).contains("LL")) {
            timingsSelectedArrayList.clear();
            earlyAmTv.setSelected(false);
            lateAmTv.setSelected(false);
            afernoonTv.setSelected(false);
            eveningTv.setSelected(false);
            nightTv.setSelected(false);
            earlyAmBn = false;
            lateAmBn = false;
            afternoonBn = false;
            eveningBn = false;
            nightBn = false;
            earlyAmTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
            lateAmTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
            afernoonTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
            eveningTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
            nightTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
        }
        if (!v.isSelected() && String.valueOf(v).contains("LL")) {
            daysSelectedArrayList.clear();
            monLL.setSelected(false);
            tueLL.setSelected(false);
            wedLL.setSelected(false);
            thuLL.setSelected(false);
            friLL.setSelected(false);
            satLL.setSelected(false);
            sunLL.setSelected(false);
            dateMonTv.setBackgroundResource(0);
            dateTueTv.setBackgroundResource(0);
            dateWedTv.setBackgroundResource(0);
            dateThuTv.setBackgroundResource(0);
            dateFriTv.setBackgroundResource(0);
            dateSatTv.setBackgroundResource(0);
            dateSunTv.setBackgroundResource(0);
            dayMonTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
            dayTueTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
            dayWedTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
            dayThuTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
            dayFriTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
            daySatTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
            daySunTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
        }
        if(selectedDay == 1){
            dateMonTv.setTextColor(ActivityCompat.getColor(this,R.color.find_shift_text_black));
        }else if(selectedDay == 2){
            dateTueTv.setTextColor(ActivityCompat.getColor(this,R.color.find_shift_text_black));
        }else if(selectedDay == 3){
            dateWedTv.setTextColor(ActivityCompat.getColor(this,R.color.find_shift_text_black));
        }else if(selectedDay == 4){
            dateThuTv.setTextColor(ActivityCompat.getColor(this,R.color.find_shift_text_black));
        }else if(selectedDay == 5){
            dateFriTv.setTextColor(ActivityCompat.getColor(this,R.color.find_shift_text_black));
        }else if(selectedDay == 6){
            dateSatTv.setTextColor(ActivityCompat.getColor(this,R.color.find_shift_text_black));
        }else if(selectedDay == 7){
            dateSunTv.setTextColor(ActivityCompat.getColor(this,R.color.find_shift_text_black));
        }
        int id = v.getId();
        if (id == R.id.toolbarBack) {
            Intent i = new Intent(SwapRequestListActivity.this, ScheduleShiftDetailsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduleDetails);
            startActivity(i);
        } else if (id == R.id.btBackTv) {
            finish();
        } else if (id == R.id.btNextTv) {
            if (getSelectedShifts().size() > 0) {
                Intent i = new Intent(SwapRequestListActivity.this, ShiftRequestSubmitActivity.class);
                i.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduleDetails);
                i.putExtra(Extras_Keys.SELECTED_WORKERSHIFT_LIST, getSelectedShifts());
                startActivityForResult(i, 100);
            } else {
                showToast("Please select atleast one shift.");
            }
        } else if ((id == R.id.monLL)) {
            selectedDay = 1;
            if (dateMonTv.isSelected()) {
                daysSelectedArrayList.remove("1");
                monLL.setSelected(false);
                dayMonTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
                dateMonTv.setBackgroundResource(0);
                dateMonTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_black));
            } else {
                daysSelectedArrayList.add("1");
                monLL.setSelected(true);
                dayMonTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_lable_bg));
                dateMonTv.setBackgroundResource(R.drawable.bg_lite_circle_shifts_blue);
                dateMonTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
            }

        } else if (id == R.id.tueLL) {
            selectedDay = 2;
            if (dateTueTv.isSelected()) {
                daysSelectedArrayList.remove("2");
                tueLL.setSelected(false);
                dayTueTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
                dateTueTv.setBackgroundResource(0);
                dateTueTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_black));
            } else {
                daysSelectedArrayList.add("2");
                tueLL.setSelected(true);
                dayTueTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_lable_bg));
                dateTueTv.setBackgroundResource(R.drawable.bg_lite_circle_shifts_blue);
                dateTueTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
            }
        } else if (id == R.id.wedLL) {
            selectedDay = 3;
            if (dateWedTv.isSelected()) {
                daysSelectedArrayList.remove("3");
                wedLL.setSelected(false);
                dayWedTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
                dateWedTv.setBackgroundResource(0);
                dateWedTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_black));
            } else {
                daysSelectedArrayList.add("3");
                wedLL.setSelected(true);
                dayWedTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_lable_bg));
                dateWedTv.setBackgroundResource(R.drawable.bg_lite_circle_shifts_blue);
                dateWedTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
            }
        } else if (id == R.id.thuLL) {
            selectedDay = 4;
            if (dateThuTv.isSelected()) {
                daysSelectedArrayList.remove("4");
                thuLL.setSelected(false);
                dayThuTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
                dateThuTv.setBackgroundResource(0);
                dateThuTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_black));
            } else {
                daysSelectedArrayList.add("4");
                thuLL.setSelected(true);
                dayThuTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_lable_bg));
                dateThuTv.setBackgroundResource(R.drawable.bg_lite_circle_shifts_blue);
                dateThuTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
            }
        } else if (id == R.id.friLL) {
            selectedDay = 5;
            if (dateFriTv.isSelected()) {
                daysSelectedArrayList.remove("5");
                friLL.setSelected(false);
                dayFriTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
                dateFriTv.setBackgroundResource(0);
                dateFriTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_black));
            } else {
                daysSelectedArrayList.add("5");
                friLL.setSelected(true);
                dayFriTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_lable_bg));
                dateFriTv.setBackgroundResource(R.drawable.bg_lite_circle_shifts_blue);
                dateFriTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
            }
        } else if (id == R.id.satLL) {
            selectedDay = 6;
            if (dateSatTv.isSelected()) {
                daysSelectedArrayList.remove("6");
                satLL.setSelected(false);
                daySatTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
                dateSatTv.setBackgroundResource(0);
                dateSatTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_black));
            } else {
                daysSelectedArrayList.add("6");
                satLL.setSelected(true);
                daySatTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_lable_bg));
                dateSatTv.setBackgroundResource(R.drawable.bg_lite_circle_shifts_blue);
                dateSatTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
            }
        } else if (id == R.id.sunLL) {
            selectedDay = 7;
            if (v.isSelected()) {
                daysSelectedArrayList.remove("7");
                sunLL.setSelected(false);
                daySunTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_light_black));
                dateSunTv.setBackgroundResource(0);
                dateSunTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_black));
            } else {
                daysSelectedArrayList.add("7");
                sunLL.setSelected(true);
                daySunTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_lable_bg));
                dateSunTv.setBackgroundResource(R.drawable.bg_lite_circle_shifts_blue);
                dateSunTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
            }
        } else if (id == R.id.earlyAmTv) {
            if (earlyAmTv.isSelected()) {
                timingsSelectedArrayList.remove("earlyAm");
                earlyAmTv.setSelected(false);
                earlyAmTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
                earlyAmTv.setBackgroundResource(0);
            } else {
                timingsSelectedArrayList.add("earlyAm");
                earlyAmTv.setSelected(true);
                earlyAmTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
                earlyAmTv.setBackgroundResource(R.drawable.bg_light_gray_border);
            }
        } else if (id == R.id.lateAmTv) {
            if (v.isSelected()) {
                timingsSelectedArrayList.remove("lateAm");
                lateAmTv.setSelected(false);
                lateAmTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
                lateAmTv.setBackgroundResource(0);
            } else {
                timingsSelectedArrayList.add("lateAm");
                lateAmTv.setSelected(true);
                lateAmTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
                lateAmTv.setBackgroundResource(R.drawable.bg_light_gray_border);
            }
        } else if (id == R.id.afernoonTv) {
            if (afernoonTv.isSelected()) {
                timingsSelectedArrayList.remove("afternoon");
                afernoonTv.setSelected(false);
                afernoonTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
                v.setBackgroundResource(0);
            } else {
                timingsSelectedArrayList.add("afternoon");
                afernoonTv.setSelected(true);
                v.setBackgroundResource(R.drawable.bg_light_gray_border);


                afernoonTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
            }
        } else if (id == R.id.eveningTv) {
            if (eveningTv.isSelected()) {
                timingsSelectedArrayList.remove("evening");
                eveningTv.setSelected(false);
                eveningTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
                eveningTv.setBackgroundResource(0);
            } else {
                timingsSelectedArrayList.add("evening");
                eveningTv.setSelected(true);
                eveningTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
                eveningTv.setBackgroundResource(R.drawable.bg_light_gray_border);
            }
        } else if (id == R.id.nightTv) {
            if (nightTv.isSelected()) {
                timingsSelectedArrayList.remove("night");
                nightTv.setSelected(false);
                nightTv.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
                nightTv.setBackgroundResource(0);
            } else {
                timingsSelectedArrayList.add("night");
                nightTv.setSelected(true);
                nightTv.setTextColor(ActivityCompat.getColor(this, R.color.find_shift_text_highlight_blue));
                nightTv.setBackgroundResource(R.drawable.bg_light_gray_border);
            }
        }
        filterdworkerShiftsList.clear();
        Log.d("days arrayList", "" + daysSelectedArrayList);
        Log.d("timings arrayList", "" + timingsSelectedArrayList);
        for (int i = 0; i < workerShiftsList.size(); i++) {
            int startHour = LegionUtils.convertFromMinutesToHoursInt(workerShiftsList.get(i).getStartMin());
            if (daysSelectedArrayList.size() > 0) {
                for (int k = 0; k < daysSelectedArrayList.size(); k++) {
                    String dayOfTheWeek = daysSelectedArrayList.get(k).toString();
                    if (dayOfTheWeek.equalsIgnoreCase(workerShiftsList.get(i).getDayOfTheWeek())) {
                        if (timingsSelectedArrayList.size() > 0) {
                            timingsFilter(startHour, i);
                        } else {
                            ScheduleWorkerShift shift = workerShiftsList.get(i);
                            filterdworkerShiftsList.add(shift);
                            checkHours(shift.getStartMin());
                        }
                    }
                }
            } else {
                if (timingsSelectedArrayList.size() > 0) {
                    timingsFilter(startHour, i);
                }
            }
        }

        if ((daysSelectedArrayList.size() == 0) && (timingsSelectedArrayList.size() == 0)) {
            for (int i = 0; i < workerShiftsList.size(); i++) {
                ScheduleWorkerShift shift = workerShiftsList.get(i);
                filterdworkerShiftsList.add(shift);
                checkHours(shift.getStartMin());
            }
        }
        if (String.valueOf(v).contains("LL"))
            checkingShiftsIntervalTimingsAvlbleOrNot();
        adapter.notifyDataSetChanged();

    }

    public void timingsFilter(int startHour, int i) {
        for (int l = 0; l < timingsSelectedArrayList.size(); l++) {
            String timings = timingsSelectedArrayList.get(l).toString();
            if (startHour != 0) {
                if ((startHour >= 5 && startHour < 9) && (timings.equalsIgnoreCase("earlyAm"))) { //5Amto9Am
                    filterdworkerShiftsList.add(workerShiftsList.get(i));
                    earlyAmBn = true;
                } else if ((startHour >= 9 && startHour < 13) && (timings.equalsIgnoreCase("lateAm"))) { //9Am to 1pm
                    lateAmBn = true;
                    filterdworkerShiftsList.add(workerShiftsList.get(i));
                } else if ((startHour >= 13 && startHour < 17) && (timings.equalsIgnoreCase("afternoon"))) { //1pm to 5pm
                    afternoonBn = true;
                    filterdworkerShiftsList.add(workerShiftsList.get(i));
                } else if ((startHour >= 17 && startHour < 21) && (timings.equalsIgnoreCase("evening"))) {  //5pm to 9pm
                    eveningBn = true;
                    filterdworkerShiftsList.add(workerShiftsList.get(i));
                } else if ((startHour >= 21 && startHour <= 24) && (timings.equalsIgnoreCase("night"))) { //9pm to 12Am
                    nightBn = true;
                    filterdworkerShiftsList.add(workerShiftsList.get(i));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String removedShiftsList = data.getStringExtra(Extras_Keys.REMOVEDSHIFTS_LIST);
            int size = 0;
            try {
                JSONArray jsonArray = new JSONArray(removedShiftsList.toString());
                size = jsonArray.length();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        int pos = Integer.parseInt(jsonArray.get(i).toString());
                        for (int k = 0; k < filterdworkerShiftsList.size(); k++) {
                            if ((pos == filterdworkerShiftsList.get(k).getPosition())) {
                                filterdworkerShiftsList.get(k).setSelectedOrNot(false);
                            }
                        }
                        workerShiftsList.get(pos).setSelectedOrNot(false);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
            changeText();
        }
    }

    public void changeText() {
        countTv.setText("" + getSelectedShifts().size());
    }
}
