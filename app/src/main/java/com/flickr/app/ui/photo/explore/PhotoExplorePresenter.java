package com.flickr.app.ui.photo.explore;

import com.flickr.app.ui.base.BasePresenter;

public abstract class PhotoExplorePresenter extends BasePresenter<PhotoExploreView> {

    abstract void fetchPhotos(int page, int limit);

    abstract void fetchMore(int page, int limit);

}