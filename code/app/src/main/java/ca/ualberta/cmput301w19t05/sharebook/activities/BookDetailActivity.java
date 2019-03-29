package ca.ualberta.cmput301w19t05.sharebook.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.fragments.AcceptedFragment;
import ca.ualberta.cmput301w19t05.sharebook.fragments.RequestListFragment;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.User;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;
/**
 * A book detial screen allow user to edit book
 */
public class BookDetailActivity extends AppCompatActivity {
    public final static int REQUEST = 1;
    public final static int DELETE = 2;
    public static final int ACCEPTED = 3;
    public final static String FUNCTION = "function";
    public final static String BOOK = "book";
    public final static String TEMP = "temp";

    private static final String TAG = "BookDetail";
    private RadioGroup title;
    private RadioGroup author;
    private RadioGroup owner;
    private RadioGroup description;
    private ImageView bookImage;
    private Book book;
    private FirebaseHandler firebaseHandler;
    private Button delete;
    private ProgressDialog progressDialog;
    private Boolean inProgress;
    private int function;
    private Uri Uri ;
    private boolean shortPress = false;
    private Bitmap Uploadedgraph;
    private StorageReference initialPhoto;



    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final TextView prompt = v.findViewWithTag("prompt");
            final TextView content = v.findViewWithTag("content");
            View view = View.inflate(BookDetailActivity.this, R.layout.content_edit, null);
            final EditText userInput = view.findViewById(R.id.user_input);
            userInput.setText(content.getText());
            userInput.requestFocus();
            userInput.selectAll();

            new AlertDialog.Builder(BookDetailActivity.this)
                    .setTitle(prompt.getText()).setView(view)
                    .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!userInput.getText().toString().equals("") && !userInput.getText().equals(content.getText())) {
                                content.setText(userInput.getText());
                                DatabaseReference ref = firebaseHandler.getMyRef().child("books").child(book.getOwner().getUserID()).child(book.getBookId());
                                if (v.equals(title)) {
                                    book.setTitle(userInput.getText().toString());
                                    ref.child("title").setValue(userInput.getText().toString());

                                } else if (v.equals(author)) {
                                    book.setAuthor(userInput.getText().toString());
                                    ref.child("author").setValue(userInput.getText().toString());
                                } else if (v.equals(description)) {
                                    book.setDescription(userInput.getText().toString());
                                    ref.child("description").setValue(userInput.getText().toString());
                                }

                            }

                        }
                    }).setNegativeButton("cancel", null).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initViews();

        Intent intent = getIntent();
        book = intent.getParcelableExtra(BOOK);
        firebaseHandler = new FirebaseHandler(this);
        function = intent.getIntExtra(FUNCTION,DELETE);

        setBookInfo();
        setClickListener();

    }

    private void setClickListener() {

        switch (function) {
            case DELETE:
                title.setOnClickListener(onClickListener);
                author.setOnClickListener(onClickListener);
                description.setOnClickListener(onClickListener);
                bookImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 0x07);
                    }


                });
                bookImage.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        System.out.println("hold long");
                        AlertDialog.Builder dialog = new AlertDialog.Builder(BookDetailActivity.this);
                        dialog.setMessage("Delete the current photo?");

                        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("yes pressed");
                                initialPhoto = firebaseHandler.getStorageRef().child("image/book_placeholder.png");
                                getUriAndUpLoad(initialPhoto, true);

                            }
                        });

                        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("no pressed");
                            }
                        });
                        dialog.show();
                        return true;
                    }

                    ;
                });


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(BookDetailActivity.this).setMessage("Are you sure?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        firebaseHandler.getMyRef().child("books").child(book.getOwner()
                                                .getUserID())
                                                .child(book.getBookId()).setValue(null);
                                        finish();
                                    }
                                }).setNegativeButton("No", null)
                                .show();
                    }
                });
            case ACCEPTED:
                switch (book.getStatus()) {
                    case Book.REQUESTED:
                        setRequestList();
                        break;

                    case Book.ACCEPTED:
                        setAcceptedList();
                        break;

                }



                return;

            case REQUEST:

                delete.setText("request");
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(BookDetailActivity.this)
                                .setMessage("Are you sure?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        firebaseHandler.sendRequest(book);
                                        finish();
                                    }
                                }).setNegativeButton("No", null)
                                .show();
                    }
                });
                break;
            default:
                delete.setVisibility(View.GONE);
            }

            TextView ownerText = owner.findViewWithTag("content");
            final String ownerName = ownerText.getText().toString();
            owner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!inProgress) {
                        showDialog();
                        Query query = firebaseHandler.getMyRef().child(getString(R.string.db_username_email_tuple))
                                .orderByChild("username").equalTo(ownerName);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        User user = data.getValue(User.class);
                                        Intent intent = new Intent(BookDetailActivity.this, UserProfile.class);
                                        intent.putExtra("owner", user);
                                        hideDialog();
                                        startActivity(intent);

                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(BookDetailActivity.this, databaseError.toString(),
                                        Toast.LENGTH_SHORT).show();
                                hideDialog();
                            }


                        });
                    }

                }
            });


    }

    private void setAcceptedList() {
        Bundle bundle = new Bundle();
        AcceptedFragment acceptedFragment = new AcceptedFragment();

        bundle.putParcelable("book",book);
        acceptedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.place_holder, acceptedFragment)
                .show(acceptedFragment).commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&requestCode==0x07) {
            if(data!= null){
                Uri uri = data.getData();

                ContentResolver cr = this.getContentResolver();
                try {
                    // get bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(cr
                            .openInputStream(uri));
                    //bookImage.setImageBitmap(bitmap);

                    Uri = uri;
                    Uploadedgraph = bitmap;
                    Glide.with(BookDetailActivity.this).load(bitmap)
                            .into(bookImage);
                    //bookImage.setImageBitmap(Uploadedgraph);



                } catch (Exception e) {
                    Log.e("Exception", e.getMessage(), e);
                }
                UploadPhoto();

            }
        }


    }
    private void UploadPhoto(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Uploadedgraph.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        String filenames = "image/" + firebaseHandler.getCurrentUser().getUserID() + "/" + book.getBookId().hashCode() + ".png";
        final StorageReference ref = firebaseHandler.getStorageRef().child(filenames);
        UploadTask uploadTask = ref.putBytes(data);



        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                getUriAndUpLoad(firebaseHandler.getStorageRef().child("image/" + firebaseHandler.getCurrentUser().getUserID() + "/" + book.getBookId().hashCode() + ".png"), false);
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }


    private void getUriAndUpLoad(StorageReference reference, final boolean place_holder) {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                book.setPhoto(String.valueOf(uri));
                //System.out.println(String.valueOf(book.getBookId()));
                //System.out.println(String.valueOf(uri));
                //System.out.println(String.valueOf(book.getPhoto()));
                DatabaseReference refB = firebaseHandler.getMyRef().child("books").child(book.getOwner().getUserID()).child(book.getBookId());
                refB.child("photo").setValue(String.valueOf(uri));
                if (place_holder){
                    Glide.with(BookDetailActivity.this).load(android.net.Uri.parse(book.getPhoto()))
                            .into(bookImage);
                }




                //finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BookDetailActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }




    private void setRequestList() {
        Bundle bundle = new Bundle();
        RequestListFragment requestListFragment = new RequestListFragment();

        bundle.putParcelable("book",book);
        requestListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.place_holder, requestListFragment)
                .show(requestListFragment).commit();
    }



    private void initViews() {
        title = findViewById(R.id.title);
        author = findViewById(R.id.author);
        owner = findViewById(R.id.owner_name);
        description = findViewById(R.id.description);
        bookImage = findViewById(R.id.book_cover);
        delete = findViewById(R.id.delete_book);

        progressDialog = new ProgressDialog(BookDetailActivity.this);
        progressDialog.setMessage("Searching user...");
        inProgress = false;


    }

    private void setBookInfo() {
        TextView titleContent = title.findViewWithTag("content");
        TextView authorContent = author.findViewWithTag("content");
        TextView ownerContent = owner.findViewWithTag("content");
        TextView desContent = description.findViewWithTag("content");

        titleContent.setText(book.getTitle());
        authorContent.setText(book.getAuthor());
        ownerContent.setText(book.getOwner().getUsername());
        desContent.setText(book.getDescription());

        //bookImage.setImageURI(Uri.parse(book.getPhoto()));
        Glide.with(this).load(Uri.parse(book.getPhoto()))

                .into(bookImage);

        System.out.println(String.valueOf(book.getBookId()));
        System.out.println(String.valueOf(book.getPhoto()));
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
