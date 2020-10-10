package com.lebogang.kxgenesis.CallBacksAndAnimations;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class BottomSheetChangeCallback extends BottomSheetBehavior.BottomSheetCallback{
    private View contentLayout;
    private View peekView;

    public BottomSheetChangeCallback(View contentLayout, View peekView, BottomSheetBehavior bottomSheetBehavior) {
        this.contentLayout = contentLayout;
        this.peekView = peekView;
        peekView.setOnClickListener(v->{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {

    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        float y = -bottomSheet.getHeight()*slideOffset;
        contentLayout.setY(y);
        float y2 = peekView.getHeight() * -slideOffset;
        peekView.setY(y2);
    }
}
