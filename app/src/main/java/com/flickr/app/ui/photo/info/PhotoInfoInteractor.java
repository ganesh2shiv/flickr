package com.flickr.app.ui.photo.info;

import com.flickr.app.ui.photo.info.model.PhotoInfo;

import io.reactivex.Single;

public interface PhotoInfoInteractor {

    Single<PhotoInfo> getPhotoInfo(String photoId);

}