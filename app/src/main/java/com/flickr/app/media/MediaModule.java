package com.flickr.app.media;

import android.content.Context;

import com.flickr.app.BuildConfig;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

@Module
public class MediaModule {

    @Provides
    @Singleton
    Picasso providePicasso(Context context) {
        return new Picasso.Builder(context)
                .loggingEnabled(BuildConfig.DEBUG)
                .indicatorsEnabled(false)
                .listener((picasso, uri, e) -> Timber.tag("Picasso").e(e, "Failed to load image: %s", uri))
                .build();
    }
}