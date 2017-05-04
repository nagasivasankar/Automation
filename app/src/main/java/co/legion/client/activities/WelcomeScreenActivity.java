package co.legion.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import base.Legion_BaseActivity;
import co.legion.client.R;
import fragments.MyProfileFragment;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/16/2016.
 */

public class WelcomeScreenActivity extends Legion_BaseActivity implements Legion_NetworkCallback {

    private Button letsGetStartedButton;
    private TextView textViewName;
    private String profileObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcomescreen);

        if (getIntent() != null && getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString(Extras_Keys.PROFILE_OBJECT) != null) {
                profileObject = getIntent().getExtras().getString(Extras_Keys.PROFILE_OBJECT);
            }
        }
        if(profileObject == null){
            try {
                Legion_RestClient restClient = new Legion_RestClient(getApplicationContext(), this);
                RequestParams reqObject = new RequestParams();
                restClient.performHTTPGetRequest(1, ServiceUrls.ONBOARDING_COMPLTED_TIMESTAMP + prefsManager.get(Legion_Constants.Prefs_Keys.WORKER_ID), reqObject, prefsManager.get(Legion_Constants.Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        intiViews();
        letsGetStartedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LegionUtils.isOnline(WelcomeScreenActivity.this)) {
                    prefsManager.save(Prefs_Keys.IS_ON_BOARDING_STARTED, "started");
                    startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class).putExtra(Extras_Keys.PROFILE_OBJECT, prefsManager.get(Prefs_Keys_Offline.PROFILE_OBJECT)).putExtra(Extras_Keys.SCHEDULED_PREFRENCES, prefsManager.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES)));
                    finish();
                } else {
                    if (prefsManager.hasKey(Prefs_Keys_Offline.SCHEDULE_PREFREENCES)) {
                        startActivity(new Intent(getApplicationContext(), OnBoardingActivity.class).putExtra(Extras_Keys.PROFILE_OBJECT, prefsManager.get(Prefs_Keys_Offline.PROFILE_OBJECT)).putExtra(Extras_Keys.SCHEDULED_PREFRENCES, prefsManager.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES)));
                        finish();
                    } else {
                        LegionUtils.showOfflineDialog(WelcomeScreenActivity.this);
                    }

                }
            }
        });
        ImageView photoIv = (ImageView) findViewById(R.id.photoIv);
        ImageView logoIv = (ImageView) findViewById(R.id.logoIv);
        Glide.with(this).load(prefsManager.get(Prefs_Keys.WELCOME_SCREEN_PIC_URL)).into(photoIv);
        Glide.with(this).load(prefsManager.get(Prefs_Keys.WELCOME_SCREEN_LOGO_URL)).into(logoIv);
        TextView addressTv = (TextView) findViewById(R.id.adressTv);
        addressTv.setText(prefsManager.get(Prefs_Keys.WELCOME_SCREEN_ADDRESS));
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
    }

    // http://enterprise-stage.legion.work/legion/business/queryEnterpriseById?enterpriseId=
    private void intiViews() {
        textViewName = (TextView) findViewById(R.id.welcomeTextView);
        letsGetStartedButton = (Button) findViewById(R.id.letStartedButton);
        if (prefsManager.get(Prefs_Keys.IS_ON_BOARDING_STARTED, null) == null) {
            letsGetStartedButton.setText("Let's get started!");
            try {
                textViewName.setText("Welcome, " + new JSONObject(profileObject).optString("firstName", "") + "!\nNow, let's personalize your account.\n\nTell us about your work preferences and availability, so we can create schedules that best meet your needs.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            letsGetStartedButton.setText("Let's get this done!");
            try {
                textViewName.setText("Welcome back, " + new JSONObject(profileObject).optString("firstName", "") + "!\n Just a few steps tp personalize your account.\n\nTell us about your work preferences and availability, so we can create schedules that best meet your needs.");
            } catch (Exception e) {
                e.printStackTrace();
            }
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

            Intent i = new Intent(getApplicationContext(), WelcomeScreenActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra(Extras_Keys.PROFILE_OBJECT, prefsManager.get(Prefs_Keys_Offline.PROFILE_OBJECT));
            i.putExtra(Extras_Keys.SCHEDULED_PREFRENCES, prefsManager.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES));
            startActivity(i);
            finish();
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
        LegionUtils.showDialog(WelcomeScreenActivity.this, reasonPhrase, true);
    }
}
