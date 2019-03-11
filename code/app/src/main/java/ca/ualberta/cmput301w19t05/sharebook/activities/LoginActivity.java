package ca.ualberta.cmput301w19t05.sharebook.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ca.ualberta.cmput301w19t05.sharebook.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private static final String TAG = "login screen";
    public static final int REGISTER_REQUEST_CODE = 0x07;
    public static final int PASSWORD_LENGTH = 4;


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    final static String DATABASE_URL = "https://sharebook-80fa6.firebaseio.com";

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    private Boolean inProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        //progressDialog.setCancelable(false);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        inProgress = false;
        progressDialog.setMessage("Attempt to Login ...");

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    if (!inProgress) {
                        username_or_email(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    }

                    return true;
                }
                return false;
            }
        });

        TextView register_button = findViewById(R.id.register_button);
        register_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


        CardView mEmailSignInButton = findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inProgress) {
                    username_or_email(mEmailView.getText().toString(), mPasswordView.getText().toString());
                }

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }


    /**
     * login attempt via username or email
     * @param email -user input for email or username
     * @param password -user input for password
     */
    private void username_or_email(final String email, final String password) {
        showDialog();
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            attemptLogin(email, password);
        } else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            //get the emailId associated with the username
            Query query = reference.child(getString(R.string.db_username_email_tuple))
                    .orderByChild("username").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    String user_email = data.child("email").getValue().toString();
                                    attemptLogin(user_email, password);
                                }

                            } else {
                                mEmailView.setError(null);
                                mEmailView.setError("username not exist");
                                mEmailView.requestFocus();
                                hideDialog();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(LoginActivity.this, databaseError.toString(),
                                    Toast.LENGTH_SHORT).show();
                            hideDialog();
                        }


                    });
        }
    }


    private void register(){
        Intent goto_register = new Intent(this, Register.class);
        startActivityForResult(goto_register, REGISTER_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mPasswordView.requestFocus();
        if (requestCode== REGISTER_REQUEST_CODE && resultCode== REGISTER_REQUEST_CODE){

            String email = data.getStringExtra("email");
            mEmailView.getText().clear();
            mPasswordView.getText().clear();
            mEmailView.setText(email);

        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(String email, String password) {


        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            hideDialog();
        }

        // Check for a valid password, if the user entered one.
        else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
            hideDialog();

        }

        // Check for a valid email address.


        else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.


            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            hideDialog();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideDialog();
                            Toast.makeText(LoginActivity.this, e.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }



    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > PASSWORD_LENGTH;
    }

    private void showDialog() {
        inProgress = true;
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        inProgress = false;
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


}

