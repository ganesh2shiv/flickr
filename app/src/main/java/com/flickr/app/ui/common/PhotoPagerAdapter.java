package com.flickr.app.ui.common;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.flickr.app.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import timber.log.Timber;

public class PhotoPagerAdapter extends PagerAdapter {

    private Picasso picasso;
    private List<String> urls;
    private boolean isZoomable;

    public PhotoPagerAdapter(Picasso picasso, List<String> urls, boolean isZoomable) {
        this.picasso = picasso;
        this.urls = urls;
        this.isZoomable = isZoomable;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setZoomable(isZoomable);

        try {
            if (isZoomable) {
                picasso.load(urls.get(position))
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_no_image)
                        .into(photoView);

                container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            } else {
                picasso.load(urls.get(position))
                        .placeholder(R.drawable.ic_loading)
                        .error(R.drawable.ic_no_image)
                        .resize(container.getWidth(), container.getHeight())
                        .into(photoView, new Callback() {
                            @Override
                            public void onSuccess() {
                                container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                            }

                            @Override
                            public void onError() {
                                picasso.load(R.drawable.ic_no_image).fit().into(photoView);
                            }
                        });
            }
        } catch (Exception e) {
            Timber.e(e);
            picasso.load(R.drawable.ic_no_image).fit().into(photoView);
        }

        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}