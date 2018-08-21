package com.flickr.app.ui.photo.info.model;

public class PhotoInfoResponse {

    private String stat;
    private String code;
    private String message;
    private PhotoInfo photo;

    public String getStat() {
        return stat;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return message;
    }

    public PhotoInfo getPhoto() {
        return photo;
    }
}