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
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Room.Model.SongHistory;
import com.lebogang.kxgenesis.databinding.ItemHistorySongBinding;

import java.util.ArrayList;
import java.util.List;

public class SongHistoryAdapter extends RecyclerView.Adapter<SongHistoryAdapter.Holder> {
    private List<SongHistory> list = new ArrayList<>();
    private SongClickListener songClickListener;
    private int highlightingColor = -1;
    private long audioId = -1;

    public void addSongHistory(SongHistory songHistory){
        if (list.add(songHistory)){
            notifyItemInserted(list.indexOf(songHistory));
        }
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public void setSongClickListener(SongClickListener songClickListener) {
        this.songClickListener = songClickListener;
    }

    public void setHighlightingColor(int highlightingColor) {
        this.highlightingColor = highlightingColor;
    }

    public void setAudioId(long audioId) {
        this.audioId = audioId;
    }

    public int getAudioPosition(Audio audio){
        for (SongHistory item:list){
            if (audio.getId() == item.getAudio().getId()){
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

    public ArrayList<Audio> getList(){
        ArrayList<Audio> arrayList = new ArrayList<>();
        for (SongHistory songHistory:list){
            arrayList.add(songHistory.getAudio());
        }
        return arrayList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHistorySongBinding binding = ItemHistorySongBinding.inflate(inflater, parent, false);
        return new Holder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SongHistory songHistory = list.get(position);
        holder.binding.titleTextView.setText(songHistory.getAudio().getTitle());
        holder.binding.subtitleTextView.setText(songHistory.getAudio().getArtistTitle() + " - " + songHistory.getAudio().getAlbumTitle());
        holder.binding.dateTextView.setText(songHistory.getDateString());
        Glide.with(holder.itemView)
                .load(songHistory.getAudio().getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .override(holder.binding.imageView.getWidth(), holder.binding.imageView.getHeight())
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .into(holder.binding.imageView)
                .waitForLayout();
        highlight(holder,songHistory.getAudio(),holder.itemView.getContext());
    }

    private void highlight(Holder holder, Audio audioItem, Context context){
        if (audioItem.getId() == audioId){
            @SuppressLint("UseCompatLoadingForDrawables") GradientDrawable gradientDrawable = (GradientDrawable)
                    context.getDrawable(R.drawable.shaper_rectangle);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            gradientDrawable.setColors(new int[]{highlightingColor, context.getResources().getColor(R.color.colorTranslucent,
                    context.getTheme())});
            holder.binding.getRoot().setBackground(gradientDrawable);
            holder.binding.titleTextView.setTextAppearance(R.style.LightTextColor);
        }else {
            holder.binding.getRoot().setBackgroundResource(R.drawable.shaper_rectangle_round_corners_4dp_with_bg);
            holder.binding.titleTextView.setTextAppearance(R.style.textColor);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private ItemHistorySongBinding binding;
        public Holder(@NonNull View itemView, ItemHistorySongBinding binding) {
            super(itemView);
            this.binding = binding;
            itemView.setOnClickListener(v->{
                songClickListener.onClick(list.get(getAdapterPosition()).getAudio());
            });
        }
    }
}
