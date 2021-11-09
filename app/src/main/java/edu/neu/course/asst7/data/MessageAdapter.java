package edu.neu.course.asst7.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import edu.neu.course.asst7.R;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder>{
    private final ArrayList<MessageCard> itemList;

    //Constructor
    public MessageAdapter(ArrayList<MessageCard> itemList) {
        this.itemList = itemList;
    }


    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card, parent, false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        MessageCard currentItem = itemList.get(position);

        holder.stickerSent.setImageResource(currentItem.getStickerSent());
        holder.sender.setText(currentItem.getSender());
        holder.timeSent.setText(currentItem.getTime());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
