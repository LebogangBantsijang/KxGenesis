package com.lebogang.kxgenesis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.Models.ArtistMediaItem;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.ItemGeneralBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ArtistAdapterMultiColumn extends RecyclerView.Adapter<ArtistAdapterMultiColumn.Holder>{

    private GeneralItemClick clickInterface;
    private List<ArtistMediaItem> items = new ArrayList<>();
    private Context context;

    public ArtistAdapterMultiColumn(GeneralItemClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    public ArtistAdapterMultiColumn() {
    }

    public void setItems(List<ArtistMediaItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemGeneralBinding binding = ItemGeneralBinding.inflate(inflater, parent, false);
        return new ArtistAdapterMultiColumn.Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ArtistMediaItem item = items.get(position);
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
        private ItemGeneralBinding binding;

        public Holder(@NonNull View itemView, ItemGeneralBinding binding) {
            super(itemView);
            this.binding = binding;
            binding.getRoot().setOnClickListener(v->{
                clickInterface.onClick(items.get(getAdapterPosition()));
            });
        }
    }
}
