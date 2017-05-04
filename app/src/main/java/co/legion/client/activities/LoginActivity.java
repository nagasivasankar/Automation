package co.legion.client.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import base.Legion_BaseActivity;
import co.legion.client.R;
import helpers.Legion_PrefsManager;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/16/2016.
 */

public class LoginActivity extends Legion_BaseActivity implements Legion_NetworkCallback, View.OnFocusChangeListener, Legion_Constants {
    EditText usernameEt, passwordEdittext;
    TextView loginButton;
    ImageButton userNameStatusIv;
    ImageView passowrdStatusButton;
    TextView usernameErrorText, passwordErrorText, forgotPasswordText, legalStatementTV;
    View userNameView, passowrdView;
    private Legion_PrefsManager legionPreferences;
    private JSONObject businessObject;
    private String onboardingCompletedTimestamp;
    private JSONObject profileObject;
    private boolean checkStatus;
    private boolean checkUserNameStatus;
    private boolean checkPwdStatus;
    private TextView forgotPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        TextView createAccountTv = (TextView) findViewById(R.id.createAccountTv);
        createAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, VerifyIdentityActivity.class);
                startActivity(i);
            }
        });
        legionPreferences = new Legion_PrefsManager(this);
        legionPreferences.save(Prefs_Keys.OFFER_TYPE, "Open");
        legionPreferences.save(Prefs_Keys.OFFER_STATUS, "Proposed");

        if (getIntent() != null && getIntent().hasExtra(Extras_Keys.IS_LOGGED_OUT)) {
            LegionUtils.showMessageDialog(this, "Logging out due to invalid session. Please try again later.", R.drawable.error_transient);
        }
        intiViews();

        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAction();
            }
        });

        LinearLayout topSection = (LinearLayout) findViewById(R.id.topSection);
        topSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LegionUtils.hideKeyboard(LoginActivity.this);
            }
        });

        findViewById(R.id.legionLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LegionUtils.hideKeyboard(LoginActivity.this);
            }
        });
        LegionUtils.getDisplayMetrics(this);
    }

    public void loginAction() {
        legionPreferences.saveBoolean(Prefs_Keys.FIRST_TIME, true);
        String userName = usernameEt.getText().toString();
        String password = passwordEdittext.getText().toString();
        String lUserName = null, lpassword = null;
        boolean checkValidations = true;
        if (userName.equals("") && password.equals("")) {
            usernameErrorText.setVisibility(View.VISIBLE);
            usernameErrorText.setText("");
            userNameStatusIv.setVisibility(View.VISIBLE);
            userNameStatusIv.setImageResource(R.drawable.ic_error);
            checkUserNameStatus = true;
            userNameView.setBackgroundColor(Color.parseColor("#DE1919"));
            passwordErrorText.setVisibility(View.VISIBLE);
            usernameErrorText.setText(R.string.usernameError);
            passwordErrorText.setText(R.string.passwordError);
            //  passwordErrorText.setText(R.string.emailpwderror);
            passowrdView.setBackgroundColor(Color.parseColor("#DE1919"));
            passowrdStatusButton.setVisibility(View.VISIBLE);
            userNameStatusIv.setClickable(true);
            passowrdStatusButton.setClickable(true);
            checkPwdStatus = true;
            passowrdStatusButton.setImageResource(R.drawable.ic_error);
            checkValidations = false;
        } else {
            if (userName.equals("")) {
                usernameErrorText.setVisibility(View.VISIBLE);
                usernameErrorText.setText(R.string.usernameError);
                userNameStatusIv.setVisibility(View.VISIBLE);
                userNameStatusIv.setImageResource(R.drawable.ic_error);
                checkUserNameStatus = true;
                userNameView.setBackgroundColor(Color.parseColor("#DE1919"));
                checkValidations = false;
            } else {
                lUserName = usernameEt.getText().toString().trim();
                userNameStatusIv.setVisibility(View.GONE);
                userNameView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrey));
                userNameStatusIv.setImageResource(R.drawable.ic_confirm);
                /*if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userNameStatusIv.setVisibility(View.VISIBLE);
                    usernameErrorText.setVisibility(View.INVISIBLE);
                    userNameStatusIv.setImageResource(R.drawable.ic_confirm);
                    userNameView.setBackgroundColor(Color.parseColor("#202020"));
                    lEmail = email;
                } else {
                    usernameErrorText.setVisibility(View.VISIBLE);
                    usernameErrorText.setText(R.string.emailError);
                    userNameStatusIv.setVisibility(View.VISIBLE);
                    userNameStatusIv.setImageResource(R.drawable.ic_error);
                    userNameView.setBackgroundColor(Color.parseColor("#DE1919"));
                    checkValidations = false;
                }*/
            }

            if (password.equals("")) {
                passwordErrorText.setVisibility(View.VISIBLE);
                passwordErrorText.setText(R.string.passwordError);
                passowrdView.setBackgroundColor(Color.parseColor("#DE1919"));
                passowrdStatusButton.setVisibility(View.VISIBLE);
                passowrdStatusButton.setClickable(true);
                passowrdStatusButton.setImageResource(R.drawable.ic_error);
                checkPwdStatus = true;
                checkValidations = false;
            } else {
                if (password.length() < 6) {
                    passowrdView.setBackgroundColor(Color.parseColor("#DE1919"));
                    passwordErrorText.setVisibility(View.VISIBLE);
                    passwordErrorText.setText(R.string.passwordError);
                    passowrdStatusButton.setVisibility(View.VISIBLE);
                    passowrdStatusButton.setClickable(true);
                    checkPwdStatus = true;
                    passowrdStatusButton.setImageResource(R.drawable.ic_error);
                    checkValidations = false;
                } else {
                    lpassword = password;
                    passwordErrorText.setVisibility(View.INVISIBLE);
                    passowrdStatusButton.setVisibility(View.GONE);
                    passowrdStatusButton.setImageResource(R.drawable.ic_confirm);
                    passowrdStatusButton.setClickable(false);
                    passowrdView.setBackgroundColor(Color.parseColor("#202020"));
                    passowrdView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrey));
                }
            }
        }
        if (checkValidations) {
            if (LegionUtils.isOnline(this)) {
                if (lUserName != null && lpassword != null) {
                    try {
                        String token = FirebaseInstanceId.getInstance().getToken();
                        if (token != null) {
                            Log.w("notification", token);
                        }
                    } catch (Exception e) {
                        Log.d("Exception", "Firebase not initialized");
                    }
                    loginButton.setText("Logging in...");
                    doLogin(lUserName, lpassword);
                    checkUserNameStatus = false;
                    checkPwdStatus = false;
                }
            } else {
                LegionUtils.showOfflineDialog(this);
            }
        }
    }

    void doLogin(String email, String password) {
        LegionUtils.hideKeyboard(this);
        try {
            legionPreferences.save(Prefs_Keys.IS_LOGGED_IN, "0");
            LegionUtils.showProgressDialog(this);
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            JSONObject reqObject = new JSONObject();
            reqObject.put("userName", email);
            reqObject.put("plainTextPassword", password);
            restClient.performHttpPostRequest(this, WebServiceRequestCodes.LOGIN, ServiceUrls.LOGIN_URL, reqObject, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //initiating the views
    private void intiViews() {
        loginButton = (TextView) findViewById(R.id.login);
        usernameEt = (EditText) findViewById(R.id.usernameET);
        passwordEdittext = (EditText) findViewById(R.id.passwordEditText);
        userNameStatusIv = (ImageButton) findViewById(R.id.usernameStatusImage);
        passowrdStatusButton = (ImageView) findViewById(R.id.passwordStatusImage);
        usernameErrorText = (TextView) findViewById(R.id.usernameErrorText);
        passwordErrorText = (TextView) findViewById(R.id.passwordErrorText);
        forgotPasswordText = (TextView) findViewById(R.id.forgotPassword);
        legalStatementTV = (TextView) findViewById(R.id.termofuse);
        userNameView = findViewById(R.id.emailView);
        passowrdView = findViewById(R.id.passwordView);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSendEmail_ForgotPassword();
            }
        });
        passwordEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginAction();
                    return true;
                }
                return false;
            }
        });

        //TODO comment credentials
        /*usernameEt.setText("lina.deleon@legion.co");
        usernameEt.setText("json.wu@mikotest.com");
        usernameEt.setText("adalbert@legion.co");
        usernameEt.setText("brad.tomkins@legion.co");
        usernameEt.setText("johny.walter@legion.co");
        usernameEt.setText("gowarriors");
        usernameEt.setText("dexter.jenkins@legion.co");
        usernameEt.setText("justin.molinario@legion.co");
        usernameEt.setText("calvin.mwila@legion.co");
        usernameEt.setText("sam.ford@legion.co");
        usernameEt.setText("rahul@legion.co");
        passwordEdittext.setText("legionco");*/

        usernameEt.addTextChangedListener(new GenericTextWatcher(usernameEt));
        passwordEdittext.addTextChangedListener(new GenericTextWatcher(passwordEdittext));
        userNameStatusIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isClickable()) {
                    clearText(usernameEt);
                }
            }
        });
        passowrdStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isClickable()) {
                    clearText(passwordEdittext);
                }
            }
        });
        usernameEt.setOnFocusChangeListener(this);
        passwordEdittext.setOnFocusChangeListener(this);
        findViewById(R.id.legionLayout).setVisibility(View.VISIBLE);

        String legalStatementText = getString(R.string.terms);
        SpannableString ss = new SpannableString(legalStatementText);
        String ppText = "Privacy Policy";
        String tsText = "Terms of Service";
        int ppIndex = legalStatementText.indexOf(ppText);
        int tsIndex = legalStatementText.indexOf(tsText);
        ClickableSpan ppClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(LoginActivity.this, PrivacyPolicyActivity.class));
            }
        };
        ClickableSpan tsClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(LoginActivity.this, LegionTermsActivity.class));
            }
        };
        ss.setSpan(ppClick, ppIndex, ppIndex + ppText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(tsClick, tsIndex, tsIndex + tsText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (LegionUtils.isFeatureEnabled(LoginActivity.this, "feature.legalScreens", "")) {
            legalStatementTV.setMovementMethod(LinkMovementMethod.getInstance());
        }
        legalStatementTV.setText(ss, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v.getId() == R.id.usernameET) {
                usernameErrorText.setVisibility(View.INVISIBLE);
                userNameView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrey));
                if (usernameEt.getText().toString().length() > 0) {
                    userNameStatusIv.setImageResource(R.drawable.ic_clear);
                    userNameStatusIv.setClickable(true);
                    passowrdStatusButton.setVisibility(View.INVISIBLE);
                    userNameStatusIv.setVisibility(View.VISIBLE);
                    if (checkPwdStatus) {
                        passowrdStatusButton.setVisibility(View.VISIBLE);
                    }
                } else if (passowrdStatusButton.getVisibility() == View.VISIBLE) {
                    passowrdStatusButton.setVisibility(View.INVISIBLE);
                }
            } else if (v.getId() == R.id.passwordEditText) {
                passwordErrorText.setVisibility(View.INVISIBLE);
                passowrdView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrey));
                if (passwordEdittext.getText().toString().length() > 0) {
                    passowrdStatusButton.setClickable(true);
                    passowrdStatusButton.setImageResource(R.drawable.ic_clear);
                    passowrdStatusButton.setVisibility(View.VISIBLE);
                    userNameStatusIv.setVisibility(View.INVISIBLE);
                    if (checkUserNameStatus) {
                        userNameStatusIv.setVisibility(View.VISIBLE);
                    }
                } else if (userNameStatusIv.getVisibility() == View.VISIBLE) {
                    userNameStatusIv.setVisibility(View.INVISIBLE);
                }
            }
        }

    }

    @Override
    public void onStartRequest(int requestCode) {
        if (requestCode == WebServiceRequestCodes.LOGIN) {

        }
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if (requestCode == WebServiceRequestCodes.LOGIN) {

            //LegionUtils.hideProgressDialog();
            //{"responseStatus":"SUCCESS","operationStatus":null,"errorString":null,
            // "enterprise":{"externalId":null,"name":"legion","displayName":"Legion",
            // "enterpriseId":"ebacf6df-ce56-497f-b6e7-eaee94ce3ef8","logoFileName":null,"logoUrl":null},
            // "user":{"sourceSystem":"legion_user","userName":"adalbert@legion.co","firstName":"Adalbert","lastName":"Wysocki","sessionId":null,"email":"adalbert@legion.co","phone":null,"groups":null,"workerId":"7aa0bf56-6879-4e27-a13c-9410753e9434","langKey":null,"activationKey":null,"smsActivationKey":null,"secureCode":null,"resetKey":null,"activated":true,"isAdmin":true,"isDebugMode":false,"isFeatureLimitedRolledOut":false}}
            Log.v("response", "response " + response.toString());
            Log.v("response", "response " + headers);

            try {
                passowrdStatusButton.setVisibility(View.VISIBLE);
                userNameStatusIv.setVisibility(View.VISIBLE);

                checkUserNameStatus = false;
                checkPwdStatus = false;
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    legionPreferences.save(Prefs_Keys.SESSION_ID, headers.toString());
                    legionPreferences.save(Prefs_Keys.USER_NAME, object.getJSONObject("enterprise").getString("displayName"));
                    legionPreferences.save(Prefs_Keys.WORKER_ID, object.getJSONObject("user").getString("workerId"));
                    legionPreferences.save(Prefs_Keys.ENTERPRISE_ID, object.getJSONObject("enterprise").getString("enterpriseId"));
                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/user_" + legionPreferences.get(Prefs_Keys.WORKER_ID));
                    Log.d("subscribe", "/topics/user_" + legionPreferences.get(Prefs_Keys.WORKER_ID));
                    getAccountDetailsService();
                } else {
                    LegionUtils.hideProgressDialog();
                    passowrdView.setBackgroundColor(Color.parseColor("#DE1919"));
                    userNameStatusIv.setImageResource(R.drawable.ic_error);
                    checkUserNameStatus = true;
                    checkPwdStatus = true;
                    userNameView.setBackgroundColor(Color.parseColor("#DE1919"));
                    passowrdStatusButton.setImageResource(R.drawable.ic_error);
                    userNameStatusIv.setClickable(true);
                    passowrdStatusButton.setClickable(true);
                    passwordErrorText.setText("Username and password don't match");
                    loginButton.setText("Login");
                    passwordErrorText.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();
                loginButton.setText("Login");
                LegionUtils.doLogout(this);
            }
        } else if (requestCode == WebServiceRequestCodes.GET_WORKER_ACCOUNT_DETAILS_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                String businessKey = object.getJSONArray("employments").getJSONObject(0).getString("businessKey");
                legionPreferences.save(Prefs_Keys.BUSSINESS_KEY, businessKey);
                String firstName = object.getString("firstName");
                String lastName = object.getString("lastName");
                String email = object.getString("email");
                String phoneNumber = object.getString("phone");
                legionPreferences.save(Prefs_Keys.PROFILE_PIC_URL, (object.isNull("picurl") ? "" : object.getString("picurl")));
                getQueryAvailiablity();
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();
                loginButton.setText("Login");
            }
        } else if (requestCode == WebServiceRequestCodes.QUERY_AVAILABILITY_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    getBusinessDetailsService(legionPreferences.get(Prefs_Keys.BUSSINESS_KEY));
                }
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();
                loginButton.setText("Login");
            }
        } else if (requestCode == WebServiceRequestCodes.GET_BUSINESS_DETAILS_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                String address = object.optString("address");
                JSONArray photosArray = object.getJSONArray("photoUrl");
                String photoUrl = photosArray.get(0).toString();
                legionPreferences.save(Prefs_Keys.BUSINESS_ID, object.optString("businessId"));
                legionPreferences.save(Prefs_Keys.BUSINESS_TIMEZONE, object.optString("timeZone"));
                legionPreferences.save(Prefs_Keys.WELCOME_SCREEN_PIC_URL, photoUrl);
                legionPreferences.save(Prefs_Keys.WELCOME_SCREEN_ADDRESS, address);
                legionPreferences.save(Prefs_Keys.PHONE_NUMBER, object.optString("phoneNumber"));
                legionPreferences.save(Prefs_Keys.BUSINESS_PHONE_NUMBER, object.optString("phoneNumber"));
                legionPreferences.save(Prefs_Keys.BUSINESS_FIRST_DAY_OF_WEEK, String.valueOf(object.optInt("firstDayOfWeek")));
                businessObject = object;
                getQueryEnterpriseDetails(object.getString("enterpriseId"));
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();
                loginButton.setText("Login");
            }
        } else if (requestCode == WebServiceRequestCodes.GET_ENTERPRISE_DETAILS_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    String logoUrl = object.getJSONObject("enterprise").getString("logoUrl");
                    String displayName = object.getJSONObject("enterprise").getString("displayName");
                    legionPreferences.save(Prefs_Keys.DISPALY_NAME, displayName);
                    Log.d("Photo", logoUrl);
                    legionPreferences.save(Prefs_Keys.WELCOME_SCREEN_LOGO_URL, logoUrl);
                    getOnBoardingCompletedTimestamp(legionPreferences.get(Prefs_Keys.WORKER_ID));
                }
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();
                loginButton.setText("Login");
            }
        } else if (requestCode == 2) {
            try {
                profileObject = new JSONObject(response.toString());
                //TODO: uncomment below line before going live
                onboardingCompletedTimestamp = profileObject.getString("onboardingCompletedTimestamp");
                //onboardingCompletedTimestamp = "0";
                String firstName = profileObject.getString("firstName");
                String lastName = profileObject.getString("lastName");
                String email = profileObject.getString("email");
                String phoneNumber = profileObject.getString("phoneNumber");
                String nickName = profileObject.getString("nickName");

                legionPreferences.save(Prefs_Keys.FIRST_NAME, firstName);
                if (!nickName.equalsIgnoreCase("null") && nickName != null) {
                    legionPreferences.save(Prefs_Keys.NICK_NAME, nickName);
                } else {
                    legionPreferences.save(Prefs_Keys.NICK_NAME, "");
                }
                legionPreferences.save(Prefs_Keys.LAST_NAME, lastName);
                legionPreferences.save(Prefs_Keys.EMAIL, email);
                legionPreferences.save(Prefs_Keys.PHONE_NUMBER, phoneNumber);

                getScheduledPreferencesForSpinner(legionPreferences.get(Prefs_Keys.WORKER_ID));
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();
                loginButton.setText("Login");
            }
        } else if (requestCode == WebServiceRequestCodes.SCHEDULED_PREFRERNCES) {
            try {
                LegionUtils.hideProgressDialog();
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    if (onboardingCompletedTimestamp != null && !onboardingCompletedTimestamp.equals("0")) {
                        legionPreferences.save(Prefs_Keys.IS_LOGGED_IN, "1");
                        legionPreferences.save(Prefs_Keys_Offline.ONBOARDING, onboardingCompletedTimestamp);
                        legionPreferences.save(Prefs_Keys_Offline.SCHEDULE_PREFREENCES, object.toString());

                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra(Extras_Keys.SCHEDULED_PREFRENCES, object.toString());
                        startActivity(i);
                        finish();
                    } else {
                        legionPreferences.save(Prefs_Keys_Offline.PROFILE_OBJECT, profileObject.toString());
                        legionPreferences.save(Prefs_Keys_Offline.SCHEDULE_PREFREENCES, object.toString());
                        legionPreferences.save(Prefs_Keys_Offline.ONBOARDING, onboardingCompletedTimestamp);

                        Intent i = new Intent(getApplicationContext(), WelcomeScreenActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra(Extras_Keys.PROFILE_OBJECT, profileObject.toString());
                        i.putExtra(Extras_Keys.SCHEDULED_PREFRENCES, object.toString());
                        startActivity(i);
                        finish();
                    }
                }
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();
                loginButton.setText("Login");
            }
        }
    }

    private void getBusinessDetailsService(String businessKey) {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_BUSINESS_DETAILS_CODE, ServiceUrls.GET_BUSINESS_DETAILS_URL + businessKey, reqObject, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getScheduledPreferencesForSpinner(String workerId) {

        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(WebServiceRequestCodes.SCHEDULED_PREFRERNCES, ServiceUrls.SCHEDULE_PREFERENCES + workerId + "&businessId=" + prefsManager.get(Prefs_Keys.BUSINESS_ID), reqObject, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getQueryAvailiablity() {
        try {
            String year = "" + Calendar.getInstance().get(Calendar.YEAR);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String formattedDate = df.format(c.getTime());
            int days = Integer.parseInt(LegionUtils.getCountOfDays(("01/01/" + year), formattedDate));

            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            reqObject.put("year", year);
            reqObject.put("workerId", legionPreferences.get(Prefs_Keys.WORKER_ID));
            reqObject.put("weekStartDayOfTheYear", days);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.QUERY_AVAILABILITY_CODE, ServiceUrls.QUERY_AVAILALIABLITY_URL, reqObject, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOnBoardingCompletedTimestamp(String workerId) {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(2, ServiceUrls.ONBOARDING_COMPLTED_TIMESTAMP + workerId, reqObject, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getQueryEnterpriseDetails(String enterpriseId) {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_ENTERPRISE_DETAILS_CODE, ServiceUrls.GET_ENTERPRISE_DETAILS_URL + enterpriseId, reqObject, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAccountDetailsService() {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_WORKER_ACCOUNT_DETAILS_CODE, ServiceUrls.WORKER_ACCOUNT_DETAILS_URL, reqObject, legionPreferences.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        checkUserNameStatus = false;
        checkPwdStatus = false;
        LegionUtils.hideProgressDialog();
        loginButton.setText("Login");
        userNameStatusIv.setVisibility(View.INVISIBLE);
        passowrdStatusButton.setVisibility(View.INVISIBLE);
        if (reasonPhrase == null) {
            return;
        }
        if (reasonPhrase.contains("Something went wrong")) {
            LegionUtils.doLogout(this);
            return;
        }
        if (requestCode == WebServiceRequestCodes.LOGIN) {
            LegionUtils.showDialog(LoginActivity.this, reasonPhrase, true);
        } else if (requestCode == 2) {
            LegionUtils.showDialog(LoginActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.SCHEDULED_PREFRERNCES) {
            LegionUtils.showDialog(LoginActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.QUERY_AVAILABILITY_PREFRENCE_CODE) {
            LegionUtils.showDialog(LoginActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.QUERY_AVAILABILITY_CODE) {
            LegionUtils.showDialog(LoginActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.GET_BUSINESS_DETAILS_CODE) {
            LegionUtils.showDialog(LoginActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.GET_WORKER_ACCOUNT_DETAILS_CODE) {
            LegionUtils.showDialog(LoginActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.GET_ENTERPRISE_DETAILS_CODE) {
            LegionUtils.showDialog(LoginActivity.this, reasonPhrase, true);
        }
    }

    public void clearText(EditText et) {
        et.setText("");
        et.requestFocus();
        try {
            InputMethodManager input = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            input.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.usernameET:
                    userNameView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorGrey));
                    usernameErrorText.setVisibility(View.INVISIBLE);

                    passowrdStatusButton.setVisibility(View.INVISIBLE);
                    if (checkPwdStatus) {
                        passowrdStatusButton.setVisibility(View.VISIBLE);
                    }
                    userNameStatusIv.setClickable(true);
                    checkUserNameStatus = false;
                    if ((editable != null && (text.trim().length() > 0))) {
                        userNameStatusIv.setImageResource(R.drawable.ic_clear);
                        userNameStatusIv.setVisibility(View.VISIBLE);
                    } else { //not include text
                        userNameStatusIv.setVisibility(View.INVISIBLE);
                        // usernameEt.setText("");
                    }
                    break;
                case R.id.passwordEditText:
                    passowrdView.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.colorGrey));
                    userNameStatusIv.setVisibility(View.INVISIBLE);
                    if (checkUserNameStatus) {
                        userNameStatusIv.setVisibility(View.VISIBLE);
                    }
                    passwordErrorText.setVisibility(View.INVISIBLE);
                    passowrdStatusButton.setClickable(true);
                    if (!passwordEdittext.getText().toString().equals("")) { //if edittext include text
                        passowrdStatusButton.setImageResource(R.drawable.ic_clear);
                        passowrdStatusButton.setVisibility(View.VISIBLE);
                        checkPwdStatus = false;
                        userNameStatusIv.setVisibility(View.INVISIBLE);
                        if (checkUserNameStatus) {
                            userNameStatusIv.setVisibility(View.VISIBLE);
                        }
                        // textView.setText(editText.getText().toString());
                    } else { //not include text
                        passowrdStatusButton.setVisibility(View.INVISIBLE);
                        // textView.setText("Edittext cleared!");
                    }
                    break;
            }
        }
    }

    private void doSendEmail_ForgotPassword() {
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
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@legion.co"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Forgot my password");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "I forgot my password. Help me reset it. Here is my information: \n" + "Name: \n" + "Username: \n" + "Email:\n");
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
}
