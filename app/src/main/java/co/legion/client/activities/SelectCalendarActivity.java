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
import java.util.Enumeration;
import java.util.Hashtable;

import adapters.SelectCalendarAdapter;
import base.Legion_BaseActivity;
import co.legion.client.R;
import helpers.CalendarHelper;
import models.SelectCalendarModel;
import utils.LegionUtils;

/**
 * Created by Administrator on 08-Mar-17.
 */
public class SelectCalendarActivity extends Legion_BaseActivity implements View.OnClickListener {
    private Hashtable<String, String> calendarsList = new Hashtable<>();
    private ArrayList<SelectCalendarModel> calendarsData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_calendar);
        ListView selectCalendarLv = (ListView) findViewById(R.id.selectCalendarLv);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        tvToolbarTile.setText("Select Calendar");
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setOnClickListener(this);
        calendarsData = getCalendarsList();

        final SelectCalendarAdapter adapter = new SelectCalendarAdapter(this, calendarsData);
        selectCalendarLv.setAdapter(adapter);
        selectCalendarLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < calendarsData.size(); i++) {
                    SelectCalendarModel model = calendarsData.get(i);
                    if (i == position) {
                        model.setSelectedCalendar(true);
                        prefsManager.save(Prefs_Keys.CALENDAR_NAME, model.getCalendarName());
                    } else {
                        model.setSelectedCalendar(false);
                    }
                }

                adapter.notifyDataSetChanged();

            }
        });
    }


    public ArrayList<SelectCalendarModel> getCalendarsList() {
        ArrayList<SelectCalendarModel> calendarsArrayList = new ArrayList<>();
        if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
            calendarsList = CalendarHelper.listCalendarId(this);
            if (calendarsList != null) {
                Enumeration e = calendarsList.keys();
                while (e.hasMoreElements()) {
                    String key = (String) e.nextElement();
                  //  if (key.contains(".") && key.contains("@")) {
                        SelectCalendarModel model = new SelectCalendarModel();
                        model.setCalendarName(key);
                        if (prefsManager.hasKey(Prefs_Keys.CALENDAR_NAME) && (prefsManager.get(Prefs_Keys.CALENDAR_NAME)).equalsIgnoreCase(key)) {
                            model.setSelectedCalendar(true);
                        } else {
                            model.setSelectedCalendar(false);
                        }
                        calendarsArrayList.add(model);
                 //   }
                }
                return calendarsArrayList;
            }
        } else {
            CalendarHelper.requestCalendarReadWritePermission(this);
        }
        return calendarsArrayList;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbarBack) {
            finish();
        }
    }
}
