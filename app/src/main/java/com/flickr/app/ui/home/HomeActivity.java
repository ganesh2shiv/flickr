package com.flickr.app.ui.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.flickr.app.BuildConfig;
import com.flickr.app.R;
import com.flickr.app.network.ApiEndpoints;
import com.flickr.app.ui.base.HomeBase;
import com.flickr.app.ui.photo.explore.PhotoExploreFragment;
import com.flickr.app.util.AlertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

import static com.flickr.app.util.Constants.EXPLORE_FRAGMENT;
import static com.flickr.app.util.Constants.INT_CONST_FOR_EXPLORE_FRAGMENT;

public class HomeActivity extends HomeBase {

    private Fragment currentFragment;
    private SparseArray<Fragment> fragmentStore = new SparseArray<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_main)
    FrameLayout layoutMain;

    @OnLongClick(R.id.toolbar)
    boolean toggleDebugFab() {
        if (BuildConfig.DEBUG) {
            if (debugFab.getVisibility() == View.VISIBLE) {
                debugFab.hide();
            } else {
                debugFab.show();
            }
        }
        return true;
    }

    @BindView(R.id.debug_fab)
    FloatingActionButton debugFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initView();

        showLandingScreen();
    }

    private void initView() {
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        if (BuildConfig.DEBUG) {
            debugFab.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(layoutMain.getContext(), view);
                popup.inflate(R.menu.menu_environment);

                if (ApiEndpoints.isReleaseMode(apiEndpoint.get())) {
                    popup.getMenu().findItem(R.id.menu_release).setChecked(true);
                }

                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_mock:
                            setEndpointAndRelaunch(ApiEndpoints.MOCK.url);
                            return true;
                        case R.id.menu_release:
                            setEndpointAndRelaunch(ApiEndpoints.RELEASE.url);
                            return true;
                        default:
                            return false;
                    }
                });
                popup.show();
            });
        }
    }

    private void showLandingScreen() {
        currentFragment = fragmentStore.get(INT_CONST_FOR_EXPLORE_FRAGMENT);
        if (currentFragment == null) {
            currentFragment = PhotoExploreFragment.newInstance();
            fragmentStore.put(INT_CONST_FOR_EXPLORE_FRAGMENT, currentFragment);
        }
        fragmentTransition(currentFragment, EXPLORE_FRAGMENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AlertUtil.showAlertDialog(this, "About", Html.fromHtml(getString(R.string.about_msg)), "Close");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}