package com.lebogang.kxgenesis.CallBacksAndAnimations;

import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;

public class DrawerChangeCallback extends DrawerLayout.SimpleDrawerListener {
    private View containerView;

    public DrawerChangeCallback(View view) {
        this.containerView = view;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        float x = slideOffset * drawerView.getWidth();
        containerView.setX(x);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
    }
}
