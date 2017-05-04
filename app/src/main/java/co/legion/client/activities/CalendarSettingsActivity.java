package co.legion.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import asynctasks.ResponseParserTask;
import base.Legion_BaseActivity;
import co.legion.client.R;
import helpers.CalendarHelper;
import interfaces.ResponseParserListener;
import models.Schedule;
import models.ScheduleWorkerShift;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 07-Mar-17.
 */
public class CalendarSettingsActivity extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback, ResponseParserListener {
    private Switch enableSwitch;
    private TextView selectedcalendarNameTv;
    private TextView minutesTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_settings);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        tvToolbarTile.setText("Calendar");
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setOnClickListener(this);
        LinearLayout alertLL = (LinearLayout) findViewById(R.id.alertLL);
        alertLL.setOnClickListener(this);
        minutesTv = (TextView) findViewById(R.id.minutesTv);
        enableSwitch = (Switch) findViewById(R.id.enableSwitch);
        if (prefsManager.hasKey(Prefs_Keys.SWITCH_CALENDAR) && prefsManager.getInt(Prefs_Keys.SWITCH_CALENDAR) == 1) {
            enableSwitch.setChecked(true);
        }
        selectedcalendarNameTv = (TextView) findViewById(R.id.selectedcalendarNameTv);
        if (prefsManager.hasKey(Prefs_Keys.CALENDAR_NAME)) {
            selectedcalendarNameTv.setText(prefsManager.get(Prefs_Keys.CALENDAR_NAME));
        }
        if (prefsManager.hasKey(Prefs_Keys.SELECTED_TIME)) {
            minutesTv.setText(prefsManager.get(Prefs_Keys.SELECTED_TIME));
        } else {
            minutesTv.setText("None");
        }
        LinearLayout seelctCalLl = (LinearLayout) findViewById(R.id.seelctCalLl);
        seelctCalLl.setOnClickListener(this);
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (CalendarHelper.haveCalendarReadWritePermissions(CalendarSettingsActivity.this)) {
                        prefsManager.saveInt(Prefs_Keys.SWITCH_CALENDAR, 1);
                        doLoadSchedules();
                    } else {
                        CalendarHelper.requestCalendarReadWritePermission(CalendarSettingsActivity.this);
                    }
                } else {
                    prefsManager.saveInt(Prefs_Keys.SWITCH_CALENDAR, 0);
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CalendarHelper.CALENDARHELPER_PERMISSION_REQUEST_CODE) {
            if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
                enableSwitch.setChecked(true);
            } else {
                enableSwitch.setChecked(false);
                Toast.makeText(CalendarSettingsActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbarBack) {
            finish();
        } else if (v.getId() == R.id.seelctCalLl) {
            Intent selectCalendarActivity = new Intent(CalendarSettingsActivity.this, SelectCalendarActivity.class);
            startActivityForResult(selectCalendarActivity, 210);
        } else if (v.getId() == R.id.alertLL) {
            Intent alertActivity = new Intent(CalendarSettingsActivity.this, AlertActivity.class);
            startActivityForResult(alertActivity, 220);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 210) && (prefsManager.hasKey(Prefs_Keys.CALENDAR_NAME))) {
            selectedcalendarNameTv.setText(prefsManager.get(Prefs_Keys.CALENDAR_NAME));
        } else if ((requestCode == 220) && (prefsManager.hasKey(Prefs_Keys.SELECTED_TIME))) {
            minutesTv.setText(prefsManager.get(Prefs_Keys.SELECTED_TIME));
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
}
