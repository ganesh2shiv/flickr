package com.flickr.app.ui.home.di;

import com.flickr.app.ui.base.HomeBase;
import com.flickr.app.ui.photo.di.PhotoModule;
import com.flickr.app.ui.photo.explore.PhotoExploreFragment;
import com.flickr.app.ui.photo.info.PhotoInfoActivity;

import dagger.Subcomponent;

@HomeScope
@Subcomponent(modules = {PhotoModule.class})
public interface HomeComponent {

    void inject(HomeBase activity);

    void inject(PhotoExploreFragment fragment);

    void inject(PhotoInfoActivity activity);

}