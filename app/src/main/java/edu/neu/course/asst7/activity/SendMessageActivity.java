package edu.neu.course.asst7.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import edu.neu.course.asst7.data.SentStickersCount;
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
    private DatabaseReference mDatabase;
    private SentStickersCount sticker1;
    private SentStickersCount sticker2;
    private SentStickersCount sticker3;
    private SentStickersCount sticker4;
    private int stickerTotalCount;
    private EditText text1;
    private EditText text2;
    private EditText text3;
    private EditText text4;
    private EditText totalCountValue;
    private User receiver;
    private String receiverToken;
    private String stickerId;

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
        sticker1 = new SentStickersCount(1);
        sticker2 = new SentStickersCount(2);
        sticker3 = new SentStickersCount(3);
        sticker4 = new SentStickersCount(4);
        text1 = findViewById(R.id.sticker1Score);
        text2 = findViewById(R.id.sticker2Score);
        text3 = findViewById(R.id.sticker3Score);
        text4 = findViewById(R.id.sticker4Score);
        totalCountValue = findViewById(R.id.totalStickercountValue);
        Intent intent = getIntent();
        String senderUser = intent.getStringExtra("sender");
        String senderToken = intent.getStringExtra("token");

        SERVER_KEY = "key=" + Utils.getProperties(getApplicationContext()).getProperty("SERVER_KEY");
        createNotificationChannel();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        getData();
        spinnerList = new ArrayList<>();
        adapter = new ArrayAdapter<>(SendMessageActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = sticker1.incrementcount();
                stickerTotalCount = stickerTotalCount + 1;
                text1.setText(String.valueOf(count));
                totalCountValue.setText(String.valueOf(stickerTotalCount));
                mDatabase.child(senderUser).child("sticker1").setValue(sticker1);
                mDatabase.child(senderUser).child("TotalStickerCount").setValue(stickerTotalCount);
                stickerId = String.valueOf(sticker1.getStickerId());
                sendMessageToDevice(view);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = sticker2.incrementcount();
                stickerTotalCount = stickerTotalCount + 1;
                totalCountValue.setText(String.valueOf(stickerTotalCount));
                text2.setText(String.valueOf(count));
                mDatabase.child(senderUser).child("sticker2").setValue(sticker2);
                mDatabase.child(senderUser).child("TotalStickerCount").setValue(stickerTotalCount);
                stickerId = String.valueOf(sticker2.getStickerId());

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = sticker3.incrementcount();
                stickerTotalCount = stickerTotalCount + 1;
                totalCountValue.setText(String.valueOf(stickerTotalCount));
                text3.setText(String.valueOf(count));
                mDatabase.child(senderUser).child("sticker3").setValue(sticker3);
                mDatabase.child(senderUser).child("TotalStickerCount").setValue(stickerTotalCount);
                stickerId = String.valueOf(sticker3.getStickerId());

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = sticker4.incrementcount();
                stickerTotalCount = stickerTotalCount + 1;
                totalCountValue.setText(String.valueOf(stickerTotalCount));
                text4.setText(String.valueOf(count));
                mDatabase.child(senderUser).child("sticker4").setValue(sticker4);
                mDatabase.child(senderUser).child("TotalStickerCount").setValue(stickerTotalCount);
                stickerId = String.valueOf(sticker4.getStickerId());


            }
        });
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
                if (users.containsKey(null)) {
                    users.remove(null);
                }
                spinnerList.addAll(users.keySet());
                adapter.notifyDataSetChanged();
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
        // TODO: select token based on the user's selected option
        User recepient = receiver;
        recepient.username = recepient.getUsername();
        recepient.token = receiverToken;
        // TODO: get the sticker id from user's selection

        new Thread(() -> sendMessageToDevice(recepient, stickerId)).start();
    }

    private void sendMessageToDevice(User recipient, String stickerName) {
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
            jdata.put("content", stickerName);
            jdata.put("sender", getSender());

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
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getItemAtPosition(i) != null) {
            String text = adapterView.getItemAtPosition(i).toString();
            receiver = users.get(text);
            receiverToken = receiver.getToken();
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}