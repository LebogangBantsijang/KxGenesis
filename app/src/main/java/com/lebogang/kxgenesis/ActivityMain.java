package com.lebogang.kxgenesis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Media;
import com.lebogang.kxgenesis.Adapters.OnClickInterface;
import com.lebogang.kxgenesis.Adapters.PlayerPagerAdapter;
import com.lebogang.kxgenesis.CallBacksAndAnimations.BottomSheetChangeCallback;
import com.lebogang.kxgenesis.CallBacksAndAnimations.DrawerChangeCallback;
import com.lebogang.kxgenesis.CallBacksAndAnimations.PagerTransformer;
import com.lebogang.kxgenesis.MusicService.MusicConnection;
import com.lebogang.kxgenesis.Utils.AudioIndicator;
import com.lebogang.kxgenesis.Utils.ExtractColor;
import com.lebogang.kxgenesis.Utils.TimeUnitConvert;
import com.lebogang.kxgenesis.databinding.ActivityMainLayoutBinding;

import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class ActivityMain extends AppCompatActivity {
    private ActivityMainLayoutBinding binding;
    private NavController navController;
    private BottomSheetBehavior bottomSheetBehavior;
    private ThreadHandler threadHandler;
    private PlayerPagerAdapter pagerAdapter = new PlayerPagerAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("LightTheme", true))
            setTheme(R.style.AppTheme);
        else
            setTheme(R.style.AppTheme_Dark);
        binding = ActivityMainLayoutBinding.inflate(getLayoutInflater());
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
            init();
        else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 21);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int res = grantResults[0];
        if (res == PackageManager.PERMISSION_GRANTED)
            init();
        else finish();
    }

    private void init(){
        setContentView(binding.getRoot());
        setSupportActionBar(binding.mainLayout.mainContent.toolbar);
        setupNavComponent();
        setupDrawer();
        setupBottomSheets();
        setupPlayerControls();
        setupPager();
        onMediaItemChanged();
        new MusicConnection(this);
        threadHandler = new ThreadHandler(this, binding,this);
    }

    private void setupNavComponent(){
        navController = Navigation.findNavController(this, R.id.fragment_host);
        NavigationUI.setupWithNavController(binding.navView,navController);
        NavigationUI.setupWithNavController(binding.mainLayout.mainContent.toolbar, navController, binding.drawerLayout);
    }

    private void setupDrawer(){
        DrawerChangeCallback drawerChangeCallback = new DrawerChangeCallback(binding.mainLayout.getRoot());
        binding.drawerLayout.addDrawerListener(drawerChangeCallback);
    }

    private void setupBottomSheets(){
        bottomSheetBehavior = BottomSheetBehavior.from(binding.mainLayout.mainPlayer.getRoot());
        BottomSheetChangeCallback callback = new BottomSheetChangeCallback(binding.mainLayout.mainContent.getRoot()
                ,binding.mainLayout.mainPlayer.peekView, bottomSheetBehavior);
        bottomSheetBehavior.addBottomSheetCallback(callback);
        binding.mainLayout.mainPlayer.collapseImageButtonBack.setOnClickListener(v->
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
    }

    private void setupPager(){
        pagerAdapter.setClickInterface(media -> {
            Audio audio = (Audio) media;
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(this);
            if (mediaControllerCompat != null){
                Bundle bundle = new Bundle();
                bundle.putParcelable("Item", audio);
                mediaControllerCompat.getTransportControls().playFromUri(audio.getUri(), bundle);
            }
        });
        binding.mainLayout.mainPlayer.pager.setAdapter(pagerAdapter);
        binding.mainLayout.mainPlayer.pager.setPageTransformer(new PagerTransformer(this));
    }

    public void setPagerData(List<Audio> list){
        pagerAdapter.setList(list);
    }

    private void onMediaItemChanged(){
        AudioIndicator.getCurrentItem().observe(this, mediaItem -> {
            binding.mainLayout.mainPlayer.firstTitle.setText(mediaItem.getTitle());
            binding.mainLayout.mainPlayer.secondTitle.setText(mediaItem.getTitle());
            binding.mainLayout.mainPlayer.firstSubtitle.setText(mediaItem.getArtistTitle());
            binding.mainLayout.mainPlayer.secondSubTitle.setText(mediaItem.getAlbumTitle());
            binding.mainLayout.mainPlayer.endDuration.setText(TimeUnitConvert.toMinutes(mediaItem.getAudioDuration()));
            binding.mainLayout.mainPlayer.wavSeekBar.setMax((int) mediaItem.getAudioDuration());
            pagerAdapter.setCurrentPlayingId(mediaItem.getId());
            binding.mainLayout.mainPlayer.pager.setCurrentItem(pagerAdapter.getIndex(mediaItem.getId()));
        });
    }

    private void setupPlayerControls(){
        binding.mainLayout.mainPlayer.playImageButton.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(this);
            if (mediaControllerCompat != null)
            if (mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
                mediaControllerCompat.getTransportControls().pause();
            else mediaControllerCompat.getTransportControls().play();
        });
        binding.mainLayout.mainPlayer.nextImageButton.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(this);
            if (mediaControllerCompat != null)
            mediaControllerCompat.getTransportControls().skipToNext();
        });
        binding.mainLayout.mainPlayer.prevImageButton.setOnClickListener(v -> {
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(this);
            if (mediaControllerCompat != null)
            mediaControllerCompat.getTransportControls().skipToPrevious();
        });
        binding.mainLayout.mainPlayer.playImageButton2.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(this);
            if (mediaControllerCompat != null)
            if (mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
                mediaControllerCompat.getTransportControls().pause();
            else mediaControllerCompat.getTransportControls().play();
        });
        binding.mainLayout.mainPlayer.nextImageButton2.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(this);
            if (mediaControllerCompat != null)
            mediaControllerCompat.getTransportControls().skipToNext();
        });
        binding.mainLayout.mainPlayer.prevImageButton2.setOnClickListener(v -> {
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(this);
            if (mediaControllerCompat != null)
            mediaControllerCompat.getTransportControls().skipToPrevious();
        });
        binding.mainLayout.mainPlayer.shuffleImageButton.setOnClickListener(v -> {
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(this);
            if (mediaControllerCompat != null)
            if (mediaControllerCompat.getShuffleMode() == PlaybackStateCompat.SHUFFLE_MODE_NONE)
                mediaControllerCompat.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
            else mediaControllerCompat.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
        });
        binding.mainLayout.mainPlayer.repeatImageButton.setOnClickListener(v->{
            MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(this);
            if (mediaControllerCompat != null)
            if (mediaControllerCompat.getRepeatMode() == PlaybackStateCompat.REPEAT_MODE_NONE)
                mediaControllerCompat.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL);
            else if (mediaControllerCompat.getRepeatMode() == PlaybackStateCompat.REPEAT_MODE_ALL)
                mediaControllerCompat.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE);
            else mediaControllerCompat.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE);
        });
        binding.mainLayout.mainPlayer.wavSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(ActivityMain.this);
                    if (mediaControllerCompat != null)
                    mediaControllerCompat.getTransportControls().seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void onPlaybackChanged(int state){
        if (state == PlaybackStateCompat.STATE_PLAYING){
            binding.mainLayout.mainPlayer.playImageButton.setImageResource(R.drawable.ic_pause);
            binding.mainLayout.mainPlayer.playImageButton2.setImageResource(R.drawable.ic_circled_pause);
        }else {
            binding.mainLayout.mainPlayer.playImageButton.setImageResource(R.drawable.ic_play_56dp);
            binding.mainLayout.mainPlayer.playImageButton2.setImageResource(R.drawable.ic_circled_play);
        }
        threadHandler.onPlaybackStateChanged(state);
    }

    public void onShuffleModeChanged(int shuffleMode){
        if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_NONE)
            binding.mainLayout.mainPlayer.shuffleImageButton.setImageResource(R.drawable.ic_shuffle_off);
        if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL)
            binding.mainLayout.mainPlayer.shuffleImageButton.setImageResource(R.drawable.ic_shuffle);
    }

    public void onRepeatModeChange(int repeatMode){
        if (repeatMode == PlaybackStateCompat.REPEAT_MODE_NONE)
            binding.mainLayout.mainPlayer.repeatImageButton.setImageResource(R.drawable.ic_repeat_off);
        if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ONE)
            binding.mainLayout.mainPlayer.repeatImageButton.setImageResource(R.drawable.ic_repeat_one);
        if (repeatMode == PlaybackStateCompat.REPEAT_MODE_ALL)
            binding.mainLayout.mainPlayer.repeatImageButton.setImageResource(R.drawable.ic_repeat_all);
    }

    public void onConnectionFailed(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Error");
        builder.setIcon(R.drawable.ic_info);
        builder.setMessage("The Music service failed to start. The behaviour of the app is unknown at the point." +
                " It might crash or not respond, we don't know.\n\nBest solution is to restart the application");
        builder.create().show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp()|super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else if (!navController.navigateUp())
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
                navController.navigate(R.id.search_fragment);
                break;
            case R.id.menu_settings:
                navController.navigate(R.id.settings_fragment);
                break;
            case R.id.menu_volume:
                navController.navigate(R.id.volume_fragment);
                break;
        }
        return true;
    }

}