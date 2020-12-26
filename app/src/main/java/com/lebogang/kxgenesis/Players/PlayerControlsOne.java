/*
 * Copyright (c) 2020. Lebogang Bantsijang
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

import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.Adapters.PlayerPagerAdapter;
import com.lebogang.kxgenesis.Animations.PagerTransformer;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.AppUtils.TimeUnitConvert;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;

import java.util.List;

public class PlayerControlsOne extends AbstractPlayer implements SongClickListener {
    private final AppCompatActivity activity;
    private final ImageButton nextButton, playButton, prevButton
            , shuffleButton, repeatButton, collapseButton;
    private final TextView titleTextView, subtitleTextView, startDurationTextView
            , endDurationTextView;
    private final ViewPager2 viewPager;
    private final SeekBar seekBar;
    private final PlayerPagerAdapter playerPagerAdapter = new PlayerPagerAdapter();


    public PlayerControlsOne(AppCompatActivity activity, View view) {
        this.activity = activity;
        nextButton = view.findViewById(R.id.nextImageButton);
        playButton = view.findViewById(R.id.playImageButton);
        prevButton = view.findViewById(R.id.prevImageButton);
        shuffleButton = view.findViewById(R.id.shuffleImageButton);
        repeatButton = view.findViewById(R.id.repeatImageButton);
        collapseButton = view.findViewById(R.id.collapseImageButton);
        titleTextView = view.findViewById(R.id.titleTextText);
        subtitleTextView = view.findViewById(R.id.subTitleTextView);
        startDurationTextView = view.findViewById(R.id.startDuration);
        endDurationTextView = view.findViewById(R.id.endDuration);
        viewPager = view.findViewById(R.id.pager);
        seekBar = view.findViewById(R.id.seekBar);
        initViewPager();
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

    private void initViewPager(){
        playerPagerAdapter.setSongClickListener(this);
        viewPager.setPageTransformer(new PagerTransformer(activity));
        viewPager.setAdapter(playerPagerAdapter);
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
        playerPagerAdapter.setCurrentPlayingId(audio.getId());
        int index = playerPagerAdapter.getIndex(audio.getId());
        viewPager.setCurrentItem(index);
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
    public void setPagerData(List<Audio> list) {
        playerPagerAdapter.setList(list);
    }

    @Override
    public void onClick(Audio audio) {
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(activity);
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", audio);
            bundle.putParcelableArrayList("List", playerPagerAdapter.getList());
            mediaControllerCompat.getTransportControls().playFromUri(audio.getUri(), bundle);
        }
    }

    @Override
    public void onClickOptions(Audio audio) {

    }
}
