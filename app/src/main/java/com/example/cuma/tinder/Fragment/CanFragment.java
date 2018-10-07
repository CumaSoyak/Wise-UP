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



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_can, container, false);


        return view;
    }




}
