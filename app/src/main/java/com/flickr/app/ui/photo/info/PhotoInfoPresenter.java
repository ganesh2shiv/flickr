package com.flickr.app.ui.photo.info;

import com.flickr.app.ui.base.BasePresenter;

public abstract class PhotoInfoPresenter extends BasePresenter<PhotoInfoView> {

    abstract void fetchPhotoInfo(String photoId);

}