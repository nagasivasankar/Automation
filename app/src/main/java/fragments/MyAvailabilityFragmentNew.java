package fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import base.Legion_BaseFragment;
import co.legion.client.R;
import co.legion.client.activities.OnBoardingActivity;
import co.legion.client.activities.UpdateAvailabilityActivityNew;
import models.Slot;
import models.SlotType;
import utils.LegionUtils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import base.Legion_BaseFragment;
import co.legion.client.R;
import co.legion.client.activities.OnBoardingActivity;
import co.legion.client.activities.UpdateAvailabilityActivityNew;
import models.Slot;
import models.SlotType;
import utils.LegionUtils;

/**
 * Created by Administrator on 11/16/2016.
 */

@SuppressLint("ValidFragment")
public class MyAvailabilityFragmentNew extends Legion_BaseFragment implements View.OnTouchListener, GestureDetector.OnGestureListener {

    public static MyAvailabilityFragmentNew instance;
    private boolean isOnBoarding;
    private boolean isPastSchedule;
    private boolean isScheduleLocked;
    private TextView saveButton;
    private LinearLayout monday4amTo430amView, monday430amTo5amView, monday5amTo530amView, monday530amTo6amView,
            monday6amTo630amView, monday630amTo7amView, monday7amTo730amView, monday730amTo8amView,
            monday8amTo830amView, monday830amTo9amView, monday9amTo930amView, monday930amTo10amView,
            monday10amTo1030amView, monday1030amTo11amView, monday11amTo1130amView, monday1130amTo12pmView,
            monday12pmTo1230pmView, monday1230pmTo1pmView, monday1pmTo130pmView, monday130pmTo2pmView,
            monday2pmTo230pmView, monday230pmTo3pmView, monday3pmTo330pmView, monday330pmTo4pmView,
            monday4pmTo430pmView, monday430pmTo5pmView, monday5pmTo530pmView, monday530pmTo6pmView,
            monday6pmTo630pmView, monday630pmTo7pmView, monday7pmTo730pmView, monday730pmTo8pmView,
            monday8pmTo830pmView, monday830pmTo9pmView, monday9pmTo930pmView, monday930pmTo10pmView,
            monday10pmTo1030pmView, monday1030pmTo11pmView, monday11pmTo1130pmView, monday1130pmTo12amView;

    private LinearLayout tuesday4amTo430amView, tuesday430amTo5amView, tuesday5amTo530amView, tuesday530amTo6amView,
            tuesday6amTo630amView, tuesday630amTo7amView, tuesday7amTo730amView, tuesday730amTo8amView,
            tuesday8amTo830amView, tuesday830amTo9amView, tuesday9amTo930amView, tuesday930amTo10amView,
            tuesday10amTo1030amView, tuesday1030amTo11amView, tuesday11amTo1130amView, tuesday1130amTo12pmView,
            tuesday12pmTo1230pmView, tuesday1230pmTo1pmView, tuesday1pmTo130pmView, tuesday130pmTo2pmView,
            tuesday2pmTo230pmView, tuesday230pmTo3pmView, tuesday3pmTo330pmView, tuesday330pmTo4pmView,
            tuesday4pmTo430pmView, tuesday430pmTo5pmView, tuesday5pmTo530pmView, tuesday530pmTo6pmView,
            tuesday6pmTo630pmView, tuesday630pmTo7pmView, tuesday7pmTo730pmView, tuesday730pmTo8pmView,
            tuesday8pmTo830pmView, tuesday830pmTo9pmView, tuesday9pmTo930pmView, tuesday930pmTo10pmView,
            tuesday10pmTo1030pmView, tuesday1030pmTo11pmView, tuesday11pmTo1130pmView, tuesday1130pmTo12amView;


    private LinearLayout wednesday4amTo430amView, wednesday430amTo5amView, wednesday5amTo530amView, wednesday530amTo6amView,
            wednesday6amTo630amView, wednesday630amTo7amView, wednesday7amTo730amView, wednesday730amTo8amView,
            wednesday8amTo830amView, wednesday830amTo9amView, wednesday9amTo930amView, wednesday930amTo10amView,
            wednesday10amTo1030amView, wednesday1030amTo11amView, wednesday11amTo1130amView, wednesday1130amTo12pmView,
            wednesday12pmTo1230pmView, wednesday1230pmTo1pmView, wednesday1pmTo130pmView, wednesday130pmTo2pmView,
            wednesday2pmTo230pmView, wednesday230pmTo3pmView, wednesday3pmTo330pmView, wednesday330pmTo4pmView,
            wednesday4pmTo430pmView, wednesday430pmTo5pmView, wednesday5pmTo530pmView, wednesday530pmTo6pmView,
            wednesday6pmTo630pmView, wednesday630pmTo7pmView, wednesday7pmTo730pmView, wednesday730pmTo8pmView,
            wednesday8pmTo830pmView, wednesday830pmTo9pmView, wednesday9pmTo930pmView, wednesday930pmTo10pmView,
            wednesday10pmTo1030pmView, wednesday1030pmTo11pmView, wednesday11pmTo1130pmView, wednesday1130pmTo12amView;


    private LinearLayout thursday4amTo430amView, thursday430amTo5amView, thursday5amTo530amView, thursday530amTo6amView,
            thursday6amTo630amView, thursday630amTo7amView, thursday7amTo730amView, thursday730amTo8amView,
            thursday8amTo830amView, thursday830amTo9amView, thursday9amTo930amView, thursday930amTo10amView,
            thursday10amTo1030amView, thursday1030amTo11amView, thursday11amTo1130amView, thursday1130amTo12pmView,
            thursday12pmTo1230pmView, thursday1230pmTo1pmView, thursday1pmTo130pmView, thursday130pmTo2pmView,
            thursday2pmTo230pmView, thursday230pmTo3pmView, thursday3pmTo330pmView, thursday330pmTo4pmView,
            thursday4pmTo430pmView, thursday430pmTo5pmView, thursday5pmTo530pmView, thursday530pmTo6pmView,
            thursday6pmTo630pmView, thursday630pmTo7pmView, thursday7pmTo730pmView, thursday730pmTo8pmView,
            thursday8pmTo830pmView, thursday830pmTo9pmView, thursday9pmTo930pmView, thursday930pmTo10pmView,
            thursday10pmTo1030pmView, thursday1030pmTo11pmView, thursday11pmTo1130pmView, thursday1130pmTo12amView;

    private LinearLayout friday4amTo430amView, friday430amTo5amView, friday5amTo530amView, friday530amTo6amView,
            friday6amTo630amView, friday630amTo7amView, friday7amTo730amView, friday730amTo8amView,
            friday8amTo830amView, friday830amTo9amView, friday9amTo930amView, friday930amTo10amView,
            friday10amTo1030amView, friday1030amTo11amView, friday11amTo1130amView, friday1130amTo12pmView,
            friday12pmTo1230pmView, friday1230pmTo1pmView, friday1pmTo130pmView, friday130pmTo2pmView,
            friday2pmTo230pmView, friday230pmTo3pmView, friday3pmTo330pmView, friday330pmTo4pmView,
            friday4pmTo430pmView, friday430pmTo5pmView, friday5pmTo530pmView, friday530pmTo6pmView,
            friday6pmTo630pmView, friday630pmTo7pmView, friday7pmTo730pmView, friday730pmTo8pmView,
            friday8pmTo830pmView, friday830pmTo9pmView, friday9pmTo930pmView, friday930pmTo10pmView,
            friday10pmTo1030pmView, friday1030pmTo11pmView, friday11pmTo1130pmView, friday1130pmTo12amView;

    private LinearLayout saturday4amTo430amView, saturday430amTo5amView, saturday5amTo530amView, saturday530amTo6amView,
            saturday6amTo630amView, saturday630amTo7amView, saturday7amTo730amView, saturday730amTo8amView,
            saturday8amTo830amView, saturday830amTo9amView, saturday9amTo930amView, saturday930amTo10amView,
            saturday10amTo1030amView, saturday1030amTo11amView, saturday11amTo1130amView, saturday1130amTo12pmView,
            saturday12pmTo1230pmView, saturday1230pmTo1pmView, saturday1pmTo130pmView, saturday130pmTo2pmView,
            saturday2pmTo230pmView, saturday230pmTo3pmView, saturday3pmTo330pmView, saturday330pmTo4pmView,
            saturday4pmTo430pmView, saturday430pmTo5pmView, saturday5pmTo530pmView, saturday530pmTo6pmView,
            saturday6pmTo630pmView, saturday630pmTo7pmView, saturday7pmTo730pmView, saturday730pmTo8pmView,
            saturday8pmTo830pmView, saturday830pmTo9pmView, saturday9pmTo930pmView, saturday930pmTo10pmView,
            saturday10pmTo1030pmView, saturday1030pmTo11pmView, saturday11pmTo1130pmView, saturday1130pmTo12amView;

    private LinearLayout sunday4amTo430amView, sunday430amTo5amView, sunday5amTo530amView, sunday530amTo6amView,
            sunday6amTo630amView, sunday630amTo7amView, sunday7amTo730amView, sunday730amTo8amView,
            sunday8amTo830amView, sunday830amTo9amView, sunday9amTo930amView, sunday930amTo10amView,
            sunday10amTo1030amView, sunday1030amTo11amView, sunday11amTo1130amView, sunday1130amTo12pmView,
            sunday12pmTo1230pmView, sunday1230pmTo1pmView, sunday1pmTo130pmView, sunday130pmTo2pmView,
            sunday2pmTo230pmView, sunday230pmTo3pmView, sunday3pmTo330pmView, sunday330pmTo4pmView,
            sunday4pmTo430pmView, sunday430pmTo5pmView, sunday5pmTo530pmView, sunday530pmTo6pmView,
            sunday6pmTo630pmView, sunday630pmTo7pmView, sunday7pmTo730pmView, sunday730pmTo8pmView,
            sunday8pmTo830pmView, sunday830pmTo9pmView, sunday9pmTo930pmView, sunday930pmTo10pmView,
            sunday10pmTo1030pmView, sunday1030pmTo11pmView, sunday11pmTo1130pmView, sunday1130pmTo12amView;

    private TextView numOfAvailableHrsTV;
    private ArrayList<Slot> mondaySlotsList;
    private ArrayList<Slot> tuesdaySlotsList;
    private ArrayList<Slot> wednesdaySlotsList;
    private ArrayList<Slot> thursdaySlotsList;
    private ArrayList<Slot> fridaySlotsList;
    private ArrayList<Slot> saturdaySlotsList;
    private ArrayList<Slot> sundaySlotsList;
    private TextView totalNumOfHoursTV;
    private long lastLongClickTime;
    private GranularAvailabilityDialogFragment dialogFragment;
    private TextView tutorialTextView;
    private LinearLayout sliderViewMonday;
    private LinearLayout sliderViewTuesday;
    private LinearLayout sliderViewWednesday;
    private LinearLayout sliderViewThursday;
    private LinearLayout sliderViewFriday;
    private LinearLayout sliderViewSaturday;
    private LinearLayout sliderViewSunday;
    private LinearLayout sliderView;
    private GestureDetector gestureScanner;
    private boolean isSlidingStarted;
    private LinearLayout currentBackgroundSlotView;
    private LinearLayout mondaySlotsParentLL;
    private int sliderDurationInMins = -1;
    private int sliderStartMins;
    private View availabilityModeLineView;
    private int initialWidthOfcurrentBackgroundSlotView;
    private float fingerDownInitialX;
    private boolean isLeftTouch, isRightTouch, isCenterTouch;
    private int sliderEndMins;
    private int initialLeftOfCurrentBackgroundSlotView;
    private PopupWindow timerPopupWindow;
    private TextView timerTextView;
    private LinearLayout parentLayout;
    private int[] sliderViewLocation;
    private LinearLayout popupView;
    private LinearLayout dottedProgressbarContainer;
    private String sliderStartTime;
    private String sliderEndTime;
    private boolean isSliderDirectionDecided;
    private float deltaDist;
    private boolean isNewSlotDrawing;
    private boolean isAvailabilityMode;
    public boolean isAVUpdated;
    private LinearLayout sundaySlotsParentLL;
    private LinearLayout tuesdaySlotsParentLL;
    private LinearLayout wednesdaySlotsParentLL;
    private LinearLayout thursdaySlotsParentLL;
    private LinearLayout fridaySlotsParentLL;
    private LinearLayout saturdaySlotsParentLL;

    private RelativeLayout sundayPTOLayout;
    private RelativeLayout mondayPTOLayout;
    private RelativeLayout tuesdayPTOLayout;
    private RelativeLayout wednesdayPTOLayout;
    private RelativeLayout thursdayPTOLayout;
    private RelativeLayout fridayPTOLayout;
    private RelativeLayout saturdayPTOLayout;

    public MyAvailabilityFragmentNew() {
    }

    @SuppressLint("ValidFragment")
    public MyAvailabilityFragmentNew(TextView saveActionView, boolean isScheduleLocked, boolean isPastSchedule, boolean isAvailabilityMode, boolean isOnBoarding) {
        this.saveButton = saveActionView;
        this.isScheduleLocked = isScheduleLocked;
        this.isPastSchedule = isPastSchedule;
        this.isAvailabilityMode = isAvailabilityMode;
        this.isOnBoarding = isOnBoarding;
        this.isAVUpdated = false;
    }

    public void doEnableSaveButtonIfRequired() {
        if (saveButton != null && saveButton.getCurrentTextColor() != Color.WHITE) {
            saveButton.setTextColor(Color.WHITE);
            saveButton.setEnabled(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_prefertowork, null);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (getActivity() != null) {
                if (getActivity() instanceof OnBoardingActivity) {
                    OnBoardingActivity activity = (OnBoardingActivity) getActivity();
                    doInitializeLists(activity, null);
                } else if (getActivity() instanceof UpdateAvailabilityActivityNew) {
                    UpdateAvailabilityActivityNew activity = (UpdateAvailabilityActivityNew) getActivity();
                    doInitializeLists(null, activity);
                } else {
                    getActivity().finish();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity() instanceof UpdateAvailabilityActivityNew) {
            setUserVisibleHint(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (dialogFragment != null && dialogFragment.isVisible()) {
            dialogFragment.dismiss();
        }
    }

    public void doInitializeLists(OnBoardingActivity activity1, UpdateAvailabilityActivityNew activity2) {
        if (activity1 != null) {
            tutorialTextView.setVisibility(View.VISIBLE);
            availabilityModeLineView.setVisibility(View.VISIBLE);
            if (isAvailabilityMode) {
                availabilityModeLineView.setBackgroundColor(ActivityCompat.getColor(getActivity(), R.color.green));
            } else {
                availabilityModeLineView.setBackgroundColor(Color.parseColor("#D02D2D"));
            }
            mondaySlotsList = activity1.mondaySlotsList;
            tuesdaySlotsList = activity1.tuesdaySlotsList;
            wednesdaySlotsList = activity1.wednesdaySlotsList;
            thursdaySlotsList = activity1.thursdaySlotsList;
            fridaySlotsList = activity1.fridaySlotsList;
            saturdaySlotsList = activity1.saturdaySlotsList;
            sundaySlotsList = activity1.sundaySlotsList;
        } else if (activity2 != null) {
            tutorialTextView.setVisibility(View.GONE);
            availabilityModeLineView.setVisibility(View.GONE);
            mondaySlotsList = activity2.mondaySlotsList;
            tuesdaySlotsList = activity2.tuesdaySlotsList;
            wednesdaySlotsList = activity2.wednesdaySlotsList;
            thursdaySlotsList = activity2.thursdaySlotsList;
            fridaySlotsList = activity2.fridaySlotsList;
            saturdaySlotsList = activity2.saturdaySlotsList;
            sundaySlotsList = activity2.sundaySlotsList;
        } else {
            getActivity().finish();
        }
        doInitialSetup();
    }

    private void doInitialSetup() {
        mondaySlotsList.get(0).setView(monday4amTo430amView);
        mondaySlotsList.get(1).setView(monday430amTo5amView);
        mondaySlotsList.get(2).setView(monday5amTo530amView);
        mondaySlotsList.get(3).setView(monday530amTo6amView);
        mondaySlotsList.get(4).setView(monday6amTo630amView);
        mondaySlotsList.get(5).setView(monday630amTo7amView);
        mondaySlotsList.get(6).setView(monday7amTo730amView);
        mondaySlotsList.get(7).setView(monday730amTo8amView);
        mondaySlotsList.get(8).setView(monday8amTo830amView);
        mondaySlotsList.get(9).setView(monday830amTo9amView);
        mondaySlotsList.get(10).setView(monday9amTo930amView);
        mondaySlotsList.get(11).setView(monday930amTo10amView);
        mondaySlotsList.get(12).setView(monday10amTo1030amView);
        mondaySlotsList.get(13).setView(monday1030amTo11amView);
        mondaySlotsList.get(14).setView(monday11amTo1130amView);
        mondaySlotsList.get(15).setView(monday1130amTo12pmView);
        mondaySlotsList.get(16).setView(monday12pmTo1230pmView);
        mondaySlotsList.get(17).setView(monday1230pmTo1pmView);
        mondaySlotsList.get(18).setView(monday1pmTo130pmView);
        mondaySlotsList.get(19).setView(monday130pmTo2pmView);
        mondaySlotsList.get(20).setView(monday2pmTo230pmView);
        mondaySlotsList.get(21).setView(monday230pmTo3pmView);
        mondaySlotsList.get(22).setView(monday3pmTo330pmView);
        mondaySlotsList.get(23).setView(monday330pmTo4pmView);
        mondaySlotsList.get(24).setView(monday4pmTo430pmView);
        mondaySlotsList.get(25).setView(monday430pmTo5pmView);
        mondaySlotsList.get(26).setView(monday5pmTo530pmView);
        mondaySlotsList.get(27).setView(monday530pmTo6pmView);
        mondaySlotsList.get(28).setView(monday6pmTo630pmView);
        mondaySlotsList.get(29).setView(monday630pmTo7pmView);
        mondaySlotsList.get(30).setView(monday7pmTo730pmView);
        mondaySlotsList.get(31).setView(monday730pmTo8pmView);
        mondaySlotsList.get(32).setView(monday8pmTo830pmView);
        mondaySlotsList.get(33).setView(monday830pmTo9pmView);
        mondaySlotsList.get(34).setView(monday9pmTo930pmView);
        mondaySlotsList.get(35).setView(monday930pmTo10pmView);
        mondaySlotsList.get(36).setView(monday10pmTo1030pmView);
        mondaySlotsList.get(37).setView(monday1030pmTo11pmView);
        mondaySlotsList.get(38).setView(monday11pmTo1130pmView);
        mondaySlotsList.get(39).setView(monday1130pmTo12amView);

        tuesdaySlotsList.get(0).setView(tuesday4amTo430amView);
        tuesdaySlotsList.get(1).setView(tuesday430amTo5amView);
        tuesdaySlotsList.get(2).setView(tuesday5amTo530amView);
        tuesdaySlotsList.get(3).setView(tuesday530amTo6amView);
        tuesdaySlotsList.get(4).setView(tuesday6amTo630amView);
        tuesdaySlotsList.get(5).setView(tuesday630amTo7amView);
        tuesdaySlotsList.get(6).setView(tuesday7amTo730amView);
        tuesdaySlotsList.get(7).setView(tuesday730amTo8amView);
        tuesdaySlotsList.get(8).setView(tuesday8amTo830amView);
        tuesdaySlotsList.get(9).setView(tuesday830amTo9amView);
        tuesdaySlotsList.get(10).setView(tuesday9amTo930amView);
        tuesdaySlotsList.get(11).setView(tuesday930amTo10amView);
        tuesdaySlotsList.get(12).setView(tuesday10amTo1030amView);
        tuesdaySlotsList.get(13).setView(tuesday1030amTo11amView);
        tuesdaySlotsList.get(14).setView(tuesday11amTo1130amView);
        tuesdaySlotsList.get(15).setView(tuesday1130amTo12pmView);
        tuesdaySlotsList.get(16).setView(tuesday12pmTo1230pmView);
        tuesdaySlotsList.get(17).setView(tuesday1230pmTo1pmView);
        tuesdaySlotsList.get(18).setView(tuesday1pmTo130pmView);
        tuesdaySlotsList.get(19).setView(tuesday130pmTo2pmView);
        tuesdaySlotsList.get(20).setView(tuesday2pmTo230pmView);
        tuesdaySlotsList.get(21).setView(tuesday230pmTo3pmView);
        tuesdaySlotsList.get(22).setView(tuesday3pmTo330pmView);
        tuesdaySlotsList.get(23).setView(tuesday330pmTo4pmView);
        tuesdaySlotsList.get(24).setView(tuesday4pmTo430pmView);
        tuesdaySlotsList.get(25).setView(tuesday430pmTo5pmView);
        tuesdaySlotsList.get(26).setView(tuesday5pmTo530pmView);
        tuesdaySlotsList.get(27).setView(tuesday530pmTo6pmView);
        tuesdaySlotsList.get(28).setView(tuesday6pmTo630pmView);
        tuesdaySlotsList.get(29).setView(tuesday630pmTo7pmView);
        tuesdaySlotsList.get(30).setView(tuesday7pmTo730pmView);
        tuesdaySlotsList.get(31).setView(tuesday730pmTo8pmView);
        tuesdaySlotsList.get(32).setView(tuesday8pmTo830pmView);
        tuesdaySlotsList.get(33).setView(tuesday830pmTo9pmView);
        tuesdaySlotsList.get(34).setView(tuesday9pmTo930pmView);
        tuesdaySlotsList.get(35).setView(tuesday930pmTo10pmView);
        tuesdaySlotsList.get(36).setView(tuesday10pmTo1030pmView);
        tuesdaySlotsList.get(37).setView(tuesday1030pmTo11pmView);
        tuesdaySlotsList.get(38).setView(tuesday11pmTo1130pmView);
        tuesdaySlotsList.get(39).setView(tuesday1130pmTo12amView);

        //set wednesday
        wednesdaySlotsList.get(0).setView(wednesday4amTo430amView);
        wednesdaySlotsList.get(1).setView(wednesday430amTo5amView);
        wednesdaySlotsList.get(2).setView(wednesday5amTo530amView);
        wednesdaySlotsList.get(3).setView(wednesday530amTo6amView);
        wednesdaySlotsList.get(4).setView(wednesday6amTo630amView);
        wednesdaySlotsList.get(5).setView(wednesday630amTo7amView);
        wednesdaySlotsList.get(6).setView(wednesday7amTo730amView);
        wednesdaySlotsList.get(7).setView(wednesday730amTo8amView);
        wednesdaySlotsList.get(8).setView(wednesday8amTo830amView);
        wednesdaySlotsList.get(9).setView(wednesday830amTo9amView);
        wednesdaySlotsList.get(10).setView(wednesday9amTo930amView);
        wednesdaySlotsList.get(11).setView(wednesday930amTo10amView);
        wednesdaySlotsList.get(12).setView(wednesday10amTo1030amView);
        wednesdaySlotsList.get(13).setView(wednesday1030amTo11amView);
        wednesdaySlotsList.get(14).setView(wednesday11amTo1130amView);
        wednesdaySlotsList.get(15).setView(wednesday1130amTo12pmView);
        wednesdaySlotsList.get(16).setView(wednesday12pmTo1230pmView);
        wednesdaySlotsList.get(17).setView(wednesday1230pmTo1pmView);
        wednesdaySlotsList.get(18).setView(wednesday1pmTo130pmView);
        wednesdaySlotsList.get(19).setView(wednesday130pmTo2pmView);
        wednesdaySlotsList.get(20).setView(wednesday2pmTo230pmView);
        wednesdaySlotsList.get(21).setView(wednesday230pmTo3pmView);
        wednesdaySlotsList.get(22).setView(wednesday3pmTo330pmView);
        wednesdaySlotsList.get(23).setView(wednesday330pmTo4pmView);
        wednesdaySlotsList.get(24).setView(wednesday4pmTo430pmView);
        wednesdaySlotsList.get(25).setView(wednesday430pmTo5pmView);
        wednesdaySlotsList.get(26).setView(wednesday5pmTo530pmView);
        wednesdaySlotsList.get(27).setView(wednesday530pmTo6pmView);
        wednesdaySlotsList.get(28).setView(wednesday6pmTo630pmView);
        wednesdaySlotsList.get(29).setView(wednesday630pmTo7pmView);
        wednesdaySlotsList.get(30).setView(wednesday7pmTo730pmView);
        wednesdaySlotsList.get(31).setView(wednesday730pmTo8pmView);
        wednesdaySlotsList.get(32).setView(wednesday8pmTo830pmView);
        wednesdaySlotsList.get(33).setView(wednesday830pmTo9pmView);
        wednesdaySlotsList.get(34).setView(wednesday9pmTo930pmView);
        wednesdaySlotsList.get(35).setView(wednesday930pmTo10pmView);
        wednesdaySlotsList.get(36).setView(wednesday10pmTo1030pmView);
        wednesdaySlotsList.get(37).setView(wednesday1030pmTo11pmView);
        wednesdaySlotsList.get(38).setView(wednesday11pmTo1130pmView);
        wednesdaySlotsList.get(39).setView(wednesday1130pmTo12amView);
        //set thursday
        thursdaySlotsList.get(0).setView(thursday4amTo430amView);
        thursdaySlotsList.get(1).setView(thursday430amTo5amView);
        thursdaySlotsList.get(2).setView(thursday5amTo530amView);
        thursdaySlotsList.get(3).setView(thursday530amTo6amView);
        thursdaySlotsList.get(4).setView(thursday6amTo630amView);
        thursdaySlotsList.get(5).setView(thursday630amTo7amView);
        thursdaySlotsList.get(6).setView(thursday7amTo730amView);
        thursdaySlotsList.get(7).setView(thursday730amTo8amView);
        thursdaySlotsList.get(8).setView(thursday8amTo830amView);
        thursdaySlotsList.get(9).setView(thursday830amTo9amView);
        thursdaySlotsList.get(10).setView(thursday9amTo930amView);
        thursdaySlotsList.get(11).setView(thursday930amTo10amView);
        thursdaySlotsList.get(12).setView(thursday10amTo1030amView);
        thursdaySlotsList.get(13).setView(thursday1030amTo11amView);
        thursdaySlotsList.get(14).setView(thursday11amTo1130amView);
        thursdaySlotsList.get(15).setView(thursday1130amTo12pmView);
        thursdaySlotsList.get(16).setView(thursday12pmTo1230pmView);
        thursdaySlotsList.get(17).setView(thursday1230pmTo1pmView);
        thursdaySlotsList.get(18).setView(thursday1pmTo130pmView);
        thursdaySlotsList.get(19).setView(thursday130pmTo2pmView);
        thursdaySlotsList.get(20).setView(thursday2pmTo230pmView);
        thursdaySlotsList.get(21).setView(thursday230pmTo3pmView);
        thursdaySlotsList.get(22).setView(thursday3pmTo330pmView);
        thursdaySlotsList.get(23).setView(thursday330pmTo4pmView);
        thursdaySlotsList.get(24).setView(thursday4pmTo430pmView);
        thursdaySlotsList.get(25).setView(thursday430pmTo5pmView);
        thursdaySlotsList.get(26).setView(thursday5pmTo530pmView);
        thursdaySlotsList.get(27).setView(thursday530pmTo6pmView);
        thursdaySlotsList.get(28).setView(thursday6pmTo630pmView);
        thursdaySlotsList.get(29).setView(thursday630pmTo7pmView);
        thursdaySlotsList.get(30).setView(thursday7pmTo730pmView);
        thursdaySlotsList.get(31).setView(thursday730pmTo8pmView);
        thursdaySlotsList.get(32).setView(thursday8pmTo830pmView);
        thursdaySlotsList.get(33).setView(thursday830pmTo9pmView);
        thursdaySlotsList.get(34).setView(thursday9pmTo930pmView);
        thursdaySlotsList.get(35).setView(thursday930pmTo10pmView);
        thursdaySlotsList.get(36).setView(thursday10pmTo1030pmView);
        thursdaySlotsList.get(37).setView(thursday1030pmTo11pmView);
        thursdaySlotsList.get(38).setView(thursday11pmTo1130pmView);
        thursdaySlotsList.get(39).setView(thursday1130pmTo12amView);
        //set friday
        fridaySlotsList.get(0).setView(friday4amTo430amView);
        fridaySlotsList.get(1).setView(friday430amTo5amView);
        fridaySlotsList.get(2).setView(friday5amTo530amView);
        fridaySlotsList.get(3).setView(friday530amTo6amView);
        fridaySlotsList.get(4).setView(friday6amTo630amView);
        fridaySlotsList.get(5).setView(friday630amTo7amView);
        fridaySlotsList.get(6).setView(friday7amTo730amView);
        fridaySlotsList.get(7).setView(friday730amTo8amView);
        fridaySlotsList.get(8).setView(friday8amTo830amView);
        fridaySlotsList.get(9).setView(friday830amTo9amView);
        fridaySlotsList.get(10).setView(friday9amTo930amView);
        fridaySlotsList.get(11).setView(friday930amTo10amView);
        fridaySlotsList.get(12).setView(friday10amTo1030amView);
        fridaySlotsList.get(13).setView(friday1030amTo11amView);
        fridaySlotsList.get(14).setView(friday11amTo1130amView);
        fridaySlotsList.get(15).setView(friday1130amTo12pmView);
        fridaySlotsList.get(16).setView(friday12pmTo1230pmView);
        fridaySlotsList.get(17).setView(friday1230pmTo1pmView);
        fridaySlotsList.get(18).setView(friday1pmTo130pmView);
        fridaySlotsList.get(19).setView(friday130pmTo2pmView);
        fridaySlotsList.get(20).setView(friday2pmTo230pmView);
        fridaySlotsList.get(21).setView(friday230pmTo3pmView);
        fridaySlotsList.get(22).setView(friday3pmTo330pmView);
        fridaySlotsList.get(23).setView(friday330pmTo4pmView);
        fridaySlotsList.get(24).setView(friday4pmTo430pmView);
        fridaySlotsList.get(25).setView(friday430pmTo5pmView);
        fridaySlotsList.get(26).setView(friday5pmTo530pmView);
        fridaySlotsList.get(27).setView(friday530pmTo6pmView);
        fridaySlotsList.get(28).setView(friday6pmTo630pmView);
        fridaySlotsList.get(29).setView(friday630pmTo7pmView);
        fridaySlotsList.get(30).setView(friday7pmTo730pmView);
        fridaySlotsList.get(31).setView(friday730pmTo8pmView);
        fridaySlotsList.get(32).setView(friday8pmTo830pmView);
        fridaySlotsList.get(33).setView(friday830pmTo9pmView);
        fridaySlotsList.get(34).setView(friday9pmTo930pmView);
        fridaySlotsList.get(35).setView(friday930pmTo10pmView);
        fridaySlotsList.get(36).setView(friday10pmTo1030pmView);
        fridaySlotsList.get(37).setView(friday1030pmTo11pmView);
        fridaySlotsList.get(38).setView(friday11pmTo1130pmView);
        fridaySlotsList.get(39).setView(friday1130pmTo12amView);
        //set saturday
        saturdaySlotsList.get(0).setView(saturday4amTo430amView);
        saturdaySlotsList.get(1).setView(saturday430amTo5amView);
        saturdaySlotsList.get(2).setView(saturday5amTo530amView);
        saturdaySlotsList.get(3).setView(saturday530amTo6amView);
        saturdaySlotsList.get(4).setView(saturday6amTo630amView);
        saturdaySlotsList.get(5).setView(saturday630amTo7amView);
        saturdaySlotsList.get(6).setView(saturday7amTo730amView);
        saturdaySlotsList.get(7).setView(saturday730amTo8amView);
        saturdaySlotsList.get(8).setView(saturday8amTo830amView);
        saturdaySlotsList.get(9).setView(saturday830amTo9amView);
        saturdaySlotsList.get(10).setView(saturday9amTo930amView);
        saturdaySlotsList.get(11).setView(saturday930amTo10amView);
        saturdaySlotsList.get(12).setView(saturday10amTo1030amView);
        saturdaySlotsList.get(13).setView(saturday1030amTo11amView);
        saturdaySlotsList.get(14).setView(saturday11amTo1130amView);
        saturdaySlotsList.get(15).setView(saturday1130amTo12pmView);
        saturdaySlotsList.get(16).setView(saturday12pmTo1230pmView);
        saturdaySlotsList.get(17).setView(saturday1230pmTo1pmView);
        saturdaySlotsList.get(18).setView(saturday1pmTo130pmView);
        saturdaySlotsList.get(19).setView(saturday130pmTo2pmView);
        saturdaySlotsList.get(20).setView(saturday2pmTo230pmView);
        saturdaySlotsList.get(21).setView(saturday230pmTo3pmView);
        saturdaySlotsList.get(22).setView(saturday3pmTo330pmView);
        saturdaySlotsList.get(23).setView(saturday330pmTo4pmView);
        saturdaySlotsList.get(24).setView(saturday4pmTo430pmView);
        saturdaySlotsList.get(25).setView(saturday430pmTo5pmView);
        saturdaySlotsList.get(26).setView(saturday5pmTo530pmView);
        saturdaySlotsList.get(27).setView(saturday530pmTo6pmView);
        saturdaySlotsList.get(28).setView(saturday6pmTo630pmView);
        saturdaySlotsList.get(29).setView(saturday630pmTo7pmView);
        saturdaySlotsList.get(30).setView(saturday7pmTo730pmView);
        saturdaySlotsList.get(31).setView(saturday730pmTo8pmView);
        saturdaySlotsList.get(32).setView(saturday8pmTo830pmView);
        saturdaySlotsList.get(33).setView(saturday830pmTo9pmView);
        saturdaySlotsList.get(34).setView(saturday9pmTo930pmView);
        saturdaySlotsList.get(35).setView(saturday930pmTo10pmView);
        saturdaySlotsList.get(36).setView(saturday10pmTo1030pmView);
        saturdaySlotsList.get(37).setView(saturday1030pmTo11pmView);
        saturdaySlotsList.get(38).setView(saturday11pmTo1130pmView);
        saturdaySlotsList.get(39).setView(saturday1130pmTo12amView);
        //set sunday
        sundaySlotsList.get(0).setView(sunday4amTo430amView);
        sundaySlotsList.get(1).setView(sunday430amTo5amView);
        sundaySlotsList.get(2).setView(sunday5amTo530amView);
        sundaySlotsList.get(3).setView(sunday530amTo6amView);
        sundaySlotsList.get(4).setView(sunday6amTo630amView);
        sundaySlotsList.get(5).setView(sunday630amTo7amView);
        sundaySlotsList.get(6).setView(sunday7amTo730amView);
        sundaySlotsList.get(7).setView(sunday730amTo8amView);
        sundaySlotsList.get(8).setView(sunday8amTo830amView);
        sundaySlotsList.get(9).setView(sunday830amTo9amView);
        sundaySlotsList.get(10).setView(sunday9amTo930amView);
        sundaySlotsList.get(11).setView(sunday930amTo10amView);
        sundaySlotsList.get(12).setView(sunday10amTo1030amView);
        sundaySlotsList.get(13).setView(sunday1030amTo11amView);
        sundaySlotsList.get(14).setView(sunday11amTo1130amView);
        sundaySlotsList.get(15).setView(sunday1130amTo12pmView);
        sundaySlotsList.get(16).setView(sunday12pmTo1230pmView);
        sundaySlotsList.get(17).setView(sunday1230pmTo1pmView);
        sundaySlotsList.get(18).setView(sunday1pmTo130pmView);
        sundaySlotsList.get(19).setView(sunday130pmTo2pmView);
        sundaySlotsList.get(20).setView(sunday2pmTo230pmView);
        sundaySlotsList.get(21).setView(sunday230pmTo3pmView);
        sundaySlotsList.get(22).setView(sunday3pmTo330pmView);
        sundaySlotsList.get(23).setView(sunday330pmTo4pmView);
        sundaySlotsList.get(24).setView(sunday4pmTo430pmView);
        sundaySlotsList.get(25).setView(sunday430pmTo5pmView);
        sundaySlotsList.get(26).setView(sunday5pmTo530pmView);
        sundaySlotsList.get(27).setView(sunday530pmTo6pmView);
        sundaySlotsList.get(28).setView(sunday6pmTo630pmView);
        sundaySlotsList.get(29).setView(sunday630pmTo7pmView);
        sundaySlotsList.get(30).setView(sunday7pmTo730pmView);
        sundaySlotsList.get(31).setView(sunday730pmTo8pmView);
        sundaySlotsList.get(32).setView(sunday8pmTo830pmView);
        sundaySlotsList.get(33).setView(sunday830pmTo9pmView);
        sundaySlotsList.get(34).setView(sunday9pmTo930pmView);
        sundaySlotsList.get(35).setView(sunday930pmTo10pmView);
        sundaySlotsList.get(36).setView(sunday10pmTo1030pmView);
        sundaySlotsList.get(37).setView(sunday1030pmTo11pmView);
        sundaySlotsList.get(38).setView(sunday11pmTo1130pmView);
        sundaySlotsList.get(39).setView(sunday1130pmTo12amView);

        sundayPTOLayout.setVisibility(View.GONE);
        mondayPTOLayout.setVisibility(View.GONE);
        tuesdayPTOLayout.setVisibility(View.GONE);
        wednesdayPTOLayout.setVisibility(View.GONE);
        thursdayPTOLayout.setVisibility(View.GONE);
        fridayPTOLayout.setVisibility(View.GONE);
        saturdayPTOLayout.setVisibility(View.GONE);
        sundaySlotsParentLL.setVisibility(View.VISIBLE);
        mondaySlotsParentLL.setVisibility(View.VISIBLE);
        tuesdaySlotsParentLL.setVisibility(View.VISIBLE);
        wednesdaySlotsParentLL.setVisibility(View.VISIBLE);
        thursdaySlotsParentLL.setVisibility(View.VISIBLE);
        fridaySlotsParentLL.setVisibility(View.VISIBLE);
        saturdaySlotsParentLL.setVisibility(View.VISIBLE);

        boolean hasSundayPTO = false;
        boolean hasMondayPTO = false;
        boolean hasTuesdayPTO = false;
        boolean hasWednesdayPTO = false;
        boolean hasThursdayPTO = false;
        boolean hasFridayPTO = false;
        boolean hasSaturdayPTO = false;

        for (Slot slot : sundaySlotsList) {
            if (slot.isPTO()) {
                hasSundayPTO = true;
                break;
            }
        }
        if (hasSundayPTO) {
            sundayPTOLayout.setVisibility(View.VISIBLE);
            sundaySlotsParentLL.setVisibility(View.GONE);
        }

        for (Slot slot : mondaySlotsList) {
            if (slot.isPTO()) {
                hasMondayPTO = true;
                break;
            }
        }
        if(hasMondayPTO){
            mondayPTOLayout.setVisibility(View.VISIBLE);
            mondaySlotsParentLL.setVisibility(View.GONE);
        }else{
            mondayPTOLayout.setVisibility(View.GONE);
            mondaySlotsParentLL.setVisibility(View.VISIBLE);
        }

        for (Slot slot : tuesdaySlotsList) {
            if (slot.isPTO()) {
                hasTuesdayPTO = true;
                break;
            }
        }
        if(hasTuesdayPTO){
            tuesdayPTOLayout.setVisibility(View.VISIBLE);
            tuesdaySlotsParentLL.setVisibility(View.GONE);
        }else{
            tuesdayPTOLayout.setVisibility(View.GONE);
            tuesdaySlotsParentLL.setVisibility(View.VISIBLE);
        }

        for (Slot slot : wednesdaySlotsList) {
            if (slot.isPTO()) {
                hasWednesdayPTO = true;
                break;
            }
        }
        if(hasWednesdayPTO){
            wednesdayPTOLayout.setVisibility(View.VISIBLE);
            wednesdaySlotsParentLL.setVisibility(View.GONE);
        }else{
            wednesdayPTOLayout.setVisibility(View.GONE);
            wednesdaySlotsParentLL.setVisibility(View.VISIBLE);
        }

        for (Slot slot : thursdaySlotsList) {
            if (slot.isPTO()) {
                hasThursdayPTO = true;
                break;
            }
        }
        if(hasThursdayPTO){
            thursdayPTOLayout.setVisibility(View.VISIBLE);
            thursdaySlotsParentLL.setVisibility(View.GONE);
        }else{
            thursdayPTOLayout.setVisibility(View.GONE);
            thursdaySlotsParentLL.setVisibility(View.VISIBLE);
        }

        for (Slot slot : fridaySlotsList) {
            if (slot.isPTO()) {
                hasFridayPTO = true;
                break;
            }
        }
        if(hasFridayPTO){
            fridayPTOLayout.setVisibility(View.VISIBLE);
            fridaySlotsParentLL.setVisibility(View.GONE);
        }else{
            fridayPTOLayout.setVisibility(View.GONE);
            fridaySlotsParentLL.setVisibility(View.VISIBLE);
        }

        for (Slot slot : saturdaySlotsList) {
            if (slot.isPTO()) {
                hasSaturdayPTO = true;
                break;
            }
        }
        if(hasSaturdayPTO){
            saturdayPTOLayout.setVisibility(View.VISIBLE);
            saturdaySlotsParentLL.setVisibility(View.GONE);
        }else{
            saturdayPTOLayout.setVisibility(View.GONE);
            saturdaySlotsParentLL.setVisibility(View.VISIBLE);
        }

        doUpdateSlotsRowUI(mondaySlotsList);
        doUpdateSlotsRowUI(tuesdaySlotsList);
        doUpdateSlotsRowUI(wednesdaySlotsList);
        doUpdateSlotsRowUI(thursdaySlotsList);
        doUpdateSlotsRowUI(fridaySlotsList);
        doUpdateSlotsRowUI(saturdaySlotsList);
        doUpdateSlotsRowUI(sundaySlotsList);
    }

    private void doDisplaySlotsStatuses(ArrayList<Slot> slotsList) {
        for (Slot slot : slotsList) {
            Log.v("SLOT" + slot.getSlotSerialNumber(), slot.getSlotType().getType() + ", weight = " + slot.getWeight() + ", startTime = " + slot.getActualStartTimeOfSlot() + ", endTime = " + slot.getActualEndTimeOfSlot());
        }
        Log.v("-----------", "--------------------------");
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentLayout = (LinearLayout) view.findViewById(R.id.parentLayout);
        LegionUtils.doApplyFont(getActivity().getAssets(), parentLayout);
        tutorialTextView = (TextView) view.findViewById(R.id.tutorialTextView);
        sliderViewMonday = (LinearLayout) view.findViewById(R.id.sliderViewMonday);
        sliderViewTuesday = (LinearLayout) view.findViewById(R.id.sliderViewTuesday);
        sliderViewWednesday = (LinearLayout) view.findViewById(R.id.sliderViewWednesday);
        sliderViewThursday = (LinearLayout) view.findViewById(R.id.sliderViewThursday);
        sliderViewFriday = (LinearLayout) view.findViewById(R.id.sliderViewFriday);
        sliderViewSaturday = (LinearLayout) view.findViewById(R.id.sliderViewSaturday);
        sliderViewSunday = (LinearLayout) view.findViewById(R.id.sliderViewSunday);
        numOfAvailableHrsTV = (TextView) view.findViewById(R.id.numOfAvailableHrsTV);
        totalNumOfHoursTV = (TextView) view.findViewById(R.id.totalNumOfHoursTV);
        int hoursPerWeek = Integer.parseInt(legionPreferences.get(Prefs_Keys.HOURS_PER_WEEK, "40"));
        totalNumOfHoursTV.setText("" + hoursPerWeek);
        availabilityModeLineView = view.findViewById(R.id.availabilityModeLineView);
        dottedProgressbarContainer = (LinearLayout) view.findViewById(R.id.dottedProgressbarContainer);
        initViews(view);
    }

    private void initViews(View view) {
        gestureScanner = new GestureDetector(getActivity(), this);

        //Identifying the view of MONDAY
        monday4amTo430amView = (LinearLayout) view.findViewById(R.id.monday4amTo430am);
        monday430amTo5amView = (LinearLayout) view.findViewById(R.id.monday430amTo5am);
        monday5amTo530amView = (LinearLayout) view.findViewById(R.id.monday5amTo530am);
        monday530amTo6amView = (LinearLayout) view.findViewById(R.id.monday530amTo6am);
        monday6amTo630amView = (LinearLayout) view.findViewById(R.id.monday6amTo630am);
        monday630amTo7amView = (LinearLayout) view.findViewById(R.id.monday630amTo7am);
        monday7amTo730amView = (LinearLayout) view.findViewById(R.id.monday7amTo730am);
        monday730amTo8amView = (LinearLayout) view.findViewById(R.id.monday730amTo8am);
        monday8amTo830amView = (LinearLayout) view.findViewById(R.id.monday8amTo830am);
        monday830amTo9amView = (LinearLayout) view.findViewById(R.id.monday830amTo9am);
        monday9amTo930amView = (LinearLayout) view.findViewById(R.id.monday9amTo930am);
        monday930amTo10amView = (LinearLayout) view.findViewById(R.id.monday930amTo10am);
        monday10amTo1030amView = (LinearLayout) view.findViewById(R.id.monday10amTo1030am);
        monday1030amTo11amView = (LinearLayout) view.findViewById(R.id.monday1030amTo11am);
        monday11amTo1130amView = (LinearLayout) view.findViewById(R.id.monday11amTo1130am);
        monday1130amTo12pmView = (LinearLayout) view.findViewById(R.id.monday1130amTo12pm);
        monday12pmTo1230pmView = (LinearLayout) view.findViewById(R.id.monday12pmTo1230pm);
        monday1230pmTo1pmView = (LinearLayout) view.findViewById(R.id.monday1230pmTo1pm);
        monday1pmTo130pmView = (LinearLayout) view.findViewById(R.id.monday1pmTo130pm);
        monday130pmTo2pmView = (LinearLayout) view.findViewById(R.id.monday130pmTo2pm);
        monday2pmTo230pmView = (LinearLayout) view.findViewById(R.id.monday2pmTo230pm);
        monday230pmTo3pmView = (LinearLayout) view.findViewById(R.id.monday230pmTo3pm);
        monday3pmTo330pmView = (LinearLayout) view.findViewById(R.id.monday3pmTo330pm);
        monday330pmTo4pmView = (LinearLayout) view.findViewById(R.id.monday330pmTo4pm);
        monday4pmTo430pmView = (LinearLayout) view.findViewById(R.id.monday4pmTo430pm);
        monday430pmTo5pmView = (LinearLayout) view.findViewById(R.id.monday430pmTo5pm);
        monday5pmTo530pmView = (LinearLayout) view.findViewById(R.id.monday5pmTo530pm);
        monday530pmTo6pmView = (LinearLayout) view.findViewById(R.id.monday530pmTo6pm);
        monday6pmTo630pmView = (LinearLayout) view.findViewById(R.id.monday6pmTo630pm);
        monday630pmTo7pmView = (LinearLayout) view.findViewById(R.id.monday630pmTo7pm);
        monday7pmTo730pmView = (LinearLayout) view.findViewById(R.id.monday7pmTo730pm);
        monday730pmTo8pmView = (LinearLayout) view.findViewById(R.id.monday730pmTo8pm);
        monday8pmTo830pmView = (LinearLayout) view.findViewById(R.id.monday8pmTo830pm);
        monday830pmTo9pmView = (LinearLayout) view.findViewById(R.id.monday830pmTo9pm);
        monday9pmTo930pmView = (LinearLayout) view.findViewById(R.id.monday9pmTo930pm);
        monday930pmTo10pmView = (LinearLayout) view.findViewById(R.id.monday930pmTo10pm);
        monday10pmTo1030pmView = (LinearLayout) view.findViewById(R.id.monday10pmTo1030pm);
        monday1030pmTo11pmView = (LinearLayout) view.findViewById(R.id.monday1030pmTo11pm);
        monday11pmTo1130pmView = (LinearLayout) view.findViewById(R.id.monday11pmTo1130pm);
        monday1130pmTo12amView = (LinearLayout) view.findViewById(R.id.monday1130pmTo12am);

        monday4amTo430amView.setOnTouchListener(this);
        monday430amTo5amView.setOnTouchListener(this);
        monday5amTo530amView.setOnTouchListener(this);
        monday530amTo6amView.setOnTouchListener(this);
        monday6amTo630amView.setOnTouchListener(this);
        monday630amTo7amView.setOnTouchListener(this);
        monday7amTo730amView.setOnTouchListener(this);
        monday730amTo8amView.setOnTouchListener(this);
        monday8amTo830amView.setOnTouchListener(this);
        monday830amTo9amView.setOnTouchListener(this);
        monday9amTo930amView.setOnTouchListener(this);
        monday930amTo10amView.setOnTouchListener(this);
        monday10amTo1030amView.setOnTouchListener(this);
        monday1030amTo11amView.setOnTouchListener(this);
        monday11amTo1130amView.setOnTouchListener(this);
        monday1130amTo12pmView.setOnTouchListener(this);
        monday12pmTo1230pmView.setOnTouchListener(this);
        monday1230pmTo1pmView.setOnTouchListener(this);
        monday1pmTo130pmView.setOnTouchListener(this);
        monday130pmTo2pmView.setOnTouchListener(this);
        monday2pmTo230pmView.setOnTouchListener(this);
        monday230pmTo3pmView.setOnTouchListener(this);
        monday3pmTo330pmView.setOnTouchListener(this);
        monday330pmTo4pmView.setOnTouchListener(this);
        monday4pmTo430pmView.setOnTouchListener(this);
        monday430pmTo5pmView.setOnTouchListener(this);
        monday5pmTo530pmView.setOnTouchListener(this);
        monday530pmTo6pmView.setOnTouchListener(this);
        monday6pmTo630pmView.setOnTouchListener(this);
        monday630pmTo7pmView.setOnTouchListener(this);
        monday7pmTo730pmView.setOnTouchListener(this);
        monday730pmTo8pmView.setOnTouchListener(this);
        monday8pmTo830pmView.setOnTouchListener(this);
        monday830pmTo9pmView.setOnTouchListener(this);
        monday9pmTo930pmView.setOnTouchListener(this);
        monday930pmTo10pmView.setOnTouchListener(this);
        monday10pmTo1030pmView.setOnTouchListener(this);
        monday1030pmTo11pmView.setOnTouchListener(this);
        monday11pmTo1130pmView.setOnTouchListener(this);
        monday1130pmTo12amView.setOnTouchListener(this);

        //Identifying the view of TUESDAY
        tuesday4amTo430amView = (LinearLayout) view.findViewById(R.id.tuesday4amTo430am);
        tuesday430amTo5amView = (LinearLayout) view.findViewById(R.id.tuesday430amTo5am);
        tuesday5amTo530amView = (LinearLayout) view.findViewById(R.id.tuesday5amTo530am);
        tuesday530amTo6amView = (LinearLayout) view.findViewById(R.id.tuesday530amTo6am);
        tuesday6amTo630amView = (LinearLayout) view.findViewById(R.id.tuesday6amTo630am);
        tuesday630amTo7amView = (LinearLayout) view.findViewById(R.id.tuesday630amTo7am);
        tuesday7amTo730amView = (LinearLayout) view.findViewById(R.id.tuesday7amTo730am);
        tuesday730amTo8amView = (LinearLayout) view.findViewById(R.id.tuesday730amTo8am);
        tuesday8amTo830amView = (LinearLayout) view.findViewById(R.id.tuesday8amTo830am);
        tuesday830amTo9amView = (LinearLayout) view.findViewById(R.id.tuesday830amTo9am);
        tuesday9amTo930amView = (LinearLayout) view.findViewById(R.id.tuesday9amTo930am);
        tuesday930amTo10amView = (LinearLayout) view.findViewById(R.id.tuesday930amTo10am);
        tuesday10amTo1030amView = (LinearLayout) view.findViewById(R.id.tuesday10amTo1030am);
        tuesday1030amTo11amView = (LinearLayout) view.findViewById(R.id.tuesday1030amTo11am);
        tuesday11amTo1130amView = (LinearLayout) view.findViewById(R.id.tuesday11amTo1130am);
        tuesday1130amTo12pmView = (LinearLayout) view.findViewById(R.id.tuesday1130amTo12pm);
        tuesday12pmTo1230pmView = (LinearLayout) view.findViewById(R.id.tuesday12pmTo1230pm);
        tuesday1230pmTo1pmView = (LinearLayout) view.findViewById(R.id.tuesday1230pmTo1pm);
        tuesday1pmTo130pmView = (LinearLayout) view.findViewById(R.id.tuesday1pmTo130pm);
        tuesday130pmTo2pmView = (LinearLayout) view.findViewById(R.id.tuesday130pmTo2pm);
        tuesday2pmTo230pmView = (LinearLayout) view.findViewById(R.id.tuesday2pmTo230pm);
        tuesday230pmTo3pmView = (LinearLayout) view.findViewById(R.id.tuesday230pmTo3pm);
        tuesday3pmTo330pmView = (LinearLayout) view.findViewById(R.id.tuesday3pmTo330pm);
        tuesday330pmTo4pmView = (LinearLayout) view.findViewById(R.id.tuesday330pmTo4pm);
        tuesday4pmTo430pmView = (LinearLayout) view.findViewById(R.id.tuesday4pmTo430pm);
        tuesday430pmTo5pmView = (LinearLayout) view.findViewById(R.id.tuesday430pmTo5pm);
        tuesday5pmTo530pmView = (LinearLayout) view.findViewById(R.id.tuesday5pmTo530pm);
        tuesday530pmTo6pmView = (LinearLayout) view.findViewById(R.id.tuesday530pmTo6pm);
        tuesday6pmTo630pmView = (LinearLayout) view.findViewById(R.id.tuesday6pmTo630pm);
        tuesday630pmTo7pmView = (LinearLayout) view.findViewById(R.id.tuesday630pmTo7pm);
        tuesday7pmTo730pmView = (LinearLayout) view.findViewById(R.id.tuesday7pmTo730pm);
        tuesday730pmTo8pmView = (LinearLayout) view.findViewById(R.id.tuesday730pmTo8pm);
        tuesday8pmTo830pmView = (LinearLayout) view.findViewById(R.id.tuesday8pmTo830pm);
        tuesday830pmTo9pmView = (LinearLayout) view.findViewById(R.id.tuesday830pmTo9pm);
        tuesday9pmTo930pmView = (LinearLayout) view.findViewById(R.id.tuesday9pmTo930pm);
        tuesday930pmTo10pmView = (LinearLayout) view.findViewById(R.id.tuesday930pmTo10pm);
        tuesday10pmTo1030pmView = (LinearLayout) view.findViewById(R.id.tuesday10pmTo1030pm);
        tuesday1030pmTo11pmView = (LinearLayout) view.findViewById(R.id.tuesday1030pmTo11pm);
        tuesday11pmTo1130pmView = (LinearLayout) view.findViewById(R.id.tuesday11pmTo1130pm);
        tuesday1130pmTo12amView = (LinearLayout) view.findViewById(R.id.tuesday1130pmTo12am);

        tuesday4amTo430amView.setOnTouchListener(this);
        tuesday430amTo5amView.setOnTouchListener(this);
        tuesday5amTo530amView.setOnTouchListener(this);
        tuesday530amTo6amView.setOnTouchListener(this);
        tuesday6amTo630amView.setOnTouchListener(this);
        tuesday630amTo7amView.setOnTouchListener(this);
        tuesday7amTo730amView.setOnTouchListener(this);
        tuesday730amTo8amView.setOnTouchListener(this);
        tuesday8amTo830amView.setOnTouchListener(this);
        tuesday830amTo9amView.setOnTouchListener(this);
        tuesday9amTo930amView.setOnTouchListener(this);
        tuesday930amTo10amView.setOnTouchListener(this);
        tuesday10amTo1030amView.setOnTouchListener(this);
        tuesday1030amTo11amView.setOnTouchListener(this);
        tuesday11amTo1130amView.setOnTouchListener(this);
        tuesday1130amTo12pmView.setOnTouchListener(this);
        tuesday12pmTo1230pmView.setOnTouchListener(this);
        tuesday1230pmTo1pmView.setOnTouchListener(this);
        tuesday1pmTo130pmView.setOnTouchListener(this);
        tuesday130pmTo2pmView.setOnTouchListener(this);
        tuesday2pmTo230pmView.setOnTouchListener(this);
        tuesday230pmTo3pmView.setOnTouchListener(this);
        tuesday3pmTo330pmView.setOnTouchListener(this);
        tuesday330pmTo4pmView.setOnTouchListener(this);
        tuesday4pmTo430pmView.setOnTouchListener(this);
        tuesday430pmTo5pmView.setOnTouchListener(this);
        tuesday5pmTo530pmView.setOnTouchListener(this);
        tuesday530pmTo6pmView.setOnTouchListener(this);
        tuesday6pmTo630pmView.setOnTouchListener(this);
        tuesday630pmTo7pmView.setOnTouchListener(this);
        tuesday7pmTo730pmView.setOnTouchListener(this);
        tuesday730pmTo8pmView.setOnTouchListener(this);
        tuesday8pmTo830pmView.setOnTouchListener(this);
        tuesday830pmTo9pmView.setOnTouchListener(this);
        tuesday9pmTo930pmView.setOnTouchListener(this);
        tuesday930pmTo10pmView.setOnTouchListener(this);
        tuesday10pmTo1030pmView.setOnTouchListener(this);
        tuesday1030pmTo11pmView.setOnTouchListener(this);
        tuesday11pmTo1130pmView.setOnTouchListener(this);
        tuesday1130pmTo12amView.setOnTouchListener(this);


        //Identifying the views of WEDNESDAY
        wednesday4amTo430amView = (LinearLayout) view.findViewById(R.id.wednesday4amTo430am);
        wednesday430amTo5amView = (LinearLayout) view.findViewById(R.id.wednesday430amTo5am);
        wednesday5amTo530amView = (LinearLayout) view.findViewById(R.id.wednesday5amTo530am);
        wednesday530amTo6amView = (LinearLayout) view.findViewById(R.id.wednesday530amTo6am);
        wednesday6amTo630amView = (LinearLayout) view.findViewById(R.id.wednesday6amTo630am);
        wednesday630amTo7amView = (LinearLayout) view.findViewById(R.id.wednesday630amTo7am);
        wednesday7amTo730amView = (LinearLayout) view.findViewById(R.id.wednesday7amTo730am);
        wednesday730amTo8amView = (LinearLayout) view.findViewById(R.id.wednesday730amTo8am);
        wednesday8amTo830amView = (LinearLayout) view.findViewById(R.id.wednesday8amTo830am);
        wednesday830amTo9amView = (LinearLayout) view.findViewById(R.id.wednesday830amTo9am);
        wednesday9amTo930amView = (LinearLayout) view.findViewById(R.id.wednesday9amTo930am);
        wednesday930amTo10amView = (LinearLayout) view.findViewById(R.id.wednesday930amTo10am);
        wednesday10amTo1030amView = (LinearLayout) view.findViewById(R.id.wednesday10amTo1030am);
        wednesday1030amTo11amView = (LinearLayout) view.findViewById(R.id.wednesday1030amTo11am);
        wednesday11amTo1130amView = (LinearLayout) view.findViewById(R.id.wednesday11amTo1130am);
        wednesday1130amTo12pmView = (LinearLayout) view.findViewById(R.id.wednesday1130amTo12pm);
        wednesday12pmTo1230pmView = (LinearLayout) view.findViewById(R.id.wednesday12pmTo1230pm);
        wednesday1230pmTo1pmView = (LinearLayout) view.findViewById(R.id.wednesday1230pmTo1pm);
        wednesday1pmTo130pmView = (LinearLayout) view.findViewById(R.id.wednesday1pmTo130pm);
        wednesday130pmTo2pmView = (LinearLayout) view.findViewById(R.id.wednesday130pmTo2pm);
        wednesday2pmTo230pmView = (LinearLayout) view.findViewById(R.id.wednesday2pmTo230pm);
        wednesday230pmTo3pmView = (LinearLayout) view.findViewById(R.id.wednesday230pmTo3pm);
        wednesday3pmTo330pmView = (LinearLayout) view.findViewById(R.id.wednesday3pmTo330pm);
        wednesday330pmTo4pmView = (LinearLayout) view.findViewById(R.id.wednesday330pmTo4pm);
        wednesday4pmTo430pmView = (LinearLayout) view.findViewById(R.id.wednesday4pmTo430pm);
        wednesday430pmTo5pmView = (LinearLayout) view.findViewById(R.id.wednesday430pmTo5pm);
        wednesday5pmTo530pmView = (LinearLayout) view.findViewById(R.id.wednesday5pmTo530pm);
        wednesday530pmTo6pmView = (LinearLayout) view.findViewById(R.id.wednesday530pmTo6pm);
        wednesday6pmTo630pmView = (LinearLayout) view.findViewById(R.id.wednesday6pmTo630pm);
        wednesday630pmTo7pmView = (LinearLayout) view.findViewById(R.id.wednesday630pmTo7pm);
        wednesday7pmTo730pmView = (LinearLayout) view.findViewById(R.id.wednesday7pmTo730pm);
        wednesday730pmTo8pmView = (LinearLayout) view.findViewById(R.id.wednesday730pmTo8pm);
        wednesday8pmTo830pmView = (LinearLayout) view.findViewById(R.id.wednesday8pmTo830pm);
        wednesday830pmTo9pmView = (LinearLayout) view.findViewById(R.id.wednesday830pmTo9pm);
        wednesday9pmTo930pmView = (LinearLayout) view.findViewById(R.id.wednesday9pmTo930pm);
        wednesday930pmTo10pmView = (LinearLayout) view.findViewById(R.id.wednesday930pmTo10pm);
        wednesday10pmTo1030pmView = (LinearLayout) view.findViewById(R.id.wednesday10pmTo1030pm);
        wednesday1030pmTo11pmView = (LinearLayout) view.findViewById(R.id.wednesday1030pmTo11pm);
        wednesday11pmTo1130pmView = (LinearLayout) view.findViewById(R.id.wednesday11pmTo1130pm);
        wednesday1130pmTo12amView = (LinearLayout) view.findViewById(R.id.wednesday1130pmTo12am);

        wednesday4amTo430amView.setOnTouchListener(this);
        wednesday430amTo5amView.setOnTouchListener(this);
        wednesday5amTo530amView.setOnTouchListener(this);
        wednesday530amTo6amView.setOnTouchListener(this);
        wednesday6amTo630amView.setOnTouchListener(this);
        wednesday630amTo7amView.setOnTouchListener(this);
        wednesday7amTo730amView.setOnTouchListener(this);
        wednesday730amTo8amView.setOnTouchListener(this);
        wednesday8amTo830amView.setOnTouchListener(this);
        wednesday830amTo9amView.setOnTouchListener(this);
        wednesday9amTo930amView.setOnTouchListener(this);
        wednesday930amTo10amView.setOnTouchListener(this);
        wednesday10amTo1030amView.setOnTouchListener(this);
        wednesday1030amTo11amView.setOnTouchListener(this);
        wednesday11amTo1130amView.setOnTouchListener(this);
        wednesday1130amTo12pmView.setOnTouchListener(this);
        wednesday12pmTo1230pmView.setOnTouchListener(this);
        wednesday1230pmTo1pmView.setOnTouchListener(this);
        wednesday1pmTo130pmView.setOnTouchListener(this);
        wednesday130pmTo2pmView.setOnTouchListener(this);
        wednesday2pmTo230pmView.setOnTouchListener(this);
        wednesday230pmTo3pmView.setOnTouchListener(this);
        wednesday3pmTo330pmView.setOnTouchListener(this);
        wednesday330pmTo4pmView.setOnTouchListener(this);
        wednesday4pmTo430pmView.setOnTouchListener(this);
        wednesday430pmTo5pmView.setOnTouchListener(this);
        wednesday5pmTo530pmView.setOnTouchListener(this);
        wednesday530pmTo6pmView.setOnTouchListener(this);
        wednesday6pmTo630pmView.setOnTouchListener(this);
        wednesday630pmTo7pmView.setOnTouchListener(this);
        wednesday7pmTo730pmView.setOnTouchListener(this);
        wednesday730pmTo8pmView.setOnTouchListener(this);
        wednesday8pmTo830pmView.setOnTouchListener(this);
        wednesday830pmTo9pmView.setOnTouchListener(this);
        wednesday9pmTo930pmView.setOnTouchListener(this);
        wednesday930pmTo10pmView.setOnTouchListener(this);
        wednesday10pmTo1030pmView.setOnTouchListener(this);
        wednesday1030pmTo11pmView.setOnTouchListener(this);
        wednesday11pmTo1130pmView.setOnTouchListener(this);
        wednesday1130pmTo12amView.setOnTouchListener(this);

        //Identifying the views of THURSDAY
        thursday4amTo430amView = (LinearLayout) view.findViewById(R.id.thursday4amTo430am);
        thursday430amTo5amView = (LinearLayout) view.findViewById(R.id.thursday430amTo5am);
        thursday5amTo530amView = (LinearLayout) view.findViewById(R.id.thursday5amTo530am);
        thursday530amTo6amView = (LinearLayout) view.findViewById(R.id.thursday530amTo6am);
        thursday6amTo630amView = (LinearLayout) view.findViewById(R.id.thursday6amTo630am);
        thursday630amTo7amView = (LinearLayout) view.findViewById(R.id.thursday630amTo7am);
        thursday7amTo730amView = (LinearLayout) view.findViewById(R.id.thursday7amTo730am);
        thursday730amTo8amView = (LinearLayout) view.findViewById(R.id.thursday730amTo8am);
        thursday8amTo830amView = (LinearLayout) view.findViewById(R.id.thursday8amTo830am);
        thursday830amTo9amView = (LinearLayout) view.findViewById(R.id.thursday830amTo9am);
        thursday9amTo930amView = (LinearLayout) view.findViewById(R.id.thursday9amTo930am);
        thursday930amTo10amView = (LinearLayout) view.findViewById(R.id.thursday930amTo10am);
        thursday10amTo1030amView = (LinearLayout) view.findViewById(R.id.thursday10amTo1030am);
        thursday1030amTo11amView = (LinearLayout) view.findViewById(R.id.thursday1030amTo11am);
        thursday11amTo1130amView = (LinearLayout) view.findViewById(R.id.thursday11amTo1130am);
        thursday1130amTo12pmView = (LinearLayout) view.findViewById(R.id.thursday1130amTo12pm);
        thursday12pmTo1230pmView = (LinearLayout) view.findViewById(R.id.thursday12pmTo1230pm);
        thursday1230pmTo1pmView = (LinearLayout) view.findViewById(R.id.thursday1230pmTo1pm);
        thursday1pmTo130pmView = (LinearLayout) view.findViewById(R.id.thursday1pmTo130pm);
        thursday130pmTo2pmView = (LinearLayout) view.findViewById(R.id.thursday130pmTo2pm);
        thursday2pmTo230pmView = (LinearLayout) view.findViewById(R.id.thursday2pmTo230pm);
        thursday230pmTo3pmView = (LinearLayout) view.findViewById(R.id.thursday230pmTo3pm);
        thursday3pmTo330pmView = (LinearLayout) view.findViewById(R.id.thursday3pmTo330pm);
        thursday330pmTo4pmView = (LinearLayout) view.findViewById(R.id.thursday330pmTo4pm);
        thursday4pmTo430pmView = (LinearLayout) view.findViewById(R.id.thursday4pmTo430pm);
        thursday430pmTo5pmView = (LinearLayout) view.findViewById(R.id.thursday430pmTo5pm);
        thursday5pmTo530pmView = (LinearLayout) view.findViewById(R.id.thursday5pmTo530pm);
        thursday530pmTo6pmView = (LinearLayout) view.findViewById(R.id.thursday530pmTo6pm);
        thursday6pmTo630pmView = (LinearLayout) view.findViewById(R.id.thursday6pmTo630pm);
        thursday630pmTo7pmView = (LinearLayout) view.findViewById(R.id.thursday630pmTo7pm);
        thursday7pmTo730pmView = (LinearLayout) view.findViewById(R.id.thursday7pmTo730pm);
        thursday730pmTo8pmView = (LinearLayout) view.findViewById(R.id.thursday730pmTo8pm);
        thursday8pmTo830pmView = (LinearLayout) view.findViewById(R.id.thursday8pmTo830pm);
        thursday830pmTo9pmView = (LinearLayout) view.findViewById(R.id.thursday830pmTo9pm);
        thursday9pmTo930pmView = (LinearLayout) view.findViewById(R.id.thursday9pmTo930pm);
        thursday930pmTo10pmView = (LinearLayout) view.findViewById(R.id.thursday930pmTo10pm);
        thursday10pmTo1030pmView = (LinearLayout) view.findViewById(R.id.thursday10pmTo1030pm);
        thursday1030pmTo11pmView = (LinearLayout) view.findViewById(R.id.thursday1030pmTo11pm);
        thursday11pmTo1130pmView = (LinearLayout) view.findViewById(R.id.thursday11pmTo1130pm);
        thursday1130pmTo12amView = (LinearLayout) view.findViewById(R.id.thursday1130pmTo12am);

        thursday4amTo430amView.setOnTouchListener(this);
        thursday430amTo5amView.setOnTouchListener(this);
        thursday5amTo530amView.setOnTouchListener(this);
        thursday530amTo6amView.setOnTouchListener(this);
        thursday6amTo630amView.setOnTouchListener(this);
        thursday630amTo7amView.setOnTouchListener(this);
        thursday7amTo730amView.setOnTouchListener(this);
        thursday730amTo8amView.setOnTouchListener(this);
        thursday8amTo830amView.setOnTouchListener(this);
        thursday830amTo9amView.setOnTouchListener(this);
        thursday9amTo930amView.setOnTouchListener(this);
        thursday930amTo10amView.setOnTouchListener(this);
        thursday10amTo1030amView.setOnTouchListener(this);
        thursday1030amTo11amView.setOnTouchListener(this);
        thursday11amTo1130amView.setOnTouchListener(this);
        thursday1130amTo12pmView.setOnTouchListener(this);
        thursday12pmTo1230pmView.setOnTouchListener(this);
        thursday1230pmTo1pmView.setOnTouchListener(this);
        thursday1pmTo130pmView.setOnTouchListener(this);
        thursday130pmTo2pmView.setOnTouchListener(this);
        thursday2pmTo230pmView.setOnTouchListener(this);
        thursday230pmTo3pmView.setOnTouchListener(this);
        thursday3pmTo330pmView.setOnTouchListener(this);
        thursday330pmTo4pmView.setOnTouchListener(this);
        thursday4pmTo430pmView.setOnTouchListener(this);
        thursday430pmTo5pmView.setOnTouchListener(this);
        thursday5pmTo530pmView.setOnTouchListener(this);
        thursday530pmTo6pmView.setOnTouchListener(this);
        thursday6pmTo630pmView.setOnTouchListener(this);
        thursday630pmTo7pmView.setOnTouchListener(this);
        thursday7pmTo730pmView.setOnTouchListener(this);
        thursday730pmTo8pmView.setOnTouchListener(this);
        thursday8pmTo830pmView.setOnTouchListener(this);
        thursday830pmTo9pmView.setOnTouchListener(this);
        thursday9pmTo930pmView.setOnTouchListener(this);
        thursday930pmTo10pmView.setOnTouchListener(this);
        thursday10pmTo1030pmView.setOnTouchListener(this);
        thursday1030pmTo11pmView.setOnTouchListener(this);
        thursday11pmTo1130pmView.setOnTouchListener(this);
        thursday1130pmTo12amView.setOnTouchListener(this);

        //Identifying the views of FRIDAY
        friday4amTo430amView = (LinearLayout) view.findViewById(R.id.friday4amTo430am);
        friday430amTo5amView = (LinearLayout) view.findViewById(R.id.friday430amTo5am);
        friday5amTo530amView = (LinearLayout) view.findViewById(R.id.friday5amTo530am);
        friday530amTo6amView = (LinearLayout) view.findViewById(R.id.friday530amTo6am);
        friday6amTo630amView = (LinearLayout) view.findViewById(R.id.friday6amTo630am);
        friday630amTo7amView = (LinearLayout) view.findViewById(R.id.friday630amTo7am);
        friday7amTo730amView = (LinearLayout) view.findViewById(R.id.friday7amTo730am);
        friday730amTo8amView = (LinearLayout) view.findViewById(R.id.friday730amTo8am);
        friday8amTo830amView = (LinearLayout) view.findViewById(R.id.friday8amTo830am);
        friday830amTo9amView = (LinearLayout) view.findViewById(R.id.friday830amTo9am);
        friday9amTo930amView = (LinearLayout) view.findViewById(R.id.friday9amTo930am);
        friday930amTo10amView = (LinearLayout) view.findViewById(R.id.friday930amTo10am);
        friday10amTo1030amView = (LinearLayout) view.findViewById(R.id.friday10amTo1030am);
        friday1030amTo11amView = (LinearLayout) view.findViewById(R.id.friday1030amTo11am);
        friday11amTo1130amView = (LinearLayout) view.findViewById(R.id.friday11amTo1130am);
        friday1130amTo12pmView = (LinearLayout) view.findViewById(R.id.friday1130amTo12pm);
        friday12pmTo1230pmView = (LinearLayout) view.findViewById(R.id.friday12pmTo1230pm);
        friday1230pmTo1pmView = (LinearLayout) view.findViewById(R.id.friday1230pmTo1pm);
        friday1pmTo130pmView = (LinearLayout) view.findViewById(R.id.friday1pmTo130pm);
        friday130pmTo2pmView = (LinearLayout) view.findViewById(R.id.friday130pmTo2pm);
        friday2pmTo230pmView = (LinearLayout) view.findViewById(R.id.friday2pmTo230pm);
        friday230pmTo3pmView = (LinearLayout) view.findViewById(R.id.friday230pmTo3pm);
        friday3pmTo330pmView = (LinearLayout) view.findViewById(R.id.friday3pmTo330pm);
        friday330pmTo4pmView = (LinearLayout) view.findViewById(R.id.friday330pmTo4pm);
        friday4pmTo430pmView = (LinearLayout) view.findViewById(R.id.friday4pmTo430pm);
        friday430pmTo5pmView = (LinearLayout) view.findViewById(R.id.friday430pmTo5pm);
        friday5pmTo530pmView = (LinearLayout) view.findViewById(R.id.friday5pmTo530pm);
        friday530pmTo6pmView = (LinearLayout) view.findViewById(R.id.friday530pmTo6pm);
        friday6pmTo630pmView = (LinearLayout) view.findViewById(R.id.friday6pmTo630pm);
        friday630pmTo7pmView = (LinearLayout) view.findViewById(R.id.friday630pmTo7pm);
        friday7pmTo730pmView = (LinearLayout) view.findViewById(R.id.friday7pmTo730pm);
        friday730pmTo8pmView = (LinearLayout) view.findViewById(R.id.friday730pmTo8pm);
        friday8pmTo830pmView = (LinearLayout) view.findViewById(R.id.friday8pmTo830pm);
        friday830pmTo9pmView = (LinearLayout) view.findViewById(R.id.friday830pmTo9pm);
        friday9pmTo930pmView = (LinearLayout) view.findViewById(R.id.friday9pmTo930pm);
        friday930pmTo10pmView = (LinearLayout) view.findViewById(R.id.friday930pmTo10pm);
        friday10pmTo1030pmView = (LinearLayout) view.findViewById(R.id.friday10pmTo1030pm);
        friday1030pmTo11pmView = (LinearLayout) view.findViewById(R.id.friday1030pmTo11pm);
        friday11pmTo1130pmView = (LinearLayout) view.findViewById(R.id.friday11pmTo1130pm);
        friday1130pmTo12amView = (LinearLayout) view.findViewById(R.id.friday1130pmTo12am);

        friday4amTo430amView.setOnTouchListener(this);
        friday430amTo5amView.setOnTouchListener(this);
        friday5amTo530amView.setOnTouchListener(this);
        friday530amTo6amView.setOnTouchListener(this);
        friday6amTo630amView.setOnTouchListener(this);
        friday630amTo7amView.setOnTouchListener(this);
        friday7amTo730amView.setOnTouchListener(this);
        friday730amTo8amView.setOnTouchListener(this);
        friday8amTo830amView.setOnTouchListener(this);
        friday830amTo9amView.setOnTouchListener(this);
        friday9amTo930amView.setOnTouchListener(this);
        friday930amTo10amView.setOnTouchListener(this);
        friday10amTo1030amView.setOnTouchListener(this);
        friday1030amTo11amView.setOnTouchListener(this);
        friday11amTo1130amView.setOnTouchListener(this);
        friday1130amTo12pmView.setOnTouchListener(this);
        friday12pmTo1230pmView.setOnTouchListener(this);
        friday1230pmTo1pmView.setOnTouchListener(this);
        friday1pmTo130pmView.setOnTouchListener(this);
        friday130pmTo2pmView.setOnTouchListener(this);
        friday2pmTo230pmView.setOnTouchListener(this);
        friday230pmTo3pmView.setOnTouchListener(this);
        friday3pmTo330pmView.setOnTouchListener(this);
        friday330pmTo4pmView.setOnTouchListener(this);
        friday4pmTo430pmView.setOnTouchListener(this);
        friday430pmTo5pmView.setOnTouchListener(this);
        friday5pmTo530pmView.setOnTouchListener(this);
        friday530pmTo6pmView.setOnTouchListener(this);
        friday6pmTo630pmView.setOnTouchListener(this);
        friday630pmTo7pmView.setOnTouchListener(this);
        friday7pmTo730pmView.setOnTouchListener(this);
        friday730pmTo8pmView.setOnTouchListener(this);
        friday8pmTo830pmView.setOnTouchListener(this);
        friday830pmTo9pmView.setOnTouchListener(this);
        friday9pmTo930pmView.setOnTouchListener(this);
        friday930pmTo10pmView.setOnTouchListener(this);
        friday10pmTo1030pmView.setOnTouchListener(this);
        friday1030pmTo11pmView.setOnTouchListener(this);
        friday11pmTo1130pmView.setOnTouchListener(this);
        friday1130pmTo12amView.setOnTouchListener(this);


        //Identifying the views of SATURDAY
        saturday4amTo430amView = (LinearLayout) view.findViewById(R.id.saturday4amTo430am);
        saturday430amTo5amView = (LinearLayout) view.findViewById(R.id.saturday430amTo5am);
        saturday5amTo530amView = (LinearLayout) view.findViewById(R.id.saturday5amTo530am);
        saturday530amTo6amView = (LinearLayout) view.findViewById(R.id.saturday530amTo6am);
        saturday6amTo630amView = (LinearLayout) view.findViewById(R.id.saturday6amTo630am);
        saturday630amTo7amView = (LinearLayout) view.findViewById(R.id.saturday630amTo7am);
        saturday7amTo730amView = (LinearLayout) view.findViewById(R.id.saturday7amTo730am);
        saturday730amTo8amView = (LinearLayout) view.findViewById(R.id.saturday730amTo8am);
        saturday8amTo830amView = (LinearLayout) view.findViewById(R.id.saturday8amTo830am);
        saturday830amTo9amView = (LinearLayout) view.findViewById(R.id.saturday830amTo9am);
        saturday9amTo930amView = (LinearLayout) view.findViewById(R.id.saturday9amTo930am);
        saturday930amTo10amView = (LinearLayout) view.findViewById(R.id.saturday930amTo10am);
        saturday10amTo1030amView = (LinearLayout) view.findViewById(R.id.saturday10amTo1030am);
        saturday1030amTo11amView = (LinearLayout) view.findViewById(R.id.saturday1030amTo11am);
        saturday11amTo1130amView = (LinearLayout) view.findViewById(R.id.saturday11amTo1130am);
        saturday1130amTo12pmView = (LinearLayout) view.findViewById(R.id.saturday1130amTo12pm);
        saturday12pmTo1230pmView = (LinearLayout) view.findViewById(R.id.saturday12pmTo1230pm);
        saturday1230pmTo1pmView = (LinearLayout) view.findViewById(R.id.saturday1230pmTo1pm);
        saturday1pmTo130pmView = (LinearLayout) view.findViewById(R.id.saturday1pmTo130pm);
        saturday130pmTo2pmView = (LinearLayout) view.findViewById(R.id.saturday130pmTo2pm);
        saturday2pmTo230pmView = (LinearLayout) view.findViewById(R.id.saturday2pmTo230pm);
        saturday230pmTo3pmView = (LinearLayout) view.findViewById(R.id.saturday230pmTo3pm);
        saturday3pmTo330pmView = (LinearLayout) view.findViewById(R.id.saturday3pmTo330pm);
        saturday330pmTo4pmView = (LinearLayout) view.findViewById(R.id.saturday330pmTo4pm);
        saturday4pmTo430pmView = (LinearLayout) view.findViewById(R.id.saturday4pmTo430pm);
        saturday430pmTo5pmView = (LinearLayout) view.findViewById(R.id.saturday430pmTo5pm);
        saturday5pmTo530pmView = (LinearLayout) view.findViewById(R.id.saturday5pmTo530pm);
        saturday530pmTo6pmView = (LinearLayout) view.findViewById(R.id.saturday530pmTo6pm);
        saturday6pmTo630pmView = (LinearLayout) view.findViewById(R.id.saturday6pmTo630pm);
        saturday630pmTo7pmView = (LinearLayout) view.findViewById(R.id.saturday630pmTo7pm);
        saturday7pmTo730pmView = (LinearLayout) view.findViewById(R.id.saturday7pmTo730pm);
        saturday730pmTo8pmView = (LinearLayout) view.findViewById(R.id.saturday730pmTo8pm);
        saturday8pmTo830pmView = (LinearLayout) view.findViewById(R.id.saturday8pmTo830pm);
        saturday830pmTo9pmView = (LinearLayout) view.findViewById(R.id.saturday830pmTo9pm);
        saturday9pmTo930pmView = (LinearLayout) view.findViewById(R.id.saturday9pmTo930pm);
        saturday930pmTo10pmView = (LinearLayout) view.findViewById(R.id.saturday930pmTo10pm);
        saturday10pmTo1030pmView = (LinearLayout) view.findViewById(R.id.saturday10pmTo1030pm);
        saturday1030pmTo11pmView = (LinearLayout) view.findViewById(R.id.saturday1030pmTo11pm);
        saturday11pmTo1130pmView = (LinearLayout) view.findViewById(R.id.saturday11pmTo1130pm);
        saturday1130pmTo12amView = (LinearLayout) view.findViewById(R.id.saturday1130pmTo12am);

        saturday4amTo430amView.setOnTouchListener(this);
        saturday430amTo5amView.setOnTouchListener(this);
        saturday5amTo530amView.setOnTouchListener(this);
        saturday530amTo6amView.setOnTouchListener(this);
        saturday6amTo630amView.setOnTouchListener(this);
        saturday630amTo7amView.setOnTouchListener(this);
        saturday7amTo730amView.setOnTouchListener(this);
        saturday730amTo8amView.setOnTouchListener(this);
        saturday8amTo830amView.setOnTouchListener(this);
        saturday830amTo9amView.setOnTouchListener(this);
        saturday9amTo930amView.setOnTouchListener(this);
        saturday930amTo10amView.setOnTouchListener(this);
        saturday10amTo1030amView.setOnTouchListener(this);
        saturday1030amTo11amView.setOnTouchListener(this);
        saturday11amTo1130amView.setOnTouchListener(this);
        saturday1130amTo12pmView.setOnTouchListener(this);
        saturday12pmTo1230pmView.setOnTouchListener(this);
        saturday1230pmTo1pmView.setOnTouchListener(this);
        saturday1pmTo130pmView.setOnTouchListener(this);
        saturday130pmTo2pmView.setOnTouchListener(this);
        saturday2pmTo230pmView.setOnTouchListener(this);
        saturday230pmTo3pmView.setOnTouchListener(this);
        saturday3pmTo330pmView.setOnTouchListener(this);
        saturday330pmTo4pmView.setOnTouchListener(this);
        saturday4pmTo430pmView.setOnTouchListener(this);
        saturday430pmTo5pmView.setOnTouchListener(this);
        saturday5pmTo530pmView.setOnTouchListener(this);
        saturday530pmTo6pmView.setOnTouchListener(this);
        saturday6pmTo630pmView.setOnTouchListener(this);
        saturday630pmTo7pmView.setOnTouchListener(this);
        saturday7pmTo730pmView.setOnTouchListener(this);
        saturday730pmTo8pmView.setOnTouchListener(this);
        saturday8pmTo830pmView.setOnTouchListener(this);
        saturday830pmTo9pmView.setOnTouchListener(this);
        saturday9pmTo930pmView.setOnTouchListener(this);
        saturday930pmTo10pmView.setOnTouchListener(this);
        saturday10pmTo1030pmView.setOnTouchListener(this);
        saturday1030pmTo11pmView.setOnTouchListener(this);
        saturday11pmTo1130pmView.setOnTouchListener(this);
        saturday1130pmTo12amView.setOnTouchListener(this);


        //Identifying the views of SUNDAY
        sunday4amTo430amView = (LinearLayout) view.findViewById(R.id.sunday4amTo430am);
        sunday430amTo5amView = (LinearLayout) view.findViewById(R.id.sunday430amTo5am);
        sunday5amTo530amView = (LinearLayout) view.findViewById(R.id.sunday5amTo530am);
        sunday530amTo6amView = (LinearLayout) view.findViewById(R.id.sunday530amTo6am);
        sunday6amTo630amView = (LinearLayout) view.findViewById(R.id.sunday6amTo630am);
        sunday630amTo7amView = (LinearLayout) view.findViewById(R.id.sunday630amTo7am);
        sunday7amTo730amView = (LinearLayout) view.findViewById(R.id.sunday7amTo730am);
        sunday730amTo8amView = (LinearLayout) view.findViewById(R.id.sunday730amTo8am);
        sunday8amTo830amView = (LinearLayout) view.findViewById(R.id.sunday8amTo830am);
        sunday830amTo9amView = (LinearLayout) view.findViewById(R.id.sunday830amTo9am);
        sunday9amTo930amView = (LinearLayout) view.findViewById(R.id.sunday9amTo930am);
        sunday930amTo10amView = (LinearLayout) view.findViewById(R.id.sunday930amTo10am);
        sunday10amTo1030amView = (LinearLayout) view.findViewById(R.id.sunday10amTo1030am);
        sunday1030amTo11amView = (LinearLayout) view.findViewById(R.id.sunday1030amTo11am);
        sunday11amTo1130amView = (LinearLayout) view.findViewById(R.id.sunday11amTo1130am);
        sunday1130amTo12pmView = (LinearLayout) view.findViewById(R.id.sunday1130amTo12pm);
        sunday12pmTo1230pmView = (LinearLayout) view.findViewById(R.id.sunday12pmTo1230pm);
        sunday1230pmTo1pmView = (LinearLayout) view.findViewById(R.id.sunday1230pmTo1pm);
        sunday1pmTo130pmView = (LinearLayout) view.findViewById(R.id.sunday1pmTo130pm);
        sunday130pmTo2pmView = (LinearLayout) view.findViewById(R.id.sunday130pmTo2pm);
        sunday2pmTo230pmView = (LinearLayout) view.findViewById(R.id.sunday2pmTo230pm);
        sunday230pmTo3pmView = (LinearLayout) view.findViewById(R.id.sunday230pmTo3pm);
        sunday3pmTo330pmView = (LinearLayout) view.findViewById(R.id.sunday3pmTo330pm);
        sunday330pmTo4pmView = (LinearLayout) view.findViewById(R.id.sunday330pmTo4pm);
        sunday4pmTo430pmView = (LinearLayout) view.findViewById(R.id.sunday4pmTo430pm);
        sunday430pmTo5pmView = (LinearLayout) view.findViewById(R.id.sunday430pmTo5pm);
        sunday5pmTo530pmView = (LinearLayout) view.findViewById(R.id.sunday5pmTo530pm);
        sunday530pmTo6pmView = (LinearLayout) view.findViewById(R.id.sunday530pmTo6pm);
        sunday6pmTo630pmView = (LinearLayout) view.findViewById(R.id.sunday6pmTo630pm);
        sunday630pmTo7pmView = (LinearLayout) view.findViewById(R.id.sunday630pmTo7pm);
        sunday7pmTo730pmView = (LinearLayout) view.findViewById(R.id.sunday7pmTo730pm);
        sunday730pmTo8pmView = (LinearLayout) view.findViewById(R.id.sunday730pmTo8pm);
        sunday8pmTo830pmView = (LinearLayout) view.findViewById(R.id.sunday8pmTo830pm);
        sunday830pmTo9pmView = (LinearLayout) view.findViewById(R.id.sunday830pmTo9pm);
        sunday9pmTo930pmView = (LinearLayout) view.findViewById(R.id.sunday9pmTo930pm);
        sunday930pmTo10pmView = (LinearLayout) view.findViewById(R.id.sunday930pmTo10pm);
        sunday10pmTo1030pmView = (LinearLayout) view.findViewById(R.id.sunday10pmTo1030pm);
        sunday1030pmTo11pmView = (LinearLayout) view.findViewById(R.id.sunday1030pmTo11pm);
        sunday11pmTo1130pmView = (LinearLayout) view.findViewById(R.id.sunday11pmTo1130pm);
        sunday1130pmTo12amView = (LinearLayout) view.findViewById(R.id.sunday1130pmTo12am);

        sunday4amTo430amView.setOnTouchListener(this);
        sunday430amTo5amView.setOnTouchListener(this);
        sunday5amTo530amView.setOnTouchListener(this);
        sunday530amTo6amView.setOnTouchListener(this);
        sunday6amTo630amView.setOnTouchListener(this);
        sunday630amTo7amView.setOnTouchListener(this);
        sunday7amTo730amView.setOnTouchListener(this);
        sunday730amTo8amView.setOnTouchListener(this);
        sunday8amTo830amView.setOnTouchListener(this);
        sunday830amTo9amView.setOnTouchListener(this);
        sunday9amTo930amView.setOnTouchListener(this);
        sunday930amTo10amView.setOnTouchListener(this);
        sunday10amTo1030amView.setOnTouchListener(this);
        sunday1030amTo11amView.setOnTouchListener(this);
        sunday11amTo1130amView.setOnTouchListener(this);
        sunday1130amTo12pmView.setOnTouchListener(this);
        sunday12pmTo1230pmView.setOnTouchListener(this);
        sunday1230pmTo1pmView.setOnTouchListener(this);
        sunday1pmTo130pmView.setOnTouchListener(this);
        sunday130pmTo2pmView.setOnTouchListener(this);
        sunday2pmTo230pmView.setOnTouchListener(this);
        sunday230pmTo3pmView.setOnTouchListener(this);
        sunday3pmTo330pmView.setOnTouchListener(this);
        sunday330pmTo4pmView.setOnTouchListener(this);
        sunday4pmTo430pmView.setOnTouchListener(this);
        sunday430pmTo5pmView.setOnTouchListener(this);
        sunday5pmTo530pmView.setOnTouchListener(this);
        sunday530pmTo6pmView.setOnTouchListener(this);
        sunday6pmTo630pmView.setOnTouchListener(this);
        sunday630pmTo7pmView.setOnTouchListener(this);
        sunday7pmTo730pmView.setOnTouchListener(this);
        sunday730pmTo8pmView.setOnTouchListener(this);
        sunday8pmTo830pmView.setOnTouchListener(this);
        sunday830pmTo9pmView.setOnTouchListener(this);
        sunday9pmTo930pmView.setOnTouchListener(this);
        sunday930pmTo10pmView.setOnTouchListener(this);
        sunday10pmTo1030pmView.setOnTouchListener(this);
        sunday1030pmTo11pmView.setOnTouchListener(this);
        sunday11pmTo1130pmView.setOnTouchListener(this);
        sunday1130pmTo12amView.setOnTouchListener(this);

        TextView mondayLabel = (TextView) view.findViewById(R.id.mondayLabel);
        TextView tuesdayLabel = (TextView) view.findViewById(R.id.tuesdayLabel);
        TextView wednesdayLabel = (TextView) view.findViewById(R.id.wednesdayLabel);
        TextView thursdayLabel = (TextView) view.findViewById(R.id.thursdayLabel);
        TextView fridayLabel = (TextView) view.findViewById(R.id.fridayLabel);
        TextView saturdayLabel = (TextView) view.findViewById(R.id.saturdayLabel);
        TextView sundayLabel = (TextView) view.findViewById(R.id.sundayLabel);
        mondayLabel.setOnTouchListener(this);
        tuesdayLabel.setOnTouchListener(this);
        wednesdayLabel.setOnTouchListener(this);
        thursdayLabel.setOnTouchListener(this);
        fridayLabel.setOnTouchListener(this);
        saturdayLabel.setOnTouchListener(this);
        sundayLabel.setOnTouchListener(this);

        sundayPTOLayout = (RelativeLayout) view.findViewById(R.id.sundayPTOLayout);
        mondayPTOLayout = (RelativeLayout) view.findViewById(R.id.mondayPTOLayout);
        tuesdayPTOLayout = (RelativeLayout) view.findViewById(R.id.tuesdayPTOLayout);
        wednesdayPTOLayout = (RelativeLayout) view.findViewById(R.id.wednesdayPTOLayout);
        thursdayPTOLayout = (RelativeLayout) view.findViewById(R.id.thursdayPTOLayout);
        fridayPTOLayout = (RelativeLayout) view.findViewById(R.id.fridayPTOLayout);
        saturdayPTOLayout = (RelativeLayout) view.findViewById(R.id.saturdayPTOLayout);

        sundaySlotsParentLL = (LinearLayout) view.findViewById(R.id.sundaySlotsParentLL);
        mondaySlotsParentLL = (LinearLayout) view.findViewById(R.id.mondaySlotsParentLL);
        tuesdaySlotsParentLL = (LinearLayout) view.findViewById(R.id.tuesdaySlotsParentLL);
        wednesdaySlotsParentLL = (LinearLayout) view.findViewById(R.id.wednesdaySlotsParentLL);
        thursdaySlotsParentLL = (LinearLayout) view.findViewById(R.id.thursdaySlotsParentLL);
        fridaySlotsParentLL = (LinearLayout) view.findViewById(R.id.fridaySlotsParentLL);
        saturdaySlotsParentLL = (LinearLayout) view.findViewById(R.id.saturdaySlotsParentLL);
    }

    private void onSlotClick(ArrayList<Slot> slotsList, int initialPosition, View v, MotionEvent event) {
        Log.v("initialPosition", initialPosition + "");
        int position = initialPosition;
        if (is_EMPTY_Slot(slotsList.get(initialPosition))) {
            position = initialPosition - (initialPosition % 4);
        }
        if (isAvailabilityMode) {
            if (is_GREEN_Slot(slotsList.get(position)) && is_EMPTY_Slot(slotsList.get(initialPosition))) {
                for (int i = position; i < initialPosition; ++i) {
                    slotsList.get(i).setSlotType(SlotType.EMPTY);
                }
            }
            Slot selectedSlot = slotsList.get(position);
            if (is_EMPTY_Slot(selectedSlot)) {
                doEnableSaveButtonIfRequired();
                for (int i = 0; i < (position == 36 ? 4 : 8); ++i) {
                    Slot slot = slotsList.get(i + position);
                    if (is_EMPTY_Slot(slot)) {
                        slot.setSlotType(SlotType.GREEN);
                    }
                }
            } else if (is_GREEN_Slot(selectedSlot)) {
                AvailabiltySlotActionPopup chromeHelpPopup = new AvailabiltySlotActionPopup(getActivity(), selectedSlot, slotsList, initialPosition);
                chromeHelpPopup.show(v);
            } else if (is_RED_Slot(selectedSlot)) {
                //if slot is already RED (i.e faded RED) then turn same colored adjacent slots to GREEN.
                //that's why we should iterate the loop too many times. i.e slotsList.size() times.
                //But there are few cases like 8Am to 1030AM are RED's, Now I clicked on 11Am slot. So it should turn 10AM to 2pm slots to GREEN.
                //But if 12pm to 4pm slots are already RED.. then just turn 10 to 12 slots to GREEN

                AvailabiltySlotActionPopup chromeHelpPopup = new AvailabiltySlotActionPopup(getActivity(), selectedSlot, slotsList, initialPosition);
                chromeHelpPopup.show(v);
                int howManyTimes = (position == 36 ? 4 : 8), j = 0;
                for (int i = selectedSlot.getSlotSerialNumber(); i < slotsList.size(); ++i, ++j) {
                    if (is_EMPTY_Slot(slotsList.get(i)) && j < howManyTimes) {
                        slotsList.get(i).setSlotType(SlotType.GREEN);
                    } else {
                        break;
                    }
                }
            }
        } else {
            if (is_RED_Slot(slotsList.get(position)) && is_EMPTY_Slot(slotsList.get(initialPosition))) {
                for (int i = position; i < initialPosition; ++i) {
                    slotsList.get(i).setSlotType(SlotType.EMPTY);
                }
            }
            Slot selectedSlot = slotsList.get(position);
            if (is_EMPTY_Slot(selectedSlot)) {
                doEnableSaveButtonIfRequired();
                for (int i = 0; i < (position == 36 ? 4 : 8); ++i) {
                    Slot slot = slotsList.get(i + position);
                    if (is_EMPTY_Slot(slot)) {
                        slot.setSlotType(SlotType.RED);
                    }
                }
            } else if (is_RED_Slot(selectedSlot)) {
                //if slot is already RED then just clear the same colored adjacent slots.
                AvailabiltySlotActionPopup chromeHelpPopup = new AvailabiltySlotActionPopup(getActivity(), selectedSlot, slotsList, initialPosition);
                chromeHelpPopup.show(v);
            } else if (is_GREEN_Slot(selectedSlot)) {
                //if slot is already GREEN (i.e faded GREEN) then turn same colored adjacent slots to RED.
                //that's why we should iterate the loop too many times. i.e slotsList.size() times.
                //But there are few cases like 8Am to 1030AM are GREEN's, Now I clicked on 11Am slot. So it should turn 10AM to 2pm slots to RED.
                //But if 12pm to 4pm slots are already GREEN.. then just turn 10 to 12 slots to RED
                int howManyTimes = (position == 36 ? 4 : 8), j = 0;
                AvailabiltySlotActionPopup chromeHelpPopup = new AvailabiltySlotActionPopup(getActivity(), selectedSlot, slotsList, initialPosition);
                chromeHelpPopup.show(v);
                for (int i = selectedSlot.getSlotSerialNumber(); i < slotsList.size(); ++i, ++j) {
                    if (is_EMPTY_Slot(slotsList.get(i)) && j < howManyTimes) {
                        slotsList.get(i).setSlotType(SlotType.RED);
                    } else {
                        break;
                    }
                }
            }
        }
        doUpdateSlotsRowUI(slotsList);
    }

    private void doUpdateSlotsRowUI(ArrayList<Slot> slotsList) {
        if (slotsList == null) {
            return;
        }
        clearRow(slotsList);

        //doDisplaySlotsStatuses(slotsList);

        int size = slotsList.size();
        for (int i = 0; i < size; ++i) {
            Slot slot = slotsList.get(i);
            boolean isPTO = slot.isPTO();

            if (is_GREEN_Slot(slot)) {
                Slot sameTypeLastSlot = getSameTypeLastSlotInRow(slotsList, slot);
                int weight = 1;
                for (int j = i; j <= sameTypeLastSlot.getSlotSerialNumber(); ++j) {
                    if (j != i) {
                        weight += 1;
                        slotsList.get(j).setWeight(0);
                        setWeightToView(0, slotsList.get(j).getView());
                    }
                }
                slot.setWeight(weight);
                setWeightToView(weight, slot.getView());

                if (isAvailabilityMode) {
                    if (slot.getSlotSerialNumber() == 0 && slot.getWeight() == 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_left_right_rounded_corners_green_slot);
                    } else if (slot.getSlotSerialNumber() == 0 && slot.getWeight() != 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_left_rounded_corners_green_slot);
                    } else if (slot.getSlotSerialNumber() != 0 && (slot.getSlotSerialNumber() + slot.getWeight()) == 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_right_rounded_corners_green_slot);
                    } else {
                        slot.getView().setBackgroundColor(Color.parseColor("#66BB6A"));
                    }
                } else {
                    if (slot.getSlotSerialNumber() == 0 && slot.getWeight() == 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_left_right_rounded_corners_green_faded_slot);
                    } else if (slot.getSlotSerialNumber() == 0 && slot.getWeight() != 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_left_rounded_corners_green_faded_slot);
                    } else if (slot.getSlotSerialNumber() != 0 && (slot.getSlotSerialNumber() + slot.getWeight()) == 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_right_rounded_corners_green_faded_slot);
                    } else {
                        slot.getView().setBackgroundColor(Color.parseColor("#B366BB6A"));
                    }
                }

                int slotDuration = (sameTypeLastSlot.getActualEndTimeOfSlot() - slot.getActualStartTimeOfSlot());
                if (slotDuration >= 4 * OnBoardingActivity.INTERVAL_MINS) {
                    if (slotDuration >= 240) {
                        ((TextView) slot.getView().getChildAt(0)).setText(getTimeStringNew(slot.getActualStartTimeOfSlot()) + " - " + getTimeStringNew(sameTypeLastSlot.getActualEndTimeOfSlot()));
                    } else {
                        ((TextView) slot.getView().getChildAt(0)).setText(getTimeStringNew(slot.getActualStartTimeOfSlot()) + "\n - \n" + getTimeStringNew(sameTypeLastSlot.getActualEndTimeOfSlot()));
                    }
                } else {
                    ((TextView) slot.getView().getChildAt(0)).setText("");
                }
                i = sameTypeLastSlot.getSlotSerialNumber();
            } else if (is_RED_Slot(slot)) {
                Slot sameTypeLastSlot = getSameTypeLastSlotInRow(slotsList, slot);
                int weight = 1;
                for (int j = i; j <= sameTypeLastSlot.getSlotSerialNumber(); ++j) {
                    if (j != i) {
                        weight += 1;
                        slotsList.get(j).setWeight(0);
                        setWeightToView(0, slotsList.get(j).getView());
                    }
                }
                slot.setWeight(weight);
                setWeightToView(weight, slot.getView());

                if (isAvailabilityMode) {
                    if (slot.getSlotSerialNumber() == 0 && slot.getWeight() == 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_left_right_rounded_corners_red_faded_slot);
                    } else if (slot.getSlotSerialNumber() == 0 && slot.getWeight() != 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_left_rounded_corners_red_faded_slot);
                    } else if (slot.getSlotSerialNumber() != 0 && (slot.getSlotSerialNumber() + slot.getWeight()) == 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_right_rounded_corners_red_faded_slot);
                    } else {
                        slot.getView().setBackgroundColor(Color.parseColor("#B3D02D2D"));
                    }
                } else {
                    if (slot.getSlotSerialNumber() == 0 && slot.getWeight() == 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_left_right_rounded_corners_red_slot);
                    } else if (slot.getSlotSerialNumber() == 0 && slot.getWeight() != 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_left_rounded_corners_red_slot);
                    } else if (slot.getSlotSerialNumber() != 0 && (slot.getSlotSerialNumber() + slot.getWeight()) == 40) {
                        slot.getView().setBackgroundResource(R.drawable.bg_right_rounded_corners_red_slot);
                    } else {
                        slot.getView().setBackgroundColor(Color.parseColor("#D02D2D"));
                    }
                }

                int slotDuration = (sameTypeLastSlot.getActualEndTimeOfSlot() - slot.getActualStartTimeOfSlot());
                if (slotDuration >= 4 * OnBoardingActivity.INTERVAL_MINS) {
                    if (slotDuration >= 240) { //if 4 hours
                        ((TextView) slot.getView().getChildAt(0)).setText(getTimeStringNew(slot.getActualStartTimeOfSlot()) + " - " + getTimeStringNew(sameTypeLastSlot.getActualEndTimeOfSlot()));
                    } else {
                        ((TextView) slot.getView().getChildAt(0)).setText(getTimeStringNew(slot.getActualStartTimeOfSlot()) + "\n - \n" + getTimeStringNew(sameTypeLastSlot.getActualEndTimeOfSlot()));
                    }
                } else {
                    ((TextView) slot.getView().getChildAt(0)).setText("");
                }
                i = sameTypeLastSlot.getSlotSerialNumber();
            }
        }
        doUpdateNumOfHours();
    }

    private Slot getSameTypeLastSlotInRow(ArrayList<Slot> slotsList, Slot initialSlot) {
        if (initialSlot.getSlotSerialNumber() == slotsList.size() - 1) {
            return slotsList.get(slotsList.size() - 1);
        }

        for (int i = initialSlot.getSlotSerialNumber(); i < slotsList.size(); ++i) {
            Slot nextSlot = slotsList.get(i);
            if (initialSlot.getSlotType().getType().equals(nextSlot.getSlotType().getType())) {
                if (i >= 39) {  //if we reach END then just return the last element
                    return slotsList.get(slotsList.size() - 1);
                }
            } else {
                return slotsList.get(i - 1);
            }
        }
        return slotsList.get(1 + initialSlot.getSlotSerialNumber());
    }

    private void onCustomAvailabilityClick(int clickedPosition, ArrayList<Slot> slotsList) {
        //clickedPosition = clickedPosition - (clickedPosition % 4);
        Log.v("clickedPosition", clickedPosition + "");
        Slot clickedSlot = slotsList.get(clickedPosition);

        Bundle b = new Bundle();
        Slot selectedSlot = slotsList.get(clickedPosition);
        int endPosition = slotsList.size() - 1;
        for (int i = clickedPosition; i < slotsList.size(); ++i) {
            Slot slot = slotsList.get(i);
            if (!slot.getSlotType().getType().equals(selectedSlot.getSlotType().getType())) {
                endPosition = i - 1;
                break;
            }
        }

        try {
            b.putInt(Extras_Keys.CURRENT_SLOT_START_TIME, slotsList.get(clickedPosition).getActualStartTimeOfSlot());
        } catch (Exception e) {
            b.putInt(Extras_Keys.CURRENT_SLOT_START_TIME, slotsList.get(clickedPosition - 1).getActualStartTimeOfSlot());
        }

        if (endPosition <= 0) {
            endPosition = 0;
        }
        b.putInt(Extras_Keys.CURRENT_SLOT_END_TIME, slotsList.get(endPosition).getActualEndTimeOfSlot());

        b.putInt(Extras_Keys.PICKER_START_TIME, 240);
        b.putInt(Extras_Keys.PICKER_END_TIME, 1440);

        b.putSerializable("slot", clickedSlot);
        if (isAvailabilityMode) {
            b.putString("granularType", is_EMPTY_Slot(clickedSlot) ? SlotType.GREEN.getType() : clickedSlot.getSlotType().getType());
        } else {
            b.putString("granularType", is_EMPTY_Slot(clickedSlot) ? SlotType.RED.getType() : clickedSlot.getSlotType().getType());
        }
        dialogFragment = new GranularAvailabilityDialogFragment();
        dialogFragment.setArguments(b);

        dialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
        dialogFragment.show(getChildFragmentManager(), "Availability Dialog Fragment");
        dialogFragment.setOnDialogClickListener(new GranularAvailabilityDialogFragment.OnDialogClickListener() {
            @Override
            public void onApplyClick(int startMins, int endMins, ArrayList<Boolean> ptoDays, Slot selectedSlot, SlotType newSlotType) {
                dialogFragment.dismiss();
                doEnableSaveButtonIfRequired();
                if (ptoDays.get(0)) {
                    doUpdateSlotsOfWeek(selectedSlot, mondaySlotsList, newSlotType, startMins, endMins);
                }
                if (ptoDays.get(1)) {
                    doUpdateSlotsOfWeek(selectedSlot, tuesdaySlotsList, newSlotType, startMins, endMins);
                }
                if (ptoDays.get(2)) {
                    doUpdateSlotsOfWeek(selectedSlot, wednesdaySlotsList, newSlotType, startMins, endMins);
                }
                if (ptoDays.get(3)) {
                    doUpdateSlotsOfWeek(selectedSlot, thursdaySlotsList, newSlotType, startMins, endMins);
                }
                if (ptoDays.get(4)) {
                    doUpdateSlotsOfWeek(selectedSlot, fridaySlotsList, newSlotType, startMins, endMins);
                }
                if (ptoDays.get(5)) {
                    doUpdateSlotsOfWeek(selectedSlot, saturdaySlotsList, newSlotType, startMins, endMins);
                }
                if (ptoDays.get(6)) {
                    doUpdateSlotsOfWeek(selectedSlot, sundaySlotsList, newSlotType, startMins, endMins);
                }
            }
        });
    }

    private void doUpdateSlotsOfWeek(Slot selectedSlot, ArrayList<Slot> slotsList, SlotType newSlotType, int startMins, int endMins) {
        //if slot is already GREEN or RED then just clear the same colored adjacent slots.
        if (newSlotType.getType().equalsIgnoreCase(SlotType.GREEN.getType())) {
            for (int i = selectedSlot.getSlotSerialNumber(); i < slotsList.size(); ++i) {
                if (is_GREEN_Slot(slotsList.get(i))) {
                    slotsList.get(i).setSlotType(SlotType.EMPTY);
                } else {
                    break;
                }
            }
        } else {
            for (int i = selectedSlot.getSlotSerialNumber(); i < slotsList.size(); ++i) {
                if (is_RED_Slot(slotsList.get(i))) {
                    slotsList.get(i).setSlotType(SlotType.EMPTY);
                } else {
                    break;
                }
            }
        }

        for (Slot slot : slotsList) {
            if (slot.getActualStartTimeOfSlot() >= startMins && slot.getActualEndTimeOfSlot() <= endMins) {
                slot.setSlotType(newSlotType);
            }
        }
        doUpdateSlotsRowUI(slotsList);
    }

    private void doUpdateNumOfHours() {
        float numOfHours = 0;

        int size = mondaySlotsList.size();
        if(mondaySlotsParentLL.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < size; ++i) {
                Slot slot = mondaySlotsList.get(i);
                if (slot.getWeight() >= 1 && is_GREEN_Slot(slot)) {
                    numOfHours += (slot.getWeight() * 0.5);
                }
            }
        }

        size = tuesdaySlotsList.size();
        if(tuesdaySlotsParentLL.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < size; ++i) {
                Slot slot = tuesdaySlotsList.get(i);
                if (slot.getWeight() >= 1 && is_GREEN_Slot(slot)) {
                    numOfHours += (slot.getWeight() * 0.5);
                }
            }
        }

        size = wednesdaySlotsList.size();
        if(wednesdaySlotsParentLL.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < size; ++i) {
                Slot slot = wednesdaySlotsList.get(i);
                if (slot.getWeight() >= 1 && is_GREEN_Slot(slot)) {
                    numOfHours += (slot.getWeight() * .5);
                }
            }
        }

        size = thursdaySlotsList.size();
        if(thursdaySlotsParentLL.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < size; ++i) {
                Slot slot = thursdaySlotsList.get(i);
                if (slot.getWeight() >= 1 && is_GREEN_Slot(slot)) {
                    numOfHours += (slot.getWeight() * .5);
                }
            }
        }

        size = fridaySlotsList.size();
        if(fridaySlotsParentLL.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < size; ++i) {
                Slot slot = fridaySlotsList.get(i);
                if (slot.getWeight() >= 1 && is_GREEN_Slot(slot)) {
                    numOfHours += (slot.getWeight() * .5);
                }
            }
        }

        size = saturdaySlotsList.size();
        if(saturdaySlotsParentLL.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < size; ++i) {
                Slot slot = saturdaySlotsList.get(i);
                if (slot.getWeight() >= 1 && is_GREEN_Slot(slot)) {
                    numOfHours += (slot.getWeight() * .5);
                }
            }
        }

        size = sundaySlotsList.size();
        if(sundaySlotsParentLL.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < size; ++i) {
                Slot slot = sundaySlotsList.get(i);
                if (slot.getWeight() >= 1 && is_GREEN_Slot(slot)) {
                    numOfHours += (slot.getWeight() * .5);
                }
            }
        }

        float hoursPerWeek = 2 * Float.parseFloat(legionPreferences.get(Prefs_Keys.HOURS_PER_WEEK, "40"));
        totalNumOfHoursTV.setText(String.valueOf((int) hoursPerWeek));
        numOfAvailableHrsTV.setText("" + (int) numOfHours);
        //progressBar.setProgress((int) ((100 / hoursPerWeek) * numOfHours));

        dottedProgressbarContainer.removeAllViews();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for (int i = 1; i <= hoursPerWeek; ++i) {
            LinearLayout dotView = (LinearLayout) inflater.inflate(R.layout.dashed_progress_item, null);
            dotView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            if (i <= numOfHours) {
                dotView.findViewById(R.id.dash).setBackgroundColor(Color.parseColor("#69BA6D"));
            } else {
                dotView.findViewById(R.id.dash).setBackgroundColor(Color.parseColor("#C3CCD7"));
            }
            dottedProgressbarContainer.addView(dotView);
        }
    }

    private void setWeightToView(float weight, LinearLayout view) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.weight = weight;
        view.setLayoutParams(params);
    }

    private boolean is_GREEN_Slot(Slot slot) {
        return slot.getSlotType().getType().equals(SlotType.GREEN.getType());
    }

    private boolean is_RED_Slot(Slot slot) {
        return slot.getSlotType().getType().equals(SlotType.RED.getType());
    }

    private boolean is_EMPTY_Slot(Slot slot) {
        return slot.getSlotType().getType().equals(SlotType.EMPTY.getType());
    }

    private void clearRow(ArrayList<Slot> slotsList) {
        for (Slot slot : slotsList) {
            slot.setWeight(1);
            ((TextView) slot.getView().getChildAt(0)).setText("");
            setWeightToView(1, slot.getView());
            slot.getView().setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void doUpdateSlots(boolean isAvailabilityMode, boolean isScheduleLocked, boolean isPastSchedule) {
        this.isAvailabilityMode = isAvailabilityMode;
        this.isScheduleLocked = isScheduleLocked;
        this.isPastSchedule = isPastSchedule;
        setUserVisibleHint(true);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //this.fingerDownEvent = e;
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent startMotionEvent, MotionEvent endMotionEvent, float distanceMovedInX, float distanceMovedInY) {
        int dayLayoutWidth = getDayLayoutWidth();
        float singleSlotWidth = dayLayoutWidth / 40;

        doEnableSaveButtonIfRequired();

        float sliderStep = 1;
        deltaDist += distanceMovedInX;
        sliderStep = (int) (deltaDist / singleSlotWidth);
        distanceMovedInX = sliderStep * singleSlotWidth;
        deltaDist -= distanceMovedInX;

        if (startMotionEvent.getX() <= endMotionEvent.getX()) {
            onRightSlide(startMotionEvent, endMotionEvent, distanceMovedInX);
        } else if (startMotionEvent.getX() >= endMotionEvent.getX()) {
            onLeftSlide(startMotionEvent, endMotionEvent, distanceMovedInX);
        } else {
            Log.v("Unknown move", "Invalid move");
            return false;
        }

        if (sliderView != null) {
            showTimerPopup(endMotionEvent);
        }

        if (isAvailabilityMode) {
            if (sliderDurationInMins <= 60) {
                ((TextView) sliderView.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else {
                ((TextView) sliderView.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_slider_availability_handler, 0, R.drawable.ic_slider_availability_handler, 0);
            }


            if (sliderStartMins <= 240 && sliderEndMins >= 1440) {
                sliderView.setBackgroundResource(R.drawable.bg_availability_slider_left_right_rounded_corners);
            } else if (sliderStartMins <= 240 && sliderEndMins < 1440) {
                sliderView.setBackgroundResource(R.drawable.bg_availability_slider_left_rounded_corners);
            } else if (sliderStartMins > 240 && sliderEndMins >= 1440) {
                sliderView.setBackgroundResource(R.drawable.bg_availability_slider_right_rounded_corners);
            } else {
                sliderView.setBackgroundResource(R.drawable.bg_availability_slider);
            }
        } else {
            if (sliderDurationInMins <= 60) {
                ((TextView) sliderView.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            } else {
                ((TextView) sliderView.getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_slider_unavailability_handler, 0, R.drawable.ic_slider_unavailability_handler, 0);
            }

            if (sliderStartMins <= 240 && sliderEndMins >= 1440) {
                sliderView.setBackgroundResource(R.drawable.bg_unavailability_slider_left_right_rounded_corners);
            } else if (sliderStartMins <= 240 && sliderEndMins < 1440) {
                sliderView.setBackgroundResource(R.drawable.bg_unavailability_slider_left_rounded_corners);
            } else if (sliderStartMins > 240 && sliderEndMins >= 1440) {
                sliderView.setBackgroundResource(R.drawable.bg_unavailability_slider_right_rounded_corners);
            } else {
                sliderView.setBackgroundResource(R.drawable.bg_unavaialbility_slider);
            }
        }
        return true;
    }

    private void onLeftSlide(MotionEvent startMotionEvent, MotionEvent endMotionEvent, float distanceMovedInX) {
        int parentLayoutWidth = getDayLayoutWidth();
        if (isSlidingStarted) {
            doClearSliderBehindSlotsIfRequired();
            if (sliderStartMins >= 240) {
                if (isCenterTouch) {
                    if (sliderStartMins > 240) {
                        sliderView.setX(sliderView.getX() + (int) (-1 * distanceMovedInX));
                    } else { //== 240
                        sliderView.setX(0);
                        ((TextView) sliderView.getChildAt(0)).setWidth(sliderView.getWidth() + (int) (-1 * distanceMovedInX));
                    }
                } else if (isLeftTouch) {
                    sliderView.setX(sliderView.getX() + (int) (-1 * distanceMovedInX));
                    ((TextView) sliderView.getChildAt(0)).setWidth(sliderView.getWidth() - (int) (-1 * distanceMovedInX));
                } else {
                    ((TextView) sliderView.getChildAt(0)).setWidth(sliderView.getWidth() + (int) (-1 * distanceMovedInX));
                }
            } else {//reached the left edge. So lets decrease the width from right end.
                Log.v("OverSlide", "OverSlide");
                sliderView.setX(0);
                ((TextView) sliderView.getChildAt(0)).setWidth(sliderView.getWidth() + (int) (-1 * distanceMovedInX));
            }

            sliderDurationInMins = ((1200 * sliderView.getWidth() / parentLayoutWidth));
            sliderStartMins = ((1200 * ((int) sliderView.getX()) / parentLayoutWidth + 240));
            sliderEndMins = sliderStartMins + sliderDurationInMins;
            sliderStartTime = getTimeStringNew(sliderStartMins);
            sliderEndTime = getTimeStringNew(sliderEndMins);

            ((TextView) sliderView.getChildAt(0)).setText(sliderStartTime + " - " + sliderEndTime);
            if (sliderDurationInMins >= 120) {
                ((TextView) sliderView.getChildAt(0)).setTextColor(Color.WHITE);
            } else {
                ((TextView) sliderView.getChildAt(0)).setTextColor(Color.TRANSPARENT);
            }
        } else {
            isSlidingStarted = true;
            if (isAvailabilityMode && isSlotBehindSliderGreen()) {
                isNewSlotDrawing = false;
                initialWidthOfcurrentBackgroundSlotView = currentBackgroundSlotView.getWidth();
                initialLeftOfCurrentBackgroundSlotView = currentBackgroundSlotView.getLeft();
                ((TextView) sliderView.getChildAt(0)).setText(((TextView) currentBackgroundSlotView.getChildAt(0)).getText().toString());
            } else if (!isAvailabilityMode && isSlotBehindSliderRed()) {
                isNewSlotDrawing = false;
                initialWidthOfcurrentBackgroundSlotView = currentBackgroundSlotView.getWidth();
                initialLeftOfCurrentBackgroundSlotView = currentBackgroundSlotView.getLeft();
                ((TextView) sliderView.getChildAt(0)).setText(((TextView) currentBackgroundSlotView.getChildAt(0)).getText().toString());
            } else {
                isNewSlotDrawing = true;
                initialWidthOfcurrentBackgroundSlotView = 8 * parentLayoutWidth / 40;
                initialLeftOfCurrentBackgroundSlotView = (int) startMotionEvent.getX() + currentBackgroundSlotView.getLeft();

                sliderView.setX(initialLeftOfCurrentBackgroundSlotView);
                ((TextView) sliderView.getChildAt(0)).setWidth(initialWidthOfcurrentBackgroundSlotView);
                sliderStartMins = ((1200 * ((int) sliderView.getX()) / parentLayoutWidth + 240));
                sliderView.setVisibility(View.VISIBLE);
                isLeftTouch = true;
                isRightTouch = false;
                isCenterTouch = false;
                return;
            }
            sliderView.setX(initialLeftOfCurrentBackgroundSlotView);
            ((TextView) sliderView.getChildAt(0)).setWidth(initialWidthOfcurrentBackgroundSlotView);
            sliderStartMins = ((1200 * ((int) sliderView.getX()) / parentLayoutWidth + 240));
            sliderView.setVisibility(View.VISIBLE);

            int part1Width = initialWidthOfcurrentBackgroundSlotView / 6;

            isLeftTouch = false;
            isRightTouch = false;
            isCenterTouch = false;

            if (part1Width >= fingerDownInitialX) {
                isLeftTouch = true;
            } else if (fingerDownInitialX >= 5 * part1Width) {
                isRightTouch = true;
            } else {
                isCenterTouch = true;
            }
        }
    }

    private void onRightSlide(MotionEvent startMotionEvent, MotionEvent endMotionEvent, float distanceMovedInX) {
        int parentLayoutWidth = getDayLayoutWidth();

        if (isSlidingStarted) {
            doClearSliderBehindSlotsIfRequired();
            if (sliderEndMins <= 1440) {
                if (isCenterTouch) {
                    sliderView.setX(sliderView.getX() + (int) (-1 * distanceMovedInX));
                } else if (isRightTouch) {
                    ((TextView) sliderView.getChildAt(0)).setWidth(sliderView.getWidth() + (int) (-1 * distanceMovedInX));
                } else {
                    sliderView.setX(sliderView.getX() + (int) (-1 * distanceMovedInX));
                    ((TextView) sliderView.getChildAt(0)).setWidth(sliderView.getWidth() - (int) (-1 * distanceMovedInX));
                }
            } else {//reached the right edge. So lets decrease the width from left end.
                Log.v("OverSlide", "OverSlide");
                sliderView.setX(sliderView.getX() + (int) (-1 * distanceMovedInX));
                ((TextView) sliderView.getChildAt(0)).setWidth(sliderView.getWidth() - (int) (-1 * distanceMovedInX));
            }

            sliderDurationInMins = ((1200 * sliderView.getWidth() / parentLayoutWidth));
            sliderStartMins = ((1200 * ((int) sliderView.getX()) / parentLayoutWidth + 240));
            sliderEndMins = sliderStartMins + sliderDurationInMins;
            sliderStartTime = getTimeStringNew(sliderStartMins);
            sliderEndTime = getTimeStringNew(sliderEndMins);

            ((TextView) sliderView.getChildAt(0)).setText(sliderStartTime + " - " + sliderEndTime);
            if (sliderDurationInMins >= 120) {
                ((TextView) sliderView.getChildAt(0)).setTextColor(Color.WHITE);
            } else {
                ((TextView) sliderView.getChildAt(0)).setTextColor(Color.TRANSPARENT);
            }
        } else {
            isSlidingStarted = true;
            if (isAvailabilityMode && isSlotBehindSliderGreen()) {
                isNewSlotDrawing = false;
                initialWidthOfcurrentBackgroundSlotView = currentBackgroundSlotView.getWidth();
                initialLeftOfCurrentBackgroundSlotView = currentBackgroundSlotView.getLeft();
                ((TextView) sliderView.getChildAt(0)).setText(((TextView) currentBackgroundSlotView.getChildAt(0)).getText().toString());

                sliderView.setVisibility(View.VISIBLE);
                sliderView.setX(initialLeftOfCurrentBackgroundSlotView);
                ((TextView) sliderView.getChildAt(0)).setWidth(initialWidthOfcurrentBackgroundSlotView);
            } else if (!isAvailabilityMode && isSlotBehindSliderRed()) {
                initialWidthOfcurrentBackgroundSlotView = currentBackgroundSlotView.getWidth();
                initialLeftOfCurrentBackgroundSlotView = currentBackgroundSlotView.getLeft();
                ((TextView) sliderView.getChildAt(0)).setText(((TextView) currentBackgroundSlotView.getChildAt(0)).getText().toString());

                sliderView.setVisibility(View.VISIBLE);
                sliderView.setX(initialLeftOfCurrentBackgroundSlotView);
                ((TextView) sliderView.getChildAt(0)).setWidth(initialWidthOfcurrentBackgroundSlotView);
            } else {
                isNewSlotDrawing = true;
                initialWidthOfcurrentBackgroundSlotView = 8 * parentLayoutWidth / 40;
                initialLeftOfCurrentBackgroundSlotView = (int) startMotionEvent.getX() + currentBackgroundSlotView.getLeft();

                sliderView.setVisibility(View.VISIBLE);
                sliderView.setX(initialLeftOfCurrentBackgroundSlotView);
                ((TextView) sliderView.getChildAt(0)).setWidth(initialWidthOfcurrentBackgroundSlotView);
                isRightTouch = true;
                isLeftTouch = false;
                isCenterTouch = false;
                return;
            }

            sliderDurationInMins = ((1200 * sliderView.getWidth() / parentLayoutWidth));
            sliderStartMins = ((1200 * ((int) sliderView.getX()) / parentLayoutWidth + 240));
            sliderStartMins = sliderStartMins - sliderStartMins % OnBoardingActivity.INTERVAL_MINS;
            sliderEndMins = sliderStartMins + sliderDurationInMins;

            int part1Width = initialWidthOfcurrentBackgroundSlotView / 6;
            isLeftTouch = false;
            isRightTouch = false;
            isCenterTouch = false;
            if (part1Width >= fingerDownInitialX) {
                isLeftTouch = true;
            } else if (fingerDownInitialX >= 5 * part1Width) {
                isRightTouch = true;
            } else {
                isCenterTouch = true;
            }
        }
    }

    private void doClearSliderBehindSlotsIfRequired() {
        if (currentBackgroundSlotView == null) {
            return;
        }
        if (currentBackgroundSlotView.getBackground() instanceof ColorDrawable) {
            if (isAvailabilityMode) {
                if (((ColorDrawable) currentBackgroundSlotView.getBackground()).getColor() != Color.parseColor("#66BB6A")) {
                    return;
                }
            } else {
                if (((ColorDrawable) currentBackgroundSlotView.getBackground()).getColor() != Color.parseColor("#D02D2D")) {
                    return;
                }
            }
        }

        //clear the adjacent GREEN slots here
        ArrayList<Slot> slotsList;
        if (sliderView == sliderViewMonday) {
            slotsList = mondaySlotsList;
        } else if (sliderView == sliderViewTuesday) {
            slotsList = tuesdaySlotsList;
        } else if (sliderView == sliderViewWednesday) {
            slotsList = wednesdaySlotsList;
        } else if (sliderView == sliderViewThursday) {
            slotsList = thursdaySlotsList;
        } else if (sliderView == sliderViewFriday) {
            slotsList = fridaySlotsList;
        } else if (sliderView == sliderViewSaturday) {
            slotsList = saturdaySlotsList;
        } else {
            slotsList = sundaySlotsList;
        }

        int selectedPos = 0;
        for (int i = 0; i < slotsList.size(); ++i) {
            Slot slot = slotsList.get(i);
            if (slot.getView() == currentBackgroundSlotView) {
                selectedPos = i;
                break;
            }
        }

        for (int i = slotsList.get(selectedPos).getSlotSerialNumber(); i < slotsList.size(); ++i) {
            Slot slot = slotsList.get(i);
            if (isAvailabilityMode && is_GREEN_Slot(slot)) {
                slot.setSlotType(SlotType.EMPTY);
            } else if (!isAvailabilityMode && is_RED_Slot(slot)) {
                slot.setSlotType(SlotType.EMPTY);
            } else {
                break;
            }
        }
        doUpdateSlotsRowUI(slotsList);
    }

    private String getTimeStringNew(int inputTimeInMins) {
        if (inputTimeInMins >= 1440) {
            inputTimeInMins = 1440;
        }
        if (inputTimeInMins <= 240) {
            inputTimeInMins = 240;
        }

        int mins = inputTimeInMins % 60;
        int hrs = inputTimeInMins / 60;

        int minsDelta = mins % OnBoardingActivity.INTERVAL_MINS;
        if (minsDelta > (OnBoardingActivity.INTERVAL_MINS / 2)) {
            //rounding to UPPER multiple of 30
            mins += (OnBoardingActivity.INTERVAL_MINS - minsDelta);
            if (mins > 59) {
                ++hrs;
                mins = 0;
            }
        } else {
            //rounding to LOWER multiple of 30
            mins -= minsDelta;
        }

        String minString, hrsString = String.valueOf(hrs);
        if (mins == 0) {
            minString = "";
        } else if (mins <= 9) {
            minString = ":0" + mins;
        } else {
            minString = ":" + String.valueOf(mins);
        }

        if (hrs == 24) {
            return "12" + minString + "am";
        } else if (hrs == 12) {
            return hrsString + minString + "pm";
        } else if (hrs < 12) {
            return hrsString + minString + "am";
        } else {
            return (hrs - 12) + minString + "pm";
        }
    }

    @Override
    public void onLongPress(MotionEvent e) {
        System.out.println(new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //System.out.println(new Object() {}.getClass().getEnclosingMethod().getName());
        return false;
    }

    private void showPastScheduleAlert() {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.popup_availability_pastweek_alert);
            final TextView okTV = (TextView) dialog.findViewById(R.id.okTV);
            final TextView tv1 = (TextView) dialog.findViewById(R.id.tv1);
            final TextView tv2 = (TextView) dialog.findViewById(R.id.tv2);
            tv1.setText("This week's Availability is locked because the week has passed.");
            //tv2.setText("To update your Availability, select a future week for which a Schedule has not yet been published.");
            tv2.setText("");
            LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
            okTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showScheduleLockedAlert() {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.popup_availability_pastweek_alert);
            final TextView okTV = (TextView) dialog.findViewById(R.id.okTV);
            final TextView tv1 = (TextView) dialog.findViewById(R.id.tv1);
            final TextView tv2 = (TextView) dialog.findViewById(R.id.tv2);
            tv1.setText("This week's Availability is locked because the schedule has been published.");
            //tv2.setText("To update your Availability, select a future week for which a Schedule has not yet been published.");
            LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
            okTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getActivity() instanceof UpdateAvailabilityActivityNew) {
            if (isPastSchedule) {
                showPastScheduleAlert();
                return false;
            } else if (isScheduleLocked) {
                showScheduleLockedAlert();
                return false;
            }
        }

        isAVUpdated = true;

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            doInitCurrentBGSlotView(v);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            deltaDist = 0;
            isSliderDirectionDecided = false;
            if (isSlidingStarted) {
                ArrayList<Slot> slotsList;
                if (sliderView == sliderViewMonday) {
                    slotsList = mondaySlotsList;
                } else if (sliderView == sliderViewTuesday) {
                    slotsList = tuesdaySlotsList;
                } else if (sliderView == sliderViewWednesday) {
                    slotsList = wednesdaySlotsList;
                } else if (sliderView == sliderViewThursday) {
                    slotsList = thursdaySlotsList;
                } else if (sliderView == sliderViewFriday) {
                    slotsList = fridaySlotsList;
                } else if (sliderView == sliderViewSaturday) {
                    slotsList = saturdaySlotsList;
                } else {
                    slotsList = sundaySlotsList;
                }

                sliderView.setVisibility(View.GONE);
                if (timerPopupWindow != null) {
                    timerPopupWindow.dismiss();
                    timerPopupWindow = null;
                }

                if (sliderStartTime != null || sliderEndTime != null) {
                    int startPos = -1;
                    int endPos = -1;
                    for (int i = 0; i < slotsList.size(); ++i) {
                        Slot slot = slotsList.get(i);
                        String slotStartTime = getTimeStringNew(slot.getActualStartTimeOfSlot());
                        String slotEndTime = getTimeStringNew(slot.getActualEndTimeOfSlot());
                        if (slotStartTime.equalsIgnoreCase(sliderStartTime)) {
                            startPos = i;
                        }
                        if (slotEndTime.equalsIgnoreCase(sliderEndTime)) {
                            endPos = i;
                        }
                    }

                    if (startPos != -1 && endPos != -1) {
                        for (int i = 0; i < slotsList.size(); ++i) {
                            Slot slot = slotsList.get(i);
                            if (i >= startPos && i <= endPos) {
                                slot.setSlotType(isAvailabilityMode ? SlotType.GREEN : SlotType.RED);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < slotsList.size(); ++i) {
                        Slot slot = slotsList.get(i);
                        if (sliderStartMins <= slot.getActualStartTimeOfSlot() && sliderEndMins >= slot.getActualEndTimeOfSlot()) {
                            slot.setSlotType(isAvailabilityMode ? SlotType.GREEN : SlotType.RED);
                        }
                    }
                }
                doUpdateSlotsRowUI(slotsList);
            } else {
                isSlidingStarted = false;
                if (sliderView != null) {
                    sliderView.setVisibility(View.GONE);
                    if (timerPopupWindow != null) {
                        timerPopupWindow.dismiss();
                        timerPopupWindow = null;
                    }
                }
                onTouchUp(v, event);
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            deltaDist = 0;
            this.fingerDownInitialX = event.getX();
            isSlidingStarted = false;
            isSliderDirectionDecided = false;
            doInitCurrentBGSlotView(v);

            int dayLayoutWidth = getDayLayoutWidth();
            float singleSlotWidth = dayLayoutWidth / 40;

            sliderViewMonday.setMinimumWidth((int) singleSlotWidth);
            sliderViewTuesday.setMinimumWidth((int) singleSlotWidth);
            sliderViewWednesday.setMinimumWidth((int) singleSlotWidth);
            sliderViewThursday.setMinimumWidth((int) singleSlotWidth);
            sliderViewFriday.setMinimumWidth((int) singleSlotWidth);
            sliderViewSaturday.setMinimumWidth((int) singleSlotWidth);
            sliderViewSunday.setMinimumWidth((int) singleSlotWidth);
        } else {
            isSlidingStarted = false;
            isSliderDirectionDecided = false;
        }
        gestureScanner.onTouchEvent(event);
        return true;
    }

    private void doInitCurrentBGSlotView(View v) {
        if (v instanceof LinearLayout) {
            this.currentBackgroundSlotView = (LinearLayout) v;
            this.sliderView = getCurrentDaySliderView(currentBackgroundSlotView);
        } else {
            ArrayList<Slot> slotsList = null;
            if (v.getId() == R.id.mondayLabel) {
                this.currentBackgroundSlotView = monday4amTo430amView;
                this.sliderView = sliderViewMonday;
                slotsList = mondaySlotsList;
            } else if (v.getId() == R.id.tuesdayLabel) {
                this.currentBackgroundSlotView = tuesday4amTo430amView;
                this.sliderView = sliderViewTuesday;
                slotsList = tuesdaySlotsList;
            } else if (v.getId() == R.id.wednesdayLabel) {
                this.currentBackgroundSlotView = wednesday4amTo430amView;
                this.sliderView = sliderViewWednesday;
                slotsList = wednesdaySlotsList;
            } else if (v.getId() == R.id.thursdayLabel) {
                this.currentBackgroundSlotView = thursday4amTo430amView;
                this.sliderView = sliderViewThursday;
                slotsList = thursdaySlotsList;
            } else if (v.getId() == R.id.fridayLabel) {
                this.currentBackgroundSlotView = friday4amTo430amView;
                this.sliderView = sliderViewFriday;
                slotsList = fridaySlotsList;
            } else if (v.getId() == R.id.saturdayLabel) {
                this.currentBackgroundSlotView = saturday4amTo430amView;
                this.sliderView = sliderViewSaturday;
                slotsList = saturdaySlotsList;
            } else if (v.getId() == R.id.sundayLabel) {
                this.currentBackgroundSlotView = sunday4amTo430amView;
                this.sliderView = sliderViewSunday;
                slotsList = sundaySlotsList;
            }

            if (slotsList != null) {
                if (!isSliderDirectionDecided) {
                    isLeftTouch = false;
                    isCenterTouch = false;
                    isRightTouch = true;
                    isSliderDirectionDecided = true;
                    if (is_EMPTY_Slot(slotsList.get(0))) {
                        for (int i = 0; i < 1; ++i) {
                            Slot slot = slotsList.get(i);
                            if (is_EMPTY_Slot(slot)) {
                                slot.setSlotType(isAvailabilityMode ? SlotType.GREEN : SlotType.RED);
                            }
                        }
                        doUpdateSlotsRowUI(slotsList);
                    } else {
                        isLeftTouch = true;
                        isCenterTouch = false;
                        isRightTouch = false;
                        for (int i = 0; i < 1; ++i) {
                            Slot slot = slotsList.get(i);
                            slot.setSlotType(isAvailabilityMode ? SlotType.GREEN : SlotType.RED);
                        }
                        doUpdateSlotsRowUI(slotsList);
                    }
                }
            }
        }
    }

    private LinearLayout getCurrentDaySliderView(LinearLayout currentBackgroundSlotView) {
        for (int i = 0; i < ((LinearLayout) currentBackgroundSlotView.getParent()).getChildCount(); ++i) {
            View v = ((LinearLayout) currentBackgroundSlotView.getParent()).getChildAt(i);
            if (v instanceof LinearLayout) {
                if (v.getTag().toString().equals("monday")) {
                    return sliderViewMonday;
                }
                if (v.getTag().toString().equals("tuesday")) {
                    return sliderViewTuesday;
                }
                if (v.getTag().toString().equals("wednesday")) {
                    return sliderViewWednesday;
                }
                if (v.getTag().toString().equals("thursday")) {
                    return sliderViewThursday;
                }
                if (v.getTag().toString().equals("friday")) {
                    return sliderViewFriday;
                }
                if (v.getTag().toString().equals("saturday")) {
                    return sliderViewSaturday;
                }
                return sliderViewSunday;
            }
        }
        return sliderViewMonday;
    }

    private void onTouchUp(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.monday4amTo430am:
                onSlotClick(mondaySlotsList, 0, v, event);
                break;
            case R.id.monday430amTo5am:
                onSlotClick(mondaySlotsList, 1, v, event);
                break;
            case R.id.monday5amTo530am:
                onSlotClick(mondaySlotsList, 2, v, event);
                break;
            case R.id.monday530amTo6am:
                onSlotClick(mondaySlotsList, 3, v, event);
                break;
            case R.id.monday6amTo630am:
                onSlotClick(mondaySlotsList, 4, v, event);
                break;
            case R.id.monday630amTo7am:
                onSlotClick(mondaySlotsList, 5, v, event);
                break;
            case R.id.monday7amTo730am:
                onSlotClick(mondaySlotsList, 6, v, event);
                break;
            case R.id.monday730amTo8am:
                onSlotClick(mondaySlotsList, 7, v, event);
                break;
            case R.id.monday8amTo830am:
                onSlotClick(mondaySlotsList, 8, v, event);
                break;
            case R.id.monday830amTo9am:
                onSlotClick(mondaySlotsList, 9, v, event);
                break;
            case R.id.monday9amTo930am:
                onSlotClick(mondaySlotsList, 10, v, event);
                break;
            case R.id.monday930amTo10am:
                onSlotClick(mondaySlotsList, 11, v, event);
                break;
            case R.id.monday10amTo1030am:
                onSlotClick(mondaySlotsList, 12, v, event);
                break;
            case R.id.monday1030amTo11am:
                onSlotClick(mondaySlotsList, 13, v, event);
                break;
            case R.id.monday11amTo1130am:
                onSlotClick(mondaySlotsList, 14, v, event);
                break;
            case R.id.monday1130amTo12pm:
                onSlotClick(mondaySlotsList, 15, v, event);
                break;
            case R.id.monday12pmTo1230pm:
                onSlotClick(mondaySlotsList, 16, v, event);
                break;
            case R.id.monday1230pmTo1pm:
                onSlotClick(mondaySlotsList, 17, v, event);
                break;
            case R.id.monday1pmTo130pm:
                onSlotClick(mondaySlotsList, 18, v, event);
                break;
            case R.id.monday130pmTo2pm:
                onSlotClick(mondaySlotsList, 19, v, event);
                break;
            case R.id.monday2pmTo230pm:
                onSlotClick(mondaySlotsList, 20, v, event);
                break;
            case R.id.monday230pmTo3pm:
                onSlotClick(mondaySlotsList, 21, v, event);
                break;
            case R.id.monday3pmTo330pm:
                onSlotClick(mondaySlotsList, 22, v, event);
                break;
            case R.id.monday330pmTo4pm:
                onSlotClick(mondaySlotsList, 23, v, event);
                break;
            case R.id.monday4pmTo430pm:
                onSlotClick(mondaySlotsList, 24, v, event);
                break;
            case R.id.monday430pmTo5pm:
                onSlotClick(mondaySlotsList, 25, v, event);
                break;
            case R.id.monday5pmTo530pm:
                onSlotClick(mondaySlotsList, 26, v, event);
                break;
            case R.id.monday530pmTo6pm:
                onSlotClick(mondaySlotsList, 27, v, event);
                break;
            case R.id.monday6pmTo630pm:
                onSlotClick(mondaySlotsList, 28, v, event);
                break;
            case R.id.monday630pmTo7pm:
                onSlotClick(mondaySlotsList, 29, v, event);
                break;
            case R.id.monday7pmTo730pm:
                onSlotClick(mondaySlotsList, 30, v, event);
                break;
            case R.id.monday730pmTo8pm:
                onSlotClick(mondaySlotsList, 31, v, event);
                break;
            case R.id.monday8pmTo830pm:
                onSlotClick(mondaySlotsList, 32, v, event);
                break;
            case R.id.monday830pmTo9pm:
                onSlotClick(mondaySlotsList, 33, v, event);
                break;
            case R.id.monday9pmTo930pm:
                onSlotClick(mondaySlotsList, 34, v, event);
                break;
            case R.id.monday930pmTo10pm:
                onSlotClick(mondaySlotsList, 35, v, event);
                break;
            case R.id.monday10pmTo1030pm:
                onSlotClick(mondaySlotsList, 36, v, event);
                break;
            case R.id.monday1030pmTo11pm:
                onSlotClick(mondaySlotsList, 37, v, event);
                break;
            case R.id.monday11pmTo1130pm:
                onSlotClick(mondaySlotsList, 38, v, event);
                break;
            case R.id.monday1130pmTo12am:
                onSlotClick(mondaySlotsList, 39, v, event);
                break;


            //TUESDAY
            case R.id.tuesday4amTo430am:
                onSlotClick(tuesdaySlotsList, 0, v, event);
                break;
            case R.id.tuesday430amTo5am:
                onSlotClick(tuesdaySlotsList, 1, v, event);
                break;
            case R.id.tuesday5amTo530am:
                onSlotClick(tuesdaySlotsList, 2, v, event);
                break;
            case R.id.tuesday530amTo6am:
                onSlotClick(tuesdaySlotsList, 3, v, event);
                break;
            case R.id.tuesday6amTo630am:
                onSlotClick(tuesdaySlotsList, 4, v, event);
                break;
            case R.id.tuesday630amTo7am:
                onSlotClick(tuesdaySlotsList, 5, v, event);
                break;
            case R.id.tuesday7amTo730am:
                onSlotClick(tuesdaySlotsList, 6, v, event);
                break;
            case R.id.tuesday730amTo8am:
                onSlotClick(tuesdaySlotsList, 7, v, event);
                break;
            case R.id.tuesday8amTo830am:
                onSlotClick(tuesdaySlotsList, 8, v, event);
                break;
            case R.id.tuesday830amTo9am:
                onSlotClick(tuesdaySlotsList, 9, v, event);
                break;
            case R.id.tuesday9amTo930am:
                onSlotClick(tuesdaySlotsList, 10, v, event);
                break;
            case R.id.tuesday930amTo10am:
                onSlotClick(tuesdaySlotsList, 11, v, event);
                break;
            case R.id.tuesday10amTo1030am:
                onSlotClick(tuesdaySlotsList, 12, v, event);
                break;
            case R.id.tuesday1030amTo11am:
                onSlotClick(tuesdaySlotsList, 13, v, event);
                break;
            case R.id.tuesday11amTo1130am:
                onSlotClick(tuesdaySlotsList, 14, v, event);
                break;
            case R.id.tuesday1130amTo12pm:
                onSlotClick(tuesdaySlotsList, 15, v, event);
                break;
            case R.id.tuesday12pmTo1230pm:
                onSlotClick(tuesdaySlotsList, 16, v, event);
                break;
            case R.id.tuesday1230pmTo1pm:
                onSlotClick(tuesdaySlotsList, 17, v, event);
                break;
            case R.id.tuesday1pmTo130pm:
                onSlotClick(tuesdaySlotsList, 18, v, event);
                break;
            case R.id.tuesday130pmTo2pm:
                onSlotClick(tuesdaySlotsList, 19, v, event);
                break;
            case R.id.tuesday2pmTo230pm:
                onSlotClick(tuesdaySlotsList, 20, v, event);
                break;
            case R.id.tuesday230pmTo3pm:
                onSlotClick(tuesdaySlotsList, 21, v, event);
                break;
            case R.id.tuesday3pmTo330pm:
                onSlotClick(tuesdaySlotsList, 22, v, event);
                break;
            case R.id.tuesday330pmTo4pm:
                onSlotClick(tuesdaySlotsList, 23, v, event);
                break;
            case R.id.tuesday4pmTo430pm:
                onSlotClick(tuesdaySlotsList, 24, v, event);
                break;
            case R.id.tuesday430pmTo5pm:
                onSlotClick(tuesdaySlotsList, 25, v, event);
                break;
            case R.id.tuesday5pmTo530pm:
                onSlotClick(tuesdaySlotsList, 26, v, event);
                break;
            case R.id.tuesday530pmTo6pm:
                onSlotClick(tuesdaySlotsList, 27, v, event);
                break;
            case R.id.tuesday6pmTo630pm:
                onSlotClick(tuesdaySlotsList, 28, v, event);
                break;
            case R.id.tuesday630pmTo7pm:
                onSlotClick(tuesdaySlotsList, 29, v, event);
                break;
            case R.id.tuesday7pmTo730pm:
                onSlotClick(tuesdaySlotsList, 30, v, event);
                break;
            case R.id.tuesday730pmTo8pm:
                onSlotClick(tuesdaySlotsList, 31, v, event);
                break;
            case R.id.tuesday8pmTo830pm:
                onSlotClick(tuesdaySlotsList, 32, v, event);
                break;
            case R.id.tuesday830pmTo9pm:
                onSlotClick(tuesdaySlotsList, 33, v, event);
                break;
            case R.id.tuesday9pmTo930pm:
                onSlotClick(tuesdaySlotsList, 34, v, event);
                break;
            case R.id.tuesday930pmTo10pm:
                onSlotClick(tuesdaySlotsList, 35, v, event);
                break;
            case R.id.tuesday10pmTo1030pm:
                onSlotClick(tuesdaySlotsList, 36, v, event);
                break;
            case R.id.tuesday1030pmTo11pm:
                onSlotClick(tuesdaySlotsList, 37, v, event);
                break;
            case R.id.tuesday11pmTo1130pm:
                onSlotClick(tuesdaySlotsList, 38, v, event);
                break;
            case R.id.tuesday1130pmTo12am:
                onSlotClick(tuesdaySlotsList, 39, v, event);
                break;

            //WEDNESDAY
            case R.id.wednesday4amTo430am:
                onSlotClick(wednesdaySlotsList, 0, v, event);
                break;
            case R.id.wednesday430amTo5am:
                onSlotClick(wednesdaySlotsList, 1, v, event);
                break;
            case R.id.wednesday5amTo530am:
                onSlotClick(wednesdaySlotsList, 2, v, event);
                break;
            case R.id.wednesday530amTo6am:
                onSlotClick(wednesdaySlotsList, 3, v, event);
                break;
            case R.id.wednesday6amTo630am:
                onSlotClick(wednesdaySlotsList, 4, v, event);
                break;
            case R.id.wednesday630amTo7am:
                onSlotClick(wednesdaySlotsList, 5, v, event);
                break;
            case R.id.wednesday7amTo730am:
                onSlotClick(wednesdaySlotsList, 6, v, event);
                break;
            case R.id.wednesday730amTo8am:
                onSlotClick(wednesdaySlotsList, 7, v, event);
                break;
            case R.id.wednesday8amTo830am:
                onSlotClick(wednesdaySlotsList, 8, v, event);
                break;
            case R.id.wednesday830amTo9am:
                onSlotClick(wednesdaySlotsList, 9, v, event);
                break;
            case R.id.wednesday9amTo930am:
                onSlotClick(wednesdaySlotsList, 10, v, event);
                break;
            case R.id.wednesday930amTo10am:
                onSlotClick(wednesdaySlotsList, 11, v, event);
                break;
            case R.id.wednesday10amTo1030am:
                onSlotClick(wednesdaySlotsList, 12, v, event);
                break;
            case R.id.wednesday1030amTo11am:
                onSlotClick(wednesdaySlotsList, 13, v, event);
                break;
            case R.id.wednesday11amTo1130am:
                onSlotClick(wednesdaySlotsList, 14, v, event);
                break;
            case R.id.wednesday1130amTo12pm:
                onSlotClick(wednesdaySlotsList, 15, v, event);
                break;
            case R.id.wednesday12pmTo1230pm:
                onSlotClick(wednesdaySlotsList, 16, v, event);
                break;
            case R.id.wednesday1230pmTo1pm:
                onSlotClick(wednesdaySlotsList, 17, v, event);
                break;
            case R.id.wednesday1pmTo130pm:
                onSlotClick(wednesdaySlotsList, 18, v, event);
                break;
            case R.id.wednesday130pmTo2pm:
                onSlotClick(wednesdaySlotsList, 19, v, event);
                break;
            case R.id.wednesday2pmTo230pm:
                onSlotClick(wednesdaySlotsList, 20, v, event);
                break;
            case R.id.wednesday230pmTo3pm:
                onSlotClick(wednesdaySlotsList, 21, v, event);
                break;
            case R.id.wednesday3pmTo330pm:
                onSlotClick(wednesdaySlotsList, 22, v, event);
                break;
            case R.id.wednesday330pmTo4pm:
                onSlotClick(wednesdaySlotsList, 23, v, event);
                break;
            case R.id.wednesday4pmTo430pm:
                onSlotClick(wednesdaySlotsList, 24, v, event);
                break;
            case R.id.wednesday430pmTo5pm:
                onSlotClick(wednesdaySlotsList, 25, v, event);
                break;
            case R.id.wednesday5pmTo530pm:
                onSlotClick(wednesdaySlotsList, 26, v, event);
                break;
            case R.id.wednesday530pmTo6pm:
                onSlotClick(wednesdaySlotsList, 27, v, event);
                break;
            case R.id.wednesday6pmTo630pm:
                onSlotClick(wednesdaySlotsList, 28, v, event);
                break;
            case R.id.wednesday630pmTo7pm:
                onSlotClick(wednesdaySlotsList, 29, v, event);
                break;
            case R.id.wednesday7pmTo730pm:
                onSlotClick(wednesdaySlotsList, 30, v, event);
                break;
            case R.id.wednesday730pmTo8pm:
                onSlotClick(wednesdaySlotsList, 31, v, event);
                break;
            case R.id.wednesday8pmTo830pm:
                onSlotClick(wednesdaySlotsList, 32, v, event);
                break;
            case R.id.wednesday830pmTo9pm:
                onSlotClick(wednesdaySlotsList, 33, v, event);
                break;
            case R.id.wednesday9pmTo930pm:
                onSlotClick(wednesdaySlotsList, 34, v, event);
                break;
            case R.id.wednesday930pmTo10pm:
                onSlotClick(wednesdaySlotsList, 35, v, event);
                break;
            case R.id.wednesday10pmTo1030pm:
                onSlotClick(wednesdaySlotsList, 36, v, event);
                break;
            case R.id.wednesday1030pmTo11pm:
                onSlotClick(wednesdaySlotsList, 37, v, event);
                break;
            case R.id.wednesday11pmTo1130pm:
                onSlotClick(wednesdaySlotsList, 38, v, event);
                break;
            case R.id.wednesday1130pmTo12am:
                onSlotClick(wednesdaySlotsList, 39, v, event);
                break;

            //THURSDAY
            case R.id.thursday4amTo430am:
                onSlotClick(thursdaySlotsList, 0, v, event);
                break;
            case R.id.thursday430amTo5am:
                onSlotClick(thursdaySlotsList, 1, v, event);
                break;
            case R.id.thursday5amTo530am:
                onSlotClick(thursdaySlotsList, 2, v, event);
                break;
            case R.id.thursday530amTo6am:
                onSlotClick(thursdaySlotsList, 3, v, event);
                break;
            case R.id.thursday6amTo630am:
                onSlotClick(thursdaySlotsList, 4, v, event);
                break;
            case R.id.thursday630amTo7am:
                onSlotClick(thursdaySlotsList, 5, v, event);
                break;
            case R.id.thursday7amTo730am:
                onSlotClick(thursdaySlotsList, 6, v, event);
                break;
            case R.id.thursday730amTo8am:
                onSlotClick(thursdaySlotsList, 7, v, event);
                break;
            case R.id.thursday8amTo830am:
                onSlotClick(thursdaySlotsList, 8, v, event);
                break;
            case R.id.thursday830amTo9am:
                onSlotClick(thursdaySlotsList, 9, v, event);
                break;
            case R.id.thursday9amTo930am:
                onSlotClick(thursdaySlotsList, 10, v, event);
                break;
            case R.id.thursday930amTo10am:
                onSlotClick(thursdaySlotsList, 11, v, event);
                break;
            case R.id.thursday10amTo1030am:
                onSlotClick(thursdaySlotsList, 12, v, event);
                break;
            case R.id.thursday1030amTo11am:
                onSlotClick(thursdaySlotsList, 13, v, event);
                break;
            case R.id.thursday11amTo1130am:
                onSlotClick(thursdaySlotsList, 14, v, event);
                break;
            case R.id.thursday1130amTo12pm:
                onSlotClick(thursdaySlotsList, 15, v, event);
                break;
            case R.id.thursday12pmTo1230pm:
                onSlotClick(thursdaySlotsList, 16, v, event);
                break;
            case R.id.thursday1230pmTo1pm:
                onSlotClick(thursdaySlotsList, 17, v, event);
                break;
            case R.id.thursday1pmTo130pm:
                onSlotClick(thursdaySlotsList, 18, v, event);
                break;
            case R.id.thursday130pmTo2pm:
                onSlotClick(thursdaySlotsList, 19, v, event);
                break;
            case R.id.thursday2pmTo230pm:
                onSlotClick(thursdaySlotsList, 20, v, event);
                break;
            case R.id.thursday230pmTo3pm:
                onSlotClick(thursdaySlotsList, 21, v, event);
                break;
            case R.id.thursday3pmTo330pm:
                onSlotClick(thursdaySlotsList, 22, v, event);
                break;
            case R.id.thursday330pmTo4pm:
                onSlotClick(thursdaySlotsList, 23, v, event);
                break;
            case R.id.thursday4pmTo430pm:
                onSlotClick(thursdaySlotsList, 24, v, event);
                break;
            case R.id.thursday430pmTo5pm:
                onSlotClick(thursdaySlotsList, 25, v, event);
                break;
            case R.id.thursday5pmTo530pm:
                onSlotClick(thursdaySlotsList, 26, v, event);
                break;
            case R.id.thursday530pmTo6pm:
                onSlotClick(thursdaySlotsList, 27, v, event);
                break;
            case R.id.thursday6pmTo630pm:
                onSlotClick(thursdaySlotsList, 28, v, event);
                break;
            case R.id.thursday630pmTo7pm:
                onSlotClick(thursdaySlotsList, 29, v, event);
                break;
            case R.id.thursday7pmTo730pm:
                onSlotClick(thursdaySlotsList, 30, v, event);
                break;
            case R.id.thursday730pmTo8pm:
                onSlotClick(thursdaySlotsList, 31, v, event);
                break;
            case R.id.thursday8pmTo830pm:
                onSlotClick(thursdaySlotsList, 32, v, event);
                break;
            case R.id.thursday830pmTo9pm:
                onSlotClick(thursdaySlotsList, 33, v, event);
                break;
            case R.id.thursday9pmTo930pm:
                onSlotClick(thursdaySlotsList, 34, v, event);
                break;
            case R.id.thursday930pmTo10pm:
                onSlotClick(thursdaySlotsList, 35, v, event);
                break;
            case R.id.thursday10pmTo1030pm:
                onSlotClick(thursdaySlotsList, 36, v, event);
                break;
            case R.id.thursday1030pmTo11pm:
                onSlotClick(thursdaySlotsList, 37, v, event);
                break;
            case R.id.thursday11pmTo1130pm:
                onSlotClick(thursdaySlotsList, 38, v, event);
                break;
            case R.id.thursday1130pmTo12am:
                onSlotClick(thursdaySlotsList, 39, v, event);
                break;

            //FRIDAY
            case R.id.friday4amTo430am:
                onSlotClick(fridaySlotsList, 0, v, event);
                break;
            case R.id.friday430amTo5am:
                onSlotClick(fridaySlotsList, 1, v, event);
                break;
            case R.id.friday5amTo530am:
                onSlotClick(fridaySlotsList, 2, v, event);
                break;
            case R.id.friday530amTo6am:
                onSlotClick(fridaySlotsList, 3, v, event);
                break;
            case R.id.friday6amTo630am:
                onSlotClick(fridaySlotsList, 4, v, event);
                break;
            case R.id.friday630amTo7am:
                onSlotClick(fridaySlotsList, 5, v, event);
                break;
            case R.id.friday7amTo730am:
                onSlotClick(fridaySlotsList, 6, v, event);
                break;
            case R.id.friday730amTo8am:
                onSlotClick(fridaySlotsList, 7, v, event);
                break;
            case R.id.friday8amTo830am:
                onSlotClick(fridaySlotsList, 8, v, event);
                break;
            case R.id.friday830amTo9am:
                onSlotClick(fridaySlotsList, 9, v, event);
                break;
            case R.id.friday9amTo930am:
                onSlotClick(fridaySlotsList, 10, v, event);
                break;
            case R.id.friday930amTo10am:
                onSlotClick(fridaySlotsList, 11, v, event);
                break;
            case R.id.friday10amTo1030am:
                onSlotClick(fridaySlotsList, 12, v, event);
                break;
            case R.id.friday1030amTo11am:
                onSlotClick(fridaySlotsList, 13, v, event);
                break;
            case R.id.friday11amTo1130am:
                onSlotClick(fridaySlotsList, 14, v, event);
                break;
            case R.id.friday1130amTo12pm:
                onSlotClick(fridaySlotsList, 15, v, event);
                break;
            case R.id.friday12pmTo1230pm:
                onSlotClick(fridaySlotsList, 16, v, event);
                break;
            case R.id.friday1230pmTo1pm:
                onSlotClick(fridaySlotsList, 17, v, event);
                break;
            case R.id.friday1pmTo130pm:
                onSlotClick(fridaySlotsList, 18, v, event);
                break;
            case R.id.friday130pmTo2pm:
                onSlotClick(fridaySlotsList, 19, v, event);
                break;
            case R.id.friday2pmTo230pm:
                onSlotClick(fridaySlotsList, 20, v, event);
                break;
            case R.id.friday230pmTo3pm:
                onSlotClick(fridaySlotsList, 21, v, event);
                break;
            case R.id.friday3pmTo330pm:
                onSlotClick(fridaySlotsList, 22, v, event);
                break;
            case R.id.friday330pmTo4pm:
                onSlotClick(fridaySlotsList, 23, v, event);
                break;
            case R.id.friday4pmTo430pm:
                onSlotClick(fridaySlotsList, 24, v, event);
                break;
            case R.id.friday430pmTo5pm:
                onSlotClick(fridaySlotsList, 25, v, event);
                break;
            case R.id.friday5pmTo530pm:
                onSlotClick(fridaySlotsList, 26, v, event);
                break;
            case R.id.friday530pmTo6pm:
                onSlotClick(fridaySlotsList, 27, v, event);
                break;
            case R.id.friday6pmTo630pm:
                onSlotClick(fridaySlotsList, 28, v, event);
                break;
            case R.id.friday630pmTo7pm:
                onSlotClick(fridaySlotsList, 29, v, event);
                break;
            case R.id.friday7pmTo730pm:
                onSlotClick(fridaySlotsList, 30, v, event);
                break;
            case R.id.friday730pmTo8pm:
                onSlotClick(fridaySlotsList, 31, v, event);
                break;
            case R.id.friday8pmTo830pm:
                onSlotClick(fridaySlotsList, 32, v, event);
                break;
            case R.id.friday830pmTo9pm:
                onSlotClick(fridaySlotsList, 33, v, event);
                break;
            case R.id.friday9pmTo930pm:
                onSlotClick(fridaySlotsList, 34, v, event);
                break;
            case R.id.friday930pmTo10pm:
                onSlotClick(fridaySlotsList, 35, v, event);
                break;
            case R.id.friday10pmTo1030pm:
                onSlotClick(fridaySlotsList, 36, v, event);
                break;
            case R.id.friday1030pmTo11pm:
                onSlotClick(fridaySlotsList, 37, v, event);
                break;
            case R.id.friday11pmTo1130pm:
                onSlotClick(fridaySlotsList, 38, v, event);
                break;
            case R.id.friday1130pmTo12am:
                onSlotClick(fridaySlotsList, 39, v, event);
                break;

            //SATURDAY
            case R.id.saturday4amTo430am:
                onSlotClick(saturdaySlotsList, 0, v, event);
                break;
            case R.id.saturday430amTo5am:
                onSlotClick(saturdaySlotsList, 1, v, event);
                break;
            case R.id.saturday5amTo530am:
                onSlotClick(saturdaySlotsList, 2, v, event);
                break;
            case R.id.saturday530amTo6am:
                onSlotClick(saturdaySlotsList, 3, v, event);
                break;
            case R.id.saturday6amTo630am:
                onSlotClick(saturdaySlotsList, 4, v, event);
                break;
            case R.id.saturday630amTo7am:
                onSlotClick(saturdaySlotsList, 5, v, event);
                break;
            case R.id.saturday7amTo730am:
                onSlotClick(saturdaySlotsList, 6, v, event);
                break;
            case R.id.saturday730amTo8am:
                onSlotClick(saturdaySlotsList, 7, v, event);
                break;
            case R.id.saturday8amTo830am:
                onSlotClick(saturdaySlotsList, 8, v, event);
                break;
            case R.id.saturday830amTo9am:
                onSlotClick(saturdaySlotsList, 9, v, event);
                break;
            case R.id.saturday9amTo930am:
                onSlotClick(saturdaySlotsList, 10, v, event);
                break;
            case R.id.saturday930amTo10am:
                onSlotClick(saturdaySlotsList, 11, v, event);
                break;
            case R.id.saturday10amTo1030am:
                onSlotClick(saturdaySlotsList, 12, v, event);
                break;
            case R.id.saturday1030amTo11am:
                onSlotClick(saturdaySlotsList, 13, v, event);
                break;
            case R.id.saturday11amTo1130am:
                onSlotClick(saturdaySlotsList, 14, v, event);
                break;
            case R.id.saturday1130amTo12pm:
                onSlotClick(saturdaySlotsList, 15, v, event);
                break;
            case R.id.saturday12pmTo1230pm:
                onSlotClick(saturdaySlotsList, 16, v, event);
                break;
            case R.id.saturday1230pmTo1pm:
                onSlotClick(saturdaySlotsList, 17, v, event);
                break;
            case R.id.saturday1pmTo130pm:
                onSlotClick(saturdaySlotsList, 18, v, event);
                break;
            case R.id.saturday130pmTo2pm:
                onSlotClick(saturdaySlotsList, 19, v, event);
                break;
            case R.id.saturday2pmTo230pm:
                onSlotClick(saturdaySlotsList, 20, v, event);
                break;
            case R.id.saturday230pmTo3pm:
                onSlotClick(saturdaySlotsList, 21, v, event);
                break;
            case R.id.saturday3pmTo330pm:
                onSlotClick(saturdaySlotsList, 22, v, event);
                break;
            case R.id.saturday330pmTo4pm:
                onSlotClick(saturdaySlotsList, 23, v, event);
                break;
            case R.id.saturday4pmTo430pm:
                onSlotClick(saturdaySlotsList, 24, v, event);
                break;
            case R.id.saturday430pmTo5pm:
                onSlotClick(saturdaySlotsList, 25, v, event);
                break;
            case R.id.saturday5pmTo530pm:
                onSlotClick(saturdaySlotsList, 26, v, event);
                break;
            case R.id.saturday530pmTo6pm:
                onSlotClick(saturdaySlotsList, 27, v, event);
                break;
            case R.id.saturday6pmTo630pm:
                onSlotClick(saturdaySlotsList, 28, v, event);
                break;
            case R.id.saturday630pmTo7pm:
                onSlotClick(saturdaySlotsList, 29, v, event);
                break;
            case R.id.saturday7pmTo730pm:
                onSlotClick(saturdaySlotsList, 30, v, event);
                break;
            case R.id.saturday730pmTo8pm:
                onSlotClick(saturdaySlotsList, 31, v, event);
                break;
            case R.id.saturday8pmTo830pm:
                onSlotClick(saturdaySlotsList, 32, v, event);
                break;
            case R.id.saturday830pmTo9pm:
                onSlotClick(saturdaySlotsList, 33, v, event);
                break;
            case R.id.saturday9pmTo930pm:
                onSlotClick(saturdaySlotsList, 34, v, event);
                break;
            case R.id.saturday930pmTo10pm:
                onSlotClick(saturdaySlotsList, 35, v, event);
                break;
            case R.id.saturday10pmTo1030pm:
                onSlotClick(saturdaySlotsList, 36, v, event);
                break;
            case R.id.saturday1030pmTo11pm:
                onSlotClick(saturdaySlotsList, 37, v, event);
                break;
            case R.id.saturday11pmTo1130pm:
                onSlotClick(saturdaySlotsList, 38, v, event);
                break;
            case R.id.saturday1130pmTo12am:
                onSlotClick(saturdaySlotsList, 39, v, event);
                break;

            //SUNDAY
            case R.id.sunday4amTo430am:
                onSlotClick(sundaySlotsList, 0, v, event);
                break;
            case R.id.sunday430amTo5am:
                onSlotClick(sundaySlotsList, 1, v, event);
                break;
            case R.id.sunday5amTo530am:
                onSlotClick(sundaySlotsList, 2, v, event);
                break;
            case R.id.sunday530amTo6am:
                onSlotClick(sundaySlotsList, 3, v, event);
                break;
            case R.id.sunday6amTo630am:
                onSlotClick(sundaySlotsList, 4, v, event);
                break;
            case R.id.sunday630amTo7am:
                onSlotClick(sundaySlotsList, 5, v, event);
                break;
            case R.id.sunday7amTo730am:
                onSlotClick(sundaySlotsList, 6, v, event);
                break;
            case R.id.sunday730amTo8am:
                onSlotClick(sundaySlotsList, 7, v, event);
                break;
            case R.id.sunday8amTo830am:
                onSlotClick(sundaySlotsList, 8, v, event);
                break;
            case R.id.sunday830amTo9am:
                onSlotClick(sundaySlotsList, 9, v, event);
                break;
            case R.id.sunday9amTo930am:
                onSlotClick(sundaySlotsList, 10, v, event);
                break;
            case R.id.sunday930amTo10am:
                onSlotClick(sundaySlotsList, 11, v, event);
                break;
            case R.id.sunday10amTo1030am:
                onSlotClick(sundaySlotsList, 12, v, event);
                break;
            case R.id.sunday1030amTo11am:
                onSlotClick(sundaySlotsList, 13, v, event);
                break;
            case R.id.sunday11amTo1130am:
                onSlotClick(sundaySlotsList, 14, v, event);
                break;
            case R.id.sunday1130amTo12pm:
                onSlotClick(sundaySlotsList, 15, v, event);
                break;
            case R.id.sunday12pmTo1230pm:
                onSlotClick(sundaySlotsList, 16, v, event);
                break;
            case R.id.sunday1230pmTo1pm:
                onSlotClick(sundaySlotsList, 17, v, event);
                break;
            case R.id.sunday1pmTo130pm:
                onSlotClick(sundaySlotsList, 18, v, event);
                break;
            case R.id.sunday130pmTo2pm:
                onSlotClick(sundaySlotsList, 19, v, event);
                break;
            case R.id.sunday2pmTo230pm:
                onSlotClick(sundaySlotsList, 20, v, event);
                break;
            case R.id.sunday230pmTo3pm:
                onSlotClick(sundaySlotsList, 21, v, event);
                break;
            case R.id.sunday3pmTo330pm:
                onSlotClick(sundaySlotsList, 22, v, event);
                break;
            case R.id.sunday330pmTo4pm:
                onSlotClick(sundaySlotsList, 23, v, event);
                break;
            case R.id.sunday4pmTo430pm:
                onSlotClick(sundaySlotsList, 24, v, event);
                break;
            case R.id.sunday430pmTo5pm:
                onSlotClick(sundaySlotsList, 25, v, event);
                break;
            case R.id.sunday5pmTo530pm:
                onSlotClick(sundaySlotsList, 26, v, event);
                break;
            case R.id.sunday530pmTo6pm:
                onSlotClick(sundaySlotsList, 27, v, event);
                break;
            case R.id.sunday6pmTo630pm:
                onSlotClick(sundaySlotsList, 28, v, event);
                break;
            case R.id.sunday630pmTo7pm:
                onSlotClick(sundaySlotsList, 29, v, event);
                break;
            case R.id.sunday7pmTo730pm:
                onSlotClick(sundaySlotsList, 30, v, event);
                break;
            case R.id.sunday730pmTo8pm:
                onSlotClick(sundaySlotsList, 31, v, event);
                break;
            case R.id.sunday8pmTo830pm:
                onSlotClick(sundaySlotsList, 32, v, event);
                break;
            case R.id.sunday830pmTo9pm:
                onSlotClick(sundaySlotsList, 33, v, event);
                break;
            case R.id.sunday9pmTo930pm:
                onSlotClick(sundaySlotsList, 34, v, event);
                break;
            case R.id.sunday930pmTo10pm:
                onSlotClick(sundaySlotsList, 35, v, event);
                break;
            case R.id.sunday10pmTo1030pm:
                onSlotClick(sundaySlotsList, 36, v, event);
                break;
            case R.id.sunday1030pmTo11pm:
                onSlotClick(sundaySlotsList, 37, v, event);
                break;
            case R.id.sunday11pmTo1130pm:
                onSlotClick(sundaySlotsList, 38, v, event);
                break;
            case R.id.sunday1130pmTo12am:
                onSlotClick(sundaySlotsList, 39, v, event);
                break;
        }
    }

    public boolean isSlotBehindSliderGreen() {
        if (currentBackgroundSlotView == null) {
            return false;
        }
        if (currentBackgroundSlotView.getBackground() instanceof ColorDrawable) {
            if (((ColorDrawable) currentBackgroundSlotView.getBackground()).getColor() == Color.parseColor("#66BB6A")) {
                return true;
            }
        } else if (currentBackgroundSlotView.getBackground() instanceof GradientDrawable) {
            ArrayList<Slot> slotsList;
            if (sliderView == sliderViewMonday) {
                slotsList = mondaySlotsList;
            } else if (sliderView == sliderViewTuesday) {
                slotsList = tuesdaySlotsList;
            } else if (sliderView == sliderViewWednesday) {
                slotsList = wednesdaySlotsList;
            } else if (sliderView == sliderViewThursday) {
                slotsList = thursdaySlotsList;
            } else if (sliderView == sliderViewFriday) {
                slotsList = fridaySlotsList;
            } else if (sliderView == sliderViewSaturday) {
                slotsList = saturdaySlotsList;
            } else {
                slotsList = sundaySlotsList;
            }
            int selectedPos = 0;
            for (int i = 0; i < slotsList.size(); ++i) {
                Slot slot = slotsList.get(i);
                if (slot.getView() == currentBackgroundSlotView) {
                    selectedPos = i;
                    break;
                }
            }
            return is_GREEN_Slot(slotsList.get(selectedPos));
        }
        return false;
    }

    boolean isSlotBehindSliderRed() {
        if (currentBackgroundSlotView == null) {
            return false;
        }
        if (currentBackgroundSlotView.getBackground() instanceof ColorDrawable) {
            if (((ColorDrawable) currentBackgroundSlotView.getBackground()).getColor() == Color.parseColor("#D02D2D")) {
                return true;
            }
        } else if (currentBackgroundSlotView.getBackground() instanceof GradientDrawable) {
            ArrayList<Slot> slotsList;
            if (sliderView == sliderViewMonday) {
                slotsList = mondaySlotsList;
            } else if (sliderView == sliderViewTuesday) {
                slotsList = tuesdaySlotsList;
            } else if (sliderView == sliderViewWednesday) {
                slotsList = wednesdaySlotsList;
            } else if (sliderView == sliderViewThursday) {
                slotsList = thursdaySlotsList;
            } else if (sliderView == sliderViewFriday) {
                slotsList = fridaySlotsList;
            } else if (sliderView == sliderViewSaturday) {
                slotsList = saturdaySlotsList;
            } else {
                slotsList = sundaySlotsList;
            }

            int selectedPos = 0;
            for (int i = 0; i < slotsList.size(); ++i) {
                Slot slot = slotsList.get(i);
                if (slot.getView() == currentBackgroundSlotView) {
                    selectedPos = i;
                    break;
                }
            }
            return is_RED_Slot(slotsList.get(selectedPos));
        }
        return false;
    }

    void showTimerPopup(MotionEvent endMotionEvent) {
        sliderViewLocation = new int[2];
        sliderView.getLocationInWindow(sliderViewLocation);

        float popupX = 0, popupY = 0;
        if (timerPopupWindow != null && popupView != null) {
            if (isRightTouch) {
                popupX = sliderViewLocation[0] + sliderView.getWidth() - (popupView.getWidth() / 2);
            } else if (isLeftTouch) {
                popupX = sliderViewLocation[0] - (popupView.getWidth() / 2);
            } else {
                popupX = sliderViewLocation[0] + sliderView.getWidth() / 2 - (popupView.getWidth() / 2);
            }
            popupY = sliderViewLocation[1] - popupView.getHeight();
        }

        if (timerPopupWindow == null || timerTextView == null) {
            popupView = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.popup_availability_time_feedback, null);
            timerTextView = (TextView) popupView.findViewById(R.id.timerTextView);
            timerPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            timerPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LegionUtils.doApplyFont(getActivity().getAssets(), popupView);
            timerPopupWindow.showAtLocation(parentLayout, Gravity.NO_GRAVITY, (int) endMotionEvent.getX(), (int) endMotionEvent.getY());
        } else {
            timerPopupWindow.update((int) popupX, (int) popupY, -1, -1, true);
        }

        String popupText = ((TextView) sliderView.getChildAt(0)).getText().toString();
        if (popupText.length() >= 1) {
            String[] tokens = popupText.split("-");

            String startToken = tokens[0].trim();
            String endToken = tokens[1].trim();
            if (!startToken.contains(":")) {
                if (startToken.contains("am")) {
                    startToken = startToken.replace("am", "") + ":00am";
                } else if (startToken.contains("pm")) {
                    startToken = startToken.replace("pm", "") + ":00pm";
                }
            }

            if (!endToken.contains(":")) {
                if (endToken.contains("am")) {
                    endToken = endToken.replace("am", "") + ":00am";
                } else if (endToken.contains("pm")) {
                    endToken = endToken.replace("pm", "") + ":00pm";
                }
            }
            timerTextView.setText(startToken + " - " + endToken);
        } else {
            //mPopupWindow.dismiss();
        }
    }

    public int getDayLayoutWidth() {
        if (mondaySlotsParentLL.getVisibility() == View.VISIBLE) {
            return mondaySlotsParentLL.getWidth();
        }
        if (tuesdaySlotsParentLL.getVisibility() == View.VISIBLE) {
            return tuesdaySlotsParentLL.getWidth();
        }
        if (wednesdaySlotsParentLL.getVisibility() == View.VISIBLE) {
            return wednesdaySlotsParentLL.getWidth();
        }
        if (thursdaySlotsParentLL.getVisibility() == View.VISIBLE) {
            return thursdaySlotsParentLL.getWidth();
        }
        if (fridaySlotsParentLL.getVisibility() == View.VISIBLE) {
            return fridaySlotsParentLL.getWidth();
        }
        if (saturdaySlotsParentLL.getVisibility() == View.VISIBLE) {
            return saturdaySlotsParentLL.getWidth();
        }
        if (sundaySlotsParentLL.getVisibility() == View.VISIBLE) {
            return sundaySlotsParentLL.getWidth();
        }
        return 1;
    }

    public class AvailabiltySlotActionPopup {
        protected WindowManager mWindowManager;
        protected Context mContext;
        protected PopupWindow mWindow;
        protected View mView;
        protected Drawable mBackgroundDrawable = null;
        Slot selectedSlot;
        ArrayList<Slot> slotsList;
        int position;
        private ImageView mUpImageView;
        private ImageView mDownImageView;

        public AvailabiltySlotActionPopup(Context context, int viewResource) {
            mContext = context;
            mWindow = new PopupWindow(context);
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(viewResource, null);
            setContentView(view);
            LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));
            mUpImageView = (ImageView) mView.findViewById(R.id.arrow_up);
            mDownImageView = (ImageView) mView.findViewById(R.id.arrow_down);
            LinearLayout deleteSlotLL = (LinearLayout) mView.findViewById(R.id.deleteLL);
            LinearLayout customSlotLL = (LinearLayout) mView.findViewById(R.id.customSlotLL);

            deleteSlotLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doEnableSaveButtonIfRequired();
                    try {
                        if (is_GREEN_Slot(selectedSlot)) {
                            for (int i = selectedSlot.getSlotSerialNumber(); i < slotsList.size(); ++i) {
                                if (is_GREEN_Slot(slotsList.get(i))) {
                                    slotsList.get(i).setSlotType(SlotType.EMPTY);
                                } else {
                                    break;
                                }
                            }
                        } else if (is_RED_Slot(selectedSlot)) {
                            for (int i = selectedSlot.getSlotSerialNumber(); i < slotsList.size(); ++i) {

                                if (is_RED_Slot(slotsList.get(i))) {
                                    slotsList.get(i).setSlotType(SlotType.EMPTY);
                                } else {
                                    break;
                                }
                            }
                        }
                        mWindow.dismiss();
                        doUpdateSlotsRowUI(slotsList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            customSlotLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindow.dismiss();
                    onCustomAvailabilityClick(position, slotsList);
                }
            });
        }

        public AvailabiltySlotActionPopup(Context context, Slot selectedSlot, ArrayList<Slot> slotsList, int position) {
            this(context, R.layout.customavailabity_popup);
            this.selectedSlot = selectedSlot;
            this.slotsList = slotsList;
            this.position = position;
        }

        public void show(View anchor) {
            preShow();
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
            mView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int rootHeight = mView.getMeasuredHeight();
            int rootWidth = mView.getMeasuredWidth();
            final int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
            final int screenHeight = mWindowManager.getDefaultDisplay().getHeight();
            int yPos = anchorRect.top - rootHeight;
            boolean onTop = true;
            if (anchorRect.top < screenHeight / 2) {
                yPos = anchorRect.bottom;
                onTop = false;
            } else {
                yPos = yPos + 16;
            }
            int whichArrow, requestedX;
            whichArrow = ((onTop) ? R.id.arrow_down : R.id.arrow_up);
            requestedX = anchorRect.centerX();
            View arrow = whichArrow == R.id.arrow_up ? mUpImageView : mDownImageView;
            View hideArrow = whichArrow == R.id.arrow_up ? mDownImageView : mUpImageView;
            final int arrowWidth = arrow.getMeasuredWidth();

            arrow.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) arrow.getLayoutParams();
            hideArrow.setVisibility(View.INVISIBLE);
            int xPos = 0;
            // ETXTREME RIGHT CLIKED
            if (anchorRect.left + rootWidth > screenWidth) {
                xPos = (screenWidth - rootWidth);
            }
            // ETXTREME LEFT CLIKED
            else if (anchorRect.left - (rootWidth / 2) < 0) {
                xPos = anchorRect.left;
            }
            // INBETWEEN
            else {
                xPos = (anchorRect.centerX() - (rootWidth / 2));
            }

            param.leftMargin = (requestedX - xPos) - (arrowWidth / 2);

            if (onTop) {
            } else {
            }
            mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos + 50, yPos);
        }

        protected void preShow() {
            if (mView == null)
                throw new IllegalStateException("view undefined");

            if (mBackgroundDrawable == null)
                mWindow.setBackgroundDrawable(new BitmapDrawable());
            else
                mWindow.setBackgroundDrawable(mBackgroundDrawable);

            mWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            mWindow.setTouchable(true);
            mWindow.setFocusable(true);
            mWindow.setOutsideTouchable(true);

            mWindow.setContentView(mView);
        }

        public void setBackgroundDrawable(Drawable background) {
            mBackgroundDrawable = background;
        }

        public void setContentView(View root) {
            mView = root;
            mWindow.setContentView(root);
        }

        public void setContentView(int layoutResID) {
            LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            setContentView(inflator.inflate(layoutResID, null));
        }

        public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
            mWindow.setOnDismissListener(listener);
        }
    }

}