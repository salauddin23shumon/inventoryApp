package com.sss.myinventoryapp;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.sss.myinventoryapp.interfaces.ChangeHomeNavListener;
import com.sss.myinventoryapp.interfaces.LocationServiceListener;
import com.sss.myinventoryapp.services.LocationUpdatesService;
import com.sss.myinventoryapp.utility.SessionManager;
import com.sss.myinventoryapp.utility.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.sss.myinventoryapp.utility.Constant.FILE_NAME;
import static com.sss.myinventoryapp.utility.Constant.IS_SERVICE_ACTIVE;
import static com.sss.myinventoryapp.utility.Utils.requestingLocationUpdates;

public class MainActivity extends AppCompatActivity implements ChangeHomeNavListener,
        LocationServiceListener, NavController.OnDestinationChangedListener {

    private static final String TAG = "MainActivity";
    private Menu menu;
    private Toolbar toolbar;
    private NavGraph navGraph;
    private NavController navController;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private AppBarConfiguration appBarConfiguration;

    // Used in checking for runtime permissions.
    private static final int REQUEST_LOCATION_PERMISSIONS_CODE = 34;

    // A reference to the service used to get location updates.
    private LocationUpdatesService locationService = null;

    // Tracks the bound state of the service.
    private boolean isBound = false;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            locationService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
            isBound = false;
        }
    };
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_SERVICE_ACTIVE, false);

        sessionManager = new SessionManager(this);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(this);

        NavigationUI.setupWithNavController(navigationView, navController);


        if (requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        if (isBound) {
            unbindService(mServiceConnection);
            isBound = false;
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.enable_gps:
                if (!requestingLocationUpdates(this)) {
                    if (!checkPermissions()) {
                        requestPermissions();
                    } else {
                        locationService.requestLocationUpdates();
                        updateMenuTitles(true);
                    }
                } else {
                    locationService.removeLocationUpdates();
                    updateMenuTitles(false);
                }
                break;
            case R.id.logout:
                if (sessionManager.getAppUser() != null) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signOut();
                    sessionManager.clearSession();
                    navController.navigate(R.id.action_global_nav_signin);
                    if (requestingLocationUpdates(this))
                        locationService.removeLocationUpdates();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                    Log.e(TAG, "onLogout: called");
                } else
                    Log.e(TAG, "onLogout: user=null");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateMenuTitles(boolean isActive) {
        MenuItem bedMenuItem = menu.findItem(R.id.enable_gps);
        if (isActive) {
            bedMenuItem.setTitle("Disable Tracking");
        } else {
            bedMenuItem.setTitle("Enable Tracking");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onHomeChange(int fragmentId) {
        navGraph = navController.getNavInflater().inflate(R.navigation.nav_graph);
        Log.e(TAG, "onHomeChange: ");
        if (fragmentId == R.id.nav_home) {
            navGraph.setStartDestination(fragmentId);
            navController.setGraph(navGraph);

            appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home,R.id.nav_gallery,R.id.nav_slideshow)
                    .setOpenableLayout(drawer)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        } else if (fragmentId == R.id.nav_signin) {
            navGraph.setStartDestination(fragmentId);
            navController.setGraph(navGraph);
        }
    }

    @Override
    public void onStartService() {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            locationService.requestLocationUpdates();
        }
    }

    @Override
    public void onStopService() {
        locationService.removeLocationUpdates();
    }

    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.nav_host_fragment),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_LOCATION_PERMISSIONS_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateMenuTitles(requestingLocationUpdates(this));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        Log.e(TAG, "onDestinationChanged: " + destination.getId());
        switch (destination.getId()) {
            case R.id.nav_start_splash:
                toolbar.setVisibility(View.GONE);
                break;
            case R.id.nav_home:
                toolbar.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_welcome_splash:
                toolbar.setVisibility(View.GONE);
                break;
            case R.id.nav_signin:
                toolbar.setVisibility(View.GONE);

                break;
            case R.id.nav_signup:
                toolbar.setVisibility(View.GONE);
                break;
        }
    }
}