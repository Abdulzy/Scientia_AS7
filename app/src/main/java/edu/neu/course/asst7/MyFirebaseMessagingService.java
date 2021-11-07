package edu.neu.course.asst7;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final static String TAG = "MyFirebaseMessagingService";


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d(TAG, "Refreshed token: " + s);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();

        Log.d(TAG, "Deleted message");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

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
