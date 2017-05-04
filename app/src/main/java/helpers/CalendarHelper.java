package helpers;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

import models.ScheduleWorkerShift;
import utils.Legion_Constants;

public class CalendarHelper {
    public static final int CALENDARHELPER_PERMISSION_REQUEST_CODE = 99;
    private static final String TAG = "CalendarHelper";

    private static long checkEventExistOrNot(Activity activity,ContentResolver cr, long startTimeInMillis) {
        long eventId = -1;
        try {
            Uri eventUri = Uri.parse("content://com.android.calendar/events");
            String projection[] = {"_id", Events.DESCRIPTION, Events.DTSTART, Events.DTEND, Events.CALENDAR_ID};
            Cursor cursor = cr.query(eventUri, projection, Events.DESCRIPTION + " LIKE ?", new String[]{"% Added by Legion"}, null);
            if (cursor.moveToFirst()) {
                String calID;
                int idCol = cursor.getColumnIndex(projection[0]);
                int dateCol = cursor.getColumnIndex(projection[2]);
                int calIdCol = cursor.getColumnIndex(projection[4]);
                String calendarName = cursor.getString(calIdCol);
                do {
                    calID = cursor.getString(idCol);
                    long dateInMillis = cursor.getLong(dateCol);
                    if (dateInMillis == startTimeInMillis) {
                        Log.d("CalNme", new Legion_PrefsManager(activity).get(Legion_Constants.Prefs_Keys.CALENDAR_NAME));
                        if (calendarName.equalsIgnoreCase(new Legion_PrefsManager(activity).get(Legion_Constants.Prefs_Keys.CALENDAR_NAME))) {
                            eventId = Integer.parseInt(calID);
                            break;
                        }
                    }
                } while (cursor.moveToNext());
                cursor.close();

            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return eventId;
    }

    public static void MakeNewCalendarEntry(ScheduleWorkerShift scheduleWorkerShift, Activity activity, int calendarId) {
        try {
            String DATE_FORMAT = "yyyy-MM-dd HH:mm";
            String title = scheduleWorkerShift.getRole().toUpperCase() + " shift @ " + scheduleWorkerShift.getBusinessKey().getName();
            long startTime = 0, endTime = 0;
            String startDate = scheduleWorkerShift.getShiftStartDate();
            String endDate = scheduleWorkerShift.getShiftEndDate();
            long eventId;
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            try {
                Date date = format.parse(startDate.replace("T", " "));
                startTime = date.getTime();
                Date date1 = format.parse(endDate.replace("T", " "));
                endTime = date1.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Description
            String notess = scheduleWorkerShift.getRole().toUpperCase() + " shift @ " + scheduleWorkerShift.getBusinessKey().getAddress();
            notess += "\nThe Shift Lead is " + scheduleWorkerShift.getAssociatedWorkerArrayList().get(0).getFirstName();
            notess += "\n\n Added by Legion";
            int reminderValue = 0;
            boolean hasAlarm;
            Legion_PrefsManager prefsManager = new Legion_PrefsManager(activity);
            if (prefsManager.hasKey(Legion_Constants.Prefs_Keys.SELECTED_TIME_IN_MINS) && !(prefsManager.get(Legion_Constants.Prefs_Keys.SELECTED_TIME).equalsIgnoreCase("None"))) {
                reminderValue = Integer.valueOf(prefsManager.get(Legion_Constants.Prefs_Keys.SELECTED_TIME_IN_MINS));
                hasAlarm = true;
            } else {
                hasAlarm = false;
            }

            ContentResolver cr = activity.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(Events.DTSTART, startTime);
            values.put(Events.DTEND, endTime);
            values.put(Events.TITLE, title);
            values.put(Events.DESCRIPTION, notess);
            values.put(Events.CALENDAR_ID, calendarId);
            values.put(Events.EVENT_LOCATION, scheduleWorkerShift.getBusinessKey().getAddress());
            values.put(Events.STATUS, Events.STATUS_CONFIRMED);
            values.put(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
            if (hasAlarm) {
                values.put(Events.HAS_ALARM, true);
            }
            values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            Log.i(TAG, "Timezone retrieved=>" + TimeZone.getDefault().getID());
            eventId = checkEventExistOrNot(activity,cr, startTime);
            if (eventId == -1) {
                //insert event
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Uri uri = cr.insert(Events.CONTENT_URI, values);
                Log.i(TAG, "Uri returned=>" + uri.toString());
                long eventID = Long.parseLong(uri.getLastPathSegment());
                Log.i(TAG, "INSERTING EVENT ID=>" + eventID + "--->" + startDate);
                if (hasAlarm) {
                    setReminder(cr, reminderValue, eventID);
                }
            } else {
                // Update event
                Log.i(TAG, " update EVENT ID=>" + eventId);
                Uri updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, (eventId));
                int iNumRowsUpdated = cr.update(updateUri, values, null, null);
                Log.i("update", "Updated " + iNumRowsUpdated + " calendar entry.--->" + startDate);
                if (hasAlarm) {
                    setReminder(cr, reminderValue, eventId);
                }
            }
        } catch (Exception e) {
            Log.d("Exception", e.getLocalizedMessage());
        }

    }

    public static void setReminder(ContentResolver cr, int selectedReminderValue, long eventId) {
        /***************** Event: Reminder(with alert) Adding reminder to event *******************/
        String reminderUriString = "content://com.android.calendar/reminders";
        ContentValues reminderValues = new ContentValues();
        reminderValues.put("event_id", eventId);
        reminderValues.put("minutes", selectedReminderValue); // Default value of the system. Minutes is a integer
        reminderValues.put("method", 1); // Alert Methods: Default(0),
        // Alert(1), Email(2),SMS(3)
        cr.insert(Uri.parse(reminderUriString), reminderValues);
    }

    public static void requestCalendarReadWritePermission(Activity caller) {
        List<String> permissionList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(caller, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_CALENDAR);
        }
        if (ContextCompat.checkSelfPermission(caller, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_CALENDAR);
        }
        if (permissionList.size() > 0) {
            String[] permissionArray = new String[permissionList.size()];
            for (int i = 0; i < permissionList.size(); i++) {
                permissionArray[i] = permissionList.get(i);
            }
            ActivityCompat.requestPermissions(caller, permissionArray, CALENDARHELPER_PERMISSION_REQUEST_CODE);
        }

    }

    public static Hashtable listCalendarId(Context c) {
        if (haveCalendarReadWritePermissions((Activity) c)) {
            String projection[] = {"_id", "calendar_displayName"};
            Uri calendars;
            calendars = Uri.parse("content://com.android.calendar/calendars");
            ContentResolver contentResolver = c.getContentResolver();
            Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);
            if (managedCursor.moveToFirst()) {
                String calName;
                String calID;
                int cont = 0;
                int nameCol = managedCursor.getColumnIndex(projection[1]);
                int idCol = managedCursor.getColumnIndex(projection[0]);
                Hashtable<String, String> calendarIdTable = new Hashtable<>();
                do {
                    calName = managedCursor.getString(nameCol);
                    calID = managedCursor.getString(idCol);
                    Log.v(TAG, "CalendarName:" + calName + " ,id:" + calID);
                    calendarIdTable.put(calName, calID);
                    cont++;
                } while (managedCursor.moveToNext());
                managedCursor.close();

                return calendarIdTable;
            }
        }
        return null;
    }

    public static boolean haveCalendarReadWritePermissions(Activity caller) {
        int permissionCheck = ContextCompat.checkSelfPermission(caller, Manifest.permission.READ_CALENDAR);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            permissionCheck = ContextCompat.checkSelfPermission(caller, Manifest.permission.WRITE_CALENDAR);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static int updateCalendarId(Context context, Hashtable<String, String> calendarIdTable) {
        int calendarId = -1;
        if (calendarIdTable == null) {
            return -1;
        }
        Enumeration e = calendarIdTable.keys();
        String key = "";
        Legion_PrefsManager prefsManager = new Legion_PrefsManager(context);
        while (e.hasMoreElements()) {
            key = (String) e.nextElement();
            if (prefsManager.hasKey(Legion_Constants.Prefs_Keys.CALENDAR_NAME)) {
                Log.d("icd", prefsManager.get(Legion_Constants.Prefs_Keys.CALENDAR_NAME));
                calendarId = Integer.parseInt(calendarIdTable.get(prefsManager.get(Legion_Constants.Prefs_Keys.CALENDAR_NAME)));
                break;
            } else {
                Log.d("icd", key);
                calendarId = Integer.parseInt(calendarIdTable.get(key));
                break;
            }
        }
        return calendarId;
    }

    // Delete Event
    public static void getDeleteEventId(Activity activity, String date, int calendarId) {
        ContentResolver cr = activity.getContentResolver();
        Uri eventUri = Uri.parse("content://com.android.calendar/events");
        int result = 0;
        String projection[] = {"_id", Events.DTSTART, Events.DESCRIPTION};
        Cursor cursor = cr.query(eventUri, null, null, null, null);
        if (cursor.moveToFirst()) {

            int count = 0;
            do {
                int eventDateIndex = cursor.getColumnIndex(projection[1]);
                int idCol = cursor.getColumnIndex(projection[0]);
                int descIndex = cursor.getColumnIndex(projection[2]);
                long eventDateInMillis = cursor.getLong(eventDateIndex);
                String desc = cursor.getString(descIndex);
                if (desc != null && desc.endsWith("Added by Legion")) {
                    String eventDate = getDate(eventDateInMillis, "yyyy-MM-dd");
                    if (eventDate.equalsIgnoreCase(date)) {
                        Log.d("event Matched", "" + eventDate);
                        deleteEvent(activity, eventUri, calendarId);
                    }
                }

            } while (cursor.moveToNext());
            Log.d("count", "" + count);
        }
        cursor.close();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private static void deleteEvent(Activity activity, Uri eventsUri, int calendarId) {
        ContentResolver cr = activity.getContentResolver();
        Cursor cursor = cr.query(eventsUri, new String[]{"_id"}, "calendar_id=" + calendarId, null, null);
        while (cursor.moveToNext()) {
            long eventId = cursor.getLong(cursor.getColumnIndex("_id"));
            cr.delete(ContentUris.withAppendedId(eventsUri, eventId), null, null);
        }
        cursor.close();
    }


}