package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.legion.client.R;
import models.AvailabilityProgress;

/**
 * Created by Administrator on 12/8/2016.
 */
public class AvailabilityProgressAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<AvailabilityProgress> avalbtyProgress;

    public AvailabilityProgressAdapter(Context context, ArrayList<AvailabilityProgress> avalbtyProgress) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.avalbtyProgress = avalbtyProgress;
    }

    @Override
    public int getCount() {
        return avalbtyProgress.size();
    }

    @Override
    public Object getItem(int position) {
        return avalbtyProgress.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_availbty_progress, null);
            holder = new ViewHolder();
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;
            width = width - (130);
            holder.bgDashedLineTv = (TextView) view.findViewById(R.id.bgDashedLineTv);
            holder.bgGapTv = (TextView) view.findViewById(R.id.bgGapTv);
            holder.bgDashedLineTv.setMinWidth((width/avalbtyProgress.size()));
            holder.bgGapTv.setMinWidth(2);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        AvailabilityProgress avlbtyProgress = avalbtyProgress.get(position);

        if(avlbtyProgress.isGreen()){
            holder.bgDashedLineTv.setBackgroundColor(ActivityCompat.getColor(context,R.color.green));
        }else{
            holder.bgDashedLineTv.setBackgroundColor(ActivityCompat.getColor(context,R.color.light_gray_2));
        }
        return view;
    }

    private class ViewHolder {
        TextView bgDashedLineTv;
        TextView bgGapTv;
    }
}