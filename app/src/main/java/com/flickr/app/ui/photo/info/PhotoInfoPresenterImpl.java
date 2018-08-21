package com.flickr.app.ui.photo.info;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PhotoInfoPresenterImpl extends PhotoInfoPresenter {

    private PhotoInfoView view;
    private PhotoInfoInteractor interactor;

    public PhotoInfoPresenterImpl(PhotoInfoInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void setView(PhotoInfoView view) {
        super.setView(view);
        this.view = view;
    }

    @Override
    public void fetchPhotoInfo(String photoId) {
        showProgressBar();

        disposable = interactor.getPhotoInfo(photoId)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(info -> {
                    hideProgressBar();
                    if (isViewAttached()) {
                        view.showInfo(info);
                    }
                }, error -> {
                    hideProgressBar();
                    showMessage(error.getMessage());
                });
    }
}