package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.activities.BookDetailActivity;
import ca.ualberta.cmput301w19t05.sharebook.activities.MapsActivity;
import ca.ualberta.cmput301w19t05.sharebook.activities.ScanActivity;
import ca.ualberta.cmput301w19t05.sharebook.activities.UserProfile;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.User;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;

import static android.app.Activity.RESULT_OK;
/**
 * Fragment for a book's accepting condition
 */
public class AcceptedFragment extends Fragment {
    private static final String TAG = "accepted fragment";
    private FirebaseHandler firebaseHandler;
    private Book book;
    private TextView positionView;
    private LatLng currentLocation;
    private Button scan;
    TextView scan_done;
    String type;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_accepted, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //LayoutInflater inflater=getLayoutInflater();
        //View v = inflater.inflate(R.layout.fragment_accepted, null);
        positionView = getActivity().findViewById(R.id.locationView);
        final Context context = this.getContext();

        Log.d(TAG, "onActivityCreated: ");

        if (getArguments() != null) {
            book = getArguments().getParcelable("book");
            type = getArguments().getString("type");
        }
        firebaseHandler = new FirebaseHandler(getContext());
        final Context fragment = getContext();

        switch (type){
            case "accepted":
                positionView.setVisibility(View.VISIBLE);
                if (book.getOwner().getUserID().equals(firebaseHandler.getCurrentUser().getUserID())){
                    positionView.setText("Set Location");
                    positionView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(fragment, MapsActivity.class);
                            startActivityForResult(intent, 0x09);
                        }
                    });
                    final TextView requesterName =getActivity().findViewById(R.id.requester);
                    firebaseHandler.getMyRef().child("accepted").child(book.getBookId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot it : dataSnapshot.getChildren()){
                                final User temp = it.getValue(User.class);
                                if (temp!= null){
                                    requesterName.setVisibility(View.VISIBLE);
                                    requesterName.setText("Requester: "+temp.getUsername());
                                    requesterName.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getContext(), UserProfile.class);
                                            intent.putExtra("owner", temp);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    positionView.setText("Location not set yet");
                }

                firebaseHandler.getMyRef().child("Location").child(book.getBookId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Map<String, Double> temp = (Map<String, Double>) dataSnapshot.getValue();
                        if (temp != null) {
                            positionView.setText("Latitude: " + temp.get("latitude") + "\nLongitude: " + temp.get("longitude"));
                            Geocoder geocoder = new Geocoder(getActivity());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(temp.get("latitude"), temp.get("longitude"),1);
                                Address obj = addresses.get(0);
                                String res = obj.getAddressLine(0);
                                positionView.setText(res);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (!book.getOwner().getUserID().equals(firebaseHandler.getCurrentUser().getUserID())){
                                positionView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+temp.get("latitude")+","+temp.get("longitude"));
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        if (mapIntent.resolveActivity(getActivity().getPackageManager())!=null){
                                            startActivity(mapIntent);
                                        }
                                    }
                                });

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            case "return":

                scan = getActivity().findViewById(R.id.scan_book_button);
                scan_done = getActivity().findViewById(R.id.scan_done);
                firebaseHandler.getMyRef().child(FirebaseHandler.LENT_SCAN).child(book.getBookId())
                        .child(firebaseHandler.getCurrentUser().getUserID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Integer a = dataSnapshot.getValue(Integer.class);
                        if (a!=null && a==1){
                            scan.setVisibility(View.GONE);
                            scan_done.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), ScanActivity.class);
                        startActivityForResult(intent, ScanActivity.SCAN_BOOK);
                    }
                });
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x10 && requestCode == 0x09) {
            if (data != null) {
                currentLocation = data.getParcelableExtra("location");
                firebaseHandler.addLocation(book,currentLocation);
            }
        }
        else if (requestCode == ScanActivity.SCAN_BOOK&& resultCode == RESULT_OK){
            String ISBN = data.getStringExtra("ISBN");
            if (book.getISBN().equals(ISBN)){
                if (type.equals("return")){
                    View view = View.inflate(getActivity(), R.layout.content_edit, null);
                    final EditText userInput = view.findViewById(R.id.user_input);;
                    AlertDialog dialog= new AlertDialog.Builder(getActivity()).setView(view)
                            .setMessage("input your rate form 0 to 10")
                            .setPositiveButton("submit", null)
                            .create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                            String a = userInput.getText().toString();
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String a = userInput.getText().toString();
                                    if (!"".equals(a)){
                                        int res = Integer.valueOf(userInput.getText().toString());
                                        if (res>=0&&res<=10){
                                            if (firebaseHandler.getCurrentUser().getUserID().equals(book.getOwner().getUserID())){
                                                firebaseHandler.returnBook(book, res);
                                                if(getActivity()!=null)
                                                    getActivity().finish();
                                            }else{
                                                firebaseHandler.returned(book, res);
                                                if(getActivity()!=null)
                                                    getActivity().finish();
                                            }

                                        }
                                        else {
                                            userInput.setError("rate must be an integer from 0 to 10");
                                            userInput.requestFocus();
                                        }
                                    }
                                }
                            });

                        }
                    });
                    dialog.show();


                }else {
                    if (firebaseHandler.getCurrentUser().getUserID().equals(book.getOwner().getUserID())) {
                        firebaseHandler.confirmLent(book);
                        getActivity().finish();

                    } else {
                        firebaseHandler.confirmBorrowing(book);
                        getActivity().finish();
                    }
                }


            }
            else {
                Toast.makeText(getActivity(), "ISBN does not match", Toast.LENGTH_LONG).show();
            }
        }

    }

}
