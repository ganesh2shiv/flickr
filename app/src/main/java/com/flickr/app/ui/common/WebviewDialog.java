package com.flickr.app.ui.common;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.flickr.app.R;
import com.flickr.app.ui.base.BaseDialog;
import com.flickr.app.util.AlertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WebviewDialog extends BaseDialog {

    private Unbinder unbinder;
    private static final String URL = "url";

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    public static WebviewDialog newInstance(String url) {
        WebviewDialog dialog = new WebviewDialog();
        Bundle args = new Bundle();
        args.putString(URL, url);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        webView.setWebViewClient(new CustomWebViewClient(progressBar));
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.loadUrl(getArguments().getString(URL));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    private class CustomWebViewClient extends WebViewClient {

        private ProgressBar progressBar;

        CustomWebViewClient(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            super.onPageStarted(webView, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onReceivedError(WebView webView, int errorCode, String desc, String failingUrl) {
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.INVISIBLE);

            AlertUtil.showSnackBar(webView, desc);
        }
    }
}