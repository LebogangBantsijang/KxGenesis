package com.lebogang.kxgenesis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lebogang.audiofilemanager.Models.Artist;
import com.lebogang.kxgenesis.R;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.Holder>{
    private Context context;
    private boolean isLayoutGrid = true;
    private List<Artist> list = new ArrayList<>();
    private OnClickInterface clickInterface;

    public ArtistAdapter(OnClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    public void setLayoutGrid(boolean layoutGrid) {
        isLayoutGrid = layoutGrid;
    }

    public void setList(List<Artist> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (isLayoutGrid){
            View view = LayoutInflater.from(context).inflate(R.layout.item_artist_multi_column,parent, false);
            return new Holder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist_single_column,parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Artist artist = list.get(position);
        holder.title.setText(artist.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        private TextView title;
        public Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            itemView.setOnClickListener(v->{
                clickInterface.onClick(list.get(getAdapterPosition()));
            });
        }
    }
}
