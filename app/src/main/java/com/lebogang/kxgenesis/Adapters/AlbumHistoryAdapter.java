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

package com.lebogang.kxgenesis.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.AlbumClickListener;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Room.Model.AlbumHistory;
import com.lebogang.kxgenesis.Room.Model.AlbumHistory;
import com.lebogang.kxgenesis.databinding.ItemHistoryAlbumBinding;
import com.lebogang.kxgenesis.databinding.ItemHistorySongBinding;

import java.util.ArrayList;
import java.util.List;

public class AlbumHistoryAdapter extends RecyclerView.Adapter<AlbumHistoryAdapter.Holder> {
    private List<AlbumHistory> list = new ArrayList<>();
    private AlbumClickListener albumClickListener;

    public void addAlbumHistory(AlbumHistory albumHistory){
        if (list.add(albumHistory)){
            notifyItemInserted(list.indexOf(albumHistory));
        }
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public void setAlbumClickListener(AlbumClickListener albumClickListener) {
        this.albumClickListener = albumClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHistoryAlbumBinding binding = ItemHistoryAlbumBinding.inflate(inflater, parent, false);
        return new Holder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AlbumHistory albumHistory = list.get(position);
        holder.binding.titleTextView.setText(albumHistory.getAlbum().getTitle());
        holder.binding.subtitleTextView.setText(albumHistory.getAlbum().getArtist());
        holder.binding.dateTextView.setText(albumHistory.getDateModified());
        Glide.with(holder.itemView)
                .load(albumHistory.getAlbum().getAlbumArtUri())
                .error(R.drawable.ic_music_record_light)
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .into(holder.binding.imageView)
                .waitForLayout();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private final ItemHistoryAlbumBinding binding;
        public Holder(@NonNull View itemView, ItemHistoryAlbumBinding binding) {
            super(itemView);
            this.binding = binding;
            itemView.setOnClickListener(v->{
                albumClickListener.onClick(list.get(getAdapterPosition()).getAlbum());
            });
        }
    }
}
