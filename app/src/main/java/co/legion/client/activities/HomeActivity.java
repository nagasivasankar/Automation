package co.legion.client.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import base.Legion_BaseActivity;
import co.legion.client.BuildConfig;
import co.legion.client.R;
import fragments.ComingSoonDialogFragment;
import models.NotificationType;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import tabs.BaseContainerFragment;
import tabs.Tab1ContainerFragment;
import tabs.Tab2ContainerFragment;
import tabs.Tab3ContainerFragment;
import tabs.Tab5ContainerFragment;
import utils.LegionUtils;

/**
 * Created by Administrator on 11/22/2016.
 */
public class HomeActivity extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback {

    public static final String TAB_1_SPEC = "TAB1";
    public static final String TAB_3_SPEC = "TAB3";
    public static final String TAB_2_SPEC = "TAB2";
    public static final String TAB_4_SPEC = "TAB4";
    public static final String TAB_5_SPEC = "TAB5";
    public FragmentTabHost mTabHost;
    private TextView tabText;
    private DrawerLayout drawerLayout;
    private ImageView arrowAboutLegion;
    private TextView aboutLegionTV, notificationSettingsTv, profileEditTv, work_preferencesTv, availabilityTv, termsOfUse, privacyPolicy;
    private LinearLayout aboutLegionLayout, privacyPolicyLayout;
    private boolean isPrivacyPolicyOpen = false;
    private View view1;
    private TextView logoutButton;
    private TextView tv_side_menu_profile_name;
    private ImageView profileImageSideMenu;
    private int prevTab;
    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
            Log.v("tabId", "" + tabId);
            int selectedTab = 0;
            if (tabId.equals("TAB1")) {
                selectedTab = 0;
            } else if (tabId.equals("TAB2")) {
                selectedTab = 1;
            } else if (tabId.equals("TAB3")) {
                selectedTab = 2;
            } /*else if (tabId.equals("TAB4")) {
                selectedTab = 3;
            }*/ else if (tabId.equals("TAB5")) {
                //.setCurrentTab(prevTab);
                return;
            }
            prevTab = selectedTab;

            for (int i = 0; i < 4; ++i) {
                View view = mTabHost.getTabWidget().getChildAt(i);
                if (view != null) {
                    final TextView tv = (TextView) view.findViewById(R.id.tab_text);
                    if (selectedTab == i) {
                        tv.setTextColor(Color.parseColor("#232265"));
                    } else {
                        tv.setTextColor(Color.parseColor("#92959A"));
                    }
                }
            }
        }
    };
    private TextView sendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findSideMenuViews();
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setOnTabChangedListener(listener);
        mTabHost.setup(HomeActivity.this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.addTab(mTabHost.newTabSpec("TAB1").setIndicator(createTabView(R.drawable.home_tab_selector, "Home", TAB_1_SPEC)), Tab1ContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("TAB2").setIndicator(createTabView(R.drawable.schedule_tab_selector, "Schedule", TAB_2_SPEC)), Tab2ContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("TAB3").setIndicator(createTabView(R.drawable.shift_offers_tab_selector, "Shift Offers", TAB_3_SPEC)), Tab3ContainerFragment.class, null);
        //  mTabHost.addTab(mTabHost.newTabSpec("TAB4").setIndicator(createTabView(R.drawable.inbox_tab_selector, "Inbox", TAB_4_SPEC)), Tab4ContainerFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("TAB5").setIndicator(createTabView(R.drawable.more_tab_selector, "More", TAB_5_SPEC)), Tab5ContainerFragment.class, null);
        mTabHost.getTabWidget().setDividerDrawable(null);

        mTabHost.setCurrentTab(0);

        final Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Extras_Keys.TYPE) && intent.getBooleanExtra(Extras_Keys.COMING_FROM_NOTIFICATION, false)) {
            String type = intent.getStringExtra(Extras_Keys.TYPE);
            if (intent.hasExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR) && (type.equalsIgnoreCase(NotificationType.SCHEDULE_PUBLISH.getType()) || type.equalsIgnoreCase(NotificationType.SCHEDULE_UPDATE.getType()) || type.equalsIgnoreCase(NotificationType.SCHEDULE_FINALIZED.getType()) || type.equalsIgnoreCase(NotificationType.SCHEDULE_UPDATED.getType()))) {
                mTabHost.setCurrentTab(1);
            } else if (type.equalsIgnoreCase("shift_offer")) {
                mTabHost.setCurrentTab(2);
            }
        }
    }

    private void findSideMenuViews() {
        aboutLegionLayout = (LinearLayout) findViewById(R.id.layout_about_legion);
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.sideMenuParentLayout));
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));
        aboutLegionTV = (TextView) findViewById(R.id.about_legion);
        arrowAboutLegion = (ImageView) findViewById(R.id.arrow);
        notificationSettingsTv = (TextView) findViewById(R.id.notifi_settings);
        TextView versionTV = (TextView) findViewById(R.id.versionTV);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            versionTV.setText(version + " (" + verCode + ")");
        } catch (Exception e) {
            Log.d("Exception", "pInfo not getting");
        }
        profileEditTv = (TextView) findViewById(R.id.profile);
        work_preferencesTv = (TextView) findViewById(R.id.work_preferences);
        availabilityTv = (TextView) findViewById(R.id.availability);
        termsOfUse = (TextView) findViewById(R.id.terms_of_use);
        privacyPolicy = (TextView) findViewById(R.id.privacy_policy);
        privacyPolicyLayout = (LinearLayout) findViewById(R.id.layout_privacy_policy);
        logoutButton = (TextView) findViewById(R.id.logout);
        TextView onboardingButton = (TextView) findViewById(R.id.onboarding);

        if (BuildConfig.IS_PRODUCTION) {
            onboardingButton.setVisibility(View.GONE);
        }

        view1 = (View) findViewById(R.id.view);
        tv_side_menu_profile_name = (TextView) findViewById(R.id.tv_side_menu_profile_name);
        profileImageSideMenu = (ImageView) findViewById(R.id.profileImageSideMenu);
        sendFeedback = (TextView) findViewById(R.id.sendFeedback);

        onboardingButton.setOnClickListener(this);
        sendFeedback.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        aboutLegionLayout.setOnClickListener(this);
        aboutLegionTV.setOnClickListener(this);
        arrowAboutLegion.setOnClickListener(this);
        notificationSettingsTv.setOnClickListener(this);
        work_preferencesTv.setOnClickListener(this);
        profileEditTv.setOnClickListener(this);
        availabilityTv.setOnClickListener(this);

        if (LegionUtils.isFeatureEnabled(this, "feature.legalScreens", "")) {
            termsOfUse.setOnClickListener(this);
            privacyPolicy.setOnClickListener(this);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        if (prefsManager.hasKey(Prefs_Keys.NICK_NAME) && prefsManager.get(Prefs_Keys.NICK_NAME).trim().length() > 0) {
            tv_side_menu_profile_name.setText("What's new, " + prefsManager.get(Prefs_Keys.NICK_NAME) + "?");
        } else {
            if (!prefsManager.get(Prefs_Keys.FIRST_NAME, "").equals("")) {
                tv_side_menu_profile_name.setText("What's new, " + prefsManager.get(Prefs_Keys.FIRST_NAME) + "?");
            }
        }

        doSetProfilePic();

        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(HomeActivity.this,/* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                if (prevTab == 0) {
                    ((Tab1ContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_1_SPEC)).getHostFragment().updateNameAndWishes();
                }
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                if (isPrivacyPolicyOpen) {
                    aboutLegionLayout.performClick();
                }
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                if (prefsManager.hasKey(Prefs_Keys.NICK_NAME) && prefsManager.get(Prefs_Keys.NICK_NAME).trim().length() > 0) {
                    tv_side_menu_profile_name.setText("What's new, " + prefsManager.get(Prefs_Keys.NICK_NAME) + "?");
                } else {
                    if (!prefsManager.get(Prefs_Keys.FIRST_NAME, "").equals("")) {
                        tv_side_menu_profile_name.setText("What's new, " + prefsManager.get(Prefs_Keys.FIRST_NAME) + "?");
                    }
                }
                doSetProfilePic();
            }
        };
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doSetProfilePic();
    }

    private void doSetProfilePic() {
        if (!prefsManager.get(Prefs_Keys.PROFILE_PIC_URL, "").equals("")) {
            Picasso.with(HomeActivity.this)
                    .load(prefsManager.get(Prefs_Keys.PROFILE_PIC_URL))
                    .error(R.drawable.ic_place_holder_profile)
                    .placeholder(R.drawable.ic_place_holder_profile)
                    .into(profileImageSideMenu);
        } else {
            profileImageSideMenu.setImageResource(R.drawable.ic_place_holder_profile);
        }
    }

    private View createTabView(final int id, String text, String tag) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_icon, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_image);
        tabText = (TextView) view.findViewById(R.id.tab_text);
        tabText.setText(text);
        imageView.setTag(tag);

        if (tag.equalsIgnoreCase(TAB_5_SPEC)) {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.moreTabLL);
            layout.setVisibility(View.VISIBLE);
            TextView tabTv = (TextView) layout.findViewById(R.id.tabTv);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
            });
            tabTv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Mallory-Book.ttf"));
        } else {
            tabText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Mallory-Book.ttf"));
            imageView.setImageDrawable(getResources().getDrawable(id));
        }
        return view;
    }

    @Override
    public void onBackPressed() {
        boolean isPopFragment = false;
        String currentTabTag = mTabHost.getCurrentTabTag();
        if (currentTabTag.equals(TAB_1_SPEC)) {
            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_1_SPEC)).popFragment();
        } else if (currentTabTag.equals(TAB_2_SPEC)) {
            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_2_SPEC)).popFragment();
        } else if (currentTabTag.equals(TAB_3_SPEC)) {
            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_3_SPEC)).popFragment();
        }/* else if (currentTabTag.equals(TAB_4_SPEC)) {
            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_4_SPEC)).popFragment();
        }*/ else if (currentTabTag.equals(TAB_5_SPEC)) {
            isPopFragment = ((BaseContainerFragment) getSupportFragmentManager().findFragmentByTag(TAB_5_SPEC)).popFragment();
        }

        if (!isPopFragment) {
            super.onBackPressed();
        }
    }

    private void getOnBoardingCompletedTimestamp(String workerId) {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(2, ServiceUrls.ONBOARDING_COMPLTED_TIMESTAMP + workerId, reqObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getScheduledPreferencesForSpinner(String workerId) {

        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(WebServiceRequestCodes.SCHEDULED_PREFRERNCES, ServiceUrls.SCHEDULE_PREFERENCES + workerId+"&businessId="+prefsManager.get(Prefs_Keys.BUSINESS_ID), reqObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onboarding:
                drawerLayout.closeDrawers();
                getOnBoardingCompletedTimestamp(prefsManager.get(Prefs_Keys.WORKER_ID));
                break;

            case R.id.sendFeedback:
                drawerLayout.closeDrawers();
                doSendFeedback();
                break;

            case R.id.layout_about_legion:
            case R.id.about_legion:
            case R.id.arrow:
                if (!isPrivacyPolicyOpen) {
                    isPrivacyPolicyOpen = true;
                    privacyPolicyLayout.setVisibility(View.VISIBLE);
                    aboutLegionLayout.setBackgroundColor(Color.parseColor("#ccffffff"));
                    arrowAboutLegion.setImageDrawable(getResources().getDrawable(R.drawable.ic_next_black));
                    view1.setVisibility(View.VISIBLE);
                } else if (isPrivacyPolicyOpen) {
                    isPrivacyPolicyOpen = false;
                    privacyPolicyLayout.setVisibility(View.GONE);
                    aboutLegionLayout.setBackgroundColor(Color.parseColor("#33ffffff"));
                    arrowAboutLegion.setImageDrawable(getResources().getDrawable(R.drawable.ic_down_black));
                    view1.setVisibility(View.GONE);
                }
                break;
            case R.id.privacy_policy:
                drawerLayout.closeDrawers();
                startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
                break;
            case R.id.terms_of_use:
                drawerLayout.closeDrawers();
                startActivity(new Intent(getApplicationContext(), LegionTermsActivity.class));
                break;
            case R.id.notifi_settings:
                drawerLayout.closeDrawers();
                if (LegionUtils.isFeatureEnabled(this, "feature.calendarIntegration", "")) {
                    Intent settingsActivity = new Intent(this, SettingsActivity.class);
                    startActivity(settingsActivity);
                } else {
                    ComingSoonDialogFragment fragment = new ComingSoonDialogFragment();
                    String message = "With Settings, you can specify how you prefer to be notified for schedule and shift offer updates.";
                    Bundle b = new Bundle();
                    b.putString(Extras_Keys.MESSAGE, message);
                    fragment.setArguments(b);
                    fragment.show(getSupportFragmentManager(), "");
                }
                break;
            case R.id.work_preferences:
                drawerLayout.closeDrawers();
                startActivity(new Intent(getApplicationContext(), UpdateWorkPreferencesActivity.class));
                break;
            case R.id.profile:
                drawerLayout.closeDrawers();
                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));
                break;
            case R.id.availability:
                drawerLayout.closeDrawers();
                startActivity(new Intent(getApplicationContext(), UpdateAvailabilityActivityNew.class));
                break;
            case R.id.logout:
                drawerLayout.closeDrawers();
                LegionUtils.logoutChangesDialog(this);
                break;
        }
    }

    private void doSendFeedback() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/html");
            final PackageManager pm = this.getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            String className = null;
            for (final ResolveInfo info : matches) {
                if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                    className = info.activityInfo.name;
                    if (className != null && !className.isEmpty()) {
                        break;
                    }
                }
            }
            if (className != null) {
                emailIntent.setClassName("com.google.android.gm", className);
            }
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback@legion.co"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " App for " + Build.MANUFACTURER + " " + Build.MODEL);
            emailIntent.putExtra(Intent.EXTRA_TEXT, "I am using " + getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + "), and I want to share my feedback on the app:\n\n");
            try {
                startActivity(emailIntent);
            } catch (ActivityNotFoundException e) {
                showToast("There is no email client installed on this device.");
            } catch (Exception e) {
                showToast(e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tab2ContainerFragment tab2ContainerFragment = (Tab2ContainerFragment) getSupportFragmentManager().findFragmentByTag(HomeActivity.TAB_2_SPEC);
        if (tab2ContainerFragment != null) {
            tab2ContainerFragment.onActivityResult(requestCode, resultCode, data);
        }

        Tab3ContainerFragment tab3ContainerFragment = (Tab3ContainerFragment) getSupportFragmentManager().findFragmentByTag(HomeActivity.TAB_3_SPEC);
        if (tab3ContainerFragment != null) {
            tab3ContainerFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == 2) {
          //  LegionUtils.showProgressDialog(this);
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {

        if (requestCode == 2) {
            try {
                prefsManager.save(Prefs_Keys_Offline.PROFILE_OBJECT,response.toString());
                getScheduledPreferencesForSpinner(prefsManager.get(Prefs_Keys.WORKER_ID));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == WebServiceRequestCodes.SCHEDULED_PREFRERNCES) {
            LegionUtils.hideProgressDialog();
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    prefsManager.save(Prefs_Keys_Offline.SCHEDULE_PREFREENCES,response.toString());
                    prefsManager.save(Extras_Keys.SCHEDULED_PREFRENCES, response.toString());
                    Intent i = new Intent(getApplicationContext(), WelcomeScreenActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra(Extras_Keys.PROFILE_OBJECT, prefsManager.get(Prefs_Keys_Offline.PROFILE_OBJECT));
                    i.putExtra(Extras_Keys.SCHEDULED_PREFRENCES, prefsManager.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES));
                    startActivity(i);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                LegionUtils.showDialog(HomeActivity.this, e.getLocalizedMessage(), true);
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
        LegionUtils.showDialog(HomeActivity.this, reasonPhrase, true);
    }
}
