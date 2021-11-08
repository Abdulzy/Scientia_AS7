package edu.neu.course.asst7.data;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.neu.course.asst7.R;
import androidx.recyclerview.widget.RecyclerView;

public class MessageHolder extends RecyclerView.ViewHolder {


    public ImageView stickerSent;
    public TextView sender;
    public TextView timeSent;

    public MessageHolder(View itemView) {
        super(itemView);
        stickerSent = itemView.findViewById(R.id.message_sticker);
        sender = itemView.findViewById(R.id.message_sender);
        timeSent = itemView.findViewById(R.id.message_time);


    }
}
