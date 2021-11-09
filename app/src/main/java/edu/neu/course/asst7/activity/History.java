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

import edu.neu.course.asst7.R;
import edu.neu.course.asst7.data.MessageAdapter;
import edu.neu.course.asst7.data.MessageCard;

public class History extends AppCompatActivity {

    private ArrayList<MessageCard> itemList = new ArrayList<>();

    private RecyclerView recyclerView;
    private MessageAdapter rViewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private String curr_InstanceID;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Bundle extra = getIntent().getExtras();
        curr_InstanceID = extra.getString("instanceId");
        getUserHistoryList(curr_InstanceID);

        init(savedInstanceState);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {


        int size = itemList == null ? 0 : itemList.size();
        outState.putInt(NUMBER_OF_ITEMS, size);

        // Need to generate unique key for each item
        // This is only a possible way to do, please find your own way to generate the key
        for (int i = 0; i < size; i++) {
            // put image information id into instance
            outState.putInt(KEY_OF_INSTANCE + i + "0", itemList.get(i).getStickerSent());
            // put itemName information into instance
            outState.putString(KEY_OF_INSTANCE + i + "1", itemList.get(i).getSender());
            // put itemDesc information into instance
            outState.putString(KEY_OF_INSTANCE + i + "2", itemList.get(i).getTime());
        }
        super.onSaveInstanceState(outState);

    }


    private void init(Bundle savedInstanceState) {

        initialItemData(savedInstanceState);
        createRecyclerView();
    }

    private void initialItemData(Bundle savedInstanceState) {

        // Not the first time to open this Activity
        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
            if (itemList == null || itemList.size() == 0) {

                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);

                // Retrieve keys we stored in the instance
                for (int i = 0; i < size; i++) {
                    Integer imgId = savedInstanceState.getInt(KEY_OF_INSTANCE + i + "0");
                    String itemName = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    String itemDesc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "2");

                    // We need to make sure names such as "XXX(checked)" will not duplicate
                    // Use a tricky way to solve this problem, not the best though
                    MessageCard itemCard = new MessageCard(imgId, itemName, itemDesc);

                    itemList.add(itemCard);
                }
            }
        }
        else {
            MessageCard item1 = new MessageCard(R.drawable.pic_gmail_01, "Gmail", "Example description");
            MessageCard item2 = new MessageCard(R.drawable.pic_google_01, "Google", "Example description");
            itemList.add(item1);
            itemList.add(item2);
        }

    }

    private void getUserHistoryList(String instanceId){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://assignment7-be4aa-default-rtdb.firebaseio.com/");
        DatabaseReference userHistory = database.getReference().child("User").child(instanceId).child("record");


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> times = new ArrayList<>();
                for (DataSnapshot d: snapshot.getChildren() ) {
                    times.add(d.getKey());
                }
                for (String time: times) {
                    DataSnapshot current_val = snapshot.child(time);
                    MessageCard item1 = new MessageCard(R.drawable.pic_gmail_01, current_val.child("senderName").getValue(String.class), time);
                    itemList.add(item1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, error.getMessage());
            }
        };

        userHistory.addListenerForSingleValueEvent(eventListener);
    }

    private void createRecyclerView() {


        rLayoutManger = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        rViewAdapter = new MessageAdapter(itemList);


        recyclerView.setAdapter(rViewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);


    }
}