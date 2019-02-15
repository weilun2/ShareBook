package ca.ualberta.cmput301w19t05.sharebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    ProgressDialog progressDialog;

    private EditText signupInputName, signupInputEmail, signupInputPassword, secondPassword;
    private Button submitRegister;
    private Button back;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        secondPassword = (EditText) findViewById(R.id.second_input_password);
        submitRegister = (Button) findViewById(R.id.submut_register);
        back = (Button) findViewById(R.id.end_register);
        submitRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondPassword.setError(null);
                String first = signupInputPassword.getText().toString();
                String second = secondPassword.getText().toString();

                if (first.equals(second) && !"".equals(first)) {
                    submitForm();
                } else {

                    secondPassword.setError(getString(R.string.error_two_password_not_match));
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void submitForm() {
        mAuth = FirebaseAuth.getInstance();
        final String email = signupInputEmail.getText().toString();
        String password = signupInputPassword.getText().toString();
        progressDialog.setMessage("Adding you ...");
        showDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideDialog();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent();
                            intent.putExtra("email", email);
                            setResult(0x07, intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

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
