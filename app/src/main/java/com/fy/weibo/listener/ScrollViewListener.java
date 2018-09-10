package com.fy.weibo.listener;

import android.support.v4.widget.NestedScrollView;

/**
 * Created by Fan on 2018/9/10.
 * Fighting!!!
 */
public class ScrollViewListener implements NestedScrollView.OnScrollChangeListener {

    private HideListener hideListener;
    private int hideHeight = 20;
    private int distance = 0;
    private boolean isVisible = true;

    public ScrollViewListener(HideListener hideListener) {
        this.hideListener = hideListener;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        if (distance > hideHeight && isVisible) {
            hideListener.hide();
            distance = 0;
            isVisible = false;
        } else if (distance < -20 && !isVisible) {
            hideListener.show();
            distance = 0;
            isVisible = true;
        }

        if ((isVisible && scrollY - oldScrollY > 0) ||(!isVisible && scrollY - oldScrollY < 0) ){
            distance += scrollY - oldScrollY;
        }
    }


}
