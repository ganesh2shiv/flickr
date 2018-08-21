package com.flickr.app.ui.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flickr.app.R;
import com.flickr.app.ui.base.BaseFragment;

public class NetworkErrorFragment extends BaseFragment {

    public static NetworkErrorFragment newInstance() {
        return new NetworkErrorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fragment_network_error, container, false);
        return dialogView;
    }
}