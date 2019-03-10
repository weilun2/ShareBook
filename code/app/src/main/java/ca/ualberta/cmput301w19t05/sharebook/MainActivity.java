package ca.ualberta.cmput301w19t05.sharebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ca.ualberta.cmput301w19t05.sharebook.fragments.BorrowingFragment;
import ca.ualberta.cmput301w19t05.sharebook.fragments.MyShelfFragment;
import ca.ualberta.cmput301w19t05.sharebook.fragments.NotificationFragment;

/**
 * MainActivity
 *      the homepage shown after a successful login
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MyShelfFragment myShelfFragment;
    private BorrowingFragment borrowingFragment;
    private NotificationFragment notificationFragment;
    private NavigationView drawerNavigationView;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton sign_out_button;
    private View headerView;
    private FirebaseUser user;
    private Fragment[] fragments;
    private int lastFragment;
    private FragmentManager supportFragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    if (lastFragment != 0) {
                        switchFragment(lastFragment, 0);
                        lastFragment = 0;
                    }
                    return true;
                }
                case R.id.navigation_borrowing: {
                    if (lastFragment != 1) {
                        switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                    return true;
                }
                case R.id.navigation_notifications: {
                    if (lastFragment != 2) {
                        switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                    return true;
                }
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.string.add_book:
                        Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
                        startActivity(intent);
                }
                return true;
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawerNavigationView = findViewById(R.id.nav_view);
        drawerNavigationView.setNavigationItemSelectedListener(this);
        initDrawer();

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragment();
    }


    private void initDrawer() {
        headerView = drawerNavigationView.getHeaderView(0);
    }


    @Override
    protected void onStart() {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onStart();
            ImageView navImageView = headerView.findViewById(R.id.imageView);

            TextView navUsername = headerView.findViewById(R.id.username);
            TextView navEmail = headerView.findViewById(R.id.email);

            Uri photoUrl = user.getPhotoUrl();
            navImageView.setImageURI(photoUrl);

            String email = user.getEmail();
            navEmail.setText(email);
            String username = user.getDisplayName();
            navUsername.setText(username);

            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, UserProfile.class);
                    startActivity(intent);
                }
            });
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, as long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {




        } else if (id == R.id.log_out) {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("Do you want to logout")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("no", null)
                    .show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initFragment() {
        myShelfFragment = new MyShelfFragment();
        borrowingFragment = new BorrowingFragment();
        notificationFragment = new NotificationFragment();
        fragments = new Fragment[]{myShelfFragment, borrowingFragment, notificationFragment};
        lastFragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, myShelfFragment)
                .show(myShelfFragment).commit();


    }

    private void switchFragment(int lastFragment, int index) {


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastFragment]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.main_container, fragments[index]);

        }
        transaction.show(fragments[index]).commitAllowingStateLoss();

    }

}
