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

import com.lebogang.audiofilemanager.Models.Artist;
import com.lebogang.kxgenesis.AppUtils.ArtistClickListener;
import com.lebogang.kxgenesis.R;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.Holder> implements FastScrollRecyclerView.SectionedAdapter {
    private Context context;
    private boolean isLayoutGrid = true;
    private List<Artist> list = new ArrayList<>();
    private ArtistClickListener artistClickListener;

    public void setArtistClickListener(ArtistClickListener artistClickListener) {
        this.artistClickListener = artistClickListener;
    }

    public void setLayoutGrid(boolean layoutGrid) {
        isLayoutGrid = layoutGrid;
    }

    public void setList(List<Artist> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (isLayoutGrid){
            View view = LayoutInflater.from(context).inflate(R.layout.item_artist_multi_column,parent, false);
            return new Holder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist_single_column,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Artist artist = list.get(position);
        holder.title.setText(artist.getTitle());
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
        private TextView title;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            itemView.setOnClickListener(v->{
                artistClickListener.onClick(list.get(getAdapterPosition()));
            });
        }
    }
}
