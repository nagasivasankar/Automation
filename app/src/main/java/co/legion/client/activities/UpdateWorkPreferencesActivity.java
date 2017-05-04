package co.legion.client.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import base.Legion_BaseActivity;
import co.legion.client.R;
import fragments.HowIWouldLikeToWorkFragment;
import fragments.WhatsImportantFragment;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 11/25/2016.
 */

public class UpdateWorkPreferencesActivity extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback {
    public JSONObject jsonObjectValues;
    private TextView buttonHow_I_Prefer, buttonMyPreferences;
    private TextView saveButton;
    private WhatsImportantFragment whatsImportantFragment;
    private HowIWouldLikeToWorkFragment howIWouldLikeToWorkFragment;
    private long lastClickTime;
    private FrameLayout frameLayout1;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workpreferences_edit);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        saveButton = (TextView) findViewById(R.id.imageviewOk);
        saveBtnDisable();
        ImageButton navigationClose = (ImageButton) findViewById(R.id.closeSetup);
        navigationClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveButton.getCurrentTextColor() == Color.WHITE) {
                    showDiscardAlert();
                } else {
                    finish();
                }
            }
        });
        init();
        getWorkData();
        buttonMyPreferences.setOnClickListener(this);
        buttonHow_I_Prefer.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        frameLayout1 = (FrameLayout) findViewById(R.id.frameLayout1);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout.setVisibility(View.VISIBLE);
        frameLayout1.setVisibility(View.GONE);
    }

    public void saveBtnDisable() {
        saveButton.setEnabled(false);
        saveButton.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
    }

    public void saveBtnEnable() {
        saveButton.setEnabled(true);
        saveButton.setTextColor(Color.WHITE);
    }

    private void getWorkData() {
        if (LegionUtils.isOnline(this)) {
            LegionUtils.showProgressDialog(this);
            try {
                Legion_RestClient restClient = new Legion_RestClient(this, this);
                RequestParams reqObject = new RequestParams();
                restClient.performHTTPGetRequest(1, ServiceUrls.SCHEDULE_PREFERENCES + prefsManager.get(Prefs_Keys.WORKER_ID)+"&businessId="+prefsManager.get(Prefs_Keys.BUSINESS_ID), reqObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (prefsManager.hasKey(Prefs_Keys_Offline.JSON_OBJ_VAL)) {
                onSuccess(1, prefsManager.get(Prefs_Keys_Offline.JSON_OBJ_VAL), null);
            } else {
                LegionUtils.showOfflineDialog(this);
            }
        }
    }

    private void init() {
        buttonHow_I_Prefer = (TextView) findViewById(R.id.buttonHow_I_Prefer);
        buttonMyPreferences = (TextView) findViewById(R.id.buttonMyPreferences);
        buttonMyPreferences.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
        buttonHow_I_Prefer.setEnabled(false);
        buttonMyPreferences.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.important_off, 0, 0);
        buttonHow_I_Prefer.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.workpreferences_on, 0, 0);
        buttonHow_I_Prefer.setTextColor(ActivityCompat.getColor(this, R.color.violet));
    }

    public void turnHowIPreferToWorkOn() {
        buttonMyPreferences.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
        buttonMyPreferences.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.important_off, 0, 0);
        buttonHow_I_Prefer.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.workpreferences_on, 0, 0);
        buttonMyPreferences.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Mark Simonson - Proxima Nova Cond Medium.otf"));
        buttonHow_I_Prefer.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Mark Simonson - Proxima Nova Cond Semibold.otf"));
        buttonHow_I_Prefer.setTextColor(ActivityCompat.getColor(this, R.color.violet));
        buttonMyPreferences.setEnabled(true);
        buttonHow_I_Prefer.setEnabled(false);
        frameLayout.setVisibility(View.VISIBLE);
        frameLayout1.setVisibility(View.GONE);
    }

    public void turnWorkPrefsOn() {
        buttonMyPreferences.setEnabled(false);
        buttonHow_I_Prefer.setEnabled(true);
        buttonHow_I_Prefer.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
        buttonHow_I_Prefer.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.work_preferences_off, 0, 0);
        buttonHow_I_Prefer.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Mark Simonson - Proxima Nova Cond Medium.otf"));
        buttonMyPreferences.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Mark Simonson - Proxima Nova Cond Semibold.otf"));
        buttonMyPreferences.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.important_on, 0, 0);
        buttonMyPreferences.setTextColor(ActivityCompat.getColor(this, R.color.violet));
        frameLayout.setVisibility(View.GONE);
        frameLayout1.setVisibility(View.VISIBLE);
    }

    private void showDiscardAlert() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_availability_discard_alert);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final TextView tv2 = (TextView) dialog.findViewById(R.id.tv2);
            tv2.setText("You made changes to your Work Preferences. Are you sure you want to close without saving changes?");
            tv2.setTag(getString(R.string.mallory_book));
            final TextView discardTV = (TextView) dialog.findViewById(R.id.discardTV);
            final TextView cancelTV = (TextView) dialog.findViewById(R.id.cancelTV);
            discardTV.setText("Cancel");
            cancelTV.setText("Close and Discard Changes");
            cancelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
            discardTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            LegionUtils.doApplyFont(getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if ((System.currentTimeMillis() - lastClickTime) < 500) {
            return;
        }
        lastClickTime = System.currentTimeMillis();
        switch (v.getId()) {
            case R.id.imageviewOk:
                if (!LegionUtils.isOnline(this)) {
                    LegionUtils.showOfflineDialog(this);
                    return;
                }
                try {
                    JSONObject innerObj = howIWouldLikeToWorkFragment.getInputtedData();
                    innerObj = whatsImportantFragment.getInputtedData(innerObj);
                    JSONObject reqObj = new JSONObject();
                    reqObj.put("schedulePreferences", innerObj);
                    reqObj.put("token", "");
                    sendData(reqObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*try {
                    String msg = "Your updated work preferences will be factored in future new schedules.\n\n You are still responsible for published schedules.\n\n Do you want to save the changes?";
                    final Dialog dialog = LegionUtils.saveChangesDialog(this, msg);
                    TextView saveTv = (TextView) dialog.findViewById(R.id.saveTv);

                    saveTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                dialog.dismiss();
                                JSONObject innerObj = howIWouldLikeToWorkFragment.getInputtedData();
                                innerObj = whatsImportantFragment.getInputtedData(innerObj);
                                JSONObject reqObj = new JSONObject();
                                reqObj.put("schedulePreferences", innerObj);
                                reqObj.put("token", "");
                                sendData(reqObj);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                break;

            case R.id.buttonHow_I_Prefer:
                try {
                    turnHowIPreferToWorkOn();
                    if (howIWouldLikeToWorkFragment == null) {
                        howIWouldLikeToWorkFragment = new HowIWouldLikeToWorkFragment(saveButton);
                        Bundle bundleWork = new Bundle();
                        bundleWork.putString("jsonObjectValues", jsonObjectValues.toString());
                        howIWouldLikeToWorkFragment.setArguments(bundleWork);
                        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, howIWouldLikeToWorkFragment).commit();
                    } else {
                        // getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, howIWouldLikeToWorkFragment).commit();
                        frameLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.buttonMyPreferences:
                try {
                    turnWorkPrefsOn();
                    if (whatsImportantFragment == null) {
                        whatsImportantFragment = new WhatsImportantFragment(saveButton);
                        Bundle bundleWhatImp = new Bundle();
                        bundleWhatImp.putString("viewType", "");
                        bundleWhatImp.putString("jsonObjectValues", jsonObjectValues.toString());
                        whatsImportantFragment.setArguments(bundleWhatImp);
                        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout1, whatsImportantFragment).commit();
                    } else {
                        //getSupportFragmentManager().beginTransaction().add(R.id.frameLayout1, whatsImportantFragment).commit();
                        frameLayout1.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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
                jsonObjectValues = new JSONObject(response.toString());
                prefsManager.save(Prefs_Keys_Offline.JSON_OBJ_VAL, jsonObjectValues.toString());

                whatsImportantFragment = new WhatsImportantFragment(saveButton);
                Bundle bundleWhatImp = new Bundle();
                bundleWhatImp.putString("viewType", "");
                bundleWhatImp.putString("jsonObjectValues", jsonObjectValues.toString());
                whatsImportantFragment.setArguments(bundleWhatImp);
                getSupportFragmentManager().beginTransaction().add(R.id.frameLayout1, whatsImportantFragment).commit();

                howIWouldLikeToWorkFragment = new HowIWouldLikeToWorkFragment(saveButton);
                Bundle bundleWork = new Bundle();
                bundleWork.putString("jsonObjectValues", jsonObjectValues.toString());
                howIWouldLikeToWorkFragment.setArguments(bundleWork);
                getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, howIWouldLikeToWorkFragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 10) {
            LegionUtils.hideProgressDialog();
            saveBtnDisable();
            try {
                final Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.popup_availability_pastweek_alert);
                final ImageView imageHeader = (ImageView) dialog.findViewById(R.id.imageHeader);
                final TextView okTV = (TextView) dialog.findViewById(R.id.okTV);
                final TextView tv1 = (TextView) dialog.findViewById(R.id.tv1);
                final TextView tv2 = (TextView) dialog.findViewById(R.id.tv2);
                imageHeader.setImageResource(R.drawable.confirmation);
                tv1.setText("Your Work Preferences have been saved, and will be factored in future new schedule.");
                tv1.setTag(getString(R.string.mallory_book));
                tv2.setVisibility(View.GONE);
                LegionUtils.doApplyFont(getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
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
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
        if (reasonPhrase == null) {
            return;
        }
        if (reasonPhrase.contains("Something went wrong")) {
            LegionUtils.doLogout(this);
            return;
        }
        if (reasonPhrase != null) {
            LegionUtils.showDialog(this, reasonPhrase, true);
        } else {
            LegionUtils.showDialog(UpdateWorkPreferencesActivity.this, reasonPhrase, true);
        }
    }

    private void sendData(JSONObject jsonObject) {
        LegionUtils.showProgressDialog(this);
        LegionUtils.hideKeyboard(this);
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performPostRequest(10, ServiceUrls.UPDATE_SCEDULED_PREFS_URL, jsonObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
