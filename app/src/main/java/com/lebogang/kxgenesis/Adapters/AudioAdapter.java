package com.lebogang.kxgenesis.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;
import com.lebogang.audiofilemanager.Models.MediaItem;
import com.lebogang.kxgenesis.Utils.TimeUnitConvert;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.ItemSongBinding;

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.Holder>{
    private GeneralItemClick clickInterface;
    private List<AudioMediaItem> items = new ArrayList<>();
    private Context context;
    private long currentItemId = -1;
    private int color = -1;

    public AudioAdapter(GeneralItemClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    public AudioAdapter() {
    }

    public void setItems(List<AudioMediaItem> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public ArrayList<MediaItem> getItems(){
        return new ArrayList<>(items);
    }

    public int getItemPosition(MediaItem mediaItem){
        int x = 0;
        for (AudioMediaItem item: items){
            if (item.getMediaId() == mediaItem.getMediaId()){
                x = items.indexOf(item);
                break;
            }
        }
        return x;
    }

    public AudioMediaItem getItemAtPosition(int position){
        return items.get(position);
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
        return new AudioAdapter.Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AudioMediaItem audioItem = items.get(position);
        holder.binding.titleTextView.setText(audioItem.getTitle());
        holder.binding.subtitleTextView.setText(audioItem.getSubTitle());
        holder.binding.durationTextView.setText(TimeUnitConvert.toMinutes(audioItem.getDuration()));
        Glide.with(context).load(audioItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .into(holder.binding.imageView);
        highlight(holder, audioItem,context);
    }

    protected void highlight(AudioAdapter.Holder holder, AudioMediaItem audioItem, Context context){
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
        return items.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public ItemSongBinding binding;

        public Holder(@NonNull View itemView, ItemSongBinding binding) {
            super(itemView);
            this.binding = binding;
            binding.imageButton.setOnClickListener(v->{
                clickInterface.onOptionsClick(items.get(getAdapterPosition()));
            });
            binding.getRoot().setOnClickListener(v->{
                clickInterface.onClick(items.get(getAdapterPosition()));
            });
        }
    }
}
