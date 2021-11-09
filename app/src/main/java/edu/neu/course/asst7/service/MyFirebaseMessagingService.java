package edu.neu.course.asst7.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import edu.neu.course.asst7.R;
import edu.neu.course.asst7.Utils;
import edu.neu.course.asst7.activity.ReceivedNotificationActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static Map<String, Integer> stickers = Utils.getStickersMap();

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
        Log.d(TAG, "FROM: " + remoteMessage.getData().get("sender"));

        showNotification(remoteMessage);
    }

    private void showNotification(@NonNull RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, ReceivedNotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        if (remoteMessage.getNotification() != null) {
            String stickerName = remoteMessage.getData().get("content");

            String channelId = getString(R.string.channel_id);
            String channelName = getString(R.string.channel_name);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT));

            Log.i(TAG, stickerName);
            NotificationCompat.Builder notifyBuild = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.small_icon_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), stickers.get(stickerName)))
                    .setContentTitle("New sticker from " + remoteMessage.getData().get("sender"))
                    .setContentText(stickerName)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            notificationManager.notify(0, notifyBuild.build());
        }
    }
}
