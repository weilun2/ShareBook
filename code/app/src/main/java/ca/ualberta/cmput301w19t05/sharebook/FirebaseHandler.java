package ca.ualberta.cmput301w19t05.sharebook;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.support.constraint.Constraints.TAG;

public class FirebaseHandler {
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String userId;
    private FirebaseAuth mAuth;

    public FirebaseHandler(Context mContext) {
        this.mContext = mContext;
        this.database = FirebaseDatabase.getInstance();
        this.myRef = database.getReference();
        this.mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }
        Log.d(TAG, "handler instance created");
    }

    public void addUsernameEmailTuple(String email, String username) {
        Log.d(TAG, "start add " + email + ", " + username);
        User user = new User(userId, username, email);
        myRef.child(mContext.getString(R.string.db_username_email_tuple))
                .child(username).setValue(email);


    }
}
