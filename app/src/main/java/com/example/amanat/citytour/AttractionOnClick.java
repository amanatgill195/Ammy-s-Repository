package com.example.amanat.citytour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanat.citytour.Adapters.AttractionImageSliderAdapter;
import com.example.amanat.citytour.Model.AttractionsClickModel;
import com.example.amanat.citytour.Model.ImageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class AttractionOnClick extends AppCompatActivity {
    private static final String TAG = "";
    private ImageView hotspotImageView;
    private TextView hotspotName, aboutHotspot, hotspotTiming;
    private ArrayList<AttractionsClickModel> attractionsClickModels;
    private FirebaseFirestore db;
    private AttractionsClickModel modelClass;
    private Bundle bundle;
    private ImageButton ibCab, ibDirections;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
    private AttractionImageSliderAdapter attractionImageSliderAdapter;
    private String docId;
    private AttractionOnClick attractionOnClick;
    private int page_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_attraction_on_click);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setText("Attractions");
        tv.setTextSize(25);
        tv.setTextColor(Color.parseColor("#9f5d7b"));
        Typeface tf = Typeface.createFromAsset(getAssets(), "quicksand_regular.ttf");
        tv.setTypeface(tf, Typeface.BOLD);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // hotspotImageView = findViewById(R.id.hotspotDetailImage);
        hotspotName = findViewById(R.id.hotspotName);
        aboutHotspot = findViewById(R.id.aboutHotspot);
        hotspotTiming = findViewById(R.id.hotspotTiming);
        ibDirections = findViewById(R.id.hotspotDirections);
        ibCab = findViewById(R.id.hotspotCab);
        viewPager = findViewById(R.id.attractionViewPager);
        circleIndicator = findViewById(R.id.attractionCircleIndicator);

        db = FirebaseFirestore.getInstance();
        bundle = getIntent().getExtras();
        final String name = bundle.getString("name");
        String image = bundle.getString("image");
        String aboutPlace = bundle.getString("aboutPlace");
        String timing = bundle.getString("timing");
        docId = bundle.getString("documentId");

        hotspotName.setText(name);
        //   Picasso.get().load(image).fit().into(hotspotImageView);
        hotspotTiming.setText(timing);
        aboutHotspot.setText(aboutPlace);

        ibDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + name);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(AttractionOnClick.this, "Please check your Internet", Toast.LENGTH_LONG).show();
                }
            }
        });

        ibCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AttractionOnClick.this, "This feature will be available soon!", Toast.LENGTH_LONG).show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttractionOnClick.this, Attractions.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        imageSlider();
    }

    private void imageSlider() {
        if (isNetworkAvailable()) {
            db.collection("hotspots").document(docId).collection("images")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot querySnapshot : task.getResult()) {
                                ImageModel imageModel = new ImageModel(querySnapshot.getString("image"));
                                imageModelArrayList.add(imageModel);
                            }
                            attractionImageSliderAdapter = new AttractionImageSliderAdapter(imageModelArrayList, attractionOnClick);
                            viewPager.setAdapter(attractionImageSliderAdapter);
                            circleIndicator.setViewPager(viewPager);
                            attractionImageSliderAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AttractionOnClick.this, "Something went Wrong", Toast.LENGTH_LONG).show();
                }
            });

            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (page_position == imageModelArrayList.size()) {
                        page_position = 0;
                    } else {
                        page_position = page_position + 1;
                    }
                    viewPager.setCurrentItem(page_position, true);
                }
            };

            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    handler.post(update);
                }
            }, 400, 4000);
        } else {
            Toast.makeText(AttractionOnClick.this, "Please Check Your Internet!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
