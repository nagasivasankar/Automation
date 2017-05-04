package adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.legion.client.R;
import models.Schedule;
import tabs.ScheduleTabFragment;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/29/2016.
 */
public class SchedulesAdapter extends BaseAdapter implements Legion_Constants {

    private final ArrayList<Schedule> schedulesList;
    private final LayoutInflater inflater;
    private final Activity activity;

    public SchedulesAdapter(ScheduleTabFragment scheduleTabFragment, ArrayList<Schedule> schedulesList) {
        this.schedulesList = schedulesList;
        this.activity = scheduleTabFragment.getActivity();
        this.inflater = scheduleTabFragment.getActivity().getLayoutInflater();
    }

    @Override
    public int getCount() {
        return schedulesList.size();
    }

    @Override
    public Object getItem(int position) {
        return schedulesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = inflater.inflate(R.layout.row_schedules_overview, null);
            viewHolder = new ViewHolder();

            viewHolder.star1 = (ImageView) convertView.findViewById(R.id.star1);
            viewHolder.star2 = (ImageView) convertView.findViewById(R.id.star2);
            viewHolder.star3 = (ImageView) convertView.findViewById(R.id.star3);
            viewHolder.star4 = (ImageView) convertView.findViewById(R.id.star4);
            viewHolder.star5 = (ImageView) convertView.findViewById(R.id.star5);
            viewHolder.businessAddress = (TextView) convertView.findViewById(R.id.businessAddress);
            viewHolder.businessName = (TextView) convertView.findViewById(R.id.businessName);
            viewHolder.weekDateTV = (TextView) convertView.findViewById(R.id.weekDateTV);
            viewHolder.scheduleWeekTv = (TextView) convertView.findViewById(R.id.scheduleWeekTv);
            viewHolder.newTV = (TextView) convertView.findViewById(R.id.newTV);

            LegionUtils.doApplyFont(activity.getAssets(), (LinearLayout) convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Schedule schedule = schedulesList.get(position);

        viewHolder.weekDateTV.setText(LegionUtils.getWeekDate(schedule.getStartOfWeekDate().replace("T", " ").replace("Z", " ")).toUpperCase() + "\n-\n" + LegionUtils.getWeekDate(LegionUtils.getOneDayMinus(schedule.getEndOfWeekDate().replace("T", " ").replace("Z", " "))).toUpperCase());
        viewHolder.weekDateTV.setTextColor(Color.parseColor("#424242"));
        viewHolder.scheduleWeekTv.setTextColor(Color.parseColor("#888888"));
        viewHolder.scheduleWeekTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_schedule_past, 0, 0, 0);

        if (!schedule.getIsSeen() && !schedule.isCurrentWeek() && !schedule.isPastWeek()) {
            viewHolder.newTV.setVisibility(View.VISIBLE);
        } else {
            viewHolder.newTV.setVisibility(View.GONE);
        }

        if (schedule.isCurrentWeek()) {
            viewHolder.scheduleWeekTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_schedule_current, 0, 0, 0);
            viewHolder.scheduleWeekTv.setText("  THIS WEEK");
            viewHolder.scheduleWeekTv.setTextColor(Color.parseColor("#4C5EC4"));
            viewHolder.weekDateTV.setBackgroundResource(R.drawable.bg_blue);
            viewHolder.weekDateTV.setTextColor(Color.WHITE);
        } else if (schedule.isPastWeek()) {
            viewHolder.weekDateTV.setBackgroundResource(R.drawable.bg_gray_date);
            viewHolder.scheduleWeekTv.setText("  PAST WEEK");
            viewHolder.weekDateTV.setBackgroundResource(R.drawable.bg_past_week_date);
        } else if (schedule.isFinal()) {
            viewHolder.scheduleWeekTv.setText("  FINALIZED");
            viewHolder.scheduleWeekTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.schedule_finalized, 0, 0, 0);
            viewHolder.weekDateTV.setBackgroundResource(R.drawable.bg_finalized);
        } else {
            viewHolder.weekDateTV.setBackgroundResource(R.drawable.bg_violet_date);
            viewHolder.scheduleWeekTv.setText("  PUBLISHED");
            viewHolder.scheduleWeekTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.schedule_proposed, 0, 0, 0);
            viewHolder.weekDateTV.setBackgroundResource(R.drawable.bg_published);
        }

        //viewHolder.ratingBar.setRating(Float.parseFloat(schedule.getRating()));
       /* viewHolder.star1.setImageResource(R.drawable.ic_star_unselected);
        viewHolder.star2.setImageResource(R.drawable.ic_star_unselected);
        viewHolder.star3.setImageResource(R.drawable.ic_star_unselected);
        viewHolder.star4.setImageResource(R.drawable.ic_star_unselected);
        viewHolder.star5.setImageResource(R.drawable.ic_star_unselected);
*/
       /* String rating = schedule.getRating();
        if (rating.equalsIgnoreCase("OneStar") || rating.equalsIgnoreCase("1")) {
            viewHolder.star1.setImageResource(R.drawable.ic_star_selected);
        } else if (rating.equalsIgnoreCase("TwoStar") || rating.equalsIgnoreCase("2")) {
            viewHolder.star1.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star2.setImageResource(R.drawable.ic_star_selected);
        } else if (rating.equalsIgnoreCase("ThreeStar") || rating.equalsIgnoreCase("3")) {
            viewHolder.star1.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star2.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star3.setImageResource(R.drawable.ic_star_selected);
        } else if (rating.equalsIgnoreCase("FourStar") || rating.equalsIgnoreCase("4")) {
            viewHolder.star1.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star2.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star3.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star4.setImageResource(R.drawable.ic_star_selected);
        } else if (rating.equalsIgnoreCase("FiveStar") || rating.equalsIgnoreCase("5")) {
            viewHolder.star1.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star2.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star3.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star4.setImageResource(R.drawable.ic_star_selected);
            viewHolder.star5.setImageResource(R.drawable.ic_star_selected);
        }*/
        viewHolder.businessAddress.setText(LegionUtils.getUpdatedAddress(schedule.getBusinessKey().getAddress()));
        viewHolder.businessName.setText(schedule.getBusinessKey().getName());
        return convertView;
    }

    private static class ViewHolder {
        ImageView star1, star2, star3, star4, star5;
        TextView businessAddress, businessName, weekDateTV, scheduleWeekTv, newTV;
    }
}
