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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.kxgenesis.AppUtils.PlaylistClickListener;
import com.lebogang.kxgenesis.R;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.Holder>{
    private List<Playlist> list = new ArrayList<>();
    private PlaylistClickListener playlistClickListener;

    public void setPlaylistClickListener(PlaylistClickListener playlistClickListener) {
        this.playlistClickListener = playlistClickListener;
    }

    public void setList(List<Playlist> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_single_line, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Playlist playlist = list.get(position);
        holder.title.setText(playlist.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView title;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            itemView.setOnClickListener(v->{
                playlistClickListener.onClick(list.get(getAdapterPosition()));
            });
            itemView.findViewById(R.id.imageButton).setOnClickListener(v->{
                playlistClickListener.onClickOptions(list.get(getAdapterPosition()));
            });
        }
    }
}
