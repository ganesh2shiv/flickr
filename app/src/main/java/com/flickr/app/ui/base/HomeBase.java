package com.flickr.app.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;

import com.flickr.app.BaseApplication;
import com.flickr.app.data.prefs.StringPreference;
import com.flickr.app.util.AlertUtil;
import com.jakewharton.processphoenix.ProcessPhoenix;

import javax.inject.Inject;
import javax.inject.Named;

import timber.log.Timber;

import static com.flickr.app.util.Constants.BACK_PRESS_INTERVAL;

public abstract class HomeBase extends BaseActivity {

    @Inject
    @Named("baseUrl")
    protected StringPreference apiEndpoint;

    private Handler backPressHandler = new Handler();
    private boolean doubleBackToExitPressedOnce = false;
    private final Runnable backPressRunnable = () -> doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).getHomeComponent().inject(this);
    }

    protected void setEndpointAndRelaunch(String endpoint) {
        Timber.d("Setting network endpoint to %s", endpoint);
        apiEndpoint.set(endpoint);

        ProcessPhoenix.triggerRebirth(this);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStackImmediate();
            return;
        } else {
            doubleBackToExitPressedOnce = true;
        }

        AlertUtil.showToast(getApplicationContext(), "Press back again to exit!", BACK_PRESS_INTERVAL);
        backPressHandler.postDelayed(backPressRunnable, BACK_PRESS_INTERVAL);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (backPressHandler != null) {
            backPressHandler.removeCallbacks(backPressRunnable);
        }

        ((BaseApplication) getApplication()).releaseHomeComponent();
    }
}