package com.arslan6015.clubherofitv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arslan6015.clubherofitv2.Adapters.MessageAdapter;
import com.arslan6015.clubherofitv2.Model.MessageModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsActivity extends AppCompatActivity {

    String messageReceiverId, messageReceiverName, messagerReceiverEmail, messageReceiverImage, messageSenderId;
    TextView custom_profile_username, custom_profile_lastseen;
    CircleImageView custom_profile_image;
    Toolbar chatToolbar;
    EditText input_chat_message;
    ImageView chat_send_button, send_files_button;
    DatabaseReference RootRef;
    final List<MessageModel> messageModelList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    MessageAdapter messageAdapter;
    RecyclerView userMessagesListRecycler;
    static boolean active = false;
    String saveCurrentTime, saveCurrentDate;
    String checker = "", myUrl = "", myUrlFiles = "";
    StorageTask uploadTask, uploadTaskFiles;
    Uri fileUri;
    ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        Intent intent = getIntent();
        messageReceiverId = intent.getStringExtra("ChatPersonId");
        messageReceiverName = intent.getStringExtra("ChatPersonName");
        messagerReceiverEmail = intent.getStringExtra("ChatPersonEmail");
        messageReceiverImage = intent.getStringExtra("ChatPersonImage");

        inialization();

        Picasso.get().load(messageReceiverImage).placeholder(R.drawable.maleicon).into(custom_profile_image);
        custom_profile_username.setText(messageReceiverName);
        custom_profile_lastseen.setText("offline");

        chat_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        displayLastSeen();

        send_files_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence option[] = new CharSequence[]
                        {
                                "Images",
                                "PDF Files",
                                "MS Word Files"
                        };
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatsActivity.this);
                builder.setTitle("Select Files: ");
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            checker = "image";

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, 438);
                        }
                        if (which == 1) {
                            checker = "pdf";

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/pdf");
                            startActivityForResult(intent, 438);
                        }
                        if (which == 2) {
                            checker = "docx";

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("application/msword");
                            startActivityForResult(intent, 438);
                        }
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            loadingBar.setTitle("Sending File");
            loadingBar.setMessage("please wait!!! we are sending the file...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            fileUri = data.getData();

            if (!checker.equals("image")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Document Files");

                final String messageSenderRef = "Message/" + messageSenderId + "/" + messageReceiverId;
                final String messageReceiverRef = "Message/" + messageReceiverId + "/" + messageSenderId;

                DatabaseReference userMessageKeyRef = RootRef.child("Message").child(messageSenderId)
                        .child(messageReceiverId).push();
                final String messagePushId = userMessageKeyRef.getKey();

                final StorageReference filePath = storageReference.child(messagePushId + "." + checker);

                uploadTaskFiles = filePath.putFile(fileUri);

                uploadTaskFiles.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri downloadUrl = task.getResult();
                        myUrlFiles = downloadUrl.toString();


                        Map messageImageBody = new HashMap();
                        messageImageBody.put("message", myUrlFiles);
                        messageImageBody.put("name", fileUri.getLastPathSegment());
                        messageImageBody.put("type", checker);
                        messageImageBody.put("from", messageSenderId);
                        messageImageBody.put("to", messageReceiverId);
                        messageImageBody.put("messageID", messagePushId);
                        messageImageBody.put("time", saveCurrentTime);
                        messageImageBody.put("date", saveCurrentDate);

                        Map messageBodyDetails = new HashMap();
                        messageBodyDetails.put(messageSenderRef + "/" + messagePushId, messageImageBody);
                        messageBodyDetails.put(messageReceiverRef + "/" + messagePushId, messageImageBody);

                        RootRef.updateChildren(messageBodyDetails)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            loadingBar.dismiss();
                                            displayToast("Message Sent");
                                            chatsList();
                                        } else {
                                            loadingBar.dismiss();
                                            displayToast("Error");
                                        }
                                    }
                                });
                    }
                });

            } else if (checker.equals("image")) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");

                final String messageSenderRef = "Message/" + messageSenderId + "/" + messageReceiverId;
                final String messageReceiverRef = "Message/" + messageReceiverId + "/" + messageSenderId;

                DatabaseReference userMessageKeyRef = RootRef.child("Message").child(messageSenderId)
                        .child(messageReceiverId).push();
                final String messagePushId = userMessageKeyRef.getKey();

                final StorageReference filePath = storageReference.child(messagePushId + ".jpg");
                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();


                        Map messageImageBody = new HashMap();
                        messageImageBody.put("message", myUrl);
                        messageImageBody.put("name", fileUri.getLastPathSegment());
                        messageImageBody.put("type", checker);
                        messageImageBody.put("from", messageSenderId);
                        messageImageBody.put("to", messageReceiverId);
                        messageImageBody.put("messageID", messagePushId);
                        messageImageBody.put("time", saveCurrentTime);
                        messageImageBody.put("date", saveCurrentDate);

                        Map messageBodyDetails = new HashMap();
                        messageBodyDetails.put(messageSenderRef + "/" + messagePushId, messageImageBody);
                        messageBodyDetails.put(messageReceiverRef + "/" + messagePushId, messageImageBody);

                        RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    loadingBar.dismiss();
                                    displayToast("Message Sent");
                                    chatsList();
                                } else {
                                    loadingBar.dismiss();
                                    displayToast("Error");
                                }
                                input_chat_message.setText("");
                            }
                        });
                    }
                });

            } else {
                loadingBar.dismiss();
                displayToast("Nothing selected! Error");
            }
        }
    }

    private void displayLastSeen() {
        RootRef.child("Users").child(messageReceiverId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("userState").hasChild("state")) {
                            String state = dataSnapshot.child("userState").child("state").getValue().toString();
                            String date = dataSnapshot.child("userState").child("date").getValue().toString();
                            String time = dataSnapshot.child("userState").child("time").getValue().toString();

                            if (state.equals("online")) {
                                custom_profile_lastseen.setText("online");
                            } else if (state.equals("offline")) {
                                custom_profile_lastseen.setText("Last seen: " + date + " " + time);
                            }
                        } else {
                            custom_profile_lastseen.setText("offline");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void inialization() {
        chatToolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(chatToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater Inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = Inflater.inflate(R.layout.custom_chat_bar, null);
        actionBar.setTitle("");
        actionBar.setCustomView(view);

        custom_profile_image = findViewById(R.id.custom_profile_image);
        custom_profile_username = findViewById(R.id.custom_profile_username);
        custom_profile_lastseen = findViewById(R.id.custom_profile_lastseen);
        input_chat_message = findViewById(R.id.input_chat_message);
        chat_send_button = findViewById(R.id.chat_send_button);
        send_files_button = findViewById(R.id.files_send_button);
        messageSenderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        messageAdapter = new MessageAdapter(messageModelList);
        userMessagesListRecycler = findViewById(R.id.recyclerview_chat);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesListRecycler.setLayoutManager(linearLayoutManager);
        userMessagesListRecycler.setAdapter(messageAdapter);

        loadingBar = new ProgressDialog(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        if (messageSenderId != null) {
            updateUserStatus("online");
        }


        RootRef.child("Message").child(messageSenderId).child(messageReceiverId)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                        messageModelList.add(messageModel);
                        messageAdapter.notifyDataSetChanged();
                        //for scrolling
                        userMessagesListRecycler.smoothScrollToPosition(userMessagesListRecycler.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendMessage() {
        String messageText = input_chat_message.getText().toString();
        if (TextUtils.isEmpty(messageText)) {
            displayToast("please enter something");
        } else {
            String messageSenderRef = "Message/" + messageSenderId + "/" + messageReceiverId;
            String messageReceiverRef = "Message/" + messageReceiverId + "/" + messageSenderId;

            DatabaseReference userMessageKeyRef = RootRef.child("Message").child(messageSenderId)
                    .child(messageReceiverId).push();
            String messagePushId = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderId);
            messageTextBody.put("to", messageReceiverId);
            messageTextBody.put("messageID", messagePushId);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushId, messageTextBody);
            messageBodyDetails.put(messageReceiverRef + "/" + messagePushId, messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        displayToast("Message Sent");
                        //here we are creating chat list for chatFragment
                        chatsList();
                    } else {
                        displayToast("Error");
                    }
                    input_chat_message.setText("");
                }
            });
        }
    }

    private void chatsList() {

        String messageSenderRef = "ChatsList/" + messageSenderId + "/" + messageReceiverId;

        DatabaseReference ChatsListRefrence = RootRef.child("ChatsList").child(messageSenderId)
                .child(messageReceiverId);

        Map chatsListBody = new HashMap();
        //in sender refrence we have to pass receiver refrence
        chatsListBody.put("email", messagerReceiverEmail);
        chatsListBody.put("fullName", messageReceiverName);
        chatsListBody.put("id", messageReceiverId);
        chatsListBody.put("image", messageReceiverImage);


        Map chatslistBodyDetailes = new HashMap();
        chatslistBodyDetailes.put(messageSenderRef, chatsListBody);
//        chatslistBodyDetailes.put(messageReceiverRef,chatsListBody);

        RootRef.updateChildren(chatslistBodyDetailes).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Log.e("TAG", "Chats sender list created");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Chats sender list failed");
            }
        });

        CallReceiverRef();

    }

    private void CallReceiverRef() {

        final String messageReceiverRef = "ChatsList/" + messageReceiverId + "/" + messageSenderId;

        //in receiver refrence we have to pass sender information
        DatabaseReference senderRefrence = RootRef.child("UserInfo").child(messageSenderId);
        senderRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String email = snapshot.child("email").getValue().toString();
                    String fullName = snapshot.child("fullName").getValue().toString();
                    String id = snapshot.child("id").getValue().toString();
                    String imagelink = snapshot.child("image").getValue().toString();
                    Log.e("TAGEmail", email);
                    Log.e("TAGFullName", fullName);
                    Log.e("TAGId", id);
                    Log.e("TAGImage", imagelink);

                    Map chatsListBodyReceiver = new HashMap();

                    chatsListBodyReceiver.put("email", email);
                    chatsListBodyReceiver.put("fullName", fullName);
                    chatsListBodyReceiver.put("id", messageSenderId);
                    chatsListBodyReceiver.put("image", imagelink);


                    Map chatslistBodyReceiverDetailes = new HashMap();
                    chatslistBodyReceiverDetailes.put(messageReceiverRef, chatsListBodyReceiver);

                    RootRef.updateChildren(chatslistBodyReceiverDetailes).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Log.e("TAG", "Chats receiver list created");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "Chats receiver list failed");
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAGError", "The read failed: " + error.getMessage());
            }

        });

    }

    private void displayToast(String text) {
        Toast.makeText(ChatsActivity.this, text, Toast.LENGTH_LONG).show();
    }

    private void updateUserStatus(String state) {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("time", saveCurrentTime);
        onlineStateMap.put("date", saveCurrentDate);
        onlineStateMap.put("state", state);

        RootRef.child("Users").child(messageSenderId).child("userState")
                .updateChildren(onlineStateMap);
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(ChatsActivity.this, HomeActivity.class);
//        startActivity(intent);
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
//        return onBackPressed();
    }

}
