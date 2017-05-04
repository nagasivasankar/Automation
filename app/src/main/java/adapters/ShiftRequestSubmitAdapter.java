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
import java.util.List;

import co.legion.client.R;
import de.hdodenhof.circleimageview.CircleImageView;
import helpers.Legion_PrefsManager;
import models.ScheduleWorkerShift;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 12/1/2016.
 */
public class ShiftRequestSubmitAdapter extends BaseAdapter implements Legion_Constants {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final ArrayList<ScheduleWorkerShift> selectedWorkerShiftsList;
    private final ScheduleWorkerShift workerShiftsList;
    private final Legion_PrefsManager legionPreferences;
    private final boolean checkCurrentWeekOrNot;
    TextView mCountTv;
    ArrayList<Integer> removedPositionsList;

    public ShiftRequestSubmitAdapter(Activity activity, ScheduleWorkerShift workerShiftsList, ArrayList<ScheduleWorkerShift> selectedWorkerShiftsList,TextView countTv) {
        this.activity = activity;
        this.inflater = activity.getLayoutInflater();
        this.workerShiftsList = workerShiftsList;
        this.selectedWorkerShiftsList = selectedWorkerShiftsList;
        legionPreferences = new Legion_PrefsManager(activity);
        removedPositionsList = new ArrayList<>();
        mCountTv = countTv;
        checkCurrentWeekOrNot = LegionUtils.getDiffernceBnTwoDates(legionPreferences.get(Prefs_Keys.START_DATE).replace("T", " ").replace("Z", " "));
    }

    @Override
    public int getCount() {
        return selectedWorkerShiftsList.size();
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
            convertView = inflater.inflate(R.layout.list_item_submit_swap, null);
            viewHolder = new ViewHolder();
            viewHolder.dayViewLL1 = (LinearLayout) convertView.findViewById(R.id.subDayLayout1);
            viewHolder.dayViewLL = (LinearLayout) convertView.findViewById(R.id.subDayLayout);
            viewHolder.dayNameTV = (TextView) convertView.findViewById(R.id.dayNameTV);
            viewHolder.roleTV = (TextView) convertView.findViewById(R.id.roleTv);
            viewHolder.addressTV = (TextView) convertView.findViewById(R.id.addressTV);
            viewHolder.statTimeTV = (TextView) convertView.findViewById(R.id.statTimeTV);
            viewHolder.endTimeTV = (TextView) convertView.findViewById(R.id.endTimeTV);
            viewHolder.durationTV = (TextView) convertView.findViewById(R.id.durationTV);
            viewHolder.statusImageView = (ImageView) convertView.findViewById(R.id.statusImageView);
            viewHolder.shiftersIv = (CircleImageView) convertView.findViewById(R.id.shiftersIv);
            viewHolder.shiftersTv2 = (TextView) convertView.findViewById(R.id.shiftersTv2);
            viewHolder.lineView = (View) convertView.findViewById(R.id.lineView);
            viewHolder.hrsTV = (TextView) convertView.findViewById(R.id.hrsTV);
            viewHolder.dayNameTV.setTextSize(legionPreferences.getInt(Prefs_Keys.SIZE));
            viewHolder.shiftersNameTv = (TextView) convertView.findViewById(R.id.nameTv1);
            viewHolder.shiftersIv2 = (CircleImageView) convertView.findViewById(R.id.shiftersIv2);
            viewHolder.dayNameTV1 = (TextView) convertView.findViewById(R.id.dayNameTV1);
            viewHolder.roleTV1 = (TextView) convertView.findViewById(R.id.roleTv1);
            viewHolder.addressTV1 = (TextView) convertView.findViewById(R.id.addressTV1);
            viewHolder.statTimeTV1 = (TextView) convertView.findViewById(R.id.statTimeTV1);
            viewHolder.endTimeTV1 = (TextView) convertView.findViewById(R.id.endTimeTV1);
            viewHolder.durationTV1 = (TextView) convertView.findViewById(R.id.durationTV1);
            viewHolder.statusImageView1 = (ImageView) convertView.findViewById(R.id.statusImageView1);
            viewHolder.shiftersIv1 = (CircleImageView) convertView.findViewById(R.id.shiftersIv1);
            viewHolder.lineView1 = (View) convertView.findViewById(R.id.lineView1);
            viewHolder.hrsTV1 = (TextView) convertView.findViewById(R.id.hrsTV1);
            viewHolder.dayNameTV1.setTextSize(legionPreferences.getInt(Prefs_Keys.SIZE));
            viewHolder.shiftersIv1 = (CircleImageView) convertView.findViewById(R.id.shiftersIv1);
            viewHolder.shiftersTv = (TextView) convertView.findViewById(R.id.shiftersTv);
            viewHolder.closeShiftIv = (ImageView) convertView.findViewById(R.id.closeShiftIv);
            viewHolder.dayNameTV1.setMinWidth((legionPreferences.getInt(Prefs_Keys.MIN_WIDTH)-20));
            viewHolder.dayNameTV1.setTextSize(legionPreferences.getInt(Prefs_Keys.SIZE));
            viewHolder.dayNameTV.setMinWidth((legionPreferences.getInt(Prefs_Keys.MIN_WIDTH)-20));
            viewHolder.dayNameTV.setTextSize(legionPreferences.getInt(Prefs_Keys.SIZE));
            convertView.setTag(viewHolder);

            LegionUtils.doApplyFont(activity.getAssets(), (LinearLayout) convertView.findViewById(R.id.itemParentLayout));
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (selectedWorkerShiftsList.size() > 0) {
            // another user details
            final ScheduleWorkerShift selectedScheduleList = selectedWorkerShiftsList.get(position);
            final ScheduleWorkerShift scheduleDetails = workerShiftsList;
            if (selectedScheduleList != null) {
                viewHolder.statTimeTV.setText(LegionUtils.getTimeFromDate(selectedScheduleList.getShiftStartDate()));
                viewHolder.endTimeTV.setText(LegionUtils.getTimeFromDate(selectedScheduleList.getShiftEndDate()));
                viewHolder.roleTV.setText(selectedScheduleList.getRole());
                viewHolder.shiftersNameTv.setText("for "+selectedScheduleList.getWorkerKey().getFirstName() + "'s");
                String picUrl1 = selectedScheduleList.getWorkerKey().getPictureUrl();
                if(picUrl1.equalsIgnoreCase("") || picUrl1.equals("null") || picUrl1==null){
                    String firstName = selectedScheduleList.getWorkerKey().getFirstName();
                    String lastName =  selectedScheduleList.getWorkerKey().getLastName();
                    String name = "";
                    if(firstName.length()>0){
                        name = firstName.substring(0,1);
                    }
                    if(lastName.length()>0){
                        name = name+lastName.substring(0,1);
                    }
                    viewHolder.shiftersTv2.setText(name.toUpperCase());
                    viewHolder.shiftersTv2.setVisibility(View.VISIBLE);
                    viewHolder.shiftersIv2.setVisibility(View.GONE);
                    viewHolder.shiftersTv2.setBackgroundResource(R.drawable.bg_lite_white_circle_shifts);
                }else {
                    viewHolder.shiftersTv2.setVisibility(View.GONE);
                    viewHolder.shiftersIv2.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(selectedScheduleList.getWorkerKey().getPictureUrl()).into(viewHolder.shiftersIv2);
                }

                String availabilityMatchOrNot = selectedScheduleList.getAvailability();
                String regularMins = LegionUtils.convertMinsToHrsReg(selectedScheduleList.getRegularMinutes());
                viewHolder.durationTV.setText(regularMins);
                viewHolder.addressTV.setText(LegionUtils.getUpdatedAddress(selectedScheduleList.getBusinessKey().getAddress()));
                if (availabilityMatchOrNot != null) {
                    if (availabilityMatchOrNot.equalsIgnoreCase("Yes")) {
                        viewHolder.addressTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_match, 0);//green
                    } else if (availabilityMatchOrNot.equalsIgnoreCase("No")) {
                        viewHolder.addressTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_mismatch, 0); // red down
                    } else {
                        viewHolder.addressTV.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_unknown, 0);// grey up
                    }
                    viewHolder.addressTV.setCompoundDrawablePadding(10);
                }
                String label;
                if ((regularMins.contains("1."))) {
                    label = "Hr";
                } else {
                    label = "Hrs";
                }
                viewHolder.hrsTV.setText(label);
                int startHour = LegionUtils.convertFromMinutesToHoursInt(selectedScheduleList.getStartMin());
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
                try {
                    String finalDate = LegionUtils.getDatefromServerDate(selectedScheduleList.getShiftStartDate());
                    if(checkCurrentWeekOrNot){
                        if(LegionUtils.getCurrentDate().equals(finalDate)){
                            viewHolder.dayNameTV.setTextColor(ActivityCompat.getColor(activity, R.color.white));
                            viewHolder.dayViewLL.setBackgroundResource(R.drawable.bg_blue);
                        }else{
                            viewHolder.dayNameTV.setTextColor(ActivityCompat.getColor(activity, R.color.light_black));
                            viewHolder.dayViewLL.setBackgroundResource(0);
                        }
                    }else{
                        viewHolder.dayNameTV.setTextColor(ActivityCompat.getColor(activity, R.color.light_black));
                        viewHolder.dayNameTV.setBackgroundResource(0);
                    }

                    viewHolder.dayNameTV.setText(Html.fromHtml(LegionUtils.getUpdatedString(LegionUtils.getDayName(Integer.parseInt(selectedScheduleList.getDayOfTheWeek())),finalDate )));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // current User Details
                viewHolder.statTimeTV1.setText(LegionUtils.getTimeFromDate(scheduleDetails.getShiftStartDate()));
                viewHolder.endTimeTV1.setText(LegionUtils.getTimeFromDate(scheduleDetails.getShiftEndDate()));
                viewHolder.roleTV1.setText(scheduleDetails.getRole());
                String picUrl = scheduleDetails.getWorkerKey().getPictureUrl();
                if(picUrl.equalsIgnoreCase("") || picUrl.equals("null") || picUrl==null){
                    String firstName = scheduleDetails.getWorkerKey().getFirstName();
                    String lastName =  scheduleDetails.getWorkerKey().getLastName();
                    String name = "";
                    if(firstName.length()>0){
                        name = firstName.substring(0,1);
                    }
                    if(lastName.length()>0){
                        name = name+lastName.substring(0,1);
                    }
                    viewHolder.shiftersTv.setText(name.toUpperCase());
                    viewHolder.shiftersTv.setVisibility(View.VISIBLE);
                    viewHolder.shiftersIv1.setVisibility(View.GONE);
                    viewHolder.shiftersTv.setBackgroundResource(R.drawable.bg_lite_white_circle_shifts);
                }else {
                    viewHolder.shiftersTv.setVisibility(View.GONE);
                    viewHolder.shiftersIv1.setVisibility(View.VISIBLE);
                    Glide.with(activity).load(scheduleDetails.getWorkerKey().getPictureUrl()).into(viewHolder.shiftersIv1);
                }


                String availabilityMatchOrNot1 = scheduleDetails.getAvailability();
                String regularMins1 = LegionUtils.convertMinsToHrsReg(scheduleDetails.getRegularMinutes());
                viewHolder.durationTV1.setText(regularMins1);
                viewHolder.addressTV1.setText(LegionUtils.getUpdatedAddress(scheduleDetails.getBusinessKey().getAddress()));
                if (availabilityMatchOrNot1 != null) {
                    if (availabilityMatchOrNot1.equalsIgnoreCase("Yes")) {
                        viewHolder.addressTV1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_match, 0);//green
                    } else if (availabilityMatchOrNot1.equalsIgnoreCase("No")) {
                        viewHolder.addressTV1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_mismatch, 0); // red down
                    } else {
                        viewHolder.addressTV1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.availability_unknown, 0);// grey up
                    }
                    viewHolder.addressTV1.setCompoundDrawablePadding(10);
                }
                String label1;
                if ((regularMins1.contains("1."))) {
                    label1 = "Hr";
                } else {
                    label1 = "Hrs";
                }
                viewHolder.hrsTV1.setText(label1);
                int startHour1 = LegionUtils.convertFromMinutesToHoursInt(scheduleDetails.getStartMin());
                int periodImage1 = 0;
                String periodColor1;
                if (startHour != 0) {
                    if (startHour1 >= 5 && startHour1 < 9) {
                        periodImage1 = R.drawable.ic_sun_early_morning; //5Am9Am
                        periodColor1 = "#fab817";
                    } else if (startHour1 >= 9 && startHour1 < 13) { //9Am to 1pm
                        periodImage1 = R.drawable.ic_sun_morning;
                        periodColor1 = "#ffa000";
                    } else if (startHour1 >= 13 && startHour1 < 17) { //1pm to 5pm
                        periodImage1 = R.drawable.ic_sun_noon;
                        periodColor1 = "#f57c00";
                    } else if (startHour1 >= 17 && startHour1 < 21) {  //5pm to 9pm
                        periodImage1 = R.drawable.ic_sun_evening;
                        periodColor1 = "#e75c15";
                    } else {
                        periodImage1 = R.drawable.ic_moon; //9Pm12Am
                        periodColor1 = "#9a1010";
                    }
                    viewHolder.lineView1.setBackgroundColor(Color.parseColor(periodColor1));
                    viewHolder.statusImageView1.setImageResource(periodImage1);
                    viewHolder.roleTV1.setTextColor(Color.parseColor(periodColor1));
                }
                try {
                    String finalDate = LegionUtils.getDatefromServerDate(scheduleDetails.getShiftStartDate());
                    if(checkCurrentWeekOrNot){
                        if(LegionUtils.getCurrentDate().equals(finalDate)){
                            viewHolder.dayNameTV1.setTextColor(ActivityCompat.getColor(activity, R.color.white));
                            viewHolder.dayViewLL1.setBackgroundResource(R.drawable.bg_blue);
                        }else{
                            viewHolder.dayNameTV1.setTextColor(ActivityCompat.getColor(activity, R.color.light_black));
                            viewHolder.dayViewLL1.setBackgroundResource(R.drawable.bg_white_corner);
                        }
                    }else{
                        viewHolder.dayNameTV1.setTextColor(ActivityCompat.getColor(activity, R.color.light_black));
                        viewHolder.dayViewLL1.setBackgroundResource(R.drawable.bg_white_corner);
                    }

                    viewHolder.dayNameTV1.setText(Html.fromHtml(LegionUtils.getUpdatedString(LegionUtils.getDayName(Integer.parseInt(scheduleDetails.getDayOfTheWeek())),finalDate)));


                } catch (Exception e) {
                    e.printStackTrace();
                }

                viewHolder.closeShiftIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removedPositionsList.add(selectedScheduleList.getPosition());
                        selectedWorkerShiftsList.remove(position);
                        notifyDataSetChanged();
                        mCountTv.setText(""+(Integer.parseInt(mCountTv.getText().toString())-1));
                    }
                });
            }
        }

        return convertView;
    }

    public List<Integer> getRemovedShitsList() {
        return removedPositionsList;
    }
    public List<ScheduleWorkerShift> getShitsList() {
        return selectedWorkerShiftsList;
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
        public TextView shiftersNameTv;
        CircleImageView shiftersIv2;

        //current user
        public ImageView statusImageView1;
        public TextView dayNameTV1;
        public TextView durationTV1;
        public TextView addressTV1;
        public TextView roleTV1;
        public TextView statTimeTV1;
        public TextView endTimeTV1;
        public TextView hrsTV1;
        public View lineView1;
        CircleImageView shiftersIv1;
        public ImageView closeShiftIv;
        public LinearLayout dayViewLL1;
        public LinearLayout dayViewLL;
        public TextView shiftersTv;
        public TextView shiftersTv2;
    }
}
