package com.example.amanat.citytour.Model;

public class MenuCollectionModel {
    private String collection, docId;

    public MenuCollectionModel(String collection, String docId) {
        this.collection = collection;
        this.docId = docId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }
}
