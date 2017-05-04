package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.legion.client.R;
import helpers.Legion_PrefsManager;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 12/6/2016.
 */
public class ShiftOffersFilterSpinnerAdapter extends BaseAdapter {
    private final Legion_PrefsManager legionPreferences;
    private int[] imageDrawables;
    private String[] spinnerTitles;
    private int[] countArray;
    private LayoutInflater inflater;
    private Activity activity;

    public ShiftOffersFilterSpinnerAdapter(Activity activity, int[] imageDrawables, String[] spinnerTitles, int[] countsArrays) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageDrawables = imageDrawables;
        this.spinnerTitles = spinnerTitles;
        this.countArray = countsArrays;
        this.legionPreferences = new Legion_PrefsManager(activity);
    }

    @Override
    public int getCount() {
        return spinnerTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return spinnerTitles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.spinner_item_dropdown,null);
            LegionUtils.doApplyFont(activity.getAssets(), (LinearLayout) convertView.findViewById(R.id.parentLayout));
        }
        ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_icon_left);
        TextView title = (TextView) convertView.findViewById(R.id.tv_spinner_item_title);
        TextView count = (TextView) convertView.findViewById(R.id.tv_count);
        LinearLayout parentLayout = (LinearLayout)convertView.findViewById(R.id.parentLayout);

        imageView.setImageResource(imageDrawables[position]);
        title.setText(spinnerTitles[position]);
        count.setText(""+countArray[position]);
        parentLayout.setBackgroundColor(Color.WHITE);
        title.setTextColor(Color.parseColor("#0A0B0C"));
        count.setTextColor(Color.parseColor("#0A0B0C"));
        if (legionPreferences.get(Legion_Constants.Prefs_Keys.OFFER_STATUS).equals("Bookmarked") && spinnerTitles[position].contains("Bookmark")) {
            parentLayout.setBackgroundColor(Color.parseColor("#F0F5FC"));
            title.setTextColor(Color.parseColor("#242365"));
            count.setTextColor(Color.parseColor("#242365"));
        }else if (legionPreferences.get(Legion_Constants.Prefs_Keys.OFFER_STATUS).equals("New") && spinnerTitles[position].contains("New")) {
            parentLayout.setBackgroundColor(Color.parseColor("#F0F5FC"));
            title.setTextColor(Color.parseColor("#242365"));
            count.setTextColor(Color.parseColor("#242365"));
        }else if (legionPreferences.get(Legion_Constants.Prefs_Keys.OFFER_STATUS).equals("Proposed") && spinnerTitles[position].contains("Unclaimed")) {
            parentLayout.setBackgroundColor(Color.parseColor("#F0F5FC"));
            title.setTextColor(Color.parseColor("#242365"));
            count.setTextColor(Color.parseColor("#242365"));
        }else if (legionPreferences.get(Legion_Constants.Prefs_Keys.OFFER_STATUS).equals("Claimed") && spinnerTitles[position].contains("Pending")) {
            parentLayout.setBackgroundColor(Color.parseColor("#F0F5FC"));
            title.setTextColor(Color.parseColor("#242365"));
            count.setTextColor(Color.parseColor("#242365"));
        }else if (legionPreferences.get(Legion_Constants.Prefs_Keys.OFFER_STATUS).equals("Accepted") && spinnerTitles[position].contains("Approved")) {
            parentLayout.setBackgroundColor(Color.parseColor("#F0F5FC"));
            title.setTextColor(Color.parseColor("#242365"));
            count.setTextColor(Color.parseColor("#242365"));
        }else if (legionPreferences.get(Legion_Constants.Prefs_Keys.OFFER_STATUS).equals("Declined") && spinnerTitles[position].contains("Rejected")) {
            parentLayout.setBackgroundColor(Color.parseColor("#F0F5FC"));
            title.setTextColor(Color.parseColor("#242365"));
            count.setTextColor(Color.parseColor("#242365"));
        }

        return convertView;
    }
}
