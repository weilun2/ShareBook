package ca.ualberta.cmput301w19t05.sharebook.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.cloudMessage.APIServer;
import ca.ualberta.cmput301w19t05.sharebook.models.Data;
import ca.ualberta.cmput301w19t05.sharebook.cloudMessage.MyResponse;
import ca.ualberta.cmput301w19t05.sharebook.cloudMessage.Notification;
import ca.ualberta.cmput301w19t05.sharebook.cloudMessage.RetroFitClient;
import ca.ualberta.cmput301w19t05.sharebook.cloudMessage.Sender;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

/**
 * FirebaseHandler
 * Handle firebase for storaging data
 * Public Methods:
 *      addUsernameEmailTuple (String, String) -> void :
 *          create an extra table to help distinguish occupied email and support email login feature
 *
 *      removeUser() -> void:
 *          remove current logged user
 *
 */
public class FirebaseHandler {
    public static final String REQUEST = "request_notification";
    public static final String ACCEPT = "accept_notification";
    public static final String DECLINE = "decline_notification";
    public static final String LENT_SCAN = "borrowing_notification";

    public static String token;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private String imageURL;
    private static String baseURL  = "https://fcm.googleapis.com/";

    public static APIServer getFCMClient(){
        return RetroFitClient.getClient(baseURL).create(APIServer.class);
    }

    public FirebaseHandler(Context mContext) {
        this.mContext = mContext;
        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.storageRef = FirebaseStorage.getInstance().getReference();
        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
        }
        RetroFitClient.getClient(baseURL).create(APIServer.class);
        Log.d(TAG, "handler instance created");
    }
    public FirebaseHandler() {
        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.storageRef = FirebaseStorage.getInstance().getReference();
        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
        }
        RetroFitClient.getClient(baseURL).create(APIServer.class);
        Log.d(TAG, "handler instance created");
    }

    public DatabaseReference getMyRef() {
        return myRef;
    }

    /**
     * create an extra table to help distinguish occupied email and support email login feature
     * expected to be called at each signing up attempt

     */
    public void addUsernameEmailTuple(User user) {
        Log.d(TAG, "start add new user");
        myRef.child(mContext.getString(R.string.db_username_email_tuple))
                .child(user.getUserID()).setValue(user);

    }


    public void uploadImage(String name, Bitmap bp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        String filenames = "image/" + getCurrentUser().getUserID() + "/" + name.hashCode() + ".png";
        final StorageReference ref = storageRef.child(filenames);
        UploadTask uploadTask = ref.putBytes(data);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });



    }

    public void addBook(Book book) {
        myRef.child("books").child(book.getOwner().getUserID()).child(book.getBookId()).setValue(book);

    }
    public void addLocation(Book book, LatLng Location){
        myRef.child("Location").child(book.getBookId()).setValue(Location);
    }

    public User getCurrentUser() {
        User res;
        if (user != null) {
            res = new User(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl());
        } else {
            res = new User();
        }
        return res;
    }

    public void generateImageFromText(String text) {

        Bitmap src = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.book_cover);
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cs = new Canvas(dest);
        Paint paint = new Paint();
        paint.setTextSize(80);
        paint.setColor(ContextCompat.getColor(mContext, R.color.text_color));
        paint.setStyle(Paint.Style.FILL);
        cs.drawBitmap(src, 0f, 0f, null);

        String[] words = text.split(" ");
        int row = 1;
        StringBuilder line = new StringBuilder();
        for (String word : words) {

            float a = paint.measureText(line + word);
            float b = src.getWidth();
            if (a < b - 10f) {
                line.append(word).append(" ");
            } else {
                float height = paint.measureText("yY") + 15f;
                float width = paint.measureText(line.toString());
                float x_coord = (src.getWidth() - width) / 2;
                cs.drawText(line.toString(), x_coord, height * row, paint);
                row++;
                line = new StringBuilder();
            }
        }
        float height = paint.measureText("yY") + 15f;
        float width = paint.measureText(line.toString());
        float x_coord = (src.getWidth() - width) / 2;
        cs.drawText(line.toString(), x_coord, height * row, paint);
        uploadImage(text, dest);
    }

    public void sendRequest(Book book) {
        changeBookStatus(book,Book.REQUESTED);


        myRef.child(Book.REQUESTED)
                .child(book.getBookId())
                .child(getCurrentUser().getUserID())
                .setValue(getCurrentUser());
        sendNotification(REQUEST, book, book.getOwner());

    }
    public void changeBookStatus(Book book, String status){
        myRef.child("books").child(book.getOwner().getUserID()).child(book.getBookId())
                .child("status").setValue(status);
    }


    public void acceptRequest(Book book, User user){
        changeBookStatus(book,Book.ACCEPTED);
        myRef.child(Book.ACCEPTED)
                .child(book.getBookId()).child(user.getUserID()).setValue(user);
        myRef.child(Book.REQUESTED).child(book.getBookId()).child(user.getUserID()).setValue(null);
        sendNotification(ACCEPT,book, user);
    }

    public void declineRequest(Book book, User user){
        myRef.child(Book.REQUESTED).child(book.getBookId()).child(user.getUserID()).setValue(null);
        final DatabaseReference bookPath = myRef.child("books").child(book.getOwner().getUserID())
                .child(book.getBookId());

        myRef.child(Book.REQUESTED).child(book.getBookId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChildren()){
                            bookPath.child("status").setValue(Book.AVAILABLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        sendNotification(DECLINE,book,user);

    }

    private void sendNotification(final String notificationType, final Book book, final User receiver){


        myRef.child(mContext.getString(R.string.db_username_email_tuple))
                .child(receiver.getUserID()).child("token")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String targetToken = dataSnapshot.getValue(String.class);
                        Notification notification = null;
                        Data data = new Data(book.getBookId(),notificationType,getCurrentUser().getUserID(),receiver.getUserID(),book.getTitle(),getCurrentUser().getUsername());
                        switch (notificationType) {
                            case REQUEST:
                                notification = new Notification("you receive a request", "Request");
                                break;
                            case ACCEPT:
                                notification = new Notification("one request has been accepted", "Accepted");
                                break;
                            case DECLINE:
                                notification = new Notification("one request has been declined","Declined");
                        }
                        myRef.child(notificationType).child(receiver.getUserID()).child(book.getBookId())
                                .setValue(data);

                        Sender sender = new Sender(notification, data, targetToken);
                        getFCMClient().sendNotification(sender).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if ((response.body() != null ? response.body().success : 0) == 1){
                                    Toast.makeText(mContext, "success", Toast.LENGTH_LONG).show();

                                }
                                else {
                                    Toast.makeText(mContext, "fail", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.e(TAG, "onFailure: ", t);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    public StorageReference getStorageRef() {
        return storageRef;
    }


    public void clearNotification(Book book) {
        if (book.getOwner().getUserID().equals(getCurrentUser().getUserID())){
            myRef.child(FirebaseHandler.REQUEST)
                    .child(getCurrentUser().getUserID())
                    .child(book.getBookId()).setValue(null);
        }else {
            myRef.child(FirebaseHandler.ACCEPT)
                    .child(getCurrentUser().getUserID())
                    .child(book.getBookId()).setValue(null);
            myRef.child(FirebaseHandler.DECLINE)
                    .child(getCurrentUser().getUserID())
                    .child(book.getBookId()).setValue(null);
        }


    }

    public void confirmBorrowing(final Book book) {
        myRef.child(FirebaseHandler.LENT_SCAN).child(book.getBookId())
                .child(getCurrentUser().getUserID()).setValue(1);
        myRef.child(FirebaseHandler.LENT_SCAN).child(book.getBookId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount()==2){

                                changeBookStatus(book,Book.BORROWED);
                                myRef.child(Book.BORROWED)
                                        .child(book.getBookId()).child(getCurrentUser().getUserID()).setValue(user);
                                myRef.child(Book.ACCEPTED).child(book.getBookId()).setValue(null);
                                myRef.child(FirebaseHandler.LENT_SCAN).child(book.getBookId())
                                        .setValue(null);
                                myRef.child("Location").child(book.getBookId()).setValue(null);




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void confirmLent(Book book) {
        myRef.child(FirebaseHandler.LENT_SCAN).child(book.getBookId())
                .child(getCurrentUser().getUserID()).setValue(1);
    }

    public void returned(final Book book, final int res) {
        myRef.child(FirebaseHandler.LENT_SCAN).child(book.getBookId())
                .child(getCurrentUser().getUserID()).setValue(res);
        myRef.child(mContext.getString(R.string.db_username_email_tuple)).child(book.getOwner()
                .getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User owner = dataSnapshot.getValue(User.class);
                if (owner!=null && owner.getUserID()!=null){
                    if (owner.getRates()==null){
                        owner.setRates(new ArrayList<Long>());
                        owner.getRates().add((long) res);
                    }
                    else {
                        owner.getRates().add((long) res);
                    }
                    myRef.child(mContext.getString(R.string.db_username_email_tuple)).child(book.getOwner()
                            .getUserID()).setValue(owner);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void returnBook(final Book book, final int res) {
        myRef.child(FirebaseHandler.LENT_SCAN).child(book.getBookId())
                .child(getCurrentUser().getUserID()).setValue(res);
        myRef.child(FirebaseHandler.LENT_SCAN).child(book.getBookId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount()==2){

                                changeBookStatus(book,Book.AVAILABLE);
                                myRef.child(Book.BORROWED).child(book.getBookId()).setValue(null);
                                myRef.child(FirebaseHandler.LENT_SCAN).child(book.getBookId())
                                        .setValue(null);
                                for(DataSnapshot it : dataSnapshot.getChildren()){
                                    if (!book.getOwner().getUserID().equals(it.getKey())){
                                        String requesterId = it.getKey();
                                        myRef.child(mContext.getString(R.string.db_username_email_tuple)).child(requesterId)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                User owner = dataSnapshot.getValue(User.class);
                                                if (owner!=null && owner.getUserID()!=null){
                                                    if (owner.getRates()==null){
                                                        owner.setRates(new ArrayList<Long>(res));
                                                    }
                                                    else {
                                                        owner.getRates().add(Long.valueOf(res));
                                                    }
                                                    myRef.child(mContext.getString(R.string.db_username_email_tuple)).child(book.getOwner()
                                                            .getUserID()).setValue(owner);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
