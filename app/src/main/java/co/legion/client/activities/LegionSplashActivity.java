package co.legion.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import base.Legion_BaseActivity;
import co.legion.client.R;
import helpers.Legion_PrefsManager;
import models.NotificationType;
import utils.LegionUtils;

/**
 * Created by Administrator on 18-Jan-17.
 */
public class LegionSplashActivity extends Legion_BaseActivity implements View.OnClickListener {

    private TextView createAccountBTN;
    private TextView loginBTN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_legion_splash);
        createAccountBTN = (TextView) findViewById(R.id.createAccountBTN);
        loginBTN = (TextView) findViewById(R.id.loginBTN);
        createAccountBTN.setOnClickListener(this);
        loginBTN.setOnClickListener(this);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));

        Intent i = getIntent();
        if (i != null) {
            try {
                if (prefsManager.hasKey(Prefs_Keys.IS_LOGGED_IN)) {
                    if (new Legion_PrefsManager(getApplicationContext()).get(Prefs_Keys.IS_LOGGED_IN).equalsIgnoreCase("1")) {
                        Bundle bundle = i.getExtras();
                        if (bundle != null && bundle.containsKey("type")) {
                            String type = bundle.getString("type");
                            String businessId = bundle.getString("business_id", null);
                            String weekStartDayOfTheYear = bundle.getString("weekStartDayOfTheYear", null);
                            String year = bundle.getString("year", null);
                            String offerId = bundle.getString("offerId", null);
                            String shiftId = bundle.getString("shiftId", null);

                            Log.v("FCM2", "type = " + type + ", businessId = " + businessId + ", weekStartDayOfTheYear = " + weekStartDayOfTheYear + ", year = " + year + ", offerId = " + offerId+ ", shiftId = " + shiftId);

                            Intent notificationIntent = new Intent(LegionSplashActivity.this, HomeActivity.class);
                            notificationIntent.putExtra(Extras_Keys.COMING_FROM_NOTIFICATION, true);
                            notificationIntent.putExtra(Extras_Keys.TYPE, type);
                            if (type.equalsIgnoreCase(NotificationType.SCHEDULE_PUBLISH.getType()) || type.equalsIgnoreCase(NotificationType.SCHEDULE_UPDATE.getType()) || type.equalsIgnoreCase(NotificationType.SCHEDULE_FINALIZED.getType()) || type.equalsIgnoreCase(NotificationType.SCHEDULE_UPDATED.getType())) {
                                notificationIntent.putExtra(Extras_Keys.NOTIFICATION_BUSINESS_ID, businessId);
                                notificationIntent.putExtra(Extras_Keys.NOTIFICATION_YEAR, year);
                                notificationIntent.putExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR, weekStartDayOfTheYear);
                                notificationIntent.putExtra(Extras_Keys.SCHEDULED_PREFRENCES, prefsManager.get(prefsManager.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES)));
                            } else if (type.equalsIgnoreCase(NotificationType.SHIFT_OFFER.getType()) && offerId == null) {
                                notificationIntent.putExtra(Extras_Keys.NOTIFICATION_YEAR, year);
                                notificationIntent.putExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR, weekStartDayOfTheYear);
                            } else if (type.equalsIgnoreCase(NotificationType.SHIFT_OFFER.getType()) && offerId != null) {
                                notificationIntent.putExtra(Extras_Keys.OFFER_ID, offerId);
                            } else if(type.equalsIgnoreCase(NotificationType.SHIFT_UPDATED.getType())){
                                notificationIntent.putExtra(Extras_Keys.NOTIFICATION_BUSINESS_ID, businessId);
                                notificationIntent.putExtra(Extras_Keys.NOTIFICATION_YEAR, year);
                                notificationIntent.putExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR, weekStartDayOfTheYear);
                                notificationIntent.putExtra(Extras_Keys.NOTIFICATION_SHIFTID, shiftId);
                            }
                            startActivity(notificationIntent);
                            finish();
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (prefsManager != null) {
            try {
                if (LegionUtils.isOnline(LegionSplashActivity.this)) {
                    if (prefsManager.hasKey(Prefs_Keys_Offline.ONBOARDING)) {
                        if (!prefsManager.get(Prefs_Keys_Offline.ONBOARDING).equals("0")) {
                            prefsManager.save(Prefs_Keys.IS_LOGGED_IN, "1");
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class).putExtra(Extras_Keys.SCHEDULED_PREFRENCES, prefsManager.get(prefsManager.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES))));
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), WelcomeScreenActivity.class).putExtra(Extras_Keys.PROFILE_OBJECT, prefsManager.get(Prefs_Keys_Offline.PROFILE_OBJECT)).putExtra(Extras_Keys.SCHEDULED_PREFRENCES, prefsManager.get(prefsManager.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES))));
                            finish();
                        }
                    }
                } else {
                    if (prefsManager.hasKey(Prefs_Keys_Offline.ONBOARDING)) {
                        if (!prefsManager.get(Prefs_Keys_Offline.ONBOARDING).equals("0")) {
                            prefsManager.save(Prefs_Keys.IS_LOGGED_IN, "1");
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class).putExtra(Extras_Keys.SCHEDULED_PREFRENCES, prefsManager.get(prefsManager.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES))));
                            finish();
                        } else {
                            startActivity(new Intent(getApplicationContext(), WelcomeScreenActivity.class).putExtra(Extras_Keys.PROFILE_OBJECT, prefsManager.get(Prefs_Keys_Offline.PROFILE_OBJECT)).putExtra(Extras_Keys.SCHEDULED_PREFRENCES, prefsManager.get(prefsManager.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES))));
                            finish();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.createAccountBTN) {
            Intent i = new Intent(LegionSplashActivity.this, VerifyIdentityActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(LegionSplashActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }
}
