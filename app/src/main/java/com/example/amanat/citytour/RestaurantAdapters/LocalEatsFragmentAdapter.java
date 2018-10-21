package com.example.amanat.citytour.RestaurantAdapters;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amanat.citytour.GpsTracker;
import com.example.amanat.citytour.Model.RestaurantModel;
import com.example.amanat.citytour.R;
import com.example.amanat.citytour.RestaurantDetail;
import com.example.amanat.citytour.RestaurantFragments.LocalEatsFragment;
import com.example.amanat.citytour.ViewHolders.RestaurantViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LocalEatsFragmentAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {
    private ArrayList<RestaurantModel> restaurantModelArrayList;
    private LocalEatsFragment localEatsFragment;
    private Double latDestination, lonDestination;
    private GpsTracker gpsTracker;
    private String locationDistance;
    private String deliver = "Don't Deliver", collection = "localEatsRestaurants";

    public LocalEatsFragmentAdapter(ArrayList<RestaurantModel> restaurantModelArrayList, LocalEatsFragment localEatsFragment) {
        this.restaurantModelArrayList = restaurantModelArrayList;
        this.localEatsFragment = localEatsFragment;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.restaurant_view, null);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, final int position) {
        holder.restrauntName.setText(restaurantModelArrayList.get(position).getRestrauntName());
        Picasso.get().load(restaurantModelArrayList.get(position).getRestrauntImage()).fit().into(holder.ibRestrauntImage);

        latDestination = restaurantModelArrayList.get(position).getLocation().getLatitude();
        lonDestination = restaurantModelArrayList.get(position).getLocation().getLongitude();
        // restaurantLocation = String.valueOf(restaurantModelArrayList.get(position).getLocation());

        try {
            if (ContextCompat.checkSelfPermission(localEatsFragment.getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(localEatsFragment.getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gpsTracker = new GpsTracker(localEatsFragment.getActivity());
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            Location loc1 = new Location("");
            loc1.setLatitude(latitude);
            loc1.setLongitude(longitude);

            Location loc2 = new Location("");
            loc2.setLatitude(latDestination);
            loc2.setLongitude(lonDestination);
            float distanceInMeters = (loc1.distanceTo(loc2) / 1000);
            // float distanceInKm = (float) (distanceInMeters * 0.001);
            String distanceInKm = String.format("%.2f", distanceInMeters);
            locationDistance = String.valueOf(distanceInKm) + " Km";

            holder.restaurantDistance.setText(locationDistance);
            //sortedList.add(String.valueOf(holder.restaurantDistance.getText()));
            //sortedList.add(locationDistance);
           /* Collections.sort(sortedList);
            for (int i = 0; i < sortedList.size(); i++) {
                holder.restaurantDistance.setText(sortedList.get(i));
            }*/

        } else {
            gpsTracker.showSettingsAlert();
        }

        final String rating = restaurantModelArrayList.get(position).getRatings();
        holder.ratingBar.setRating(Float.parseFloat(rating));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(localEatsFragment.getActivity(), RestaurantDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", restaurantModelArrayList.get(position).getRestrauntName());
                bundle.putString("image", restaurantModelArrayList.get(position).getRestrauntImage());
                bundle.putString("documentId", restaurantModelArrayList.get(position).getDocumentId());
                bundle.putString("latDestination", String.valueOf(restaurantModelArrayList.get(position).getLocation().getLatitude()));
                bundle.putString("lonDestination", String.valueOf(restaurantModelArrayList.get(position).getLocation().getLongitude()));
                bundle.putString("deliveryStatus", deliver);
                bundle.putString("collection", collection);
                bundle.putString("rating", String.valueOf(restaurantModelArrayList.get(position).getRatings()));
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantModelArrayList.size();
    }
}
