package tabs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import base.Legion_BaseFragment;
import co.legion.client.R;
import helpers.CustomTypefaceSpan;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/22/2016.
 */
public class ShiftOffersTabCOMINGSOONFragment extends Legion_BaseFragment implements Legion_Constants {
    private Spinner dropdownSpinner;
    private TextView tvCallOuts;
    private TextView tvSwaps;
    private TextView tvOpenShifts;
    private LinearLayout linearNoShifts;
    private ImageView ivNoShifts;
    private TextView tvNoShiftsTitle;
    private SwipeRefreshLayout swiperefresh;
    private ListView listViewShifts;
    private TextView toolbarTile;
    private ImageView arrow;
    private int[] countsArrays;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_shift_offers_coming_soon, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));


        TextView tv = (TextView)view.findViewById(R.id.comingSoonTV);

        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mallory Light Regular.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Mallory Medium Regular.ttf");
        SpannableStringBuilder ss = new SpannableStringBuilder("With Shift Offers, you can add to your paycheck by claiming unscheduled shifts.\n\n\nWhether shifts open up at short notice, or are available for a future week, we'll highlight shifts that best match your availability and preferences.");
        ss.setSpan(new CustomTypefaceSpan("", light), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", bold), 5, 17, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", light), 17, ss.toString().length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tv.setText(ss);
        //findViews(view);
    }

    /*private void findViews(View view) {
        toolbarTile = (TextView)view.findViewById(R.id.tv_toolbar_title);
        arrow = (ImageView)view.findViewById(R.id.iv_down_arrow);
        dropdownSpinner = (Spinner)view.findViewById( R.id.dropdown_spinner);
        tvCallOuts = (TextView)view.findViewById( R.id.tv_call_outs );
        tvSwaps = (TextView)view.findViewById( R.id.tv_swaps );
        tvOpenShifts = (TextView)view.findViewById( R.id.tv_open_shifts );
        linearNoShifts = (LinearLayout)view.findViewById( R.id.linear_no_shifts );
        ivNoShifts = (ImageView)view.findViewById( R.id.iv_no_shifts );
        tvNoShiftsTitle = (TextView)view.findViewById( R.id.tv_no_shifts_title );
        swiperefresh = (SwipeRefreshLayout)view.findViewById( R.id.swiperefresh );
        listViewShifts = (ListView)view.findViewById( R.id.listView_shifts );

        arrow.setOnClickListener(this);
        tvCallOuts.setOnClickListener(this);
        tvSwaps.setOnClickListener(this);
        tvOpenShifts.setOnClickListener(this);

        int[] imageDrawables = {R.drawable.check_mdpi, R.drawable.check_mdpi,
                                     R.drawable.check_mdpi, R.drawable.check_mdpi,
                                     R.drawable.check_mdpi, R.drawable.check_mdpi};

        String[] spinnerTitles = {getString(R.string.unclaimed_offers), getString(R.string.new_offers), getString(R.string.my_pending_offers), getString(R.string.my_approved_offers), getString(R.string.my_rejected_offers), getString(R.string.my_bookmarks)};
        countsArrays  = new int[]{50, 5, 3, 4, 2, 10};

        dropdownSpinner.setAdapter(new ShiftOffersFilterSpinnerAdapter(getActivity(), imageDrawables, spinnerTitles, countsArrays));

        doGetShiftOrders();
    }

    private void doGetShiftOrders() {
        if (!LegionUtils.isOnline(getActivity())) {
            showToast(getString(R.string.device_offline_message));
            return;
        }
        LegionUtils.hideKeyboard(getActivity());
        Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));

        try {
            Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SHIFT_ORDERS_REQUEST_CODE, ServiceUrls.GET_SHIFT_ORDERS_URL, new RequestParams(), legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.iv_down_arrow){
            dropdownSpinner.performClick();
        }else if (v.getId() == R.id.tv_call_outs){

        }else if (v.getId() == R.id.tv_swaps){

        }else if (v.getId() == R.id.tv_open_shifts){

        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
            LegionUtils.showProgressDialog(getActivity());
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
            LegionUtils.hideProgressDialog();
            doParseShiftOffers(response.toString());
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
            showToast(reasonPhrase);
            LegionUtils.hideProgressDialog();
        }
    }

    private void doParseShiftOffers(String response) {
        try {
            JSONObject mainJsonObject = new JSONObject(response);
            String responseStatus = mainJsonObject.optString("responseStatus");
            String errorString = mainJsonObject.optString("errorString");
            String operationStatus = mainJsonObject.optString("operationStatus");

            if (responseStatus.equals("SUCCESS")){
                JSONArray shiftOfferJsonArray = mainJsonObject.optJSONArray("shiftOffer");

                if (shiftOfferJsonArray.length() > 0){
                    linearNoShifts.setVisibility(View.GONE);
                    listViewShifts.setVisibility(View.VISIBLE);

                }else {
                    listViewShifts.setVisibility(View.GONE);
                    linearNoShifts.setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
