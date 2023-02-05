package com.badfish.jobber.Models;

public class UploadModel {
    private String name, imageUrl;

    public UploadModel() {

    }

    public UploadModel(String name, String imageUrl) {
        name="profilePicture";
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
