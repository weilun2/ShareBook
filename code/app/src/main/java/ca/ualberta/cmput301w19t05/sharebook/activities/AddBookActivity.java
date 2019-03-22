
package ca.ualberta.cmput301w19t05.sharebook.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;

/**
 * A addBook screen Allow user adding books into their sheff
 */
public class AddBookActivity extends AppCompatActivity {

    private String titleText;
    private String authorText;
    private String descriptionText;
    private String ISBN;
    private FirebaseHandler firebaseHandler;
    private int IMAGE_REQUEST_CODE = 1;
    private int flag = 0;
    private Uri Uri ;
    private Bitmap Uploadedgraph;
    //private int RESIZE_REQUEST_CODE =2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseHandler = new FirebaseHandler(AddBookActivity.this);
        setContentView(R.layout.activity_add_book_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button submitButton = findViewById(R.id.submit);
        Button cancelButton = findViewById(R.id.cancel);
        Button uploadButton = findViewById(R.id.PhotoUpload);
        final EditText editTitle = findViewById(R.id.title);
        final EditText editAuthor = findViewById(R.id.author);
        final EditText editDescription = findViewById(R.id.description);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });
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

                            if(flag == 1){
                                book.setPhoto(String.valueOf(Uri));}
                            else if(flag == 0){
                                book.setPhoto(String.valueOf(uri));
                                //System.out.println(flag);
                            }
                            if (!descriptionText.equals("")) {
                                book.setDescription(descriptionText);
                            }
                            firebaseHandler.addBook(book);
                            firebaseHandler.generateImageFromText(book.getTitle());
                            if (flag == 1) {
                                firebaseHandler.uploadImage(book.getTitle(), Uploadedgraph);
                            }
                            finish();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddBookActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                                }
                            });

                }

            }

        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(data!= null){
                ImageView photoUplaoded = (ImageView)findViewById(R.id.photoUploaded);
                Uri uri = ShowresizedImage(data);
                Log.e("uri", uri.toString());
                ContentResolver cr = this.getContentResolver();
                try {
                    // get bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(cr
                            .openInputStream(uri));
                    // get file path
                    //String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    //Cursor cursor = cr.query(uri, filePathColumn, null, null,
                            //null);

                    // get the index of the photo user selected
                    //int column_index = cursor
                           // .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    //int column_index = cursor.getColumnIndex(filePathColumn[0]);
                    // move the cursor to the begin
                    //cursor.moveToFirst();
                    // get the final path
                    //final String filepath = cursor.getString(column_index);
                   // Log.e("uri", filepath);
                    //System.out.println(filepath);
                    photoUplaoded.setImageBitmap(bitmap);
                    Uri = uri;
                    flag =1;


                    Uploadedgraph = bitmap;
                    //cursor.close();
                    //firebaseHandler.uploadImage(book.getTitle(),bitmap);
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage(), e);
                }
            }
        }
    }



    /*public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }*/
    public Uri ShowresizedImage(Intent data){
        Uri uri = data.getData();
        return uri;
    }

}