package com.flickr.app;

import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.flickr.app.data.event.NetworkEventMessage;
import com.flickr.app.data.prefs.PrefsModule;
import com.flickr.app.media.MediaModule;
import com.flickr.app.network.NetworkModule;
import com.flickr.app.ui.home.di.HomeComponent;
import com.flickr.app.ui.photo.di.PhotoModule;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.flickr.app.util.Constants.EVENT_CONNECTIVITY_CONNECTED;
import static com.flickr.app.util.Constants.EVENT_CONNECTIVITY_LOST;

public class BaseApplication extends MultiDexApplication {

    private Disposable disposable;
    private AppComponent appComponent;
    private HomeComponent homeComponent;

    @Override
    public void onCreate() {
        super.onCreate();

//        StrictMode.enableDefaults();
        appComponent = createAppComponent();

//        ButterKnife.setDebug(BuildConfig.DEBUG);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, @NonNull String msg, Throwable t) {
                    super.log(priority, "flickr_logs_" + tag, msg, t);
                }
            });
        }

        if (disposable == null) {
            disposable = ReactiveNetwork.observeNetworkConnectivity(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(connectivity -> {
                        if (connectivity.getState() != NetworkInfo.State.CONNECTED) {
                            EventBus.getDefault().post(new NetworkEventMessage(EVENT_CONNECTIVITY_LOST));
                        } else {
                            EventBus.getDefault().post(new NetworkEventMessage(EVENT_CONNECTIVITY_CONNECTED));
                        }
                    });
        }
    }

    private AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .application(this)
                .appModule(new AppModule())
                .networkModule(new NetworkModule())
                .prefsModule(new PrefsModule())
                .mediaModule(new MediaModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public HomeComponent getHomeComponent() {
        if (homeComponent != null) {
            return homeComponent;
        }
        return createHomeComponent();
    }

    private HomeComponent createHomeComponent() {
        homeComponent = appComponent.plus(new PhotoModule());
        return homeComponent;
    }

    public void releaseHomeComponent() {
        homeComponent = null;
    }
}