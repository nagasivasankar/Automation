package co.legion.client.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import base.Legion_BaseActivity;
import co.legion.client.R;
import fragments.MyProfileFragment;
import helpers.Legion_PrefsManager;
import interfaces.CompleteProfileData;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import ui.ErrorHandlingMethods;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/24/2016.
 */
public class UpdateProfileActivity extends Legion_BaseActivity implements Legion_NetworkCallback, CompleteProfileData {

    private TextView saveButton;
    private String phone;
    private String email;
    private View viewEmail;
    private TextView textViewemail;
    private ErrorHandlingMethods errorHandlingMethods;
    private View viewPhone;
    private TextView textViewPhone;
    private MyProfileFragment myProfileFragment;
    private long lastClickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        errorHandlingMethods = new ErrorHandlingMethods();
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
        saveButton = (TextView) findViewById(R.id.imageviewOk);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myProfileFragment == null) {
                    return;
                }
                if ((System.currentTimeMillis() - lastClickTime) < 1000) {
                    return;
                }
                lastClickTime = System.currentTimeMillis();

                boolean validated = myProfileFragment.doValidateProfileInputs();
                if (validated) {
                    LegionUtils.hideKeyboard(UpdateProfileActivity.this);
                    JSONObject jsonObject = myProfileFragment.getInputtedData();
                    if (jsonObject != null) {
                        doProfileUpdate(jsonObject);
                    }
                }
            }
        });
        getOnBoardingCompletedTimestamp(prefsManager.get(Legion_Constants.Prefs_Keys.WORKER_ID));
    }

    private void showDiscardAlert() {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_availability_discard_alert);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            final TextView tv2 = (TextView) dialog.findViewById(R.id.tv2);
            tv2.setText("You made changes to your Profile. Are you sure you want to close without saving changes?");
            final TextView discardTV = (TextView) dialog.findViewById(R.id.discardTV);
            final TextView cancelTV = (TextView) dialog.findViewById(R.id.cancelTV);
            discardTV.setText("Cancel");
            cancelTV.setText("Close and Discard Changes");
            cancelTV.setTag(getString(R.string.mallory_book));
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

    private void doProfileUpdate(JSONObject jsonObject) {
        LegionUtils.hideKeyboard(this);
        try {
            if (!LegionUtils.isOnline(this)) {
                LegionUtils.showOfflineDialog(this);
                return;
            }
            LegionUtils.showProgressDialog(this);
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performPostRequest(2, ServiceUrls.PROFILE_UPDATE_URL, jsonObject, prefsManager.get(Legion_Constants.Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOnBoardingCompletedTimestamp(String workerId) {
        try {
            if (!LegionUtils.isOnline(this)) {
                if (prefsManager.hasKey(Prefs_Keys_Offline.PROFILE_OBJECT))
                    onSuccess(1, prefsManager.get(Prefs_Keys_Offline.PROFILE_OBJECT), null);
                else {
                    LegionUtils.showOfflineDialog(this);
                }
            } else {
                Legion_RestClient restClient = new Legion_RestClient(getApplicationContext(), this);
                RequestParams reqObject = new RequestParams();
                restClient.performHTTPGetRequest(1, ServiceUrls.ONBOARDING_COMPLTED_TIMESTAMP + workerId, reqObject, prefsManager.get(Legion_Constants.Prefs_Keys.SESSION_ID), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == 1) {
            LegionUtils.showProgressDialog(this);
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        LegionUtils.hideProgressDialog();
        if (requestCode == 1) {
            prefsManager.save(Prefs_Keys_Offline.PROFILE_OBJECT, response.toString());
            myProfileFragment = new MyProfileFragment(saveButton);
            Bundle bundleWork = new Bundle();
            bundleWork.putString(Extras_Keys.PROFILE_OBJECT, response.toString());
            myProfileFragment.setArguments(bundleWork);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, myProfileFragment);
            fragmentTransaction.commitAllowingStateLoss ();
        } else if (requestCode == 2) {
            Log.d("profile", response.toString());
            try {
                JSONObject obj = new JSONObject(response.toString());
                String responseStatus = obj.getString("responseStatus");
                if (responseStatus.equals("SUCCESS")) {
                    JSONArray arr = obj.getJSONArray("updatedWorkers");
                    JSONObject profileObj = arr.getJSONObject(0);
                    prefsManager.save(Prefs_Keys.PROFILE_PIC_URL, (profileObj.isNull("pictureUrl")?"":profileObj.getString("pictureUrl")));
                    prefsManager.save(Prefs_Keys.FIRST_NAME, profileObj.getString("firstName"));
                    if (!profileObj.getString("nickName").equalsIgnoreCase("null") && (profileObj.getString("nickName") != null)) {
                        prefsManager.save(Prefs_Keys.NICK_NAME, profileObj.getString("nickName"));
                    } else {
                        prefsManager.save(Prefs_Keys.NICK_NAME, "");
                    }
                    LegionUtils.showMessageDialog(this, "Your Profile has been saved.", R.drawable.confirmation);
                    saveButton.setTextColor(ActivityCompat.getColor(this, R.color.swap_text_color));
                    saveButton.setEnabled(false);
                }
            } catch (JSONException e) {
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
        LegionUtils.showDialog(UpdateProfileActivity.this, reasonPhrase, true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
