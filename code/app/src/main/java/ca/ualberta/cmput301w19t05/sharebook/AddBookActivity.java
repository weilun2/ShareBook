package ca.ualberta.cmput301w19t05.sharebook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddBookActivity extends AppCompatActivity {

    private String titleText;
    private String authorText;
    private String descriptionText;
    private String ISBN;
    private FirebaseHandler firebaseHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseHandler = new FirebaseHandler(AddBookActivity.this);
        setContentView(R.layout.activity_add_book_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button submitButton = findViewById(R.id.submit);
        final EditText editTitle = findViewById(R.id.title);
        final EditText editAuthor = findViewById(R.id.author);
        final EditText editDescription = findViewById(R.id.description);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;

                //test case for empty blank
                titleText = editTitle.getText().toString();
                if (TextUtils.isEmpty(titleText)) {
                    editTitle.setError("Need to fill");
                    valid = false;
                }

                authorText = editAuthor.getText().toString();
                if (TextUtils.isEmpty(authorText)) {
                    editAuthor.setError("Need to fill");
                    valid = false;
                }
                descriptionText = editDescription.getText().toString();

                //check ok
                if (valid) {
                    ISBN = "place holder";
                    final Book book = new Book(titleText, authorText, ISBN, firebaseHandler.getCurrentUser());
                    StorageReference reference = FirebaseStorage.getInstance().getReference().child("image/book_placeholder.png");
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            book.setPhoto(String.valueOf(uri));
                            firebaseHandler.addBook(book);
                            finish();
                        }
                    });

                }
            }

        });


    }
}