package edu.neu.course.asst7.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.neu.course.asst7.R;
import edu.neu.course.asst7.data.User;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private EditText username;
    private DatabaseReference reference;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    token = task.getResult();

                    // Log and toast
                    String msg = getString(R.string.msg_token_fmt, token);
                    Log.d(TAG, msg);
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                });

        reference = FirebaseDatabase.getInstance().getReference("Users");
        username = findViewById(R.id.username_id);

    }

    public void signin(View view) {

        createAccount(view);
        Intent intent = new Intent(MainActivity.this, SendMessageActivity.class);
        intent.putExtra("sender", username.getText().toString());
        startActivity(intent);
    }

    public void createAccount(View view) {
        String userNameValue = username.getText().toString();
        User user = new User(userNameValue, token);

        // TODO: Functionality add on: check if user with such name already exists or we override it

        reference.child(userNameValue)
                .setValue(user)
                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(unused -> Toast.makeText(MainActivity.this, "Data has been saved", Toast.LENGTH_SHORT).show());
    }
}