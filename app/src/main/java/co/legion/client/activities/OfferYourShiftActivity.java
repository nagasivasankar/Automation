package co.legion.client.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import base.Legion_BaseActivity;
import co.legion.client.BuildConfig;
import co.legion.client.R;
import helpers.Legion_PrefsManager;
import models.ScheduleWorkerShift;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 04-Jan-17.
 */
public class OfferYourShiftActivity extends Legion_BaseActivity implements View.OnClickListener, Legion_Constants {
    int minWidth = 0;
    private Legion_PrefsManager legionPreferences;
    private float size;
    private ScheduleWorkerShift scheduleDetails;
    private boolean checkCurrentWeekOrNot;
    private LinearLayout dayViewLL;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = OfferYourShiftActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_your_swift);
        legionPreferences = new Legion_PrefsManager(this);
        minWidth = legionPreferences.getInt(Prefs_Keys.MIN_WIDTH);
        size = legionPreferences.getInt(Prefs_Keys.SIZE);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        tvToolbarTile.setText("Offer your Shift");
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setImageResource(R.drawable.dismiss);
        backImage.setOnClickListener(this);
        TextView nameTV = (TextView) findViewById(R.id.nameTV);
        TextView roleTV = (TextView) findViewById(R.id.roleTV);
        TextView addressTV = (TextView) findViewById(R.id.addressTV);
        TextView statTimeTV = (TextView) findViewById(R.id.statTimeTV);
        View lineView = (View) findViewById(R.id.lineView);
        dayViewLL = (LinearLayout) findViewById(R.id.dayViewLL);
        final TextView endTimeTV = (TextView) findViewById(R.id.endTimeTV);
        TextView durationTV = (TextView) findViewById(R.id.durationTV);
        ImageView statusImageView = (ImageView) findViewById(R.id.statusImageView);
        ImageView statusAvlbtyIv = (ImageView) findViewById(R.id.statusAvlbtyIv);
        TextView dayOfTheWeekTv = (TextView) findViewById(R.id.dayOfTheWeekTv);
        TextView dayNameTVText = (TextView) findViewById(R.id.dayNameTV);
        LinearLayout swapLL = (LinearLayout) findViewById(R.id.swapLL);
        swapLL.setOnClickListener(this);
        LinearLayout dropLL = (LinearLayout) findViewById(R.id.dropLL);
        dropLL.setOnClickListener(this);
        try {
            scheduleDetails = (ScheduleWorkerShift) getIntent().getExtras().get(Extras_Keys.WORKSHIFTS_LIST);
            TextView hrsTV = (TextView) findViewById(R.id.hrsTV);
            nameTV.setText(scheduleDetails.getBusinessKey().getName());
            statTimeTV.setText(LegionUtils.convertMinsToTimeinHrs(Long.valueOf(scheduleDetails.getStartMin())));
            dayOfTheWeekTv.setText("");
            endTimeTV.setText(LegionUtils.convertMinsToTimeinHrs(Long.valueOf(scheduleDetails.getEndMin())));
            String regularMins = LegionUtils.convertMinsToHrsReg(scheduleDetails.getRegularMinutes());
            durationTV.setText(regularMins);
            LinearLayout clockInOrOutLL = (LinearLayout) findViewById(R.id.clockInOrOutLL);
            TextView clockInTv = (TextView) findViewById(R.id.clockInTv);
            TextView clockOutTv = (TextView) findViewById(R.id.clockOutTv);
            TextView totalHrsTv = (TextView) findViewById(R.id.totalHrsTv);
            TextView totalMinsTv = (TextView) findViewById(R.id.totalMinsTv);
            String label;
            if ((regularMins.contains("1."))) {
                label = "Hr";
            } else {
                label = "Hrs";
            }
            hrsTV.setText(label);
            roleTV.setText(scheduleDetails.getRole());
            addressTV.setText(scheduleDetails.getBusinessKey().getAddress());
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

            int startHour = LegionUtils.convertFromMinutesToHoursInt(scheduleDetails.getStartMin());
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
                statusImageView.setImageResource(periodImage);
                roleTV.setTextColor(Color.parseColor(periodColor));
            }

            clockInOrOutLL.setMinimumHeight(60);
            if (((scheduleDetails.getClockInTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0")) && ((scheduleDetails.getClockOutTime().length() > 0) && !scheduleDetails.getClockInTime().equalsIgnoreCase("0"))) {
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
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(scheduleDetails.getShiftStartDate().toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("d");
            String finalDate = fmtOut.format(date);
            dayNameTVText.setTextSize(size);
            checkCurrentWeekOrNot = LegionUtils.getDiffernceBnTwoDates(legionPreferences.get(Prefs_Keys.START_DATE).replace("T", " ").replace("Z", " "));
            dayNameTVText.setTextColor(ActivityCompat.getColor(this, R.color.light_black));
            if (checkCurrentWeekOrNot) {
                if (LegionUtils.getCurrentDate().equals(finalDate)) {
                    dayNameTVText.setTextColor(ActivityCompat.getColor(this, R.color.white));
                    dayViewLL.setBackgroundResource(R.drawable.bg_blue);
                }
            }

            dayNameTVText.setText(Html.fromHtml(LegionUtils.getUpdatedString(LegionUtils.getDayName(Integer.parseInt(scheduleDetails.getDayOfTheWeek())), finalDate)));
            dayNameTVText.setMinWidth(minWidth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.toolbarBack) {
            finish();
        } else if (id == R.id.swapLL) {
            Intent swapReqIntent = new Intent(this, SwapRequestListActivity.class);
            swapReqIntent.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduleDetails);
            startActivity(swapReqIntent);
        } else if (id == R.id.dropLL) {
            Intent swapReqIntent = new Intent(this, ShiftDropRequestSubmitActivity.class);
            swapReqIntent.putExtra(Extras_Keys.WORKSHIFTS_LIST, scheduleDetails);
            startActivity(swapReqIntent);
        }
    }


}
