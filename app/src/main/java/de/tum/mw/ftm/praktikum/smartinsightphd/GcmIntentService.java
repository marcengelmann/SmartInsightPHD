package de.tum.mw.ftm.praktikum.smartinsightphd;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by marcengelmann on 12.01.16.
 */
public class GcmIntentService extends IntentService {

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Logger.getLogger("GCM_RECEIVED").log(Level.INFO, extras.toString());

                Intent notificationIntent = new Intent(this, MainActivity.class);

                PendingIntent backToAppintent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

                Notification notification = new NotificationCompat.Builder(this)
                        .setCategory(Notification.CATEGORY_PROMO)
                        .setContentTitle(extras.getString("title"))
                        .setContentText(extras.getString("message"))
                        .setSmallIcon(R.drawable.ic_action_user)
                        .setAutoCancel(true)
                        .setVisibility(1)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(extras.getString("message")))
                        .addAction(android.R.drawable.ic_menu_view, "Details anzeigen ...", backToAppintent)
                        .setContentIntent(backToAppintent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).build();
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                UserLocalStore userLocalStore = new UserLocalStore(this);
                if(userLocalStore.getUserLoggedIn()) {
                    notificationManager.notify(0, notification);
                } else {
                    System.out.println("User is not logged in anymore!");
                }
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
