package com.flickr.app.ui.photo.explore;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhotoExplorePresenterImpl extends PhotoExplorePresenter {

    private PhotoExploreView view;
    private PhotoExploreInteractor interactor;

    public PhotoExplorePresenterImpl(PhotoExploreInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(PhotoExploreView view) {
        super.setView(view);
        this.view = view;
    }

    @Override
    void fetchPhotos(int page, int limit) {
        showProgressBar();

        disposable = interactor.getPhotosList(page, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(photos -> {
                    hideProgressBar();
                    if (isViewAttached()) {
                        view.showList(photos);
                    }
                }, error -> {
                    hideProgressBar();
                    showMessage(error.getMessage());
                    showEmptyView();
                });
    }

    @Override
    void fetchMore(int page, int limit) {
        showProgress();

        disposable = interactor.getPhotosList(page, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(newPhotos -> {
                    hideProgress();

                    if (isViewAttached()) {
                        view.showList(newPhotos);
                    }
                }, error -> {
                    if (isViewAttached()) {
                        view.showRetry();
                    }
                    hideProgress();
                    showMessage(error.getMessage());
                });
    }

    private void showProgress() {
        if (isViewAttached()) {
            view.showProgress();
        }
    }

    private void hideProgress() {
        if (isViewAttached()) {
            view.hideProgress();
        }
    }
}