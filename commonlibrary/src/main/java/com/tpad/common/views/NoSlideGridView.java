package com.tpad.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 禁止滑动的GridView
 * Created by jone.sun on 2015/7/15.
 */
public class NoSlideGridView extends GridView {
    public NoSlideGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public NoSlideGridView(Context context) {
        super(context);
    }
    public NoSlideGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
