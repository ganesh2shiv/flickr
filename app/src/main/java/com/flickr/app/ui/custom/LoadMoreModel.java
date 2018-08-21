package com.flickr.app.ui.custom;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.flickr.app.R;
import com.flickr.app.ui.base.BaseHolder;

import butterknife.BindView;

import static com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash;

@EpoxyModelClass(layout = R.layout.item_load_more)
public abstract class LoadMoreModel extends EpoxyModelWithHolder<LoadMoreModel.Holder> {

    @EpoxyAttribute boolean retry;
    @EpoxyAttribute(DoNotHash) View.OnClickListener clickListener;

    @Override
    public void bind(@NonNull Holder holder) {

        if (retry) {
            holder.btnRetry.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        } else {
            holder.btnRetry.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.VISIBLE);
        }

        holder.btnRetry.setOnClickListener(clickListener);
    }

    static class Holder extends BaseHolder {

        @BindView(R.id.progressBar) ProgressBar progressBar;
        @BindView(R.id.btn_retry) Button btnRetry;

    }
}