package edu.neu.course.asst7.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import edu.neu.course.asst7.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        // TODO: send token to database???
        Log.d(TAG, "Refreshed token: " + s);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();

        Log.d(TAG, "Deleted message");
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        showNotification(remoteMessage);
    }

    private void showNotification(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            String channelId = getString(R.string.channel_id);
            String channelName = getString(R.string.channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));

            NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.small_icon_foreground)
                    .setContentTitle("New sticker from " + remoteMessage.getFrom())
                    .setContentText(remoteMessage.getNotification().getTitle())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            notificationManager.notify(0, notifyBuild.build());
        }
    }
}
