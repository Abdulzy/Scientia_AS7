package edu.neu.course.asst7.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.neu.course.asst7.R;
import edu.neu.course.asst7.Utils;
import edu.neu.course.asst7.data.MessageAdapter;
import edu.neu.course.asst7.data.MessageCard;

public class History extends AppCompatActivity {



    private RecyclerView recyclerView;
    private MessageAdapter rViewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private String curr_InstanceID;
    private static Map<String, Integer> stickers ;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Bundle extra = getIntent().getExtras();
        curr_InstanceID = extra.getString("sender");
        stickers = Utils.getStickersMap();
        createRecyclerView();
        getUserHistoryList(curr_InstanceID);
    }
    private void createRecyclerView() {


        rLayoutManger = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(rLayoutManger);


    }

    private void getUserHistoryList(String instanceId){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://assignment7-be4aa-default-rtdb.firebaseio.com/");
        DatabaseReference userHistory = database.getReference().child("Users").child(instanceId).child("history");


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> times = new ArrayList<>();
                ArrayList<MessageCard> itemList = new ArrayList<>();
                for (DataSnapshot d: snapshot.getChildren() ) {
                    times.add(d.getKey());
                }
                for (String time: times) {
                    DataSnapshot current_val = snapshot.child(time);
                    String stick = current_val.child("sticker").getValue(String.class);
                    String send = current_val.child("senderName").getValue(String.class);
                    if(stick != null){
                        MessageCard item1 = new MessageCard(stickers.get(stick), send, time);
                        itemList.add(0,item1);
                    }

                }
                rViewAdapter = new MessageAdapter(itemList);
                recyclerView.setAdapter(rViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        };

        userHistory.addValueEventListener(eventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }





}