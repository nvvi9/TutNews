package com.nvvi9.tutnews.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class ViewUtils {

    public static void showKeyboard(@NonNull View v) {
        Object inputMethodManager = v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager instanceof InputMethodManager) {
            ((InputMethodManager) inputMethodManager).showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideKeyboard(@NonNull View v) {
        Object inputMethodManager = v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager instanceof InputMethodManager) {
            ((InputMethodManager) inputMethodManager).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
