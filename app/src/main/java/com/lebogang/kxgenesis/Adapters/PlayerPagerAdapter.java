package com.lebogang.kxgenesis.Adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Utils.AudioIndicator;
import com.lebogang.kxgenesis.Utils.ExtractColor;

import java.util.ArrayList;
import java.util.List;

public class PlayerPagerAdapter extends RecyclerView.Adapter<PlayerPagerAdapter.Holder>{
    private Context context;
    private List<Audio> list = new ArrayList<>();
    private OnClickInterface clickInterface;
    private long currentPlayingId = -1;

    public void setList(List<Audio> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setClickInterface(OnClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    public void setCurrentPlayingId(long currentPlayingId) {
        this.currentPlayingId = currentPlayingId;
        notifyDataSetChanged();
    }

    public int getIndex(long id){
        for (Audio audio:list){
            if (audio.getId() == id)
                return list.indexOf(audio);
        }
        return 0;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Audio audio = list.get(position);
        Glide.with(context).load(audio.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .skipMemoryCache(true)
                .into(holder.imageView)
                .clearOnDetach()
                .waitForLayout();
        String string = "" + (1+position) + " of " + list.size();
        holder.countTextView.setText(string);
        if (audio.getId() == currentPlayingId)
            holder.titleTextView.setText("");
        else
            holder.titleTextView.setText(audio.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView countTextView, titleTextView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            countTextView = itemView.findViewById(R.id.counterTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView.setOnClickListener(v->{
                Audio audio = list.get(getAdapterPosition());
                if (audio.getId() != currentPlayingId)
                    clickInterface.onClick(audio);
            });
        }
    }
}
