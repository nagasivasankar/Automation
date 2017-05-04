package fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import co.legion.client.R;
import helpers.Legion_PrefsManager;
import utils.LegionUtils;

/**
 * Created by Administrator on 11/16/2016.
 */

@SuppressLint("ValidFragment")
public class WhatsImportantFragment extends Fragment implements View.OnClickListener {
    de.hdodenhof.circleimageview.CircleImageView workingCheck, consistentCheck, verityCheck, flexCheck;
    LinearLayout workingLayout, consistentLayout, verityLayout, flexLayout;
    boolean peoplePrefBoolean = true, wagesPreferenceBoolean = true, varietyPreferenceBoolean = true, flexibilityPreferenceBoolean = true;
    TextView saveBtnStatus;
    private Legion_PrefsManager legionPreferences;

    @SuppressLint("ValidFragment")
    public WhatsImportantFragment(TextView saveBtnStatus) {
        if (saveBtnStatus != null) {
            this.saveBtnStatus = saveBtnStatus;
            saveBtnStatus.setEnabled(false);
        }
    }

    public void enableImageView() {
        if (saveBtnStatus != null) {
            saveBtnStatus.setEnabled(true);
            saveBtnStatus.setTextColor(Color.WHITE);
        }
    }
    public WhatsImportantFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whats_important, null);
        workingCheck = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.workingCheck);
        consistentCheck = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.consistentCheck);
        verityCheck = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.verityCheck);
        flexCheck = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.flexCheck);

        workingLayout = (LinearLayout) view.findViewById(R.id.workingLayout);
        consistentLayout = (LinearLayout) view.findViewById(R.id.consistentLayout);
        verityLayout = (LinearLayout) view.findViewById(R.id.verityLayout);
        flexLayout = (LinearLayout) view.findViewById(R.id.flexlayout);

        workingLayout.setOnClickListener(this);
        consistentLayout.setOnClickListener(this);
        verityLayout.setOnClickListener(this);
        flexLayout.setOnClickListener(this);


        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.whatsParentLayout);
        LegionUtils.doApplyFont(getActivity().getAssets(), linearLayout);
        legionPreferences = new Legion_PrefsManager(getActivity());
        String response = getArguments().getString("jsonObjectValues");
        try {

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject jsonObjectValues = jsonObject.optJSONObject("schedulePreferences");
            if(jsonObjectValues != null) {

                if (Boolean.parseBoolean(jsonObjectValues.getString("peoplePreference"))) {
                    workingCheck.setImageResource(R.drawable.check_mdpi);
                    peoplePrefBoolean = false;
                    //(getActivity()).findViewById(R.id.imageviewOk).setAlpha(1f);
                } else {
                    workingCheck.setImageResource(R.drawable.check_selected_mdpi);
                    peoplePrefBoolean = true;
                    // (getActivity()).findViewById(R.id.imageviewOk).setAlpha(1f);
                }

                if (Boolean.parseBoolean(jsonObjectValues.getString("wagesPreference"))) {
                    consistentCheck.setImageResource(R.drawable.check_mdpi);
                    wagesPreferenceBoolean = false;
                    // (getActivity()).findViewById(R.id.imageviewOk).setAlpha(1f);
                } else {
                    consistentCheck.setImageResource(R.drawable.check_selected_mdpi);
                    // (getActivity()).findViewById(R.id.imageviewOk).setAlpha(1f);
                    wagesPreferenceBoolean = true;
                }

                if (Boolean.parseBoolean(jsonObjectValues.getString("flexibilityPreference"))) {
                    flexCheck.setImageResource(R.drawable.check_mdpi);
                    flexibilityPreferenceBoolean = false;
                } else {
                    flexCheck.setImageResource(R.drawable.check_selected_mdpi);
                    flexibilityPreferenceBoolean = true;
                }

                if (Boolean.parseBoolean(jsonObjectValues.getString("varietyPreference"))) {
                    verityCheck.setImageResource(R.drawable.check_mdpi);
                    varietyPreferenceBoolean = false;
                } else {
                    verityCheck.setImageResource(R.drawable.check_selected_mdpi);
                    varietyPreferenceBoolean = true;
                }
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        enableImageView();
        switch (v.getId()) {
            case R.id.workingLayout:
                if (peoplePrefBoolean) {
                    workingCheck.setImageResource(R.drawable.check_mdpi);
                    peoplePrefBoolean = false;

                } else {
                    workingCheck.setImageResource(R.drawable.check_selected_mdpi);
                    peoplePrefBoolean = true;
                }
                break;

            case R.id.consistentLayout:
                if (wagesPreferenceBoolean) {
                    consistentCheck.setImageResource(R.drawable.check_mdpi);
                    wagesPreferenceBoolean = false;
                } else {
                    consistentCheck.setImageResource(R.drawable.check_selected_mdpi);
                    wagesPreferenceBoolean = true;
                }
                break;

            case R.id.verityLayout:
                if (varietyPreferenceBoolean) {
                    verityCheck.setImageResource(R.drawable.check_mdpi);
                    varietyPreferenceBoolean = false;
                } else {
                    verityCheck.setImageResource(R.drawable.check_selected_mdpi);
                    varietyPreferenceBoolean = true;
                }
                break;

            case R.id.flexlayout:
                if (flexibilityPreferenceBoolean) {
                    flexCheck.setImageResource(R.drawable.check_mdpi);
                    flexibilityPreferenceBoolean = false;
                } else {
                    flexCheck.setImageResource(R.drawable.check_selected_mdpi);
                    flexibilityPreferenceBoolean = true;
                }
                break;
        }
    }


    public JSONObject getInputtedData(JSONObject likeToWorkJsonObject) {
        try {
            likeToWorkJsonObject.put("peoplePreference", !peoplePrefBoolean);
            likeToWorkJsonObject.put("flexibilityPreference", !flexibilityPreferenceBoolean);
            likeToWorkJsonObject.put("varietyPreference", !varietyPreferenceBoolean);
            likeToWorkJsonObject.put("wagesPreference", !wagesPreferenceBoolean);
            return likeToWorkJsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
