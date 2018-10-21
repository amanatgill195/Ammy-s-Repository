package com.example.amanat.citytour.Model;

import com.google.firebase.firestore.GeoPoint;

public class LocationModel {
    GeoPoint restaurantLocation;
    String loc;

    public LocationModel(GeoPoint restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public LocationModel(String loc) {
        this.loc = loc;
    }

    public GeoPoint getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(GeoPoint restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }
}
