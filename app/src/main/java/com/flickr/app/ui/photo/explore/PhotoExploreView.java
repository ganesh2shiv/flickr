package com.flickr.app.ui.photo.explore;

import android.view.View;

import com.flickr.app.ui.base.BaseView;
import com.flickr.app.ui.photo.explore.model.Photo;

import java.util.List;

interface PhotoExploreView extends BaseView {

    void showProgress();

    void hideProgress();

    void showList(List<Photo> photos);

    void fetchMore();

    void showRetry();

    void hideLoadMore();

    void onItemClicked(View itemView, Photo photo);

}