package com.lebogang.kxgenesis.Adapters;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TotalArtistAlbumsAdapter extends RecyclerView.Adapter<TotalArtistAlbumsAdapter.Holder>{
    private LinkedHashMap<String, Audio> hashMap = new LinkedHashMap<>();
    private List<Audio> list = new ArrayList<>();
    private Context context;
    private OnClickInterface clickInterface;

    public void setList(List<Audio> list) {
        for (Audio audio: list){
            if (!hashMap.containsKey(audio.getAlbumTitle()))
                hashMap.put(audio.getAlbumTitle(), audio);
        }
        this.list = new ArrayList<>(hashMap.values());
        notifyDataSetChanged();
    }

    public void setClickInterface(OnClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_total_artist,parent
                , false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Audio audio = list.get(position);
        holder.title.setText(audio.getAlbumTitle());
        Glide.with(context).load(audio.getAlbumArtUri())
                .error(R.drawable.ic_music_record)
                .downsample(DownsampleStrategy.AT_MOST)
                .dontAnimate()
                .skipMemoryCache(true)
                .into(holder.imageView)
                .clearOnDetach()
                .waitForLayout();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView imageView;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(v->{
                clickInterface.onClick(list.get(getAdapterPosition()));
            });
        }
    }
}
