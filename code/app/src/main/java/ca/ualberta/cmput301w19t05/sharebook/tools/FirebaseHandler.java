package ca.ualberta.cmput301w19t05.sharebook.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.User;

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
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private String imageURL;

    public FirebaseHandler(Context mContext) {
        this.mContext = mContext;
        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.storageRef = FirebaseStorage.getInstance().getReference();
        if (mAuth.getCurrentUser() != null) {
            user = mAuth.getCurrentUser();
        }
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
        myRef.child("books").child(book.getOwner().getUserID()).child(book.getBookId())
                .child("status").setValue("requested");

        myRef.child("requests")
                .child(book.getBookId())
                .child(getCurrentUser().getUserID())
                .setValue(getCurrentUser());

    }


    public void acceptRequest(Book book, User user){
        myRef.child("books").child(book.getOwner().getUserID()).child(book.getBookId())
                .child("status").setValue("accepted");
        myRef.child("accepted")
                .child(book.getBookId()).child(user.getUserID()).setValue(user);
        myRef.child("requests").child(book.getBookId()).child(user.getUserID()).setValue(null);
    }

    public void declineRequest(Book book, User user){
        myRef.child("books").child(book.getOwner().getUserID()).child(book.getBookId())
                .child("status").setValue("available");
        myRef.child("requests").child(book.getBookId()).child(user.getUserID()).setValue(null);
    }


    public StorageReference getStorageRef() {
        return storageRef;
    }

}
