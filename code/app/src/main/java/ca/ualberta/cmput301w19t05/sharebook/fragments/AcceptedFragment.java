package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.activities.MapsActivity;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;

public class AcceptedFragment extends Fragment {
    private static final String TAG = "acce[ted fragment";
    private FirebaseHandler firebaseHandler;
    private Book book;
    private LocationManager locationManager;
    private String locationProvider;
    private TextView positionView;
    private LatLng currentLocation;


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
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        System.out.print("created");



        if (getArguments() != null) {
            book = getArguments().getParcelable("book");
        }
        firebaseHandler = new FirebaseHandler(getContext());
        firebaseHandler.getMyRef().child("Location").child(book.getBookId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String, Double> temp = (Map<String, Double>) dataSnapshot.getValue();
                if (temp != null) {
                    positionView.setText("Latitude: " + temp.get("latitude") + "\nLongitude: " + temp.get("longitude"));
                    //positionView.setText(temp.toString());

                }else {
                    positionView.setText("Click me to add location");
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final Context fragment = getContext();

        positionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment, MapsActivity.class);
                startActivityForResult(intent, 0x09);
            }
        });


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

    }

}
