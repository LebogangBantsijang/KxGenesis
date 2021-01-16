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

package com.lebogang.kxgenesis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.AppSettings;
import com.lebogang.kxgenesis.Players.AbstractPlayer;
import com.lebogang.kxgenesis.Players.PlayerControlsFour;
import com.lebogang.kxgenesis.Players.PlayerControlsOne;
import com.lebogang.kxgenesis.Players.PlayerControlsThree;
import com.lebogang.kxgenesis.Players.PlayerControlsTwo;
import com.lebogang.kxgenesis.Service.MusicConnection;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private ActivityMainBinding binding;
    private NavController navController;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private AbstractPlayer player;
    private ThreadHandler threadHandler;
    public static int COLOR;
    public static int COLOR_SURFACE;
    public static int TEXT_COLOR;
    private List<Audio> list = new ArrayList<>();
    private static PendingIntent PENDING_INTENT;
    private AudioManager audioManager;
    private int selectedPlayerIndex;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MusicConnection(this).connectService();
        threadHandler = new ThreadHandler(this, this);
        setTheme(AppSettings.getTheme(this));
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        PENDING_INTENT = PendingIntent.getActivity(this,1211
                ,Intent.makeMainActivity(getComponentName()).addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                , PendingIntent.FLAG_UPDATE_CURRENT);
        initColors();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result:grantResults){
            if (result == PackageManager.PERMISSION_DENIED){
                showPermissionDialog();
                break;
            }
        }
    }

    private void showPermissionDialog(){
        new MaterialAlertDialogBuilder(this)
                .setTitle("Permission Error")
                .setIcon(R.drawable.ic_round_warning_24)
                .setMessage("Due to the fact that you refuse to grant the app permission to access your music files, " +
                        "the app will shut down and you can try again by reopening the app and granting the permission. Thank you for your time.")
                .setPositiveButton("Accept", null)
                .setOnDismissListener(dialog -> finish())
                .create().show();
    }

    private void initViews(){
        navController = Navigation.findNavController(this, R.id.fragment_host);
        navController.addOnDestinationChangedListener(this);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        setBottomSheetBehavior(false);
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetsView);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED){
                    binding.fragmentHostContainerView.setVisibility(View.GONE);
                    setNavigationColor(AudioIndicator.Colors.getBackgroundColor(), true);
                }
                if (i == BottomSheetBehavior.STATE_HIDDEN){
                    binding.fragmentHostContainerView.setVisibility(View.VISIBLE);
                    setNavigationColor(MainActivity.COLOR_SURFACE, true);
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
    }

    public void setNavigationColor(int color, boolean allow){
        if (selectedPlayerIndex == 3 && allow)
            getWindow().setNavigationBarColor(color);
    }

    public int getBottomSheetState(){
        return bottomSheetBehavior.getState();
    }

    @SuppressLint("NonConstantResourceId")
    public void setBottomSheetBehavior(boolean value){
        binding.bottomSheetsView.removeAllViews();
        int playerRes = AppSettings.getSelectedPlayer(this);
        selectedPlayerIndex = AppSettings.getSelectedPlayerIndex(this);
        View view = getLayoutInflater().inflate(playerRes,
                null);
        switch (playerRes){
            case R.layout.player_view_one:
                player = new PlayerControlsOne(this, view);
                break;
            case R.layout.player_view_two:
                player = new PlayerControlsTwo(this, view);
                break;
            case R.layout.player_view_three:
                player = new PlayerControlsThree(this, view);
                break;
            case R.layout.player_view_four:
                player = new PlayerControlsFour(this, view);
                break;
        }
        threadHandler.setSeekBar(player.getSeekBar());
        threadHandler.setStartDurationTextView(player.getStartDurationTextView());
        binding.bottomSheetsView.addView(view);
        if (value){
            Audio audio = AudioIndicator.getCurrentItem().getValue();
            if (audio != null){
                threadHandler.onPlaybackStateChanged(PlaybackStateCompat.STATE_PAUSED);
                player.setPagerData(list);
                player.onMediaChanged(audio);
                MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
                player.onPlaybackChanged(mediaController.getPlaybackState().getState());
                player.onRepeatModeChange(mediaController.getRepeatMode());
                player.onShuffleModeChanged(mediaController.getShuffleMode());
                threadHandler.onPlaybackStateChanged(mediaController.getPlaybackState().getState());
            }
        }
    }

    private void initColors(){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
        MainActivity.COLOR = typedValue.data;
        if (AppSettings.isThemeLight(this)){
            MainActivity.COLOR_SURFACE = getResources().getColor(R.color.colorWhite, getTheme());
            MainActivity.TEXT_COLOR = getResources().getColor(R.color.colorSurfaceDark, getTheme());
        }else {
            MainActivity.COLOR_SURFACE = getResources().getColor(R.color.colorSurfaceDark, getTheme());
            MainActivity.TEXT_COLOR = getResources().getColor(R.color.colorWhite, getTheme());
        }
    }

    public void expandBottomSheets(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void collapse(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp()|super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        else if (!navController.navigateUp())
            moveTaskToBack(true);
    }

    public void onConnectionFailed() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Error");
        builder.setIcon(R.drawable.ic_info);
        builder.setMessage("The Music service failed to start. The behaviour of the app is unknown at this point." +
                " It might crash or not respond, we don't know.\n\nBest solution is to restart the application");
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        AudioIndicator.getCurrentItem().observe(this, audio -> player.onMediaChanged(audio));
    }

    @Override
    protected void onStop() {
        super.onStop();
        AudioIndicator.getCurrentItem().removeObservers(this);
    }

    public void setPagerData(List<Audio> list){
        this.list = list;
        player.setPagerData(list);
    }

    public void onPlaybackChanged(int state) {
        player.onPlaybackChanged(state);
        threadHandler.onPlaybackStateChanged(state);
    }

    public void onShuffleModeChanged(int shuffleMode) {
        player.onShuffleModeChanged(shuffleMode);
    }

    public void onRepeatModeChange(int repeatMode) {
        player.onRepeatModeChange(repeatMode);
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        int desId = destination.getId();
        if (desId == R.id.songs_fragment || desId == R.id.albums_fragment || desId == R.id.artists_fragment
                || desId == R.id.playlists_fragment || desId == R.id.home_fragment){
            binding.bottomNavigation.setVisibility(View.VISIBLE);
        }else {
            binding.bottomNavigation.setVisibility(View.GONE);
        }
    }

    public static PendingIntent getPendingIntent(){
        return PENDING_INTENT;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP|| keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE ){
            player.setVolumeToSeekBar(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }
        return super.onKeyDown(keyCode, event);
    }


}