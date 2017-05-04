package co.legion.client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import base.Legion_BaseActivity;
import co.legion.client.R;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 17-Jan-17.
 */
public class CreateAccountActivity extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback {
    private TextView pagetextTV;
    private EditText usernameEt;
    private TextView errorUserNameEt;
    private EditText passwordET;
    private TextView errorPasswordTv;
    private Button createAccountBTN;
    private TextView errorPasswordValidationTv;
    private JSONObject businessObject;
    private JSONObject profileObject;
    private String onboardingCompletedTimestamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        initViews();
    }

    private void initViews() {
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        tvToolbarTile.setText("Create Account");
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        backImage.setVisibility(View.GONE);

        TextView legalStatementTV = (TextView) findViewById(R.id.legalStatementTV);
        pagetextTV = (TextView)findViewById( R.id.pagetext_TV );
        String workerName = getIntent().getStringExtra(Extras_Keys.WORKER_NAME);
        if(workerName == null)workerName = "";
        pagetextTV.setText("Hello, " + workerName +"!\nOnly one more step left.");
        usernameEt = (EditText)findViewById( R.id.usernameEt );
        errorUserNameEt = (TextView)findViewById( R.id.errorUserNameEt );
        errorPasswordValidationTv =  (TextView)findViewById( R.id.errorPasswordValidationTv );
        passwordET = (EditText)findViewById( R.id.passwordET );
        usernameEt.addTextChangedListener(new GenericTextWatcher(usernameEt));
        passwordET.addTextChangedListener(new GenericTextWatcher(passwordET));
        errorPasswordTv = (TextView)findViewById( R.id.errorPassword_tv );
        createAccountBTN = (Button)findViewById( R.id.createAccountBTN );
        createAccountBTN.setOnClickListener( this );
        LegionUtils.doApplyFont(getAssets(),(LinearLayout)findViewById(R.id.parentLayout));

        String legalStatementText = getString(R.string.terms_of_service);
        SpannableString ss = new SpannableString(legalStatementText);
        String ppText = "Privacy Policy";
        String tsText = "Terms of Service";
        int ppIndex = legalStatementText.indexOf(ppText);
        int tsIndex = legalStatementText.indexOf(tsText);
        ClickableSpan ppClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(CreateAccountActivity.this, PrivacyPolicyActivity.class));
            }
        };
        ClickableSpan tsClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(CreateAccountActivity.this, LegionTermsActivity.class));
            }
        };
        ss.setSpan(ppClick, ppIndex, ppIndex + ppText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(tsClick, tsIndex, tsIndex + tsText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (LegionUtils.isFeatureEnabled(CreateAccountActivity.this, "feature.legalScreens", "")) {
            legalStatementTV.setMovementMethod(LinkMovementMethod.getInstance());
        }
        legalStatementTV.setText(ss, TextView.BufferType.SPANNABLE);
    }

    public boolean doValidations() {
        boolean verification = true;
        String userName = usernameEt.getText().toString().trim();
        if (userName.length() == 0) {
            errorUserNameEt.setText("Please enter a username");
            errorUserNameEt.setVisibility(View.VISIBLE);
            verification = false;
        } else if (userName.length() < 6) {
            errorUserNameEt.setText("Username too short. Min 6 characters.");
            errorUserNameEt.setVisibility(View.VISIBLE);
            verification = false;
        } else if (userName.length() > 64) {
            errorUserNameEt.setText("Please enter less than 64 characters");
            errorUserNameEt.setVisibility(View.VISIBLE);
            verification = false;
        } else if (userName.startsWith(".") || userName.endsWith(".") || userName.startsWith("-") || userName.endsWith("-") || userName.startsWith("_") || userName.endsWith("_")) {
            errorUserNameEt.setText("Username can't start or end with periods hyphens or underscores");
            errorUserNameEt.setVisibility(View.VISIBLE);
            verification = false;
        } else if (userName.contains("..") || userName.contains("--") || userName.contains("__")) {
            errorUserNameEt.setText("Username can't contain more than one period, hyphen or underscore in a row");
            errorUserNameEt.setVisibility(View.VISIBLE);
            verification = false;
        }

        if ((passwordET.getText().toString().trim().length() == 0)) {
            errorPasswordValidationTv.setVisibility(View.VISIBLE);
            errorPasswordValidationTv.setText("Please enter a valid password");
            verification = false;
        } else if ((passwordET.getText().toString().trim().length() < 6)) {
            errorPasswordTv.setVisibility(View.VISIBLE);
            errorPasswordTv.setTextColor(ActivityCompat.getColor(this, android.R.color.holo_red_dark));
            verification = false;
        }

        return verification;
    }

    public boolean containsSpecialCharacter(String s) {
        return (s == null) ? false : s.matches("[^A-Za-z0-9]");
    }

    private String getInvalidCharIfAny(String name) {
        ArrayList<String> allowedChars = new ArrayList<>();
        allowedChars.add(".");
        allowedChars.add("-");
        allowedChars.add("_");
        allowedChars.add("'");

        for (int i = 0; i < name.length(); ++i) {
            String ch = String.valueOf(name.charAt(i));
            if (!containsSpecialCharacter(ch)) {
                continue;
            }
            if (!allowedChars.contains(ch)) {
                return ch;
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if (v == createAccountBTN) {
            if (doValidations()) {
                doCreateAccountRequest();
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void doCreateAccountRequest() {
        try {
            if (!LegionUtils.isOnline(this)) {
                LegionUtils.showOfflineDialog(this);
                return;
            }
            errorUserNameEt.setVisibility(View.INVISIBLE);
            Log.v("sessionId", prefsManager.get(Prefs_Keys.SESSION_ID));
            LegionUtils.hideKeyboard(this);
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            JSONObject object = new JSONObject();
            object.put("userName", usernameEt.getText().toString());
            object.put("passwordPlainText", passwordET.getText().toString());
            JSONObject obj = new JSONObject();
            obj.put("password", object);
            LegionUtils.showProgressDialog(this);
            restClient.performHttpPostRequest(this, WebServiceRequestCodes.CREATE_ACCOUNT_CODE, ServiceUrls.SETUP_CREDENTIALS_URL, obj, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void getOnBoardingCompletedTimestamp(String workerId) {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(2, ServiceUrls.ONBOARDING_COMPLTED_TIMESTAMP + workerId, reqObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    void doLogin(String email, String password) {
        LegionUtils.hideKeyboard(this);
        try {
            prefsManager.save(Prefs_Keys.IS_LOGGED_IN, "0");
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


    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        if(requestCode == WebServiceRequestCodes.CREATE_ACCOUNT_CODE) {
            try {
                JSONObject responseObject = new JSONObject(response.toString());
                String responseStatus = responseObject.optString("responseStatus");
                String errorString = responseObject.optString("errorString");
                LegionUtils.hideProgressDialog();
                if (responseStatus.equalsIgnoreCase("success")) {
                    doLogin(usernameEt.getText().toString(), passwordET.getText().toString());
                } else if(responseStatus.equalsIgnoreCase("failed")){
                    if(errorString != null){
                        errorUserNameEt.setText(errorString);
                        errorUserNameEt.setVisibility(View.VISIBLE);
                    }
                }else{
                    LegionUtils.showMessageDialog(this, errorString, R.drawable.error_transient);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == WebServiceRequestCodes.LOGIN) {
            //LegionUtils.hideProgressDialog();
            //{"responseStatus":"SUCCESS","operationStatus":null,"errorString":null,
            // "enterprise":{"externalId":null,"name":"legion","displayName":"Legion",
            // "enterpriseId":"ebacf6df-ce56-497f-b6e7-eaee94ce3ef8","logoFileName":null,"logoUrl":null},
            // "user":{"sourceSystem":"legion_user","userName":"adalbert@legion.co","firstName":"Adalbert","lastName":"Wysocki","sessionId":null,"email":"adalbert@legion.co","phone":null,"groups":null,"workerId":"7aa0bf56-6879-4e27-a13c-9410753e9434","langKey":null,"activationKey":null,"smsActivationKey":null,"secureCode":null,"resetKey":null,"activated":true,"isAdmin":true,"isDebugMode":false,"isFeatureLimitedRolledOut":false}}
            Log.v("response", "response " + response.toString());
            Log.v("response", "response " + headers);

            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    prefsManager.save(Prefs_Keys.SESSION_ID, headers.toString());
                    prefsManager.save(Prefs_Keys.USER_NAME, object.getJSONObject("enterprise").getString("displayName"));
                    prefsManager.save(Prefs_Keys.WORKER_ID, object.getJSONObject("user").getString("workerId"));
                    prefsManager.save(Prefs_Keys.ENTERPRISE_ID, object.getJSONObject("enterprise").getString("enterpriseId"));
                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/user_" + prefsManager.get(Prefs_Keys.WORKER_ID));
                    Log.d("subscribe", "/topics/user_" + prefsManager.get(Prefs_Keys.WORKER_ID));
                    getAccountDetailsService();
                } else {
                    LegionUtils.hideProgressDialog();
                }
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();
                LegionUtils.doLogout(this);
            }
        } else if (requestCode == WebServiceRequestCodes.GET_WORKER_ACCOUNT_DETAILS_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                String businessKey = object.getJSONArray("employments").getJSONObject(0).getString("businessKey");
                prefsManager.save(Prefs_Keys.BUSSINESS_KEY, businessKey);
                String firstName = object.getString("firstName");
                String lastName = object.getString("lastName");
                String email = object.getString("email");
                String phoneNumber = object.getString("phone");
                prefsManager.save(Prefs_Keys.PROFILE_PIC_URL, (object.isNull("picurl")?"":object.getString("picurl")));
                getQueryAvailiablity();
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();

            }
        } else if (requestCode == WebServiceRequestCodes.QUERY_AVAILABILITY_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    getBusinessDetailsService(prefsManager.get(Prefs_Keys.BUSSINESS_KEY));
                }
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();
            }
        } else if (requestCode == WebServiceRequestCodes.GET_BUSINESS_DETAILS_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                String address = object.optString("address");
                JSONArray photosArray = object.getJSONArray("photoUrl");
                String photoUrl = photosArray.get(0).toString();
                prefsManager.save(Prefs_Keys.BUSINESS_ID, object.optString("businessId"));
                prefsManager.save(Prefs_Keys.BUSINESS_TIMEZONE, object.optString("timeZone"));
                prefsManager.save(Prefs_Keys.WELCOME_SCREEN_PIC_URL, photoUrl);
                prefsManager.save(Prefs_Keys.WELCOME_SCREEN_ADDRESS, address);
                prefsManager.save(Prefs_Keys.PHONE_NUMBER, object.optString("phoneNumber"));
                prefsManager.save(Prefs_Keys.BUSINESS_PHONE_NUMBER, object.optString("phoneNumber"));
                prefsManager.save(Prefs_Keys.BUSINESS_FIRST_DAY_OF_WEEK, String.valueOf(object.optInt("firstDayOfWeek")));
                businessObject = object;
                getQueryEnterpriseDetails(object.getString("enterpriseId"));
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();

            }
        } else if (requestCode == WebServiceRequestCodes.GET_ENTERPRISE_DETAILS_CODE) {
            try {
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    String logoUrl = object.getJSONObject("enterprise").getString("logoUrl");
                    String displayName = object.getJSONObject("enterprise").getString("displayName");
                    prefsManager.save(Prefs_Keys.DISPALY_NAME, displayName);
                    Log.d("Photo", logoUrl);
                    prefsManager.save(Prefs_Keys.WELCOME_SCREEN_LOGO_URL, logoUrl);
                    getOnBoardingCompletedTimestamp(prefsManager.get(Prefs_Keys.WORKER_ID));
                }
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();

            }
        } else if (requestCode == 2) {
            try {
                profileObject = new JSONObject(response.toString());
                //TODO: uncomment below line before going live
                onboardingCompletedTimestamp = profileObject.getString("onboardingCompletedTimestamp");
                //onboardingCompletedTimestamp = "1";

                String firstName = profileObject.getString("firstName");
                String lastName = profileObject.getString("lastName");
                String email = profileObject.getString("email");
                String phoneNumber = profileObject.getString("phoneNumber");


                prefsManager.save(Prefs_Keys.FIRST_NAME, firstName);
                prefsManager.save(Prefs_Keys.LAST_NAME, lastName);
                prefsManager.save(Prefs_Keys.EMAIL, email);
                prefsManager.save(Prefs_Keys.PHONE_NUMBER, phoneNumber);

                getScheduledPreferencesForSpinner(prefsManager.get(Prefs_Keys.WORKER_ID));
            } catch (Exception e) {
                LegionUtils.hideProgressDialog();
                e.printStackTrace();

            }
        } else if (requestCode == WebServiceRequestCodes.SCHEDULED_PREFRERNCES) {
            try {
                LegionUtils.hideProgressDialog();
                JSONObject object = new JSONObject(response.toString());
                if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                    if (onboardingCompletedTimestamp != null && !onboardingCompletedTimestamp.equals("0")) {
                        prefsManager.save(Prefs_Keys.IS_LOGGED_IN, "1");
                        prefsManager.save(Prefs_Keys_Offline.ONBOARDING, onboardingCompletedTimestamp);
                        prefsManager.save(Prefs_Keys_Offline.SCHEDULE_PREFREENCES, object.toString());

                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra(Extras_Keys.SCHEDULED_PREFRENCES, object.toString());
                        startActivity(i);
                        finish();
                    } else {
                        prefsManager.save(Prefs_Keys_Offline.PROFILE_OBJECT, profileObject.toString());
                        prefsManager.save(Prefs_Keys_Offline.SCHEDULE_PREFREENCES, object.toString());
                        prefsManager.save(Prefs_Keys_Offline.ONBOARDING, onboardingCompletedTimestamp);

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
            restClient.performHTTPGetRequest(WebServiceRequestCodes.SCHEDULED_PREFRERNCES, ServiceUrls.SCHEDULE_PREFERENCES + workerId+"&businessId="+prefsManager.get(Prefs_Keys.BUSINESS_ID), reqObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
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
            String days = LegionUtils.getCountOfDays(("01/01/" + year), formattedDate);
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            reqObject.put("year", year);
            reqObject.put("workerId", prefsManager.get(Prefs_Keys.WORKER_ID));
            reqObject.put("weekStartDayOfTheYear", days);
            restClient.performHTTPGetRequest(WebServiceRequestCodes.QUERY_AVAILABILITY_CODE, ServiceUrls.QUERY_AVAILALIABLITY_URL, reqObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
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

    private void getQueryEnterpriseDetails(String enterpriseId) {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_ENTERPRISE_DETAILS_CODE, ServiceUrls.GET_ENTERPRISE_DETAILS_URL + enterpriseId, reqObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAccountDetailsService() {
        try {
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            RequestParams reqObject = new RequestParams();
            restClient.performHTTPGetRequest(WebServiceRequestCodes.GET_WORKER_ACCOUNT_DETAILS_CODE, ServiceUrls.WORKER_ACCOUNT_DETAILS_URL, reqObject, prefsManager.get(Prefs_Keys.SESSION_ID), this);
        } catch (Exception e) {
            e.printStackTrace();
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
        if (requestCode == WebServiceRequestCodes.LOGIN) {
            LegionUtils.showDialog(CreateAccountActivity.this, reasonPhrase, true);
        } else if (requestCode == 2) {
            LegionUtils.showDialog(CreateAccountActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.SCHEDULED_PREFRERNCES) {
            LegionUtils.showDialog(CreateAccountActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.QUERY_AVAILABILITY_PREFRENCE_CODE) {
            LegionUtils.showDialog(CreateAccountActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.QUERY_AVAILABILITY_CODE) {
            LegionUtils.showDialog(CreateAccountActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.GET_BUSINESS_DETAILS_CODE) {
            LegionUtils.showDialog(CreateAccountActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.GET_WORKER_ACCOUNT_DETAILS_CODE) {
            LegionUtils.showDialog(CreateAccountActivity.this, reasonPhrase, true);
        } else if (requestCode == WebServiceRequestCodes.GET_ENTERPRISE_DETAILS_CODE) {
            LegionUtils.showDialog(CreateAccountActivity.this, reasonPhrase, true);
        }
    }

    @Override
    public void onStartRequest(int requestCode) {

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

                case R.id.usernameEt:
                    errorUserNameEt.setVisibility(View.INVISIBLE);
                    break;
                case R.id.passwordET:
                    errorPasswordValidationTv.setVisibility(View.INVISIBLE);
                    errorPasswordTv.setTextColor(ActivityCompat.getColor(CreateAccountActivity.this,R.color.gray_text_color));
                    break;
            }
        }
    }
}
