package com.flickr.app.ui.photo.explore.model;

import com.flickr.app.util.Constants;

import org.parceler.Parcel;

@Parcel
public class Photo {

    String id;
    String owner;
    String secret;
    String server;
    int farm;
    String title;
    int ispublic;
    int isfriend;
    int isfamily;
    String url;

    public Photo() {
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
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

    public String getTitle() {
        return title;
    }

    public int getIspublic() {
        return ispublic;
    }

    public int getIsfriend() {
        return isfriend;
    }

    public int getIsfamily() {
        return isfamily;
    }

    public String getUrl(PhotoSize size) {
        // https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
        return String.format(Constants.FLICKR_PHOTO_URL, farm, server, id, secret, size.getValue());
    }
}