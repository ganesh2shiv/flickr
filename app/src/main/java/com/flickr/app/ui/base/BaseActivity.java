package com.flickr.app.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.flickr.app.R;
import com.flickr.app.data.event.NetworkEventMessage;
import com.flickr.app.ui.common.NetworkErrorFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.flickr.app.util.Constants.EVENT_CONNECTIVITY_CONNECTED;
import static com.flickr.app.util.Constants.EVENT_CONNECTIVITY_LOST;
import static com.flickr.app.util.Constants.NETWORK_ERROR_FRAGMENT;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private ProgressDialog progressDialog;
    private NetworkErrorFragment networkErrorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (networkErrorFragment == null) {
            networkErrorFragment = NetworkErrorFragment.newInstance();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetworkEventMessage event) {
        switch (event.getResultCode()) {
            case EVENT_CONNECTIVITY_LOST:
                showNetworkErrorFragmentIfNotShowing();
                break;
            case EVENT_CONNECTIVITY_CONNECTED:
                dismissNetworkErrorFragmentIfShowing();
                break;
        }
    }

    protected void showNetworkErrorFragmentIfNotShowing() {
        if (!isNetworkErrorFragmentShowing()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, networkErrorFragment, NETWORK_ERROR_FRAGMENT)
                    .commit();
        }
    }

    protected void dismissNetworkErrorFragmentIfShowing() {
        if (isNetworkErrorFragmentShowing()) {
            getSupportFragmentManager().beginTransaction().remove(networkErrorFragment).commit();
        }
    }

    private boolean isNetworkErrorFragmentShowing() {
        return networkErrorFragment != null && networkErrorFragment.isVisible();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public Context context() {
        return this;
    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void hideProgressBar() {
    }

    @Override
    public void showProgressDialog() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.processing_msg));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
    }

    protected void fragmentTransition(Fragment fragment, String tag) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_main, fragment, tag)
                    .addToBackStack(null)
                    .commit();
        }
    }
}