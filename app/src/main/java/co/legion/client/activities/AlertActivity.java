package co.legion.client.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.AlertAdapter;
import base.Legion_BaseActivity;
import co.legion.client.R;
import models.AlertModel;
import utils.LegionUtils;

/**
 * Created by Administrator on 08-Mar-17.
 */
public class AlertActivity extends Legion_BaseActivity implements View.OnClickListener {
    String[] timeArray = new String[]{"None", "", "At time of event", "5 minutes before", "15 minutes before", "30 minutes before", "1 hour before", "2 hours before", "1 day before", "2 days before"};
    String[] timeArrayinMins = new String[]{"None", "", "0", "5", "15", "30", "60", "120", "1440", "2880"};
    private ListView alertLv;
    private LinearLayout alertLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        tvToolbarTile.setText("Alert");
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setOnClickListener(this);
        alertLv = (ListView) findViewById(R.id.alertLv);
        alertLL = (LinearLayout) findViewById(R.id.alertLL);
        final ArrayList<AlertModel> data = getData();
        final AlertAdapter adapter = new AlertAdapter(this, data);
        alertLv.setAdapter(adapter);
        alertLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertModel model1 = data.get(position);
                if (!model1.getTimeInText().equalsIgnoreCase("")) {
                    for (int i = 0; i < data.size(); i++) {
                        AlertModel model = data.get(i);
                        if (i == position) {
                            model.setSelectedTime(true);
                            prefsManager.save(Prefs_Keys.SELECTED_TIME, model.getTimeInText());
                            prefsManager.save(Prefs_Keys.SELECTED_TIME_IN_MINS, model.getTime());
                        } else {
                            model.setSelectedTime(false);
                        }
                    }

                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public ArrayList<AlertModel> getData() {
        ArrayList<AlertModel> dataArrayList = new ArrayList<>();
        for (int i = 0; i < timeArray.length; i++) {
            AlertModel model = new AlertModel();
            model.setTime(timeArrayinMins[i]);
            if (prefsManager.hasKey(Prefs_Keys.SELECTED_TIME) && (prefsManager.get(Prefs_Keys.SELECTED_TIME)).equalsIgnoreCase(timeArray[i])) {
                model.setSelectedTime(true);
            } else {
                model.setSelectedTime(false);
            }

            model.setTimeInText(timeArray[i]);
            dataArrayList.add(model);
        }
        return dataArrayList;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.toolbarBack){
            finish();
        }
    }
}
