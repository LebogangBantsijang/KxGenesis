package com.lebogang.kxgenesis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.Models.AlbumMediaItem;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.ItemAlbumSingleColumnBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlbumAdapterSingleColumn extends RecyclerView.Adapter<AlbumAdapterSingleColumn.Holder>{

    private GeneralItemClick clickInterface;
    private List<AlbumMediaItem> items = new ArrayList<>();
    private Context context;

    public AlbumAdapterSingleColumn(GeneralItemClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    public AlbumAdapterSingleColumn() {
    }

    public void setItems(List<AlbumMediaItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemAlbumSingleColumnBinding binding = ItemAlbumSingleColumnBinding.inflate(inflater, parent, false);
        return new AlbumAdapterSingleColumn.Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AlbumMediaItem item = items.get(position);
        holder.binding.titleTextView.setText(item.getTitle());
        holder.binding.subtitleTextView.setText(item.getSubTitle() + "(" + TimeUnit.MILLISECONDS.toMinutes(item.getDuration()) + "min)");
        holder.binding.numSongsTextView.setText(item.getTrackCount());
        Glide.with(context).load(item.getAlbumArtUri())
                .error(R.drawable.ic_music_record_light)
                .into(holder.binding.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private ItemAlbumSingleColumnBinding binding;

        public Holder(@NonNull View itemView, ItemAlbumSingleColumnBinding binding) {
            super(itemView);
            this.binding = binding;
            binding.getRoot().setOnClickListener(v->{
                clickInterface.onClick(items.get(getAdapterPosition()));
            });
        }
    }
}
