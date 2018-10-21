package com.example.amanat.citytour;

import android.annotation.SuppressLint;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanat.citytour.Model.ImageMenuModel;
import com.example.amanat.citytour.Model.ImageModel;
import com.example.amanat.citytour.Model.MenuCollectionModel;
import com.example.amanat.citytour.Model.RestaurantCommentModel;
import com.example.amanat.citytour.RestaurantAdapters.RestaurantCommentAdapter;
import com.example.amanat.citytour.RestaurantAdapters.RestaurantImageMenuAdapter;
import com.example.amanat.citytour.RestaurantAdapters.RestaurantImageSliderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class RestaurantDetail extends AppCompatActivity {
    private static final String TAG = "";
    private static int set = 0;
    private static String convertRating;
    private TextView restrauntAddress, timing, aboutPlace, estimatedCost, fullAddress, paymentOptions, restrauntName, specials, restrauntStatus;
    private TextView txtAverageRating;
    private Button btComment;
    private ImageButton btCab, btCab1, btCall, btCall1, btDirections, btDirections1, btFavorite, btFavourite1;
    private RatingBar ratingBar;
    private EditText etComment;
    private Bundle bundle;
    private Button btOrderNow;
    private FirebaseUser user;
    private String userName, userImage, userEmail, phoneNumber, uId, comment;
    private FirebaseFirestore db;
    private String restrauntAddressDb, openingTimeDb, closingTimeDb, aboutPlaceDb, estimatedCostDb, fullAddressDb, paymentOptionsDb, contactNumberDb, specialsDb, ratingDb;
    private ArrayList<RestaurantCommentModel> commentModels;
    private RestaurantCommentAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView menuRecycler;
    private RestaurantImageMenuAdapter menuAdapter;
    private ArrayList<ImageMenuModel> imageMenuModelArrayList;
    private String name, image, docId;
    private String latDestination, lonDestination;
    private DocumentReference docRef;
    private float getrating, sum, databaseRating;
    private ArrayList<Float> ratingArrayList = new ArrayList<>();
    private int j;
    private SimpleDateFormat simpleDateFormat;
    private String time, restaurantTiming, compareOpen, compareClose;
    private Calendar calendar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
    private RestaurantImageSliderAdapter restaurantImageSliderAdapter;
    private int page_position = 0;
    private CircleIndicator circleIndicator;
    private ViewPager viewPager;
    private Typeface tf;
    private String deliveryStatus = "Don't Deliver", deliver;
    private String newRating, collection, rating;
    private ArrayList<MenuCollectionModel> menuCollectionModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_restaurant_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setText("RESTAURANTS");
        tv.setTextSize(25);
        tv.setTextColor(Color.parseColor("#9f5d7b"));
        tf = Typeface.createFromAsset(getAssets(), "quicksand_regular.ttf");
        tv.setTypeface(tf, Typeface.BOLD);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getId();

        bundle = getIntent().getExtras();
        name = bundle.getString("name");
        image = bundle.getString("image");
        docId = bundle.getString("documentId");
        latDestination = bundle.getString("latDestination");
        lonDestination = bundle.getString("lonDestination");
        deliver = bundle.getString("deliveryStatus");
        collection = bundle.getString("collection");
        rating = bundle.getString("rating");

        MenuCollectionModel model = new MenuCollectionModel(collection, docId);
        menuCollectionModelArrayList.add(model);
        restrauntName.setText(name);
        txtAverageRating.setText(rating);
        //Picasso.get().load(image).fit().into(restrauntDetailImage);

        if (deliveryStatus.equals(deliver)) {
            btOrderNow.setVisibility(View.INVISIBLE);
        } else {

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetail.this, RestaurantSwiper.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();
        loadFirestoreViewData();
        getCurrentUser();
        showMenu();
        showComment();
        favourites();
        cab();
        time();
        imageSlider();

        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:" + contactNumberDb;
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                    startActivity(dialIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Your call has failed...",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        btCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uri = "tel:" + contactNumberDb;
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                    startActivity(dialIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Your call has failed...", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        btDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + name);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(RestaurantDetail.this, "Please check your Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btDirections1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + name);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(RestaurantDetail.this, "Please check your Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(RestaurantDetail.this, RestaurantOrderMenu.class);
                // bundle.putString("documentId", docId);
                // intent.putExtras(bundle);
                //startActivity(intent);
                //startActivity(new Intent(RestaurantDetail.this, OrderSummary.class));
                //finish();
                Toast.makeText(RestaurantDetail.this, "This feature will be available soon!", Toast.LENGTH_LONG).show();
            }
        });


        btComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    addCommentToFirestore();
                    Rating();
                } else {
                    Toast.makeText(RestaurantDetail.this, "Please check your Internet Connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        mSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#ff4b00"));
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        myUpdateOperation();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    private void myUpdateOperation() {
        db = FirebaseFirestore.getInstance();
        loadFirestoreViewData();
        getCurrentUser();
        showMenu();
        showComment();
        favourites();
        cab();
        time();
    }

    private void loadFirestoreViewData() {
        db.collection(collection).document(docId).collection("restaurantDetails")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot querySnapshot : task.getResult()) {
                            aboutPlaceDb = querySnapshot.getString("aboutPlace");
                            contactNumberDb = querySnapshot.getString("contactNumber");
                            estimatedCostDb = querySnapshot.getString("estimatedCost");
                            fullAddressDb = querySnapshot.getString("fullAddress");
                            paymentOptionsDb = querySnapshot.getString("paymentOptions");
                            restrauntAddressDb = querySnapshot.getString("address");
                            openingTimeDb = querySnapshot.getString("openingTime");
                            closingTimeDb = querySnapshot.getString("closingTime");
                            specialsDb = querySnapshot.getString("specials");
                        }
                        aboutPlace.setText(aboutPlaceDb);
                        estimatedCost.setText(estimatedCostDb);
                        fullAddress.setText(fullAddressDb);
                        paymentOptions.setText(paymentOptionsDb);
                        restrauntAddress.setText(restrauntAddressDb);
                        restaurantTiming = openingTimeDb + "-" + closingTimeDb;
                        timing.setText(restaurantTiming);
                        specials.setText(specialsDb);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RestaurantDetail.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Something went wrong", e);
                    }
                });
    }

    private void getId() {
        restrauntAddress = (TextView) findViewById(R.id.restrauntAddress);
        fullAddress = (TextView) findViewById(R.id.restrauntFullAddress);
        aboutPlace = (TextView) findViewById(R.id.txtAboutPlace);
        timing = (TextView) findViewById(R.id.restauntTimings);
        estimatedCost = (TextView) findViewById(R.id.estimatedCost);
        paymentOptions = (TextView) findViewById(R.id.paymentOptions);
        restrauntName = (TextView) findViewById(R.id.txtRestrauntName);
        //restrauntDetailImage = (ImageView) findViewById(R.id.restrauntImage1);
        btCab = (ImageButton) findViewById(R.id.ibCab);
        btCab1 = (ImageButton) findViewById(R.id.ibCab1);
        btCall = (ImageButton) findViewById(R.id.ibCall);
        btCall1 = (ImageButton) findViewById(R.id.ibCall1);
        btDirections = (ImageButton) findViewById(R.id.ibDirections);
        btDirections1 = (ImageButton) findViewById(R.id.ibDirections1);
        btFavorite = (ImageButton) findViewById(R.id.ibFavorites);
        btFavourite1 = (ImageButton) findViewById(R.id.ibFavorites1);
        ratingBar = (RatingBar) findViewById(R.id.ratingStar);
        txtAverageRating = (TextView) findViewById(R.id.txtAverageRating);
        btOrderNow = findViewById(R.id.btOrderNow);
        etComment = findViewById(R.id.etRestrauntComment);
        btComment = findViewById(R.id.btComment);
        specials = findViewById(R.id.txtSpecials);
        restrauntStatus = findViewById(R.id.restrauntStatus1);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshRestaurantDetail);
        viewPager = findViewById(R.id.restrauntImageViewPager);
        circleIndicator = findViewById(R.id.viewPagerDots);
    }

    private void getCurrentUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            try {
                userName = user.getDisplayName();
                //Toast.makeText(restaurantDetail,userName,Toast.LENGTH_LONG).show();
                userImage = String.valueOf(user.getPhotoUrl());
                userEmail = user.getEmail();
                phoneNumber = user.getPhoneNumber();
                uId = user.getUid();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addCommentToFirestore() {
        if (etComment.getText() != null && !etComment.getText().toString().isEmpty()) {
            comment = etComment.getText().toString().trim();
            Map<String, Object> users = new HashMap<>();
            users.put("userName", userName);
            users.put("userEmail", userEmail);
            users.put("phoneNumber", phoneNumber);
            users.put("image", userImage);
            users.put("uId", uId);
            users.put("comment", comment);

            db.collection(collection).document(docId).collection("Comments").document(uId)
                    .set(users)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            etComment.setText("");
                            Toast.makeText(RestaurantDetail.this, "Comment Successfully", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                            Toast.makeText(RestaurantDetail.this, "Something went Wrong", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


    private void showComment() {
        commentModels = new ArrayList<>();
        setRecyclerView();
        db = FirebaseFirestore.getInstance();
        loadFirestore();
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.commentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadFirestore() {
        if (isNetworkAvailable()) {
            if (commentModels.size() > 0)
                commentModels.clear();
            db.collection(collection).document(docId).collection("Comments")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot querySnapshot : task.getResult()) {
                                RestaurantCommentModel model = new RestaurantCommentModel(querySnapshot.getString("userName")
                                        , querySnapshot.getString("image")
                                        , querySnapshot.getString("comment"));

                                commentModels.add(model);
                            }
                            adapter = new RestaurantCommentAdapter(commentModels, RestaurantDetail.this);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RestaurantDetail.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Something went wrong", e);
                        }
                    });
        } else {
            Toast.makeText(RestaurantDetail.this, "Unable to load data Please Check your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public void favourites() {
        docRef = db.collection("users").document(uId).collection("userFavourites").document("Restaurants").collection("favRestaurants").document(name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    btFavorite.setBackgroundResource(R.drawable.favourite_selected);
                    set = 0;
                } else {
                    btFavorite.setBackgroundResource(R.drawable.favourite);
                    set = 1;
                }
            }
        });

        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    if (set == 0) {
                        db.collection("users").document(uId).collection("userFavourites").document(name)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        btFavorite.setBackgroundResource(R.drawable.favourite);
                                        btFavourite1.setBackgroundResource(R.drawable.favourite);
                                        Toast.makeText(RestaurantDetail.this, "Bookmark Removed!", Toast.LENGTH_LONG).show();
                                        set = 1;
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting bookmark", e);
                                    }
                                });

                    } else {

                        Map<String, Object> userFav = new HashMap<>();
                        userFav.put("favRestaurantDocId", docId);
                        userFav.put("restaurantName", name);
                        userFav.put("restaurantImage", image);
                        userFav.put("restaurantCollection", collection);

                        db.collection("users").document(uId).collection("userFavourites").document("Restaurants").collection("favRestaurants").document(name)
                                .set(userFav)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        btFavorite.setBackgroundResource(R.drawable.favourite_selected);
                                        btFavourite1.setBackgroundResource(R.drawable.favourite_selected);
                                        set = 0;
                                        Toast.makeText(RestaurantDetail.this, "Bookmark Added", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RestaurantDetail.this, "Something went Wrong Please check your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } else {
                    Toast.makeText(RestaurantDetail.this, "Please check your Internet!", Toast.LENGTH_LONG).show();
                }
            }
        });

        btFavourite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    if (set == 0) {
                        db.collection("users").document(uId).collection("userFavourites").document(name)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        btFavorite.setBackgroundResource(R.drawable.favourite);
                                        btFavourite1.setBackgroundResource(R.drawable.favourite);
                                        Toast.makeText(RestaurantDetail.this, "Bookmark Removed!", Toast.LENGTH_LONG).show();
                                        set = 1;
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting bookmark", e);
                                    }
                                });

                    } else {

                        Map<String, Object> userFav = new HashMap<>();
                        userFav.put("favRestaurantDocId", docId);
                        userFav.put("restaurantName", name);
                        userFav.put("restaurantImage", image);
                        userFav.put("restaurantCollection", collection);

                        db.collection("users").document(uId).collection("userFavourites")
                                .add(userFav)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        btFavorite.setBackgroundResource(R.drawable.favourite_selected);
                                        btFavourite1.setBackgroundResource(R.drawable.favourite_selected);
                                        set = 0;
                                        Toast.makeText(RestaurantDetail.this, "Bookmark Added", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RestaurantDetail.this, "Something went Wrong Please check your Internet", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(RestaurantDetail.this, "Please check you Internet!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cab() {
        btCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RestaurantDetail.this, "This feature will be available soon", Toast.LENGTH_LONG).show();
            }
        });

        btCab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RestaurantDetail.this, "This feature will be available soon", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pubs, menu);
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
                startActivity(new Intent(RestaurantDetail.this, AboutUs.class));
                break;

            case R.id.menuContactUs:
                startActivity(new Intent(RestaurantDetail.this, ContactUs.class));
                break;

            case R.id.menuLogOut:
                FirebaseAuth.getInstance().signOut();
                break;

            case R.id.bookmark_pub:
                startActivity(new Intent(RestaurantDetail.this, BookmarkRestaurants.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void time() {

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        time = simpleDateFormat.format(calendar.getTime());
        db.collection(collection).document(docId).collection("restaurantDetails")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot querySnapshot : task.getResult()) {
                            compareOpen = querySnapshot.getString("openingTime");
                            compareClose = querySnapshot.getString("closingTime");
                        }
                        try {
                            Date time1 = new SimpleDateFormat("HH:mm a").parse(compareOpen);
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.setTime(time1);

                            Date time2 = new SimpleDateFormat("HH:mm a").parse(compareClose);
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.setTime(time2);
                            calendar2.add(Calendar.DATE, 1);

                            Date d = new SimpleDateFormat("HH:mm a").parse(time);
                            Calendar calendar3 = Calendar.getInstance();
                            calendar3.setTime(d);
                            calendar3.add(Calendar.DATE, 1);

                            Date x = calendar3.getTime();
                            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                                restrauntStatus.setText("Open Now");
                                restrauntStatus.setTextColor(Color.parseColor("#39b54a"));
                                restrauntStatus.setTypeface(tf);
                            } else {
                                restrauntStatus.setText("Closed");
                                restrauntStatus.setTextColor(Color.parseColor("#F44336"));
                                restrauntStatus.setTypeface(tf);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RestaurantDetail.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showMenu() {
        imageMenuModelArrayList = new ArrayList<>();
        menuRecyclerView();
        db = FirebaseFirestore.getInstance();
        loadMenuDataFirestore();
    }

    private void menuRecyclerView() {
        menuRecycler = findViewById(R.id.restaurant_menu_recycler_view);
        menuRecycler.setHasFixedSize(true);
        menuRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadMenuDataFirestore() {
        if (isNetworkAvailable()) {
            if (imageMenuModelArrayList.size() > 0)
                imageMenuModelArrayList.clear();
            db.collection(collection).document(docId).collection("menu")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot querySnapshot : task.getResult()) {
                                ImageMenuModel model = new ImageMenuModel(querySnapshot.getString("image"));

                                imageMenuModelArrayList.add(model);
                            }
                            menuAdapter = new RestaurantImageMenuAdapter(imageMenuModelArrayList, RestaurantDetail.this, menuCollectionModelArrayList);
                            menuAdapter.notifyDataSetChanged();
                            menuRecycler.setAdapter(menuAdapter);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RestaurantDetail.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Something went wrong", e);

                        }
                    });
        } else {
            Toast.makeText(RestaurantDetail.this, "Please check your Internet!", Toast.LENGTH_LONG).show();
        }
    }

    private void Rating() {
        if (isNetworkAvailable()) {
            getrating = ratingBar.getRating();
            if (getrating != 0) {
                Map<String, Object> rate = new HashMap<>();
                rate.put("Rating", getrating);

                db.collection(collection).document(docId).collection("Ratings").document(uId)
                        .set(rate)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RestaurantDetail.this, "Rated Successfully!", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RestaurantDetail.this, "Something went Wrong", Toast.LENGTH_LONG).show();
                    }
                });
            }

            db.collection(collection).document(docId).collection("Ratings")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot querySnapshot : task.getResult()) {
                                databaseRating = (querySnapshot.getDouble("Rating").floatValue());
                                ratingArrayList.add(databaseRating);
                            }
                            for (j = 0; j < ratingArrayList.size(); j++) {
                                sum += ratingArrayList.get(j) / ratingArrayList.size();
                                convertRating = String.valueOf(sum);
                                txtAverageRating.setText(convertRating);
                            }
                            totalRating();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RestaurantDetail.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
                }
            });

        } else

        {
            Toast.makeText(RestaurantDetail.this, "Please Check your Internet!", Toast.LENGTH_LONG).show();
        }

    }

    private void totalRating() {
        newRating = String.valueOf(sum);
        if (!newRating.equals("0.0") | newRating != null | sum < 5.0) {
            Map<String, Object> finalRating = new HashMap<>();

            finalRating.put("showRating", newRating);

            db.collection(collection).document(docId)
                    .update(finalRating).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void documentReference) {
                    Toast.makeText(RestaurantDetail.this, "Rated Successfully!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RestaurantDetail.this, "Something went Wrong", Toast.LENGTH_LONG).show();
                }
            });

            db.collection(collection).document(docId).collection("restaurantDetails").document()
                    .update(finalRating).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void documentReference) {
                    sum = 0;
                    getrating = 0;
                    ratingArrayList.clear();
                    newRating = null;
                    Toast.makeText(RestaurantDetail.this, "Rated Successfully!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RestaurantDetail.this, "Something went Wrong", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void imageSlider() {
        imageSliderInitialize();

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

    }

    private void imageSliderInitialize() {
        db.collection(collection).document(docId).collection("restaurantImages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot querySnapshot : task.getResult()) {
                            ImageModel model = new ImageModel(querySnapshot.getString("image"));
                            imageModelArrayList.add(model);
                        }

                        restaurantImageSliderAdapter = new RestaurantImageSliderAdapter(imageModelArrayList, RestaurantDetail.this);
                        viewPager.setAdapter(restaurantImageSliderAdapter);
                        circleIndicator.setViewPager(viewPager);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RestaurantDetail.this, "Something went Wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
