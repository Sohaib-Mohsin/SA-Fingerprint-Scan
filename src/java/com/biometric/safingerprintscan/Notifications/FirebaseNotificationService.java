package com.biometric.safingerprintscan.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Screens.Admin_MainScreen;
import com.biometric.safingerprintscan.Screens.Login_Screen;
import com.biometric.safingerprintscan.Screens.User_MainScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private Util util = new Util();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> map = remoteMessage.getData();
            String title = map.get("title");
            String message = map.get("message");
            String hisID = map.get("hisID");
            String hisImage = map.get("hisImage");
            String chatID = map.get("chatID");

            Log.d("TAG", "onMessageReceived: chatID is " + chatID + "\n hisID" + hisID);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                createOreoNotification(title, message, hisID, hisImage, chatID);
            else
                createNormalNotification(title, message, hisID, hisImage, chatID);
        } else Log.d("TAG", "onMessageReceived: no data ");


        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        updateToken(s);
        super.onNewToken(s);
    }

    private void updateToken(String token) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(util.getUID());
            Map<String, Object> map = new HashMap<>();
            map.put("token_id", token);
            databaseReference.updateChildren(map);
        }

    }


    private void createNormalNotification(String title, String message, String hisID, String hisImage, String chatID) {

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);
        String AS = sharedPreferences.getString("AS", "null");

        if (AS.equals("Admin")) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AllConstants.CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_login_round)
                    .setAutoCancel(true)
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.black, null))
                    .setSound(uri);

            Intent intent = new Intent(this, Admin_MainScreen.class);
            intent.putExtra("chatID", chatID);
            intent.putExtra("hisID", hisID);
            intent.putExtra("hisImage", hisImage);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(new Random().nextInt(85 - 65), builder.build());

        } else if (AS.equals("User")) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AllConstants.CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.mipmap.ic_login_round)
                    .setAutoCancel(true)
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.black, null))
                    .setSound(uri);

            Intent intent = new Intent(this, User_MainScreen.class);
            intent.putExtra("chatID", chatID);
            intent.putExtra("hisID", hisID);
            intent.putExtra("hisImage", hisImage);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            builder.setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(new Random().nextInt(85 - 65), builder.build());

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOreoNotification(String title, String message, String hisID, String hisImage, String chatID) {

        NotificationChannel channel = new NotificationChannel(AllConstants.CHANNEL_ID, "Message", NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        SharedPreferences sharedPreferences = getSharedPreferences("LOGIN_AS", Context.MODE_PRIVATE);
        String AS = sharedPreferences.getString("AS", "null");

        Intent intent;

        if (AS.equals("Admin")) {
            intent = new Intent(this, Admin_MainScreen.class);
            intent.putExtra("hisID", hisID);
            intent.putExtra("hisImage", hisImage);
            intent.putExtra("chatID", chatID);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Notification notification = new Notification.Builder(this, AllConstants.CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.black, null))
                    .setSmallIcon(R.mipmap.ic_login_round)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            manager.notify(100, notification);

        } else if (AS.equals("User")) {

            intent = new Intent(this, User_MainScreen.class);
            intent.putExtra("hisID", hisID);
            intent.putExtra("hisImage", hisImage);
            intent.putExtra("chatID", chatID);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Notification notification = new Notification.Builder(this, AllConstants.CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(ResourcesCompat.getColor(getResources(), R.color.black, null))
                    .setSmallIcon(R.mipmap.ic_login_round)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
            manager.notify(100, notification);

        }

    }
}
