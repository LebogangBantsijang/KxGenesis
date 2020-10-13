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
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.Utils.TimeUnitConvert;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.ItemSongBinding;

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.Holder>{
    private OnClickInterface clickInterface;
    private OnClickOptionsInterface clickOptionsInterface;
    private List<Audio> list = new ArrayList<>();
    private Context context;
    private long currentItemId = -1;
    private int color = -1;

    public AudioAdapter(OnClickInterface clickInterface, OnClickOptionsInterface clickOptionsInterface) {
        this.clickInterface = clickInterface;
        this.clickOptionsInterface = clickOptionsInterface;
    }

    public void setList(List<Audio> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<Audio> getList(){
        return new ArrayList<>(list);
    }

    public int getItemPosition(Audio audio){
        for (Audio item: list){
            if (item.getId() == audio.getId()){
                return list.indexOf(item);
            }
        }
        return 0;
    }

    public Audio getItemAtPosition(int position){
        return list.get(position);
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
        Audio audioItem = list.get(position);
        holder.binding.titleTextView.setText(audioItem.getTitle());
        holder.binding.subtitleTextView.setText(audioItem.getArtistTitle() + " - " + audioItem.getAlbumTitle());
        holder.binding.durationTextView.setText(TimeUnitConvert.toMinutes(audioItem.getAudioDuration()));
        Glide.with(context).load(audioItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .into(holder.binding.imageView).waitForLayout();
        highlight(holder, audioItem,context);
    }

    protected void highlight(AudioAdapter.Holder holder, Audio audioItem, Context context){
        if (audioItem.getId() == currentItemId){
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
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        public ItemSongBinding binding;

        public Holder(@NonNull View itemView, ItemSongBinding binding) {
            super(itemView);
            this.binding = binding;
            binding.imageButton.setOnClickListener(v->{
                clickOptionsInterface.onOptionsClick(list.get(getAdapterPosition()));
            });
            binding.getRoot().setOnClickListener(v->{
                clickInterface.onClick(list.get(getAdapterPosition()));
            });
        }
    }
}
