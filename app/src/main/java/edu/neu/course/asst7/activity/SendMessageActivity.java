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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Objects;

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
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private TextView welcome;
    private String currentUser;
    private String total;

    private Map<String, User> users = new HashMap<>();
    private Map<String, Sticker> stickers = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmessage);
        Bundle extra = getIntent().getExtras();
        currentUser = extra.getString("sender");
        welcome = findViewById(R.id.welcomeMessage_id);
        welcome.setText( currentUser + ",Welcome !!!!");
        image1 = findViewById(R.id.imageViewAngry);
        image2 = findViewById(R.id.imageViewCool);
        image3 = findViewById(R.id.imageViewCry);
        image4 = findViewById(R.id.imageViewJoy);

        spinner = findViewById(R.id.spinner_id);
        sticker1 = new SentStickersCount("anger");
        sticker2 = new SentStickersCount("cool");
        sticker3 = new SentStickersCount("cry");
        sticker4 = new SentStickersCount("joy");
        getCount(currentUser);
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

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = sticker1.incrementcount();
                stickerTotalCount = stickerTotalCount + 1;
                text1.setText(String.valueOf(count));
                totalCountValue.setText(String.valueOf(stickerTotalCount));
                mDatabase.child(senderUser).child("sticker1").setValue(sticker1);
                mDatabase.child(senderUser).child("TotalStickerCount").setValue(stickerTotalCount);
                stickerId = sticker1.getStickerId();
                sendMessageToDevice(view);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = sticker2.incrementcount();
                stickerTotalCount = stickerTotalCount + 1;
                totalCountValue.setText(String.valueOf(stickerTotalCount));
                text2.setText(String.valueOf(count));
                mDatabase.child(senderUser).child("sticker2").setValue(sticker2);
                mDatabase.child(senderUser).child("TotalStickerCount").setValue(stickerTotalCount);
                stickerId = sticker2.getStickerId();
                sendMessageToDevice(view);

            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = sticker3.incrementcount();
                stickerTotalCount = stickerTotalCount + 1;
                totalCountValue.setText(String.valueOf(stickerTotalCount));
                text3.setText(String.valueOf(count));
                mDatabase.child(senderUser).child("sticker3").setValue(sticker3);
                mDatabase.child(senderUser).child("TotalStickerCount").setValue(stickerTotalCount);
                stickerId = sticker3.getStickerId();
                sendMessageToDevice(view);

            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = sticker4.incrementcount();
                stickerTotalCount = stickerTotalCount + 1;
                totalCountValue.setText(String.valueOf(stickerTotalCount));
                text4.setText(String.valueOf(count));
                mDatabase.child(senderUser).child("sticker4").setValue(sticker4);
                mDatabase.child(senderUser).child("TotalStickerCount").setValue(stickerTotalCount);
                stickerId = sticker4.getStickerId();
                sendMessageToDevice(view);
            }
        });
    }

    public void getCount(String user) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference Sticker1 = database.getReference().child("Users").child(user).child("sticker1");
        DatabaseReference Sticker2 = database.getReference().child("Users").child(user).child("sticker2");
        DatabaseReference Sticker3 = database.getReference().child("Users").child(user).child("sticker3");
        DatabaseReference Sticker4 = database.getReference().child("Users").child(user).child("sticker4");





        Sticker1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("count")) {
                    stickerTotalCount = stickerTotalCount - sticker1.getCount();
                    sticker1.setCount((snapshot.child("count").getValue(Integer.class)));
                    stickerTotalCount =  stickerTotalCount + sticker1.getCount();
                    text1.setText(String.valueOf(sticker1.getCount()));
                    totalCountValue.setText(String.valueOf(stickerTotalCount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        Sticker2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("count")) {
                    stickerTotalCount = stickerTotalCount - sticker2.getCount();
                    sticker2.setCount((snapshot.child("count").getValue(Integer.class)));
                    stickerTotalCount =  stickerTotalCount + sticker2.getCount();
                    text2.setText(String.valueOf(sticker2.getCount()));
                    totalCountValue.setText(String.valueOf(stickerTotalCount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        Sticker3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("count")) {
                    stickerTotalCount = stickerTotalCount - sticker3.getCount();
                    sticker3.setCount((snapshot.child("count").getValue(Integer.class)));
                    stickerTotalCount =  stickerTotalCount + sticker3.getCount();
                    text3.setText(String.valueOf(sticker3.getCount()));
                    totalCountValue.setText(String.valueOf(stickerTotalCount));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        Sticker4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("count")) {
                    stickerTotalCount = stickerTotalCount - sticker4.getCount();
                    sticker4.setCount((snapshot.child("count").getValue(Integer.class)));
                    stickerTotalCount =  stickerTotalCount + sticker4.getCount();
                    text4.setText(String.valueOf(sticker4.getCount()));
                    totalCountValue.setText(String.valueOf(stickerTotalCount));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    public void history(View view) {
        Intent intent = new Intent(this, History.class);
        intent.putExtra("sender", currentUser);
        startActivity(intent);
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
        recepient.setUsername(receiver.getUsername());
        recepient.setToken(receiver.getToken());
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
            jdata.put("receiver", recipient.getUsername());

            // To whom
            jPayload.put("to", recipient.token);

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String resp = Utils.fcmHttpConnection(SERVER_KEY, jPayload);
//        Utils.postToastMessage("Status from Server: " + resp, getApplicationContext());
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
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}