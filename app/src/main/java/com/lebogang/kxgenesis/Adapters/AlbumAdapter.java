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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.kxgenesis.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder>{
    private Context context;
    private boolean isLayoutGrid = true;
    private List<Album> list = new ArrayList<>();
    private OnClickInterface clickInterface;

    public AlbumAdapter(OnClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    public void setLayoutGrid(boolean layoutGrid) {
        isLayoutGrid = layoutGrid;
    }

    public void setList(List<Album> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isLayoutGrid){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_multiple_column,parent, false);
            return new Holder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_album_single_column,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Album album = list.get(position);
        holder.title.setText(album.getTitle());
        holder.subtitle.setText(album.getArtist());
        Glide.with(context).load(album.getAlbumArtUri())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_music_record_light)
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
        private TextView title, subtitle;
        private ImageView imageView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            subtitle = itemView.findViewById(R.id.subtitleTextView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(v->{
                clickInterface.onClick(list.get(getAdapterPosition()));
            });
        }
    }
}
