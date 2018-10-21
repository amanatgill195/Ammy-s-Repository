package com.example.amanat.citytour.Model;

import com.google.firebase.firestore.GeoPoint;

public class RestaurantModel {
    private String restrauntName, restrauntImage, documentId;
    private GeoPoint location;
    private String ratings;

    public RestaurantModel(String restrauntName, String restrauntImage, String documentId, GeoPoint location, String ratings) {
        this.restrauntName = restrauntName;
        this.restrauntImage = restrauntImage;
        this.documentId = documentId;
        this.location = location;
        this.ratings = ratings;
    }

    public String getRestrauntName() {
        return restrauntName;
    }

    public void setRestrauntName(String restrauntName) {
        this.restrauntName = restrauntName;
    }

    public String getRestrauntImage() {
        return restrauntImage;
    }

    public void setRestrauntImage(String restrauntImage) {
        this.restrauntImage = restrauntImage;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }
}
