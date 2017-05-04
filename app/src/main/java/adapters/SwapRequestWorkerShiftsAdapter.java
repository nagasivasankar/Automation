package adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import co.legion.client.R;
import de.hdodenhof.circleimageview.CircleImageView;
import helpers.Legion_PrefsManager;
import models.ScheduleWorkerShift;
import models.WorkerKey;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 12/1/2016.
 */
public class SwapRequestWorkerShiftsAdapter extends BaseAdapter implements Legion_Constants {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final ArrayList<ScheduleWorkerShift> workerShiftsList;
    private final Legion_PrefsManager legionPreferences;
    private final boolean checkCurrentWeekOrNot;
    private int selctedCount;
    private TextView mCountTv;
    private TextView mbtNextTv;

    public SwapRequestWorkerShiftsAdapter(Activity activity, ArrayList<ScheduleWorkerShift> workerShiftsList, TextView countTv,TextView btNextTv) {
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.workerShiftsList = workerShiftsList;
        legionPreferences = new Legion_PrefsManager(activity);
        mCountTv =countTv;
        mbtNextTv = btNextTv;
        checkCurrentWeekOrNot = LegionUtils.getDiffernceBnTwoDates(legionPreferences.get(Prefs_Keys.START_DATE).replace("T", " ").replace("Z", " "));
    }

    @Override
    public int getCount() {
        return workerShiftsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = inflater.inflate(R.layout.list_item_request_sd_swift, null);
            viewHolder = new ViewHolder();
            viewHolder.shiftersIvAlphabets = (TextView) convertView.findViewById(R.id.shiftersIvAlphabets);
            viewHolder.dayNameTV = (TextView) convertView.findViewById(R.id.dayNameTV);
            viewHolder.dateTv = (TextView) convertView.findViewById(R.id.dateTv);
            viewHolder.subDayLayout = (LinearLayout) convertView.findViewById(R.id.subDayLayout);
            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.nameTV);
            viewHolder.roleTV = (TextView) convertView.findViewById(R.id.roleTV);
            viewHolder.addressTV = (TextView) convertView.findViewById(R.id.addressTV);
            viewHolder.statTimeTV = (TextView) convertView.findViewById(R.id.statTimeTV);
            viewHolder.statusAvlbtyIv = (ImageView) convertView.findViewById(R.id.statusAvlbtyIv);
            viewHolder.endTimeTV = (TextView) convertView.findViewById(R.id.endTimeTV);
            viewHolder.durationTV = (TextView) convertView.findViewById(R.id.durationTV);
            viewHolder.statusImageView = (ImageView) convertView.findViewById(R.id.statusImageView);
            viewHolder.shiftersIv = (CircleImageView) convertView.findViewById(R.id.shiftersIv);
            viewHolder.lineView = (View) convertView.findViewById(R.id.lineView);
            viewHolder.hrsTV = (TextView) convertView.findViewById(R.id.hrsTV);
            viewHolder.dayNameTV.setMinWidth(legionPreferences.getInt(Prefs_Keys.MIN_WIDTH));
            viewHolder.dayNameTV.setTextSize(legionPreferences.getInt(Prefs_Keys.SIZE));
            viewHolder.shiftersNameTv = (TextView) convertView.findViewById(R.id.shiftersNameTv);
            viewHolder.clockInOrOutLL = (LinearLayout) convertView.findViewById(R.id.clockInOrOutLL);
            viewHolder.clockInTv = (TextView) convertView.findViewById(R.id.clockInTv);
            viewHolder.clockOutTv = (TextView) convertView.findViewById(R.id.clockOutTv);
            viewHolder.totalHrsTv = (TextView) convertView.findViewById(R.id.totalHrsTv);
            viewHolder.totalMinsTv = (TextView) convertView.findViewById(R.id.totalMinsTv);
            viewHolder.selectedIv = (ImageView) convertView.findViewById(R.id.selectedIv);
            viewHolder.dayViewLL = (LinearLayout) convertView.findViewById(R.id.dayViewLL);
            viewHolder.firstItemGap = (View) convertView.findViewById(R.id.firstItemGap);
            viewHolder.topSelctorLL = (LinearLayout) convertView.findViewById(R.id.topSelctorLL);
            convertView.setTag(viewHolder);
            LegionUtils.doApplyFont(activity.getAssets(), (LinearLayout) convertView.findViewById(R.id.itemParentLayout));
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (workerShiftsList.size() > 0) {

            final ScheduleWorkerShift scheduLedDetails = workerShiftsList.get(position);
            if(position == 0){
                viewHolder.firstItemGap.setVisibility(View.VISIBLE);
            }else{
                viewHolder.firstItemGap.setVisibility(View.GONE);
            }
            if (scheduLedDetails != null) {
                viewHolder.statTimeTV.setText(LegionUtils.getTimeFromDate(scheduLedDetails.getShiftStartDate()));
                viewHolder.endTimeTV.setText(LegionUtils.getTimeFromDate(scheduLedDetails.getShiftEndDate()));
                viewHolder.roleTV.setText(scheduLedDetails.getRole());
                viewHolder.shiftersNameTv.setText(scheduLedDetails.getWorkerKey().getFirstName() + "'s shift");
                viewHolder.nameTV.setText(scheduLedDetails.getBusinessKey().getName());
                WorkerKey workerKey =scheduLedDetails.getWorkerKey();
                if(workerKey.getPictureUrl().equalsIgnoreCase("") || workerKey.getPictureUrl().equals("null") || workerKey.getPictureUrl()==null){
                    String firstName = workerKey.getFirstName().trim();
                    String lastName = workerKey.getLastName().trim();
                    String name = "";
                    if(firstName.length()>0){
                        name = firstName.substring(0,1);
                    }
                    if(lastName.length()>0){
                        name = name+lastName.substring(0,1);
                    }
                    viewHolder.shiftersIvAlphabets.setText(name.toUpperCase());
                    viewHolder.shiftersIvAlphabets.setVisibility(View.VISIBLE);
                    viewHolder.shiftersIv.setVisibility(View.GONE);
                    viewHolder.shiftersIvAlphabets.setBackgroundResource(R.drawable.bg_lite_white_circle_shifts);
                }else {
                    viewHolder.shiftersIvAlphabets.setVisibility(View.GONE);
                    viewHolder.shiftersIv.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(scheduLedDetails.getWorkerKey().getPictureUrl()).into(viewHolder.shiftersIv);
                }
                String availabilityMatchOrNot = scheduLedDetails.getAvailability();
                String regularMins = LegionUtils.convertMinsToHrsReg(scheduLedDetails.getRegularMinutes());
                viewHolder.durationTV.setText(regularMins);
                viewHolder.addressTV.setText(LegionUtils.getUpdatedAddress(scheduLedDetails.getBusinessKey().getAddress()));
                if (availabilityMatchOrNot != null) {
                    if (availabilityMatchOrNot.equalsIgnoreCase("Yes")) {
                        viewHolder.statusAvlbtyIv.setImageResource(R.drawable.availability_match);
                    } else if (availabilityMatchOrNot.equalsIgnoreCase("No")) {
                        viewHolder.statusAvlbtyIv.setImageResource(R.drawable.availability_mismatch);
                    } else {
                        viewHolder.statusAvlbtyIv.setImageResource(R.drawable.availability_unknown);
                    }
                }
                String label;
                if ((regularMins.contains("1."))) {
                    label = "Hr";
                } else {
                    label = "Hrs";
                }
                viewHolder.hrsTV.setText(label);
                int startHour = LegionUtils.convertFromMinutesToHoursInt(scheduLedDetails.getStartMin());
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
                    viewHolder.lineView.setBackgroundColor(Color.parseColor(periodColor));
                    viewHolder.statusImageView.setImageResource(periodImage);
                    viewHolder.roleTV.setTextColor(Color.parseColor(periodColor));
                }
                if (scheduLedDetails.isSelectedOrNot()) {
                    viewHolder.selectedIv.setImageResource(R.drawable.ic_save_check_mark);
                    viewHolder.selectedIv.setBackgroundResource(R.drawable.bg_blue_circle_shifts);
                } else {
                    viewHolder.selectedIv.setImageResource(0);
                    viewHolder.selectedIv.setBackgroundResource(R.drawable.bg_white_circle_shifts);
                }
                viewHolder.topSelctorLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScheduleWorkerShift list = workerShiftsList.get(position);
                        if (scheduLedDetails.isSelectedOrNot()) {
                            selctedCount = Integer.valueOf(mCountTv.getText().toString());
                            viewHolder.selectedIv.setImageResource(0);
                            viewHolder.selectedIv.setBackgroundResource(R.drawable.bg_white_circle_shifts);
                            list.setSelectedOrNot(false);
                            selctedCount = selctedCount-1;
                            mCountTv.setText(""+selctedCount);
                            nextButtonColorfn(selctedCount,mbtNextTv);
                        } else {
                            selctedCount = Integer.valueOf(mCountTv.getText().toString());
                            viewHolder.selectedIv.setImageResource(R.drawable.ic_save_check_mark);
                            viewHolder.selectedIv.setBackgroundResource(R.drawable.bg_blue_circle_shifts);
                            list.setSelectedOrNot(true);
                            selctedCount = selctedCount+1;
                            mCountTv.setText(""+selctedCount);
                            nextButtonColorfn(selctedCount,mbtNextTv);
                        }
                        notifyDataSetChanged();

                    }
                });

                viewHolder.clockInOrOutLL.setMinimumHeight(60);
                if (((scheduLedDetails.getClockInTime().length() > 0) && !scheduLedDetails.getClockInTime().equalsIgnoreCase("0")) && ((scheduLedDetails.getClockOutTime().length() > 0) && !scheduLedDetails.getClockInTime().equalsIgnoreCase("0"))) {
                    viewHolder.clockInTv.setText(LegionUtils.convertFromMillsToTime(Long.parseLong(scheduLedDetails.getClockInTime())));
                    viewHolder.clockOutTv.setText(LegionUtils.convertFromMillsToTime(Long.valueOf(scheduLedDetails.getClockOutTime())));
                    ArrayList timeAndMins = LegionUtils.getDifferenceBnTwoMillis(Long.parseLong(scheduLedDetails.getClockInTime()), Long.parseLong(scheduLedDetails.getClockOutTime()));
                    if (timeAndMins != null && (timeAndMins.size() == 2)) {
                        viewHolder.totalHrsTv.setText(timeAndMins.get(0).toString());
                        viewHolder.totalMinsTv.setText(timeAndMins.get(1).toString());
                    }
                    viewHolder.clockInOrOutLL.setVisibility(View.VISIBLE);
                    viewHolder.clockInOrOutLL.setBackgroundResource(R.drawable.bg_clock_right);
                } else {
                    viewHolder.clockInOrOutLL.setVisibility(View.GONE);
                }
                try {
                    String finalDate = LegionUtils.getDatefromServerDate(scheduLedDetails.getShiftStartDate());
                    if(checkCurrentWeekOrNot) {
                        if (LegionUtils.getCurrentDate().equals(finalDate)) {
                            viewHolder.dayNameTV.setTextColor(ActivityCompat.getColor(activity, R.color.white));
                            viewHolder.subDayLayout.setBackgroundResource(R.drawable.bg_blue_bottom_one_corner);
                        } else {
                            viewHolder.dayNameTV.setTextColor(ActivityCompat.getColor(activity, R.color.light_black));
                            viewHolder.subDayLayout.setBackgroundResource(0);
                        }
                    }else {
                        viewHolder.dayNameTV.setTextColor(ActivityCompat.getColor(activity, R.color.light_black));
                    }
                  //  Log.d("image", "" + scheduLedDetails.getDayOfTheWeek());
                    viewHolder.dayNameTV.setText(Html.fromHtml(LegionUtils.getUpdatedString(LegionUtils.getDayName(Integer.parseInt(scheduLedDetails.getDayOfTheWeek())),finalDate)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return convertView;
    }


    private static class ViewHolder {
        public ImageView statusImageView;
        public TextView dayNameTV;
        public TextView durationTV;
        public TextView dateTv;
        public TextView nameTV;
        public TextView addressTV;
        public TextView roleTV;
        public TextView statTimeTV;
        public TextView endTimeTV;
        public TextView hrsTV;
        public View lineView;
        public CircleImageView shiftersIv;
        public LinearLayout clockInOrOutLL;
        public TextView clockInTv;
        public TextView clockOutTv;
        public TextView totalHrsTv;
        public TextView totalMinsTv;
        public TextView shiftersNameTv;
        public ImageView selectedIv;
        public LinearLayout subDayLayout;
        public LinearLayout dayViewLL;
        public View firstItemGap;
        public LinearLayout topSelctorLL;
        public TextView shiftersIvAlphabets;
        public ImageView statusAvlbtyIv;
    }

    private void nextButtonColorfn(int count,TextView btNextTv){

        if (count>0){
            btNextTv.setTextColor(activity.getResources().getColor(R.color.white));
        }
        else {
            btNextTv.setTextColor(activity.getResources().getColor(R.color.gray_text_color));
        }
    }
}
