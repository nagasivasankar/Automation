package adapters;

import android.content.Context;
import android.graphics.Color;
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
import models.AlertModel;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/29/2016.
 */
public class AlertAdapter extends BaseAdapter implements Legion_Constants {

    private final ArrayList<AlertModel> calendarsList;
    private final LayoutInflater inflater;
    private final Context context;

    public AlertAdapter(Context context, ArrayList<AlertModel> calendarsList) {
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
        AlertModel model = calendarsList.get(position);
        if (model.getTimeInText().equalsIgnoreCase("")) {
            convertView.setBackgroundColor(Color.parseColor("#F5F5F9"));
        } else {
            convertView.setBackgroundColor(ActivityCompat.getColor(context, R.color.white));
        }
        viewHolder.calendarTv.setText(model.getTimeInText());
        if (model.isSelectedTime()) {
            DrawableCompat.setTint(viewHolder.selectedIv.getDrawable(), ContextCompat.getColor(context, R.color.light_blue));
            viewHolder.selectedIv.setVisibility(View.VISIBLE);
        } else {
            viewHolder.selectedIv.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView calendarTv;
        ImageView selectedIv;
    }
}
