package com.flickr.app.ui.photo.explore;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flickr.app.BaseApplication;
import com.flickr.app.R;
import com.flickr.app.ui.base.BaseFragment;
import com.flickr.app.ui.custom.EmptyRecyclerView;
import com.flickr.app.ui.custom.InfiniteScrollListener;
import com.flickr.app.ui.photo.explore.model.Photo;
import com.flickr.app.ui.photo.info.PhotoInfoActivity;
import com.flickr.app.util.AlertUtil;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

import static com.flickr.app.util.Constants.PHOTO_ITEM;
import static com.flickr.app.util.Constants.PHOTO_THUMB;

public class PhotoExploreFragment extends BaseFragment implements PhotoExploreView {

    private int page = 1;
    private int limit = 15;
    private Unbinder unbinder;
    private PhotoExploreController controller;
    private List<Photo> photos = new ArrayList<>();

    @Inject
    Picasso picasso;

    @Inject
    PhotoExplorePresenter presenter;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.emptyView)
    View emptyView;

    @BindView(R.id.recyclerView)
    EmptyRecyclerView rvPhotos;

    public static PhotoExploreFragment newInstance() {
        return new PhotoExploreFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseApplication) getActivity().getApplication()).getHomeComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new GridLayoutManager(context(), 2);
        rvPhotos.setLayoutManager(layoutManager);
        rvPhotos.setHasFixedSize(true);
        rvPhotos.setEmptyView(emptyView);
        rvPhotos.addOnScrollListener(new InfiniteScrollListener(limit, layoutManager) {
            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {
                if (!controller.isLoadMore() && !controller.isRetry()) {
                    presenter.fetchMore(++page, limit);
                }
            }
        });

        controller = new PhotoExploreController(this, picasso);
        rvPhotos.setAdapter(controller.getAdapter());

        swipeLayout.setOnRefreshListener(() -> {
            page = 1;
            presenter.fetchPhotos(page, limit);
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter.setView(this);
        presenter.fetchPhotos(page, limit);
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        rvPhotos.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        page = 1;
        swipeLayout.setRefreshing(true);
        emptyView.setVisibility(View.GONE);
        rvPhotos.setVisibility(View.GONE);
        photos.clear();
    }

    @Override
    public void hideProgressBar() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String msg) {
        AlertUtil.showSnackBar(swipeLayout, msg);
    }

    @Override
    public void showProgress() {
        controller.loadMore(true);
    }

    @Override
    public void hideProgress() {
        controller.loadMore(false);
    }

    @Override
    public void showList(List<Photo> newPhotos) {
        photos.addAll(newPhotos);
        controller.setList(photos);
        emptyView.setVisibility(View.GONE);
        rvPhotos.setVisibility(View.VISIBLE);
        hideLoadMore();
    }

    @Override
    public void fetchMore() {
        presenter.fetchMore(page, limit);
    }

    @Override
    public void showRetry() {
        controller.retry(true);
    }

    @Override
    public void hideLoadMore() {
        controller.retry(false);
        controller.loadMore(false);
    }

    @Override
    public void onItemClicked(View itemView, Photo photo) {
        Intent intent = new Intent(getActivity(), PhotoInfoActivity.class);
        intent.putExtra(PHOTO_ITEM, Parcels.wrap(photo));

        ImageView ivThumb = itemView.findViewById(R.id.iv_thumb);

        try {
            Bitmap photoThumbBitmap = ((BitmapDrawable) ivThumb.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photoThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageInByte = baos.toByteArray();
            intent.putExtra(PHOTO_THUMB, imageInByte);
        } catch (Exception e) {
            Timber.e(e);
        }

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(), itemView, ViewCompat.getTransitionName(ivThumb));
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        page = 1;
        presenter.destroy();
        unbinder.unbind();
    }
}