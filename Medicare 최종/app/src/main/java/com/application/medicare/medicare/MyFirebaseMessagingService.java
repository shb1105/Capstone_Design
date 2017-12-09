package com.application.medicare.medicare;

/**
 * Created by user1 on 2017-11-07.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.application.medicare.medicare.patient.PatientListActivity;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // Check if message contains a notification payload.
       // Log.d(TAG, "MessageBody: " + remoteMessage.getNotification().getBody());
       // Log.d(TAG, "MessageTitle: " + remoteMessage.getNotification().getTitle());
        //복용한 환자 이름
        String patient = remoteMessage.getNotification().getTitle();
        //추가한것
        sendNotification(remoteMessage.getData().get("message"));
    }

    private void sendNotification(String messageBody) {

        Intent intent = new Intent(this, PatientListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("FCM Push Test")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}