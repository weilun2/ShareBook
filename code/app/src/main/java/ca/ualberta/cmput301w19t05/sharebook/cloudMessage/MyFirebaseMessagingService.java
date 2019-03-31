package ca.ualberta.cmput301w19t05.sharebook.cloudMessage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.activities.BookDetailActivity;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;

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
                    setRequestNotification(remoteMessage);
                    break;
                case FirebaseHandler.ACCEPT:
                    setRequestNotification(remoteMessage);
                    break;
            }

            Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        }

    }



    private void setRequestNotification(final RemoteMessage remoteMessage) {
        FirebaseHandler firebaseHandler = new FirebaseHandler(this);
        String receiver = remoteMessage.getData().get("receiver");
        String bookId = remoteMessage.getData().get("bookId");
        final Context mContext  = this;
        if (receiver!=null&&bookId!=null){
            firebaseHandler.getMyRef().child("books").child(receiver).child(bookId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Book book = dataSnapshot.getValue(Book.class);
                    if ((book != null ? book.getBookId() : null) != null){
                        Intent intent;
                        intent = new Intent(mContext, BookDetailActivity.class);
                        intent.putExtra(BookDetailActivity.BOOK, book);
                        intent.putExtra(BookDetailActivity.FUNCTION,BookDetailActivity.DELETE);
                        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                                0,intent,PendingIntent.FLAG_ONE_SHOT);
                        if (remoteMessage.getNotification()!= null){
                            showNotification(remoteMessage.getNotification().getTitle(),
                                    remoteMessage.getNotification().getBody(),pendingIntent);
                            Log.d(TAG, "onMessageReceived: " +
                                    remoteMessage.getNotification().getBody());
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }





    }

    private void showNotification(String title, String body,PendingIntent pendingIntent){

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "ca.ualberta.cmput301w19t05.sharebook.test";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Notification",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Test");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationManager.createNotificationChannel(notificationChannel);

        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_hand)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("info")
                .setContentIntent(pendingIntent);

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
