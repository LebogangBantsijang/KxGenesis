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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lebogang.kxgenesis.AppUtils.SelectPlayerOnClick;
import com.lebogang.kxgenesis.R;

import java.util.ArrayList;
import java.util.List;

public class SelectPlayerAdapter extends RecyclerView.Adapter<SelectPlayerAdapter.Holder>{
    private SelectPlayerOnClick selectPlayerOnClick;
    private List<Integer> resourceList = new ArrayList<>();

    public SelectPlayerAdapter(SelectPlayerOnClick selectPlayerOnClick) {
        this.selectPlayerOnClick = selectPlayerOnClick;
        resourceList.add(R.raw.player_1);
        resourceList.add(R.raw.player_2);
        resourceList.add(R.raw.player_3);
        resourceList.add(R.drawable.ic_circled_play);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_player,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.imageView.setImageResource(resourceList.get(position));
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(v->{
                selectPlayerOnClick.onClick(getAdapterPosition());
            });
        }
    }
}
