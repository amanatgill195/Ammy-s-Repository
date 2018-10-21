package com.example.amanat.citytour;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.amanat.citytour.Authentication.SignUpLogin;
import com.example.amanat.citytour.Model.Users;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "";
    private static final int REQUEST_INVITE = 7575;
    private ImageButton ibAboutCity, ibPlacesToVisit, ibRestaurants;
    private ImageButton ibAboutTheCity, ibRestraunts, ibPubs, ibEvents, ibCityTour, ibHotspot, ibWeather;
    private TextView drawerLogout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase firebaseDatabase;
    private ImageView mDisplayImageView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private FirebaseFirestore db;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View decorView = getWindow().getDecorView();

        // Obtain the FirebaseAnalytics instance.
        // Trace myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setText("HOME");
        tv.setTextSize(25);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(Color.parseColor("#9f5d7b"));
        Typeface tf = Typeface.createFromAsset(getAssets(), "quicksand_regular.ttf");
        tv.setTypeface(tf, Typeface.BOLD);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //userInfo();
        //mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(MainMenu.this, SignUpLogin.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        };

        inItComponent();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeaderView = navigationView.getHeaderView(0);

        mDisplayImageView = (ImageView) navHeaderView.findViewById(R.id.navImageView);
        mNameTextView = (TextView) navHeaderView.findViewById(R.id.navNameText);
        mEmailTextView = (TextView) navHeaderView.findViewById(R.id.navEmailText);
        userInfo();
    }

    public void inItComponent() {
        ibAboutCity = findViewById(R.id.ibAboutCity);
        ibPlacesToVisit = findViewById(R.id.ibPlacestoVisit);
        ibRestaurants = findViewById(R.id.ibRestaurants);

        Picasso.get().load(R.drawable.derek_story_579524_unsplash).fit().into(ibAboutCity);
        Picasso.get().load(R.drawable.thomas_young_637663_unsplash).fit().into(ibPlacesToVisit);
        Picasso.get().load(R.drawable.summer_barbeque_feast).fit().into(ibRestaurants);

        ibAboutCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, AboutTheCity.class);
                startActivity(intent);
            }
        });

        ibPlacesToVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, Attractions.class));
            }
        });

        ibRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainMenu.this, RestaurantSwiper.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menuAboutUs:
                startActivity(new Intent(MainMenu.this, AboutUs.class));
                break;

            case R.id.menuContactUs:
                startActivity(new Intent(MainMenu.this, ContactUs.class));
                break;

            case R.id.menuLogOut:
                FirebaseAuth.getInstance().signOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_city) {
            // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            startActivity(new Intent(MainMenu.this, AboutTheCity.class));
            //drawer.closeDrawers();
        } else if (id == R.id.nav_attractions) {
            startActivity(new Intent(MainMenu.this, Attractions.class));

        } else if (id == R.id.nav_restaurants) {
            startActivity(new Intent(MainMenu.this, RestaurantSwiper.class));

        } else if (id == R.id.nav_share) {
            sendLink();
        } else if (id == R.id.nav_favourites) {
            startActivity(new Intent(MainMenu.this, BookmarkRestaurants.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        drawer.setFocusable(false);

        return true;
    }

    private void sendLink() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String sAux = "\nLet me recommend you this application\n\n";

            sAux = sAux + "https://play.google.com/store/apps/details?id=com.google.android.apps.searchlite \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private void userInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photoUrl = String.valueOf(user.getPhotoUrl());
            String phoneNumber = user.getPhoneNumber();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();

            Picasso.get().load(photoUrl).fit().into(mDisplayImageView);
            mNameTextView.setText(name);
            mEmailTextView.setText(email);

            firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            Users users = new Users(name, email, uid, phoneNumber);
            databaseReference.child("App Users").child(user.getUid()).setValue(users);

/*
                Map<String, Object> userdata = new HashMap<>();
                userdata.put("name", name);
                userdata.put("email", email);
                userdata.put("image", photoUrl);
                userdata.put("contactNumber", phoneNumber);
                userdata.put("uId", uid);


                // Add a new document with a generated ID

                db.collection("users").document(uid)
                        .set(userdata)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(MainMenu.this, "Document sent successfully", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(MainMenu.this, "Error writing document", Toast.LENGTH_LONG).show();
                            }
                        });*/
        }
    }
}
