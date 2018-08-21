package com.flickr.app.ui.photo.explore;

import android.support.annotation.NonNull;

import com.airbnb.epoxy.AutoModel;
import com.flickr.app.ui.custom.LoadMoreModel_;
import com.flickr.app.ui.custom.PagingEpoxyController;
import com.flickr.app.ui.photo.explore.model.Photo;
import com.flickr.app.ui.photo.explore.model.PhotoSize;
import com.squareup.picasso.Picasso;

import java.util.List;

import timber.log.BuildConfig;

public class PhotoExploreController extends PagingEpoxyController<Photo> {

    @AutoModel LoadMoreModel_ loadMoreModel;

    private boolean retry;
    private boolean loadMore;

    private Picasso picasso;
    private PhotoExploreView view;

    PhotoExploreController(PhotoExploreView view, Picasso picasso) {
        this.view = view;
        this.picasso = picasso;
        setFilterDuplicates(true);
        setDebugLoggingEnabled(BuildConfig.DEBUG);
    }

    void retry(boolean retry) {
        this.retry = retry;
        requestModelBuild();
    }

    boolean isRetry() {
        return this.retry;
    }

    void loadMore(boolean loadMore) {
        this.loadMore = loadMore;
        requestModelBuild();
    }

    boolean isLoadMore() {
        return this.loadMore;
    }

    @Override
    protected void buildModels(@NonNull List<Photo> photos) {
        for (Photo item : photos) {
            new PhotoExploreItemModel_()
                    .id(item.getId())
                    .view(view)
                    .picasso(picasso)
                    .title(item.getTitle())
                    .url(item.getUrl(PhotoSize.MEDIUM))
                    .clickListener(itemView -> view.onItemClicked(itemView, item))
                    .addTo(this);
        }

        loadMoreModel
                .retry(retry)
                .clickListener(itemView -> view.fetchMore())
                .addIf(loadMore || retry, this);
    }
}