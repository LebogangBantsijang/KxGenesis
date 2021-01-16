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
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.kxgenesis.AppUtils.PlaylistClickListener;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Room.Model.PlaylistDetails;
import com.lebogang.kxgenesis.databinding.ItemPlaylistBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.Holder>{
    private List<PlaylistDetails> list = new ArrayList<>();
    private PlaylistClickListener playlistClickListener;

    public void setPlaylistClickListener(PlaylistClickListener playlistClickListener) {
        this.playlistClickListener = playlistClickListener;
    }

    public void setList(List<PlaylistDetails> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(inflater, parent, false);
        return new Holder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PlaylistDetails playlist = list.get(position);
        holder.binding.titleTextView.setText(playlist.getPlaylistName());
        holder.binding.subTitleTextView.setText(playlist.getDateCreated());
        Uri uri = Uri.parse(playlist.getArtUri());
        Glide.with(holder.itemView).load(uri)
                .downsample(DownsampleStrategy.AT_MOST)
                .error(R.drawable.ic_playlist)
                .override(holder.binding.imageView.getWidth(), holder.binding.imageView.getHeight())
                .dontAnimate()
                .into(holder.binding.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private final ItemPlaylistBinding binding;
        public Holder(@NonNull View itemView, ItemPlaylistBinding binding) {
            super(itemView);
            this.binding = binding;
            binding.getRoot().setOnClickListener(v->{
                playlistClickListener.onClick(list.get(getAdapterPosition()));
            });
            binding.imageButton.setOnClickListener(v->{
                playlistClickListener.onClickOptions(list.get(getAdapterPosition()));
            });
        }
    }
}
