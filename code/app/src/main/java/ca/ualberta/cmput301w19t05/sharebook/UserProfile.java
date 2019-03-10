package ca.ualberta.cmput301w19t05.sharebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfile extends AppCompatActivity {
    private ImageView viewUserImage;
    private TextView viewUserName;
    private TextView viewUserEmail;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();

        viewUserImage = (ImageView) findViewById(R.id.UserImage);
        viewUserName = (TextView) findViewById(R.id.UserName);
        viewUserEmail = (TextView) findViewById(R.id.UserEmail);

    }

    protected void onStart(){
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            viewUserImage.setImageURI(user.getPhotoUrl());
            viewUserName.setText(user.getDisplayName());
            viewUserEmail.setText(user.getEmail());

            /** need new activity to edit the image of the user here */

            viewUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserProfile.this, UserProfileEditName.class);
                    startActivity(intent);
                }
            });

            viewUserEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserProfile.this, UserProfileEditEmail.class);
                    startActivity(intent);
                }
            });
        }

    }

    protected void onResume(){
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            viewUserImage.setImageURI(user.getPhotoUrl());
            viewUserName.setText(user.getDisplayName());
            viewUserEmail.setText(user.getEmail());
        }
    }

}
