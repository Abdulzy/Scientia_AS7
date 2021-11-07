package edu.neu.course.asst7.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.neu.course.asst7.R;
import edu.neu.course.asst7.Utils;
import edu.neu.course.asst7.data.Sticker;
import edu.neu.course.asst7.data.User;

public class SendMessageActivity extends AppCompatActivity {

    private final String TAG = "SendMessageActivity";
    private static String SERVER_KEY;

    private List<User> users;
    private List<Sticker> stickers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);

        SERVER_KEY = "key=" + Utils.getProperties(getApplicationContext()).getProperty("SERVER_KEY");

        createNotificationChannel();
        getData();
    }

    private void getData() {
        this.users = new LinkedList<>(); // TODO: Get all users from database and display who's available to send messages to
        this.stickers = new ArrayList<>(); // TODO: Get all available images from the database and display what can be sent
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.channel_id),
                    getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.channel_description));

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // TODO: add as event listener to the "Send" button in the view
    public void sendMessageToDevice(View view) {
        // TODO: get the recipient token based on what user is selected
        String recipient = "ejspVuoQQsmZVfQh6xUKFg:APA91bGAUVYHeSgiujvPl9BE9wCsPSFgTPZtu_3JJGpInY_fuBXZEQSLiSEeuNDI1ZkuJ_58oP4JdAnzFVaYV_ZRb0Bt08F6Jsoe3T4-hUBpczbTdqrokgjrkAW0WAve9Ff-JECrun96";
        int stickerId = 0; // TODO: get the sticker id from user's selection

        new Thread(() -> sendMessageToDevice(recipient, stickerId)).start();
    }

    private void sendMessageToDevice(String recipientToken, int stickerId) {
        // Prepare data
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        // TODO: convert to image
        try {
            jNotification.put("title", "Message Title from 'SEND MESSAGE TO CLIENT BUTTON'");
            jNotification.put("body", "Message body from 'SEND MESSAGE TO CLIENT BUTTON'");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");

            jdata.put("title", "data title from 'SEND MESSAGE TO CLIENT BUTTON'");
            jdata.put("content", "data content from 'SEND MESSAGE TO CLIENT BUTTON'");


            // If sending to a single client
            jPayload.put("to", recipientToken);

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String resp = Utils.fcmHttpConnection(SERVER_KEY, jPayload);
        Utils.postToastMessage("Status from Server: " + resp, getApplicationContext());
    }
}