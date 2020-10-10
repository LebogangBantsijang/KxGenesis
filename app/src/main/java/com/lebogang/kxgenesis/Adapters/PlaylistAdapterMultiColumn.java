package com.lebogang.kxgenesis.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lebogang.audiofilemanager.Models.PlaylistMediaItem;
import com.lebogang.kxgenesis.databinding.ItemPlaylistMultiColumnBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlaylistAdapterMultiColumn extends RecyclerView.Adapter<PlaylistAdapterMultiColumn.Holder>{

    private List<PlaylistMediaItem> items = new ArrayList<>();
    private GeneralItemClick clickInterface;

    public PlaylistAdapterMultiColumn(GeneralItemClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    public PlaylistAdapterMultiColumn() {
    }

    public void setItems(List<PlaylistMediaItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void create(PlaylistMediaItem mediaItem){
        items.add(mediaItem);
        notifyItemInserted((1-items.size()));
    }

    public void update(PlaylistMediaItem mediaItem){
        int pos = 0;
        for (PlaylistMediaItem item:items){
            if (item.getMediaId() == mediaItem.getMediaId()){
                pos = items.indexOf(item);
                break;
            }
        }
        items.remove(pos);
        items.add(pos, mediaItem);
        notifyItemChanged(pos);
    }

    public void remove(PlaylistMediaItem mediaItem){
        int pos = items.indexOf(mediaItem);
        if (pos > -1)
            items.remove(pos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPlaylistMultiColumnBinding binding = ItemPlaylistMultiColumnBinding.inflate(inflater, parent, false);
        return new PlaylistAdapterMultiColumn.Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        PlaylistMediaItem item = items.get(position);
        holder.binding.titleTextView.setText(item.getTitle());
        holder.binding.numSongsTextView.setText(item.getTrackCount());
        holder.binding.subTitleTextView.setText(item.getSubTitle() + " (" + TimeUnit.MILLISECONDS.toMinutes(item.getDuration()) + "min)");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private ItemPlaylistMultiColumnBinding binding;

        public Holder(@NonNull View itemView, ItemPlaylistMultiColumnBinding binding) {
            super(itemView);
            this.binding = binding;
            binding.getRoot().setOnClickListener(v -> {
                clickInterface.onClick(items.get(getAdapterPosition()));
            });
            binding.imageButton2.setOnClickListener(v->{
                clickInterface.onOptionsClick(items.get(getAdapterPosition()));
            });
        }
    }
}
