package com.lebogang.kxgenesis.CallBacksAndAnimations;

import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.lebogang.kxgenesis.R;

public class ControlsAnim {
    private ImageButton collapseImageButton;
    private ImageButton viewImageButton;
    private LinearLayout controlsLinearLayout;


    public ControlsAnim(ImageButton collapseImageButton, ImageButton viewImageButton, LinearLayout controlsLinearLayout) {
        this.collapseImageButton = collapseImageButton;
        this.viewImageButton = viewImageButton;
        this.controlsLinearLayout = controlsLinearLayout;
        this.viewImageButton.setTag(true);
        setupView();
    }

    private void setupView(){
        viewImageButton.setOnClickListener(v->{
            boolean tag = (boolean) v.getTag();
            float height = controlsLinearLayout.getHeight() - viewImageButton.getHeight();
            if (tag){
                controlsLinearLayout.animate().setDuration(500).translationY(height).start();
                collapseImageButton.animate().setDuration(500).translationY(-collapseImageButton.getHeight()).withEndAction(() -> {
                    viewImageButton.setImageResource(R.drawable.ic_collapse_arrow_16dp);
                }).start();
                viewImageButton.setTag(false);
            }else {
                controlsLinearLayout.animate().setDuration(500).translationY(0).start();
                collapseImageButton.animate().setDuration(500).translationY(0).withEndAction(()->{
                    viewImageButton.setImageResource(R.drawable.ic_expand_arrow_16dp);
                }).start();
                viewImageButton.setTag(true);
            }
        });
    }
}
