package com.flickr.app.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.flickr.app.BuildConfig;
import com.flickr.app.data.prefs.StringPreference;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class NetworkModule {

    private static final int READ_TIME_OUT = 30;
    private static final int CONNECT_TIMEOUT = 10;

    @Provides
    @Named("baseUrl")
    @Singleton
    StringPreference provideEndpointPreference(SharedPreferences prefs) {
        return new StringPreference(prefs, "baseUrl", ApiEndpoints.RELEASE.url);
    }

    @Provides
    @Singleton
    HttpUrl provideHttpUrl(@Named("baseUrl") StringPreference apiEndpoint) {
        return HttpUrl.parse(apiEndpoint.get());
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Context context) {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor(message -> Timber.tag("OkHttp").d(message))
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new ChuckInterceptor(context))
                .cache(null)
                .build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofitClient(HttpUrl baseUrl, OkHttpClient okHttpClient) {

        RxJava2CallAdapterFactory rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io());

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.DEBUG ? baseUrl : HttpUrl.parse(ApiEndpoints.RELEASE.url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxAdapter)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    RestService provideRestService(Retrofit retrofit) {
        return retrofit.create(RestService.class);
    }
}