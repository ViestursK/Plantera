package com.firstapp.plantera;

public class PlantItem {
    private String mImageUrl;
    private String mPlant;
    private int mPlantId; // Add the plant ID

    public PlantItem(String imageUrl, String plant, int plantId) {
        mImageUrl = imageUrl;
        mPlant = plant.toUpperCase();
        mPlantId = plantId; // Add the plant ID
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getPlant() {
        return mPlant;
    }

    public int getPlantId() { // Add the plant ID
        return mPlantId;
    }
}
