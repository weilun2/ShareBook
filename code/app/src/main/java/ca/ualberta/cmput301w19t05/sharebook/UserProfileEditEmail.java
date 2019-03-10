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

public class UserProfileEditEmail extends AppCompatActivity {

    private TextView userEmail;
    private FirebaseUser user;
    private String newEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit_email);

        Intent intent = getIntent();

        userEmail = (TextView) findViewById(R.id.editUserEmail);

        Button updateEmail = (Button) findViewById(R.id.updateEmail);

        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInFireBase();
            }
        });

    }

    protected void onStart(){
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            userEmail.setText(user.getEmail());
        }
    }

    private void saveInFireBase(){
        user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(userEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "User email address updated.");
                }
            }
        });

        finish();
    }

}
