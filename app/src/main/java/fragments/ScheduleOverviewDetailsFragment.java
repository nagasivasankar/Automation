package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.SchedulesViewPagerAdapter;
import base.Legion_BaseFragment;
import co.legion.client.R;
import models.Schedule;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/30/2016.
 */
public class ScheduleOverviewDetailsFragment extends Legion_BaseFragment implements Legion_Constants {

    ArrayList<Schedule> scheduledData = new ArrayList<>();
    private SchedulesViewPagerAdapter viewPagerAdapter;
    private TextView dateTv;
    private int selectedPosition;
    private String startWeekDate;
    private String endWeekDate;
    private int prevPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_overview_details, null);
        return view;
    }

    public ScheduleOverviewDetailsFragment(){

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar(view, true, "Past Schedule");
        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));
        dateTv = (TextView) view.findViewById(R.id.dateTv);

        if (getArguments() != null) {
            scheduledData = (ArrayList<Schedule>) getArguments().getSerializable(Extras_Keys.SCHEDULE_DETAILS_DATA);
            int position = getArguments().getInt(Extras_Keys.POSITION);
            endWeekDate = LegionUtils.getOneDayMinus(scheduledData.get(position).getEndOfWeekDate());
            startWeekDate = scheduledData.get(position).getStartOfWeekDate();
            selectedPosition = (position + 500);
            setDate(startWeekDate, endWeekDate, selectedPosition);
            legionPreferences.save(Prefs_Keys.START_DATE,startWeekDate);
            legionPreferences.save( Prefs_Keys.END_DATE,endWeekDate);
            prevPosition = selectedPosition;
        }
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new SchedulesViewPagerAdapter(getActivity(), getChildFragmentManager(), scheduledData,this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(prevPosition);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (prevPosition > position) {
                 //   Toast.makeText(getActivity(), "Swiped Left", Toast.LENGTH_SHORT).show();
                    endWeekDate = LegionUtils.getOneDayMinus(startWeekDate);
                    startWeekDate = LegionUtils.getStartDate(endWeekDate);
                    legionPreferences.save(Prefs_Keys.START_DATE,startWeekDate);
                    legionPreferences.save( Prefs_Keys.END_DATE,endWeekDate);
                    setDate(startWeekDate, endWeekDate, position);
                } else {
                   //Toast.makeText(getActivity(), "Swiped Right", Toast.LENGTH_SHORT).show();
                    startWeekDate = LegionUtils.getOneDayPlus(endWeekDate);
                    endWeekDate = LegionUtils.getEndDate(startWeekDate);
                    legionPreferences.save(Prefs_Keys.START_DATE,startWeekDate);
                    legionPreferences.save( Prefs_Keys.END_DATE,endWeekDate);
                    setDate(startWeekDate, endWeekDate, position);
                }
                prevPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        ImageView prevIv = (ImageView) view.findViewById(R.id.prevIv);
        ImageView nextIv = (ImageView) view.findViewById(R.id.nextIv);

        prevIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 2);
            }
        });
        nextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 2);
            }
        });
    }

    public void setDate(String startOfWeekDate, String endOfWeekDate, int position) {
        try {

            String[] startDate = LegionUtils.getWeekDate(startOfWeekDate.replace("T", " ")).split(" ");
            String[] endDate = LegionUtils.getWeekDate(endOfWeekDate.replace("T", " ")).split(" ");
            String dateTvString;
            if(!(startDate[0].equalsIgnoreCase(endDate[0]))){
                dateTvString = LegionUtils.getWeekDate(startOfWeekDate.replace("T", " ").replace("Z", " ")) + " - " + LegionUtils.getWeekDate(endOfWeekDate.replace("T", " ").replace("Z", " "));
            }else{
                dateTvString = (LegionUtils.getWeekDate(startOfWeekDate.replace("T", " "))+" - "+endDate[1]).toString();   //.toUpperCase()
            }

            dateTv.setText(dateTvString);
/*
            if (LegionUtils.isCurrentWeek(startOfWeekDate.replace("T", " ").replace("Z", " "), endOfWeekDate.replace("T", " ").replace("Z", " "))) {
                setupToolbar(getView(), true, "Current Schedule");
            } else if (((500 + scheduledData.size()) > position) && (position >= 500)) {
                if (scheduledData.get(position - 500).isPastWeek()) {
                    setupToolbar(getView(), true, "Past Schedule");
                } else if (scheduledData.get(position - 500).isFinal()) {
                    setupToolbar(getView(), true, "Finalized Schedule");
                } else {
                    setupToolbar(getView(), true, "Published Schedule");
                }
            } else if (LegionUtils.isPastWeek(startOfWeekDate.replace("T", " ").replace("Z", " "), endOfWeekDate.replace("T", " ").replace("Z", " "))) {
                setupToolbar(getView(), true, "Past Schedule");
            } else {
                setupToolbar(getView(), true, "Future Schedule");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
