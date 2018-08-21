package com.flickr.app.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Util {

    private Util() {
    }

    public static void showSoftKeyboard(final Context context, final EditText editText) {
        editText.requestFocus();
        new Handler().postDelayed(() -> {
            final InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 0);
    }

    public static void hideSoftKeyboard(final Context context, final EditText editText) {
        editText.clearFocus();
        final InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static boolean handleUrl(Context context, String url) {
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(url));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
                return true;
            }
        } else if (url.startsWith("mailto:")) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(url));
            intent.putExtra(Intent.EXTRA_EMAIL, Uri.parse(url));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
                return true;
            }
        } else if (url.startsWith("http")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }
}