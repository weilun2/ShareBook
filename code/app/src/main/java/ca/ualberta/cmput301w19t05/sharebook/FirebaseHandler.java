package ca.ualberta.cmput301w19t05.sharebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static android.support.constraint.Constraints.TAG;

/**
 * FirebaseHandler
 *
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


    /**
     * create an extra table to help distinguish occupied email and support email login feature
     * expected to be called at each signing up attempt
     * @param email -user email
     * @param username -unique username string
     */
    public void addUsernameEmailTuple(String email, String username) {
        Log.d(TAG, "start add " + email + ", " + username);
        myRef.child(mContext.getString(R.string.db_username_email_tuple))
                .child(username).setValue(email);

    }


    /**
     * remove current logged user
     */

    public void removeUser() {
        Log.d(TAG, "remove current user" );
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "User account deleted.");
                }
            }
        });

    }

    public void uploadImage(String name, Bitmap bp) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        String filenames = "image/" + name.hashCode() + ".png";
        final StorageReference ref = storageRef.child(filenames);
        UploadTask uploadTask = ref.putBytes(data);


//        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                imageURL = uri.toString();
//            }
//        });

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

}
