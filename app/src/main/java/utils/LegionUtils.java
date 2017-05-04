package utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.IntentCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import co.legion.client.BuildConfig;
import co.legion.client.R;
import co.legion.client.activities.LoginActivity;
import helpers.Legion_PrefsManager;
import helpers.ProgressHUD;
import models.AssociatedWorker;
import models.BusinessKey;
import models.ScheduleWorkerShift;
import models.WorkerKey;

/**
 * Created by Administrator on 18-Nov-16.
 */
public class LegionUtils implements Legion_Constants {

    public static final int SECOND                    = 1000; //millis
    public static final int MINUTE                    = 60 * SECOND;
    public static final int HOUR                      = 60 * MINUTE;
    public static final int DAY                       = 24 * HOUR;
    private static final long SUCCESS_DIALOG_DURATION = 2 * SECOND;
    public static String DATE_FORMAT                  = "yyyy-MM-dd HH:mm:ss.SSS";
    private static ProgressHUD progressHUD;
    public static Dialog errorDialog;
    private static Dialog offlineDialog;

    private final static SimpleDateFormat JsonDateFormatter =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    public static void doApplyFont(AssetManager assetManager, ViewGroup view) {
        try {
            int numOfChildren = view.getChildCount();
            for (int i = 0; i < numOfChildren; ++i) {
                View childView = view.getChildAt(i);
                if (childView instanceof ViewGroup) {
                    doApplyFont(assetManager, (ViewGroup) childView);
                } else {
                    applyFont(assetManager, childView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void applyFont(AssetManager assetManager, View view) {
        Object tag = view.getTag();
        String font = "fonts/Mallory Book Regular.ttf";
        if (tag != null) {
            if (tag.toString().equals(view.getContext().getString(R.string.mallory_book))) {
                font = "fonts/Mallory-Book.ttf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_book_regular))) {
                font = "fonts/Mallory Book Regular.ttf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_light))) {
                font = "fonts/Mallory Light Regular.ttf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_mediuem))) {
                font = "fonts/Mallory Medium Regular.ttf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_book_Italic))) {
                font = "fonts/Mallory-BookItalic.otf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_bold))) {
                font = "fonts/Mallory Bold Regular.ttf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_black))) {
                font = "fonts/Mallory Black Regular.ttf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_extra_light))) {
                font = "fonts/Mallory Extra Light Regular.ttf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_thin))) {
                font = "fonts/Mallory Thin Regular.ttf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_ultra))) {
                font = "fonts/Mallory Ultra Regular.ttf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.sf_ui_disply_semi_bold))) {
                font = "fonts/SF-UI-Display-Semibold.otf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.sf_ui_text_semi_bold))) {
                font = "fonts/SF-UI-Text-Semibold.otf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.proximaNovaCond_Medium))) {
                font = "fonts/Mark Simonson - Proxima Nova Cond Medium.otf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.proximaNovaCond_Regular))) {
                font = "fonts/Mark Simonson - Proxima Nova Cond.otf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.proximaNovaCond_SemiBold))) {
                font = "fonts/Mark Simonson - Proxima Nova Cond Semibold.otf";
            } else if (tag.toString().equals(view.getContext().getString(R.string.mallory_medium_regular))) {
                font = "fonts/Mallory Medium Regular.ttf";
            }
        }
        Typeface typeface = Typeface.createFromAsset(assetManager, font);
        if (view instanceof EditText) {
            ((EditText) view).setTypeface(typeface);
        } else if (view instanceof Button) {
            ((Button) view).setTypeface(typeface);
        } else if (view instanceof CheckBox) {
            ((CheckBox) view).setTypeface(typeface);
        } else if (view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }
    }

    public static void showDialog(Activity activity, String text, boolean isCancellable) {
        try {
            if (activity == null) {
                return;
            }
            if (errorDialog == null) {
                errorDialog = new Dialog(activity);
                errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                errorDialog.setCancelable(isCancellable);
                errorDialog.setCanceledOnTouchOutside(isCancellable);
                errorDialog.setContentView(R.layout.custom_dialog);
                errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                LegionUtils.doApplyFont(activity.getAssets(), (LinearLayout) errorDialog.findViewById(R.id.dialogParentLayout));
                final TextView msgTv = (TextView) errorDialog.findViewById(R.id.msgTV);
                final TextView okTV = (TextView) errorDialog.findViewById(R.id.okTV);
                msgTv.setText(text);
                okTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (errorDialog != null && errorDialog.isShowing()) {
                            errorDialog.dismiss();
                        }
                    }
                });
                errorDialog.show();
            } else {
                errorDialog.dismiss();
                errorDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // For displaying Progress Bar.
    public static void showProgressDialog(Activity activity, String message) {
        if (activity == null) {
            return;
        }
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
        progressHUD = ProgressHUD.show(activity, message, false, true, null);
    }

    public static void showProgressDialog(Activity activity) {
        if (activity == null) {
            return;
        }
        try {
            if (progressHUD != null && progressHUD.isShowing()) {
                progressHUD.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (activity != null)
                progressHUD = ProgressHUD.show(activity, "", false, true, null);
        }
    }

    public static void showProgressDialogWithCanlable(Activity activity, String title, String message) {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
        progressHUD = ProgressHUD.show(activity, message, true, true, null);
    }

    public static void hideProgressDialog() {
        try {
            if (progressHUD != null && progressHUD.isShowing()) {
                progressHUD.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isOnline(Activity activity) {
        try {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            return nInfo != null && nInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }

    public static String getWeekDate(String dateFromServer) {
        String inputPattern = DATE_FORMAT;
        String outputPattern = "MMM d";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(dateFromServer);
            date = getConvertedDate(date, "UTC");
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Date getConvertedDate(Date date, String newTimeZone) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setTimeZone(TimeZone.getTimeZone(newTimeZone));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String newdt = df.format(calendar.getTime());
            try {
                date = df.parse(newdt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static String getDayName(int position) {
        if (position == 1) {
            return "MON";
        } else if (position == 2) {
            return "TUE";
        } else if (position == 3) {
            return "WED";
        } else if (position == 4) {
            return "THU";
        } else if (position == 5) {
            return "FRI";
        } else if (position == 6) {
            return "SAT";
        } else if (position == 7) {
            return "SUN";
        }
        return "";
    }

    public static void doLogout(Activity activity) {
        if (activity != null) {
            try {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/user_" + new Legion_PrefsManager(activity).get(Prefs_Keys.WORKER_ID));
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (Exception e) {
                Log.d("Exception", "Not subscribed");
            }
            Legion_PrefsManager spManager = new Legion_PrefsManager(activity);
            spManager.clearAllPrefs();

            Intent i = new Intent(activity, LoginActivity.class);
            i.putExtra(Extras_Keys.IS_LOGGED_OUT, true);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(i);
            activity.finish();
        }
    }

    public static boolean isCurrentWeek(String startOfWeekDate, String endOfWeekDate) {
        Calendar today = Calendar.getInstance();
        String inputPattern = DATE_FORMAT;
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);

        try {
            Date endWeekDate = inputFormat.parse(endOfWeekDate);
            Date startWeekDate = inputFormat.parse(startOfWeekDate);
            Calendar startWeekCal = Calendar.getInstance();
            startWeekCal.setTime(startWeekDate);
            Calendar endWeekCal = Calendar.getInstance();
            endWeekCal.setTime(endWeekDate);

            if ((startWeekCal.before(today) && endWeekCal.after(today)) || (today.get(Calendar.YEAR) == startWeekCal.get(Calendar.YEAR) && today.get(Calendar.MONTH) == startWeekCal.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) == startWeekCal.get(Calendar.DAY_OF_MONTH))) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getDiffernceBnTwoDates(String startDate) {

        try {
            Calendar c = Calendar.getInstance();
            //System.out.println("Current time => " + c.getTime());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String endDate1 = df.format(c.getTime());
            Date endWeekDate = df.parse(endDate1);
            c.setTime(endWeekDate);
            String endDate = df.format(c.getTime());

            Calendar startWeekCal = Calendar.getInstance();
            Date startWeekDate = df.parse(startDate);
            String startDte = df.format(startWeekDate.getTime());
            Date startTime = df.parse(startDte);
            startWeekCal.setTime(startTime);
            String startDaate = df.format(startWeekCal.getTime());
            //milliseconds
            long different = c.getTimeInMillis() - startWeekDate.getTime();

            //System.out.println("startDate : " + startDaate);
            //System.out.println("endDate : " + endDate);
            //System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            //System.out.printf("%d days, %d hours, %d minutes, %d seconds%n", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
            if ((elapsedDays >= 0) && (elapsedDays < 7)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList getOneWeekDates(String date) {
        ArrayList days = new ArrayList();
        try {
            String dt = date.replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            for (int i = 0; i < 7; i++) {
                if (i > 0) {
                    c.add(Calendar.DATE, 1);
                }
                String day = (String) android.text.format.DateFormat.format("d", c.getTime());
                if (day.startsWith("0")) {
                    day = day.replace("0", "");
                }
                days.add(day);
            }
            return days;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return days;
    }

    public static String getEndDate(String startWeekDate) {
        try {
            String dt = startWeekDate.replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, 6);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getOneDayMinus(String endWeekDate) {
        try {
            String dt = endWeekDate.replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, -1);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getOneDayPlus(String date) {
        try {
            String dt = date.replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, 1);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getStartDate(String endWeekDate) {
        try {
            String dt = endWeekDate.replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, (-6));
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Intent getGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        return intent;
    }

    public static String getClickedPhotoPath(Context c) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return getAppDirPath(c) + File.separator + LegionUtils.getNewFileName() + IMAGE_FILE_EXTENSION;
        }
        return null;
    }

    public static String getAppDirPath(Context c) {
        return SDCARD_PATH + File.separator + c.getString(R.string.app_name);
    }

    public static String getNewFileName() {
        return "Legion" + getCurrentTime();
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat(DATE_TIME_FORMAT_IN_FILE_NAMES, Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public static String getPathOfExternalPhotoFromUri(final Context context, final Uri uri) {
        try {
            // DocumentProvider
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static int getPhotoOrientation(String photoPath) {
        try {
            switch (new ExifInterface(photoPath).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeFromDate(String date) {
        try {
            Date date1 = null;
            String inputPattern = "yyyy-MM-dd HH:mm:ss.SSS-'Z'";
            String[] extractTime = date.split("T");
            Log.d("da", extractTime[1]);
            String upTo5Characters = extractTime[1].substring(0, Math.min(extractTime[1].length(), 5));
            Log.d("time", upTo5Characters);
            String time = getAmorPm(upTo5Characters);
            return time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAmorPm(String time) {
        try {
            String _24HourTime = time;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("h:mm a");
            //    _12HourSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            //rintln(_24HourDt);
            //System.out.println(_12HourSDF.format(_24HourDt));
            //  System.out.println(hr%12 + ":" + min + " " + ((hr>=12) ? "PM" : "AM"));
            return _12HourSDF.format(_24HourDt).replace("AM", "am").replace("PM", "pm").replace("a.m.", "am").replace("p.m.", "pm").replace(" ", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String convertFromMillsToTime(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        StringBuilder sb = new StringBuilder(64);
        sb.append(hours);
        sb.append(":");
        sb.append(minutes);
        try {
            String _24HourTime = sb.toString();
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("h:mma");
            //_12HourSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            //System.out.println(_24HourDt);
            //System.out.println(_12HourSDF.format(_24HourDt));
            return _12HourSDF.format(_24HourDt).replace("AM", "am").replace("PM", "pm").replace("a.m.", "am").replace("p.m.", "pm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getYearFromDate(String startDate) {
        try {
            if (startDate != null) {
                String[] year = startDate.split("-");
                return year[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static String convertMinsToTimeinHrs(long min) {
        long hours = min / 60;
        long minutes = min % 60;
        //System.out.printf("%d:%02d", hours, minutes);
        return getAmorPm(hours + ":" + new DecimalFormat("##").format(minutes));
    }

    public static String convertMinsToHrs(String mints) {
        if (mints != null) {
            long min = Long.valueOf(mints);
            long hours = min / 60;
            long minutes = min % 60;
            //System.out.printf("%d:%02d", hours, minutes);
            if (min != 0) {
                return "" + ((hours + "." + minutes));
            } else {
                return "" + ((hours));
            }
        }
        return "0";
    }

    public static String convertMinsToHrsReg(String mints) {
        try {
            if (mints != null) {
                double min = Double.valueOf(mints);
                double hours = min / 60;
                // return String.valueOf(hours);
                double roundedvalue = round(hours, 1);
                if ((roundedvalue - (int) roundedvalue) != 0)
                    return String.valueOf(round(hours, 1));
                else
                    return String.valueOf((int) round(hours, 1));
                // return String.valueOf(round(hours,1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static ArrayList getDifferenceBnTwoMillis(long clockIn, long clockOut) {
        try {
            long clockInminutes = TimeUnit.MILLISECONDS.toMinutes(clockIn);//((clockIn / (1000 * 60)) % 60);
            long clockOutminutes = TimeUnit.MILLISECONDS.toMinutes(clockOut);//((clockOut / (1000 * 60)) % 60);
            long finalminutes = (clockOutminutes - clockInminutes);
            int hours = (int) (finalminutes / 60);
            long minutes = finalminutes % 60;
            ///System.out.printf("%d:%02d", hours, minutes);
            ArrayList minsHrsArrayList = new ArrayList();
            minsHrsArrayList.add(hours);
            minsHrsArrayList.add(minutes);
            return minsHrsArrayList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isPastWeek(String startOfWeekDate, String endOfWeekDate) {
        Calendar cal = Calendar.getInstance();
        String inputPattern = DATE_FORMAT;
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        try {
            Date endWeekDate = inputFormat.parse(endOfWeekDate);
            Calendar endWeekCal = Calendar.getInstance();
            endWeekCal.setTime(endWeekDate);
            return cal.after(endWeekCal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String getCountOfDaysWithDateFormat(String startDate, String endDate) {
        // 360 days from sever date
        try {
            SimpleDateFormat myFormat = new SimpleDateFormat(DATE_FORMAT);
            String inputString1 = startDate.replace("T", " ");
            String inputString2 = endDate.replace("T", " ");
            //  myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date date1 = myFormat.parse(inputString1);
                Date date2 = myFormat.parse(inputString2);
                long diff = date2.getTime() - date1.getTime();
                //System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                return "" + ((TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getCountOfDays(String startDate, String endDate) {
        // 360 days
        try {
            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            String inputString1 = startDate;
            String inputString2 = endDate;
            //   myFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date date1 = myFormat.parse(inputString1);
                Date date2 = myFormat.parse(inputString2);
                long diff = date2.getTime() - date1.getTime();
                //System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                return "" + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ArrayList getSelectedCurrentDate(String date, boolean currentWeekOrNot) {
        ArrayList days = new ArrayList();
        try {
            String dt = date.replace("T", " ");  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            for (int i = 0; i < 7; i++) {
                if (i > 0)
                    c.add(Calendar.DATE, 1);
                String day = (String) android.text.format.DateFormat.format("d", c.getTime());
                if (currentWeekOrNot) {
                    try {
                        Calendar cal = Calendar.getInstance();
                        String day1 = (String) android.text.format.DateFormat.format("d", cal.getTime());
                        if (day1.equalsIgnoreCase(day)) {
                            days.add(day1);
                            //System.out.println("Current time => " + cal.getTime() + "day" + day1);
                        } else {
                            days.add("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    days.add("");
                }
            }
            return days;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        String day1 = (String) android.text.format.DateFormat.format("d", cal.getTime());
        return day1;
    }

    public static long getTimeMatchedOrNot(String serverDate) {
        // hours difference from current Date to Server Date
        try {
            if (serverDate != null) {
                String[] extractTime = serverDate.split("T");
                String serverDateString = extractTime[0] + " " + extractTime[1].substring(0, Math.min(extractTime[1].length(), 5));
                Log.d("CurrentDateFromServer", serverDateString);
                Calendar servercal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
                servercal.setTime(sdf.parse(serverDateString));
                Log.d("hours diff", "" + (servercal.getTimeInMillis() - System.currentTimeMillis()));
                return (servercal.getTimeInMillis() - System.currentTimeMillis());
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("hours diff", "-1");
            return -1;
        }
    }


    public static void getDisplayMetrics(Context context) {
        int paddingBig = 0, paddingSmall = 0, minWidth = 0, paddingNormal = 0, textSize = 0;
        Legion_PrefsManager prefManager = new Legion_PrefsManager(context);
        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_MEDIUM:
                //    showToast("MEd");
                paddingBig = 40;
                paddingSmall = 20;
                minWidth = 130;
                paddingNormal = 40;
                textSize = 13;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                //  showToast("HI");
                paddingBig = 60;
                paddingSmall = 20;
                minWidth = 130;
                paddingNormal = 40;
                textSize = 13;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                //  showToast("XHI");
                paddingBig = 80;
                paddingSmall = 30;
                minWidth = 150;
                paddingNormal = 50;
                textSize = 14;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                //    showToast("XXHI");
                paddingBig = 100;
                paddingSmall = 50;
                minWidth = 220;
                paddingNormal = 70;
                textSize = 14;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                //showToast("XXXHI");
                paddingBig = 100;
                paddingSmall = 50;
                minWidth = 280;
                paddingNormal = 70;
                textSize = 15;
                break;
            case DisplayMetrics.DENSITY_420:
                //   showToast("420");
                paddingBig = 100;
                paddingSmall = 50;
                minWidth = 220;
                paddingNormal = 70;
                textSize = 15;
                break;
            case DisplayMetrics.DENSITY_560:
                //     showToast("560");
                paddingBig = 100;
                paddingSmall = 50;
                minWidth = 220;
                paddingNormal = 70;
                textSize = 15;
                break;
            case DisplayMetrics.DENSITY_400:
                paddingBig = 80;
                paddingSmall = 30;
                minWidth = 150;
                paddingNormal = 50;
                textSize = 14;
                break;
            case DisplayMetrics.DENSITY_360:
                paddingBig = 40;
                paddingSmall = 20;
                minWidth = 130;
                paddingNormal = 40;
                textSize = 14;
                break;
            case DisplayMetrics.DENSITY_280:
                paddingBig = 40;
                paddingSmall = 20;
                minWidth = 130;
                paddingNormal = 40;
                textSize = 14;
                break;
            case DisplayMetrics.DENSITY_TV:
                paddingBig = 80;
                paddingSmall = 30;
                minWidth = 150;
                paddingNormal = 50;
                textSize = 14;
                break;
            default:
                paddingBig = 100;
                paddingSmall = 50;
                minWidth = 220;
                paddingNormal = 70;
                textSize = 15;
                break;
        }
        prefManager.saveInt(Prefs_Keys.PADDING_BIG, paddingBig);
        prefManager.saveInt(Prefs_Keys.PADDING_LESS, paddingSmall);
        prefManager.saveInt(Prefs_Keys.MIN_WIDTH, minWidth);
        prefManager.saveInt(Prefs_Keys.PADDING_50, paddingNormal);
        prefManager.saveInt(Prefs_Keys.SIZE, textSize);
    }

    public static int convertFromMinutesToHoursInt(String hrs) {
        // 7.5 in hours
        String hours = "0";
        if (hrs != null) {
            hours = String.valueOf(Double.parseDouble(hrs) / 60).replace(".0", "");
        }
        if (hours.contains(".")) {
            String[] splitHours = hours.split("\\.");
            hours = splitHours[0];
        }
        return Integer.valueOf(hours);
    }

    public static String getDatefromServerDate(String shiftDate) {
        //  Extract date like 9,10
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(shiftDate.toString().split("T")[0]);
            SimpleDateFormat fmtOut = new SimpleDateFormat("d");
            String finalDate = fmtOut.format(date);
            return finalDate;
        } catch (Exception e) {
            return shiftDate;
        }
    }

    public static String getMonthAndYearFromDate(String shiftDate) {
        // Monday jan 9th
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(shiftDate.toString().split("T")[0]);
            c.setTime(date);
            SimpleDateFormat fmtOut1 = new SimpleDateFormat("EEEE, MMM d");
            String finalDate = fmtOut1.format(date);

            SimpleDateFormat fmtOut2 = new SimpleDateFormat("d");
            int day = Integer.parseInt(fmtOut2.format(date));

            String suffix = "th";
            if (day == 1 || day == 21 || day == 31) {
                suffix = "st";
            } else if (day == 2 || day == 22) {
                suffix = "nd";
            } else if (day == 3 || day == 23) {
                suffix = "rd";
            }
            return finalDate + suffix;
        } catch (Exception e) {
            return shiftDate;
        }
    }

    public static String getUpdatedString(String dayName, String dateTv) {
        String text = dayName + "<br/>" + "<big><big>" + dateTv + "</big></big>";
        return text;
    }

    public static ScheduleWorkerShift getUpdatedSchedule(JSONObject object, ScheduleWorkerShift scheduLedPos) {
        try {
            if (object.getString("responseStatus").equalsIgnoreCase("SUCCESS")) {
                JSONArray workerShiftsArray = object.optJSONArray("workerShifts");
                JSONObject workerShiftJsonObject = workerShiftsArray.optJSONObject(0);
                ScheduleWorkerShift scheduleWorkerShift = scheduLedPos;
                scheduLedPos.getAssociatedWorkerArrayList().clear();
                String year = (workerShiftJsonObject.optString("year"));
                String weekStartDayOfTheYear = workerShiftJsonObject.optString("weekStartDayOfTheYear");
                String role1 = workerShiftJsonObject.optString("role");
                String name1 = workerShiftJsonObject.optString("name");
                String notes = workerShiftJsonObject.optString("notes");
                String dayOfTheWeek = workerShiftJsonObject.optString("dayOfTheWeek");
                String startMin = workerShiftJsonObject.optString("startMin");
                String endMin = workerShiftJsonObject.optString("endMin");
                String shiftStartDate = workerShiftJsonObject.optString("shiftStartDate");
                String shiftEndDate = workerShiftJsonObject.optString("shiftEndDate");
                String shiftId = workerShiftJsonObject.optString("shiftId");
                String status1 = workerShiftJsonObject.optString("status");
                String clockInTime = workerShiftJsonObject.optString("clockInTime");
                String clockOutTime = workerShiftJsonObject.optString("clockOutTime");
                String estimatedPay = workerShiftJsonObject.optString("estimatedPay");
                String availability = workerShiftJsonObject.optString("availability");
                String hasMeal = workerShiftJsonObject.optString("hasMeal");
                String unpaidBreakMinutes = workerShiftJsonObject.optString("unpaidBreakMinutes");
                String doubleOvertimeMinutes = workerShiftJsonObject.optString("doubleOvertimeMinutes");
                String weekOvertimeMinutes = workerShiftJsonObject.optString("weekOvertimeMinutes");
                String weekDoubleOvertimeMinutes = workerShiftJsonObject.optString("weekDoubleOvertimeMinutes");
                String cost = workerShiftJsonObject.optString("cost");
                String offerStatus = workerShiftJsonObject.optString("offerStatus");
                String type = workerShiftJsonObject.optString("type");
                String regularMinutes = workerShiftJsonObject.optString("regularMinutes");
                scheduleWorkerShift.setAvailability(availability);
                scheduleWorkerShift.setClockInTime(clockInTime);
                scheduleWorkerShift.setClockOutTime(clockOutTime);
                scheduleWorkerShift.setCost(cost);
                scheduleWorkerShift.setDayOfTheWeek(dayOfTheWeek);
                scheduleWorkerShift.setDoubleOvertimeMinutes(doubleOvertimeMinutes);
                scheduleWorkerShift.setEndMin(endMin);
                scheduleWorkerShift.setEstimatedPay(estimatedPay);
                scheduleWorkerShift.setHasMeal(hasMeal);
                scheduleWorkerShift.setStatus(status1);
                scheduleWorkerShift.setRegularMinutes(regularMinutes);
                scheduleWorkerShift.setWeekStartDayOfTheYear(weekStartDayOfTheYear);
                scheduleWorkerShift.setWeekDoubleOvertimeMinutes(weekDoubleOvertimeMinutes);
                scheduleWorkerShift.setWeekOvertimeMinutes(weekOvertimeMinutes);
                scheduleWorkerShift.setUnpaidBreakMinutes(unpaidBreakMinutes);
                scheduleWorkerShift.setShiftEndDate(shiftEndDate);
                scheduleWorkerShift.setShiftStartDate(shiftStartDate);
                scheduleWorkerShift.setShiftId(shiftId);
                scheduleWorkerShift.setYear(year);
                scheduleWorkerShift.setRole(role1);
                scheduleWorkerShift.setName(name1);
                scheduleWorkerShift.setNotes(notes);
                scheduleWorkerShift.setStartMin(startMin);
                scheduleWorkerShift.setType(type);
                scheduleWorkerShift.setOfferStatus(offerStatus);
                JSONArray associatedWorkersArray = workerShiftJsonObject.optJSONArray("associatedWorkers");
                Log.v("associateArray: ", "" + associatedWorkersArray.length());
                if (associatedWorkersArray != null && associatedWorkersArray.length() > 0) {
                    ArrayList<AssociatedWorker> associatedWorkerArrayList = new ArrayList<>();
                    int associatedWorkersArraySize = associatedWorkersArray.length();
                    for (int j = 0; j < associatedWorkersArraySize; ++j) {
                        JSONObject associatedWorkerJsonObj = associatedWorkersArray.getJSONObject(j);
                        AssociatedWorker associatedWorker = new AssociatedWorker();
                        associatedWorker.setExternalId(associatedWorkerJsonObj.optString("externalId"));
                        associatedWorker.setAddress1(associatedWorkerJsonObj.optString("address1"));
                        associatedWorker.setAddress2(associatedWorkerJsonObj.optString("address2"));
                        associatedWorker.setCity(associatedWorkerJsonObj.optString("city"));
                        associatedWorker.setEmail(associatedWorkerJsonObj.optString("email"));
                        associatedWorker.setFirstName(associatedWorkerJsonObj.optString("firstName"));
                        associatedWorker.setLastName(associatedWorkerJsonObj.optString("lastName"));
                        String nickName = associatedWorkerJsonObj.optString("nickName").trim();
                        if (!nickName.equalsIgnoreCase("null") || nickName != null || nickName.length() > 0) {
                            associatedWorker.setNickName(associatedWorkerJsonObj.optString("nickName"));
                        } else {
                            associatedWorker.setNickName("");
                        }
                        associatedWorker.setMemberId(associatedWorkerJsonObj.optString("memberId"));
                        associatedWorker.setPhoneNumber(associatedWorkerJsonObj.optString("phoneNumber"));
                        associatedWorker.setPictureUrl(associatedWorkerJsonObj.optString("pictureUrl"));
                        associatedWorker.setRole(associatedWorkerJsonObj.optString("role"));
                        associatedWorker.setZip(associatedWorkerJsonObj.optString("zip"));
                        associatedWorker.setState(associatedWorkerJsonObj.optString("state"));
                        associatedWorker.setStartEngagementDate(associatedWorkerJsonObj.optString("startEngagementDate"));
                        associatedWorker.setStatus(associatedWorkerJsonObj.optString("status"));
                        associatedWorker.setShiftLead(Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead")));
                        if (Boolean.parseBoolean(associatedWorkerJsonObj.optString("isShiftLead"))) {
                            associatedWorkerArrayList.add(0, associatedWorker);
                        } else {
                            associatedWorkerArrayList.add(associatedWorker);
                        }
                    }
                    scheduleWorkerShift.setAssociatedWorkerArrayList(associatedWorkerArrayList);
                }

                JSONObject workerJsonObject = workerShiftJsonObject.optJSONObject("workerKey");
                if (workerJsonObject != null) {
                    WorkerKey workerKey = new WorkerKey();
                    String externalId = workerJsonObject.optString("externalId");
                    String firstName = workerJsonObject.optString("firstName");
                    String lastName = workerJsonObject.optString("lastName");
                    String email = workerJsonObject.optString("email");
                    String phoneNumber = workerJsonObject.optString("phoneNumber");
                    String address1 = workerJsonObject.optString("address1");
                    String address2 = workerJsonObject.optString("address2");
                    String city = workerJsonObject.optString("city");
                    String state = workerJsonObject.optString("state");
                    String zip = workerJsonObject.optString("zip");
                    String memberId = workerJsonObject.optString("memberId");
                    String pictureUrl = workerJsonObject.optString("pictureUrl");
                    String status = workerJsonObject.optString("status");
                    String role = workerJsonObject.optString("role");
                    String startEngagementDate = workerJsonObject.optString("startEngagementDate");
                    workerKey.setExternalId(externalId);
                    workerKey.setAddress1(address1);
                    workerKey.setAddress2(address2);
                    workerKey.setCity(city);
                    workerKey.setEmail(email);
                    workerKey.setFirstName(firstName);
                    workerKey.setLastName(lastName);
                    workerKey.setMemberId(memberId);
                    workerKey.setPhoneNumber(phoneNumber);
                    workerKey.setPictureUrl(pictureUrl);
                    workerKey.setRole(role);
                    workerKey.setZip(zip);
                    workerKey.setState(state);
                    workerKey.setStartEngagementDate(startEngagementDate);
                    workerKey.setStatus(status);
                    scheduleWorkerShift.setWorkerKey(workerKey);
                }

                JSONObject businessJsonObject = workerShiftJsonObject.optJSONObject("businessKey");
                if (businessJsonObject != null) {
                    BusinessKey businessKey = new BusinessKey();

                    String externalId = businessJsonObject.optString("externalId");
                    String enterpriseName = businessJsonObject.optString("enterpriseName");
                    String enterpriseId = businessJsonObject.optString("enterpriseId");
                    String name = businessJsonObject.optString("name");
                    String address = businessJsonObject.optString("address");
                    String businessId = businessJsonObject.optString("businessId");
                    String googlePlacesId = businessJsonObject.optString("googlePlacesId");
                    businessKey.setAddress(address);
                    businessKey.setBusinessId(businessId);
                    businessKey.setEnterpriseId(enterpriseId);
                    businessKey.setExternalId(externalId);
                    businessKey.setEnterpriseName(enterpriseName);
                    businessKey.setGooglePlacesId(googlePlacesId);
                    businessKey.setName(name);
                    scheduleWorkerShift.setBusinessKey(businessKey);
                }
                return scheduleWorkerShift;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showMessageDialog(Context context, String msg, int drawable) {
        if(context == null){
            return;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_success_popup);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        final TextView msgTv = (TextView) dialog.findViewById(R.id.msgTv);
        final ImageView msgIv = (ImageView) dialog.findViewById(R.id.msgIv);
        msgIv.setImageResource(drawable);
        msgTv.setText(msg);
        LegionUtils.doApplyFont(context.getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
        msgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        if (drawable == R.drawable.confirmation) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, SUCCESS_DIALOG_DURATION);
        }
    }

    public static void showOfflineDialog(Activity context) {
        if (offlineDialog == null) {
            offlineDialog = new Dialog(context);
            offlineDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            offlineDialog.setCancelable(true);
            offlineDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            offlineDialog.setContentView(R.layout.dialog_success_popup);
            offlineDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            final TextView msgTv = (TextView) offlineDialog.findViewById(R.id.msgTv);
            final ImageView msgIv = (ImageView) offlineDialog.findViewById(R.id.msgIv);
            msgTv.setText(context.getString(R.string.device_offline_alert_message));
            LegionUtils.doApplyFont(context.getAssets(), (LinearLayout) offlineDialog.findViewById(R.id.dialogParentLayout));
            msgTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (offlineDialog != null) {
                        offlineDialog.dismiss();
                        offlineDialog = null;
                    }
                }
            });
            offlineDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (offlineDialog != null) {
                        offlineDialog.dismiss();
                        offlineDialog = null;
                    }
                }
            }, SUCCESS_DIALOG_DURATION);
        } else {
            offlineDialog.dismiss();
            offlineDialog = null;
        }
    }

    public static Dialog saveChangesDialog(Context context, String msg) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x59000000));
            dialog.setContentView(R.layout.save_changes_dialog);
            final TextView msgTv = (TextView) dialog.findViewById(R.id.msgTv);
            TextView cancelTv = (TextView) dialog.findViewById(R.id.cancelTv);
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            msgTv.setText(msg);
            LegionUtils.doApplyFont(context.getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
            msgTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            return dialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static void logoutChangesDialog(final Activity activity) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x59000000));
            dialog.setContentView(R.layout.save_changes_dialog);
            final TextView msgTv = (TextView) dialog.findViewById(R.id.msgTv);
            TextView cancelTv = (TextView) dialog.findViewById(R.id.cancelTv);
            TextView okTv = (TextView) dialog.findViewById(R.id.saveTv);
            okTv.setText("Logout");

            okTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (activity != null) {
                        try {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/user_" + new Legion_PrefsManager(activity).get(Prefs_Keys.WORKER_ID));
                        } catch (Exception e) {
                            Log.d("Exception", "Not subscribed");
                        }
                        Legion_PrefsManager spManager = new Legion_PrefsManager(activity);
                        spManager.clearAllPrefs();
                        Intent i = new Intent(activity, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(i);
                    }
                }
            });
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            LegionUtils.doApplyFont(activity.getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
            msgTv.setText("Do you want to log out of the Legion app?");
            msgTv.setOnClickListener(new View.OnClickListener() {
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

    public static Dialog callDialog(final Activity activity, String msg) {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x59000000));
            dialog.setContentView(R.layout.save_changes_dialog);
            final TextView msgTv = (TextView) dialog.findViewById(R.id.msgTv);
            TextView cancelTv = (TextView) dialog.findViewById(R.id.cancelTv);
            TextView okTv = (TextView) dialog.findViewById(R.id.saveTv);
            okTv.setText("Call");
            okTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            msgTv.setText(msg);
            LegionUtils.doApplyFont(activity.getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
            msgTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            return dialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUpdatedAddress(String fullAddress) {
        //Street and city split by comma
        try {
            String[] address = fullAddress.split(",");
            return (address[0] + "," + address[1]);
        } catch (Exception e) {
            return fullAddress;
        }
    }

    public static int getEstimatedPayAsInt(String estimatedPay) {
        int estPay = 0;
        try {
            if ((estimatedPay != null) && (estimatedPay.trim().length() > 1)) {
                estPay = (int) Math.round(Double.parseDouble(estimatedPay.trim()));
            }
        } catch (Exception e) {
            Log.d("Exception is :", "NumberFormatException");
        }
        return estPay;
    }

    public static ArrayList getDates() {
        ArrayList datesArrayList = new ArrayList();
        Calendar calendar = Calendar.getInstance();
        Log.v("Current Week", String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));
        // get the starting and ending date
        // Set the calendar to sunday of the current week
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - 1);
        }
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        //System.out.println("Current week = " + Calendar.DAY_OF_WEEK);

        // Print dates of the current week starting on Sunday
        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        String startDate = "", endDate = "";

        startDate = df.format(calendar.getTime());
        calendar.add(Calendar.DATE, 6);
        endDate = df.format(calendar.getTime());

        //System.out.println("Start Date = " + startDate);
        //System.out.println("End Date = " + endDate);
        datesArrayList.add(startDate);
        datesArrayList.add(endDate);
        Log.d("ArraylistofDates", datesArrayList.toString());
        return datesArrayList;
    }

    public static void showContactUsPopup(final Activity act) {
        try {
            final Dialog dialog = new Dialog(act);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.popup_contact_us);
            final TextView okButton = (TextView) dialog.findViewById(R.id.okButton);
            final TextView contactUsButton = (TextView) dialog.findViewById(R.id.contactUsButton);
            final TextView tv1 = (TextView) dialog.findViewById(R.id.tv1);
            tv1.setText("Oops, something went wrong...\nPlease try again later or let us know what went wrong.");
            LegionUtils.doApplyFont(act.getAssets(), (LinearLayout) dialog.findViewById(R.id.dialogParentLayout));
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            contactUsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("text/html");
                        final PackageManager pm = act.getPackageManager();
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
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, act.getString(R.string.app_name) + " App for " + Build.MANUFACTURER + " " + Build.MODEL);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "I am using " + act.getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + "), and I just had an issue with the app:\n\n\n");
                        try {
                            act.startActivity(emailIntent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(act, "There is no email client installed on this device.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(act, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isFeatureEnabled(Context context, String feature, String currentBusinessName) {
        String json = null;
        boolean isEnabled = true;
        try {
            InputStream is = context.getAssets().open("Features.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            try {
                JSONObject featuresObj = new JSONObject(json);
                if (featuresObj.has(feature + ".enabled")) {
                    isEnabled = featuresObj.optBoolean(feature + ".enabled");
                } else if (!LegionUtils.nullOrEmpty(currentBusinessName) &&
                        feature.contains(feature + ".enabledForBusinessName")) {
                    String ftBusName = featuresObj.optString(feature + ".enabledForBusinessName");
                    isEnabled = ftBusName.contains(currentBusinessName);
                }
            } catch (Exception jsonException) {
                jsonException.printStackTrace();
            }
            return isEnabled;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Log.d("json", json);
        return isEnabled;
    }

    public static boolean nullOrEmpty(String string) {
        return (string == null || string.trim().length() == 0);
    }

    public static SimpleDateFormat getInputDateFormatterTimeZone(SimpleDateFormat inputDateFormatter) {
        try {
            inputDateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return inputDateFormatter;
    }

    public static SimpleDateFormat getOutputDateFormatterTimeZone(SimpleDateFormat outputDateFormatter, Legion_PrefsManager legionPreferences) {
        try {
            outputDateFormatter.setTimeZone(TimeZone.getTimeZone(legionPreferences.get(Prefs_Keys.BUSINESS_TIMEZONE)));
        }catch (Exception e){
            e.printStackTrace();
        }
        return outputDateFormatter;
    }

    public static String addDaysToDate(String date,int noOfDays) {
        try {
            String dt = date.replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, noOfDays);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static synchronized Date parseDateFromJsonString (String dateStr) throws ParseException {
        return JsonDateFormatter.parse(dateStr);
    }
}
