package com.example.cuma.tinder.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuma.tinder.Class.Kupa;
import com.example.cuma.tinder.R;

import java.util.ArrayList;
import java.util.List;

public class KupaAdapter extends RecyclerView.Adapter<KupaAdapter.MyViewHolder> {

    public List<Kupa> kupaList=new ArrayList<>();

    public KupaAdapter(List<Kupa> kupaList) {
        this.kupaList = kupaList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.kupa_card_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Holder tutacak demek
        holder.kupa_ad.setText(kupaList.get(position).getKupa_ad());
        holder.kupa_puan.setText(kupaList.get(position).getKupa_puan());
        holder.kupa_image.setImageResource(kupaList.get(position).getKupa_image());
    }

    @Override
    public int getItemCount() {
        return kupaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView kupa_ad,kupa_puan;
        private ImageView kupa_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            kupa_ad=(TextView)itemView.findViewById(R.id.kupa_ad);
            kupa_puan=(TextView)itemView.findViewById(R.id.kupa_puan);
            kupa_image=(ImageView)itemView.findViewById(R.id.kupa_image);

        }
    }
}
