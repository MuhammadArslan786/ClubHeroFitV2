package com.arslan6015.clubherofitv2.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.arslan6015.clubherofitv2.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView message_profile_image;
    public TextView receiver_message_text, sender_message_text;
    public ImageView message_sender_picture, message_receiver_picture;


    public MessageViewHolder(@NonNull View itemView)
    {
        super(itemView);
        message_profile_image = itemView.findViewById(R.id.message_profile_image);
        receiver_message_text = itemView.findViewById(R.id.receiver_message_text);
        sender_message_text = itemView.findViewById(R.id.sender_message_text);
        message_sender_picture = itemView.findViewById(R.id.message_sender_imageView);
        message_receiver_picture = itemView.findViewById(R.id.message_receiver_imageView);
    }
}
