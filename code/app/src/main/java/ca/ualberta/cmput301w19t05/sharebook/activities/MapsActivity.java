package ca.ualberta.cmput301w19t05.sharebook.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import ca.ualberta.cmput301w19t05.sharebook.R;
/**
 * A map screen shows the location by google map
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "onMap";
    private static final int DEFAULT_ZOOM = 15;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0x0011;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private Location mLastKnownLocation;
    private LatLng resLocation;
    private LatLng mDefaultLocation;
    private Marker mapMaker;
    private FloatingActionButton myLocation;
    private Button submitLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Initialize Places.
        if (!com.google.android.libraries.places.api.Places.isInitialized()) {
            com.google.android.libraries.places.api.Places.initialize(getApplicationContext(), "AIzaSyCbSE_0Li8h1OHDnmzgMEHrBbFj8QHNNcY");
        }

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mDefaultLocation =new LatLng(-34, 151);

        getLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.location_auto_search);

        initClick();





// Set up a PlaceSelectionListener to handle the response.
        if (autocompleteFragment != null) {
            // Specify the types of place data to return.
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,
                    Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {

                    // TODO: Get info about the selected place.
                    LatLng res = place.getLatLng();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(res, DEFAULT_ZOOM));
                    if (res != null) {
                        mapMaker.setPosition(res);
                        resLocation = res;
                    }

                    //Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                }

                @Override
                public void onError(@NonNull Status status) {
                    // TODO: Handle the error.
                    //Log.i(TAG, "An error occurred: " + status);
                }


            });
        }
    }

    private void initClick() {
        myLocation = findViewById(R.id.goto_my_location);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
        submitLocation = findViewById(R.id.submit_location);
        submitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putDouble("Latitude", mLastKnownLocation.getLatitude());
                            bundle.putDouble("Longitude", mLastKnownLocation.getLongitude());
                            intent.putExtras(bundle);
                            setResult(0x10, intent);//返回值调用函数，返回值的标志*/
                Intent intent = new Intent(MapsActivity.this,BookDetailActivity.class);
                intent.putExtra("location",resLocation);
                setResult(0x10,intent);
                finish();

            }
        });
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                LatLng center = mMap.getCameraPosition().target;
                if (mapMaker==null){
                    mapMaker = mMap.addMarker(new MarkerOptions().position(center));
                }
                else{
                    mapMaker.setPosition(center);
                }
                resLocation = center;

            }
        });

/*        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        updateLocationUI();
        getDeviceLocation();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                    @Override
                    public void onComplete(@NonNull Task<android.location.Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            LatLng currentLocation = new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    currentLocation, DEFAULT_ZOOM));
                            if (mapMaker==null){
                                mapMaker = mMap.addMarker(new MarkerOptions().position(currentLocation));
                            }
                            else{
                                mapMaker.setPosition(currentLocation);
                            }
                            resLocation = currentLocation;
/*                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putDouble("Latitude", mLastKnownLocation.getLatitude());
                            bundle.putDouble("Longitude", mLastKnownLocation.getLongitude());
                            intent.putExtras(bundle);
                            setResult(0x10, intent);//返回值调用函数，返回值的标志*/
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


}
