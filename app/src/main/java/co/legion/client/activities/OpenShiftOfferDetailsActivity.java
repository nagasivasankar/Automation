package co.legion.client.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import adapters.ScheduleShiftDetailsListAdapter;
import asynctasks.ResponseParserTask;
import base.Legion_BaseActivity;
import co.legion.client.R;
import fragments.ComingSoonDialogFragment;
import interfaces.ResponseParserListener;
import models.AssociatedWorker;
import models.BusinessKey;
import models.ShiftDeclinedVo;
import models.ShiftOffer;
import models.StaffingShift;
import models.Worker;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import ui.HorizontalListView;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 1/5/2017.
 */
public class OpenShiftOfferDetailsActivity extends Legion_BaseActivity implements View.OnClickListener, ResponseParserListener, Legion_NetworkCallback, Legion_Constants {

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
    private TextView textLinearShiftLead;
    private LinearLayout linearDirections;
    private TextView tvDirections;
    private LinearLayout linearMobileNumber;
    private TextView tvMobileNumber;
    private TextView tvNotesDescription;
    private Button claimShiftOfferBtn;
    private int demandPeakHours = 0;
    private ArrayList<AssociatedWorker> associatedWorkerArrayList = new ArrayList<>();
    private TextView tvToolbarTile;
    private View viewLine;
    private TextView offerAppTv;
    private LinearLayout offerLL;
    private View offerShiftLine;
    private ShiftOffer shiftOffer;
    private ImageView bookmarkImage;
    private TextView declineTv;
    private Button previewInSchedulesButton;
    private boolean isOfferUpdated;
    private Dialog confirmDialog;
    private Dialog declineDialog;
    private String shiftStartDate;
    private String shiftEndDate;
    private LinearLayout offerAppLL;
    private long serverhitms;
    private ShiftDeclinedVo declinedVo = new ShiftDeclinedVo();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_shift_offer_details);
        findViews();

    }

    private void findViews() {
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        bookmarkImage = (ImageView) findViewById(R.id.bookmarkImage);
        bookmarkImage.setVisibility(View.VISIBLE);
        bookmarkImage.setOnClickListener(this);
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        tvTimeInterval = (TextView) findViewById(R.id.tv_time_interval);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        viewLine = (View) findViewById(R.id.viewLine);
        tvPeakHours = (TextView) findViewById(R.id.tv_peak_hours);
        tvCost = (TextView) findViewById(R.id.tv_cost);
        tvCostType = (TextView) findViewById(R.id.tv_cost_type);
        offerShiftLine = (View) findViewById(R.id.offerShiftLine);
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
        claimShiftOfferBtn = (Button) findViewById(R.id.btn_claim_your_shift_offer);
        offerAppTv = (TextView) findViewById(R.id.offerAppTv);
        offerLL = (LinearLayout) findViewById(R.id.offerLL);
        offerAppLL = (LinearLayout) findViewById(R.id.offerAppLL);
        claimShiftOfferBtn.setOnClickListener(this);
        backImage.setOnClickListener(this);
        linearShiftLead.setOnClickListener(this);
        declineTv = (TextView) findViewById(R.id.declineTv);
        declineTv.setOnClickListener(this);
        previewInSchedulesButton = (Button) findViewById(R.id.btn_preview_in_schedules);
        previewInSchedulesButton.setOnClickListener(this);
        try {
            shiftOffer = (ShiftOffer) getIntent().getExtras().get(Extras_Keys.WORKSHIFTS_LIST);
        } catch (Exception e) {
            Log.d("Exception", "Maybe");
            finish();
        }
        doUpdateUI();
        if (!shiftOffer.isSeen()) {
            doUpdateOpenShiftOffer(WebServiceRequestCodes.SEEN_SHIFT_OFFER_REQUEST_CODE, shiftOffer, "see", false);
        }
    }

    private void doUpdateUI() {
        try {
            if (shiftOffer.isPinned()) {
                bookmarkImage.setImageResource(R.drawable.ic_bookmark_on);
            } else {
                bookmarkImage.setImageResource(R.drawable.ic_bookmark_off);
            }
            if (shiftOffer.getStaffingShift().isHasMeal()) {
                tvTimeInterval.setCompoundDrawablesWithIntrinsicBounds(R.drawable.meal_included, 0, 0, 0);
            }
            try {
                Glide.with(this).load(shiftOffer.getStaffingShift().getBusinessKey().getPhotoUrlsList().get(0)).into(ivImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(shiftOffer.getStaffingShift().getShiftStartDate().toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            String finalDate = fmtOut.format(date);
            String offerStatus = shiftOffer.getOfferStatus();
            tvToolbarTile.setText("Open Shift - " + getDayName(Integer.parseInt(shiftOffer.getStaffingShift().getDayOfTheWeek())) + ", " + finalDate);
            if (getIntent().getExtras().getString(Extras_Keys.STARTTIME) != null) {
                String startTime = getIntent().getExtras().getString(Extras_Keys.STARTTIME);
                String endTime = getIntent().getExtras().getString(Extras_Keys.ENDTIME);
                String totalShiftHours = getIntent().getStringExtra("totalShiftHours");
                prefsManager.save(Prefs_Keys.TOTAL_SHIFT_HOURS, totalShiftHours);

                tvTime.setText((getIntent().getExtras().getString(Extras_Keys.SHIFT_MINS)));
                prefsManager.save(Prefs_Keys.TOTAL_SHIFT_MINS, (getIntent().getExtras().getString(Extras_Keys.SHIFT_MINS)));
                tvTimeInterval.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + shiftOffer.getStaffingShift().getRole().toUpperCase());
                if (totalShiftHours.equals("1")) {
                    tvTextHours.setText("Hr This Week");
                } else {
                    tvTextHours.setText("Hrs This Week");
                }
            } else if (getIntent().getExtras().getString(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS) != null) {

                String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getStaffingShift().getStartMin()));
                String endTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getStaffingShift().getEndMin()));
                //   String totalShiftHours = prefsManager.get(Prefs_Keys.TOTAL_SHIFT_HOURS, "8 Hrs");
                String totalShiftHours = LegionUtils.convertMinsToHrsReg(shiftOffer.getStaffingShift().getRegularMinutes()) + " Hrs";
                tvTime.setText(getIntent().getExtras().getString(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS));
                tvTimeInterval.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + shiftOffer.getStaffingShift().getRole().toUpperCase());
                if (totalShiftHours != null && totalShiftHours.equals("1")) {
                    tvTextHours.setText("Hr This Week");
                } else {
                    tvTextHours.setText("Hrs This Week");
                }
            } else {
                String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getStaffingShift().getStartMin()));
                String endTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getStaffingShift().getEndMin()));
                String totalShiftHours = prefsManager.get(Prefs_Keys.TOTAL_SHIFT_HOURS, "8 Hrs");
                tvTime.setText(prefsManager.get(Prefs_Keys.TOTAL_SHIFT_MINS));
                tvTimeInterval.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + shiftOffer.getStaffingShift().getRole().toUpperCase());
                if (totalShiftHours != null && totalShiftHours.equals("1")) {
                    tvTextHours.setText("Hr This Week");
                } else {
                    tvTextHours.setText("Hrs This Week");
                }
            }
            if (prefsManager.get(Prefs_Keys.BUSSINESS_KEY).equals(shiftOffer.getStaffingShift().getBusinessKey().getBusinessId())) {
                tvAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_location, 0, 0, 0);
            }
            tvAddress.setText(LegionUtils.getUpdatedAddress(shiftOffer.getStaffingShift().getBusinessKey().getAddress()));
            // int estPay = LegionUtils.getEstimatedPayAsInt(shiftOffer.getEstimatedPay());
            int estPay = (int) (Double.parseDouble(shiftOffer.getEstimatedPay()));
            tvCost.setText("$" + estPay);
            if (shiftOffer.getStaffingShift().getNotes().equals("null") || (shiftOffer.getStaffingShift().getNotes().isEmpty())) {
                tvNotesDescription.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                findViewById(R.id.noteText).setVisibility(View.GONE);
            } else {
                viewLine.setVisibility(View.VISIBLE);
                findViewById(R.id.noteText).setVisibility(View.VISIBLE);
                tvNotesDescription.setText(shiftOffer.getStaffingShift().getNotes().equals("null") ? "" : shiftOffer.getStaffingShift().getNotes());
            }

            /*if (!offerStatus.equalsIgnoreCase("null") && !(offerStatus.equalsIgnoreCase("NotOffered"))) {
                btnOfferYourShift.setVisibility(View.GONE);
                if (offerStatus.equalsIgnoreCase("SwapPending")) {
                    //btnCancelShift.setText("Cancel your Swap Request");
                    // btnCancelShift.setVisibility(View.VISIBLE);
                } else if (offerStatus.equalsIgnoreCase("DropPending")) {
                    //btnCancelShift.setText("Cancel your Drop Request");
                    //btnCancelShift.setVisibility(View.VISIBLE);
                } else {
                    //btnCancelShift.setVisibility(View.GONE);
                }
            } else {
                // btnCancelShift.setVisibility(View.GONE);
                if (LegionUtils.isCurrentWeek(shiftOffer.getStaffingShift().getShiftStartDate().replace("T", " ").replace("Z", " "), shiftOffer.getStaffingShift().getShiftEndDate().replace("T", " ").replace("Z", " "))) {
                    long minutes = LegionUtils.getTimeMatchedOrNot(shiftOffer.getStaffingShift().getShiftStartDate());
                    long difference = (minutes / LegionUtils.MINUTE) + 1;
                    if (difference > 0) {
                        btnOfferYourShift.setVisibility(View.VISIBLE);
                    } else {
                        btnOfferYourShift.setVisibility(View.GONE);
                    }
                } else if (LegionUtils.isPastWeek(shiftOffer.getStaffingShift().getShiftStartDate().replace("T", " ").replace("Z", " "), shiftOffer.getStaffingShift().getShiftEndDate().replace("T", " ").replace("Z", " "))) {
                    btnOfferYourShift.setVisibility(View.GONE);
                    //offerShiftLine.setVisibility(View.GONE);
                } else {
                    btnOfferYourShift.setVisibility(View.VISIBLE);
                    offerShiftLine.setVisibility(View.VISIBLE);
                }
            }*/
            tvDirections.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + shiftOffer.getStaffingShift().getBusinessKey().getAddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });


            tvMobileNumber.setText(prefsManager.get(Prefs_Keys.BUSINESS_PHONE_NUMBER));
            tvMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = "Do you want to call the store " + shiftOffer.getStaffingShift().getBusinessKey().getName() + "?";
                    final Dialog dialog = LegionUtils.callDialog(OpenShiftOfferDetailsActivity.this, msg);
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
            String availabilityMatchOrNot = shiftOffer.getStaffingShift().getAvailability();
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
            if (shiftOffer.getAssociatedWorkerArrayList() != null) {
                associatedWorkerArrayList.addAll(shiftOffer.getAssociatedWorkerArrayList());
            }
            HorizontalListView horizontalListview = (HorizontalListView) findViewById(R.id.hlv);
            ScheduleShiftDetailsListAdapter scheduleShiftDetailsListAdapter = new ScheduleShiftDetailsListAdapter(OpenShiftOfferDetailsActivity.this, associatedWorkerArrayList);
            horizontalListview.setAdapter(scheduleShiftDetailsListAdapter);

            setHeaderFooter(shiftOffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissProgressWheelWhenMinTimeReached() {
        long uiwaittime = serverhitms + 4000;
        long newrecordedTime = System.currentTimeMillis();
        if (newrecordedTime < uiwaittime) {        //main
            long waitingTime = uiwaittime - newrecordedTime;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (confirmDialog != null) {
                        confirmDialog.dismiss();
                        doLoadShiftOfferDetails();
                    }
                }

            }, waitingTime);
        } else {
            if (confirmDialog != null) {
                confirmDialog.dismiss();
                doLoadShiftOfferDetails();
            }
        }
    }

    private void dismissDeclinedAlert() {
        long uiwaittime = serverhitms + 4000;
        long newrecordedTime = System.currentTimeMillis();
        if (newrecordedTime < uiwaittime) {        //main
            long waitingTime = uiwaittime - newrecordedTime;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (declineDialog != null) {
                        declineDialog.dismiss();
                        showDeclineSuccessAlert(OpenShiftOfferDetailsActivity.this, "All right, the offer is declined.", R.drawable.confirmation, true);
                    }
                }

            }, waitingTime);
        } else {
            if (declineDialog != null) {
                declineDialog.dismiss();
                showDeclineSuccessAlert(OpenShiftOfferDetailsActivity.this, "All right, the offer is declined.", R.drawable.confirmation, true);

            }
        }
    }

    private String getDayName(int position) {
        if (position == 1) {
            return "Mon";
        } else if (position == 2) {
            return "Tue";
        } else if (position == 3) {
            return "Wed";
        } else if (position == 4) {
            return "Thu";
        } else if (position == 5) {
            return "Fri";
        } else if (position == 6) {
            return "Sat";
        } else if (position == 7) {
            return "Sun";
        }
        return "";
    }


    @Override
    public void onStartRequest(int requestCode) {
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        isOfferUpdated = true;
        try {
            JSONObject mainJsonObject = new JSONObject(response.toString());
            String responseStatus = mainJsonObject.optString("responseStatus");
            String errorString = mainJsonObject.optString("errorString");
            String operationStatus = mainJsonObject.optString("operationStatus");

            if (requestCode == WebServiceRequestCodes.GET_SHIFT_OFFER_DETAILS) {
                LegionUtils.hideProgressDialog();
                if (responseStatus.equalsIgnoreCase("SUCCESS") && shiftOffer != null) {
                    new ResponseParserTask(ResponseParserConstants.PARSE_SHIFT_OFFERS, prefsManager, this).execute(response.toString());

                }
            }
            if (responseStatus.equalsIgnoreCase("SUCCESS") && shiftOffer != null) {
                if (requestCode == WebServiceRequestCodes.BOOKMARK_SHIFT_OFFER) {
                    LegionUtils.hideProgressDialog();
                    shiftOffer.setPinned(true);
                    bookmarkImage.setImageResource(R.drawable.ic_bookmark_on);
                } else if (requestCode == WebServiceRequestCodes.UNBOOKMARK_SHIFT_OFFER) {
                    LegionUtils.hideProgressDialog();
                    shiftOffer.setPinned(false);
                    bookmarkImage.setImageResource(R.drawable.ic_bookmark_off);
                } else if (requestCode == WebServiceRequestCodes.DECLINE_SHIFT_OFFER) {
                    //  dismissDeclinedAlert();
                    shiftOffer.setOfferStatus("Declined");
                    setHeaderFooter(shiftOffer);
                    showDeclineSuccessAlert(this, "All right, the offer is declined.", R.drawable.confirmation, true);
                    //doLoadShiftOfferDetails();
                }
            } else if (responseStatus.contains("FAIL")) {
                if (requestCode == WebServiceRequestCodes.DECLINE_SHIFT_OFFER) {
                    if (errorString.contains("Found")) {
                        showOpenShiftOfferAlert(this, "", "Sorry, this offer is no longer available!<br/><br/>Please check out other available offers.", R.drawable.error_transient);
                    } else {
                        showOpenShiftOfferAlert(this, "", "An error occured declining the shift.", R.drawable.error_transient);
                    }
                }
            }

            if (requestCode == WebServiceRequestCodes.CLAIM_SHIFT_OFFER) {
                dismissProgressWheelWhenMinTimeReached();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LegionUtils.hideProgressDialog();
        }
    }

    private void doLoadShiftOfferDetails() {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SHIFT_OFFER_DETAILS, ServiceUrls.GET_SHIFT_ORDERS_URL + "/" + shiftOffer.getOfferId(), new RequestParams(), prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showOpenShiftOfferAlert(Context context, String msg, String jobdescption, int drawable) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.good_job_dilalog_with_desc);
        final TextView msgTv = (TextView) dialog.findViewById(R.id.msgTv);
        final TextView jobdesc = (TextView) dialog.findViewById(R.id.job_desc);
        final ImageView statusIv = (ImageView) dialog.findViewById(R.id.statusIv);
        statusIv.setImageResource(drawable);
        final TextView okTv = (TextView) dialog.findViewById(R.id.okTv);
        msgTv.setText(msg);
        jobdesc.setText(Html.fromHtml(jobdescption));
        LegionUtils.doApplyFont(context.getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //shiftOffer.setOfferStatus(!isSuccess ? "Declined" : "Accepted");
                setHeaderFooter(shiftOffer);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showDeclineSuccessAlert(Context context, String msg, int drawable, final boolean isSuccess) {
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
                onBackPressed();
            }
        });
        setHeaderFooter(shiftOffer);
        dialog.show();
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        if (isOfferUpdated) {
            setResult(RESULT_OK, i);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbarBack:
                onBackPressed();
                break;

            case R.id.bookmarkImage:
                boolean isBookmarked = shiftOffer.isPinned();
                if (!isBookmarked) {
                    doUpdateOpenShiftOffer(WebServiceRequestCodes.BOOKMARK_SHIFT_OFFER, shiftOffer, "pin", true);
                } else {
                    if (!LegionUtils.isOnline(this)) {
                        LegionUtils.showOfflineDialog(this);
                        return;
                    }
                    LegionUtils.hideKeyboard(this);
                    LegionUtils.showProgressDialog(this);
                    Log.v("SESSION_ID", "" + prefsManager.get(Prefs_Keys.SESSION_ID));
                    try {
                        Legion_RestClient restClient = new Legion_RestClient(this, this);
                        restClient.performDeleteRequest(WebServiceRequestCodes.UNBOOKMARK_SHIFT_OFFER, ServiceUrls.UPDATE_SHIFT_OFFER_URL + shiftOffer.getOfferId() + "/pin", new RequestParams(), prefsManager.get(Prefs_Keys.SESSION_ID), this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.declineTv:
                //  doUpdateOpenShiftOffer(WebServiceRequestCodes.DECLINE_SHIFT_OFFER, shiftOffer, "decline", true);
                showDeclineAlert();
                break;

            case R.id.btn_claim_your_shift_offer:
                showConfirmAlert();
                break;
            case R.id.btn_preview_in_schedules:
                Intent previewInScheduleActivity = new Intent(this, PreviewInScheduleActivity.class);
                int dayOfTheWeek = Integer.parseInt(shiftOffer.getStaffingShift().getDayOfTheWeek());
                shiftStartDate = shiftOffer.getStaffingShift().getShiftStartDate();
                shiftEndDate = shiftOffer.getStaffingShift().getShiftEndDate();
                try {
                    String dt = shiftOffer.getStaffingShift().getShiftStartDate().replace("T", " ");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    Calendar c = Calendar.getInstance();
                    c.setTime(sdf.parse(dt));
                    c.add(Calendar.DATE, (-1 * dayOfTheWeek) + 1);
                    shiftStartDate = sdf.format(c.getTime());
                    c.add(Calendar.DATE, 6);
                    shiftEndDate = sdf.format(c.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                prefsManager.save(Prefs_Keys.START_DATE, shiftStartDate);
                prefsManager.save(Prefs_Keys.END_DATE, shiftEndDate);
                previewInScheduleActivity.putExtra(Extras_Keys.SHIFT_OFFER_KEY, shiftOffer);
                startActivity(previewInScheduleActivity);
                break;
            case R.id.linear_shift_lead:
                ComingSoonDialogFragment fragment = new ComingSoonDialogFragment();
                Bundle b = new Bundle();
                b.putString(Extras_Keys.MESSAGE, "Running a few minutes late,or have a question about a shift? <br/><br/> Just <b>Message the Store Lead</b> directly, and address the matter quickly and easily.\n");
                fragment.setArguments(b);
                fragment.show(getSupportFragmentManager(), "");
                break;
        }
    }

    private void showDeclineAlert() {
        declineDialog = new Dialog(this);
        declineDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        declineDialog.setCancelable(true);
        declineDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        declineDialog.setContentView(R.layout.shift_decline_dailog);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) declineDialog.findViewById(R.id.dialogParentLayout));
        final TextView cancelTv = (TextView) declineDialog.findViewById(R.id.cancelTv);
        final TextView declineTv = (TextView) declineDialog.findViewById(R.id.declineTv);
        final TextView declineTitle = (TextView) declineDialog.findViewById(R.id.declineTitle);
        final ImageView timeconflict_Iv = (ImageView) declineDialog.findViewById(R.id.timeconflict_Iv);
        final ImageView locationIv = (ImageView) declineDialog.findViewById(R.id.locationIv);
        final EditText reason_Et = (EditText) declineDialog.findViewById(R.id.reason_Et);
        final LinearLayout alertLL = (LinearLayout) declineDialog.findViewById(R.id.alertLL);
        final LinearLayout processingLL = (LinearLayout) declineDialog.findViewById(R.id.processingLL);
        declineTitle.setText("Decline Open Shift Offer");
        reason_Et.setSelection(reason_Et.getText().toString().trim().length());
        timeconflict_Iv.setImageResource(0);
        timeconflict_Iv.setBackgroundResource(R.drawable.bg_white_circle_shifts);
        locationIv.setImageResource(0);
        locationIv.setBackgroundResource(R.drawable.bg_white_circle_shifts);
        declinedVo.setTimeconflict(false);
        declinedVo.setLocationIssue(false);
        final ArrayList<String> reasonitems = new ArrayList<>();
        reasonitems.clear();

        timeconflict_Iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!declinedVo.isTimeconflict()) {
                    timeconflict_Iv.setImageResource(R.drawable.ic_save_check_mark);
                    timeconflict_Iv.setBackgroundResource(R.drawable.bg_blue_circle_shifts);
                    reasonitems.add("TimeConflict");
                    declinedVo.setTimeconflict(true);
                } else {
                    timeconflict_Iv.setImageResource(0);
                    timeconflict_Iv.setBackgroundResource(R.drawable.bg_white_circle_shifts);
                    declinedVo.setTimeconflict(false);
                    reasonitems.remove("TimeConflict");
                }
            }
        });

        locationIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!declinedVo.isLocationIssue()) {
                    locationIv.setImageResource(R.drawable.ic_save_check_mark);
                    locationIv.setBackgroundResource(R.drawable.bg_blue_circle_shifts);
                    reasonitems.add("LocationConflict");
                    declinedVo.setLocationIssue(true);
                } else {
                    locationIv.setImageResource(0);
                    locationIv.setBackgroundResource(R.drawable.bg_white_circle_shifts);
                    declinedVo.setLocationIssue(false);
                    reasonitems.remove("LocationConflict");
                }
            }
        });

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declinedVo.setTimeconflict(false);
                declinedVo.setLocationIssue(false);
                declineDialog.dismiss();
            }
        });
        declineTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  alertLL.setVisibility(View.GONE);
                //   processingLL.setVisibility(View.VISIBLE);
                //  serverhitms = System.currentTimeMillis();
                declinedVo.setTimeconflict(false);
                declinedVo.setLocationIssue(false);
                //doUpdateOpenShiftOffer(WebServiceRequestCodes.DECLINE_SHIFT_OFFER, shiftOffer, "decline", true);
                if (!LegionUtils.isOnline(OpenShiftOfferDetailsActivity.this)) {
                    LegionUtils.showOfflineDialog(OpenShiftOfferDetailsActivity.this);
                    return;
                }
                LegionUtils.hideKeyboard(OpenShiftOfferDetailsActivity.this);
                Log.v("SESSION_ID", "" + prefsManager.get(Prefs_Keys.SESSION_ID));
                try {
                    String resonsarray = TextUtils.join(",", reasonitems);
                    String reason_text = reason_Et.getText().toString();
                    JSONObject resonreqObj = new JSONObject();
                    resonreqObj.put("reason", resonsarray.isEmpty() ? JSONObject.NULL : resonsarray);
                    resonreqObj.put("reasonText", reason_text);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(resonreqObj);
                    JSONObject shiftswapoffersObj = new JSONObject();
                    shiftswapoffersObj.put("shiftSwapOffers", jsonArray);
                    LegionUtils.showProgressDialog(OpenShiftOfferDetailsActivity.this);
                    final Legion_RestClient restClient = new Legion_RestClient(OpenShiftOfferDetailsActivity.this, OpenShiftOfferDetailsActivity.this);
                    restClient.performPutRequest(WebServiceRequestCodes.DECLINE_SHIFT_OFFER, ServiceUrls.UPDATE_SHIFT_OFFER_URL + shiftOffer.getOfferId() + "/decline", shiftswapoffersObj, prefsManager.get(Prefs_Keys.SESSION_ID), OpenShiftOfferDetailsActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                declineDialog.dismiss();
            }
        });

        declineDialog.show();

    }

    private void showConfirmAlert() {
        confirmDialog = new Dialog(this);
        confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmDialog.setCancelable(false);
        confirmDialog.setCanceledOnTouchOutside(false);
        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmDialog.setContentView(R.layout.confirm_claim_offer_dialog);
        final LinearLayout alertLL = (LinearLayout) confirmDialog.findViewById(R.id.alertLL);
        final LinearLayout processingLL = (LinearLayout) confirmDialog.findViewById(R.id.processingLL);
        final TextView msgTV = (TextView) confirmDialog.findViewById(R.id.msgTv);

        try {
            String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getStaffingShift().getStartMin()));
            String endTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getStaffingShift().getEndMin()));

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(shiftOffer.getStaffingShift().getShiftStartDate().toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            String finalDate = fmtOut.format(date);
            msgTV.setText(startTime + " - " + endTime + " " + shiftOffer.getStaffingShift().getRole() + " on " + getDayName(Integer.parseInt(shiftOffer.getStaffingShift().getDayOfTheWeek())) + ", " + finalDate + " at " + shiftOffer.getStaffingShift().getBusinessKey().getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final TextView cancelTv = (TextView) confirmDialog.findViewById(R.id.cancelTv);
        final TextView agreeTV = (TextView) confirmDialog.findViewById(R.id.agreeTV);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) confirmDialog.findViewById(R.id.dialogParentLayout));
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        agreeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertLL.setVisibility(View.GONE);
                processingLL.setVisibility(View.VISIBLE);
                serverhitms = System.currentTimeMillis();
                //dismissProgressWheelWhenMinTimeReached();
                doUpdateOpenShiftOffer(WebServiceRequestCodes.CLAIM_SHIFT_OFFER, shiftOffer, "claim", false);
            }
        });
        confirmDialog.show();
    }

    private void doUpdateOpenShiftOffer(final int requestCode, final ShiftOffer shiftOffer, final String action, boolean showProgress) {
        if (!LegionUtils.isOnline(this)) {
            LegionUtils.showOfflineDialog(this);
            return;
        }
        LegionUtils.hideKeyboard(this);
        Log.v("SESSION_ID", "" + prefsManager.get(Prefs_Keys.SESSION_ID));
        try {
            if (showProgress) {
                LegionUtils.showProgressDialog(this);
            }
            final Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performPutRequest(requestCode, ServiceUrls.UPDATE_SHIFT_OFFER_URL + shiftOffer.getOfferId() + "/" + action, new RequestParams(), prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHeaderFooter(ShiftOffer offer) {
        if (offer.getOfferStatus().equalsIgnoreCase("Proposed") || !offer.isSeen()) {
            previewInSchedulesButton.setVisibility(View.VISIBLE);
            claimShiftOfferBtn.setVisibility(View.VISIBLE);
            declineTv.setVisibility(View.VISIBLE);
            offerAppLL.setGravity(Gravity.LEFT);
            offerAppTv.setText(getExpiryTime(offer.getExpiryTimestamp(), offer).toUpperCase());
        } else {
            previewInSchedulesButton.setVisibility(View.GONE);
            claimShiftOfferBtn.setVisibility(View.GONE);
            declineTv.setVisibility(View.GONE);
            //  offerAppTv.setGravity(Gravity.LEFT);
            if (offer.getOfferStatus().equalsIgnoreCase("Accepted")) {
                offerAppLL.setGravity(Gravity.CENTER);
                offerAppTv.setGravity(Gravity.CENTER);
                offerAppTv.setText("OFFER APPROVED");
                offerAppTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.save_disable_checkmark, 0, 0, 0);
            } else if (offer.getOfferStatus().equalsIgnoreCase("Declined")) {
                offerAppTv.setText("OFFER DECLINED");
                offerAppLL.setGravity(Gravity.CENTER);
                offerAppTv.setGravity(Gravity.CENTER);

            } else if (offer.getOfferStatus().equalsIgnoreCase("Claimed")) {
                offerAppTv.setText("APPROVAL PENDING");
                offerAppTv.setGravity(Gravity.CENTER);
                offerAppLL.setGravity(Gravity.CENTER);
            }
        }
    }

    private String getExpiryTime(long expiryTimestamp, ShiftOffer offer) {
        Calendar now = Calendar.getInstance();

        Calendar expiryCal = Calendar.getInstance();
        expiryCal.setTimeInMillis(expiryTimestamp);

        long diffDays = TimeUnit.MILLISECONDS.toDays(expiryCal.getTimeInMillis() - now.getTimeInMillis());
        if (expiryCal.getTimeInMillis() < now.getTimeInMillis()) {
            if (offer.getOfferStatus().equalsIgnoreCase("Proposed")) {
                return "Offer Proposed";
            } else if (offer.getOfferStatus().equalsIgnoreCase("Claimed")) {
                return "Offer Accepted";
            } else if (offer.getOfferStatus().equalsIgnoreCase("Accepted")) {
                return "Offer Approved";
            } else if (offer.getOfferStatus().equalsIgnoreCase("Declined")) {
                return "Offer Rejected";
            }
            return offer.getOfferStatus();
        } else {
            if (diffDays < 1) {
                long diffHrs = TimeUnit.MILLISECONDS.toHours(expiryCal.getTimeInMillis() - now.getTimeInMillis());
                if (diffHrs < 1) {
                    long diffMins = TimeUnit.MILLISECONDS.toMinutes(expiryCal.getTimeInMillis() - now.getTimeInMillis());
                    if (diffMins < 1) {
                        return "Expires in " + TimeUnit.MILLISECONDS.toSeconds(expiryCal.getTimeInMillis() - now.getTimeInMillis()) + " seconds";
                    } else if (diffMins == 1) {
                        return "Expires in " + diffMins + " minute";
                    }
                    return "Expires in " + diffMins + " minutes";
                } else if (diffHrs == 1) {
                    return "Expires in " + diffHrs + " hr";
                }
                return "Expires in " + diffHrs + " hrs";
            } else if (diffDays >= 365) {
                int diffYears = (int) diffDays / 365;
                if (diffYears == 1) {
                    return "Expires in " + diffYears + " year";
                }
                return "Expires in " + diffYears + " years";
            } else if (diffDays >= 30) {
                int diffMonths = (int) diffDays / 30;
                if (diffMonths == 1) {
                    return "Expires in " + diffMonths + " month";
                }
                return "Expires in " + diffMonths + " months";
            } else {
                if (diffDays == 1) {
                    return "Expires in " + diffDays + " day";
                }
                return "Expires in " + diffDays + " days";
            }
        }
    }

    @Override
    public void onResponseParsingStart(int parserId) {
    }

    @Override
    public void onResponseParsingComplete(int parserId, Object response) {
        if (parserId == ResponseParserConstants.PARSE_SHIFT_OFFERS) {
            if (response instanceof Exception) {
                LegionUtils.showDialog(OpenShiftOfferDetailsActivity.this, "Oops, Something went wrong.\n\nPlease try again later.", true);
                ((Exception) response).printStackTrace();
            } else if (response instanceof String) {
                showOpenShiftOfferAlert(this, "We're sorry.", "You're no longer eligible to take this shift, as it will trigger Over Time.", R.drawable.error_transient);
            } else if (response instanceof ArrayList<?>) {
                ArrayList<ShiftOffer> offersList = (ArrayList<ShiftOffer>) response;
                shiftOffer = offersList.get(0);
                if (shiftOffer.getOfferStatus().equalsIgnoreCase("Accepted")) {
                    showOpenShiftOfferAlert(this, "Good Job, " + getNicknameifExist() + "!", "This shift is yours, and has been added to your schedule.", R.drawable.confirmation);
                } else if (shiftOffer.getOfferStatus().equalsIgnoreCase("Claimed")) {
                    showOpenShiftOfferAlert(this, "Good Job, " + getNicknameifExist() + "!", "Your request has been received and sent to the store lead for approval.", R.drawable.confirmation);
                } else if (shiftOffer.getOfferStatus().equalsIgnoreCase("Declined")) {
                    showOpenShiftOfferAlert(this, "We're sorry.", "You're no longer eligible to take this shift, as it will trigger Over Time.", R.drawable.error_transient);
                        /*if (shiftOffer.getReason() != null && shiftOffer.getReason().contains("Overtime")) {
                            showOpenShiftOfferAlert(this, "We're sorry.", "You're no longer eligible to take this shift, as it will trigger Over Time.", R.drawable.error_transient);
                        } else {
                            Log.v("declined reason", "Unhandled declined reason");
                        }*/
                } else if (shiftOffer.getOfferStatus().equalsIgnoreCase("Expired") || shiftOffer.getOfferStatus().equalsIgnoreCase("Withdrawn")) {
                    showOpenShiftOfferAlert(this, "", "Sorry, this offer is no longer available!<br/><br/>Please check out other available offers.", R.drawable.error_transient);
                }
            }
        }
    }

    private String  getNicknameifExist() {
        if (prefsManager.hasKey(Prefs_Keys.NICK_NAME) && prefsManager.get(Prefs_Keys.NICK_NAME).trim().length() > 0) {
            return prefsManager.get(Prefs_Keys.NICK_NAME);
        } else if (!prefsManager.get(Prefs_Keys.FIRST_NAME, "").equals("")) {
                return prefsManager.get(Prefs_Keys.FIRST_NAME);
        }
        return "";
    }
}

