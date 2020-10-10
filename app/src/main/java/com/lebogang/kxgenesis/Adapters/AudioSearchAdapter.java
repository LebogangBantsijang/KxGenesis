package com.lebogang.kxgenesis.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;
import com.lebogang.kxgenesis.Utils.TimeUnitConvert;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.ItemSongBinding;

import java.util.ArrayList;
import java.util.List;

public class AudioSearchAdapter extends RecyclerView.Adapter<AudioSearchAdapter.Holder> implements Filterable {
    private GeneralItemClick clickInterface;
    private List<AudioMediaItem> items = new ArrayList<>();
    private List<AudioMediaItem> searchItems = new ArrayList<>();
    private Context context;
    private long currentItemId = -1;
    private int color = -1;

    public AudioSearchAdapter(GeneralItemClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    public AudioSearchAdapter() {
    }

    public void setItems(List<AudioMediaItem> items){
        this.items = items;
    }

    public void update(AudioMediaItem mediaItem){
        int pos = 0;
        for (AudioMediaItem item: items){
            if (item.getMediaId() == mediaItem.getMediaId()){
                pos = items.indexOf(item);
                break;
            }
        }
        items.remove(pos);
        items.add(pos, mediaItem);
        notifyItemChanged(pos);
    }

    public void remove(AudioMediaItem mediaItem){
        int pos = 0;
        for (AudioMediaItem item: items){
            if (item.getMediaId() == mediaItem.getMediaId()){
                pos = items.indexOf(item);
                break;
            }
        }
        items.remove(pos);
        notifyDataSetChanged();
    }

    public void setCurrentID(long id, int color){
        this.currentItemId = id;
        this.color = color;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemSongBinding binding = ItemSongBinding.inflate(inflater,parent, false);
        return new AudioSearchAdapter.Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AudioMediaItem audioItem = searchItems.get(position);
        holder.binding.titleTextView.setText(audioItem.getTitle());
        holder.binding.subtitleTextView.setText(audioItem.getSubTitle());
        holder.binding.durationTextView.setText(TimeUnitConvert.toMinutes(audioItem.getDuration()));
        Glide.with(context).load(audioItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .into(holder.binding.imageView);
        highlight(holder, audioItem,context);
    }

    protected void highlight(AudioSearchAdapter.Holder holder, AudioMediaItem audioItem, Context context){
        if (audioItem.getMediaId() == currentItemId){
            holder.binding.lottieAnimationView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Drawable drawable = context.getDrawable(R.drawable.shaper_rectangle_round_corners_4dp_with_cp);
                drawable.setTint(color);
                holder.binding.getRoot().setBackground(drawable);
                holder.binding.titleTextView.setTextAppearance(R.style.LightTextColor);
            }
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.binding.getRoot().setBackgroundResource(R.drawable.shaper_rectangle_round_corners_4dp_with_bg);
                holder.binding.titleTextView.setTextAppearance(R.style.textColor);
            }
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
                for (AudioMediaItem item:items){
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
        public ItemSongBinding binding;

        public Holder(@NonNull View itemView, ItemSongBinding binding) {
            super(itemView);
            this.binding = binding;
            binding.imageButton.setImageResource(R.drawable.ic_forward);
            binding.getRoot().setOnClickListener(v->{
                clickInterface.onClick(searchItems.get(getAdapterPosition()));
            });
        }
    }
}
