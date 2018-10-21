package com.example.amanat.citytour.Model;

public class AttractionsClickModel {
    private String hotspotName, hotspotImage, aboutHotspot, hotspotTiming;

    public AttractionsClickModel(String hotspotName, String hotspotImage, String aboutHotspot, String hotspotTiming) {
        this.hotspotName = hotspotName;
        this.hotspotImage = hotspotImage;
        this.aboutHotspot = aboutHotspot;
        this.hotspotTiming = hotspotTiming;
    }

    public String getHotspotName() {
        return hotspotName;
    }

    public void setHotspotName(String hotspotName) {
        this.hotspotName = hotspotName;
    }

    public String getHotspotImage() {
        return hotspotImage;
    }

    public void setHotspotImage(String hotspotImage) {
        this.hotspotImage = hotspotImage;
    }

    public String getAboutHotspot() {
        return aboutHotspot;
    }

    public void setAboutHotspot(String aboutHotspot) {
        this.aboutHotspot = aboutHotspot;
    }

    public String getHotspotTiming() {
        return hotspotTiming;
    }

    public void setHotspotTiming(String hotspotTiming) {
        this.hotspotTiming = hotspotTiming;
    }
}
