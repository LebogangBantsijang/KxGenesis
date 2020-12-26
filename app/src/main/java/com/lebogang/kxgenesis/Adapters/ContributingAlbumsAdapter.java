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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.R;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ContributingAlbumsAdapter extends RecyclerView.Adapter<ContributingAlbumsAdapter.Holder>{
    private List<Audio> list = new ArrayList<>();
    private SongClickListener songClickListener;
    private final LinkedHashMap<Long, Audio> linkedHashMap = new LinkedHashMap<>();

    public void setList(List<Audio> list) {
        for (Audio audio: list){
            linkedHashMap.put(audio.getAlbumId(), audio);
        }
        this.list = new ArrayList<>(linkedHashMap.values());
        notifyDataSetChanged();
    }

    public void setSongClickListener(SongClickListener songClickListener) {
        this.songClickListener = songClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_total_artist,parent
                , false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Audio audio = list.get(position);
        holder.title.setText(audio.getAlbumTitle());
        Glide.with(holder.imageView).load(audio.getAlbumArtUri())
                .error(R.drawable.ic_music_record)
                .downsample(DownsampleStrategy.AT_MOST)
                .into(holder.imageView)
                .waitForLayout();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView imageView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(v->{
                songClickListener.onClick(list.get(getAdapterPosition()));
            });
        }
    }
}
