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

package com.lebogang.kxgenesis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerPagerAdapter extends RecyclerView.Adapter<PlayerPagerAdapter.Holder>{
    private List<Audio> list = new ArrayList<>();
    private SongClickListener songClickListener;
    private long currentPlayingId = -1;

    public void setList(List<Audio> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setSongClickListener(SongClickListener songClickListener) {
        this.songClickListener = songClickListener;
    }

    public ArrayList<Audio> getList() {
        return new ArrayList<>(list);
    }

    public void setCurrentPlayingId(long currentPlayingId) {
        this.currentPlayingId = currentPlayingId;
        notifyDataSetChanged();
    }

    public int getIndex(long id){
        for (Audio audio:list){
            if (audio.getId() == id)
                return list.indexOf(audio);
        }
        return 0;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Audio audio = list.get(position);
        Glide.with(holder.itemView).load(audio.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .into(holder.imageView)
                .waitForLayout();
        String string = "" + (1+position) + " of " + list.size();
        holder.countTextView.setText(string);
        if (audio.getId() == currentPlayingId)
            holder.titleTextView.setText("");
        else
            holder.titleTextView.setText(audio.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView countTextView, titleTextView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            countTextView = itemView.findViewById(R.id.counterTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView.setOnClickListener(v->{
                Audio audio = list.get(getAdapterPosition());
                if (audio.getId() != currentPlayingId)
                    songClickListener.onClick(audio);
            });
        }
    }
}
