package com.flickr.app.ui.photo.di;

import com.flickr.app.network.RestService;
import com.flickr.app.ui.photo.explore.PhotoExploreInteractor;
import com.flickr.app.ui.photo.explore.PhotoExploreInteractorImpl;
import com.flickr.app.ui.photo.explore.PhotoExplorePresenter;
import com.flickr.app.ui.photo.explore.PhotoExplorePresenterImpl;
import com.flickr.app.ui.photo.info.PhotoInfoInteractor;
import com.flickr.app.ui.photo.info.PhotoInfoInteractorImpl;
import com.flickr.app.ui.photo.info.PhotoInfoPresenter;
import com.flickr.app.ui.photo.info.PhotoInfoPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class PhotoModule {

    @Provides
    PhotoExploreInteractor providePhotoExploreInteractor(RestService restService) {
        return new PhotoExploreInteractorImpl(restService);
    }

    @Provides
    PhotoExplorePresenter providePhotoExplorePresenter(PhotoExploreInteractor interactor) {
        return new PhotoExplorePresenterImpl(interactor);
    }

    @Provides
    PhotoInfoInteractor providePhotoInfoInteractor(RestService restService) {
        return new PhotoInfoInteractorImpl(restService);
    }

    @Provides
    PhotoInfoPresenter providePhotoInfoPresenter(PhotoInfoInteractor interactor) {
        return new PhotoInfoPresenterImpl(interactor);
    }
}