package com.flickr.app.ui.photo.info;

import android.support.annotation.NonNull;

import com.flickr.app.ui.photo.info.model.PhotoInfo;
import com.flickr.app.ui.photo.info.model.PhotoInfoResponse;

import retrofit2.Response;

class PhotoInfoParser {

    @NonNull
    static PhotoInfo parse(Response<PhotoInfoResponse> response) throws NullPointerException {

        if (response.isSuccessful()) {
            PhotoInfoResponse body = response.body();
            if (body.getStat().equalsIgnoreCase("ok")) {
                PhotoInfo photoInfo = body.getPhoto();
                if (photoInfo != null) {
                    return photoInfo;
                } else {
                    throw new RuntimeException("Response payload is empty!");
                }
            } else {
                throw new RuntimeException(body.getMsg());
            }
        } else {
            throw new RuntimeException(response.message());
        }
    }
}