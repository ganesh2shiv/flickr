package com.flickr.app;

import com.flickr.app.data.prefs.PrefsModule;
import com.flickr.app.media.MediaModule;
import com.flickr.app.network.NetworkModule;
import com.flickr.app.ui.home.di.HomeComponent;
import com.flickr.app.ui.photo.di.PhotoModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, PrefsModule.class, MediaModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(BaseApplication application);

        Builder appModule(AppModule appModule);

        Builder networkModule(NetworkModule networkModule);

        Builder prefsModule(PrefsModule prefsModule);

        Builder mediaModule(MediaModule mediaModule);

        AppComponent build();
    }

    HomeComponent plus(PhotoModule photoModule);
}