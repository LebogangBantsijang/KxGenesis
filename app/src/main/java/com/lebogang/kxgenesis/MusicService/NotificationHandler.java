package com.lebogang.kxgenesis.MusicService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media.app.NotificationCompat.MediaStyle;

import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Utils.ExtractColor;

public class NotificationHandler {
    private final String NOTIFICATION_ID = "1021";
    private Context context;
    private MediaSessionCompat.Token token;
    private NotificationCompat.Builder builder;
    private NotificationChannel channel;
    private NotificationManagerCompat managerCompat;
    private PendingIntent prevPendingIntent, playPausePendingIntent, nextPendingIntent;

    public NotificationHandler(Context context, MediaSessionCompat.Token token) {
        this.context = context;
        this.token = token;
        prevPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(CustomReceiver.ACTION_PREVIOUS),PendingIntent.FLAG_UPDATE_CURRENT);
        playPausePendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(CustomReceiver.ACTION_PLAY_PAUSE),PendingIntent.FLAG_UPDATE_CURRENT);
        nextPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(CustomReceiver.ACTION_NEXT),PendingIntent.FLAG_UPDATE_CURRENT);
        managerCompat = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(NOTIFICATION_ID, "KxGen", NotificationManager.IMPORTANCE_LOW);
            channel.enableLights(false);
            channel.enableVibration(false);
            managerCompat.createNotificationChannel(channel);
        }
    }

    public void displayNotification(Audio mediaItem, int state){
        managerCompat.notify(1021,getNotification(mediaItem,state));
    }

    public Notification getNotification(Audio mediaItem, int state){
        builder = new NotificationCompat.Builder(context, NOTIFICATION_ID);
        builder.addAction(R.drawable.ic_round_navigate_before_24,"Prev", prevPendingIntent);
        if (state == PlaybackStateCompat.STATE_PLAYING){
            builder.addAction(R.drawable.ic_round_pause_24,"Pause", playPausePendingIntent);
            builder.setSubText("Playing");
        }else {
            builder.addAction(R.drawable.ic_round_play_arrow_24,"Play", playPausePendingIntent);
            builder.setSubText("Paused");
        }
        builder.addAction(R.drawable.ic_round_navigate_next_24,"Next", nextPendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_LOW);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_round_play_circle_outline_24);
        builder.setStyle(new MediaStyle()
                .setMediaSession(token)
                .setShowActionsInCompactView(0,1,2)
                .setShowCancelButton(false)
        );
        builder.setCategory(NotificationCompat.CATEGORY_TRANSPORT);
        builder.setShowWhen(false);
        builder.setContentText(mediaItem.getTitle());
        builder.setContentTitle(mediaItem.getArtistTitle() + " - " + mediaItem.getAlbumTitle());
        Bitmap bitmap = ExtractColor.getBitmap(context, mediaItem.getAlbumArtUri());
        builder.setLargeIcon(bitmap);
        return builder.build();
    }

}
