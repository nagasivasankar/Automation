package fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.jeesoft.widget.pickerview.CharacterPickerView;
import cn.jeesoft.widget.pickerview.OnOptionChangedListener;
import co.legion.client.R;
import co.legion.client.activities.OnBoardingActivity;
import models.Slot;
import models.SlotType;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 12/7/2016.
 */
public class GranularAvailabilityDialogFragment extends DialogFragment implements View.OnClickListener, Legion_Constants {

    protected OnDialogClickListener callback = null;
    private TextView tvMonday;
    private TextView tvTuesday;
    private TextView tvWednesday;
    private TextView tvThursday;
    private TextView tvFriday;
    private TextView tvSaturday;
    private TextView tvSunday;
    private TextView tvCancel;
    private TextView tvApply;
    private Dialog dialog;
    private int endTime;
    private int startTime;
    private LinkedHashMap<Integer, String> pickerTimesList = new LinkedHashMap<>();
    private Slot clickedSlot;
    private CharacterPickerView rightWheelView = null;
    private CharacterPickerView leftWheelView;
    private View line;
    private int startMinsPos, endMinsPos;
    private String granularType;
    private int preSelectStartTime;
    private int preSelectEndTime;
    private long lastClickTime;
    private ImageView selectedIv;
    private TextView customTitleTv;
    private boolean rightWheelViewBoolean;

    public GranularAvailabilityDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.availability_dialog_fragment, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LegionUtils.doApplyFont(getActivity().getAssets(), (LinearLayout) view.findViewById(R.id.parentLayout));
        this.dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        tvMonday = (TextView) view.findViewById(R.id.tv_monday);
        line = view.findViewById(R.id.line);
        selectedIv = (ImageView) view.findViewById(R.id.selectedIv);
        tvMonday = (TextView) view.findViewById(R.id.tv_monday);
        tvTuesday = (TextView) view.findViewById(R.id.tv_tuesday);
        tvWednesday = (TextView) view.findViewById(R.id.tv_wednesday);
        tvThursday = (TextView) view.findViewById(R.id.tv_thursday);
        tvFriday = (TextView) view.findViewById(R.id.tv_friday);
        tvSaturday = (TextView) view.findViewById(R.id.tv_saturday);
        tvSunday = (TextView) view.findViewById(R.id.tv_sunday);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvApply = (TextView) view.findViewById(R.id.tv_apply);
        customTitleTv = (TextView) view.findViewById(R.id.customTitleTv);
        tvMonday.setOnClickListener(this);
        tvTuesday.setOnClickListener(this);
        tvWednesday.setOnClickListener(this);
        tvThursday.setOnClickListener(this);
        tvFriday.setOnClickListener(this);
        tvSaturday.setOnClickListener(this);
        tvSunday.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        tvMonday.setSelected(false);
        tvTuesday.setSelected(false);
        tvWednesday.setSelected(false);
        tvThursday.setSelected(false);
        tvFriday.setSelected(false);
        tvSaturday.setSelected(false);
        tvSunday.setSelected(false);
        /*tvMonday.setEnabled(false);
        tvTuesday.setEnabled(false);
        tvWednesday.setEnabled(false);
        tvThursday.setEnabled(false);
        tvFriday.setEnabled(false);
        tvSaturday.setEnabled(false);
        tvSunday.setEnabled(false);*/

        initFragment(view);

        leftWheelView.findViewById(R.id.j_layout1).setBackgroundColor(Color.parseColor("#FCFAFA"));
        leftWheelView.findViewById(R.id.j_layout2).setBackgroundColor(Color.parseColor("#FCFAFA"));
        leftWheelView.findViewById(R.id.j_layout3).setBackgroundColor(Color.parseColor("#FCFAFA"));
        rightWheelView.findViewById(R.id.j_layout1).setBackgroundColor(Color.parseColor("#FCFAFA"));
        rightWheelView.findViewById(R.id.j_layout2).setBackgroundColor(Color.parseColor("#FCFAFA"));
        rightWheelView.findViewById(R.id.j_layout3).setBackgroundColor(Color.parseColor("#FCFAFA"));
    }

    private void initFragment(View view) {
        startTime = getArguments().getInt(Extras_Keys.PICKER_START_TIME);
        endTime = getArguments().getInt(Extras_Keys.PICKER_END_TIME);
        preSelectStartTime = getArguments().getInt(Extras_Keys.CURRENT_SLOT_START_TIME);
        preSelectEndTime = getArguments().getInt(Extras_Keys.CURRENT_SLOT_END_TIME);
        Log.v("preSelectStartTime", ""+preSelectStartTime);
        Log.v("preSelectEndTime", ""+preSelectEndTime);

        clickedSlot = (Slot) getArguments().getSerializable("slot");
        if (clickedSlot != null) {
            int dayOfWeek = clickedSlot.getDayOfTheWeek();
            if (dayOfWeek == 1) {
                tvMonday.performClick();
            } else if (dayOfWeek == 2) {
                tvTuesday.performClick();
            } else if (dayOfWeek == 3) {
                tvWednesday.performClick();
            } else if (dayOfWeek == 4) {
                tvThursday.performClick();
            } else if (dayOfWeek == 5) {
                tvFriday.performClick();
            } else if (dayOfWeek == 6) {
                tvSaturday.performClick();
            } else if (dayOfWeek == 7) {
                tvSunday.performClick();
            }
        }
        granularType = getArguments().getString("granularType");
        if (granularType.equals(SlotType.GREEN.getType())) {
            line.setBackgroundColor(Color.parseColor("#66BB6A"));
            //setTitle("Customize");
        } else {
            line.setBackgroundColor(Color.parseColor("#D02D2D"));
            //setTitle("Customize");
        }

        leftWheelView = (CharacterPickerView) view.findViewById(R.id.left_wheel_view);
        doSetPicker(leftWheelView, pickerTimesList, startTime, endTime, preSelectStartTime, false);
        leftWheelView.setOnOptionChangedListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int i, int i1, int i2) {
                try {
                    tvApply.setEnabled(true);
                    tvApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
                    // validation bn two slots : min is 30 mins
                    if ((i > endMinsPos) && rightWheelViewBoolean) {
                        rightWheelView.setCurrentItems(i, 0, 0);
                    }
                    startMinsPos = i;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        rightWheelView = (CharacterPickerView) view.findViewById(R.id.right_wheel_view);
        doSetPicker(rightWheelView, pickerTimesList, startTime, endTime, preSelectEndTime, true);
        rightWheelView.setOnOptionChangedListener(new OnOptionChangedListener() {
            @Override
            public void onOptionChanged(int i, int i1, int i2) {
                try {
                    // validation bn two slots : min is 30 mins
                    rightWheelViewBoolean =true;
                    if ((i < startMinsPos)) {
                        leftWheelView.setCurrentItems(i, 0, 0);
                    }
                    tvApply.setEnabled(true);
                    tvApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
                    endMinsPos = i;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTitle(String title) {
        customTitleTv.setText(title);
        customTitleTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if ((System.currentTimeMillis() - lastClickTime) < 100) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        tvApply.setEnabled(true);
        tvCancel.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_brown));
        int numOfDaysSelected = getNumOfDaysSelected();
        if (v.getId() == R.id.tv_monday) {
            if (numOfDaysSelected >= 2 && tvMonday.isSelected()) {
                tvMonday.setSelected(false);
                tvMonday.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvMonday.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_yash));
            } else {
                tvMonday.setBackgroundResource(R.color.dark_skyblue);
                tvMonday.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvMonday.setSelected(true);
                tvApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
            }
        } else if (v.getId() == R.id.tv_tuesday) {
            if (numOfDaysSelected >= 2 && tvTuesday.isSelected()) {
                tvTuesday.setSelected(false);
                tvTuesday.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvTuesday.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_yash));
            } else {
                tvTuesday.setBackgroundResource(R.color.dark_skyblue);
                tvTuesday.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvTuesday.setSelected(true);
                tvApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
            }
        } else if (v.getId() == R.id.tv_wednesday) {
            if (numOfDaysSelected >= 2 && tvWednesday.isSelected()) {
                tvWednesday.setSelected(false);
                tvWednesday.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvWednesday.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_yash));
            } else {
                tvWednesday.setBackgroundResource(R.color.dark_skyblue);
                tvWednesday.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvWednesday.setSelected(true);
                tvApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
            }
        } else if (v.getId() == R.id.tv_thursday) {
            if (numOfDaysSelected >= 2 && tvThursday.isSelected()) {
                tvThursday.setSelected(false);
                tvThursday.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvThursday.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_yash));
            } else {
                tvThursday.setBackgroundResource(R.color.dark_skyblue);
                tvThursday.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvThursday.setSelected(true);
                tvApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
            }
        } else if (v.getId() == R.id.tv_friday) {
            if (numOfDaysSelected >= 2 && tvFriday.isSelected()) {
                tvFriday.setSelected(false);
                tvFriday.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvFriday.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_yash));
            } else {
                tvFriday.setBackgroundResource(R.color.dark_skyblue);
                tvFriday.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvFriday.setSelected(true);
                tvApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
            }
        } else if (v.getId() == R.id.tv_saturday) {
            if (numOfDaysSelected >= 2 && tvSaturday.isSelected()) {
                tvSaturday.setSelected(false);
                tvSaturday.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvSaturday.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_yash));
            } else {
                tvSaturday.setBackgroundResource(R.color.dark_skyblue);
                tvSaturday.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvSaturday.setSelected(true);
                tvApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
            }
        } else if (v.getId() == R.id.tv_sunday) {
            if (numOfDaysSelected >= 2 && tvSunday.isSelected()) {
                tvSunday.setSelected(false);
                tvSunday.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvSunday.setTextColor(ContextCompat.getColor(getActivity(), R.color.light_yash));
            } else {
                tvSunday.setBackgroundResource(R.color.dark_skyblue);
                tvSunday.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                tvSunday.setSelected(true);
                tvApply.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
            }
        } else if (v.getId() == R.id.tv_cancel) {
            tvCancel.setTextColor(ContextCompat.getColor(getActivity(), R.color.dark_skyblue));
            dialog.dismiss();
        } else if (v.getId() == R.id.tv_apply) {
            if (endMinsPos < startMinsPos) {
                dialog.dismiss();
                return;
            }

            ArrayList<Integer> list1 = new ArrayList<>();
            ArrayList<Integer> list2 = new ArrayList<>();
            for (Map.Entry<Integer, String> entry : pickerTimesList.entrySet()) {
                list1.add(entry.getKey());
            }
            for (Map.Entry<Integer, String> entry : pickerTimesList.entrySet()) {
                list2.add(entry.getKey());
            }
            callback.onApplyClick(list1.get(startMinsPos), list2.get(endMinsPos + 1), getPTODays(), clickedSlot, granularType.equals(SlotType.GREEN.getType()) ? SlotType.GREEN : SlotType.RED);
            dialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        if (dialog == null || dialog.getWindow() == null) {
            return;
        }
        int dialogWidth = (int) (width * 0.95f);
        int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);
    }

    public void doSetPicker(CharacterPickerView view, LinkedHashMap<Integer, String> pickerTimesList, int startTime, int endTime, int preSelectTime, boolean isEndTimePicker) {

        int initialPos = 0;
        if (isEndTimePicker) {
            initialPos = pickerTimesList.size()-1;
        }

        for (int i = startTime; i <= endTime; i += OnBoardingActivity.INTERVAL_MINS) {
            pickerTimesList.put(i, doConvertMinsToTime(i));
        }

        ArrayList<String> pickerItems = new ArrayList<>();
        int i = 0;
        for (Map.Entry<Integer, String> entry : pickerTimesList.entrySet()) {
            if (entry.getKey() == preSelectTime) {
                if(i!=0 &&  isEndTimePicker) {
                    initialPos = i - 1;
                }else{
                    initialPos = i;
                }
            }
            pickerItems.add(entry.getValue());
            ++i;
        }
        if (!isEndTimePicker) {
            pickerItems.remove(pickerItems.size() - 1);
        } else {
            pickerItems.remove(0);
        }
        view.setPicker(pickerItems);
        view.setCurrentItems(initialPos, 0, 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvApply.setEnabled(false);
                tvApply.setTextColor(Color.parseColor("#73157EFB"));
            }
        }, 300);
    }

    private String doConvertMinsToTime(int intputTimeInMins) {
        int mins = intputTimeInMins % 60;
        int hrs = intputTimeInMins / 60;

        String minString, hrsString = String.valueOf(hrs);
        if (mins <= 9) {
            minString = "0" + mins;
        } else {
            minString = String.valueOf(mins);
        }
        if (hrs == 24) {
            return "12:" + minString + " am";
        } else if (hrs == 12) {
            return hrsString + ":" + minString + " pm";
        } else if (hrs < 12) {
            return hrsString + ":" + minString + " am";
        } else {
            return (hrs - 12) + ":" + minString + " pm";
        }
    }

    public ArrayList<Boolean> getPTODays() {
        ArrayList<Boolean> ptoDays = new ArrayList<>();
        ptoDays.add(tvMonday.isSelected());
        ptoDays.add(tvTuesday.isSelected());
        ptoDays.add(tvWednesday.isSelected());
        ptoDays.add(tvThursday.isSelected());
        ptoDays.add(tvFriday.isSelected());
        ptoDays.add(tvSaturday.isSelected());
        ptoDays.add(tvSunday.isSelected());
        return ptoDays;
    }

    public int getNumOfDaysSelected() {
        int days = 0;
        if (tvMonday.isSelected()) {
            ++days;
        }
        if (tvTuesday.isSelected()) {
            ++days;
        }
        if (tvWednesday.isSelected()) {
            ++days;
        }
        if (tvThursday.isSelected()) {
            ++days;
        }
        if (tvFriday.isSelected()) {
            ++days;
        }
        if (tvSaturday.isSelected()) {
            ++days;
        }
        if (tvSunday.isSelected()) {
            ++days;
        }
        return days;
    }

    public void setOnDialogClickListener(OnDialogClickListener l) {
        callback = l;
    }

    public interface OnDialogClickListener {
        void onApplyClick(int startMins, int endMins, ArrayList<Boolean> ptoDays, Slot selectedSlot, SlotType slotType);
    }
}
