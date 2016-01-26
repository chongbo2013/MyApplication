package com.john.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by jiajun.jiang on 2015/12/28.
 */
public class BaseTopRelativeLayout extends RelativeLayout {

    public BaseTopRelativeLayout(Context context) {
        super(context);
    }

    public BaseTopRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTopRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }
}
