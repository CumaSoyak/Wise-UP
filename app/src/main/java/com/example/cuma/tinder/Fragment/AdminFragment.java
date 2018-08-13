package com.example.cuma.tinder.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cuma.tinder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment {


    public AdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_admin, container, false);
        return view;
    }

}
