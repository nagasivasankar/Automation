package fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import asynctasks.ResponseParserTask;
import base.Legion_BaseFragment;
import co.legion.client.R;
import co.legion.client.activities.ScheduleShiftDetailsActivity;
import helpers.CalendarHelper;
import interfaces.ResponseParserListener;
import models.Schedule;
import models.ScheduleWorkerShift;
import models.ShiftOffer;
import models.Slot;
import models.WorkerShiftSummary;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 12/1/2016.
 */
@SuppressLint("ValidFragment")
public class PastScheduleFragment extends Legion_BaseFragment implements View.OnClickListener, ResponseParserListener, Legion_NetworkCallback, SwipeRefreshLayout.OnRefreshListener {

    public LinearLayout shiftLayoutLL;
    boolean emptyLineViewBoolean = false;
    int paddingBig = 0;
    int paddingSmall = 0;
    int minWidth = 0;
    int paddingNormal = 0;
    LinkedHashMap<Integer, ArrayList<ScheduleWorkerShift>> dayToShiftsList;
    boolean shitStartedOrNot = false;
    int currentClickPosition;
    ShiftOffer shiftOffer;
    boolean isshiftorswapscrolled = false;
    boolean isshiftsameday = false;
    String requestedshiftDay = "";
    private ScheduleOverviewDetailsFragment hostFrag = null;
    private ArrayList<ScheduleWorkerShift> workerShiftsList = new ArrayList<>();
    private LinearLayout summaryCollapsedLL;
    private ImageView expandCollapseImage;
    private LinearLayout summaryExpandedLL;
    private TextView numOfShiftsTV;
    private TextView numOfHoursTV;
    private TextView hrsTV;
    private TextView claimedTv;
    private TextView offersTV;
    private TextView sceduledPreferedTv;
    private TextView totalPreferedTv;
    private TextView conflictsTV;
    private TextView proposedPayTV;
    private TextView shiftTv;
    private LinearLayout mainLl;
    private ArrayList datesArrayList;
    private boolean isViewShown;
    private ArrayList<Slot> slotArrayList;
    private boolean checkCurrentWeekOrNot;
    private ArrayList currentDateCheckingArrayList;
    private boolean isPTO;
    private int PTOSkipDayCount = 0;
    private TextView startShiftTv;
    private String shiftDate;
    private View view;
    private String weekStartDayOfTheYear;
    private ScrollView scrollView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int scrollX;
    private int scrollY;
    private int size;
    private String startDate;
    private String endDate;
    /*-------------------   Schedule start intimation before 60 minutes ----------------------------*/
    private BroadcastReceiver updateTimeAndDateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            try {
                long diff = LegionUtils.getTimeMatchedOrNot(shiftDate);
                long difference = (diff / LegionUtils.MINUTE);
                if (difference <= 0) {
                    startShiftTv.setVisibility(View.GONE);
                    unregisterUpdateTimeReciever();
                } else {
                    startShiftTv.setText("   STARTING IN " + difference + (difference == 1 ? " MINUTE" : " MINUTES"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @SuppressLint("ValidFragment")
    public PastScheduleFragment(ScheduleOverviewDetailsFragment hostFrag) {
        if (hostFrag != null) {
            this.hostFrag = hostFrag;
        }
    }

    public PastScheduleFragment() {

    }

    /* New Schedule Seen Service ---------------------------*/
    void doScheduleSeen(String scheduleId) {
        LegionUtils.hideKeyboard(getActivity());
        try {
            LegionUtils.showProgressDialog(getActivity());
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            JSONObject requestObject = new JSONObject();
            restClient.performPostRequest(WebServiceRequestCodes.SCHEDULE_SEEN, ServiceUrls.MARK_SCHEDULE_SEEN + scheduleId, requestObject, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule_details, null);
        if (hostFrag == null) {
            setMenuVisibility(true);
            isViewShown = true;
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getArguments();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        if (b != null && b.getSerializable(Extras_Keys.SHIFT_OFFER_KEY) != null) {
            shiftOffer = (ShiftOffer) b.getSerializable(Extras_Keys.SHIFT_OFFER_KEY);
            // swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setEnabled(false);
            if (!shiftOffer.isShiftSwapOffer()) {
                shiftOffer.getStaffingShift();
            }
        } else {
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setEnabled(true);
        }


        legionPreferences.save(Prefs_Keys.REFRESH, "DONTREFRESH");
        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));
    }

    /* ------------  Receiver registration for minute updation ----------*/
    public void registerUpdateTimeReciever() {
        try {
            IntentFilter mTime = new IntentFilter(Intent.ACTION_TIME_TICK);
            getActivity().registerReceiver(updateTimeAndDateReceiver, mTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isViewShown = true;
        } else {
            isViewShown = false;
        }
    }

    /* Unregistered reciever after schedule starts ------------*/
    public void unregisterUpdateTimeReciever() {
        try {
            getActivity().unregisterReceiver(updateTimeAndDateReceiver);
        } catch (Exception e) {
            Log.d("Not registered", "Reciever");
        }
    }

    private void init(View view) {
        workerShiftsList.clear();
        try {

            doUpdateDeviceSpecificData();

            //FIX : Updating the pretty old build with latest build will cause weekly schedule screen OFF.
            if ((minWidth == 0 || size == 0) && getActivity() != null) {
                LegionUtils.getDisplayMetrics(getActivity());
                doUpdateDeviceSpecificData();
            }

            mainLl = (LinearLayout) view.findViewById(R.id.mainLl);
            scrollView = (ScrollView) view.findViewById(R.id.scrollView);
            summaryExpandedLL = (LinearLayout) view.findViewById(R.id.summaryExpandedLL);
            summaryCollapsedLL = (LinearLayout) view.findViewById(R.id.summaryCollapsedLL);
            summaryCollapsedLL.setOnClickListener(this);
            numOfShiftsTV = (TextView) view.findViewById(R.id.numOfShiftsTV);
            shiftTv = (TextView) view.findViewById(R.id.shiftTv);
            numOfHoursTV = (TextView) view.findViewById(R.id.numOfHoursTV);
            hrsTV = (TextView) view.findViewById(R.id.hrsTV);
            claimedTv = (TextView) view.findViewById(R.id.claimedTv);
            offersTV = (TextView) view.findViewById(R.id.offersTV);
            sceduledPreferedTv = (TextView) view.findViewById(R.id.sceduledPreferedTv);
            totalPreferedTv = (TextView) view.findViewById(R.id.totalPreferedTv);
            conflictsTV = (TextView) view.findViewById(R.id.conflictsTV);
            proposedPayTV = (TextView) view.findViewById(R.id.proposedPayTV);
            startDate = legionPreferences.get(Prefs_Keys.START_DATE);
            endDate = legionPreferences.get(Prefs_Keys.END_DATE);
            checkCurrentWeekOrNot = LegionUtils.getDiffernceBnTwoDates(startDate.replace("T", " ").replace("Z", " "));
            currentDateCheckingArrayList = LegionUtils.getSelectedCurrentDate(startDate, checkCurrentWeekOrNot);
            datesArrayList = LegionUtils.getOneWeekDates(startDate);
            expandCollapseImage = (ImageView) view.findViewById(R.id.expandCollapseImage);
            if (summaryExpandedLL.getVisibility() == View.VISIBLE) {
                summaryCollapsedLL.performClick();
            }

            if (LegionUtils.isOnline(getActivity())) {
                if (getActivity() != null) {
                    LegionUtils.showProgressDialog(getActivity());
                    doPtoRequest();
                    if (Extras_Keys.FIRST_TIME_TITLE_UPDATED != null) {
                        try {
                            if (Extras_Keys.SCHEDULE_ID != null) {
                                doScheduleSeen(Extras_Keys.SCHEDULE_ID);
                                Extras_Keys.SCHEDULE_ID = null;
                            }
                            if (hostFrag != null) {
                                if (checkCurrentWeekOrNot) {
                                    hostFrag.setupToolbar(hostFrag.getView(), true, "Current Schedule");
                                } else if (LegionUtils.isPastWeek(startDate.replace("T", " ").replace("Z", " "), endDate.replace("T", " ").replace("Z", " "))) {
                                    hostFrag.setupToolbar(hostFrag.getView(), true, "Past Schedule");
                                } else {
                                    if (Extras_Keys.FIRST_TIME_TITLE_UPDATED.toString().toUpperCase().equalsIgnoreCase("Finalized".toUpperCase())) {
                                        hostFrag.setupToolbar(hostFrag.getView(), true, "Upcoming Schedule");
                                    } else if (Extras_Keys.FIRST_TIME_TITLE_UPDATED.toString().toUpperCase().equalsIgnoreCase("Published".toUpperCase())) {
                                        hostFrag.setupToolbar(hostFrag.getView(), true, "Upcoming Schedule");
                                    } else {
                                        hostFrag.setupToolbar(hostFrag.getView(), true, "Future Schedule");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Extras_Keys.FIRST_TIME_TITLE_UPDATED = null;
                    } else {
                        doScheduleRatingRequest();
                    }

                }
            } else {
                LegionUtils.showOfflineDialog(getActivity());
            }
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    ViewTreeObserver observer = scrollView.getViewTreeObserver();
                    observer.addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged() {
                            scrollX = scrollView.getScrollX();
                            scrollY = scrollView.getScrollY();
                            if ((scrollX == 0) && (scrollY == 0)) {
                                if (shiftOffer == null) {
                                    swipeRefreshLayout.setEnabled(true);
                                }
                            } else {
                                summaryExpandedLL.setVisibility(View.GONE);
                                swipeRefreshLayout.setEnabled(false);
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            if (getActivity() != null)
                LegionUtils.showMessageDialog(getActivity(), "Something went wrong! Please try again.", R.drawable.error_transient);
            e.printStackTrace();
        }
    }

    private void doUpdateDeviceSpecificData() {
        paddingBig = legionPreferences.getInt(Prefs_Keys.PADDING_BIG);
        paddingSmall = legionPreferences.getInt(Prefs_Keys.PADDING_LESS);
        minWidth = legionPreferences.getInt(Prefs_Keys.MIN_WIDTH);
        paddingNormal = legionPreferences.getInt(Prefs_Keys.PADDING_50);
        size = legionPreferences.getInt(Prefs_Keys.SIZE);
    }

    @Override
    public void onRefresh() {
        shitStartedOrNot = false;
        checkCurrentWeekOrNot = LegionUtils.getDiffernceBnTwoDates(startDate.replace("T", " ").replace("Z", " "));
        currentDateCheckingArrayList = LegionUtils.getSelectedCurrentDate(startDate, checkCurrentWeekOrNot);
        datesArrayList = LegionUtils.getOneWeekDates(startDate);
        swipeRefreshLayout.setRefreshing(true);
        doPtoRequest();
        doScheduleRatingRequest();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.summaryCollapsedLL:
                if (summaryExpandedLL.getVisibility() == View.VISIBLE) {
                    summaryExpandedLL.setVisibility(View.GONE);
                    expandCollapseImage.setImageResource(R.drawable.ic_down_black);
                } else {
                    expandCollapseImage.setImageResource(R.drawable.ic_top_black);
                    Animation a = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0);
                    a.setFillAfter(true);
                    summaryExpandedLL.setAnimation(a);
                    a.setDuration(250);
                    summaryExpandedLL.startAnimation(a);
                    summaryExpandedLL.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void doLoadWeeklySchedules() {
        try {
            if (!LegionUtils.isOnline(getActivity())) {
                LegionUtils.showOfflineDialog(getActivity());
                return;
            }
            LegionUtils.hideKeyboard(getActivity());
            Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));
            Calendar cal = Calendar.getInstance();
            RequestParams params = new RequestParams();
            String year = LegionUtils.getYearFromDate(startDate);
            params.put("year", year);
            params.put("weekStartDayOfTheYear", LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", startDate));
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_WEEKLY_SCHEDULES_REQUEST_CODE, ServiceUrls.GET_WEEK_SCHEDULES_URL, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLoadScheduleSummary() {
        try {
            swipeRefreshLayout.setRefreshing(false);
            if (!LegionUtils.isOnline(getActivity())) {
                LegionUtils.showOfflineDialog(getActivity());
                return;
            }
            LegionUtils.hideKeyboard(getActivity());
            Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));

            Calendar cal = Calendar.getInstance();
            RequestParams params = new RequestParams();
            String year = LegionUtils.getYearFromDate(startDate);
            params.put("year", year);
            params.put("weekStartDayOfTheYear", LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", startDate));
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SCHEDULE_SUMMARY_REQUEST_CODE, ServiceUrls.GET_SCHEDULE_SUMMERY_URL, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doPtoRequest() {
        try {
            if (getActivity() != null) {
                if (!LegionUtils.isOnline(getActivity())) {
                    swipeRefreshLayout.setRefreshing(false);
                    LegionUtils.showOfflineDialog(getActivity());
                    return;
                }
                LegionUtils.hideKeyboard(getActivity());
                Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));
                Calendar cal = Calendar.getInstance();
                RequestParams params = new RequestParams();
                String year = LegionUtils.getYearFromDate(startDate);
                params.put("year", year);
                params.put("weekStartDayOfTheYear", LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", startDate));
                Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
                restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_PTO_REQUEST_CODE, ServiceUrls.GET_PTO_URL, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doScheduleRatingRequest() {
        try {
            if (!LegionUtils.isOnline(getActivity())) {
                swipeRefreshLayout.setRefreshing(false);
                LegionUtils.showOfflineDialog(getActivity());
                return;
            }
            LegionUtils.hideKeyboard(getActivity());
            Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));
            Calendar cal = Calendar.getInstance();
            RequestParams params = new RequestParams();
            String year = LegionUtils.getYearFromDate(startDate);
            params.put("year", year);
            params.put("weekStartDayOfTheYear", weekStartDayOfTheYear = LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", startDate));
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SCHEDULED_RATING, ServiceUrls.GET_SCHEDULES_URL, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.GET_WEEKLY_SCHEDULES_REQUEST_CODE) {
            // LegionUtils.showProgressDialog(getActivity());
        } else if (requestCode == WebServiceRequestCodes.GET_SCHEDULE_SUMMARY_REQUEST_CODE) {
            // LegionUtils.showProgressDialog(getActivity());
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.GET_WEEKLY_SCHEDULES_REQUEST_CODE) {
            if ((getActivity() != null) && (getView() != null)) {
                if (getActivity() != null) {
                    new ResponseParserTask(ResponseParserConstants.PARSE_WEEKLY_SHIFTS_OF_WORKER_RESPONSE, legionPreferences, this).execute(response.toString());
                }
            }
        } else if (requestCode == WebServiceRequestCodes.GET_PTO_REQUEST_CODE) {
            if (getActivity() != null) {
                onResponseParsingComplete(ResponseParserConstants.PARSE_PTO_REQUEST_CODE, ResponseParserTask.doParsePtoDetails(response.toString()));
            }
        } else if (requestCode == WebServiceRequestCodes.GET_SCHEDULE_SUMMARY_REQUEST_CODE) {
            LegionUtils.hideProgressDialog();
            if (getActivity() != null) {
                new ResponseParserTask(ResponseParserConstants.PARSE_SCHEDULE_SUMMARY_DETAILS, legionPreferences, this).execute(response.toString());
            }
        } else if (requestCode == WebServiceRequestCodes.GET_SCHEDULED_RATING) {
            if ((getActivity() != null) && (Extras_Keys.FIRST_TIME_TITLE_UPDATED == null)) {
                new ResponseParserTask(ResponseParserConstants.PARSE_SCHEDULES, legionPreferences, this).execute(response.toString());
            }
        }
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if (view == null) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        init(view);
                    }
                }, 300);
            } else {
                shitStartedOrNot = false;
                init(view);
            }
        }
    }

    private String checkingStringIsNullOrNot(String data) {
        String result = "0";
        if (data != null) {
            result = data.replace(".0", "");
        }
        return result;
    }

    private String convertFromMinutesToHours(String hrs) {
        String hours = "0";
        if (hrs != null) {
            hours = String.valueOf(Double.parseDouble(hrs) / 60).replace(".0", "");
        }
        return hours;
    }

    private int convertFromMinutesToHoursInt(String hrs) {
        String hours = "0";
        if (hrs != null) {
            hours = String.valueOf(Double.parseDouble(hrs) / 60).replace(".0", "");
        }
        if (hours.contains(".")) {
            String[] splitHours = hours.split("\\.");
            hours = splitHours[0];
        }
        return Integer.valueOf(hours);
    }

    public void setDataToList(final ArrayList<ScheduleWorkerShift> workerShift, final int position, boolean isPTOTrueOrNot, boolean ptoNextShift, String PTODayOfTheWeek) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout dayLayout = (LinearLayout) inflater.inflate(R.layout.past_schedule_day_layout, null); //day
        LinearLayout shiftsContainerLayoutInDay = (LinearLayout) dayLayout.findViewById(R.id.shiftsContainerLayoutInDay);
        LinearLayout bgChangeLL = (LinearLayout) dayLayout.findViewById(R.id.bgChangeLL);
        LinearLayout backgroundLL = (LinearLayout) dayLayout.findViewById(R.id.backgroundLL);
        backgroundLL.setBackgroundResource(R.drawable.bg_white_with_corners);
        final LinearLayout dateViewLl = (LinearLayout) dayLayout.findViewById(R.id.dateViewLl);
        String currentDaySelect = "";
        String dayName = "";
        String dateTv = "";

           /* 1) position == DayOfTheWeek
            * 2) DayName and Date updation for every item */
        if (position == 1) {
            dayName = "MON";
            currentDaySelect = currentDateCheckingArrayList.get(0).toString();
            dateTv = (datesArrayList.get(0).toString());
        } else if (position == 2) {
            dayName = "TUE";
            currentDaySelect = currentDateCheckingArrayList.get(1).toString();
            dateTv = (datesArrayList.get(1).toString());
        } else if (position == 3) {
            dayName = "WED";
            currentDaySelect = currentDateCheckingArrayList.get(2).toString();
            dateTv = (datesArrayList.get(2).toString());
        } else if (position == 4) {
            dayName = "THU";
            dateTv = (datesArrayList.get(3).toString());
            currentDaySelect = currentDateCheckingArrayList.get(3).toString();
        } else if (position == 5) {
            dayName = "FRI";
            currentDaySelect = currentDateCheckingArrayList.get(4).toString();
            dateTv = (datesArrayList.get(4).toString());
        } else if (position == 6) {
            dayName = "SAT";
            currentDaySelect = currentDateCheckingArrayList.get(5).toString();
            dateTv = (datesArrayList.get(5).toString());
        } else if (position == 7) {
            dayName = "SUN";
            currentDaySelect = currentDateCheckingArrayList.get(6).toString();
            dateTv = (datesArrayList.get(6).toString());
        }
        dateViewLl.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        int len = 0;
        boolean checkOddOrEven = false;
        len = workerShift.size();
        double middle = len / 2;
        boolean executedMiddleItem = false;
           /* ----------------  On same day multiple shifts is even or odd number -----------------*/
        if (len > 0) {
            if (len % 2 == 0) {
                dateViewLl.setWeightSum((float) (Float.valueOf("" + len) + 0.5));
                checkOddOrEven = true;
            } else {
                dateViewLl.setWeightSum(len);
                middle = middle + 0.5;
                checkOddOrEven = false;
            }
        }
          /* ----------------  Schedule Item data appending Layout  -----------------*/
        for (int i = 0; i < len; ++i) {
            final LinearLayout shiftLayout = (LinearLayout) inflater.inflate(R.layout.past_schedule_row_layout, null);

            View lineView = (View) shiftLayout.findViewById(R.id.lineView);
            TextView nameTV = (TextView) shiftLayout.findViewById(R.id.nameTV);
            TextView roleTV = (TextView) shiftLayout.findViewById(R.id.roleTV);
            TextView addressTV = (TextView) shiftLayout.findViewById(R.id.addressTV);
            TextView statTimeTV = (TextView) shiftLayout.findViewById(R.id.statTimeTV);
            final TextView endTimeTV = (TextView) shiftLayout.findViewById(R.id.endTimeTV);
            TextView durationTV = (TextView) shiftLayout.findViewById(R.id.durationTV);
            TextView swapIv = (TextView) shiftLayout.findViewById(R.id.swapIv);
            LinearLayout openShiftBannerLayout = (LinearLayout) shiftLayout.findViewById(R.id.openShiftBannerLayout);
            LinearLayout shiftll = (LinearLayout) shiftLayout.findViewById(R.id.shiftll);
            ImageView openshiftbannerimage = (ImageView) shiftLayout.findViewById(R.id.openshiftbannerimage);
            TextView shifttitle = (TextView) shiftLayout.findViewById(R.id.shifttitle);
            ImageView statusAvlbtyIv = (ImageView) shiftLayout.findViewById(R.id.statusAvlbtyIv);
            TextView dayOfTheWeekTv = (TextView) shiftLayout.findViewById(R.id.dayOfTheWeekTv);
            View lineViewClock = (View) shiftLayout.findViewById(R.id.lineViewClock);
            LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) shiftLayout.findViewById(R.id.itemParentLayout));
            final ScheduleWorkerShift scheduleDetails = workerShift.get(i);
            TextView hrsTV = (TextView) shiftLayout.findViewById(R.id.hrsTV);

            nameTV.setText(scheduleDetails.getBusinessKey().getName());
            emptyLineViewBoolean = true;
            statTimeTV.setText(LegionUtils.convertMinsToTimeinHrs(Long.valueOf(scheduleDetails.getStartMin())));
            dayOfTheWeekTv.setText(scheduleDetails.getDayOfTheWeek() + "," + (currentClickPosition + 1) + "," + scheduleDetails.getPosition());
            endTimeTV.setText(LegionUtils.convertMinsToTimeinHrs(Long.valueOf(scheduleDetails.getEndMin())));
            long diff = LegionUtils.getTimeMatchedOrNot(scheduleDetails.getShiftStartDate());
            long difference = (diff / LegionUtils.MINUTE);
            String regularMins = LegionUtils.convertMinsToHrsReg(scheduleDetails.getRegularMinutes());
            durationTV.setText(regularMins);
            String offerStatus = scheduleDetails.getOfferStatus();

            if (offerStatus != null && (offerStatus.equalsIgnoreCase("SwapPending") || offerStatus.equalsIgnoreCase("DropPending"))) {
                swapIv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shiftswapoff_drop, 0);
                swapIv.setVisibility(View.VISIBLE);
            } else if (offerStatus != null && offerStatus.equalsIgnoreCase("Open")) {
                swapIv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.openshiftoff, 0);
                swapIv.setVisibility(View.VISIBLE);
            } else {
                swapIv.setVisibility(View.GONE);
            }
            if (scheduleDetails.isstaffinShiftAdded()) {
                openShiftBannerLayout.setVisibility(View.VISIBLE);
                openShiftBannerLayout.setBackgroundResource(R.drawable.bg_skyblue_top_right_corner);
                openshiftbannerimage.setImageResource(R.drawable.ic_open_shift);
                shifttitle.setText("open shift");
                if (scheduleDetails.getEstimatedPay() != null) {
                    swapIv.setVisibility(View.VISIBLE);
                    int estPay = LegionUtils.getEstimatedPayAsInt(scheduleDetails.getEstimatedPay());
                    swapIv.setText(Html.fromHtml("Est.<font color='#66BB6A'> $ " + estPay + "</font>"));
                }

                shiftll.setBackgroundResource(R.drawable.bg_light_gray_corners);
            }
            if (scheduleDetails.isShiftoffered()) {
                openShiftBannerLayout.setVisibility(View.VISIBLE);
                openShiftBannerLayout.setBackgroundResource(R.drawable.bg_voilet_top_right_corner);
                shifttitle.setText("offered shift");
                openshiftbannerimage.setImageResource(R.drawable.shift_swap);
                if (scheduleDetails.getEstimatedPay() != null) {
                    swapIv.setVisibility(View.VISIBLE);
                    int estPay = LegionUtils.getEstimatedPayAsInt(scheduleDetails.getEstimatedPay());
                    swapIv.setText(Html.fromHtml("Est.<font color='#66BB6A'> $ " + estPay + "</font>"));

                }

                if (isshiftsameday) {
                    shiftll.setBackgroundResource(R.drawable.bg_light_gray_without_radious);   // remove right bottom radious
                } else {
                    shiftll.setBackgroundResource(R.drawable.bg_light_gray_corners);  // add right bottom radious
                }
                // requestedshiftDay = scheduleDetails.getDayOfTheWeek();
            }
            if (scheduleDetails.isShiftRequested()) {
                openShiftBannerLayout.setVisibility(View.VISIBLE);
                if (isshiftsameday) {
                    openShiftBannerLayout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.swapshift_offer_bg));
                } else {
                    openShiftBannerLayout.setBackgroundResource(R.drawable.bg_voilet_top_right_corner);
                }
                shifttitle.setText("your shift");
                openshiftbannerimage.setImageResource(R.drawable.shift_swap);
                if (scheduleDetails.getEstimatedPay() != null) {
                    swapIv.setVisibility(View.VISIBLE);
                    int estPay = LegionUtils.getEstimatedPayAsInt(scheduleDetails.getEstimatedPay());
                    swapIv.setText("Est. $ " + estPay);

                }

            }

            String label;
            if ((regularMins.contains("1."))) {
                label = "Hr";
            } else {
                label = "Hrs";
            }
            hrsTV.setText(label);
            roleTV.setText(scheduleDetails.getRole());
            addressTV.setText(LegionUtils.getUpdatedAddress(scheduleDetails.getBusinessKey().getAddress()));
            String availabilityMatchOrNot = scheduleDetails.getAvailability();
            if (availabilityMatchOrNot != null) {
                if (availabilityMatchOrNot.equalsIgnoreCase("Yes")) {
                    statusAvlbtyIv.setImageResource(R.drawable.availability_match);
                } else if (availabilityMatchOrNot.equalsIgnoreCase("No")) {
                    statusAvlbtyIv.setImageResource(R.drawable.availability_mismatch);
                } else {
                    statusAvlbtyIv.setImageResource(R.drawable.availability_unknown);
                }

            }

            int startHour = convertFromMinutesToHoursInt(scheduleDetails.getStartMin());
            int periodImage = 0;
            String periodColor;
            if (startHour != 0) {
                if (startHour >= 5 && startHour < 9) {
                    periodImage = R.drawable.ic_sun_early_morning; //5Am9Am
                    periodColor = "#fab817";
                } else if (startHour >= 9 && startHour < 13) { //9Am to 1pm
                    periodImage = R.drawable.ic_sun_morning;
                    periodColor = "#ffa000";
                } else if (startHour >= 13 && startHour < 17) { //1pm to 5pm
                    periodImage = R.drawable.ic_sun_noon;
                    periodColor = "#f57c00";
                } else if (startHour >= 17 && startHour < 21) {  //5pm to 9pm
                    periodImage = R.drawable.ic_sun_evening;
                    periodColor = "#e75c15";
                } else {
                    periodImage = R.drawable.ic_moon; //9Pm12Am
                    periodColor = "#9a1010";
                }
                lineView.setBackgroundColor(Color.parseColor(periodColor));
                roleTV.setTextColor(Color.parseColor(periodColor));
            }
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            TextView statusTv = new TextView(getActivity());
            statusTv.setTextColor(ActivityCompat.getColor(getActivity(), R.color.light_black));
            if (currentDaySelect.equalsIgnoreCase(dateTv)) {
                statusTv.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                dateViewLl.setBackgroundResource(R.drawable.bg_blue_schedule);
                          /* ----------------  checking Shift difference in 60 minutes or not. if diff is <60 Time Tick reciever register ow not -----------------*/
                if (difference > 0 && (difference <= 60)) {
                    if (!shitStartedOrNot) {
                        startShiftTv = (TextView) shiftLayout.findViewById(R.id.startShiftTv);
                        shiftDate = scheduleDetails.getShiftStartDate();
                        startShiftTv.setVisibility(View.VISIBLE);
                        startShiftTv.setText("   STARTING IN " + difference + (difference == 1 ? " MINUTE" : " MINUTES"));
                        if (scheduleDetails.isstaffinShiftAdded() || scheduleDetails.isShiftoffered() || scheduleDetails.isShiftRequested()) {
                            startShiftTv.setBackgroundColor(ActivityCompat.getColor(getActivity(), R.color.time_indicator_cr));
                        }
                        backgroundLL.setBackgroundResource(R.drawable.bg_light_blue_with_bottom_right_corner);
                        registerUpdateTimeReciever();
                        shitStartedOrNot = true;
                    }
                }
            }
            if (scheduleDetails.isstaffinShiftAdded()) {          // scroll the view based on openshift
                isshiftorswapscrolled = true;
                scrollLayoutView(scheduleDetails, dateViewLl);
            } else if (scheduleDetails.isShiftRequested()) {         //scroll the view based on swapshift
                scrollLayoutView(scheduleDetails, dateViewLl);
                isshiftorswapscrolled = true;
            } else if (currentDaySelect.equalsIgnoreCase(dateTv)) {   //scroll the view based on currentday
                if (!isshiftorswapscrolled) {
                    try {
                        final boolean[] scrollCertainPos = {true};
                        ViewTreeObserver viewTreeObserver = dateViewLl.getViewTreeObserver();
                        if (viewTreeObserver.isAlive()) {
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        dateViewLl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    }
                                    if (scrollCertainPos[0]) {
                                        scrollCertainPos[0] = false;
                                            /*if(dayNameTVText.getText().toString().contains("MON")){
                                                scrollView.fullScroll(View.FOCUS_UP);
                                            }else if(dayNameTVText.getText().toString().contains("SUN")){
                                                scrollView.fullScroll(View.FOCUS_DOWN);
                                            }*/
                                        if (scheduleDetails.getDayOfTheWeek().equalsIgnoreCase("1")) {
                                            scrollView.fullScroll(View.FOCUS_UP);
                                        } else if (scheduleDetails.getDayOfTheWeek().equalsIgnoreCase("2")) {
                                            scrollView.fullScroll(View.FOCUS_UP);
                                        } else if (scheduleDetails.getDayOfTheWeek().equalsIgnoreCase("7")) {
                                            scrollView.fullScroll(View.FOCUS_DOWN);
                                        } else if (scheduleDetails.getDayOfTheWeek().equalsIgnoreCase("6")) {
                                            scrollView.fullScroll(View.FOCUS_DOWN);
                                        } else {
                                            scrollView.scrollTo(0, dateViewLl.getHeight());
                                        }

                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.d("Exception", "height not getting");
                    }
                }
            }
            statusTv.setTag(getString(R.string.mallory_medium_regular));
            if (len == 1) {
                tableRow.setGravity(Gravity.CENTER);
                statusTv.setGravity(Gravity.CENTER);
                statusTv.setTextSize(size);
                if (scheduleDetails.getClockInTime() != null && ((scheduleDetails.getClockInTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0")) && ((scheduleDetails.getClockOutTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0"))) {
                    statusTv.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                    statusTv.setPadding(0, 0, 0, 50);
                    statusTv.setText(Html.fromHtml(LegionUtils.getUpdatedString(dayName, dateTv)));
                } else {
                    statusTv.setText(Html.fromHtml(LegionUtils.getUpdatedString(dayName, dateTv)));
                    statusTv.setPadding(0, 20, 0, 23);
                    statusTv.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                }
                statusTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, periodImage);
            } else {
                tableRow.setGravity(Gravity.BOTTOM);
                statusTv.setGravity(Gravity.BOTTOM);
                statusTv.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
            }
            statusTv.setMinWidth(minWidth);

            if (checkOddOrEven && (len > 1)) { //even
                if ((i > (middle - 1)) && (len > 1)) {
                    if (scheduleDetails.getClockInTime() != null && ((scheduleDetails.getClockInTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0")) && ((scheduleDetails.getClockOutTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0"))) {
                        if (i == len - 1)
                            statusTv.setPadding(0, paddingSmall + paddingNormal + 10, 0, paddingSmall + 10);
                        else {
                            statusTv.setPadding(0, paddingSmall + 10, 0, paddingNormal + 10);
                        }
                    } else {
                        statusTv.setPadding(0, 0, 0, paddingNormal);
                    }
                    statusTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, periodImage);
                } else {
                    boolean checkB4ClockInOut = true;
                    if (scheduleDetails.getClockInTime() != null && ((scheduleDetails.getClockInTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0")) && ((scheduleDetails.getClockOutTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0"))) {
                        if (i == 0) {
                            if (checkB4ClockInOut)
                                statusTv.setPadding(0, paddingSmall + 10, 0, paddingBig + 10);
                            else {
                                statusTv.setPadding(0, paddingNormal, 0, paddingBig + 10);
                            }
                        } else {
                            statusTv.setPadding(0, paddingNormal, 0, paddingSmall);
                        }
                    } else {

                        if (i == 0) {
                            statusTv.setPadding(0, paddingSmall, 0, 0);
                        } else {
                            statusTv.setPadding(0, paddingNormal, 0, 0);
                        }
                        checkB4ClockInOut = false;
                    }
                    statusTv.setCompoundDrawablesWithIntrinsicBounds(0, periodImage, 0, 0);
                }
                setMarginsToView(lineViewClock, 0, 0, 0, 0);
            } else {
                if ((i > (middle - 1)) && (len > 1)) { //odd
                    tableRow.setGravity(Gravity.CENTER);

                    if (!executedMiddleItem) {
                        executedMiddleItem = true;
                        statusTv.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        statusTv.setTextSize(size);
                        statusTv.setText(Html.fromHtml(LegionUtils.getUpdatedString(dayName, dateTv)) + "\n");
                        statusTv.setGravity(Gravity.CENTER);
                        statusTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, periodImage);
                    } else {
                        if (i != 0)
                            if (scheduleDetails.getClockInTime() != null && ((scheduleDetails.getClockInTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0")) && ((scheduleDetails.getClockOutTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0"))) {
                                statusTv.setPadding(0, paddingBig, 0, paddingSmall);
                            } else {
                                statusTv.setPadding(0, 0, 0, paddingSmall);
                            }
                        statusTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, periodImage);
                    }

                } else {
                    if (len > 1) {
                        boolean checkB4ClockInOut = true;
                        if (scheduleDetails.getClockInTime() != null && ((scheduleDetails.getClockInTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0")) && ((scheduleDetails.getClockOutTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0"))) {
                            if (!checkB4ClockInOut) {
                                statusTv.setPadding(0, 0, 0, paddingBig);
                            } else {
                                statusTv.setPadding(0, paddingSmall, 0, paddingBig);
                            }
                            checkB4ClockInOut = true;
                        } else {
                            checkB4ClockInOut = false;
                            statusTv.setPadding(0, paddingSmall, 0, 0);
                        }
                        statusTv.setCompoundDrawablesWithIntrinsicBounds(0, periodImage, 0, 0);
                    }
                }
                setMarginsToView(lineViewClock, 0, 0, 12, 0);
            }
            LegionUtils.applyFont(getActivity().getAssets(), statusTv);
            tableRow.addView(statusTv);
            TableRow tableRow1 = null;
            if ((i == middle) && (len > 1) && (checkOddOrEven)) {
                tableRow1 = new TableRow(getActivity());
                tableRow1.setGravity(Gravity.CENTER);
                tableRow1.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 0.5f));
                TextView middleDayTextinEven = new TextView(getActivity());
                middleDayTextinEven.setTextSize(size);
                middleDayTextinEven.setGravity(Gravity.CENTER);
                middleDayTextinEven.setText(Html.fromHtml(LegionUtils.getUpdatedString(dayName, dateTv)));
                middleDayTextinEven.setTag(getString(R.string.mallory_medium_regular));
                middleDayTextinEven.setTextColor(ActivityCompat.getColor(getActivity(), R.color.light_black));
                if (currentDaySelect.equalsIgnoreCase(dateTv)) {
                    middleDayTextinEven.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                    dateViewLl.setBackgroundResource(R.drawable.bg_blue_schedule);

                }
                LegionUtils.applyFont(getActivity().getAssets(), middleDayTextinEven);
                middleDayTextinEven.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                tableRow1.addView(middleDayTextinEven);
            }

            if (tableRow1 != null) {
                dateViewLl.addView(tableRow1);
            }
            dateViewLl.addView(tableRow);
            TextView clockInTv = (TextView) shiftLayout.findViewById(R.id.clockInTv);
            TextView clockOutTv = (TextView) shiftLayout.findViewById(R.id.clockOutTv);
            TextView totalHrsTv = (TextView) shiftLayout.findViewById(R.id.totalHrsTv);
            TextView totalMinsTv = (TextView) shiftLayout.findViewById(R.id.totalMinsTv);
            LinearLayout clockInOrOutLL = (LinearLayout) shiftLayout.findViewById(R.id.clockInOrOutLL);
            clockInOrOutLL.setMinimumHeight(60);
            if (scheduleDetails.getClockInTime() != null && ((scheduleDetails.getClockInTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0")) && ((scheduleDetails.getClockOutTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0"))) {
                clockInTv.setText(LegionUtils.convertFromMillsToTime(Long.parseLong(scheduleDetails.getClockInTime())));
                clockOutTv.setText(LegionUtils.convertFromMillsToTime(Long.valueOf(scheduleDetails.getClockOutTime())));
                ArrayList timeAndMins = LegionUtils.getDifferenceBnTwoMillis(Long.parseLong(scheduleDetails.getClockInTime()), Long.parseLong(scheduleDetails.getClockOutTime()));
                if (timeAndMins != null && (timeAndMins.size() == 2)) {
                    totalHrsTv.setText(timeAndMins.get(0).toString());
                    totalMinsTv.setText(timeAndMins.get(1).toString());
                }
                clockInOrOutLL.setVisibility(View.VISIBLE);
                clockInOrOutLL.setBackgroundResource(R.drawable.bg_clock_right);
            } else {
                clockInOrOutLL.setVisibility(View.GONE);
            }
            shiftsContainerLayoutInDay.addView(shiftLayout);
            final int pos = i;
            shiftLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shiftOffer != null) {
                        return; //disabling the click if we have come from Shift Offers screen
                    }
                    try {
                        Intent intent = new Intent(getActivity(), ScheduleShiftDetailsActivity.class);
                        TextView dayOfTheWeekTv1 = (TextView) v.findViewById(R.id.dayOfTheWeekTv);
                        TextView hrsTv = (TextView) v.findViewById(R.id.hrsTV);
                        TextView statTimeTV = (TextView) v.findViewById(R.id.statTimeTV);
                        TextView endTimTv = (TextView) v.findViewById(R.id.endTimeTV);
                        String[] dataList = dayOfTheWeekTv1.getText().toString().split(",");
                        ScheduleWorkerShift workShiftList = null;
                        ArrayList<ScheduleWorkerShift> shiftsList = dayToShiftsList.get(Integer.parseInt(dataList[0]));
                        for (int i = 0; i < shiftsList.size(); i++) {
                            if (shiftsList.get(i).getPosition() == Integer.valueOf(dataList[2])) {
                                workShiftList = shiftsList.get(i);
                                break;
                            }
                        }
                        intent.putExtra("workerShiftsList", workShiftList);
                        TextView tvHrs = (TextView) v.findViewById(R.id.durationTV);
                        intent.putExtra(Extras_Keys.STARTTIME, statTimeTV.getText().toString());
                        intent.putExtra(Extras_Keys.ENDTIME, endTimTv.getText().toString());
                        legionPreferences.save(Prefs_Keys.REFRESH, "DONTREFRESH");
                        intent.putExtra(Extras_Keys.SHIFT_MINS, numOfHoursTV.getText().toString());
                        intent.putExtra("totalShiftHours", tvHrs.getText().toString() + " " + hrsTv.getText().toString());
                        if (workerShift != null)
                            getActivity().startActivityForResult(intent, Extras_Keys.REQUEST_CODE);
                    } catch (Exception e) {
                        Log.d("Exception", e.getLocalizedMessage());
                    }
                }
            });


        }


        // ---------------------    PTO LAYOUT --------------------------------------//

        if (isPTOTrueOrNot) {
            Log.d("position", "" + position);
            final LinearLayout mainPtoLayout = (LinearLayout) inflater.inflate(R.layout.fragment_past_schedule_pto_layout, null);
            LinearLayout ptoContainerLayout = (LinearLayout) mainPtoLayout.findViewById(R.id.ptoContainerLayout);
            backgroundLL.setBackgroundResource(R.drawable.bg_pto_with_corners);
            try {
                String ptoDay = PTODayOfTheWeek;
                boolean checkMultiplePtos = false;
                for (int i = Integer.valueOf(PTODayOfTheWeek); i < slotArrayList.size(); i++) {
                    final LinearLayout ptoRowLayout = (LinearLayout) inflater.inflate(R.layout.past_scedule_pto_row_day_layout, null);
                    View lineViewPto = (View) ptoRowLayout.findViewById(R.id.lineViewPto);
                    if (ptoDay.equalsIgnoreCase("" + slotArrayList.get(i).getDayOfTheWeek())) {
                        final TextView dayNameTVText = (TextView) ptoRowLayout.findViewById(R.id.dayNameTV);
                        if ((position == 1) || (emptyLineViewBoolean)) {
                            emptyLineViewBoolean = false;
                        }
                        emptyLineViewBoolean = true;
                        dayNameTVText.setTextSize(size);
                        dayNameTVText.setTag(getString(R.string.mallory_medium_regular));
                        dayNameTVText.setGravity(Gravity.CENTER);
                        dayNameTVText.setTextColor(ActivityCompat.getColor(getActivity(), R.color.light_gray_2));
                        dayNameTVText.setText(Html.fromHtml(LegionUtils.getUpdatedString(dayName, dateTv)));
                        dayNameTVText.setMinWidth(minWidth);
                        LegionUtils.applyFont(getActivity().getAssets(), dayNameTVText);
                        if (checkMultiplePtos) {
                            lineViewPto.setVisibility(View.VISIBLE);
                        } else {
                            lineViewPto.setVisibility(View.GONE);
                        }
                        if (currentDaySelect.equalsIgnoreCase(dateTv) && checkMultiplePtos) {
                            dayNameTVText.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                            dayNameTVText.setBackgroundResource(R.drawable.bg_blue_top_one_corner);
                        } else if (currentDaySelect.equalsIgnoreCase(dateTv) && (PTOSkipDayCount > 1)) {
                            dayNameTVText.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                            dayNameTVText.setBackgroundResource(R.drawable.bg_blue_no_corners);
                        } else if (currentDaySelect.equalsIgnoreCase(dateTv)) {
                            dayNameTVText.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                            dayNameTVText.setBackgroundResource(R.drawable.bg_blue_schedule);
                        }
                        if (currentDaySelect.equalsIgnoreCase(dateTv)) {
                            final boolean[] scrollCertainHeight = {true};
                            try {
                                ViewTreeObserver viewTreeObserver = mainPtoLayout.getViewTreeObserver();
                                if (viewTreeObserver.isAlive()) {
                                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                        @Override
                                        public void onGlobalLayout() {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                                mainPtoLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                            }

                                            if (scrollCertainHeight[0]) {
                                                scrollCertainHeight[0] = false;
                                                if (dayNameTVText.getText().toString().contains("MON")) {
                                                    scrollView.fullScroll(View.FOCUS_UP);
                                                } else if (dayNameTVText.getText().toString().contains("TUE")) {
                                                    scrollView.fullScroll(View.FOCUS_UP);
                                                } else if (dayNameTVText.getText().toString().contains("SAT")) {
                                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                                } else if (dayNameTVText.getText().toString().contains("SUN")) {
                                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                                } else {
                                                    scrollView.scrollTo(0, mainPtoLayout.getHeight());///here positions giving (0,0)
                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.d("Exception", "height not getting");
                            }
                        }
                        LegionUtils.applyFont(getActivity().getAssets(), mainPtoLayout.findViewById(R.id.timeOfTV));
                        ptoContainerLayout.addView(ptoRowLayout);

                        if (((i + 1) < 8) && (i + 1 < slotArrayList.size()) && !(String.valueOf(slotArrayList.get(i + 1).getDayOfTheWeek()).equalsIgnoreCase("0")) && (dayToShiftsList.get(i + 1).size() == 0)) {
                            PTOSkipDayCount = PTOSkipDayCount + 1;
                            ptoDay = "" + slotArrayList.get(i + 1).getDayOfTheWeek();
                            checkMultiplePtos = true;
                            if ((i + 1) == 1) {
                                dayName = ("MON");
                                dateTv = (datesArrayList.get(0).toString());
                                currentDaySelect = currentDateCheckingArrayList.get(0).toString();

                            } else if ((i + 1) == 2) {
                                dayName = ("TUE");
                                currentDaySelect = currentDateCheckingArrayList.get(1).toString();
                                dateTv = (datesArrayList.get(1).toString());
                            } else if ((i + 1) == 3) {
                                dayName = ("WED");
                                currentDaySelect = currentDateCheckingArrayList.get(2).toString();
                                dateTv = (datesArrayList.get(2).toString());
                            } else if ((i + 1) == 4) {
                                dayName = ("THU");
                                currentDaySelect = currentDateCheckingArrayList.get(3).toString();
                                dateTv = (datesArrayList.get(3).toString());
                            } else if ((i + 1) == 5) {
                                dayName = ("FRI");
                                currentDaySelect = currentDateCheckingArrayList.get(4).toString();
                                dateTv = (datesArrayList.get(4).toString());
                            } else if ((i + 1) == 6) {
                                dayName = ("SAT");
                                currentDaySelect = currentDateCheckingArrayList.get(5).toString();
                                dateTv = (datesArrayList.get(5).toString());
                            } else if ((i + 1) == 7) {
                                dayName = ("SUN");
                                currentDaySelect = currentDateCheckingArrayList.get(6).toString();
                                dateTv = (datesArrayList.get(6).toString());
                            }

                        } else {
                            if (checkMultiplePtos) {
                                if (currentDaySelect.equalsIgnoreCase(dateTv) && checkMultiplePtos) {
                                    dayNameTVText.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                                    dayNameTVText.setBackgroundResource(R.drawable.bg_blue_bottom_one_corner);
                                }
                            }
                            checkMultiplePtos = false;
                            break;
                        }
                    } else {
                        break;
                    }

                }
                shiftsContainerLayoutInDay.addView(mainPtoLayout);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            // ---------------------    EMPTY LAYOUT --------------------------------------//
            if ((workerShift.size() == 0)) {
                final LinearLayout shiftLayout = (LinearLayout) inflater.inflate(R.layout.fragment_past_schedule_empty_layout, null);
                LinearLayout dayViewLL = (LinearLayout) shiftLayout.findViewById(R.id.dayViewLL);

                final TextView dayNameTVText = (TextView) dayViewLL.findViewById(R.id.dayNameTV);
                View emptyLineView = (View) shiftLayout.findViewById(R.id.emptyLineView);
                if ((position == 1) || (emptyLineViewBoolean)) {
                    emptyLineView.setVisibility(View.GONE);
                    emptyLineViewBoolean = false;
                }
                dayNameTVText.setTextSize(size);
                dayNameTVText.setTag(getString(R.string.mallory_medium_regular));
                dayNameTVText.setGravity(Gravity.CENTER);
                dayNameTVText.setTextColor(ActivityCompat.getColor(getActivity(), R.color.light_gray_2));
                if (currentDaySelect.equalsIgnoreCase(dateTv)) {
                    final boolean[] scrollCertainHeight = {true};
                    dayNameTVText.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                    dayViewLL.setBackgroundResource(R.drawable.bg_blue_schedule);
                    shiftLayoutLL = (LinearLayout) inflater.inflate(R.layout.fragment_past_schedule_empty_layout, null);
                    try {
                        ViewTreeObserver viewTreeObserver = shiftLayout.getViewTreeObserver();
                        if (viewTreeObserver.isAlive()) {
                            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        shiftLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    }

                                    if (scrollCertainHeight[0]) {
                                        scrollCertainHeight[0] = false;
                                        if (dayNameTVText.getText().toString().contains("SUN")) {
                                            scrollView.fullScroll(View.FOCUS_DOWN);
                                        } else if (dayNameTVText.getText().toString().contains("MON")) {
                                            scrollView.fullScroll(View.FOCUS_UP);
                                        } else if (dayNameTVText.getText().toString().contains("SAT")) {
                                            scrollView.scrollTo(0, shiftLayout.getHeight() + 200);
                                        } else {
                                            scrollView.scrollTo(0, shiftLayout.getHeight());
                                        }
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Exception", "height not getting");
                    }
                }
                dayNameTVText.setText(Html.fromHtml(LegionUtils.getUpdatedString(dayName, dateTv)));
                dayNameTVText.setMinWidth(minWidth);
                // dateTvv.setText(dateTv);
                LegionUtils.applyFont(getActivity().getAssets(), dayNameTVText);
                bgChangeLL.setBackgroundResource(R.drawable.bg_gray_left);
                shiftLayout.setBackgroundResource(R.drawable.bg_gray_right);
                shiftsContainerLayoutInDay.addView(shiftLayout);
            }
        }
        mainLl.addView(dayLayout);
    }

    public String loadPtoRespnseFromAssets() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("pto.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        Log.d("json", json);
        return json;
    }

    private void scrollLayoutView(final ScheduleWorkerShift scheduleDetailssc, final LinearLayout dateViewLl) {

        {
            try {
                final boolean[] scrollCertainPos = {true};
                ViewTreeObserver viewTreeObserver = dateViewLl.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                dateViewLl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                            if (scrollCertainPos[0]) {
                                scrollCertainPos[0] = false;
                                            /*if(dayNameTVText.getText().toString().contains("MON")){
                                                scrollView.fullScroll(View.FOCUS_UP);
                                            }else if(dayNameTVText.getText().toString().contains("SUN")){
                                                scrollView.fullScroll(View.FOCUS_DOWN);
                                            }*/
                                if (scheduleDetailssc.getDayOfTheWeek().equalsIgnoreCase("1")) {
                                    scrollView.fullScroll(View.FOCUS_UP);
                                } else if (scheduleDetailssc.getDayOfTheWeek().equalsIgnoreCase("2")) {
                                    scrollView.fullScroll(View.FOCUS_UP);
                                } else if (scheduleDetailssc.getDayOfTheWeek().equalsIgnoreCase("7")) {
                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                } else if (scheduleDetailssc.getDayOfTheWeek().equalsIgnoreCase("6")) {
                                    scrollView.fullScroll(View.FOCUS_DOWN);
                                } else {
                                    scrollView.scrollTo(0, dateViewLl.getHeight());
                                }

                            }
                        }
                    });
                }
            } catch (Exception e) {
                Log.d("Exception", "height not getting");
            }
        }
    }

    private void addViewsToLayout(final ArrayList<ScheduleWorkerShift> workerShiftsList) {
        new Handler().postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          try {
                                              dayToShiftsList = new LinkedHashMap<>();
                                              ArrayList<ScheduleWorkerShift> day1ShiftsList = new ArrayList<>();
                                              ArrayList<ScheduleWorkerShift> day2ShiftsList = new ArrayList<>();
                                              ArrayList<ScheduleWorkerShift> day3ShiftsList = new ArrayList<>();
                                              ArrayList<ScheduleWorkerShift> day4ShiftsList = new ArrayList<>();
                                              ArrayList<ScheduleWorkerShift> day5ShiftsList = new ArrayList<>();
                                              ArrayList<ScheduleWorkerShift> day6ShiftsList = new ArrayList<>();
                                              ArrayList<ScheduleWorkerShift> day7ShiftsList = new ArrayList<>();

                                              if (shiftOffer != null && !shiftOffer.isShiftSwapOffer()) {
                                                  shiftOffer.getStaffingShift().getDayOfTheWeek();
                                                  ScheduleWorkerShift shiftOfferShedulewShift = new ScheduleWorkerShift();//new ScheduleWorkerShift();
                                                  shiftOfferShedulewShift.setIsstaffinShiftAdded(true);
                                                  shiftOfferShedulewShift.setShiftId(shiftOffer.getStaffingShift().getShiftId());
                                                  shiftOfferShedulewShift.setEstimatedPay(shiftOffer.getEstimatedPay());
                                                  shiftOfferShedulewShift.setBusinessKey(shiftOffer.getStaffingShift().getBusinessKey());
                                                  shiftOfferShedulewShift.setStartMin(shiftOffer.getStaffingShift().getStartMin());
                                                  shiftOfferShedulewShift.setDayOfTheWeek(shiftOffer.getStaffingShift().getDayOfTheWeek());
                                                  shiftOfferShedulewShift.setEndMin(shiftOffer.getStaffingShift().getEndMin());
                                                  // shiftOfferShedulewShift.setPosition(shiftOffer.getStaffingShift().getPEstimatedPay());

                                                  shiftOfferShedulewShift.setShiftStartDate(shiftOffer.getStaffingShift().getShiftStartDate());
                                                  shiftOfferShedulewShift.setRegularMinutes(shiftOffer.getStaffingShift().getRegularMinutes());

                                                  shiftOfferShedulewShift.setRole(shiftOffer.getStaffingShift().getRole());
                                                  shiftOfferShedulewShift.setAvailability(shiftOffer.getStaffingShift().getAvailability());
                                                  shiftOfferShedulewShift.setStartMin(shiftOffer.getStaffingShift().getStartMin());
                                                  // shiftOfferShedulewShift.getClockInTime(shiftOffer.getStaffingShift().getcClockintime);
                                                  if (shiftOffer.getStaffingShift().getDayOfTheWeek().equals("1")) {
                                                      day1ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getStaffingShift().getDayOfTheWeek().equals("2")) {
                                                      day2ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getStaffingShift().getDayOfTheWeek().equals("3")) {
                                                      day3ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getStaffingShift().getDayOfTheWeek().equals("4")) {
                                                      day4ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getStaffingShift().getDayOfTheWeek().equals("5")) {
                                                      day5ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getStaffingShift().getDayOfTheWeek().equals("6")) {
                                                      day6ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getStaffingShift().getDayOfTheWeek().equals("7")) {
                                                      day7ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                              }
                                              if (shiftOffer != null && shiftOffer.isShiftSwapOffer()) {
                                                  shiftOffer.getShiftOffered().getDayOfTheWeek();
                                                  ScheduleWorkerShift shiftOfferShedulewShift = new ScheduleWorkerShift();//new ScheduleWorkerShift();
                                                  shiftOfferShedulewShift.setShiftoffered(true);
                                                  shiftOfferShedulewShift.setShiftId(shiftOffer.getShiftOffered().getShiftId());
                                                  shiftOfferShedulewShift.setEstimatedPay(shiftOffer.getShiftOffered().getEstimatedPay());
                                                  shiftOfferShedulewShift.setBusinessKey(shiftOffer.getShiftOffered().getBusinessKey());
                                                  shiftOfferShedulewShift.setStartMin(shiftOffer.getShiftOffered().getStartMin());
                                                  shiftOfferShedulewShift.setDayOfTheWeek(shiftOffer.getShiftOffered().getDayOfTheWeek());
                                                  shiftOfferShedulewShift.setEndMin(shiftOffer.getShiftOffered().getEndMin());
                                                  // shiftOfferShedulewShift.setPosition(shiftOffer.getShiftOffered().getPEstimatedPay());

                                                  shiftOfferShedulewShift.setShiftStartDate(shiftOffer.getShiftOffered().getShiftStartDate());
                                                  shiftOfferShedulewShift.setRegularMinutes(shiftOffer.getShiftOffered().getRegularMinutes());

                                                  shiftOfferShedulewShift.setRole(shiftOffer.getShiftOffered().getRole());
                                                  shiftOfferShedulewShift.setAvailability(shiftOffer.getShiftOffered().getAvailability());
                                                  shiftOfferShedulewShift.setStartMin(shiftOffer.getShiftOffered().getStartMin());
                                                  // shiftOfferShedulewShift.getClockInTime(shiftOffer.getShiftOffered().getcClockintime);
                                                  if (shiftOffer.getShiftOffered().getDayOfTheWeek().equals("1")) {
                                                      day1ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftOffered().getDayOfTheWeek().equals("2")) {
                                                      day2ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftOffered().getDayOfTheWeek().equals("3")) {
                                                      day3ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftOffered().getDayOfTheWeek().equals("4")) {
                                                      day4ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftOffered().getDayOfTheWeek().equals("5")) {
                                                      day5ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftOffered().getDayOfTheWeek().equals("6")) {
                                                      day6ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftOffered().getDayOfTheWeek().equals("7")) {
                                                      day7ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                              }
                                              if (shiftOffer != null && shiftOffer.isShiftSwapOffer()) {
                                                  shiftOffer.getShiftRequested().getDayOfTheWeek();
                                                  if (shiftOffer.getShiftOffered().getDayOfTheWeek().equalsIgnoreCase(shiftOffer.getShiftRequested().getDayOfTheWeek())) {
                                                      isshiftsameday = true;
                                                  }
                                                  ScheduleWorkerShift shiftOfferShedulewShift = new ScheduleWorkerShift();//new ScheduleWorkerShift();
                                                  shiftOfferShedulewShift.setShiftRequested(true);
                                                  shiftOfferShedulewShift.setShiftId(shiftOffer.getShiftRequested().getShiftId());
                                                  shiftOfferShedulewShift.setEstimatedPay(shiftOffer.getShiftRequested().getEstimatedPay());
                                                  shiftOfferShedulewShift.setBusinessKey(shiftOffer.getShiftRequested().getBusinessKey());
                                                  shiftOfferShedulewShift.setStartMin(shiftOffer.getShiftRequested().getStartMin());
                                                  shiftOfferShedulewShift.setDayOfTheWeek(shiftOffer.getShiftRequested().getDayOfTheWeek());
                                                  shiftOfferShedulewShift.setEndMin(shiftOffer.getShiftRequested().getEndMin());
                                                  // shiftOfferShedulewShift.setPosition(shiftOffer.getShiftRequested().getPEstimatedPay());

                                                  shiftOfferShedulewShift.setShiftStartDate(shiftOffer.getShiftRequested().getShiftStartDate());
                                                  shiftOfferShedulewShift.setRegularMinutes(shiftOffer.getShiftRequested().getRegularMinutes());

                                                  shiftOfferShedulewShift.setRole(shiftOffer.getShiftRequested().getRole());
                                                  shiftOfferShedulewShift.setAvailability(shiftOffer.getShiftRequested().getAvailability());
                                                  shiftOfferShedulewShift.setStartMin(shiftOffer.getShiftRequested().getStartMin());
                                                  // shiftOfferShedulewShift.getClockInTime(shiftOffer.getShiftRequested().getcClockintime);
                                                  if (shiftOffer.getShiftRequested().getDayOfTheWeek().equals("1")) {
                                                      day1ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftRequested().getDayOfTheWeek().equals("2")) {
                                                      day2ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftRequested().getDayOfTheWeek().equals("3")) {
                                                      day3ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftRequested().getDayOfTheWeek().equals("4")) {
                                                      day4ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftRequested().getDayOfTheWeek().equals("5")) {
                                                      day5ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftRequested().getDayOfTheWeek().equals("6")) {
                                                      day6ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                                  if (shiftOffer.getShiftRequested().getDayOfTheWeek().equals("7")) {
                                                      day7ShiftsList.add(shiftOfferShedulewShift);
                                                  }
                                              }

                                              for (ScheduleWorkerShift shift : workerShiftsList) {
                                                  if (shift.getDayOfTheWeek().equals("1")) {
                                                      //  day1ShiftsList.add(shift);
                                                      day1ShiftsList = checkDuplicateAdd(shift, day1ShiftsList);

                                                  } else if (shift.getDayOfTheWeek().equals("2")) {
                                                      // day2ShiftsList.add(shift);
                                                      day2ShiftsList = checkDuplicateAdd(shift, day2ShiftsList);

                                                  } else if (shift.getDayOfTheWeek().equals("3")) {
                                                      // day3ShiftsList.add(shift);
                                                      day3ShiftsList = checkDuplicateAdd(shift, day3ShiftsList);

                                                  } else if (shift.getDayOfTheWeek().equals("4")) {
                                                      // day4ShiftsList.add(shift);
                                                      day4ShiftsList = checkDuplicateAdd(shift, day4ShiftsList);

                                                  } else if (shift.getDayOfTheWeek().equals("5")) {
                                                      //  day5ShiftsList.add(shift);
                                                      day5ShiftsList = checkDuplicateAdd(shift, day5ShiftsList);


                                                  } else if (shift.getDayOfTheWeek().equals("6")) {
                                                      //  day6ShiftsList.add(shift);
                                                      day6ShiftsList = checkDuplicateAdd(shift, day6ShiftsList);

                                                  } else if (shift.getDayOfTheWeek().equals("7")) {
                                                      //  day7ShiftsList.add(shift);
                                                      day7ShiftsList = checkDuplicateAdd(shift, day7ShiftsList);

                                                  }
                                              }


                                              /* ----------------  Sorting ArrayList order based on DayOfTheWeek  -----------------*/
                                              dayToShiftsList.put(1, day1ShiftsList);
                                              dayToShiftsList.put(2, day2ShiftsList);
                                              dayToShiftsList.put(3, day3ShiftsList);
                                              dayToShiftsList.put(4, day4ShiftsList);
                                              dayToShiftsList.put(5, day5ShiftsList);
                                              dayToShiftsList.put(6, day6ShiftsList);
                                              dayToShiftsList.put(7, day7ShiftsList);
                                              currentClickPosition = 0;
                                              PTOSkipDayCount = 0;
                                              for (Integer i : dayToShiftsList.keySet()) {
                                                  isPTO = false;
                                                  ArrayList<ScheduleWorkerShift> dayShifts = dayToShiftsList.get(i);
                                                  if (dayShifts.size() == 0) {
                                                      /* ---------------- Checking PTO OR Empty Layout  -----------------*/
                                                      if (String.valueOf(slotArrayList.get(i).getDayOfTheWeek()).contains("" + (i))) {
                                                          Log.d("contains", "" + i);
                                                          isPTO = true;
                                                      } else {
                                                          isPTO = false;
                                                      }
                                                  } else {
                                                      isPTO = false;
                                                  }
                                                  boolean ptoNextShift = false;
                                                  if ((i + 1) < 8) {
                                                      ptoNextShift = (dayToShiftsList.get(i).size() > 0);
                                                  }
                                                  if (dayShifts.size() > 0) {
                                                      /* ----------------  Schedule Data Appending to scrollview  -----------------*/
                                                      PTOSkipDayCount = 0;
                                                      setDataToList(dayShifts, i, isPTO, ptoNextShift, String.valueOf(slotArrayList.get(i).getDayOfTheWeek()));
                                                  } else {
                                                      if (PTOSkipDayCount == 0) {
                                                          setDataToList(dayShifts, i, isPTO, ptoNextShift, String.valueOf(slotArrayList.get(i).getDayOfTheWeek()));
                                                      } else {
                                                          PTOSkipDayCount--;
                                                      }
                                                  }
                                              }

                                          } catch (Exception e) {
                                              e.printStackTrace();

                                          }
                                      }
                                  }
                , 0);
    }

    private ArrayList<ScheduleWorkerShift> checkDuplicateAdd(ScheduleWorkerShift shift, ArrayList<ScheduleWorkerShift> existingShiftsArray) {
        ArrayList<ScheduleWorkerShift> shiftsArray = new ArrayList<>();
        shiftsArray.addAll(existingShiftsArray);
        boolean isFound = false;
        if (existingShiftsArray != null && existingShiftsArray.size() > 0) {
            for (ScheduleWorkerShift existingshift : existingShiftsArray) {
                if (existingshift.getShiftId().equals(shift.getShiftId())) {
                    isFound = true;
                    break;
                }
            }
        }
        if (!isFound) {
            shiftsArray.add(shift);
        }
        return shiftsArray;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.destroyDrawingCache();
            swipeRefreshLayout.clearAnimation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
               /* ----------------  Checking Refresh : For Schedule updation   -----------------*/
            if (!legionPreferences.get(Prefs_Keys.REFRESH).equalsIgnoreCase("DONTREFRESH")) {
                if (isViewShown && numOfShiftsTV.getText().toString() != null) {
                    onRefresh();
                    legionPreferences.save(Prefs_Keys.REFRESH, "DONTREFRESH");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        if (reasonPhrase != null) {
            if (requestCode == WebServiceRequestCodes.GET_WEEKLY_SCHEDULES_REQUEST_CODE) {
                LegionUtils.hideProgressDialog();
                if (reasonPhrase.contains("Something went wrong")) {
                    LegionUtils.doLogout(getActivity());
                    return;
                }
            } else if (requestCode == WebServiceRequestCodes.GET_SCHEDULE_SUMMARY_REQUEST_CODE) {
                LegionUtils.hideProgressDialog();
                if (reasonPhrase.contains("Something went wrong")) {
                    if (getActivity() != null)
                        LegionUtils.doLogout(getActivity());
                    return;
                }
            } else if (requestCode == WebServiceRequestCodes.GET_PTO_REQUEST_CODE) {
                LegionUtils.hideProgressDialog();
                if (reasonPhrase.contains("Something went wrong")) {
                    if (getActivity() != null)
                        LegionUtils.doLogout(getActivity());
                    return;
                }
            }
            LegionUtils.showMessageDialog(getActivity(), "Something went wrong! Please try again.", R.drawable.error_transient);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (updateTimeAndDateReceiver != null)
            unregisterUpdateTimeReciever();
    }

    private void setMarginsToView(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
            //  v.setLayoutParams(p);
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
            LegionUtils.hideProgressDialog();
            if (response instanceof Exception) {
                ((Exception) response).printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
                LegionUtils.hideProgressDialog();
                LegionUtils.showMessageDialog(getActivity(), "Oops,Something went wrong! Please try again.", R.drawable.error_transient);
            } else if (response instanceof String) {
                showToast(response.toString());
            } else if (response instanceof ArrayList<?>) {
                workerShiftsList.clear();
                ArrayList<ScheduleWorkerShift> workerShiftsarrayList = (ArrayList<ScheduleWorkerShift>) response;
                workerShiftsList.addAll(workerShiftsarrayList);
                mainLl.removeAllViews();
                addViewsToLayout(workerShiftsList);
                doLoadScheduleSummary();
               new CalendarSyncTask().execute("");
            }
        } else if (parserId == ResponseParserConstants.PARSE_PTO_REQUEST_CODE) {
            LegionUtils.hideProgressDialog();
            if (response instanceof Exception) {
                ((Exception) response).printStackTrace();
                LegionUtils.hideProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
            } else if (response instanceof String) {
                showToast(response.toString());
            } else if (response instanceof ArrayList<?>) {
                slotArrayList = (ArrayList<Slot>) response;
                doLoadWeeklySchedules();
            }
        } else if (parserId == ResponseParserConstants.PARSE_SCHEDULE_SUMMARY_DETAILS) {
            LegionUtils.hideProgressDialog();
            if (response instanceof Exception) {
                ((Exception) response).printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
                LegionUtils.hideProgressDialog();
            } else if (response instanceof String) {
                showToast(response.toString());
            } else if (response instanceof WorkerShiftSummary) {
                WorkerShiftSummary summery = (WorkerShiftSummary) response;
                if (summery.getTotalShift() != null) {
                    numOfShiftsTV.setText(summery.getTotalShift());
                    if (summery.getTotalShift().equals("1")) {
                        shiftTv.setText(" Shift, ");
                    } else {
                        shiftTv.setText(" Shifts, ");
                    }
                }
                numOfHoursTV.setText(convertFromMinutesToHours(summery.getTotalShiftMinutes()));
                if (convertFromMinutesToHours(summery.getTotalShiftMinutes()).equals("1")) {
                    hrsTV.setText(" Hr ");
                } else {
                    hrsTV.setText(" Hrs ");
                }

                claimedTv.setText(checkingStringIsNullOrNot(summery.getTotalOffersClaimed()));
                offersTV.setText(checkingStringIsNullOrNot(summery.getTotalOffers()));
                sceduledPreferedTv.setText(checkingStringIsNullOrNot(convertFromMinutesToHours(summery.getTotalPreferredMinutesScheduled())));
                totalPreferedTv.setText(checkingStringIsNullOrNot(checkingStringIsNullOrNot(convertFromMinutesToHours(summery.getTotalPreferredMinutes()))));
                conflictsTV.setText(checkingStringIsNullOrNot(summery.getTotalConflicts()));
                proposedPayTV.setText("$" + LegionUtils.getEstimatedPayAsInt(summery.getTotalProjectedPay()) + " Projected pay");
            }
        } else if (parserId == ResponseParserConstants.PARSE_SCHEDULES) {
            if (response instanceof Exception) {
                LegionUtils.showDialog(getActivity(), "Oops, Something went wrong.\n\nPlease try again later.", true);
                ((Exception) response).printStackTrace();
            } else if (response instanceof String) {
                showToast(response.toString());
            } else if (response instanceof ArrayList<?>) {
                ArrayList<Schedule> schedulesArrayList = (ArrayList<Schedule>) response;

                for (int i = 0; i < schedulesArrayList.size(); i++) {
                    Schedule scheduleShift = schedulesArrayList.get(i);
                    if (hostFrag != null) {

                        if ((scheduleShift.getWeekStartDayOfTheYear().equalsIgnoreCase(weekStartDayOfTheYear))) {

                            if (checkCurrentWeekOrNot) {
                                hostFrag.setupToolbar(hostFrag.getView(), true, "Current Schedule");
                            } else if (LegionUtils.isPastWeek(startDate.replace("T", " ").replace("Z", " "), endDate.replace("T", " ").replace("Z", " "))) {
                                hostFrag.setupToolbar(hostFrag.getView(), true, "Past Schedule");
                            } else {
                                String scheduleStatus = scheduleShift.getScheduleStatus();
                                if (scheduleStatus.toUpperCase().contains("Final".toUpperCase())) {
                                    hostFrag.setupToolbar(hostFrag.getView(), true, "Upcoming Schedule");
                                } else if (scheduleStatus.toUpperCase().contains("Publish".toUpperCase())) {
                                    hostFrag.setupToolbar(hostFrag.getView(), true, "Upcoming Schedule");
                                } else {
                                    hostFrag.setupToolbar(hostFrag.getView(), true, "Future Schedule");
                                }
                            }
                        } else {
                            if (checkCurrentWeekOrNot) {
                                hostFrag.setupToolbar(hostFrag.getView(), true, "Current Schedule");
                            } else if (LegionUtils.isPastWeek(startDate.replace("T", " ").replace("Z", " "), endDate.replace("T", " ").replace("Z", " "))) {
                                hostFrag.setupToolbar(hostFrag.getView(), true, "Past Schedule");
                            } else {
                                hostFrag.setupToolbar(hostFrag.getView(), true, "Upcoming Schedule"); //Future Schedule
                            }
                        }
                    }
                }
            }
        }
    }
    private class CalendarSyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                // Adding event to calendar
                if (legionPreferences.hasKey(Prefs_Keys.SWITCH_CALENDAR) && legionPreferences.getInt(Prefs_Keys.SWITCH_CALENDAR) == 1) {
                    if (CalendarHelper.haveCalendarReadWritePermissions(getActivity())) {
                        try {
                            Hashtable<String, String> calendarIdTable = CalendarHelper.listCalendarId(getActivity());
                            LinkedHashMap<Integer, Boolean> dayNumbersToStatusMap = new LinkedHashMap<>();
                            dayNumbersToStatusMap.put(1, false);
                            dayNumbersToStatusMap.put(2, false);
                            dayNumbersToStatusMap.put(3, false);
                            dayNumbersToStatusMap.put(4, false);
                            dayNumbersToStatusMap.put(5, false);
                            dayNumbersToStatusMap.put(6, false);
                            dayNumbersToStatusMap.put(7, false);
                            int calendarId = 0;
                            for (int i = 0; i < workerShiftsList.size(); i++) {
                                String startOfWeekDate = workerShiftsList.get(i).getShiftStartDate();
                                String endOfWeekDate = workerShiftsList.get(i).getShiftEndDate();
                                dayNumbersToStatusMap.put(Integer.parseInt(workerShiftsList.get(i).getDayOfTheWeek()), true);
                                if (!LegionUtils.isPastWeek(startOfWeekDate.replace("T", " ").replace("Z", " "), endOfWeekDate.replace("T", " ").replace("Z", " "))) {
                                    calendarId = CalendarHelper.updateCalendarId(getActivity(), calendarIdTable);
                                    if (calendarId >= 0) {
                                        CalendarHelper.MakeNewCalendarEntry(workerShiftsList.get(i), getActivity(), calendarId);
                                    }
                                }
                            }
                            String shiftStartDate = legionPreferences.get(Prefs_Keys.START_DATE);
                            int index = -1;
                            for (LinkedHashMap.Entry<Integer, Boolean> entry : dayNumbersToStatusMap.entrySet()) {
                                ++index;
                                if (!dayNumbersToStatusMap.get(entry.getKey())) {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    Calendar calendar = Calendar.getInstance();
                                    Date date = null;
                                    try {
                                        date = formatter.parse(LegionUtils.addDaysToDate(shiftStartDate, index));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    calendar.setTime(date);
                                    String newdt = formatter.format(calendar.getTime());
                                    calendarId = CalendarHelper.updateCalendarId(getActivity(), calendarIdTable);
                                    CalendarHelper.getDeleteEventId(getActivity(), newdt, calendarId);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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
