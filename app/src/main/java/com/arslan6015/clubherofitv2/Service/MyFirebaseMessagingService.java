package com.arslan6015.clubherofitv2.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.arslan6015.clubherofitv2.ChatsActivity;
import com.arslan6015.clubherofitv2.Common.common;
import com.arslan6015.clubherofitv2.HomeActivity;
import com.arslan6015.clubherofitv2.R;
import com.arslan6015.clubherofitv2.ui.chats.ChatsListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        Map<String ,String > extraData = remoteMessage.getData();

        String messageSenderIdd = extraData.get("messageSenderId");
//        String messageReceiverName = extraData.get("messageReceiverName");
//        String messageReceiverEmail = extraData.get("messageReceiverEmail");
//        String messageReceiverImage = extraData.get("messageReceiverImage");



        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this,"fb.com/arslan6015")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_baseline_chat_24);
//                .setLargeIcon(R.drawable.ic_baseline_send_24);

//        Intent intent;
//        if (messageSenderId.equals("shoes")){
//            intent = new Intent(this, ChatsActivity.class);
////            intent.putExtra("brandId",brandId);
////            intent.putExtra("category",category);
//        }else {
//            intent = new Intent(this, ChatsActivity.class);
//        }
//        intent.putExtra("brandId",brandId);
//        intent.putExtra("category",category);

        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra("ChatPersonId",messageSenderIdd);
//        intent.putExtra("ChatPersonName",common.getCurrentName());
//        intent.putExtra("ChatPersonEmail",common.getCurrentName());
//        intent.putExtra("ChatPersonImage", common.getCurrentProfileLink());
//
        PendingIntent pendingIntent = PendingIntent.getActivity(this,10,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int id = (int) System.currentTimeMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("fb.com/arslan6015",
                    "clubHerofit",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id,notificationBuilder.build());
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

//
//    private void sendRegistrationToServer(String token) {
//        DatabaseReference reference =  FirebaseDatabase.getInstance().getReference("Tokens")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .child("token");
//
//        reference.setValue(token);
//    }
}
