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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Utils.TimeUnitConvert;
import com.lebogang.kxgenesis.databinding.ItemSong3Binding;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Holder> implements Filterable {
    private OnClickInterface clickInterface;
    private List<Audio> list = new ArrayList<>();
    private Context context;
    private long currentItemId = -1;
    private int color = -1;
    private List<Audio> searchItems = new ArrayList<>();

    public SearchAdapter(OnClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    public void setList(List<Audio> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<Audio> getList(){
        return new ArrayList<>(searchItems);
    }

    public void setCurrentID(long id, int color){
        this.currentItemId = id;
        this.color = color;
        notifyDataSetChanged();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSong3Binding binding = ItemSong3Binding.inflate(inflater,parent, false);
        return new SearchAdapter.Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Audio audioItem = searchItems.get(position);
        holder.binding.titleTextView.setText(audioItem.getTitle());
        holder.binding.subtitleTextView.setText(audioItem.getArtistTitle() + " - " + audioItem.getAlbumTitle());
        holder.binding.durationTextView.setText(TimeUnitConvert.toMinutes(audioItem.getAudioDuration()));
        Glide.with(context).load(audioItem.getAlbumArtUri())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_music_light)
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .skipMemoryCache(true)
                .into(holder.binding.imageView)
                .clearOnDetach()
                .waitForLayout();
        highlight(holder, audioItem,context);
    }

    protected void highlight(SearchAdapter.Holder holder, Audio audioItem, Context context){
        if (audioItem.getId() == currentItemId){
            holder.binding.lottieAnimationView.setVisibility(View.VISIBLE);
            GradientDrawable gradientDrawable = (GradientDrawable)
                    context.getDrawable(R.drawable.shaper_rectangle_round_corners_4dp_with_cp);
            gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
            gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
            gradientDrawable.setColors(new int[]{color, context.getResources().getColor(R.color.colorTranslucent, context.getTheme())});
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
        return searchItems.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint.length() == 0){
                    searchItems.clear();
                    return filterResults;
                }
                searchItems.clear();
                for (Audio item:list){
                    if (item.getTitle() != null && item.getAlbumTitle() != null && item.getArtistTitle() != null)
                        if (item.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())
                                || item.getAlbumTitle().toLowerCase().contains(constraint.toString().toLowerCase())
                                || item.getArtistTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                            searchItems.add(item);
                        }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    public class Holder extends RecyclerView.ViewHolder{
        public ItemSong3Binding binding;

        public Holder(@NonNull View itemView, ItemSong3Binding binding) {
            super(itemView);
            this.binding = binding;
            binding.getRoot().setOnClickListener(v->{
                clickInterface.onClick(searchItems.get(getAdapterPosition()));
            });
        }
    }
}
