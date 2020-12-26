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
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.ItemSong2Binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaylistAudioAdapter extends RecyclerView.Adapter<PlaylistAudioAdapter.Holder>{
    private List<Audio> list = new ArrayList<>();
    private final HashMap<Long, Audio> checkedList = new HashMap<>();

    public PlaylistAudioAdapter() {
    }

    public void setList(List<Audio> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<Audio> getCheckedItems() {
        return new ArrayList<>(checkedList.values());
    }

    public void deleteAudio(Audio audio){
        int x = list.indexOf(audio);
        list.remove(audio);
        checkedList.remove(audio.getId(), audio);
        notifyItemRemoved(x);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSong2Binding binding = ItemSong2Binding.inflate(inflater,parent, false);
        return new Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Audio audioItem = list.get(position);
        holder.binding.titleTextView.setText(audioItem.getTitle());
        holder.binding.subtitleTextView.setText(audioItem.getArtistTitle() + " - " + audioItem.getAlbumTitle());
        Glide.with(holder.itemView).load(audioItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .downsample(DownsampleStrategy.AT_MOST)
                .into(holder.binding.imageView)
                .waitForLayout();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public ItemSong2Binding binding;

        public Holder(@NonNull View itemView, ItemSong2Binding binding) {
            super(itemView);
            this.binding = binding;
            binding.getRoot().setOnClickListener(v->{
                boolean value = !binding.checkBox.isChecked();
                binding.checkBox.setChecked(value);
            });
            binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Audio audio = list.get(getAdapterPosition());
                if (isChecked){
                    checkedList.put(audio.getId(), audio);
                }else {
                    checkedList.remove(audio.getId(), audio);
                }
            });
        }
    }
}
