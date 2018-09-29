package com.example.cuma.tinder.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cuma.tinder.R;
import com.example.cuma.tinder.Class.Satinal;
import com.example.cuma.tinder.Adapter.SatinalAdapter;

import java.util.ArrayList;
import java.util.List;

public class CanFragment extends Fragment {

    private List<Satinal> satinalList;
    private RecyclerView recyclerView;
    private SatinalAdapter satinalAdapter;
    public static CanFragment newInstance(){
        return new CanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_can, container, false);

        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view_can);

        satinalList=new ArrayList<Satinal>();


        satinalAdapter=new SatinalAdapter(satinalList);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(satinalAdapter);

        satinalList.add(new Satinal("8 tane can",R.drawable.kalpfazla,"24 tl"));
        satinalList.add(new Satinal("100 tane can",R.drawable.kalpkamyon,"56 tl"));
        satinalList.add(new Satinal("250 tane can",R.drawable.kalpbira,"75 tl"));
        satinalList.add(new Satinal("750 tane can",R.drawable.kalpkamyonlar,"99 tl"));



        satinalAdapter.notifyDataSetChanged();

        return view;
    }




}
