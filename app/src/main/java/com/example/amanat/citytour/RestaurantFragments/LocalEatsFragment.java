package com.example.amanat.citytour.RestaurantFragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.amanat.citytour.Model.RestaurantModel;
import com.example.amanat.citytour.R;
import com.example.amanat.citytour.RestaurantAdapters.LocalEatsFragmentAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LocalEatsFragment extends Fragment {
    private static final String TAG = "";
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ArrayList<RestaurantModel> restaurantModelArrayList;
    private LocalEatsFragmentAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_local_eats, container, false);

        setProgressDialog();
        restaurantModelArrayList = new ArrayList<>();
        recyclerView = v.findViewById(R.id.local_eats_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        //setRecyclerView();
        db = FirebaseFirestore.getInstance();
        loadDataFirestore();

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

        return v;
    }

    private void myUpdateOperation() {
        loadDataFirestore();
    }

    public void loadDataFirestore() {
        if (isNetworkAvailable()) {
            if (restaurantModelArrayList.size() > 0)
                restaurantModelArrayList.clear();
            db.collection("localEatsRestaurants")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot querySnapshot : task.getResult()) {
                                RestaurantModel restaurantModel = new RestaurantModel(querySnapshot.getString("name")
                                        , querySnapshot.getString("image")
                                        , querySnapshot.getString("documentId")
                                        , querySnapshot.getGeoPoint("location")
                                        , querySnapshot.getString("showRating"));

                                restaurantModelArrayList.add(restaurantModel);
                            }
                            adapter = new LocalEatsFragmentAdapter(restaurantModelArrayList, LocalEatsFragment.this);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();

                           /* runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                }
                            });*/

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Something went wrong", e);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Please check your Internet!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setProgressDialog() {
        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
        if (isNetworkAvailable()) {
            progressDialog.show();
        } else {
            Toast.makeText(getActivity(), "Error: Please check your Internet Connection", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }
}
