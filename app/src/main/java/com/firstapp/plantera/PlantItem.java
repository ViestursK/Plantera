package com.firstapp.plantera;

public class PlantItem {
    private String mImageUrl;
    private String mPlant;

    public PlantItem(String imageUrl, String plant) {
        mImageUrl = imageUrl;
        mPlant = plant.toUpperCase();
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getPlant() {
        return mPlant;
    }
}
