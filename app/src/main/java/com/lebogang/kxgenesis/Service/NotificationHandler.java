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

package com.lebogang.kxgenesis.Service;

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
import androidx.media.session.MediaButtonReceiver;

import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;

public class NotificationHandler {
    private final String NOTIFICATION_ID = "1021";
    private Context context;
    private MediaSessionCompat.Token token;
    private NotificationCompat.Builder builder;
    private NotificationChannel channel;
    private NotificationManagerCompat managerCompat;
    private PendingIntent prevPendingIntent, playPausePendingIntent, nextPendingIntent;

    public NotificationHandler(Context context, MediaSessionCompat.Token token) {
        //MediaButtonReceiver.buildMediaButtonPendingIntent()
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
        builder.addAction(R.drawable.ic_round_navigate_before_24,"Prev",prevPendingIntent);
        if (state == PlaybackStateCompat.STATE_PLAYING){
            builder.addAction(R.drawable.ic_round_pause_24,"Pause", playPausePendingIntent);
            builder.setSubText("Playing");
        }else {
            builder.addAction(R.drawable.ic_round_play_arrow_32,"Play", playPausePendingIntent);
            builder.setSubText("Paused");
        }
        builder.addAction(R.drawable.ic_round_navigate_next_24,"Next", nextPendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_MIN);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_google_play);
        builder.setStyle(new MediaStyle()
                .setMediaSession(token)
                .setShowActionsInCompactView(0,1,2)
                .setShowCancelButton(false)
        );
        builder.setContentIntent(MainActivity.getPendingIntent());
        builder.setCategory(NotificationCompat.CATEGORY_TRANSPORT);
        builder.setShowWhen(false);
        //firebase test lab is forcing me to do this
        if (mediaItem != null && mediaItem.getTitle() != null && mediaItem.getAlbumTitle()
                != null && mediaItem.getArtistTitle() != null && mediaItem.getAlbumArtUri() != null){
            builder.setContentText(mediaItem.getTitle());
            builder.setContentTitle(mediaItem.getArtistTitle() + " - "
                    +  mediaItem.getAlbumTitle());
            Bitmap bitmap = AudioIndicator.getBitmap(context, mediaItem.getAlbumArtUri());
            builder.setLargeIcon(bitmap);
        }
        builder.setSilent(true);
        return builder.build();
    }

}
