package com.flickr.app.ui.photo.explore;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.flickr.app.R;
import com.flickr.app.ui.base.BaseHolder;
import com.flickr.app.ui.base.BaseView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import timber.log.Timber;

import static com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash;

@EpoxyModelClass(layout = R.layout.item_photo_explore)
public abstract class PhotoExploreItemModel extends EpoxyModelWithHolder<PhotoExploreItemModel.Holder> {

    @EpoxyAttribute BaseView view;
    @EpoxyAttribute Picasso picasso;
    @EpoxyAttribute String title;
    @EpoxyAttribute String url;
    @EpoxyAttribute(DoNotHash) View.OnClickListener clickListener;

    @Override
    public void bind(@NonNull Holder holder) {
        try {
            picasso.load(url)
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_no_image)
                    .into(holder.ivThumb);
        } catch (Exception e) {
            Timber.e(e);
            picasso.load(R.drawable.ic_no_image).fit().into(holder.ivThumb);
        }

        holder.getItemView().setOnClickListener(clickListener);
    }

    static class Holder extends BaseHolder {

        @BindView(R.id.iv_thumb) ImageView ivThumb;

    }
}