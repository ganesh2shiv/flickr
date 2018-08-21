package com.flickr.app.ui.custom;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private final int maxItemsPerRequest;
    private final LinearLayoutManager layoutManager;

    /**
     * Initializes InfiniteScrollListener, which can be added
     * to RecyclerView with addOnScrollListener method
     *
     * @param maxItemsPerRequest Max items to be loaded in a single request.
     * @param layoutManager      LinearLayoutManager created in the Activity.
     */
    protected InfiniteScrollListener(int maxItemsPerRequest, LinearLayoutManager layoutManager) {
        checkIfPositive(maxItemsPerRequest, "maxItemsPerRequest <= 0");
        checkNotNull(layoutManager, "layoutManager == null");
        this.maxItemsPerRequest = maxItemsPerRequest;
        this.layoutManager = layoutManager;
    }

    /**
     * Callback method to be invoked when the RecyclerView has been scrolled
     *
     * @param recyclerView The RecyclerView which scrolled.
     * @param dx           The amount of horizontal scroll.
     * @param dy           The amount of vertical scroll.
     */
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (canLoadMoreItems()) {
            onScrolledToEnd(layoutManager.findFirstVisibleItemPosition());
        }
    }

    /**
     * Refreshes RecyclerView by setting new adapter,
     * calling invalidate method and scrolling to given position
     *
     * @param view     RecyclerView to be refreshed
     * @param adapter  adapter with new list of items to be loaded
     * @param position position to which RecyclerView will be scrolled
     */
    protected void refreshView(RecyclerView view, RecyclerView.Adapter adapter, int position) {
        view.setAdapter(adapter);
        view.invalidate();
        view.scrollToPosition(position);
    }

    /**
     * Checks if more items can be loaded to the RecyclerView
     *
     * @return boolean Returns true if can load more items or false if not.
     */
    protected boolean canLoadMoreItems() {
        final int visibleItemsCount = layoutManager.getChildCount();
        final int totalItemsCount = layoutManager.getItemCount();
        final int pastVisibleItemsCount = layoutManager.findFirstVisibleItemPosition();
        final boolean lastItemShown = visibleItemsCount + pastVisibleItemsCount >= totalItemsCount;
        return lastItemShown && totalItemsCount >= maxItemsPerRequest;
    }

    /**
     * Callback method to be invoked when the RecyclerView has been scrolled to the end
     *
     * @param firstVisibleItemPosition Id of the first visible item on the list.
     */
    public abstract void onScrolledToEnd(final int firstVisibleItemPosition);

    private void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    private void checkIfPositive(int number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
    }
}