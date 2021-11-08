package edu.neu.course.asst7.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.neu.course.asst7.R;
import edu.neu.course.asst7.Utils;
import edu.neu.course.asst7.data.Sticker;
import edu.neu.course.asst7.data.User;

public class SendMessageActivity extends AppCompatActivity {

    private final String TAG = "SendMessageActivity";
    private static String SERVER_KEY;

    private Set<User> users;
    private Set<Sticker> stickers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);

        SERVER_KEY = "key=" + Utils.getProperties(getApplicationContext()).getProperty("SERVER_KEY");

        createNotificationChannel();
        getData();
    }

    private void getData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        getUsers(databaseReference);
        getStickers(databaseReference);
    }

    private void getUsers(DatabaseReference databaseReference) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = new HashSet<>();
                DataSnapshot usersSnapshot = snapshot.child("Users");
                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    users.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, error.getMessage());
            }
        };

        databaseReference.addValueEventListener(valueEventListener);
    }

    private void getStickers(DatabaseReference databaseReference) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stickers = new HashSet<>();
                DataSnapshot stickersSnapshot = snapshot.child("Images");
                for (DataSnapshot stickerSnapshot : stickersSnapshot.getChildren()) {
                    Sticker sticker = stickerSnapshot.getValue(Sticker.class);
                    stickers.add(sticker);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, error.toString());
            }
        };

        databaseReference.addValueEventListener(valueEventListener);
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
        // TODO: change given example to image
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