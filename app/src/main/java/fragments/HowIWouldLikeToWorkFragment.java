package fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;

import co.legion.client.R;
import helpers.Legion_PrefsManager;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/16/2016.
 */

@SuppressLint("ValidFragment")
public class HowIWouldLikeToWorkFragment extends Fragment implements Legion_NetworkCallback, Legion_Constants {
    RangeBar hourWeekSeekBar, shiftWeekSeeKBar;
    RangeBar shiftLegnthSeekBar;
    TextView shiftCountTextView, shiftWeekTextview, hoursWeekCount;
    Switch otherLocationToggleButton;
    String[] shiftLengthArray = new String[]{"2", "3", "4", "5", "6", "7", "8"};
    String[] shifthourWeekArray = new String[41];
    String[] shiftWeekArray = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    TextView toggleText;
    TextView saveBtnStatus;
    boolean firstTime = false;
    private Legion_PrefsManager legionPreferences;

    @SuppressLint("ValidFragment")
    public HowIWouldLikeToWorkFragment(TextView saveBtnStatus) {
        if (saveBtnStatus != null) {
            this.saveBtnStatus = saveBtnStatus;
            saveBtnStatus.setEnabled(false);
        }
        for (int i = 0; i < shifthourWeekArray.length; i++) {
            shifthourWeekArray[i] = "" + i;
        }
    }

    public void enableImageView() {
        if (saveBtnStatus != null && firstTime) {
            saveBtnStatus.setEnabled(true);
            saveBtnStatus.setTextColor(Color.WHITE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_howiwouldliketowork, null);
        otherLocationToggleButton = (Switch) view.findViewById(R.id.locationSwitch);
        otherLocationToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enableImageView();
            }
        });

        shiftLegnthSeekBar = (RangeBar) view.findViewById(R.id.shiftLengthSeekbar);
        hourWeekSeekBar = (RangeBar) view.findViewById(R.id.hoursWeekCountSeekBar);
        shiftWeekSeeKBar = (RangeBar) view.findViewById(R.id.shiftWeekSeekBar);

        shiftCountTextView = (TextView) view.findViewById(R.id.shiftCount);
        shiftWeekTextview = (TextView) view.findViewById(R.id.shiftWeekCount);
        hoursWeekCount = (TextView) view.findViewById(R.id.hoursWeekCount);
        toggleText = (TextView) view.findViewById(R.id.toggleText);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.parentLayoutWork);
        LegionUtils.doApplyFont(getActivity().getAssets(), linearLayout);
        legionPreferences = new Legion_PrefsManager(getActivity());
        shiftLegnthSeekBar.setTickHeight(0);
        hourWeekSeekBar.setTickHeight(0);
        shiftWeekSeeKBar.setTickHeight(0);

        shiftLegnthSeekBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1) {
                enableImageView();
                shiftCountTextView.setText(shiftLengthArray[rangeBar.getLeftIndex()] + " - " + shiftLengthArray[rangeBar.getRightIndex()] + " HRS");
            }
        });

        hourWeekSeekBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1) {
                enableImageView();
                legionPreferences.save(Prefs_Keys.HOURS_PER_WEEK, shifthourWeekArray[rangeBar.getRightIndex()]);
                hoursWeekCount.setText(shifthourWeekArray[rangeBar.getLeftIndex()] + " - " + shifthourWeekArray[rangeBar.getRightIndex()] + " HRS");
            }
        });
        shiftWeekSeeKBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int i, int i1) {
                enableImageView();
                shiftWeekTextview.setText(shiftWeekArray[rangeBar.getLeftIndex()] + " - " + shiftWeekArray[rangeBar.getRightIndex()] + " SHIFTS");
            }
        });
        //  getWorkData();
        String response = getArguments().getString("jsonObjectValues");
        legionPreferences.save(Prefs_Keys_Offline.JSON_OBJ_VAL, response);
        try {
            toggleText.setText(getString(R.string.toggleText) + " " + legionPreferences.get(Prefs_Keys.DISPALY_NAME) + " locations.");
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject jsonObjectValues = jsonObject.optJSONObject("schedulePreferences");
            if (jsonObjectValues != null) {
                shiftCountTextView.setText(jsonObjectValues.getString("shiftMinLength") + " - " + jsonObjectValues.getString("shiftMaxLength") + " HRS");
                shiftWeekTextview.setText(jsonObjectValues.getString("numShiftsPerWeekMin") + " - " + jsonObjectValues.getString("numShiftsPerWeek") + " Shifts");
                hoursWeekCount.setText(jsonObjectValues.getString("numHoursPerWeekMin") + " - " + jsonObjectValues.getString("numHoursPerWeek") + " HRS");
                ArrayList<Integer> result = stringArray(shiftLengthArray, Integer.valueOf(jsonObjectValues.getString("shiftMinLength")), Integer.valueOf(jsonObjectValues.getString("shiftMaxLength")));
                shiftLegnthSeekBar.setThumbIndices(result.get(0), result.get(1));
                ArrayList<Integer> result1 = stringArray(shifthourWeekArray, Integer.valueOf(jsonObjectValues.getString("numHoursPerWeekMin")), Integer.valueOf(jsonObjectValues.getString("numHoursPerWeek")));
                ArrayList<Integer> result2 = stringArray(shiftWeekArray, Integer.valueOf(jsonObjectValues.getString("numShiftsPerWeekMin")), Integer.valueOf(jsonObjectValues.getString("numShiftsPerWeek")));
                hourWeekSeekBar.setThumbIndices(result1.get(0), result1.get(1));
                shiftWeekSeeKBar.setThumbIndices(result2.get(0), result2.get(1));
                otherLocationToggleButton.setChecked(Boolean.parseBoolean(jsonObjectValues.getString("otherLocationsPreference")));
                firstTime = true;
            } else {
                shiftCountTextView.setText(2 + " - " + 7 + " HRS");
                shiftWeekTextview.setText(0 + " - " + 5 + " Shifts");
                hoursWeekCount.setText(0 + " - " + 35 + " HRS");
                shiftLegnthSeekBar.setThumbIndices(0, 5);
                hourWeekSeekBar.setThumbIndices(0, 35);
                shiftWeekSeeKBar.setThumbIndices(0, 5);
                otherLocationToggleButton.setChecked(true);
                firstTime = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public ArrayList<Integer> stringArray(String[] result, int jsonLeftThumbValue, int jsonRightThumbValue) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < result.length; ++i) {
            if (Integer.parseInt(result[i]) == jsonLeftThumbValue) {
                res.add(0, i);
            }
            if (Integer.parseInt(result[i]) == jsonRightThumbValue) {
                res.add(i);
            }
        }
        return res;
    }

    private void getWorkData() {
        if (LegionUtils.isOnline(getActivity())) {
            LegionUtils.showProgressDialog(getActivity());
            try {
                Legion_RestClient restClient = new Legion_RestClient(getActivity(), this);
                RequestParams reqObject = new RequestParams();
                restClient.performHTTPGetRequest(1, ServiceUrls.SCHEDULE_PREFERENCES + legionPreferences.get(Prefs_Keys.WORKER_ID)+"&businessId="+legionPreferences.get(Prefs_Keys.BUSINESS_ID), reqObject, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LegionUtils.showOfflineDialog(getActivity());
        }
    }

    @Override
    public void onStartRequest(int requestCode) {

    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == 1) {
            LegionUtils.hideProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONObject jsonObjectValues = jsonObject.getJSONObject("schedulePreferences");
                firstTime = false;
            /*shiftLegnthSeekBar.setRangeValues( Integer.valueOf(jsonObjectValues.getString("shiftMinLength"))   ,
                                               Integer.valueOf(jsonObjectValues.getString("shiftMaxLength")));*/
                // hourWeekSeekBar.setProgress(Integer.parseInt(jsonObjectValues.getString("numHoursPerWeek")));
                // shiftWeekSeeKBar.setProgress(Integer.parseInt(jsonObjectValues.getString("numShiftsPerWeek")));
                // otherLocationToggleButton.setChecked(Boolean.parseBoolean(jsonObjectValues.getString("otherLocationsPreference")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        if (requestCode == 1) {
            LegionUtils.hideProgressDialog();
            LegionUtils.showDialog(getActivity(), reasonPhrase, true);
        }
    }

    public JSONObject getInputtedData() {

        try {
            JSONObject schedulePreferences = new JSONObject();
            String[] shiftsCountArray = shiftCountTextView.getText().toString().replace(" ", "").replace("HRS", "").split("-");
            String[] shiftsWeekArray = shiftWeekTextview.getText().toString().replace(" ", "").replace("SHIFTS", "").split("-");
            String[] shiftsHoursPerWeekArray = hoursWeekCount.getText().toString().replace(" ", "").replace("HRS", "").split("-");
            schedulePreferences.put("workerId", legionPreferences.get(Prefs_Keys.WORKER_ID));
            schedulePreferences.put("numShiftsPerWeek", shiftsWeekArray[1]);
            schedulePreferences.put("shiftMinLength", shiftsCountArray[0]);
            schedulePreferences.put("shiftMaxLength", shiftsCountArray[1]);
            schedulePreferences.put("numHoursPerWeek", shiftsHoursPerWeekArray[1]);
            schedulePreferences.put("numHoursPerWeekMin", shiftsHoursPerWeekArray[0]);
            schedulePreferences.put("numShiftsPerWeekMin", shiftsWeekArray[0]);
            schedulePreferences.put("otherLocationsPreference", otherLocationToggleButton.isChecked());
            return schedulePreferences;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
