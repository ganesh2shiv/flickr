package com.flickr.app.util;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxUtils {

    private RxUtils() {
    }

    public static void dispose(Disposable disposable) {
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    public static void dispose(CompositeDisposable compositeDisposable) {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}