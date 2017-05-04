package co.legion.client.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import base.Legion_BaseActivity;
import co.legion.client.R;
import fragments.PastScheduleFragment;
import models.ScheduleWorkerShift;
import models.ShiftOffer;
import utils.LegionUtils;

/**
 * Created by Administrator on 17-Jan-17.
 */
public class PreviewInScheduleActivity extends Legion_BaseActivity implements View.OnClickListener {
    private PastScheduleFragment fragment;
    private boolean checkCurrentWeekOrNot;
    private ImageView backImage;
    private TextView toolbarTitleTV;
    private ShiftOffer shiftOffer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String dateTvString = LegionUtils.getWeekDate(prefsManager.get(Prefs_Keys.START_DATE).replace("T", " ").replace("Z", " ")) + " - " + LegionUtils.getWeekDate(prefsManager.get(Prefs_Keys.END_DATE).replace("T", " ").replace("Z", " "));
        setContentView(R.layout.activity_preview_in_schedule);
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        TextView dateTv = (TextView) findViewById(R.id.dateTv);
      //  dateTv.setText(dateTvString);

        String startWeekDate =  LegionUtils.getWeekDate(prefsManager.get(Prefs_Keys.START_DATE));
        String endWeekDate = LegionUtils.getWeekDate(prefsManager.get(Prefs_Keys.END_DATE));

        setDate(startWeekDate,endWeekDate,dateTv);
        checkCurrentWeekOrNot = LegionUtils.getDiffernceBnTwoDates(prefsManager.get(Prefs_Keys.START_DATE).replace("T", " ").replace("Z", " "));
        if (checkCurrentWeekOrNot) {
          setupToolbar(true,"Schedule Preview");
        } else if (LegionUtils.isPastWeek(prefsManager.get(Prefs_Keys.START_DATE).replace("T", " ").replace("Z", " "), prefsManager.get(Prefs_Keys.START_DATE).replace("T", " ").replace("Z", " "))) {
          setupToolbar(true,"Schedule Preview");
        } else {
          setupToolbar(true,"Schedule Preview");
        }


        fragment = new PastScheduleFragment();
        Bundle b = new Bundle();
        if (getIntent().hasExtra(Extras_Keys.SHIFT_OFFER_KEY)){
            shiftOffer = (ShiftOffer) getIntent().getExtras().get(Extras_Keys.SHIFT_OFFER_KEY);
            b.putSerializable(Extras_Keys.SHIFT_OFFER_KEY, shiftOffer);
        }


        fragment.setArguments(b);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.previewInScheduleContainer, fragment);
        transaction.commit();
        LegionUtils.doApplyFont(getAssets(),parentLayout);
    }
    public void setupToolbar( boolean isBackRequired, String toolbarTitle) {
        backImage = (ImageView) findViewById(R.id.toolbarBack);
        toolbarTitleTV = (TextView) findViewById(R.id.tv_title);
        backImage.setOnClickListener(this);

        if (isBackRequired) {
            backImage.setVisibility(View.VISIBLE);
        } else {
            backImage.setVisibility(View.GONE);
        }
        toolbarTitleTV.setText(toolbarTitle);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbarBack){
            finish();
        }
    }

    public void setDate(String startOfWeekDate, String endOfWeekDate, TextView dateTv) {
        try {

            String[] startDate = startOfWeekDate.split(" ");
            String[] endDate = endOfWeekDate.split(" ");
            String dateTvString;
            if(!(startDate[0].equalsIgnoreCase(endDate[0]))){
                dateTvString = startOfWeekDate + " - " + endOfWeekDate;
            }else{
                dateTvString = (startOfWeekDate+" - "+endDate[1]).toString();   //.toUpperCase()
            }

            dateTv.setText(dateTvString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
