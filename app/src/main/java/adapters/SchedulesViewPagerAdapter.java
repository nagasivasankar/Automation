package adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import fragments.PastScheduleFragment;
import fragments.ScheduleOverviewDetailsFragment;
import models.Schedule;
import utils.Legion_Constants;

/**
 * Created by Administrator on 12/1/2016.
 */
public class SchedulesViewPagerAdapter extends FragmentStatePagerAdapter implements Legion_Constants {
    private final ScheduleOverviewDetailsFragment hostFrag;
    public int currentIndex;
    ArrayList<Schedule> scheduleData;
    Context mContext;

    public SchedulesViewPagerAdapter(Context activity, FragmentManager fm, ArrayList<Schedule> data, ScheduleOverviewDetailsFragment hostFrag) {
        super(fm);
        mContext = activity;
        scheduleData = data;
        this.hostFrag = hostFrag;
    }

    @Override
    public Fragment getItem(int position) {
        PastScheduleFragment groupFragment = new PastScheduleFragment(hostFrag);
        Bundle b = new Bundle();
        groupFragment.setArguments(b);
        return groupFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Details";
    }

    @Override
    public int getCount() {
        return 1000;
    }

}