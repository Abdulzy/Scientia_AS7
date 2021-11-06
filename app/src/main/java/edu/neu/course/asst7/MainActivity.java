package edu.neu.course.asst7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.CharArrayWriter;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private Button login_button;
    private DatabaseReference reference;
    private String un;
    private CharArrayWriter e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reference = FirebaseDatabase.getInstance().getReference("credentials");
        username = findViewById(R.id.username_id);
        login_button = findViewById(R.id.login_id);

    }

    public void signin(View view) {

        createAccount(view);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void createAccount(View view) {
        un = username.getText().toString().toLowerCase();
        reference.child("Users").child("username").setValue(un).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "Data has been saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}