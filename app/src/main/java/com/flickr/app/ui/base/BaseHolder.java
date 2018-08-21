package com.flickr.app.ui.base;

import android.support.annotation.CallSuper;
import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;

import butterknife.ButterKnife;

public abstract class BaseHolder extends EpoxyHolder {

    private View itemView;

    @CallSuper
    @Override
    protected void bindView(View itemView) {
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }

    public View getItemView() {
        return itemView;
    }
}