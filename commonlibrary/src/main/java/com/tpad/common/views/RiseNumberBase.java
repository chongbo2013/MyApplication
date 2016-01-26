package com.tpad.common.views;

/**
 * Created by jone.sun on 2015/5/18.
 */
public interface RiseNumberBase {
    public void start();

    public RiseNumberTextView withNumber(float fromNumber, float number);

    public RiseNumberTextView withNumber(int fromNumber, int number);

    public RiseNumberTextView setDuration(long duration);

    public void setOnEnd(RiseNumberTextView.EndListener callback);
}
