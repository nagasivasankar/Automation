package co.legion.client.activities;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import base.Legion_BaseActivity;
import co.legion.client.R;
import network.Legion_NetworkCallback;
import network.Legion_RestClient;
import utils.LegionUtils;

/**
 * Created by Administrator on 17-Jan-17.
 */
public class VerifyIdentityActivity extends Legion_BaseActivity implements View.OnClickListener, Legion_NetworkCallback {
    private static MonitoringEditText edt1;
    private View view1;
    private static MonitoringEditText edt2;
    private View view2;
    private static MonitoringEditText edt3;
    private View view3;
    private static MonitoringEditText edt4;
    private View view4;
    private static MonitoringEditText edt5;
    private View view5;
    private static MonitoringEditText edt6;
    private View view6;
    private TextView errorVerificationTv;
    private Button verifyMyIdentityBt;
    private TextView errorFirstNameTv;
    private TextView errorLastNameTv;
    private EditText firstNameEt;
    private EditText lastNameEt;
    private String verificationCode;
    private int focusedEdt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_identity);
        TextView tvToolbarTile = (TextView) findViewById(R.id.tv_title);
        tvToolbarTile.setText("Create Account");
        ImageView backImage = (ImageView) findViewById(R.id.toolbarBack);
        edt1 = (MonitoringEditText) findViewById(R.id.edt1);
        view1 = (View) findViewById(R.id.view1);
        edt2 = (MonitoringEditText) findViewById(R.id.edt2);
        view2 = (View) findViewById(R.id.view2);
        edt3 = (MonitoringEditText) findViewById(R.id.edt3);
        view3 = (View) findViewById(R.id.view3);
        edt4 = (MonitoringEditText) findViewById(R.id.edt4);
        view4 = (View) findViewById(R.id.view4);
        edt5 = (MonitoringEditText) findViewById(R.id.edt5);
        view5 = (View) findViewById(R.id.view5);
        edt6 = (MonitoringEditText) findViewById(R.id.edt6);
        view6 = (View) findViewById(R.id.view6);
        verifyMyIdentityBt = (Button) findViewById(R.id.verifyMyIdentityBt);
        verifyMyIdentityBt.setOnClickListener(this);
        backImage.setOnClickListener(this);
        errorVerificationTv = (TextView) findViewById(R.id.errorverification_tv);
        errorFirstNameTv = (TextView) findViewById(R.id.errorFirstNameTv);
        errorLastNameTv = (TextView) findViewById(R.id.errorLastNameTv);
        firstNameEt = (EditText) findViewById(R.id.firstNameEt);
        lastNameEt = (EditText) findViewById(R.id.lastNameEt);
        firstNameEt.addTextChangedListener(new GenericTextWatcher(firstNameEt));
        lastNameEt.addTextChangedListener(new GenericTextWatcher(lastNameEt));
        edt1.addTextChangedListener(new GenericTextWatcher(edt1));
        edt2.addTextChangedListener(new GenericTextWatcher(edt2));
        edt3.addTextChangedListener(new GenericTextWatcher(edt3));
        edt4.addTextChangedListener(new GenericTextWatcher(edt4));
        edt5.addTextChangedListener(new GenericTextWatcher(edt5));
        edt6.addTextChangedListener(new GenericTextWatcher(edt6));
        LegionUtils.doApplyFont(getAssets(), (LinearLayout) findViewById(R.id.parentLayout));

        /*Intent i = new Intent(VerifyIdentityActivity.this, CreateAccountActivity.class);
        i.putExtra(Extras_Keys.WORKER_NAME, "Santhosh");
        startActivity(i);*/
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.verifyMyIdentityBt) {
            if (validations()) {
                doVerificationRequest();
            }
        } else if (id == R.id.dateTv) {
        } else if (id == R.id.toolbarBack) {
            finish();
        }
    }

    public boolean validations() {
        boolean verification = true;
        verificationCode = edt1.getText().toString().trim() + edt2.getText().toString().trim() + edt3.getText().toString().trim() + edt4.getText().toString().trim() + edt5.getText().toString().trim() + edt6.getText().toString().trim();

        if (verificationCode.length() == 0) {
            verification = false;
            errorVerificationTv.setText("Enter verification code");
            errorVerificationTv.setVisibility(View.VISIBLE);
        } else if (verificationCode.length() != 6) {
            verification = false;
            errorVerificationTv.setText("Verification code is incorrect");
            errorVerificationTv.setVisibility(View.VISIBLE);
        }
        return verification;
    }

    @Override
    public void onStartRequest(int requestCode) {
        LegionUtils.showProgressDialog(this);
    }

    @Override
    public void onSuccess(int requestCode, Object response, Object headers) {
        LegionUtils.hideProgressDialog();
        if (requestCode == WebServiceRequestCodes.VERIFY_IDENTITY_REQUEST_CODE) {
            try {
                if (response == null || response.toString().trim().length() == 0) {
                    errorVerificationTv.setVisibility(View.VISIBLE);
                    errorVerificationTv.setText("Verification code is incorrect");
                    return;
                }
                errorVerificationTv.setVisibility(View.INVISIBLE);
                JSONObject responseObject = new JSONObject(response.toString());
                String responseStatus = responseObject.optString("responseStatus");
                String errorString = responseObject.optString("error");

                if (responseStatus.equals("SUCCESS")) {
                    JSONObject workerObj = responseObject.optJSONObject("worker");
                    if (workerObj != null && headers != null) {
                        prefsManager.save(Prefs_Keys.SESSION_ID, headers.toString());
                        String workerName = workerObj.getString("firstName");
                        Intent i = new Intent(VerifyIdentityActivity.this, CreateAccountActivity.class);
                        i.putExtra(Extras_Keys.WORKER_NAME, workerName);
                        startActivity(i);
                    } else {
                        LegionUtils.showMessageDialog(this, errorString, R.drawable.error_transient);
                    }
                } else {
                    errorVerificationTv.setVisibility(View.VISIBLE);
                    errorVerificationTv.setText("Verification code is incorrect");
                    //LegionUtils.showMessageDialog(this, errorString, R.drawable.error_transient);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LegionUtils.showMessageDialog(this, "Oops, Something went wrong. Please try again.", R.drawable.error_transient);
            }
        }
    }

    @Override
    public void onFailure(int requestCode, String reasonPhrase) {
        LegionUtils.hideProgressDialog();
        if (reasonPhrase != null) {
            if (requestCode == WebServiceRequestCodes.VERIFY_IDENTITY_REQUEST_CODE) {
                LegionUtils.showMessageDialog(this, reasonPhrase, R.drawable.error_transient);
            }
        }
    }

    private void doVerificationRequest() {
        try {
            if (!LegionUtils.isOnline(this)) {
                LegionUtils.showOfflineDialog(this);
                return;
            }
            LegionUtils.hideKeyboard(this);
            Legion_RestClient restClient = new Legion_RestClient(this, this);
            JSONObject params = new JSONObject();
            params.put("secret", "");
            params.put("activationKey", verificationCode);
            LegionUtils.showProgressDialog(this);
            restClient.performHttpPostRequest(this, WebServiceRequestCodes.VERIFY_IDENTITY_REQUEST_CODE, ServiceUrls.VERIFY_IDENTITY_URL, params, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (focusedEdt == 2) {
                edt1.setText("");
            } else if (focusedEdt == 3) {
                edt2.setText("");
            } else if (focusedEdt == 4) {
                edt3.setText("");
            } else if (focusedEdt == 5) {
                edt4.setText("");
            } else if (focusedEdt == 6) {
                edt5.setText("");
            }
        }
        return super.onKeyDown(keyCode, event);
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
            if (String.valueOf(view).contains("edt")) {
                errorVerificationTv.setVisibility(View.INVISIBLE);
            }
            switch (view.getId()) {
                case R.id.edt1:
                    if (text.length() == 1) {
                        edt2.setFocusableInTouchMode(true);
                        edt2.requestFocus();
                        focusedEdt = 2;
                        view1.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        focusedEdt = 1;
                        view1.setBackgroundColor(ActivityCompat.getColor(VerifyIdentityActivity.this, R.color.light_black));
                        edt1.requestFocus();
                    }
                    break;
                case R.id.edt2:
                    if (text.length() == 1) {
                        edt3.setFocusableInTouchMode(true);
                        edt3.requestFocus();
                        focusedEdt = 3;
                        view2.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        focusedEdt = 2;
                        view2.setBackgroundColor(ActivityCompat.getColor(VerifyIdentityActivity.this, R.color.light_black));
                        edt2.requestFocus();
                    }
                    break;
                case R.id.edt3:
                    if (text.length() == 1) {
                        edt4.setFocusableInTouchMode(true);
                        edt4.requestFocus();
                        focusedEdt = 4;
                        view3.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        focusedEdt = 3;
                        view3.setBackgroundColor(ActivityCompat.getColor(VerifyIdentityActivity.this, R.color.light_black));
                        edt3.requestFocus();
                    }
                    break;
                case R.id.edt4:
                    if (text.length() == 1) {
                        edt5.setFocusableInTouchMode(true);
                        edt5.requestFocus();
                        focusedEdt = 5;
                        view4.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        focusedEdt = 4;
                        view4.setBackgroundColor(ActivityCompat.getColor(VerifyIdentityActivity.this, R.color.light_black));
                        edt4.requestFocus();
                    }
                    break;
                case R.id.edt5:
                    if (text.length() == 1) {
                        edt6.setFocusableInTouchMode(true);
                        edt6.requestFocus();
                        focusedEdt = 6;
                        view5.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        focusedEdt = 5;
                        view5.setBackgroundColor(ActivityCompat.getColor(VerifyIdentityActivity.this, R.color.light_black));
                        edt5.requestFocus();
                    }
                    break;
                case R.id.edt6:
                    if (text.length() == 1) {
                        view6.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        focusedEdt = 6;
                        view6.setBackgroundColor(ActivityCompat.getColor(VerifyIdentityActivity.this, R.color.light_black));
                        edt6.requestFocus();
                    }
                    break;
                case R.id.firstNameEt:
                    errorFirstNameTv.setVisibility(View.INVISIBLE);
                    break;
                case R.id.lastNameEt:
                    errorLastNameTv.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    }

    private static class MonitoringEditText extends EditText {

        private final Context context;

        /*
            Just the constructors to create a new EditText...
         */
        public MonitoringEditText(Context context) {
            super(context);
            this.context = context;
        }

        public MonitoringEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.context = context;
        }

        public MonitoringEditText(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.context = context;
        }

        /**
         * <p>This is where the "magic" happens.</p>
         * <p>The menu used to cut/copy/paste is a normal ContextMenu, which allows us to
         * overwrite the consuming method and react on the different events.</p>
         *
         * @see <a href="http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3_r1/android/widget/TextView.java#TextView.onTextContextMenuItem%28int%29">Original Implementation</a>
         */
        @Override
        public boolean onTextContextMenuItem(int id) {
            boolean consumed = super.onTextContextMenuItem(id);
            switch (id) {
                case android.R.id.cut:
                    break;
                case android.R.id.paste:
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    if (clipboard.hasPrimaryClip()) {
                        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                        String copiedText = item.getText().toString();
                        if (copiedText.trim().length() == 0) {
                            break;
                        }
                        doPasteInBoxes(copiedText);
                    }
                    break;
                case android.R.id.copy:
                    break;
            }
            return consumed;
        }
    }

    private static void doPasteInBoxes(String copiedText) {
        try {
            char c1 = copiedText.charAt(0);
            if (Character.isDigit(c1)) {
                edt1.setText(String.valueOf(c1));
                edt1.setSelection(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            char c2 = copiedText.charAt(1);
            if (Character.isDigit(c2)) {
                edt2.setText(String.valueOf(c2));
                edt2.setSelection(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            char c3 = copiedText.charAt(2);
            if (Character.isDigit(c3)) {
                edt3.setText(String.valueOf(c3));
                edt3.setSelection(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            char c4 = copiedText.charAt(3);
            if (Character.isDigit(c4)) {
                edt4.setText(String.valueOf(c4));
                edt4.setSelection(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            char c5 = copiedText.charAt(4);
            if (Character.isDigit(c5)) {
                edt5.setText(String.valueOf(c5));
                edt5.setSelection(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            char c6 = copiedText.charAt(5);
            if (Character.isDigit(c6)) {
                edt6.setText(String.valueOf(c6));
                edt6.setSelection(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
