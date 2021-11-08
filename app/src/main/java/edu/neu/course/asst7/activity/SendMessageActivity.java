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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.neu.course.asst7.R;
import edu.neu.course.asst7.Utils;
import edu.neu.course.asst7.data.Sticker;
import edu.neu.course.asst7.data.User;

public class SendMessageActivity extends AppCompatActivity {

    private final String TAG = "SendMessageActivity";
    private static String SERVER_KEY;

    private Map<String, User> users = new HashMap<>();
    private Map<String, Sticker> stickers = new HashMap<>();

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
        Thread userThread = new Thread(() -> getUsers(databaseReference));
        Thread stickerThread = new Thread(() -> getStickers(databaseReference));
        userThread.start();
        stickerThread.start();
    }

    private void getUsers(DatabaseReference databaseReference) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users = new HashMap<>();
                DataSnapshot usersSnapshot = snapshot.child("Users");
                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        users.put(user.username, user);
                    }
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
                stickers = new HashMap<>();
                DataSnapshot stickersSnapshot = snapshot.child("Images");
                for (DataSnapshot stickerSnapshot : stickersSnapshot.getChildren()) {
                    Sticker sticker = stickerSnapshot.getValue(Sticker.class);
                    if (sticker != null) {
                        downloadImage(stickerSnapshot.getKey());
                        stickers.put(sticker.name, sticker);
                    }
                }
            }

            private void downloadImage(String id) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images");
                DatabaseReference image = databaseReference.child(id).child("location");

                image.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String url = snapshot.getValue(String.class);
                        Log.i(TAG, url);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i(TAG, "Cancelled image download " + error.getMessage());
                    }
                });
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
        if (users == null) {
            Log.i(TAG, "USERS ARE NULL");
            return;
        }
        // TODO: select token based on the user's selected option
        User recepient = new User();
        recepient.username = "test";
        recepient.token = "eUkqfxoLR0SiiLn0UvMYnv:APA91bGScCLCQZ8ZPFfZlbLAss7DOlCbgzdgySFN-0iob2xgUqoBOe4nYwsBwMN12_5D6V9yw27hgsUBE1u5SK_Q2rnPIfvSAV45_kT_pNXozWGsvfEDalZmOdxUiWbYkwVow1W8MHBr";
        int stickerId = 0; // TODO: get the sticker id from user's selection

        new Thread(() -> sendMessageToDevice(recepient, stickerId)).start();
    }

    private void sendMessageToDevice(User recipient, int stickerId) {
        // Prepare data
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            // Foreground
            jNotification.put("title", "From " + getSender());
            jNotification.put("body", stickerId);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");

            // Background
            jdata.put("title", "From " + getSender());
            jdata.put("content", stickerId);
            jdata.put("sender", getSender());
            jdata.put("receipent", recipient.username);

            // To whom
            jPayload.put("to", recipient.token);

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String resp = Utils.fcmHttpConnection(SERVER_KEY, jPayload);
        Utils.postToastMessage("Status from Server: " + resp, getApplicationContext());
    }

    private String getSender() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString("sender");
        }

        return "ERROR";
    }
}