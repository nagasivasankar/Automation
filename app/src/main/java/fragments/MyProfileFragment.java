package fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import adapters.StatesSpinnerAdapter;
import base.Legion_BaseFragment;
import co.legion.client.R;
import de.hdodenhof.circleimageview.CircleImageView;
import interfaces.ImagePickerListener;
import ui.ErrorHandlingMethods;
import utils.LegionUtils;
import utils.Legion_Constants;

/**
 * Created by Administrator on 11/16/2016.
 */

@SuppressLint("ValidFragment")
public class MyProfileFragment extends Legion_BaseFragment implements View.OnFocusChangeListener/*, ImagePickerListener*/ {
    public EditText firstName, lastName, nickName, phone, email, streetAddress, city, zipcode;
    TextView titleText, emailErrorText, phoneErrorTextProfile;
    View emailView, phoneView;
    String phoneNumber, emailId;
    Spinner statesSpinner;
    ArrayAdapter<String> spinnerAdapter;
    ImageView uploadImage;
    LinearLayout frameLayout;
    LinearLayout frameLayoutInvisible;
    CircleImageView circleImageIV;
    ErrorHandlingMethods errorHandlingMethods;
    ImageButton emailErrorImage, phoneErrorImage;
    String selectedState = "";
    String selectedStateCode = "";
    ImageButton uploadImageInvisible;
    String[] states = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado",
            "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois",
            "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
            "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana",
            "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
            "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
            "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah",
            "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming", "State"};

    private HashMap<String, String> stateNameToCodeMap = new HashMap<>();
    ImageView imageReset;
    private File selectedFile;
    private Cloudinary cloudinary;
    private String memberId;
    private boolean active;
    private String timeCreated;
    private String externalId;
    private String id;
    private String title;
    private String address1;
    private String languages;
    private String interests;
    private TextView saveActionView;
    private boolean isProfilePicDeleted;
    private String profilePicUrl = null;
    private boolean firstTime = true;

    @SuppressLint("ValidFragment")
    public MyProfileFragment(TextView saveActionView) {
        if (saveActionView != null) {
            this.saveActionView = saveActionView;
            saveActionView.setEnabled(false);
            if (getActivity() != null) {
                saveActionView.setTextColor(ActivityCompat.getColor(getActivity(), R.color.swap_text_color));
            }
        }
    }

    public MyProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Returning the layout file after inflating
        View viewGroup = inflater.inflate(R.layout.fragment_completemyprofile, null);

        stateNameToCodeMap.put("Alabama", "AL");
        stateNameToCodeMap.put("Alaska", "AK");
        stateNameToCodeMap.put("Arizona", "AZ");
        stateNameToCodeMap.put("Arkansas", "AR");
        stateNameToCodeMap.put("California", "CA");
        stateNameToCodeMap.put("Colorado", "CO");
        stateNameToCodeMap.put("Connecticut", "CT");
        stateNameToCodeMap.put("Delaware", "DE");
        stateNameToCodeMap.put("Florida", "FL");
        stateNameToCodeMap.put("Georgia", "GA");
        stateNameToCodeMap.put("Hawaii", "HI");
        stateNameToCodeMap.put("Idaho", "ID");
        stateNameToCodeMap.put("Illinois", "IL");
        stateNameToCodeMap.put("Indiana", "IN");
        stateNameToCodeMap.put("Iowa", "IA");
        stateNameToCodeMap.put("Kansas", "KS");
        stateNameToCodeMap.put("Kentucky", "KY");
        stateNameToCodeMap.put("Louisiana", "LA");
        stateNameToCodeMap.put("Maine", "ME");
        stateNameToCodeMap.put("Maryland", "MD");
        stateNameToCodeMap.put("Massachusetts", "MA");
        stateNameToCodeMap.put("Michigan", "MI");
        stateNameToCodeMap.put("Minnesota", "MN");
        stateNameToCodeMap.put("Mississippi", "MS");
        stateNameToCodeMap.put("Missouri", "MO");
        stateNameToCodeMap.put("Montana", "MT");
        stateNameToCodeMap.put("Nebraska", "NA");
        stateNameToCodeMap.put("Nevada", "NV");
        stateNameToCodeMap.put("New Hampshire", "NH");
        stateNameToCodeMap.put("New Jersey", "NJ");
        stateNameToCodeMap.put("New Mexico", "NM");
        stateNameToCodeMap.put("New York", "NY");
        stateNameToCodeMap.put("North Carolina", "NC");
        stateNameToCodeMap.put("North Dakota", "ND");
        stateNameToCodeMap.put("Ohio", "OH");
        stateNameToCodeMap.put("Oklahoma", "OK");
        stateNameToCodeMap.put("Oregon", "OR");
        stateNameToCodeMap.put("Pennsylvania", "PA");
        stateNameToCodeMap.put("Rhode Island", "RI");
        stateNameToCodeMap.put("South Carolina", "SC");
        stateNameToCodeMap.put("South Dakota", "SD");
        stateNameToCodeMap.put("Tennessee", "TN");
        stateNameToCodeMap.put("Texas", "TX");
        stateNameToCodeMap.put("Utah", "UT");
        stateNameToCodeMap.put("Vermont", "VT");
        stateNameToCodeMap.put("Virginia", "VI");
        stateNameToCodeMap.put("Washington", "WA");
        stateNameToCodeMap.put("West Virginia", "WV");
        stateNameToCodeMap.put("Wisconsin", "WI");
        stateNameToCodeMap.put("Wyoming", "WY");

        cloudinary = new Cloudinary("cloudinary://716358787461634:ebF6_v0SdT_xZ4RLDEXuVcDCFFA@legiontech");
        LegionUtils.doApplyFont(getActivity().getAssets(), (ScrollView) viewGroup.findViewById(R.id.parentLayout));
        statesSpinner = (Spinner) viewGroup.findViewById(R.id.stateSpinner);
        titleText = (TextView) viewGroup.findViewById(R.id.titleText);
        firstName = (EditText) viewGroup.findViewById(R.id.firstName);
        lastName = (EditText) viewGroup.findViewById(R.id.lastName);
        nickName = (EditText) viewGroup.findViewById(R.id.nickName);
        phone = (EditText) viewGroup.findViewById(R.id.phoneEdittext);
        email = (EditText) viewGroup.findViewById(R.id.emailEdittext);
        streetAddress = (EditText) viewGroup.findViewById(R.id.streetEdittext);
        city = (EditText) viewGroup.findViewById(R.id.cityEdittext);
        zipcode = (EditText) viewGroup.findViewById(R.id.zipcode);
        emailView = viewGroup.findViewById(R.id.emailView);
        phoneView = viewGroup.findViewById(R.id.phoneView);
        emailErrorImage = (ImageButton) viewGroup.findViewById(R.id.emailErrorImage);
        phoneErrorImage = (ImageButton) viewGroup.findViewById(R.id.phoneErrorImage);
        circleImageIV = (CircleImageView) viewGroup.findViewById(R.id.circleImageIV);
        uploadImage = (ImageView) viewGroup.findViewById(R.id.uploadImage);
        phoneErrorTextProfile = (TextView) viewGroup.findViewById(R.id.phoneErrorTextProfile);
        emailErrorText = (TextView) viewGroup.findViewById(R.id.emailErrorTextProfile);
        ((LinearLayout) viewGroup.findViewById(R.id.imageLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LegionUtils.hideKeyboard(getActivity());
            }
        });
        frameLayout = (LinearLayout) viewGroup.findViewById(R.id.frameLayout);
        errorHandlingMethods = new ErrorHandlingMethods();
        uploadImageInvisible = (ImageButton) viewGroup.findViewById(R.id.uploadImageInvisible);
        imageReset = (ImageView) viewGroup.findViewById(R.id.imageReset);
        imageReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveActionView != null) {
                    saveActionView.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                    saveActionView.setEnabled(true);
                }
                uploadImage.setVisibility(View.VISIBLE);
                circleImageIV.setImageResource(R.drawable.ic_place_holder_profile);
                imageReset.setVisibility(View.INVISIBLE);
                uploadImageInvisible.setVisibility(View.INVISIBLE);
                isProfilePicDeleted = true;
            }
        });
        uploadImageInvisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraGalleryFragment frgament = CameraGalleryFragment.newInstance(MyProfileFragment.this, false, 0);
                frgament.show(getActivity().getSupportFragmentManager(), "camera");
            }
        });
        StatesSpinnerAdapter spinnerAdapter = new StatesSpinnerAdapter(getActivity(), states);
        statesSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (statesSpinner.getCount() == statesSpinner.getSelectedItemPosition()) {
                        statesSpinner.setSelection(0);
                    }
                }
                return false;
            }
        });
        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (firstTime) {
                    firstTime = false;
                    return;
                }
                selectedState = states[Integer.parseInt(parent.getItemAtPosition(position).toString())];
                selectedStateCode = stateNameToCodeMap.get(selectedState);

                if (saveActionView != null) {
                    saveActionView.setEnabled(true);
                    saveActionView.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        statesSpinner.setAdapter(spinnerAdapter);
        statesSpinner.setSelection(statesSpinner.getCount());

        city.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    LegionUtils.hideKeyboard(getActivity());
                    statesSpinner.performClick();
                    return true;
                }
                return false;
            }
        });
        String profileObject = getArguments().getString(Extras_Keys.PROFILE_OBJECT);
        legionPreferences.save(Legion_Constants.Prefs_Keys_Offline.PROFILE_OBJECT, profileObject);
        try {
            JSONObject profileJsonObject = new JSONObject(profileObject);
            firstName.setText(profileJsonObject.optString("firstName"));
            lastName.setText(profileJsonObject.optString("lastName"));
            firstName.setSelection(firstName.getText().toString().trim().length());
            lastName.setSelection(lastName.getText().toString().trim().length());
            if (!profileJsonObject.isNull("nickName")) {
                nickName.setText(profileJsonObject.optString("nickName").trim());
            } else {
                nickName.setText("");
            }
            nickName.setSelection(nickName.getText().toString().trim().length());
            memberId = profileJsonObject.optString("objectId");
            id = profileJsonObject.optString("id");
            active = profileJsonObject.optBoolean("active");
            timeCreated = profileJsonObject.optString("timeCreated");
            externalId = profileJsonObject.optString("externalId");
            title = profileJsonObject.optString("title");
            address1 = profileJsonObject.optString("address1");
            languages = profileJsonObject.optString("languages");
            interests = profileJsonObject.optString("interests");
            email.setText(profileJsonObject.optString("email"));
            email.setSelection(email.getText().toString().trim().length());
            streetAddress.setText(address1.equalsIgnoreCase("null") ? "" : address1);
            streetAddress.setSelection(streetAddress.getText().toString().trim().length());
            city.setText(profileJsonObject.optString("city").equalsIgnoreCase("null") ? "" : profileJsonObject.optString("city"));
            city.setSelection(city.getText().toString().trim().length());
            zipcode.setText(profileJsonObject.optString("zip").equalsIgnoreCase("null") ? "" : profileJsonObject.optString("zip"));
            zipcode.setSelection(zipcode.getText().toString().trim().length());

            //profileJsonObject.put("state", JSONObject.NULL);
            try {
                if (!profileJsonObject.isNull("state") && !profileJsonObject.optString("state").isEmpty()) {
                    String stateCode = profileJsonObject.optString("state");
                    selectedStateCode = stateCode;

                    int pos = getStatePosition(getKeyFromValue(stateNameToCodeMap, stateCode));
                    if (pos == -1) {
                        throw new Exception();
                    }
                    statesSpinner.setSelection(pos);
                } else {
                    //Toast.makeText(getActivity(), "Did not select any : "+profileJsonObject.optString("state"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //e.printStackTrace();
                try {
                    //int statePos = Integer.parseInt(profileJsonObject.getString("state"));
                    //statesSpinner.setSelection(statePos);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    // statesSpinner.setSelection(getStatePosition(profileJsonObject.getString("state")));
                }
            }
            emailId = profileJsonObject.optString("email");
            phoneNumber = profileJsonObject.optString("phoneNumber");
            legionPreferences.save(Legion_Constants.Prefs_Keys.PROFILE_PIC_URL, (profileJsonObject.isNull("pictureUrl") ? "" : profileJsonObject.getString("pictureUrl")));
            doUpdateTopSection(profileJsonObject, true);
            phone.addTextChangedListener(new PhoneNumberFormatter(phone));
            phone.setText(profileJsonObject.optString("phoneNumber").trim().replace(" ", "").replace(".", ""));
            int pSelection = phone.getText().toString().trim().length();
            if(pSelection > 12){
                pSelection = 12;
            }
            phone.setSelection(pSelection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        phone.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        streetAddress.setOnFocusChangeListener(this);
        streetAddress.addTextChangedListener(new GenericTextWatcher(streetAddress));
        firstName.addTextChangedListener(new GenericTextWatcher(firstName));
        lastName.addTextChangedListener(new GenericTextWatcher(lastName));
        nickName.addTextChangedListener(new GenericTextWatcher(nickName));
        zipcode.addTextChangedListener(new GenericTextWatcher(zipcode));

        city.addTextChangedListener(new GenericTextWatcher(city));
        email.addTextChangedListener(new GenericTextWatcher(email));
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraGalleryFragment frgament = CameraGalleryFragment.newInstance(MyProfileFragment.this, false, 0);
                frgament.show(getActivity().getSupportFragmentManager(), "camera");
            }
        });
        if (saveActionView != null) {
            saveActionView.setEnabled(false);
            saveActionView.setTextColor(ActivityCompat.getColor(getActivity(), R.color.swap_text_color));
        }
        return viewGroup;
    }

    public static String getKeyFromValue(HashMap<String, String> hm, String value) {
        for (String key : hm.keySet()) {
            if (hm.get(key).equalsIgnoreCase(value)) {
                return key;
            }
        }
        return null;
    }

    private int getStatePosition(String state) {
        for (int i = 0; i < states.length; ++i) {
            if (states[i].equalsIgnoreCase(state)) {
                return i;
            }
        }
        return -1;
    }

    public JSONObject getInputtedData() {
        try {

            JSONObject jsonObjectProfile = new JSONObject();
            JSONArray jsonArrayProfile = new JSONArray();
            JSONObject reqObj = new JSONObject();

            reqObj.put("title", title.trim());
            reqObj.put("city", getValidString(city.getText().toString()));
            reqObj.put("address", getValidString(streetAddress.getText().toString()));
            reqObj.put("lastName", getValidString(lastName.getText().toString()));
            reqObj.put("objectId", memberId);
            reqObj.put("memberId", memberId);
            reqObj.put("email", email.getText().toString().trim());
            if (profilePicUrl != null) {
                reqObj.put("pictureUrl", profilePicUrl);
                legionPreferences.save(Legion_Constants.Prefs_Keys.PROFILE_PIC_URL, profilePicUrl);
            } else {
                reqObj.put("pictureUrl", legionPreferences.get(Legion_Constants.Prefs_Keys.PROFILE_PIC_URL).equals("") ? JSONObject.NULL : legionPreferences.get(Legion_Constants.Prefs_Keys.PROFILE_PIC_URL));
            }
            if (isProfilePicDeleted) {
                reqObj.put("pictureUrl", JSONObject.NULL);
                isProfilePicDeleted = false;
                legionPreferences.save(Legion_Constants.Prefs_Keys.PROFILE_PIC_URL, "");
            }
            reqObj.put("nickName", getValidString(nickName.getText().toString()));
            reqObj.put("id", id);
            reqObj.put("zip", getValidString(zipcode.getText().toString()));
            reqObj.put("phoneNumber", phone.getText().toString().trim().replace("-", "").replace(".", ""));
            reqObj.put("firstName", getValidString(firstName.getText().toString()));
            reqObj.put("timeCreated", timeCreated);
            reqObj.put("state", (selectedStateCode == null || selectedStateCode.equals("") ? JSONObject.NULL : selectedStateCode));
            reqObj.put("active", active);
            JSONArray jsonArray = new JSONArray();
            reqObj.put("associatedExternalIds", jsonArray);
            reqObj.put("externalId", getValidString(externalId));
            reqObj.put("interests", getValidString(interests));
            reqObj.put("languages", getValidString(languages));
            reqObj.put("address1", getValidString(streetAddress.getText().toString()));
            reqObj.put("timeUpdated", Calendar.getInstance().getTimeInMillis());

            jsonArrayProfile.put(0, reqObj);
            jsonObjectProfile.put("worker", jsonArrayProfile);

            doUpdateTopSection(reqObj, false);

            return jsonObjectProfile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void doUpdateTopSection(JSONObject reqObj, boolean loadProfilePic) {
        try {
            if (!reqObj.isNull("pictureUrl") && !reqObj.optString("pictureUrl").equals("") && !reqObj.optString("pictureUrl").equals("null")) {
                uploadImageInvisible.setVisibility(View.VISIBLE);
                imageReset.setVisibility(View.VISIBLE);
                uploadImage.setVisibility(View.GONE);
                if (legionPreferences.hasKey(Legion_Constants.Prefs_Keys.NICK_NAME) && legionPreferences.get(Legion_Constants.Prefs_Keys.NICK_NAME).trim().length() > 0) {
                    titleText.setText("What's new, " + legionPreferences.get(Legion_Constants.Prefs_Keys.NICK_NAME) + "?");
                } else {
                    titleText.setText("What's new, " + reqObj.optString("firstName") + "?");
                }
                if (loadProfilePic) {
                    Picasso.with(getActivity()).load(reqObj.optString("pictureUrl")).error(R.drawable.ic_place_holder_profile).placeholder(R.drawable.ic_place_holder_profile).into(circleImageIV);
                }
            } else {
                uploadImageInvisible.setVisibility(View.INVISIBLE);
                imageReset.setVisibility(View.INVISIBLE);
                titleText.setText(R.string.personalizeText);
            }

            if (nickName.getText().toString().trim().length() > 0) {
                titleText.setText("What's new, " + nickName.getText().toString() + "?");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* @Override*/
    public void onImagePick(String imagePath, Bitmap bmp, DialogInterface dialog, int position) {
        dialog.dismiss();
        if (imagePath != null) {
            if (saveActionView != null) {
                saveActionView.setEnabled(true);
                saveActionView.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
            }
            selectedFile = new File(imagePath);
            Glide.with(this)
                    .load(new File(imagePath)) // Uri of the picture
                    .fitCenter().into(circleImageIV);
            imageReset.setVisibility(View.VISIBLE);
            uploadImage.setVisibility(View.GONE);
            uploadImageInvisible.setVisibility(View.VISIBLE);
            circleImageIV.setVisibility(View.VISIBLE);
            new PhotoUploadtoCloudinary().execute("");
        }
    }

    /*@Override
    public void onImageRemove(DialogInterface dialog) {

    }*/

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v.getId() == R.id.phoneEdittext) {

                phoneNumber = phone.getText().toString();
                phoneErrorImage.setVisibility(View.GONE);
                phoneErrorTextProfile.setVisibility(View.INVISIBLE);
                phoneView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.underLine));

            } else if (v.getId() == R.id.emailEdittext) {
                phoneNumber = phone.getText().toString();
                int phonelength = phoneNumber.length();

                emailErrorText.setVisibility(View.INVISIBLE);
                emailErrorImage.setVisibility(View.INVISIBLE);
                emailView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.underLine));

                if (phoneNumber.equals("") || phonelength < 10 || phonelength > 12 || phoneNumber.equals(null) || phoneNumber.isEmpty()) {
                    errorHandlingMethods.phoneError(phoneView, phoneErrorTextProfile, getActivity(), phoneErrorImage);
                } else {
                    //completeProfileData.getPhoneNumber(phoneNumber);
                    errorHandlingMethods.phoneSuccess(phoneView, phoneErrorTextProfile, getActivity(), phoneErrorImage);
                }

            } else if (v.getId() == R.id.streetEdittext) {
                emailId = email.getText().toString();
                if (email.getText().toString().equals("") ||
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    errorHandlingMethods.emailError(emailView, emailErrorText, getActivity(), emailErrorImage);
                } else {
                    //completeProfileData.getEmail(emailId);
                    errorHandlingMethods.emailSuccess(emailView, emailErrorText, getActivity(), emailErrorImage);
                }
            }
        }
    }

    public boolean doValidateProfileInputs() {
        boolean isPhoneValid = false, isEmailValid = false, isNameValid = false;

        emailErrorImage.setVisibility(View.INVISIBLE);
        phoneErrorImage.setVisibility(View.GONE);
        phoneView.setBackgroundColor(ActivityCompat.getColor(getActivity(), R.color.underLine));
        emailView.setBackgroundColor(ActivityCompat.getColor(getActivity(), R.color.underLine));

        String emailId = email.getText().toString().trim();
        String phNum = phone.getText().toString().trim();
        String name = firstName.getText().toString().trim();

        if (name.length() >= 1) {
            isNameValid = true;
        }

        if (phNum.length() == 12) {
            isPhoneValid = true;
        } else {
            phoneView.setBackgroundColor(ActivityCompat.getColor(getActivity(), R.color.errorRedColor));
            phoneErrorImage.setVisibility(View.VISIBLE);
            phoneErrorTextProfile.setVisibility(View.VISIBLE);
        }

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            isEmailValid = true;
        } else {
            emailErrorImage.setVisibility(View.VISIBLE);
            emailView.setBackgroundColor(ActivityCompat.getColor(getActivity(), R.color.errorRedColor));
            emailErrorText.setVisibility(View.VISIBLE);
        }
        return isNameValid && isEmailValid && isPhoneValid;
    }

    private class PhotoUploadtoCloudinary extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Map uploadResult = null;
            try {
                uploadResult = cloudinary.uploader().upload(selectedFile, ObjectUtils.emptyMap());
                //http://res.cloudinary.com/demo/image/facebook/w_80,h_120,c_fill/65646572251.jpg
                //http://res.cloudinary.com/demo/image/facebook/c_thumb,g_face,h_90,w_120/
                return "http://res.cloudinary.com/legiontech/image/upload/w_400,h_400,c_fill/" + uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
                // http://res.cloudinary.com/legiontech/image/upload/v1481174233/cwmiiqonb7plyo1ymcso.jpg  -- actual
                //converted --  http://res.cloudinary.com/legiontech/image/upload/w_400,h_400,c_fill/cwmiiqonb7plyo1ymcso.jpg
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LegionUtils.showProgressDialog(getActivity());
        }

        @Override
        protected void onPostExecute(String result) {
            LegionUtils.hideProgressDialog();
            if (result != null) {
                profilePicUrl = result;
                //  prefsManager.save(Legion_Constants.Prefs_Keys.PROFILE_PIC_URL, result);
                Log.d("url", result);
            }
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
            if (saveActionView != null) {
                if ((text != null) && (text.length() > 0)) {
                    saveActionView.setEnabled(true);
                    saveActionView.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                }
            }
            switch (view.getId()) {
               /* case R.id.phoneEdittext:
                    phoneErrorTextProfile.setVisibility(View.GONE);
                    phoneErrorImage.setVisibility(View.GONE);
                    completeProfileData.getPhoneNumber(text);
                    phoneView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGrey));
                    break;*/
                case R.id.emailEdittext:
                    emailView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.underLine));
                    emailErrorText.setVisibility(View.INVISIBLE);
                    emailErrorImage.setVisibility(View.GONE);
                    break;
            }
        }
    }


    private class PhoneNumberFormatter implements TextWatcher {

        private final EditText phoneEditText;

        public PhoneNumberFormatter(EditText phoneEditText) {
            this.phoneEditText = phoneEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public synchronized void afterTextChanged(final Editable text) {
            String number = text.toString().trim();
            number = number.replace("-", "").replace(" ", "");
            if (saveActionView != null) {
                if ((text != null) && (text.length() > 0)) {
                    saveActionView.setEnabled(true);
                    saveActionView.setTextColor(ActivityCompat.getColor(getActivity(), R.color.white));
                }
            }
            if (number.length() >= 1) {
                phoneErrorTextProfile.setVisibility(View.INVISIBLE);
                phoneErrorImage.setVisibility(View.GONE);
                phoneView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.underLine));
            }

            int len = number.length();
            if (len > 3 && len < 7) {
                number = number.substring(0, 3) + "-" + number.substring(3);
            } else if (len >= 6) {
                number = number.substring(0, 3) + "-" + number.substring(3, 6) + "-" + number.substring(6);
            }
            phoneEditText.removeTextChangedListener(this);
            phoneEditText.setText(number);
            phoneEditText.setSelection(number.trim().length());
            phoneEditText.addTextChangedListener(this);
        }
    }

    private Object getValidString(String input) {
        if (input != null && input.trim().length() >= 1 && !input.equalsIgnoreCase("null")) {
            return input.trim();
        }
        return JSONObject.NULL;
    }
}
