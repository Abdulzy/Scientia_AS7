package edu.neu.course.asst7.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

import edu.neu.course.asst7.R;
import edu.neu.course.asst7.Utils;
import edu.neu.course.asst7.data.Sticker;
import edu.neu.course.asst7.data.User;

public class SendMessageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = "SendMessageActivity";
    private static String SERVER_KEY;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Spinner spinner;
    private ArrayList spinnerList;
    private ArrayAdapter<String> adapter;


    private Map<String, User> users = new HashMap<>();
    private Map<String, Sticker> stickers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        spinner = findViewById(R.id.spinner_id);

        SERVER_KEY = "key=" + Utils.getProperties(getApplicationContext()).getProperty("SERVER_KEY");

        createNotificationChannel();
        getData();
        spinnerList = new ArrayList<>();
        adapter = new ArrayAdapter<>(SendMessageActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        adapter.notifyDataSetChanged();

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
                spinnerList.addAll(users.keySet());

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
                        stickers.put(sticker.name, sticker);
                    }
                }

                Log.i(TAG, "USERS COUNT " + users.size());
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
        Log.i(TAG, "SENDING MESSAGE to " + "Josh");
        // TODO: select token based on the user's selected option
        String recipientToken = "eUkqfxoLR0SiiLn0UvMYnv:APA91bGScCLCQZ8ZPFfZlbLAss7DOlCbgzdgySFN-0iob2xgUqoBOe4nYwsBwMN12_5D6V9yw27hgsUBE1u5SK_Q2rnPIfvSAV45_kT_pNXozWGsvfEDalZmOdxUiWbYkwVow1W8MHBr";
        int stickerId = 0; // TODO: get the sticker id from user's selection

        new Thread(() -> sendMessageToDevice(recipientToken, stickerId)).start();
    }

    private void sendMessageToDevice(String recipientToken, int stickerId) {
        // Prepare data
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        // TODO: change given example to image
        try {
            jNotification.put("title", "Sticker id is " + stickerId);
            jNotification.put("body", "Message body");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");

            jdata.put("title", "data title from 'SEND MESSAGE TO CLIENT BUTTON'");
            jdata.put("content", "data content from 'SEND MESSAGE TO CLIENT BUTTON'");
            jdata.put("sender", getSender());

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

    private String getSender() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            return extras.getString("sender");
        }

        return "ERROR";
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}