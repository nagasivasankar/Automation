package recievers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.IntentCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import co.legion.client.R;
import co.legion.client.activities.HomeActivity;
import helpers.Legion_PrefsManager;
import models.NotificationType;
import utils.Legion_Constants;

public class FCMMessageReceiverService extends FirebaseMessagingService implements Legion_Constants {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(remoteMessage.getNotification().getBody(), new JSONObject(remoteMessage.getData()));
    }

    private void sendNotification(String messageBody, JSONObject jsonObject) {
        try {
            if (jsonObject == null) {
                return;
            }
            Log.v("FCM", jsonObject.toString());

            Legion_PrefsManager prefsManager = new Legion_PrefsManager(getApplicationContext());
            if (prefsManager.hasKey(Prefs_Keys.IS_LOGGED_IN)) {
                if (new Legion_PrefsManager(getApplicationContext()).get(Prefs_Keys.IS_LOGGED_IN).equalsIgnoreCase("1")) {
                    String type = jsonObject.optString("type", null);
                    String businessId = jsonObject.optString("business_id", null);
                    String weekStartDayOfTheYear = jsonObject.optString("weekStartDayOfTheYear", null);
                    String year = jsonObject.optString("year", null);
                    String offerId = jsonObject.optString("offerId", null);
                    String shiftId = jsonObject.optString("shiftId", null);

                    Log.v("FCM1", "type = " + type + ", businessId = " + businessId + ", weekStartDayOfTheYear = " + weekStartDayOfTheYear + ", year = " + year + ", offerId = " + offerId+ ", shiftId = " + shiftId);

                    Intent resultIntent = new Intent(this, HomeActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    resultIntent.putExtra(Extras_Keys.TYPE, type);
                    resultIntent.putExtra(Extras_Keys.COMING_FROM_NOTIFICATION, true);
                    if (type.equalsIgnoreCase(NotificationType.SCHEDULE_PUBLISH.getType()) || type.equalsIgnoreCase(NotificationType.SCHEDULE_UPDATE.getType()) || type.equalsIgnoreCase(NotificationType.SCHEDULE_FINALIZED.getType()) || type.equalsIgnoreCase(NotificationType.SCHEDULE_UPDATED.getType())) {
                        resultIntent.putExtra(Extras_Keys.NOTIFICATION_BUSINESS_ID, businessId);
                        resultIntent.putExtra(Extras_Keys.NOTIFICATION_YEAR, year);
                        resultIntent.putExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR, weekStartDayOfTheYear);
                    } else if (type.equalsIgnoreCase(NotificationType.SHIFT_OFFER.getType()) && offerId == null) {
                        resultIntent.putExtra(Extras_Keys.NOTIFICATION_YEAR, year);
                        resultIntent.putExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR, weekStartDayOfTheYear);
                    } else if (type.equalsIgnoreCase(NotificationType.SHIFT_OFFER.getType()) && offerId != null) {
                        resultIntent.putExtra(Extras_Keys.OFFER_ID, offerId);
                    }else if(type.equalsIgnoreCase(NotificationType.SHIFT_UPDATED.getType())){
                        resultIntent.putExtra(Extras_Keys.NOTIFICATION_BUSINESS_ID, businessId);
                        resultIntent.putExtra(Extras_Keys.NOTIFICATION_YEAR, year);
                        resultIntent.putExtra(Extras_Keys.NOTIFICATION_WEEKSTARTDAYOFTHEYEAR, weekStartDayOfTheYear);
                        resultIntent.putExtra(Extras_Keys.NOTIFICATION_SHIFTID, shiftId);
                    }
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notification = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.notification_small_icon)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.legion_stage_launcher))
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setCategory(Notification.CATEGORY_PROMO)
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                            .setContentTitle("Legion")
                            .setContentText(messageBody)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setContentIntent(contentIntent)
                            .build();
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify((int) System.currentTimeMillis(), notification);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}