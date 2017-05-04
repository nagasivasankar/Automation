package co.legion.client.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import base.Legion_BaseActivity;
import co.legion.client.R;
import fragments.MyAvailabilityFragmentNew;
import models.Slot;
import models.SlotType;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 2/19/2017.
 */
public class UpdateAvailabilityActivityNew extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback, Legion_Constants {

    public ArrayList<Slot> mondaySlotsList = new ArrayList<>();
    public ArrayList<Slot> tuesdaySlotsList = new ArrayList<>();
    public ArrayList<Slot> wednesdaySlotsList = new ArrayList<>();
    public ArrayList<Slot> thursdaySlotsList = new ArrayList<>();
    public ArrayList<Slot> fridaySlotsList = new ArrayList<>();
    public ArrayList<Slot> saturdaySlotsList = new ArrayList<>();
    public ArrayList<Slot> sundaySlotsList = new ArrayList<>();

    private TextView buttonWhenIamBusy, buttonWhenIPreferTowork;
    private MyAvailabilityFragmentNew availabilityFragment;
    private long lastClickTime;
    private TextView saveAvailability;
    private TextView headerDateTv;
    private ImageView prevIv, nextIv;
    private String endWeekDate;
    private String startWeekDate;
    private String weekStartDayOfTheYear;
    private String AVLBTYSTARTDATE;
    private String AVLBTYENDDATE;
    private LinearLayout toolTipLayout;
    private TextView gotItTv;
    private boolean isScheduleLocked;
    private boolean isPrevWeekClick;
    private boolean isNextWeekClick;
    private ImageView lockImage;
    private boolean doFinishActivity;
    private boolean isOnResumeCalled;
    private boolean isAvailabilityMode = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);
        headerDateTv = (TextView) findViewById(R.id.headerDateTv);
        lockImage = (ImageView) findViewById(R.id.lockImage);
        LegionUtils.doApplyFont(getAssets(), (RelativeLayout) findViewById(R.id.parentLayout));
        saveAvailability = (TextView) findViewById(R.id.saveAvailability);
        toolTipLayout = (LinearLayout) findViewById(R.id.toolTipLayout);
        gotItTv = (TextView) findViewById(R.id.gotItTv);
        if (prefsManager.getBoolean(Prefs_Keys.FIRST_TIME)) {
            toolTipLayout.setVisibility(View.VISIBLE);
        } else {
            toolTipLayout.setVisibility(View.GONE);
        }
        gotItTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipLayout.setVisibility(View.GONE);
                prefsManager.saveBoolean(Prefs_Keys.FIRST_TIME, false);
            }
        });
        saveAvailability.setOnClickListener(this);
        ImageButton navigationClose = (ImageButton) findViewById(R.id.closeSetup);
        nextIv = (ImageView) findViewById(R.id.nextIv);
        prevIv = (ImageView) findViewById(R.id.prevIv);
        prevIv.setOnClickListener(this);
        nextIv.setOnClickListener(this);
        navigationClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveAvailability.getCurrentTextColor() == Color.WHITE) {
                    showDiscardAlert(false, false, true);
                } else {
                    finish();
                }
            }
        });
        init();
        buttonWhenIamBusy.setOnClickListener(this);
        buttonWhenIPreferTowork.setOnClickListener(this);
        setupMondaySlotsNew(mondaySlotsList);
        setupTuesdaySlotsNew(tuesdaySlotsList);
        setupWednesdaySlotsNew(wednesdaySlotsList);
        setupThursdaySlotsNew(thursdaySlotsList);
        setupFridaySlotsNew(fridaySlotsList);
        setupSaturdaySlotsNew(saturdaySlotsList);
        setupSundaySlotsNew(sundaySlotsList);

        ArrayList avbtyDates = LegionUtils.getDates();
        AVLBTYSTARTDATE = avbtyDates.get(0).toString();
        AVLBTYENDDATE = avbtyDates.get(1).toString();
        setDate(AVLBTYSTARTDATE, AVLBTYENDDATE);

        doLoadAvailabilityPreferences(prefsManager.get(Prefs_Keys.FIRST_NAME), prefsManager.get(Prefs_Keys.LAST_NAME), prefsManager.get(Prefs_Keys.EMAIL), prefsManager.get(Prefs_Keys.PHONE_NUMBER));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        availabilityFragment = new MyAvailabilityFragmentNew(saveAvailability, isScheduleLocked, isPastDate(AVLBTYENDDATE), isAvailabilityMode, false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container1, availabilityFragment).commit();
                        return null;
                    }
                }.execute();
            }
        }, 100);
    }

    private void init() {
        buttonWhenIamBusy = (TextView) findViewById(R.id.whenIamBusy);
        buttonWhenIPreferTowork = (TextView) findViewById(R.id.whenIPreferToWork);
    }

    @Override
    public void onClick(View v) {
        if ((System.currentTimeMillis() - lastClickTime) < 50) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        switch (v.getId()) {
            case R.id.whenIPreferToWork:
                isAvailabilityMode = true;
                buttonWhenIPreferTowork.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_availability_on, 0, 0);
                buttonWhenIamBusy.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.unavailability_off, 0, 0);
                buttonWhenIPreferTowork.setTextColor(Color.parseColor("#66BB6A"));
                buttonWhenIamBusy.setTextColor(Color.parseColor("#A2A5A9"));
                if (availabilityFragment == null) {
                    return;
                }
                availabilityFragment.doUpdateSlots(isAvailabilityMode = true, isScheduleLocked, isPastDate(AVLBTYENDDATE));
                break;

            case R.id.whenIamBusy:
                isAvailabilityMode = false;
                buttonWhenIPreferTowork.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_availability_off, 0, 0);
                buttonWhenIamBusy.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_unavailability_on, 0, 0);
                buttonWhenIPreferTowork.setTextColor(Color.parseColor("#A2A5A9"));
                buttonWhenIamBusy.setTextColor(Color.parseColor("#D02E2E"));
                if (availabilityFragment == null) {
                    return;
                }
                availabilityFragment.doUpdateSlots(isAvailabilityMode = false, isScheduleLocked, isPastDate(AVLBTYENDDATE));
                break;

            case R.id.saveAvailability:
                if (saveAvailability.getCurrentTextColor() == Color.WHITE) {
                    if (!LegionUtils.isOnline(this)) {
                        LegionUtils.showOfflineDialog(this);
                        return;
                    }
                    showSaveAlert(false, false, false);
                }
                break;

            case R.id.nextIv:
                if (saveAvailability.getCurrentTextColor() == Color.WHITE) {
                    showDiscardAlert(false, true, false);
                } else {
                    onNextWeekClick();
                }
                break;

            case R.id.prevIv:
                if (saveAvailability.getCurrentTextColor() == Color.WHITE) {
                    showDiscardAlert(true, false, false);
                } else {
                    onPrevWeekClick();
                }
                break;
        }
    }

    private void onNextWeekClick() {
        startWeekDate = LegionUtils.getOneDayPlus(AVLBTYENDDATE);
        endWeekDate = LegionUtils.getEndDate(startWeekDate);
        setDate(startWeekDate, endWeekDate);
        doLoadAvailabilityPreferences(prefsManager.get(Prefs_Keys.FIRST_NAME), prefsManager.get(Prefs_Keys.LAST_NAME), prefsManager.get(Prefs_Keys.EMAIL), prefsManager.get(Prefs_Keys.PHONE_NUMBER));
    }

    private void onPrevWeekClick() {
        endWeekDate = LegionUtils.getOneDayMinus(AVLBTYSTARTDATE);
        startWeekDate = LegionUtils.getStartDate(endWeekDate);
        setDate(startWeekDate, endWeekDate);
        doLoadAvailabilityPreferences(prefsManager.get(Prefs_Keys.FIRST_NAME), prefsManager.get(Prefs_Keys.LAST_NAME), prefsManager.get(Prefs_Keys.EMAIL), prefsManager.get(Prefs_Keys.PHONE_NUMBER));
    }

    private void showDiscardAlert(final boolean isPrev, final boolean isNext, final boolean doFinishActivity) {
        try {
            this.isNextWeekClick = isNext;
            this.isPrevWeekClick = isPrev;
            this.doFinishActivity = doFinishActivity;

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.popup_availability_discard_alert_new);
            final ImageView repeatImage = (ImageView) dialog.findViewById(R.id.repeatImage);
            final TextView discardChangesButton = (TextView) dialog.findViewById(R.id.discardChangesButton);
            final TextView saveChangesButton = (TextView) dialog.findViewById(R.id.saveChangesButton);
            final TextView cancelButton = (TextView) dialog.findViewById(R.id.cancelButton);
            final TextView applyChangesTV = (TextView) dialog.findViewById(R.id.tv_apply);
            applyChangesTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (repeatImage.getTag().toString().equals("on")) {
                        repeatImage.setImageDrawable(null);
                        repeatImage.setBackgroundResource(R.drawable.bg_white_circle_shifts);
                        repeatImage.setTag("off");
                    } else {
                        repeatImage.setImageResource(R.drawable.ic_save_check_mark);
                        repeatImage.setBackgroundResource(R.drawable.bg_blue_circle_shifts);
                        repeatImage.setTag("on");
                    }
                }
            });
            repeatImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (repeatImage.getTag().toString().equals("on")) {
                        repeatImage.setImageDrawable(null);
                        repeatImage.setBackgroundResource(R.drawable.bg_white_circle_shifts);
                        repeatImage.setTag("off");
                    } else {
                        repeatImage.setImageResource(R.drawable.ic_save_check_mark);
                        repeatImage.setBackgroundResource(R.drawable.bg_blue_circle_shifts);
                        repeatImage.setTag("on");
                    }
                }
            });
            saveChangesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (repeatImage.getTag().toString().equals("on")) {
                        doUpdateAvailabilityPrefs("FromWeek", isPrev, isNext, doFinishActivity);
                    } else {
                        doUpdateAvailabilityPrefs("ForWeek", isPrev, isNext, doFinishActivity);
                    }
                }
            });
            discardChangesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (doFinishActivity) {
                        finish();
                    } else {
                        saveAvailability.setEnabled(false);
                        saveAvailability.setTextColor(ActivityCompat.getColor(UpdateAvailabilityActivityNew.this, R.color.swap_text_color));
                        if (isPrev) {
                            onPrevWeekClick();
                        } else {
                            onNextWeekClick();
                        }
                        //onSuccess(WebServiceRequestCodes.QUERY_AVAILABILITY_PREFRENCE_CODE, prefsManager.get(Prefs_Keys_Offline.AVALIABILTY_PREFS), null);
                    }
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            LegionUtils.doApplyFont(getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSaveAlert(final boolean isPrev, final boolean isNext, final boolean doFinishActivity) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.popup_availability_save_alert);
            final TextView thisWeekOnlyButton = (TextView) dialog.findViewById(R.id.thisWeekOnlyButton);
            final TextView repeatForwardButton = (TextView) dialog.findViewById(R.id.repeatForwardButton);
            final TextView cancelButton = (TextView) dialog.findViewById(R.id.cancelButton);
            repeatForwardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    doUpdateAvailabilityPrefs("FromWeek", isPrev, isNext, doFinishActivity);
                }
            });
            thisWeekOnlyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    doUpdateAvailabilityPrefs("ForWeek", isPrev, isNext, doFinishActivity);
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            LegionUtils.doApplyFont(getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLoadAvailabilityPreferences(String firstName, String lastName, String email, String phoneNumber) {
        try {
            if (!LegionUtils.isOnline(this)) {
                if (prefsManager.hasKey(Prefs_Keys_Offline.AVALIABILTY_PREFS)) {
                    onSuccess(WebServiceRequestCodes.QUERY_AVAILABILITY_PREFRENCE_CODE, prefsManager.get(Prefs_Keys_Offline.AVALIABILTY_PREFS), null);
                    return;
                } else {
                    LegionUtils.showOfflineDialog(this);
                    return;
                }
            }
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            String year = LegionUtils.getYearFromDate(AVLBTYSTARTDATE);
            reqObject.put("year", year);
            reqObject.put("workerId", prefsManager.get(Prefs_Keys.WORKER_ID));
            reqObject.put("businessId", prefsManager.get(Prefs_Keys.BUSINESS_ID));
            weekStartDayOfTheYear = LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", AVLBTYSTARTDATE);
            reqObject.put("weekStartDayOfTheYear", Integer.parseInt(weekStartDayOfTheYear) + 1); // Adding +1 as we need to pass weekStartDayOfTheYear as MONDAY, but not SUNDAY..
            restClient.performHTTPGetRequest(WebServiceRequestCodes.QUERY_AVAILABILITY_PREFRENCE_CODE, ServiceUrls.QUERY_AVAILALIABLITY_URL, reqObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.QUERY_AVAILABILITY_PREFRENCE_CODE) {
            LegionUtils.showProgressDialog(this);
        } else if (requestCode == WebServiceRequestCodes.UPDATE_AVAILABILITY) {
            LegionUtils.showProgressDialog(this);
        }
    }

    @Override
    public void onSuccess(int requestCode, final Object response, final Object headers) {
        if (requestCode == WebServiceRequestCodes.QUERY_AVAILABILITY_PREFRENCE_CODE) {
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.getString("responseStatus").equals("SUCCESS")) {
                    doParseAvailabilityPrefs(jsonObject);
                    prefsManager.save(Prefs_Keys_Offline.AVALIABILTY_PREFS, response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Something went wrong.");
            }
            LegionUtils.hideProgressDialog();
            lockImage.setVisibility((isScheduleLocked || isPastDate(AVLBTYENDDATE)) ? View.VISIBLE : View.GONE);
            headerDateTv.setTextColor(lockImage.getVisibility() == View.VISIBLE ? Color.parseColor("#A2A5A9") : Color.parseColor("#31397C"));

            if (isAvailabilityMode) {
                buttonWhenIPreferTowork.performClick();
            } else {
                buttonWhenIamBusy.performClick();
            }
        } else if (requestCode == WebServiceRequestCodes.UPDATE_AVAILABILITY) {
            LegionUtils.hideProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.getString("responseStatus").equals("SUCCESS")) {
                    saveAvailability.setEnabled(false);
                    saveAvailability.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));

                    if (doFinishActivity) {
                        finish();
                    } else if (!isNextWeekClick && !isPrevWeekClick) {
                        LegionUtils.showMessageDialog(this, "Your Availability has been saved.", R.drawable.confirmation);
                        saveAvailability.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
                    } else {
                        if (isNextWeekClick) {
                            onNextWeekClick();
                        } else if (isPrevWeekClick) {
                            onPrevWeekClick();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(UpdateAvailabilityActivityNew.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
        if (reasonPhrase == null) {
            return;
        }
        if (reasonPhrase.contains("Something went wrong")) {
            LegionUtils.doLogout(this);
            return;
        }

        if (reasonPhrase != null) {
            LegionUtils.showDialog(this, reasonPhrase, true);
        }
    }

    private void doParseAvailabilityPrefs(JSONObject jsonObject) {
        try {
            JSONObject availabilityJsonObj = jsonObject.getJSONObject("availability");
            final String calendarOption = availabilityJsonObj.optString("calendarOption");
            isScheduleLocked = availabilityJsonObj.optBoolean("isScheduleLocked");
            JSONArray slotsJsonArray = availabilityJsonObj.getJSONArray("slot");

            doClearSlots(mondaySlotsList);
            doClearSlots(tuesdaySlotsList);
            doClearSlots(wednesdaySlotsList);
            doClearSlots(thursdaySlotsList);
            doClearSlots(fridaySlotsList);
            doClearSlots(saturdaySlotsList);
            doClearSlots(sundaySlotsList);

            if (slotsJsonArray != null && slotsJsonArray.length() >= 1) {
                int size = slotsJsonArray.length();
                for (int i = 0; i < size; ++i) {
                    JSONObject slotJsonObj = slotsJsonArray.getJSONObject(i);
                    int dayOfWeek = slotJsonObj.getInt("dayOfTheWeek");
                    int endMins = slotJsonObj.getInt("endMinutes");
                    int startMins = slotJsonObj.getInt("startMinutes");
                    boolean positiveFlag = slotJsonObj.getBoolean("positiveFlag");
                    boolean isPTO = slotJsonObj.optBoolean("isPTO", false);

                    if (dayOfWeek == 1) {
                        doUpdateSlotsWithServerData(sundaySlotsList, startMins, endMins, positiveFlag, isPTO);
                    } else if (dayOfWeek == 2) {
                        doUpdateSlotsWithServerData(mondaySlotsList, startMins, endMins, positiveFlag, isPTO);
                    } else if (dayOfWeek == 3) {
                        doUpdateSlotsWithServerData(tuesdaySlotsList, startMins, endMins, positiveFlag, isPTO);
                    } else if (dayOfWeek == 4) {
                        doUpdateSlotsWithServerData(wednesdaySlotsList, startMins, endMins, positiveFlag, isPTO);
                    } else if (dayOfWeek == 5) {
                        doUpdateSlotsWithServerData(thursdaySlotsList, startMins, endMins, positiveFlag, isPTO);
                    } else if (dayOfWeek == 6) {
                        doUpdateSlotsWithServerData(fridaySlotsList, startMins, endMins, positiveFlag, isPTO);
                    } else if (dayOfWeek == 7) {
                        doUpdateSlotsWithServerData(saturdaySlotsList, startMins, endMins, positiveFlag, isPTO);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPastDate(String inputDateString) {
        SimpleDateFormat format = new SimpleDateFormat(LegionUtils.DATE_FORMAT);
        try {
            Date inputDate = format.parse(inputDateString);
            Date today = new Date();
            if (inputDate.before(today)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void doClearSlots(ArrayList<Slot> slotsList) {
        int size = slotsList.size();
        for (int j = 0; j < size; ++j) {
            Slot slot = slotsList.get(j);
            slot.setSlotType(SlotType.EMPTY);
        }
    }

    private void doUpdateSlotsWithServerData(ArrayList<Slot> slotsList, int startMins, int endMins, boolean positiveFlag, boolean isPTO) {
        int size = slotsList.size();
        for (int j = 0; j < size; ++j) {
            Slot slot = slotsList.get(j);
            slot.setIsPTO(isPTO);
            if (slot.getActualStartTimeOfSlot() >= startMins && slot.getActualEndTimeOfSlot() <= endMins && positiveFlag) {
                slot.setSlotType(SlotType.GREEN);
            } else if (slot.getActualStartTimeOfSlot() >= startMins && slot.getActualEndTimeOfSlot() <= endMins && !positiveFlag) {
                slot.setSlotType(SlotType.RED);
            }
        }
    }

    private void doUpdateAvailabilityPrefs(String repeatForward, boolean isPrev, boolean isNext, boolean doFinishActivity) {
        try {
            this.isPrevWeekClick = isPrev;
            this.isNextWeekClick = isNext;
            this.doFinishActivity = doFinishActivity;

            JSONObject availabilityJsonObj = new JSONObject();
            /*availabilityJsonObj.put("firstName", prefsManager.get(Prefs_Keys.FIRST_NAME));
            availabilityJsonObj.put("lastName", prefsManager.get(Prefs_Keys.LAST_NAME));
            availabilityJsonObj.put("email", prefsManager.get(Prefs_Keys.EMAIL));
            availabilityJsonObj.put("phoneNumber", prefsManager.get(Prefs_Keys.PHONE_NUMBER));*/
            String year = LegionUtils.getYearFromDate(AVLBTYSTARTDATE);
            availabilityJsonObj.put("year", year);
            availabilityJsonObj.put("weekStartDayOfTheYear", Integer.parseInt(weekStartDayOfTheYear) + 1); // Adding +1 as we need to pass weekStartDayOfTheYear as MONDAY, but not SUNDAY..
            availabilityJsonObj.put("calendarOption", repeatForward);
            availabilityJsonObj.put("workerId", prefsManager.get(Prefs_Keys.WORKER_ID));
            availabilityJsonObj.put("businessId", prefsManager.get(Prefs_Keys.BUSINESS_ID));
            availabilityJsonObj.put("isScheduleLocked", isScheduleLocked);
            JSONArray slotJsonArray = new JSONArray();

            putSlotAvailabilityIntoJsonArray(sundaySlotsList, slotJsonArray, 1);
            putSlotAvailabilityIntoJsonArray(mondaySlotsList, slotJsonArray, 2);
            putSlotAvailabilityIntoJsonArray(tuesdaySlotsList, slotJsonArray, 3);
            putSlotAvailabilityIntoJsonArray(wednesdaySlotsList, slotJsonArray, 4);
            putSlotAvailabilityIntoJsonArray(thursdaySlotsList, slotJsonArray, 5);
            putSlotAvailabilityIntoJsonArray(fridaySlotsList, slotJsonArray, 6);
            putSlotAvailabilityIntoJsonArray(saturdaySlotsList, slotJsonArray, 7);

            availabilityJsonObj.put("slot", slotJsonArray);

            //LegionUtils.showProgressDialog(this);
            //LegionUtils.hideKeyboard(this);
            try {
                Legion_RestClient restClient = new Legion_RestClient(this, this);
                restClient.performPostRequest(WebServiceRequestCodes.UPDATE_AVAILABILITY, ServiceUrls.UPDATE_AVAILABILITY_PREFERENCES, availabilityJsonObj, prefsManager.get(Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putSlotAvailabilityIntoJsonArray(ArrayList<Slot> slotsList, JSONArray slotJsonArray, int dayOfWeek) throws Exception {
        boolean hasDayPTO = false;

        for (Slot slot : slotsList) {
            if (slot.isPTO()) {
                hasDayPTO = true;
                break;
            }
        }
        if (hasDayPTO) {
            return;
        }


        for (int i = 0; i < slotsList.size(); ++i) {
            Slot slot = slotsList.get(i);
            if (slot.getSlotType().getType().equals(SlotType.GREEN.getType())) {
                JSONObject slotJsonObj = new JSONObject();
                slotJsonObj.put("positiveFlag", true);
                slotJsonObj.put("dayOfTheWeek", dayOfWeek);
                slotJsonObj.put("startMinutes", slot.getActualStartTimeOfSlot());
                Slot sameTypeLastSlot = getSameTypeLastSlotInRow(slotsList, slot);
                slotJsonObj.put("endMinutes", sameTypeLastSlot.getActualEndTimeOfSlot());
                i = sameTypeLastSlot.getSlotSerialNumber();
                slotJsonArray.put(slotJsonObj);
            } else if (slot.getSlotType().getType().equals(SlotType.RED.getType())) {
                JSONObject slotJsonObj = new JSONObject();
                slotJsonObj.put("positiveFlag", false);
                slotJsonObj.put("dayOfTheWeek", dayOfWeek);
                slotJsonObj.put("startMinutes", slot.getActualStartTimeOfSlot());
                Slot sameTypeLastSlot = getSameTypeLastSlotInRow(slotsList, slot);
                slotJsonObj.put("endMinutes", sameTypeLastSlot.getActualEndTimeOfSlot());
                i = sameTypeLastSlot.getSlotSerialNumber();
                slotJsonArray.put(slotJsonObj);
            }
        }
    }

    private Slot getSameTypeLastSlotInRow(ArrayList<Slot> slotsList, Slot initialSlot) {
        if (initialSlot.getSlotSerialNumber() == slotsList.size() - 1) {
            return slotsList.get(slotsList.size() - 1);
        }

        for (int i = initialSlot.getSlotSerialNumber(); i < slotsList.size(); ++i) {
            Slot nextSlot = slotsList.get(i);
            if (initialSlot.getSlotType().getType().equals(nextSlot.getSlotType().getType())) {
                if (i >= 36) {  //if we reach END then just return the last element
                    return slotsList.get(slotsList.size() - 1);
                }
            } else {
                return slotsList.get(i - 1);
            }
        }
        return slotsList.get(1 + initialSlot.getSlotSerialNumber());
    }

   /* @Override
    protected void onResume() {
        super.onResume();
        if (isOnResumeCalled) {
            buttonWhenIamBusy.performClick();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttonWhenIPreferTowork.performClick();
                }
            }, 60);
        }
        isOnResumeCalled = true;
    }*/

    @Override
    public void onBackPressed() {
        if (saveAvailability.getCurrentTextColor() == Color.WHITE) {
            showDiscardAlert(false, false, true);
        } else {
            finish();
        }
    }

    public void setupMondaySlotsNew(ArrayList<Slot> mondaySlotsList) {

        int j = 0;
        for (int i = 240; i < 1440; i += OnBoardingActivity.INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + OnBoardingActivity.INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + OnBoardingActivity.INTERVAL_MINS);
            slot.setDayOfTheWeek(1);
            mondaySlotsList.add(slot);
        }
    }

    public void setupTuesdaySlotsNew(ArrayList<Slot> tuesdaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += OnBoardingActivity.INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + OnBoardingActivity.INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + OnBoardingActivity.INTERVAL_MINS);
            slot.setDayOfTheWeek(2);
            tuesdaySlotsList.add(slot);
        }
    }

    public void setupWednesdaySlotsNew(ArrayList<Slot> wednesdaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += OnBoardingActivity.INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + OnBoardingActivity.INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + OnBoardingActivity.INTERVAL_MINS);
            slot.setDayOfTheWeek(3);
            wednesdaySlotsList.add(slot);
        }
    }

    public void setupThursdaySlotsNew(ArrayList<Slot> thursdaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += OnBoardingActivity.INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + OnBoardingActivity.INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + OnBoardingActivity.INTERVAL_MINS);
            slot.setDayOfTheWeek(4);
            thursdaySlotsList.add(slot);
        }
    }

    public void setupFridaySlotsNew(ArrayList<Slot> fridaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += OnBoardingActivity.INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + OnBoardingActivity.INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + OnBoardingActivity.INTERVAL_MINS);
            slot.setDayOfTheWeek(5);
            fridaySlotsList.add(slot);
        }
    }

    public void setupSaturdaySlotsNew(ArrayList<Slot> saturdaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += OnBoardingActivity.INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + OnBoardingActivity.INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + OnBoardingActivity.INTERVAL_MINS);
            slot.setDayOfTheWeek(6);
            saturdaySlotsList.add(slot);
        }
    }

    public void setupSundaySlotsNew(ArrayList<Slot> sundaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += OnBoardingActivity.INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + OnBoardingActivity.INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + OnBoardingActivity.INTERVAL_MINS);
            slot.setDayOfTheWeek(7);
            sundaySlotsList.add(slot);
        }
    }

    private String doConvertMinsToTime(int intputTimeInMins) {
        int mins = intputTimeInMins % 60;
        int hrs = intputTimeInMins / 60;

        String minString, hrsString = String.valueOf(hrs);
        if (mins == 0) {
            minString = "";
        } else if (mins <= 9) {
            minString = ":0" + mins;
        } else {
            minString = ":" + String.valueOf(mins);
        }

        if (hrs == 24) {
            return "12" + minString + "am";
        } else if (hrs == 12) {
            return hrsString + minString + "pm";
        } else if (hrs < 12) {
            return hrsString + minString + "am";
        } else {
            return (hrs - 12) + minString + "pm";
        }
    }

    public void setDate(String startOfWeekDate1, String endOfWeekDate1) {
        AVLBTYSTARTDATE = startOfWeekDate1;
        AVLBTYENDDATE = endOfWeekDate1;
        String startDate = LegionUtils.getWeekDate(startOfWeekDate1.replace("T", " "));
        String endDate = LegionUtils.getWeekDate(endOfWeekDate1.replace("T", " "));
        headerDateTv.setText(startDate + " - " + endDate);
    }
}