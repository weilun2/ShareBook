package ca.ualberta.cmput301w19t05.sharebook.tools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import ca.ualberta.cmput301w19t05.sharebook.R;

import static android.support.constraint.Constraints.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getFrom());
        int a  = remoteMessage.getData().size();
        if (remoteMessage.getData().size()>0){
            switch (remoteMessage.getData().get("requestType")){
                case FirebaseHandler.REQUEST:
                    setRequestNotification();
                    break;
                case FirebaseHandler.ACCEPT:
                    setAcceptNotification();
                    break;
            }

            Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification()!= null){
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            Log.d(TAG, "onMessageReceived: " + remoteMessage.getNotification().getBody());
        }
    }

    private void setAcceptNotification() {
    }

    private void setRequestNotification() {

    }

    private void showNotification(String title, String body){

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "ca.ualberta.cmput301w19t05.sharebook.test";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Test");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationManager.createNotificationChannel(notificationChannel);

        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_hand)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("info");
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());


    }


    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "onNewToken: " + s);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String s) {
        FirebaseHandler firebaseHandler = new  FirebaseHandler();
        firebaseHandler.getMyRef().child(getString(R.string.db_username_email_tuple))
                .child(firebaseHandler.getCurrentUser().getUserID()).child("token").setValue(s);
    }

}
