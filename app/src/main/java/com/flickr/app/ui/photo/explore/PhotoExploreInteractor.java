package com.flickr.app.ui.photo.explore;

import com.flickr.app.ui.photo.explore.model.Photo;

import java.util.List;

import io.reactivex.Single;

public interface PhotoExploreInteractor {

    Single<List<Photo>> getPhotosList(int page, int limit);

}