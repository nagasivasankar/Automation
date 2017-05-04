package tabs;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import asynctasks.ResponseParserTask;
import base.Legion_BaseFragment;
import co.legion.client.R;
import co.legion.client.activities.HomeActivity;
import co.legion.client.activities.ScheduleShiftDetailsActivity;
import interfaces.ResponseParserListener;
import models.AssociatedWorker;
import models.BusinessKey;
import models.NotificationType;
import models.ScheduleWorkerShift;
import models.WorkerKey;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 11/22/2016.
 */
public class HomeTabFragment extends Legion_BaseFragment implements Legion_NetworkCallback, View.OnClickListener, ResponseParserListener {

    private static final int RC_CURRENT_WEEK_SHIFTS = 222;
    private static final int RC_LAST_WEEK_SHIFTS = 223;
    private static final int RC_NEXT_WEEK_SHIFTS = 224;
    private static final int RC_NOTIFICATION_WEEK_SHIFTS = 225;
    boolean lastWeekLoaded, currentWeekLoaded, nextWeekLoaded;
    private TextView schedules_count_tv;
    private TextView messages_count_tv;
    private TextView shift_offers_count_tv;
    private TextView prevDay4TimeTV;
    private TextView prevDay3TimeTV;
    private TextView prevDay2TimeTV;
    private TextView currentDayTimeTV;
    private TextView nextDay4TimeTV;
    private TextView nextDay3TimeTV;
    private TextView nextDay2TimeTV;
    private TextView nextDay1TimeTV;
    private TextView prevDay1TimeTV;
    private TextView prevDay4TV;
    private TextView prevDay3TV;
    private TextView nextDay3TV;
    private TextView nextDay1TV;
    private TextView prevDay1TV;
    private TextView prevDay2TV;
    private TextView currentDayTV;
    private TextView nextDay2TV;
    private TextView nextDay4TV;
    private LineChart lineChart;
    private TextView noShiftsTV;
    private TextView shift_day_with_time_tv;
    private TextView dayWishesTV;
    private String shiftStartDate;
    private String shiftEndDate;
    private ArrayList<ScheduleWorkerShift> workerShiftsList = new ArrayList<>();
    private HashMap<String, String> dateToShiftsCountMap = new HashMap<>();
    private ArrayList<String> shiftStartDatesList = new ArrayList<>();
    int shiftoffercount = 0, swapoffercount = 0;
    private int weeklySchedulesMins;

    public HomeTabFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //setupToolbar(view, false, "Home");
        lastWeekLoaded = false;
        currentWeekLoaded = false;
        nextWeekLoaded = false;

        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));

        lineChart = (LineChart) view.findViewById(R.id.graphView);
        schedules_count_tv = (TextView) view.findViewById(R.id.schedule_count_tv);
        messages_count_tv = (TextView) view.findViewById(R.id.messages_count_tv);
        shift_offers_count_tv = (TextView) view.findViewById(R.id.shift_offers_count_tv);

        LinearLayout schedulesBoxLL = (LinearLayout) view.findViewById(R.id.schedulesBoxLL);
        LinearLayout messagesBoxLL = (LinearLayout) view.findViewById(R.id.messagesBoxLL);
        LinearLayout shiftsBoxLL = (LinearLayout) view.findViewById(R.id.shiftOffersBoxLL);
        schedulesBoxLL.setOnClickListener(this);
        messagesBoxLL.setOnClickListener(this);
        shiftsBoxLL.setOnClickListener(this);

        prevDay4TimeTV = (TextView) view.findViewById(R.id.prevDay4TimeTV);
        prevDay3TimeTV = (TextView) view.findViewById(R.id.prevDay3TimeTV);
        prevDay2TimeTV = (TextView) view.findViewById(R.id.prevDay2TimeTV);
        prevDay1TimeTV = (TextView) view.findViewById(R.id.prevDay1TimeTV);
        currentDayTimeTV = (TextView) view.findViewById(R.id.currentDayTimeTV);
        nextDay1TimeTV = (TextView) view.findViewById(R.id.nextDay1TimeTV);
        nextDay2TimeTV = (TextView) view.findViewById(R.id.nextDay2TimeTV);
        nextDay3TimeTV = (TextView) view.findViewById(R.id.nextDay3TimeTV);
        nextDay4TimeTV = (TextView) view.findViewById(R.id.nextDay4TimeTV);

        prevDay4TV = (TextView) view.findViewById(R.id.prevDay4TV);
        prevDay3TV = (TextView) view.findViewById(R.id.prevDay3TV);
        prevDay2TV = (TextView) view.findViewById(R.id.prevDay2TV);
        prevDay1TV = (TextView) view.findViewById(R.id.prevDay1TV);
        currentDayTV = (TextView) view.findViewById(R.id.currentDayTV);
        nextDay1TV = (TextView) view.findViewById(R.id.nextDay1TV);
        nextDay2TV = (TextView) view.findViewById(R.id.nextDay2TV);
        nextDay3TV = (TextView) view.findViewById(R.id.nextDay3TV);
        nextDay4TV = (TextView) view.findViewById(R.id.nextDay4TV);
        noShiftsTV = (TextView) view.findViewById(R.id.noShiftsTV);
        shift_day_with_time_tv = (TextView) view.findViewById(R.id.shift_day_with_time);

        setupDaysLabels();

        TextView todayTV = (TextView) view.findViewById(R.id.todayTV);
        dayWishesTV = (TextView) view.findViewById(R.id.dayWishesTV);
        updateNameAndWishes();
        todayTV.setText(getTodayDetails());
        doInitialSetupOfGraph();

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Extras_Keys.TYPE)) {
            String type = intent.getStringExtra(Extras_Keys.TYPE);
            if (type.equalsIgnoreCase(NotificationType.SHIFT_UPDATED.getType())) {
                if (intent.hasExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR) && intent.hasExtra(Extras_Keys.NOTIFICATION_YEAR) && intent.hasExtra(Extras_Keys.NOTIFICATION_SHIFTID)) {
                    doLoadWeeklySchedulesOfWorker(RC_NOTIFICATION_WEEK_SHIFTS, Integer.parseInt(intent.getStringExtra(Extras_Keys.NOTIFICATION_YEAR)), Integer.parseInt(intent.getStringExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR)));
                    intent.removeExtra(Extras_Keys.TYPE);
                }
            }
        }
    }

    private void doInitialSetupOfGraph() {
        //Initial setup of GRAPH
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisLeft.setLabelCount(0, false);
        yAxisRight.setLabelCount(0, false);
        yAxisRight.setStartAtZero(false);
        yAxisLeft.setStartAtZero(false);
        yAxisLeft.setDrawTopYLabelEntry(false);
        yAxisRight.setDrawTopYLabelEntry(false);
        yAxisRight.setAxisMaxValue(12);
        yAxisLeft.setAxisMaxValue(12);
        yAxisRight.setEnabled(false);
        xAxis.setEnabled(false);
        yAxisLeft.setEnabled(false);
        xAxis.setDrawAxisLine(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setAxisMaxValue(12);
        yAxisLeft.setDrawGridLines(false);
        yAxisRight.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        yAxisLeft.setDrawLabels(false);
        yAxisRight.setDrawLabels(false);
        lineChart.setDescription("");
        lineChart.setBorderWidth(0);
        lineChart.setBorderColor(Color.TRANSPARENT);
        lineChart.setPinchZoom(false);
        lineChart.setOnClickListener(null);
        lineChart.setOnDragListener(null);
        lineChart.setDrawGridBackground(false);
        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(false);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0f, 0));
        entries.add(new Entry(0f, 1));
        entries.add(new Entry(0f, 2));
        entries.add(new Entry(0f, 3));
        entries.add(new Entry(0f, 4));
        entries.add(new Entry(0f, 5));
        entries.add(new Entry(0f, 6));
        entries.add(new Entry(0f, 7));
        entries.add(new Entry(0f, 8));

        LineDataSet dataset = new LineDataSet(entries, "");
        ArrayList<String> labels = new ArrayList<>();
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("");
        labels.add("");

        LineData data = new LineData(labels, dataset);

        dataset.setColor(Color.TRANSPARENT);
        dataset.setColors(new int[]{Color.parseColor("#7796E0"), Color.parseColor("#7796E0"),
                Color.parseColor("#7796E0"),
                Color.parseColor("#7796E0"),
                Color.parseColor("#949392"),
                Color.parseColor("#949392"),
                Color.parseColor("#949392"),
                Color.parseColor("#949392"),
                Color.parseColor("#949392")});

        dataset.setCircleColors(new int[]{Color.parseColor("#7796E0"),
                Color.parseColor("#7796E0"),
                Color.parseColor("#7796E0"),
                Color.parseColor("#7796E0"),
                Color.parseColor("#FF7C00"),
                Color.parseColor("#949392"),
                Color.parseColor("#949392"),
                Color.parseColor("#949392"),
                Color.parseColor("#949392")});

        dataset.setDrawCircles(true);
        dataset.setDrawCircleHole(true);
        dataset.setCircleSize(5);
        dataset.setDrawCubic(false);
        dataset.setDrawFilled(false);
        dataset.setDrawValues(false);
        dataset.setLineWidth(3);

        lineChart.setData(data);
    }

    private void doLoadWeeklySchedulesOfWorker(int reqCode, int year, int weekStartDayOfTheYear) {
        try {
            Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));
            RequestParams params = new RequestParams();
            params.put("year", year);
            params.put("weekStartDayOfTheYear", weekStartDayOfTheYear);
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            restClient.performHTTPGetRequest(reqCode, ServiceUrls.GET_WEEK_SCHEDULES_URL, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
            ///LegionUtils.showProgressDialog(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNameAndWishes();
        workerShiftsList.clear();
        doLoadData();
    }

    private void doLoadData() {
        if (LegionUtils.isOnline(getActivity())) {
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DATE, now.get(Calendar.DAY_OF_WEEK) * -1);

            doLoadWeeklySchedules(now.get(Calendar.YEAR), now.get(Calendar.DAY_OF_YEAR)); //to get the count
            doGetShiftOffers();        //to get the count
            doGetSwapOffers();  //to get the swapshift count

            //Loading shifts of 3 weeks (i.e current, last, next)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, (cal.get(Calendar.DAY_OF_WEEK) - 1) * -1);
            doLoadWeeklySchedulesOfWorker(RC_CURRENT_WEEK_SHIFTS, cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR));

            cal.add(Calendar.DATE, -7);
            doLoadWeeklySchedulesOfWorker(RC_LAST_WEEK_SHIFTS, cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR));

            cal.add(Calendar.DATE, 14);
            doLoadWeeklySchedulesOfWorker(RC_NEXT_WEEK_SHIFTS, cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR));
        } else {
            if (legionPreferences.hasKey(Prefs_Keys_Offline.GET_SFIFTOFFERS_COUNT) && legionPreferences.hasKey(Prefs_Keys_Offline.GET_SWAPSHIFT_COUNT)) {
                shiftoffercount = ResponseParserTask.getOpenShiftOffersCount(legionPreferences.get(Prefs_Keys_Offline.GET_SFIFTOFFERS_COUNT));
                swapoffercount = ResponseParserTask.getSwapShiftOffersCount(legionPreferences.get(Prefs_Keys_Offline.GET_SWAPSHIFT_COUNT));
                setShiftOfferText(shiftoffercount + swapoffercount);
                if (legionPreferences.hasKey(Prefs_Keys_Offline.GET_SCHEDULES_COUNT)) {
                    int newSchedulesCount = ResponseParserTask.getWeeklySchedulesRatingsCount(legionPreferences.get(Prefs_Keys_Offline.GET_SCHEDULES_COUNT));

                    schedules_count_tv.setText(newSchedulesCount + "");
                    if (newSchedulesCount >= 1) {
                        schedules_count_tv.setTextColor(ActivityCompat.getColor(getActivity(), R.color.colorOrange));
                    } else {
                        schedules_count_tv.setTextColor(ActivityCompat.getColor(getActivity(), R.color.textViewColor));
                    }

                    currentWeekLoaded = false;
                    nextWeekLoaded = false;
                    lastWeekLoaded = false;
                    dateToShiftsCountMap.clear();
                    if (legionPreferences.hasKey(Prefs_Keys_Offline.RC_CURRENT_WEEK_SHIFTS)) {
                        Log.d("RC_CURRENT_WEEK_SHIFTS ", legionPreferences.get(Prefs_Keys_Offline.RC_CURRENT_WEEK_SHIFTS));
                        onSuccess(RC_CURRENT_WEEK_SHIFTS, legionPreferences.get(Prefs_Keys_Offline.RC_CURRENT_WEEK_SHIFTS), null);
                    }
                    if (legionPreferences.hasKey(Prefs_Keys_Offline.RC_LAST_WEEK_SHIFTS)) {
                        Log.d("RC_LAST_WEEK_SHIFTS ", legionPreferences.get(Prefs_Keys_Offline.RC_LAST_WEEK_SHIFTS));
                        onSuccess(RC_LAST_WEEK_SHIFTS, legionPreferences.get(Prefs_Keys_Offline.RC_LAST_WEEK_SHIFTS), null);
                    }
                    if (legionPreferences.hasKey(Prefs_Keys_Offline.GET_NEXTWEEK_SHIFTS)) {
                        Log.d("RC_NEXT_WEEK_SHIFTS ", legionPreferences.get(Prefs_Keys_Offline.GET_NEXTWEEK_SHIFTS));
                        onSuccess(RC_NEXT_WEEK_SHIFTS, legionPreferences.get(Prefs_Keys_Offline.GET_NEXTWEEK_SHIFTS), null);
                    }
                }
            } else {
                LegionUtils.showOfflineDialog(getActivity());
            }
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if(getActivity() == null || !isAdded()){
            return;
        }
        if (requestCode == RC_NOTIFICATION_WEEK_SHIFTS) {
            LegionUtils.hideProgressDialog();
            new ResponseParserTask(ResponseParserConstants.NOTIFICATION_SHIFTS_OF_WORKER_RESPONSE, legionPreferences, this).execute(response.toString());
        } else if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
            LegionUtils.hideProgressDialog();
            legionPreferences.save(Prefs_Keys_Offline.GET_SCHEDULES_COUNT, response.toString());
            int newSchedulesCount = ResponseParserTask.getWeeklySchedulesRatingsCount(response.toString());
            schedules_count_tv.setText(newSchedulesCount + "");
            if (newSchedulesCount >= 1) {
                schedules_count_tv.setTextColor(ActivityCompat.getColor(getActivity(), R.color.colorOrange));
            } else {
                schedules_count_tv.setTextColor(ActivityCompat.getColor(getActivity(), R.color.textViewColor));
            }
        } else if (requestCode == WebServiceRequestCodes.GET_SHIFT_OFFERS_REQUEST_CODE) {
            LegionUtils.hideProgressDialog();
            legionPreferences.save(Prefs_Keys_Offline.GET_SFIFTOFFERS_COUNT, response.toString());
            shiftoffercount = ResponseParserTask.getOpenShiftOffersCount(response.toString());
            setShiftOfferText(shiftoffercount + swapoffercount);
        } else if (requestCode == WebServiceRequestCodes.GET_SWAP_SHIFT_OFFERS) {
            LegionUtils.hideProgressDialog();
            legionPreferences.save(Prefs_Keys_Offline.GET_SWAPSHIFT_COUNT, response.toString());
            swapoffercount = ResponseParserTask.getSwapShiftOffersCount(response.toString());
            setShiftOfferText(shiftoffercount + swapoffercount);
        } else if (requestCode == RC_CURRENT_WEEK_SHIFTS || requestCode == RC_LAST_WEEK_SHIFTS || requestCode == RC_NEXT_WEEK_SHIFTS) {
            LegionUtils.hideProgressDialog();
            if (RC_CURRENT_WEEK_SHIFTS == requestCode) {
                legionPreferences.save(Prefs_Keys_Offline.RC_CURRENT_WEEK_SHIFTS, response.toString());
                currentWeekLoaded = true;
            } else if (RC_LAST_WEEK_SHIFTS == requestCode) {
                lastWeekLoaded = true;
                legionPreferences.save(Prefs_Keys_Offline.RC_LAST_WEEK_SHIFTS, response.toString());
            } else {
                legionPreferences.save(Prefs_Keys_Offline.GET_NEXTWEEK_SHIFTS, response.toString());
                nextWeekLoaded = true;
            }
            new ResponseParserTask(ResponseParserConstants.PARSE_WEEKLY_SHIFTS_OF_WORKER_RESPONSE, legionPreferences, this).execute(response.toString());
        }
    }

    private void doUpdateGraph() {
        if (lastWeekLoaded && currentWeekLoaded && nextWeekLoaded) {
            shift_day_with_time_tv.setOnClickListener(null);
            boolean allZeros = true;
            Calendar cal = Calendar.getInstance();
            String shiftHrs = dateToShiftsCountMap.get(String.valueOf(cal.get(Calendar.DATE)));
            if (shiftHrs != null) {
                allZeros = false;
            }
            currentDayTimeTV.setText((shiftHrs == null ? "0" : shiftHrs) + "hr");
            currentDayTimeTV.setTypeface(null, Typeface.BOLD);
            cal.add(Calendar.DATE, -1);
            shiftHrs = dateToShiftsCountMap.get(String.valueOf(cal.get(Calendar.DATE)));
            if (shiftHrs != null) {
                allZeros = false;
            }
            prevDay1TimeTV.setText((shiftHrs == null ? "0" : shiftHrs) + "hr");

            cal.add(Calendar.DATE, -1);
            shiftHrs = dateToShiftsCountMap.get(String.valueOf(cal.get(Calendar.DATE)));
            if (shiftHrs != null) {
                allZeros = false;
            }
            prevDay2TimeTV.setText((shiftHrs == null ? "0" : shiftHrs) + "hr");

            cal.add(Calendar.DATE, -1);
            shiftHrs = dateToShiftsCountMap.get(String.valueOf(cal.get(Calendar.DATE)));
            if (shiftHrs != null) {
                allZeros = false;
            }
            prevDay3TimeTV.setText((shiftHrs == null ? "0" : shiftHrs) + "hr");

            cal.add(Calendar.DATE, -1);
            shiftHrs = dateToShiftsCountMap.get(String.valueOf(cal.get(Calendar.DATE)));
            if (shiftHrs != null) {
                allZeros = false;
            }
            prevDay4TimeTV.setText((shiftHrs == null ? "0" : shiftHrs) + "hr");


            cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            shiftHrs = dateToShiftsCountMap.get(String.valueOf(cal.get(Calendar.DATE)));
            if (shiftHrs != null) {
                allZeros = false;
            }
            nextDay1TimeTV.setText((shiftHrs == null ? "0" : shiftHrs) + "hr");

            cal.add(Calendar.DATE, 1);
            shiftHrs = dateToShiftsCountMap.get(String.valueOf(cal.get(Calendar.DATE)));
            if (shiftHrs != null) {
                allZeros = false;
            }
            nextDay2TimeTV.setText((shiftHrs == null ? "0" : shiftHrs) + "hr");

            cal.add(Calendar.DATE, 1);
            shiftHrs = dateToShiftsCountMap.get(String.valueOf(cal.get(Calendar.DATE)));
            if (shiftHrs != null) {
                allZeros = false;
            }
            nextDay3TimeTV.setText((shiftHrs == null ? "0" : shiftHrs) + "hr");

            cal.add(Calendar.DATE, 4);
            shiftHrs = dateToShiftsCountMap.get(String.valueOf(cal.get(Calendar.DATE)));
            if (shiftHrs != null) {
                allZeros = false;
            }
            nextDay4TimeTV.setText((shiftHrs == null ? "0" : shiftHrs) + "hr");

            if (allZeros) {
                shift_day_with_time_tv.setVisibility(View.GONE);
                noShiftsTV.setVisibility(View.VISIBLE);
            } else {
                shift_day_with_time_tv.setVisibility(View.VISIBLE);
                noShiftsTV.setVisibility(View.GONE);

                String nextShiftTime = getNextShiftTime(shiftStartDatesList, workerShiftsList);
                lineChart.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                if (nextShiftTime == null || nextShiftTime.equals("")) {
                    shift_day_with_time_tv.setVisibility(View.GONE);
                    noShiftsTV.setVisibility(View.VISIBLE);
                } else {
                    shift_day_with_time_tv.setOnClickListener(this);
                    shift_day_with_time_tv.setText(Html.fromHtml("Your next shift starts <font color=#ff7c00>" + nextShiftTime + "</font>"));
                }
            }

            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(Float.parseFloat(prevDay4TimeTV.getText().toString().replace("hr", "")), 0));
            entries.add(new Entry(Float.parseFloat(prevDay3TimeTV.getText().toString().replace("hr", "")), 1));
            entries.add(new Entry(Float.parseFloat(prevDay2TimeTV.getText().toString().replace("hr", "")), 2));
            entries.add(new Entry(Float.parseFloat(prevDay1TimeTV.getText().toString().replace("hr", "")), 3));
            entries.add(new Entry(Float.parseFloat(currentDayTimeTV.getText().toString().replace("hr", "")), 4));
            entries.add(new Entry(Float.parseFloat(nextDay1TimeTV.getText().toString().replace("hr", "")), 5));
            entries.add(new Entry(Integer.parseInt(nextDay2TimeTV.getText().toString().replace("hr", "")), 6));
            entries.add(new Entry(Float.parseFloat(nextDay3TimeTV.getText().toString().replace("hr", "")), 7));
            entries.add(new Entry(Float.parseFloat(nextDay4TimeTV.getText().toString().replace("hr", "")), 8));

            LineDataSet dataSet = new LineDataSet(entries, "");
            ArrayList<String> labels = new ArrayList<>();
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");
            labels.add("");

            LineData data = new LineData(labels, dataSet);

            dataSet.setColor(Color.TRANSPARENT);
            dataSet.setColors(new int[]{Color.parseColor("#7796E0"),
                    Color.parseColor("#7796E0"),
                    Color.parseColor("#7796E0"),
                    Color.parseColor("#7796E0"),
                    Color.parseColor("#949392"),
                    Color.parseColor("#949392"),
                    Color.parseColor("#949392"),
                    Color.parseColor("#949392"),
                    Color.parseColor("#949392")});

            dataSet.setCircleColors(new int[]{Color.parseColor("#7796E0"),
                    Color.parseColor("#7796E0"),
                    Color.parseColor("#7796E0"),
                    Color.parseColor("#7796E0"),
                    Color.parseColor("#FF7C00"),
                    Color.parseColor("#949392"),
                    Color.parseColor("#949392"),
                    Color.parseColor("#949392"),
                    Color.parseColor("#949392")});

            dataSet.setDrawCircles(true);
            dataSet.setDrawCircleHole(true);
            dataSet.setCircleSize(5);
            dataSet.setDrawCubic(false);
            dataSet.setDrawFilled(false);
            dataSet.setDrawValues(false);
            dataSet.setLineWidth(3);

            lineChart.setData(data);
        }
    }

    private void setShiftOfferText(int shiftsCount) {
        if(getActivity() == null){
            return;
        }
        shift_offers_count_tv.setText(shiftsCount + "");
        if (shiftsCount >= 1) {
            shift_offers_count_tv.setTextColor(ActivityCompat.getColor(getActivity(), R.color.colorOrange));
        } else {
            shift_offers_count_tv.setTextColor(ActivityCompat.getColor(getActivity(), R.color.textViewColor));
        }
    }

    private void setupDaysLabels() {
        Calendar cal = Calendar.getInstance();
        currentDayTV.setText(new SimpleDateFormat("EEEEE").format(cal.getTime()) + "\n" + cal.get(Calendar.DATE));
        currentDayTV.setTypeface(null, Typeface.BOLD);
        cal.add(Calendar.DATE, -1);
        prevDay1TV.setText(new SimpleDateFormat("EEEEE").format(cal.getTime()) + "\n" + cal.get(Calendar.DATE));

        cal.add(Calendar.DATE, -1);
        prevDay2TV.setText(new SimpleDateFormat("EEEEE").format(cal.getTime()) + "\n" + cal.get(Calendar.DATE));

        cal.add(Calendar.DATE, -1);
        prevDay3TV.setText(new SimpleDateFormat("EEEEE").format(cal.getTime()) + "\n" + cal.get(Calendar.DATE));

        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        nextDay1TV.setText(new SimpleDateFormat("EEEEE").format(cal.getTime()) + "\n" + cal.get(Calendar.DATE));

        cal.add(Calendar.DATE, 1);
        nextDay2TV.setText(new SimpleDateFormat("EEEEE").format(cal.getTime()) + "\n" + cal.get(Calendar.DATE));

        cal.add(Calendar.DATE, 1);
        nextDay3TV.setText(new SimpleDateFormat("EEEEE").format(cal.getTime()) + "\n" + cal.get(Calendar.DATE));
    }

    private void doLoadWeeklySchedules(int year, int weekStartDayOfYear) {
        LegionUtils.hideKeyboard(getActivity());
        Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));
        RequestParams params = new RequestParams();
        params.put("year", year);
        params.put("weekStartDayOfTheYear", weekStartDayOfYear);
        try {
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE, ServiceUrls.GET_SCHEDULES_URL, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doGetSwapOffers() {
        LegionUtils.hideKeyboard(getActivity());
        Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));
        try {
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SWAP_SHIFT_OFFERS, ServiceUrls.UPDATE_SWAP_SHIFT_OFFER_URL, new RequestParams(), legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doGetShiftOffers() {
        LegionUtils.hideKeyboard(getActivity());
        Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));

        try {
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SHIFT_OFFERS_REQUEST_CODE, ServiceUrls.GET_SHIFT_ORDERS_URL, new RequestParams(), legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
        if (reasonPhrase == null) {
            return;
        }
        if (reasonPhrase.contains("Something went wrong")) {
            LegionUtils.doLogout(getActivity());
            return;
        }
        LegionUtils.showDialog(getActivity(), reasonPhrase, true);
    }

    public String getDayWishes() {
        Calendar cal = Calendar.getInstance();
        int cHour = cal.get(Calendar.HOUR_OF_DAY);
        if (cHour < 12) {
            return "Good morning, ";
        } else if (cHour < 17) {
            return "Good afternoon, ";
        }
        return "Good evening, ";
    }

    public String getTodayDetails() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String suffix = "th";
        if (day == 1 || day == 21 || day == 31) {
            suffix = "st";
        } else if (day == 2 || day == 22) {
            suffix = "nd";
        } else if (day == 3 || day == 23) {
            suffix = "rd";
        }
        return "It's " + new SimpleDateFormat("EEEE, MMM d").format(cal.getTime()) + suffix + ".";
    }

    public String getNextShiftTime(ArrayList<String> shiftStartDatesList, ArrayList<ScheduleWorkerShift> workerShiftsList) {
        ArrayList<Long> nextShiftTimeInMillis = new ArrayList<>();
        for (int i = 0; i < shiftStartDatesList.size(); ++i) {
            String shiftDate = shiftStartDatesList.get(i);
            shiftDate = shiftDate.replace("T", " ");
            if (shiftDate.contains(".")) {
                shiftDate = shiftDate.substring(0, shiftDate.indexOf("."));
            }
            Calendar serverCal = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                serverCal.setTime(sdf.parse(shiftDate));
                if (serverCal.getTimeInMillis() > cal.getTimeInMillis()) {
                    nextShiftTimeInMillis.add(serverCal.getTimeInMillis());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (nextShiftTimeInMillis.size() == 0) {
            return "";
        }
        Collections.sort(nextShiftTimeInMillis);

        Calendar cal = Calendar.getInstance();
        if (new SimpleDateFormat("yyyy MMM dd").format(cal.getTime()).equals(new SimpleDateFormat("yyyy MMM dd").format(nextShiftTimeInMillis.get(0)))) {
            doCalculateWeeklyHrs(cal, workerShiftsList);
            return "today at " + new SimpleDateFormat("h:mm a.").format(nextShiftTimeInMillis.get(0));
        }

        cal.add(Calendar.DATE, 1);
        if (new SimpleDateFormat("yyyy MMM dd").format(cal.getTime()).equals(new SimpleDateFormat("yyyy MMM dd").format(nextShiftTimeInMillis.get(0)))) {
            doCalculateWeeklyHrs(cal, workerShiftsList);
            return "tomorrow at " + new SimpleDateFormat("h:mm a.").format(nextShiftTimeInMillis.get(0));
        }

        //if nextShift is in current week then just show the day, otherwise show the date too.
        Calendar shiftCal = Calendar.getInstance();
        shiftCal.setTimeInMillis(nextShiftTimeInMillis.get(0));
      /*  if (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) == shiftCal.get(Calendar.WEEK_OF_YEAR)) {*/
        if (daysBetween(Calendar.getInstance().getTime(), shiftCal.getTime()) < 7) {
            doCalculateWeeklyHrs(shiftCal, workerShiftsList);
            return new SimpleDateFormat("EEEE").format(nextShiftTimeInMillis.get(0)) + " at " + new SimpleDateFormat("h:mm a.").format(nextShiftTimeInMillis.get(0));
        } else {
            doCalculateWeeklyHrs(shiftCal, workerShiftsList);
            return new SimpleDateFormat("EEE, MMM dd").format(nextShiftTimeInMillis.get(0)) + " at " + new SimpleDateFormat("h:mm a.").format(nextShiftTimeInMillis.get(0));
        }
    }

    private int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    private void doCalculateWeeklyHrs(Calendar cal, ArrayList<ScheduleWorkerShift> workerShiftsList) {
        weeklySchedulesMins = 0;
        cal.add(Calendar.DATE, (cal.get(Calendar.DAY_OF_WEEK) - 1) * -1);
        for (ScheduleWorkerShift workerShift : workerShiftsList) {
            if (cal.get(Calendar.DAY_OF_YEAR) == Integer.parseInt(workerShift.getWeekStartDayOfTheYear())) {
                weeklySchedulesMins += (Integer.parseInt(workerShift.getRegularMinutes()));
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.shiftOffersBoxLL:
                if(getActivity() == null){
                    return;
                }
                LegionUtils.hideKeyboard(getActivity());
                ((HomeActivity) getActivity()).mTabHost.setCurrentTab(2);
                break;
            case R.id.messagesBoxLL:
                if(getActivity() == null){
                    return;
                }
                LegionUtils.hideKeyboard(getActivity());
                ((HomeActivity) getActivity()).mTabHost.setCurrentTab(3);
                break;
            case R.id.schedulesBoxLL:
                if(getActivity() == null){
                    return;
                }
                LegionUtils.hideKeyboard(getActivity());
                ((HomeActivity) getActivity()).mTabHost.setCurrentTab(1);
                break;
            case R.id.shift_day_with_time:
                ScheduleWorkerShift shift = getNextShiftModel(workerShiftsList);
                if(shift != null) {
                    doNavigateToShiftDetails(shift, weeklySchedulesMins);
                }
                break;
        }
    }

    private void doNavigateToShiftDetails(ScheduleWorkerShift shift, int weeklySchedulesMins) {
        Intent i = new Intent(getActivity(), ScheduleShiftDetailsActivity.class);
        String regularMins = LegionUtils.convertMinsToHrsReg(shift.getRegularMinutes());
        String label;
        if ((regularMins.contains("1."))) {
            label = "Hr";
        } else {
            label = "Hrs";
        }
        shiftStartDate = shift.getShiftStartDate();
        shiftEndDate = shift.getShiftEndDate();
        try {
            String dt = shift.getShiftStartDate().replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, (-1 * Integer.parseInt(shift.getDayOfTheWeek())) + 1);
            shiftStartDate = sdf.format(c.getTime());
            c.add(Calendar.DATE, 6);
            shiftEndDate = sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        legionPreferences.save(Prefs_Keys.START_DATE, shiftStartDate);
        legionPreferences.save(Prefs_Keys.END_DATE, shiftEndDate);
        i.putExtra(Extras_Keys.WORKSHIFTS_LIST, shift);
        i.putExtra(Extras_Keys.STARTTIME, LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shift.getStartMin())));
        i.putExtra(Extras_Keys.ENDTIME, LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shift.getEndMin())));
        // legionPreferences.save(Prefs_Keys.START_DATE,shift.getShiftStartDate());
        i.putExtra("totalShiftHours", regularMins + " " + label);
        float hrs = (weeklySchedulesMins / 60f);
        i.putExtra(Extras_Keys.SHIFT_MINS, (hrs + "").endsWith(".0") ? (int) (hrs) + "" : (int) (hrs) + "");
        startActivity(i);
    }

    public void updateNameAndWishes() {
        String name = legionPreferences.get(Prefs_Keys.NICK_NAME);
        if (legionPreferences.hasKey(Prefs_Keys.NICK_NAME) && name != null && name.trim().length() > 0) {
            dayWishesTV.setText(getDayWishes() + legionPreferences.get(Prefs_Keys.NICK_NAME) + ".");
        } else {
            dayWishesTV.setText(getDayWishes() + legionPreferences.get(Prefs_Keys.FIRST_NAME) + ".");
        }
    }

    public ScheduleWorkerShift getNextShiftModel(ArrayList<ScheduleWorkerShift> workerShiftsList) {

        Collections.sort(workerShiftsList, new Comparator<ScheduleWorkerShift>() {
            public int compare(ScheduleWorkerShift o1, ScheduleWorkerShift o2) {

                String shiftDate1 = o1.getShiftStartDate().replace("T", " ");
                String shiftDate2 = o2.getShiftStartDate().replace("T", " ");
                if (shiftDate1.contains(".")) {
                    shiftDate1 = shiftDate1.substring(0, shiftDate1.indexOf("."));
                }

                if (shiftDate2.contains(".")) {
                    shiftDate2 = shiftDate2.substring(0, shiftDate2.indexOf("."));
                }

                Calendar serverCal1 = Calendar.getInstance();
                Calendar serverCal2 = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    serverCal1.setTime(sdf.parse(shiftDate1));
                    serverCal2.setTime(sdf.parse(shiftDate2));
                    Date date1 = serverCal1.getTime();
                    Date date2 = serverCal2.getTime();
                    return date1.compareTo(date2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });

        for (ScheduleWorkerShift shift : workerShiftsList) {
            String shiftDate = shift.getShiftStartDate();
            shiftDate = shiftDate.replace("T", " ");
            if (shiftDate.contains(".")) {
                shiftDate = shiftDate.substring(0, shiftDate.indexOf("."));
            }
            Calendar serverCal = Calendar.getInstance();
            Calendar cal = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                serverCal.setTime(sdf.parse(shiftDate));
                if (serverCal.getTimeInMillis() > cal.getTimeInMillis()) {
                    return shift;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(workerShiftsList.size() >= 1) {
            return workerShiftsList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void onResponseParsingStart(int parserId) {
    }

    @Override
    public void onResponseParsingComplete(int parserId, Object response) {
        if (getActivity() == null || !isAdded()) {
            return;
        }
        if (parserId == ResponseParserConstants.PARSE_WEEKLY_SHIFTS_OF_WORKER_RESPONSE) {
            if (response instanceof Exception) {
                LegionUtils.showDialog(getActivity(), "Oops, Something went wrong.\n\nPlease try again later.", true);
                ((Exception)response).printStackTrace();
            } else if (response instanceof String) {
                LegionUtils.showDialog(getActivity(), response.toString(), true);
            } else if (response instanceof ArrayList<?>) {
                ArrayList<ScheduleWorkerShift> shiftsList = (ArrayList<ScheduleWorkerShift>) response;
                try {
                    for (ScheduleWorkerShift shift : shiftsList) {
                        int startMinInt = Integer.parseInt(shift.getStartMin());
                        int endMinInt = Integer.parseInt(shift.getEndMin());
                        String shiftStartDate = shift.getShiftStartDate();
                        shiftStartDatesList.add(shiftStartDate);
                        String shiftJustDate = shiftStartDate.split("T")[0].trim();
                        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date startDate = dFormat.parse(shiftJustDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(startDate);
                        dateToShiftsCountMap.put(String.valueOf(cal.get(Calendar.DATE)), String.valueOf((endMinInt - startMinInt) / 60));

                        workerShiftsList.add(shift);
                    }
                    doUpdateGraph();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (parserId == ResponseParserConstants.NOTIFICATION_SHIFTS_OF_WORKER_RESPONSE) {
            if (response instanceof Exception) {
                LegionUtils.showDialog(getActivity(), "Oops, Something went wrong.\n\nPlease try again later.", true);
                ((Exception)response).printStackTrace();
            } else if (response instanceof String) {
                LegionUtils.showDialog(getActivity(), response.toString(), true);
            } else if (response instanceof ArrayList<?>) {
                ArrayList<ScheduleWorkerShift> shiftsList = (ArrayList<ScheduleWorkerShift>) response;
                int weekHours = 0;
                ScheduleWorkerShift wShift = null;
                for (ScheduleWorkerShift shift : shiftsList) {
                    String shiftId = shift.getShiftId();
                    String startMinString = shift.getStartMin();
                    String endMinString = shift.getEndMin();

                    weekHours += (Integer.parseInt(endMinString) - Integer.parseInt(startMinString));
                    if (!shiftId.equals(getActivity().getIntent().getStringExtra(Extras_Keys.NOTIFICATION_SHIFTID))) {
                        continue;
                    }
                    wShift = shift;
                }
                if (wShift != null) {
                    doNavigateToShiftDetails(wShift, weekHours);
                }
            }
        }
    }
}
