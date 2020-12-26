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
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.AppUtils.TimeUnitConvert;
import com.lebogang.kxgenesis.databinding.ItemSongBinding;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.Holder> implements Filterable {
    private SongClickListener songClickListener;
    private ArrayList<Audio> list = new ArrayList<>();
    private final ArrayList<Audio> searchList = new ArrayList<>();
    private boolean isUserSearching = false;
    private boolean hideOptionsButton = false;
    private int highlightingColor = -1;
    private long audioId = -1;

    public void setSongClickListener(SongClickListener songClickListener) {
        this.songClickListener = songClickListener;
    }

    public void setList(ArrayList<Audio> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setUserSearching(boolean userSearching) {
        isUserSearching = userSearching;
        notifyDataSetChanged();
    }

    public void setHideOptionsButton(boolean hideOptionsButton) {
        this.hideOptionsButton = hideOptionsButton;
    }

    public int getAudioPosition(Audio audio){
        return list.indexOf(audio);
    }

    public void setAudioIdHighlightingColor(long audioId, int highlightingColor) {
        this.highlightingColor = highlightingColor;
        this.audioId = audioId;
        notifyDataSetChanged();
    }

    public ArrayList<Audio> getSearchList() {
        return searchList;
    }

    public ArrayList<Audio> getList() {
        return list;
    }

    public void deleteAudio(Audio audio){
        for (int x = 0; x < list.size(); x++){
            Audio item = list.get(x);
            if (item.getId() == audio.getId()){
                list.remove(x);
                break;
            }
        }
        for (int x = 0; x < searchList.size(); x++){
            Audio item = searchList.get(x);
            if (item.getId() == audio.getId()){
                searchList.remove(x);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSongBinding binding = ItemSongBinding.inflate(inflater, parent, false);
        return new Holder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Audio audio;
        if (isUserSearching)
            audio = searchList.get(position);
        else
            audio = list.get(position);
        holder.binding.titleTextView.setText(audio.getTitle());
        holder.binding.subtitleTextView.setText(audio.getArtistTitle() + " - " + audio.getAlbumTitle());
        holder.binding.durationTextView.setText(TimeUnitConvert.toMinutes(audio.getAudioDuration()));
        Glide.with(holder.itemView)
                .load(audio.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .into(holder.binding.imageView)
                .waitForLayout();
        highlight(holder,audio,holder.itemView.getContext());
    }

    private void highlight(Holder holder, Audio audioItem, Context context){
        if (audioItem.getId() == audioId){
            holder.binding.lottieAnimationView.setVisibility(View.VISIBLE);
            GradientDrawable gradientDrawable = (GradientDrawable)
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
        if (isUserSearching)
            return searchList.size();
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                searchList.clear();
                if (constraint == null || constraint.length() < 1){
                    return new FilterResults();
                }
                for (Audio item:list){
                    if (item.getTitle() != null && item.getAlbumTitle() != null && item.getArtistTitle() != null)
                        if (item.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())
                                || item.getAlbumTitle().toLowerCase().contains(constraint.toString().toLowerCase())
                                || item.getArtistTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                            searchList.add(item);
                        }
                }
                return new FilterResults();
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }


    public class Holder extends RecyclerView.ViewHolder{
        private final ItemSongBinding binding;
        public Holder(@NonNull View itemView, ItemSongBinding binding) {
            super(itemView);
            this.binding = binding;
            initViews();
        }

        private void initViews(){
            binding.getRoot().setOnClickListener(v->{
                if (isUserSearching)
                    songClickListener.onClick(searchList.get(getAdapterPosition()));
                else
                    songClickListener.onClick(list.get(getAdapterPosition()));
            });
            if (hideOptionsButton){
                binding.optionsButton.setImageDrawable(null);
            }else {
                binding.optionsButton.setOnClickListener(v->{
                    if (isUserSearching)
                        songClickListener.onClickOptions(searchList.get(getAdapterPosition()));
                    else
                        songClickListener.onClickOptions(list.get(getAdapterPosition()));
                });
            }
        }
    }
}
