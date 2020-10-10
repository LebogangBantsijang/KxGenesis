package com.lebogang.kxgenesis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lebogang.audiofilemanager.Models.GenreMediaItem;
import com.lebogang.kxgenesis.databinding.ItemGenreBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.Holder>{
    private GeneralItemClick clickInterface;
    private List<GenreMediaItem> items = new ArrayList<>();
    private Context context;

    public GenreAdapter(GeneralItemClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    public GenreAdapter() {
    }

    public void setItems(List<GenreMediaItem> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenreAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemGenreBinding binding = ItemGenreBinding.inflate(inflater, parent, false);
        return new GenreAdapter.Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.Holder holder, int position) {
        GenreMediaItem generalItem = items.get(position);
        holder.binding.titleTextView.setText(generalItem.getTitle());
        holder.binding.subtitleTextView.setText(generalItem.getSubTitle() + "(" + TimeUnit.MILLISECONDS.toMinutes(generalItem.getDuration()) + "min)");
        holder.binding.countTextView.setText(generalItem.getTrackCount());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private ItemGenreBinding binding;

        public Holder(@NonNull View itemView, ItemGenreBinding binding) {
            super(itemView);
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                clickInterface.onClick(items.get(getAdapterPosition()));
            });
        }
    }
}
