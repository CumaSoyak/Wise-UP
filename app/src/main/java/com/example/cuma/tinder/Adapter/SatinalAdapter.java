package com.example.cuma.tinder.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuma.tinder.R;
import com.example.cuma.tinder.Class.Satinal;

import java.util.ArrayList;
import java.util.List;

public class SatinalAdapter extends RecyclerView.Adapter<SatinalAdapter.MyViewHolder> {
    private List<Satinal> satinalList = new ArrayList<>();

    public SatinalAdapter(List<Satinal> satinalList){
        this.satinalList=satinalList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.satinal_card_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // Satinal satinal=satinalList.get(position);
        holder.title.setText(satinalList.get(position).getTitle());
        holder.image.setImageResource(satinalList.get(position).getImage());
        holder.fiyat.setText(satinalList.get(position).getFiyat());
    }

    @Override
    public int getItemCount() {
        return satinalList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,fiyat;
        public ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.para_text);
            image=(ImageView) itemView.findViewById(R.id.para_image);
            fiyat=(TextView)itemView.findViewById(R.id.para_buton);

        }
    }
}
