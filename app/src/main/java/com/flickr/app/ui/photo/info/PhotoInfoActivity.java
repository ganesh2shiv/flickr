package com.flickr.app.ui.photo.info;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flickr.app.BaseApplication;
import com.flickr.app.R;
import com.flickr.app.ui.base.BaseActivity;
import com.flickr.app.ui.custom.SpannyText;
import com.flickr.app.ui.photo.explore.model.Photo;
import com.flickr.app.ui.photo.explore.model.PhotoSize;
import com.flickr.app.ui.photo.info.model.PhotoInfo;
import com.flickr.app.util.AlertUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.flickr.app.util.Constants.PHOTO_ITEM;
import static com.flickr.app.util.Constants.PHOTO_THUMB;

public class PhotoInfoActivity extends BaseActivity implements PhotoInfoView {

    private Photo photo;

    @Inject
    Picasso picasso;

    @Inject
    PhotoInfoPresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_main)
    NestedScrollView layoutMain;

    @BindView(R.id.iv_photo)
    PhotoView ivPhoto;

    @OnClick(R.id.iv_photo)
    void goBack() {
        finish();
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_total_views)
    TextView tvTotalViews;

    @BindView(R.id.tv_upload_date)
    TextView tvUploadDate;

    @BindView(R.id.tv_desc)
    TextView tvDesc;

    @BindView(R.id.tv_owner)
    TextView tvOwner;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_info);
        ButterKnife.bind(this);

        ((BaseApplication) getApplication()).getHomeComponent().inject(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        photo = Parcels.unwrap(getIntent().getParcelableExtra(PHOTO_ITEM));

        byte[] imageInByte = getIntent().getByteArrayExtra(PHOTO_THUMB);
        if (imageInByte != null) {
            Bitmap photoBitmap = BitmapFactory.decodeByteArray(imageInByte, 0, imageInByte.length);
            ivPhoto.setImageBitmap(photoBitmap);
        }

        presenter.setView(this);
        presenter.fetchPhotoInfo(photo.getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String msg) {
        AlertUtil.showSnackBar(layoutMain, msg);
    }

    @Override
    public void showInfo(PhotoInfo info) {
        try {
            picasso.load(photo.getUrl(PhotoSize.LARGE))
                    .error(R.drawable.ic_no_image)
                    .noPlaceholder()
                    .into(ivPhoto);
        } catch (Exception e) {
            Timber.e(e);
            picasso.load(R.drawable.ic_no_image).fit().into(ivPhoto);
        }

        if (info.getTitle() != null && !TextUtils.isEmpty(info.getTitle().getContent())) {
            tvTitle.setText(info.getTitle().getContent());
        }

        if (!TextUtils.isEmpty(info.getViews())) {
            SpannyText spanny = new SpannyText("Total views:", new StyleSpan(Typeface.BOLD))
                    .append(" ")
                    .append(info.getViews());

            tvTotalViews.setText(spanny);
        }

        if (!TextUtils.isEmpty(info.getDateuploaded())) {
            try {
                long seconds = Long.parseLong(info.getDateuploaded());
                Date uploadDate = new Date(seconds * 1000);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                SpannyText spanny = new SpannyText("Uploaded on:", new StyleSpan(Typeface.BOLD))
                        .append(" ")
                        .append(simpleDateFormat.format(uploadDate));

                tvUploadDate.setText(spanny);
            } catch (NumberFormatException e) {
                Timber.e(e);
            }
        }

        if (info.getOwner() != null && !TextUtils.isEmpty(info.getOwner().getRealname())) {
            SpannyText spanny = new SpannyText("Uploaded by:", new StyleSpan(Typeface.BOLD))
                    .append(" ")
                    .append(info.getOwner().getRealname());

            tvOwner.setText(spanny);
        }

        if (info.getDescription() != null && !TextUtils.isEmpty(info.getDescription().getContent())) {
            SpannyText spanny = new SpannyText("Description:", new StyleSpan(Typeface.BOLD))
                    .append(" ")
                    .append(info.getDescription().getContent());

            tvDesc.setText(spanny);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.destroy();
    }
}