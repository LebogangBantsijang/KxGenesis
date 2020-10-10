package com.lebogang.kxgenesis.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.ItemPagerItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PlayerPagerAdapter extends RecyclerView.Adapter<PlayerPagerAdapter.Holder>{

    private Context context;
    private List<AudioMediaItem> items = new ArrayList<>();
    private GeneralItemClick clickInterface;
    private long currentItemId = -1;

    public PlayerPagerAdapter(GeneralItemClick clickInterface) {
        this.clickInterface = clickInterface;
    }

    public PlayerPagerAdapter() {
    }

    public void setItems(List<AudioMediaItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }


    public AudioMediaItem getItem(int pos){
        return items.get(pos);
    }

    public int getItemPosition(AudioMediaItem mediaItem){
        int pos = items.indexOf(mediaItem);
        if (pos > -1)
            return pos;
        return 0;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemPagerItemBinding binding = ItemPagerItemBinding.inflate(inflater, parent, false);
        return new Holder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AudioMediaItem audioItem = items.get(position);
        holder.binding.counterTextView.setText(""+(1+position)+ "/" + items.size());
        holder.binding.titleTextView.setText(audioItem.getTitle());
        Glide.with(context).load(audioItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .into(holder.binding.imageView);
    }

    public void setCurrentItemId(long id, int color) {
        this.currentItemId = id;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        private ItemPagerItemBinding binding;
        private GestureDetector gestureDetector;

        @SuppressLint("ClickableViewAccessibility")
        public Holder(@NonNull View itemView, ItemPagerItemBinding binding) {
            super(itemView);
            this.binding = binding;
            gestureDetector = new GestureDetector(context, getSimpleOnGestureListener());
            binding.imageView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
        }

        private GestureDetector.SimpleOnGestureListener getSimpleOnGestureListener(){
            return new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    clickInterface.onClick(items.get(getAdapterPosition()));
                    return true;
                }
            };
        }
    }
}
