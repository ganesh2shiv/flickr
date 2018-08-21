package com.flickr.app.ui.base;

import com.flickr.app.util.RxUtils;

import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<T extends BaseView> {

    private T view;
    protected Disposable disposable;

    public void setView(T view) {
        this.view = view;
    }

    public T getView() {
        return view;
    }

    protected void showEmptyView() {
        if (isViewAttached()) {
            view.showEmptyView();
        }
    }

    protected void showProgressBar() {
        if (isViewAttached()) {
            view.showProgressBar();
        }
    }

    protected void hideProgressBar() {
        if (isViewAttached()) {
            view.hideProgressBar();
        }
    }

    protected void showProgressDialog() {
        if (isViewAttached()) {
            view.showProgressDialog();
        }
    }

    protected void hideProgressDialog() {
        if (isViewAttached()) {
            view.hideProgressDialog();
        }
    }

    protected void showMessage(String msg) {
        if (isViewAttached()) {
            view.showMessage(msg);
        }
    }

    protected boolean isViewAttached() {
        return view != null;
    }

    public void destroy() {
        view = null;
        RxUtils.dispose(disposable);
    }
}