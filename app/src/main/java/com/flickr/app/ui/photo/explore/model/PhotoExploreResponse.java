package com.flickr.app.ui.photo.explore.model;

public class PhotoExploreResponse {

    private String stat;
    private String code;
    private String message;
    private Photos photos;

    public String getStat() {
        return stat;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    public Photos getPhotos() {
        return photos;
    }
}