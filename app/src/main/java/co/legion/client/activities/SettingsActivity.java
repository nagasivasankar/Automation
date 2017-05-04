package co.legion.client.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import asynctasks.ResponseParserTask;
import base.Legion_BaseActivity;
import co.legion.client.R;
import helpers.CalendarHelper;
import interfaces.ResponseParserListener;
import models.Schedule;
import models.ScheduleWorkerShift;
import models.SelectCalendarModel;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 08-Feb-17.
 */
public class SettingsActivity extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback, ResponseParserListener {
    String calendarString;
    private ArrayList<Schedule> schedulesList = new ArrayList<>();
    private JSONObject responseObject;
    private ArrayList<ScheduleWorkerShift> workerShiftsList = new ArrayList<>();
    private Hashtable<String, String> calendarIdTable = new Hashtable<>();
    private TextView switchTv;
    private boolean onClickSettingsBoolean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        tvToolbarTile.setText("Settings");
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setImageResource(R.drawable.dismiss);
        switchTv = (TextView) findViewById(R.id.switchTv);
        LinearLayout showCalendarLL = (LinearLayout) findViewById(R.id.showCalendarLL);
        showCalendarLL.setOnClickListener(this);
        backImage.setOnClickListener(this);
        new LongOperation().execute("");
        if (prefsManager.hasKey(Prefs_Keys.SWITCH_CALENDAR) && prefsManager.getInt(Prefs_Keys.SWITCH_CALENDAR) == 1) {
            onClickSettingsBoolean = true;
            getCalendarIds();
        } else {
            switchTv.setText("Off");
        }

    }

    public void getCalendarIds() {
        if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
            calendarIdTable = CalendarHelper.listCalendarId(this);
            CalendarHelper.updateCalendarId(this, calendarIdTable);
            doLoadSchedules();
            switchTv.setText("On");
        } else {
            CalendarHelper.requestCalendarReadWritePermission(this);
        }
    }

    public void getPrimaryEmail() {
        if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
            ArrayList<SelectCalendarModel> calendarsArrayList = new ArrayList<>();
            if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
                Hashtable calendarsList = CalendarHelper.listCalendarId(this);
                if (calendarsList != null) {
                    Enumeration e = calendarsList.keys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        //  if (key.contains(".") && key.contains("@")) {
                        SelectCalendarModel model = new SelectCalendarModel();
                        model.setCalendarName(key);
                        if (prefsManager.hasKey(Prefs_Keys.CALENDAR_NAME) && (prefsManager.get(Prefs_Keys.CALENDAR_NAME)).equalsIgnoreCase(key)) {
                            model.setSelectedCalendar(true);
                        } else {
                            model.setSelectedCalendar(false);
                        }
                        calendarsArrayList.add(model);
                        //   }
                    }

                }
                prefsManager.save(Prefs_Keys.CALENDAR_NAME, calendarsArrayList.get(calendarsArrayList.size() - 1).getCalendarName());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbarBack) {
            onBackPressed();
        } else if (v.getId() == R.id.showCalendarLL) {
            if (CalendarHelper.haveCalendarReadWritePermissions(SettingsActivity.this)) {
                calendarActivity();
            } else {
                onClickSettingsBoolean = false;
                CalendarHelper.requestCalendarReadWritePermission(SettingsActivity.this);
            }
        }
    }

    private void doLoadSchedules() {
        if (LegionUtils.isOnline(this)) {
            Calendar cal = Calendar.getInstance();
            RequestParams params = new RequestParams();
            params.put("year", "2016"/*cal.get(Calendar.YEAR)*/);
            cal.add(Calendar.DATE, cal.get(Calendar.DAY_OF_WEEK) * -1);
            params.put("weekStartDayOfTheYear", 17/*cal.get(Calendar.DAY_OF_YEAR)*/);

            try {
                Legion_RestClient restClient = new Legion_RestClient(this, this);
                restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE, ServiceUrls.GET_SCHEDULES_URL, params, prefsManager.get(Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doParseSchedules(String response) {
        try {
            LegionUtils.hideProgressDialog();
            schedulesList.clear();
            responseObject = new JSONObject(response);
            String responseStatus = responseObject.optString("responseStatus");
            String errorString = responseObject.optString("errorString");
            if (responseStatus.equalsIgnoreCase("SUCCESS")) {

                JSONArray weeklyScheduleRatingsArray = responseObject.optJSONArray("weeklyScheduleRatings");
                if (weeklyScheduleRatingsArray != null && weeklyScheduleRatingsArray.length() >= 1) {
                    int size = weeklyScheduleRatingsArray.length();
                    for (int i = 0; i < size; ++i) {
                        JSONObject weekScheduleRating = weeklyScheduleRatingsArray.getJSONObject(i);
                        String startOfWeekDate = weekScheduleRating.optString("startOfWeekDate");
                        String endOfWeekDate = weekScheduleRating.optString("endOfWeekDate");
                        if (!LegionUtils.isPastWeek(startOfWeekDate.replace("T", " ").replace("Z", " "), endOfWeekDate.replace("T", " ").replace("Z", " "))) {
                            doLoadWeeklySchedules(startOfWeekDate);
                        }
                    }
                }
            } else {
                showToast(errorString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLoadWeeklySchedules(String startDate) {
        try {
            if (!LegionUtils.isOnline(this)) {
                return;
            }
            LegionUtils.hideKeyboard(this);
            Log.v("SESSION_ID", "" + prefsManager.get(Prefs_Keys.SESSION_ID));
            Calendar cal = Calendar.getInstance();
            RequestParams params = new RequestParams();
            String year = LegionUtils.getYearFromDate(startDate);
            params.put("year", year);
            params.put("weekStartDayOfTheYear", LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", startDate));
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_WEEKLY_SCHEDULES_REQUEST_CODE, ServiceUrls.GET_WEEK_SCHEDULES_URL, params, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
        } else if (requestCode == WebServiceRequestCodes.GET_WEEKLY_SCHEDULES_REQUEST_CODE) {
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
            new ResponseParserTask(ResponseParserConstants.PARSE_SCHEDULES, prefsManager, this).execute(response.toString());
        } else if (requestCode == WebServiceRequestCodes.GET_WEEKLY_SCHEDULES_REQUEST_CODE) {
            new ResponseParserTask(ResponseParserConstants.PARSE_WEEKLY_SHIFTS_OF_WORKER_RESPONSE, prefsManager, this).execute(response.toString());
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        Log.d("" + requestCode, reasonPhrase.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (prefsManager.hasKey(Prefs_Keys.SWITCH_CALENDAR) && prefsManager.getInt(Prefs_Keys.SWITCH_CALENDAR) == 1) {
            if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
                doLoadSchedules();
                switchTv.setText("On");
            }
        } else {
            switchTv.setText("Off");
        }
    }

    public void calendarActivity() {
        Intent calendarScheduleSettings = new Intent(SettingsActivity.this, CalendarSettingsActivity.class);
        startActivityForResult(calendarScheduleSettings, 78);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CalendarHelper.CALENDARHELPER_PERMISSION_REQUEST_CODE) {
            if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
                new LongOperation().execute("");
                if (onClickSettingsBoolean) {
                    getCalendarIds();
                } else {
                    calendarActivity();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResponseParsingStart(int parserId) {

    }

    @Override
    public void onResponseParsingComplete(int parserId, Object response) {
        if (parserId == ResponseParserConstants.PARSE_WEEKLY_SHIFTS_OF_WORKER_RESPONSE) {
            try {
                if (response instanceof Exception) {
                    ((Exception) response).printStackTrace();
                    LegionUtils.hideProgressDialog();
                } else if (response instanceof String) {
                    LegionUtils.showDialog(this, response.toString(), true);
                } else if (response instanceof ArrayList<?>) {
                    ArrayList<ScheduleWorkerShift> shiftsList = (ArrayList<ScheduleWorkerShift>) response;
                    Hashtable<String, String> calendarIdTable = CalendarHelper.listCalendarId(this);
                    LinkedHashMap<Integer, Boolean> dayNumbersToStatusMap = new LinkedHashMap<>();
                    dayNumbersToStatusMap.put(1, false);
                    dayNumbersToStatusMap.put(2, false);
                    dayNumbersToStatusMap.put(3, false);
                    dayNumbersToStatusMap.put(4, false);
                    dayNumbersToStatusMap.put(5, false);
                    dayNumbersToStatusMap.put(6, false);
                    dayNumbersToStatusMap.put(7, false);
                    for (int i = 0; i < shiftsList.size(); i++) {
                        dayNumbersToStatusMap.put(Integer.parseInt(shiftsList.get(i).getDayOfTheWeek()), true);
                        int calendarId = CalendarHelper.updateCalendarId(this, calendarIdTable);
                        if (calendarId >= 0) {
                            CalendarHelper.MakeNewCalendarEntry(shiftsList.get(i), this, calendarId);
                            Thread.sleep(100);
                        }
                    }
                    if (shiftsList.size() > 0) {
                        String startDate = LegionUtils.addDaysToDate(shiftsList.get(0).getShiftStartDate(), (Integer.parseInt(shiftsList.get(0).getDayOfTheWeek()) - 1));
                        int index = -1;
                        for (LinkedHashMap.Entry<Integer, Boolean> entry : dayNumbersToStatusMap.entrySet()) {
                            ++index;
                            if (!dayNumbersToStatusMap.get(entry.getKey())) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                Calendar calendar = Calendar.getInstance();
                                Date date = null;
                                try {
                                    date = formatter.parse(LegionUtils.addDaysToDate(startDate, index));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                calendar.setTime(date);
                                String newdt = formatter.format(calendar.getTime());
                                int calendarId = CalendarHelper.updateCalendarId(this, calendarIdTable);
                                CalendarHelper.getDeleteEventId(this, newdt, calendarId);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (parserId == ResponseParserConstants.PARSE_SCHEDULES) {
            if (response instanceof Exception) {
                ((Exception) response).printStackTrace();
                LegionUtils.hideProgressDialog();
            } else if (response instanceof String) {
                LegionUtils.showDialog(this, response.toString(), true);
            } else if (response instanceof ArrayList<?>) {
                try {
                    ArrayList<Schedule> shiftsList = (ArrayList<Schedule>) response;
                    for (int i = 0; i < shiftsList.size(); i++) {
                        String startOfWeekDate = shiftsList.get(i).getStartOfWeekDate();
                        String endOfWeekDate = shiftsList.get(i).getEndOfWeekDate();
                        if (!LegionUtils.isPastWeek(startOfWeekDate.replace("T", " ").replace("Z", " "), endOfWeekDate.replace("T", " ").replace("Z", " "))) {
                            doLoadWeeklySchedules(startOfWeekDate);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                if (!prefsManager.hasKey(Prefs_Keys.CALENDAR_NAME)) {
                    getPrimaryEmail();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }
}