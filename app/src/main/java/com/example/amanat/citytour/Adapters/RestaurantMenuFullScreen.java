package com.example.amanat.citytour.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.amanat.citytour.Model.ImageModel;
import com.example.amanat.citytour.R;
import com.example.amanat.citytour.RestaurantAdapters.RestaurantMenuFullScreenAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class RestaurantMenuFullScreen extends AppCompatActivity {
    private FirebaseFirestore db;
    private String docId, collection;
    private ArrayList<ImageModel> imageMenuModelArrayList;
    private RestaurantMenuFullScreenAdapter adapter;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_restaurant_menu_full_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.restaurantMenuFullScreenViewPager);
        circleIndicator = (CircleIndicator) findViewById(R.id.fullScreenDots);
        imageMenuModelArrayList = new ArrayList<>();

        bundle = getIntent().getExtras();
        docId = bundle.getString("docId");
        collection = bundle.getString("collection");
        db = FirebaseFirestore.getInstance();
        db.collection(collection).document(docId).collection("menu")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot querySnapshot : task.getResult()) {
                            ImageModel model = new ImageModel(querySnapshot.getString("image"));
                            imageMenuModelArrayList.add(model);
                        }
                        adapter = new RestaurantMenuFullScreenAdapter(imageMenuModelArrayList, RestaurantMenuFullScreen.this);
                        viewPager.setAdapter(adapter);
                        circleIndicator.setViewPager(viewPager);
                        adapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RestaurantMenuFullScreen.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
