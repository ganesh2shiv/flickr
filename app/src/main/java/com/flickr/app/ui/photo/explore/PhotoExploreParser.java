package com.flickr.app.ui.photo.explore;

import android.support.annotation.NonNull;

import com.flickr.app.ui.photo.explore.model.Photo;
import com.flickr.app.ui.photo.explore.model.PhotoExploreResponse;

import java.util.List;

import retrofit2.Response;

class PhotoExploreParser {

    @NonNull
    static List<Photo> parse(Response<PhotoExploreResponse> response) throws NullPointerException {

        if (response.isSuccessful()) {
            PhotoExploreResponse body = response.body();
            if (body.getStat().equalsIgnoreCase("ok")) {
                List<Photo> photos = body.getPhotos().getPhoto();
                if (photos != null && !photos.isEmpty()) {
                    return photos;
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