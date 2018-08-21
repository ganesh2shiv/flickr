package com.flickr.app.ui.photo.info;

import com.flickr.app.ui.base.BaseView;
import com.flickr.app.ui.photo.info.model.PhotoInfo;

interface PhotoInfoView extends BaseView {

    void showInfo(PhotoInfo info);

}