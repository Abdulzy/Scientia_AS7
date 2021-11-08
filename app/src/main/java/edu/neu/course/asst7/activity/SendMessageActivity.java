package edu.neu.course.asst7.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
                    String filePath = getApplicationContext().getDir("img", Context.MODE_PRIVATE) +  "/" + sticker.name + ".png";
                    sticker.localFilePath = filePath;
                    if (sticker != null) {
                        downloadImage(stickerSnapshot.getKey(), filePath);
                        stickers.put(sticker.name, sticker);
                    }
                }
            }

            private void downloadImage(String id, String filePath) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Images").child(id);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                        Map<String, String> map = snapshot.getValue(genericTypeIndicator );
                        String url = map.get("token");
                        File myImageFile = new File(filePath);
                        download(myImageFile, url);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i(TAG, "Cancelled image download " + error.getMessage());
                    }
                });
            }

            private void download(File file, String url) {
                Picasso.get().load(url).into(getTarget(file));
            }

            private Target getTarget(File myImageFile) {
                return new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                Log.i(TAG, myImageFile.getAbsolutePath());
                            }
                        }
                        Log.i(TAG, "IMAGE SAVED TO " + myImageFile.getAbsolutePath());
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Log.i(TAG, "Bitmap load failed: " + e.getMessage() );
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Log.i(TAG, "Bitmap prepared to load ");
                    }
                };
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