package co.legion.client.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
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
import de.hdodenhof.circleimageview.CircleImageView;
import fragments.ComingSoonDialogFragment;
import interfaces.ResponseParserListener;
import models.AssociatedWorker;
import models.BusinessKey;
import models.ShiftDeclinedVo;
import models.ShiftOffer;
import models.ShiftOfferedRequested;
import models.Worker;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import ui.HorizontalListView;
import utils.LegionUtils;
import utils.Legion_Constants;

import static java.security.AccessController.getContext;

/**
 * Created by Administrator on 1/10/2017.
 */
public class SwapShiftOfferDetailsActivity extends Legion_BaseActivity implements View.OnClickListener, ResponseParserListener, Legion_NetworkCallback, Legion_Constants {

    private TextView tvToolbarTile;
    private ImageView bookmarkImage;
    private TextView offerAppTv;
    private Button previewInSchedulesButton;
    private TextView declineTv;
    private LinearLayout offerLL;
    private RelativeLayout container1Header;
    private TextView header1TitleTV;
    private ImageView header1ExapndCollapseImage;
    private RelativeLayout container2Header;
    private TextView header2TitleTV;
    private ImageView header2ExapndCollapseImage;
    private LinearLayout expandedContainer1;
    private LinearLayout expandedContainer2;
    private ScrollView scrollView;
    private ShiftOffer shiftOffer;

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
    private View viewLine;
    private TextView profileTv1;
    private CircleImageView shiftersIv1;
    private TextView profileTv2;
    private CircleImageView shiftersIv2;


    private TextView tvTimeInterval2;
    private TextView tvAddress2;
    private TextView tvPeakHours2;
    private TextView tvCost2;
    private TextView tvCostType2;
    private TextView tvAvailability2;
    private TextView tvTime2;
    private TextView tvTextHours2;
    private ImageView ivImage2;
    private LinearLayout linearShiftLead2;
    private TextView textLinearShiftLead2;
    private LinearLayout linearDirections2;
    private TextView tvDirections2;
    private LinearLayout linearMobileNumber2;
    private TextView tvMobileNumber2;
    private TextView tvNotesDescription2;
    private int demandPeakHours2 = 0;
    private ArrayList<AssociatedWorker> associatedWorkerArrayList2 = new ArrayList<>();
    private View viewLine2;
    private boolean isOfferUpdated;
    private Dialog confirmDialog;
    private String shiftStartDate;
    private String shiftEndDate;
    private long serverhitms;
    private LinearLayout offerAppLL;
    private Dialog declineDialog;
    private ShiftDeclinedVo declinedVo = new ShiftDeclinedVo();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_shift_offer_details);
        findViews();

    }

    private void findViews() {
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        bookmarkImage = (ImageView) findViewById(R.id.bookmarkImage);
        bookmarkImage.setVisibility(View.VISIBLE);
        bookmarkImage.setOnClickListener(this);
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setOnClickListener(this);
        offerAppTv = (TextView) findViewById(R.id.offerAppTv);
        offerLL = (LinearLayout) findViewById(R.id.offerLL);
        declineTv = (TextView) findViewById(R.id.declineTv);
        declineTv.setOnClickListener(this);
        previewInSchedulesButton = (Button) findViewById(R.id.btn_preview_in_schedules);
        previewInSchedulesButton.setOnClickListener(this);
        claimShiftOfferBtn = (Button) findViewById(R.id.btn_claim_your_shift_offer);
        claimShiftOfferBtn.setOnClickListener(this);

        container1Header = (RelativeLayout) findViewById(R.id.container1Header);
        container1Header.setOnClickListener(this);
        header1TitleTV = (TextView) findViewById(R.id.header1TitleTV);
        header1ExapndCollapseImage = (ImageView) findViewById(R.id.header1ExapndCollapseImage);

        container2Header = (RelativeLayout) findViewById(R.id.container2Header);
        container2Header.setOnClickListener(this);
        header2TitleTV = (TextView) findViewById(R.id.header2TitleTV);
        header2ExapndCollapseImage = (ImageView) findViewById(R.id.header2ExapndCollapseImage);

        expandedContainer1 = (LinearLayout) findViewById(R.id.expandedContainer1);
        expandedContainer2 = (LinearLayout) findViewById(R.id.expandedContainer2);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        tvTimeInterval = (TextView) findViewById(R.id.tv_time_interval);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvPeakHours = (TextView) findViewById(R.id.tv_peak_hours);
        viewLine = (View) findViewById(R.id.viewLine);
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

        tvTimeInterval2 = (TextView) findViewById(R.id.tv_time_interval2);
        tvAddress2 = (TextView) findViewById(R.id.tv_address2);
        tvPeakHours2 = (TextView) findViewById(R.id.tv_peak_hours2);
        viewLine2 = (View) findViewById(R.id.viewLine2);
        tvCost2 = (TextView) findViewById(R.id.tv_cost2);
        tvCostType2 = (TextView) findViewById(R.id.tv_cost_type2);
        tvAvailability2 = (TextView) findViewById(R.id.tv_availability2);
        tvTime2 = (TextView) findViewById(R.id.tv_time2);
        tvTextHours2 = (TextView) findViewById(R.id.tv_text_hours2);
        ivImage2 = (ImageView) findViewById(R.id.iv_image2);
        linearShiftLead2 = (LinearLayout) findViewById(R.id.linear_shift_lead2);
        textLinearShiftLead2 = (TextView) findViewById(R.id.text_linear_shift_lead2);
        linearDirections2 = (LinearLayout) findViewById(R.id.linear_directions2);
        tvDirections2 = (TextView) findViewById(R.id.tv_directions2);
        linearMobileNumber2 = (LinearLayout) findViewById(R.id.linear_mobile_number2);
        tvMobileNumber2 = (TextView) findViewById(R.id.tv_mobile_number2);
        tvNotesDescription2 = (TextView) findViewById(R.id.tv_notes_description2);
        profileTv1 = (TextView) findViewById(R.id.profileTv1);
        profileTv2 = (TextView) findViewById(R.id.profileTv2);
        shiftersIv1 = (CircleImageView) findViewById(R.id.shiftersIv1);
        shiftersIv2 = (CircleImageView) findViewById(R.id.shiftersIv2);
        offerAppLL = (LinearLayout) findViewById(R.id.offerAppLL);
        linearShiftLead.setOnClickListener(this);
        linearShiftLead2.setOnClickListener(this);
        
        container1Header.performClick();

        shiftOffer = (ShiftOffer) getIntent().getExtras().get(Extras_Keys.WORKSHIFTS_LIST);
        setDataToViews1();
        setDataToViews2();
        if (!shiftOffer.isSeen()) {
            doUpdateOpenShiftOffer(WebServiceRequestCodes.SEEN_SHIFT_OFFER_REQUEST_CODE, shiftOffer, "see", false);
        }
    }

    private void setDataToViews1() {
        try {
            if (shiftOffer.isPinned()) {
                bookmarkImage.setImageResource(R.drawable.ic_bookmark_on);
            } else {
                bookmarkImage.setImageResource(R.drawable.ic_bookmark_off);
            }
            if ((shiftOffer.getShiftOffered().getHasMeal() != null) && (!shiftOffer.getShiftOffered().getHasMeal().isEmpty())) {
                if (Boolean.parseBoolean(shiftOffer.getShiftOffered().getHasMeal())) {
                    tvTimeInterval.setCompoundDrawablesWithIntrinsicBounds(R.drawable.meal_included, 0, 0, 0);
                }
            }

            setImageOrText(shiftOffer.getShiftOffered(), profileTv1, shiftersIv1);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(shiftOffer.getShiftOffered().getShiftStartDate().toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            String finalDate = fmtOut.format(date);
            header1TitleTV.setText(shiftOffer.getShiftOffered().getWorkerKey().getFirstName() + " wants to trade\nthe " + getDayName(Integer.parseInt(shiftOffer.getShiftOffered().getDayOfTheWeek())) + ", " + finalDate + " Shift");
            try {
                Glide.with(this).load(shiftOffer.getShiftOffered().getBusinessKey().getPhotoUrlsList().get(0)).into(ivImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String offerStatus = shiftOffer.getOfferStatus();
            tvToolbarTile.setText("  Shift Swap - " + getDayName(Integer.parseInt(shiftOffer.getShiftOffered().getDayOfTheWeek())) + ", " + finalDate);
            if (getIntent().getExtras().getString(Extras_Keys.STARTTIME) != null) {
                String startTime = getIntent().getExtras().getString(Extras_Keys.STARTTIME);
                String endTime = getIntent().getExtras().getString(Extras_Keys.ENDTIME);
                String totalShiftHours = getIntent().getStringExtra("totalShiftHours");
                prefsManager.save(Prefs_Keys.TOTAL_SHIFT_HOURS, totalShiftHours);

                tvTime.setText((getIntent().getExtras().getString(Extras_Keys.SHIFT_MINS)));
                prefsManager.save(Prefs_Keys.TOTAL_SHIFT_MINS, (getIntent().getExtras().getString(Extras_Keys.SHIFT_MINS)));
                tvTimeInterval.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + shiftOffer.getShiftOffered().getRole().toUpperCase());
                if (totalShiftHours.equals("1")) {
                    tvTextHours.setText(" Hr This Week");
                } else {
                    tvTextHours.setText(" Hrs This Week");
                }
            } else if (getIntent().getExtras().getString(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS) != null) {

                String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftOffered().getStartMin()));
                String endTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftOffered().getEndMin()));
                //  String totalShiftHours = prefsManager.get(Prefs_Keys.TOTAL_SHIFT_HOURS, "8 Hrs");
                String totalShiftHours = LegionUtils.convertMinsToHrsReg(shiftOffer.getShiftOffered().getRegularMinutes()) + " Hrs";
                tvTime.setText(getIntent().getExtras().getString(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS));
                tvTimeInterval.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + shiftOffer.getShiftOffered().getRole().toUpperCase());
                if (totalShiftHours != null && totalShiftHours.equals("1")) {
                    tvTextHours.setText(" Hr This Week");
                } else {
                    tvTextHours.setText(" Hrs This Week");
                }
            } else {
                String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftOffered().getStartMin()));
                String endTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftOffered().getEndMin()));
                String totalShiftHours = prefsManager.get(Prefs_Keys.TOTAL_SHIFT_HOURS, "8 Hrs");
                tvTime.setText(prefsManager.get(Prefs_Keys.TOTAL_SHIFT_MINS));
                tvTimeInterval.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + shiftOffer.getShiftOffered().getRole().toUpperCase());
                if (totalShiftHours != null && totalShiftHours.equals("1")) {
                    tvTextHours.setText(" Hr This Week");
                } else {
                    tvTextHours.setText(" Hrs This Week");
                }
            }
            if (prefsManager.get(Prefs_Keys.BUSSINESS_KEY).equals(shiftOffer.getShiftOffered().getBusinessKey().getBusinessId())){
                tvAddress.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_location, 0, 0, 0);
            }
            tvAddress.setText(LegionUtils.getUpdatedAddress(shiftOffer.getShiftOffered().getBusinessKey().getAddress()));
            int estPay = LegionUtils.getEstimatedPayAsInt(shiftOffer.getShiftOffered().getEstimatedPay());
            tvCost.setText("$" + estPay);
            if (shiftOffer.getShiftOffered().getNotes().equals("null") || (shiftOffer.getShiftOffered().getNotes().isEmpty())) {
                tvNotesDescription.setVisibility(View.GONE);
                viewLine.setVisibility(View.GONE);
                findViewById(R.id.noteText).setVisibility(View.GONE);
            } else {
                viewLine.setVisibility(View.VISIBLE);
                findViewById(R.id.noteText).setVisibility(View.VISIBLE);
                tvNotesDescription.setText(shiftOffer.getShiftOffered().getNotes().equals("null") ? "" : shiftOffer.getShiftOffered().getNotes());
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
            if (LegionUtils.isCurrentWeek(shiftOffer.getShiftOffered().getShiftStartDate().replace("T", " ").replace("Z", " "), shiftOffer.getShiftOffered().getShiftEndDate().replace("T", " ").replace("Z", " "))) {
                long minutes = LegionUtils.getTimeMatchedOrNot(shiftOffer.getShiftOffered().getShiftStartDate());
                long difference = (minutes / LegionUtils.MINUTE) + 1;
                if (difference > 0) {
                    btnOfferYourShift.setVisibility(View.VISIBLE);
                } else {
                    btnOfferYourShift.setVisibility(View.GONE);
                }
            } else if (LegionUtils.isPastWeek(shiftOffer.getShiftOffered().getShiftStartDate().replace("T", " ").replace("Z", " "), shiftOffer.getShiftOffered().getShiftEndDate().replace("T", " ").replace("Z", " "))) {
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
                    try {
                        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + shiftOffer.getShiftOffered().getBusinessKey().getAddress());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    } catch (Exception e) {
                        Log.d("Exception", "Please installed Maps application.");
                    }
                }
            });


            tvMobileNumber.setText(prefsManager.get(Prefs_Keys.BUSINESS_PHONE_NUMBER));
            tvMobileNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = "Do you want to call the store " + shiftOffer.getShiftOffered().getBusinessKey().getName() + "?";
                    final Dialog dialog = LegionUtils.callDialog(SwapShiftOfferDetailsActivity.this, msg);
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
            String availabilityMatchOrNot = shiftOffer.getShiftOffered().getAvailability();
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
            if (shiftOffer.getShiftOffered().getAssociatedWorkerArrayList() != null) {
                associatedWorkerArrayList.addAll(shiftOffer.getShiftOffered().getAssociatedWorkerArrayList());
            }
            HorizontalListView horizontalListview = (HorizontalListView) findViewById(R.id.hlv);
            ScheduleShiftDetailsListAdapter scheduleShiftDetailsListAdapter = new ScheduleShiftDetailsListAdapter(SwapShiftOfferDetailsActivity.this, associatedWorkerArrayList);
            horizontalListview.setAdapter(scheduleShiftDetailsListAdapter);

            setHeaderFooter(shiftOffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataToViews2() {
        try {
            if (shiftOffer.isPinned()) {
                bookmarkImage.setImageResource(R.drawable.ic_bookmark_on);
            } else {
                bookmarkImage.setImageResource(R.drawable.ic_bookmark_off);
            }
            setImageOrText(shiftOffer.getShiftRequested(), profileTv2, shiftersIv2);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(shiftOffer.getShiftRequested().getShiftStartDate().toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            String finalDate = fmtOut.format(date);
            header2TitleTV.setText("In exchange for your\n" + getDayName(Integer.parseInt(shiftOffer.getShiftRequested().getDayOfTheWeek())) + ", " + finalDate + " Shift");
            try {
                Glide.with(this).load(shiftOffer.getShiftRequested().getBusinessKey().getPhotoUrlsList().get(0)).into(ivImage2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String offerStatus = shiftOffer.getOfferStatus();
            if (getIntent().getExtras().getString(Extras_Keys.STARTTIME) != null) {
                String startTime = getIntent().getExtras().getString(Extras_Keys.STARTTIME);
                String endTime = getIntent().getExtras().getString(Extras_Keys.ENDTIME);
                String totalShiftHours = getIntent().getStringExtra("totalShiftHours");
                prefsManager.save(Prefs_Keys.TOTAL_SHIFT_HOURS, totalShiftHours);

                tvTime2.setText((getIntent().getExtras().getString(Extras_Keys.SHIFT_MINS)));
                prefsManager.save(Prefs_Keys.TOTAL_SHIFT_MINS, (getIntent().getExtras().getString(Extras_Keys.SHIFT_MINS)));
                tvTimeInterval2.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + shiftOffer.getShiftRequested().getRole().toUpperCase());
                if (totalShiftHours.equals("1")) {
                    tvTextHours2.setText("Hr This Week");
                } else {
                    tvTextHours2.setText("Hrs This Week");
                }
                if ((totalShiftHours != null) && (!totalShiftHours.replace(" ", "").replace("Hrs", "").trim().isEmpty())) {
                    try {
                        if (Integer.parseInt(totalShiftHours.replace(" ", "").replace("Hrs", "")) >= 6)
                            tvTimeInterval2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.meal_included, 0, 0, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (getIntent().getExtras().getString(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS) != null) {
                String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftRequested().getStartMin()));
                String endTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftRequested().getEndMin()));
                //   String totalShiftHours = prefsManager.get(Prefs_Keys.TOTAL_SHIFT_HOURS, "8 Hrs");
                String totalShiftHours = LegionUtils.convertMinsToHrsReg(shiftOffer.getShiftRequested().getRegularMinutes()) + " Hrs";
                tvTime2.setText(getIntent().getExtras().getString(Extras_Keys.TOTAL_SHIFT_MINS_SHIFT_OFFERS));
                tvTimeInterval2.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + shiftOffer.getShiftRequested().getRole().toUpperCase());
                if (totalShiftHours != null && totalShiftHours.equals("1")) {
                    tvTextHours2.setText("Hr This Week");
                } else {
                    tvTextHours2.setText("Hrs This Week");
                }
               /* if ((totalShiftHours != null) && (!totalShiftHours.replace(" ", "").replace("Hrs", "").trim().isEmpty())) {
                    try {
                        if (Integer.parseInt(totalShiftHours.replace(" ", "").replace("Hrs", "")) >= 6)
                            tvTimeInterval2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.meal_included, 0, 0, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                if ((shiftOffer.getShiftRequested().getHasMeal() != null) && (!shiftOffer.getShiftRequested().getHasMeal().isEmpty())) {
                    if (Boolean.parseBoolean(shiftOffer.getShiftRequested().getHasMeal())) {
                        tvTimeInterval2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.meal_included, 0, 0, 0);
                    }
                }
            } else {
                String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftRequested().getStartMin()));
                String endTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftRequested().getEndMin()));
                String totalShiftHours = prefsManager.get(Prefs_Keys.TOTAL_SHIFT_HOURS, "8 Hrs");
                tvTime2.setText(prefsManager.get(Prefs_Keys.TOTAL_SHIFT_MINS));
                tvTimeInterval2.setText("" + startTime + " - " + endTime + " (" + totalShiftHours + ") " + shiftOffer.getShiftRequested().getRole().toUpperCase());
                if (totalShiftHours != null && totalShiftHours.equals("1")) {
                    tvTextHours2.setText("Hr This Week");
                } else {
                    tvTextHours2.setText("Hrs This Week");
                }
                if ((totalShiftHours != null) && (!totalShiftHours.replace(" ", "").replace("Hrs", "").trim().isEmpty())) {
                    try {
                        if (Integer.parseInt(totalShiftHours.replace(" ", "").replace("Hrs", "")) >= 6)
                            tvTimeInterval2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.meal_included, 0, 0, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (prefsManager.get(Prefs_Keys.BUSSINESS_KEY).equals(shiftOffer.getShiftRequested().getBusinessKey().getBusinessId())){
                tvAddress2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_location, 0, 0, 0);
            }
            tvAddress2.setText(LegionUtils.getUpdatedAddress(shiftOffer.getShiftRequested().getBusinessKey().getAddress()));
            int estPay = LegionUtils.getEstimatedPayAsInt(shiftOffer.getShiftRequested().getEstimatedPay());
            tvCost2.setText("$" + estPay);
            if (shiftOffer.getShiftRequested().getNotes().equals("null") || (shiftOffer.getShiftRequested().getNotes().isEmpty())) {
                tvNotesDescription2.setVisibility(View.GONE);
                viewLine2.setVisibility(View.GONE);
                findViewById(R.id.noteText2).setVisibility(View.GONE);
            } else {
                viewLine2.setVisibility(View.VISIBLE);
                findViewById(R.id.noteText2).setVisibility(View.VISIBLE);
                tvNotesDescription2.setText(shiftOffer.getShiftRequested().getNotes().equals("null") ? "" : shiftOffer.getShiftRequested().getNotes());
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
                if (LegionUtils.isCurrentWeek(shiftOffer.getShiftRequested().getShiftStartDate().replace("T", " ").replace("Z", " "), shiftOffer.getShiftRequested().getShiftEndDate().replace("T", " ").replace("Z", " "))) {
                    long minutes = LegionUtils.getTimeMatchedOrNot(shiftOffer.getShiftRequested().getShiftStartDate());
                    long difference = (minutes / LegionUtils.MINUTE) + 1;
                    if (difference > 0) {
                        btnOfferYourShift.setVisibility(View.VISIBLE);
                    } else {
                        btnOfferYourShift.setVisibility(View.GONE);
                    }
                } else if (LegionUtils.isPastWeek(shiftOffer.getShiftRequested().getShiftStartDate().replace("T", " ").replace("Z", " "), shiftOffer.getShiftRequested().getShiftEndDate().replace("T", " ").replace("Z", " "))) {
                    btnOfferYourShift.setVisibility(View.GONE);
                    //offerShiftLine.setVisibility(View.GONE);
                } else {
                    btnOfferYourShift.setVisibility(View.VISIBLE);
                    offerShiftLine.setVisibility(View.VISIBLE);
                }
            }*/
            tvDirections2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + shiftOffer.getShiftRequested().getBusinessKey().getAddress());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });


            tvMobileNumber2.setText(prefsManager.get(Prefs_Keys.BUSINESS_PHONE_NUMBER));
            tvMobileNumber2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = "Do you want to call the store " + shiftOffer.getShiftRequested().getBusinessKey().getName() + "?";
                    final Dialog dialog = LegionUtils.callDialog(SwapShiftOfferDetailsActivity.this, msg);
                    TextView okTv = (TextView) dialog.findViewById(R.id.saveTv);
                    okTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + tvMobileNumber2.getText().toString()));
                            startActivity(callIntent);
                        }
                    });
                    dialog.show();

                }
            });
            String availabilityMatchOrNot = shiftOffer.getShiftRequested().getAvailability();
            if (availabilityMatchOrNot != null) {
                if (availabilityMatchOrNot.equalsIgnoreCase("Yes")) {
                    tvAvailability2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.availability_match, 0, 0, 0);//green
                } else if (availabilityMatchOrNot.equalsIgnoreCase("No")) {
                    tvAvailability2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.availability_mismatch, 0, 0, 0); // red down
                } else {
                    tvAvailability2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.availability_unknown, 0, 0, 0);// grey up
                }
                tvAvailability2.setCompoundDrawablePadding(10);
            }
            if (demandPeakHours2 >= 4) {
                tvPeakHours2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.peakhr_yes, 0, 0, 0);
            } else {
                tvPeakHours2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.peakhr_no, 0, 0, 0);
            }
            associatedWorkerArrayList2.clear();
            if (shiftOffer.getShiftRequested().getAssociatedWorkerArrayList() != null) {
                associatedWorkerArrayList2.addAll(shiftOffer.getShiftRequested().getAssociatedWorkerArrayList());
            }
            HorizontalListView horizontalListview = (HorizontalListView) findViewById(R.id.hlv2);
            ScheduleShiftDetailsListAdapter scheduleShiftDetailsListAdapter = new ScheduleShiftDetailsListAdapter(SwapShiftOfferDetailsActivity.this, associatedWorkerArrayList2);
            horizontalListview.setAdapter(scheduleShiftDetailsListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartRequest(int requestCode) {

    }

    private void dismissProgressWheelWhenMinTimeReached() {
        long uiwaittime = serverhitms + 4000;
        long cTime = System.currentTimeMillis();
        if (cTime < uiwaittime) {        //main
            long waitingTime = uiwaittime - cTime;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (confirmDialog != null) {
                        confirmDialog.dismiss();
                        doLoadSwapShiftOfferDetails();
                    }
                }
            }, waitingTime);
        } else {
            if (confirmDialog != null) {
                confirmDialog.dismiss();
                doLoadSwapShiftOfferDetails();
            }
        }
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
                    new ResponseParserTask(ResponseParserConstants.PARSE_SHIFT_SWAP_OFFERS, prefsManager, this).execute(response.toString());
                }
            }
            if (responseStatus.equals("SUCCESS") && shiftOffer != null) {
                if (requestCode == WebServiceRequestCodes.BOOKMARK_SHIFT_OFFER) {
                    LegionUtils.hideProgressDialog();
                    shiftOffer.setPinned(true);
                    bookmarkImage.setImageResource(R.drawable.ic_bookmark_on);
                } else if (requestCode == WebServiceRequestCodes.UNBOOKMARK_SHIFT_OFFER) {
                    LegionUtils.hideProgressDialog();
                    shiftOffer.setPinned(false);
                    bookmarkImage.setImageResource(R.drawable.ic_bookmark_off);
                } else if (requestCode == WebServiceRequestCodes.DECLINE_SHIFT_OFFER) {
                    shiftOffer.setOfferStatus("Declined");
                    setHeaderFooter(shiftOffer);
                    showDeclineSuccessAlert(this, "All right, the offer is declined.", R.drawable.confirmation, true);
                    // doLoadSwapShiftOfferDetails();
                }
            } else if (responseStatus.contains("FAIL")) {
                if (requestCode == WebServiceRequestCodes.DECLINE_SHIFT_OFFER) {
                    if (errorString.contains("Found")) {
                        showSwapShiftOfferAlert(this, "", "Sorry, this offer is no longer available!<br/><br/>Please check out other available offers.", R.drawable.error_transient);
                    } else {
                        showSwapShiftOfferAlert(this, "", "An error occured declining the shift.", R.drawable.error_transient);
                    }
                }
            }

            if (requestCode == WebServiceRequestCodes.CLAIM_SHIFT_OFFER) {
                dismissProgressWheelWhenMinTimeReached();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void doLoadSwapShiftOfferDetails() {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SHIFT_OFFER_DETAILS, ServiceUrls.GET_SWAP_SHIFT_OFFERS + "/" + shiftOffer.getOfferId(), new RequestParams(), prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSwapShiftOfferAlert(Context context, String msg, String jobdescption, int drawable) {
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
                //shiftOffer.setOfferStatus("Declined");
                setHeaderFooter(shiftOffer);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbarBack:
                onBackPressed();
                break;

            case R.id.container1Header:
                if (expandedContainer1.getVisibility() == View.GONE) {
                    expandedContainer1.setVisibility(View.VISIBLE);
                    expandedContainer2.setVisibility(View.GONE);
                    header1ExapndCollapseImage.setImageResource(R.drawable.ic_top_black);
                    header2ExapndCollapseImage.setImageResource(R.drawable.ic_down_black);
                } else {
                    expandedContainer1.setVisibility(View.GONE);
                    header1ExapndCollapseImage.setImageResource(R.drawable.ic_down_black);
                }
                scrollView.smoothScrollTo(0, 0);
                break;

            case R.id.container2Header:
                if (expandedContainer2.getVisibility() == View.GONE) {
                    expandedContainer2.setVisibility(View.VISIBLE);
                    expandedContainer1.setVisibility(View.GONE);
                    header1ExapndCollapseImage.setImageResource(R.drawable.ic_down_black);
                    header2ExapndCollapseImage.setImageResource(R.drawable.ic_top_black);
                } else {
                    expandedContainer2.setVisibility(View.GONE);
                    header2ExapndCollapseImage.setImageResource(R.drawable.ic_down_black);
                }
                scrollView.smoothScrollTo(0, 0);
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
                        restClient.performDeleteRequest(WebServiceRequestCodes.UNBOOKMARK_SHIFT_OFFER, ServiceUrls.UPDATE_SWAP_SHIFT_OFFER_URL + shiftOffer.getOfferId() + "/pin", new RequestParams(), prefsManager.get(Prefs_Keys.SESSION_ID), this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.declineTv:
                //   doUpdateOpenShiftOffer(WebServiceRequestCodes.DECLINE_SHIFT_OFFER, shiftOffer, "decline", true);
                showDeclineAlert();
                break;

            case R.id.btn_claim_your_shift_offer:
                showConfirmAlert();
                break;

            case R.id.linear_shift_lead2:
                ComingSoonDialogFragment fragment = new ComingSoonDialogFragment();
                Bundle b = new Bundle();
                b.putString(Extras_Keys.MESSAGE, "Running a few minutes late,or have a question about a shift? <br/><br/> Just <b>Message the Store Lead</b> directly, and address the matter quickly and easily.\n");
                fragment.setArguments(b);
                fragment.show(getSupportFragmentManager(), "");
                break;

            case R.id.linear_shift_lead:
                ComingSoonDialogFragment fragment1 = new ComingSoonDialogFragment();
                Bundle b1 = new Bundle();
                b1.putString(Extras_Keys.MESSAGE, "Running a few minutes late,or have a question about a shift? <br/><br/> Just <b>Message the Store Lead</b> directly, and address the matter quickly and easily.\n");
                fragment1.setArguments(b1);
                fragment1.show(getSupportFragmentManager(), "");
                break;
            case R.id.btn_preview_in_schedules:
                Intent previewInScheduleActivity = new Intent(this, PreviewInScheduleActivity.class);
                int dayOfTheWeek = Integer.parseInt(shiftOffer.getShiftOffered().getDayOfTheWeek());
                shiftStartDate = shiftOffer.getShiftOffered().getShiftStartDate();
                shiftEndDate = shiftOffer.getShiftOffered().getShiftEndDate();
                try {
                    String dt = shiftOffer.getShiftOffered().getShiftStartDate().replace("T", " ");
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
        final ImageView timeconflict_Iv = (ImageView) declineDialog.findViewById(R.id.timeconflict_Iv);
        final ImageView locationIv = (ImageView) declineDialog.findViewById(R.id.locationIv);
        final EditText reason_Et = (EditText) declineDialog.findViewById(R.id.reason_Et);
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
                    reasonitems.remove("LocationConflict");
                    declinedVo.setLocationIssue(false);
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
                //   doUpdateOpenShiftOffer(WebServiceRequestCodes.DECLINE_SHIFT_OFFER, shiftOffer, "decline", true);
                declinedVo.setTimeconflict(false);
                declinedVo.setLocationIssue(false);
                if (!LegionUtils.isOnline(SwapShiftOfferDetailsActivity.this)) {
                    LegionUtils.showOfflineDialog(SwapShiftOfferDetailsActivity.this);
                    return;
                }
                LegionUtils.hideKeyboard(SwapShiftOfferDetailsActivity.this);
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
                    LegionUtils.showProgressDialog(SwapShiftOfferDetailsActivity.this);
                    final Legion_RestClient restClient = new Legion_RestClient(SwapShiftOfferDetailsActivity.this, SwapShiftOfferDetailsActivity.this);
                    restClient.performPutRequest(WebServiceRequestCodes.DECLINE_SHIFT_OFFER, ServiceUrls.UPDATE_SWAP_SHIFT_OFFER_URL + shiftOffer.getOfferId() + "/decline", shiftswapoffersObj, prefsManager.get(Prefs_Keys.SESSION_ID), SwapShiftOfferDetailsActivity.this);
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
        // confirmDialog.setBackgroundResource(android.R.color.transparent);
        confirmDialog.setContentView(R.layout.confirm_claim_offer_dialog);
        final LinearLayout alertLL = (LinearLayout) confirmDialog.findViewById(R.id.alertLL);
        final LinearLayout processingLL = (LinearLayout) confirmDialog.findViewById(R.id.processingLL);
        final TextView msgTV = (TextView) confirmDialog.findViewById(R.id.msgTv);
        final TextView msgTv1 = (TextView) confirmDialog.findViewById(R.id.msgTv1);

        try {
            String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftOffered().getStartMin()));
            String endTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftOffered().getEndMin()));

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(shiftOffer.getShiftOffered().getShiftStartDate().toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            String finalDate = fmtOut.format(date);
            msgTV.setText(startTime + " - " + endTime + " " + shiftOffer.getShiftOffered().getRole() + " on " + getDayName(Integer.parseInt(shiftOffer.getShiftOffered().getDayOfTheWeek())) + ", " + finalDate + " at " + shiftOffer.getShiftOffered().getBusinessKey().getAddress());
            msgTv1.setText(Html.fromHtml("Once approved, this shift will be <b>added</b> to your schedule and your <b>" + getShiftremoveddaytime(shiftOffer) + "</b> shift will be removed."));
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

    private void doUpdateOpenShiftOffer(int requestCode, ShiftOffer shiftOffer, String action, boolean showProgress) {
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
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performPutRequest(requestCode, ServiceUrls.UPDATE_SWAP_SHIFT_OFFER_URL + shiftOffer.getOfferId() + "/" + action, new RequestParams(), prefsManager.get(Prefs_Keys.SESSION_ID), this);
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
            offerAppLL.setGravity(Gravity.LEFT);
            offerAppTv.setGravity(Gravity.LEFT);
            if (offer.getOfferStatus().equalsIgnoreCase("Accepted")) {
                offerAppTv.setText("OFFER APPROVED");
                offerAppTv.setGravity(Gravity.CENTER);
                offerAppLL.setGravity(Gravity.CENTER);
                offerAppTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.save_disable_checkmark, 0, 0, 0);
            } else if (offer.getOfferStatus().equalsIgnoreCase("Declined")) {
                offerAppTv.setText("OFFER DECLINED");
                offerAppTv.setGravity(Gravity.CENTER);
                offerAppLL.setGravity(Gravity.CENTER);
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
    public void onBackPressed() {
        Intent i = new Intent();
        if (isOfferUpdated) {
            setResult(RESULT_OK, i);
        }
        finish();
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

    private String getDayFullName(int position) {
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

    private String getShiftrequesteddaytime(ShiftOffer shiftOffer) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(shiftOffer.getShiftOffered().getShiftStartDate().toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            String finalDate = fmtOut.format(date);
            String finalmonthday = getDayFullName(Integer.parseInt(shiftOffer.getShiftOffered().getDayOfTheWeek())) + ", " + finalDate;
            String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftOffered().getStartMin()));
            String finaldatewithtime = finalmonthday + " " + startTime;

            return finaldatewithtime;

        } catch (Exception e) {
            return "";
        }
    }

    private String getShiftremoveddaytime(ShiftOffer shiftOffer) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(shiftOffer.getShiftRequested().getShiftStartDate().toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            String finalDate = fmtOut.format(date);
            String finalmonthday = getDayFullName(Integer.parseInt(shiftOffer.getShiftRequested().getDayOfTheWeek())) + ", " + finalDate;
            String startTime = LegionUtils.convertMinsToTimeinHrs(Long.valueOf(shiftOffer.getShiftRequested().getStartMin()));
            String finaldatewithtime = finalmonthday + " " + startTime;

            return finaldatewithtime;

        } catch (Exception e) {
            return "";
        }
    }

    private void setImageOrText(ShiftOfferedRequested shiftOfferedRequested, TextView textviewholder, CircleImageView circularImageholder) {
        try {
            Worker worker = shiftOfferedRequested.getWorkerKey();
            if (worker != null) {
                String picUrl = worker.getPictureUrl();
                if (picUrl == null || picUrl.isEmpty() || (picUrl.equalsIgnoreCase("null")) || (picUrl.equalsIgnoreCase(""))) {
                    String firstName = worker.getFirstName().trim();
                    String lastName = worker.getLastName().trim();
                    String name = "";
                    if ((firstName != null) && (firstName.length() > 0)) {
                        name = firstName.substring(0, 1);
                    }
                    if ((lastName != null) && (lastName.length() > 0)) {
                        name = name + lastName.substring(0, 1);
                    }
                    circularImageholder.setVisibility(View.GONE);
                    textviewholder.setVisibility(View.VISIBLE);
                    textviewholder.setText(name.toUpperCase());
                } else {
                    textviewholder.setVisibility(View.GONE);
                    circularImageholder.setVisibility(View.VISIBLE);
                    Glide.with(SwapShiftOfferDetailsActivity.this).load(picUrl).into(circularImageholder);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResponseParsingStart(int parserId) {
    }

    @Override
    public void onResponseParsingComplete(int parserId, Object response) {
        if (parserId == ResponseParserConstants.PARSE_SHIFT_SWAP_OFFERS) {
            if (response instanceof Exception) {
                LegionUtils.showDialog(SwapShiftOfferDetailsActivity.this, "Oops, Something went wrong.\n\nPlease try again later.", true);
                ((Exception) response).printStackTrace();
            } else if (response instanceof String) {
                LegionUtils.showDialog(SwapShiftOfferDetailsActivity.this, response.toString(), true);
            } else if (response instanceof ArrayList<?>) {
                ArrayList<ShiftOffer> offersList = (ArrayList<ShiftOffer>) response;
                shiftOffer = offersList.get(0);
                if (shiftOffer.getOfferStatus().equalsIgnoreCase("Accepted")) {
                    showSwapShiftOfferAlert(this, "Swap Approved. Thank you, " + getNicknameifExist() + "!", "<b>" + getShiftrequesteddaytime(shiftOffer) + "</b>" + " shift has been <b>added</b> to your schedule,\n " + "<b>" + getShiftremoveddaytime(shiftOffer) + "</b>" + " shift has been <b>removed</b> from your schedule.", R.drawable.confirmation);
                } else if (shiftOffer.getOfferStatus().equalsIgnoreCase("Claimed")) {
                    showSwapShiftOfferAlert(this, "Good Job, " + getNicknameifExist() + "!", "Your request has been received and sent to the store lead for approval.", R.drawable.confirmation);
                } else if (shiftOffer.getOfferStatus().equalsIgnoreCase("Declined")) {
                    showSwapShiftOfferAlert(this, "We're sorry.", "You're no longer eligible to take this shift, as it will trigger Over Time.", R.drawable.error_transient);
                    /* if (shiftOffer.getReason() != null && shiftOffer.getReason().contains("Overtime")) {
                                showSwapShiftOfferAlert(this, "We're sorry.", "You're no longer eligible to take this shift, as it will trigger Over Time.", R.drawable.error_transient, false);
                            } else if (shiftOffer.getReason() != null && !shiftOffer.getReason().equalsIgnoreCase("null")) {
                                showSwapShiftOfferAlert(this, "Oops, looks like your claim was declined.", shiftOffer.getReason(), R.drawable.error_transient, false);
                            }else{
                                Log.v("declined reason", "Unhandled declined reason");
                            }*/
                } else if (shiftOffer.getOfferStatus().equalsIgnoreCase("Expired") || shiftOffer.getOfferStatus().equalsIgnoreCase("Withdrawn")) {
                    showSwapShiftOfferAlert(this, "", "Sorry, this offer is no longer available!<br/><br/>Please check out other available offers.", R.drawable.error_transient);
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

