package com.arslan6015.clubherofitv2.Adapters;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitv2.ImageViewerActivity;
import com.arslan6015.clubherofitv2.Model.MessageModel;
import com.arslan6015.clubherofitv2.R;
import com.arslan6015.clubherofitv2.ViewHolder.MessageViewHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<MessageModel> userMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, RootRef;

    public MessageAdapter(List<MessageModel> userMessageList) {
        this.userMessageList = userMessageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_layout,parent,false);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, final int position)
    {
        String messageSenderId = mAuth.getCurrentUser().getUid();
        MessageModel messages  = userMessageList.get(position);

        String fromUserId = messages.getFrom();
        String fromMessageType = messages.getType();

        RootRef = FirebaseDatabase.getInstance().getReference();

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild("image"))
                {
                    String receiverImage = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(receiverImage).placeholder(R.drawable.maleicon).into(holder.message_profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.receiver_message_text.setVisibility(View.GONE);
        holder.message_profile_image.setVisibility(View.GONE);
        holder.sender_message_text.setVisibility(View.GONE);
        holder.message_sender_picture.setVisibility(View.GONE);
        holder.message_receiver_picture.setVisibility(View.GONE);


        if (fromMessageType.equals("text"))
        {
            if (fromUserId.equals(messageSenderId))
            {

                holder.sender_message_text.setVisibility(View.VISIBLE);

                holder.sender_message_text.setBackgroundResource(R.drawable.sender_layout);
                holder.sender_message_text.setTextColor(Color.BLACK);
                holder.sender_message_text.setText(messages.getMessage() + "\n\n"+ messages.getTime()+ " - "+ messages.getDate());
            }
            else
            {

                holder.receiver_message_text.setVisibility(View.VISIBLE);
                holder.message_profile_image.setVisibility(View.VISIBLE);

                holder.receiver_message_text.setText(messages.getMessage() + "\n\n"+ messages.getTime()+ " - "+ messages.getDate());
                holder.receiver_message_text.setBackgroundResource(R.drawable.receiver_layout);
                holder.receiver_message_text.setTextColor(Color.BLACK);
            }
        }
        else if (fromMessageType.equals("image"))
        {
            if (fromUserId.equals(messageSenderId))
            {
                holder.message_sender_picture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.message_sender_picture);
            }
            else
            {
                holder.message_profile_image.setVisibility(View.VISIBLE);
                holder.message_receiver_picture.setVisibility(View.VISIBLE);

                Picasso.get().load(messages.getMessage()).into(holder.message_receiver_picture);
            }
        }
        else if (fromMessageType.equals("pdf") || fromMessageType.equals("docx"))
        {
            if (fromUserId.equals(messageSenderId))
            {
                holder.message_sender_picture.setVisibility(View.VISIBLE);
                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/dailytennistips.appspot.com/o/file.png?alt=media&token=b0ffd3cc-d242-458f-9fc4-c3a9ab89ecf7")
                        .into(holder.message_sender_picture);
            }
            else
            {
                holder.message_profile_image.setVisibility(View.VISIBLE);
                holder.message_receiver_picture.setVisibility(View.VISIBLE);

                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/dailytennistips.appspot.com/o/file.png?alt=media&token=b0ffd3cc-d242-458f-9fc4-c3a9ab89ecf7")
                        .into(holder.message_receiver_picture);

            }
        }

        if (fromUserId.equals(messageSenderId))
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (userMessageList.get(position).getType().equals("pdf") || userMessageList.get(position).getType().equals("docx") )
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "Download and view the document",
                                        "Cancel",
                                        "Delete for everyone"
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Would you like to!");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0)
                                {
                                    deleteSentMessages(position,holder);
                                }
                                else if (which == 1)
                                {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessageList.get(position).getMessage() ));
                                    holder.itemView.getContext().startActivity(intent);
                                }
                                else if (which == 3)
                                {
                                    deleteMessagesForEveryone(position,holder);
                                }
                            }
                        });
                        builder.show();
                    }
                    else if (userMessageList.get(position).getType().equals("text") )
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "Cancel",
                                        "Delete for everyone"
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Would you like to!");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0)
                                {
                                    deleteSentMessages(position,holder);
                                }
                                else if (which == 2)
                                {
                                    deleteMessagesForEveryone(position,holder);
//                                    Intent intent = new Intent(holder.itemView.getContext(), MainActivity.class);
//                                    holder.itemView.getContext().startActivity(intent);

                                }
                            }
                        });
                        builder.show();
                    }
                    else if (userMessageList.get(position).getType().equals("image") )
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "View this image",
                                        "Cancel",
                                        "Delete for everyone"
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Would you like to!");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0)
                                {
                                    deleteSentMessages(position,holder);
                                }
                                else if (which == 1)
                                {
                                    retriveUserInfo(userMessageList.get(position).getTo(),userMessageList.get(position).getMessage(),holder);
//
//                                    Intent intent = new Intent(holder.itemView.getContext(), ImageViewerActivity.class);
//                                    intent.putExtra("url",userMessageList.get(position).getMessage());
//                                    holder.itemView.getContext().startActivity(intent);
                                }
                                else if (which == 3)
                                {
                                    deleteMessagesForEveryone(position,holder);

                                }
                            }
                        });
                        builder.show();
                    }
                }
            });
        }
        else
        {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (userMessageList.get(position).getType().equals("pdf") || userMessageList.get(position).getType().equals("docx") )
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "Download and view the document",
                                        "Cancel",
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Would you like to!");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0)
                                {
                                    deleteReceiveMessages(position,holder);
                                }
                                else if (which == 1)
                                {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessageList.get(position).getMessage() ));
                                    holder.itemView.getContext().startActivity(intent);
                                }
                            }
                        });
                        builder.show();
                    }
                    else if (userMessageList.get(position).getType().equals("text") )
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "Cancel",
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Would you like to!");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0)
                                {
                                    deleteReceiveMessages(position,holder);
                                }
                            }
                        });
                        builder.show();
                    }
                    else if (userMessageList.get(position).getType().equals("image") )
                    {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Delete for me",
                                        "View this image",
                                        "Cancel",
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("Would you like to!");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which == 0)
                                {
                                    deleteReceiveMessages(position,holder);
                                }
                                else if (which == 1)
                                {
//                                        Intent intent = new Intent(holder.itemView.getContext(), ImageViewerActivity.class);
//                                        intent.putExtra("url",userMessageList.get(position).getMessage());
//                                        holder.itemView.getContext().startActivity(intent);

                                    retriveUserInfo(userMessageList.get(position).getFrom(),userMessageList.get(position).getMessage(),holder);

                                }
                            }
                        });
                        builder.show();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }

    private void deleteSentMessages(final int position, final MessageViewHolder holder)
    {
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Message")
                .child(userMessageList.get(position).getFrom())
                .child(userMessageList.get(position).getTo())
                .child(userMessageList.get(position).getMessageID())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {

                            userMessageList.remove(position);
                            notifyDataSetChanged();

                            Toast.makeText(holder.itemView.getContext(),"Deleted",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void deleteReceiveMessages(final int position, final MessageViewHolder holder)
    {
        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Message")
                .child(userMessageList.get(position).getTo())
                .child(userMessageList.get(position).getFrom())
                .child(userMessageList.get(position).getMessageID())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {

                            userMessageList.remove(position);
                            notifyDataSetChanged();

                            Toast.makeText(holder.itemView.getContext(),"Deleted",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void deleteMessagesForEveryone(final int position, final MessageViewHolder holder)
    {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.child("Message")
                .child(userMessageList.get(position).getTo())
                .child(userMessageList.get(position).getFrom())
                .child(userMessageList.get(position).getMessageID())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            RootRef.child("Message")
                                    .child(userMessageList.get(position).getFrom())
                                    .child(userMessageList.get(position).getTo())
                                    .child(userMessageList.get(position).getMessageID())
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {

                                            userMessageList.remove(position);
                                            notifyDataSetChanged();

                                            Toast.makeText(holder.itemView.getContext(),"Deleted For Everyone",Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });
    }

    private void retriveUserInfo(String toIdFromId, final String url, final MessageViewHolder holder)
    {
        RootRef.child("Users").child(toIdFromId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if ( (dataSnapshot.exists()) && (dataSnapshot.hasChild("name")) && (dataSnapshot.hasChild("image")) )
                {
                    String retriveUserName = dataSnapshot.child("name").getValue().toString();
                    String retriveuid = dataSnapshot.child("uid").getValue().toString();
                    String  retriveProfileImage = dataSnapshot.child("image").getValue().toString();


                    Intent intent = new Intent(holder.itemView.getContext(), ImageViewerActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("PersonId",retriveuid);
                    intent.putExtra("PersonName",retriveUserName);
                    intent.putExtra("PersonImage",retriveProfileImage);

                    holder.itemView.getContext().startActivity(intent);
                }
                else if ( (dataSnapshot.exists())  && (dataSnapshot.hasChild("name")) )
                {
                    String retriveUserName = dataSnapshot.child("name").getValue().toString();
                    String retriveuid = dataSnapshot.child("uid").getValue().toString();
                    String retriveProfileImage = "https://firebasestorage.googleapis.com/v0/b/dailytennistips.appspot.com/o/profile_image.png?alt=media&token=749347b3-9d86-4d82-8af3-3277598b1329";

                    Intent intent = new Intent(holder.itemView.getContext(), ImageViewerActivity.class);
                    intent.putExtra("url",url);
                    intent.putExtra("PersonId",retriveuid);
                    intent.putExtra("PersonName",retriveUserName);
                    intent.putExtra("PersonImage",retriveProfileImage);

                    holder.itemView.getContext().startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
