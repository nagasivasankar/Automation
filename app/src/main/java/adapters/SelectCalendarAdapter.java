package adapters;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import co.legion.client.R;
import models.SelectCalendarModel;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/29/2016.
 */
public class SelectCalendarAdapter extends BaseAdapter implements Legion_Constants {

    private final ArrayList<SelectCalendarModel> calendarsList;
    private final LayoutInflater inflater;
    private final Context context;

    public SelectCalendarAdapter(Context context , ArrayList<SelectCalendarModel> calendarsList) {
        this.calendarsList = calendarsList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return calendarsList.size();
    }

    @Override
    public Object getItem(int position) {
        return calendarsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            convertView = inflater.inflate(R.layout.row_calendar_settings, null);
            viewHolder = new ViewHolder();
            viewHolder.calendarTv = (TextView) convertView.findViewById(R.id.calendarTv);
            viewHolder.selectedIv = (ImageView) convertView.findViewById(R.id.selectedIv);
            LegionUtils.doApplyFont(context.getAssets(), (LinearLayout) convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SelectCalendarModel model = calendarsList.get(position);
        viewHolder.calendarTv.setText(model.getCalendarName());
        if(model.isSelectedCalendar()){
            viewHolder.selectedIv.setVisibility(View.VISIBLE);
            DrawableCompat.setTint(viewHolder.selectedIv.getDrawable(), ContextCompat.getColor(context, R.color.light_blue));
        }
        else{
            viewHolder.selectedIv.setVisibility(View.INVISIBLE);
        }
        convertView.setBackgroundColor(ActivityCompat.getColor(context, R.color.white));
        return convertView;
    }

    private static class ViewHolder {
        TextView calendarTv;
        ImageView selectedIv;
    }
}
