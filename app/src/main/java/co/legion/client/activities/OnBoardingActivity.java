package co.legion.client.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import base.Legion_BaseActivity;
import co.legion.client.R;
import de.hdodenhof.circleimageview.CircleImageView;
import fragments.HowIWouldLikeToWorkFragment;
import fragments.MyAvailabilityFragmentNew;
import fragments.MyProfileFragment;
import fragments.WhatsImportantFragment;
import helpers.Legion_PrefsManager;
import models.Slot;
import models.SlotType;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import ui.CirclePageIndicator;
import ui.CustomViewPager;
import ui.ErrorHandlingMethods;
import utils.LegionUtils;
import utils.Legion_Constants;


/**
 * Created by Administrator on 11/16/2016.
 */

public class OnBoardingActivity extends Legion_BaseActivity implements interfaces.CompleteProfileData, Legion_NetworkCallback, Legion_Constants, View.OnClickListener {

    public static final int INTERVAL_MINS = 30;
    public ArrayList<Slot> mondaySlotsList = new ArrayList<>();
    public ArrayList<Slot> tuesdaySlotsList = new ArrayList<>();
    public ArrayList<Slot> wednesdaySlotsList = new ArrayList<>();
    public ArrayList<Slot> thursdaySlotsList = new ArrayList<>();
    public ArrayList<Slot> fridaySlotsList = new ArrayList<>();
    public ArrayList<Slot> saturdaySlotsList = new ArrayList<>();
    public ArrayList<Slot> sundaySlotsList = new ArrayList<>();

    private boolean isAVUpdated;
    ImageView selectedImageview;
    Toolbar toolbar;
    String phone, email, profileObject;
    View viewPhone, viewEmail;
    ErrorHandlingMethods errorHandlingMethods;
    TextView textViewPhone, textViewemail;
    String userChoosenTask;
    CirclePageIndicator circlePageIndicator;
    TextView titleText;
    JSONObject jsonObjectValues;
    ImageButton navigationClose;
    JSONObject profileJsonObject;
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            btnPrev.setColorFilter(ContextCompat.getColor(OnBoardingActivity.this, R.color.white));
            customAvailabilityIv.setVisibility(View.INVISIBLE);
            clearNextBtnColorFilter();
            if (position == 0) {
                btnPrev.setColorFilter(ContextCompat.getColor(OnBoardingActivity.this, R.color.light_gray_2));
            } else {
                btnPrev.clearColorFilter();
                btnPrev.invalidate();
            }
            switch (position) {
                case 0:
                    titleText.setText(R.string.complete_my_profile);
                    btnNext.setImageResource(R.drawable.ic_next_arrow_white);
                    break;
                case 1:
                    titleText.setText(R.string.how_i_work);
                    btnNext.setImageResource(R.drawable.ic_next_arrow_white);
                    break;
                case 2:
                    customAvailabilityIv.setVisibility(View.INVISIBLE);
                    titleText.setText(R.string.when_i_prefer);
                    btnNext.setImageResource(R.drawable.ic_next_arrow_white);
                    break;
                case 3:
                    customAvailabilityIv.setVisibility(View.INVISIBLE);
                    titleText.setText(R.string.usually);
                    btnNext.setImageResource(R.drawable.ic_next_arrow_white);
                    break;
                case 4:
                    titleText.setText(R.string.whats_imp);
                    btnNext.setImageResource(R.drawable.ic_save_check_mark);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public void clearNextBtnColorFilter() {
        if (btnNext != null) {
            btnNext.setEnabled(true);
            btnNext.setColorFilter(null);
            btnNext.clearColorFilter();
            btnNext.invalidate();
        }
    }

    private Legion_PrefsManager legionPreferences;
    private CustomViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private ImageView btnPrev, btnNext;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private JSONObject likeToWorkJsonObject;
    private Cloudinary cloudinary;
    private LinearLayout toolTipLayout;
    private TextView gotItTv;
    private ImageView customAvailabilityIv;
    private String AVAILABILITY_START_DATE;
    private String weekStartDayOfTheYear;
    private boolean isScheduleLocked;
    private String calendarOption;
    private boolean haveFutureSchedules;
    private int weekStartDayOfTheYearInt;
    private String date1, date2;

    private void doProfileUpdateService(JSONObject jsonObject) {
        LegionUtils.hideKeyboard(this);
        if (LegionUtils.isOnline(this)) {
            try {
                Legion_RestClient restClient = new Legion_RestClient(this, this);
                restClient.performPostRequest(WebServiceRequestCodes.GET_PROFILE_UPDATE_CODE, ServiceUrls.PROFILE_UPDATE_URL, jsonObject, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LegionUtils.showOfflineDialog(this);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_setupwizardlayout);
        cloudinary = new Cloudinary("cloudinary://716358787461634:ebF6_v0SdT_xZ4RLDEXuVcDCFFA@legiontech");
        new LongOperation().execute("");
        toolTipLayout = (LinearLayout) findViewById(R.id.toolTipLayout);
        gotItTv = (TextView) findViewById(R.id.gotItTv);
        gotItTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolTipLayout.setVisibility(View.GONE);
                legionPreferences.saveBoolean(Prefs_Keys.FIRST_TIME, false);
            }
        });
        customAvailabilityIv = (ImageView) findViewById(R.id.customAvailabilityIv);
        customAvailabilityIv.setOnClickListener(this);
        setupMondaySlotsNew(mondaySlotsList);
        setupTuesdaySlotsNew(tuesdaySlotsList);
        setupWednesdaySlotsNew(wednesdaySlotsList);
        setupThursdaySlotsNew(thursdaySlotsList);
        setupFridaySlotsNew(fridaySlotsList);
        setupSaturdaySlotsNew(saturdaySlotsList);
        setupSundaySlotsNew(sundaySlotsList);

        doLoadWeeklySchedules();
    }

    private void doLoadWeeklySchedules() {
        if (!LegionUtils.isOnline(this)) {
            LegionUtils.showOfflineDialog(this);
            return;
        }
        LegionUtils.hideKeyboard(this);
        Log.v("SESSION_ID", "" + legionPreferences.get(Prefs_Keys.SESSION_ID));

        LegionUtils.showProgressDialog(this);
        Calendar cal = Calendar.getInstance();
        RequestParams params = new RequestParams();
        params.put("year", cal.get(Calendar.YEAR));
        cal.add(Calendar.DATE, cal.get(Calendar.DAY_OF_WEEK) * -1);
        params.put("weekStartDayOfTheYear", cal.get(Calendar.DAY_OF_YEAR));
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE, ServiceUrls.GET_SCHEDULES_URL, params, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialization() {
        LegionUtils.doApplyFont(getAssets(), (RelativeLayout) findViewById(R.id.parentLayout));
        legionPreferences = new Legion_PrefsManager(this);

        try {
            if (legionPreferences.hasKey(Prefs_Keys_Offline.PROFILE_OBJECT) && legionPreferences.hasKey(Prefs_Keys_Offline.SCHEDULE_PREFREENCES)) {
                profileObject = legionPreferences.get(Prefs_Keys_Offline.PROFILE_OBJECT);
                jsonObjectValues = new JSONObject(legionPreferences.get(Prefs_Keys_Offline.SCHEDULE_PREFREENCES));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        errorHandlingMethods = new ErrorHandlingMethods();

        //Initializing the views
        initViews();
        navigationClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsManager.save(Prefs_Keys.IS_LOGGED_IN, "1");
                startActivity(new Intent(OnBoardingActivity.this, HomeActivity.class));
                finish();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoardingActivity.this, HomeActivity.class));
            }
        });
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), 5);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        circlePageIndicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cPosition = viewPager.getCurrentItem();
                if (cPosition != 0) {
                    viewPager.setCurrentItem(--cPosition);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setEnabled(false);
                btnNext.setColorFilter(ContextCompat.getColor(OnBoardingActivity.this, R.color.light_gray_2));
                LegionUtils.hideKeyboard(OnBoardingActivity.this);
                int current = getItem(+1);
                if (current < 5) {
                    // move to next screen
                    if (current == 1) {
                        MyProfileFragment profileFragment = (MyProfileFragment) myViewPagerAdapter.getItem(0);
                        boolean validated = profileFragment.doValidateProfileInputs();
                        if (validated) {
                            LegionUtils.hideKeyboard(OnBoardingActivity.this);
                            profileJsonObject = profileFragment.getInputtedData();
                            doProfileUpdateService(profileJsonObject);
                        } else {
                            clearNextBtnColorFilter();
                        }
                    } else if (current == 2) {
                        LegionUtils.showProgressDialog(OnBoardingActivity.this);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                if ((profileJsonObject != null) && (profileJsonObject.length() > 0)) {
                                    HowIWouldLikeToWorkFragment fragment = (HowIWouldLikeToWorkFragment) myViewPagerAdapter.getItem(1);
                                    likeToWorkJsonObject = fragment.getInputtedData();
                                    Log.e("HowIWould Data", "" + fragment.getInputtedData());
                                    if (likeToWorkJsonObject != null) {
                                        viewPager.setCurrentItem(2);
                                    } else {
                                        clearNextBtnColorFilter();
                                    }
                                    if (legionPreferences.getBoolean(Prefs_Keys.FIRST_TIME)) {
                                        toolTipLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        toolTipLayout.setVisibility(View.GONE);
                                    }
                                } else {
                                    clearNextBtnColorFilter();
                                }
                                LegionUtils.hideProgressDialog();
                            }
                        }, 200);
                    } else if (current == 3) {
                        viewPager.setCurrentItem(current);
                    } else if (current == 4) {
                        // Saving the previous screen data
                        doUpdateAvailabilityPrefsToServer();
                    }
                } else {
                    WhatsImportantFragment fragment = (WhatsImportantFragment) myViewPagerAdapter.getItem(4);
                    likeToWorkJsonObject = fragment.getInputtedData(likeToWorkJsonObject);

                    JSONObject outerMostJObj = new JSONObject();
                    try {
                        outerMostJObj.put("schedulePreferences", likeToWorkJsonObject);
                        outerMostJObj.put("token", "");
                        Log.d("completeObj", "" + outerMostJObj);
                        if (LegionUtils.isOnline(OnBoardingActivity.this)) {
                            doUpdateScheduledPrefsService(outerMostJObj);
                        } else {
                            LegionUtils.showOfflineDialog(OnBoardingActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void doUpdateAvailabilityPrefsToServer() {
        try {
            JSONObject availabilityJsonObj = new JSONObject();
            /*availabilityJsonObj.put("firstName", prefsManager.get(Prefs_Keys.FIRST_NAME));
            availabilityJsonObj.put("lastName", prefsManager.get(Prefs_Keys.LAST_NAME));
            availabilityJsonObj.put("email", prefsManager.get(Prefs_Keys.EMAIL));
            availabilityJsonObj.put("phoneNumber", prefsManager.get(Prefs_Keys.PHONE_NUMBER));*/
            availabilityJsonObj.put("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            if (weekStartDayOfTheYear != null && !haveFutureSchedules) {
                availabilityJsonObj.put("weekStartDayOfTheYear", Integer.parseInt(weekStartDayOfTheYear) + 1); // Adding +1 as we need to pass weekStartDayOfTheYear as MONDAY, but not SUNDAY..
            } else if (haveFutureSchedules) {
                availabilityJsonObj.put("weekStartDayOfTheYear", weekStartDayOfTheYearInt);
            }
            availabilityJsonObj.put("calendarOption", "ForWeek");
            availabilityJsonObj.put("workerId", legionPreferences.get(Prefs_Keys.WORKER_ID));
            availabilityJsonObj.put("businessId", legionPreferences.get(Prefs_Keys.BUSINESS_ID));
            availabilityJsonObj.put("isScheduleLocked", isScheduleLocked);
            JSONArray slotJsonArray = new JSONArray();

            putSlotAvailabilityIntoJsonArray(mondaySlotsList, slotJsonArray, 2);
            putSlotAvailabilityIntoJsonArray(tuesdaySlotsList, slotJsonArray, 3);
            putSlotAvailabilityIntoJsonArray(wednesdaySlotsList, slotJsonArray, 4);
            putSlotAvailabilityIntoJsonArray(thursdaySlotsList, slotJsonArray, 5);
            putSlotAvailabilityIntoJsonArray(fridaySlotsList, slotJsonArray, 6);
            putSlotAvailabilityIntoJsonArray(saturdaySlotsList, slotJsonArray, 7);
            putSlotAvailabilityIntoJsonArray(sundaySlotsList, slotJsonArray, 1);

            availabilityJsonObj.put("slot", slotJsonArray);

            if (!LegionUtils.isOnline(this)) {
                showToast(getString(R.string.device_offline_alert_message));
                viewPager.setCurrentItem(4);
                return;
            }
            LegionUtils.showProgressDialog(this);
            LegionUtils.hideKeyboard(this);
            try {
                Legion_RestClient restClient = new Legion_RestClient(this, this);
                restClient.performPostRequest(WebServiceRequestCodes.UPDATE_AVAILABILITY, ServiceUrls.UPDATE_AVAILABILITY_PREFERENCES, availabilityJsonObj, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAVUpdateAlert(Context context, String msg, String jobdescption, int drawable) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.good_job_dilalog_with_desc);
        final TextView msgTv = (TextView) dialog.findViewById(R.id.msgTv);
        final TextView jobdesc = (TextView) dialog.findViewById(R.id.job_desc);
        final ImageView statusIv = (ImageView) dialog.findViewById(R.id.statusIv);
        statusIv.setImageResource(drawable);
        final TextView okTv = (TextView) dialog.findViewById(R.id.okTv);
        msgTv.setText(msg);
        msgTv.setVisibility(View.GONE);
        jobdesc.setText(Html.fromHtml(jobdescption));
        LegionUtils.doApplyFont(context.getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void putSlotAvailabilityIntoJsonArray(ArrayList<Slot> slotsList, JSONArray slotJsonArray, int dayOfWeek) throws Exception {
        for (int i = 0; i < slotsList.size(); ++i) {
            Slot slot = slotsList.get(i);
            if (slot.getSlotType().getType().equals(SlotType.GREEN.getType())) {
                JSONObject slotJsonObj = new JSONObject();
                slotJsonObj.put("positiveFlag", true);
                slotJsonObj.put("dayOfTheWeek", dayOfWeek);
                slotJsonObj.put("startMinutes", slot.getActualStartTimeOfSlot());
                Slot sameTypeLastSlot = getSameTypeLastSlotInRow(slotsList, slot);
                slotJsonObj.put("endMinutes", sameTypeLastSlot.getActualEndTimeOfSlot());
                i = sameTypeLastSlot.getSlotSerialNumber();
                slotJsonArray.put(slotJsonObj);
            } else if (slot.getSlotType().getType().equals(SlotType.RED.getType())) {
                JSONObject slotJsonObj = new JSONObject();
                slotJsonObj.put("positiveFlag", false);
                slotJsonObj.put("dayOfTheWeek", dayOfWeek);
                slotJsonObj.put("startMinutes", slot.getActualStartTimeOfSlot());
                Slot sameTypeLastSlot = getSameTypeLastSlotInRow(slotsList, slot);
                slotJsonObj.put("endMinutes", sameTypeLastSlot.getActualEndTimeOfSlot());
                i = sameTypeLastSlot.getSlotSerialNumber();
                slotJsonArray.put(slotJsonObj);
            }
        }
    }

    private Slot getSameTypeLastSlotInRow(ArrayList<Slot> slotsList, Slot initialSlot) {
        if (initialSlot.getSlotSerialNumber() == slotsList.size() - 1) {
            return slotsList.get(slotsList.size() - 1);
        }

        for (int i = initialSlot.getSlotSerialNumber(); i < slotsList.size(); ++i) {
            Slot nextSlot = slotsList.get(i);
            if (initialSlot.getSlotType().getType().equals(nextSlot.getSlotType().getType())) {
                if (i >= 36) {  //if we reach END then just return the last element
                    return slotsList.get(slotsList.size() - 1);
                }
            } else {
                return slotsList.get(i - 1);
            }
        }
        return slotsList.get(1 + initialSlot.getSlotSerialNumber());
    }

    private void doLoadAvailabilityPreferences(int weekStartDayOfTheYearInt, String firstName, String lastName, String email, String phoneNumber) {
        if (LegionUtils.isOnline(this)) {
            try {
                Legion_RestClient restClient = new Legion_RestClient(this, this);
                RequestParams reqObject = new RequestParams();
                /*reqObject.put("firstName", firstName);
                reqObject.put("lastName", lastName);
                reqObject.put("email", email);
                reqObject.put("phoneNumber", phoneNumber);*/
                ArrayList avbtyDates = LegionUtils.getDates();
                AVAILABILITY_START_DATE = avbtyDates.get(0).toString();
                String year = LegionUtils.getYearFromDate(AVAILABILITY_START_DATE);
                reqObject.put("year", year);
                reqObject.put("workerId", legionPreferences.get(Prefs_Keys.WORKER_ID));
                reqObject.put("businessId", legionPreferences.get(Prefs_Keys.BUSINESS_ID));
                if (weekStartDayOfTheYearInt == -1) {
                    weekStartDayOfTheYear = LegionUtils.getCountOfDaysWithDateFormat(year + "-01-01T00:00:00.000-00", AVAILABILITY_START_DATE);
                    reqObject.put("weekStartDayOfTheYear", Integer.parseInt(weekStartDayOfTheYear) + 1); // Adding +1 as we need to pass weekStartDayOfTheYear as MONDAY, but not SUNDAY..
                } else {
                    reqObject.put("weekStartDayOfTheYear", weekStartDayOfTheYearInt);
                }
                restClient.performHTTPGetRequest(WebServiceRequestCodes.QUERY_AVAILABILITY_PREFRENCE_CODE, ServiceUrls.QUERY_AVAILALIABLITY_URL, reqObject, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (legionPreferences.hasKey(Prefs_Keys_Offline.AVALIABILTY_PREFS)) {
                doParseAvailabilityPrefs(legionPreferences.get(Prefs_Keys_Offline.AVALIABILTY_PREFS));
            }
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        titleText = (TextView) findViewById(R.id.title);
        viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        btnPrev = (ImageView) findViewById(R.id.btn_prev);
        btnPrev.setColorFilter(ContextCompat.getColor(OnBoardingActivity.this, R.color.light_gray_2));
        btnNext = (ImageView) findViewById(R.id.btn_next);
        viewPhone = findViewById(R.id.phoneView);
        viewEmail = findViewById(R.id.emailView);
        textViewPhone = (TextView) findViewById(R.id.phoneErrorTextProfile);
        textViewemail = (TextView) findViewById(R.id.emailErrorTextProfile);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.viewpagerIndicator);
        titleText.setText("Complete my profile");
        navigationClose = (ImageButton) findViewById(R.id.closeSetup);
        viewPager.setSwipingEnabled(false);
    }


    private void doUpdateScheduledPrefsService(JSONObject jsonObject) {
        LegionUtils.showProgressDialog(this);
        LegionUtils.hideKeyboard(this);
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            restClient.performPostRequest(WebServiceRequestCodes.UPDATE_SCHEDULED_PREFS_CODE, ServiceUrls.UPDATE_SCEDULED_PREFS_URL, jsonObject, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(intent);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(intent);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        ImageView imageReset = (ImageView) findViewById(R.id.imageReset);
        ImageView uploadImage = (ImageView) findViewById(R.id.uploadImage);
        ImageButton uploadImageInvisible = (ImageButton) findViewById(R.id.uploadImageInvisible);
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        imageReset.setVisibility(View.VISIBLE);
        uploadImage.setVisibility(View.GONE);
        uploadImageInvisible.setVisibility(View.VISIBLE);
        //  frameLayout.setVisibility(View.GONE);
        CircleImageView selectedImageview = (CircleImageView) findViewById(R.id.circleImageIV);
        selectedImageview.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        try {
            // Map params = ObjectUtils.asMap("public_id", "my_folder/my_sub_folder/my_name");
            Uri uri = data.getData();
            FileInputStream in = (FileInputStream) getContentResolver().openInputStream(uri);
            Map uploadResult = cloudinary.uploader().upload(in, ObjectUtils.emptyMap());
            Log.d("rese", "" + uploadResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView imageReset = (ImageView) findViewById(R.id.imageReset);
        ImageView uploadImage = (ImageView) findViewById(R.id.uploadImage);
        ImageButton uploadImageInvisible = (ImageButton) findViewById(R.id.uploadImageInvisible);
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageReset.setVisibility(View.VISIBLE);
        uploadImage.setVisibility(View.GONE);
        uploadImageInvisible.setVisibility(View.VISIBLE);
        CircleImageView selectedImageview = (CircleImageView) findViewById(R.id.circleImageIV);
        selectedImageview.setImageBitmap(bm);
    }


    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.GET_PROFILE_UPDATE_CODE) {
            LegionUtils.showProgressDialog(this);
        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.GET_SCHEDULES_REQUEST_CODE) {
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.getString("responseStatus").equals("SUCCESS")) {
                    if (!jsonObject.isNull("weeklyScheduleRatings")) {
                        JSONArray weeklySchedulesArray = jsonObject.getJSONArray("weeklyScheduleRatings");
                        if (weeklySchedulesArray.length() >= 1) {
                            haveFutureSchedules = true;
                            int weekStartDayOfTheYearOfLastSchedule = weeklySchedulesArray.getJSONObject(weeklySchedulesArray.length() - 1).getInt("weekStartDayOfTheYear");
                            weekStartDayOfTheYearOfLastSchedule += 7;
                            this.weekStartDayOfTheYearInt = weekStartDayOfTheYearOfLastSchedule;
                            date1 = weeklySchedulesArray.getJSONObject(weeklySchedulesArray.length() - 1).getString("endOfWeekDate").split("T")[0];

                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "MMMM d";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            inputFormat = LegionUtils.getInputDateFormatterTimeZone(inputFormat);

                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                            outputFormat = LegionUtils.getOutputDateFormatterTimeZone(outputFormat, legionPreferences);

                            Date date = inputFormat.parse(date1);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            cal.add(Calendar.DATE, 1);

                            date1 = outputFormat.format(date);
                            date2 = outputFormat.format(cal.getTime());
                            doLoadAvailabilityPreferences(weekStartDayOfTheYearOfLastSchedule, legionPreferences.get(Prefs_Keys.FIRST_NAME), legionPreferences.get(Prefs_Keys.LAST_NAME), legionPreferences.get(Prefs_Keys.EMAIL), legionPreferences.get(Prefs_Keys.PHONE_NUMBER));
                        } else {
                            doLoadAvailabilityPreferences(-1, legionPreferences.get(Prefs_Keys.FIRST_NAME), legionPreferences.get(Prefs_Keys.LAST_NAME), legionPreferences.get(Prefs_Keys.EMAIL), legionPreferences.get(Prefs_Keys.PHONE_NUMBER));
                        }
                    } else {
                        doLoadAvailabilityPreferences(-1, legionPreferences.get(Prefs_Keys.FIRST_NAME), legionPreferences.get(Prefs_Keys.LAST_NAME), legionPreferences.get(Prefs_Keys.EMAIL), legionPreferences.get(Prefs_Keys.PHONE_NUMBER));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                doLoadAvailabilityPreferences(-1, legionPreferences.get(Prefs_Keys.FIRST_NAME), legionPreferences.get(Prefs_Keys.LAST_NAME), legionPreferences.get(Prefs_Keys.EMAIL), legionPreferences.get(Prefs_Keys.PHONE_NUMBER));
            }
        } else if (requestCode == WebServiceRequestCodes.GET_PROFILE_UPDATE_CODE) {
            viewPager.setCurrentItem(1);
            LegionUtils.hideProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                Log.d("result" + WebServiceRequestCodes.GET_PROFILE_UPDATE_CODE, jsonObject.toString());
                if (jsonObject.getString("responseStatus").equals("SUCCESS")) {
                    legionPreferences.save(Prefs_Keys.IS_LOGGED_IN, "1");
                    legionPreferences.save(Prefs_Keys_Offline.ONBOARDING, "" + (System.currentTimeMillis() / 1000));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == WebServiceRequestCodes.UPDATE_SCHEDULED_PREFS_CODE) {
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.getString("responseStatus").equals("SUCCESS")) {
                    doCompleteOnBoarding();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(OnBoardingActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == WebServiceRequestCodes.QUERY_AVAILABILITY_PREFRENCE_CODE) {
            LegionUtils.hideProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.getString("responseStatus").equals("SUCCESS")) {
                    legionPreferences.save(Prefs_Keys_Offline.AVALIABILTY_PREFS, response.toString());
                    doParseAvailabilityPrefs(response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(OnBoardingActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == WebServiceRequestCodes.UPDATE_AVAILABILITY) {
            LegionUtils.hideProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
                if (jsonObject.getString("responseStatus").equals("SUCCESS")) {
                    if (haveFutureSchedules && (((MyAvailabilityFragmentNew) myViewPagerAdapter.getItem(2)).isAVUpdated || ((MyAvailabilityFragmentNew) myViewPagerAdapter.getItem(3)).isAVUpdated)) {
                        showAVUpdateAlert(this, "", "You already have schedule through " + date1 + ".<br/><br/>Your updated availability will be applied from " + date2 + " onwards.", R.drawable.confirmation);
                    } else {
                        viewPager.setCurrentItem(4);
                    }
                    ((MyAvailabilityFragmentNew) myViewPagerAdapter.getItem(2)).isAVUpdated = false;
                    ((MyAvailabilityFragmentNew) myViewPagerAdapter.getItem(3)).isAVUpdated = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                viewPager.setCurrentItem(4);
            }
        }
    }

    /**
     * Updates the worker onboardingCompletedTimestamp with current timestamp.
     */
    private void doCompleteOnBoarding() {
        try {
            if (profileJsonObject != null && profileJsonObject.has("worker")) {
                JSONArray ws = profileJsonObject.getJSONArray("worker");
                if (ws.length() != 1) {
                    Log.e(this.getClass().getName(), "Error completing onboarding: expected 1 worker, found " + ws.length());
                }
                legionPreferences.save(Prefs_Keys_Offline.ONBOARDING, System.currentTimeMillis() + "");
                ws.getJSONObject(0).put("onboardingCompletedTimestamp", System.currentTimeMillis());
                doProfileUpdateService(profileJsonObject);
            }
        } catch (JSONException e) {
            Log.e(this.getClass().getName(), "Error completing onboarding", e);
        }
    }

    private void doParseAvailabilityPrefs(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject availabilityJsonObj = jsonObject.optJSONObject("availability");
            if (availabilityJsonObj != null) {
                calendarOption = availabilityJsonObj.optString("calendarOption");
                isScheduleLocked = availabilityJsonObj.optBoolean("isScheduleLocked");
                JSONArray slotsJsonArray = availabilityJsonObj.getJSONArray("slot");
                if (slotsJsonArray != null && slotsJsonArray.length() >= 1) {
                    int size = slotsJsonArray.length();
                    for (int i = 0; i < size; ++i) {
                        JSONObject slotJsonObj = slotsJsonArray.getJSONObject(i);
                        int dayOfWeek = slotJsonObj.getInt("dayOfTheWeek");
                        int endMins = slotJsonObj.getInt("endMinutes");
                        int startMins = slotJsonObj.getInt("startMinutes");
                        boolean positiveFlag = slotJsonObj.getBoolean("positiveFlag");

                        if (dayOfWeek == 1) {
                            doUpdateSlots(sundaySlotsList, startMins, endMins, positiveFlag);
                        } else if (dayOfWeek == 2) {
                            doUpdateSlots(mondaySlotsList, startMins, endMins, positiveFlag);
                        } else if (dayOfWeek == 3) {
                            doUpdateSlots(tuesdaySlotsList, startMins, endMins, positiveFlag);
                        } else if (dayOfWeek == 4) {
                            doUpdateSlots(wednesdaySlotsList, startMins, endMins, positiveFlag);
                        } else if (dayOfWeek == 5) {
                            doUpdateSlots(thursdaySlotsList, startMins, endMins, positiveFlag);
                        } else if (dayOfWeek == 6) {
                            doUpdateSlots(fridaySlotsList, startMins, endMins, positiveFlag);
                        } else if (dayOfWeek == 7) {
                            doUpdateSlots(saturdaySlotsList, startMins, endMins, positiveFlag);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //updating the initially created slots with server data.
    private void doUpdateSlots(ArrayList<Slot> slotsList, int startMins, int endMins, boolean positiveFlag) {
        for (int i = 0; i < slotsList.size(); ++i) {
            Slot slot = slotsList.get(i);
            if (slot.getActualStartTimeOfSlot() >= startMins && slot.getActualEndTimeOfSlot() <= endMins && positiveFlag) {
                slot.setSlotType(SlotType.GREEN);
            } else if (slot.getActualStartTimeOfSlot() >= startMins && slot.getActualEndTimeOfSlot() <= endMins && !positiveFlag) {
                slot.setSlotType(SlotType.RED);
            }
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
        clearNextBtnColorFilter();
        if (reasonPhrase == null) {
            return;
        }
        if (reasonPhrase.contains("Something went wrong")) {
            LegionUtils.doLogout(this);
            return;
        }
        if (requestCode == WebServiceRequestCodes.GET_PROFILE_UPDATE_CODE) {
            if (reasonPhrase != null)
                LegionUtils.showDialog(this, reasonPhrase, true);
        } else {
            LegionUtils.showDialog(OnBoardingActivity.this, reasonPhrase, true);
        }
    }

    public void setupMondaySlotsNew(ArrayList<Slot> mondaySlotsList) {

        int j = 0;
        for (int i = 240; i < 1440; i += INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + INTERVAL_MINS);
            slot.setDayOfTheWeek(1);
            mondaySlotsList.add(slot);
        }
    }

    public void setupTuesdaySlotsNew(ArrayList<Slot> tuesdaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + INTERVAL_MINS);
            slot.setDayOfTheWeek(2);
            tuesdaySlotsList.add(slot);
        }
    }

    public void setupWednesdaySlotsNew(ArrayList<Slot> wednesdaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + INTERVAL_MINS);
            slot.setDayOfTheWeek(3);
            wednesdaySlotsList.add(slot);
        }
    }

    public void setupThursdaySlotsNew(ArrayList<Slot> thursdaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + INTERVAL_MINS);
            slot.setDayOfTheWeek(4);
            thursdaySlotsList.add(slot);
        }
    }

    public void setupFridaySlotsNew(ArrayList<Slot> fridaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + INTERVAL_MINS);
            slot.setDayOfTheWeek(5);
            fridaySlotsList.add(slot);
        }
    }

    public void setupSaturdaySlotsNew(ArrayList<Slot> saturdaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + INTERVAL_MINS);
            slot.setDayOfTheWeek(6);
            saturdaySlotsList.add(slot);
        }
    }

    public void setupSundaySlotsNew(ArrayList<Slot> sundaySlotsList) {
        int j = 0;
        for (int i = 240; i < 1440; i += INTERVAL_MINS) {
            Slot slot = new Slot();
            slot.setSlotSerialNumber(j++);
            slot.setWeight(1);
            slot.setStartTime(doConvertMinsToTime(i));
            slot.setEndTime(doConvertMinsToTime(i + INTERVAL_MINS));
            slot.setSlotType(SlotType.EMPTY);
            slot.setAvailabilityMode(true);
            slot.setActualStartTimeOfSlot(i);
            slot.setActualEndTimeOfSlot(i + INTERVAL_MINS);
            slot.setDayOfTheWeek(7);
            sundaySlotsList.add(slot);
        }
    }

    private String doConvertMinsToTime(int intputTimeInMins) {
        int mins = intputTimeInMins % 60;
        int hrs = intputTimeInMins / 60;

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
    public void onClick(View v) {
    }

    public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
        int tabCount;
        SparseArray<Fragment> registeredFragments;

        public MyViewPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
            registeredFragments = new SparseArray<>();
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = registeredFragments.get(position, null);
            if (fragment != null) {
                return fragment;
            }
            switch (position) {
                case 0:
                    Bundle bundle = new Bundle();
                    bundle.putString(Extras_Keys.PROFILE_OBJECT, profileObject);
                    try {
                        JSONObject jsonObject = new JSONObject(profileObject);
                        jsonObject.getString("email");
                        jsonObject.getString("phoneNumber");
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    fragment = new MyProfileFragment(null);
                    fragment.setArguments(bundle);
                    break;
                case 1:
                    fragment = new HowIWouldLikeToWorkFragment(null);
                    Bundle bundleWork = new Bundle();
                    bundleWork.putString("viewType", "viewPager");
                    if (jsonObjectValues != null) {
                        bundleWork.putString("jsonObjectValues", jsonObjectValues.toString());
                    }
                    fragment.setArguments(bundleWork);
                    break;
                case 2:
                    fragment = new MyAvailabilityFragmentNew(null, false, false, true, true);
                    break;
                case 3:
                    fragment = new MyAvailabilityFragmentNew(null, false, false, false, true);
                    break;
                case 4:
                    fragment = new WhatsImportantFragment(null);
                    Bundle bundleWhatImp = new Bundle();
                    bundleWhatImp.putString("viewType", "viewPager");
                    bundleWhatImp.putString("jsonObjectValues", jsonObjectValues.toString());
                    fragment.setArguments(bundleWhatImp);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tabCount;
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int position) {
            try {
                Object obj = super.instantiateItem(viewGroup, position);
                if (obj instanceof Fragment) {
                    registeredFragments.put(position, (Fragment) obj);
                }
                return obj;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Remove the reference when destroy it
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initialization();
            LegionUtils.showProgressDialog(OnBoardingActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception";
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            LegionUtils.hideProgressDialog();
        }
    }
}
