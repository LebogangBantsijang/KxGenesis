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
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.kxgenesis.AppUtils.AlbumClickListener;
import com.lebogang.kxgenesis.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> implements FastScrollRecyclerView.SectionedAdapter {
    private boolean isLayoutGrid = true;
    private List<Album> list = new ArrayList<>();
    private AlbumClickListener albumClickListener;

    public void setAlbumClickListener(AlbumClickListener albumClickListener) {
        this.albumClickListener = albumClickListener;
    }

    public void setLayoutGrid(boolean layoutGrid) {
        isLayoutGrid = layoutGrid;
    }

    public void setList(List<Album> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isLayoutGrid){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_multiple_column,parent, false);
            return new Holder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_single_column,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Album album = list.get(position);
        holder.title.setText(album.getTitle());
        holder.subtitle.setText(album.getArtist());
        Glide.with(holder.imageView).load(album.getAlbumArtUri())
                .error(R.drawable.ic_music_record_light)
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .into(holder.imageView)
                .waitForLayout();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        String name = list.get(position).getTitle() != null? list.get(position).getTitle():"?";
        return name.substring(0,1).toUpperCase();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView title, subtitle;
        private ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            subtitle = itemView.findViewById(R.id.subtitleTextView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(v->{
                albumClickListener.onClick(list.get(getAdapterPosition()));
            });
        }
    }
}
