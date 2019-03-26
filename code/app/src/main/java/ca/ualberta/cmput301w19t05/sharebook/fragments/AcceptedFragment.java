package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.activities.MainActivity;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;


import static android.content.Context.LOCATION_SERVICE;

public class AcceptedFragment extends Fragment {
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

        AddLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> providers = locationManager.getProviders(true);

                if(providers.contains(LocationManager.GPS_PROVIDER)){
                    //For GPS
                    locationProvider = LocationManager.GPS_PROVIDER;
                }else if(providers.contains(LocationManager.NETWORK_PROVIDER)){
                    //For Network
                    locationProvider = LocationManager.NETWORK_PROVIDER;
                }else{
                    Toast.makeText(getActivity(), "No location providers available", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                }
                Location location = locationManager.getLastKnownLocation(locationProvider);
                if(location!=null){
                    //set location:
                    showLocation(location);
                }else{
                    LocationListener locationListener = new LocationListener() {
                        public void onLocationChanged(Location location) {
                        }

                        public void onStatusChanged(String provider, int status, Bundle extras) {
                        }

                        public void onProviderEnabled(String provider) {
                        }

                        public void onProviderDisabled(String provider) {
                        }
                    };
                    locationManager.requestLocationUpdates(locationProvider, 1000, 0, locationListener);
                    location = locationManager.getLastKnownLocation(locationProvider);
                    if(location!=null) {
                        //set location:
                        showLocation(location);
                    }


                }
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }



        });


    }
    private void showLocation(Location location){
        String locationStr = "latitude：" + location.getLatitude() +"\n"
                + "Longitude：" + location.getLongitude();
        System.out.print(locationStr);
        positionView.setText(locationStr);
    }


}
