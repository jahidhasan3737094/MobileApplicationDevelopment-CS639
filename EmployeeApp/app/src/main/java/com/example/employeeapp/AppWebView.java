package com.example.employeeapp;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.content.Context;

public class AppWebView extends WebView {

    public AppWebView(Context context) {
        super(context);
    }

    public AppWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }
}
