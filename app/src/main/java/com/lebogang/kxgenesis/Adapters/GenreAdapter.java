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

import com.lebogang.audiofilemanager.Models.Genre;
import com.lebogang.kxgenesis.AppUtils.GenreClickListener;
import com.lebogang.kxgenesis.R;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.Holder>{
    private List<Genre> list = new ArrayList<>();
    private GenreClickListener genreClickListener;

    public GenreAdapter() {
    }

    public void setGenreClickListener(GenreClickListener genreClickListener) {
        this.genreClickListener = genreClickListener;
    }

    public void setList(List<Genre> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre_multiple_column,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.Holder holder, int position) {
        Genre generalItem = list.get(position);
        holder.title.setText(generalItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView title;

        public Holder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.titleTextView);
            itemView.setOnClickListener(v -> {
                genreClickListener.onClick(list.get(getAdapterPosition()));
            });
        }
    }
}
