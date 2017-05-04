package tabs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapters.ShiftOffersAdapter;
import adapters.ShiftOffersFilterSpinnerAdapter;
import asynctasks.ResponseParserTask;
import base.Legion_BaseFragment;
import co.legion.client.BuildConfig;
import co.legion.client.R;
import co.legion.client.activities.CalloutShiftOfferDetailsActivity;
import co.legion.client.activities.OpenShiftOfferDetailsActivity;
import co.legion.client.activities.SwapShiftOfferDetailsActivity;
import interfaces.ResponseParserListener;
import models.AssociatedWorker;
import models.BusinessKey;
import models.NotificationType;
import models.ScheduleWorkerShift;
import models.ShiftOffer;
import models.ShiftOfferedRequested;
import models.Worker;
import models.WorkerShiftSummary;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 1/4/2017.
 */
public class ShiftOffersTabFragment extends Legion_BaseFragment implements ResponseParserListener, AdapterView.OnItemClickListener, Legion_Constants, SwipeRefreshLayout.OnRefreshListener, Legion_NetworkCallback {

    private TextView tvCallOuts;
    private TextView tvSwaps;
    private TextView tvOpenShifts;
    private LinearLayout emptyElement;
    private ImageView noShiftsIcon;
    private TextView noShiftsMessageTextview;
    private SwipeRefreshLayout swipeToRefresh;
    private StickyListHeadersListView shiftOffersListview;
    private ImageView iv_open_close_arrow;
    private int[] countsArrays;
    private ArrayList<ShiftOffer> allOffersList = new ArrayList<>();
    private ArrayList<ShiftOffer> dataset = new ArrayList<>();
    private ShiftOffersAdapter shiftOffersAdapter;
    private ShiftOffersFilterSpinnerAdapter spinnerAdapter;
    private TextView toolbarTile;
    private boolean isFirstTime = true;
    private int[] shiftTypeIcons;
    private String[] shiftTypeTitles;
    private TextView header_offers_count_tv;
    private TextView header_offers_range_date_tv;
    private int itempostion = -1;
    private String notificationOfferId;
    private ShiftOffer clickedOffer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_shift_offers, container, false);
    }

    public ShiftOffersTabFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));
        legionPreferences.save(Prefs_Keys.OFFER_TYPE, "Open");
        legionPreferences.save(Prefs_Keys.OFFER_STATUS, "Proposed");
        findViews(view);
    }

    private void findViews(View view) {
        toolbarTile = (TextView) view.findViewById(R.id.tv_toolbar_title);
        iv_open_close_arrow = (ImageView) view.findViewById(R.id.iv_open_close_arrow);
        tvCallOuts = (TextView) view.findViewById(R.id.tv_call_outs);
        tvSwaps = (TextView) view.findViewById(R.id.tv_swaps);
        tvOpenShifts = (TextView) view.findViewById(R.id.tv_open_shifts);
        emptyElement = (LinearLayout) view.findViewById(R.id.emptyElement);
        noShiftsIcon = (ImageView) view.findViewById(R.id.noShiftsIcon);
        noShiftsMessageTextview = (TextView) view.findViewById(R.id.noShiftsTextview);
        swipeToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeToRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        shiftOffersListview = (StickyListHeadersListView) view.findViewById(R.id.shiftOffersListview);
        LinearLayout titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);

        /*headerView = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.header_shift_offers_listview, null);
        header_offers_count_tv = (TextView) headerView.findViewById(R.id.header_offers_count_tv);
        header_offers_range_date_tv = (TextView) headerView.findViewById(R.id.header_offers_range_date_tv);

        LegionUtils.doApplyFont(getActivity().getAssets(), headerView);*/

        iv_open_close_arrow.setOnClickListener(this);
        titleLayout.setOnClickListener(this);
        tvCallOuts.setOnClickListener(this);
        tvSwaps.setOnClickListener(this);
        tvOpenShifts.setOnClickListener(this);
        swipeToRefresh.setOnRefreshListener(this);

        shiftTypeIcons = new int[]{R.drawable.ic_offer_unclaimed, R.drawable.ic_offer_new, R.drawable.ic_offer_pending, R.drawable.ic_offer_approved, R.drawable.ic_offer_rejected, R.drawable.ic_offer_bookmarked};
        shiftTypeTitles = new String[]{getString(R.string.unclaimed_offers), getString(R.string.new_offers), getString(R.string.my_pending_offers), getString(R.string.my_approved_offers), getString(R.string.my_rejected_offers), getString(R.string.my_bookmarks)};
        countsArrays = new int[]{0, 0, 0, 0, 0, 0};

        shiftOffersAdapter = new ShiftOffersAdapter(this, dataset);
        shiftOffersListview.setAdapter(shiftOffersAdapter);
        shiftOffersListview.setOnItemClickListener(this);
        shiftOffersListview.setEmptyView(emptyElement);

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Extras_Keys.TYPE)) {
            String type = intent.getStringExtra(Extras_Keys.TYPE);
            if (type.equalsIgnoreCase(NotificationType.SHIFT_OFFER.getType())) {
                if (intent.hasExtra(Extras_Keys.OFFER_ID) && intent.getStringExtra(Extras_Keys.OFFER_ID) != null) {
                    notificationOfferId = intent.getStringExtra(Extras_Keys.OFFER_ID);
                    intent.removeExtra(Extras_Keys.TYPE);
                } else if (intent.hasExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR) && intent.getStringExtra(Extras_Keys.OFFER_ID) == null) {
                    toolbarTile.setText(shiftTypeTitles[1]);
                    legionPreferences.save(Prefs_Keys.OFFER_STATUS, "New");
                    intent.removeExtra(Extras_Keys.TYPE);
                }
            }
        }

        doGetShiftOffers();

        if (legionPreferences.get(Prefs_Keys.OFFER_TYPE) != null) {
            if (legionPreferences.get(Prefs_Keys.OFFER_TYPE).equalsIgnoreCase("Open")) {
                tvOpenShifts.performClick();
            } else if (legionPreferences.get(Prefs_Keys.OFFER_TYPE).equalsIgnoreCase("Callout")) {
                tvCallOuts.performClick();
            } else if (legionPreferences.get(Prefs_Keys.OFFER_TYPE).equalsIgnoreCase("ShiftSwap")) {
                tvSwaps.performClick();
            }
        }

        if (legionPreferences.get(Prefs_Keys.OFFER_STATUS) != null) {
            if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equalsIgnoreCase("Proposed")) {
                toolbarTile.setText(getString(R.string.unclaimed_offers));
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equalsIgnoreCase("New")) {
                toolbarTile.setText(getString(R.string.new_offers));
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equalsIgnoreCase("Claimed")) {
                toolbarTile.setText(getString(R.string.my_pending_offers));
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equalsIgnoreCase("Accepted")) {
                toolbarTile.setText(getString(R.string.my_approved_offers));
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equalsIgnoreCase("Declined")) {
                toolbarTile.setText(getString(R.string.my_rejected_offers));
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equalsIgnoreCase("Bookmarked")) {
                toolbarTile.setText(getString(R.string.my_bookmarks));
            }
        }
    }

    private void doGetShiftOffers() {
        if (!LegionUtils.isOnline(getActivity())) {
            swipeToRefresh.setRefreshing(false);
            LegionUtils.showOfflineDialog(getActivity());
            return;
        }
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_open_close_arrow:
            case R.id.titleLayout:
                PopupWindow popUp = doInitPopup();
                popUp.showAsDropDown(v, 0, 50);
                break;

            case R.id.tv_call_outs:
                if (legionPreferences.get(Prefs_Keys.OFFER_TYPE).equalsIgnoreCase("Callout")) {
                    legionPreferences.save(Prefs_Keys.OFFER_TYPE, "");
                    tvCallOuts.setBackgroundColor(Color.WHITE);
                    tvCallOuts.setTextColor(Color.parseColor("#8493A9"));
                } else {
                    legionPreferences.save(Prefs_Keys.OFFER_TYPE, "Callout");
                    tvCallOuts.setTextColor(Color.parseColor("#242365"));
                    tvCallOuts.setBackgroundColor(ActivityCompat.getColor(getActivity(), R.color.find_shift_lable_bg));
                }
                tvOpenShifts.setBackgroundColor(Color.WHITE);
                tvSwaps.setBackgroundColor(Color.WHITE);
                tvSwaps.setTextColor(Color.parseColor("#8493A9"));
                tvOpenShifts.setTextColor(Color.parseColor("#8493A9"));
                doRefreshListView();
                break;

            case R.id.tv_open_shifts:
                if (legionPreferences.get(Prefs_Keys.OFFER_TYPE).equalsIgnoreCase("Open")) {
                    legionPreferences.save(Prefs_Keys.OFFER_TYPE, "");
                    tvOpenShifts.setBackgroundColor(Color.WHITE);
                    tvOpenShifts.setTextColor(Color.parseColor("#8493A9"));
                } else {
                    legionPreferences.save(Prefs_Keys.OFFER_TYPE, "Open");
                    tvOpenShifts.setTextColor(Color.parseColor("#242365"));
                    tvOpenShifts.setBackgroundColor(ActivityCompat.getColor(getActivity(), R.color.find_shift_lable_bg));
                }
                tvCallOuts.setBackgroundColor(Color.WHITE);
                tvSwaps.setBackgroundColor(Color.WHITE);
                tvSwaps.setTextColor(Color.parseColor("#8493A9"));
                tvCallOuts.setTextColor(Color.parseColor("#8493A9"));

                doRefreshListView();
                break;

            case R.id.tv_swaps:
                if (legionPreferences.get(Prefs_Keys.OFFER_TYPE).equalsIgnoreCase("ShiftSwap")) {
                    legionPreferences.save(Prefs_Keys.OFFER_TYPE, "");
                    tvSwaps.setBackgroundColor(Color.WHITE);
                    tvSwaps.setTextColor(Color.parseColor("#8493A9"));
                } else {
                    legionPreferences.save(Prefs_Keys.OFFER_TYPE, "ShiftSwap");
                    tvSwaps.setTextColor(Color.parseColor("#242365"));
                    tvSwaps.setBackgroundColor(ActivityCompat.getColor(getActivity(), R.color.find_shift_lable_bg));
                }
                tvOpenShifts.setBackgroundColor(Color.WHITE);
                tvCallOuts.setBackgroundColor(Color.WHITE);
                tvCallOuts.setTextColor(Color.parseColor("#8493A9"));
                tvOpenShifts.setTextColor(Color.parseColor("#8493A9"));

                doRefreshListView();
                break;
        }
    }

    private PopupWindow doInitPopup() {
        onSpinnerOpened();
        final PopupWindow popupWindow = new PopupWindow(getActivity());
        spinnerAdapter = new ShiftOffersFilterSpinnerAdapter(getActivity(), shiftTypeIcons, shiftTypeTitles, countsArrays);
        ListView listViewSort = new ListView(getActivity());
        listViewSort.setAdapter(spinnerAdapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
                String selectedOfferType = "Proposed";
                if (position == 0) {
                    selectedOfferType = "Proposed";
                } else if (position == 1) {
                    selectedOfferType = "New";
                } else if (position == 2) {
                    selectedOfferType = "Claimed";
                } else if (position == 3) {
                    selectedOfferType = "Accepted";
                } else if (position == 4) {
                    selectedOfferType = "Declined";
                } else if (position == 5) {
                    selectedOfferType = "Bookmarked";
                }
                toolbarTile.setText(shiftTypeTitles[position]);
                legionPreferences.save(Prefs_Keys.OFFER_STATUS, selectedOfferType);
                doRefreshListView();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                onSpinnerClosed();
            }
        });

        // some other visual settings for popup window
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // popupWindow.setElevation(10);
        popupWindow.setContentView(listViewSort);

        return popupWindow;
    }

    private void doRefreshListView() {
        dataset.clear();
        shiftOffersAdapter.notifyDataSetChanged();

        for (ShiftOffer offer : allOffersList) {
            if (legionPreferences.get(Prefs_Keys.OFFER_TYPE).equalsIgnoreCase("")) {
                if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("Bookmarked")) {
                    if (offer.isPinned()) {
                        dataset.add(offer);
                    }
                } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("New")) {
                    if (!offer.isSeen()) {
                        dataset.add(offer);
                    }
                } else if (offer.getOfferStatus().equalsIgnoreCase(legionPreferences.get(Prefs_Keys.OFFER_STATUS))) {
                    dataset.add(offer);
                }
            } else {
                if (offer.getOfferType().equalsIgnoreCase(legionPreferences.get(Prefs_Keys.OFFER_TYPE))) {
                    if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("Bookmarked")) {
                        if (offer.isPinned()) {
                            dataset.add(offer);
                        }
                    } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("New")) {
                        if (!offer.isSeen()) {
                            dataset.add(offer);
                        }
                    } else if (offer.getOfferStatus().equalsIgnoreCase(legionPreferences.get(Prefs_Keys.OFFER_STATUS))) {
                        dataset.add(offer);
                    }
                }
            }
        }

        doUpdateOffersCounts(allOffersList);

        if (dataset.size() == 0) {
            if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("Bookmarked")) {
                noShiftsIcon.setImageResource(R.drawable.ic_bookmarks_none);
                noShiftsMessageTextview.setText("You haven't bookmarked any Offers yet!\n\nBookmarking's easy. Just tap on an offer to view its details. From there, you'll find a tappable bookmark icon at the top of the screen.");
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("New")) {
                noShiftsIcon.setImageResource(R.drawable.ic_new_offers_none);
                noShiftsMessageTextview.setText("There are are no New Offers right now.\n\nPlease check back later.");
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("Proposed")) {
                noShiftsIcon.setImageResource(R.drawable.ic_shift_offers_none);
                noShiftsMessageTextview.setText("There are are no available Unclaimed Offers right now.\n\nPlease check back later.");
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("Claimed")) {
                noShiftsIcon.setImageResource(R.drawable.ic_pending_shift_offers_none);
                noShiftsMessageTextview.setText("You have no Pending Shift Offers.\n\nYour claimed offers may have been Approved or Rejected.");
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("Accepted")) {
                noShiftsIcon.setImageResource(R.drawable.ic_approved_shift_offers_none);
                noShiftsMessageTextview.setText("You have no Approved Shift Offers.\n\nYour claimed offers may be Pending or Rejected.");
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("Declined")) {
                noShiftsIcon.setImageResource(R.drawable.ic_rejected_shift_offers_none);
                noShiftsMessageTextview.setText("You have no Rejected Shift Offers.\n\nYour claimed offers may be Pending or Approved.");
            }
        }
        shiftOffersAdapter.notifyDataSetChanged();

        if (notificationOfferId != null) {
            int size = allOffersList.size();
            for (int i = 0; i < size; ++i) {
                ShiftOffer offer = allOffersList.get(i);
                if (offer.getOfferId().equals(notificationOfferId)) {
                    onOfferClick(offer, -1);
                    notificationOfferId = null;
                    break;
                }
            }
        }
    }

    private void doUpdateOffersCounts(ArrayList<ShiftOffer> allOffersList) {
        int openShiftsCount = 0, calloutShiftsCount = 0, swapShiftsCount = 0;
        for (ShiftOffer offer : allOffersList) {
            if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("Bookmarked")) {
                if (offer.isPinned()) {
                    if (offer.getOfferType().equalsIgnoreCase("Open")) {
                        ++openShiftsCount;
                    } else if (offer.getOfferType().equalsIgnoreCase("ShiftSwap")) {
                        ++swapShiftsCount;
                    } else if (offer.getOfferType().equalsIgnoreCase("Callout")) {
                        ++calloutShiftsCount;
                    }
                }
            } else if (legionPreferences.get(Prefs_Keys.OFFER_STATUS).equals("New")) {
                if (!offer.isSeen()) {
                    if (offer.getOfferType().equalsIgnoreCase("Open")) {
                        ++openShiftsCount;
                    } else if (offer.getOfferType().equalsIgnoreCase("ShiftSwap")) {
                        ++swapShiftsCount;
                    } else if (offer.getOfferType().equalsIgnoreCase("Callout")) {
                        ++calloutShiftsCount;
                    }
                }
            } else if (offer.getOfferStatus().equalsIgnoreCase(legionPreferences.get(Prefs_Keys.OFFER_STATUS))) {
                if (offer.getOfferType().equalsIgnoreCase("Open")) {
                    ++openShiftsCount;
                } else if (offer.getOfferType().equalsIgnoreCase("ShiftSwap")) {
                    ++swapShiftsCount;
                } else if (offer.getOfferType().equalsIgnoreCase("Callout")) {
                    ++calloutShiftsCount;
                }
            }

        }

        tvSwaps.setText("SWAP (" + swapShiftsCount + ")");
        tvOpenShifts.setText("OPEN SHIFTS (" + openShiftsCount + ")");
        tvCallOuts.setText("CALLOUTS (" + calloutShiftsCount + ")");
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.GET_SHIFT_OFFERS_REQUEST_CODE) {
            swipeToRefresh.setRefreshing(false);
            LegionUtils.showProgressDialog(getActivity());
        } else if (requestCode == WebServiceRequestCodes.GET_SWAP_SHIFT_OFFERS) {
            swipeToRefresh.setRefreshing(false);
            LegionUtils.showProgressDialog(getActivity());
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if(getActivity() == null || !isAdded()){
            return;
        }
        if (requestCode == WebServiceRequestCodes.GET_SHIFT_OFFERS_REQUEST_CODE) {
            try {
                Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
                restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SWAP_SHIFT_OFFERS, ServiceUrls.GET_SWAP_SHIFT_OFFERS, new RequestParams(), legionPreferences.get(Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ResponseParserTask(ResponseParserConstants.PARSE_SHIFT_OFFERS, legionPreferences, this).execute(response.toString());
        } else if (requestCode == WebServiceRequestCodes.GET_SWAP_SHIFT_OFFERS) {
            LegionUtils.hideProgressDialog();
            new ResponseParserTask(ResponseParserConstants.PARSE_SHIFT_SWAP_OFFERS, legionPreferences, this).execute(response.toString());
        } else if (requestCode == WebServiceRequestCodes.GET_SCHEDULE_SUMMARY_REQUEST_CODE) {
            LegionUtils.hideProgressDialog();
            if (getActivity() != null) {
                new ResponseParserTask(ResponseParserConstants.PARSE_SCHEDULE_SUMMARY_DETAILS, legionPreferences, this).execute(response.toString());
            }
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        swipeToRefresh.setRefreshing(false);
        if (reasonPhrase == null) {
            return;
        }
        if (requestCode == WebServiceRequestCodes.GET_SHIFT_OFFERS_REQUEST_CODE || requestCode == WebServiceRequestCodes.GET_SWAP_SHIFT_OFFERS || requestCode == WebServiceRequestCodes.GET_SCHEDULE_SUMMARY_REQUEST_CODE) {
            LegionUtils.hideProgressDialog();
            if (reasonPhrase.contains("Something went wrong")) {
                LegionUtils.doLogout(getActivity());
                return;
            }
            LegionUtils.showDialog(getActivity(), reasonPhrase, true);
        }
    }

    public void onSpinnerOpened() {
        iv_open_close_arrow.setImageResource(R.drawable.ic_up_arrow_white);
    }

    public void onSpinnerClosed() {
        iv_open_close_arrow.setImageResource(R.drawable.ic_down_arrow_white);
    }

    @Override
    public void onRefresh() {
        swipeToRefresh.setRefreshing(true);
        doGetShiftOffers();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onOfferClick(dataset.get(position), position);
    }

    private void onOfferClick(ShiftOffer offer, int position) {
        clickedOffer = offer;
        itempostion = position;
        if (offer.getOfferType().equalsIgnoreCase("Open")) {
            doLoadScheduleSummary(offer.getStaffingShift().getShiftStartDate());
        } else if (offer.getOfferType().equalsIgnoreCase("Callout")) {
            doLoadScheduleSummary(offer.getStaffingShift().getShiftStartDate());
        } else if (offer.getOfferType().equalsIgnoreCase("ShiftSwap")) {
            doLoadScheduleSummary(offer.getShiftOffered().getShiftStartDate());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            onRefresh();
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            onRefresh();
        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            onRefresh();
        }
    }

    private void doLoadScheduleSummary(String startDate) {
        try {
            swipeToRefresh.setRefreshing(false);
            if (!LegionUtils.isOnline(getActivity())) {
                LegionUtils.showOfflineDialog(getActivity());
                return;
            }
            LegionUtils.hideKeyboard(getActivity());
            Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat myFormat = new SimpleDateFormat(LegionUtils.DATE_FORMAT);
            String inputString1 = startDate.replace("T", " ");
            Date date1 = myFormat.parse(inputString1);
            calendar.setTime(date1);
            calendar.add(Calendar.DATE, (-1 * calendar.get(Calendar.DAY_OF_WEEK)) + 1);
            RequestParams params = new RequestParams();
            String year = LegionUtils.getYearFromDate(startDate);
            params.put("year", year);
            params.put("weekStartDayOfTheYear", calendar.get(Calendar.DAY_OF_YEAR));
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            LegionUtils.showProgressDialog(getActivity());
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SCHEDULE_SUMMARY_REQUEST_CODE, ServiceUrls.GET_SCHEDULE_SUMMERY_URL, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
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
        if (parserId == ResponseParserConstants.PARSE_SHIFT_OFFERS) {
            if (response instanceof Exception) {
                LegionUtils.showDialog(getActivity(), "Oops, Something went wrong.\n\nPlease try again later.", true);
                ((Exception) response).printStackTrace();
            } else if (response instanceof String) {
                LegionUtils.showDialog(getActivity(), response.toString(), true);
            } else if (response instanceof ArrayList<?>) {
                ArrayList<ShiftOffer> offersList = (ArrayList<ShiftOffer>) response;
                allOffersList.clear();
                allOffersList.addAll(offersList);
            }
        } else if (parserId == ResponseParserConstants.PARSE_SHIFT_SWAP_OFFERS) {
            if (response instanceof Exception) {
                LegionUtils.showDialog(getActivity(), "Oops, Something went wrong.\n\nPlease try again later.", true);
                ((Exception) response).printStackTrace();
            } else if (response instanceof String) {
                LegionUtils.showDialog(getActivity(), response.toString(), true);
            } else if (response instanceof ArrayList<?>) {
                for (int i = 0; i < countsArrays.length; ++i) {
                    countsArrays[i] = 0;
                }
                ArrayList<ShiftOffer> offersList = (ArrayList<ShiftOffer>) response;
                for (ShiftOffer sOffer : offersList) {
                    allOffersList.add(0, sOffer);
                }
                for (ShiftOffer offer : allOffersList) {
                    if (offer.getOfferStatus().equalsIgnoreCase("Proposed") /*|| offer.getOfferStatus().equalsIgnoreCase("Withdrawn")*/) {
                        countsArrays[0] = ++countsArrays[0];
                    } else if (offer.getOfferStatus().equalsIgnoreCase("Claimed")) {
                        countsArrays[2] = ++countsArrays[2];
                    } else if (offer.getOfferStatus().equalsIgnoreCase("Accepted")) {
                        countsArrays[3] = ++countsArrays[3];
                    } else if (offer.getOfferStatus().equalsIgnoreCase("Declined")) {
                        countsArrays[4] = ++countsArrays[4];
                    }

                    if (!offer.isSeen()) {
                        countsArrays[1] = ++countsArrays[1];
                    }

                    if (offer.isPinned()) {
                        countsArrays[5] = ++countsArrays[5];
                    }
                }
                doRefreshListView();
            }
        } else if (parserId == ResponseParserConstants.PARSE_SCHEDULE_SUMMARY_DETAILS) {
            if (response instanceof Exception) {
                ((Exception) response).printStackTrace();
                swipeToRefresh.setRefreshing(false);
                LegionUtils.hideProgressDialog();
            } else if (response instanceof WorkerShiftSummary) {
                if (clickedOffer == null) {
                    return;
                }
                WorkerShiftSummary summery = (WorkerShiftSummary) response;
                String numOfHrs = summery.getNumberOfHours();
                String offerType = clickedOffer.getOfferType();
                if (offerType.equalsIgnoreCase("Open") || offerType.equalsIgnoreCase("Callout")) {
                    Intent i = new Intent(getActivity(), OpenShiftOfferDetailsActivity.class);
                    if (itempostion == -1) {
                        i.putExtra(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS, "0");
                    } else {
                        i.putExtra(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS, numOfHrs);
                    }
                    i.putExtra(Extras_Keys.WORKSHIFTS_LIST, clickedOffer);
                    getActivity().startActivityForResult(i, 1);
                } else if (offerType.equalsIgnoreCase("ShiftSwap")) {
                    Intent i = new Intent(getActivity(), SwapShiftOfferDetailsActivity.class);
                    if (itempostion == -1) {
                        i.putExtra(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS, "0");
                    } else {
                        i.putExtra(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS, numOfHrs);
                    }
                    i.putExtra(Extras_Keys.WORKSHIFTS_LIST, clickedOffer);
                    getActivity().startActivityForResult(i, 3);
                }
            }
        }
    }
}
