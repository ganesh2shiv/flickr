package com.flickr.app;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    @Provides
    @Singleton
    Context provideContext(BaseApplication application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Resources provideResources(Context context) {
        return context.getResources();
    }

}