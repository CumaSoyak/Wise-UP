package com.example.cuma.tinder.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cuma.tinder.Adapter.KupaAdapter;
import com.example.cuma.tinder.Class.Kupa;
import com.example.cuma.tinder.R;

import java.util.ArrayList;
import java.util.List;

public class KupaFragment extends Fragment {
    private List<Kupa> kupaList;
    private RecyclerView recyclerView;
    private KupaAdapter kupaAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_kupa, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view_kupa); //Reclerview tanımladık
        kupaList=new ArrayList<Kupa>();         //Arraylist oluşturduk
        kupaAdapter=new KupaAdapter(kupaList);      //Adaptare listeyi atadık

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(kupaAdapter);   //reclerviewe et ettik kupa adapteri

        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));
        kupaList.add(new Kupa("Cuma","4533",R.drawable.spor));

        kupaAdapter.notifyDataSetChanged();


        return view;
    }
}
