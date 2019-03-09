package ca.ualberta.cmput301w19t05.sharebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditBookDescriptionActivity extends AppCompatActivity {
    private String titleText;
    private String authorText;
    private String descriptionText;
    private String ISBN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_book_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button submitButton = (Button) findViewById(R.id.submit);
        final EditText editTitle = (EditText) findViewById(R.id.title);
        final EditText editAuthor = (EditText) findViewById(R.id.author);
        final EditText editDescription = (EditText) findViewById(R.id.description);

        final Book book = (Book) getIntent().getBundleExtra("putI").getSerializable("I");

        editTitle.setText(Book.getTitle());
        editTitle.setText(Book.getAuthor());
        editTitle.setText(Book.getDescription());

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
                    Book book = new Book(titleText, authorText, descriptionText, ISBN);

                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(EditBookDescriptionActivity.this, MainActivity.class);
                    bundle.putSerializable("getB", book);
                    intent.putExtra("B", bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }


        });
}
