package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
    Button addLocation;


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
            final TextView positionView= (TextView) getActivity().findViewById(R.id.locationView);
            final Context context = this.getContext();
            addLocation = getActivity().findViewById(R.id.addLocBtn);
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Button AddLoc = (Button) getActivity().findViewById(R.id.addLocBtn);
            System.out.print("created");




        firebaseHandler = new FirebaseHandler(getContext());
        if (getArguments() != null) {
            book = getArguments().getParcelable("book");
        }
        final Context fragment = getContext();

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fragment, MapsActivity.class);
                startActivityForResult(intent, 0x09);
            }
        });



    }



}
