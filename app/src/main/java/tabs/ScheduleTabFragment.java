package tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapters.SchedulesAdapter;
import asynctasks.ResponseParserTask;
import base.Legion_BaseFragment;
import co.legion.client.R;
import fragments.ScheduleOverviewDetailsFragment;
import interfaces.ResponseParserListener;
import models.BusinessKey;
import models.Schedule;
import models.ShiftOffer;
import models.WorkerKey;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/22/2016.
 */
public class ScheduleTabFragment extends Legion_BaseFragment implements Legion_NetworkCallback, ResponseParserListener, Legion_Constants, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView schedulesListview;
    private ArrayList<Schedule> schedulesList = new ArrayList<>();
    private SchedulesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noSchedulesLayout;
    private int notificationPosition;
    private ImageView noShiftsIcon;
    private TextView noShiftsTextview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedules, null);
        noSchedulesLayout = (LinearLayout) view.findViewById(R.id.emptyElement);
        noShiftsTextview = (TextView) view.findViewById(R.id.noShiftsTextview);
        noShiftsTextview.setText("There are no available Schedules for you right now.\n\nPlease check back later.");
        noShiftsIcon = (ImageView) view.findViewById(R.id.noShiftsIcon);
        schedulesListview = (ListView) view.findViewById(R.id.schedulesListview);
        schedulesListview.setOnItemClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        adapter = new SchedulesAdapter(this, schedulesList);
        schedulesListview.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        LegionUtils.showProgressDialog(getActivity());
        doLoadSchedules();
        return view;
    }

    public ScheduleTabFragment() {

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        if (!LegionUtils.isOnline(getActivity())) {
            if (legionPreferences.hasKey(Prefs_Keys_Offline.SCHEDULES_LIST_TAB_FRAGEMNT)) {
                new ResponseParserTask(ResponseParserConstants.PARSE_SCHEDULES, legionPreferences, this).execute(legionPreferences.get(Prefs_Keys_Offline.SCHEDULES_LIST_TAB_FRAGEMNT));
                LegionUtils.hideProgressDialog();
            } else {
                LegionUtils.showOfflineDialog(getActivity());
                return;
            }
        } else {
            doLoadSchedules();
        }
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view, false, "Schedule Overview");
        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));
    }

    private void doLoadSchedules() {
        if (!LegionUtils.isOnline(getActivity())) {
            if (legionPreferences.hasKey(Prefs_Keys_Offline.SCHEDULES_LIST_TAB_FRAGEMNT)) {
                new ResponseParserTask(ResponseParserConstants.PARSE_SCHEDULES, legionPreferences, this).execute(legionPreferences.get(Prefs_Keys_Offline.SCHEDULES_LIST_TAB_FRAGEMNT));
            } else {
                LegionUtils.hideProgressDialog();
                LegionUtils.showOfflineDialog(getActivity());
                return;
            }
        } else {
            LegionUtils.hideKeyboard(getActivity());
            Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));

            Calendar cal = Calendar.getInstance();

            RequestParams params = new RequestParams();
            params.put("year", "2016"/*cal.get(Calendar.YEAR)*/);
            cal.add(Calendar.DATE, cal.get(Calendar.DAY_OF_WEEK) * -1);
            params.put("weekStartDayOfTheYear", 17/*cal.get(Calendar.DAY_OF_YEAR)*/);

            try {
                Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
                restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE, ServiceUrls.GET_SCHEDULES_URL, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {

        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if(getActivity() == null || !isAdded()){
            return;
        }
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
            legionPreferences.save(Prefs_Keys_Offline.SCHEDULES_LIST_TAB_FRAGEMNT, response.toString());
            LegionUtils.hideProgressDialog();
            schedulesList.clear();
            swipeRefreshLayout.setRefreshing(false);
            new ResponseParserTask(ResponseParserConstants.PARSE_SCHEDULES, legionPreferences, this).execute(response.toString());
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (reasonPhrase == null) {
            return;
        }
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
            if (reasonPhrase.contains("Something went wrong")) {
                LegionUtils.doLogout(getActivity());
                return;
            }
            LegionUtils.hideProgressDialog();
            LegionUtils.showDialog(getActivity(), reasonPhrase, true);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle b = new Bundle();
        Schedule schedule = schedulesList.get(position);
        TextView weekDateTV = (TextView) view.findViewById(R.id.weekDateTV);
        TextView scheduleWeekTv = (TextView) view.findViewById(R.id.scheduleWeekTv);
        TextView newTv = (TextView) view.findViewById(R.id.newTV);
        ScheduleOverviewDetailsFragment frag = new ScheduleOverviewDetailsFragment();
        b.putString(Extras_Keys.WEEKDAY_TV, weekDateTV.getText().toString());
        Extras_Keys.FIRST_TIME_TITLE_UPDATED = scheduleWeekTv.getText().toString().trim();
        b.putInt(Extras_Keys.POSITION, position);
        Extras_Keys.SCHEDULE_ID = schedule.getWeeklyScheduleId();
        //b.putString(Extras_Keys.SCHEDULE_ID,schedule.getWeeklyScheduleId());
        b.putInt(Extras_Keys.IS_SEEN, newTv.getVisibility() == View.VISIBLE ? 1 : 0);
        b.putSerializable(Extras_Keys.SCHEDULE_DETAILS_DATA, schedulesList);
        frag.setArguments(b);
        ((BaseContainerFragment) getParentFragment()).replaceFragment(frag, true, null);
    }

    @Override
    public void onResponseParsingStart(int parserId) {
    }

    @Override
    public void onResponseParsingComplete(int parserId, Object response) {
        if (getActivity() == null || !isAdded()) {
            return;
        }
        if (parserId == ResponseParserConstants.PARSE_SCHEDULES) {
            LegionUtils.hideProgressDialog();
            schedulesList.clear();
            swipeRefreshLayout.setRefreshing(false);

            if (response instanceof Exception) {
                LegionUtils.showDialog(getActivity(), "Oops, Something went wrong.\n\nPlease try again later.", true);
                ((Exception) response).printStackTrace();
            } else if (response instanceof String) {
                showToast(response.toString());
            } else if (response instanceof ArrayList<?>) {
                ArrayList<Schedule> schedulesArrayList = (ArrayList<Schedule>) response;

                schedulesList.addAll(schedulesArrayList);
                int size = schedulesList.size();
                adapter.notifyDataSetChanged();

                if (size == 0) {
                    noSchedulesLayout.setVisibility(View.VISIBLE);
                    schedulesListview.setVisibility(View.GONE);
                } else {
                    noSchedulesLayout.setVisibility(View.GONE);
                    schedulesListview.setVisibility(View.VISIBLE);
                    int moveToPosition = -1;
                    for(int i = 0; i < size; ++i) {
                        Schedule schedule = schedulesList.get(i);
                        if (getActivity().getIntent().hasExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR)) {
                            if (schedule.getWeekStartDayOfTheYear().equalsIgnoreCase(getActivity().getIntent().getStringExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR))) {
                                notificationPosition = i;
                            }
                        }

                        if (schedule.isCurrentWeek()) {
                            moveToPosition = i;
                        }
                    }

                    //if current week has no schedules then automatically scroll to next available week.
                    if(moveToPosition == -1){
                        Date now = Calendar.getInstance().getTime();
                        for(int i = 0; i < size; ++i) {
                            Schedule schedule = schedulesList.get(i);
                            Date shiftStartDate = now;
                            try {
                                shiftStartDate = LegionUtils.parseDateFromJsonString(schedule.getStartOfWeekDate());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if(shiftStartDate.after(now)){
                                moveToPosition = i;
                                break;
                            }
                        }
                    }

                    if(moveToPosition == -1){
                        moveToPosition = schedulesList.size() - 1;
                    }
                    schedulesListview.setSelection(moveToPosition);
                    if (getActivity().getIntent().hasExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR)) {
                        getActivity().getIntent().removeExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR);
                        getActivity().getIntent().removeExtra(Extras_Keys.TYPE);
                        schedulesListview.performItemClick(schedulesListview.getAdapter().getView(notificationPosition, null, null), notificationPosition, notificationPosition);
                    }
                }
            }
        }
    }
}
