package com.arimdor.sharednotes.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.arimdor.sharednotes.R;
import com.arimdor.sharednotes.ui.login.LoginActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Not getting messages here? See why this may be: {ENLACE ELIMINADO}
        Log.d("amd", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("amd", "Message data payload: " + remoteMessage.getData());
            showNotification(remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("amd", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void showNotification(Map<String, String> data) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String idChannel = "my_channel_01";
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        // The id of the channel.

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, null);
        builder.setContentTitle(this.getString(R.string.app_name))
                .setSmallIcon(R.drawable.logo_splash)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_splash))
                .setContentIntent(pendingIntent)
                .setContentText(data.get("message"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("message")))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(idChannel, this.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription("Notificaciones de Shared Notes");
            mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        } else {
            builder.setContentTitle(this.getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setSmallIcon(R.drawable.logo_splash)
                    .setVibrate(new long[]{100, 250})
                    .setLights(Color.BLUE, 500, 5000);
        }
        builder.setChannelId(idChannel);

        if (mNotificationManager != null) {
            mNotificationManager.notify(1, builder.build());
        }

    }
}
