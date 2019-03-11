package ca.ualberta.cmput301w19t05.sharebook.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.ualberta.cmput301w19t05.sharebook.R;
/**
 * A userprofile screen that offers Username, useremail, userImage, and can be edited
 */
public class UserProfile extends AppCompatActivity {
    private ImageView viewUserImage;
    private TextView viewUserName;
    private TextView viewUserEmail;
    private FirebaseUser user;
    ProgressDialog progressDialog;
    private DatabaseReference reference;
    private String TAG;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final TextView textView = (TextView) v;
            View view = View.inflate(UserProfile.this, R.layout.content_edit, null);
            final EditText userInput = view.findViewById(R.id.user_input);
            userInput.setText(textView.getText());
            userInput.requestFocus();
            userInput.selectAll();

            new AlertDialog.Builder(UserProfile.this)
                    .setTitle("edit " + textView.getTag()).setView(view)
                    .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (textView.getTag().equals("username")) {
                                progressDialog.setMessage("updating your username...");
                                showDialog();

                                Query query = reference
                                        .orderByChild("username")
                                        .equalTo(userInput.getText().toString());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() == null) {
                                            updateUsername(userInput.getText().toString());
                                        } else {
                                            hideDialog();
                                            Toast.makeText(UserProfile.this, "username exists", Toast.LENGTH_SHORT).show();
                                            progressDialog.setMessage(null);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(UserProfile.this, databaseError.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } else if (textView.getTag().equals("email")) {
                                progressDialog.setMessage("updating your email...");
                                showDialog();
                                Query query = reference
                                        .orderByChild("email")
                                        .equalTo(userInput.getText().toString());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() == null) {
                                            updateEmail(userInput.getText().toString());
                                        } else {
                                            hideDialog();
                                            Toast.makeText(UserProfile.this, "update email failed", Toast.LENGTH_SHORT).show();
                                            progressDialog.setMessage(null);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(UserProfile.this, databaseError.toString(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                                
                            }


                        }
                    }).setNegativeButton("cancel", null).show();
        }
    };

    private void updateEmail(final String toString) {
        user.updateEmail(toString)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideDialog();
                        Log.d(TAG, "onComplete: update email to " + toString);
                        viewUserEmail.setText(toString);
                    }
                });


    }

    private void updateUsername(final String username) {

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();
        user.updateProfile(profileChangeRequest)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfile.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideDialog();
                        Log.d(TAG, "onComplete: update username to " + username);
                        reference.child(user.getUid()).child("username").setValue(username);
                        viewUserName.setText(username);
                    }
                });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference()
                .child(getString(R.string.db_username_email_tuple));
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        viewUserImage = findViewById(R.id.UserImage);
        viewUserName = findViewById(R.id.UserName);
        viewUserEmail = findViewById(R.id.UserEmail);
        viewUserName.setText(user.getDisplayName());
        viewUserEmail.setText(user.getEmail());

        viewUserEmail.setOnClickListener(onClickListener);
        viewUserName.setOnClickListener(onClickListener);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
