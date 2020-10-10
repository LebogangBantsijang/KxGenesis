package com.lebogang.kxgenesis.CallBacksAndAnimations;

import android.content.Context;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class PagerTransformer implements ViewPager2.PageTransformer {
    private int maxTranslateOffsetX;

    public PagerTransformer(Context context) {
        this.maxTranslateOffsetX = dp2px(context, 180);
    }

    public void transformPage(View view, float position) {
        int leftInScreen = view.getLeft() - view.getScrollX();
        int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
        int offsetX = centerXInViewPager - view.getMeasuredWidth() / 2;
        float offsetRate = (float) offsetX * 0.38f / view.getMeasuredWidth();
        float scaleFactor = 1 - Math.abs(offsetRate);

        if (scaleFactor > 0) {
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            view.setTranslationX(-maxTranslateOffsetX * offsetRate);
        }
    }

    /**
     * Dp to pixel conversion
     */
    private int dp2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }
}