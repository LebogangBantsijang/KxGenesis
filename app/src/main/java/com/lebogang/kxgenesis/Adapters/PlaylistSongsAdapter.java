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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.AppUtils.SongDeleteListener;
import com.lebogang.kxgenesis.AppUtils.TimeUnitConvert;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.ItemPlaylistSongBinding;
import com.lebogang.kxgenesis.databinding.ItemSongBinding;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;

public class PlaylistSongsAdapter extends RecyclerView.Adapter<PlaylistSongsAdapter.Holder> implements FastScrollRecyclerView.SectionedAdapter {
    private SongClickListener songClickListener;
    private SongDeleteListener songDeleteListener;
    private ArrayList<Audio> list = new ArrayList<>();
    private int highlightingColor = -1;
    private long audioId = -1;

    public void setSongClickListener(SongClickListener songClickListener) {
        this.songClickListener = songClickListener;
    }

    public void setSongDeleteListener(SongDeleteListener songDeleteListener) {
        this.songDeleteListener = songDeleteListener;
    }

    public void setList(ArrayList<Audio> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getAudioPosition(Audio audio){
        for (Audio item:list){
            if (audio.getId() == item.getId()){
                return list.indexOf(item);
            }
        }
        return 0;
    }

    public void setAudioIdHighlightingColor(long audioId, int highlightingColor) {
        this.highlightingColor = highlightingColor;
        this.audioId = audioId;
        notifyDataSetChanged();
    }

    public ArrayList<Audio> getList() {
        return list;
    }

    public void deleteAudio(Audio audio){
        for (int x = 0; x < list.size(); x++){
            Audio item = list.get(x);
            if (item.getId() == audio.getId()){
                list.remove(x);
                notifyItemRemoved(x);
                break;
            }
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistSongBinding binding = ItemPlaylistSongBinding.inflate(inflater, parent, false);
        return new Holder(binding.getRoot(), binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Audio audio = list.get(position);
        holder.binding.titleTextView.setText(audio.getTitle());
        holder.binding.subtitleTextView.setText(audio.getArtistTitle() + " - " + audio.getAlbumTitle());
        holder.binding.durationTextView.setText(TimeUnitConvert.toMinutes(audio.getAudioDuration()));
        Glide.with(holder.itemView)
                .load(audio.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .override(holder.binding.imageView.getWidth(), holder.binding.imageView.getHeight())
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .into(holder.binding.imageView)
                .waitForLayout();
        highlight(holder,audio,holder.itemView.getContext());
    }

    private void highlight(Holder holder, Audio audioItem, Context context){
        if (audioItem.getId() == audioId){
            holder.binding.lottieAnimationView.setVisibility(View.VISIBLE);
            @SuppressLint("UseCompatLoadingForDrawables") GradientDrawable gradientDrawable = (GradientDrawable)
                    context.getDrawable(R.drawable.shaper_rectangle_round_corners_4dp_with_cp);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            gradientDrawable.setColors(new int[]{highlightingColor, context.getResources().getColor(R.color.colorTranslucent,
                    context.getTheme())});
            holder.binding.getRoot().setBackground(gradientDrawable);
            holder.binding.titleTextView.setTextAppearance(R.style.LightTextColor);
        }else {
            holder.binding.getRoot().setBackgroundResource(R.drawable.shaper_rectangle_round_corners_4dp_with_bg);
            holder.binding.titleTextView.setTextAppearance(R.style.textColor);
            holder.binding.lottieAnimationView.setVisibility(View.GONE);
        }
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
        private final ItemPlaylistSongBinding binding;
        public Holder(@NonNull View itemView, ItemPlaylistSongBinding binding) {
            super(itemView);
            this.binding = binding;
            initViews();
        }

        private void initViews(){
            binding.getRoot().setOnClickListener(v->{
                songClickListener.onClick(list.get(getAdapterPosition()));
            });
            binding.deleteButton.setOnClickListener(v->{
                songDeleteListener.onDelete(list.get(getAdapterPosition()));
            });
        }
    }
}
