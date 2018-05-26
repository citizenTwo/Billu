package xyz.mrdeveloper.social;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.login.LoginManager;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {

    @BindView(R.id.materialViewPagerStore)
    MaterialViewPager materialViewPagerStore;

    private static final int NUM_TABS = 3;

//    @BindView(R.pid.materialViewPagerCheckout)
//    MaterialViewPager materialViewPagerCheckout;

    ArrayList<String> headingsStore = new ArrayList<String>();
    ArrayList<String> headingsCheckout = new ArrayList<String>();

    ArrayList<Integer> drawablesStore = new ArrayList<Integer>();
    ArrayList<Integer> drawablesCheckout = new ArrayList<Integer>();

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    private BeaconManager mBeaconManager;
    private Bundle bundleOfJoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkLocationPermission();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.pid.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Opening Checkout...", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//                Intent i = new Intent(HomeActivity.this, CheckoutActivity.class);
//                startActivity(i);
//            }
//        });

        setTitle("");
        ButterKnife.bind(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, 0, 0);
        mDrawer.addDrawerListener(mDrawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        final Toolbar toolbar = materialViewPagerStore.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        headingsStore.add("Featured");
        headingsStore.add("New Arrivals");
        headingsStore.add("On Sale");

        headingsCheckout.add("Checkout");

        drawablesStore.add(R.drawable.people_shopping);
        drawablesStore.add(R.drawable.new_arrival);
        drawablesStore.add(R.drawable.on_sale);

        drawablesCheckout.add(R.drawable.social_logo);

        SetupMaterialViewPager(NUM_TABS, headingsStore, drawablesStore);


        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    materialViewPagerStore.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        // Detect the main Eddystone-UID frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        // Detect the telemetry (TLM) frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));
        // Detect the URL frame:
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        mBeaconManager.bind(this);

        ImageView imageProfile = navigationView.getHeaderView(0).findViewById(R.id.image_profile);
        TextView textUsername = navigationView.getHeaderView(0).findViewById(R.id.text_username);

        bundleOfJoy = getIntent().getExtras();
        textUsername.setText("Hi, " + bundleOfJoy.getString("FirstName"));

        Glide.with(this)
                .load(bundleOfJoy.getString("URLs"))
                .thumbnail(0.5f)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                .into(imageProfile);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
//            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);  // OPEN DRAWER
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.button_logout) {
            // Handle the camera action
            LoginManager.getInstance().logOut();
            Intent login = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }
// else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBeaconManager.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        try {
            mBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
        }

        mBeaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i("debug", "I just saw a beacon for the first time!");
                runOnUiThread(new Runnable() {
                    public void run() {


//                        ///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                      FIRESTORE METHOD
//                        ///////////////////////////////////////////////////////////////////////////////////////////////////

                        HashMap<String, String> status = new HashMap<>();
                        status.put("step","1");
                        status.put("name",bundleOfJoy.getString("FirstName") + " " + bundleOfJoy.getString("LastName"));
                        status.put("payment","Pending");

                        DocumentReference db = FirebaseFirestore.getInstance().document("process/checkout");

                        db.set(status,SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Log.d("FireStore","Checkout Step 1 SUCCESSFUL");
                                else
                                    Log.d("FireStore","Checkout Step 1 FAILED " + task.getException());
                            }
                        });
//                        ///////////////////////////////////////////////////////////////////////////////////////////////////////

//                        ///////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                      FIREBASE METHOD
//                        ///////////////////////////////////////////////////////////////////////////////////////////////////

//                        FirebaseDatabase.getInstance().getReference().child("checkout_step").setValue("1");
//                        FirebaseDatabase.getInstance().getReference().child("customer_name").setValue(bundleOfJoy.getString("FirstName") + " " + bundleOfJoy.getString("LastName"));
//                        FirebaseDatabase.getInstance().getReference().child("payment_status").setValue("Pending");

//                        ///////////////////////////////////////////////////////////////////////////////////////////////////////


                        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                        startActivity(intent);

//                        SetupMaterialViewPager(1, headingsCheckout, drawablesCheckout);
//                        materialViewPagerStore.notifyHeaderChanged();

//                        textCheckout.setText("Getting your scanned items!!");
                    }
                });
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i("debug", "I no longer see a beacon");
                runOnUiThread(new Runnable() {
                    public void run() {
//                        SetupMaterialViewPager(NUM_TABS, headingsStore, drawablesStore);
//                        materialViewPagerStore.notifyHeaderChanged();

//                        textCheckout.setText("Not in a checkout zone!\nPlease go to a checkout zone.");
                    }
                });
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i("debug", "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

//        Log.i("debug","Adding range notifier");
//        mBeaconManager.addRangeNotifier(new RangeNotifier() {
//
//            @Override
//            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//                if (beacons.size() > 0) {
//                    Log.i("debug", "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
//                } else {
//                    Log.i("debug", "No beacons found!");
//                }
//            }
//        });
    }

//    @Override
//    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
//        for (final Beacon beacon : beacons) {
//            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
//                // This is a Eddystone-UID frame
//                final Identifier namespaceId = beacon.getId1();
//                Identifier instanceId = beacon.getId2();
//                Log.d("debug", "I see a beacon transmitting namespace pid: " + namespaceId +
//                        " and instance pid: " + instanceId +
//                        " approximately " + beacon.getDistance() + " meters away.");
//                runOnUiThread(new Runnable() {
//                    public void run() {
//
//                    }
//                });
//            }
//        }
//    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("This app needs location access")
                        .setMessage("Please grant location access so this app can detect beacons.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    private void SetupMaterialViewPager(final int nItems, final ArrayList<String> headings, final ArrayList<Integer> drawables) {

        materialViewPagerStore.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
//                Log.i("debug", "In " + position % nItems + " fragment");
                RecyclerViewFragment temp = RecyclerViewFragment.newInstance();
                temp.CURRENT_TAB = position % nItems;
                temp.GRID_LAYOUT = nItems > 1;
                return temp;
            }

            @Override
            public int getCount() {
                return nItems;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return headings.get(position % nItems);
            }
        });

        materialViewPagerStore.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.colorHeader1,
                                getDrawable(drawables.get(0 % nItems)));
                    case 1:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.colorHeader2,
                                getDrawable(drawables.get(1 % nItems)));
                    case 2:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.colorHeader3,
                                getDrawable(drawables.get(2 % nItems)));
//                    case 3:
//                        return HeaderDesign.fromColorResAndDrawable(
//                                R.color.red,
//                                getDrawable(drawables.get(3 % nItems)));
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        materialViewPagerStore.getViewPager().setOffscreenPageLimit(materialViewPagerStore.getViewPager().getAdapter().getCount());
        materialViewPagerStore.getPagerTitleStrip().setViewPager(materialViewPagerStore.getViewPager());
    }
}
