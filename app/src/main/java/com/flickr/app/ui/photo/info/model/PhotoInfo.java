package com.flickr.app.ui.photo.info.model;

public class PhotoInfo {

    private String id;
    private String secret;
    private String server;
    private int farm;
    private String dateuploaded;
    private int isfavorite;
    private int license;
    private int safety_level;
    private int rotation;
    private String views;
    private String media;
    private Owner owner;
    private Content title;
    private Content description;

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public int getFarm() {
        return farm;
    }

    public String getDateuploaded() {
        return dateuploaded;
    }

    public int getIsfavorite() {
        return isfavorite;
    }

    public int getLicense() {
        return license;
    }

    public int getSafety_level() {
        return safety_level;
    }

    public int getRotation() {
        return rotation;
    }

    public String getViews() {
        return views;
    }

    public String getMedia() {
        return media;
    }

    public Owner getOwner() {
        return owner;
    }

    public Content getTitle() {
        return title;
    }

    public Content getDescription() {
        return description;
    }
}