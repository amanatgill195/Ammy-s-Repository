package com.example.amanat.citytour.Model;

import java.io.Serializable;

public class AttractionModel implements Serializable {
    public String hotspotImage, hotspotName, aboutPlace, txtTime, documentId;

    public AttractionModel(String hotspotImage, String hotspotName, String aboutPlace, String txtTime, String documentId) {
        this.hotspotImage = hotspotImage;
        this.hotspotName = hotspotName;
        this.aboutPlace = aboutPlace;
        this.txtTime = txtTime;
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAboutPlace() {
        return aboutPlace;
    }

    public void setAboutPlace(String aboutPlace) {
        this.aboutPlace = aboutPlace;
    }


    public String getTxtTime() {
        return txtTime;
    }

    public void setTxtTime(String txtTime) {
        this.txtTime = txtTime;
    }


    public String getHotspotImage() {
        return hotspotImage;
    }

    public void setHotspotImage(String hotspotImage) {
        this.hotspotImage = hotspotImage;
    }

    public String getHotspotName() {
        return hotspotName;
    }

    public void setHotspotName(String hotspotName) {
        this.hotspotName = hotspotName;
    }
}