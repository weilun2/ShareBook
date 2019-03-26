package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.content.Context;
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

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;

public class AcceptedFragment extends Fragment {
    private static final String TAG = "acce[ted fragment";
    private FirebaseHandler firebaseHandler;
    private Book book;
    private LocationManager locationManager;
    private String locationProvider;
    private TextView positionView;


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
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Button AddLoc = (Button) getActivity().findViewById(R.id.addLocBtn);
            System.out.print("created");




        firebaseHandler = new FirebaseHandler(getContext());
        if (getArguments() != null) {
            book = getArguments().getParcelable("book");
        }


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.location_auto_search);



// Set up a PlaceSelectionListener to handle the response.
        if (autocompleteFragment != null) {
            // Specify the types of place data to return.
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    // TODO: Get info about the selected place.
                    positionView.setText(place.getName());
                    //Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                }

                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    //Log.i(TAG, "An error occurred: " + status);
                }


            });
        }

        /*        PlaceAutocompleteFragment  placeAutocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.location_auto_search);



// Set up a PlaceSelectionListener to handle the response.
        if (placeAutocompleteFragment != null) {
            // Specify the types of place data to return.

            placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    positionView.setText(place.getName());
                }

                @Override
                public void onError(Status status) {

                }
            });
        }*/
    }



}
