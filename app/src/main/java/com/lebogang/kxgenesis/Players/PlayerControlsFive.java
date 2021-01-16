/*
 * Copyright (c) 2021. Lebogang Bantsijang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lebogang.kxgenesis.Players;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.AppUtils.TimeUnitConvert;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;

import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class PlayerControlsFive extends AbstractPlayer{
    private final AppCompatActivity activity;
    private final ImageButton nextButton, playButton, prevButton
            , shuffleButton, repeatButton, collapseButton, shareButton;
    private final TextView titleTextView, subtitleTextView, startDurationTextView
            , endDurationTextView;
    private final SeekBar seekBar, volSeekBar;
    private final ImageView albumArtImageView, backImageView;

    public PlayerControlsFive(AppCompatActivity activity, View view) {
        this.activity = activity;
        nextButton = view.findViewById(R.id.nextImageButton);
        playButton = view.findViewById(R.id.playImageButton);
        prevButton = view.findViewById(R.id.prevImageButton);
        shuffleButton = view.findViewById(R.id.shuffleImageButton);
        repeatButton = view.findViewById(R.id.repeatImageButton);
        collapseButton = view.findViewById(R.id.collapseImageButton);
        shareButton = view.findViewById(R.id.shareImageButton);
        titleTextView = view.findViewById(R.id.titleTextText);
        subtitleTextView = view.findViewById(R.id.subTitleTextView);
        startDurationTextView = view.findViewById(R.id.startDuration);
        endDurationTextView = view.findViewById(R.id.endDuration);
        seekBar = view.findViewById(R.id.seekBar);
        volSeekBar = view.findViewById(R.id.volSeekBar);
        albumArtImageView = view.findViewById(R.id.albumArtImageView);
        backImageView = view.findViewById(R.id.backImageView);
        super.initViews();
    }


    @Override
    public SeekBar getSeekBar() {
        return seekBar;
    }

    @Override
    public TextView getStartDurationTextView() {
        return startDurationTextView;
    }

    @Override
    public void onPlaybackChanged(int state) {
        if (state == PlaybackStateCompat.STATE_PLAYING){
            playButton.setImageResource(R.drawable.ic_pause);
        }else {
            playButton.setImageResource(R.drawable.ic_play_56dp);
        }
    }

    @Override
    public void onShuffleModeChanged(int state) {
        if (state == PlaybackStateCompat.SHUFFLE_MODE_NONE)
            shuffleButton.setImageResource(R.drawable.ic_shuffle_off);
        if (state == PlaybackStateCompat.SHUFFLE_MODE_ALL)
            shuffleButton.setImageResource(R.drawable.ic_shuffle);
    }

    @Override
    public void onRepeatModeChange(int state) {
        if (state == PlaybackStateCompat.REPEAT_MODE_NONE)
            repeatButton.setImageResource(R.drawable.ic_repeat_off);
        if (state == PlaybackStateCompat.REPEAT_MODE_ONE)
            repeatButton.setImageResource(R.drawable.ic_repeat_one);
        if (state == PlaybackStateCompat.REPEAT_MODE_ALL)
            repeatButton.setImageResource(R.drawable.ic_repeat_all);
    }

    @Override
    public void onMediaChanged(Audio audio) {
        if (seekBar.isEnabled())
            seekBar.setEnabled(true);
        titleTextView.setText(audio.getTitle());
        subtitleTextView.setText(audio.getArtistTitle());
        endDurationTextView.setText(TimeUnitConvert.toMinutes(audio.getAudioDuration()));
        seekBar.setMax((int) audio.getAudioDuration());
        Glide.with(activity)
                .load(audio.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .into(albumArtImageView)
                .waitForLayout();
        @SuppressLint("UseCompatLoadingForDrawables")
        GradientDrawable gradientDrawable = (GradientDrawable) activity.getResources().getDrawable(R.drawable.shape_overlay, activity.getTheme());
        int colorTop = activity.getResources().getColor(R.color.colorTranslucent,activity.getTheme());
        int[] colors = {colorTop, AudioIndicator.Colors.getBackgroundColor()};
        gradientDrawable.setColors(colors);
        Bitmap bitmap = AudioIndicator.getBitmap(activity, audio.getAlbumArtUri());
        Blurry.with(activity)
                .radius(30)
                .from(bitmap)
                .into(backImageView);
        ((MainActivity) activity).setNavigationColor(AudioIndicator.Colors.getBackgroundColor()
                , ((MainActivity) activity).getBottomSheetState() == BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onPlayClick() {
        playButton.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(activity);
            if (mediaControllerCompat != null)
                if (mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
                    mediaControllerCompat.getTransportControls().pause();
                else mediaControllerCompat.getTransportControls().play();
        });
    }

    @Override
    public void onNextClick() {
        nextButton.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(activity);
            if (mediaControllerCompat != null)
                mediaControllerCompat.getTransportControls().skipToNext();
        });
    }

    @Override
    public void onPrevClick() {
        prevButton.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(activity);
            if (mediaControllerCompat != null)
                mediaControllerCompat.getTransportControls().skipToPrevious();
        });
    }

    @Override
    public void onRepeatClick() {
        repeatButton.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(activity);
            if (mediaControllerCompat != null)
                if (mediaControllerCompat.getRepeatMode() == PlaybackStateCompat.REPEAT_MODE_NONE)
                    mediaControllerCompat.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL);
                else if (mediaControllerCompat.getRepeatMode() == PlaybackStateCompat.REPEAT_MODE_ALL)
                    mediaControllerCompat.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE);
                else mediaControllerCompat.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE);
        });
    }

    @Override
    public void onShuffleClick() {
        shuffleButton.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(activity);
            if (mediaControllerCompat != null)
                if (mediaControllerCompat.getShuffleMode() == PlaybackStateCompat.SHUFFLE_MODE_NONE)
                    mediaControllerCompat.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
                else mediaControllerCompat.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
        });
    }

    @Override
    public void onSeek() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(activity);
                    if (mediaControllerCompat != null && AudioIndicator.getCurrentItem().getValue() != null)
                        if (seekBar.getMax() > 0)
                            mediaControllerCompat.getTransportControls().seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    public void onCollapse() {
        collapseButton.setOnClickListener(v->{
            ((MainActivity) activity).collapse();
        });
    }

    @Override
    public void onShare() {
        shareButton.setOnClickListener(v->{
            Audio audio = AudioIndicator.getCurrentItem().getValue();
            if (audio != null){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("audio/*");
                intent.putExtra(Intent.EXTRA_STREAM, audio.getUri());
                activity.startActivity(Intent.createChooser(intent,"Share Song"));
            }
        });
    }

    @Override
    protected void onVolume() {
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        volSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_SHOW_UI);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void setVolumeToSeekBar(int volume) {
        volSeekBar.setProgress(volume);
    }

    @Override
    public void setPagerData(List<Audio> list) {
    }

}
