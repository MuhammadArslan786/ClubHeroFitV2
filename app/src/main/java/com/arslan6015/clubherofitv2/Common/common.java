package com.arslan6015.clubherofitv2.Common;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class common {
    static String fullName;
    static String imageLink = null;
    static String token;
    static String email;
    static int count;

    public static String getCurrentName(){
    DatabaseReference currentUserInfo = FirebaseDatabase.getInstance().getReference("UserInfo")
            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        currentUserInfo.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                fullName = snapshot.child("fullName").getValue().toString();
                Log.e("TAGCommon","CurrentName: "+fullName);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("TAG","CurrentName: "+error);
        }
    });
        return fullName;
    }

    public static String getCurrentEmail(){
        DatabaseReference currentUserInfo = FirebaseDatabase.getInstance().getReference("UserInfo")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        currentUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    email = snapshot.child("email").getValue().toString();
                    Log.e("TAGCommon","CurrentEmail: "+email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG","CurrentEmail: "+error);
            }
        });
        return email;
    }

    public static String getCurrentProfileLink(){
        DatabaseReference currentUserInfo = FirebaseDatabase.getInstance().getReference("UserInfo")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        currentUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() & snapshot.hasChild("image")){
                    imageLink = snapshot.child("image").getValue().toString();
                    Log.e("TAGCommon","CurrentImage: "+imageLink);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG","CurrentImage: "+error);
            }
        });
        return imageLink;
    }

    public static String getSenderToken(String messageReceiverId){

        DatabaseReference recevierToken = FirebaseDatabase.getInstance().getReference("Tokens")
                .child(messageReceiverId);

        recevierToken.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() & snapshot.hasChild("token")){
                    token = snapshot.child("token").getValue().toString();
                    Log.e("TAGCommon","receiverToken: "+token);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG","receiverToken: "+error);
            }
        });
        return token;

    }

    public static int getUnseenMessages(String messageReceiverId,String messageSenderId){

        DatabaseReference recevierSideUnseenMessages = FirebaseDatabase.getInstance().getReference("ChatsList")
                .child(messageReceiverId).child(messageSenderId);

        recevierSideUnseenMessages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() & snapshot.hasChild("unseen_msg_count")){
                    count = Integer.parseInt(snapshot.child("unseen_msg_count").getValue().toString());
                    Log.e("TAGCommon","unseen_msg_count: "+count);
                }else {
                    count = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG","unseen_msg_count: "+error);
            }
        });
        return count;

    }

}
