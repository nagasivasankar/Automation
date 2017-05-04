package co.legion.client.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import adapters.ScheduleShiftDetailsListAdapter;
import base.Legion_BaseActivity;
import co.legion.client.R;
import fragments.ComingSoonDialogFragment;
import models.AssociatedWorker;
import models.ScheduleWorkerShift;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import ui.HorizontalListView;
import utils.LegionUtils;

/**
 * Created by Administrator on 12/8/2016.
 */
public class ScheduleShiftDetailsActivity extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback {
    private static final int VALID_TIME_TO_SWAP_SHIFT = 24 * LegionUtils.HOUR;
    public TextView textLinearShiftLead;
    public LinearLayout linearDirections;
    public LinearLayout linearMobileNumber;
    private TextView tvTimeInterval;
    private TextView tvAddress;
    private TextView tvPeakHours;
    private TextView tvCost;
    private TextView tvCostType;
    private TextView tvAvailability;
    private TextView tvTime;
    private TextView tvTextHours;
    private ImageView ivImage;
    private LinearLayout linearShiftLead;
    private TextView tvDirections;
    private TextView tvMobileNumber;
    private TextView tvNotesDescription;
    private Button btnOfferYourShift;
    private int demandPeakHours = 0;
    private ArrayList<AssociatedWorker> associatedWorkerArrayList = new ArrayList<>();
    private TextView tvToolbarTile;
    private Dialog loadingDialog;
    private View viewLine;
    private TextView offerAppTv;
    private LinearLayout offerLL;
    private ScheduleWorkerShift scheduLedPos;
    private Button btnCancelShift;
    private TextView viewedTv;
    private LinearLayout offerLL1;
    private ImageView viewed_showdetailsIv;
    private String statusesponse;
    private long serverhitms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_shift_details);
        findViews();
    }

    private void findViews() {
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        tvTimeInterval = (TextView) findViewById(R.id.tv_time_interval);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        viewedTv = (TextView) findViewById(R.id.viewedTv);
        viewLine = findViewById(R.id.viewLine);
        tvPeakHours = (TextView) findViewById(R.id.tv_peak_hours);
        tvCost = (TextView) findViewById(R.id.tv_cost);
        tvCostType = (TextView) findViewById(R.id.tv_cost_type);
        tvAvailability = (TextView) findViewById(R.id.tv_availability);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvTextHours = (TextView) findViewById(R.id.tv_text_hours);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        linearShiftLead = (LinearLayout) findViewById(R.id.linear_shift_lead);
        textLinearShiftLead = (TextView) findViewById(R.id.text_linear_shift_lead);
        linearDirections = (LinearLayout) findViewById(R.id.linear_directions);
        tvDirections = (TextView) findViewById(R.id.tv_directions);
        linearMobileNumber = (LinearLayout) findViewById(R.id.linear_mobile_number);
        tvMobileNumber = (TextView) findViewById(R.id.tv_mobile_number);
        tvNotesDescription = (TextView) findViewById(R.id.tv_notes_description);
        btnOfferYourShift = (Button) findViewById(R.id.btn_offer_your_shift);
        btnCancelShift = (Button) findViewById(R.id.btnCancelShift);
        offerAppTv = (TextView) findViewById(R.id.offerAppTv);
        offerLL1 = (LinearLayout) findViewById(R.id.offerLL1);
        offerLL = (LinearLayout) findViewById(R.id.offerLL);
        viewed_showdetailsIv = (ImageView) findViewById(R.id.viewed_showdetailsIv);
        btnOfferYourShift.setOnClickListener(this);
        backImage.setOnClickListener(this);
        btnCancelShift.setOnClickListener(this);
        linearShiftLead.setOnClickListener(this);
        try {
            scheduLedPos = (ScheduleWorkerShift) getIntent().getExtras().get(Extras_Keys.WORKSHIFTS_LIST);
        } catch (Exception e) {
            Log.v("Exception", "Matybe");
        }
        setDataToViews();
    }

    private void setDataToViews() {

        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(scheduLedPos.getShiftStartDate().toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            String finalDate = fmtOut.format(date);
            try {
                Glide.with(this).load(scheduLedPos.getBusinessKey().getPhotoUrlsList().get(0)).into(ivImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String offerStatus = scheduLedPos.getOfferStatus();
            Log.v("offerstaus", scheduLedPos.getOfferStatus());
            tvToolbarTile.setText(getDayName(Integer.parseInt(scheduLedPos.getDayOfTheWeek())) + ", " + finalDate);
            if (getIntent().getExtras().getString(Extras_Keys.STARTTIME) != null) {
                String startTime = getIntent().getExtras().getString(Extras_Keys.STARTTIME).replace(":00", "");
                String endTime = getIntent().getExtras().getString(Extras_Keys.ENDTIME).replace(":00", "");
                String totalShiftHours = getIntent().getStringExtra("totalShiftHours").replace(".0", "");
                prefsManager.save(Prefs_Keys.TOTAL_SHIFT_HOURS, totalShiftHours);
                tvTime.setText((getIntent().getExtras().getString(Extras_Keys.SHIFT_MINS)));
                prefsManager.save(Prefs_Keys.TOTAL_SHIFT_MINS, "" + (getIntent().getExtras().getString(Extras_Keys.SHIFT_MINS)));
                tvTimeInterval.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + scheduLedPos.getRole().toUpperCase());
                if (totalShiftHours.equals("1")) {
                    tvTextHours.setText("Hr This Week");
                } else {
                    tvTextHours.setText("Hrs This Week");
                }
            } else {
                String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(scheduLedPos.getStartMin())).replace(":00", "");
                String endTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(scheduLedPos.getEndMin())).replace(":00", "");
                String totalShiftHours = prefsManager.get(Prefs_Keys.TOTAL_SHIFT_HOURS).replace(".0", "");
                tvTime.setText(prefsManager.get(Prefs_Keys.TOTAL_SHIFT_MINS));
                tvTimeInterval.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + scheduLedPos.getRole().toUpperCase());
                if (totalShiftHours.equals("1")) {
                    tvTextHours.setText("Hr This Week");
                } else {
                    tvTextHours.setText("Hrs This Week");
                }
            }
            if ((scheduLedPos.getHasMeal() != null) && (!scheduLedPos.getHasMeal().isEmpty())) {
                if (Boolean.parseBoolean(scheduLedPos.getHasMeal())) {
                    tvTimeInterval.setCompoundDrawablesWithIntrinsicBounds(R.drawable.meal_included, 0, 0, 0);
                }
            }
            tvAddress.setText(LegionUtils.getUpdatedAddress(scheduLedPos.getBusinessKey().getAddress()));
            int estPay = LegionUtils.getEstimatedPayAsInt(scheduLedPos.getEstimatedPay());
            tvCost.setText("$" + estPay);
            if (scheduLedPos.getNotes().equals("null") || (scheduLedPos.getNotes().isEmpty())) {
                tvNotesDescription.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                findViewById(R.id.noteText).setVisibility(View.GONE);
            } else {
                viewLine.setVisibility(View.VISIBLE);
                findViewById(R.id.noteText).setVisibility(View.VISIBLE);
                tvNotesDescription.setText(scheduLedPos.getNotes().equals("null") ? "" : scheduLedPos.getNotes());
            }

            if (!offerStatus.equalsIgnoreCase("null") && !(offerStatus.equalsIgnoreCase("NotOffered"))) {
                btnOfferYourShift.setVisibility(View.GONE);
                if (offerStatus.equalsIgnoreCase("SwapPending")) {
                    btnCancelShift.setText("Cancel your Swap Request");
                    btnCancelShift.setVisibility(View.VISIBLE);
                } else if (offerStatus.equalsIgnoreCase("DropPending")) {
                    btnCancelShift.setText("Cancel your Drop Request");
                    btnCancelShift.setVisibility(View.VISIBLE);
                } else {
                    btnCancelShift.setVisibility(View.GONE);
                }
            } else {
                btnCancelShift.setVisibility(View.GONE);

                //show OfferYourShiftButton until specified time
                if (scheduLedPos.canOffer()) {
                    btnOfferYourShift.setVisibility(View.VISIBLE);
                } else {
                    btnOfferYourShift.setVisibility(View.GONE);
                }
            }

            String type = "";
            if (offerStatus.equalsIgnoreCase("SwapPending") || offerStatus.equalsIgnoreCase("DropPending")) {
                if (offerStatus.equalsIgnoreCase("SwapPending")) {
                    type = "SWAP";
                } else {
                    type = "DROP";
                }
                if (isExpired()) {
                    offerAppTv.setText(type + " EXPIRED");
                    offerLL.setBackgroundColor(ActivityCompat.getColor(this, R.color.banner_color_violet));
                    offerAppTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check_approved, 0, 0, 0);
                    viewedTv.setText("");
                    offerLL.setClickable(false);
                    viewed_showdetailsIv.setVisibility(View.GONE);
                } else {
                    if (offerStatus.equalsIgnoreCase("SwapPending")) {
                        offerAppTv.setText(type + " APPROVAL PENDING");
                    } else {
                        offerAppTv.setText(type + " APPROVAL PENDING");
                    }
                    viewedTv.setText("");
                    offerLL.setClickable(false);
                    viewed_showdetailsIv.setVisibility(View.GONE);
                    offerLL1.setGravity(Gravity.LEFT);
                    offerLL.setBackgroundColor(ActivityCompat.getColor(this, R.color.banner_color_violet));
                    offerAppTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    doLoadSwapOffers();
                }
            } else {
                if (!scheduLedPos.getType().equalsIgnoreCase("Standard")) {
                    offerAppTv.setText("Offer Approved");
                    offerLL1.setGravity(Gravity.CENTER);
                    offerAppTv.setVisibility(View.VISIBLE);
                    offerLL.setVisibility(View.VISIBLE);
                    offerLL.setBackgroundColor(ActivityCompat.getColor(this, R.color.banner_blue_color));
                    offerAppTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check_approved, 0, 0, 0);
                    viewedTv.setVisibility(View.GONE);
                    offerLL.setClickable(false);
                    viewed_showdetailsIv.setVisibility(View.GONE);
                } else {
                    offerAppTv.setVisibility(View.GONE);
                    offerLL.setVisibility(View.GONE);
                }

            }
            tvDirections.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + scheduLedPos.getBusinessKey().getAddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });

            tvMobileNumber.setText(prefsManager.get(Prefs_Keys.BUSINESS_PHONE_NUMBER));
            tvMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = "Do you want to call the store " + scheduLedPos.getBusinessKey().getName() + "?";
                    final Dialog dialog = LegionUtils.callDialog(ScheduleShiftDetailsActivity.this, msg);
                    TextView okTv = (TextView) dialog.findViewById(R.id.saveTv);
                    okTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + tvMobileNumber.getText().toString()));
                            startActivity(callIntent);
                        }
                    });
                    dialog.show();
                }
            });
            String availabilityMatchOrNot = scheduLedPos.getAvailability();
            if (availabilityMatchOrNot != null) {
                if (availabilityMatchOrNot.equalsIgnoreCase("Yes")) {
                    tvAvailability.setCompoundDrawablesWithIntrinsicBounds(R.drawable.availability_match, 0, 0, 0);//green
                } else if (availabilityMatchOrNot.equalsIgnoreCase("No")) {
                    tvAvailability.setCompoundDrawablesWithIntrinsicBounds(R.drawable.availability_mismatch, 0, 0, 0); // red down
                } else {
                    tvAvailability.setCompoundDrawablesWithIntrinsicBounds(R.drawable.availability_unknown, 0, 0, 0);// grey up
                }
                tvAvailability.setCompoundDrawablePadding(10);
            }
            if (demandPeakHours >= 4) {
                tvPeakHours.setCompoundDrawablesWithIntrinsicBounds(R.drawable.peakhr_yes, 0, 0, 0);
            } else {
                tvPeakHours.setCompoundDrawablesWithIntrinsicBounds(R.drawable.peakhr_no, 0, 0, 0);
            }
            associatedWorkerArrayList.clear();
            associatedWorkerArrayList.addAll(scheduLedPos.getAssociatedWorkerArrayList());

            HorizontalListView horizontalListview = (HorizontalListView) findViewById(R.id.hlv);
            ScheduleShiftDetailsListAdapter scheduleShiftDetailsListAdapter = new ScheduleShiftDetailsListAdapter(ScheduleShiftDetailsActivity.this, associatedWorkerArrayList);
            horizontalListview.setAdapter(scheduleShiftDetailsListAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!LegionUtils.isFeatureEnabled(this, "feature.offerYourShift", scheduLedPos.getBusinessKey().getName())) {
            btnOfferYourShift.setVisibility(View.GONE);
        }
    }

    private void doLoadSwapOffers() {
        try {
            if (!LegionUtils.isOnline(this)) {
                LegionUtils.showOfflineDialog(this);
                return;
            }
            LegionUtils.hideKeyboard(this);
            Log.v("SESSION_ID", "" + prefsManager.get(Prefs_Keys.SESSION_ID));
            RequestParams params = new RequestParams();
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.SHIFT_SWAP_COUNT_REQ_CODE, ServiceUrls.SWAP_REQUEST_DROP_LIST_URL + scheduLedPos.getShiftId() + "/shiftSwapOffers", params, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExpired() {
        int startMin = 1440;
        int endMin = 1440;
        String offerStatus = scheduLedPos.getOfferStatus();
        if (offerStatus.equalsIgnoreCase("Expired")) {
            return true;
        }
       /* SimpleDateFormat df = new SimpleDateFormat(LegionUtils.DATE_FORMAT);
        Date startDate;
        try {
            startDate = df.parse(scheduLedPos.getShiftStartDate().replace("T"," "));
            String newDateString = df.format(startDate);
            System.out.println("newDateString"+newDateString);
            return startDate.after(addMinutesToDate(Math.min(startMin,endMin),new Date()));

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return false;
    }

    private String getDayName(int position) {
        if (position == 1) {
            return "Monday";
        } else if (position == 2) {
            return "Tuesday";
        } else if (position == 3) {
            return "Wednesday";
        } else if (position == 4) {
            return "Thursday";
        } else if (position == 5) {
            return "Friday";
        } else if (position == 6) {
            return "Saturday";
        } else if (position == 7) {
            return "Sunday";
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        if (v == btnOfferYourShift) {
            // Handle clicks for btnOfferYourShift
            Intent intent = new Intent(ScheduleShiftDetailsActivity.this, OfferYourShiftActivity.class);
            intent.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduLedPos);
            startActivity(intent);
        } else if (v.getId() == R.id.toolbarBack) {
            finish();
        } else if (v.getId() == R.id.linear_shift_lead) {
            ComingSoonDialogFragment fragment = new ComingSoonDialogFragment();
            Bundle b = new Bundle();
            b.putString(Extras_Keys.MESSAGE, "Running a few minutes late,or have a question about a shift? <br/><br/> Just <b>Message the Store Lead</b> directly, and address the matter quickly and easily.\n");
            fragment.setArguments(b);
            fragment.show(getSupportFragmentManager(), "");
        } else if (v == btnCancelShift) {
            showDialog(this);
        } else if (v == offerLL) {

            Intent intent = new Intent(ScheduleShiftDetailsActivity.this, SwapRequestedStatusActivity.class);
            intent.putExtra(Extras_Keys.STATUS_RESPONSE_KEY, statusesponse);
            startActivity(intent);
        }
    }


    public void doCancelForSwap() {
        LegionUtils.hideKeyboard(this);
        try {
            showLoadingDialog("Processing your cancellation..");
            serverhitms = System.currentTimeMillis();
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            JSONObject reqObject = new JSONObject();
            restClient.performPutRequest(WebServiceRequestCodes.CANCEL_SWAP_REQ_CODE, ServiceUrls.SWAP_REQUEST_DROP_LIST_URL + scheduLedPos.getShiftId() + "/cancelOffers", reqObject, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doCancelForDrop() {
        LegionUtils.hideKeyboard(this);
        try {
            showLoadingDialog("Processing your cancellation..");
            serverhitms = System.currentTimeMillis();
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            JSONObject reqObject = new JSONObject();
            restClient.performPutRequest(WebServiceRequestCodes.CANCEL_DROP_REQ_CODE, ServiceUrls.SWAP_REQUEST_DROP_LIST_URL + scheduLedPos.getShiftId() + "/cancelOffers", reqObject, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        if ((requestCode == WebServiceRequestCodes.CANCEL_SWAP_REQ_CODE) || (requestCode == WebServiceRequestCodes.CANCEL_DROP_REQ_CODE)) {

        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.CANCEL_SWAP_REQ_CODE) {
            ProgressWeel(requestCode, response, headers);
        } else if (requestCode == WebServiceRequestCodes.CANCEL_DROP_REQ_CODE) {
            ProgressWeel(requestCode, response, headers);
        } else if (requestCode == WebServiceRequestCodes.SHIFT_SWAP_COUNT_REQ_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    int totalCount = 0;
                    int seenCount = 0;
                    int declinedCount = 0;
                    int claimedCount = 0;
                    JSONArray shitSwapArray = object.getJSONArray("shiftSwapOffers");
                    for (int i = 0; i < shitSwapArray.length(); i++) {
                        totalCount += 1;
                        JSONObject swapObj = shitSwapArray.getJSONObject(i);
                        String offerStatus = swapObj.getString("offerStatus");
                        if (offerStatus.equalsIgnoreCase("Claimed")) {
                            claimedCount += 1;
                        } else if (offerStatus.equalsIgnoreCase("Declined")) {
                            declinedCount += 1;
                        }

                        if (swapObj.getBoolean("seen")) {
                            seenCount += 1;
                        }
                        statusesponse = response.toString();
                        viewed_showdetailsIv.setVisibility(View.VISIBLE);
                        offerLL.setClickable(true);
                        offerLL.setOnClickListener(this);
                        viewedTv.setText(claimedCount + " claimed, " + totalCount + " offered");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onSuccessSetData(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.CANCEL_SWAP_REQ_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    scheduLedPos = LegionUtils.getUpdatedSchedule(object, scheduLedPos);
                    if (scheduLedPos != null) {
                        setDataToViews();
                        showSuccessAlert(this, "Your request has been canceled!", R.drawable.confirmation, true);
                        prefsManager.save(Prefs_Keys.REFRESH, "REFRESH");
                    }
                } else {
                    showSuccessAlert(this, "Oops, your request could not be canceled at this point.", R.drawable.error_transient, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == WebServiceRequestCodes.CANCEL_DROP_REQ_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    scheduLedPos = LegionUtils.getUpdatedSchedule(object, scheduLedPos);
                    if (scheduLedPos != null) {
                        setDataToViews();
                        showSuccessAlert(this, "Your request has been canceled!", R.drawable.confirmation, true);
                        prefsManager.save(Prefs_Keys.REFRESH, "REFRESH");
                    }
                } else {
                    showSuccessAlert(this, "Oops, your request could not be droped at this point.", R.drawable.error_transient, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void ProgressWeel(final int requestCode, final Object response, final Object headers) {
        long uiwaittime = serverhitms + 4000;
        long cTime = System.currentTimeMillis();
        if (cTime < uiwaittime) {        //main
            long waitingTime = uiwaittime - cTime;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                        onSuccessSetData(requestCode, response, headers);
                    }
                }
            }, waitingTime);
        } else {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
                onSuccessSetData(requestCode, response, headers);
            }
        }
    }

    private void showLoadingDialog(String msg) {
        loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setContentView(R.layout.loading_dialog);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) loadingDialog.findViewById(R.id.dialogParentLayout));
        TextView message = (TextView) loadingDialog.findViewById(R.id.message);
        message.setText(msg);
        loadingDialog.show();
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
        if (reasonPhrase != null) {
            if (requestCode == WebServiceRequestCodes.CANCEL_SWAP_REQ_CODE) {
                if (reasonPhrase.contains("Something went wrong")) {
                    LegionUtils.doLogout(this);
                    return;
                }
                showToast(reasonPhrase);
            } else if (requestCode == WebServiceRequestCodes.CANCEL_DROP_REQ_CODE) {
                if (reasonPhrase.contains("Something went wrong")) {
                    LegionUtils.doLogout(this);
                    return;
                }
                showToast(reasonPhrase);

            } else if (requestCode == WebServiceRequestCodes.SHIFT_SWAP_COUNT_REQ_CODE) {
                if (reasonPhrase.contains("Something went wrong")) {
                    LegionUtils.doLogout(this);
                    return;
                }
                showToast(reasonPhrase);
            }
        }
    }

    public void showDialog(Activity activity) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.cancel_swap_dialog);
        final TextView msgTv = (TextView) dialog.findViewById(R.id.msgTv);
        final TextView cancelTv = (TextView) dialog.findViewById(R.id.cancelTv);
        final TextView cancelSwapTv = (TextView) dialog.findViewById(R.id.cancelSwapTv);
        if (scheduLedPos.getOfferStatus().equalsIgnoreCase("SwapPending")) {
            cancelSwapTv.setText("Cancel Swap");
            msgTv.setText("Are you sure you want to Cancel your Shift Swap Request?");
        } else if (scheduLedPos.getOfferStatus().equalsIgnoreCase("DropPending")) {
            cancelSwapTv.setText("Cancel Drop");
            msgTv.setText("Are you sure you want to Cancel your Shift Drop Request?");
        }

        LegionUtils.doApplyFont(getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancelSwapTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (scheduLedPos.getOfferStatus().equalsIgnoreCase("SwapPending")) {
                    doCancelForSwap();
                } else if (scheduLedPos.getOfferStatus().equalsIgnoreCase("DropPending")) {
                    doCancelForDrop();
                }
            }
        });
        dialog.show();

    }

    public void showSuccessAlert(Context context, String msg, int drawable, final boolean isSuccess) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.good_job_dialog);
        final TextView msgTv = (TextView) dialog.findViewById(R.id.msgTv);
        final ImageView statusIv = (ImageView) dialog.findViewById(R.id.statusIv);
        statusIv.setImageResource(drawable);
        final TextView okTv = (TextView) dialog.findViewById(R.id.okTv);
        msgTv.setText(msg);
        LegionUtils.doApplyFont(context.getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shiftOffer.setOfferStatus(!isSuccess ? "Declined" : "Accepted");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
