package com.flickr.app.ui.custom;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LoopViewPager extends ViewPager {

    private static final boolean DEFAULT_BOUNDARY_CASHING = false;
    private static final boolean DEFAULT_BOUNDARY_LOOPING = true;

    private LoopPagerAdapterWrapper mAdapter;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;
    private boolean mBoundaryLooping = DEFAULT_BOUNDARY_LOOPING;

    public LoopViewPager(Context context) {
        super(context);
        init();
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        if (onPageChangeListener != null) {
            super.removeOnPageChangeListener(onPageChangeListener);
        }
        super.addOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new LoopPagerAdapterWrapper(adapter);
        mAdapter.setBoundaryCaching(mBoundaryCaching);
        mAdapter.setBoundaryLooping(mBoundaryLooping);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        addOnPageChangeListener(listener);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners == null) {
            mOnPageChangeListeners = new ArrayList<>();
        }
        mOnPageChangeListeners.add(listener);
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.remove(listener);
        }
    }

    @Override
    public void clearOnPageChangeListeners() {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.clear();
        }
    }

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {

            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;

                if (mOnPageChangeListeners != null) {
                    for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                        OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                        if (listener != null) {
                            listener.onPageSelected(realPosition);
                        }
                    }
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);

                if (positionOffset == 0 && mPreviousOffset == 0 && (position == 0
                        || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            mPreviousOffset = positionOffset;

            if (mOnPageChangeListeners != null) {
                for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                    OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                    if (listener != null) {
                        if (realPosition != mAdapter.getRealCount() - 1) {
                            listener.onPageScrolled(realPosition, positionOffset,
                                    positionOffsetPixels);
                        } else {
                            if (positionOffset > .5) {
                                listener.onPageScrolled(0, 0, 0);
                            } else {
                                listener.onPageScrolled(realPosition, 0, 0);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = LoopViewPager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE && (position == 0
                        || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            if (mOnPageChangeListeners != null) {
                for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                    OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                    if (listener != null) {
                        listener.onPageScrollStateChanged(state);
                    }
                }
            }
        }
    };

    private class LoopPagerAdapterWrapper extends PagerAdapter {

        private PagerAdapter mAdapter;

        private SparseArray<ToDestroy> mToDestroy = new SparseArray<>();

        private static final boolean DEFAULT_BOUNDARY_CASHING = true;
        private static final boolean DEFAULT_BOUNDARY_LOOPING = true;

        private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;
        private boolean mBoundaryLooping = DEFAULT_BOUNDARY_LOOPING;

        void setBoundaryCaching(boolean flag) {
            mBoundaryCaching = flag;
        }

        void setBoundaryLooping(boolean flag) {
            mBoundaryLooping = flag;
        }

        LoopPagerAdapterWrapper(PagerAdapter adapter) {
            this.mAdapter = adapter;
        }

        @Override
        public void notifyDataSetChanged() {
            mToDestroy = new SparseArray<>();
            super.notifyDataSetChanged();
        }

        int toRealPosition(int position) {
            int realPosition = position;
            int realCount = getRealCount();
            if (realCount == 0) return 0;
            if (mBoundaryLooping) {
                realPosition = (position - 1) % realCount;
                if (realPosition < 0) realPosition += realCount;
            }

            return realPosition;
        }

        int toInnerPosition(int realPosition) {
            int position = (realPosition + 1);
            return mBoundaryLooping ? position : realPosition;
        }

        private int getRealFirstPosition() {
            return mBoundaryLooping ? 1 : 0;
        }

        private int getRealLastPosition() {
            return getRealFirstPosition() + getRealCount() - 1;
        }

        @Override
        public int getCount() {
            int count = getRealCount();
            return mBoundaryLooping ? count + 2 : count;
        }

        int getRealCount() {
            return mAdapter.getCount();
        }

        PagerAdapter getRealAdapter() {
            return mAdapter;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realPosition = (mAdapter instanceof FragmentPagerAdapter
                    || mAdapter instanceof FragmentStatePagerAdapter) ? position
                    : toRealPosition(position);

            if (mBoundaryCaching) {
                ToDestroy toDestroy = mToDestroy.get(position);
                if (toDestroy != null) {
                    mToDestroy.remove(position);
                    return toDestroy.object;
                }
            }
            return mAdapter.instantiateItem(container, realPosition);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            int realFirst = getRealFirstPosition();
            int realLast = getRealLastPosition();
            int realPosition = (mAdapter instanceof FragmentPagerAdapter
                    || mAdapter instanceof FragmentStatePagerAdapter) ? position
                    : toRealPosition(position);

            if (mBoundaryCaching && (position == realFirst || position == realLast)) {
                mToDestroy.put(position, new ToDestroy(container, realPosition, object));
            } else {
                mAdapter.destroyItem(container, realPosition, object);
            }
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            mAdapter.finishUpdate(container);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return mAdapter.isViewFromObject(view, object);
        }

        @Override
        public void restoreState(Parcelable bundle, ClassLoader classLoader) {
            mAdapter.restoreState(bundle, classLoader);
        }

        @Override
        public Parcelable saveState() {
            return mAdapter.saveState();
        }

        @Override
        public void startUpdate(ViewGroup container) {
            mAdapter.startUpdate(container);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mAdapter.setPrimaryItem(container, position, object);
        }

        class ToDestroy {
            ViewGroup container;
            int position;
            Object object;

            ToDestroy(ViewGroup container, int position, Object object) {
                this.container = container;
                this.position = position;
                this.object = object;
            }
        }
    }
}