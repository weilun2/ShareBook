package ca.ualberta.cmput301w19t05.sharebook;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static android.support.constraint.Constraints.TAG;

public class UserProfileEditName extends AppCompatActivity {

    private TextView userName;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit_name);

        Intent intent = getIntent();

        userName = (TextView) findViewById(R.id.editUserName);

        Button updateName = (Button) findViewById(R.id.updateName);

        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInFireBase();
            }
        });

    }

    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            userName.setText(user.getDisplayName());
        }
    }

    private void saveInFireBase() {
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userName.getText().toString())
                    .build();


            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User name updated.");
                                finish();
                            }
                        }
                    });
        }

    }

}


